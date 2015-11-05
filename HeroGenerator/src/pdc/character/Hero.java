/**
 * Person.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package pdc.character;


import java.io.Serializable;
import java.util.ArrayList;

import mylib.pdc.MetaDie;
import pdc.character.Thief.TSKILL;


/**
 * @author Alan Cline
 * @version Sept 4 2015 // rewrite per revised generation rules \n\t
 */
public class Hero implements Serializable // IRegistryElement
{
  // Statics and transients are not saved with the serialized Person object
  /** Recommended serialization constant */
  static final long serialVersionUID = 1007L;

  /* INTERNAL OBJECT COMPONENTS */
  /** One of the four canonical Hero klasses: Fighter, Cleric, Wizard, or Thief */
  private Klass _klass;
  /** The Race object for this Person (Input), and contains the Hunger and Age objects */
  private Race _race = null;

  /** Input Data for Hero */
  private String _name = null;
  /** Male or female Person */
  private String _gender = null;;
  /** Person's hair color, used in building their physical appearance */
  protected String _hairColor = null;
  /** Name of the race to convert to a Race component */
  private String _racename;
  /** Name of the klass to convert to a Klass component */
  private String _klassname;
  /** What we see when we look at the Person */
  private String _description = null;

  /** Hero game attributes */
  private int _level = 1;
  private int _XP = 0;
  private int _HP = 0;
  private int _AP = 0;
  private int _AC = 0;

  // Non-lethal combat stats
  int OVERBEAR = 0;
  int GRAPPLE = 1;
  int PUMMEL = 2;
  int BASH = 3;
  int[] _apMods;

  // Speed is measured in block movement per turn */
  int _speed = 0;

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

  // INT mods
  ArrayList<String> _knownLangs; // everyone knows Common, and perhaps a race language
  ArrayList<String> _spellBook; // spells in the Wizard spell book
  int _maxLangs; // some can learn new languages, up to this number
  // Wizard only
  private int _MSPs = 0;
  private int _MSPsPerLevel = 0;
  private int _percentToKnow = 0;
  private int _spellsKnown = 0;

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
  int _toHitDex = 0;
  int _ACMod = 0;

  // Misc: height and weight of the Hero, affected by race and gender
  int _weight = 0;
  int _height = 0;

  // Gold in hand and gold banked. The decimal represents silver pieces */
  double _gold = 0.0;
  double _goldBanked = 0.0;

  ArrayList<String> _skills; // literacy, occupational, and race skills
  // Number of skills only the thief has
  final int NBR_THIEF_SKILLS = TSKILL.values().length;
  // The first index is the skill name, the second is the chance of success
  String[][] _thiefSkills = new String[NBR_THIEF_SKILLS][2];

  // List of all inventory items
  private enum KitNdx {
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
  private ArrayList<String> _inventory;

  // Table to hold occupational skills
  private String _occupation = "";
  private ArrayList<String> _ocpSkills;

  // Various occupations (31) for random selection
  private String[] _ocpTable = {
      "Academic", "Acrobat", "Alchemist", "Apothecary", "Armorer", "Banker", "Bowyer",
      "Carpenter", "Farmer", "Fisher", "Forester", "Freighter", "Gambler", "Hunter",
      "Husbandman", "Innkeeper", "Jeweler", "Leatherworker", "Painter", "Mason",
      "Miner", "Navigator", "Sailor", "Shipwright", "Tailor", "Trader", "Trapper",
      "Weaponsmith", "Weaver", "Woodworker", "Drifter"};

