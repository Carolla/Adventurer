/**
 * TestPerson.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package test.pdc;

import java.io.File;
import java.util.ArrayList;

import junit.framework.TestCase;
import mylib.Constants;
import mylib.MsgCtrl;
import pdc.character.Person;
import pdc.character.Person.MockPerson;
import chronos.Chronos;
import chronos.pdc.Race;

/**
 * Tests the abstract Person's unit methods, which apply to a combination of
 * Races, Klass, Inventory, or any other components of the Person object.
 * Methods that are specific for a Race or Klass derived type are tested in
 * their own test class. This test class is used also when the particular Race
 * or Klass is not relevant. Private methods of Person are tested through a
 * MockPerson inner class that acts as a public wrapper for the private methods.
 * The public interface is tested through an instantiation of the Person class,
 * in alphabetical order. <br>
 * See <code>Test[Race]Peasant</code> class for specific tests of the Person's
 * attributes of the methods in the order called.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 May 23 2009 // original
 *          <DD>
 *          </DL>
 */
public class TestPerson extends TestCase {
	/* Internal reusable objects */
	private Person _p = null;
	private MockPerson _mock = null;

	// Base path for all files in this project
	// final String RESOURCE_DIR =
	// "/Projects/workspace/Adventurer/src/resources/";
	final String RESOURCE_DIR = Chronos.CHRONOS_LIB_RESOURCES_PATH;

	// These attributes are bare minumum to allow access into the object methods
	private final String _hisName = "Falsoon";
	private final String _hisOccup = "Gambler";
	private final String _hisHair = "no";

	// Uses the Human race as the default class. Specific tests use the
	// implemented derived Races.
	private final String _raceName = "Human";
	private final String _klassName = "Peasant";

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ LET THE TESTS
	 * BEGIN! ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Create the objects needed for testing; called before each test. The
	 * methods are called, as independent of the Race and Klass as possible.
	 */
	protected void setUp() {
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(true);
		try {
			// Create an initial Person from scratch
			_p = new Person(_hisName, Race.MALE, _hisOccup, _hisHair,
					_raceName, _klassName);
			assertNotNull(_p);
		} catch (InstantiationException ex) {
			System.err.println(ex.getMessage());
			return;
		}
		// Create the mock object for examining the Person's private methods
		_mock = _p.new MockPerson();
		assertNotNull(_mock);
	}

	/** Release the objects created during setup; called after each test */
	protected void tearDown() {
		_p = null;
		_mock = null;
	}

	/*
	 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * CONSTRUCTOR RELATED METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Serialize the Person created in setup(), delete the object, then reload
	 * it from the file system. Works closely with savePerson().
	 * 
	 * @Normal Person.loadFile(String filename) ok
	 * @Error Person.loadFile(String filename) ok
	 * @Null Person.loadFile(String filename) ok
	 */
	public void testLoadPerson() {
		// This method is tested, can turn off message now
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestLoadperson()");

		// NORMAL The Person reference _p was created from scratch in setup()
		assertTrue(_p.save());
		_p = null;
		// The line below works, but 'new Person(filename)' with a loadFile()
		// call inside doesn't?!
		Person newCopy = new Person().loadFile(_hisName);
		assertNotNull(newCopy);
		assertTrue(newCopy.getName().equals(_hisName));

		// NULL - What happens if load is not given a filename?
		newCopy = new Person().loadFile(null);
		assertNull(newCopy);

		// ERROR What happens if load is not given a valid filename?
		newCopy = new Person().loadFile("FakeFile");
		assertNull(newCopy);
	}

