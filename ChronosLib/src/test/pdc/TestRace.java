/**
 * TestRace.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package test.pdc;

import junit.framework.TestCase;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import chronos.Chronos;
import chronos.pdc.Race;

/**
 * Tests the abstract Race class by implementing a concrete subclass from which
 * the base class methods are called.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Jun 4, 2009 // original
 *          <DD>
 *          <DT>Build 2.0 Jan 18, 2010 // add in non-Human racial tests
 *          <DD>
 *          </DL>
 */
public class TestRace extends TestCase {
	/** Original package name for class definiton */
	static private String _originalPkgName = null;
	/** Redirected package name for Mock classes for testing */
	static private final String _mockPkgName = "test.pdc.";
	/** MockRace object to which the methods are called */
	MockRace _mr = null;

	private final String[] _races = { "Human", "Dwarf", "Elf", "Gnome",
			"Half-Elf", "Half-Orc", "Hobbit" };

//	// Three sets of starting tratis: LOW, MEDIUM, HIGH
//	private final int[] _startLow = { 8, 8, 8, 8, 8, 8 };
//	private final int[] _start = { 11, 11, 11, 11, 11, 11 };
//	private final int[] _startHigh = { 18, 18, 18, 18, 18, 18 };
//
//  /** Gender is a race attribute, more specific than Person */
//  private final String MALE = "Male";
//  private final String FEMALE = "Female";
//  
//	private final AttributeList _startLowTraits = new AttributeList(_startLow);
//	private final AttributeList _startTraits = new AttributeList(_start);
//	private final AttributeList _startHighTraits = new AttributeList(_startHigh);

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  STATE HANDLING METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** The target and file is created before any test */
    @BeforeClass
    public static void runOnce()
    {
    }
    
    /** The target file is deleted after all tests */
    @AfterClass
    public static void cleanUp()
    {
    }
    
	/**
	 * Redirect the class loader to look in the test directory for Mock class,
	 * then create the MockRace in the test directory
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		// Save default package name for later restore
		_originalPkgName = Chronos.getPackageName();
		Chronos.setPackageName(_mockPkgName);
		_mr = (MockRace) Race.createRace("MockRace");
		assertNotNull(_mr);
	}

	/**
	 * Redirect the class loader back to its original class directory, and
	 * delete the MockRace object
	 * 
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Delete the MockRace object
		_mr = null;
		// Restore original package name (non-testing version)
		Chronos.setPackageName(_originalPkgName);
		// Audit messages are OFF after each test
		MsgCtrl.auditMsgsOn(false);
	}

	// -------------------------------------------------------------------------
	// BEGIN TESTING
	// -------------------------------------------------------------------------

//	/**
//	 * Test that the MockRace can be created so that the abstract base Race
//	 * class methods can be tested.
//	 * 
//	 * @Normal MockRace.MockRace() used in setUp()
//	 * @Null MockRace.MockRace() N/A
//	 * @Error MockRace.MockRace() N/A
//	 */
//	public void testMockRace() 
//	{
//        MsgCtrl.auditMsgsOn(false);
//        MsgCtrl.errorMsgsOn(false);
//		MsgCtrl.msgln(this, "\ttestMockRace()");
//		// NORMAL
//		// Create an instance of the inner class after setup() redirects to
//		// class loader to
//		// the test directory tree
//		MockRace tr = (MockRace) Race.createRace("MockRace");
//		assertNotNull(tr);
//		assertTrue(tr.getRaceName().equals("MockRace"));
//	}

	/**
	 * Test the various Races that can be created
	 * 
	 * @Normal Race.createRace(String ) implemented classes checked
	 * @Null Race.createRace(String ) N/A
	 * @Error Race.createRace(String ) unimplemented classes checked
	 */
	public void testCreateRace() 
	{
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestCreateRace()");

		// No mocks needed so restore original package name (non-testing version)
		Chronos.setPackageName(_originalPkgName);
		// The setUp() method following will restore to the testing package again

		// NORMAL
		// Human and Dwarf are only classes implemented now
		Race aRace = null;
		for (int k = 0; k < 2; k++) {
			aRace = Race.createRace(_races[k]);
			assertNotNull(aRace);
			assertTrue(aRace.getRaceName().equals(_races[k]));
		}
		// ERROR
		// These classes are not implemented yet
		for (int k = 2; k < _races.length; k++) {
			MsgCtrl.errMsg("\tExpected error: ");
			aRace = Race.createRace(_races[k]);
			assertNull(aRace);
		}
	}

