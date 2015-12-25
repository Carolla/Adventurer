/**
 * TestTown.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


/*
 * Town(String, String, String) addBuildings(String[]) buildingRegContainsName(String)
 * equals(IRegistryElement) getAllBuildings() getBuilding(String) getCostOfLiving()
 * getDayDescription() getKey() getName() getNightDescription() setCostOfLiving(double) toString()
 */


package test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.Town;
import chronos.pdc.buildings.Building;
import mylib.MsgCtrl;
import test.pdc.buildings.FakeBuilding;

/**
 * Verify that the Town Class works as expected
 * 
 * @author Alan Cline
 * @version Feb 6, 2013 // original <br>
 *          Oct 17, 2014 // added more tests <br>
 */
public class TestTown
{
    /** Target object */
    private Town _town = null;

    /** Expected value for name */
    private String NAME = "Test Town";
    /** Expected Cost of Living */
    private double EXP_COL = 1.0;
    /** Expected value for daytime description */
    private String DESC_DAY = "Of all the towns in all the world, she had to walk into mine.";
    /** Expected value for night-time description */
    private String DESC_NIGHT =
            "The town square was eerily quiet, except for the chirp of evil crickets.";

    /** Expected list of buildings to add to Town */
    static private String[] _bldgNames = {"Arcaneum", "Jail", "Monastery",
            "Rat's Pack General Store",
            "Rogues' Den", "Stadium", "The Bank", "Ugly Ogre Inn"};

    static private List<Building> _bldgList;

    /** Number of buildings added for this test */
    private int NBR_BUILDINGS = 8;

    /** Registries to open and close before and after testing */
    // static private BuildingRegistry _bldgReg = null;
    // static private NPCRegistry _patrons = null;


    // ===========================================================================
    // Fixtures
    // ===========================================================================

    /** Initial prep */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        int bSize = _bldgNames.length;
        _bldgList = new ArrayList<Building>(bSize);
        for (int k = 0; k < bSize; k++) {
            _bldgList.add(new FakeBuilding(_bldgNames[k]));
        }
    }

    /** Once only */
    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
        // MsgCtrl.auditMsgsOn(true);
        // MsgCtrl.msgln("\t Inside cleanUp() method");
        // // Close secondary registries
        // _bldgReg.closeRegistry();
        // _patrons.closeRegistry();
        // _townReg.closeRegistry(false);
        // _dgn.closeRegistry(false);
        // MsgCtrl.auditMsgsOn(false);
    }

    @Before
    public void setUp() throws Exception
    {
        // Create the target town
        _town = new Town(NAME, DESC_DAY, DESC_NIGHT);
        assertNotNull(_town);
        _town.addBuildings(_bldgList);
    }

    @After
    public void tearDown() throws Exception
    {
        _town = null;
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }

    // ===========================================================================
    // BEGIN TESTS
    // ===========================================================================

    /*
     * Town(String, String, String) addBuildings(String[]) buildingRegContainsName(String)
     * equals(IRegistryElement) getAllBuildings() getBuilding(String) getCostOfLiving()
     * getDayDescription() getKey() getName() getNightDescription() setCostOfLiving(double)
     * toString()
     */

    /**
     * Test constructor was built correctly in setUp()
     * 
     * @Normal Create a full Town without Buildings and Arena
     * @Normal Create a full Town with Buildings and Arena
     * @Error null parms
     */
    @Test
    public void testTown()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testTown()");

        // NORMAL verify setup() data
        assertEquals(NAME, _town.getName());
        assertEquals(DESC_DAY, _town.getDayDescription());
        assertEquals(DESC_NIGHT, _town.getNightDescription());
        assertEquals(EXP_COL, _town.getCostOfLiving(), 0.05);
        assertEquals(NBR_BUILDINGS, _town.getAllBuildings().size());
        dump(_town);

        // NORMAL verify null descNight is ok with throw-away town
        assertNotNull(new Town(NAME, DESC_DAY, null));

        // ERROR null name parm
        try {
            assertNull(new Town(null, DESC_DAY, DESC_NIGHT));
        } catch (NullPointerException ex) {
            MsgCtrl.msgln("\n\t Expected exception: " + ex.getMessage());
        }
    }


    /**
     * Tests that are not implemented besides simple getters and setters
     * 
     * @Not_Needed addBuilding(String bldgName) // part of Town() test
     */
    public void NotNeeded()
    {}

    // ===========================================================================
    // Private Helper Methods
    // ===========================================================================

    /**
     * Display the contents of the town
     * 
     * @param town to display
     */
    private void dump(Town town)
    {
        MsgCtrl.msgln("\n\t TOWN DUMP:");
        MsgCtrl.msgln("\t Town name \t" + town.getName());
        MsgCtrl.msgln("\t Daylight: \t" + town.getDayDescription());
        MsgCtrl.msgln("\t Nightime: \t" + town.getNightDescription());
        MsgCtrl.msgln("\t Cost of living: \t" + town.getCostOfLiving());
        // Display all the buildings in town
        List<Building> bList = _town.getAllBuildings();
        MsgCtrl.msg("\t Buildings in town:\t ");
        for (Building s : bList) {
            MsgCtrl.msg(s.getName() + ",  ");
        }

    }

} // end of TestTown class
