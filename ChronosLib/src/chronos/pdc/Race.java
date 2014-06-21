/**
 * Race.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc;

import java.io.Serializable;
import java.util.ArrayList;

import mylib.ApplicationException;
import mylib.Constants;
import mylib.MsgCtrl;
import mylib.pdc.MetaDie;
import chronos.Chronos;
import chronos.Chronos.ATTRIBUTE;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import chronos.pdc.registry.SkillRegistry;

/**
 * Defines the common methods and attributes for all Races; <code>Human</code>
 * is the default Race. A concrete derived subclass of <code>Race</code> is one
 * of the major components of the Person object. <br>
 * NOTE: This class is serializable, so contains transients and statics that
 * will not be stored.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Feb 14 2009 // original
 *          <DD>
 *          <DT>Build 1.1 Feb 28 2009 // modifed after Race and Klass re-design
 *          <DD>
 *          <DT>Build 1.2 Mar 5 2009 // modifed for serialization
 *          <DD>
 *          <DT>Build 1.3 Mar 15 2009 // removed ctor and added gender accessor
 *          <DD>
 *          <DT>Build 1.4 Oct 9 2009 // add satiety attributes
 *          <DD>
 *          <DT>Build 2.0 Jan 18 2010 // add Dwarf support
 *          <DD>
 *          <DT>Build 2.1 Jul 5 2010 // support for CIV and data shuttle
 *          <DD>
 *          <DT>Build 2.2 Sep 26 2010 // modified slightly for QA tag suport
 *          <DD>
 *          <DT>Build 2.3 Nov 14 2010 // pulled out any methods that were not
 *          part of creating a Person and are part of running the Person during
 *          the game, e.g, Hunger and Age adjustments
 *          <DD>
 *          <DT>Build 2.4 May 15 2011 // TAA reinserted skill methods, fixed
 *          errors
 *          <DD>
 *          <DT>Build 2.5 March 25 2013 // TAA added
 *          <DD>
 *          </DL>
 */
public abstract class Race implements Serializable
{
    /** Recommended serialization constant. */
    static final long serialVersionUID = 1200L;
    /** All subclass Races are assumed to be in the current package */
    static private final String RACE_PACKAGE = "pdc.character.";

    // METADATA TO SUPPORT CALCULATIONS AND METHODS
    /** Gender is a race attribute, more specific than Person */
    static public final String MALE = "Male";
    /** Gender is a race attribute, more specific than Person */
    static public final String FEMALE = "Female";

    // Hyphens must be removed from hybird races form for Java classes
    private static final String RACE_PREFIX = "Half-";

    /**
     * Define the kinds of races a Hero might be (first one is default): Human,
     * Dwarf, Elf, Gnome, Half-Elf, Half-Orc, and Hobbit.
     */
    static public final String[] _races = { "Human", "Dwarf", "Elf", "Gnome",
            "Half-Elf", "Half-Orc", "Hobbit" };

    static private ArrayList<String> _raceList = null;
    /** Convert the static array to an ArrayList */
    static {
        _raceList = new ArrayList<String>(_races.length);
        for (int k = 0; k < _races.length; k++) {
            _raceList.add(k, _races[k]);
        }
    }

    /**
     * Some things apply across all Races, e.g. Body Type descriptors. The
     * following height and weight ranges are dubbed "standard" (human) because
     * what is "short" and "tall" is a human perspective.
     */
    transient protected final double STD_MIN_HEIGHT = 54;
    transient protected final double STD_MAX_HEIGHT = 70;
    transient protected final double STD_MIN_WEIGHT = 110;
    transient protected final double STD_MAX_WEIGHT = 175;

    /** Block Movement mod constants: for each MOD points, an extra movement */
    transient private final int MVMT_MOD = 8;
    /** Block Movement minimum */
    transient private final int MIN_MVMT = 1;
    /** Block Movement maxiumum */
    transient private final int MAX_MVMT = 5;
    /** Movement penalty for short Persons */
    transient private final int PENALTY_HEIGHT = 48;
    /** Movement bonus for tall Persons */
    transient private final int BONUS_HEIGHT = 78;

