/**
 * TestHumanPeasant.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package test.pdc;

import java.util.ArrayList;

import junit.framework.TestCase;
import mylib.MsgCtrl;
import pdc.character.Person;
import chronos.pdc.AttributeList;
import chronos.pdc.Race;
import chronos.pdc.Skill;

/**
 * Tests the Peasant Hero object. Private methods of Peasant are tested through
 * a MockPerson inner class that acts as a public wrapper for the private
 * methods. The public interface is tested through an instantiation of the
 * Person class.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Feb 13 2009 // original
 *          <DD>
 *          <DT>Build 1.1 Feb 28 2009 // revised for Race and Klass re-design
 *          <DD>
 *          <DT>Build 1.2 Apr 29 2009 // add MockPerson private testing
 *          <DD>
 *          <DT>Build 1.3 May 11 2009 // added Age calcs, which affects traits
 *          and most
 *          <DD>
 *          <DT>Build 2.0 April 10 2011 // TAA Starting rebuild to match new
 *          Person class
 *          <DD>
 *          secondary calculations
 *          <DD>
 *          </DL>
 */
public class TestHumanPeasant extends TestCase {
	/* GENERIC METADATA CONSTANTS */
	// Specific independent Person constants; ratios have large precisions
	private final double LOW_SIGMA = 5.0 / 6.0; // multiplier for 16% under
												// average
	private final double HI_SIGMA = 7.0 / 6.0; // multiplier for 16% over
												// average

	private final int STARTING_HP = 10; // peasant's starting hit points
    // private final double STARTING_GOLD = 15.7; // peasant's starting gold
    //
    // // Limit ranges for movement calc
    // private final int DEFAULT_MVMT = 4;
    // private final int PENALTY_HEIGHT = 48;
    // private final int BONUS_HEIGHT = 78;
    // private final double AVG_TRAIT = 11.5;
    // private final int STR = 0;
    // private final int INT = 1;
    // private final int WIS = 2;
    // private final int CON = 3;
    // private final int DEX = 4;
    // private final int CHR = 5;

	/* HUMAN METADATA CONSTANTS */
	// Specific Human constants
	private final int AVG_MALE_HEIGHT = 70; // inches
	private final int AVG_MALE_WEIGHT = 175; // pounds

//	private final int AVG_FEMALE_HEIGHT = 64; // inches
//	private final int AVG_FEMALE_WEIGHT = 130; // pounds

	/* SPECIFIC PEASANT ATTRIBUTES TO COMPARE WHEN TESTING */
	// Human Peasant attributes for Male
	private final String _hisName = "Clyde P. Falsoon";
	private final String _hisGender = "Male";
	private final String _hisOccup = "Gambler";
	private final String _hisHair = "bald"; // bald

	// private int[] _defaultTrait = {11, 11, 11, 11, 11, 11};
	// after gender & age adjustment, -1 WIS, +1 CON
	private int[] _guyTrait = { 11, 11, 10, 12, 11, 11 };

//	private int _hisAP = _guyTrait[STR] + _guyTrait[DEX]; // for STR + DEX = 22
//	private int _hisHeight = 78;
//	private int _hisWeight = 147;
//	private int _hisWtAllow = 2249; // oz
	private double _startingAge = 17.0; // Humans start at 85% of YoungAdult

//	private int _hisMvmt = 4; // 3 plus height bonus
//	private double mOVERBEARING = 88.0;
//	private int mGRAPPLING = 22;
//	private int mPUMMELING = 22;

	// Human Peasant attributes for Female
	private final String _herName = "Ann Pennington";
	private final String _herGender = "Female";
	private final String _herOccup = "Weaver";
	private final String _herHair = "blonde";
	// After gender adjustment: -1 STR, +1 CON, +1 CHR
	// After age adjustment: -1 WIS, +1 CON
	private int[] _galTrait = { 10, 11, 10, 13, 11, 12 }; // female-adjusted
															// traits
//	private int _herAP = _galTrait[STR] + _galTrait[DEX]; // for STR + DEX = 21
//
//	private int _herMvmt = 3;
//	private int fOVERBEARING = 66;
//	private int fGRAPPLING = 21;
//	private int fPUMMELING = 21;

