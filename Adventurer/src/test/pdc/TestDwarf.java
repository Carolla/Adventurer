/**
 * TestDwarf.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package test.pdc;

import pdc.character.Dwarf;
import pdc.character.Dwarf.MockDwarf;

import chronos.pdc.Age;
import chronos.pdc.AttributeList;
import chronos.pdc.Skill;

import mylib.ApplicationException;
import mylib.Constants;
import mylib.MsgCtrl;
import mylib.pdc.MetaDie;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

/**
 * Tests only the derived methods of the Dwarf class, using an empty
 * constructor. To test an actual Dwarf Person object, use the Person class
 * factory
 * <code>Person(name, gender, occup, hairColor, "Dwarf", klassName)</code>
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Jun 19, 2009 // original
 *          <DD>
 *          <DT>Build 1.1 Apr 21 2011 // TAA Added AdjTraitsForRace, ensured
 *          funcntion of tests
 *          </DL>
 */
public class TestDwarf extends TestCase {
	private final int AVG_HEIGHT_MALE = 48;
	private final int AVG_HEIGHT_FEMALE = 46;
	private final int AVG_WEIGHT_MALE = 150;
	private final int AVG_WEIGHT_FEMALE = 120;

	// Create class references
	Dwarf _hero = null;
	MockDwarf _mock = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Audit messages are OFF at start of each test
		MsgCtrl.auditMsgsOn(false);
		// Error messages are ON at start of each test
		MsgCtrl.errorMsgsOn(true);

