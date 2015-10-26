/*
 * Dwarf.java
 * Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package pdc.character;

import java.io.Serializable;
import java.util.List;

import mylib.ApplicationException;
import mylib.MsgCtrl;
import chronos.Chronos.ATTRIBUTE;
import chronos.pdc.AttributeList;
import chronos.pdc.Race;
import chronos.pdc.Skill;

/**
 * The <code>Dwarf</code> is a hearty and stout Hero. Dwarves tend to to make
 * excellent Fighters because of their naturally high constitution. Dwarves have
 * the following differences from Humans: <BL> <LI >Tend to be shorter and
 * smaller than Humans</LI> <LI >Tend to slightly higher Constitution, slightly
 * lower Charisma</LI> <LI >Racial language in addition to Common: Grokken</LI>
 * <LI >High resistance to magic</LI> <LI >Special abilities: Infravision = 60',
 * and</LI> <LI><i> Geasing</i>: Detecting hidden or subtle differences in
 * stonework construction and traps, including knowing depth and direction when
 * underground</LI> </BL>
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Jan 2 2010 // original
 *          <DD>
 *          <DT>Build 1.1 May 15 2011 // TAA updated some methods for skills
 *          </DL>
 */
@SuppressWarnings("serial")
public class Dwarf extends Race implements Serializable {
    /** Average height (inches) for a Dwarf male */
    private final int AVG_HEIGHT_MALE = 48;
    /** Average height (inches) for a Dwarf female */
    private final int AVG_HEIGHT_FEMALE = 46;

    /** Average weight (pounds) for a Dwarf male */
    private final int AVG_WEIGHT_MALE = 150;
    /** Average weight (pounds) for a Dwarf female */
    private final int AVG_WEIGHT_FEMALE = 120;

    /** The minimum Trait values for a Dwarf: STR, INT, WIS, DEX, CON, CHR */
    private final int[] _minTraits = { 8, 8, 8, 8, 9, 7 };
    /** The maximum Trait values for a Dwarf: STR, INT, WIS, DEX, CON, CHR */
    private final int[] _maxTraits = { 18, 18, 18, 18, 19, 17 };

    /** The oldest ages in each of the Dwarf age brackets */
    private final int[] _dwarfAgeBracket = { 50, 150, 250, 350, 450 };

    /** The name of the this Race */
    private final String RACE_NAME = "Dwarf";
    /** All Dwarves have beards, even the females */
    private final String RACE_DESCRIPTOR = "a scraggly beard";
    /** The names of the special abilities that come with being a dwarf */
    private final String[] dwarfSkills = { "Infravision", "Geasing" };

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
     * CONSTRUCTOR(S) AND RELATED METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Empty constructor for testing. This should not be called except by the
     * Person factory
     */
    public Dwarf() {
        super._raceName = RACE_NAME;
    }

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
     * PROTECTED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    // /** Verify that the traits stay within the race limits, else revise them.
    // * The derived class data is passed to the super method as a parm so only
    // one method
    // * need be written
    // *
    // * @param traitList the six traits will be returned within the allowed
    // range
    // */
    // protected int[] verifyTraits(int[] traitList)
    // {
    // return super.verifyTraits(traitList, _minTraits, _maxTraits);
    // }

    /*
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
     * PRIVATE SUPPORT METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /** Assign the skills that the Dwarf has */
    public List<Skill> assignSkills(List<Skill> inSkills) {
        for (int i = 0; i < dwarfSkills.length; i++) {
            String skillName = dwarfSkills[i];
            Skill skill = Skill.getSkill(skillName);
            inSkills.add(skill);
        }
        return inSkills;
    }

    /** Return min values for traits */
    public int[] getTraitMin(){
    	return _minTraits;
    }
    
    /** Return max values for traits */
    public int[] getTraitMax(){
    	return _maxTraits;
    }

