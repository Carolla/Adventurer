/**
 * TestHunger.java
 * Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
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
import org.junit.Before;

import pdc.character.Hunger;
import pdc.character.Hunger.MockHunger;
import pdc.character.Hunger.State;

/**
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Oct 30, 2010 // original
 *          <DD>
 *          <DT>Build 1.1 April 9, 2011 // TAA added Error/Nulls
 *          <DD>
 *          </DL>
 */
public class TestHunger extends TestCase {
	/** Object to be tested */
	Hunger _lightHunger = null;
	Hunger _averageHunger = null;
	Hunger _heavyHunger = null;

	// Create a single Mock to be shared
	MockHunger _mo = null;

	/** Several weights to run tests with, in lb */
	private double lightWt = 90.0;
	private double averageWt = 150.0;
	private double heavyWt = 300.0;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Audit messages are OFF at start of each test
		MsgCtrl.auditMsgsOn(false);
		// Error messages are ON at start of each test
		MsgCtrl.errorMsgsOn(true);
		// Create the Hunger objects for all three persons
		_averageHunger = new Hunger(averageWt);
		assertNotNull(_averageHunger);
		_lightHunger = new Hunger(lightWt);
		assertNotNull(_lightHunger);
		_heavyHunger = new Hunger(heavyWt);
		assertNotNull(_heavyHunger);
		// Create the MockHunger in the test, depending on the weight, to get
		// private attributes
		// All Hunger methods are public
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Clear the Hunger objects
		_averageHunger = null;
		_lightHunger = null;
		_heavyHunger = null;
		// Clear out whatever mock was created
		_mo = null;

