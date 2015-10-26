/**
 * 
 * fee. Request permission to use from Carolla Development, Inc. by email: acline@carolla.com
 */

package pdc.character;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import mylib.ApplicationException;
import mylib.Constants;
import mylib.MsgCtrl;
import mylib.civ.DataShuttle;
import mylib.dmc.IRegistryElement;
import pdc.Inventory;
import pdc.Klass;
import chronos.Chronos;
import chronos.Chronos.ATTRIBUTE;
import chronos.civ.MiscKeys;
import chronos.civ.MiscKeys.ItemFields;
import chronos.civ.MiscKeys.PersonFileData;
import chronos.pdc.Age;
import chronos.pdc.Age.CATEGORY;
import chronos.pdc.AttributeList;
import chronos.pdc.Item;
import chronos.pdc.Occupation;
import chronos.pdc.Race;
import chronos.pdc.Skill;
import chronos.pdc.registry.OccupationRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import chronos.pdc.registry.SkillRegistry;
import civ.HeroDisplayCiv;
import civ.PersonKeys;
import dmc.PersonReadWriter;

/**
 * TODO: [3] Revise the speed calc to have some affect at 1.5 weight allowance, instead of only
 * between 1 and 2 times weight allowed.
 */

/**
 * Generates a new Hero for playing <i>Adventurer</i>, according to the rules documented in the
 * <i>Developers<kbd>'</kbd> Reference Guide</i> of <i>Adventurer: A Fantasy Role-Playing Adventure
 * Program for the <b>Chronos</b> Gaming Framework</i>, Al Cline, February 12, 2002, Revised May 5,
 * 2012. See section 3, pp12-24.
 * <P>
 * <OL>
 * <LI>PROFILE DATA: Collect the input data to create the <code>Person</code> object: name, gender,
 * occupation, hair color, and name of Race. All Heroes are created by default as Peasant Klass.</LI>
 * <LI>PRIME TRAITS: Each Person has six prime traits on which all actions and reactions are based:
 * Strength (STR), Intelligence (INT), Wisdom (WIS), Dexterity (DEX), Constitution (CON), and
 * Charisma (CHR). Peasants are initialized to fixed mediocre values except for CON and CHR. The
 * Hero's traits for the others are generated based on the <code>Guild</code> of which Klass they
 * join later. All traits are rolled using the <i>4d6 - lowest</i> algorithm</LI>
 * <LI>ADJUST FOR RACE: Various non-human races have bonuses and penalties; human Heroes are the
 * norm with no adjustments.</LI>
 * <LI>ADJUST FOR GENDER: Females are weaker than males but are more charismatic and have a higher
 * constitution, so they get CON+1, STR-1, CHR+1 adjustments.</LI>
 * <LI>SET INITIAL AGE: All Heroes start as Young Adult, whose actual age depends on the Hero's
 * Race. Adjustment for Young Adult age: WIS-1, CON+1.</LI>
 * <LI>VERIFY RACE LIMITS: After adjustments, ensure that no trait is greater than possible for that
 * particular Race. Humans range from 8-18 (except STR, which ranges from 8-19). If the trait is
 * outside the limit, it is set to the min or max limit.</LI>
 * <LI>HEIGHT and WEIGHT: Init <i>height</i> and <i>weight</i> of the Person. These values are
 * random, but constrained to a normal (Gaussian) demographic for the Race and gender about a Racial
 * mean value.</LI>
 * <LI>DESCRIPTION: Based on the Person's height, weight, hair color, and charisma, generate a text
 * description about that Person's appearance. High charisma results in positive connotations and
 * adjectives, and low charisma gives a similar description but with negative connotations.</LI>
 * <LI>STR Modifiers: Init <i>ToHitMeleeAdj</i>, <i>DamageAdj</i>, and <i>WeightAllowance</i><br>
 * (a) A stronger Person is more likely to break through his/her opponent's defenses and score a hit
 * with a hand-held weapon (<i>ToHitMeleeAdj</i>), <br>
 * (b) and do more damage with that hit (<i>DamageAdj</i>). <br>
 * (c) The amount of weight the Person can carry (<i>WeightAllowance</i>) depends on the Person's
 * STR. It is assumed that a Hero of average STR can carry their own weight without being encumbered
 * (losing movement speed). Stronger and weaker Persons can carry more or less weight, respectively.
 * </LI>
 * <LI>INT Modifiers: Intelligence drives languages known and learnable, and the Literacy skill of
 * the Hero. <br>
 * (a) Set the languages known and possible for the Person (all Heroes know <i>Common</i>),
 * determine any possible Race-based language and count the total <i>langsKnown</i> (one or two at
 * this point). <br>
 * (b) Calculate the additional INT-based <i>maxLangs</i> that the Person can learn. <br>
 * (c) Find the <i>Literacy</i> ability: Illiterate, Read, or Read and Write.</LI>
 * <LI>WIS Modifier: Init the <i>MagicAttackAdj</i>, a Person's ability to defend against magical
 * attacks. Dwarves, Gnomes, and Hobbits get extra bonuses for their natural resistence to magic.</LI>
 * <LI>CON Modifiers: Init <i>HitPointMod</i>, the bonus/penalty given for high/low physical stamina
 * when determining Hit Points. Also used for Guild promotions.</LI>
 * <LI>DEX Modifiers: Init <i>ToHitMissileMod</i> and <i>ArmorClassMod</i>, the bonus/penalty to aim
 * missile weapons, as in better/worse eye-hand coordination. Somes Races, like Elves, have a
 * natural ability and get an bonus to this Mod. The Hero<kbd>'</kbd>s defensive ability due to
 * ability to dodge and duck affects their AC, which is adjusted for a high/low DEX because they are
 * better or worse at avoiding being hit.</LI>
 * <LI>HUNGER: Calculate the Hero<kbd>'</kbd>s burn rate before getting hungry, weak, or even
 * starving to death. All Heroes normally burn 15 Satiety Points (SP) per pound weight, and all
 * start with full bellies.</LI>
 * <LI>EXPERIENCE: The Hero's power is measured through a large-grained metric, <i>Level</i>, and a
 * fine-grained metric, <i>Experience Points (XP)</i>. For every few thousand XP gained, the Hero
 * can rise in Level and power. <br>
 * (a) Level: All Heroes start at 0 level. Peasants need 200 XP to reach Level 1 and be eligible to
 * join a Guild. This requirement forces the Hero to explore and gain some starting experience.<br>
 * (b) Experience Points (XP): All Heroes start at 0 XP. XP are earned by defeating monsters,
 * solving puzzles, and obtaining gold and valueables.</LI>
 * <LI>HIT POINTS: Set the initial <i>Hit Points (HP)</i> to 10, plus whatever HP Mod the Hero has.
 * Peasants start with a fixed bare minimum; later, Guilds will bestow additional random number of
 * Klass-specific HPs to the Hero.</LI>
 * <LI>INVENTORY: Create a new <code>Inventory</code> object and initialize it with the default
 * Items for new Hero. All Peasants start with the same bare minimum. Some Occupations have a Kit,
 * which should also be added to the Hero<kbd>'</kbd>s Inventory.</LI>
 * <LI>CASH: Set the starting <i>gold</i> for the Peasant, measured in gold pieces (gp) and silver
 * pieces (sp), where 1 gp = 10 sp. All Peasants start with the same bare minimum, which is less
 * than the poorest of the Guild classes, the Magic-User. The cash's weight is added to the Hero's
 * load. Set the <i>gold banked</i> (saved) to 0/0.</LI>
 * <LI>ACTION POINTS (AP): Add STR and DEX to yield the AP. The AP modifiers (<i>overbearing,
 * grappling, pummeling, shield bash, movement</i>) are based on quickly changing conditions, so are
 * calculated as needed. The AP value is frequently needed and somewhat stable so is kept as an
 * attribute.<br>
 * (a) Overbearing = AP + (1 per 25 pound weight) <br>
 * (b) Grappling = AP + DamageMod + ToHitMissleMod <br>
 * (c) Pummeling = AP <br>
 * (d) Shield Bash = 0 (Hero has no Shield when created).</LI>
 * <LI>ARMOR CLASS (AC): Set <i>armor class</i> to 10 + AC Mod. Hero has no armor yet.</LI>
 * <LI>SPEED. <i>Speed</i>, or <i>base movement</i>, depends on the AP and weight carried by the
 * Hero. Calculate the base movement, which can change as the Hero plays.</LI>
 * <LI>SPECIAL ABILITIES: Assign any special abilities that comes with the <i>occupation</i>
 * attribute, then add the Race-based <i>special abilities</i>. Later, after the Hero joins a
 * Klass-based Guild, special Klass abilities are added. Human Peasants have none.</LI>
 * </OL>
 * <P>
 * After the Hero is generated, display the Hero and certain traits to the user. The user sees only
 * attributes that they would directly experience, such as height and weight. The traits, e.g., STR
 * and CON, are not shown. CHR can be inferred by the Hero's description.
 * <P>
 * IMPLEMENTATION NOTE: The <code>Person</code> class includes a specific <code>Race</code> subclass
 * and a specific <code>Klass</code> subclass as component objects. It also is assocated with an
 * <code>Inventory</code> object. All calls to the component objects go through a Person wrapper. <br>
 * Attributes that are set in the constructor and change rarely are defined with
 * <code>init___()</code> methods. <code>init</code> methods are not called outside the constructor,
 * and are only called once. <br>
 * Attributes frequently used and with low volatility, or are part of the class interface, are
 * calculated and saved as attributes for efficiency with methods that are often prefixed as
 * <code>set___()</code> methods. <br>
 * Values that change frequently, or depend on values that do, like weight of Items carried, or
 * Armor Class, are calculated when needed, and not defined as attributes. These methods are
 * prefixed as <code>calc___()</code> methods.
 * 
 * @author Alan Cline
 * @version Feb 14 2009 // original <br>
 *          Feb 28 2009 // modified after Race and Klass re-design <br>
 *          Mar 4 2009 // modified for serialization <br>
 *          Mar 14 2009 // modified for factor methods and cleaner analysis <br>
 *          Apr 24 2009 // remove AP mods and movement as attributes because they will be calculated
 *          as needed <br>
 *          Apr 26 2009 // added MockPerson inner class, and consequently was able to make public
 *          and private methods as they should be. Also added the {@code init} methods for
 *          fundamental attributes. <br>
 *          May 10 2009 // added age attribute and cleaned up <br>
 *          Jan 2 2010 // replaced occupation string with Occupation object <br>
 *          Jul 3 2010 // support for CIV and packFields() <br>
 *          Mar 26 2011 // revise for refactoring requirements spec <br>
 *          Apr 10 2011 // TAA uncommented methods, ensured function for testing <br>
 *          Oct 2 2011 // refactored for MVP Stack implementation <br>
 *          Nov 3 2011 // polished for final integration testing <br>
 */
