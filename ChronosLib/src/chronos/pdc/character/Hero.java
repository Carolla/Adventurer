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
  /** Person's hair color, used in building their physical appearance */
  protected String _hairColor;
  /** What we see when we look at the Person */
  private String _description;
  /** The hungers state of the Hero as he/she burns calories and eats */
  private String _hunger;

  /** Hero game attributes */
  private int _level = 1;
  private int _XP;
  private int _AC;
  private int _AC_Magic;


  /** Each Person has six prime traits, adjusted by gender, race, and klass */
  private TraitList _traits = null;

  // Spells in the Cleric or Wizard's spell book
  List<String> _spellBook = new ArrayList<String>();

  // INT mods
  // Everyone knows Common, and perhaps a race language
  Set<String> _knownLangs = new TreeSet<String>();
  
  // Misc: height and weight of the Hero, affected by race and gender
  int _height;
  int _weight;

  // Gold pieces and silver pieces in hand.
  int _gold;
  int _silver;
  // Gold banked. The decimal represents silver pieces
  double _goldBanked;
  
  // Inventory object containing map with ItemCategory key
  private Inventory _inven;

  private Occupation _occ;


  // ====================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ====================================================
  /**
   * Create the Person from the basic non-klass attributes.
   * 
   * @param name of the Person, default filename under which it is saved
   * @param gender male or female Person
   * @param hairColor selected hair color for this Person
   * @param raceName the concrete subclass object of Race, e.g., Human or Hobbit
   * @param klassName the concrete subclass object of Klass, e.g. Fighter or Thief

   */
  public Hero(String name, String gender, String hairColor, String raceName, String klassName)
  {
    // Guards
    if ((name == null) || (gender == null) || (hairColor == null)) {
      throw new NullPointerException("Missing input data passed to Hero constructor");
    }
    if ((raceName == null) || (klassName == null)) {
      throw new NullPointerException("Missing Race or Klass passed to Hero constructor");
    }

    // Setup internal data
    _traits = new TraitList();

    // 1. INPUT DATA
    _name = name;
    _gender = new Gender(gender);
    _hairColor = hairColor;

    // 3. REARRANGE THE PRIME TRAIT FOR THE KLASS
    _klass = Klass.createKlass(klassName, _traits);
    _traits = _klass.adjustTraitsForKlass(_traits);

    // 4b. REARRANGE THE PRIME TRAITS FOR THE GENDER
    _gender.adjustTraitsForGender(_traits);

    // 4a. REARRANGE THE PRIME TRAITS FOR THE RACE
    _race = Race.createRace(raceName, _gender);
    _traits = _race.adjustTraitsForRace(_traits);

    // 5. ENSURE ALL ADJUSTMENTS REMAIN WITH RACIAL LIMITS
    _traits = _race.verifyRaceLimits(_traits);


    // 7a. ASSIGN THE INTELLIGENCE MODIFIERS: Known Languages, Max Languages, Literacy Skill
    _knownLangs.add("Common");
    _knownLangs.add(_race.getRacialLanguage());

    // 11a. ASSIGN HERO'S HEIGHT AND WEIGHT
    _weight = _race.calcWeight();
    _height = _race.calcHeight();

    // 11b. GET THE HERO'S PHYSICAL DESCRIPTION FROM THIS BODY-TYPE
    _description = initDescription();
    
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
    _spellBook = _klass.addKlassSpells(_spellBook);

    // 22. Assign initial inventory
    _inven = new Inventory();
    _inven = _inven.assignBasicInventory(_inven);
    _inven = _klass.addKlassItems(_inven);
  } // end of Hero constructor


  public Hero(EnumMap<HeroInput, String> inputMap)
  {
    this(inputMap.get(HeroInput.NAME),
        inputMap.get(HeroInput.GENDER),
        inputMap.get(HeroInput.HAIR),
        inputMap.get(HeroInput.RACE),
        inputMap.get(HeroInput.KLASS));
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

  /**
   * Load all the Hero attriutes into a single output map, keyed by the {@code PersonKeys} enum
   * 
   * @param map the keyed map of Hero data attributes
   * @return the EnumMap with attribute data
   */
  public EnumMap<PersonKeys, String> loadAttributes(EnumMap<PersonKeys, String> map)
  {
    // Now load the attributes in display order (values in parens are derived)
    // Row 1: Name
    map.put(PersonKeys.NAME, _name);

    // Row 2: Gender, Race and Klass
    map.put(PersonKeys.GENDER, _gender.toString());

    // Row 3: Level, Current HP, Max HP, AC, (AC with Magic adj)
    map.put(PersonKeys.LEVEL, "" + _level);
    map.put(PersonKeys.AC, "" + _AC);
    map.put(PersonKeys.AC_MAGIC, "" + _AC_Magic);

    // Row 4: XP, Speed, Gold/Silver (gp/sp), Gold Banked
    map.put(PersonKeys.XP, "" + _XP);
    _traits.loadTraitKeys(map);
    map.put(PersonKeys.GOLD, "" + _gold);
    map.put(PersonKeys.SILVER, "" + _silver);
    map.put(PersonKeys.GOLD_BANKED, "" + _goldBanked);

    // Row 5: Occupation, Description
    map.put(PersonKeys.OCCUPATION, _occ.getName());
    map.put(PersonKeys.DESCRIPTION, _description);


    // Row 7: INT and INT mods: percent to know spell, current MSP, max MSP, MSPs/Level,
    // spells known (in book), and max languages
    _klass.loadKlassTraits(map);


    // Row 11: CHR, then Weight and Height of Hero
    map.put(PersonKeys.WEIGHT, "" + _weight);
    map.put(PersonKeys.HEIGHT, "" + _height);
    map.put(PersonKeys.HUNGER, _hunger);


    // Row 14: All known languages as single string
    StringBuilder sb = new StringBuilder();
    for (String lang : _knownLangs) {
      sb.append(lang);
      sb.append(", ");
    }
    String langList = new String(sb);
    map.put(PersonKeys.LANGUAGES, langList);

    return map;
  }


  /**
   * Check if the first letter of a string is a vowel so the proper article (A or An) can be placed
   * in front of it.
   * 
   * @param target the string to check if the first letter is a vowel or not
   * @return true if a the target starts with a vowel; else false
   */
  private boolean checkFirstVowel(String target)
  {
    final Character[] c = {'a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'};
    boolean retflag = false;

    for (int k = 0; k < c.length; k++) {
      if (target.charAt(0) == c[k]) {
        retflag = true;
      }
    }
    return retflag;
  }

  // ====================================================
  // Private helper methods
  // ====================================================

  /**
   * Build the physical appearance of the Person, without regard to what they are wearing or
   * anything that can drastically change. The description depends on height, weight, race, klass,
   * hair color, gender and charisma. A special racial note is appended to all races except Human.
   * <P>
   * Template for the attributes in description: \n\t
   * "A [height_descriptor] and [weight_descriptor]" + "[gender] with [color] hair" +
   * "and [racial note]". [She | He] is [CHR reflection]". \n\t
   * 
   * @return a string that describes the Person's body-type (a Race function).
   */
  private String initDescription()
  {
    // Infer body-type from racial attributes of height, weight, and charisma
    int chr = _traits.getTrait(PrimeTraits.CHR);
    String bodyType = _race.initBodyType(chr, _height, _weight);
    // Start the description with a vowel-sensitive article
    String article = (checkFirstVowel(bodyType) == true) ? "An " : "A ";

    // Process baldness.
    String hairType = (_hairColor.equalsIgnoreCase("bald")) ? "a bald head" : _hairColor + " hair";
    String desc1 =
        article + bodyType + " " + _gender.toString().toLowerCase() + " with " + hairType;

    // Get race descriptor for suffix.
    String desc2 = _race.getRaceDescriptor();

    // Get Charisma description
    String chrDesc = _race.initCharismaDescriptor(chr);
    String desc3 = _gender.pronoun() + " is " + chrDesc + ".";

    String desc = desc1 + " and " + desc2 + ". " + desc3;
    return desc;
  }

  @Override
  public boolean equals(IRegistryElement target)
  {
    return equals((Object) target);
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
    result = prime * result + ((_hairColor == null) ? 0 : _hairColor.hashCode());
    result = prime * result + ((_klass == null) ? 0 : _klass.hashCode());
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    result = prime * result + ((_occ == null) ? 0 : _occ.hashCode());
    result = prime * result + ((_race == null) ? 0 : _race.hashCode());
    return result;
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
    if (_hairColor == null) {
      if (other._hairColor != null)
        return false;
    } else if (!_hairColor.equals(other._hairColor))
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

} // end of Hero class
