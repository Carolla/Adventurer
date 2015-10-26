/*
 * Hobbit.java
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
import java.util.ArrayList;
import java.util.List;

import mylib.ApplicationException;
import mylib.MsgCtrl;
import chronos.Chronos.ATTRIBUTE;
import chronos.pdc.AttributeList;
import chronos.pdc.Race;
import chronos.pdc.Skill;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import chronos.pdc.registry.SkillRegistry;

/**
 * The <code>Hobbit</code> is a stout and loyal friend. Hobbit tends to make
 * excellent Thieves because of their silent movement. Hobbits have the
 * following differences from Humans: <BL> <LI >Tend to be shorter, weaker but
 * more dexterous</LI> <LI >Racial language in addition to Common: Tolkeen</LI>
 * <LI >Slightly higher resistance to poison</LI> <LI >Special abilities:
 * Infravision = 30' and some Geasing</LI> </BL>
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 7 April 2012 // original
 *          <DD>
 *          </DL>
 */
@SuppressWarnings("serial")
public class Hobbit extends Race implements Serializable {
    /** Average height (inches) for a Hobbit male */
    private final int AVG_HEIGHT_MALE = 36;
    /** Average height (inches) for a Hobbit female */
    private final int AVG_HEIGHT_FEMALE = 33;

    /** Average weight (pounds) for a Hobbit male */
    private final int AVG_WEIGHT_MALE = 60;
    /** Average weight (pounds) for a Hobbit female */
    private final int AVG_WEIGHT_FEMALE = 50;

    /** The minimum Trait values for a Hobbit: STR, INT, WIS, DEX, CON, CHR */
    private final int[] _minTraits = { 7, 8, 8, 9, 8, 8 };
    /** The maximum Trait values for a Hobbit: STR, INT, WIS, DEX, CON, CHR */
    private final int[] _maxTraits = { 17, 18, 18, 19, 18, 18 };

    /** The oldest ages in each of the Hobbit age brackets (see DMG, p13) */
    private final int[] _hobbitAgeBracket = { 33, 68, 101, 144, 199 };

    /** The name of the this Race */
    private final String RACE_NAME = "Hobbit";
    /** The lanugage of this Race */
    private final String RACE_LANGUAGE = "Tolkeen";
    /** Elves have pointed ears */
    private final String RACE_DESCRIPTOR = "hairy bare feet";

    /** The names of the special abilities that come with being a dwarf */
    private final String[] _hobbitSkills = { "Infravision", "Geasing",
            "Resistance to Poison" };
    /** Define specifics for certain skills */
    private final int INFRA_DISTANCE = 30;

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR(S) AND
     * RELATED METHODS ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Empty constructor for testing. This should not be called except by the
     * Person factory
     */
    public Hobbit() {
        super._raceName = RACE_NAME;
    }

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
     * PROTECTED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /** Assign the skills that the Hobbit has */
    public List<Skill> assignSkills(List<Skill> inSkills) {
        Skill skill = new Skill();
        SkillRegistry skreg = (SkillRegistry) RegistryFactory.getInstance().getRegistry(RegKey.SKILL);
        // skreg.load();
        for (int i = 0; i < _hobbitSkills.length; i++) {
            String skillName = _hobbitSkills[i];
            skill = skreg.getSkill(skillName);
            inSkills.add(skill);
        }
        super._infraDistance = INFRA_DISTANCE;
        return inSkills;
    }

    /*
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE
     * SUPPORT METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /** Return min values for traits */
    public int[] getTraitMin(){
    	return _minTraits;
    }
    
    /** Return max values for traits */
    public int[] getTraitMax(){
    	return _maxTraits;
    }

