/*
 * Half-Elf.java
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
import mylib.pdc.MetaDie;
import chronos.pdc.AttributeList;
import chronos.pdc.Race;
import chronos.pdc.Skill;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import chronos.pdc.registry.SkillRegistry;

/**
 * The <code>Half-Elf</code> is a mixed breed of human and elf. Half-Elves have
 * the following differences from Humans: <BL> <LI >Tend to be shorter and
 * smaller than Humans</LI> <LI >Racial language in addition to Common: 50%
 * chance of knowing Elvish</LI> <LI >High resistance to Charm and Sleep spells</LI>
 * <LI >Special abilities: Infravision = 60' and Tingling</LI> </BL>
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 7 April 2012 // original
 *          <DD>
 *          </DL>
 */
@SuppressWarnings("serial")
public class HalfElf extends Race implements Serializable {
    /** Average height (inches) for a Half-Elf male */
    private final int AVG_HEIGHT_MALE = 66;
    /** Average height (inches) for a Half-Elf female */
    private final int AVG_HEIGHT_FEMALE = 62;

    /** Average weight (pounds) for a Half-Elf male */
    private final int AVG_WEIGHT_MALE = 130;
    /** Average weight (pounds) for a Half-Elf female */
    private final int AVG_WEIGHT_FEMALE = 100;

    /** The minimum Trait values for a Half-Elf: STR, INT, WIS, DEX, CON, CHR */
    private final int[] _minTraits = { 8, 8, 8, 8, 8, 8 };
    /** The maximum Trait values for a Half-Elf: STR, INT, WIS, DEX, CON, CHR */
    private final int[] _maxTraits = { 18, 18, 18, 18, 18, 18 };

    /**
     * The oldest ages in each of the Half-Elf age brackets (see DMG, p13; use
     * Drow elf table)
     */
    private final int[] _helfAgeBracket = { 40, 100, 175, 250, 325 };

    /** The name of the this Race */
    private final String RACE_NAME = "Half-Elf";
    /** The language of this Race */
    private final String RACE_LANGUAGE = "Elvish";
    /** The chance of this Half-Elf knowing his/her native language */
    private final int LANGUAGE_CHANCE = 50;
    /** Elves have pointed ears */
    private final String RACE_DESCRIPTOR = "somewhat pointed ears";

    /** The names of the special abilities that come with being a dwarf */
    private final String[] _helfSkills = { "Infravision",
            "Resistance to Sleep", "Resistance to Charm", "Tingling" };
    /** Define specifics for certain skills */
    private final int INFRA_DISTANCE = 60;

