/**
 * Integration test A01_N01: Create Human Hero 
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package test.integ.A01_CreateHero.old;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import pdc.character.Person;
import pdc.character.Person.MockPerson;

import civ.NewHeroCiv;
import civ.NewHeroCiv.MockNewHeroCiv;
import civ.NewHeroFields;
import civ.PersonKeys;

import chronos.Chronos.ATTRIBUTE;
import chronos.pdc.AttributeList;
import chronos.pdc.Occupation;

import mylib.Constants;
import mylib.MsgCtrl;
import mylib.civ.DataShuttle;
import mylib.pdc.MetaDie;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Base integration Test to create a Human Peasant Hero, hair color = bald,
 * Occupation = "None". This is a base class from which derived test classes
 * reuse these same tests.
 * <P>
 * Program inpoint from GUI to
 * <code>NewHeroCiv.submit(DataShuttle&ltNewHeroFields&gt ws). </code>
 * <P>
 * Program has three outpoints to GUI:
 * <UL>
 * <LI>
 * <code>HeroDisplayCiv.populateAttributes(DataShuttle&ltPersonKeys&gt ds); </code>
 * </LI>
 * <LI>
 * <code>HeroDisplayCiv.populateInventory(ArrayList&ltItem&gt inventory); </code>
 * </LI>
 * <LI><code>HeroDisplayCiv.populateSkills(ArrayList&ltSkill&gt skills).</code></LI>
 * </UL>
 * 
 * @author Al Cline
 * @version <DL>
 *          <DT> Build 1.0 November 21, 2011 // original <DD> <DT> Build 1.1 23
 *          May 2012 // Updated as first-element test of three <DD>
 *          </DL>
 */
public class A01_N01 // extends TestCase
{
	/** These objects are created once before the gamut of tests starts */
	static private NewHeroCiv _nhCiv = null;
	/** Mock Object to access private methods to test */
	static private MockNewHeroCiv _mockCiv = null;
	/** DataShuttle for passing the field data */
	static private DataShuttle<NewHeroFields> _ws = null;
	/** DataShuttle for passing the model data */
	static private DataShuttle<PersonKeys> _ds = null;

	/** The Human Person Hero object under test */
	Person _p = null;
	/** MockPerson object to access Person's private data */
	MockPerson _mock = null;

	// Input values to send to submit()
	private final String IP_NAME = "Hoffmok the Beefy";
	private final String IP_GENDER = "Male";
	private final String IP_HAIRCOLOR = "bald";
	private final String IP_RACENAME = "Human";
	private final String IP_KLASSNAME = "Peasant";
	private final String IP_OCCUPATION = "None";

	// Model output values in order built
	// Fixed prime traits; CON and CHR are random
	// STR, DEX, INT, WIS, CON (adj), CHR (adj)
	int[] expectedTraits = { 11, 11, 11, 10, 0, 0 };
	private AttributeList _expTraits = new AttributeList(expectedTraits);
	// Traits will be reset after the Person is created
	int[] resultantTraits = { 0, 0, 0, 0, 0, 0 };
	private AttributeList _resultTraits = new AttributeList(resultantTraits);
	private final int MALE_TRAITMIN = 8; // for CON and CHR
	private final int MALE_TRAITMAX = 18; // for CON and CHR

	// Expected value in order of creation
	private final double OP_AGE = 17.0;
	private final double OP_WEIGHT = 175; // male
	private final int OP_HEIGHT = 70; // male
	private final String OP_DESCRIPTION = null;
	private final int OP_TOHITMELEE_MOD = 0; // STR modifed for STR = 11
	private final int OP_DAMAGE_MOD = 0; // STR modifed for STR = 11
	private final String OP_LANGUAGE = "Commmon"; // Only Common

	// NOT INTEGRATED YET
	private final int OP_XP = 0;
	private final int OP_LEVEL = 0;
	private final int OP_HP = 10;
	private final String OP_HUNGER = "FULL";

	private final int OP_AC = 10;
	private final double OP_SPEED = 3.0; // AP = 11+11 => BM of 3.0

	private final int OP_GOLD = 15;
	private final int OP_SILVER = 8;
	private final double OP_GOLD_BANKED = 0.0;
	private final int OP_LOAD = 464; // ounces + 38 ounces in cash;