	/**
	 * Serialize a Person, delete it, and then reload it from the file system.
	 * Works closely with loadPerson().
	 * 
	 * @Normal Person.savePersonString filename) ok
	 * @Error Person.savePerson(String filename) n/a
	 * @Null Person.savePerson(String filename) ok
	 */
	public void testSavePerson() {
		// This method is tested, can turn off message now
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestSavePerson()");

		final String name = "TestClyde";
		final String longpath = RESOURCE_DIR + name + ".chr";

		// NORMAL
		// Delete the expected filename, but absolute path must be used
		File ser = new File(longpath);
		ser.delete();
		// Call the save service using a different filename; save() converts
		// filename to longPath
		assertTrue(_p.save());
		// Check that the filename now exists
		assertTrue(ser.exists());
		// Now load it back in and check for proper name, race, and class
		// The line below works, but 'new Person(filename)' with a loadFile()
		// call inside doesn't?!
		Person newCopy = new Person().loadFile(name);
		assertNotNull(newCopy);
		assertTrue(newCopy.getName().equals(_hisName));
		assertTrue(newCopy.getRaceName().equals(_raceName));
		assertTrue(newCopy.getKlassName().equals(_klassName));

		// ERROR -- Not sure how to cause this

		// // NULL -- Save a null name
		// assertFalse(_p.save());
		// assertFalse(_p.save());
	}

	/**
	 * Test the initLanguages method for the Person constructor. All Races have
	 * Common; Dwarves have additional racial languag "Grokken".
	 * 
	 * @Normal Person.initLanguages() ok
	 * @Error Person.initLanguages() n/a
	 * @Null Person.initLanguages() n/a
	 * @throws InstantiationException
	 *             if one of the Races cannot be created
	 */
	public void testInitLanguages() throws InstantiationException {
		// This method is tested, can turn off message now
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestInitLanguages()");

		// NORMAL -- Humans have only Common
		ArrayList<String> langs = _mock.initLanguages();
		assertTrue(langs.size() == 1);
		assertTrue(langs.get(0).equals("Common"));
		MsgCtrl.msgln("Languages known: " + langs.get(0));

		// NORMAL -- Dwarves have Common and Grokken; create new Person
		_p = new Person(_hisName, Race.MALE, _hisOccup, _hisHair, "Dwarf",
				"Peasant");
		_mock = _p.new MockPerson();
		langs = _mock.initLanguages();
		assertTrue(langs.size() == 2);
		MsgCtrl.msg("Languages known: ");
		MsgCtrl.msg("\t" + langs.get(0));
		MsgCtrl.msgln("\t\t" + langs.get(1));
		assertTrue(langs.get(0).equals("Common"));
		assertTrue(langs.get(1).equals("Dwarven"));

		// Add other languages here as new Races are implemented
	}

	/**
	 * Test the total number of languages the Person can know. Common is always
	 * known, and a racial language is always known if non-Human, so maxLangs is
	 * a minimum of 1 or 2. In some cases, half-Elves and half-Orcs do not know
	 * their racial language.
	 * 
	 * @Normal Person.initMaxLangs() ok
	 * @Error Person.initMaxLangs() ok
	 * @Null Person.initMaxLangs() compile error
	 */
	public void testInitMaxLangs() {
		// This method is tested, can turn off message now
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestInitMaxLangs()");

		// NORMAL -- Human has 1 base language: Common
		verifyMaxLangs(_mock, "Human");
		assertEquals(_p.getLangsKnown(), 1);

		// NORMAL -- Dwarf has 2 base languages: Common and Grokken
		try {
			_p = new Person(_hisName, Race.MALE, _hisOccup, _hisHair, "Dwarf",
					"Peasant");
			_mock = _p.new MockPerson();
		} catch (InstantiationException ex) {
			MsgCtrl.errMsgln(this, ex.getMessage());
		}
		verifyMaxLangs(_mock, "Dwarf");
		assertEquals(_p.getLangsKnown(), 2);

		// Add more tests as more Races are implemented

		// ERROR -- empty languages list to ensure proper error
		_mock.clearLanguages();
		assertEquals(_mock.initMaxLangs(12), Constants.UNASSIGNED);

		// ERROR -- invalid character attempting to be created
		try {
			_p = new Person(_hisName, Race.MALE, _hisOccup, _hisHair, "Elf",
					"Peasant");
			assertNull(_p);
		} catch (InstantiationException ex) {
			MsgCtrl.errMsgln(this, ex.getMessage());
		}
	}