    // Not sure where these go: into the Skill action or as Racial attribute
    // private final int SLEEP_RESIST = 30;
    // private final int CHARM_RESIST = 30;
    // private final int TINGLING_ACTIVE = 33;

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR(S) AND
     * RELATED METHODS ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Empty constructor for testing. This should not be called except by the
     * Person factory
     */
    public HalfElf() {
        super._raceName = RACE_NAME;
    }

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
     * PROTECTED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /** Assign the skills that the Half-Elf has */
    public List<Skill> assignSkills(List<Skill> inSkills) {
        Skill skill = new Skill();
        SkillRegistry skreg = (SkillRegistry) RegistryFactory.getInstance().getRegistry(RegKey.SKILL);
        for (int i = 0; i < _helfSkills.length; i++) {
            String skillName = _helfSkills[i];
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
        return traits;
    }

    /**
     * All Half-Elfs start out at the same age. Set the newbie's racial starting
     * age, and call the super method to set the current age in seconds. Each
     * age value is a pre-defined constant, and used for trait adjustment as
     * Person changes age brackets.
     * 
     * @return current starting age of Half-Elf in years
     */
    public long initAge() {
        return super.initAge((long) _helfAgeBracket[0]);
    }

    /**
     * Initalize the height of the Half-Elf with a range of plus/minus 16% of
     * average. Save the value as a base class attribute.
     * 
     * @return height of Half-Elf in inches
     * @throws ChronosException
     *             if the defined Half-Elf heights are invalid
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
     * Retrieve the description suffix that is particular to the Race involved.
     * 
     * @return the suffix
     */
    public String initRaceDescriptor() {
        return super._descriptor = RACE_DESCRIPTOR;
    }

    /**
     * Initialize the weight of the Half-Elf with a range of plus/minus 16% of
     * average. Save the value as a base class attribute.
     * 
     * @return weight of Half-Elf in pounds
     */
    public int initWeight() {
        // String gender = super._gender;
        // MetaDie rnd = super._random;
        // // Tertiary operator for if/else clause
        // super._weight = (gender.equalsIgnoreCase(Race.MALE)) ?
        // (int) rnd.getGaussian(AVG_WEIGHT_MALE) :
        // (int) rnd.getGaussian(AVG_WEIGHT_FEMALE);
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

    /**
     * Required override, but does nothing
     * 
     * @param defMAA
     *            default magic attack adjustment (race-independent)
     * @param con
     *            used to calculate the bonus
     * @return final magic attack adjustment
     */
    public int updateMagicAttackAdj(int defMAA, int con) {
        super._magicAttackMod = defMAA;
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
    // /** Retrive the specific category for the Half-Elf's age. See Race class
    // for list.
    // *
    // * @return one of the standard age categories
    // */
    // static public String getAgeCategory()
    // {
    // String cat = null;
    // // Find what age the Half-Elf category falls into
    // try {
    // cat = Age.calcAgeCategory(_currentAge, _dwarfAgeBracket);
    // }
    // catch (ChronosException ex) {
    // MsgCtrl.errMsg(this, ex.getMessage());
    // }
    // return cat;
    // }

    /**
     * Set the raclai language for the Half-Elf, who has a 50% chance of knowing
     * Elvish.
     * 
     * @return possible race language or Elvish or null
     */
    public String getLanguage() {
        MetaDie md = new MetaDie();
        int roll = md.rollPercent();
        super._raceLanguage = (roll >= LANGUAGE_CHANCE) ? RACE_LANGUAGE : null;
        return super._raceLanguage;
    }

    /** Accesses and tests the private methods of the Person object. */
    public class MockHalfElf {
        /** Default constructor */
        public MockHalfElf() {}

        /**
         * Accessor for private method
         * 
         * @throws ChronosException
         */
        public AttributeList adjustTraitsForRace(AttributeList traits)
                throws ApplicationException {
            return HalfElf.this.adjTraitsForRace(traits);
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
        // return Half-Elf.this.assignRacialSkills(skillList);
        // }

        /**
         * Calculate the height of the Half-Elf with a range of plus/minus 16%
         * of average. Save the value as a base class attribute.
         * 
         * @return height of Half-Elf in inches
         */
        public int calcHeight() {
            return HalfElf.this.initHeight();
        }

        /**
         * Calculate the weight of the Half-Elf with a range of plus/minus 16%
         * of average. Save the value as a base class attribute.
         * 
         * @return weight of Half-Elf in pounds
         */
        public int calcWeight() {
            return HalfElf.this.initWeight();
        }

        /** Return the race name */
        public String getRaceName() {
            return HalfElf.this.getRaceName();
        }

        /*
         * Test current age and age bracket
         * 
         * @return currentAge of Half-Elf in seconds
         */
        public long initAge() {
            return HalfElf.this.initAge();
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
            return HalfElf.this.initHeightDescriptor(height);
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
            return HalfElf.this.initWeightDescriptor(weight);
        }

        public List<Skill> assignRacialSkills(List<Skill> inSkills) {
            // TODO Auto-generated method stub
            return HalfElf.this.assignSkills(inSkills);
        }

    } // end of MockHalf-Elf inner class

} // end of Half-Elf class