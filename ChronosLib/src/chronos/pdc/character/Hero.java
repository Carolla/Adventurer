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

import mylib.dmc.IRegistryElement;
import chronos.civ.PersonKeys;
import chronos.pdc.Occupation;
import chronos.pdc.character.TraitList.PrimeTraits;
import chronos.pdc.race.Race;



/**
 * @author Alan Cline
 * @version Sept 4 2015 // rewrite per revised generation rules \n\t
 */
public class Hero implements IRegistryElement
{

  /** Input data fields to create a new hero */
  public enum HeroInput {
    NAME, GENDER, HAIR, RACE, KLASS
  };

  /* INTERNAL OBJECT COMPONENTS */
  /** One of the four canonical Hero klasses: Fighter, Cleric, Wizard, or Thief */
  private Klass _klass;
  /** The Race object for this Person (Input), and contains the Hunger objects */
  private Race _race;

  /** Input Data for Hero */
  private String _name;
  /** Male or female Person */
  private Gender _gender;
  /** What we see when we look at the Person */
  private Description _description;
  /** The hungers state of the Hero as he/she burns calories and eats */
  private String _hunger;

  /** Hero game attributes */
  private int _level = 1;
  private int _XP;
  private int _AC;
  private int _AC_Magic;
  private int _HP;

  /** Each Person has six prime traits, adjusted by gender, race, and klass */
  private TraitList _traits = null;
  Set<String> _knownLangs = new TreeSet<String>();

  // Gold pieces and silver pieces in hand.
  int _gold;
  int _silver;
  double _goldBanked;
  
  private Inventory _inven;
  private Occupation _occ;
  
  // ====================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ====================================================
  
  public Hero() {}; //db4o attempt
  
  /**
   * Create the Person from the basic non-klass attributes.
   * 
   * @param name of the Person, default filename under which it is saved
   * @param gender male or female Person
   * @param hairColor selected hair color for this Person
   * @param raceName the concrete subclass object of Race, e.g., Human or Hobbit
   * @param klassName the concrete subclass object of Klass, e.g. Fighter or Thief
   * 
   */
  public Hero(String name, String gender, String hairColor, String raceName, String klassName)
  {
    // Guards
    if (name == null || gender == null || hairColor == null || raceName == null
        || klassName == null) {
      throw new NullPointerException("Missing input data passed to Hero constructor");
    }

    // Setup internal data
    _traits = new TraitList();

    // 1. INPUT DATA
    _name = name;
    _gender = new Gender(gender);
    _gender.adjustTraitsForGender(_traits);

    // 4a. REARRANGE THE PRIME TRAITS FOR THE RACE
    _race = Race.createRace(raceName, _gender);
    _traits = _race.adjustTraitsForRace(_traits);

    // 3. REARRANGE THE PRIME TRAIT FOR THE KLASS
    _klass = Klass.createKlass(klassName, _traits);
    _klass.calcClassMods();
    _klass.adjustTraitsForKlass(_traits);

    // 7a. ASSIGN THE INTELLIGENCE MODIFIERS: Known Languages, Max Languages, Literacy Skill
    _knownLangs.add("Common");
    _knownLangs.add(_race.getRacialLanguage());

    // 11a. ASSIGN HERO'S HEIGHT AND WEIGHT
    _HP = _klass.rollHP();

    // 11b. GET THE HERO'S PHYSICAL DESCRIPTION FROM THIS BODY-TYPE
    _description = new Description(_traits.getTrait(PrimeTraits.CHR), _race.getRaceDescriptor(),
        hairColor, _gender, _race.calcHeight(), _race.calcWeight());

    // 11c. SET THE HERO'S INITIAL HUNGER STATE
    _hunger = "Full";

    // 12. SET THE INITIAL LEVEL AND EXPERIENCE POINTS
    _level = 1;
    _XP = 0;

    // 16. SET INITIAL ARMOR CLASS
    _AC = 10 + _traits.getACMod();
    _AC_Magic = 0; // no magica adjustments initially

    // 17. ROLL FOR KLASS-SPECIFIC STARTING GOLD
    _gold = _klass.rollGold();
    _goldBanked = 0.0;

    // 20. ADD RANDOM OCCUPATION AND OCCUPATIONAL SKILLS
    _occ = Occupation.getRandomOccupation();

    // 21. ASSIGN SPELLS TO CLERICS (WIZARDS ALREADY WERE ASSIgned 'READ MAGIC')
    _klass.addKlassSpells();

    // 22. Assign initial inventory
    _inven = new Inventory();
    _inven.assignBasicInventory();
    _klass.addKlassItems(_inven);
  } // end of Hero constructor

//  public Hero(EnumMap<HeroInput, String> inputMap)
//  {
//    this(inputMap.get(HeroInput.NAME),
//        inputMap.get(HeroInput.GENDER),
//        inputMap.get(HeroInput.HAIR),
//        inputMap.get(HeroInput.RACE),
//        inputMap.get(HeroInput.KLASS));
//  }

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