	/**
	 * Ensure that the proper Literacy skill is assigned to the Person. This
	 * skill depends on INT and is independent of Race or Klass. See also the
	 * other skill tests in this class for non-INT tests.
	 * 
	 * @Normal Person.initSkills(String occup, int intelligence, Race race,
	 *         Klass klass) ok
	 * @Error Person.initSkills(String occup, int intelligence, Race race, Klass
	 *        klass) ok
	 * @Null Person.initSkills(String occup, int intelligence, Race race, Klass
	 *       klass) compile error
	 */
	public void testInitLiteracySkill() {
		// This method is tested, can turn off message now
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestInitLiteracySkill()");

		//
		// // Get the Person's skill list for reassignment
		// ArrayList<Skill> skls = _p.getSkills();
		//
		// int MIN_READ_INT = 10;
		// int MIN_WRITE_INT = 12;
		//
		// // NORMAL -- Traverse a few INT values for the proper Literacy rating
		// // Set the literacy plus the NoSkill skill (total = 2)
		// for (int k=8; k < 16; k++) {
		// skls = _mock.initSkills(OccupTable.NO_OCP, k);
		// MsgCtrl.msgln("\tINT = " + k);
		// if (k < MIN_READ_INT) {
		// assertTrue(_p.hasSkill(litTable[0]));
		// }
		// else if ((k >= MIN_READ_INT) && (k < MIN_WRITE_INT)) {
		// assertTrue(_p.hasSkill(litTable[1]));
		// }
		// else if ((k >= MIN_READ_INT) && (k < MIN_WRITE_INT)) {
		// assertTrue(_p.hasSkill(litTable[2]));
		// }
		// assertEquals(skls.size(), 2); // No-Skill skill and Literacy skill
		// _mock.dumpSkills();
		// }
		//
		// // NORMAL --A quasi-occupation still will assign Literacy
		// skls = _mock.initSkills(OccupTable.NO_OCP, 22);
		// MsgCtrl.msgln("\tINT = " + 22);
		// _mock.dumpSkills();
		// assertEquals(skls.size(), 2); // Literacy with NoSkill skill
		//
		// // ERROR -- An occupation is necessary to init Literacy
		// skls = _mock.initSkills(" ", 22);
		// assertNull(skls);
		// // Reset currently null skill list
		// setUp();
		// MsgCtrl.errorMsgsOn(false);
		//
		// // ERROR -- Non-positive intelligence is not valid
		// skls = _mock.initSkills(OccupTable.NO_OCP, 0);
		// assertNull(skls);
		// setUp();
		// MsgCtrl.errorMsgsOn(false);
		//
		// // ERROR -- Non-positive intelligence is not valid
		// skls = _mock.initSkills(OccupTable.NO_OCP, -3);
		// assertNull(skls);
		// setUp();
		// MsgCtrl.errorMsgsOn(false);
		//
		// // ERROR -- Invalid intelligence, so no literacy skills assigned
		// skls = _mock.initSkills("Acrobat", -7);
		// assertNull(skls);
		// }
		//
		//
		// /** Ensure that the proper skills are assigned for the Person's
		// occupation. These methods are
		// * testing individually by their respective objects, but here the
		// tests are integrated.
		// *
		// * @Normal Person.initSkills(String occup, int intelligence, Race
		// race, Klass klass) ok
		// * @Error Person.initSkills(String occup, int intelligence, Race race,
		// Klass klass) ok
		// * @Null Person.initSkills(String occup, int intelligence, Race race,
		// Klass klass) ok
		// * @Special Person.initSkills(String occup, int intelligence, Race
		// race, Klass klass) null occupation
		// */
		// public void testInitOcpSkills()
		// {
		// // This method is tested, can turn off message now
		// MsgCtrl.auditMsgsOn(false);
		// MsgCtrl.errorMsgsOn(false);
		// MsgCtrl.msgln(this, "\ttestInitOcpSkills()");
		//
		// // List of skills to test associated occupations
		// final String[] regOccup = {OccupTable.NO_OCP, "Acrobat",
		// "Weaponsmith", "Woodworker"};
		// final String[] regSkills = {OccupTable.NO_SKL, "Tumbling",
		// "Make Weapons", "Woodworking"};
		// final String ACTION = "none";
		//
		// // Create Skills from each of these four possibilities as test
		// subjects
		// ArrayList<Skill> skls = new ArrayList<Skill>();
		// try {
		// for (int k=0; k < regOccup.length; k++) {
		// Skill s = new Skill(regOccup[k], regSkills[k], ACTION);
		// skls.add(s);
		// }
		// } catch (ChronosException ex) {
		// MsgCtrl.errMsgln(this, ex.getMessage());
		// fail();
		// }
		// // Get the special NoSkill skill to work with
		// SkillRegistry skreg = (SkillRegistry) AdvRegistryFactory.getInstance().getRegistry(RegKey.SKILL);
		// Skill s0 = skreg.findSkill(OccupTable.NO_SKL);
		//
		// // NORMAL Human with Skills; confirm skills match occupation,
		// including NoSkill
		// // Set the skill set, use same INT; confirm proper skill set
		// for (int k=0; k < regOccup.length; k++) {
		// // Set the skill set
		// skls = _mock.initSkills(regOccup[k], 14);
		// _mock.dumpSkills();
		// MsgCtrl.msgln("");
		// // MsgCtrl.msgln("Skill => " + regOccup[k] +" => " + regSkills[k]);
		// // ...and confirm that the associated skill is present
		// assertTrue(_p.hasSkill(regSkills[k]));
		// }
		//
		// // NORMAL Human with No Skills (good skills should replace no skills)
		// // Case 1: NoSkill skill removed after adding an occup skill
		// MsgCtrl.msgln("\nCase 1: NoSkill skill removed after adding an occup skill");
		// int intel = 14;
		// skls = _mock.initSkills(OccupTable.NO_OCP, intel);
		// assertNotNull(skls);
		// // Only literacy and NoSkill is in place
		// assertEquals(skls.size(), 2);
		// // Replace NoSkill with Brokering
		// MsgCtrl.msgln("Before adding new skill: ");
		// _mock.dumpSkills();
		// Skill s1 = skreg.findSkill("Brokering");
		// assertNotNull(s1);
		// assertEquals(_p.addSkill(s1), 2);
		// MsgCtrl.msgln("After adding new skill: ");
		// _mock.dumpSkills();
		//
		// // Case 2: No Skill not allowed to be added if a real skill is in
		// place
		// MsgCtrl.msgln("\nCase 2: No Skill not allowed to be added if a real skill is in place");
		// skls = _mock.initSkills("Gambler", intel);
		// assertNotNull(skls);
		// assertEquals(skls.size(), 2); // Literacy and Tumbling
		// MsgCtrl.msgln("Before adding new skill: ");
		// _mock.dumpSkills();
		// // Attemping unsuccessfully to add NoSkill
		// assertEquals(_p.addSkill(s0), 2);
		// MsgCtrl.msgln("After adding new skill: ");
		// _mock.dumpSkills();
		//
		// // Case 3: Adding NoSkill twice should fail
		// MsgCtrl.msgln("\nCase 3: Adding NoSkill twice should fail");
		// skls = _mock.initSkills(OccupTable.NO_OCP, intel);
		// assertNotNull(skls);
		// assertEquals(skls.size(), 2); // Literacy and NoSkill
		// MsgCtrl.msgln("Before adding new skill: ");
		// _mock.dumpSkills();
		// // Attemping unsuccessfully to add NoSkill again
		// assertEquals(_p.addSkill(s0), 2);
		// MsgCtrl.msgln("After adding new skill: ");
		// _mock.dumpSkills();
		//
		// // Case 4: Adding any Skill twice should fail
		// MsgCtrl.msgln("\nCase 4: Adding any Skill twice should fail");
		// skls = _mock.initSkills("Banker", intel); // brokering skill
		// assertNotNull(skls);
		// assertEquals(skls.size(), 2); // Literacy and NoSkill
		// MsgCtrl.msgln("Before adding new skill: ");
		// _mock.dumpSkills();
		// // Attemping unsuccessfully to add NoSkill again
		// assertEquals(_p.addSkill(s1), 2); // brokering skill
		// MsgCtrl.msgln("After adding new skill: ");
		// _mock.dumpSkills();
		//
		// // ERROR -- Missing occupation, no skills assigned for it
		// skls = _mock.initSkills(" ", 10);
		// assertNull(skls);
		// // MsgCtrl.msgln("\tINT = " + 13);
		// // _mock.dumpSkills();
		// // assertEquals(skls.size(), 2); // Literacy with default NoSkill
		// skill tacked on
		//
		// // NULL -- no occupation at all is invalid
		// skls = _mock.initSkills(null, 12);
		// MsgCtrl.msgln("\tNull occupation= ");
		// assertNull(skls);
		// }
		//
		//
		// /** Ensure that the proper skills are assigned for the Person's race.
		// These methods are
		// * testing individually by their respective objects, but here the
		// tests are integrated.
		// * Add new skill tests as more races are implemented.
		// *
		// * @Normal Person.initSkills(String occup, int intelligence, Race
		// race, Klass klass) ok
		// * @Error Person.initSkills(String occup, int intelligence, Race race,
		// Klass klass) n/a
		// * @Null Person.initSkills(String occup, int intelligence, Race race,
		// Klass klass) n/a
		// */
		// public void testInitRacialSkills()
		// {
		// // This method is tested, can turn off message now
		// MsgCtrl.auditMsgsOn(false);
		// MsgCtrl.errorMsgsOn(false);
		// MsgCtrl.msgln(this, "\ttestInitRacialSkills()");
		//
		// // NORMAL 1: No racial skills for Human
		// ArrayList<Skill> skills = _p.getSkills();
		// _mock.dumpSkills();
		// assertTrue(skills.size() == 2);
		//
		// // NORMAL 2: Two dwarf skills in addition to literacy and occupation
		// Person dwarf = null;
		// MockPerson mock = null;
		// try {
		// dwarf = new Person(_hisName, Race.MALE, _hisOccup, _hisHair, "Dwarf",
		// "Peasant");
		// assertNotNull(dwarf);
		// mock = dwarf.new MockPerson();
		// assertNotNull(mock);
		// } catch (InstantiationException ex) {
		// MsgCtrl.errMsgln(this, ex.getMessage());
		// }
		// skills = dwarf.getSkills();
		// mock.dumpSkills();
		// assertTrue(skills.size() == 4);
	}

