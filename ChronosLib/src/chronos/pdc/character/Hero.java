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
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import mylib.dmc.IRegistryElement;
import mylib.pdc.MetaDie;
import chronos.civ.PersonKeys;
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
  
  // Statics and transients are not saved with the serialized Person object
  /** Recommended serialization constant */
  static final long serialVersionUID = 1007L;

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

  /** Indices into the Hero's prime traits */
  public enum PrimeTraits {
    STR, INT, WIS, DEX, CON, CHR
  };

  /** Contains the indexes into the prime traits */
  public static final int NBR_TRAITS = 6;
  /** Each Person has six prime traits, adjusted by gender, race, and klass */
  private int[] _traits = null;

  // STR mods
  private int _toHitStr = 0; // to hit with melee weapon
  private int _damage = 0; // damage bonus
  private int _wtAllow = 0; // load allowed

  // Spells in the Cleric or Wizard's spell book
  List<String> _spellBook = new ArrayList<String>();

  // INT mods
  // Everyone knows Common, and perhaps a race language
  List<String> _knownLangs = new ArrayList<String>();
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

  // Race skills
  private ArrayList<String> _raceSkills = new ArrayList<String>();
  // Klass-based skills, mostly for Thief
  private ArrayList<String> _klassSkills = new ArrayList<String>();
  // Inventory object containing map with ItemCategory key
  private Inventory _inven; 

  // Keys to all occupational kits
  public enum KitNdx {
    ALCHEMIST, LEATHER, METAL, SEWING, WOOD, THIEVES
  };

  // Name (value) | wt (gpw) ...(8 gp = 1 lb)
  final String[] kits = {
      "Alchemists Kit (100 gp) | 40", // 5 lb
      "Leatherworking Kit (50 gp) | 64", // 8 lb
      "Metalsmith Kit (50 gp) | 80", // 10 lb
      "Sewing Kit (30 gp) | 16", // 2 lb
      "Woodworking Kit (50 gp) | 64", // 8 lb
      "Thieves Kit (50 gp) | 8" // 1 lb
  };

  // Table to hold occupational skills
  private String _occupation = "";
  private ArrayList<String> _ocpSkills = new ArrayList<String>();

  // Various occupations (31) for random selection
  private String[] _ocpTable = {
      "Academic", "Acrobat", "Alchemist", "Apothecary", "Armorer", "Banker", "Bowyer",
      "Carpenter", "Farmer", "Fisher", "Forester", "Freighter", "Gambler", "Hunter",
      "Husbandman", "Innkeeper", "Jeweler", "Leatherworker", "Painter", "Mason",
      "Miner", "Navigator", "Sailor", "Shipwright", "Tailor", "Trader", "Trapper",
      "Weaponsmith", "Weaver", "Woodworker", "Drifter"};

  // Occupational skills and descriptions for defined occupations
  private String[] _ocpSkillTable = {
      "Animal Empathy: Communicate emotionally with animals",
      "Appraise Jewelry: Estimate selling value of gems and jewelry",
      "Appraise Tapestries: Estimate selling value of tapestries",
      "Arcane Knowledge: +1 INT to recognize things, substances, and potions  "
          + "\n\t --Identify substances (2gp, 1hr)  "
          + "\n\t --Identify potions (2gp 1hr)  "
          + "\n\t --Make acid: half-pint, d4 dmg or dissolve metal (15gp, 1 hr in town only) "
          + "\n\t --Make weak explosive (2d6 dmg (5gp, 1hr) "
          + "\n\t --Make medium explosive (3d8 dmg (20gp, 4hr in town only) ",
      "Balance: +1 DEX for balancing tasks and saves",
      "Bluff: +2 CHR if the lie is only a matter of luck that the listener believes you",
      "Bowmaking: in the field if proper materials available:"
          + "\n\t --Make/repair short bow (-1 to hit) (20 gp, 3 days) "
          + "\n\t --Make arrows (-1 damage), need 1 bird for feathers "
          + "(1 gp per 3d4+2 per day) ",
      "Cargo Transport: knows tack, harness, and transport equipment"
          + "\n\t --Repair wagons",
      "Cavern Lore: +1 WIS to guide party through caverns without getting lost"
          + "\n\t --Avoid natural cavern hazards"
          + "\n\t --Identify most kinds of rock ores, +1 INT on rarer ores"
          + "\n\t --Use picks and shovels as +1, +1 weapons",
      "Climb Walls: as a Level 1 Thief (%)",
      "Concentration: +1 Save vs INT to avoid distraction (and spell interruption if spell caster)",
      "Diplomacy: +1 CHR for all political negotiating",
      "Disable Device Skill: same as Remove Traps as Level 1 Thief (%)"
          + "\n\t --Undo or jam wooden devices or traps at +1",
      "Escape Artist: +1 DEX to slip from manacles, ropes, or through tight spaces",
      "Fast Swim: Gains +1 BM when moving in water or underwater (normal water penalty = BM/2) "
          + "\n\t --Gets +4 Save when falling into water due to diving ",
      "Financial Brokering: +1 CHR when negotiating money deals "
          + "\n\t --No fee banking "
          + "\n\t --Gets 10% discount on all transactions in town ",
      "Find Secrets in Woodwork: +10% chance to find secret doors in wall panels, cabinets, etc.",
      "Find Secrets in Stonework: +10% chance to find openings in stone construction, "
          + "e.g. cavern walls, stone floors, fireplaces ",
      "Find/Set Traps: for simple traps like snares and deadweights, as a Level 1 Thief (%)",
      "Gather Information: +2 CHR to hear rumor when in an inn or similarly crowded place"
          + "\n\t -- Gets +2 CHR to find contact information for a key person",
      "General Knowledge: +1 INT on any general question on specific topic ",
      "Hide in Shadows: as a Level 1 Thief (%)",
      "Hunting: 20% chance of finding wild game",
      "Husbandry: 10% chance of catching live animals"
          + "\n\t --From vet skills, ca heal d3 HP human dmg or Slow Poison. Needs herbs",
      "Identify Plants: +1 INT on rarer items",
      "Intimidate: +1 CHR to get info from a prison or backdown a bully",
      "Intuit Outdoor Direction: +1 WIS to know direction of travel when outside",
      "Intuit Underground Direction: +1 WIS to know direction when underground",
      "Jump: +2 AP for leaping chasms or reaching lower tree branches",
      "Leatherworking: Make/repair leather armor (10gp, 3 days)"
          + "\n\t --Make/repair boots or gloves (5gp, 1 day)",
      "Listening: as a Level 1 Thief (%)",
      "Luck: Gets +1 on all Saves involving luck and risk taking"
          + "\n\t --Gets +2 on all throws involving gaming luck",
      "Make Raft: Make a sailing raft. Needs hand axe (3 days)",
      "Make Weapons: Make small metal shield (12gp, 4hr)"
          + "\n\t --Make/repair small melee weapons (all at -1 to Hit, -1 dmg):"
          + "\n\t . . dagger (5gp, 2hr); battle axe (5gp, 2hr, need hand axe); spear (5gp, 2hr); "
          + "short sword (no scabbard) (10gp, 1 day)"
          + "\n\t --Make/repair small missile weapons (all at -1 to Hit, -1 dmg): "
          + "throwing axe (5gp, 2hr, need hand axe); "
          + "\n\t . . light xbow bolts (10sp, 15 min); heavy bolts (1gp, 30 min); "
          + "sling bullet or dart (1 gp per 3d6 bullets/4hrs)",
      "Move Silently: as a Level 1 Thief (%)",
      "Natural Knowledge: +1 WIS for biological or chemical question "
          + "\n\t --Identify substance (3gp, 1hr) "
          + "\n\t --Identify potion (3gp, 1hr) "
          + "\n\t --Detect poison in bottle or by symptoms of person (10gp, 1 hr) "
          + "\n\t --Detect potency and kind of poison after detection (20gp, 1 hr) "
          + "\n\t --Make weak medicinal potions (d4 healing) (10gp, 1 hr) "
          + "\n\t --Make medium medicinal potions (2d6 healing) (20gp, 4 hr in town only) "
          + "\n\t --Make weak poison (L1 poison)( (5gp, 1hr) "
          + "\n\t --Make medium poison (L2 poison)(40gp, 4hr in town only) ",
      "Negotiations: +1 CHR when negotiating money deals",
      "Netmaking: Make/repair 10'x10' net that can provide 10d4 fish per day for food. "
          + " Needs 50' rope (2 days) ",
      "Open Locks: as a Level 1 Thief (%)",
      "Painting: Paint buildings, mix paint. (yep, that's it).",
      "Pick Pockets: as a Level 1 Thief (%)",
      "Predict Weather: Predict next day weather at +2 WIS",
      "Read Lips: Understand about 1 minute of speaker's speech if within 30' "
          + "and knows the speaker's language",
      "Repair Armor: in the field if proper materials available:"
          + "\n\t --Make shield, small metal (10gp, 1 day) or small wooden (2 gp, 4 hr) "
          + "\n\t --Convert lantern from open (hooded) to bulls-eye lantern (10gp, 4hr) "
          + "\n\t --Make caltrop from 4 spikes (1gp, 1hr) ",
      "Sense Motive: +1 WIS to determine if person is lying or bluffing",
      "Sewing: Make/repair belt (1gp, 1hr), boots (5gp, 1 day), cloak (1gp, 1hr), hat (1gp, 1hr)",
      "Spot Details: +2 WIS to notice details such as obscure items in a dim room, "
          + "\n\t or centipedes in a pile of trash",
      "Train Animals: Train animals or work teams",
      "Trapping: Catch animals alive (20%)",
      "Tumble: land softer when falling (reduce dmg by d3) "
          + "\n\t--Dive tumble over opponents at +2 AC ",
      "Water Lore: +1 WIS to guide party through water areas and avoid natural hazards",
      "Wilderness Lore: Guide party through badlands and avoid natural hazards "
          + "\n\t --Navigate outdoor course without getting lost "
          + "\n\t --Survive off the land",
      "Woodworking: Make/repair wooden items, e.g. repair xbows (not bows),"
          + "\n\t --Add secret compartments to chests"
  };


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
    _traits = new int[NBR_TRAITS];

    // 1. INPUT DATA
    _name = name;
    _gender = new Gender(gender);
    _hairColor = hairColor;
    _racename = raceName;
    _klassname = klassName;
    auditOutHero();

    // 2. SET PRIME TRAITS for Peasant (base is same for all Klasses and Races)
    MetaDie md = new MetaDie();
    for (int k = 0; k < NBR_TRAITS; k++) {
      _traits[k] = md.rollTrait();
    }
    displayTraits("Raw traits: ", _traits);

    // 3. REARRANGE THE PRIME TRAIT FOR THE KLASS
    // Create the Klass object
    _klass = Klass.createKlass(_klassname);

    _traits = _klass.adjustTraitsForKlass(_traits);
    // displayTraits(_klassname + "-adjusted Traits: ", _traits);

    // 4b. REARRANGE THE PRIME TRAIT FOR THE GENDER
    if (_gender.isFemale()) {
      _traits = adjustTraitsForGender(_traits);
    }
    
    // 4a. REARRANGE THE PRIME TRAIT FOR THE RACE
    // Create the Race object
    _race = Race.createRace(_racename, _gender);

    _traits = _race.adjustTraitsForRace(_traits);
    // displayTraits(_racename + "-adjusted Traits: ", _traits);


    // 5. ENSURE ALL ADJUSTMENTS REMAIN WITH RACIAL LIMITS
    _traits = _race.verifyRaceLimits(_traits);
