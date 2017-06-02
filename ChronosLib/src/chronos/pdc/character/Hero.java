/**
 * Hero.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.character;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import chronos.civ.PersonKeys;
import chronos.pdc.Occupation;
import chronos.pdc.character.TraitList.PrimeTraits;
import chronos.pdc.race.Race;
import mylib.dmc.IRegistryElement;


/**
 * Create a Hero, of either gender and any race, but only for a Peasant klass. Fighters, Clerics,
 * Rogues, and Wizards are created after the Hero joins one of those Guilds.
 * 
 * @author Alan Cline
 * @version Sept 4 2015 // rewrite per revised generation rules <br>
 *          May 22, 2017 // refined so all new Heroes are Peasant klass <br>
 *          June 2 2017 // refactored for clearer organization <br>
 */
public class Hero implements IRegistryElement
{
  /** Input data fields to create a new hero */
  public enum HeroInput {
    NAME, GENDER, HAIR, RACE, KLASS
  };

  /* INTERNAL OBJECT COMPONENTS */
  /** One of the five canonical Hero klasses: Peasant, Fighter, Cleric, Wizard, or Thief */
  private Klass _klass;
  /** The Race object for this Person (Input), and contains the Hunger objects */
  private Race _race;
  /** The equipment associated with the Hero */
  private Inventory _inven;
  /** The Hero's occupation, until they join a Guild */
  private Occupation _occ;
  /** Each Person has six prime traits, adjusted by gender, race, and klass */
  private TraitList _traits = null;

  /** Hero initial constants */
  private final int INITIAL_AC = 10;
  /** All Heroes start as Peasant */
  private final String INITIAL_KLASS = "Peasant";

  /** Hero fields */
  private String _name;
  /** Armor Class depends on what the person is wearing */
  private int _AC;
  /** Each Person knows at least one language, and can usually learn others */
  Set<String> _knownLangs = new TreeSet<String>();
  /** Single string of all known languages */
  private String _langString;
  // Special cases
  private double _gold;
  private double _goldBanked;


  // ====================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ====================================================

  // public Hero() {}; // db4o attempt

  /**
   * Create the Person from the basic non-klass attributes. <br>
   * <i>Implementation:</i> Independent attributes are created first, then those that depend on the
   * newly created attributes are used to create the secondary attributes, etc. In this way, the
   * dependencies are pushed downward and minimized.
   * 
   * @param name of the Person, default filename under which it is saved
   * @param gender male or female Person
   * @param hairColor selected hair color for this Person
   * @param raceName the concrete subclass object of Race, e.g., Human or Hobbit
   * 
   */
  public Hero(String name, String gender, String hairColor, String raceName)
  {
    // Guards
    if (name == null || gender == null || hairColor == null || raceName == null) {
      throw new NullPointerException("Missing input data passed to Hero constructor");
    }

    // DEPENDENT ON ONLY INPUTS, OR ON NOTHING
    _name = name;
    _knownLangs.add("Common");
    _goldBanked = 0.0;
    
    // Add random occupation and occupational skills
    _occ = Occupation.getRandomOccupation();
    // Assign initial inventory
    _inven = new Inventory();
    // Setup internal data
    _traits = new TraitList();

    // DEPENDENT ON RACE AND TRAITS
    _race = Race.createRace(raceName, new Gender(gender), hairColor);
    // Fill out other race-dependent attributes
    _traits = _race.buildRace(_traits);
    // Assign Race language
    _knownLangs.add(_race.getRacialLanguage());
    // Initial Armor Class from adjusted DEX
    _AC = INITIAL_AC + _traits.calcMod(PrimeTraits.DEX);
    // Set AP and non-lethal mods
    _traits.calcAPMods(_race.getWeight());
    // Calc speed, adjusted by height and AP
    _traits.calcSpeed(_race.getHeight());
    // Build single string of all known languages
    _langString = buildLangString();

    // Set Klass attributes
     _klass = Klass.createKlass(INITIAL_KLASS, _traits);
     _gold = _klass.getStartingGold();
     
     // Now add the inventory
     _inven = new Inventory();

  } // end of Hero constructor


  // ====================================================
  // PRIVATE METHODS
  // ====================================================