	// General expected values for all Clyde and Ann in this test class
	private final String _raceName = "Human";
	private final String _klassName = "Peasant";
//	private int _maxLangs = 2; // Humans can know Common and one other
//	private int _langsKnown = 1; // Humans start with knowing Common only
	private int _magicAttackAdj = 0; // for Human Peasant
	private int _hitPointAdj = 0; // for a CON = 10
	private int _toHitAdjMissile = 0; // for a DEX = 10
	private int _acAdj = 0; // for a DEX = 10

//	private int _herWtAllow = 1516; // oz
	private int _inventoryOzLoad = 445;
//	private double _lbCarried = 26.5;

	Person _pGuy = null;
	Person _pGal = null;
	Person.MockPerson _mock = null; // accesses both genders

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ LET THE TESTS
	 * BEGIN! ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/** Create the objects needed for testing; called before each test */
	protected void setUp() {
		MsgCtrl.auditMsgsOn(true);
		try {
			// Pass both objects to Person constructor to create beginning
			// Kharacter
			_pGuy = new Person(_hisName, Race.MALE, _hisOccup, _hisHair,
					_raceName, _klassName);
			// Pass both objects to Person constructor to create beginning
			// Kharacter
			_pGal = new Person(_herName, Race.FEMALE, _herOccup, _herHair,
					_raceName, _klassName);
		} catch (InstantiationException ex) {
			System.err.println(ex.getMessage());
			return;
		}
		// Create the mock object for testing Person's private methods
		_mock = _pGuy.new MockPerson();
		MsgCtrl.auditMsgsOn(true);
	}

	/** Release the objects created during setup; called after each test */
	protected void tearDown() {
		// Clear out Person and MockPerson
		_pGuy = null;
		_pGal = null;
		_mock = null;
	}