	private final int OP_MAX_LANGS = 2; // for INT = 12

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ TEST
	 * INITIALIZATION ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Create the Person before running all tests against its attributes,
	 * displayable and non-displable
	 */
	@BeforeClass
	public static void onceOnly() {
		MsgCtrl.errorMsgsOn(true);
		Constants.IN_TEST = true;
		// Create a civ without a model
		_nhCiv = new NewHeroCiv();
		assertNotNull(_nhCiv);
		_mockCiv = _nhCiv.new MockNewHeroCiv();
		assertNotNull(_mockCiv);

		// Create empty widget shuttle for input data from GUI
		_ws = new DataShuttle<NewHeroFields>(NewHeroFields.class);
		assertNotNull(_ws);
		// Create empty data shuttle for data returning from the model
		_ds = new DataShuttle<PersonKeys>(PersonKeys.class);
		assertNotNull(_ds);
	}

	/**
	 * Create the objects needed for testing; called before each test. The
	 * methods are called, as independent of the Race and Klass as possible.
	 */
	@Before
	public void setUp() throws Exception {
		MsgCtrl.auditMsgsOn(false);
		// MsgCtrl.errorMsgsOn(true);
		// Chronos.IN_TEST = true;

		// // Create a civ without a model
		// _nhCiv = new NewHeroCiv();
		// assertNotNull(_nhCiv);
		// _mockCiv = _nhCiv.new MockNewHeroCiv();
		// assertNotNull(_mockCiv);
		//
		// // Create empty widget shuttle for input data from GUI
		// _ws = new DataShuttle<NewHeroFields>(NewHeroFields.class);
		// assertNotNull(_ws);
		// // Create empty data shuttle for data returning from the model
		// _ds = new DataShuttle<PersonKeys>(PersonKeys.class);
		// assertNotNull(_ds);
	}

