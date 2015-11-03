/**
 * TestHeroDisplayCiv.java
 * Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.civ;

import java.io.File;
import java.util.ArrayList;

import junit.framework.TestCase;
import mylib.Constants;
import mylib.MsgCtrl;
import mylib.civ.DataShuttle;

import org.junit.After;
import org.junit.Before;

import pdc.character.Hero;
import civ.HeroDisplayCiv;
import civ.HeroDisplayCiv.MockHeroDisplayCiv;
import civ.PersonKeys;

/**
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Jul 31, 2010 // original
 *          <DD>
 *          </DL>
 */
public class TestHeroDisplayCiv extends TestCase
{

    // Person to use for Testing
    private final String NAME = "Clyde";
    private final String GENDER = "Male";
    private final String OCCUP = "Gambler";
    private final String HAIRCOLOR = "brown";
    private final String RACENAME = "Human";
    private final String KLASSNAME = "Peasant";

    // Person needed for this CIV
    private Hero _p = null;
    // Widget needed for this CIV
    // private JPanel _widget = null;
    // CIV reference and mock
    private HeroDisplayCiv _civ = null;
    private MockHeroDisplayCiv _mock = null;
    // Data shuttle for transfering person data
    DataShuttle<PersonKeys> _ds = null;

    // // File location for the person files
    // private final String PERSON_LOCATION =
    // "/Projects/workspace/Adventurer/resources/user/dormitory/";
    // File location for the person files
    private final String PERSON_LOCATION = "resources/user/dormitory/";
    // Extension for all Person (character) files
    private final String PERSON_FILE_EXT = ".chr";
    // Label for file chooser to identify person files
    // private final String PERSON_LABEL = "Person Files";
    // File name for default file
    private final String PERSON_DEFAULT_FILE = PERSON_LOCATION + NAME
            + PERSON_FILE_EXT;
    // File location for the person files
    // private final String PERSON_SAVEFILE = "myHero";
    //
    // // Number of Items of each category in the test Inventory
    // private final int GENERAL_NBR = 9;
    // private final int PROVISION_NBR = 2;
    // private final int WEAPON_NBR = 1;
    // private final int CASH_NBR = 2;
    // private final int ARMOR_NBR = 0;
    // private final int VALUEABLE_NBR = 0;

    // Model output values
    // Row 1: XP, Level, Hit Points, [Occupation], Hunger state
    private final int OP_XP = 0;
    private final int OP_LEVEL = 0;
    private final int OP_HP = 10;
    private final String OP_HUNGER = "FULL";

    // Row 2: Armor Class, Speed, Age, Height, and Weight
    private final int OP_AC = 10;
    private final double OP_SPEED = 4.0;
    // private final double OP_AGE = 17.0;
    private final double OP_HEIGHT = 70.0;
    private final double OP_WEIGHT = 175.0;

    // Row 3: Gold, Silver, Gold Banked, and Load
    private final int OP_GOLD = 15;
    private final int OP_SILVER = 8;
    private final double OP_GOLD_BANKED = 0.0;
    private final int OP_LOAD = 464; // ounces + 38 ounces in cash;

    // Row 4: Languages: Max and Known (List)
    private final String OP_LANGUAGES = "Common";
    private final int OP_MAX_LANGS = 2; // for INT = 11

    // Row 5: Description of the new Hero
    private final String OP_DESCRIPTION = "A giant male with brown hair. He is weather-beaten and tough.";

    // DISPLAY VALUES when different from Person output data
//    private final String DSP_XP = "0";
//    private final String DSP_LEVEL = "0";
//    private final String DSP_HP = "10";

    // Row 2: Armor Class, Speed, Age, Height, and Weight
//    private final String DSP_AC = "10";
//    private final String DSP_SPEED = "4.0";
    // private final String DSP_AGE = "17.0";
//    private final String DSP_HEIGHT = "6' 9\"";
//    private final String DSP_WEIGHT = "175";