    // RACE-SPECIFIC ATTRIBUTES
    /** Name of the Race subclass, e.g., Human, Hobbit, or Elf */
    protected String _raceName = null;
    /** "Male" is the default; else "Female": affects weight and height. */
    protected String _gender = null;
    /**
     * Each race (except Human) has a race language; half-breed races, like
     * half-elf, have a 50% chance at a racial language.
     */
    protected String _raceLanguage = null;
    /** Description suffix that is specific to each race */
    protected String _descriptor = null;

    /** The current age of the Person in seconds. */
    protected long _currentAge = 0L;
    /** The height (in inches) of the Person, depends on race and gender */
    protected int _height = Constants.UNASSIGNED;
    /** The weight (in pounds) of the Person, depends on race and gender */
    protected int _weight = Constants.UNASSIGNED;

    /**
     * Distance (feet) that this Person can see in the dark using infrared
     * vision
     */
    protected int _infraDistance = Constants.UNASSIGNED;

    /** Movement speed is calculated from DEX and STR traits. */
    protected int _baseMovement = Constants.UNASSIGNED;

    /** Ajustment to defense against magical attack; set from WIS trait and Race */
    protected int _magicAttackMod = Constants.UNASSIGNED;

    /**
     * Adjustment to chance To Hit with a missile weapon (speed of aim), based
     * on DEX
     */
    protected int _toHitMissileMod = Constants.UNASSIGNED;

    /**
     * Reference to random generator (MetaDie object) for some of the
     * calculations
     */
    transient protected MetaDie _random = null;

    /*
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * ABSTRACT METHODS, IMPLEMENTED IN SUBCLASSES
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /** Adjust the traits of the subclass based on its age brackets */
    // abstract protected int[] adjTraitsForRace(int[] unadjustedTraits);

    abstract public AttributeList adjTraitsForRace(
            AttributeList unadjustedTraits);

    /** Set the Race subclass skills of the Person */
    abstract public ArrayList<Skill> assignSkills(ArrayList<Skill> skillList);

    /** Add the specific race language to the Person's list */
    abstract public String getLanguage();

    /**
     * Each subclass Person starts at the same Race-dependent age. Age changes
     * with time in the Dungeon, particularly after Guild graduation and during
     * promotion. The current age of the Person is initialized in units of seconds.
     * 
     * @see p13 of the Dungeon Masters' Guide.
     */
    abstract public long initAge();

    /** Retrieve the description suffix that is particular to the Race involved, */
    abstract public String initRaceDescriptor();

    /** Calculate the height of the Person, depending on specific Race. */
    abstract public int initHeight();

    /** Calculate the weight of the Person, depending on specific Race. */
    abstract public int initWeight();

    /** Adjust the default magic attack value for racial factors */
    abstract public int updateMagicAttackAdj(int defMagicAttackAdj, int con);
    
//    int[] _minTraits = {8, 8, 8, 8, 8, 8};
//    int[] _maxTraits = {18, 18, 18, 18, 18, 18};
    
    /** Return subrace trait minimums */
    abstract public int[] getTraitMin();
    
    /** Return subrace trait maximums */
    abstract public int[] getTraitMax();

    /**
     * Verify that the specified traits stay within their race limits, else
     * adjust errant values
     * 
     * @throws ChronosException
     */
    abstract public AttributeList verifyTraits(AttributeList _trait)
            throws ApplicationException;

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
     * CONSTRUCTOR AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Race is an abstract class, and all subclasses must extend Race: Human,
     * Dwarf, Elf, Gnome, Half-Elf, Half-Orc, and Hobbit.
     */
    public Race() {
        _random = new MetaDie();
    }