public class Person implements Serializable, IRegistryElement
{
  // Statics and transients are not saved with the serialized Person object
  /** Recommended serialization constant */
  static final long serialVersionUID = 1007L;

  /*
   * METADATA CONSTANTS
   */

  // /**
  // * Minimum intelligence to be able to learn any new language except Common and Race lang
  // */
  // static private final int MIN_LANG_INT = 10;

  /* INTERNAL OBJECT COMPONENTS */
  /** The DMC read/writer for Person */
  transient private PersonReadWriter _prw = null;
  /**
   * The Race object for this Person (Input), and contains the Hunger and Age objects
   */
  private Race _Race = null;
  /**
   * The Peasant, a non-Klassed person at Level 0, is the default Person (Input)
   */
  private Klass _klass = null;
  /** The background vocation of the Person before he/she became a hero */
  private Occupation _Occupation = null;
  /**
   * Collection of Items that Person carries when first starting out. This object affects the
   * Person's Action Points, and contains the Person's money.
   */
  private Inventory _Inventory = null;
  /** A Person's hunger is a separate object due to its behavior during play */
  private Hunger _myHunger = null;

  /*
   * DIRECT ATTRIBUTES OF PERSON
   */
  /** Name of the Person (Input) */
  private String _name = null;
  /** Male or female Person (Input) */
  private String _gender = null;;
  /** Person's hair color, used in building their physical appearance (Input) */
  protected String _hairColor = null;
  /** What we see when we look at the Person (derived) */
  private String _description = null;

  /**
   * Array of key traits of the Person: STR, INT, WIS, CON, DEX, CHR. Generated for Klass and
   * adjusted for Race, but shared commonly with all elements of Person.
   */
  protected AttributeList _trait = null;

  /**
   * The list of languages the Person knows; default to 2 at init (updated as player plays). Human
   * Peasants start only with Common. Other races have also a racial language; mixed races, e.g.
   * half-elf, have a chance at a racial language. Rogues also know "Thieves' Cant", a Klass-based
   * language.
   */
  private List<String> _langs = new ArrayList<String>();
  /**
   * The special abilities for the Person, depending on the occupation, Race or Klass. (if none,
   * then assign "None")
   */
  private List<Skill> _skills = new ArrayList<Skill>();

  // Derived from STR attribute; low volatility
  /**
   * Adjustment to chance To Hit (cause damage) with melee weapons due to extra STR
   */
  private int _toHitMeleeMod = Constants.UNASSIGNED;
  /** Extra damage due to STR if the Person makes a Hit */
  private int _damageMod = Constants.UNASSIGNED;
  /**
   * The weight (ounces) a Person can carry before being encumbered. Average Person can carry their
   * own weight.
   */
  private int _wtAllow = Constants.UNASSIGNED;

  /** Maximum number of languages Person can know, based on INT */
  private int _maxLangs = Constants.UNASSIGNED;

  // Derived from INT attribute, not volatile
  /** Minimum intelligence to be able to read, even Common */
  transient private final int MIN_READ_INT = 10;
  /** Minimum intelligence to be able to write, even Common */
  transient private final int MIN_WRITE_INT = 12;
  /** Literacy of the Person, based on INT */
  private MiscKeys.Literacy _literacy = MiscKeys.Literacy.ILLITERATE;

  // Derived from DEX attribute; low volatility */
  /** Adjustment to defensive armor class (AC) based on DEX */
  private int _acMod = Constants.UNASSIGNED;

  /**
   * Action Points (STR + DEX) determine non-weapon battle outcomes; low voltatilty but here for
   * efficiency
   */
  private int _ap = Constants.UNASSIGNED;

  private final String BAD_INSTANTIATION_MSG = "Class %s could not be instantiated";

  private String _key;

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /** Default constructor */
  public Person()
  {}

