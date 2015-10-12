/**
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
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

import mylib.ApplicationException;
import mylib.MsgCtrl;
import chronos.pdc.AttributeList;
import chronos.pdc.Race;
import chronos.pdc.Skill;

/**
 * Human is the normalized Person of all Races and Klasses. It implements the
 * abstract Race Class with no adjustments.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Feb 14 2009 // original
 *          <DD>
 *          <DT>Build 1.1 Feb 28 2009 // modifed after Race and Klass redesign
 *          <DD>
 *          </DL>
 */
@SuppressWarnings("serial")
public class Human extends Race implements Serializable {
	/** Average height (inches) for a Human male */
	private final int AVG_HEIGHT_MALE = 70;
	/** Average height (inches) for a Human female */
	private final int AVG_HEIGHT_FEMALE = 64;

	/** Average weight (pounds) for a Human male */
	private final int AVG_WEIGHT_MALE = 175;
	/** Average weight (pounds) for a Human female */
	private final int AVG_WEIGHT_FEMALE = 130;

	/** Human has no Race descriptor, so merely ends the description suffix */
	private final String RACE_DESCRIPTOR = "no special racial traits";

	/** The oldest ages in each of the Human age brackets */
	private final int[] _humanAgeBracket = { 20, 40, 60, 90, 120 };

	/** The minimum Trait values for a Human: STR, INT, WIS, DEX, CON, CHR */
	private final int[] _minTraits = { 8, 8, 8, 8, 8, 8 };
	/** The maximum Trait values for a Human: STR, INT, WIS, DEX, CON, CHR */
	private final int[] _maxTraits = { 18, 18, 18, 18, 18, 18 };

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR(S) AND
	 * RELATED METHODS ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Empty constructor for testing. This should not be called except by the
	 * Person factory
	 */
	public Human() {
		super._raceName = "Human";
	}

	/*
	 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE
	 * SUPPORT METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PROTECTED METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
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
	 * Nothing to do for Humans
	 * 
	 * @param traits
	 *            unadjusted prime traits
	 * @return return the input traits without change
	 */
	public AttributeList adjTraitsForRace(AttributeList traits) {
		return traits;
	}

	/**
	 * Humans have no special racial skills--they are the norm.
	 * 
	 * @param skillList
	 *            target list on which this method will add new skills
	 * @return the original list since Human adds no new skill
	 */
	@Override
	public ArrayList<Skill> assignSkills(ArrayList<Skill> skillList) {
		return skillList;
	}

	/*
	 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * PROTECTED SUPPORT METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * There are no racel languages for a Human, but implementation is required
	 * for subclass of abstract Race class.
	 * 
	 * @return null
	 */
	public String getLanguage() {
		return null;
	}

	/**
	 * All Human start out near the top end of the he youngest age range.
	 * 
	 * @return starting age of a Human in seconds
	 */
	public long initAge() {
		// Pass starting age of Human to be adjusted and converted
		return super.initAge((long) _humanAgeBracket[0]);
	}

	/**
	 * Initalize the height of the Human with a range of plus/minus 16% of
	 * average. Save the value as a base class attribute.
	 * 
	 * @return height of Human in inches
	 */
	public int initHeight() {
		return super.initHeight(AVG_HEIGHT_MALE, AVG_HEIGHT_FEMALE);
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
	 * @return only a period to end the description.
	 */
	public String initRaceDescriptor() {
		return super._descriptor = RACE_DESCRIPTOR;
	}

	/**
	 * Initialize the weight of the Human with a range of plus/minus 16% of
	 * average. Save the value as a base class attribute.
	 * 
	 * @return weight of Human in pounds
	 */
	public int initWeight() {
		return super.initWeight(AVG_WEIGHT_MALE, AVG_WEIGHT_FEMALE);
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
	 * There are no racial adjustments for Human, so no updates are made
	 * 
	 * @param defMAA
	 *            default adj from the base (race-independent) method
	 * @param con
	 *            CON is ignored for humans, but required by the base abstract
	 *            class
	 * @return final magic attack adjustment
	 */
	public int updateMagicAttackAdj(int defMAA, int con) {
		// For humans, there are no other adjustments
		super._magicAttackMod = defMAA;
		return defMAA;
	}

	/**
	 * Verify that the traits stay within the race limits, else revise them. For
	 * human, all values are valid, so this method merely makes a redundant
	 * sanity check for all values between 8 and 19 (initialization values).
	 * 
	 * @param traitList
	 *            the six traits before verification
	 * @return the six traits after verification and/or revisions; else null if
	 *         parm is null
	 * @throws ChronosException
	 *             if the traitlist is null or invalid
	 */
	public AttributeList verifyTraits(AttributeList traitList)
			throws ApplicationException {
		// Guard against no traits input
		if ((traitList == null)
				|| (traitList.size() < AttributeList.NUM_ATTRIBUTES)) {
			return null;
		}
		return super.verifyTraits(traitList, _minTraits, _maxTraits);
	}

	/*
	 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE
	 * SUPPORT METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/** Accesses and tests the private methods of the Person object. */
	public class MockHuman {
		public MockHuman() {
		}

		/** Return the race name */
		public String getRaceName() {
			return Human.this.getRaceName();
		}

		/**
		 * Ensure that no race skills are added but infraDistance is set to 0
		 * 
		 * @param skillList
		 *            set of skills the Person has from other initilizations
		 * @return original skillList
		 */
		public ArrayList<Skill> assignSkills(ArrayList<Skill> skillList) {
			return Human.this.assignSkills(skillList);
		}

		/**
		 * Calculate the height of the Human with a range of plus/minus 16% of
		 * average. Save the value as a base class attribute.
		 * 
		 * @return height of Human in inches
		 */
		public int calcHeight() {
			return Human.this.initHeight();
		}

		/**
		 * Calculate the weight of the Human with a range of plus/minus 16% of
		 * average. Save the value as a base class attribute.
		 * 
		 * @return weight of Human in pounds
		 */
		public int calcWeight() {
			return Human.this.initWeight();
		}

		/*
		 * Get the maximum racial traits
		 * 
		 * @return the prime trait array
		 */
		public int[] getMaxTraits() {
			return Human.this._maxTraits;
		}

		/*
		 * Get the minimum racial traits
		 * 
		 * @return the prime trait array
		 */
		public int[] getMinTraits() {
			return Human.this._minTraits;
		}

		/*
		 * Test current age and age bracket
		 * 
		 * @return currentAge of Human in seconds
		 */
		public long initAge() {
			return Human.this.initAge();
		}

		/*
		 * Get the height of this Character
		 * 
		 * @param gender of the character determines the Hero's height
		 * 
		 * @return height of Human in inches
		 */
		public long initHeight(String gender) {
			Human.this._gender = gender;
			return Human.this.initHeight();
		}

		/*
		 * Check that the traits fall within the legal ranges. If they are not,
		 * the array is adjusted (forced) to be within legal bounds.
		 * 
		 * @param traits to compare against
		 * 
		 * @return the adjusted traits, or no change if the traits array was
		 * valid; or null of an exception occurs
		 */
		public AttributeList verifyTraits(AttributeList traits) {
			AttributeList result = null;
			try {
				result = Human.this.verifyTraits(traits);
			} catch (ApplicationException ex) {
				MsgCtrl.errMsgln(ex.getMessage());
			}
			return result;
		}

	} // end of MockHuman inner class

} // end of Human class