  /**
   * Load all the Hero attributes into a single output map, keyed by the {@code PersonKeys} enum
   * 
   * @param map the keyed map of Hero data attributes
   * @return the EnumMap with attribute data
   */
  public Map<PersonKeys, String> loadAttributes()
  {
    Map<PersonKeys, String> map =
        new EnumMap<PersonKeys, String>(PersonKeys.class);
    
    // Now load the attributes in display order (values in parens are derived)
    // Row 1: Name 
    map.put(PersonKeys.NAME, _name);

    // Row 2: Gender, Race and Klass
    map.put(PersonKeys.GENDER, _gender.toString());
    map.put(PersonKeys.RACENAME, _race.getName());
    map.put(PersonKeys.KLASSNAME, _klass.className());

    // Row 3: Level, Current HP, Max HP, AC, (AC with Magic adj)
    map.put(PersonKeys.LEVEL, "" + _level);
    map.put(PersonKeys.AC, "" + _AC);
    map.put(PersonKeys.AC_MAGIC, "" + _AC_Magic);
    map.put(PersonKeys.HP, "" + _HP);
    map.put(PersonKeys.HP_MAX, "" + _HP);

    // Row 4: XP, Speed, Gold/Silver (gp/sp), Gold Banked
    map.put(PersonKeys.XP, "" + _XP);
    map.put(PersonKeys.GOLD, "" + _gold);
    map.put(PersonKeys.SILVER, "" + _silver);
    map.put(PersonKeys.GOLD_BANKED, "" + _goldBanked);

    // Row 5: Occupation, Description
    map.put(PersonKeys.OCCUPATION, _occ.getName());

    // Row 11: CHR, then Weight and Height of Hero
    map.put(PersonKeys.HUNGER, _hunger);


    // Row 14: All known languages as single string
    StringBuilder sb = new StringBuilder();
    for (String lang : _knownLangs) {
      sb.append(lang);
      sb.append(", ");
    }
    String langList = new String(sb);
    map.put(PersonKeys.LANGUAGES, langList);

    _description.loadKeys(map);
    _traits.loadTraitKeys(map);
    _race.loadRaceKeys(map);
    _klass.loadKlassKeys(map);

    return map;
  }

  public String toNamePlate()
  {
    return _name + ": " + _gender.toString() + " " + _race.getName()+ " " + _klass.className();
  }

  @Override
  public String toString()
  {
    return toNamePlate() + ". " + _traits;
  }
  // ====================================================
  // Private helper methods
  // ====================================================

  @Override
  public boolean equals(IRegistryElement target)
  {
    return equals((Object) target);
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Hero other = (Hero) obj;
    if (_description == null) {
      if (other._description != null)
        return false;
    } else if (!_description.equals(other._description))
      return false;
    if (_gender == null) {
      if (other._gender != null)
        return false;
    } else if (!_gender.equals(other._gender))
      return false;
    if (_klass == null) {
      if (other._klass != null)
        return false;
    } else if (!_klass.equals(other._klass))
      return false;
    if (_name == null) {
      if (other._name != null)
        return false;
    } else if (!_name.equals(other._name))
      return false;
    if (_occ == null) {
      if (other._occ != null)
        return false;
    } else if (!_occ.equals(other._occ))
      return false;
    if (_race == null) {
      if (other._race != null)
        return false;
    } else if (!_race.equals(other._race))
      return false;
    return true;
  }

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
    result = prime * result + ((_description == null) ? 0 : _description.hashCode());
    result = prime * result + ((_gender == null) ? 0 : _gender.hashCode());
    result = prime * result + ((_klass == null) ? 0 : _klass.hashCode());
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    result = prime * result + ((_occ == null) ? 0 : _occ.hashCode());
    result = prime * result + ((_race == null) ? 0 : _race.hashCode());
    return result;
  }

} // end of Hero class