	/**
	 * Test the amount of weight a Person can carry, depending on their STR and
	 * how much they weigh.
	 * 
	 * @Normal Person.initWeightAllowance(int str, int weight) ok
	 * @Error Person.initWeightAllowance(int str, int weight) ok
	 * @Null Person.initWeightAllowance(int str, int weight) compile error
	 */
	public void testWtAllow() {
		// This method is tested, can turn off message now
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.msgln(this, "\ttestWtAllow()");

		// NORMAL
		int canCarry = 0;
		int exp = 0;
		for (int str = 4; str < 21; str++) {
			MsgCtrl.msgln("\n");
			for (int wt = 50; wt < 300; wt += 25) { // wt in lbs
				// canCarry = _mock.testWeightAllowance(str, wt);
				double strFactor = (double) str / 11.5;
				double wtFactor = strFactor * (double) wt;
				exp = (int) (wtFactor * 16.0);
				MsgCtrl.msgln("STR: " + str + "; Weight: " + wt
						+ "\t weightAllowance = " + canCarry + "\t ("
						+ canCarry / 16 + " lbs)");
				assertTrue(canCarry == exp);
			}
		}

		// ERROR -- Person has negative str or negative weight
		// canCarry = _mock.testWeightAllowance(-12, 145);
		assertEquals(canCarry, 0);
		// canCarry = _mock.testWeightAllowance(3, 145);
		// assertEquals(canCarry, 0);
		// canCarry = _mock.testWeightAllowance(1, 145);
		// assertEquals(canCarry, 0);
		// canCarry = _mock.testWeightAllowance(11, 0);
		// assertEquals(canCarry, 0);
		// canCarry = _mock.testWeightAllowance(11, -1);
		// assertEquals(canCarry, 0);
	}