    // // Row 3: Gold, Silver, Gold Banked, and Load
    // private final String DSP_GOLD = "15";
    // private final String DSP_SILVER = "8";
    // private final String DSP_GOLD_BANKED = "0.0";
    // private final String DSP_LOAD = "29.0"; // 426 ounces + 38 ounces in
    // cash;
    //
    // // Row 4: Languages: Max and Known (List)
    // private final String DSP_MAX_LANGS = "2"; // for INT = 11
    //
    // // Number of general items in the Item display
    // // Fields names are: Category, Name, Quantity, LbWt, and OzWt
    // private final int FIELD_NBR = 5;
    //
    // // Used for error testing
    // private enum BadTest {
    // FRAMIS, TROBE;
    // };

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(true);
        _p = new Hero(NAME, GENDER, OCCUP, HAIRCOLOR, RACENAME);
        assertNotNull(_p);
        _civ = new HeroDisplayCiv(null);
        assertNotNull(_civ);
        _mock = _civ.new MockHeroDisplayCiv();
        assertTrue(_mock != null);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _p = null;
        _civ = null;
        _mock = null;
        _ds = null;
        MsgCtrl.auditMsgsOn(false);
    }

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ TESTS BEGIN HERE!
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    public void testConvertAttributes()
    {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(true);
        // MsgCtrl.msg(this, "\n testConvertAttributes()");
        MsgCtrl.msgln(this, "\n testConvertAttributes()");
        MsgCtrl.errMsgln(this, "Test code not yet implemented");
    }

    public void testConvertItems()
    {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(true);
        MsgCtrl.msg(this, "\n testConvertItems()");
        MsgCtrl.errMsgln(this, "\tTest code not yet implemented");
    }

    public void testConvertSkills()
    {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(true);
        MsgCtrl.msg(this, "\n testConvertSkills()");
        MsgCtrl.errMsgln(this, "\tTest code not yet implemented");
    }

    public void testGetPersonFileData()
    {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(true);
        MsgCtrl.msg(this, "\n testGetPersonFileData()");
        MsgCtrl.errMsgln(this, "\tTest code not yet implemented");
    }

    /**
     * Ctor: Prepare the Hero for displaying the Hero's properties.
     * <code>HeroDisplayCiv(Container, ObservableModel&ltPersonKeys&gt) </code>
     * 
     * @throws InstantiationException
     *             only if both parms are null
     * 
     * @Normal Display existing Hero, DataShuttle is created, and this
     *         HeroDisplayCiv is an observer of the model parm passed
     * @Null proper exception through for both parms null; ; 1 parm is permitted
     *       to be null
     * @Error n/a
     */
    public void testHeroDisplayCiv() throws InstantiationException
    {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(true);
        MsgCtrl.msgln(this, "\n testHeroDisplayCiv()");

        // NORMAL. Civ is created in setup, verify it here
        assertTrue(verifyPersonData(_p));

        // // NULL model case as Normal
        // _civ = null;
        // _civ = new HeroDisplayCiv(null);
        // assertNotNull(_civ);

        // EXCEPTION case with both parms null
    }

    // /**
    // * Convert the DataShuttle to a formatted String shuttle for display.
    // * <code>DataShuttle<PersonKeys> convertToDisplay(DataShuttle<PersonKeys>
    // ds)</code>
    // *
    // * @Normal compare before and after conversion
    // * @Error missing special and normal key, empty shuttle,
    // * @Null shuttle passed
    // */
    // public void testConvertToDisplay()
    // {
    // MsgCtrl.auditMsgsOn(false);
    // MsgCtrl.errorMsgsOn(false);
    // MsgCtrl.msgln(this, "\ttestConvertToDisplay()");
    //
    // // NORMAL Compare person data with Stringized version
    // // Get the data from the Person created in setup
    // _ds = loadPersonData();
    // assertNotNull(_ds);
    // DataShuttle<PersonKeys> ws = _civ.convertToDisplay(_ds);
    // assertNotNull(ws);
    // assertTrue(verifyPersonDisplay(ws));
    //
    // // ERROR Shuttle is missing a special key: LOAD
    // _ds.clear();
    // _ds = loadPersonData();
    // _ds.removeKey(PersonKeys.LOAD);
    // MsgCtrl.msg("\tExpected error: shuttle missing special format key...");
    // ws = _civ.convertToDisplay(_ds);
    // assertNotNull(ws);
    // assertEquals(ws.size(), _ds.size());
    // MsgCtrl.msgln("\tReceived");
    //
    // // ERROR Shuttle is missing a normal key (not requiring special
    // // formatting)
    // _ds.clear();
    // _ds = loadPersonData();
    // _ds.removeKey(PersonKeys.DESCRIPTION);
    // MsgCtrl.msg("\tExpected error: shuttle missing key not requiring formatting...");
    // _ds.setErrorType(ErrorType.UNKNOWN);
    // ws = _civ.convertToDisplay(_ds);
    // assertTrue(DataShuttle.hasErrors(ws));
    // MsgCtrl.msgln("\tReceived");
    //
    // // ERROR Shuttle is empty
    // MsgCtrl.msg("\tExpected error: shuttle empty...");
    // _ds.clear();
    // ws = _civ.convertToDisplay(_ds);
    // assertNull(ws);
    // MsgCtrl.msgln("\tReceived");
    //
    // // NULL Shuttle is null
    // MsgCtrl.msg("\tExpected error: null shuttle...");
    // ws = _civ.convertToDisplay(null);
    // assertNull(ws);
    // MsgCtrl.msgln("\tReceived");
    // }

    /**
     * Save the Person to a new file. <code>boolean savePerson()</code>
     * 
     * @throws Exception
     * 
     * @Normal Save person to file (create), then overwrite (replaced), then to
     *         different
     * @Error Wrapper so most testing done in PersonReadWriter
     * @Null null filename passed in
     */
    public void testSavePerson() throws Exception
    {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(true);
        MsgCtrl.msgln(this, "\ntestSavePerson()");

        // NORMAL Verify that person is saved to newly created file under
        // default name
        File heroFile = new File(PERSON_DEFAULT_FILE); // long path to
                                                       // destination file
        // MsgCtrl.msgln("File path is " + PERSON_DEFAULT_FILE);
        // assertTrue(heroFile.exists());
        heroFile.delete();
        assertFalse(heroFile.exists());
        assertNotNull(_p);
        MsgCtrl.msg("\tSaving file for Hero " + heroFile.getName());
        assertTrue(_civ.savePerson(false)); // name only; save will expand path
        // Reattach File object to serialized file in the proper directory
        heroFile = new File(PERSON_DEFAULT_FILE); // long path to destination
                                                  // file
        long fileLen = heroFile.length();
        MsgCtrl.msgln("\t" + fileLen + "  bytes found");
        assertTrue(heroFile.getCanonicalFile().exists());
        assertTrue(heroFile.getAbsoluteFile().exists());

        // NORMAL Replace an existing Hero file
        heroFile = new File(PERSON_DEFAULT_FILE); // long path to destination
                                                  // file
        assertTrue(heroFile.exists());
        assertNotNull(_p);
        assertTrue(_civ.savePerson(false)); // name only; save will expand path
        // Confirm that file exists in the proper directory
        fileLen = heroFile.length();
        MsgCtrl.msgln("\tHero file " + heroFile.getName() + "written: "
                + fileLen + "  bytes");
        assertTrue(heroFile.exists());
        // Remove test file
        heroFile.delete();
        assertFalse(heroFile.exists());

        // // NULL filename string
        // assertFalse(_civ.savePerson(null));
    }

    /**
     * Convert a list of fields for each Item to a String list for a a
     * particular category. Inventory is created when the Person object is
     * created. <br>
     * Fields: name, category, quantity, weight in lb, remaining weight in
     * ounces. Enter the kind of items as an Inventory sublist wanted for
     * display; must be the name of one of the ItemCategories. Returns the
     * EnumMap of Item fields and data for that category.
     * 
     * @Normal HeroDisplay.getItemsByCategory(ItemCategory category)
     * @Null HeroDisplay.getItemsByCategory(ItemCategory category)
     */
    // public void testGetItemsByCategory()
    // {
    // MsgCtrl.auditMsgsOn(false);
    //
    // // NORMAL tests
    // // Call method to extract and test some categories
    // extractCategory(ItemCategory.GENERAL, GENERAL_NBR);
    // extractCategory(ItemCategory.PROVISION, PROVISION_NBR);
    // extractCategory(ItemCategory.WEAPON, WEAPON_NBR);
    // extractCategory(ItemCategory.CASH, CASH_NBR);
    // extractCategory(ItemCategory.ARMOR, ARMOR_NBR);
    // extractCategory(ItemCategory.VALUEABLE, VALUEABLE_NBR);
    //
    // // NULL test (second parm = tells the helper method that null is expected
    // extractCategory(null, Chronos.NOT_FOUND);
    // }

    /**
     * Fail to extract a set of Items from the Inventory
     * 
     * @Error HeroDisplay.getItemsByCategory(ItemCategory category)
     */
    // public void testFailGetItemsByCategory()
    // {
    // MsgCtrl.auditMsgsOn(false);
    // // ERROR: What if the Inventory didn't already exist?
    // // Ask for an illegal catgeory
    // // Clear out the Person's inventory for this failed test
    // MockPerson personMock = _p.new MockPerson();
    // personMock.clearInventory();
    // extractCategory(ItemCategory.GENERAL, GENERAL_NBR);

    // // ERROR: What if the Inventory didn't already exist?
    // // Inventory must be created for this test
    // Inventory inven = new Inventory();
    // assertNotNull(inven);
    // inven.initStartingInventory();
    // }

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ Private Helper
     * Methods ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    // /** Extract a list of Items by their category
    // * @param category of Item
    // * @param number of items of that category in the inventory used (if
    // NOT_FOUND
    // * is entered, then the itemList is expected to be null
    // */
    // private void extractCategory(ItemCategory category, int nbrItems)
    // {
    // MsgCtrl.auditMsgsOn(false);
    // MsgCtrl.msgln("testExtractCategory():");
    //
    // ArrayList<EnumMap<HeroKeys.ItemFields, String>> things;
    // // Get ArrayList of all General category items //
    // things = _civ.getItemsByCategory(category);
    // // Check against a null item, as indicated by a NOT_FOUND for nbrItems
    // if (nbrItems == Chronos.NOT_FOUND) {
    // assertNull(things);
    // return;
    // }
    // // Verify that the correct number was extracted
    // assertTrue(things.size() == nbrItems);
    // // Get the EnumMap of fields from the Arraylist
    // for (int k=0; k < things.size(); k++) {
    // EnumMap<ItemFields, String> map = things.get(k);
    // // MsgCtrl.msg("\titem " + k + ": \t" + things.get(k));
    // assertEquals(map.size(), FIELD_NBR);
    // // Verify that the correct category was extracted
    // assertTrue(map.get(ItemFielws.CATEGORY).
    // equals(category.toString()));
    // // Walk the map and print the fields: Category, Name, Quantity, LbWt, and
    // OzWt
    // MsgCtrl.msg("\tName = \t" + map.get(ItemFields.NAME));
    // MsgCtrl.msg("\tCategory = \t" + map.get(ItemFields.CATEGORY));
    // MsgCtrl.msg("\tQuantity = \t" + map.get(ItemFields.QTY));
    // MsgCtrl.msg("\tWeight (lb) = \t" + map.get(ItemFields.LBWT));
    // MsgCtrl.msg("\tWeight (oz) = \t" + map.get(ItemFields.OZWT));
    // MsgCtrl.msgln(" ");
    // }
    // }

    // /** Esnure that the right charisma descriptor is returned for the given
    // CHR. */
    // public void testChrDesc()
    // {
    // MsgCtrl.auditMsgsOn(false);
    // MsgCtrl.msgln(this, "\ttestChrDesc()");
    // // Possible descriptors for negative charismas in increasing order,
    // // Ranges from CHR=8 to CHR=18 are normal; CHR=7 and CHR=19 are
    // exceptional
    // // and probably never occur
    // final String[] descs = {
    // "crippled and horribly ugly", // < 8
    // "horribly scarred", "scarred from war or fire",
    // "the result of years of misery", // 8-10
    // "weather-beaten and tough", "nothing special to look at", // 11-12
    // "clear-eyed and rugged but not handsome", // 13
    // "slightly attractive if one could scrape off the years of wear and tear",
    // // 14
    // "a handsome adventurer", "gorgeous", "very attactive", // 15-17
    // "stunningly beautiful", // 18
    // "mesmerizing, and you will do whatever this person commands of you", // >
    // 18
    // };
    // final int MIN_CHR = 8;
    // final int MAX_CHR = 18;
    // final int START_CHR = 6; // start with out-of-range charisma
    // final int END_CHR = 20; // end with out-of-range charisma
    // for (int chr = START_CHR; chr <=END_CHR; chr++) {
    // String chrDesc = _mr.initChrDescriptor(chr);
    // // Check for special case outside normal range
    // if (chr < MIN_CHR) {
    // assertTrue(chrDesc.equalsIgnoreCase(descs[0]));
    // }
    // else if (chr > MAX_CHR) {
    // assertTrue(chrDesc.equalsIgnoreCase(descs[descs.length-1]));
    // }
    // else {
    // assertTrue(chrDesc.equalsIgnoreCase(descs[chr-MIN_CHR+1]));
    // }
    // }
    // } // end test method

    // /** Test various permutations of the Person's description. This requires
    // * direct access into the Race component via MockRace
    // *
    // * @Normal Race.initBodyType(int charisma) not yet
    // * @Error Race.initBodyType(int charisma) not yet
    // * @Null Race.initBodyType(int charisma) not yet
    // */
    // public void testInitBodyType()
    // {
    // MsgCtrl.auditMsgsOn(false);
    // MsgCtrl.msgln(this, "\ttestInitBodyType()");
    // // Turn off error messages once this test passes
    // MsgCtrl.errorMsgsOn(false);
    //
    // // Test permutations of the height/weight pairs in the range of
    // // MIN_HT (27.5) to MAX_HT (81.7), and MIN_WT (41.5) to MAX_WT (204.1)
    // // Plus two out of range
    // final int[] heights = {28, 60, 81}; // short, average, tall
    // final int[] weights = {42, 145, 204}; // light, average, heavy
    // final String[][] negTypes = {
    // {"tiny", "pudgy", "squat"}, // Short height
    // {"slinky", "average-size", "heavy"}, // Average height
    // {"skinny", "tall", "giant"} // Tall height
    // };
    // final String[][] posTypes = {
    // // Light Average Heavy
    // {"petite", "compact", "burly"}, // Short height
    // {"lithe", "athletic", "muscular"}, // Average height
    // {"thin", "tall", "towering"}, // Tall height
    // };
    //
    // // NORMAL cases, plus two ERROR cases
    // int chr = 0;
    // // Low CHR values
    // for (chr = 7; chr < 12; chr++) {
    // MsgCtrl.msgln("Charisma =  " + chr);
    // for (int h=0; h < heights.length; h++) {
    // for (int w=0; w < weights.length; w++) {
    // String bodyType = _mr.testBodyType(heights[h], weights[w], chr);
    // MsgCtrl.msgln("\t[Ht, Wt] = [" + heights[h] + ", " + weights[w] + "] => "
    // + bodyType);
    // assertTrue(bodyType.equalsIgnoreCase(negTypes[h][w]));
    // }
    // }
    // }
    // MsgCtrl.msgln("");
    // // High CHR values
    // for (chr = 12; chr <= 19; chr++) {
    // MsgCtrl.msgln("Charisma =  " + chr);
    // for (int h=0; h < heights.length; h++) {
    // for (int w=0; w < weights.length; w++) {
    // String bodyType = _mr.testBodyType(heights[h], weights[w], chr);
    // MsgCtrl.msgln("\t[Ht, Wt] = [" + heights[h] + ", " + weights[w] + "] => "
    // + bodyType);
    // assertTrue(bodyType.equalsIgnoreCase(posTypes[h][w]));
    // }
    // }
    // }
    // } // end test method

    // /** Test the description using hair color. The other parts of the
    // description are
    // * part of Race, and already tested.
    // */
    // public void testInitDescription()
    // {
    // /** Suppress messageson this now tested method */
    // MsgCtrl.setSuppression(true);
    // // Give a few hair colors and test the results
    // String desc = _mock.testInitDescription("blue", 8);
    // MsgCtrl.msgln("\n" + desc);
    // desc = _mock.testInitDescription("red", 15);
    // MsgCtrl.msgln(desc);
    // desc = _mock.testInitDescription("magenta", 18);
    // MsgCtrl.msgln(desc);
    // /** Reset messages on*/
    // MsgCtrl.setSuppression(true);
    // }

    // /** Test chkFirstVowel of a string to a vowel or not */
    // public void testChkFirstVowel()
    // {
    // String[] trueWords = {"always", "elephant", "iota","omega", "usually",
    // "Unusual"};
    // String[] falseWords = {"big", "foxy", "historian", "yippee"};
    //
    // for (int k=0; k < trueWords.length; k++) {
    // assertTrue(_mock.chkFirstVowel(trueWords[k]));
    // }
    // for (int k=0; k < falseWords.length; k++) {
    // assertFalse(_mock.chkFirstVowel(falseWords[k]));
    // }
    // }

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ TESTS END HERE!
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * List of methods that do not need JUnit test because they are too trivial,
     * or some other test method tests them equally well. <br>
     * <code>heroDisplayCiv()</code>: -- Unused default contructor<br>
     * <code>convertToModel(DataShuttle&ltNewHeroFields&gt)</code>: -- unused
     * override <br>
     * <code>getDefaults()</code>: -- unused override <br>
     * <code>getFieldData()</code>: -- <br>
     * <code>getInventorySize()</code>: -- wrapper <br>
     * <code>isValid(DataShuttle&ltNewHeroFields&gt)</code>: -- unused override <br>
     * <code>populateAttributes(DataShuttle&ltPersonKeys&gt ds)</code>-- calls
     * GUI <code>populateInventory(ArrayList&ltItem&gt)</code> -- calls GUI
     * <code>populateSkills(ArrayList&ltSkill&gt)</code> -- calls GUI
     */
    public void notNeeded()
    {}

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE HELPER
     * METHODS! ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /** Load data shuttle */
    private DataShuttle<PersonKeys> loadPersonData()
    {
        _p.new MockHero();

        // Build data shuttle with all the desired keys
        DataShuttle<PersonKeys> ds = new DataShuttle<PersonKeys>(
                PersonKeys.class);
        for (PersonKeys key : PersonKeys.values()) {
            ds.assignKey(key);
        }
        return ds;
    }

    /**
     * Load a data shuttle from the Person and verify that the Person contains
     * the expected data.
     * 
     * @param p
     *            Person, the model, created
     * @return true only if all asserts passed
     */
    private boolean verifyPersonData(Hero p)
    {
        // Null guards
        if (p == null) {
            return false;
        }
        _ds = loadPersonData();
        assertNotNull(_ds);

        // temps
        String s = null;
        int intNum = Constants.UNASSIGNED;
        double dblNum = -99.00;
        // Verify that the Person contains the expected data
        // Compare each requested key value with the expected output value
        // NamePlate before Attribute grid: Name, gender, Race, Klass
        s = (String) _ds.getField(PersonKeys.NAME);
        assertEquals(s, NAME);
        s = (String) _ds.getField(PersonKeys.GENDER);
        assertEquals(s, GENDER);
        s = (String) _ds.getField(PersonKeys.RACENAME);
        assertEquals(s, RACENAME);
        s = (String) _ds.getField(PersonKeys.KLASSNAME);
        assertEquals(s, KLASSNAME);
        // Row 1: XP, Level, Hit Points, Occupation, Hunger state
        intNum = ((Integer) _ds.getField(PersonKeys.XP)).intValue();
        assertEquals(intNum, OP_XP);
        intNum = ((Integer) _ds.getField(PersonKeys.LEVEL)).intValue();
        assertEquals(intNum, OP_LEVEL);
        intNum = ((Integer) _ds.getField(PersonKeys.HP)).intValue();
        assertEquals(intNum, OP_HP);
        s = (String) _ds.getField(PersonKeys.OCCUPATION);
        assertEquals(s, OCCUP);
        s = (String) _ds.getField(PersonKeys.HUNGER);
        assertEquals(s, OP_HUNGER);

        // Row 2: Armor Class, Speed, Age, Height, and Weight
        intNum = ((Integer) _ds.getField(PersonKeys.AC)).intValue();
        assertEquals(intNum, OP_AC);
        dblNum = ((Double) _ds.getField(PersonKeys.SPEED)).doubleValue();
        assertEquals(dblNum, OP_SPEED, 0.05);
        intNum = ((Integer) _ds.getField(PersonKeys.HEIGHT)).intValue();
        assertEquals(intNum, OP_HEIGHT, OP_HEIGHT * 0.16);
        intNum = ((Integer) _ds.getField(PersonKeys.WEIGHT)).intValue();
        assertEquals(intNum, OP_WEIGHT, OP_WEIGHT * 0.16);

        // Row 3: Gold, Silver, Gold Banked, and Load (wt carried)
        intNum = (Integer) _ds.getField(PersonKeys.GOLD);
        assertEquals(intNum, OP_GOLD);
        intNum = (Integer) _ds.getField(PersonKeys.SILVER);
        assertEquals(intNum, OP_SILVER);
        dblNum = ((Double) _ds.getField(PersonKeys.GOLD_BANKED)).doubleValue();
        assertEquals(dblNum, OP_GOLD_BANKED);
        intNum = ((Integer) _ds.getField(PersonKeys.LOAD)).intValue();
        assertEquals(intNum, OP_LOAD);

        // Row 4: Languages: Max and Known (List)
        intNum = ((Integer) _ds.getField(PersonKeys.MAX_LANGS)).intValue();
        assertEquals(intNum, OP_MAX_LANGS);
        @SuppressWarnings("unchecked")
        ArrayList<String> langs = (ArrayList<String>) _ds
                .getField(PersonKeys.LANGUAGES);
        assertEquals(1, langs.size());
        assertEquals(langs.get(0), OP_LANGUAGES);

        // Row 5: Description of the new Hero
        s = (String) _ds.getField(PersonKeys.DESCRIPTION);
        assertEquals(s, OP_DESCRIPTION);

        return true;
    }

    /**
     * Compare the display version of the data shuttle with the original data.
     * Except for special cases, the labeling is added by the widget, and the
     * shuttle contains only data values.
     * 
     * @param ws
     *            shuttle containing data formatted for the widget
     * @return true only if all asserts passed
     */