//    displayTraits("Race-verified final Traits: ", _traits);

    // 6. ASSIGN THE STRENGTH MODIFIERS: To Hit Mod, Damage Mod, and Wt Allowance
    int[] strMods = calcStrengthMods(_traits);
    _toHitStr = strMods[0];
    _damage = strMods[1];
    _wtAllow = strMods[2];
    // displayStrMods(strMods);

    // 7a. ASSIGN THE INTELLIGENCE MODIFIERS: Known Languages, Max Languages, Literacy Skill
    int intel = _traits[PrimeTraits.INT.ordinal()]; // for typing convenience
    _knownLangs = addLanguages(_knownLangs);
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
      // displayWizardMods();
      displayList(String.format("Spell book contains: %s spells: ", _spellsKnown), _spellBook);
    }

    // 8a. ASSIGN THE WISDOM MODIFIERS: Magic Attack Mod
    int wisdom = _traits[PrimeTraits.WIS.ordinal()]; // for typing convenience
    _magicAttackMod = findInRange(wisdom);
    // 8b. FOR CLERICS ONLY: CSPs/Level, CSPS, Turn Undead
    if (_klassname.equalsIgnoreCase("Cleric")) {
      _CSPsPerLevel = wisdom / 2;
      _CSPs = _CSPsPerLevel; // for level 1
      _turnUndead = wisdom;
      // displayClericMods();
    }

    // 9. ASSIGN THE CONSTITUTION MODIFIERS: HP Mod, Racial Magic Resist
    int constitution = _traits[PrimeTraits.CON.ordinal()]; // for typing convenience
    _HPMod = findInRange(constitution);
    // Update Magic Attack Mod by racial resist
    if ((_racename.equalsIgnoreCase("Dwarf")) ||
        (_racename.equalsIgnoreCase("Gnome")) ||
        (_racename.equalsIgnoreCase("Hobbit"))) {
      _racialPoisonResist = (int) Math.round((float) constitution / 3.5);
      _magicAttackMod += _racialPoisonResist;
      // displayRacialMods();
    }
    // display for all characters
    // System.out.println("\n\t Magic Attack Mod = " + _magicAttackMod);

    // 10. ASSIGN THE DEXTERITY MODIFIERS: To Hit Mod (missile), AC Mod
    int dex = _traits[PrimeTraits.DEX.ordinal()]; // for typing convenience
    _toHitDex = findInRange(dex);
    _ACMod = findInRange(dex);
    // System.out.println("\n\t to Hit (missile) = " + _toHitDex + ", \t AC Mod = " + _ACMod);

    // 11a. ASSIGN HERO'S HEIGHT AND WEIGHT
    _weight = _race.calcWeight();
    _height = _race.calcHeight();
    // System.out.print("\n" + _name + " weighs " + _weight + " lbs,");
    // System.out.println(" and is " + _height + " inches tall.");
    // 11b. GET THE HERO'S PHYSICAL DESCRIPTION FROM THIS BODY-TYPE
    _description = initDescription();
    // System.out.println(_description);
    // 11c. SET THE HERO'S INITIAL HUNGER STATE
    _hunger = "Full";

    // 12. SET THE INITIAL LEVEL AND EXPERIENCE POINTS
    _level = 1;
    _XP = 0;

    // 13. ROLL FOR KLASS-SPECIFIC HIT POINTS
    _HP = _klass.rollHP(_HPMod);
    // System.out.println("\nInitial HP = " + _HP + ", including the " + _HPMod + " HP mod.");

    // 14. SET THE NON-LETHAL COMBAT STATS: OVERBEARING, GRAPPLING, PUMMELING, AND SHIELD BASH
    _AP = _traits[PrimeTraits.STR.ordinal()] + _traits[PrimeTraits.DEX.ordinal()];
    _apMods = new int[4];
    _apMods = calcAPMods(_apMods);
    // displayCombatStats();

    // 15. CALCULATE SPEED (BLOCK MOVEMENT)
    _speed = calcSpeed(_AP);
    // System.out.println("\nAP = " + _AP + " yields speed = " + _speed +
    // ", adjusted for height of " + _height);

    // 16. SET INITIAL ARMOR CLASS
    _AC = 10 + _ACMod;
    _AC_Magic = 0; // no magica adjustments initially
    // System.out.println("\nArmor Class (without armor) = " + _AC);

    // 17. ROLL FOR KLASS-SPECIFIC STARTING GOLD
    _gold = _klass.rollGold();
    _silver = 9; // for testing
    _goldBanked = 0.0;
    // System.out.println("\nInitial gold for " + _klassname + " = " + _gold + " gp");

    // 18. ASSIGN SPECIAL THIEF ABILITIES
    String[][] klassSkills = _klass.assignKlassSkills();
    _klassSkills = toArrayList(klassSkills);

    // 19. ADD RACIAL ABILITIES
    // TESTING: Set Spot Detail
    _raceSkills = _race.addRaceSkills(_raceSkills);
    // displayList("Racial Skills: ", _skills);

    // 20. ADD RANDOM OCCUPATION AND OCCUPATIONAL SKILLS
    _occupation = assignOccupation();
    _ocpSkills = assignOcpSkills();
    // testing only
    // _ocpSkills.add("Spot Details: +2 WIS to notice details such as bandits in ambush, "
    // + "\n\t obscure items in a dim room, or centipedes in a pile of trash");
    // displayList("Skills for occupation " + _occupation + ":", _ocpSkills);
    // displayList("Inventory in backpack: ", _inventory);

    // 21. ASSIGN SPELLS TO CLERICS (WIZARDS ALREADY WERE ASSIgned 'READ MAGIC')
    _spellBook = _klass.addKlassSpells(_spellBook);
    _spellsKnown = _spellBook.size();