    /**
     * Dwarves get an extra CON but lose a CHR point
     * 
     * @param traits
     *            unadjusted prime traits
     * @return newly adjusted prime traits
     */
    public AttributeList adjTraitsForRace(AttributeList traits) {
        if (traits == null) {
            MsgCtrl.errMsgln("ERROR: Null traits were received.");
        } else if (traits.size() < 6) {
            MsgCtrl.errMsgln("ERROR: Traits were formatted improperly");
        } else {
            traits.put(ATTRIBUTE.CON, traits.get(ATTRIBUTE.CON) + 1);
            traits.put(ATTRIBUTE.CHR, traits.get(ATTRIBUTE.CHR) - 1);
        }
        return traits;
    }

    /**
     * Dwarfs have a few special Racial skills. The names are looked up, and the
     * Skill objects are assigned.
     * 
     * @param skillList
     *            target list to which this method will add new skills
     * @return the skillList updated with dwarf skills
     */
    // protected ArrayList<Skill> assignRacialSkills(ArrayList<Skill> skillList)
    // {
    // return super.assignSkills(skillList, dwarfSkills);
    // }

    // TODO: Move to CIV
    // /** Retrive the specific category for the Dwarf's age. See Race class for
    // list.
    // *
    // * @return one of the standard age categories
    // */
    // static public String getAgeCategory()
    // {
    // String cat = null;
    // // Find what age the Dwarf category falls into
    // try {
    // cat = Age.calcAgeCategory(_currentAge, _dwarfAgeBracket);
    // }
    // catch (ChronosException ex) {
    // MsgCtrl.errMsg(this, ex.getMessage());
    // }
    // return cat;
    // }

    /**
     * All Dwarfs start out at the same age. Set the newbie's racial starting
     * age, and call the super method to set the current age in seconds. Each
     * age value is a pre-defined constant, and used for trait adjustment as
     * Person changes age brackets.
     * 
     * @return current starting age of Dwarf in years
     */
    public long initAge() {
        return super.initAge((long) _dwarfAgeBracket[0]);
    }

    /**
     * Initalize the height of the Dwarf with a range of plus/minus 32% of
     * average. Save the value as a base class attribute.
     * 
     * @return height of Dwarf in inches
     * @throws ChronosException
     *             if the defined Dwarf heights are invalid
     */
    public int initHeight() {
        // String gender = super._gender;
        // MetaDie rnd = super._random;
        // Tertiary operator for if/else clause
        // super._height = (gender.equalsIgnoreCase(Race.MALE)) ?
        // (int) rnd.getGaussian((double)AVG_HEIGHT_MALE) :
        // (int) rnd.getGaussian((double)AVG_HEIGHT_FEMALE);
        initHeight(AVG_HEIGHT_MALE, AVG_HEIGHT_FEMALE);
        return super._height;
    }

    /**
     * Selects a word to describe the person's appearance based on height. This
     * method provides subclass specific values and calls the base class helper
     * method to get the matching description for the characteristic range for
     * the Person.
     * 
     * @param height
     *            Person's height in inches
     * @return description for the attribute, else null on error
     */
    protected String initHeightDescriptor(double height) {
        return super.initHeightDescriptor(height, AVG_HEIGHT_MALE,
                AVG_HEIGHT_FEMALE);
    }

    /**
     * Dwarfs have a few special Racial skills. The names are looked up, and the
     * Skill objects are assigned.
     * 
     * @param skillList
     *            target list to which this method will add new skills
     * @return the skillList updated with dwarf skills
     */
    // protected ArrayList<Skill> assignRacialSkills(ArrayList<Skill> skillList)
    // {
    // return super.assignSkills(skillList, dwarfSkills);
    // }

    // TODO: Move to CIV
    // /** Retrive the specific category for the Dwarf's age. See Race class for
    // list.
    // *
    // * @return one of the standard age categories
    // */
    // static public String getAgeCategory()
    // {
    // String cat = null;
    // // Find what age the Dwarf category falls into
    // try {
    // cat = Age.calcAgeCategory(_currentAge, _dwarfAgeBracket);
    // }
    // catch (ChronosException ex) {
    // MsgCtrl.errMsg(this, ex.getMessage());
    // }
    // return cat;
    // }