	// /** Test the encumbrance function
	// * @Normal Person.calcEncumberance(int ozAllow, int load) ok
	// * @Error Person.calcEncumberance(int ozAllow, int load) ok
	// * @Null Person.calcEncumberance(int ozAllow, int load) compile error
	// */
	// public void testCalcEncumbrance()
	// {
	// // This method is tested, can turn off message now
	// MsgCtrl.auditMsgsOn(true);
	// MsgCtrl.errorMsgsOn(true);
	// MsgCtrl.msgln(this, "\ttestCalcEncumbrance()");
	//
	// // NORMAL
	// // For several wtAllowances below, above, and within the weight Allowed,
	// check that the
	// // correct factor is calculated
	// final int ozAllowed = 100 * 16; // 100 lbs = 1600 oz
	// // Well below
	// double encFactor = _p.calcEncumberance(ozAllowed, 1100);
	// assertEquals(encFactor, 1.00, .01);
	// // Just below
	// encFactor = _p.calcEncumberance(ozAllowed, 1599);
	// assertEquals(encFactor, 1.00, .01);
	// // Barely within (unmeasureable)
	// encFactor = _p.calcEncumberance(ozAllowed, 1601);
	// assertEquals(encFactor, 0.99, .01);
	// // Measureably within (.05 result)
	// encFactor = _p.calcEncumberance(ozAllowed, 1616);
	// assertEquals(encFactor, 0.99, .01);
	// // Well within
	// encFactor = _p.calcEncumberance(ozAllowed, 3000);
	// assertEquals(encFactor, 0.125, .01);
	// // Barely within
	// encFactor = _p.calcEncumberance(ozAllowed, 3199);
	// assertEquals(encFactor, 0.0, .01);
	// // Just above
	// encFactor = _p.calcEncumberance(ozAllowed, 3201);
	// assertEquals(encFactor, 0.0, .01);
	// // Well above
	// encFactor = _p.calcEncumberance(ozAllowed, 3500);
	// assertEquals(encFactor, 0.0, .01);
	// // No weight at all to carry is still ok
	// encFactor = _p.calcEncumberance(ozAllowed, 0);
	// assertEquals(encFactor, 1.0, .001);
	//
	// // ERROR -- zero values
	// encFactor = _p.calcEncumberance(0, 3500);
	// assertEquals(encFactor, 0.0, .001);
	// // Either parm can be zero, but not both
	// encFactor = _p.calcEncumberance(0, 0);
	// assertEquals(encFactor, 0.0, .001);
	//
	// // ERROR -- negative values
	// encFactor = _p.calcEncumberance(-100, 3500);
	// assertEquals(encFactor, 0.0, .001);
	// encFactor = _p.calcEncumberance(ozAllowed, -100);
	// assertEquals(encFactor, 0.0, .001);
	// }