  /** Compress all languages into a single comma-delimted string */
  private String buildLangString()
  {
    StringBuilder sb = new StringBuilder();
    for (String lang : _knownLangs) {
      if (!lang.isEmpty()) {
        sb.append(lang);
        sb.append(",");
      }
    }
    // Remove extraneous last comma
    sb.deleteCharAt(sb.length() - 1);
    String langList = new String(sb);
    return langList;
  }

  /**
   * Load all the Hero attributes into a single output map, keyed by the {@code PersonKeys} enum.
   * Each Race, Trait, and Klass component loads their own keys
   * 
   * @param map the keyed map of Hero data attributes
   * @return the EnumMap with attribute data
   */
  public Map<PersonKeys, String> loadAttributes()
  {
    Map<PersonKeys, String> map =
        new EnumMap<PersonKeys, String>(PersonKeys.class);

    // Load the Hero attributes
    map.put(PersonKeys.NAME, _name);
    map.put(PersonKeys.AC, "" + _AC);
    map.put(PersonKeys.OCCUPATION, _occ.getName());
    map.put(PersonKeys.LANGUAGES, _langString);
    map.put(PersonKeys.GOLD, Double.toString(_gold));
    map.put(PersonKeys.GOLD_BANKED, Double.toString(_goldBanked));

    // Load the Trait attributes
    _traits.loadTraitKeys(map);

    // Load the Race attributes
    _race.loadRaceKeys(map);

    // Load the Klass attributes
    _klass.loadKlassKeys(map);

    // Load inventory (in ounces)
    int load = _inven.calcInventoryWeight();
    map.put(PersonKeys.LOAD, Integer.toString(load));
    map.put(PersonKeys.INVENTORY, _inven.toString());
    
    
    return map;
  }



  public boolean canUseMagic()
  {
    return _klass.canUseMagic();
  }

  public List<String> getKlassSkills()
  {
    return _klass.getSkills();
  }

  public Inventory getInventory()
  {
    return _inven;
  }

  public String getName()
  {
    return _name;
  }

  public void setName(String newName)
  {
    _name = newName;
  }

  public List<String> getRaceSkills()
  {
    return _race.getSkills();
  }

  public List<String> getOcpSkills()
  {
    return _occ.getSkillNames();
  }

  public List<String> getSpellBook()
  {
    return _klass.getSpells();
  }


  public String toNamePlate()
  {
    return _name + ": " + _race.getGender().toString() + " " + _race.getName() + " "
        + _klass.className();
  }

//  @Override
//  public String toString()
//  {
//    return toNamePlate() + ". " + _traits;
//  }

//  @Override
//  public boolean equals(Object obj)
//  {
//    if (this == obj)
//      return true;
//    if (obj == null)
//      return false;
//    if (getClass() != obj.getClass())
//      return false;
//    Hero other = (Hero) obj;
//    if (_description == null) {
//      if (other._description != null)
//        return false;
//    } else if (!_description.equals(other._description))
//      return false;
//    if (_gender == null) {
//      if (other._gender != null)
//        return false;
//    } else if (!_gender.equals(other._gender))
//      return false;
//    if (_klass == null) {
//      if (other._klass != null)
//        return false;
//    } else if (!_klass.equals(other._klass))
//      return false;
//    if (_name == null) {
//      if (other._name != null)
//        return false;
//    } else if (!_name.equals(other._name))
//      return false;
//    if (_occ == null) {
//      if (other._occ != null)
//        return false;
//    } else if (!_occ.equals(other._occ))
//      return false;
//    if (_race == null) {
//      if (other._race != null)
//        return false;
//    } else if (!_race.equals(other._race))
//      return false;
//    return true;
//  }

  @Override
  public String getKey()
  {
    return _name;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
//    result = prime * result + ((_description == null) ? 0 : _description.hashCode());
//    result = prime * result + ((_gender == null) ? 0 : _gender.hashCode());
    result = prime * result + ((_klass == null) ? 0 : _klass.hashCode());
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    result = prime * result + ((_occ == null) ? 0 : _occ.hashCode());
    result = prime * result + ((_race == null) ? 0 : _race.hashCode());
    return result;
  }

} // end of Hero class
