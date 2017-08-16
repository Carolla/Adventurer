/**
 * Hero.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.character;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
 *          Aug 6, 2017 // revised for serialization and to work with HeroRegistry <br>
 */
public class Hero implements IRegistryElement, Serializable
{
  // Required for serialization
  static final long serialVersionUID = 42L;

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
  // Special cases
  private double _gold;
  private double _goldBanked;


  // ====================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ====================================================

  /** Required for serialization */
  public Hero()
  {};


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
    _traits = _race.buildRace(gender, hairColor);
    // Assign Race language
    _knownLangs = _race.getLanguages();
    // Build single string of all known languages
    // _langString = buildLangString();
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


  public boolean canUseMagic()
  {
    return _klass.canUseMagic();
  }

  /**
   * Compares two Heroes, who are equal when name, gender, race, and klass are equal. Required for
   * serialization.
   * 
   * @param obj some other possible HeroRegistry to compare with
   */
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass()) {
      return false;
    }
    // Heroes are equal if name, gender, race, and klass are the same
    Hero other = (Hero) obj;
    if (!_name.equals(other._name)) {
      return false;
    }
    if (!_race.equals(other._race)) {
      return false;
    }
    if (!_klass.equals(other._klass)) {
      return false;
    }
    if (!_race.getGender().equals(other._race.getGender())) {
      return false;
    }
    return true;
  }


  /** Return the integer armor class */
  public int getAC()
  {
    return _AC;
  }


  /** Return the string version of the Hero's gender */
  public String getGender()
  {
    return _race.getGender();
  }


  /**
   * The key distinguishes equal Heros from others, and the name is sufficient
   */
  @Override
  public String getKey()
  {
    return _name;
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

  public List<String> getOcpSkills()
  {
    return _occ.getSkillNames();
  }

  public List<String> getRaceSkills()
  {
    return _race.getSkills();
  }


  public List<String> getSpellBook()
  {
    return _klass.getSpells();
  }


  // ====================================================
  // PRIVATE METHODS
  // ====================================================

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



  /*
   * Serialize the Hero to a file derived from its name.
   * 
   * @param pathname filename to save the Hero to
   */
  public void save(String pathname)
  {
    // Write out Hero
    FileOutputStream fileOut = null;
    ObjectOutputStream oos = null;
    try {
      fileOut = new FileOutputStream(pathname);
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
    try {
      oos = new ObjectOutputStream(fileOut);
      oos.writeObject(this);
      oos.flush();
      oos.close();
    } catch (IOException ex) {
      System.err.println("Could not write out the Hero");
      System.err.println("\t" + ex.getMessage());
    }
  }


  public void setName(String newName)
  {
    _name = newName;
  }


  public String toNamePlate()
  {
    return _name + ": " + _race.getGender().toString() + " " + _race.getName() + " "
        + _occ.getName();
  }


  /*
   * Create an object output stream file into which to Serialize this Hero. Note: serialization
   * methods will throw a {@code NotActiveException runtime error} if all component objects are not
   * serializable. The Hero's name will be used as the filename.
   */
  public void writeObject()
  {
    FileOutputStream fileOut = null;
    ObjectOutputStream oos = null;
    try {
      fileOut = new FileOutputStream(_name);
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
    try {
      oos = new ObjectOutputStream(fileOut);
      oos.defaultWriteObject();
      oos.flush();
      oos.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
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



} // end of Hero class
