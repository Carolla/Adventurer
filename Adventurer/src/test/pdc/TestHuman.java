/**
 * TestHuman.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package test.pdc;

import pdc.character.Human;
import pdc.character.Human.MockHuman;

import chronos.pdc.Age;
import chronos.pdc.AttributeList;

import mylib.Constants;
import mylib.MsgCtrl;
import mylib.pdc.MetaDie;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

/**
 * Tests only the derived methods of the Human class, using an empty
 * constructor. To test an actual Human Person object, use the Person class
 * factory
 * <code>Person(name, gender, occup, hairColor, "Human", klassName)</code>
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Jun 19, 2009 // original
 *          <DD>
 *          <DT>Build 2.0 Dec 27 2010 // modified for QA and refactored MetaDie
 *          <DD>
 *          <DT>Build 2.1 Jan 7 2011 // revised ctors as protected and added
 *          generic Joe
 *          <DD>
 *          <DT>Build 2.2 Apr 10 2011 // TAA changed AssignSkills to
 *          AssignRacialSkills, ensured function
 *          <DD>
 *          <DT>Build 2.3 Apr 21 2011 // TAA commented out assignRacialSkills
 *          section, not valid for humans
 *          <DD>
 *          </DL>
 */
public class TestHuman extends TestCase {
	private final int AVG_HEIGHT_MALE = 70;
	private final int AVG_HEIGHT_FEMALE = 64;
	private final int AVG_WEIGHT_MALE = 175;
	private final int AVG_WEIGHT_FEMALE = 130;

	// Create class references
	// Person _hero = null;
	// Person.MockPerson _mockPerson = null; // accesses both genders