  /**
   * Create the Person from the basic non-klass attributes.
   * 
   * @param name of the Person, default filename under which it is saved
   * @param gender male or female Person
   * @param occup selected occupation of the non-klass Person
   * @param hairColor selected hair color for this Person
   * @param raceName the concrete subclass object of Race, e.g., Human or Hobbit;
   * @param klassName the concrete subclass object of Klass, initially a Peasant;
   * @param attribs the attribute scores of the Person.
   * 
   * @throws InstantiationException if any of the input parms are null
   */
  public Person(String name, String gender, String occup, String hairColor,
      String raceName, String klassName, AttributeList attribs) throws InstantiationException
  {
    // Guards
    if ((name == null) || (gender == null) || (occup == null)
        || (hairColor == null)) {
      throw new InstantiationException(
          "Missing input data passed to Person constructor");
    }
    if ((raceName == null) || (klassName == null)) {
      throw new InstantiationException(
          "Missing Race or Klass passed to Person constructor");
    }

    // TODO: ABC Ensure that source of error is passed into the shuttle for
    // passing into the error
    // message at the GUI
    // Create the Race object and componentize it
    _Race = Race.createRace(raceName);
    if (_Race == null) {
      throw new InstantiationException(String.format(
          BAD_INSTANTIATION_MSG, raceName));
    }

    // Create the Klass object and componetize it
    _klass = Klass.createKlass(klassName);
    // Verify that good objects were created
    if (_klass == null) {
      throw new InstantiationException(String.format(
          BAD_INSTANTIATION_MSG, klassName));
    }

    // Create the Occupation object from its name
    _Occupation =
        ((OccupationRegistry) RegistryFactory.getInstance().getRegistry(RegKey.OCP))
            .getOccupation(occup);
    // Verify that good objects were created
    if (_Occupation == null) {
      throw new InstantiationException(String.format(
          BAD_INSTANTIATION_MSG, occup));
    }

    /*
     * CONSTRUCT THE PERSON OBJECT General heuristic: use set methods for initiating low volatility
     * or unchanging attributes; use calc methods if they change more frequently.
     */

    // 1. PROFILE DATA
    _name = name;
    _Race.setGender(gender);
    _hairColor = hairColor;

    // 2. SET PRIME TRAITS for Peasant (base is same for all Klasses and
    // Races)

    // _trait = _klass.rollInitialTraits();

    // 3. ADJUST TRAITS FOR RACE
    // _trait = _Race.adjTraitsForRace(_trait);

    // 4. ADJUST TRAITS FOR GENDER
    // _trait = _Race.adjTraitsForGender(_trait);

    _trait = attribs; // traits are now set during creation.

    // 5. SET INITIAL AGE: Race dependent
    // Adjust traits for Young Adult, the Age at which all new Heroes start
    _Race.initAge();
    _trait = Age.adjTraitsForAge(CATEGORY.YOUNG_ADULT, _trait);

    // 6. VERIFY RACE LIMITS. If a trait is out of bounds, revise it
    try {
      _trait = _Race.verifyTraits(_trait);
    } catch (ApplicationException ex) {
      MsgCtrl.errMsg(this, ex.getMessage());
    }

    // 7. HEIGHT and WEIGHT (unchanging attributes, but have random
    // calculation)
    _Race.initHeight();
    _Race.initWeight();

    // 8. BUILD DESCRIPTION (no Chronos analog) built from many other
    // attributes
    _description = initDescription();

    // 9. STR Modifiers: toHitMeleeMod, DamageMod, and WeightAllowance
    // STR modifiers work off a bit higher than the middle trait, thus the
    // +2 in the last parm
    _toHitMeleeMod = calcTraitMod(_trait.get(ATTRIBUTE.STR),
        Chronos.LOW_TRAIT, Chronos.MID_TRAIT + 2);
    // Extra damage due to STR if the Person makes a Hit; currently, it is
    // the same as the ToHitMod
    _damageMod = _toHitMeleeMod;
    // This STR modifer is calculated after height and weight are found
    // Weight allowance is the weight (pounds) a Person can carry before
    // being encumbered.
    // An average Person can carry his or her own weight before being
    // encumbered (slowed).
    _wtAllow = _Race.calcWeightAllowance(_trait.get(ATTRIBUTE.STR));

    // 10. INT Modifiers: Language known and learnable
    // All Persons know Common, and their Race language if they have one
    _langs = initLanguages();
    // Calculate additional languages that can be learned (1 is a minimum
    // due to Common)
    _maxLangs = initMaxLangs();
    // Find the literarcy skill of the Hero
    _literacy = initLiteracy(_trait.get(ATTRIBUTE.INT));

    // 11. WIS modifier: MagicAttackMod: Update default for Race factors
    int defMAA = calcTraitMod(_trait.get(ATTRIBUTE.WIS), Chronos.LOW_TRAIT,
        Chronos.MID_TRAIT);
    // Special racial adjustment for magic resistance due to racial CON
    _Race.updateMagicAttackAdj(defMAA, _trait.get(ATTRIBUTE.CON));

    // 12. CON Modifers: HitPointMod
    int hpMod = calcTraitMod(_trait.get(ATTRIBUTE.CON), Chronos.LOW_TRAIT,
        Chronos.MID_TRAIT);
    _klass.updateHPAdj(hpMod); // then set it in Klass since it is used
                               // there

    // 13. DEX Modifiers: ToHitMissileMod and ArmorClassMod
    // Set the DEX modifier ToHitMod with Missile weapon...
    int hpMissleMod = calcTraitMod(_trait.get(ATTRIBUTE.DEX),
        Chronos.LOW_TRAIT, Chronos.MID_TRAIT);
    // ...then set it in Race
    _Race.updateToHitMissileAdj(hpMissleMod, _trait.get(ATTRIBUTE.DEX));
    // Set the adjustment for armor class, based on ability to avoid a hit
    // (DEX)
    _acMod = calcTraitMod(_trait.get(ATTRIBUTE.DEX), Chronos.LOW_TRAIT,
        Chronos.MID_TRAIT);

    // 14. HUNGER Values, requires Hero's weight, and has multiple
    // attributes (defaults to FULL)
    _myHunger = new Hunger(_Race.getWeight());

    // 15. EXPERIENCE: Set starting Level to 0 and XP to 0
    _klass.initExperience();

    // 16. HIT POINTS: All beginning Persons start with same value
    _klass.initHitPoints();

    // 17. CASH (Gold and Silver pieces):
    // Set the initial cash for the Peasant; nothing is in the Bank at start
    _Inventory = new Inventory();
    // _Inventory.initCash(_klass); rolled into initStartingInventory()
    // method

    // 18. INVENTORY: Create default set of Items.
    // All Persons start with some default inventory items, including gold
    // and silver, depending
    // on what Klass they are. In this case, the Hero starts as a Peasant
    // Klass.
    _Inventory.initStartingInventory();

    // 19. ACTION POINTS (associated with Overbearing, Grappling, Pummeling,
    // Shield Bash
    _ap = _trait.get(ATTRIBUTE.STR) + _trait.get(ATTRIBUTE.DEX);

    // 20. ARMOR CLASS: Calculate the Armor Class, based on inventory and
    // DEX adjustment
    _Inventory.calcAC(_acMod);

    // 21. SPEED: Base movement from AP and load carrried
    calcEncumberance();
    // TODO: Add in non-lethal fighting values (net Speed affects affect
    // Overbearing)

    // 23. SPECIAL ABILITIES are compiled from occupation, intelligence,
    // Race, and Klass
    _skills = initSkills();

  } // end of Person constructor


  /**
   * Person constructor without attributes
   * 
   * @param name of the Person, default filename under which it is saved
   * @param gender male or female Person
   * @param occup selected occupation of the non-klass Person
   * @param hairColor selected hair color for this Person
   * @param raceName the concrete subclass object of Race, e.g., Human or Hobbit;
   * @param klassName the concrete subclass object of Klass, initially a Peasant; *
   * @throws InstantiationException if any of the input parms are null
   */
  public Person(String name, String gender, String occup, String hairColor,
      String raceName, String klassName) throws InstantiationException
  {

    /** roll random traits if none were provided */
    this(name, gender, occup, hairColor, raceName, klassName, Klass.createKlass(klassName)
        .rollInitialTraits());

  }

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS TO SUPPORT
   * THE CONSTRUCTOR ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  // /** Action Points: composite value of STR and DEX that reflects
  // athleticism, prowess, and action
  // * success. Used to calc AP for overbearing, grappling, pummeling, and
  // shield bash.
  // * @return the number of Action Points
  // */
  // public int calcAP()
  // {
  // return ( _trait[STR] + _trait[DEX]);
  // }