		_hero = new Dwarf();
		assertNotNull(_hero);
		_mock = _hero.new MockDwarf();
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
		MsgCtrl.auditMsgsOn(false);
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR(S) AND
	 * RELATED METHODS ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/** Test the Dwarf constructor */
	public void testDwarfCtor() {
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestDwarfCtor()");
		assertTrue(_mock.getRaceName().equals("Dwarf"));
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PUBLIC METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */
	/**
	 * Verify that the required skills for a dwarf are assigned
	 * 
	 * @throws ChronosException
	 * 
	 * @Normal Dwarf.adjTraitsForAge() ok
	 * @Error Dwarf.adjTraitsForAge() ok
	 * @Null Dwarf.adjTraitsForAge() ok
	 */
	public void testAdjTraitsForRace() throws ApplicationException {
		MsgCtrl.auditMsgsOn(true);
		MsgCtrl.msgln(this, "\ttestAdjTraitsForRace()");
		int[] arrTraits = { 10, 10, 10, 10, 10, 10 };
		int[] arrAdjVerifiedTraits = { 10, 10, 10, 11, 12, 9 };
		int[] arrShortTraits = { 1, 1 };
		AttributeList traits = new AttributeList(arrTraits);
		AttributeList adjVerifiedTraits = new AttributeList(
				arrAdjVerifiedTraits);
		AttributeList shortTraits = new AttributeList(arrShortTraits);

		// Dump traits
		// for (int i=0; i<=5; i++)
		// {
		// MsgCtrl.msg("" + traits[i] + ", ");
		// }
		// MsgCtrl.msg("\n");

		// NORMAL
		_mock.adjustTraitsForRace(traits);

		for (int i = 0; i <= 5; i++) {
			assertEquals(traits, adjVerifiedTraits);
		}

		// ERROR - error message seen
		MsgCtrl.msgln("shortTraits.length= " + shortTraits.size());
		_mock.adjustTraitsForRace(shortTraits);

		// NULL pass null set, should throw error
		_mock.adjustTraitsForRace((AttributeList) null);
	}

	/**
	 * Verify that the required skills for a dwarf are assigned
	 * 
	 * @Normal Dwarf.Dwarf() ok
	 * @Error Dwarf.Dwarf() N/A
	 * @Null Dwarf.Dwarf() N/A
	 */
	public void testAssignRacialSkills() {
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestAssignRacialSkills()");
		// Build array of fake skills
		ArrayList<Skill> inSkills = new ArrayList<Skill>();

		// Check that inSkills and outSkills are equal
		ArrayList<Skill> outSkills = _mock.assignRacialSkills(inSkills);
		assertTrue(outSkills.size() == inSkills.size());
		assertTrue(inSkills.get(0).equals(outSkills.get(0)));
		assertTrue(inSkills.get(1).equals(outSkills.get(1)));
		MsgCtrl.auditMsgsOn(true);
	}

	/**
	 * Test for Dwarf Male height calculation for 100 random rolls. the age
	 * inits: current age in years (double), and the age category
	 * 
	 * @Normal Dwarf.initAge() ok
	 * @Error Dwarf.initAge() n/a
	 * @Null Dwarf.initAge() n/a
	 */
	public void testCalcHeightMale() {
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestInitHeightMale()");
		_hero.setGender("Male");
		// Male Height
		MsgCtrl.msgln("\nMale Height calc: ");
		// Check limits of 40" (3'4") to 56" (4'8") (Average = 48" = 4'0")
		int limitLo = (int) Math.round(AVG_HEIGHT_MALE
				* (1.0 - MetaDie.HALF_SIGMA));
		int limitHi = (int) Math.round(AVG_HEIGHT_MALE
				* (1.0 + MetaDie.HALF_SIGMA));
		MsgCtrl.msg("Male Height range: [" + limitLo + ", " + limitHi + "]");
		MsgCtrl.msgln("\tAverage = " + AVG_HEIGHT_MALE);
		for (int k = 0; k < 100; k++) {
			int roll = _mock.calcHeight();
			if ((roll < limitLo) || (roll > limitHi)) {
				MsgCtrl.errMsg(roll + " ");
			}
			if ((k == 25) || (k == 50) || (k == 75)) {
				MsgCtrl.msg("\n");
			} else {
				MsgCtrl.msg(roll + "  ");
			}
		}
	}

	/**
	 * Test for Dwarf Female height calculation for 100 random rolls Height
	 * varies around mean at within plus/minus half-sigma
	 * 
	 * @Normal Dwarf.initHeight() ok
	 * @Error Dwarf.initHeight() N/A
	 * @Null Dwarf.initHeight() N/A
	 */
	public void testCalcHeightFemale() {
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "testInitHeightFemale()");
		_hero.setGender("Female");

		// Female Height
		MsgCtrl.msgln("Female Height calc: ");
		// Check limits of 38" (3'2") to 54" (4'8") (Average = 46" = 3'10")
		int limitLo = (int) Math.round(AVG_HEIGHT_FEMALE
				* (1.0 - MetaDie.HALF_SIGMA));
		int limitHi = (int) Math.round(AVG_HEIGHT_FEMALE
				* (1.0 + MetaDie.HALF_SIGMA));
		MsgCtrl.msg("Female Height range: [" + limitLo + ", " + limitHi + "]");
		MsgCtrl.msgln("\tAverage = " + AVG_HEIGHT_FEMALE);
		for (int k = 0; k < 100; k++) {
			int roll = _mock.calcHeight();
			if ((k == 25) || (k == 50) || (k == 75)) {
				MsgCtrl.msg("\n");
			}
			if ((roll < limitLo) || (roll > limitHi)) {
				MsgCtrl.errMsg(roll + " ");
			} else {
				MsgCtrl.msg(roll + "  ");
			}
		}
		MsgCtrl.msgln("\n");
	}

	/**
	 * Test for Dwarf Male weight calculation for 100 random rolls Weight varies
	 * around mean at within plus/minus sigma
	 * 
	 * @Normal Dwarf.initWeight() ok
	 * @Error Dwarf.initWeight() N/A
	 * @Null Dwarf.initWeight() N/A
	 */
	public void testCalcWeightMale() {
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestInitWeightMale()");
		_hero.setGender("Male");

		// Male Weight
		// Check limits of 99# to 201# (Average = 150#)
		int limitLo = (int) Math.round(AVG_WEIGHT_MALE * (1.0 - MetaDie.SIGMA));
		int limitHi = (int) Math.round(AVG_WEIGHT_MALE * (1.0 + MetaDie.SIGMA));
		MsgCtrl.msg("Male Weight range: [" + limitLo + ", " + limitHi + "]");
		MsgCtrl.msgln("\tAverage = " + AVG_WEIGHT_MALE);
		MsgCtrl.msgln("Male Weight calc: ");
		for (int k = 0; k < 100; k++) {
			int roll = _mock.calcWeight();
			if ((k == 25) || (k == 50) || (k == 75)) {
				MsgCtrl.msg("\n");
			}
			if ((roll < limitLo) || (roll > limitHi)) {
				MsgCtrl.errMsg(roll + " ");
			} else {
				MsgCtrl.msg(roll + "  ");
			}
			assertTrue((roll >= limitLo) && (roll <= limitHi));
		}
	}