    /**
     * Retrieve the description suffix that is particular to the Race involved.
     * 
     * @return the suffix
     */
    public String initRaceDescriptor() {
        return super._descriptor = RACE_DESCRIPTOR;
    }

    /**
     * Initialize the weight of the Dwarf with a range of plus/minus 16% of
     * average. Save the value as a base class attribute.
     * 
     * @return weight of Dwarf in pounds
     */
    public int initWeight() {
        initWeight(AVG_WEIGHT_MALE, AVG_WEIGHT_FEMALE);
        return super._weight;
    }

    /**
     * Selects a word to describe the person's appearance based on weight. This
     * method provides subclass specific values and calls the base class helper
     * method to get the matching description for the characteristic range for
     * the Person.
     * 
     * @param weight
     *            Person's weight in pounds
     * @return description for the attribute, else null on error
     */
    protected String initWeightDescriptor(double weight) {
        return super.initWeightDescriptor(weight, AVG_WEIGHT_MALE,
                AVG_WEIGHT_FEMALE);
    }

    // /** Selects a word to describe the person's appearance based on height.
    // This method
    // * provides subclass specific values and calls the base class helper
    // method to get the
    // * matching description for the characteristic range for the Person.
    // *
    // * @param height Person's height in inches
    // * @return description for the attribute, else null on error
    // */
    // protected String initHeightDescriptor(double height)
    // {
    // // Possible descriptors, from short to tall
    // final String[] htDesc = {"tiny", "short", "average-height", "tall",
    // "towering"};
    //
    // // determine legal ranges
    // double minValue = 0;
    // double maxValue = 0;
    // if (super._gender.equalsIgnoreCase(Race.FEMALE)) {
    // minValue = (double) Math.round(AVG_HEIGHT_FEMALE * (1.0 -
    // MetaDie.HALF_SIGMA));
    // maxValue = (double) Math.round(AVG_HEIGHT_FEMALE * (1.0 +
    // MetaDie.HALF_SIGMA));
    // }
    // else {
    // minValue = (double) Math.round(AVG_HEIGHT_MALE * (1.0 -
    // MetaDie.HALF_SIGMA));
    // maxValue = (double) Math.round(AVG_HEIGHT_MALE * (1.0 +
    // MetaDie.HALF_SIGMA));
    // }
    // // Call the baseclass's generic helper method
    // String desc = findRangeDescriptor(height, minValue, maxValue, htDesc);
    // return desc;
    // }

    // /** Selects a word to describe the person's appearance based on weight.
    // This method
    // * provides subclass specific values and calls the base class helper
    // method to get the
    // * matching description for the characteristic range for the Person.
    // *
    // * @param weight Person's weight in pounds
    // * @return description for the attribute, else null on error
    // */
    // protected String initWeightDescriptor(double weight)
    // {
    // // Possible descriptors, from light to heavy
    // final String[] wtDesc = {"skinny", "thin", "medium-weight", "heavy",
    // "bulky"};
    //
    // // determine legal ranges
    // double minValue = 0;
    // double maxValue = 0;
    // if (super._gender.equalsIgnoreCase(Race.FEMALE)) {
    // minValue = (double) Math.round(AVG_WEIGHT_FEMALE * (1.0 -
    // MetaDie.SIGMA));
    // maxValue = (double) Math.round(AVG_WEIGHT_FEMALE * (1.0 +
    // MetaDie.SIGMA));
    //
    // }
    // else {
    // minValue = (double) Math.round(AVG_WEIGHT_MALE * (1.0 - MetaDie.SIGMA));
    // maxValue = (double) Math.round(AVG_WEIGHT_MALE * (1.0 + MetaDie.SIGMA));
    //
    // }
    // // Call the baseclass's generic helper method
    // String desc = findRangeDescriptor(weight, minValue, maxValue, wtDesc);
    // return desc;
    // }