	/*
	 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * SPECIFIC PERSON TEST: Male Human Peasant
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Male Human Peasant was created, test for proper attributes Setup creates
	 * the Person and MockPerson objects
	 */
	public void testMaleHumanPeasant() {
		MsgCtrl.auditMsgsOn(true);
		MsgCtrl.msgln(this, "testMaleHumanPeasant()");
		// Person is already created; dump internals as a check
		// _pGuy.dump();

		// 1. Profile Data
		assertTrue(_pGuy.getName().equalsIgnoreCase(_hisName));
		assertTrue(_pGuy.getGender().equalsIgnoreCase(_hisGender));
		assertTrue(_mock.getHairColor().equalsIgnoreCase(_hisHair));
		assertEquals(_pGuy.getOccupation().getName(), _hisOccup);
		assertTrue(_pGuy.getRaceName().equalsIgnoreCase(_raceName));
		assertTrue(_pGuy.getKlassName().equalsIgnoreCase(_klassName));

		// -- Calculate Person's Age and Bracket: Race Dependent
		// All Person's start at 17 years of age, Young Adult
		assertTrue(_pGuy.getAge() == _startingAge);

		// 2. Person Traits: all young males should be 11 except for WIS (10)
		// and CON (12)
		assertEquals(new AttributeList(_guyTrait), _pGuy.getTraits());

		// 3. Languages (INT): For a human, language list contains only Common,
		// and one other learnable
		assertTrue(_pGuy.knowsLanguage("Common") == true);
		// assertTrue(_pGuy.getMaxLangs()==_maxLangs);
		// assertTrue(_pGuy.getLangsKnown()==_langsKnown);

		// 4. WIS modifier: Magical Attack Adjusment
		assertTrue(_pGuy.getMagicAttackMod() == _magicAttackAdj);

		// 5. CON Modifers: HitPointAdj
		assertTrue(_pGuy.getHitPointAdj() == _hitPointAdj);

		// 6. DEX Modifiers: ToHitAdjMissile and ArmorClassAdj
		assertTrue(_pGuy.getToHitMissileMod() == _toHitAdjMissile);
		assertTrue(_pGuy.getACMod() == _acAdj);

		// 7. HEIGHT and WEIGHT (unchanging attributes, but have random
		// calculation)
		// Check limits of 59" (4'11") to 81" (6'11") (Average = 70" = 5'10")
		double htLimitLo = AVG_MALE_HEIGHT * LOW_SIGMA;
		double htLimitHi = AVG_MALE_HEIGHT * HI_SIGMA;
		// assertTrue(_pGuy.getHeight() == _hisHeight);
		int ht = _pGuy.getHeight();
		assertEquals(_pGuy.getHeight(), 70, 11);
		assertTrue((ht >= htLimitLo) && (ht <= htLimitHi));
		// Check limits of 147# to 203# (Average = 175#)
		double wtLimitLo = AVG_MALE_WEIGHT * LOW_SIGMA;
		double wtLimitHi = AVG_MALE_WEIGHT * HI_SIGMA;
		int wt = _pGuy.getWeight();
		assertTrue((wt >= wtLimitLo) && (wt <= wtLimitHi));

		// 8. STR Modifiers: toHitAdjMelee, DamageAdj, and WeightAllowance
		// assertTrue(_pGuy.getToHitMelee()== 0);
		// assertTrue(_pGuy.getDamageAdj()== 0);
		// STR / AVG_STR = 11/11.5 = .957 of Gal's weight
		// assertTrue(_pGuy.getWtAllowance() == _hisWtAllow);

		// 8. HUNGER
		// All Characters are initialized as FULL -- nohunger
		assertTrue(_pGuy.findHungerState().equals("FULL"));

		// 9. EXPERIENCE
		// Experience is measured in XP and grouped by Level; all start at 0
		assertTrue((_pGuy.getLevel() == 0) && (_pGuy.getXP() == 0));

		// 10. HIT POINTS: All Peasants start with same value
		// Hit Points are Klass-dependent values; for Human Peasant, set to a
		// fixed value
		assertTrue(_pGuy.getHP() == STARTING_HP);

		// 12. GOLD: The Peasant gets the default minimum, and adds to his load.
		// Inventory test to check weight for inventory load
		assertTrue(_pGuy.getInventoryWeight() == _inventoryOzLoad);
		// Test for proper starting gold, and a new load once he has it
		// assertEquals(_pGuy.getGold(), STARTING_GOLD, 0.05);
		// assertEquals(_pGuy.getGoldBanked(), 0.0, 0.05);
		assertTrue(_pGuy.calcLoad() == _inventoryOzLoad);

		// 13. ACTION POINTS: Calc AP for movement, overbearing, grappling, and
		// pummeling
		// int ap = _pGuy.calcAP();
		// assertTrue(ap == _hisAP);
		// double speed = _pGuy.calcSpeed();
		// assertEquals(speed, _hisMvmt, .1);
		// assertEquals(_pGuy.calcOverbearing(_hisAP, _hisWtAllow,
		// _inventoryOzLoad),
		// mOVERBEARING, .1);
		// assertTrue(_pGuy.calcGrappling(_hisAP) == _hisAP);
		// assertTrue(_pGuy.calcPummeling(_hisAP) == _hisAP);

		// 14. SPECIAL ABILITIES are compiled from occupation, Race, and Klass
		// abilities
		// This human has only the Luck skill of a gambler
		assertTrue(_pGuy.hasSkill("Luck"));
		ArrayList<Skill> skills = _pGuy.getSkills(); // Luck plus readOnly skill
		assertTrue(skills.size() == 2);
		// Although all Persons have this attribute, Humans have it for 0 feet
		// assertEquals(_pGuy.getInfraDistance(), 0);

		// 15. BUILD DESCRIPTION: built from many other attributes
		// The method was throoughyl tested for various values elsewhere
		String desc = "A tall male with no hair. He is weather-beaten and tough.";
		assertTrue(_pGuy.getDescription().equals(desc));

		MsgCtrl.auditMsgsOn(true);
	}

