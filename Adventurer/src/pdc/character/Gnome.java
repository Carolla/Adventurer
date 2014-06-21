/*
 * Gnome.java
 * Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package pdc.character;

import pdc.registry.AdvRegistryFactory;

import chronos.Chronos.ATTRIBUTE;
import chronos.pdc.AttributeList;
import chronos.pdc.Race;
import chronos.pdc.Skill;
import chronos.pdc.registry.RegistryFactory.RegKey;
import chronos.pdc.registry.SkillRegistry;

import mylib.ApplicationException;
import mylib.MsgCtrl;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The <code>Gnome</code> is a swarthy and mischevious little guy. Gnome tends
 * to make excellent Thieves because of their short and often unnoticed stature.
 * Gnomes have the following differences from Humans: <BL> <LI >Tend to be
 * shorter and smaller than Humans</LI> <LI >Racial language in addition to
 * Common: Gnomen</LI> <LI >Slightly higher resistance to magic</LI> <LI >
 * Special abilities: Infravision = 60' and Geasing</LI> </BL>
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 7 April 2012 // original
 *          <DD>
 *          </DL>
 */
@SuppressWarnings("serial")
public class Gnome extends Race implements Serializable {
    /** Average height (inches) for a Gnome male */
    private final int AVG_HEIGHT_MALE = 42;
    /** Average height (inches) for a Gnome female */
    private final int AVG_HEIGHT_FEMALE = 39;

    /** Average weight (pounds) for a Gnome male */
    private final int AVG_WEIGHT_MALE = 80;
    /** Average weight (pounds) for a Gnome female */
    private final int AVG_WEIGHT_FEMALE = 75;

    /** The minimum Trait values for a Gnome: STR, INT, WIS, DEX, CON, CHR */
    private final int[] _minTraits = { 7, 8, 8, 8, 9, 8 };
    /** The maximum Trait values for a Gnome: STR, INT, WIS, DEX, CON, CHR */
    private final int[] _maxTraits = { 16, 18, 18, 18, 19, 19 };

    /** The oldest ages in each of the Gnome age brackets (see DMG, p13) */
    private final int[] _gnomeAgeBracket = { 90, 300, 450, 600, 750 };

    /** The name of the this Race */
    private final String RACE_NAME = "Gnome";
    /** The lanugage of this Race */
    private final String RACE_LANGUAGE = "Gnomen";
    /** Elves have pointed ears */
    private final String RACE_DESCRIPTOR = "piercing blue eyes";

    /** The names of the special abilities that come with being a dwarf */
    private final String[] _gnomeSkills = { "Infravision", "Geasing" };
    /** Define specifics for certain skills */
    private final int INFRA_DISTANCE = 60;

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR(S) AND
     * RELATED METHODS ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Empty constructor for testing. This should not be called except by the
     * Person factory
     */
    public Gnome() {
        super._raceName = RACE_NAME;
    }

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PROTECTED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /** Assign the skills that the Gnome has */
    public ArrayList<Skill> assignSkills(ArrayList<Skill> inSkills) {
        Skill skill = new Skill();
        SkillRegistry skreg = (SkillRegistry) AdvRegistryFactory.getRegistry(RegKey.SKILL);
        // skreg.load();
        for (int i = 0; i < _gnomeSkills.length; i++) {
            String skillName = _gnomeSkills[i];
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
    public AttributeList adjTraitsForRace(AttributeList traits) {
        if (traits == null) {
            MsgCtrl.errMsgln("ERROR: Null traits were received.");
        } else if (traits.size() < 6) {
            MsgCtrl.errMsgln("ERROR: Traits were formatted improperly");
        } else {
            traits.put(ATTRIBUTE.CON, traits.get(ATTRIBUTE.CON) + 1);
            traits.put(ATTRIBUTE.DEX, traits.get(ATTRIBUTE.DEX) + 1);
        }
        return traits;
    }

    /**
     * Gnomes have a few special Racial skills. The names are looked up, and the
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
    // /** Retrive the specific category for the Gnome's age. See Race class for
    // list.
    // *
    // * @return one of the standard age categories
    // */
    // static public String getAgeCategory()
    // {
    // String cat = null;
    // // Find what age the Gnome category falls into
    // try {
    // cat = Age.calcAgeCategory(_currentAge, _gnomeAgeBracket);
    // }
    // catch (ChronosException ex) {
    // MsgCtrl.errMsg(this, ex.getMessage());
    // }
    // return cat;
    // }

    /**
     * All Gnomes start out at the same age. Set the newbie's racial starting
     * age, and call the super method to set the current age in seconds. Each
     * age value is a pre-defined constant, and used for trait adjustment as
     * Person changes age brackets.
     * 
     * @return current starting age of Gnome in years
     */
    public long initAge() {
        return super.initAge((long) _gnomeAgeBracket[0]);
    }

    /**
     * Initalize the height of the Gnome with a range of plus/minus 16% of
     * average. Save the value as a base class attribute.
     * 
     * @return height of Gnome in inches
     * @throws ChronosException
     *             if the defined Gnome heights are invalid
     */
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
     * Initialize the weight of the Gnome with a range of plus/minus 16% of
     * average. Save the value as a base class attribute.
     * 
     * @return weight of Gnome in pounds
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
     * Gnomes get an additional +1 adjustment for each 4 points of CON
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
    // /** Retrive the specific category for the Gnome's age. See Race class for
    // list.
    // *
    // * @return one of the standard age categories
    // */
    // static public String getAgeCategory()
    // {
    // String cat = null;
    // // Find what age the Gnome category falls into
    // try {
    // cat = Age.calcAgeCategory(_currentAge, _dwarfAgeBracket);
    // }
    // catch (ChronosException ex) {
    // MsgCtrl.errMsg(this, ex.getMessage());
    // }
    // return cat;
    // }

    /**
     * Set the raclai language for the Gnome
     * 
     * @return null
     */
    public String getLanguage() {
        return super._raceLanguage = RACE_LANGUAGE;
    }

    /** Accesses and tests the private methods of the Person object. */
    public class MockGnome {
        /** Default constructor */
        public MockGnome() {}

        /**
         * Accessor for private method
         * 
         * @throws ChronosException
         */
        public AttributeList adjustTraitsForRace(AttributeList traits)
                throws ApplicationException {
            return Gnome.this.adjTraitsForRace(traits);
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
        // return Gnome.this.assignRacialSkills(skillList);
        // }

        /**
         * Calculate the height of the Gnome with a range of plus/minus 16% of
         * average. Save the value as a base class attribute.
         * 
         * @return height of Gnome in inches
         */
        public int calcHeight() {
            return Gnome.this.initHeight();
        }

        /**
         * Calculate the weight of the Gnome with a range of plus/minus 16% of
         * average. Save the value as a base class attribute.
         * 
         * @return weight of Gnome in pounds
         */
        public int calcWeight() {
            return Gnome.this.initWeight();
        }

        /** Return the race name */
        public String getRaceName() {
            return Gnome.this.getRaceName();
        }

        /*
         * Test current age and age bracket
         * 
         * @return currentAge of Gnome in seconds
         */
        public long initAge() {
            return Gnome.this.initAge();
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
            return Gnome.this.initHeightDescriptor(height);
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
            return Gnome.this.initWeightDescriptor(weight);
        }

        public ArrayList<Skill> assignRacialSkills(ArrayList<Skill> inSkills) {
            // TODO Auto-generated method stub
            return Gnome.this.assignSkills(inSkills);
        }

    } // end of MockGnome inner class

} // end of Gnome class