    /**
     * Creates a specific subclass of Race based on its class name (String).
     * This method is the Race object factory.
     * 
     * @param raceName
     *            the name of the subclass to be created
     * @return Race, the subclass created, but referenced polymorphically; else
     *         null
     */
    static public Race createRace(String raceName)
    {
        Race newRace = null;
        try {
            // Subclass Commands must have empty constructors (no formal input
            // arguments)
            String racePath = RACE_PACKAGE + raceName;
            newRace = (Race) Class.forName(racePath).newInstance();

        } catch (Exception e) {
            MsgCtrl.errMsgln("\tRace.createRace(): Cannot find class requested: " + e.getMessage());
        }
        return newRace;
    }

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     * SUPPORTS CONCRETE CLASSES
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Get the possible list of Race subclasses the user can play
     * 
     * @return Race's subclass name
     */
    static public ArrayList<String> getRaceTypes()
    {
        return _raceList;
    }

    /**
     * Checks if a passed string is contained in the race list.
     * 
     * @param name
     *            of Race to confirm
     * @return Is true if the string is found in the race list
     */
    public static boolean isRace(String name)
    {
        return _raceList.contains(name);
    }

    /**
     * Removes the hyphen for Half-breeds
     * 
     * @param name
     *            of Race to confirm
     * @return Nonhyphenated name
     */
    public static String trueRace(String name)
    {
        if (name.contains(RACE_PREFIX)) {
            int index = RACE_PREFIX.length() - 1;
            // Remove the hyphen
            String s2 = name.substring(0, index) + name.substring(index + 1);
            name = s2;
        }
        return name;
    }

    /**
     * Adjust the Prime Traits based on gender, regardless of race. Gender
     * affects Race-based weight and height, so is included in the Race class. <br>
     * Algorithm: Males are default, so no changes are made to the traits.
     * Females are given CON+1, CHR +1, STR-1
     * 
     * @param traitList
     *            the six traits will be overwritten with the new ones
     * @return new trait list; else null for no gender or no input parm
     */

    public AttributeList adjTraitsForGender(AttributeList traitList)
    {
        // Guard against traits not derived yet
        if (traitList == null) {
            MsgCtrl.errMsgln("Traits not yet rolled");
            return traitList;
        }
        // No adjustment for males
        if (_gender.equalsIgnoreCase(MALE)) {
            return traitList;
        }
        // Adjust for female; no adjustment for males
        else if (_gender.equalsIgnoreCase(FEMALE)) {
            traitList.put(ATTRIBUTE.STR, traitList.get(ATTRIBUTE.STR) - 1);
            traitList.put(ATTRIBUTE.CON, traitList.get(ATTRIBUTE.CON) + 1);
            traitList.put(ATTRIBUTE.CHR, traitList.get(ATTRIBUTE.CHR) + 1);
        }
        // Invalid or missing gender
        else {
            MsgCtrl.errMsgln(this, "Person's gender not specified");
        }
        return traitList;
    }

    /**
     * Update the given skill list with any racial skills
     * 
     * @param skillList
     *            target list to which this method will add new skills
     * @return the updated skillList
     */
    protected ArrayList<Skill> assignSkills(ArrayList<Skill> skillList,
            String[] racialList)
    {
        // Ensure that there are racial skills to add; if not, no changes
        if (racialList.length == 0) {
            return skillList;
        }

        // Get the collection of all skills for lookup
        SkillRegistry skReg = (SkillRegistry) RegistryFactory.getRegistry(RegKey.SKILL);
        for (int k = 0; k < racialList.length; k++) {
            Skill skill = skReg.getSkill(racialList[k]);
            if (skill != null) {
                skillList.add(skill);
            }
            else {
                MsgCtrl.errMsgln(this, "Skill " + racialList[k] + " could not be found");
            }
        }
        return skillList;
    }

    /**
     * This is called by the subclass, so that the age reflects the
     * particular race;
     * adjustments are based on the gender, age category, and verified against
     * permitted Racial limits.
     * 
     * @param ageCat adjust Person by which age-category they are in
     * @param defValues the initial Race-based, pre-Guild traits to adjust for
     *            Race
     * @return the modifed trait list
     */
    protected AttributeList adjTraitsForRace(Age.CATEGORY ageCat, AttributeList defValues)
    {
        AttributeList traits = adjTraitsForGender(defValues);
        traits = Age.adjTraitsForAge(ageCat, traits);
        traits = verifyTraits(traits);
        return traits;
    }