    /**
     * Elves get an extra CON and an extra DEX point
     * 
     * @param traits
     *            unadjusted prime traits
     * @return newly adjusted prime traits
     */
    @Override
    public AttributeList adjTraitsForRace(AttributeList traits) {
        if (traits == null) {
            MsgCtrl.errMsgln("ERROR: Null traits were received.");
        } else if (traits.size() < 6) {
            MsgCtrl.errMsgln("ERROR: Traits were formatted improperly");
        } else {
            traits.put(ATTRIBUTE.STR, traits.get(ATTRIBUTE.STR) - 1);
            traits.put(ATTRIBUTE.DEX, traits.get(ATTRIBUTE.DEX) - 1);
        }
        return traits;
    }

    /**
     * All Hobbits start out at the same age. Set the newbie's racial starting
     * age, and call the super method to set the current age in seconds. Each
     * age value is a pre-defined constant, and used for trait adjustment as
     * Person changes age brackets.
     * 
     * @return current starting age of Hobbit in years
     */
    public long initAge() {
        return super.initAge((long) _hobbitAgeBracket[0]);
    }

    /**
     * Initalize the height of the Hobbit with a range of plus/minus 16% of
     * average. Save the value as a base class attribute.
     * 
     * @return height of Hobbit in inches
     * @throws ChronosException
     *             if the defined Hobbit heights are invalid
     */
    @Override
    public int initHeight() {
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
     * Retrieve the description suffix that is particular to the Race involved.
     * 
     * @return the suffix
     */
    public String initRaceDescriptor() {
        return super._descriptor = RACE_DESCRIPTOR;
    }

    /**
     * Initialize the weight of the Hobbit with a range of plus/minus 16% of
     * average. Save the value as a base class attribute.
     * 
     * @return weight of Hobbit in pounds
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
     * Hobbits get an additional +1 adjustment for each 4 points of CON
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
    // /** Retrive the specific category for the Hobbit's age. See Race class
    // for list.
    // *
    // * @return one of the standard age categories
    // */
    // static public String getAgeCategory()
    // {
    // String cat = null;
    // // Find what age the Hobbit category falls into
    // try {
    // cat = Age.calcAgeCategory(_currentAge, _dwarfAgeBracket);
    // }
    // catch (ChronosException ex) {
    // MsgCtrl.errMsg(this, ex.getMessage());
    // }
    // return cat;
    // }

    /**
     * Set the raclai language for the Hobbit
     * 
     * @return null
     */
    public String getLanguage() {
        return super._raceLanguage = RACE_LANGUAGE;
    }

    /** Accesses and tests the private methods of the Person object. */
    public class MockHobbit {
        /** Default constructor */
        public MockHobbit() {}

        /**
         * Accessor for private method
         * 
         * @throws ChronosException
         */
        public AttributeList adjustTraitsForRace(AttributeList traits)
                throws ApplicationException {
            return Hobbit.this.adjTraitsForRace(traits);
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
        // return Hobbit.this.assignRacialSkills(skillList);
        // }

        /**
         * Calculate the height of the Hobbit with a range of plus/minus 16% of
         * average. Save the value as a base class attribute.
         * 
         * @return height of Hobbit in inches
         */
        public int calcHeight() {
            return Hobbit.this.initHeight();
        }

        /**
         * Calculate the weight of the Hobbit with a range of plus/minus 16% of
         * average. Save the value as a base class attribute.
         * 
         * @return weight of Hobbit in pounds
         */
        public int calcWeight() {
            return Hobbit.this.initWeight();
        }

        /** Return the race name */
        public String getRaceName() {
            return Hobbit.this.getRaceName();
        }

        /*
         * Test current age and age bracket
         * 
         * @return currentAge of Hobbit in seconds
         */
        public long initAge() {
            return Hobbit.this.initAge();
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
            return Hobbit.this.initHeightDescriptor(height);
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
            return Hobbit.this.initWeightDescriptor(weight);
        }

        public List<Skill> assignRacialSkills(List<Skill> inSkills) {
            // TODO Auto-generated method stub
            return Hobbit.this.assignSkills(inSkills);
        }

    } // end of MockHobbit inner class

} // end of Hobbit class