  /** Adjusts the provided trait value for Females */
  public static int adjustTraitsForFemale(int num, String attrib)
  {

    if (attrib.equalsIgnoreCase("Strength")) {
      return num - 1;
    }
    if (attrib.equalsIgnoreCase("Charisma")) {
      return num + 1;
    }

    return num;

  }

  // TODO: Move this to Inventory class?
  /**
   * Check if the Person is encumbered by carrying more weight than allowed. If he is encumbered,
   * his movement is slowed by the amount of over-weight carried until if too much weight, he will
   * not be able to move at all. If his load is less than the weight allowed, he will not be
   * impeded. Encumberance applies for loads over the weight allowed to twice the weight allowed.
   * The encumberance factor is multiplied times the speed of the person to get the net speed. <br>
   * ALGORITHM: The encumberance E is a factor going from 1 to 0 as the ratio R (= load / wt
   * allowed) goes from 0 to 2 (or greater). If R <= 1, E = 1; if R >= 2, E = 0 (no movement). The
   * general equation for this linear calculation is <br>
   * E = -R + 2 for (1 <= x <= 2). <br>
   * The weight allowance is converted to ounces for a more precise compare. <br>
   * See <code>calcSpeed()</code>.
   * 
   * @return a speed multiplier factor from 0 to 1
   */
  public double calcEncumberance()
  {
    // Get the needed inputs
    int ozAllow = _Race.calcWeightAllowance(_trait.get(ATTRIBUTE.STR));
    int load = _Inventory.calcInventoryWeight();
    double rating = 0.0; // unassigned
    double ratio = (double) load / (double) ozAllow;
    if (ratio <= 1) {
      rating = 1.0;
    } else if (ratio >= 2) {
      rating = 0.0;
    } else {
      rating = -ratio + 2.0;
    }
    return rating;
  }

  /**
   * Return the total weight of all inventory items and gold carried
   * 
   * @return the Person's load in ounces
   */
  public double calcLoad()
  {
    return _Inventory.calcInventoryWeight();
  }

  /**
   * Get a linear adjustment mod from a low, middle, or high bracket for a particular trait value.
   * If a trait is below a lower limit, the mod is offset negative; else if the trait is above a
   * higher limit, the mod is offset positive; the middle bracket gives 0 mod value. <br>
   * This algorithm is used frequently by both Race and Klass objects, so it is generalized into a
   * consistent lookup, and placed in this class.
   * 
   * @param targetValue to search in table
   * @param lowValue penalty per point below
   * @param highValue bonus per point above
   * @return the bonus or penalty for the given traitValue
   */
  public int calcTraitMod(int targetValue, int lowValue, int highValue)
  {
    // Set AC adjustment limits
    // Guards against inverted range endpoints, or no middle range
    if (lowValue >= highValue) {
      return Constants.UNASSIGNED;
    }
    // Mods default to 0
    int mod = 0;
    if (targetValue < lowValue) {
      mod = targetValue - lowValue;
    } else if (targetValue > highValue) {
      mod = targetValue - highValue;
    }
    return mod;
  }

  /**
   * Display the Hero data. Create the civ and widget, then push each of three data shuttles to be
   * converted and displayed: Person's attributes, Inventory, and Skills.
   */
  public void display()
  {
    // Create the civ to process the output data
    HeroDisplayCiv heroCiv = new HeroDisplayCiv(this);
    // Load the Person's attribute data into a shuttle
    // DataShuttle<PersonKeys> attribShuttle = loadPersonData(new
    // DataShuttle<PersonKeys>(PersonKeys.class));
    DataShuttle<PersonKeys> attribShuttle = null; // temp
    heroCiv.populateAttributes(attribShuttle);
    // Load the Person's Inventory data into a list
    heroCiv.populateInventory(_Inventory.getInventory());
    // Load the Person's Skillset into a list
    heroCiv.populateSkills(_skills);
  }

  /**
   * Return the state of hunger of the Person, i.e., the description
   * 
   * @return how hungry the Person is
   */
  public Hunger.State findHungerState()
  {
    return _myHunger.findHungerState();
  }

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ COMMON GETTERS and SETTERS for
   * BASE CLASS and COMPONENTS ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /**
   * Slight DEX adjustment to avoid damage by adjusting the Person's armor class.
   * 
   * @return Armor Class adjustment
   */
  public int getACMod()
  {
    return _acMod;
  }

  /**
   * Get the age of the person
   * 
   * @return the Person's age (in years)
   */
  public double getAge()
  {
    return _Race.getAge() / Constants.SECS_PER_YEAR;
  }

  /**
   * Get the AP of the person
   * 
   * @return the Person's Action Points
   */
  public int getAP()
  {
    return _ap;
  }

  /**
   * Return the current hunger of the Person in satiety points
   * 
   * @return current number of satiety points
   */
  public double getCurSatiety()
  {
    return _myHunger.calcHunger(0L);
  }

  // /** Slight bit of extra damage due to STR when an opponent has been hit
  // * @return Damage bonus or penalty
  // */
  // public int getDamageAdj()
  // {
  // return _damageAdj;
  // }

  /**
   * What the Person sees if looking in a mirror at mid-range, or what someone else will see when
   * looking at this Person.
   * 
   * @return description of Person
   */
  public String getDescription()
  {
    return _description;
  }

  // /** How much money is the Person carrying?
  // * @return the money (in gold pieces)
  // */
  // public double getGold()
  // {
  // return _gold;
  // }
  //
  // /** How much money does the Person have stored away?
  // * @return money value
  // */
  // public double getGoldBanked()
  // {
  // return _goldBanked;
  // }

  /**
   * Is the Person male or female?
   * 
   * @return gender of Person
   */
  public String getGender()
  {
    return _Race.getGender();
  }

  /**
   * Get the data needed to save the Person
   * 
   * @param pfd person file data needed for saving the person
   * @return the PersonReadWriter data
   */
  public DataShuttle<PersonFileData> getFileData(DataShuttle<PersonFileData> pfd)
  {
    _prw = new PersonReadWriter();
    // pfd = packFileData(pfd);
    // Add the Person's name as a default file name
    pfd.putField(PersonFileData.DEFAULT_FILENAME, _name);
    return pfd;
  }

  /**
   * How tall the Person depends on the gender and Race
   * 
   * @return the Person's height in inches
   */
  public int getHeight()
  {
    return _Race.getHeight();
  }

  /**
   * Return the calculated Armor Class
   * 
   * @return the armor class value (10 = no armor)
   */
  public int getAC()
  {
    return _klass.calcAC(_acMod);
  }

  /**
   * Retrieve the HP Adj from the Klass component
   * 
   * @return the extra Hit Points for each level
   */
  public int getHitPointAdj()
  {
    return _klass.getHitPointAdj();
  }

  /**
   * The life force (Hit Points) of the Person. As Person is dead when HP go to zero.
   * 
   * @return current HP value
   */
  public int getHP()
  {
    return _klass.getHP();
  }

  /**
   * Return the Inventory object (for display)
   * 
   * @return the Inventory object
   */
  public Inventory getInventory()
  {
    return _Inventory;
  }

  /**
   * Return the total weight (in ounces) of all inventory items, without gold weight
   * 
   * @return the Person's Item weight only
   */
  public int getInventoryWeight()
  {
    return _Inventory.calcInventoryWeight();
  }