    /**
     * Calculate the base movement speed, based on Action Points (AP = STR+DEX)
     * and height. Movement penalties based on encumbrance (weight carried and
     * weight allowed) are calculated elsewhere. See
     * <code>Person.calcMovement()</code> where movement is adjusted for
     * encumberance.
     * <P>
     * ALGORITHM: For each MVMT_MOD Action Points, the Person can move one block
     * movement (about 1.0 feet per second). Movement is constrained between
     * MIN_MVMT (down to 1) and MAX_MVMT (up to 5) for a 10 second interval.
     * There is a +1/-1 height adjustment for extra tall/short Persons after the
     * original base calculation, although no Person's movement can be less than
     * 1 (unless encumbered by too much weight carried). <br>
     * 
     * @param ap
     *            action points used for table lookup
     * @return movement calculated
     */
    public int calcBaseMovement(int ap)
    {
        // Guard against AP not derived yet
        if (ap < 1) {
            MsgCtrl.errMsgln("AP not yet assigned");
            return ap;
        }
        int baseMovement = Constants.UNASSIGNED;

        // Calculate basic block movement before height adjustments
        int base = (ap / MVMT_MOD) + 1;
        baseMovement = (base < MIN_MVMT) ? MIN_MVMT : base;
        baseMovement = (base > MAX_MVMT) ? MAX_MVMT : base;

        // Adjust for movement variance due to height, but never below 1
        if (_height <= PENALTY_HEIGHT) {
            if (baseMovement > 1) {
                baseMovement--;
            }
        }
        // Adjust for movement variance due to height, and can be MAX_MVMT +1
        else if (_height >= BONUS_HEIGHT) {
            baseMovement++;
        }
        return baseMovement;
    }

    /**
     * Find how much weight the Person can carry. WeightAllowance depends on the
     * Person's weight. It assums that an average Person (STR=11.5) can carry
     * their own weight without being encumbered; others are pro-rated by STR.
     * 
     * @param str
     *            strength of the Person
     * @return weight allowance (in ounces); else zero for invalid input
     */
    public int calcWeightAllowance(int str)
    {
        // Guard
        if ((str <= Chronos.COMA_TRAIT_VALUE) || (_weight <= 0)) {
            MsgCtrl.errMsgln(this, "Invalid parm values");
            return 0;
        }
        // Weight allowance is a pro-rated calculation, not a table lookup
        double wtStrength = str / Chronos.AVERAGE_TRAIT;
        double lbAllow = wtStrength * _weight;
        int canCarry = (int) (lbAllow * Constants.OUNCES_PER_POUND);
        return canCarry;
    }