    /**
     * Dwarves get an additional +1 adjustment for each 4 points of CON
     * 
     * @param defMAA
     *            default magic attack adjustment (race-independent)
     * @param con
     *            used to calculate the bonus
     * @return final magic attack adjustment
     */
    public int updateMagicAttackAdj(int defMAA, int con) {
        super._magicAttackMod = defMAA + con / 4;
        return super._magicAttackMod;
    }

    /**
     * Verify that the traits stay within the race limits, else revise them. The
     * derived class data is passed to the super method as a parm so only one
     * method need be written
     * 
     * @param traitList
     *            the six traits will be returned within the allowed range
     * @throws ChronosException
     *             if trait list is null or lists are of different sizes
     */
    @Override
    public AttributeList verifyTraits(AttributeList traitList)
            throws ApplicationException {
        return super.verifyTraits(traitList, _minTraits, _maxTraits);
    }

    /*
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE
     * SUPPORT METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    // TODO: Move to CIV
    // /** Retrive the specific category for the Dwarf's age. See Race class for
    // list.
    // *
    // * @return one of the standard age categories
    // */
    // static public String getAgeCategory()
    // {
    // String cat = null;
    // // Find what age the Dwarf category falls into
    // try {
    // cat = Age.calcAgeCategory(_currentAge, _dwarfAgeBracket);
    // }
    // catch (ChronosException ex) {
    // MsgCtrl.errMsg(this, ex.getMessage());
    // }
    // return cat;
    // }

    /**
     * Set the raclai language for the Dwarf
     * 
     * @return null
     */
    public String getLanguage() {
        return super._raceLanguage = "Dwarven";
    }

    /** Accesses and tests the private methods of the Person object. */
    public class MockDwarf {
        /** Default constructor */
        public MockDwarf() {}

        /**
         * Accessor for private method
         * 
         * @throws ChronosException
         */
        public AttributeList adjustTraitsForRace(AttributeList traits)
                throws ApplicationException {
            return Dwarf.this.adjTraitsForRace(traits);
        }

        /**
         * Ensure that no race skills are added but infraDistance is set to 0
         * 
         * @param skillList
         *            set of skills the Person has from other initilizations
         * @return original skillList
         */
        // public ArrayList<Skill> assignRacialSkills(ArrayList<Skill>
        // skillList)
        // {
        // return Dwarf.this.assignRacialSkills(skillList);
        // }

        /**
         * Calculate the height of the Dwarf with a range of plus/minus 16% of
         * average. Save the value as a base class attribute.
         * 
         * @return height of Dwarf in inches
         */
        public int calcHeight() {
            return Dwarf.this.initHeight();
        }

        /**
         * Calculate the weight of the Dwarf with a range of plus/minus 16% of
         * average. Save the value as a base class attribute.
         * 
         * @return weight of Dwarf in pounds
         */
        public int calcWeight() {
            return Dwarf.this.initWeight();
        }

        /** Return the race name */
        public String getRaceName() {
            return Dwarf.this.getRaceName();
        }

        /*
         * Test current age and age bracket
         * 
         * @return currentAge of Dwarf in seconds
         */
        public long initAge() {
            return Dwarf.this.initAge();
        }

        // TODO: Move to CIV
        /*
         * Verify correct adjective for given height
         * 
         * @param height of the Person
         * 
         * @param gender of the person to return different values for testing
         * 
         * @return word that describes the person in terms of height
         */
        public String initHeightDescriptor(double height, String gender) {
            setGender(gender);
            return Dwarf.this.initHeightDescriptor(height);
        }

        // TODO: Move to CIV
        /*
         * Verify correct adjective for given weight
         * 
         * @param weight of the Person
         * 
         * @param gender of the person to return different values for testing
         * 
         * @return word that describes the person in terms of height
         */
        public String initWeightDescriptor(double weight, String gender) {
            setGender(gender);
            return Dwarf.this.initWeightDescriptor(weight);
        }

        public List<Skill> assignRacialSkills(List<Skill> inSkills) {
            // TODO Auto-generated method stub
            return Dwarf.this.assignSkills(inSkills);
        }

    } // end of MockDwarf inner class

} // end of Dwarf class