		// Audit messages are OFF after each test
		MsgCtrl.auditMsgsOn(false);
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR(S) AND
	 * RELATED METHODS ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Test the constructor. Verify that all the attributes are correct for the
	 * average Person
	 * 
	 * @Normal Hunger.Hunger(int weight) ok
	 * @Error Hunger.Hunger(int weight) now
	 * @Nuil Hunger.Hunger(int weight) now
	 */
	public void testHunger() {
		// Audit messages are OFF at start of each test
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestHunger()");
		// Create the mock to get to the private attributes
		_mo = _lightHunger.new MockHunger();
		MsgCtrl.msgln("LightHunger 90#");
		_mo.display();
		// verifyHunger(Hunger hunger, double current, double max, double
		// burnRate, double lastChange
		verifyHunger(_lightHunger, 4050.0, 4050.0, 0.016, 0.0);
		MsgCtrl.msgln("");

		// Create the mock to get to the private attributes
		_mo = _averageHunger.new MockHunger();
		MsgCtrl.msgln("AverageHunger 150#");
		_mo.display();
		verifyHunger(_averageHunger, 6750.0, 6750.0, 0.026, 0.0);
		MsgCtrl.msgln("");

		// Create the mock to get to the private attributes
		_mo = _heavyHunger.new MockHunger();
		MsgCtrl.msgln("HeavyHunger 300#");
		_mo.display();
		verifyHunger(_heavyHunger, 13500.0, 13500.0, 0.052, 0.0);
		MsgCtrl.msgln("");

		// Error: try to create new hunger with a negative weight
		MsgCtrl.msgln("Error hunger: ");
		Hunger errorHunger = new Hunger(-10.0);
		 assertNull(errorHunger);
		MsgCtrl.msgln("");

        // Null: create hunger with null weight
        MsgCtrl.msgln("Null hunger: ");
        Hunger nullHunger = new Hunger(0.0);
        assertNull(nullHunger);
        MsgCtrl.msgln("");
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PUBLIC METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Show the remaining SPs for all three Persons after one day
	 * 
	 * @Normal Hunger.calcHunger(long burnTime) ok
	 * @Null Hunger.calcHunger(long burnTime) n/a
	 * @Error Hunger.calcHunger(long burnTime) n/a
	 */
	public void testCalcHunger() {
		// Turn off normal error messages from Race class
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestCalcHunger()");
		// Burn down period (hours)
		int numberHours = 24;

		// NORMAL
		// AVERAGE
		MsgCtrl.msgln("\tAverage Hunger burndown");
		_mo = _averageHunger.new MockHunger();
		double startSP = _mo.getCurrentSatiety();
		MsgCtrl.msgln("\tStarting SP  (" + 0 + ") = " + startSP);
		assertEquals(startSP, _averageHunger.calcHunger(0L), .000001);

		// Start with Strengths values for low, medium and high and allow enough
		// time
		// for satiety to reach zero.
		double curSP = _averageHunger.calcHunger(numberHours * 3600);
		MsgCtrl.msgln("\tAt end of 1st day  (" + numberHours + ") = " + curSP);
		double diff = startSP - curSP;
		MsgCtrl.msgln("\tBurnrate per day = " + diff);
		double br_day = _mo.getBurnRate() * 3600 * 24;
		assertEquals(br_day, diff, .001);
		MsgCtrl.msgln("");

		// LIGHT
		MsgCtrl.msgln("\tLight Hunger burndown");
		_mo = _lightHunger.new MockHunger();
		startSP = _mo.getCurrentSatiety();
		MsgCtrl.msgln("\tStarting SP  (" + 0 + ") = " + startSP);
		assertEquals(startSP, _lightHunger.calcHunger(0L), .000001);

		// Start with Strengths values for low, medium and high and allow enough
		// time
		// for satiety to reach zero.
		curSP = _lightHunger.calcHunger(numberHours * 3600);
		MsgCtrl.msgln("\tAt end of 1st day  (" + numberHours + ") = " + curSP);
		diff = startSP - curSP;
		MsgCtrl.msgln("\tBurnrate per day = " + diff);
		br_day = _mo.getBurnRate() * 3600 * 24;
		assertEquals(br_day, diff, .001);
		MsgCtrl.msgln("");

		// HEAVY
		MsgCtrl.msgln("\tHeavy Hunger burndown");
		_mo = _heavyHunger.new MockHunger();
		startSP = _mo.getCurrentSatiety();
		MsgCtrl.msgln("\tStarting SP  (" + 0 + ") = " + startSP);
		assertEquals(startSP, _heavyHunger.calcHunger(0L), .000001);

		// Start with Strengths values for low, medium and high and allow enough
		// time
		// for satiety to reach zero.
		curSP = _heavyHunger.calcHunger(numberHours * 3600);
		MsgCtrl.msgln("\tAt end of 1st day  (" + numberHours + ") = " + curSP);
		diff = startSP - curSP;
		MsgCtrl.msgln("\tBurnrate per day = " + diff);
		br_day = _mo.getBurnRate() * 3600 * 24;
		assertEquals(br_day, diff, .001);
		MsgCtrl.msgln("");
	}

	/**
	 * Test that the hunger rate reflects the time spent burning calories in
	 * activities
	 * 
	 * @Normal Hunger.calcHunger(long burnTime) ok
	 * @Error Hunger.calcHunger(long burnTime) ok
	 * @Null Hunger.calcHunger(long burnTime) compile error
	 */
	public void testCalcHungerBurnDown() {
		// Turn off normal error messages from Race class
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestCalcHungerBurnDown()");

		// NORMAL
		// No burn time means no change in satiety points
		_mo = _averageHunger.new MockHunger();
		double curSP = _mo.getCurrentSatiety();
		assertEquals(curSP, _averageHunger.calcHunger(0L), .000001);
		// Counterers
		long time = 0;
		int hours = 0;
		int numberHours = 4;
		int loopCnt = 72 / numberHours;

		// Start with Strengths values for low, medium and high and allow enough
		// time
		// for satiety to reach zero.
		MsgCtrl.msgln("\tAverage Hunger burndown");
		for (int k = 0; k < loopCnt; k++) {
			// current satiety should reach 0 after 72 hours
			time = 3600 * numberHours;
			curSP = _averageHunger.calcHunger(time); // show points each 4 hours
			hours = hours + numberHours;
			MsgCtrl.msg("\tCurrent SP  (" + hours + ") = " + curSP);
		}
		// The satiety points should last 72 hours
		assertEquals(_mo.getCurrentSatiety(), 0.0, .00001);
		MsgCtrl.msgln("");

		// For light people, at 4 hours intervals, the satiety points should
		// still last 72 hours
		MsgCtrl.msgln("\tLight Hunger burndown");
		_mo = _lightHunger.new MockHunger();
		// reset hours
		hours = 0;
		for (int k = 0; k < loopCnt; k++) {
			// current satiety should reach 0 after 72 hours
			time = 3600 * numberHours;
			curSP = _lightHunger.calcHunger(time); // show points each 4 hours
			hours = hours + numberHours;
			MsgCtrl.msgln("\tCurrent SP  (" + hours + ") = " + curSP);
		}
		// The satiety points should last 72 hours
		assertEquals(_mo.getCurrentSatiety(), 0.0, .00001);
		MsgCtrl.msgln("");

		// For heavy people, at 4 hours intervals, the satiety points should
		// still last 72 hours
		MsgCtrl.msgln("\tHeavy Hunger burndown");
		_mo = _heavyHunger.new MockHunger();
		// reset hours
		hours = 0;
		for (int k = 0; k < loopCnt; k++) {
			// current satiety should reach 0 after 72 hours
			time = 3600 * numberHours;
			curSP = _heavyHunger.calcHunger(time); // show points each 4 hours
			hours = hours + numberHours;
			MsgCtrl.msgln("\tCurrent SP  (" + hours + ") = " + curSP);
		}
		// The satiety points should last 72 hours
		assertEquals(_mo.getCurrentSatiety(), 0.0, .00001);
		MsgCtrl.msgln("");

		// ERROR
		// Try a time that is negative and zero
		_mo = _averageHunger.new MockHunger();
		curSP = _mo.getCurrentSatiety();
		assertEquals(curSP, _averageHunger.calcHunger(-1L), .000001);
		assertEquals(curSP, _averageHunger.calcHunger(0L), .000001);

		// NULL test causes compile error
	}

	/**
	 * Test that STR points drop as hunger grows, and that up tp 18 pts of STR
	 * is reduced to 0 after 72 hours, regardless of Person's maxSatiety. Test
	 * for STR recovery also, which will rise in large jumps as the Person eats.
	 * 
	 * @Normal Hunger.calcStrHungerDelta() ok
	 * @Error Hunger.calcStrHungerDelta() n/a
	 * @Null Hunger.calcStrHungerDelta() n/a
	 */
	public void testCalcStrHungerDelta() {
		// Turn off normal error messages from Race class
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestCalcStrHungerDelta()");

		// NORMAL -- all three weights
		// While satiety points are dropping, STR will also drop
		// Set the current satiety to some testable value
		_mo = _lightHunger.new MockHunger();
		MsgCtrl.msgln("\ttestLightweight Hunger");
		// rundownHunger(Hunger hunger, MockHunger mo, int hrs, int group)
		rundownHunger(_lightHunger, _mo, 72, 4);
		MsgCtrl.msgln("");

		// While satiety points are dropping, STR will also drop
		// Set the current satiety to some testable value
		_mo = _averageHunger.new MockHunger();
		MsgCtrl.msgln("\ttestAverageweight Hunger");
		// rundownHunger(Hunger hunger, MockHunger mo, int hrs, int group)
		rundownHunger(_averageHunger, _mo, 72, 4);
		MsgCtrl.msgln("");

		// While satiety points are dropping, STR will also drop
		// Set the current satiety to some testable value
		_mo = _heavyHunger.new MockHunger();
		MsgCtrl.msgln("\ttestHeavyweight Hunger");
		// rundownHunger(Hunger hunger, MockHunger mo, int hrs, int group)
		rundownHunger(_heavyHunger, _mo, 72, 4);
		MsgCtrl.msgln("");

		// ERROR no bad inputs applicable
		// NULL test causes compile error
	}

	/**
	 * Run the hunger rundown tests again, but this time verify the Hunger state
	 * with each changes to current satiety and STR.
	 * 
	 * @Normal Hunger.findHungerState() ok
	 * @Error Hunger.findHungerState() ok
	 * @Null Hunger.findHungerState() n/a
	 * 
	 */
	public void testFindHungerState() {
		// Turn off normal error messages from Race class
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestFindHungerState()");

		// Note that the ranges are inclusive, or exclusive;
		// FULL, and WEAK ranges are bounded on both sides intentionally
		// final int SP_FULL = 90; // [90. 100]
		// final int SP_NOT_HUNGRY = 30; // (30, 90)
		// final int SP_HUNGRY = 0; // (0, 30]
		// final int SP_WEAK = -40; // [-40. 0]
		// final int SP_FAINT = -100; // [-100, -40)
		// Starved is any percential greater than FAINT

		// Provide a few test cases and confirm correct response
		// These values are weight-INDEPENDENT
		int[] testPosSat = { 100, 99, 91, 90, 89, 45, 31, 30, 29, 1 };
		int[] testNegSat = { 0, -1, -10, -40, -41, -100, -101, -999 };
		State[] testPosState = { State.FULL, State.FULL, State.FULL,
				State.FULL, State.NOT_HUNGRY, State.NOT_HUNGRY,
				State.NOT_HUNGRY, State.HUNGRY, State.HUNGRY, State.HUNGRY };
		State[] testNegState = { State.WEAK, State.WEAK, State.WEAK,
				State.WEAK, State.FAINT, State.FAINT, State.STARVED,
				State.STARVED };
		// For average SP, show the expected STR adjustments for the
		// corresponding testNegSat
		// each 4 hours loses another STR pt
		_mo = _averageHunger.new MockHunger();
		double strLossPerHour = _mo.getMaxSatiety() / 72;
		double strLossPerSP = 18 / _mo.getMaxSatiety();
		MsgCtrl.msg("\tSTR loss per hour = " + strLossPerHour);
		MsgCtrl.msgln("\tSTR loss per SP = " + strLossPerSP);
		// SP per second = 0.026; SP per minute = 1.5625; SP per hour = 93.75
		int[] expStrAdj = { -1, -1, -2, -8, -8, -19, -19, -180 };

		// NORMAL
		// No burn time means no change in satiety points
		_mo = _averageHunger.new MockHunger();

		// Start with Strengths values for low, medium and high and allow enough
		// time
		// for satiety to reach zero.
		MsgCtrl.msg("\tAverage Hunger burndown");
		double maxSat = _mo.getMaxSatiety();
		MsgCtrl.msgln("\tmaxSat = " + maxSat);

		// For positive satiety, only state changes
		for (int k = 0; k < testPosSat.length; k++) {
			double newSat = maxSat * testPosSat[k] / 100.0;
			_mo.setCurrentSatiety(newSat);
			// check that STR doesn't change when positive satiety
			int stradj = _averageHunger.calcStrHungerDelta();
			// Get the hunger state
			State st = _averageHunger.findHungerState();
			MsgCtrl.msg("\tnewSat = " + newSat + "= " + newSat / maxSat * 100.0);
			MsgCtrl.msgln("\t= " + "\tState = " + st + "\t\tSTR Adj = "
					+ stradj);
			assertEquals(stradj, 0);
			assertEquals(st, testPosState[k]);
		}
		MsgCtrl.msgln("");

		// For negative satiety, state and STR changes
		maxSat = _mo.getMaxSatiety();
		assertEquals(testNegSat.length, expStrAdj.length);
		for (int k = 0; k < testNegSat.length; k++) {
			double newSat = maxSat * testNegSat[k] / 100.0;
			_mo.setCurrentSatiety(newSat);
			State st = _averageHunger.findHungerState();
			MsgCtrl.msg("\tnewSat = " + newSat + "= " + newSat / maxSat * 100);
			// _averageHunger.calcHunger((long) expStrAdj[k] * 3600L);
			int stradj = _averageHunger.calcStrHungerDelta();
			MsgCtrl.msg("\t= " + "\tState = " + st);
			MsgCtrl.msgln("\t= " + "\tSTR Adj = " + stradj);
			assertEquals(st, testNegState[k]);
			assertEquals(stradj, expStrAdj[k]);
		}
		MsgCtrl.msgln("");

		// ERROR -- somehoe if current satiety > max satiety by 1%
		_mo.setCurrentSatiety(_mo.getMaxSatiety() * 1.01);
		assertNull(_averageHunger.findHungerState());
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Take the attributes and compare their values. No return statement because
	 * <code>assert</code>s are called directly.
	 * 
	 * @param hunger
	 *            the Hunger object to verify
	 * @param current
	 *            current satiety points
	 * @param max
	 *            max satiety points
	 * @param burnRate
	 *            personal burnrate
	 * @param lastChange
	 *            curent satiety at last strength change
	 */
	private void verifyHunger(Hunger hunger, double current, double max,
			double burnRate, double lastChange) {
		// Create mock to access private attributes
		MockHunger mo = hunger.new MockHunger();
		assertEquals(mo.getCurrentSatiety(), current, .01);
		assertEquals(mo.getMaxSatiety(), max, .01);
		assertEquals(mo.getBurnRate(), burnRate, .01);
		// assertEquals(mo.getLastChange(), lastChange, .01);
	}

	/**
	 * Loop through hunger, showing that STR reduced 18 points in 72 hours 0
	 * hours after satiety=0, STR should be STR - 1; 24 hours after satiety=0,
	 * STR should be STR - 6; 68 hours after satiety=0, STR should by STR - 18
	 * 72 hours after satiety=0, STR should by STR - 19
	 * 
	 * @param hunger
	 *            object to use for this test
	 * @param mo
	 *            hunger object for various Character weights
	 * @param hrs
	 *            number of hours to burn
	 * @param group
	 *            number of hours between verifications
	 */
	private void rundownHunger(Hunger hunger, MockHunger mo, int hrs, int group) {
		// First ensure that current satiety is at zero
		double curSP = hunger.calcHunger(hrs * 3600);
		assertEquals(mo.getCurrentSatiety(), 0.0, .00001);
		// As long as current Satiety >= 0, no change to STR
		hunger.calcStrHungerDelta();
		assertEquals(mo.getCurrentSatiety(), 0.0, .00001);

		// Slowly countdown satiety until all STR is gone (19 pts in 72 hours
		// because of 0-basing)
		MsgCtrl.msg("\tStarting SP  = " + curSP);
		MsgCtrl.msgln("\tHunger increment for maxSatiety of "
				+ mo.getMaxSatiety() + "  = " + mo.getStrDrop());
		double oldAdj = 0;
		// Current satiety starts at 0 here
		for (int k = 0; k < hrs; k++) {
			// STR drops 1 pt each 4 hours
			if (k % group == 0) {
				double strAdj = hunger.calcStrHungerDelta();
				MsgCtrl.msg("\tCurrent SP  (" + k + " hours) = " + curSP
						+ " = " + curSP / mo.getStrDrop());
				MsgCtrl.msgln("\tSTR Adj = " + strAdj);
				assertEquals(strAdj, oldAdj - 1);
				oldAdj = strAdj;
			}
			curSP = hunger.calcHunger(3600); // show points each hour
		}
	}

	/**
	 * Tests that are not needed for various reasons, mostly setters and getters
	 * 
	 * @Not_Needed Hunger.Hunger() unused constructor
	 */
	public void testNotNeeded() {
	}

	/**
	 * Tests that are not yet implemented
	 */
	public void testNotImplemented() {
	}

} // end of TestHunger class