	// TODO: Move this test to the playing module. It is not part of Character
	// initialization.
	// /** Test that the traits are adjusted for the proper age bracket.
	// /** Adjust the prime traits based on age, regardless of race. <br>
	// * Algorithm:
	// * <BL>
	// * <LI> Young Adult: -1WIS, +1 CON </LI>
	// * <LI> Mature: +1 STR, +1WIS </LI>
	// * <LI> Middle-Aged: -1 STR, -1 CON, +1 INT, +1 WIS </LI>
	// * <LI> Old: -2 STR, -2 DEX, -1 CON, +1 WIS </LI>
	// * <LI> Venerable: -1 STR, -1 DEX, -1 CON, +1 INT, +1 WIS </LI>
	// * </BL>
	// * Net effects over a lifetime (which affects prime trait generation) are:
	// <br>
	// * STR = -3; INT = +2; WIS = +3; CON = -2; DEX = -3; CHR = 0 <br>
	// * No checking is done on return values; any value is valid from the
	// source method
	// *
	// * @Normal Race.adjTraitsForAge(int[] traitList) check various valid
	// ranges
	// * @Boundary Race.adjTraitsForAge(int[] traitList) zero trait value as
	// input
	// * @Null Race.adjTraitsForAge(int[] traitList) null input traits
	// * @Error Race.adjTraitsForAge(int[] traitList) can't find age category
	// */
	// public void testAdjTraitsForAge()
	// {
	// MsgCtrl.auditMsgsOn(false);
	// MsgCtrl.msgln(this, "\ttestAdjTraitsForAge()");
	// // Turn off error messages once this test passes
	// MsgCtrl.errorMsgsOn(false);
	//
	// // STR : INT : WIS : CON : DEX : CHR
	// // Based on these categories...
	// final String[] ageCats = {
	// "Young Adult", "Mature", "Middle-Aged", "Old", "Venerable"};
	// // and a starting set of traits all 11's, these traits will change to...
	// // Young Adult: -1 WIS, +1 CON
	// final int[] yadultTraits = {11, 11, 10, 12, 11, 11};
	// // Mature: +1 STR, +1WIS
	// final int[] matureTraits = {12, 11, 12, 11, 11, 11};
	// // Middle-Aged: -1 STR, -1 CON, +1 INT, +1 WIS
	// final int[] midagedTraits = {10, 12, 12, 10, 11, 11};
	// // Old: // -2 STR, -2 DEX, -1 CON, +1 WIS
	// final int[] oldTraits = {9, 11, 12, 10, 9, 11};
	// // Young Venerable: // -1 STR, -1 DEX, -1 CON, +1 INT, +1 WIS
	// final int[] venTraits = {10, 12, 12, 10, 10, 11};
	//
	// // NORMAL cases
	// // Beware! the method under test changes the start trait vales
	// // Reset to original values for this test
	// int[] resultSet = new int[6];
	// int[] testSet = new int[6];
	// // Copy the attrbute set into the set used for testing, then throw it
	// away
	// System.arraycopy(_startTraits, 0, testSet, 0, 6);
	//
	// // Indivudal age categories
	// resultSet = _mr.adjTraitsForAge(testSet, ageCats[0]);
	// assertTrue(isTraitsEqual(yadultTraits, resultSet));
	// System.arraycopy(_startTraits, 0, testSet, 0, 6);
	//
	// resultSet = _mr.adjTraitsForAge(testSet, ageCats[1]);
	// assertTrue(isTraitsEqual(matureTraits, resultSet));
	// System.arraycopy(_startTraits, 0, testSet, 0, 6);
	//
	// resultSet = _mr.adjTraitsForAge(testSet, ageCats[2]);
	// assertTrue(isTraitsEqual(midagedTraits, resultSet));
	// System.arraycopy(_startTraits, 0, testSet, 0, 6);
	//
	// resultSet = _mr.adjTraitsForAge(testSet, ageCats[3]);
	// assertTrue(isTraitsEqual(oldTraits, resultSet));
	// System.arraycopy(_startTraits, 0, testSet, 0, 6);
	//
	// resultSet = _mr.adjTraitsForAge(testSet, ageCats[4]);
	// assertTrue(isTraitsEqual(venTraits, resultSet));
	// System.arraycopy(_startTraits, 0, testSet, 0, 6);
	//
	// // STR = -3; INT = +2; WIS = +3; CON = -2; DEX = -3; CHR = 0
	// // Net life for midrange traits (starting at 11)
	// final int[] netTraits = {8, 13, 14, 9, 8, 11};
	// // Cumulative value for net effects for all age categories (life span of
	// Person)
	// int k = 0;
	// do {
	// resultSet = _mr.adjTraitsForAge(testSet, ageCats[k]);
	// k++;
	// } while (k < ageCats.length);
	// assertTrue(isTraitsEqual(netTraits, resultSet));
	//
	// // Extreme Low -- apply all age categories successively from low traits
	// final int[] accLowTraits = {5, 10, 11, 6, 5, 8};
	// k = 0;
	// do {
	// resultSet = _mr.adjTraitsForAge(_startLowTraits, ageCats[k]);
	// k++;
	// } while (k < ageCats.length);
	// assertTrue(isTraitsEqual(accLowTraits, resultSet));
	//
	// // Extreme High -- apply all age categories successively from high traits
	// final int[] accHighTraits = {15, 20, 21, 16, 15, 18};
	// k = 0;
	// do {
	// resultSet = _mr.adjTraitsForAge(_startHighTraits, ageCats[k]);
	// k++;
	// } while (k < ageCats.length);
	// assertTrue(isTraitsEqual(accHighTraits, resultSet));
	//
	// // NULL case: no test data is passed
	// MsgCtrl.errMsg(this, "\tExpected error:");
	// assertNull(_mr.adjTraitsForAge(null, "null case"));
	//
	// // ERROR case: missing age category returns input without change
	// MsgCtrl.errMsg(this, "\tExpected error:");
	// resultSet = _mr.adjTraitsForAge(_startTraits, "no age cat");
	// assertTrue(isTraitsEqual(_startTraits, resultSet));
	//
	// // BOUNDARY: Start with two 0 value traits
	// // Starting sets to compare against expected value sets
	// // Over time, each of these traits will go to (or remain at) 0
	// final int[] lifetimeTraits = {-3, 2, 3, -2, -3, 0};
	// final int[] zeroTraits = {0, 0, 0, 0, 0, 0};
	// System.arraycopy(zeroTraits, 0, testSet, 0, 6);
	//
	// // Cumulative value for net effects for all age categories (life span of
	// Person)
	// k = 0;
	// MsgCtrl.msgln("\tEvolving traits");
	// do {
	// resultSet = _mr.adjTraitsForAge(testSet, ageCats[k]);
	// MsgCtrl.msg("\t" + ageCats[k] + " = ");
	// for (int p : resultSet) {
	// MsgCtrl.msg("\t" + p);
	// }
	// MsgCtrl.msgln("");
	// k++;
	// } while (k < ageCats.length);
	// assertTrue(isTraitsEqual(lifetimeTraits, resultSet));
	// }

//	/**
//	 * Adjust the prime traits based on gender, regardless of race. <br>
//	 * Algorithm: Males are default, so no changes are made to the traits.
//	 * Females are given +1 CON, +1 CHR, but -1 STR
//	 * 
//	 * @Normal Race.adjTraitsForGender(int[] traitList) traverse groups of
//	 *         values
//	 * @Error Race.adjTraitsForGender(int[] traitList) invalid or missing gender
//	 * @Null Race.adjTraitsForGender(int[] traitList) null input parm
//	 */
//	public void testAdjTraitsForGender() {
//		MsgCtrl.auditMsgsOn(false);
//		MsgCtrl.msgln(this, "\ttestAdjTraitsForGender()");
//		// Turn off error messages once this test passes
//		MsgCtrl.errorMsgsOn(false);
//
//		// Start low and adjust for male and female
//		final int[] femLowTraits = { 7, 8, 8, 9, 8, 9 };
//		final int[] femMedTraits = { 10, 11, 11, 12, 11, 12 };
//		final int[] femHighTraits = { 17, 18, 18, 19, 18, 19 };
//
//		// NORMAL cases
//		AttributeList resultSet = null;
//		// Test Male and Female adjustments for low traits
//		resultSet = _mr.adjTraitsForGender(_startLowTraits, MALE);
//		assertTrue(_startLowTraits.equals(resultSet));
//		resultSet = _mr.adjTraitsForGender(_startLowTraits, FEMALE);
//		assertTrue(new AttributeList(femLowTraits).equals(resultSet));
//
//		// Test Male and Female adjustments for medium traits
//		resultSet = _mr.adjTraitsForGender(_startTraits, MALE);
//		assertTrue(_startTraits.equals(resultSet));
//		resultSet = _mr.adjTraitsForGender(_startTraits, FEMALE);
//		assertTrue(new AttributeList(femMedTraits).equals(resultSet));
//
//		// Test Male and Female adjustments for high traits
//		resultSet = _mr.adjTraitsForGender(_startHighTraits, MALE);
//		assertTrue(_startHighTraits.equals(resultSet));
//		resultSet = _mr.adjTraitsForGender(_startHighTraits, FEMALE);
//		assertTrue(new AttributeList(femHighTraits).equals(resultSet));
//
//		// NULL case
//		MsgCtrl.errMsg(this, "\tExpected error:");
//		assertNull(_mr.adjTraitsForGender(null, MALE));
//
//		// // ERROR case: missing or invalid gender
//		// MsgCtrl.errMsg(this, "\tExpected error:");
//		// resultSet = _mr.adjTraitsForGender(_startTraits, "No Gender");
//		// assertTrue(Utilities.isTraitsEqual(_startTraits, resultSet));
//	}