    /**
     * Finds the body-type for the individual by calculating which percentile
     * (<i>N-tile</i>) that Person is in. N-tile is calculated by dividing the
     * range by N, where N is the number of categories desired (usually N is the
     * number of descriptions available). The ranges are broken down into N
     * number of subdivisions: div[0] = from MIN_RANGE to div[1], div[1] <=
     * div[2], ... div[N] to MAX_RANGE. <br>
     * This method is called to find a Person's weight or height N-tiles.
     * 
     * @param value
     *            the Person's specific value to be applied within the range
     * @param minValue
     *            the smallest possible value for the range of the population
     * @param maxValue
     *            the largest possible value for the range of the population
     * @param descriptors
     *            the population of specific descriptions to use for range; The
     *            number of the descriptor array determines the N to calculate
     *            the N-tile.
     * @return the string that corresponds to the value within the segmented
     *         range specified by maxValue and minValue
     */
    protected String findRangeDescriptor(double value, double minValue,
            double maxValue, String[] descriptors)
    {
        // Guards
        if ((value < minValue) || (value > maxValue)) {
            MsgCtrl.errMsgln("Race.findRangeDescriptor: Illegal value and range entered.");
            return null;
        }
        if ((descriptors == null) || (descriptors.length <= 0)) {
            MsgCtrl.errMsgln("Race.findRangeDescriptor: No descriptions entered.");
            return null;
        }
        // Create an equal sized array of percentiles
        int nbrDivisions = descriptors.length; // need one more fencepost for
                                               // fence
        double range = maxValue - minValue;
        double divSize = range / (double) nbrDivisions;
        double[] div = new double[nbrDivisions + 1];
        // Segment the range into equal parts
        div[0] = minValue;
        for (int k = 1; k <= nbrDivisions; k++) {
            div[k] = div[k - 1] + divSize;
        }
        // the last division should equal the maxValue for the range; if it is
        // too far off,
        // then report an error
        if ((div[nbrDivisions] - maxValue) > .01) {
            MsgCtrl.errMsgln("Race.findRangeDescriptor(): "
                    + "End of Range inconsistent with calculation.");
            return null;
        }
        // Find which division the value falls into
        int divNo = 0;
        boolean fell = true;
        int k = 0;

        // Traverse increasing descriptor list
        for (k = 0; k <= nbrDivisions; k++) {
            if (value > div[k]) {
                continue;
            } else {
                // Protect against negative index value
                divNo = Math.max(0, k - 1); // kth bucket, but k-1 index when
                                            // 0-based
                fell = false;
                break;
            }
        }
        // Check if fell out of loop, or broke out
        if (fell == true) {
            divNo = nbrDivisions - 1;
        }
        // Get descriptor for that division
        return descriptors[divNo];
    }

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ GETTERS AND
     * SETTERS ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Return the Person's age (internal format: seconds)
     * 
     * @return age
     */
    public double getAge()
    {
        return _currentAge;
    }

    /**
     * Return the Person's age (in years)
     * 
     * @return age
     */
    public double getAgeYears()
    {
        // seconds to years conversion
        double yrsAge = (double) _currentAge / (double) Constants.SECS_PER_YEAR;
        return yrsAge;
    }

    // /** Return the category that describes the Person's age
    // * @return age descriptor
    // */
    // public String getAgeCategory()
    // {
    // return _ageCategory;
    // }

    /**
     * Return either male or female Person
     * 
     * @return gender
     */
    public String getGender()
    {
        return _gender;
    }

    /**
     * How tall is the Person?
     * 
     * @return the Person's height, in inches
     */
    public int getHeight()
    {
        return _height;
    }

    // /**How hungry is the Person?
    // * @return the hunger flag, from FULL to STARVED
    // */
    // public String getHungerState()
    // {
    // return _hungerFlag;
    // }

    /**
     * How far can the Person see in the dark, with infravision?
     * 
     * @return the Person's infravision distance (in feet)
     */
    public int getInfraDistance()
    {
        return _infraDistance;
    }

    /**
     * What is the Person's resistance bonus (or penalty) to magical attack?
     * 
     * @return the magic attack adjustment value
     */
    public int getMagicAttackMod()
    {
        return _magicAttackMod;
    }

    // /** How fast can the Person run?
    // * @return the Person's movement factor
    // */
    // public int getMvmt()
    // {
    // return _baseMovement;
    // }

    /**
     * What is the name of the Person's Race?
     * 
     * @return Race's subclass name
     */
    public String getRaceName()
    {
        return _raceName;
    }

    /**
     * Return the ToHit bonus/penalty for missile weapons
     * 
     * @return To Hit Adj for Missile weapson
     */
    public int getToHitMissileMod()
    {
        return _toHitMissileMod;
    }

    /**
     * How much does the Person weigh?
     * 
     * @return the Person's weight, in pounds
     */
    public int getWeight()
    {
        return _weight;
    }

    /**
     * All Persons start out near the top of their Race's youngest age range.
     * Set the newbie's current age in seconds.
     * 
     * @param startingAge
     *            for the Race subclass in years
     * @return starting age of Character in seconds
     */
    protected long initAge(long startingAge)
    {
        _currentAge = (long) (startingAge * Age.STARTING_AGE_ADJ * (double) Constants.SECS_PER_YEAR);
        return _currentAge;
    }

