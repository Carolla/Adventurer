/**
 * TestNewHeroCiv.java
 * Copyright (c) 2011, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package test.civ;

import junit.framework.TestCase;

/**
 * Units tests for NewHeroCiv.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 May 8, 2011 // original
 *          <DD>
 *          <DT>Build 1.1 Oct 1, 2011 // refactored for MVP Data Shuttle
 *          <DD>
 *          <DT>Build 1.2 Nov 3, 2011 // final polishing
 *          <DD>
 *          </DL>
 */
public class TestNewHeroCiv extends TestCase 
{
  
//	/** Object under test */
//	private NewHeroCiv _nhCiv = null;
//	/** Mock Object to access private methods to test */
//	private MockNewHeroCiv _mock = null;
//	/** DataShuttle for passing the field data */
//	private DataShuttle<NewHeroFields> _ws = null;
//	/** DataShuttle for passing the model data */
//	private DataShuttle<PersonKeys> _ds = null;
//
//	/** Number of keys in the PersonField enum */
//	private final int NBR_DEF_KEYS = 6;
//	/** Default Person attributes on initial display */
//	private final String DEF_NAME = "";
//	private final String DEF_GENDER = "Male";
//	private final String DEF_RACENAME = "Human";
//	private final String DEF_OCCUPATION = "None";
//	private final String DEF_HAIRCOLOR = "bald";
//	private final int RACE_LISTSIZE = 7;
//	private final int OCCUPATION_LISTSIZE = 28;
//	private final int HAIRCOLOR_LISTSIZE = 9;
//
//	// Constants for testing submit()
//	private final String IP_NAME = "Hoffmok the Beefy";
//	private final String IP_GENDER = "Female";
//	private final String IP_HAIRCOLOR = "red";
//	private final String IP_RACENAME = "Human";
//	private final String IP_KLASSNAME = "Peasant";
//	private final String IP_OCCUPATION = "Woodworker";
//
//	// Model output values
//	// Row 1: XP, Level, Hit Points, [Occupation], Hunger state
//	private final int OP_XP = 0;
//	private final int OP_LEVEL = 0;
//	private final int OP_HP = 10;
//	private final String OP_HUNGER = "FULL";
//
//	// Row 2: Armor Class, Speed, Age, Height, and Weight
//	private final int OP_AC = 10;
//	private final double OP_SPEED = 3.0;
//	private final double OP_AGE = 17.0;
//	private final double OP_HEIGHT = 72.0;
//	private final double OP_WEIGHT = 150.0;
//
//	// Row 3: Gold, Silver, Gold Banked, and Load
//	private final int OP_GOLD = 15;
//	private final int OP_SILVER = 8;
//	private final double OP_GOLD_BANKED = 0.0;
//	private final int OP_LOAD = 464; // ounces + 38 ounces in cash;
//
//	// Row 4: Languages: Max and Known (List)
//	private final String OP_LANGUAGES = "Common";
//	private final int OP_MAX_LANGS = 2; // for INT = 12
//
//	// Row 5: Description of the new Hero
//	private final String OP_DESCRIPTION = "A tall female with red hair. She is nothing special to look at.";
//
//	// Skill list only contain the occupational skill (for Human Peasant)
//	private final String OP_SKILLNAME = "Woodworking";
//	private final int NBR_SKILLS = 1;
//
//	// Intelligence bounds for literacy
//	private final int MIN_READ_INT = 10;
//	private final int MIN_WRITE_INT = 12;
//
//	// The number of items in inventory, including cash
//	private final int NBR_ITEMS = 14;
//	// The expected weight of the inventory (in ounces), including cash
//	private final int ITEMS_WT = 464;
//
//	// Position in list for default data
//	private final int DEF_POS = 0;
//
//	/**
//	 * Creates test object and its mock before each test.
//	 * 
//	 * @throws java.lang.Exception
//	 */
//	@Before
//	public void setUp() throws Exception {
//		MsgCtrl.auditMsgsOn(true);
//		MsgCtrl.errorMsgsOn(true);
//		// Ensure that the GUI is turned off for this series of tests
//		Constants.IN_TEST = true;
//		_nhCiv = new NewHeroCiv();
//		assertNotNull(_nhCiv);
//		_mock = _nhCiv.new MockNewHeroCiv();
//		assertNotNull(_mock);
//		// Verify both shuttles are created
//		_ws = _mock.getWS();
//		assertNotNull(_ws);
//		_ds = _mock.getDS();
//		assertNotNull(_ds);
//	}
//
//	/**
//	 * Tears down all pre-states after each test
//	 * 
//	 * @throws java.lang.Exception
//	 */
//	@After
//	public void tearDown() throws Exception {
//		Constants.IN_TEST = false;
//		_nhCiv = null;
//		_mock = null;
//		_ws = null;
//		_ds = null;
//		MsgCtrl.errorMsgsOn(false);
//	}
//
//	/*
//	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ BEGIN TESTING
//	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
//	 */
//
//	/**
//	 * Tests constructor <code>NewHeroCiv(Container widget, 
//	 *          ObservableModel&ltPersonKeys&gt model)</code>
//	 * 
//	 * @Normal constructor works correctly for various inputs
//	 * @Error n/a
//	 * @Null expected exception thrown
//	 */
//	public void testNewHeroCiv() {
//		MsgCtrl.auditMsgsOn(false);
//		MsgCtrl.errorMsgsOn(false);
//		MsgCtrl.msgln(this, "\ttestNewHeroCiv() -- constructor");
//
//		// NORMAL case -- created in setup
//		assertSame(_ds, _mock.getDS());
//		assertSame(_ws, _mock.getWS());
//		MsgCtrl.msgln("\tdata shuttle " + _ds);
//		MsgCtrl.msgln("\twidget shuttle " + _ws);
//		assertEquals(_ws.size(), 0);
//		assertEquals(_ds.size(), 0);
//		_ws.display(true);
//		_ds.display(true);
//	}
//
//	/**
//	 * Tests <code>getDefaults()</code>.
//	 * 
//	 * @Normal correct default data is retrieved and supplied to the widget
//	 * @Error n/a
//	 * @Null n/a
//	 */
//	@SuppressWarnings("unchecked")
//	public void testGetDefaults() {
//		MsgCtrl.auditMsgsOn(false);
//		MsgCtrl.errorMsgsOn(false);
//		MsgCtrl.msgln(this, "\ttestGetDefaults()");
//
//		// Get default data from the widget keys, and the collection from the
//		// Civ
//		_ws = _nhCiv.getDefaults();
//		assertNotNull(_ws);
//		assertEquals(DataShuttle.ErrorType.OK, _ws.getErrorType());
//		// Show the populated shuttle data
//		_ws.display(true);
//		// Verify the results
//		assertEquals(NBR_DEF_KEYS, _ws.size());
//		assertEquals(DEF_NAME, _ws.getField(NewHeroFields.NAME));
//		assertEquals(DEF_GENDER, _ws.getField(NewHeroFields.GENDER));
//		List<String> list = (List<String>) _ws
//				.getField(NewHeroFields.RACE_OPTIONS);
//		assertEquals(list.get(DEF_POS), DEF_RACENAME);
//		assertEquals(RACE_LISTSIZE, list.size());
//
//		list = (List<String>) _ws
//				.getField(NewHeroFields.OCCUPATION_OPTIONS);
//		assertEquals(list.get(DEF_POS), DEF_OCCUPATION);
//		assertEquals(OCCUPATION_LISTSIZE, list.size());
//
//		list = (List<String>) _ws
//				.getField(NewHeroFields.HAIR_COLOR_OPTIONS);
//		assertEquals(list.get(DEF_POS), DEF_HAIRCOLOR);
//		assertEquals(HAIRCOLOR_LISTSIZE, list.size());
//	}
//
//	/**
//	 * Tests method <code>submit(DataShuttle&ltPersonKeys&gt)</code>
//	 * 
//	 * @Normal confirm that the model is updated from the widget;
//	 * @Error n/a testInvalid() checks parm-level handling
//	 * @Null proper error handling for null parms, cleared shuttle, and null
//	 *       shuttle
//	 * @Special extra shuttle field is ignored and method proceeds normally
//	 */
//	public void testSubmit() {
//		MsgCtrl.auditMsgsOn(false);
//		MsgCtrl.errorMsgsOn(false);
//		MsgCtrl.msgln(this, "\ttestSubmit() ");
//
//		// Simulate input data from the widget
//		_ws = new DataShuttle<NewHeroFields>(NewHeroFields.class);
//		_ws.putField(NewHeroFields.NAME, IP_NAME);
//		_ws.putField(NewHeroFields.HAIR_COLOR, IP_HAIRCOLOR);
//		_ws.putField(NewHeroFields.GENDER, IP_GENDER);
//		_ws.putField(NewHeroFields.OCCUPATION, IP_OCCUPATION);
//		_ws.putField(NewHeroFields.RACENAME, IP_RACENAME);
//		// This is not entered by the user, but is a default
//		_ws.putField(NewHeroFields.KLASSNAME, IP_KLASSNAME);
//
//		// NORMAL - Get the widget shuttle back after updating the Person model
//		// Person should not yet be populated
//		_ws = _nhCiv.submit(_ws);
//		assertNotNull(_ws);
//		assertEquals(NBR_DEF_KEYS, _ws.size());
//		assertFalse(DataShuttle.hasErrors(_ws));
//
//		// _ds returned contains the input data
//		_ds = _mock.getDS();
//		assertNotNull(_ds);
//		assertFalse(DataShuttle.hasErrors(_ds));
//		assertEquals(_ds.size(), _ws.size());
//
//		// SPECIAL -- extra field in widget shuttle should be ignored
//		int oldSize = _ws.size();
//		ArrayList<String> extraHair = new ArrayList<String>();
//		extraHair.add("Fuzzy");
//		_ws.putField(NewHeroFields.HAIR_COLOR_OPTIONS, extraHair);
//		int newSize = _ws.size();
//		assertEquals(newSize, oldSize + 1);
//		_ws = _nhCiv.submit(_ws);
//		assertFalse(DataShuttle.hasErrors(_ws));
//
//		// NULL -- input name
//		oldSize = _ws.size();
//		_ws.putField(NewHeroFields.NAME, null);
//		newSize = _ws.size();
//		assertEquals(oldSize, newSize);
//		_ws = _nhCiv.submit(_ws);
//		MsgCtrl.errMsgln("\tExpected error: shuttle contains null NAME value");
//		assertTrue(DataShuttle.hasErrors(_ws));
//
//		// NULL shuttle
//		assertNull(_nhCiv.submit(null));
//
//		// NULL -- Empty shuttle
//		_ws.clear();
//		assertEquals(_ws.size(), 0);
//		_nhCiv.submit(_ws);
//		assertTrue(DataShuttle.hasErrors(_ws));
//
//		// ERROR -- invalid name field
//		_ws.putField(NewHeroFields.NAME,
//				"Name is too long: greater than 30 characters");
//		_ws = _nhCiv.submit(_ws);
//		MsgCtrl.errMsgln("\tExpected error: shuttle contains contains overly long Name");
//		assertTrue(DataShuttle.hasErrors(_ws));
//	}
//
//	/**
//	 * Tests
//	 * <code>convertToModel(DataShuttle&ltDataShuttle&ltNewHeroFields&gt ws) </code>
//	 * 
//	 * @Normal widget shuttle data is transferred into model shuttle
//	 * @Error n/a
//	 * @Null n/a
//	 */
//	public void testConvertToModel() {
//		MsgCtrl.auditMsgsOn(false);
//		MsgCtrl.errorMsgsOn(false);
//		MsgCtrl.msgln(this, "\ttestConvertToModel() -- private target method");
//
//		// NORMAL -- widget shuttle data is transferred into model shuttle
//		_ws.putField(NewHeroFields.NAME, IP_NAME);
//		_ws.putField(NewHeroFields.GENDER, IP_GENDER);
//		_ws.putField(NewHeroFields.RACENAME, IP_RACENAME);
//		_ws.putField(NewHeroFields.OCCUPATION, IP_OCCUPATION);
//		_ws.putField(NewHeroFields.HAIR_COLOR, IP_HAIRCOLOR);
//		_ws.putField(NewHeroFields.KLASSNAME, IP_KLASSNAME);
//		_ws.display(true);
//		assertEquals(_ws.size(), NBR_DEF_KEYS);
//		_ds = _mock.convertToModel(_ws);
//		assertEquals(_ds.size(), NBR_DEF_KEYS);
//		_ds.display(true);
//
//		// NULL -- no widget shuttle
//		assertNull(_mock.convertToModel(null));
//		MsgCtrl.errMsgln("\tExpected error: null widget shuttle");
//
//		// NULL -- empty widget shuttle
//		// Create an empty copy with the default keys
//		DataShuttle<NewHeroFields> emptyShuttle = new DataShuttle<NewHeroFields>(
//				NewHeroFields.class);
//		assertEquals(emptyShuttle.size(), 0);
//		assertNull(_mock.convertToModel(emptyShuttle));
//		MsgCtrl.errMsgln("\tExpected error: empty widget shuttle");
//
//		// ERROR -- missing key value, in this case, GENDER
//		// Clear only one key instead of all of them
//		_ws.removeKey(NewHeroFields.GENDER);
//		_ds = _mock.convertToModel(_ws);
//		assertEquals(_ds.size(), NBR_DEF_KEYS);
//		assertTrue(_ws.getErrorType() != ErrorType.OK);
//		MsgCtrl.errMsg("\tExpected error: " + _ws.getErrorMessage());
//		MsgCtrl.errMsgln("\t" + _ws.getErrorSource());
//	}
//
//	/**
//	 * Tests <code>createPerson(DataShuttle&ltNewHeroFields&gt inMap) </code>
//	 * 
//	 * @Normal known options generate known Person attributes, inventory,
//	 *         literacy, and skills
//	 * @Error bad input widget shuttle returns
//	 * @Error force a CREATION_INSTANTIATION exception
//	 * @Null
//	 */
//	public void testCreatePerson() {
//		MsgCtrl.auditMsgsOn(true);
//		MsgCtrl.errorMsgsOn(true);
//		MsgCtrl.msgln(this, "\ttestCreatePerson() -- private target method");
//
//		// Create the person from test input data
//		_ws = loadInputData(_ws);
//		_mock.createPerson(_ws);
//		// Check person's attributes
//		Person p = _mock.getPerson();
//		assertTrue(verifyPersonData(p));
//		// Check person's inventory, or at least the number of items and the
//		// weight
//		assertEquals(p.getInventory().getNbrItems(), NBR_ITEMS);
//		assertEquals(p.getInventory().calcInventoryWeight(), ITEMS_WT);
//		// Check person's skill
//		ArrayList<Skill> skList = p.getSkills();
//		assertNotNull(skList);
//		assertTrue(skList.size() == 1);
//		MsgCtrl.msgln("\t" + p.getName() + " is skilled at "
//				+ skList.get(0).getName());
//		assertEquals(skList.size(), NBR_SKILLS);
//		assertEquals(skList.get(0).getName(), OP_SKILLNAME);
//		// Check the person's literacy
//		AttributeList traits = p.getTraits();
//		int intel = traits.get(ATTRIBUTE.INT);
//		MsgCtrl.msgln("\tShe has intelligence of " + intel
//				+ ", so she can also Read & Write");
//		assertTrue((intel >= MIN_READ_INT) && (intel < MIN_WRITE_INT));
//		assertEquals(p.getLiteracy(), MiscKeys.Literacy.READING);
//
//		// ERROR -- force a CREATION_INSTANTIATION exception by proving null for
//		// required input
//		_ws.clearErrors();
//		_ws.putField(NewHeroFields.RACENAME, null);
//		// Clear the person in the civ
//		_mock.clearPerson();
//		_mock.createPerson(_ws);
//		p = _mock.getPerson();
//		assertNull(p);
//		assertTrue(_ws.getErrorType() == ErrorType.CREATION_EXCEPTION);
//
//		// ERROR -- errors exist in shuttle; haven't been cleared.
//		// Reset input shuttle and try with random error code
//		_ws.putField(NewHeroFields.RACENAME, IP_NAME);
//		_ws.setErrorType(ErrorType.FIELD_INVALID);
//		_mock.createPerson(_ws);
//		p = _mock.getPerson();
//		// Verify that no person was created
//		assertNull(p);
//		assertTrue(_ws.getErrorType() != ErrorType.OK);
//	}
//
//	/**
//	 * List of methods not necessary to test <br>
//	 * <code>isValid(DataShuttle&ltNewHeroFields&gt ws)</code> -- wrapper <br>
//	 */
//	public void notNeeded() {
//	}
//
//	/*
//	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ Private Helper
//	 * Methods ++++++++++++++++++++++++++++++++++++++++++++++++++++++
//	 */
//
//	/**
//	 * Load the data that creates the Person object
//	 * 
//	 * @param map
//	 *            shuttle to load
//	 * @return loaded shuttle, or null if a problem occurs
//	 */
//	private DataShuttle<NewHeroFields> loadInputData(
//			DataShuttle<NewHeroFields> map) {
//		MsgCtrl.msgln(this, "\t\tloadInputData()");
//		// Guarantee we are working with a pristine shuttle
//		map.clear();
//		map.putField(NewHeroFields.NAME, IP_NAME);
//		map.putField(NewHeroFields.GENDER, IP_GENDER);
//		map.putField(NewHeroFields.RACENAME, IP_RACENAME);
//		map.putField(NewHeroFields.OCCUPATION, IP_OCCUPATION);
//		map.putField(NewHeroFields.HAIR_COLOR, IP_HAIRCOLOR);
//		map.putField(NewHeroFields.KLASSNAME, IP_KLASSNAME);
//		return map;
//	}
//
//	/**
//	 * Load a data shuttle from the Person and verify that the Person contains
//	 * the expected INPUT data in the Person. Full output data is verifiedin the
//	 * HeroDisplayCiv and Person test classes.
//	 * 
//	 * @param p
//	 *            Person, the model, created
//	 * @return true only if all asserts passed
//	 */
//	private boolean verifyPersonData(Person p) {
//		// Null guards
//		if (p == null) {
//			return false;
//		}
//		// Build data shuttle with all the desired keys
//		DataShuttle<PersonKeys> ds = new DataShuttle<PersonKeys>(
//				PersonKeys.class);
//		// Load the actual data in the Person
//		ds = p.loadPersonData(ds);
//
//		// temps
//		String s = null;
//		int intNum = Constants.UNASSIGNED;
//		double dblNum = -99.00;
//		// Verify that the Person contains the expected data
//		// Compare each requested key value with the expected output value
//		// NamePlate before Attribute grid: Name, gender, Race, Klass
//		s = (String) ds.getField(PersonKeys.NAME);
//		MsgCtrl.msgln("\tVerifying Hero = " + s);
//		assertEquals(s, IP_NAME);
//		s = (String) ds.getField(PersonKeys.GENDER);
//		assertEquals(s, IP_GENDER);
//		s = (String) ds.getField(PersonKeys.RACENAME);
//		assertEquals(s, IP_RACENAME);
//		s = (String) ds.getField(PersonKeys.KLASSNAME);
//		assertEquals(s, IP_KLASSNAME);
//		// Hair color is transposed into the description; search for whole word
//		s = (String) ds.getField(PersonKeys.DESCRIPTION);
//		assertTrue(s.contains(" " + IP_HAIRCOLOR + " "));
//
//		// Row 1: XP, Level, Hit Points, Occupation, Hunger state
//		intNum = ((Integer) ds.getField(PersonKeys.XP)).intValue();
//		assertEquals(intNum, OP_XP);
//		intNum = ((Integer) ds.getField(PersonKeys.LEVEL)).intValue();
//		assertEquals(intNum, OP_LEVEL);
//		intNum = ((Integer) ds.getField(PersonKeys.HP)).intValue();
//		assertEquals(intNum, OP_HP);
//		Occupation ocp = (Occupation) ds.getField(PersonKeys.OCCUPATION);
//		assertEquals(IP_OCCUPATION, ocp.getName());
//		s = (String) ds.getField(PersonKeys.HUNGER);
//		assertEquals(s, OP_HUNGER);
//
//		// Row 2: Armor Class, Speed, Age, Height, and Weight
//		intNum = ((Integer) ds.getField(PersonKeys.AC)).intValue();
//		assertEquals(intNum, OP_AC);
//		dblNum = ((Double) ds.getField(PersonKeys.SPEED)).doubleValue();
//		assertEquals(dblNum, OP_SPEED, 0.05);
//		dblNum = ((Double) ds.getField(PersonKeys.AGE)).doubleValue();
//		assertEquals(dblNum, OP_AGE, 1.0);
//		intNum = ((Integer) ds.getField(PersonKeys.HEIGHT)).intValue();
//		assertEquals(intNum, OP_HEIGHT, OP_HEIGHT * MetaDie.HALF_SIGMA);
//		intNum = ((Integer) ds.getField(PersonKeys.WEIGHT)).intValue();
//		assertEquals(intNum, OP_WEIGHT, OP_WEIGHT * MetaDie.HALF_SIGMA);
//
//		// Row 3: Gold, Silver, Gold Banked, and Load (wt carried)
//		intNum = ((Integer) ds.getField(PersonKeys.GOLD)).intValue();
//		assertEquals(intNum, OP_GOLD);
//		intNum = ((Integer) ds.getField(PersonKeys.SILVER)).intValue();
//		assertEquals(intNum, OP_SILVER);
//		dblNum = ((Double) ds.getField(PersonKeys.GOLD_BANKED)).doubleValue();
//		assertEquals(dblNum, OP_GOLD_BANKED);
//		intNum = ((Integer) ds.getField(PersonKeys.LOAD)).intValue();
//		assertEquals(intNum, OP_LOAD);
//
//		// Row 4: Languages: Max and Known (List)
//		intNum = ((Integer) ds.getField(PersonKeys.MAX_LANGS)).intValue();
//		assertEquals(intNum, OP_MAX_LANGS);
//		@SuppressWarnings("unchecked")
//		ArrayList<String> langs = (ArrayList<String>) ds
//				.getField(PersonKeys.LANGUAGES);
//		assertEquals(1, langs.size());
//		assertEquals(langs.get(0), OP_LANGUAGES);
//
//		// Row 5: Description of the new Hero
//		s = (String) ds.getField(PersonKeys.DESCRIPTION);
//		assertEquals(s, OP_DESCRIPTION);
//
//		// Inventory tab: All Items that belong to the new Hero
//		Inventory inv = (Inventory) ds.getField(PersonKeys.INVENTORY);
//		assertNotNull(inv);
//		assertEquals(inv.getNbrItems(), NBR_ITEMS);
//		assertEquals(inv.calcInventoryWeight(), ITEMS_WT);
//
//		// Skills tab: Beginning skill and literacy for the new Hero
//		Literacy lit = (Literacy) ds.getField(PersonKeys.LITERACY);
//		assertNotNull(lit);
//		assertEquals(lit, MiscKeys.Literacy.READING);
//		@SuppressWarnings("unchecked")
//		ArrayList<Skill> skillList = (ArrayList<Skill>) ds
//				.getField(PersonKeys.SKILLS);
//		assertNotNull(skillList);
//		assertNotNull(skillList.get(0)); // check that the skillList does not
//											// contain a null entry
//		assertEquals(skillList.size(), NBR_SKILLS);
//		assertEquals(inv.calcInventoryWeight(), ITEMS_WT);
//		assertEquals(skillList.get(0).getName(), OP_SKILLNAME);
//
//		return true;
//	}
//
} // end of TestNewHeroCiv class