	// /** Test that certain ages fall into the proper age bracket.
	// Interestingly, there are no
	// * age categories for a mock Race (unlike the derived classes), so they
	// are arbitrarily
	// * defined in this test.
	// *
	// * @Normal Race.calcAgeCategory(long curAge, int[] ageBrackets) ok
	// * @Error Race.calcAgeCategory(long curAge, int[] ageBrackets) too young
	// * @Null Race.calcAgeCategory(long curAge, int[] ageBrackets) no age
	// bracket
	// */
	// public void testCalcAgeCategory()
	// {
	// MsgCtrl.auditMsgsOn(false);
	// MsgCtrl.msgln(this, "\ttestCalcAgeCategory()");
	// // Turn off error messages once this test passes
	// MsgCtrl.errorMsgsOn(false);
	//
	// // Get the exact names of the age categories
	// String[] ageCat = Age.getAgeCategories();
	//
	// // Top values of the age brackets (in years) and corresponds to
	// // Young Adult : 16-21; Mature: 22 - 40; Middle-Age: 41-60;
	// // Old = 60-75; Venerable: 75+
	// final int[] testBracket = {21, 40, 60, 75};
	// // Test values are longs, but double are used in the tests to allow
	// fractional years
	// // The first test value is the minimum age of any Person
	// final double[] testAge =
	// {16, 17, 20.9, 21, 30, 35, 40, 48, 55, 62.5, 69, 70.1, 80, 299};
	// // Young Adult Mature Middle-Age Old Venerable
	//
	// // Resulting age brackets descriptions
	// final String[] expectedCat = {
	// ageCat[0], ageCat[0], ageCat[0], ageCat[0], ageCat[1], ageCat[1],
	// ageCat[1],
	// ageCat[2], ageCat[2], ageCat[3], ageCat[3], ageCat[3], ageCat[4],
	// ageCat[4]
	// };
	//
	// // NORMAL cases, including max range of venerable
	// // Match the ages with the proper category
	// Age.CATEGORY desc = null;
	// int COMP_SIZE = 3;
	// try {
	// for (int k=0; k < testAge.length; k++) {
	// // Convert years old to seconds old
	// desc = Age.calcAgeCategory((long)(testAge[k] * Chronos.SECS_PER_YEAR),
	// testBracket);
	// // Only the first 3 chars of the enum and String name need be compared;
	// // the others won't because of underscores and hyphens
	// String ageStr = expectedCat[k].toUpperCase().substring(0, COMP_SIZE);
	// String descStr = desc.name().substring(0, COMP_SIZE);
	// MsgCtrl.msg(this, "Age " + testAge[k] + " = " + descStr);
	// MsgCtrl.msgln("\tExpecting " + ageStr);
	// assertEquals(ageStr, descStr);
	// }
	// // ERROR: Anyone less than MIN_AGE year old should error out
	// MsgCtrl.errMsg(this, "\tExpected errors:");
	// assertNull(_mr.calcAgeCategory(1L * Chronos.SECS_PER_YEAR, testBracket));
	// assertNull(_mr.calcAgeCategory(0L, testBracket));
	// assertNull(_mr.calcAgeCategory(-1L * Chronos.SECS_PER_YEAR,
	// testBracket));
	// assertNull(_mr.calcAgeCategory(Age.MINIMUM_AGE-1L, testBracket));
	//
	// // ERROR -- below minimum age
	// MsgCtrl.errMsg(this, "\tExpected error:");
	// assertNull(_mr.calcAgeCategory(12L * Chronos.SECS_PER_YEAR, null));
	//
	// // NULL: Missing age bracket
	// MsgCtrl.errMsg(this, "\tExpected error:");
	// assertNull(_mr.calcAgeCategory(50L * Chronos.SECS_PER_YEAR, null));
	// }
	// catch (ChronosException ex) {
	// MsgCtrl.errMsgln(this, ex.getMessage());
	// }
	// }

//	/**
//	 * Calculate the base movement speed, based on Action Points and height.
//	 * Algorithm: For each MVMT_MOD Action Points (STR+DEX), the person can move
//	 * one more block movement, to a maximum of MAX_MOVEMENT. Movement is given
//	 * +1/-1 adjustment for extra tall/short Persons. MockRace is used for
//	 * testing, so arbitrary values are given for heights.
//	 * 
//	 * @Normal Race.calcBaseMovement(int ap) ok
//	 * @Error Race.calcBaseMovement(int ap) worked within Normal test loop
//	 * @Null Race.calcBaseMovement(int ap) N/A
//	 */
//	public void testCalcBaseMovement() {
//		// Turn off normal error messages from Race class
//		MsgCtrl.auditMsgsOn(false);
//		MsgCtrl.msgln(this, "\ttestCalcBaseMovement()");
//		// Turn off error messages once this test passes
//		MsgCtrl.errorMsgsOn(false);
//
//		// Tests values to test movement
//		final int[] aps = { -1, 0, 1, 2, 3, 10, 15, 17, 25, 33, 55 };
//		final int[] bmResult = { -1, 0, 1, 1, 1, 2, 2, 3, 4, 5, 5 }; // [1-5]
//		final int[] bmLoResult = { -1, 0, 1, 1, 1, 1, 1, 2, 3, 4, 4 }; // [1-4]
//		final int[] bmHiResult = { -1, 0, 2, 2, 2, 3, 3, 4, 5, 6, 6 }; // [2-6]
//		/** Movement penalty for short Persons */
//		final int PENALTY_HEIGHT = 48;
//		/** Movement bonus for tall Persons */
//		final int BONUS_HEIGHT = 78;
//		/** Non-adjusting Height */
//		final int AVG_HEIGHT = 60;
//
//		// NORMAL cases
//		// Average height test
//		int bm = Constants.UNASSIGNED;
//		for (int k = 0; k < aps.length; k++) {
//			// ERROR cases inner loop
//			if (k < 2) {
//				MsgCtrl.errMsg("\tExpected error: ");
//			}
//			bm = _mr.calcBaseMovement(aps[k], AVG_HEIGHT);
//			assertTrue(bm == bmResult[k]);
//		}
//		// Short height test
//		bm = Constants.UNASSIGNED;
//		for (int k = 0; k < aps.length; k++) {
//			// ERROR cases inner loop
//			if (k < 2) {
//				MsgCtrl.errMsg(this, "\tExpected error:");
//			}
//			bm = _mr.calcBaseMovement(aps[k], PENALTY_HEIGHT);
//			assertTrue(bm == bmLoResult[k]);
//		}
//		// Tall height test
//		bm = Constants.UNASSIGNED;
//		for (int k = 0; k < aps.length; k++) {
//			// ERROR cases inner loop
//			if (k < 2) {
//				MsgCtrl.errMsg(this, "\tExpected error:");
//			}
//			bm = _mr.calcBaseMovement(aps[k], BONUS_HEIGHT);
//			assertTrue(bm == bmHiResult[k]);
//		}
//	}
//
//	/**
//	 * Generate a series of strings for a value that falls within a certain set
//	 * of ranges
//	 * 
//	 * @Normal Race.testRangeDesc(double value, double minValue, ok double
//	 *         maxValue, String[] descriptors)
//	 * @Normal Race.testRangeDesc(double value, double minValue, ok double
//	 *         maxValue, String[] descriptors)
//	 * @Normal Race.testRangeDesc(double value, double minValue, double
//	 *         maxValue, String[] descriptors) n/a
//	 */
//	public void testRangeDesc1() {
//		// This method is tested; can turn off messaging now
//		MsgCtrl.auditMsgsOn(false);
//		MsgCtrl.msgln(this, "\ttestRangeDesc1()");
//		MsgCtrl.errorMsgsOn(false);
//
//		// Three categories (for an odd number)
//		final String[] ranges = { "low", "medium", "high" };
//		final double[] vals = { 1, 5, 10, 20, 60, 89, 90 };
//		final String[] testResults = { "low", "low", "low", "low", "medium",
//				"high" };
//
//		// NORMAL Test target should return these values after grouping them
//		// into 1 of 3 ranges
//		for (int k = 0; k < vals.length - 1; k++) {
//			// args: target value, minvalue, maxvalue, set of ranges
//			String result = _mr.testRangeDescriptor(vals[k], vals[0],
//					vals[vals.length - 1], ranges);
//			MsgCtrl.msgln(vals[k] + " => " + result);
//			assertTrue(testResults[k].equals(result));
//		}
//		// ERROR Deliberate failed tests (suppress the error messages)
//		MsgCtrl.errMsg("\tExpected error: ");
//		assertNull(_mr.testRangeDescriptor(91, vals[0], vals[vals.length - 1],
//				ranges));
//		MsgCtrl.errMsg("\tExpected error: ");
//		assertNull(_mr.testRangeDescriptor(299, vals[0], vals[vals.length - 1],
//				ranges));
//		MsgCtrl.errMsg("\tExpected error: ");
//		assertNull(_mr.testRangeDescriptor(-1, vals[0], vals[vals.length - 1],
//				ranges));
//		MsgCtrl.errMsg("\tExpected error: ");
//		assertNull(_mr.testRangeDescriptor(0, vals[0], vals[vals.length - 1],
//				ranges));
//	} // end of test method
//
//	/**
//	 * Generate a series of strings for a value that falls within a certain set
//	 * of ranges
//	 * 
//	 * @Normal Race.testRangeDesc(double value, double minValue, ok double
//	 *         maxValue, String[] descriptors)
//	 * @Normal Race.testRangeDesc(double value, double minValue, ok double
//	 *         maxValue, String[] descriptors)
//	 * @Normal Race.testRangeDesc(double value, double minValue, double
//	 *         maxValue, String[] descriptors) n/a
//	 */
//	public void testRangeDesc2() {
//		// This method is tested; can turn off messaging now
//		MsgCtrl.auditMsgsOn(false);
//		MsgCtrl.msgln(this, "\ttestRangeDesc2()");
//		MsgCtrl.errorMsgsOn(false);
//		// Six categories (for an even number) with non-integral range
//		// boundaries
//		final String[] ranges =
//		// 0-21.5 21.6-43 43.1-64.5 64.6-86 86.1-107.6 107.7-129.0
//		{ "alpha", "beta", "gamma", "delta", "eta", "phi" };
//		final double[] vals = { 0, 5, 10, 20, 60, 64, 65, 66, 78, 114, 129 };
//		final String[] testResults = { "alpha", "alpha", "alpha", "alpha",
//				"gamma", "gamma", "delta", "delta", "delta", "phi", "phi" };
//		// NORMAL Test target should return these values after grouping them
//		// into one of the three
//		// ranges
//		for (int k = 0; k < vals.length - 1; k++) {
//			// args: target value, minvalue, maxvalue, set of ranges
//			String result = _mr.testRangeDescriptor(vals[k], vals[0],
//					vals[vals.length - 1], ranges);
//			MsgCtrl.msgln(vals[k] + " => " + result);
//			assertTrue(testResults[k].equals(result));
//		}
//		// NORMAL Deliberate failed tests (suppress the error messages)
//		MsgCtrl.errMsg("\tExpected error: ");
//		assertNull(_mr.testRangeDescriptor(129.1, vals[0],
//				vals[vals.length - 1], ranges));
//		MsgCtrl.errMsg("\tExpected error: ");
//		assertNull(_mr.testRangeDescriptor(129.2, vals[0],
//				vals[vals.length - 1], ranges));
//		MsgCtrl.errMsg("\tExpected error: ");
//		assertNull(_mr.testRangeDescriptor(-1, vals[0], vals[vals.length - 1],
//				ranges));
//		MsgCtrl.errMsg("\tExpected error: ");
//		assertNull(_mr.testRangeDescriptor(300, vals[0], vals[vals.length - 1],
//				ranges));
//		// Deliberate failed tests (suppress the error messages)
//
//	} // end of test method