	Human _hero = null;
	MockHuman _mock = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Audit messages are OFF at start of each test
		MsgCtrl.auditMsgsOn(true);
		// Error messages are ON at start of each test
		MsgCtrl.errorMsgsOn(true);
		// Pass both objects to Person constructor to create beginning Kharacter
		_hero = new Human();
		assertNotNull(_hero);
		_mock = _hero.new MockHuman();
		assertNotNull(_mock);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		_hero = null;
		_mock = null;
		// Audit messages are OFF after each test
		MsgCtrl.auditMsgsOn(true);
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR(S) AND
	 * RELATED METHODS ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Test the Human constructor
	 * 
	 * @Normal Human.Human() ensure that Human is race name
	 * @Error Human.Human() N/A
	 * @Null Human.Human() N/A
	 */
	public void testHumanCtor() {
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestHumanCtor()");
		assertTrue(_mock.getRaceName().equals("Human"));
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PUBLIC METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	// /** Assign no racial skills, but set infraDistance to 0
	// * @Normal Human.assignRacialSkills(ArrayList<Skill> skillList) ok
	// * @Error Human.assignRacialSkills(ArrayList<Skill> skillList) N/A
	// * @Null Human.assignRacialSkills(ArrayList<Skill> skillList) ok
	// */
	// public void testAssignRacialSkills() throws ChronosException
	// {
	// MsgCtrl.auditMsgsOn(false);
	// MsgCtrl.msgln(this, "\ttestAssignRacialSkills()");
	// //Build array of fake skills
	// ArrayList<Skill> inSkills = new ArrayList<Skill>();
	// inSkills.add(new Skill("Shoeshining", "test skill", null));
	// inSkills.add(new Skill("Honey-dipping", "test skill", null));
	//
	// // Check that inSkills and outSkills are equal, no modifications done
	// ArrayList<Skill> outSkills = _mock.assignRacialSkills(inSkills);
	// assertTrue(outSkills.size() == inSkills.size());
	// assertTrue(inSkills.get(0).equals(outSkills.get(0)));
	// assertTrue(inSkills.get(1).equals(outSkills.get(1)));
	//
	// // Null Pass empty array of skills
	// ArrayList<Skill> emptySkills = null;
	// assertNull (_mock.assignRacialSkills(emptySkills));
	// // assertTrue(emptySkills.size() == 0);
	// }

	/**
	 * Test the age inits: current age in years (double), and the age category
	 * 
	 * @Normal Human.initAge() ok
	 * @Error Human.initAge() n/a
	 * @Null Human.initAge() n/a
	 */
	public void testInitAge() {
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestInitAge()");

		// Calls the superclass method, which implies the Human method call
		_mock.initAge();
		// Expected age is 85% of 20 years old = 17.0
		double expAge = 20.0 * Age.STARTING_AGE_ADJ;
		MsgCtrl.msgln("\tHero's age = " + _hero.getAge() + "\t expected age = "
				+ expAge);
		// Testing starting age for Human is 85% of 20 years old,
		assertEquals(_hero.getAge(), expAge * Constants.SECS_PER_YEAR, .01);
	}

	/**
	 * Test for Human Female height calculation for 100 random rolls Height
	 * varies around mean at within plus/minus half-sigma
	 * 
	 * @Normal Human.initHeight() ok
	 * @Error Human.initHeight() N/A
	 * @Null Human.initHeight() N/A
	 */
	public void testInitHeightFemale() {
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestInitHeightFemale()");
		_hero.setGender("Female");

		// Female Height
		int limitLo = (int) Math.round(AVG_HEIGHT_FEMALE
				* (1.0 - MetaDie.HALF_SIGMA));
		int limitHi = (int) Math.round(AVG_HEIGHT_FEMALE
				* (1.0 + MetaDie.HALF_SIGMA));
		// Check limits of 54" (4'6") to 74" (6'2") (Average = 64" = 5'4")
		MsgCtrl.msg("Female Height range: [" + limitLo + ", " + limitHi + "]");
		MsgCtrl.msgln("\tAverage = " + AVG_HEIGHT_FEMALE);
		for (int k = 0; k < 100; k++) {
			int roll = _mock.calcHeight();
			assertTrue((roll >= limitLo) && (roll <= limitHi));
			// if ((roll < limitLo) || (roll > limitHi)) {
			// MsgCtrl.errMsg(roll + " ");
			// }
			// else {
			// MsgCtrl.msg(roll + "  ");
			// }
			// // Place a new line every 25 results
			// if (k % 25 == 0 ) {
			// MsgCtrl.msgln("");
			// }
		} // end of for loop
	}

	/**
	 * Test for Human Male height calculation for 100 random rolls. Height
	 * varies around mean at within plus/minus half-sigma
	 * 
	 * @Normal Human.initHeight() ok
	 * @Error Human.initHeight() N/A
	 * @Null Human.initHeight() N/A
	 */
	public void testInitHeightMale() {
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestInitHeightMale()");
		_hero.setGender("Male");
		int roll = -1;
		// Male Height
		int limitLo = (int) Math.round(AVG_HEIGHT_MALE
				* (1.0 - MetaDie.HALF_SIGMA));
		int limitHi = (int) Math.round(AVG_HEIGHT_MALE
				* (1.0 + MetaDie.HALF_SIGMA));
		// Check limits of 59" (4'11") to 81" (6'11") (Average = 70" = 5'10")
		MsgCtrl.msg("Male Height range: [" + limitLo + ", " + limitHi + "]");
		MsgCtrl.msgln("\tAverage = " + AVG_HEIGHT_MALE);
		// MsgCtrl.msg("Male Height calc: ");
		for (int k = 0; k < 100; k++) {
			roll = _mock.calcHeight();
			assertTrue((roll >= limitLo) && (roll <= limitHi));
			// if ((roll < limitLo) || (roll > limitHi)) {
			// MsgCtrl.errMsg(roll + " ");
			// }
			// else {
			// MsgCtrl.msg(roll + "  ");
			// }
		}
	}

	/**
	 * Test for Human Female weight calculation for 100 random rolls Weight
	 * varies around mean at within plus/minus sigma
	 * 
	 * @Normal Human.initWeight() ok
	 * @Error Human.initWeight() N/A
	 * @Null Human.initWeight() N/A
	 */
	public void testInitWeightFemale() {
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestCalcWeightFemale()");
		int limitLo = 0;
		int limitHi = 0;
		// Female Height
		_hero.setGender("Female");
		// Check limits of 109# to 151# (Average = 130#)
		limitLo = (int) Math.round(AVG_WEIGHT_FEMALE * (1.0 - MetaDie.SIGMA));
		limitHi = (int) Math.round(AVG_WEIGHT_FEMALE * (1.0 + MetaDie.SIGMA));
		MsgCtrl.msg("Female Weight range: [" + limitLo + ", " + limitHi + "]");
		MsgCtrl.msgln("\tAverage = " + AVG_WEIGHT_FEMALE);
		// MsgCtrl.msg("\tFemale Weight calc: ");
		for (int k = 0; k < 100; k++) {
			int roll = _mock.calcWeight();
			assertTrue((roll >= limitLo) && (roll <= limitHi));
			// if ((roll < limitLo) || (roll > limitHi)) {
			// MsgCtrl.errMsg(roll + " ");
			// }
			// else {
			// MsgCtrl.msg(roll + "  ");
			// }
			// // Place a new line every 25 results
			// if (k % 25 == 0 ) {
			// MsgCtrl.msgln("");
			// }
		} // end of for loop
	}

	/**
	 * Test for Human Male weight calculation for 100 random rolls Weight varies
	 * around mean at within plus/minus sigma
	 * 
	 * @Normal Human.initWeight() ok
	 * @Error Human.initWeight() N/A
	 * @Null Human.initWeight() N/A
	 */
	public void testInitWeightMale() {
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestInitWeightMale()");

		int limitLo = 0;
		int limitHi = 0;
		_hero.setGender("Male");
		int roll = -1;
		// Male Height
		// Check limits of 146# to 203# (Average = 175#)
		limitLo = (int) Math.round(AVG_WEIGHT_MALE * (1.0 - MetaDie.SIGMA));
		limitHi = (int) Math.round(AVG_WEIGHT_MALE * (1.0 + MetaDie.SIGMA));
		MsgCtrl.msg("Male Height range: [" + limitLo + ", " + limitHi + "]");
		MsgCtrl.msgln("\tAverage = " + AVG_WEIGHT_MALE);
		// MsgCtrl.msg("\tMale Weight calc: ");
		for (int k = 0; k < 100; k++) {
			roll = _mock.calcWeight();
			assertTrue((roll >= limitLo) && (roll <= limitHi));
			// if ((roll < limitLo) || (roll > limitHi)) {
			// MsgCtrl.errMsg(roll + " ");
			// }
			// else {
			// MsgCtrl.msg(roll + "  ");
			// }
			// // Place a new line every 25 results
			// if (k % 25 == 0 ) {
			// MsgCtrl.msgln("");
			// }
		} // end of for loop
	}

	/**
	 * Test for Human traits fall within range {#link Human.verifyTraits(int[])}
	 * 
	 * @Normal Human.verifyTraits(int[]) ok
	 * @Error Human.verifyTraits(int[]) ok
	 * @Null Human.verifyTraits(int[]) ok
	 * @Special Human.verifyTraits(int[]) invalid but readjusted
	 */
	public void testVerifyTraits() {
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestVerifyTraits()");

		int[] minT = { 8, 8, 8, 8, 8, 8 };
		int[] maxT = { 19, 18, 18, 18, 18, 18 };
		/** All values must be >= 8 */
		AttributeList minTraits = new AttributeList(minT);
		/** All values must be <= 18 except max STR = 19 */
		AttributeList maxTraits = new AttributeList(maxT);

		// Chronos.ATTRIBUTE[] attOrder = { ATTRIBUTE.STR, ATTRIBUTE.DEX,
		// ATTRIBUTE.INT, ATTRIBUTE.WIS, ATTRIBUTE.CON, ATTRIBUTE.CHR };
		//
		// for (int i = 0; i < miniTraits.length; i++) {
		// minTraits.put(attOrder[i], miniTraits[i]);
		// maxTraits.put(attOrder[i], maxiTraits[i]);
		// }

		// NORMAL -- Valid traits are not changed
		AttributeList results = _mock.verifyTraits(minTraits);
		assertTrue(results.equals(minTraits));
		results = _mock.verifyTraits(maxTraits);
		assertTrue(results.equals(maxTraits));

		// NULL case
		results = _mock.verifyTraits(null);
		assertNull(results);

		// SPECIAL -- Set a couple traits to invalid values and let them get
		// readjusted
		minTraits.put(2, 7);
		results = _mock.verifyTraits(minTraits);
		assertTrue(results.equals(minTraits));

		minTraits.put(0, 20);
		results = _mock.verifyTraits(maxTraits);
		assertTrue(results.equals(maxTraits));

		// // ERROR -- index out of bounds with an extra element
		// int[] longTraits = { 19, 18, 18, 18, 18, 18, 15 };
		// results = _mock.verifyTraits(longTraits);
		// assertNull(results);
	}

	/**
	 * Tests that are not needed for various reasons, mostly setters and getters
	 * 
	 * @Not_Needed Human.Race() simple random call
	 * @Not_Needed Human.AdjTraitsForRace() wrapper for verifyTraits
	 * @Not_Needed Human.AdjTraitsForAge() wrapper for Age.AdjTraitsForAge
	 * @Not_Needed Human.getLanguage() getter
	 * @Not_Needed Human.updateMagicAttackAdj(int, int) setter
	 */
	public void testNotNeeded() {
	}

	/**
	 * Tests that are not implemented yet
	 */
	public void testNotImplemented() {
	}

} // end of TestHuman class