	/*
	 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * SPECIFIC PERSON TEST: Female Human Peasant
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Test for Female Human Peasant Setup creates the Person and MockPerson
	 * objects
	 */
	public void testFemaleHumanPeasant() {
		MsgCtrl.auditMsgsOn(true);
		MsgCtrl.msgln(this, "testFemalePeasant()");
		// Person is already created; dump internals as a check
		// _pGal.dump();

		// 1. Profile Data
		assertTrue(_pGal.getName().equalsIgnoreCase(_herName));
		assertTrue(_pGal.getGender().equalsIgnoreCase(_herGender));
		assertEquals(_pGal.getOccupation().getName(), _herOccup);
		assertTrue(_pGal.getRaceName().equalsIgnoreCase(_raceName));
		assertTrue(_pGal.getKlassName().equalsIgnoreCase(_klassName));

		// -- Calculate Person's Age and Bracket: Race Dependent
		// All Person's start at 17 years of age, Young Adult
		assertTrue(_pGal.getAge() == _startingAge);

		// 2. Person Traits: all young females should be 11 except for -1 STR,
		// -1 WIS,
		// +1 CON. +1 CHR = {10, 11, 10, 13, 11, 12}; // female- and
		// age-adjusted traits
		assertEquals(new AttributeList(_galTrait), _pGal.getTraits());

		// // 3. Languages (INT): For a human, language list contains only
		// Common,
		// // and one other learnable
		// assertTrue(_pGal.knowsLanguage("Common") == true);
		// assertTrue(_pGal.getMaxLangs()==_maxLangs);
		// assertTrue(_pGal.getLangsKnown()==_langsKnown);
		//
		// // 4. WIS modifier: Magical Attack Adjusment
		// assertTrue(_pGal.getMagicAttackAdj() == _magicAttackAdj);
		//
		// // 5. CON Modifers: HitPointAdj
		// assertTrue(_pGal.getHitPointAdj() == _hitPointAdj);
		//
		// // 6. DEX Modifiers: ToHitAdjMissile and ArmorClassAdj
		// assertTrue(_pGal.getToHitAdjMissile() == _toHitAdjMissile);
		// assertTrue(_pGal.getACAdj() == _acAdj);
		//
		// // 7. HEIGHT and WEIGHT (unchanging attributes, but have random
		// calculation)
		// // Check limits of 59" (4'11") to 81" (6'11") (Average = 70" = 5'10")
		// double htLimitLo = AVG_FEMALE_HEIGHT * LOW_SIGMA;
		// double htLimitHi = AVG_FEMALE_HEIGHT * HI_SIGMA;
		// int ht = _pGal.getHeight();
		// assertTrue((ht >= htLimitLo) && (ht <= htLimitHi));
		// // Check limits of 147# to 203# (Average = 175#)
		// double wtLimitLo = AVG_FEMALE_WEIGHT * LOW_SIGMA;
		// double wtLimitHi = AVG_FEMALE_WEIGHT * HI_SIGMA;
		// int wt = _pGal.getWeight();
		// assertTrue((wt >= wtLimitLo) && (wt <= wtLimitHi));
		//
		// // 8. HUNGER
		// // All Characters are initialized as FULL -- nohunger
		// assertTrue(_pGuy.findHungerState().equals("FULL"));
		//
		// // 9. STR Modifiers: toHitAdjMelee, DamageAdj, and WeightAllowance
		// assertTrue(_pGal.getToHitMelee()== 0);
		// assertTrue(_pGal.getDamageAdj()== 0);
		// // STR / AVG_STR = 10/11.5 = .870 of Gal's weight
		// assertTrue(_pGal.getWtAllowance() == _herWtAllow);
		//
		// // 10. EXPERIENCE
		// // Experience is measured in XP and grouped by Level; all start at 0
		// assertTrue((_pGal.getLevel() == 0) && (_pGal.getXP() == 0));
		//
		// // 11. HIT POINTS: All Peasants start with same value
		// // Hit Points are Klass-dependent values; for Human Peasant, set to a
		// fixed value
		// assertTrue(_pGal.getHP() == STARTING_HP);
		//
		// // 12. GOLD: The Peasant gets the default minimum, and adds to his
		// load.
		// // Inventory test to check weight for inventory load
		// assertTrue(_pGal.getInventoryWeight() == _inventoryOzLoad);
		// // Test for proper starting gold, and a new load once he has it
		// assertEquals(_pGal.getGold(), STARTING_GOLD, 0.05);
		// assertEquals(_pGal.getGoldBanked(), 0.0, 0.05);
		// assertTrue(_pGal.calcLoad() == _inventoryOzLoad);
		//
		// // 13. ACTION POINTS: Calc AP for movement, overbearing, grappling,
		// and pummeling
		// int ap = _pGal.getAP();
		// assertTrue(ap == _herAP);
		// double speed = _pGal.calcSpeed();
		// assertEquals(speed, _herMvmt, .1);
		// assertEquals(_pGal.calcOverbearing(_hisAP, _herWtAllow,
		// _inventoryOzLoad),
		// fOVERBEARING, .1);
		// assertTrue(_pGal.calcGrappling(_herAP) == _herAP);
		// assertTrue(_pGal.calcPummeling(_herAP) == _herAP);
		//
		// // 14. SPECIAL ABILITIES are compiled from occupation, Race, and
		// Klass abilities
		// // This human has only the Luck skill of a gambler
		// assertTrue(_pGal.hasSkill("Appraise Tapestries"));
		// ArrayList<Skill> skills = _pGal.getSkills(); // Appraise plus
		// readOnly skill
		// assertTrue(skills.size() == 2);
		// // Although all Persons have this attribute, Humans have it for 0
		// feet
		// // assertEquals(_pGal.getInfraDistance(), 0);
		//
		// // 15. BUILD DESCRIPTION: built from many other attributes
		// // The method was throughll tested for various values elsewhere
		// String desc =
		// "A thin female with blonde hair. She is nothing special to look at.";
		// assertTrue(_pGal.getDescription().equals(desc));

		MsgCtrl.auditMsgsOn(true);
	}