//    if (_klassname.equalsIgnoreCase("Cleric")) {
//      _spellBook = ((Cleric) _klass).addClericalSpells(_spellBook);
//      _spellsKnown = _spellBook.size();
//      // displayList(String.format("Clerical spells knowns: %s spells: ", _spellsKnown),
//      // _spellBook);
//    }
    
    // 22. Assign initial inventory
    _inven = new Inventory();
    _inven = _inven.assignBasicInventory(_inven);
    _inven = _klass.addKlassItems(_inven);

    // displayList("Inventory in backpack: ", _inventory);


  } // end of Hero constructor


  public Hero(EnumMap<HeroInput, String> inputMap)
  {
    this(inputMap.get(HeroInput.NAME),
         inputMap.get(HeroInput.GENDER),
         inputMap.get(HeroInput.HAIR),
         inputMap.get(HeroInput.RACE),
         inputMap.get(HeroInput.KLASS));
  }

  /** Converts from Skill name, description, and percent change into a string */
  private ArrayList<String> toArrayList(String[][] thiefSkills)
  {
    ArrayList<String> sk = new ArrayList<String>();

    for (int k = 0; k < thiefSkills.length; k++) {
      sk.add(thiefSkills[k][0] + ": " + thiefSkills[k][1]);
    }
    return sk;
  }


  public boolean canUseMagic()
  {
    return _klassname.equals(Klass.CLERIC_CLASS_NAME) ||
           _klassname.equals(Klass.WIZARD_CLASS_NAME);
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

  public ArrayList<String> getKlassSkills()
  {
    return _klassSkills;
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
    String ocpName = _occupation.substring(0,_occupation.indexOf(':'));
    return ocpName;
  }

  public String getRaceName()
  {
    return _racename;
  }

  public List<String> getRaceSkills()
  {
    return _raceSkills;
  }

  public List<String> getOcpSkills()
  {
    return _ocpSkills;
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
    map.put(PersonKeys.LEVEL, String.format("%s", _level));
    map.put(PersonKeys.HP, String.format("%s", _HP));
    map.put(PersonKeys.HP_MAX, String.format("%s", _HP));
    map.put(PersonKeys.AC, String.format("%s", _AC));
    map.put(PersonKeys.AC_MAGIC, String.format("%s", _AC_Magic));

    // Row 4: XP, Speed, Gold/Silver (gp/sp), Gold Banked
    map.put(PersonKeys.XP, String.format("%s", _XP));
    map.put(PersonKeys.SPEED, String.format("%s", _speed));
    map.put(PersonKeys.GOLD, String.format("%s", _gold));
    map.put(PersonKeys.SILVER, String.format("%s", _silver));
    map.put(PersonKeys.GOLD_BANKED, String.format("%s", _goldBanked));

    // Row 5: Occupation, Description
    map.put(PersonKeys.OCCUPATION, _occupation);
    map.put(PersonKeys.DESCRIPTION, _description);

    // Row 6: STR and STR mods: ToHit, StrDamage, Wt Allowance, Load Carried
    map.put(PersonKeys.STR, String.format("%s", _traits[PrimeTraits.STR.ordinal()]));
    map.put(PersonKeys.TO_HIT_MELEE, String.format("%s", _toHitStr));
    map.put(PersonKeys.DAMAGE, String.format("%s", _damage));
    map.put(PersonKeys.WT_ALLOW, String.format("%s", _wtAllow));
    map.put(PersonKeys.LOAD, String.format("%s", "134")); // for testing

    // Row 7: INT and INT mods: percent to know spell, current MSP, max MSP, MSPs/Level,
    // spells known (in book), and max languages
    map.put(PersonKeys.INT, String.format("%s", _traits[PrimeTraits.INT.ordinal()]));
    map.put(PersonKeys.TO_KNOW, String.format("%s", _percentToKnow));
    map.put(PersonKeys.CURRENT_MSP, String.format("%s", _MSPs));
    map.put(PersonKeys.MAX_MSP, String.format("%s", _MSPs));
    map.put(PersonKeys.MSP_PER_LEVEL, String.format("%s", _MSPsPerLevel));
    map.put(PersonKeys.SPELLS_KNOWN, String.format("%s", _spellsKnown));
    map.put(PersonKeys.MAX_LANGS, String.format("%s", _maxLangs));
    map.put(PersonKeys.LITERACY, _literacy);

    // Row 8: WIS and WIS mods: Magic Attack Mod, Current CSP, Max CSPs, CSPs/Level, Turn Undead
    map.put(PersonKeys.WIS, String.format("%s", _traits[PrimeTraits.WIS.ordinal()]));
    map.put(PersonKeys.MAM, String.format("%s", _magicAttackMod));
    map.put(PersonKeys.CURRENT_CSP, String.format("%s", _CSPs));
    map.put(PersonKeys.MAX_CSP, String.format("%s", _CSPs));
    map.put(PersonKeys.CSP_PER_LEVEL, String.format("%s", _CSPsPerLevel));
    map.put(PersonKeys.TURN_UNDEAD, String.format("%s", _turnUndead));

    // Row 9: CON and HP Mod
    map.put(PersonKeys.CON, String.format("%s", _traits[PrimeTraits.CON.ordinal()]));
    map.put(PersonKeys.HP_MOD, String.format("%s", _HPMod));
    map.put(PersonKeys.RMR, String.format("%s", _racialPoisonResist));

    // Row 10: DEX and DEX mods: ToHit Missile, AC Mod
    map.put(PersonKeys.DEX, String.format("%s", _traits[PrimeTraits.DEX.ordinal()]));
    map.put(PersonKeys.TO_HIT_MISSLE, String.format("%s", _toHitDex));
    map.put(PersonKeys.AC_MOD, String.format("%s", _ACMod));

    // Row 11: CHR, then Weight and Height of Hero
    map.put(PersonKeys.CHR, String.format("%s", _traits[PrimeTraits.CHR.ordinal()]));
    map.put(PersonKeys.WEIGHT, String.format("%s", _weight));
    map.put(PersonKeys.HEIGHT, String.format("%s", _height));
    map.put(PersonKeys.HUNGER, _hunger);

    // Row 12: AP and non-lethal combat values
    map.put(PersonKeys.AP, String.format("%s", _AP));
    map.put(PersonKeys.OVERBEARING, String.format("%s", _apMods[OVERBEAR]));
    map.put(PersonKeys.PUMMELING, String.format("%s", _apMods[PUMMEL]));
    map.put(PersonKeys.GRAPPLING, String.format("%s", _apMods[GRAPPLE]));
    map.put(PersonKeys.SHIELD_BASH, String.format("%s", _apMods[BASH]));

    // Row 13: Maximum languages
    map.put(PersonKeys.MAX_LANGS, String.format("%s", _maxLangs));

    // Row 14: All known languages as single string
    StringBuilder sb = new StringBuilder();
    for (int k = 0; k < _knownLangs.size(); k++) {
      sb.append(_knownLangs.get(k));
      sb.append(", ");
    }
    String langList = new String(sb);
    map.put(PersonKeys.LANGUAGES, langList);

    return map;
  }

  /** Update the list with initial languages, including race langauges */
  private List<String> addLanguages(List<String> _knownLangs2)
  {
    _knownLangs2.add("Common");
    String s = _race.getRacialLanguage();
    if (s != null) {
      // langs.add(s);
      addUnique(_knownLangs2, s);
    }
    return _knownLangs2;
  }

  /**
   * Adds the object to the ArrayList, but does not add duplicated
   * 
   * @param _knownLangs2 list to add object to
   * @param obj to add to list if it isn't in the list already
   */
  private void addUnique(List<String> _knownLangs2, String obj)
  {
    if (!_knownLangs2.contains(obj)) {
      _knownLangs2.add(obj);
    }
  }

  /** Females are given more CON and CHR but less STR */
  private int[] adjustTraitsForGender(int[] traits)
  {
    traits[PrimeTraits.STR.ordinal()] -= 1;
    traits[PrimeTraits.CON.ordinal()] += 1;
    traits[PrimeTraits.CHR.ordinal()] += 1;
    return traits;
  }

  // Assign a random occupation to the Hero
  private String assignOccupation()
  {
    MetaDie md = new MetaDie();
    int maxLimit = _ocpTable.length;
    // ndx is index into table, so ranges between 0 and length of table - 1
    int ndx = md.getRandom(1, maxLimit) - 1; // range must be between 1 and maxLimit
    return _ocpTable[ndx];
  }


  // Return the name and description of each skill associated with the given occupation
  private ArrayList<String> assignOcpSkills()
  {
    ArrayList<String> skills = new ArrayList<String>();
    String ocpDesc = "";

    // typing shorthand
    int STR = _traits[PrimeTraits.STR.ordinal()];
    int INT = _traits[PrimeTraits.INT.ordinal()];
    int WIS = _traits[PrimeTraits.WIS.ordinal()];
    int DEX = _traits[PrimeTraits.DEX.ordinal()];
    int CON = _traits[PrimeTraits.CON.ordinal()];
    int CHR = _traits[PrimeTraits.CHR.ordinal()];

    // for testing
    // _occupation = "Armorer";
    // STR = 17;
    // INT = 17;
    // WIS = 17;
    // DEX = 17;
    // CON = 17;
    // CHR = 17;

    // Get all conditional skills for each occupation
    switch (_occupation)
    {
      case ("Academic"): {
        ocpDesc = "Knows diverse information, court politics and bureaucrats.";
        if (INT > 14) {
          skills.add(extractSkillSet("General Knowledge"));
          skills.add(extractSkillSet("Concentration"));
          if (CHR > 14) {
            skills.add(extractSkillSet("Diplomacy"));
          }
        }
        break;
      }
      case ("Acrobat"): {
        ocpDesc = "Acrobatic and aerial body control.";
        if (DEX > 14) {
          skills.add(extractSkillSet("Climb Walls"));
          skills.add(extractSkillSet("Balance"));
          skills.add(extractSkillSet("Escape Artist"));
          skills.add(extractSkillSet("Jump"));
          skills.add(extractSkillSet("Tumble"));
        }
        break;
      }
      case ("Alchemist"): {
        ocpDesc = "Knows chemicals and elixirs. Owns Alchemists' Kit.";
        // _inventory.add(kits[KitNdx.ALCHEMIST.ordinal()]);
        if (INT > 14) {
          skills.add(extractSkillSet("Arcane Knowledge"));
        }
        break;
      }
      case ("Apothecary"): {
        ocpDesc = "Knows herbs, ointments, and medicines. Owns Alchemists' Kit.";
        // _inventory.add(kits[KitNdx.ALCHEMIST.ordinal()]);
        if (WIS > 14) {
          skills.add(extractSkillSet("Natural Knowledge"));
        }
        break;
      }
      case ("Armorer"): {
        ocpDesc = "Makes and repairs metal armor, helmets and shields. Owns Metalsmith Kit.";
        // _inventory.add(kits[KitNdx.METAL.ordinal()]);
        skills.add(extractSkillSet("Repair Armor"));
        break;
      }
      case ("Banker"): {
        ocpDesc = "You were a financial businessman.";
        skills.add(extractSkillSet("Financial Brokering"));
        if (INT > 15) {
          skills.add(extractSkillSet("Appraise Jewelry"));
        }
        break;
      }
      case ("Bowyer"): {
        ocpDesc = "Can make bows and arrows. Owns Woodworking Kit.";
        // _inventory.add(kits[KitNdx.WOOD.ordinal()]);
        skills.add(extractSkillSet("Bowmaking"));
        break;
      }
      case ("Carpenter"): {
        ocpDesc = "Knows wood and woodworking tools. Owns Woodworking Kit.";
        // _inventory.add(kits[KitNdx.WOOD.ordinal()]);
        skills.add(extractSkillSet("Find Secrets in Woodwork"));
        break;
      }
      case ("Farmer"): {
        ocpDesc = "Knows plants, common herbs, greenery.";
        skills.add(extractSkillSet("Identify Plants"));
        skills.add(extractSkillSet("Predict Weather"));
        break;
      }
      case ("Fisher"): {
        ocpDesc = "Knows about bodies of fresh water and lakes. Owns Sewing Kit.";
        // _inventory.add(kits[KitNdx.SEWING.ordinal()]);
        skills.add(extractSkillSet("Netmaking"));
        if (STR > 14) {
          skills.add(extractSkillSet("Fast Swim"));
        }
        break;
      }
      case ("Forester"): {
        ocpDesc = "Has natural knowledge in wooded areas.";
        skills.add(extractSkillSet("Hide in Shadows"));
        skills.add(extractSkillSet("Move Silently"));
        skills.add(extractSkillSet("Wilderness Lore"));
        skills.add(extractSkillSet("Intuit Outdoor Direction"));
        skills.add(extractSkillSet("Spot Details"));
        if (STR > 14) {
          skills.add(extractSkillSet("Fast Swim"));
        }
        break;
      }
      case ("Freighter"): {
        ocpDesc = "Businessman. Ships cargo in wagons. Owns Woodworking Kit.";
        // _inventory.add(kits[KitNdx.WOOD.ordinal()]);
        skills.add(extractSkillSet("Negotiations"));
        skills.add(extractSkillSet("Cargo Transport"));
        if (WIS > 14) {
          skills.add(extractSkillSet("Train Animals"));
        }
        break;
      }
      case ("Gambler"): {
        ocpDesc = "Skilled in games of chance.";
        skills.add(extractSkillSet("Luck"));
        skills.add(extractSkillSet("Pick Pockets"));
        skills.add(extractSkillSet("Open Locks"));
        skills.add(extractSkillSet("Bluff"));
        skills.add(extractSkillSet("Sense Motive"));
        break;
      }
      case ("Hunter"): {
        ocpDesc = "Tracks and kills wild animals for food";
        skills.add(extractSkillSet("Hunting"));
        skills.add(extractSkillSet("Find/Set Traps"));
        skills.add(extractSkillSet("Move Silently"));
        skills.add(extractSkillSet("Hide in Shadows"));
        skills.add(extractSkillSet("Spot Details"));
        if (CHR > 14) {
          skills.add(extractSkillSet("Intimidate"));
        }
        if (CON > 14) {
          skills.add(extractSkillSet("Listening"));
        }
        break;
      }
      case ("Husbandman"):
        ocpDesc = "Knows livestock of all kinds (horses, sheep, cattle, pigs)";
        skills.add(extractSkillSet("Husbandry"));
        if (WIS > 14) {
          skills.add(extractSkillSet("Animal Empathy"));
          skills.add(extractSkillSet("Train Animals"));
        }
        break;
      case ("Innkeeper"): {
        ocpDesc = "Businessman. Runs crowded places, people-oriented, business-savvy";
        skills.add(extractSkillSet("Negotiations"));
        skills.add(extractSkillSet("Sense Motive"));
        if (CHR > 14) {
          skills.add(extractSkillSet("Gather Information"));
        }
        if (INT > 14) {
          skills.add(extractSkillSet("Read Lips"));
        }
        break;
      }
      case ("Jeweler"): {
        ocpDesc = "Recognizes true value of gems, jewelry, etc. " +
            "Works intricate devices like a watchmaker.";
        skills.add(extractSkillSet("Appraise Jewelry"));
        if (DEX > 14) {
          skills.add(extractSkillSet("Open Locks"));
        }
        break;
      }
      case ("Leatherworker"): {
        ocpDesc = "Tans hides and makes leather items. Owns Leatherworking Kit";
        // _inventory.add(kits[KitNdx.LEATHER.ordinal()]);
        skills.add(extractSkillSet("Leatherworking"));
        break;
      }
      case ("Painter"): {
        ocpDesc = "Paints buildings and mixes paint.";
        skills.add(extractSkillSet("Painting"));
        if (CHR > 14) {
          skills.add(extractSkillSet("Gather Information"));
        }
        break;
      }
      case ("Mason"): {
        ocpDesc = "Constructs buildings, works mortar, lays brick; knows stonework.";
        if (INT > 14) {
          skills.add(extractSkillSet("Find Secrets in Stonework"));
        }
        break;
      }
      case ("Miner"): {
        ocpDesc = "Digs ores from caverns and mines. Knows kinds of rock and ores";
        skills.add(extractSkillSet("Intuit Underground Direction"));
        skills.add(extractSkillSet("Cavern Lore"));
        if (INT > 14) {
          skills.add(extractSkillSet("Find Secrets in Stonework"));
        }
        break;
      }
      case ("Navigator"): {
        ocpDesc = "Knows direction at sea, plots water course without getting lost";
        skills.add(extractSkillSet("Predict Weather"));
        skills.add(extractSkillSet("Water Lore"));
        skills.add(extractSkillSet("Intuit Outdoor Direction"));
        skills.add(extractSkillSet("Spot Details"));
        if (STR > 14) {
          skills.add(extractSkillSet("Fast Swim"));
        }
        break;
      }
      case ("Sailor"): {
        ocpDesc = "Knows ships, has knowledge of bodies of water.";
        skills.add(extractSkillSet("Make Raft"));
        if (STR > 14) {
          skills.add(extractSkillSet("Fast Swim"));
        }
        break;
      }
      case ("Shipwright"): {
        ocpDesc = "Builds ships, knows wood and wood-working tools.";
        skills.add(extractSkillSet("Make Raft"));
        if (STR > 14) {
          skills.add(extractSkillSet("Fast Swim"));
        }
        break;
      }
      case ("Tailor"): {
        ocpDesc = "Makes clothing, knows dyes. Owns Sewing Kit";
        // _inventory.add(kits[KitNdx.SEWING.ordinal()]);
        skills.add(extractSkillSet("Sewing"));
        if (CHR > 14) {
          skills.add(extractSkillSet("Gather Information"));
        }
        break;
      }
      case ("Trader"): {
        ocpDesc = "Businessman. Familar with transport equipment.";
        skills.add(extractSkillSet("Financial Brokering"));
        skills.add(extractSkillSet("Sense Motive"));
        if (CHR > 14) {
          skills.add(extractSkillSet("Diplomacy"));
        }
        break;
      }
      case ("Trapper"): {
        ocpDesc = "Catches animals for tanning or money.";
        skills.add(extractSkillSet("Trapping"));
        skills.add(extractSkillSet("Find/Set Traps"));
        skills.add(extractSkillSet("Move Silently"));
        skills.add(extractSkillSet("Open Locks"));
        skills.add(extractSkillSet("Hide in Shadows"));
        skills.add(extractSkillSet("Spot Details"));
        skills.add(extractSkillSet("Wilderness Lore"));
        if ((DEX > 14) && (INT > 14)) {
          skills.add(extractSkillSet("Disable Device Skill"));
        }
        break;
      }
      case ("Weaponsmith"): {
        ocpDesc = "Knows metal weapons of all types and metalworking. Owns Metalsmith Kit.";
        // _inventory.add(kits[KitNdx.METAL.ordinal()]);
        skills.add(extractSkillSet("Make Weapons"));
        break;
      }
      case ("Weaver"): {
        ocpDesc = "Makes tapestries, rugs, bed clothing. Knows dyes. Owns Sewing Kit";
        // _inventory.add(kits[KitNdx.SEWING.ordinal()]);
        skills.add(extractSkillSet("Appraise Tapestries"));
        break;
      }
      case ("Woodworker"): {
        ocpDesc = "Builds wood furniture, cabinets. Knows wood and wood-working tools. " +
            "Owns Woodworking Kit.";
        // _inventory.add(kits[KitNdx.WOOD.ordinal()]);
        skills.add(extractSkillSet("Woodworking"));
        skills.add(extractSkillSet("Find Secrets in Woodwork"));
        if ((DEX > 14) && (INT > 14)) {
          skills.add(extractSkillSet("Disable Device Skill"));
        }
        break;
      }
      case ("Drifter"): {
        ocpDesc = "Everyone is running from something. What's your story?";
        skills.add("No special skills");
        break;
      }
      default: {
        System.err.println(
            "\n assignOcpSkills(): Can't find the occupation given as " + _occupation);
        break;
      }
    }
    // Update occupational description
    _occupation += ": " + ocpDesc;
    return skills;
  }

  /** Display the Hero's key characteristics */
  private void auditOutHero()
  {
    StringBuilder out = new StringBuilder();
    out.append(_name + " ");
    out.append(_gender + " ");
    out.append(_racename + " ");
    out.append(_klassname + " ");
    System.out.println("Hero " + out);
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
  private int[] calcStrengthMods(int[] traits)
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
    int ndx = traits[PrimeTraits.STR.ordinal()] - 3; // read from the table 3 places to the left
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

  /**
   * Display the Hero's prime traits
   * 
   * @param msg header message before traits display
   * @param traits traits for display
   */
  private void displayTraits(String msg, int[] traits)
  {
//    // TODO Make this list depend on PrimeTraits order, and not these constants
//    final String[] ndx = {"STR", "INT", "WIS", "DEX", "CON", "CHR"};
//    System.out.println(msg);
//    for (int k = 0; k < 6; k++) {
//      System.out.print("\t" + ndx[k] + " = " + traits[k] + "\t");
//    }
  }

  // Private: Extract the skills for a given skillname of the given occupation
  private String extractSkillSet(String name)
  {
    String skill = new String();
    // Traverse ocp skills table looking for specified skillname
    for (int k = 0; k < _ocpSkillTable.length; k++) {
      String fullStr = _ocpSkillTable[k].toString();
      // Separate skill name from skill description
      int ndx = fullStr.indexOf(":");
      String skillStr = fullStr.substring(0, ndx); // get skillname from table to match
      if (skillStr.equalsIgnoreCase(name)) {
        skill = fullStr; // get matching line including skill name
        break; // break from forloop when skillname found
      }
    }
    return skill;
  }

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
    int chr = _traits[PrimeTraits.CHR.ordinal()];
    String bodyType = _race.initBodyType(chr, _height, _weight);
    // Start the description with a vowel-sensitive article
    String article = (checkFirstVowel(bodyType) == true) ? "An " : "A ";
  
    // Process baldness.
    String hairType = (_hairColor.equalsIgnoreCase("bald")) ? "a bald head" : _hairColor + " hair";
    String desc1 = article + bodyType + " " + getGender().toString().toLowerCase() + " with " + hairType;
  
    // Get race descriptor for suffix.
    String desc2 = _race.getRaceDescriptor();
  
    // Get Charisma description
    String chrDesc = _race.initCharismaDescriptor(chr);
    String desc3 = _gender.pronoun() + " is " + chrDesc + ".";
  
    String desc = desc1 + " and " + desc2 + ". " + desc3;
    return desc;
  }


  // ====================================================
  // INNER CLASS MockHero
  // ====================================================

  /** Accesses and tests the private methods of the Person object. */
  public class MockHero
  {
    /** Default constructor */
    public MockHero()
    {}

    /** for test */
    public int[] getTraits()
    {
      return Hero.this._traits;
    }

    public Klass getKlass()
    {
      return Hero.this._klass;
    }

    public String getKlassName()
    {
      return Hero.this._klassname;
    }

    public Race getRace()
    {
      return Hero.this._race;
    }

    public String getRaceName()
    {
      return Hero.this._racename;
    }

  } // end of MockHero inner class


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
    result = prime * result + _height;
    result = prime * result + ((_klassname == null) ? 0 : _klassname.hashCode());
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    result = prime * result + ((_occupation == null) ? 0 : _occupation.hashCode());
    result = prime * result + ((_racename == null) ? 0 : _racename.hashCode());
    result = prime * result + Arrays.hashCode(_traits);
    result = prime * result + _weight;
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
    if (_height != other._height)
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
    if (_occupation == null) {
      if (other._occupation != null)
        return false;
    } else if (!_occupation.equals(other._occupation))
      return false;
    if (_racename == null) {
      if (other._racename != null)
        return false;
    } else if (!_racename.equals(other._racename))
      return false;
    if (!Arrays.equals(_traits, other._traits))
      return false;
    if (_weight != other._weight)
      return false;
    return true;
  }

} // end of Hero class