	/**
	 * Test for Dwarf Female weight calculation for 100 random rolls Weight
	 * varies around mean at within plus/minus sigma
	 * 
	 * @Normal Dwarf.initWeight() ok
	 * @Error Dwarf.initWeight() N/A
	 * @Null Dwarf.initWeight() N/A
	 */
	public void testCalcWeightFemale() {
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestInitWeightFemale()");
		_hero.setGender("Female");

		// Female Height
		// Check limits of 79# to 161# (Average = 120#)
		int limitLo = (int) Math.round(AVG_WEIGHT_FEMALE
				* (1.0 - MetaDie.SIGMA));
		int limitHi = (int) Math.round(AVG_WEIGHT_FEMALE
				* (1.0 + MetaDie.SIGMA));
		MsgCtrl.msg("Female Weight range: [" + limitLo + ", " + limitHi + "]");
		MsgCtrl.msgln("\tAverage = " + AVG_WEIGHT_FEMALE);
		MsgCtrl.msg("\nFemale Weight calc: ");
		for (int k = 0; k < 100; k++) {
			int roll = _mock.calcWeight();
			if ((k == 25) || (k == 50) || (k == 75)) {
				MsgCtrl.msg("\n");
			}
			if ((roll < limitLo) || (roll > limitHi)) {
				MsgCtrl.errMsg(roll + " ");
			} else {
				MsgCtrl.msg(roll + "  ");
			}
			assertTrue((roll >= limitLo) && (roll <= limitHi));
		}
		MsgCtrl.msgln("\n");
	}

	/**
	 * Test the age inits: current age in years (double), and the age category
	 * 
	 * @Normal Dwarf.initAge() ok
	 * @Error Dwarf.initAge() n/a
	 * @Null Dwarf.initAge() n/a
	 */
	public void testInitAge() {
		MsgCtrl.auditMsgsOn(true);
		MsgCtrl.msgln(this, "\ttestInitAge()");

		// Calls the superclass method, which implies the Dwarf method call
		_mock.initAge();
		// Expected age is 85% of 50 years old = 42.5, converted to seconds
		// rounded down because of type and not being good at programming yet
		// long expAge = (long) Math.round( 50.0 * Age.STARTING_AGE_ADJ) - 1;
		double expAge = 50.0 * Age.STARTING_AGE_ADJ;
		MsgCtrl.msgln("\tHero's age = " + _hero.getAge() + "\t expected age = "
				+ expAge);
		double calcAge = _hero.getAge();
		assertEquals(calcAge, expAge * Constants.SECS_PER_YEAR, .01);
		// TODO: move test to CIV
		// assertTrue(_hero.getAgeCategory().equals("Young Adult"));
	}

	// TODO: Move to CIV
	// /** Test that the height adjective is correct for the give height
	// category
	// * Race.findRangeDescriptor() is already tested, so check only Dwarf
	// specifics.
	// * @Normal Dwarf.initHeightDesc() ok
	// * @Error Dwarf.initHeightDesc() ok
	// * @Null Dwarf.initHeightDesc() n/a
	// */
	// public void testInitHeightDesc()
	// {
	// MsgCtrl.auditMsgsOn(false);
	// MsgCtrl.errorMsgsOn(false);
	// MsgCtrl.msg("\n");
	// // Possible descriptors, from short to tall
	// final String[] htDesc = {"tiny", "short", "average-height", "tall",
	// "towering"};
	//
	// // Male heights range from 40.0 to 56.0
	// String desc = null;
	// // Illegal: too short, just below bottom
	// desc = _mock.initHeightDescriptor(39.0, "Male");
	// MsgCtrl.msgln(desc);
	// assertNull(desc);
	// // Legal range: just above bottom
	// desc = _mock.initHeightDescriptor(40.0, "Male");
	// MsgCtrl.msgln(desc);
	// assertTrue(desc.equals(htDesc[0]));
	// // Legal range: just below top
	// desc = _mock.initHeightDescriptor(56.0, "Male");
	// MsgCtrl.msgln(desc);
	// assertTrue(desc.equals(htDesc[4]));
	// // Illegal: too tall, just above top
	// desc = _mock.initHeightDescriptor(57.0, "Male");
	// MsgCtrl.msgln(desc);
	// assertNull(desc);
	// MsgCtrl.msg("\n");
	//
	// // Female heights range from 38 to 54
	// // Illegal: too short, just below bottom
	// desc = _mock.initHeightDescriptor(37.0, "Female");
	// MsgCtrl.msgln(desc);
	// assertNull(desc);
	// // Legal range: just above bottom
	// desc = _mock.initHeightDescriptor(38.0, "Female");
	// MsgCtrl.msgln(desc);
	// assertTrue(desc.equals(htDesc[0]));
	// // Legal range: just below top
	// desc = _mock.initHeightDescriptor(54.0, "Female");
	// MsgCtrl.msgln(desc);
	// assertTrue(desc.equals(htDesc[4]));
	// // Illegal: too tall, just above top
	// desc = _mock.initHeightDescriptor(55.0, "Female");
	// MsgCtrl.msgln(desc);
	// assertNull(desc);
	// }