    /**
     * Associate the height and weight of the character with their Charisma to
     * get a body type descriptor. For this implementation, height and weight
     * are broken into only three cartegories.
     * 
     * @param charisma
     *            body types are perceived as favorable or unfavorable depending
     *            on the Person's CHR
     * @return what the person looks like for their Charisma trait
     */
    public String initBodyType(int charisma)
    {
        // Guards: ensure that weight and height have been defined
        if ((_height == Constants.UNASSIGNED)
                || (_weight == Constants.UNASSIGNED)) {
            MsgCtrl.errMsgln("Race.initBodyType(): height or weight not yet assigned");
            return null;
        }

        // Index for body type table: Light, Average, Heavy map to 0, 1, 2 index
        // values

        // Possible descriptors for positive charismas in a Height x Weight
        // matrix,
        // must be in increasing order to call findRangeDescriptor
        final String[][] posBody = {
                // Light Average Heavy
                { "petite", "compact", "burly" }, // Short height
                { "lithe", "athletic", "muscular" }, // Average height
                { "thin", "tall", "towering" }, // Tall height
        };
        // Possible descriptors for negative charismas in a Height x Weight
        // matrix,
        // must be in increasing order when calling findRangeDescriptor()
        final String[][] negBody = { { "tiny", "pudgy", "squat" }, // Short
                                                                   // height
                { "slinky", "average-size", "heavy" }, // Average height
                { "skinny", "tall", "giant" } // Tall height
        };

        // Find which list to use
        String[][] descrChoice = ((double) charisma >= Chronos.AVERAGE_TRAIT) ? posBody
                : negBody;

        // Find if Person is heavy, average, or light
        // String rowNbr = findRangeDescriptor(_weight, STD_MIN_WEIGHT,
        // STD_MAX_WEIGHT, indexes);
        // Find which category the height is in
        int rowNbr = -1;
        if (_height <= STD_MIN_HEIGHT) {
            rowNbr = 0;
        } else if (_height >= STD_MAX_HEIGHT) {
            rowNbr = 2;
        } else {
            rowNbr = 1;
        }

        // Find which category the weight is in
        int colNbr = -1;
        if (_weight <= STD_MIN_WEIGHT) {
            colNbr = 0;
        } else if (_weight >= STD_MAX_WEIGHT) {
            colNbr = 2;
        } else {
            colNbr = 1;
        }
        String descr = descrChoice[rowNbr][colNbr];
        return descr;
    }

    /**
     * Associate the Charisma of the character with their attractiveness, a
     * simple string matching algorithm to a description table.
     * 
     * @param charisma
     *            the Person's prime trait for making friends and negotiating
     * @return what the person looks like for their Charisma trait
     */
    public String initCharismaDescriptor(int charisma)
    {
        // Possible descriptors for charismas in increasing order,
        // Ranges from CHR=8 to CHR=18 are normal; CHR=7 and CHR=19 are
        // exceptional and probably never occur
        final String[] _chrDescs = {
                "crippled and horribly ugly", // < 8
                "horribly scarred",
                "scarred from war or fire",
                "the result of years of misery", // 8-10
                "weather-beaten and tough",
                "nothing special to look at", // 11-12
                "clear-eyed and rugged but not handsome", // 13
                "slightly attractive if one could scrape off the years of wear and tear", // 14
                "a handsome adventurer", "gorgeous",
                "very attactive", // 15-17
                "stunningly beautiful", // 18
                "mesmerizing, and you will do whatever this person suggests to you", // > 18
        };

        String deschr = null;
        // Find if Person is ugly, average, or beautiful (brute force lookup)
        // Check for exception cases first, before calling generic routine
        if (charisma < Chronos.LOW_TRAIT) {
            deschr = _chrDescs[0];
        } else if (charisma > Chronos.HIGH_TRAIT) {
            deschr = _chrDescs[_chrDescs.length - 1];
        } else {
            deschr = _chrDescs[charisma - Chronos.LOW_TRAIT + 1];
        }
        return deschr;
    }

