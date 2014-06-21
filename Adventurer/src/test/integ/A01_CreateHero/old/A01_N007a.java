package test.integ.A01_CreateHero.old;

import pdc.Inventory;
import pdc.character.Person;
import pdc.character.Person.MockPerson;

import civ.NewHeroCiv;
import civ.NewHeroCiv.MockNewHeroCiv;
import civ.NewHeroFields;
import civ.PersonKeys;

import chronos.pdc.Occupation;
import chronos.pdc.Skill;

import mylib.ApplicationException;
import mylib.MsgCtrl;
import mylib.civ.DataShuttle;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JPanel;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Integration Test to create a person and verify that they were created
 * properly
 * 
 * @author Timothy Armstrong
 * @version <DL>
 *          <DT>Build 1.0 November 21, 2011 // original
 *          <DD>
 *          </DL>
 */
public class A01_N007a extends TestCase {
    /* Internal reusable objects */
    private Person _p = null;
    private MockPerson _mockP = null;

    /** Object under test */
    private NewHeroCiv _nhCiv = null;
    /** Mock Object to access private methods to test */
    private MockNewHeroCiv _mockCiv = null;
    /** DataShuttle for passing the field data */
    private DataShuttle<NewHeroFields> _ws = null;
    /** Test widget for simulating input data */
    private JPanel _widget = null;
    /** DataShuttle for passing the model data */
    private DataShuttle<PersonKeys> _ds = null;

    // Constants for testing submit()
    private final String IP_NAME = "Terry Brambleberry";
    private final String IP_GENDER = "Male";
    private final String IP_HAIRCOLOR = "black";
    private final String IP_RACENAME = "Hobbit";
    private final String IP_KLASSNAME = "Peasant";
    private final String IP_OCCUPATION = "Trapper";
    // // Model values from the user input
    // private final int OP_SIZE = 21;

    // Model output values
    // Row 1: XP, Level, Hit Points, [Occupation], Hunger state
    private final int OP_XP = 0;
    private final int OP_LEVEL = 0;
    private final int OP_HP = 10;
    private final String OP_HUNGER = "FULL";

    // Row 2: Armor Class, Speed, Age, Height, and Weight
    private final double OP_SPEED = 3.0;
    private final double OP_AGE = 42.5;
    private final int OP_HEIGHT = 56;
    private final int OP_WEIGHT = 150;

    // Row 3: Gold, Silver, Gold Banked, and Load
    private final int OP_GOLD = 15;
    private final int OP_SILVER = 8;
    private final double OP_GOLD_BANKED = 0.0;
    private final int OP_LOAD = 464; // ounces + 38 ounces in cash;

    // Row 4: Languages: Max and Known (List)
    private final String[] OP_LANGUAGES = { "Common", "Tolkien" };
    private final int OP_MAX_LANGS = 2; // for INT = 12

    // Row 5: Description of the new Hero
    private final String OP_DESCRIPTION = "An average-size male with black hair. He is the result of years of misery.";

    // Skill Tab
    private final String[][] OP_SKILLS = {
            { "Trapping", "Set, find, or remove simple mechanical traps" },
            { "Infravision 60'", "Can see warm bodies in the dark" },
            { "Geasing",
                    "Able to detect undeground direction and stone-based traps" } };

    // Inventory tab
    private final String[] OP_ITEMS = { "Cloak", "Belt pouch, small",
            "Pair of Boots", "Tinderbox, Flint & Steel", "Torches",
            "Water skein" };
    private final int OP_INVENTORY_SIZE = 14;

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ LET THE TESTS
     * BEGIN! ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Create the objects needed for testing; called before each test. The
     * methods are called, as independent of the Race and Klass as possible.
     */
    protected void setUp() throws Exception {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(true);

        // Create a civ without a model
        // _widget = new JPanel();
        // assertNotNull(_widget);
        _nhCiv = new NewHeroCiv();
        assertNotNull(_nhCiv);
        _mockCiv = _nhCiv.new MockNewHeroCiv();
        assertNotNull(_mockCiv);
        // Verify both shuttles are created
        _ws = _mockCiv.getWS();
        assertNotNull(_ws);
        _ds = _mockCiv.getDS();
        assertNotNull(_ds);
        _p = new Person();
        assertNotNull(_p);
        _mockP = _p.new MockPerson();
        assertNotNull(_mockP);
    }

    /** Release the objects created during setup; called after each test */
    protected void tearDown() {
        _p = null;
        _mockP = null;
        _widget = null;
        _nhCiv = null;
        _mockCiv = null;
        _ws = null;
        _ds = null;
    }

