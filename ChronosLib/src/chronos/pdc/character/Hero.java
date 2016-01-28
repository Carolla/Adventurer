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
  private Race _race = null;

  /** Input Data for Hero */
  private String _name = null;
  /** Male or female Person */
  private Gender _gender = null;;
  /** Person's hair color, used in building their physical appearance */
  protected String _hairColor = null;
  /** Name of the race to convert to a Race component */
  private String _racename;
  /** Name of the klass to convert to a Klass component */
  private String _klassname;
  /** What we see when we look at the Person */
  private String _description;
  /** The hungers state of the Hero as he/she burns calories and eats */
  private String _hunger;

  /** Hero game attributes */
  private int _level = 1;
  private int _XP;
  private int _HP;
  private int _AP;
  private int _AC;
  private int _AC_Magic;

  // Non-lethal combat stats
  int OVERBEAR = 0;
  int GRAPPLE = 1;
  int PUMMEL = 2;
  int BASH = 3;
  int[] _apMods;

  // Speed is measured in block movement per turn */
  int _speed;

  /** Contains the indexes into the prime traits */
  public static final int NBR_TRAITS = 6;
  /** Each Person has six prime traits, adjusted by gender, race, and klass */
  private TraitList _traits = null;

  // STR mods
  private int _toHitStr = 0; // to hit with melee weapon
  private int _damage = 0; // damage bonus
  private int _wtAllow = 0; // load allowed

  // Spells in the Cleric or Wizard's spell book
  List<String> _spellBook = new ArrayList<String>();

  // INT mods
  // Everyone knows Common, and perhaps a race language
  Set<String> _knownLangs = new TreeSet<String>();
  int _maxLangs; // some can learn new languages, up to this number
  // Wizard only
  private int _MSPs = 0;
  private int _MSPsPerLevel = 0;
  private int _percentToKnow = 0;
  private int _spellsKnown = 0;
  private String _literacy;

  // WIS mods
  int _magicAttackMod = 0; // adjusted by racial magic resistence too
  // Clerical mods
  private int _CSPsPerLevel = 0;
  private int _CSPs = 0;
  private int _turnUndead = 0;

  // CON mods
  int _HPMod = 0;
  int _racialPoisonResist = 0;

  // DEX mods
  int _toHitDex;
  int _ACMod;

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
    _racename = raceName;
    _klassname = klassName;

    // 3. REARRANGE THE PRIME TRAIT FOR THE KLASS
    _klass = Klass.createKlass(_klassname);
    _traits = _klass.adjustTraitsForKlass(_traits);

    // 4b. REARRANGE THE PRIME TRAITS FOR THE GENDER
    _gender.adjustTraitsForGender(_traits);

    // 4a. REARRANGE THE PRIME TRAITS FOR THE RACE
    _race = Race.createRace(_racename, _gender);
    _traits = _race.adjustTraitsForRace(_traits);

    // 5. ENSURE ALL ADJUSTMENTS REMAIN WITH RACIAL LIMITS
    _traits = _race.verifyRaceLimits(_traits);

    // 6. ASSIGN THE STRENGTH MODIFIERS: To Hit Mod, Damage Mod, and Wt Allowance
    int[] strMods = calcStrengthMods(_traits);
    _toHitStr = strMods[0];
    _damage = strMods[1];
    _wtAllow = strMods[2];

    // 7a. ASSIGN THE INTELLIGENCE MODIFIERS: Known Languages, Max Languages, Literacy Skill
    int intel = _traits.getTrait(PrimeTraits.INT); // for typing convenience
    _knownLangs.add("Common");
    _knownLangs.add(_race.getRacialLanguage());
    // displayList("Known Languages: \t", _knownLangs);
    _maxLangs = intel / 2 - 3;
    // System.out.println("Hero can learn an additional " + _maxLangs + " languages.");
    _literacy = getLiteracy(intel);
    // displayList("Skills: \t", _skills);

    // 7b. FOR WIZARDS ONLY: PercentToKnow, MSPs/Level, MSPS, Spells Known
    if (_klassname.equalsIgnoreCase("Wizard")) {
      int[] wizMods = calcWizardMods(intel);
      _MSPsPerLevel = wizMods[0];
      _percentToKnow = wizMods[1];
      _MSPs = _MSPsPerLevel; // for first level
      displayList(String.format("Spell book contains: %s spells: ", _spellsKnown), _spellBook);
    }

    // 8a. ASSIGN THE WISDOM MODIFIERS: Magic Attack Mod
    int wisdom = _traits.getTrait(PrimeTraits.WIS); // for typing convenience
    _magicAttackMod = findInRange(wisdom);
    
    // 8b. FOR CLERICS ONLY: CSPs/Level, CSPS, Turn Undead
    if (_klassname.equalsIgnoreCase("Cleric")) {
      _CSPsPerLevel = wisdom / 2;
      _CSPs = _CSPsPerLevel; // for level 1
      _turnUndead = wisdom;
      // displayClericMods();
    }

    // 9. ASSIGN THE CONSTITUTION MODIFIERS: HP Mod, Racial Magic Resist
    int constitution = _traits.getTrait(PrimeTraits.CON); // for typing convenience
    _HPMod = findInRange(constitution);
    
    // Update Magic Attack Mod by racial resist
    if ((_racename.equalsIgnoreCase("Dwarf")) ||
        (_racename.equalsIgnoreCase("Gnome")) ||
        (_racename.equalsIgnoreCase("Hobbit"))) {
      _racialPoisonResist = (int) Math.round((float) constitution / 3.5);
      _magicAttackMod += _racialPoisonResist;
    }

    // 10. ASSIGN THE DEXTERITY MODIFIERS: To Hit Mod (missile), AC Mod
    int dex = _traits.getTrait(PrimeTraits.DEX); // for typing convenience
    _toHitDex = findInRange(dex);
    _ACMod = findInRange(dex);

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

    // 13. ROLL FOR KLASS-SPECIFIC HIT POINTS
    _HP = _klass.rollHP(_HPMod);

    // 14. SET THE NON-LETHAL COMBAT STATS: OVERBEARING, GRAPPLING, PUMMELING, AND SHIELD BASH
    _AP = _traits.getTrait(PrimeTraits.STR) + dex;
    _apMods = new int[4];
    _apMods = calcAPMods(_apMods);

    // 15. CALCULATE SPEED (BLOCK MOVEMENT)
    _speed = calcSpeed(_AP);

    // 16. SET INITIAL ARMOR CLASS
    _AC = 10 + _ACMod;
    _AC_Magic = 0; // no magica adjustments initially

    // 17. ROLL FOR KLASS-SPECIFIC STARTING GOLD
    _gold = _klass.rollGold();
    _goldBanked = 0.0;

    // 20. ADD RANDOM OCCUPATION AND OCCUPATIONAL SKILLS
    _occ = Occupation.getRandomOccupation();

    // 21. ASSIGN SPELLS TO CLERICS (WIZARDS ALREADY WERE ASSIgned 'READ MAGIC')
    _spellBook = _klass.addKlassSpells(_spellBook);
    _spellsKnown = _spellBook.size();

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

  public Gender getGender()
  {
    return _gender;
  }

  public String getHairColor()
  {
    return _hairColor;
  }

  public String getKlassName()
  {
    return _klassname;
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

  /** Remove the description after the delimeter to return only the name */
  public String getOccupationName()
  {
    return _occ.getName();
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
    return _spellBook;
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
    map.put(PersonKeys.RACENAME, _racename);
    // Rogue is the user pseudonym for the Thief class
    _klassname = (_klassname.equalsIgnoreCase("Thief")) ? _klassname = "Rogue" : _klassname;
    map.put(PersonKeys.KLASSNAME, _klassname);

    // Row 3: Level, Current HP, Max HP, AC, (AC with Magic adj)
    map.put(PersonKeys.LEVEL, "" + _level);
    map.put(PersonKeys.HP, "" + _HP);
    map.put(PersonKeys.HP_MAX, "" + _HP);
    map.put(PersonKeys.AC, "" + _AC);
    map.put(PersonKeys.AC_MAGIC, "" + _AC_Magic);

    // Row 4: XP, Speed, Gold/Silver (gp/sp), Gold Banked
    map.put(PersonKeys.XP, "" + _XP);
    map.put(PersonKeys.SPEED, "" + _speed);
    map.put(PersonKeys.GOLD, "" + _gold);
    map.put(PersonKeys.SILVER, "" + _silver);
    map.put(PersonKeys.GOLD_BANKED, "" + _goldBanked);

    // Row 5: Occupation, Description
    map.put(PersonKeys.OCCUPATION, getOccupationName());
    map.put(PersonKeys.DESCRIPTION, _description);

    // Row 6: STR and STR mods: ToHit, StrDamage, Wt Allowance, Load Carried
    map.put(PersonKeys.STR, "" + _traits.getTrait(PrimeTraits.STR));
    map.put(PersonKeys.TO_HIT_MELEE, "" + _toHitStr);
    map.put(PersonKeys.DAMAGE, "" + _damage);
    map.put(PersonKeys.WT_ALLOW, "" + _wtAllow);
    map.put(PersonKeys.LOAD, "" + "134"); // for testing

    // Row 7: INT and INT mods: percent to know spell, current MSP, max MSP, MSPs/Level,
    // spells known (in book), and max languages
    map.put(PersonKeys.INT, "" + _traits.getTrait(PrimeTraits.INT));
    map.put(PersonKeys.TO_KNOW, "" + _percentToKnow);
    map.put(PersonKeys.CURRENT_MSP, "" + _MSPs);
    map.put(PersonKeys.MAX_MSP, "" + _MSPs);
    map.put(PersonKeys.MSP_PER_LEVEL, "" + _MSPsPerLevel);
    map.put(PersonKeys.SPELLS_KNOWN, "" + _spellsKnown);
    map.put(PersonKeys.MAX_LANGS, "" + _maxLangs);
    map.put(PersonKeys.LITERACY, _literacy);

    // Row 8: WIS and WIS mods: Magic Attack Mod, Current CSP, Max CSPs, CSPs/Level, Turn Undead
    map.put(PersonKeys.WIS, "" + _traits.getTrait(PrimeTraits.WIS));
    map.put(PersonKeys.MAM, "" + _magicAttackMod);
    map.put(PersonKeys.CURRENT_CSP, "" + _CSPs);
    map.put(PersonKeys.MAX_CSP, "" + _CSPs);
    map.put(PersonKeys.CSP_PER_LEVEL, "" + _CSPsPerLevel);
    map.put(PersonKeys.TURN_UNDEAD, "" + _turnUndead);

    // Row 9: CON and HP Mod
    map.put(PersonKeys.CON, "" + _traits.getTrait(PrimeTraits.CON));
    map.put(PersonKeys.HP_MOD, "" + _HPMod);
    map.put(PersonKeys.RMR, "" + _racialPoisonResist);

    // Row 10: DEX and DEX mods: ToHit Missile, AC Mod
    map.put(PersonKeys.DEX, "" + _traits.getTrait(PrimeTraits.DEX));
    map.put(PersonKeys.TO_HIT_MISSLE, "" + _toHitDex);
    map.put(PersonKeys.AC_MOD, "" + _ACMod);

    // Row 11: CHR, then Weight and Height of Hero
    map.put(PersonKeys.CHR, "" + _traits.getTrait(PrimeTraits.CHR));
    map.put(PersonKeys.WEIGHT, "" + _weight);
    map.put(PersonKeys.HEIGHT, "" + _height);
    map.put(PersonKeys.HUNGER, _hunger);

    // Row 12: AP and non-lethal combat values
    map.put(PersonKeys.AP, "" + _AP);
    map.put(PersonKeys.OVERBEARING, "" + _apMods[OVERBEAR]);
    map.put(PersonKeys.PUMMELING, "" + _apMods[PUMMEL]);
    map.put(PersonKeys.GRAPPLING, "" + _apMods[GRAPPLE]);
    map.put(PersonKeys.SHIELD_BASH, "" + _apMods[BASH]);

    // Row 13: Maximum languages
    map.put(PersonKeys.MAX_LANGS, "" + _maxLangs);

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

  /** Calculate the non-lethal combat stats: overbearing, grappling, pummeling, and shield bash */
  private int[] calcAPMods(int[] mods)
  {
    mods[OVERBEAR] = _AP + (_weight / 25);
    mods[GRAPPLE] = _AP + _damage;
    mods[PUMMEL] = _AP + _damage + _toHitDex;
    mods[BASH] = 0;
    return mods;
  }

  /** Lookup the speed of the character, factoring in height */
  private int calcSpeed(int ap)
  {
    int speed = 0;
    if (ap <= 15) {
      speed = 2;
    } else if ((ap >= 16) && (ap <= 23)) {
      speed = 3;
    } else if ((ap >= 24) && (ap <= 32)) {
      speed = 4;
    } else if (ap > 32) {
      speed = 5;
    }
    // Adjust for height
    if (_height <= 48) {
      speed -= 1;
    }
    if (_height >= 74) {
      speed += 1;
    }
    return speed;
  }

  // Set the strength modifiers: ToHit, Damage, and Wt Allowace
  private int[] calcStrengthMods(TraitList traits)
  {
    // STR values 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21
    final int[] toHitTbl = {-3, -2, -2, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 3};
    final int[] dmgTbl = {-3, -3, -2, -2, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5};

    // STR values 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
    final int[] wtTbl = {80, 120, 160, 200, 280, 360, 440, 520, 600, 700, 800, 900, 1000,
        // STR values 16, 17, 18, 19, 20, 21
        1200, 1500, 1800, 2100, 2300, 2600};

    // Internal check
    if ((toHitTbl.length != dmgTbl.length) && (toHitTbl.length != wtTbl.length)) {
      System.err.println("calcCtrengthMods(): invalid internal tables");
      System.exit(-1);
    }
    int[] mods = new int[3];
    int ndx = traits.getTrait(PrimeTraits.STR) - 3; // read from the table 3 places to the left
    mods[0] = toHitTbl[ndx];
    mods[1] = dmgTbl[ndx];
    mods[2] = wtTbl[ndx];
    return mods;
  }

  private int[] calcWizardMods(int intell)
  {
    int[] mod = new int[2];
    mod[0] = intell / 2 - 3; // MPSs per level
    mod[1] = intell * 5; // percent to know new spells
    return mod;
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

  /**
   * Display a list with a header message
   * 
   * @param msg the message to display as label
   * @param _spellBook2 display each element of this list
   */
  private void displayList(String msg, List<String> _spellBook2)
  {
    System.out.println("\n" + msg);
    for (int k = 0; k < _spellBook2.size(); k++) {
      System.out.println("\t" + _spellBook2.get(k) + ", ");
    }
    System.out.println();
  }

  // ====================================================
  // Private helper methods
  // ====================================================

  // Find a number in one of three ranges: low, medium, high
  private int findInRange(int value)
  {
    final int HI_GATE = 14;
    final int LO_GATE = 9;
    if (value > HI_GATE)
      return (value - HI_GATE);
    if (value < LO_GATE)
      return (value - LO_GATE);
    return 0;
  }

  /** Get the literacy based on intelligence; Spell casters are always literate */
  private String getLiteracy(int intel)
  {
    final String ILLITERATE = "ILLITERATE: Cannot read or write";
    final String LITERATE = "LITERATE: Can read and write";
    final String PART_LITERATE = "PARTIALLY LITERATE: Can read but cannot write";
    String lit = "";
    if ((_klassname.equalsIgnoreCase("Cleric")) || (_klassname.equalsIgnoreCase("Wizard"))) {
      return LITERATE;
    }
    if (intel <= 10)
      return ILLITERATE;
    if (intel >= 12)
      return LITERATE;
    if (intel == 11)
      return PART_LITERATE;
    return lit;
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
        article + bodyType + " " + getGender().toString().toLowerCase() + " with " + hairType;

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
    result = prime * result + ((_klassname == null) ? 0 : _klassname.hashCode());
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    result = prime * result + ((_occ == null) ? 0 : _occ.hashCode());
    result = prime * result + ((_racename == null) ? 0 : _racename.hashCode());
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
    if (_klassname == null) {
      if (other._klassname != null)
        return false;
    } else if (!_klassname.equals(other._klassname))
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
    if (_racename == null) {
      if (other._racename != null)
        return false;
    } else if (!_racename.equals(other._racename))
      return false;
    return true;
  }

} // end of Hero class