  // Occupational skills and descriptions for defined occupations
  private String[] _ocpSkillTable = {
      "Animal Empathy: Can communicate emotionally with animals",
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
          + "\n\t --make short bow (-1 to hit) (20 gp, 3 days) "
          + "\n\t --make arrows (-1 dmg), need 1 bird for feathers "
          + "(1 gp per 3d4+2 per day) ",
      "Cargo Transport: knows tack, harness, and transport equipment"
          + "\n\t --Can repair wagons",
      "Cavern Lore: +1 WIS to guide party through caverns without getting lost"
          + "\n\t --Can avoid natural cavern hazards"
          + "\n\t --Identify most rock ores, +1 INT on rarer ores"
          + "\n\t --Uses picks and shovels as +1, +1 weapons",
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
          + "\n\t --+2 CHR to find contact information for a key person",
      "General Knowledge: +1 INT on any general question on specific topic ",
      "Hide in Shadows: as a Level 1 Thief (%)",
      "Hunting: 20% chance of finding wild game",
      "Husbandry: 10% chance of catching live animals"
          + "\n\t --From vet skills, heal d2 HP human dmg or Slow Poison. Needs herbs",
      "Identify Plants: +1 INT on rarer items",
      "Intimidate: +1 CHR to get info from a prison or backdown a bully",
      "Intuit Outdoor Direction: +1 WIS to know direction of travel when outside",
      "Intuit Underground Direction: +1 WIS to know direction when underground",
      "Jump: +2 AP for leaping chasms or reaching lower tree branches",
      "Leatherworking: Makes leather armor (10gp, 3 days)"
          + "\n\t --Makes boots or gloves (5gp, 1 day)",
      "Listening: as a Level 1 Thief (%)",
      "Luck: Gets +1 on all Saves involving luck and risk taking"
          + "\n\t --Gets +2 on all throws involving gaming luck",
      "Make Raft: Make a sailing raft. Needs hand axe (3 days)",
      "Make Weapons: Can make small metal shield (12gp, 4hr)"
          + "\n\t --Make or repair small melee weapons (all at -1 to Hit, -1 dmg):"
          + "\n\t . . dagger (5gp, 2hr); battle axe (5gp, 2hr, need hand axe); spear (5gp, 2hr); "
          + "short sword (no scabbard) (10gp, 1 day)"
          + "\n\t --Make or repair small missile weapons (all at -1 to Hit, -1 dmg): "
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
      "Netmaking: Makes or repairs 10'x10' net that can provide 10d4 fish per day for food. "
          + " Needs 50' rope (2 days) ",
      "Open Locks: as a Level 1 Thief (%)",
      "Painting: Paints buildings, mixes paint. (yep, that's it).",
      "Pick Pockets: as a Level 1 Thief (%)",
      "Predict Weather: Predict next day weather at +2 WIS",
      "Read Lips: Can understand about 1 minute of speaker's speech if within 30' "
          + "and knows the speaker's language",
      "Repair Armor: in the field if proper materials available:"
          + "\n\t --Make shield, small metal (10gp, 1 day) or small wooden (2 gp, 4 hr) "
          + "\n\t --Convert lantern from open (hooded) to bulls-eye lantern (10gp, 4hr) "
          + "\n\t --Make caltrop from 4 spikes (1gp, 1hr) ",
      "Sense Motive: +1 WIS to determine if person is lying or bluffing",
      "Sewing: Can make belt (1gp, 1hr), boots (5gp, 1 day), cloak (1gp, 1hr), hat (1gp, 1hr)",
      "Spot Details: +2 WIS to notice details such as ambushing bandits, obscure items in "
          + "dim room, centipedes in pile of trash",
      "Train Animals: Can train animals or work teams",
      "Trapping: Catch animals alive at 20%",
      "Tumble: land softer when falling (reduce dmg by d3) "
          + "\n\t--Dive tumble over opponents at +2 AC ",
      "Water Lore: +1 WIS to guide party through water areas and avoid natural hazards",
      "Wilderness Lore: Can guide party through badlands and avoid natural hazards "
          + "\n\t --Can navigate outdoor course without getting lost "
          + "\n\t --Can survive off the land",
      "Woodworking: Repair or make mods to wooden items, e.g. repair xbows (not bows),"
          + "\n\t --Add secret compartments to chests"
  };


  // ====================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ====================================================

  /** Default Hero object used for testing */
  public Hero()
  {}