//    private boolean verifyPersonDisplay(DataShuttle<PersonKeys> ws)
//    {
//        // Null guards
//        if (ws == null) {
//            return false;
//        }
//        if (DataShuttle.hasErrors(ws)) {
//            return false;
//        }
//        // temps
//        String s = null;
//        int intNum = 0;
//        double dblNum = 0.0f;
//
//        // Verify that the Person contains the expected data
//        // Compare each requested key value with the expected output value
//        // NamePlate before Attribute grid: Name, gender, Race, Klass
//        s = (String) ws.getField(PersonKeys.NAME);
//        assertEquals(s, NAME);
//        s = (String) ws.getField(PersonKeys.GENDER);
//        assertEquals(s, GENDER);
//        s = (String) ws.getField(PersonKeys.RACENAME);
//        assertEquals(s, RACENAME);
//        s = (String) ws.getField(PersonKeys.KLASSNAME);
//        assertEquals(s, KLASSNAME);
//
//        // Row 1: XP, Level, Hit Points, Occupation, Hunger state
//        s = (String) ws.getField(PersonKeys.XP);
//        assertEquals(s, DSP_XP);
//        s = (String) ws.getField(PersonKeys.LEVEL);
//        assertEquals(s, DSP_LEVEL);
//        s = (String) ws.getField(PersonKeys.HP);
//        assertEquals(s, DSP_HP);
//        s = (String) ws.getField(PersonKeys.OCCUPATION);
//        assertEquals(s, OCCUP);
//        s = (String) ws.getField(PersonKeys.HUNGER);
//        assertEquals(s, OP_HUNGER);
//
//        // Row 2: Armor Class, Speed, Age, Height, and Weight
//        s = (String) ws.getField(PersonKeys.AC);
//        assertEquals(s, DSP_AC);
//        s = (String) ws.getField(PersonKeys.SPEED);
//        assertEquals(s, DSP_SPEED);
//        s = (String) ws.getField(PersonKeys.HEIGHT);
//        assertEquals(s, DSP_HEIGHT);
//        s = (String) ws.getField(PersonKeys.WEIGHT);
//        assertEquals(s, DSP_WEIGHT);
//
//        // Row 3: Gold, Silver, Gold Banked, and Load (wt carried)
//        intNum = ((Item) ws.getField(PersonKeys.GOLD)).getQuantity();
//        assertEquals(intNum, OP_GOLD);
//        intNum = ((Item) ws.getField(PersonKeys.SILVER)).getQuantity();
//        assertEquals(intNum, OP_SILVER);
//        dblNum = ((Double)
//                ws.getField(PersonKeys.GOLD_BANKED)).doubleValue();
//        assertEquals(dblNum, OP_GOLD_BANKED);
//        intNum = ((Integer) ws.getField(PersonKeys.LOAD)).intValue();
//        assertEquals(intNum, OP_LOAD);
//
//        // Row 4: Languages: Max and Known (List)
//        intNum = ((Integer) ws.getField(PersonKeys.MAX_LANGS)).intValue();
//        assertEquals(intNum, OP_MAX_LANGS);
//        @SuppressWarnings("unchecked")
//        ArrayList<String> langs = (ArrayList<String>)
//                ws.getField(PersonKeys.LANGUAGES);
//        assertEquals(1, langs.size());
//        assertEquals(langs.get(0), OP_LANGUAGES);
//
//        // Row 5: Description of the new Hero
//        s = (String) ws.getField(PersonKeys.DESCRIPTION);
//        assertEquals(s, OP_DESCRIPTION);
//
//        return true;
//    }

} // end of TestHeroDisplayCiv class