	// /** Calculate the Grappling Action Point mod (wrestling).
	// * Grappling = AP + STR Damage adj - metal armor adj.
	// */
	// public void testCalcGrappling()
	// {
	// // This method is tested, can turn off message now
	// MsgCtrl.auditMsgsOn(false);
	// MsgCtrl.errorMsgsOn(false);
	// MsgCtrl.msgln(this, "\ttestCalcGrappling()");
	//
	// int grappleAdj = -99;
	// // Test WITHOUT metal armor
	// MsgCtrl.msgln("\n");
	// for (int ap=15; ap < 38; ap++) {
	// for (int k=-3; k < 5; k++) {
	// // pass AP, DamageAdj, and metalAdj flag
	// grappleAdj = _mock.testGrappling(ap, k, false);
	// MsgCtrl.msgln("AP: " + ap + "; DamageAdj: " + k + " metal? = false" +
	// "\t grappling mod =" + grappleAdj);
	// assertTrue(grappleAdj == ap + k);
	// }
	// }
	// // Test WITH metal armor
	// MsgCtrl.msgln("\n");
	// for (int ap=15; ap < 38; ap++) {
	// for (int k=-3; k < 5; k++) {
	// // pass AP, DamageAdj, and metalAdj flag
	// grappleAdj = _mock.testGrappling(ap, k, true);
	// MsgCtrl.msgln("AP: " + ap + "; DamageAdj: " + k + " metal? = true" +
	// "\t grappling mod =" + grappleAdj);
	// assertTrue(grappleAdj == ap + k - Inventory.AP_METAL_ADJ);
	// }
	// }
	// MsgCtrl.setSuppression(true);
	// }