	// /** Test the total number of languages the Person can know. Common is
	// always known,
	// * and a racial language is always known if non-Human, so maxLangs is a
	// minimum of 1 or 2.
	// * In some cases, half-Elves and half-Orcs do not know their racial
	// language.*/
	// public void testInitMaxLangs()
	// {
	// // This method is tested, can turn off message now
	// MsgCtrl.auditMsgsOn(true);
	// MsgCtrl.errorMsgsOn(true);
	// MsgCtrl.msgln(this, "\ttestInitMaxLangs()");
	//
	// int minInt = 10;
	// // NORMAL -- walk through intelligence values for Human
	// // int intelligence, ArrayList<String> langs
	// for (int k=6; k < 20; k++) {
	// int maxLangs = _mock.testInitMaxLangs(k);
	// MsgCtrl.msgln("INT = " + k +": " + maxLangs + ";\t");
	// if (k <= minInt) {
	// assertEquals(maxLangs, 1);
	// }
	// else if (k > minInt)
	// assertEquals(maxLangs, k/2-3);
	// }
	//
	// // ERROR -- invalid intelligence
	// assertEquals(_mock.testInitMaxLangs(7), 1);
	//
	// }

	// /** Test for saving a Person */
	// public void testSavePerson()
	// {
	// MsgCtrl.auditMsgsOn(true);
	// // Pass both objects to Person constructor to create beginning Kharacter
	// Person pGal = null;
	// try {
	// pGal = new Person(_herName, Race.FEMALE, _herOccup, _herHair, "Human",
	// "Peasant");
	// } catch (InstantiationException ex) {
	// System.err.println(ex.getMessage());
	// return;
	// }
	// // pGal.dump();
	// // Call the save service
	// assertTrue(pGal.save("Annie"));
	// MsgCtrl.auditMsgsOn(true);
	// }

	// TODO: Move this to Hunger object
	// /** Test satiety calculations for Human; gender and Klass are irrelevant.
	// * Set a person's weight and check that the maxSatiety is correct.
	// * A mock is needed to get to the internal Race attributes; Person only
	// provides the
	// * <code>getHunger()</code> service.
	// */
	// public void testInitSatiety()
	// {
	// MsgCtrl.auditMsgsOn(true);
	// // Create the mock; gender is irrelevant
	// Person.MockPerson _mock = _pGuy.new MockPerson();
	// // Init for 100 lbs
	// double maxSat = _mock.initSatiety(100);
	// double curSat = _mock.getCurSatiety();
	// MsgCtrl.msg("\nMax Sat = " + maxSat + "\tCurrent Sat = " + curSat);
	// assertEquals(maxSat, 1500.0, .001);
	// assertEquals(curSat, 1500.0, .001);
	// MsgCtrl.msgln("\t " + _pGuy.getHunger());
	// assertTrue(_pGuy.getHunger().equals("FULL"));
	//
	// // Init for 350 lbs
	// maxSat = _mock.initSatiety(350);
	// curSat = _mock.getCurSatiety();
	// MsgCtrl.msg("\nMax Sat = " + maxSat + "\tCurrent Sat = " + curSat);
	// assertEquals(maxSat, 5250.0, .001);
	// assertEquals(curSat, 5250.0, .001);
	// MsgCtrl.msgln("\t " + _pGuy.getHunger());
	// assertTrue(_pGuy.getHunger().equals("FULL"));
	// MsgCtrl.auditMsgsOn(true);
	// }