  /**
   * Create the Person from the basic non-klass attributes.
   * 
   * @param name of the Person, default filename under which it is saved
   * @param gender male or female Person
   * @param hairColor selected hair color for this Person
   * @param raceName the concrete subclass object of Race, e.g., Human or Hobbit
   * @param klassName the concrete subclass object of Klass, e.g. Fighter or Thief
   * 
   * @throws InstantiationException if any of the input parms are null
   */
  public Hero(String name, String gender, String hairColor, String raceName, String klassName)
      throws InstantiationException
  {
    // Guards
    if ((name == null) || (gender == null) || (hairColor == null)) {
      throw new InstantiationException("Missing input data passed to Hero constructor");
    }
    if ((raceName == null) || (klassName == null)) {
      throw new InstantiationException("Missing Race or Klass passed to Hero constructor");
    }

    // Setup internal data
    _traits = new int[NBR_TRAITS];

    // 1. INPUT DATA
    _name = name;
    _gender = gender;
    _hairColor = hairColor;
    _racename = raceName;
    _klassname = klassName;
    displayHero();

    // 2. SET PRIME TRAITS for Peasant (base is same for all Klasses and Races)
    MetaDie md = new MetaDie();
    for (int k = 0; k < NBR_TRAITS; k++) {
      _traits[k] = md.rollTrait();
    }
    // displayTraits("Raw traits: ", _traits);

    // 3. REARRANGE THE PRIME TRAIT FOR THE KLASS
    // Create the Klass object
    _klass = Klass.createKlass(_klassname);
    // Verify that a good Klass was created
    if (_klass == null) {
      throw new InstantiationException(String.format("Could not create klass %s", klassName));
    }
    _traits = _klass.adjustTraitsForKlass(_traits);
    // displayTraits(_klassname + "-adjusted Traits: ", _traits);

    // 3. REARRANGE THE PRIME TRAIT FOR THE RACE
    // Create the Race object
    _race = Race.createRace(_racename);
    // Verify that a good Race was created
    if (_race == null) {
      throw new InstantiationException(String.format("Could not create race %s", raceName));
    }
    _traits = _race.adjustTraitsForRace(_traits);
    // displayTraits(_racename + "-adjusted Traits: ", _traits);

    // 4. REARRANGE THE PRIME TRAIT FOR THE GENDER
    if (_gender.equalsIgnoreCase("female")) {
      _traits = adjustTraitsForGender(_traits);
      // displayTraits(_gender + "-adjusted Traits: ", _traits);
    }

    // 5. ENSURE ALL ADJUSTMENTS REMAIN WITH RACIAL LIMITS
    _traits = _race.verifyRaceLimits(_traits);
    displayTraits("Race-verified final Traits: ", _traits);

    // 6. ASSIGN THE STRENGTH MODIFIERS: To Hit Mod, Damage Mod, and Wt Allowance (Load)
    int[] strMods = calcStrengthMods(_traits);
    _toHitStr = strMods[0];
    _damage = strMods[1];
    _wtAllow = strMods[2];
    // displayStrMods(strMods);

    // 7a. ASSIGN THE INTELLIGENCE MODIFIERS: Known Languages, Max Languages, Literacy Skill
    int intel = _traits[PrimeTraits.INT.ordinal()]; // for typing convenience
    _knownLangs = new ArrayList<String>();
    _knownLangs = addLanguages(_knownLangs);
    // displayList("Known Languages: \t", _knownLangs);
    _maxLangs = intel / 2 - 3;
    // System.out.println("Hero can learn an additional " + _maxLangs + " languages.");
    _skills = new ArrayList<String>();
    addUnique(_skills, getLiteracy(intel));
    addUnique(_skills, getLiteracy(intel));
    // displayList("Skills: \t", _skills);
    // 7b. FOR WIZARDS ONLY: PercentToKnow, MSPs/Level, MSPS, Spells Known
    if (_klassname.equalsIgnoreCase("Wizard")) {
      int[] wizMods = calcWizardMods(intel);
      _MSPsPerLevel = wizMods[0];
      _percentToKnow = wizMods[1];
      _MSPs = _MSPsPerLevel; // for first level
      _spellBook = new ArrayList<String>();
      addUnique(_spellBook, "Read Magic");
      _spellsKnown = _spellBook.size();
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
    _weight = _race.calcWeight(_gender);
    _height = _race.calcHeight(_gender);
    // System.out.print("\n" + _name + " weighs " + _weight + " lbs,");
    // System.out.println(" and is " + _height + " inches tall.");
    // 11b. GET THE HERO'S PHYSICAL DESCRIPTION FROM THIS BODY-TYPE
    _description = initDescription();
    // System.out.println(_description);

    // 12. SET THE INITIAL LEVEL AND EXPERIENCE POINTS
    _level = 1;
    _XP = 0;

    // 13. ROLL FOR KLASS-SPECIFIC HIT POINTS
    _HP = _klass.rollHP(_HPMod);
    // System.out.println("\nInitial HP = " + _HP + ", including the " + _HPMod + " HP mod.");

    // 14. SET THE NON-LETAHL COMBAT STATS: OVERBEARING, GRAPPLING, PUMMELING, AND SHIELD BASH
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
    // System.out.println("\nArmor Class (without armor) = " + _AC);

    // 17. ROLL FOR KLASS-SPECIFIC STARTING GOLD
    _gold = _klass.rollGold();
    _goldBanked = 0.0;
    // System.out.println("\nInitial gold for " + _klassname + " = " + _gold + " gp");

    // 18. ASSIGN SPECIAL THIEF ABILITIES
    if (_klassname.equalsIgnoreCase("Thief")) {
      int dexterity = _traits[PrimeTraits.DEX.ordinal()];
      _thiefSkills = ((Thief) _klass).assignThiefSkills(dexterity); // one subclass of Klass only
      // displayThiefSkills(_thiefSkills);
      _thiefSkills = _race.adjRacialThiefSkills(_thiefSkills);
      // displayThiefSkills(_thiefSkills);
    }

    // 19. ADD RACIAL ABILITIES
    _skills = _race.addRaceSkills(_skills);
    // displayList("Racial Skills: ", _skills);

    // 20. ADD RANDOM OCCUPATION AND OCCUPATIONAL SKILLS
    _occupation = assignOccupation();
    _ocpSkills = new ArrayList<String>();
    _ocpSkills = assignOcpSkills();
//    displayList("Skills for occupation " + _occupation + ":", _ocpSkills);
//    displayList("Inventory in backpack: ", _inventory);

    // 21. ASSIGN SPELLS TO CLERICS (WIZARDS ALREADY WERE ASSIgned 'READ MAGIC') 
    if (_klassname.equalsIgnoreCase("Cleric")) {
      _spellBook = new ArrayList<String>();
      _spellBook = ((Cleric) _klass).addClericalSpells(_spellBook);
      _spellsKnown = _spellBook.size();
//      displayList(String.format("Clerical spells knowns: %s spells: ", _spellsKnown), _spellBook);
    }
    // 22. Assign initial inventory
    _inventory = new ArrayList<String>();
    _inventory = _klass.assignBasicInventory(_inventory);
    _inventory = _klass.addKlassItems(_inventory);
//    displayList("Inventory in backpack: ", _inventory);


  } // end of Hero constructor


  /** Adds the object to the ArrayList, but does not add duplicated 
   *  
   * @param oList   list to add object to
   * @param obj     to add to list if it isn't in the list already
   */
  private void addUnique(ArrayList<String> oList, String obj) 
  {
    if (!oList.contains(obj)) {
      oList.add(obj);
    }
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


  private void displayThiefSkills(String[][] thiefSkills)
  {
    // Put the skill name and chance into a single output string
    System.out.println("\nThief Skills:");
    for (int k = 0; k < thiefSkills.length; k++) {
      String name = thiefSkills[k][0];
      String chance = thiefSkills[k][1];
      System.out.println("\t" + name + " = " + chance + "%");
    }
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



  /** Display the Action Points and non-lethal combat stats */
  private void displayCombatStats()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("AP = " + _AP);
    sb.append("\t Overbearing (for " + _weight + " lbs) = " + _apMods[OVERBEAR]);
    sb.append("\t Grappling = " + _apMods[GRAPPLE]);
    sb.append("\t Pummeling = " + _apMods[PUMMEL]);
    sb.append("\t Shield Bash (with no shield) = " + _apMods[BASH]);
    System.out.println("\n" + sb);
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



  public String getName()
  {
    return _name;
  }

  public String getGender()
  {
    return _gender;
  }

  public String getRaceName()
  {
    return _racename;
  }

  public String getKlassName()
  {
    return _klassname;
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
    // Determine proper gender for descriptive statement
    String pronoun = _gender.equalsIgnoreCase("female") ? "She" : "He";

    // Process baldness.
    String hairType = (_hairColor.equalsIgnoreCase("bald")) ? "a bald head" : _hairColor + " hair";
    String desc1 = article + bodyType + " " + getGender().toLowerCase() + " with " + hairType;

    // Get race descriptor for suffix.
    String desc2 = _race.getRaceDescriptor();

    // Get Charisma description
    String chrDesc = _race.initCharismaDescriptor(chr);
    String desc3 = pronoun + " is " + chrDesc + ".";

    String desc = desc1 + " and " + desc2 + ". " + desc3;
    return desc;
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


  private void displayRacialMods()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("Dwarves, Gnomes, and Hobbits only: ");
    sb.append("\t Updated Magic Attack Mod = " + _magicAttackMod);
    sb.append("\t Racial Poison Resist = " + _racialPoisonResist);
    System.out.println("\n" + sb);
  }


  private void displayClericMods()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("Clerics only: ");
    sb.append("\t CSPs per Level = " + _CSPsPerLevel);
    sb.append("\t CSPs = " + _CSPs);
    sb.append("\t Turn Undead: " + _turnUndead + " + ULD");
    System.out.println("\n" + sb);
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


  private void displayWizardMods()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("Wizards only: ");
    sb.append("\t MSPs per Level = " + _MSPsPerLevel);
    sb.append("\t MSPs = " + _MSPs);
    sb.append("\t % to know new spell = " + _percentToKnow);
    sb.append("\t MSPs per Level = " + _MSPsPerLevel);
    sb.append("\t Spells known (in Spellbook): " + _spellsKnown);
    System.out.println("\n" + sb);
  }

  private int[] calcWizardMods(int intell)
  {
    int[] mod = new int[2];
    mod[0] = intell / 2 - 3; // MPSs per level
    mod[1] = intell * 5; // percent to know new spells
    return mod;
  }


  /** Get the literacy based on intelligence; Spell casters are always literate */
  private String getLiteracy(int intel)
  {
    final String ILLITERATE = "Illiterate: Cannot read nor write";
    final String LITERATE = "Literate: Can read and write";
    final String PART_LITERATE = "Can read but cannot write";
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



  /** Update the list with initial languages, including race langauges */
  private ArrayList<String> addLanguages(ArrayList<String> langs)
  {
    langs.add("Common");
    String s = _race.getRacialLanguage();
    if (s != null) {
//      langs.add(s);
      addUnique(langs, s);
    }
    return langs;
  }


  /**
   * Display a list with a header message
   * 
   * @param msg the message to display as label
   * @param list display each element of this list
   */
  private void displayList(String msg, ArrayList<String> list)
  {
    System.out.println("\n" + msg);
    for (int k = 0; k < list.size(); k++) {
      System.out.println("\t" + list.get(k) + ", ");
    }
    System.out.println();
  }

  private void displayStrMods(int[] mods)
  {
    System.out.println("\n\tStr Mods: \t To Hit (melee) = " + _toHitStr + ";\t Dmg = " + _damage
        + ";\t Wt Allownace = " + _wtAllow);
  }

  // Set the strength modifiers: ToHit, Damage, and Wt Allowace
  private int[] calcStrengthMods(int[] traits)
  {
    // STR values 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21
    final int[] toHitTbl = {-3, -2, -2, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 3};
    final int[] dmgTbl = {-3, -3, -2, -2, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5};

    // STR values 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
    final int[] wtTbl = {80, 120, 160, 200, 280, 400, 480, 560, 640, 720, 880, 1040, 1200,
        // STR values 16, 17, 18, 19, 20, 21
        1440, 1680, 2000, 2400, 2800, 3200};

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


  /** Females are given more CON and CHR but less STR */
  private int[] adjustTraitsForGender(int[] traits)
  {
    traits[PrimeTraits.STR.ordinal()] -= 1;
    traits[PrimeTraits.CON.ordinal()] += 1;
    traits[PrimeTraits.CHR.ordinal()] += 1;
    return traits;
  }

  /** Display the Hero's key characteristics */
  private void displayHero()
  {
    StringBuilder out = new StringBuilder();
    out.append(_name + " ");
    out.append(_gender + " ");
    out.append(_racename + " ");
    out.append(_klassname + " ");
    System.out.println("Hero " + out);
  }


  /**
   * Display the Hero's prime traits
   * 
   * @param msg header message before traits display
   * @param traits traits for display
   */
  private void displayTraits(String msg, int[] traits)
  {
    // TODO Make this list depend on PrimeTraits order, and not these constants
    final String[] ndx = {"STR", "INT", "WIS", "DEX", "CON", "CHR"};
    System.out.println("\n" + msg);
    for (int k = 0; k < 6; k++) {
      System.out.print("\t" + ndx[k] + " = " + traits[k] + "\t");
    }
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
    _occupation = "Drifter";
    STR = 17;
    INT = 17;
    WIS = 17;
    DEX = 17;
    CON = 17;
    CHR = 17;

    // Get all conditional skills for each occupation
    switch (_occupation) {
      case ("Academic"): {
        ocpDesc = "Knows diverse information, court politics and bureaucrats.";
        if (INT > 14) {
          skills.add(extractSkillSet("General Knowledge"));
          skills.add(extractSkillSet("Concentration"));
          if (CHR > 14) {
            skills.add(extractSkillSet("Diplomacy"));
          }
        } else {
          skills.add("Too much book-learning. No practical Adventuring skills.");
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
        } else {
          skills.add("You'll break your neck. Don't try it in the dungeon.");
        }
        break;
      }
      case ("Alchemist"): {
        ocpDesc = "Knows chemicals and elixirs. Owns Alchemists' Kit.";
        _inventory.add(kits[KitNdx.ALCHEMIST.ordinal()]);
        if (INT > 14) {
          skills.add(extractSkillSet("Arcane Knowledge"));
        } else {
          skills.add("You'll blow yourself up. Don't try it even once.");
        }
        break;
      }
      case ("Apothecary"): {
        ocpDesc = "Knows herbs, ointments, and medicines. Owns Alchemists' Kit.";
        _inventory.add(kits[KitNdx.ALCHEMIST.ordinal()]);
        if (WIS > 14) {
          skills.add(extractSkillSet("Natural Knowledge"));
        } else {
          skills.add("One mistake and you'll poison yourself. Stick to aspirin.");
        }
        break;
      }
      case ("Armorer"): {
        ocpDesc = "Makes and repairs metal armor, helmets and shields. Owns Metalsmith Kit.";
        _inventory.add(kits[KitNdx.METAL.ordinal()]);
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
        _inventory.add(kits[KitNdx.WOOD.ordinal()]);
        skills.add(extractSkillSet("Bowmaking"));
        break;
      }
      case ("Carpenter"): {
        ocpDesc = "Knows wood and woodworking tools. Owns Woodworking Kit.";
        _inventory.add(kits[KitNdx.WOOD.ordinal()]);
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
        _inventory.add(kits[KitNdx.SEWING.ordinal()]);
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
        _inventory.add(kits[KitNdx.WOOD.ordinal()]);
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
        _inventory.add(kits[KitNdx.LEATHER.ordinal()]);
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
        } else {
          skills.add("You're especially good at putting your finger in a dike");
        }
        break;
      }
      case ("Miner"): {
        ocpDesc = "Digs ores from caverns and mines. Know rock and ores";
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
        _inventory.add(kits[KitNdx.SEWING.ordinal()]);
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
        _inventory.add(kits[KitNdx.METAL.ordinal()]);
        skills.add(extractSkillSet("Make Weapons"));
        break;
      }
      case ("Weaver"): {
        ocpDesc = "Makes tapestries, rugs, bed clothing. Knows dyes. Owns Sewing Kit";
        _inventory.add(kits[KitNdx.SEWING.ordinal()]);
        skills.add(extractSkillSet("Appraise Tapestries"));
        break;
      }
      case ("Woodworker"): {
        ocpDesc = "Builds wood furniture, cabinets. Knows wood and wood-working tools. " +
            "Owns Woodworking Kit.";
        _inventory.add(kits[KitNdx.WOOD.ordinal()]);
        skills.add(extractSkillSet("Woodworking"));
        skills.add(extractSkillSet("Find Secrets in Woodwork"));
        if ((DEX > 14) && (INT > 14)) {
          skills.add(extractSkillSet("Disable Device Skill"));
        }
        break;
      }
      case ("Drifter"): {
        ocpDesc = "Everyone is running from something. What is your story?";
        skills.add("No special skills");
        break;
      }
      default: {
        System.err
            .println("\n assignOcpSkills(): Can't find the occupation given as " + _occupation);
        break;
      }
    }
    // Update occupational description
    _occupation += ": " + ocpDesc;
    return skills;
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

} // end of Hero class