  /*
   * Two {@code Person}s are considered equal if they have the same name, gender, Occupation, Race,
   * and Skill set.
   * 
   * @return false if not all elements are the same
   */
  public boolean equals(IRegistryElement target)
  {
    if (target == null) {
      return false;
    }
    if (this == target) {
      return true;
    }
    Person other = (Person) target;
    boolean bName = _name.equals(other.getName());
    boolean bGender = _gender.equals(other.getGender());
    boolean bOccup = _Occupation.equals(other.getOccupation());
    boolean bRace = _Race.equals(other.getRace());
    List<Skill> skillset = other.getSkills();
    boolean bSkills = _skills.equals(skillset);

    return (bName || bGender || bOccup || bRace || bSkills);
  }


  public String getKey()
  {
    return _key;
  }

  /**
   * Return the name of the Klass subclass, e.g. Peasant, Fighter, Cleric
   * 
   * @return the Klass subclass name of this Person
   */
  public String getKlassName()
  {
    return _klass.getKlassName();
  }

  /**
   * Concatenate all the languages known by the Person into a String list for display
   * 
   * @return the list of known languages
   */
  public String getLangs()
  {
    StringBuilder sb = new StringBuilder();
    for (int k = 0; k < _langs.size(); k++) {
      sb.append(_langs.get(k));
      // Add a comma and space if there are more languages yet
      if (k < _langs.size() - 1) {
        sb.append(", ");
      }
    }
    return new String(sb);
  }

  /**
   * How many languages does this Person know?
   * 
   * @return the number of languages known
   */
  public List<String> getLangsKnown()
  {
    return _langs;
  }

  /**
   * Return the experience level of this Person. All Peasant's start at Level 0, then get promoted
   * by a Guild to higher levels after so many Experience Points achived.
   * 
   * @return level of the Person
   */
  public int getLevel()
  {
    return _klass.calcLevel();
  }

  /**
   * Return the literacy of the Person: Illiteration, Reading, Writing (which assumes reading).
   * 
   * @return enum value of Person
   */
  public MiscKeys.Literacy getLiteracy()
  {
    return _literacy;
  }

  /**
   * Some WIS Persons and certain races are resistance to magical attacks. Clerics can use magical
   * attack adjustment to increase certain kinds of attacks.
   * 
   * @return magic attack adjustment for this Person
   */
  public int getMagicAttackMod()
  {
    return _Race.getMagicAttackMod();
  }

  /**
   * Based on INT, Persons have a limit on how many languages they can learn (besides Common and
   * their possible race language.
   * 
   * @return the total number of languages a Person can learn (and know)
   */
  public int getMaxLangs()
  {
    return _maxLangs;
  }

  /**
   * Get the name of this Person
   * 
   * @return the Person's name
   */
  public String getName()
  {
    return _name;
  }

  /**
   * What did the Person do as a Peasant, before they became a Hero?
   * 
   * @return occupation of Person
   */
  public Occupation getOccupation()
  {
    return _Occupation;
  }


  /**
   * Get the Race of this Person
   * 
   * @return the Race subclass
   */
  public Race getRace()
  {
    return _Race;
  }

  // TODO: Remove this method, and go through {@code getRace} method to get the race name
  /**
   * Return the name of the Race subclass, e.g. Human, Elf, Hobbit, etc.
   * 
   * @return the Race subclass name of this Person
   */
  public String getRaceName()
  {
    return _Race.getRaceName();
  }

  /**
   * Return a list of skills for the Person originating from Race and Klass
   * 
   * @return array of skills
   */
  public List<Skill> getSkills()
  {
    return _skills;
  }

  /**
   * Person's with higher DEX are more likely to hit with a missile weapon, e.g. bow or thrown
   * dagger. Elves are also extemely skilled with the bow, and have a slightly higher adjustment.
   * 
   * @return the adjustment to hit an opponent with a missile weapon
   */
  public int getToHitMissileMod()
  {
    return _Race.getToHitMissileMod();
  }

  /**
   * Returnt the Person's six prime traits: STR, INT, WIS, CON, DEX, CHR
   * 
   * @return the int array containing the traits
   */
  public AttributeList getTraits()
  {
    return _trait;
  }

  /**
   * How much the Person can carry before being encumbered (being slowed down) depends on STR
   * 
   * @return the max amount of weight (load) the Person can carry (in ounces)
   */
  public int getWtAllowance()
  {
    return _wtAllow;
  }

  /*
   * INVENTORY WRAPPERS
   */

  /**
   * What the Person weighs depends on gender and race.
   * 
   * @return the Person's weight, in pounds
   */
  public int getWeight()
  {
    return _Race.getWeight();
  }

  /*
   * KLASS WRAPPERS
   */

  // /** Return the calculated Armor Class
  // * @return the armor class value (10 = no armor) */
  // public int getAC()
  // {
  // return _Klass.calcAC(_acAdj);
  // }

  /**
   * The value that characterizes experience playing. As XP are achieved, the Person can be promoted
   * to a higher level for more power.
   * 
   * @return current value of XP
   */
  public int getXP()
  {
    return _klass.getXP();
  }

  /**
   * Examine the Person's inventory for a particular item.
   * 
   * @param target the name, or adjective, of the Item being searched; it should find "small"
   *        equally as well as "shield"
   * @return true if Item was found
   */
  public boolean hasItem(String target)
  {
    return _Inventory.hasItem(target);
  }

  /**
   * Determine if the Person has a particular skill by traversing through the skill list. Skills are
   * built from the occupation, INT, race, and klass abilties
   * 
   * @param target the name of the skill being searched
   * @return true if skill was found
   */
  public boolean hasSkill(String target)
  {
    // Guard against no input parm
    if ((target == null) || (target.length() <= 0)) {
      return false;
    }
    boolean retval = false;
    for (int k = 0; k < _skills.size(); k++) {
      if (_skills.get(k).getName().equalsIgnoreCase(target)) {
        retval = true;
        break;
      }
    }
    return retval;
  }

  /**
   * Checks that the Person knows a particular language
   * 
   * @param lang the language in question
   * @return true if the language is in the Person's repertoire
   */
  public boolean knowsLanguage(String lang)
  {
    for (int k = 0; k < _langs.size(); k++) {
      if (_langs.get(k).equalsIgnoreCase(lang)) {
        return true;
      }
    }
    return false;
  }

  /*
   * COMMON GETTERS and SETTERS for BASE CLASS and COMPONENTS 
   */