	// TODO: Move this to the playing module; it is not related to Character
	// initialization
	// /** Burn satiety points and check that the current satiety and hunger
	// flag changes
	// * correctly. Also check that the STR and secondary attributes change
	// accordingly as
	// * STR is decremented.
	// *
	// * A mock is needed to get to the internal Race attributes; Person only
	// provides the
	// * <code>getHunger()</code> service.
	// */
	// public void testBurnSatiety()
	// {
	// MsgCtrl.auditMsgsOn(true);
	// // Create the mock; gender is irrelevant
	// Person.MockPerson _mock = _pGuy.new MockPerson();
	// int deltaStr = 0;
	//
	// // Burn down from 100 lbs, checking each hour
	// deltaStr = burnSatiety(100, 3600, _mock);
	// MsgCtrl.msgln("\n");
	//
	// // Burn down from 100 lbs, checking each half hour
	// burnSatiety(100, 1800, _mock);
	// MsgCtrl.msgln("\n");
	//
	// // Burn down from 350 lbs, checking each hour
	// burnSatiety(350, 3600, _mock);
	// MsgCtrl.msgln("\n");
	//
	// // Burn down from 225 lbs, checking each hour
	// burnSatiety(225, 3600, _mock);
	// MsgCtrl.msgln("\n");
	//
	// MsgCtrl.auditMsgsOn(true);
	// }

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE HELPER
	 * FUNCTIONS ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	// TODO: Move this to the playing module; it is not part of Character
	// intialization
	// /** Test that the satiety points burn down in 72 hours, checking
	// periodically
	// * @param startWt how heavy is the person (in lbs) determiend maxSatiety
	// * @param ckRate how often satiety is checked; each hour = 3600 seconds
	// * @param mock the MockPerson object so internal private methods can be
	// called
	// * @return the strength loss due to hunger
	// */
	// private int burnSatiety(int startWt, int ckRate, MockPerson mock)
	// {
	// MsgCtrl.auditMsgsOn(true);
	// // Set the burnrate to 10% drop by iteration
	// long burnrate = startWt * 15; // consumption need per day for Humans
	// double maxSat = mock.initSatiety(startWt);
	// double curSat = maxSat;
	// double hRatio = 100;
	// double lastSet = 100;
	// MsgCtrl.msg("\n\tCurrent Sat = " + curSat);
	// MsgCtrl.msgln("\t " + _pGuy.getHunger());
	// int cnt = 0;
	// int deltaStr = 0;
	// // Current satiety runs from 100% to -300%, so loop through 40 iterations
	// of 10%
	// do {
	// // _mock.calcHunger(burnrate);
	// deltaStr = mock.calcHunger(ckRate);
	// // Show curSat in terms of percentage nn.n%
	// curSat = ((int)(mock.getCurSatiety() * 10.0)) /10.0;
	// // Show hungry ratio to nearest .n
	// hRatio = ((int) (curSat / maxSat * 1000.0)) / 10.0;
	// // Only print each 10% values
	// cnt++;
	// if (lastSet - hRatio > 10) {
	// lastSet = hRatio;
	// MsgCtrl.msg("[" + cnt + "]\tCurrent Sat = " + curSat + "\tRatio = " +
	// hRatio + "%" );
	// MsgCtrl.msg("\t " + _pGuy.getHunger());
	// MsgCtrl.msgln("\t STR loss = " + deltaStr);
	// }
	// } while (hRatio > -200.0);
	//
	// MsgCtrl.msg("\tCurrent Sat = " + curSat + "\tRatio = " + hRatio + "%" );
	// MsgCtrl.msg("\t " + _pGuy.getHunger());
	// MsgCtrl.msgln("\t STR loss = " + deltaStr);
	//
	// return deltaStr;
	// }


} // end of TestPeasant test class