	// /** Test the overbearing factor.
	// * Checks reasonable ranges for varying loads, weight allowances, and
	// Action Points.
	// * This test implicitly checks the calcSpeed method also. */
	// public void testCalcOverbearing()
	// {
	// MsgCtrl.setSuppression(true);
	// double ob = 0;
	// // NORMAL TEST ON Falsoon, vary AP
	// // Get the weight allowed for the current Person under test
	// int wtAllow = _p.getWtAllowance();
	// // Ge tthe current load carried
	// int load = _p.getInventoryWeight();
	// // Check for fixed load and unencumbered weight allowances,
	// // ranging from minAP to maxAP
	// MsgCtrl.msgln("\nWt allowance = " + wtAllow/16.0 + "\t Load = " +
	// load/16.0);
	//
	// // Due to roundoff, ob factor jumps occassionally
	// helpTestOB(10, 15, 3.0, wtAllow, load);
	// helpTestOB(16, 23, 4.0, wtAllow, load);
	// helpTestOB(24, 31, 5.0, wtAllow, load);
	// helpTestOB(32, 36, 6.0, wtAllow, load);
	// // Now change the load so that the person is encumbered
	// int encLoad = (int) (1.5 * wtAllow);
	// MsgCtrl.msgln("\nWt allowance = " + wtAllow/16.0 +
	// "\t Encumbered Load = " + encLoad/16.0);
	// helpTestOB(10, 15, 1.5, wtAllow, encLoad);
	// helpTestOB(16, 23, 2.0, wtAllow, encLoad);
	// helpTestOB(24, 31, 2.5, wtAllow, encLoad);
	// helpTestOB(32, 39, 3.0, wtAllow, encLoad);
	// // Should have one more test when the Person can move at all
	// int tooLoad = 3 * wtAllow;
	// MsgCtrl.msgln("\nWt allowance = " + wtAllow/16.0 +
	// "\t Encumbered Load = " + tooLoad/16.0);
	// helpTestOB(10, 39, 0, wtAllow, tooLoad);
	// MsgCtrl.setSuppression(true);
	// }

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE
	 * HELPER METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Helper method for repeated calls to initMaxLanguage()
	 * 
	 * @param mock
	 *            mock for the Person
	 * @param raceType
	 *            of the Person being verified
	 */
	private void verifyMaxLangs(MockPerson mock, String raceType) {
		int minInt = 10;
		int highInt = 18;
		// Adjust for learned languages (e.g., Human = 1; Dwarf = 2)
		MsgCtrl.msg("\tFor " + raceType + " Person: ");
		int baseLangs = mock.getNbrOfCurrentLangs();
		MsgCtrl.msgln("\tBase langs = " + baseLangs);
		for (int k = 6; k < 21; k++) {
			int totalLangs = mock.initMaxLangs(k);
			int learnable = totalLangs - baseLangs;
			MsgCtrl.msg("\tINT = " + k + ": ");
			MsgCtrl.msgln("\tLearned Langs = " + learnable);
			if (k < minInt) {
				assertEquals(learnable, 0);
				assertEquals(totalLangs, baseLangs);
			}
			// one language for each even INT point above minimum
			else if ((k >= minInt) && (k < highInt)) {
				int calcLang = k / 2 - 4;
				assertEquals(totalLangs, calcLang + baseLangs);
			} else { // an additional language for each high INT point
				int calcLang = k - 13;
				assertEquals(totalLangs, calcLang + baseLangs);
			}
		}
	}