	/** Release the objects created during setup; called after each test */
	@After
	public void tearDown() {
		// _nhCiv = null;
		// _mockCiv = null;
		// _ws = null;
		// _ds = null;
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ LET THE TESTS
	 * BEGIN! ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Integration test A01_N01. Create New Hero with elements from the end of
	 * the options lists: human, male, hair color = bald (position 0),
	 * Occcupation = None (position 0), no Occupational skills or Kit in
	 * Inventory. Race and gender (for male) are not adjusted; no tests.
	 */
	@Test
	public void testA01_N01() {
		MsgCtrl.auditMsgsOn(true);
		MsgCtrl.errorMsgsOn(true);

		// Load the Person's input data to give to the civ's submit method
		_ws = packFields(_ws);
		assertNotNull(_ws);

		// Send the data into the civ to create the Person
		_ws = _nhCiv.submit(_ws);
		// Verify that no errors occured
		assertEquals(DataShuttle.ErrorType.OK, _ws.getErrorType());

		// For now, get the Person's data from the Person object
		// Later, it should come from the HeroDisplayCiv after it populates, but
		// before it displays
		_p = _mockCiv.getPerson();
		assertNotNull(_p);
		_ds = _p.loadPersonData(_ds);
		// Create the Person mock for internal data
		_mock = _p.new MockPerson();

		// This data is not returned in the shuttle for display
		_resultTraits = _mockCiv.getTraits();
		verifyTraits(_expTraits, _resultTraits);
		verifyConChrTraits(_resultTraits.get(ATTRIBUTE.CON),
				_resultTraits.get(ATTRIBUTE.CHR), MALE_TRAITMIN, MALE_TRAITMAX);

		// Test Hero's initial age
		assertEquals("\tAge fail", OP_AGE, _ds.getField(PersonKeys.AGE));

		// Test Hero's weight, but convert integer weight to double first
		double weight = ((Integer) _ds.getField(PersonKeys.WEIGHT)).intValue();
		verifyNormalRange("Weight", OP_WEIGHT, weight);

		// Test Hero's height, but convert integer weight to double first
		double height = ((Integer) _ds.getField(PersonKeys.HEIGHT)).intValue();
		verifyNormalRange("Height", OP_HEIGHT, height);

		// Test Hero's description based on CHR
		verifyDescription(_expTraits.get(ATTRIBUTE.CHR),
				(String) _ds.getField(PersonKeys.DESCRIPTION));

		// Test Hero's STR modifiers
		verifyStrMods(_expTraits.get(ATTRIBUTE.STR), weight);

		// // Test Hero's INT modifiers
		// verifyIntMods();
	}

	// // Create the expected values
	// DataShuttle<PersonKeys> veris = buildVerifyShuttle();
	//
	// // Verify that the Hero outputs are correct
	// assertTrue(verifyOutputs(_ds, veris));

	// assertEquals(_ds.getField(PersonKeys.NAME),
	// _ds.getField(PersonKeys.NAME));
	// assertEquals(_ds.getField(PersonKeys.GENDER),
	// _ds.getField(PersonKeys.GENDER));
	// assertEquals(_ds.getField(PersonKeys.OCCUPATION),
	// _ds.getField(PersonKeys.OCCUPATION));
	// assertEquals(_ds.getField(PersonKeys.RACENAME),
	// _ds.getField(PersonKeys.RACENAME));
	// assertEquals(_ds.getField(PersonKeys.KLASSNAME),
	// _ds.getField(PersonKeys.KLASSNAME));

	// Verify that the attributes are correct

	// Then, make sure the data is displayed properly
	// _p = _mockCiv.getPerson();

	// //Verify all fields the same from _ds to _p
	// assertEquals(_ds.getField(PersonKeys.NAME), _p.getName());
	// assertEquals(_ds.getField(PersonKeys.GENDER), _p.getGender());
	// assertEquals(_ds.getField(PersonKeys.OCCUPATION), _p.getOccupation());
	// assertEquals(_ds.getField(PersonKeys.RACENAME), _p.getRaceName());
	// assertEquals(_ds.getField(PersonKeys.KLASSNAME), _p.getKlassName());

	// Verify Inventory
	// Inventory i = _p.getInventory();
	// //Check a few items in the inventory
	// for (int l=0;l<OP_ITEMS.length;l++)
	// {
	// assertTrue (i.hasItem(OP_ITEMS[l]));
	// }
	// //Check that the inventory size is correct
	// assertEquals(OP_INVENTORY_SIZE, i.getNbrItems());

	// Verify Skills - iterating through OP_SKILLS for each listed skill
	// ArrayList<Skill> skills = _p.getSkills();
	// Skill p_skill = null;
	// for (int j=0;j<OP_SKILLS.length;j++)
	// {
	// int k=0;
	// {
	// try {
	// p_skill = new Skill (OP_SKILLS[j][k], OP_SKILLS[j][k+1],
	// "Not yet implemented");
	// }
	// catch (ChronosException e) {
	// MsgCtrl.errMsgln(this, "Error creating skill during test");
	// }
	// assertTrue (skills.get(j).getName().equalsIgnoreCase(p_skill.getName()));
	// assertTrue
	// (skills.get(j).getDescription().equalsIgnoreCase(p_skill.getDescription()));
	// }
	// }

	// //Verify Attributes
	// _p.loadPersonData(_ds);
	// assertEquals (_ds.getField(PersonKeys.AC), OP_AC);
	// assertEquals (_ds.getField(PersonKeys.AGE), OP_AGE);
	// assertTrue (((String)
	// _ds.getField(PersonKeys.DESCRIPTION)).equalsIgnoreCase(OP_DESCRIPTION));
	// assertEquals (_ds.getField(PersonKeys.GENDER), IP_GENDER);
	// assertEquals (_ds.getField(PersonKeys.GOLD), OP_GOLD);
	// assertEquals (_ds.getField(PersonKeys.GOLD_BANKED), OP_GOLD_BANKED);
	// assertEquals (_ds.getField(PersonKeys.HAIR_COLOR),IP_HAIRCOLOR);
	// assertEquals (_ds.getField(PersonKeys.HEIGHT), OP_HEIGHT);
	// assertEquals (_ds.getField(PersonKeys.HP), OP_HP);
	// assertEquals (_ds.getField(PersonKeys.HUNGER), OP_HUNGER);
	// assertTrue (((String)
	// _ds.getField(PersonKeys.KLASSNAME)).equalsIgnoreCase(IP_KLASSNAME));
	// assertEquals (_ds.getField(PersonKeys.LANGUAGES),
	// Arrays.asList(OP_LANGUAGES));
	// assertEquals (_ds.getField(PersonKeys.LEVEL), OP_LEVEL);
	// // assertEquals (_ds.getField(PersonKeys.LITERACY), OP_LITERACY);
	// assertEquals (_ds.getField(PersonKeys.LOAD), OP_LOAD);
	// assertEquals (_ds.getField(PersonKeys.MAX_LANGS), OP_MAX_LANGS);
	// assertTrue (((String)
	// _ds.getField(PersonKeys.NAME)).equalsIgnoreCase(IP_NAME));
	// assertTrue (((Occupation)
	// _ds.getField(PersonKeys.OCCUPATION)).getName().equalsIgnoreCase(IP_OCCUPATION));
	// assertTrue (((String)
	// _ds.getField(PersonKeys.RACENAME)).equalsIgnoreCase(IP_RACENAME));
	// assertEquals (_ds.getField(PersonKeys.SILVER), OP_SILVER);
	// assertEquals (_ds.getField(PersonKeys.SPEED), OP_SPEED);
	// assertEquals (_ds.getField(PersonKeys.WEIGHT), OP_WEIGHT);
	// assertEquals (_ds.getField(PersonKeys.XP), OP_XP);
	//
	// }

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE
	 * HELPER METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * PROTECTED HELPER METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/**
	 * Pack the internal fields then send the new Hero data to the Civ to be
	 * validated. Scavanged method from NewHeroDisplay
	 * 
	 * @param inShuttle
	 *            Data shuttle with default data from NHCiv
	 * @return data shuttle of fields for this widget
	 */
	protected DataShuttle<NewHeroFields> packFields(
			DataShuttle<NewHeroFields> inShuttle) {
		// Package each of these into the data shuttle
		inShuttle.putField(NewHeroFields.NAME, IP_NAME);
		inShuttle.putField(NewHeroFields.GENDER, IP_GENDER);
		inShuttle.putField(NewHeroFields.HAIR_COLOR, IP_HAIRCOLOR);
		inShuttle.putField(NewHeroFields.OCCUPATION, IP_OCCUPATION);
		inShuttle.putField(NewHeroFields.RACENAME, IP_RACENAME);
		inShuttle.putField(NewHeroFields.KLASSNAME, IP_KLASSNAME);
		return inShuttle;
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * PROTECTED HELPER METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	// /** Pack the expected data into a data shuttle for comparison
	// *
	// * @return data shuttle of model data (internal format) for this widget
	// */
	// protected DataShuttle<PersonKeys> buildVerifyShuttle()
	// {
	// // Package each of these into the data shuttle
	// DataShuttle<PersonKeys> outShuttle = new
	// DataShuttle<PersonKeys>(PersonKeys.class);
	// // Pack all the input data expected to come back as output data
	// outShuttle.putField(PersonKeys.NAME, IP_NAME);
	// outShuttle.putField(PersonKeys.GENDER, IP_GENDER);
	// outShuttle.putField(PersonKeys.OCCUPATION, IP_OCCUPATION);
	// outShuttle.putField(PersonKeys.RACENAME, IP_RACENAME);
	// outShuttle.putField(PersonKeys.KLASSNAME, IP_KLASSNAME);
	//
	// // Pack all the expected output data
	// // Row 1: XP, Level, Hit Points, [Occupation], Hunger state
	// outShuttle.putField(PersonKeys.XP, OP_XP);
	// outShuttle.putField(PersonKeys.LEVEL, OP_LEVEL);
	// outShuttle.putField(PersonKeys.HP, OP_HP);
	// outShuttle.putField(PersonKeys.OCCUPATION, IP_OCCUPATION);
	// outShuttle.putField(PersonKeys.HUNGER, OP_HUNGER);
	//
	// // Row 2: Armor Class, Speed, Age, Height, and Weight
	// outShuttle.putField(PersonKeys.AC, OP_AC);
	// outShuttle.putField(PersonKeys.SPEED, OP_SPEED);
	// outShuttle.putField(PersonKeys.AGE, OP_AGE);
	// outShuttle.putField(PersonKeys.HEIGHT, OP_HEIGHT);
	// outShuttle.putField(PersonKeys.WEIGHT, OP_WEIGHT);
	//
	// // Row 3: Gold, Silver, Gold Banked, and Load
	// outShuttle.putField(PersonKeys.GOLD, OP_GOLD);
	// outShuttle.putField(PersonKeys.SILVER, OP_SILVER);
	// outShuttle.putField(PersonKeys.GOLD_BANKED, OP_GOLD_BANKED);
	// outShuttle.putField(PersonKeys.LOAD, OP_LOAD);
	//
	// // Row 4: Languages: Max and Known (List)
	// outShuttle.putField(PersonKeys.MAX_LANGS, OP_MAX_LANGS);
	// outShuttle.putField(PersonKeys.LANGUAGES, Arrays.asList(OP_LANGUAGES));
	//
	// // Row 5: Description of the new Hero
	// outShuttle.putField(PersonKeys.DESCRIPTION, OP_DESCRIPTION);
	// return outShuttle;
	// }

	/**
	 * Check the Hero's description as inferred from various attributes.
	 * Description for this test Hero: Gender ("He"), hair color (bald =
	 * "no hair"), racial (none), and charisma traits.
	 * 
	 * @param chr
	 *            Hero's CHR
	 * @param desc
	 *            Hero's description
	 */
	private void verifyDescription(int chr, String desc) {
		MsgCtrl.msgln("A01_N01.verifyDescription()");

		// Correspeonding to CHR from 8 to 18 (7 and 19 are included for
		// completeness)
		final String[] keyword = {
				"crippled and horribly ugly", // < 8
				"horribly scarred",
				"scarred from war or fire",
				"the result of years of misery", // 8-10
				"weather-beaten and tough",
				"nothing special to look at", // 11-12
				"clear-eyed and rugged but not handsome", // 13
				"slightly attractive if one could scrape off the years of wear and tear", // 14
				"a handsome adventurer", "gorgeous",
				"very attactive", // 15-17
				"stunningly beautiful", // 18
				"mesmerizing, and you will do whatever this person commands of you", // >
																						// 18
		};

		MsgCtrl.msg("\tCharisma = " + chr);
		MsgCtrl.msgln("\tDescription: " + desc);
		assertTrue(desc.contains("He ")); // gender check
		assertTrue(desc.contains("no hair")); // baldness check
		assertTrue(desc.contains("no special racial traits")); // racial check
		// Check for key words in CHR specific phrasing
		for (int k = 8; k < keyword.length + 8; k++) {
			if (k != chr) {
				continue;
			} else {
				assertTrue(desc.contains(keyword[k - 7]));
				break;
			}
		}

	}

	/**
	 * Verify that all new Hero input data are correct.
	 * 
	 * @param ds
	 *            shuttle containing the Hero's output display data
	 * @return true if the method gets that far
	 */
	private boolean verifyInputs(DataShuttle<PersonKeys> ds) {
		assertTrue(((String) _ds.getField(PersonKeys.NAME))
				.equalsIgnoreCase(IP_NAME));
		assertEquals(IP_GENDER, _ds.getField(PersonKeys.GENDER));
		assertTrue(((String) _ds.getField(PersonKeys.KLASSNAME))
				.equalsIgnoreCase(IP_KLASSNAME));
		assertTrue(((String) _ds.getField(PersonKeys.RACENAME))
				.equalsIgnoreCase(IP_RACENAME));
		assertTrue(((Occupation) _ds.getField(PersonKeys.OCCUPATION)).getName()
				.equalsIgnoreCase(IP_OCCUPATION));
		return true;
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * PROTECTED HELPER METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	// /** Pack the expected data into a data shuttle for comparison
	// *
	// * @return data shuttle of model data (internal format) for this widget
	// */
	// protected DataShuttle<PersonKeys> buildVerifyShuttle()
	// {
	// // Package each of these into the data shuttle
	// DataShuttle<PersonKeys> outShuttle = new
	// DataShuttle<PersonKeys>(PersonKeys.class);
	// // Pack all the input data expected to come back as output data
	// outShuttle.putField(PersonKeys.NAME, IP_NAME);
	// outShuttle.putField(PersonKeys.GENDER, IP_GENDER);
	// outShuttle.putField(PersonKeys.OCCUPATION, IP_OCCUPATION);
	// outShuttle.putField(PersonKeys.RACENAME, IP_RACENAME);
	// outShuttle.putField(PersonKeys.KLASSNAME, IP_KLASSNAME);
	//
	// // Pack all the expected output data
	// // Row 1: XP, Level, Hit Points, [Occupation], Hunger state
	// outShuttle.putField(PersonKeys.XP, OP_XP);
	// outShuttle.putField(PersonKeys.LEVEL, OP_LEVEL);
	// outShuttle.putField(PersonKeys.HP, OP_HP);
	// outShuttle.putField(PersonKeys.OCCUPATION, IP_OCCUPATION);
	// outShuttle.putField(PersonKeys.HUNGER, OP_HUNGER);
	//
	// // Row 2: Armor Class, Speed, Age, Height, and Weight
	// outShuttle.putField(PersonKeys.AC, OP_AC);
	// outShuttle.putField(PersonKeys.SPEED, OP_SPEED);
	// outShuttle.putField(PersonKeys.AGE, OP_AGE);
	// outShuttle.putField(PersonKeys.HEIGHT, OP_HEIGHT);
	// outShuttle.putField(PersonKeys.WEIGHT, OP_WEIGHT);
	//
	// // Row 3: Gold, Silver, Gold Banked, and Load
	// outShuttle.putField(PersonKeys.GOLD, OP_GOLD);
	// outShuttle.putField(PersonKeys.SILVER, OP_SILVER);
	// outShuttle.putField(PersonKeys.GOLD_BANKED, OP_GOLD_BANKED);
	// outShuttle.putField(PersonKeys.LOAD, OP_LOAD);
	//
	// // Row 4: Languages: Max and Known (List)
	// outShuttle.putField(PersonKeys.MAX_LANGS, OP_MAX_LANGS);
	// outShuttle.putField(PersonKeys.LANGUAGES, Arrays.asList(OP_LANGUAGES));
	//
	// // Row 5: Description of the new Hero
	// outShuttle.putField(PersonKeys.DESCRIPTION, OP_DESCRIPTION);
	// return outShuttle;
	// }

	/**
	 * Verify that the median value given is within the expected 16% standard
	 * deviation
	 * 
	 * @param label
	 *            name of attribute represented
	 * @param median
	 *            median of range being tested
	 * @param value
	 *            that must reside within range
	 */
	private void verifyNormalRange(String label, double median, double value) {
		MsgCtrl.msgln("A01_N01.verifyNormalRange()");

		// Convert to double for assert statement tolerance
		MsgCtrl.msg("\t" + label + "= " + value);
		// Legal value lies within plus or minus half sigma of given median
		double hiRange = median * (1.0 + MetaDie.HALF_SIGMA);
		double lowRange = median * (1.0 - MetaDie.HALF_SIGMA);
		MsgCtrl.msg(" [" + (int) lowRange + ", " + (int) hiRange + "]");
		// Median must be within half standard deviation
		assertTrue("\t" + label + " fail",
				((value < hiRange) && (value > lowRange)));
		MsgCtrl.msgln("\n");
	}

	/**
	 * Verify that the output shuttle matches the expected verification shuttle
	 * data
	 * 
	 * @param ds
	 *            shuttle containing the Hero's output display data
	 * @param veris
	 *            shuttle containing the Hero's correct data for verification
	 * @return true if all matches; else JUnit will stop at the incorrect assert
	 *         statemetn
	 */
	private boolean verifyOutputs(DataShuttle<PersonKeys> ds,
			DataShuttle<PersonKeys> veris) {
		MsgCtrl.msgln("verifyOutputs():");

		// // First compare prime traits
		// MsgCtrl.msg("\tHero's traits: \t");
		// MsgCtrl.msg("\tSTR = " + _traits[Person.STR]);
		// MsgCtrl.msg("\tINT = " + _traits[Person.INT]);
		// MsgCtrl.msg("\tWIS = " + _traits[Person.WIS]);
		// MsgCtrl.msg("\tDEX = " + _traits[Person.DEX]);
		// MsgCtrl.msg("\tCON = " + _traits[Person.CON]);
		// MsgCtrl.msg("\tCHR = " + _traits[Person.CHR]);
		//
		// assertEquals("\tStrength", T_STR, _traits[Person.STR]);
		// assertEquals("\tIntelligence", T_INT, _traits[Person.INT]);
		// assertEquals("\tWisdom", T_WIS, _traits[Person.WIS]);
		// assertEquals("\tDexterity", T_DEX, _traits[Person.DEX]);

		// Row 1: XP, Level, Hit Points, [Occupation], Hunger state
		assertEquals("\tXP", OP_XP, _ds.getField(PersonKeys.XP));
		assertEquals("\tLevel", OP_LEVEL, _ds.getField(PersonKeys.LEVEL));
		// If CON is high enough, HP+1; if CON is low enough, HP-1; else 0

		assertEquals("\tHP", OP_HP, _ds.getField(PersonKeys.HP));
		assertTrue("\tOccupation",
				((Occupation) _ds.getField(PersonKeys.OCCUPATION)).getName()
						.equalsIgnoreCase(IP_OCCUPATION));
		assertEquals("\tHunger", OP_HUNGER, _ds.getField(PersonKeys.HUNGER));

		// Row 2: Armor Class, Speed, Age, Height, and Weight
		assertEquals("\tAC", _ds.getField(PersonKeys.AC), OP_AC);
		assertEquals("\tSpeed", _ds.getField(PersonKeys.SPEED), OP_SPEED);
		// assertEquals ("\tAge", _ds.getField(PersonKeys.AGE), OP_AGE);
		assertEquals("\tHeight", _ds.getField(PersonKeys.HEIGHT), OP_HEIGHT);
		assertEquals("\tWeight", _ds.getField(PersonKeys.WEIGHT), OP_WEIGHT);

		// Row 3: Gold, Silver, Gold Banked, and Load
		assertEquals("\tGold", _ds.getField(PersonKeys.GOLD), OP_GOLD);
		assertEquals("\tSilver", _ds.getField(PersonKeys.SILVER), OP_SILVER);
		assertEquals("\tGold banked", _ds.getField(PersonKeys.GOLD_BANKED),
				OP_GOLD_BANKED);
		assertEquals("\tLoad", _ds.getField(PersonKeys.LOAD), OP_LOAD);

		// Row 4: Languages: Max and Known (List)
		assertEquals("\tMax Langs", _ds.getField(PersonKeys.MAX_LANGS),
				OP_MAX_LANGS);
		assertEquals("\tLanguages", _ds.getField(PersonKeys.LANGUAGES),
				Arrays.asList(OP_LANGUAGE));

		// Row 5: Description of the new Hero
		assertTrue("\tDescription",
				((String) _ds.getField(PersonKeys.DESCRIPTION))
						.equalsIgnoreCase(OP_DESCRIPTION));

		return true;
	}

	/**
	 * Verify that all three INT mods are correct: Languages known, Number of
	 * learnable languages, and Literacy
	 * 
	 * @parm
	 */
	@SuppressWarnings("unchecked")
    private void verifyIntMods() {
		MsgCtrl.msgln("A01_N01.verifyIntMods()");

		ArrayList<String> langList = (ArrayList<String>) (_ds
				.getField(PersonKeys.LANGUAGES));
		String lang = langList.get(0);

		MsgCtrl.msgln("\tLanguages Known Mod = " + lang);
		// MsgCtrl.msgln("\tSTR Damage Mod = " + _mock.getDamageMod());
		// MsgCtrl.msgln("\tSTR Weight Allowance =  " +
		// _mock.getWeightAllowance());

		assertEquals("Languages Known Mod fails", OP_LANGUAGE, lang);

	}

	/**
	 * Verify that all three STR mods are correct: ToHitMelee, Damage, and
	 * Weight Allowance
	 * 
	 * @parm str Hero's strength
	 * @parm weight Hero's weight, with half sigma of the median of OP_WEIGHT
	 */
	private void verifyStrMods(int str, double weight) {
		MsgCtrl.msgln("A01_N01.verifyStrMods()");
		MsgCtrl.msgln("\tSTR ToHit Mod = " + _mock.getToHitMelee());
		MsgCtrl.msgln("\tSTR Damage Mod = " + _mock.getDamageMod());
		MsgCtrl.msgln("\tSTR Weight Allowance =  " + _mock.getWeightAllowance());

		assertEquals("ToHitMelee Mod fails", OP_TOHITMELEE_MOD,
				_mock.getToHitMelee());
		assertEquals("Damage Mod fails", OP_DAMAGE_MOD, _mock.getDamageMod());
		int expectedWeightAllow = (int) (str / 11.5 * weight * Constants.OUNCES_PER_POUND);
		assertEquals("Weight Allowance fails", expectedWeightAllow,
				_mock.getWeightAllowance());

	}

	/**
	 * Verify that the Person's fixed traits are as expected. CON and CHR are
	 * not included in this test because they are random.
	 * 
	 * @See verifyConChrTraits(int, int)
	 * 
	 * @param expTraits
	 *            the expected values for the six prime trait for every hero;
	 * @param resultTraits
	 *            the actual results for the six prime traits
	 */
	protected void verifyTraits(AttributeList expTraits,
			AttributeList resultTraits) {
		MsgCtrl.msgln("A01_N01.verifyTraits()");
		// final int TRAIT_MIN = 8;
		// final int TRAIT_MAX = 18;

		// First show the primate traits
		MsgCtrl.msg("\tSTR = " + resultTraits.get(ATTRIBUTE.STR));
		MsgCtrl.msg("\tINT = " + resultTraits.get(ATTRIBUTE.INT));
		MsgCtrl.msg("\tWIS = " + resultTraits.get(ATTRIBUTE.WIS));
		MsgCtrl.msg("\tDEX = " + resultTraits.get(ATTRIBUTE.DEX));

		assertEquals("\tStrength fail", expTraits.get(ATTRIBUTE.STR),
				resultTraits.get(ATTRIBUTE.STR));
		assertEquals("\tIntelligence fail", expTraits.get(ATTRIBUTE.INT),
				resultTraits.get(ATTRIBUTE.INT));
		assertEquals("\tWisdom fail", expTraits.get(ATTRIBUTE.WIS),
				resultTraits.get(ATTRIBUTE.WIS));
		assertEquals("\tDexterity fail", expTraits.get(ATTRIBUTE.DEX),
				resultTraits.get(ATTRIBUTE.DEX));
		MsgCtrl.msgln("\n");
	}

	/**
	 * Verify that the CON and CHR are in the proper range.
	 * 
	 * @param min
	 *            lowest CON and CHR allowed
	 * @param max
	 *            highest CON and CHR allowed
	 */
	protected void verifyConChrTraits(int con, int chr, int min, int max) {
		MsgCtrl.msgln("A01_N01.verifyConChrTraits()");

		MsgCtrl.msg("\tCON = " + con);
		MsgCtrl.msg("\tCHR = " + chr);
		// CON must be within 8 and 18
		assertTrue("\tConstitution fail", (con <= max) && (con >= min));
		// CHR must be within 8 and 18 for a male
		assertTrue("\tCharisma fail", (chr <= max) && (chr >= min));
		MsgCtrl.msgln("\n");
	}

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * PROTECTED HELPER METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	// /** Pack the expected data into a data shuttle for comparison
	// *
	// * @return data shuttle of model data (internal format) for this widget
	// */
	// protected DataShuttle<PersonKeys> buildVerifyShuttle()
	// {
	// // Package each of these into the data shuttle
	// DataShuttle<PersonKeys> outShuttle = new
	// DataShuttle<PersonKeys>(PersonKeys.class);
	// // Pack all the input data expected to come back as output data
	// outShuttle.putField(PersonKeys.NAME, IP_NAME);
	// outShuttle.putField(PersonKeys.GENDER, IP_GENDER);
	// outShuttle.putField(PersonKeys.OCCUPATION, IP_OCCUPATION);
	// outShuttle.putField(PersonKeys.RACENAME, IP_RACENAME);
	// outShuttle.putField(PersonKeys.KLASSNAME, IP_KLASSNAME);
	//
	// // Pack all the expected output data
	// // Row 1: XP, Level, Hit Points, [Occupation], Hunger state
	// outShuttle.putField(PersonKeys.XP, OP_XP);
	// outShuttle.putField(PersonKeys.LEVEL, OP_LEVEL);
	// outShuttle.putField(PersonKeys.HP, OP_HP);
	// outShuttle.putField(PersonKeys.OCCUPATION, IP_OCCUPATION);
	// outShuttle.putField(PersonKeys.HUNGER, OP_HUNGER);
	//
	// // Row 2: Armor Class, Speed, Age, Height, and Weight
	// outShuttle.putField(PersonKeys.AC, OP_AC);
	// outShuttle.putField(PersonKeys.SPEED, OP_SPEED);
	// outShuttle.putField(PersonKeys.AGE, OP_AGE);
	// outShuttle.putField(PersonKeys.HEIGHT, OP_HEIGHT);
	// outShuttle.putField(PersonKeys.WEIGHT, OP_WEIGHT);
	//
	// // Row 3: Gold, Silver, Gold Banked, and Load
	// outShuttle.putField(PersonKeys.GOLD, OP_GOLD);
	// outShuttle.putField(PersonKeys.SILVER, OP_SILVER);
	// outShuttle.putField(PersonKeys.GOLD_BANKED, OP_GOLD_BANKED);
	// outShuttle.putField(PersonKeys.LOAD, OP_LOAD);
	//
	// // Row 4: Languages: Max and Known (List)
	// outShuttle.putField(PersonKeys.MAX_LANGS, OP_MAX_LANGS);
	// outShuttle.putField(PersonKeys.LANGUAGES, Arrays.asList(OP_LANGUAGES));
	//
	// // Row 5: Description of the new Hero
	// outShuttle.putField(PersonKeys.DESCRIPTION, OP_DESCRIPTION);
	// return outShuttle;
	// }

} // end of A01_N01 class