  // /* Load the Person's displayable data into the data shuttle for the given key
  // * @see mylib.pdc.ObservableModel#load(mylib.civ.List)
  // * This should only be called through the base class
  // ObservableModel.loadShuttle(ds)
  // *
  // * @param ds shuttle to receive the Person data, as requested by the keys
  // within it
  // * @return the shuttle with the loaded data
  // */
  // @Override
  // protected DataShuttle<PersonKeys> load(DataShuttle<PersonKeys> ds)
  // throws ClassCastException
  // {
  // // Load a value into the shuttle for each slot requested; ignore others
  // // Return immediately if the requested key is not found
  // for (PersonKeys key : ds.getKeys())
  // {
  // // Store each of the key's values into the shuttle
  // switch(key) {
  // case AC:
  // ds.putField(PersonKeys.AC, new Integer(_Inventory.calcAC(_acMod)));
  // break;
  // case AGE:
  // ds.putField(PersonKeys.AGE, new Double(_Race.getAgeYears()));
  // break;
  // case DESCRIPTION:
  // ds.putField(PersonKeys.DESCRIPTION, _description);
  // break;
  // case GENDER:
  // ds.putField(PersonKeys.GENDER, _Race.getGender());
  // break;
  // case GOLD:
  // Integer gold = new Integer(_Inventory.getItem("Gold").getQuantity());
  // ds.putField(PersonKeys.GOLD, gold);
  // break;
  // case GOLD_BANKED:
  // ds.putField(PersonKeys.GOLD_BANKED, new
  // Double(_Inventory.getGoldBanked()));
  // break;
  // case HAIR_COLOR: // replaced with Description
  // ds.removeKey(key);
  // ds.putField(PersonKeys.DESCRIPTION, _description);
  // break;
  // case HEIGHT:
  // ds.putField(PersonKeys.HEIGHT, new Integer(_Race.getHeight()));
  // break;
  // case HP:
  // ds.putField(PersonKeys.HP, new Integer(_Klass.getHP()));
  // break;
  // case HUNGER:
  // // Convert Hunger State to string
  // ds.putField(PersonKeys.HUNGER, _myHunger.findHungerString());
  // break;
  // case KLASSNAME:
  // ds.putField(PersonKeys.KLASSNAME, _Klass.getKlassName());
  // break;
  // case LANGUAGES:
  // ds.putField(PersonKeys.LANGUAGES, _langs);
  // break;
  // case LEVEL:
  // ds.putField(PersonKeys.LEVEL, new Integer(getLevel()));
  // break;
  // case MAX_LANGS:
  // ds.putField(PersonKeys.MAX_LANGS, new Integer(_maxLangs));
  // break;
  // case NAME:
  // ds.putField(PersonKeys.NAME, _name);
  // break;
  // case OCCUPATION:
  // ds.putField(PersonKeys.OCCUPATION, _occupation);
  // break;
  // case RACENAME:
  // ds.putField(PersonKeys.RACENAME, _Race.getRaceName());
  // break;
  // case SILVER:
  // Integer silver = new Integer(_Inventory.getItem("Silver").getQuantity());
  // ds.putField(PersonKeys.SILVER, silver);
  // break;
  // case SPEED:
  // ds.putField(PersonKeys.SPEED, new Double(calcSpeed()));
  // break;
  // case WEIGHT:
  // ds.putField(PersonKeys.WEIGHT, new Integer(_Race.getWeight()));
  // break;
  // case LOAD:
  // ds.putField(PersonKeys.LOAD, new
  // Integer(_Inventory.calcInventoryWeight()));
  // break;
  // case XP:
  // ds.putField(PersonKeys.XP, new Integer(_Klass.getXP()));
  // break;
  // default:
  // ds.setErrorType(ErrorType.MISSING_KEY);
  // ds.setErrorMessage(key.name() + List.MISSING_KEY_MSG);
  // ds.setErrorSource(key);
  // } // end of switch
  // } // end of for-loop
  // return ds;
  // }

  /*
   * Load the Person's displayable attributes, inventory, and skills into the data shuttle
   * 
   * @param ds shuttle to receive the Person data, as requested by the keys within it
   * 
   * @return the shuttle with the loaded data
   */
  public DataShuttle<PersonKeys> loadPersonData(DataShuttle<PersonKeys> ds)
  {
    ds.putField(PersonKeys.AC, new Integer(_Inventory.calcAC(_acMod)));
    ds.putField(PersonKeys.AGE, new Double(_Race.getAgeYears()));
    ds.putField(PersonKeys.DESCRIPTION, _description);
    ds.putField(PersonKeys.GENDER, _Race.getGender());

    ds.putField(PersonKeys.GOLD, new Integer(_Inventory.getItem("Gold pieces")
        .getQuantity()));
    ds.putField(PersonKeys.GOLD_BANKED,
        new Double(_Inventory.getGoldBanked()));
    ds.putField(PersonKeys.DESCRIPTION, _description);
    ds.putField(PersonKeys.HEIGHT, new Integer(_Race.getHeight()));
    ds.putField(PersonKeys.HP, new Integer(_klass.getHP()));

    // Convert Hunger State to string
    ds.putField(PersonKeys.HUNGER, _myHunger.findHungerString());
    ds.putField(PersonKeys.INVENTORY, _Inventory);
    ds.putField(PersonKeys.KLASSNAME, _klass.getKlassName());
    ds.putField(PersonKeys.LANGUAGES, _langs);

    ds.putField(PersonKeys.LITERACY, _literacy);
    ds.putField(PersonKeys.LEVEL, new Integer(getLevel()));
    ds.putField(PersonKeys.MAX_LANGS, new Integer(_maxLangs));
    ds.putField(PersonKeys.NAME, _name);
    ds.putField(PersonKeys.OCCUPATION, _Occupation);
    ds.putField(PersonKeys.RACENAME, _Race.getRaceName());

    // Convert silver from Inventory
    ds.putField(PersonKeys.SILVER, new Integer(_Inventory.getItem("Silver pieces")
        .getQuantity()));
    ds.putField(PersonKeys.SKILLS, _skills);
    ds.putField(PersonKeys.SPEED, new Double(calcSpeed()));
    ds.putField(PersonKeys.WEIGHT, new Integer(_Race.getWeight()));
    ds.putField(PersonKeys.LOAD,
        new Integer(_Inventory.calcInventoryWeight()));
    ds.putField(PersonKeys.XP, new Integer(_klass.getXP()));
    ds.putField(PersonKeys.ABILITY_SCORES, _trait);

    return ds;
  }

  /**
   * De-serialize and instantiate a Person object
   * 
   * @param filename the filename for the Person file, without the extension. The chosen file
   *        originates from the HIC to restore a particular Person, and is passed through the Person
   *        to its component <code>PersonReadWriter</code>.
   * @return false if a problem occured with the loading
   * @see PersonReadWriter
   */
  public Person loadFile(String filename)
  {
    _prw = new PersonReadWriter();
    return (Person) _prw.load(filename);
  }

  /**
   * Load a person from the database
   * 
   * @param pName the name of the person to be summon, this person is passed through the Person to
   *        its component <code>PersonReadWriter</code>.
   * @return false if a problem occured with the loading
   * @see PersonReadWriter
   */
  public Person load(String pName)
  {
    _prw = new PersonReadWriter();
    return (Person) _prw.load(pName);
  }

  /**
   * Get the names of all people in the database
   * 
   */
  public ArrayList<String> wake()
  {
    _prw = new PersonReadWriter();
    return _prw.wakePeople();
  }

  // /**
  // * Populate the data shuttle map with Hero atttibutes; the HeroKeys enums
  // * defines which of the attributes to pack. The Race and Klass components
  // * are also called so they can pack their contribution to the shuttle.
  // * All attributes are convered to Strings for transports.<br>
  // * Implementation Note: Although <code>String.valueOf()</code> will work
  // for all cases,
  // * the <code>Double.toString()</code> or <code>Integer.toString()</code>
  // methods and the like
  // * are used instead to highlight the field type being passed.
  // *
  // * @return the data shuttle containing Hero attributes
  // */
  // public EnumMap<PersonKeys, String> packPersonShuttle()
  // {
  // // Create the map of output strings
  // EnumMap<PersonKeys, String> fields =
  // new EnumMap<PersonKeys, String>(PersonKeys.class);
  //
  // // First pack the Person attributes
  // // fields.put(PersonKeys.DESCRIPTION, _description);
  // // fields.put(PersonKeys.HAIR_COLOR, _hairColor);
  // // fields.put(PersonKeys.MAX_LANGS, Integer.toString(_maxLangs));
  // // fields.put(PersonKeys.NAME, _name);
  // // fields.put(PersonKeys.OCCUPATION, _occupation);
  // // fields.put(PersonKeys.HUNGER, _myHunger.findHungerState().toString());
  // // fields.put(PersonKeys.WT_CARRIED, Double.toString(calcLoad()));
  // // fields.put(PersonKeys.LANGUAGES, getLangs());
  // // fields.put(PersonKeys.SPEED, Double.toString(calcSpeed()));
  // //
  // // // Pack the Race attributes
  // // fields = _Race.packShuttle(fields);
  // //
  // // // Pack the Klass attributes
  // // fields = _Klass.packShuttle(fields);
  //
  // // Pack the Inventory attributes
  // // fields = _Inventory.packShuttle(fields);
  // // TODO: Put these methods into the Inventory class
  // // fields = fields.put(PersonKeys.GOLD,
  // String.valueOf(_Inventory.getCash()));
  // // fields = fields.put(PersonKeys.GOLD_BANKED,
  // String.valueOf(_Inventory.getGoldBanked()));
  //
  // // Return this data shuttle back to the caller
  // return fields;
  // }