    /**
     * Selects a word to describe the person's appearance based on weight.
     * 
     * @param weight
     *            Person's specific weight in inches
     * @param maleWt
     *            Average weight of males of the derviced race
     * @param femaleWt
     *            Average weight of female of the derived race
     * @return description for the attribute, else null on error
     */
    protected String initWeightDescriptor(double weight, int maleWt,
            int femaleWt)
    {
        // Possible descriptors, from light to heavy
        final String[] wtDesc = { "skinny", "thin", "medium-weight", "heavy",
                "bulky" };

        // determine legal ranges
        double minValue = 0;
        double maxValue = 0;
        if (_gender.equalsIgnoreCase(Race.FEMALE)) {
            minValue = femaleWt * Chronos.MIN_MULTIPLIER;
            maxValue = femaleWt * Chronos.MAX_MULTIPLIER;
        } else {
            minValue = maleWt * Chronos.MIN_MULTIPLIER;
            maxValue = maleWt * Chronos.MAX_MULTIPLIER;
        }
        // Call the baseclass's generic helper method
        String desc = findRangeDescriptor(weight, minValue, maxValue, wtDesc);
        return desc;
    }

    /**
     * Selects a word to describe the person's appearance based on height.
     * 
     * @param height
     *            Person's specific height in inches
     * @param maleHt
     *            Average height of males of the derviced race
     * @param femaleHt
     *            Average height of female of the derived race
     * @return description for the attribute, else null on error
     */
    protected String initHeightDescriptor(double height, int maleHt,
            int femaleHt)
    {
        // Possible descriptors, from short to tall
        final String[] htDesc = { "tiny", "short", "average-height", "tall",
                "towering" };

        // determine legal ranges
        double minValue = 0;
        double maxValue = 0;
        if (_gender.equalsIgnoreCase(Race.FEMALE)) {
            minValue = femaleHt * Chronos.MIN_MULTIPLIER;
            maxValue = femaleHt * Chronos.MAX_MULTIPLIER;
        } else {
            minValue = maleHt * Chronos.MIN_MULTIPLIER;
            maxValue = maleHt * Chronos.MAX_MULTIPLIER;
        }
        // Call the baseclass's generic helper method
        String desc = findRangeDescriptor(height, minValue, maxValue, htDesc);
        return desc;
    }

    /**
     * One-time calculation to get the (randomly-based) height of this race,
     * based on male and female averages. Height has a narrow variance, so
     * HALF_SIGMA is used.
     * 
     * @param maleHt
     *            average height of a male of the derived race
     * @param femaleHt
     *            average height of a female of the derived race
     * @return the randomly calculated height for the Person's gender
     */
    protected int initHeight(int maleHt, int femaleHt)
    {
        String gender = _gender;
        MetaDie rnd = _random;
        int low;
        int high;
        double tmpHeight;
        try {
            tmpHeight = gender.equalsIgnoreCase(Race.MALE) ? maleHt : femaleHt;
            low = (int) Math.round(tmpHeight * (1.0 - MetaDie.HALF_SIGMA));
            high = (int) Math.round(tmpHeight * (1.0 + MetaDie.HALF_SIGMA));
            _height = rnd.getGaussian(tmpHeight, low, high);
        } catch (ApplicationException ex) {
            MsgCtrl.errMsgln(this, "Invalid racial height received");
        }
        return _height;
    }

    /**
     * One-time calculation to get the (randomly-based) weight of this race,
     * based on male and female averages. Weight has a variance of one standard
     * deveiation, so SIGMA is used.
     * 
     * @param maleWt
     *            average weight of a male of the derived race
     * @param femaleWt
     *            average weight of a female of the derived race
     * @return the randomly calculated weight for the Person's gender
     */
    protected int initWeight(int maleWt, int femaleWt)
    {
        String gender = _gender;
        MetaDie rnd = _random;
        int low;
        int high;
        double tmpWeight;
        try {
            tmpWeight = gender.equalsIgnoreCase(Race.MALE) ? maleWt : femaleWt;
            low = (int) Math.round(tmpWeight * (1.0 - MetaDie.HALF_SIGMA));
            high = (int) Math.round(tmpWeight * (1.0 + MetaDie.HALF_SIGMA));
            _weight = rnd.getGaussian(tmpWeight, low, high);
        } catch (ApplicationException ex) {
            MsgCtrl.errMsgln(this, "Invalid racial weight received");
        }
        return _weight;
    }