    /*
     * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ PERSON
     * CREATION TESTS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Test method for A01_N001.
     * 
     * @Normal OK
     * @Error NA
     * @Null NA
     */
    @Test
    public void testA01_N001() {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\tTest");

        // Pack the data into the shuttle
        _ws = packFields(_ws);
        assertNotNull(_ws);

        // First, we record the input data passed to the nhciv
        _ds = _mockCiv.convertToModel(_ws);

        // Now, make sure the person was created properly
        DataShuttle<PersonKeys> ds2 = _mockCiv.createPerson(_ws);

        // Verify all fields the same from _ds to _p
        assertEquals(_ds.getField(PersonKeys.NAME),
                ds2.getField(PersonKeys.NAME));
        assertEquals(_ds.getField(PersonKeys.GENDER),
                ds2.getField(PersonKeys.GENDER));
        assertEquals(_ds.getField(PersonKeys.OCCUPATION),
                ds2.getField(PersonKeys.OCCUPATION));
        assertEquals(_ds.getField(PersonKeys.RACENAME),
                ds2.getField(PersonKeys.RACENAME));
        assertEquals(_ds.getField(PersonKeys.KLASSNAME),
                ds2.getField(PersonKeys.KLASSNAME));

        // Then, make sure the data is displayed properly
        _p = _mockCiv.getPerson();

        // Verify all fields the same from _ds to _p
        assertEquals(ds2.getField(PersonKeys.NAME), _p.getName());
        assertEquals(ds2.getField(PersonKeys.GENDER), _p.getGender());
        assertEquals(ds2.getField(PersonKeys.OCCUPATION), _p.getOccupation());
        assertEquals(ds2.getField(PersonKeys.RACENAME), _p.getRaceName());
        assertEquals(ds2.getField(PersonKeys.KLASSNAME), _p.getKlassName());

        // Verify Inventory
        Inventory i = _p.getInventory();
        // Check a few items in the inventory
        for (int l = 0; l < OP_ITEMS.length; l++) {
            assertTrue(i.hasItem(OP_ITEMS[l]));
        }
        // Check that the inventory size is correct
        assertEquals(OP_INVENTORY_SIZE, i.getNbrItems());

        // Verify Skills - iterating through OP_SKILLS for each listed skill
        ArrayList<Skill> skills = _p.getSkills();
        Skill p_skill = null;
        for (int j = 0; j < OP_SKILLS.length; j++) {
            int k = 0;
            {
                try {
                    p_skill = new Skill(OP_SKILLS[j][k], OP_SKILLS[j][k + 1]);
                } catch (ApplicationException e) {
                    MsgCtrl.errMsgln(this, "Error creating skill during test");
                }
                assertTrue(skills.get(j).getName()
                        .equalsIgnoreCase(p_skill.getName()));
                assertTrue(skills.get(j).getDescription()
                        .equalsIgnoreCase(p_skill.getDescription()));
            }
        }

        // Verify Attributes
        _p.loadPersonData(_ds);
        // assertEquals (_ds.getField(PersonKeys.AC), OP_AC);
        assertEquals(_ds.getField(PersonKeys.AGE), OP_AGE);
        assertTrue(((String) _ds.getField(PersonKeys.DESCRIPTION))
                .equalsIgnoreCase(OP_DESCRIPTION));
        assertEquals(_ds.getField(PersonKeys.GENDER), IP_GENDER);
        assertEquals(_ds.getField(PersonKeys.GOLD), OP_GOLD);
        assertEquals(_ds.getField(PersonKeys.GOLD_BANKED), OP_GOLD_BANKED);
        assertEquals(_ds.getField(PersonKeys.HAIR_COLOR), IP_HAIRCOLOR);
        assertEquals(_ds.getField(PersonKeys.HEIGHT), OP_HEIGHT);
        assertEquals(_ds.getField(PersonKeys.HP), OP_HP);
        assertEquals(_ds.getField(PersonKeys.HUNGER), OP_HUNGER);
        assertTrue(((String) _ds.getField(PersonKeys.KLASSNAME))
                .equalsIgnoreCase(IP_KLASSNAME));
        assertEquals(_ds.getField(PersonKeys.LANGUAGES),
                Arrays.asList(OP_LANGUAGES));
        assertEquals(_ds.getField(PersonKeys.LEVEL), OP_LEVEL);
        // assertEquals (_ds.getField(PersonKeys.LITERACY), OP_LITERACY);
        assertEquals(_ds.getField(PersonKeys.LOAD), OP_LOAD);
        assertEquals(_ds.getField(PersonKeys.MAX_LANGS), OP_MAX_LANGS);
        assertTrue(((String) _ds.getField(PersonKeys.NAME))
                .equalsIgnoreCase(IP_NAME));
        assertTrue(((Occupation) _ds.getField(PersonKeys.OCCUPATION)).getName()
                .equalsIgnoreCase(IP_OCCUPATION));
        assertTrue(((String) _ds.getField(PersonKeys.RACENAME))
                .equalsIgnoreCase(IP_RACENAME));
        assertEquals(_ds.getField(PersonKeys.SILVER), OP_SILVER);
        assertEquals(_ds.getField(PersonKeys.SPEED), OP_SPEED);
        assertEquals(_ds.getField(PersonKeys.WEIGHT), OP_WEIGHT);
        assertEquals(_ds.getField(PersonKeys.XP), OP_XP);

    }

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE
     * HELPER METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */
    /**
     * Pack the internal fields then send the new Hero data to the Civ to be
     * validated. Scavanged method from NewHeroDisplay
     * 
     * @param ds
     *            Data shuttle with default data from NHCiv
     * @return data shuttle of fields for this widget
     */
    private DataShuttle<NewHeroFields> packFields(DataShuttle<NewHeroFields> ds) {
        // Package each of these into the data shuttle
        ds.putField(NewHeroFields.NAME, IP_NAME);
        ds.putField(NewHeroFields.GENDER, IP_GENDER);
        ds.putField(NewHeroFields.HAIR_COLOR, IP_HAIRCOLOR);
        ds.putField(NewHeroFields.OCCUPATION, IP_OCCUPATION);
        ds.putField(NewHeroFields.RACENAME, IP_RACENAME);
        ds.putField(NewHeroFields.KLASSNAME, IP_KLASSNAME);
        assertEquals(NewHeroFields.values().length, ds.size() + 3);
        return ds;
    }

} // end of A01_N002a class