	// /** Helper method for repeated calls to calcOverbearing
	// * @param start AP, low end to start
	// * @param end AP, high end of range to test
	// * @param exp expected value of OB to check
	// * @param wtAllow amount that Person can carry w/o encumberance
	// * @param load amount of weight Person is carrying
	// */
	// private void helpTestOB(int start, int end, double exp, int wtAllow, int
	// load)
	// {
	// double ob = 0;
	// for (int k=start; k <= end; k++) {
	// ob = _p.calcOverbearing(k, wtAllow, load);
	// MsgCtrl.msgln(k + " = " + ob);
	// assertEquals(ob, k * exp, 1.0);
	// }
	// }

	// /** Calculate the Pummeling Action Point mod (wrestling).
	// * Pummeling = AP + STR Damage adj + DEX ToHit Adj + metal adj.
	// */
	// public void testCalcPummeling()
	// {
	// /** Suppress messageson this now tested method */
	// MsgCtrl.setSuppression(true);
	// int pummel = -99;
	// // Test WITHOUT metal armor
	// MsgCtrl.msgln("\n");
	// for (int ap=15; ap < 38; ap++) {
	// for (int damAdj=-3; damAdj < 5; damAdj++) {
	// for (int toHit=-3; toHit < 5; toHit++) {
	// // pass AP, DamageAdj, and metalAdj flag
	// pummel = _mock.testPummeling(ap, damAdj, toHit, false);
	// MsgCtrl.msgln("AP: " + ap + "; DamageAdj: " + damAdj + " metal? = false"
	// +
	// "\t DEX ToHit Adj =" + toHit +
	// "\t pummeling mod =" + pummel);
	// assertTrue(pummel == ap + damAdj + toHit);
	// }
	// }
	// }
	// // Test WITH metal armor
	// MsgCtrl.msgln("\n");
	// for (int ap=15; ap < 38; ap++) {
	// for (int damAdj=-3; damAdj < 5; damAdj++) {
	// for (int toHit=-3; toHit < 5; toHit++) {
	// // pass AP, DamageAdj, and metalAdj flag
	// pummel = _mock.testPummeling(ap, damAdj, toHit, true);
	// MsgCtrl.msgln("AP: " + ap + "; DamageAdj: " + damAdj + " metal? = true" +
	// "\t DEX ToHit Adj =" + toHit +
	// "\t pummeling mod =" + pummel);
	// assertTrue(pummel == ap + damAdj + toHit + Inventory.AP_METAL_ADJ);
	// }
	// }
	// }
	// MsgCtrl.setSuppression(true);
	// }

	/**
	 * This method is tested indirectly in so many places, only a few error
	 * conditions and a normal set of tests are run.
	 */
	public void testCalcTraitMod() {
		MsgCtrl.auditMsgsOn(true);
		MsgCtrl.errorMsgsOn(true);
		// Check some obvious error conditions
		assertTrue(_p.calcTraitMod(10, 12, 11) == Constants.UNASSIGNED); // inverted
																			// range
																			// points
		assertTrue(_p.calcTraitMod(10, 12, 12) == Constants.UNASSIGNED); // no
																			// middle
																			// range
		// Test for some value between 11 and 15 having normal mods
		final int LOWVAL = 9;
		final int HIGHVAL = 15;
		// First test negative mods
		for (int k = 6; k < LOWVAL; k++) {
			assertTrue(_p.calcTraitMod(k, LOWVAL, HIGHVAL) == k - LOWVAL);
		}
		// Test for zero mod
		for (int k = LOWVAL; k < HIGHVAL; k++) {
			assertTrue(_p.calcTraitMod(k, LOWVAL, HIGHVAL) == 0);
		}
		// Test for postive mods
		for (int k = HIGHVAL; k <= 21; k++) {
			assertTrue(_p.calcTraitMod(k, LOWVAL, HIGHVAL) == k - HIGHVAL);
		}
	}

	/**
	 * Tests that are not needed for various reasons, mostly setters and getters
	 * 
	 * @Not_Needed Person.Person() empty ctor
	 */
	public void testNotNeeded() {
	}

} // end of TestPerson class