    // /** Pack the Race-specific fields into a data shuttle for display *
    // *
    // * @param shuttle enum values, as Strings, to hold the data
    // * @return the data shuttle
    // */
    // public EnumMap<PersonFields, String> packShuttle(
    // EnumMap<PersonFields, String> fields)
    // {
    // // fields.put(PersonFields.AGE, String.valueOf(_currentAge));
    // fields.put(PersonFields.GENDER, _gender);
    // // fields.put(PersonFields.HEIGHT, String.valueOf(_height));
    // // fields.put(PersonFields.HUNGER, String.valueOf(_currentSatiety));
    // fields.put(PersonFields.RACENAME, _raceName);
    // // fields.put(PersonFields.SPEED, String.valueOf(_weight));
    // // fields.put(PersonFields.WEIGHT, String.valueOf(_weight));
    // return fields;
    // }

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ GETTERS AND
     * SETTERS ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Set the gender of the Person
     * 
     * @param gender
     *            male or female
     */
    public void setGender(String gender)
    {
        _gender = gender;
    }

    /**
     * Update the default HP Adj by the Person's DEX. There is a Race factor for
     * all Elves
     * 
     * @param defAdj
     *            default Adj from new Person
     * @param dex
     *            dexterity of the Person, not used for this base method
     * @return the adjusted value after it is set
     */
    public int updateToHitMissileAdj(int defAdj, int dex)
    {
        return _toHitMissileMod = defAdj;
    }

    /**
     * Verify that the traits stay within the race limits, else revise them.
     * Traits are STR, INT, WIS, DEX, CON, and CHR
     * 
     * @param traits
     *            the actual traits to be verifed for this Person
     * @param minList
     *            list of minimum values allowed for traits of derived Race
     * @param maxList
     *            list of maximum values allowed for traits of derived Race
     * @return final set of traits, revised if necessary
     * @throws ChronosException
     *             if trait list is null or lists are of different sizes
     */
    protected AttributeList verifyTraits(AttributeList traits,
            AttributeList minList, AttributeList maxList)
            throws ApplicationException
    {
        // Guard against no traits input
        if ((traits == null) || (traits.size() == 0)) {
            throw new ApplicationException("Personal traits null or invalid");
        }
        // Guard against mismatched arrays
        if ((traits.size() != minList.size())
                || (traits.size() != maxList.size())
                || (minList.size() != maxList.size())) {
            throw new ApplicationException("Trait lists are not same length");
        }
        for (ATTRIBUTE att : ATTRIBUTE.values()) {
            traits.put(att,
                    (traits.get(att) < minList.get(att)) ? minList.get(att)
                            : traits.get(att));
            traits.put(att,
                    (traits.get(att) > maxList.get(att)) ? maxList.get(att)
                            : traits.get(att));
        }
        return traits;
    }

    public AttributeList verifyTraits(AttributeList traits, int[] minList,
            int[] maxList) throws ApplicationException
    {

        // Guard against no traits input
        if ((traits == null) || (traits.size() == 0)) {
            throw new ApplicationException("Personal traits null or invalid");
        }
        // Guard against mismatched arrays
        if ((traits.size() != minList.length)
                || (traits.size() != maxList.length)
                || (minList.length != maxList.length)) {
            throw new ApplicationException("Trait lists are not same length");
        }
        int count = 0;
        for (ATTRIBUTE att : ATTRIBUTE.values()) {
            traits.put(att, (traits.get(att) < minList[count]) ? minList[count]
                    : traits.get(att));
            traits.put(att, (traits.get(att) > maxList[count] ? maxList[count]
                    : traits.get(att)));
            count++;
        }
        return traits;
    }
} // end of abstract Race class