	/**
	 * Ensure proper weights for male and female Characters
	 * 
	 * @Normal Race.calcWeight(int maleWt, int femaleWt)
	 */
	public void testCalcWeight() {
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestCalcWeight()");
		MsgCtrl.errorMsgsOn(false);

	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * PRIVATE METHODS THAT THE TESTS USE
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	// /** Check that an int array matches the traits expected
	// * @param expValue array of 6 traits to compare
	// * @return true if all match; else false
	// */
	// private boolean isTraitsEqual(int[] expValue, int[] testValue)
	// {
	// // Guard against non-existent trait array
	// if (expValue == null) {
	// return false;
	// }
	// // Guard against overrunning the array
	// if (expValue.length != testValue.length) {
	// return false;
	// }
	// // Compare each element of the two arrays
	// boolean retval = false;
	// // Traverse both arrays for equality
	// for (int k=0; k< testValue.length; k++) {
	// if (testValue[k] != expValue[k]) {
	// retval = false;
	// break;
	// }
	// else {
	// retval = true;
	// }
	// }
	// return retval;
	// }

	// -------------------------------------------------------------------------
	// QA SUPPORT
	// -------------------------------------------------------------------------
	/**
	 * Tests that are not needed for various reasons, mostly setters and getters
	 * 
	 * @Not_Needed Race.Race() simple random call
	 * @Not_Needed Race.adjTraitsForRace(int[] defValues) wrapper
	 * @Not_Needed Race.calcHeight(int maleHt, int femaleHt) wrapper
	 * @Not_Needed Race.calcWeight(int maleWt, int femaleWt) wrapper
	 * @Not_Needed Race.calcMaxSatiety(int satietyRate)
	 * @Not_Needed Race.getAge() getter
	 * @Not_Needed Race.getAgeCategory() getter
	 * @Not_Needed Race.getGender() getter
	 * @Not_Needed Race.getHeight() getter
	 * @Not_Needed Race.getHungerState() getter
	 * @Not_Needed Race.getMagicAttackAdj() getter
	 * @Not_Needed Race.getMvmt() getter
	 * @Not_Needed Race.getRaceName() getter
	 * @Not_Needed Race.getRaceTypes() getter
	 * @Not_Needed Race.getToHitMissile() getter
	 * @Not_Needed Race.getWeight() getter
	 * @Not_Needed Race.setGender(String gender) setter
	 * @Not_Needed Race.updateToHitAdjMissile(int defAdj, int dex) setter
	 */
	public void testNotNeeded() {
	}

	public void testNotImplemented() {
	}
	/**
	 * Tests that are not yet implemented Public and protected methods
	 * 
	 * @Not_Implemented Race.initAge(int topYoungster)
	 * @Not_Implemented Race.findRangeDescriptor(double value, double minValue,
	 *                  double maxValue, String[] descriptors)
	 * @Not_Implemented Race.packShuttle(EnumMap<PersonFields, String> fields)
	 * @Not_Implemented Race.verifyTraits(int[] traits, int[] minList, int[]
	 *                  maxList)
	 */

} // end of TestRace class