  // TODO: Move this to Inventory class. Have it pack its own shuttle
  /**
   * Pack a data shuttle with Item attributes from the Inventory.
   * 
   * @return an list of EnumMap data shuttles, one per Item
   */
  public ArrayList<EnumMap<ItemFields, String>> packItemShuttle()
  {
    int listLen = _Inventory.getNbrItems();
    // Get number of Items to know how large to make the list
    ArrayList<EnumMap<ItemFields, String>> inventory = new ArrayList<EnumMap<ItemFields, String>>(
        listLen);

    // Create the map of Item output strings
    for (int k = 0; k < listLen; k++) {
      EnumMap<ItemFields, String> shuttle = new EnumMap<ItemFields, String>(
          ItemFields.class);
      Item item = _Inventory.getItem(k);
      inventory.add(k, item.packShuttle(shuttle));
    }
    return inventory;
  }

  public boolean delete()
  {
    PersonReadWriter prw = new PersonReadWriter();
    if (prw.delete(this) == false) {
      MsgCtrl.errMsgln(this, "Error trying to delete the Person.");
      return false;
    }
    return true;
  }

  /** Wrapper for save(boolean) method */
  public boolean rename(String newName)
  {
    this._name = newName;
    return true;
  }

  /** Wrapper for save(boolean) method */
  public boolean save()
  {
    return this.save(false);
  }

  /**
   * Serialize the Person to a file. All components will be stored with it as components. The file
   * is saved in the default resource directory.
   * 
   * @param filename the filename for the Person file, without the extension. The chosen file
   *        originates from the HIC to restore a particular Person, and is passed through the Person
   *        to its component <code>PersonReadWriter</code>.
   * @return false if the save goes awry, else return true
   */
  public boolean save(boolean overwrite)
  {
    // Save Person in proper file
    if (overwrite) {
      try {
        PersonReadWriter prw = new PersonReadWriter();
        if (prw.overwrite(this, _name) == false) {
          MsgCtrl.msgln(this, "Save failed using overwrite flag.");
          return false;
        }
      } catch (Exception ex) {
        MsgCtrl.errMsgln(this,
            "Error trying to save the Person to file.");
        MsgCtrl.errMsgln(
            this,
            "Exception thrown while attempting to save: "
                + ex.getMessage());
      }
    } else {
      try {
        PersonReadWriter prw = new PersonReadWriter();
        if (prw.save(this, _name) == false) {
          MsgCtrl.msgln(this, "Save failed without overwrite flag.");
          return false;
        }
      } catch (Exception ex) {
        System.err.println("Error trying to save the Person to file.  " + ex.getMessage());
        MsgCtrl.errMsgln(
            this,
            "Exception thrown while attempting to save: "
                + ex.getMessage());
      }
    }
    return true;
  }

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ COMMON GETTERS and SETTERS for
   * BASE CLASS and COMPONENTS ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /*
   * Unloads the already-validated data from the shuttle, indicated by the key, and casts it into
   * this object's attributes
   * 
   * @param ds contains the data to load into this object
   * 
   * @see mylib.pdc.ObservableModel#unload(mylib.civ.List)
   * 
   * @throws ClassCastException if the value cannot be cast properly during validation
   */
  public DataShuttle<PersonKeys> unload(DataShuttle<PersonKeys> ds)
      throws ClassCastException
  {
    // Load a value into the shuttle for each slot requested; ignore others
    // Return immediately if the requested key is not found
    // for (PersonKeys key : ds.getKeys()) {
    // Object value = ds.getField(key);
    // // Every key must have valid data of the right type to be saved in
    // // the model, else error
    // // There should be no keys without data
    // if (value == null) {
    // ds.setErrorType(ErrorType.FIELD_INVALID);
    // ds.setErrorMessage(key.name());
    // ds.setErrorSource(key);
    // break;
    // } else {
    // switch (key) {
    // case NAME:
    // _name = ds.getField(PersonKeys.NAME).toString();
    // break;
    // case GENDER:
    // _gender = ds.getField(PersonKeys.GENDER).toString();
    // break;
    // case HAIR_COLOR:
    // _hairColor = ds.getField(PersonKeys.HAIR_COLOR)
    // .toString();
    // break;
    // case KLASSNAME:
    // ds.putField(PersonKeys.KLASSNAME, _klass.getKlassName());
    // break;
    // case OCCUPATION:
    // ds.putField(PersonKeys.OCCUPATION, _Occupation);
    // break;
    // case RACENAME:
    // ds.putField(PersonKeys.RACENAME, _Race.getRaceName());
    // break;
    // case XP:
    // ds.putField(PersonKeys.XP, _klass.getXP());
    // break;
    // default:
    // ds.setErrorType(ErrorType.MISSING_FIELD);
    // ds.setErrorMessage(key.name());
    // ds.setErrorSource(key);
    // } // end of switch
    // } // end of if-else
    // } // end of for-loop
    // Return shuttle in case it contains errors
    return ds;

  } // end of unload() method

  /*
   * Unloads the already-validated data from the shuttle, indicated by the key, and casts it into
   * this object's attributes
   * 
   * @param ds contains the data to load into this object
   * 
   * @see mylib.pdc.ObservableModel#unload(mylib.civ.List)
   * 
   * @throws ClassCastException if the value cannot be cast properly during validation
   */
  // @Override
  // protected void unload(DataShuttle<PersonKeys> ds)
  // throws ClassCastException
  // {
  // // Load a value into the shuttle for each slot requested; ignore others
  // // Return immediately if the requested key is not found
  // for (PersonKeys key : ds.getKeys())
  // {
  // Object value = ds.getField(key);
  // // Every key must have valid data of the right type to be saved in the
  // model, else error
  // // There should be no keys without data
  // if (value == null) {
  // ds.setErrorType(ErrorType.FIELD_INVALID);
  // ds.setErrorMessage(key.name());
  // ds.setErrorSource(key);
  // break;
  // }
  // else {
  // switch(key) {
  // case NAME:
  // _name = ds.getField(PersonKeys.NAME).toString();
  // break;
  // case GENDER:
  // _gender = ds.getField(PersonKeys.GENDER).toString();
  // break;
  // case HAIR_COLOR:
  // _hairColor = ds.getField(PersonKeys.HAIR_COLOR).toString();
  // break;
  // case OCCUPATION:
  // ds.putField(PersonKeys.OCCUPATION, _occupation);
  // break;
  // case RACENAME:
  // ds.putField(PersonKeys.RACENAME, _Race.getRaceName());
  // break;
  // case XP:
  // ds.putField(PersonKeys.XP, _Klass.getXP());
  // break;
  // default:
  // ds.setErrorType(ErrorType.MISSING_FIELD);
  // ds.setErrorMessage(key.name());
  // ds.setErrorSource(key);
  // } // end of switch
  // } // end of if-else
  // } // end of for-loop
  // } // end of unload() method

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Private methods
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */
  /**
   * Calculate the speed of movement for the Person, which depends on Action Points and weight
   * carried. The Racial base movement is adjusted by the Inventory weight carried and pro-rated
   * against the Person's weight allowance.
   * 
   * // * @param ap Action Points // * @param wtAllow the weight that can be carried before
   * encumberance
   * 
   * @param load the load being carried
   * @return the Person's speed per round, (2 to 5 generally if not encumbered)
   */
  public double calcSpeed()
  {
    double speed = 0.0; // stopped
    _Race.calcWeightAllowance(_trait.get(ATTRIBUTE.STR));
    // Find the encumberance of the Person, if any
    double enc = calcEncumberance();
    // Find the default speed for this Person
    speed = ((double) _Race.calcBaseMovement(_ap)) * enc;
    return speed;
  }

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ Private methods
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /**
   * Check if the first letter of a string is a vowel so the proper article (A or An) can be placed
   * in front of it.
   * 
   * @param target the string to check if the first letter is a vowel or not
   * @return true if a the target starts with a vowel; else false
   */
  private boolean checkFirstVowel(String target)
  {
    final Character[] c = {'a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O',
        'U'};
    boolean retflag = false;

    for (int k = 0; k < c.length; k++) {
      if (target.charAt(0) == c[k]) {
        retflag = true;
      }
    }
    return retflag;
  }