	// TODO: Move to CIV
	// /** Test that the weight adjective is correct for the give weight
	// category
	// * Race.findRangeDescriptor() is already tested, so check only Dwarf
	// specifics.
	// * @Normal Dwarf.initWeightDesc() ok
	// * @Error Dwarf.initWeightDesc() ok
	// * @Null Dwarf.initWeightDesc() n/a
	// */
	// public void testInitWeightDesc()
	// {
	// MsgCtrl.auditMsgsOn(false);
	// MsgCtrl.msgln("\n");
	// // Possible descriptors, from light to heavy
	// final String[] wtDesc = {"skinny", "thin", "medium-weight", "heavy",
	// "bulky"};
	//
	// // Male heights range from 99 to 201
	// String desc = null;
	// // Illegal: too short, just below bottom
	// desc = _mock.initWeightDescriptor(98.0, "Male");
	// MsgCtrl.msgln(desc);
	// assertNull(desc);
	// // Legal range: just above bottom
	// desc = _mock.initWeightDescriptor(99.0, "Male");
	// MsgCtrl.msgln(desc);
	// assertTrue(desc.equals(wtDesc[0]));
	// // Legal range: just below top
	// desc = _mock.initWeightDescriptor(201.0, "Male");
	// MsgCtrl.msgln(desc);
	// assertTrue(desc.equals(wtDesc[4]));
	// // Illegal: too tall, just above top
	// desc = _mock.initWeightDescriptor(202.0, "Male");
	// MsgCtrl.msgln(desc);
	// assertNull(desc);
	// MsgCtrl.msgln("\n");
	//
	// // Female heights range from 79 to 161
	// // Illegal: too short, just below bottom
	// desc = _mock.initWeightDescriptor(78.0, "Female");
	// MsgCtrl.msgln(desc);
	// assertNull(desc);
	// // Legal range: just above bottom
	// desc = _mock.initWeightDescriptor(79.0, "Female");
	// MsgCtrl.msgln(desc);
	// assertTrue(desc.equals(wtDesc[0]));
	// // Legal range: just below top
	// desc = _mock.initWeightDescriptor(161.0, "Female");
	// MsgCtrl.msgln(desc);
	// assertTrue(desc.equals(wtDesc[4]));
	// // Illegal: too tall, just above top
	// desc = _mock.initWeightDescriptor(162.0, "Female");
	// MsgCtrl.msgln(desc);
	// assertNull(desc);
	// MsgCtrl.auditMsgsOn(false);
	// }

	/**
	 * Tests that are not needed for various reasons, mostly setters and getters
	 * 
	 * @Not_Needed Dwarf.adjTraitsForAge (int[] defValues) wrapper for
	 *             Age.adjTraitsForAge()
	 * @Not_Needed Dwarf.assignSkills((ArrayList<Skill>) getter
	 * @Not_Needed Dwarf.getLanguage() getter
	 * @Not_Needed Dwarf.updateMagicAttackAdj(int, int) setter
	 * @Not_Needed Dwarf.verifyTraits(int[])) wrapper to superclass
	 */
	public void testNotNeeded() {
	}

	/**
	 * Tests that are not implemented yet
	 * 
	 * @Not_Implemented Human.initHeightDescriptor(double) defer to
	 *                  playing-version of game
	 * @Not_Implemented Human.initWeightDescriptor(double) defer to
	 *                  playing-version of game
	 */
	public void testNotImplemented() {
	}

} // end of TestDwarf class

