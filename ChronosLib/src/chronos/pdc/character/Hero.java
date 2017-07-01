/**
 * Hero.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.character;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import chronos.civ.PersonKeys;
import chronos.pdc.Occupation;
import chronos.pdc.Skill;
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
  private TraitList _traits;

  /** Hero initial constants */
  private final int INITIAL_AC = 10;
  /** All Heroes start as Peasant */
  private final String INITIAL_KLASS = "Peasant";

  /** Hero fields */
  private String _name;
  /** Armor Class depends on what the person is wearing */
  private int _AC;
  /** Each Person knows at least one language, and can usually learn others */
  ArrayList<String> _knownLangs;
  /** Skills can originate from occupation, races (not Human), and klasses (not Peasant) */
  private ArrayList<Skill> _skills;
//  /** Single string of all known languages */
//  private String _langString;
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

    // INDEPENDENT FIELDS
    _name = name;
    _goldBanked = 0.0;

    // Add random occupation and occupational skills
    _occ = Occupation.getRandomOccupation();
    // Assign initial inventory
    _inven = new Inventory();

    // DEPENDENT ON RACE, GENDER, AND TRAITS
    _race = Race.createRace(raceName, new Gender(gender), hairColor);
    // Fill out other race-dependent traits and attributes, 
    _traits = _race.buildRace();
    // Assign Race language
    _knownLangs = _race.getLanguages();
    // Build single string of all known languages
//    _langString = buildLangString();
    // Initial Armor Class from adjusted DEX
    _AC = INITIAL_AC + _traits.calcMod(PrimeTraits.DEX);
    // Set AP and non-lethal mods
    _traits.calcAPMods(_race.getWeight());
    // Calc speed, adjusted by height and AP
    _traits.calcSpeed(_race.getHeight());

    // Set Klass attributes
    _klass = Klass.createKlass(INITIAL_KLASS, _traits);
    _gold = _klass.getStartingGold();

    // Add occupational skills (Humans and Peasants have no special skills)
    _skills = (ArrayList<Skill>) _occ.getSkills();

  } // end of Hero constructor


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
    map.put(PersonKeys.LANGUAGES, buildLangString());
    int opGold = (int) _gold;
    int opSilver = (int) ((_gold - opGold) * 10);
    map.put(PersonKeys.GOLD, Integer.toString(opGold));
    map.put(PersonKeys.SILVER, Integer.toString(opSilver));
    map.put(PersonKeys.GOLD_BANKED, Double.toString(_goldBanked));

    // Load the occupation, occupation description, and skill list. Each skill in the list
    // contains a skill name and skill action (description)
    map.put(PersonKeys.OCCUPATION, _occ.getName());
    map.put(PersonKeys.OCC_DESCRIPTOR, _occ.getDescription());
    map.put(PersonKeys.SKILL, _skills.toString());

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
    // return _name + ": " + _race.getGender().toString() + " " + _race.getName() + " "
    // + _klass.className();
    return _name + ": " + _race.getGender().toString() + " " + _race.getName() + " "
        + _occ.getName();
  }

  // ====================================================
  // PRIVATE METHODS
  // ====================================================

  /** Compress all languages into a single comma-delimited string */
  private String buildLangString()
  {
    StringBuilder sb = new StringBuilder();
    for (String lang : _knownLangs) {
      if (!lang.isEmpty()) {
        sb.append(lang);
        sb.append(", ");
      }
    }
    // Remove extraneous last comma and space
    sb.deleteCharAt(sb.length() - 2);
    String langList = new String(sb).trim();
    return langList;
  }


  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_klass == null) ? 0 : _klass.hashCode());
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    result = prime * result + ((_occ == null) ? 0 : _occ.hashCode());
    result = prime * result + ((_race == null) ? 0 : _race.hashCode());
    return result;
  }


  @Override
  public String getKey()
  {
    return _name;
  }

} // end of Hero class