  /**
   * Build the physical appearance of the Person, without regard to what they are wearing or
   * anything that can drastically change. The description depends on height, weight, race, klass,
   * hair color, gender and charisma. A special racial note is appended to all races except Human. <br>
   * NOTE: Although the description is actually a user-interface attribute, it is generated and
   * saved in the Person object for expedience to avoid the high overhead of calculating it.
   * <P>
   * Template for the attributes in description: <br>
   * "A [height_descriptor] and [weight_descriptor]" + "[gender] with [color] hair" +
   * "and [racial note]". [She | He] is [CHR reflection]". <br>
   * 
   * @return a string that describes the Person's body-type (a Race function).
   */
  private String initDescription()
  {
    // Infer body-type from racial attributes of height, weight, and
    // charisma
    String bodyType = _Race.initBodyType(_trait.get(ATTRIBUTE.CHR));
    // Start the description with a vowel-sensitive article
    String article = (checkFirstVowel(bodyType) == true) ? "An " : "A ";
    // Determine proper gender for descriptive statement
    String pronoun = _Race.getGender().equalsIgnoreCase(Race.FEMALE) ? "She"
        : "He";

    // Process baldness.
    String hairType = (_hairColor.equalsIgnoreCase("bald")) ? "no hair"
        : _hairColor + " hair";
    String desc1 = article + bodyType + " " + getGender().toLowerCase()
        + " with " + hairType;

    // Get race descriptor for suffix.
    String desc2 = _Race.initRaceDescriptor();

    // Get Charisma description
    String chrDesc = _Race
        .initCharismaDescriptor(_trait.get(ATTRIBUTE.CHR));
    String desc3 = pronoun + " is " + chrDesc + ".";

    String desc = desc1 + " and " + desc2 + ". " + desc3;
    return desc;
  }

  /**
   * Assign the languages natural to the Person. All Persons know the Common language, and non-human
   * races might also have a race language. Klass languages are assigned during Guild activity, and
   * not during (Peasant) initialization
   * 
   * @return an arrayList containing the language(s) intialized for this Person
   */
  private List<String> initLanguages()
  {
    String newLang = null;
    // Create the language list
    if (_langs.contains("Common") == false) {
      // Add Common to each Person's reportoire of languages
      _langs.add("Common");
    }
    // Add racial language to the Person's reportoire of languages, if any
    newLang = _Race.getLanguage();
    if ((newLang != null) && (_langs.contains(newLang) == false)) {
      _langs.add(newLang);
    }
    return _langs;
  }

  /**
   * Add the literacy skill based on intelligence. Anyone who can write, can also read.
   * 
   * @param intelligence of the person, determines literacy skill
   * @return read-only, read/write, or illiterate; or null if there was a problem
   */
  private MiscKeys.Literacy initLiteracy(int intelligence)
  {
    // Guard against foolish input parms
    if (intelligence <= 0) {
      return null;
    }
    if (intelligence < MIN_READ_INT) {
      _literacy = MiscKeys.Literacy.ILLITERATE;
    } else if ((intelligence >= MIN_READ_INT)
        && (intelligence < MIN_WRITE_INT)) {
      _literacy = MiscKeys.Literacy.READING;
    } else if (intelligence >= MIN_WRITE_INT) {
      _literacy = MiscKeys.Literacy.WRITING;
    }
    return _literacy;
  }

  /**
   * Calculate the number of learnable languages for the Person, depending on his INT. NOTE:
   * Learning a language means that the Hero can only speak and understand it. He or she must have
   * the proper Intelligence to be Literate and be able to read and/or write it.
   * 
   * @return the number of learnable languages for this Person
   */
  private int initMaxLangs()
  {
    // This algorithm ensures that no languages are learnable for INT < 8,
    // and the value is positive.
    // Common is factored in as the 1 minimum value for maxLangs.
    return Math.max(_trait.get(ATTRIBUTE.INT) / 2 - 3, 1);
  }

  /**
   * Call the initial skill set assignments. Any existing skill list is cleared.
   * 
   * @return the new skills for this Person
   */
  private List<Skill> initSkills()
  {
    _skills.clear();
    // Get the skill associated with the person's occupation
    Skill s = _Occupation.getSkill();
    _skills.add(s);
    // Append the skills associated with the person's Race and return
    _skills = _Race.assignSkills(_skills);
    // Append the skills associated with the person's Klass and return
    _skills = _klass.assignSkills(_skills);
    return _skills;
  }

  /*
   * INNER CLASS: MockPerson
   */

  /** Inner class for testing Person */
  public class MockPerson
  {
    /** Default constructor */
    public MockPerson()
    {}

    /** Clear the Inventory for certain kinds of testing */
    public void clearInventory()
    {
      Person.this._Inventory = null;
    }

    /** Clear the language list and related attributes for testing */
    public void clearLanguages()
    {
      _langs = null;
      _maxLangs = Constants.UNASSIGNED;
    }

    /**
     * Get the person's hair color for testing
     * 
     * @return hair color
     */
    public String getHairColor()
    {
      return _hairColor;
    }

    /**
     * Get the current number of known languages
     * 
     * @return language number
     */
    public int getNbrOfCurrentLangs()
    {
      return _langs.size();
    }

    /**
     * Person's with higher STR are more likely to hit with a melee (non-missile) weapon.
     * 
     * @return the adjustment to hit an opponent with a melee weapon
     */
    public int getToHitMelee()
    {
      return _toHitMeleeMod;
    }

    /**
     * Person's with higher STR can get damage easier when they hit with a melee (non-missile)
     * weapon.
     * 
     * @return the adjustment to hit an opponent with a melee weapon
     */
    public int getDamageMod()
    {
      return _damageMod;
    }

    /**
     * Person's with higher STR can carry more weight
     * 
     * @return the adjustment to weight that can be carried
     */
    public int getWeightAllowance()
    {
      return _wtAllow;
    }

    // /** Get the current number of skills the person has
    // * @return number of skills
    // */
    // public int getNbrOfSkills()
    // {
    // return _skills.size();
    // }

    /**
     * Test initLanguages. Overrides private scope for Person object.
     * 
     * @return list of known languages
     */
    public List<String> initLanguages()
    {
      return Person.this.initLanguages();
    }

    /**
     * Test the total number of languages the Person can know. Common is always known, so maxLangs
     * is a minimum of 1.
     * 
     * @param intelligence of Person
     * @return max number of languages Person can know
     */
    public int initMaxLangs(int intelligence)
    {
      _trait.put(ATTRIBUTE.INT, intelligence);
      return Person.this.initMaxLangs();
    }

  } // end of MockPerson inner class

} // end of Person class

