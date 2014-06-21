/*
 * Half-Orc.java
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
import mylib.pdc.MetaDie;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The <code>Half-Orc</code> is a mixed breed of human and orc (ugh!). Half-Orcs
 * have the following differences from Humans: <BL> <LI >Tend to be stronger,
 * heartier and more asocial than Humans</LI> <LI >Racial language in addition
 * to Common: 50% chance of knowing Orcish</LI> <LI >Special abilities:
 * Infravision = 60'</LI> </BL>
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 14 April 2012 // original
 *          <DD>
 *          </DL>
 */
@SuppressWarnings("serial")
public class HalfOrc extends Race implements Serializable {
    /** Average height (inches) for a Half-Orc male */
    private final int AVG_HEIGHT_MALE = 66;
    /** Average height (inches) for a Half-Orc female */
    private final int AVG_HEIGHT_FEMALE = 62;

    /** Average weight (pounds) for a Half-Orc male */
    private final int AVG_WEIGHT_MALE = 150;
    /** Average weight (pounds) for a Half-Orc female */
    private final int AVG_WEIGHT_FEMALE = 120;

    /** The minimum Trait values for a Half-Orc: STR, INT, WIS, DEX, CON, CHR */
    private final int[] _minTraits = { 9, 8, 8, 8, 9, 6 };
    /** The maximum Trait values for a Half-Orc: STR, INT, WIS, DEX, CON, CHR */
    private final int[] _maxTraits = { 18, 18, 18, 18, 19, 17 };

    /** The oldest ages in each of the Half-Orc age brackets (see DMG, p13) */
    private final int[] _horcAgeBracket = { 15, 30, 45, 60, 80 };

    /** The name of the this Race */
    private final String RACE_NAME = "Half-Orc";
    /** The language of this Race */
    private final String RACE_LANGUAGE = "Orcish";
    /** The chance of this Half-Orc knowing his/her native language */
    private final int LANGUAGE_CHANCE = 50;
    /** Elves have pointed ears */
    private final String RACE_DESCRIPTOR = "a squat snoutish face";

    /** The names of the special abilities that come with being a dwarf */
    private final String[] _horcSkills = { "Infravision" };
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
    public HalfOrc() {
        super._raceName = RACE_NAME;
    }

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
     * PROTECTED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /** Assign the skills that the Half-Orc has */
    public ArrayList<Skill> assignSkills(ArrayList<Skill> inSkills) {
        Skill skill = new Skill();
        SkillRegistry skreg = (SkillRegistry) AdvRegistryFactory.getRegistry(RegKey.SKILL);
        for (int i = 0; i < _horcSkills.length; i++) {
            String skillName = _horcSkills[i];
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
     * Half-Orcs get an extra STR and CON but lose two extra CHR points
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
            traits.put(ATTRIBUTE.STR, traits.get(ATTRIBUTE.STR) + 1);
            traits.put(ATTRIBUTE.CHR, traits.get(ATTRIBUTE.CHR) - 2);
        }
        return traits;
    }

    /**
     * All Half-Orcs start out at the same age. Set the newbie's racial starting
     * age, and call the super method to set the current age in seconds. Each
     * age value is a pre-defined constant, and used for trait adjustment as
     * Person changes age brackets.
     * 
     * @return current starting age of Half-Orc in years
     */
    public long initAge() {
        return super.initAge((long) _horcAgeBracket[0]);
    }

    /**
     * Initalize the height of the Half-Orc with a range of plus/minus 16% of
     * average. Save the value as a base class attribute.
     * 
     * @return height of Half-Orc in inches
     * @throws ChronosException
     *             if the defined Half-Orc heights are invalid
     */
    public int initHeight() {
        // String gender = super._gender;
        // MetaDie rnd = super._random;
        // Tertiary operator for if/else clause
        // super._height = (gender.equalsIgnoreCase(Race.MALE)) ?
        // (int) rnd.getGaussian((double)AVG_HEIGHT_MALE) :
        // (int) rnd.getGaussian((double)AVG_HEIGHT_FEMALE);
        super.initHeight(AVG_HEIGHT_MALE, AVG_HEIGHT_FEMALE);
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
     * Initialize the weight of the Half-Orc with a range of plus/minus 16% of
     * average. Save the value as a base class attribute.
     * 
     * @return weight of Half-Orc in pounds
     */
    public int initWeight() {
        // String gender = super._gender;
        // MetaDie rnd = super._random;
        // // Tertiary operator for if/else clause
        // super._weight = (gender.equalsIgnoreCase(Race.MALE)) ?
        // (int) rnd.getGaussian(AVG_WEIGHT_MALE) :
        // (int) rnd.getGaussian(AVG_WEIGHT_FEMALE);
        super.initWeight(AVG_WEIGHT_MALE, AVG_WEIGHT_FEMALE);
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

    /**
     * Set the raclai language for the Half-Orc, who has a 50% chance of knowing
     * Orcish.
     * 
     * @return possible race language of Orcish or null
     */
    public String getLanguage() {
        MetaDie md = new MetaDie();
        int roll = md.rollPercent();
        super._raceLanguage = (roll >= LANGUAGE_CHANCE) ? RACE_LANGUAGE : null;
        return super._raceLanguage;
    }

    /** Accesses and tests the private methods of the Person object. */
    public class MockHalfOrc {
        /** Default constructor */
        public MockHalfOrc() {}

        /**
         * Accessor for private method
         * 
         * @throws ChronosException
         */
        public AttributeList adjustTraitsForRace(AttributeList traits)
                throws ApplicationException {
            return HalfOrc.this.adjTraitsForRace(traits);
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
        // return Half-Orc.this.assignRacialSkills(skillList);
        // }

        /**
         * Calculate the height of the Half-Orc with a range of plus/minus 16%
         * of average. Save the value as a base class attribute.
         * 
         * @return height of Half-Orc in inches
         */
        public int calcHeight() {
            return HalfOrc.this.initHeight();
        }

        /**
         * Calculate the weight of the Half-Orc with a range of plus/minus 16%
         * of average. Save the value as a base class attribute.
         * 
         * @return weight of Half-Orc in pounds
         */
        public int calcWeight() {
            return HalfOrc.this.initWeight();
        }

        /** Return the race name */
        public String getRaceName() {
            return HalfOrc.this.getRaceName();
        }

        /*
         * Test current age and age bracket
         * 
         * @return currentAge of Half-Orc in seconds
         */
        public long initAge() {
            return HalfOrc.this.initAge();
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
            return HalfOrc.this.initHeightDescriptor(height);
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
            return HalfOrc.this.initWeightDescriptor(weight);
        }

        public ArrayList<Skill> assignRacialSkills(ArrayList<Skill> inSkills) {
            // TODO Auto-generated method stub
            return HalfOrc.this.assignSkills(inSkills);
        }

    } // end of MockHalf-Orc inner class

} // end of Half-Orc class