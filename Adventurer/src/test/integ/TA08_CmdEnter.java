/**
 * TA08_EnterBuilding.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.command.CommandFactory;
import pdc.command.Scheduler;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.NPCRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.BuildingDisplayCiv;
import civ.CommandParser;
import civ.MainframeCiv;

/**
 * Enter a specified building from the building's exterior or from the town. If the Hero is outside
 * a particular Building, no name is required.
 * <P>
 * Format: {@code ENTER [BuildingName]} enters the specified Building if not already in one, or
 * enters the current building if one is not specified. <br>
 * <P>
 * Implementation Note: The {@code MainFrameProxy}, which implements {@code MainframeInterface},
 * intervenes between the {@code civ.CommandParser} and the {@code hic.Mainframe}. All inputs are
 * sent directly to the {@code civ.CmdParser} and outputs are received by the
 * {@code test.integ.MainframeProxy}.
 * 
 * @author Alan Cline
 * @version Dec 20, 2014 // original <br>
 *          Mar 5, 2015 // updated for more testing <br>
 *          Jun 22, 2015 // replaced {@code hic.integ.IOProxy} with {@code hic.integ.MainframeProxy} <br>
 *          Jun 30, 2015 // adding in remainder of {@code CmdEnter} integration tests <br>
 *          Jul 5, 2015 // adding in error cases <br>
 */
public class TA08_CmdEnter
{
    static private CommandParser _cp = null;
    static private BuildingDisplayCiv _bldgCiv = null;
    static private MainframeProxy _mfProxy = null;
    static private MainframeCiv _mfCiv;
    static private RegistryFactory _regFactory = null;
    static private BuildingRegistry _bReg = null;

    /** List of valid Buildings that can be entered */
    static private List<String> _bldgs = null;
    private static Scheduler _skedder;


    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        // Start up the support classes
        _regFactory = RegistryFactory.getInstance();
        _bReg = (BuildingRegistry) _regFactory.getRegistry(RegKey.BLDG);

        // Replace the GUI objects with their test facades
        _mfProxy = new MainframeProxy();
        // This will open the BuildingRegistry, which must be closed before exiting
        _bldgCiv = new BuildingDisplayCiv(_mfProxy, _bReg);

        _mfCiv = new MainframeCiv(_mfProxy, _bldgCiv);
        _skedder = new Scheduler();
        _cp = new CommandParser(_skedder, new CommandFactory(_mfCiv, _bldgCiv));
        
        // Get list of names for all buildings that can be entered
        _bldgs = _bReg.getElementNames();
    }


    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
        // Close BuildingRegistry, left open from BuildingDisplayCiv
        _bReg.closeRegistry();
        // Close NPCRegistry, left open from BuildingDisplayCiv
        NPCRegistry npcReg = (NPCRegistry) _regFactory.getRegistry(RegKey.NPC);
        npcReg.closeRegistry();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        // Set Hero back to town with no current Building
        resetBuildingState();
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }


    // ==========================================================
    // Begin the tests!
    // ==========================================================


    /**
     * Normal case: Enter a valid building from the town (no current building)
     * 
     * @throws InterruptedException
     */
    @Test
    public void test_EnterBuildingFromTownOrExterior() throws InterruptedException
    {
        MsgCtrl.where(this);

        for (int k = 0; k < _bldgs.size(); k++) {
            // Setup: onTown must be true, and inBuilding flag must be false
            resetBuildingState();
            assertTrue(_bldgCiv.isOnTown());
            assertFalse(_bldgCiv.isInside());

            // TEST
            _cp.receiveCommand("Enter " + _bldgs.get(k));

            // After Cmd is executed...
            _skedder.doOneCommand();
            
            // Confirm Hero is no longer on town, but is inside a building
            assertFalse(_bldgCiv.isOnTown());
            assertTrue(_bldgCiv.isInside());

            String bName = _bldgCiv.getCurrentBuilding();
            MsgCtrl.msg("\tBuilding name = " + bName);
            assertTrue("Expected " + _bldgs.get(k) + ", got " + bName, bName.equals(_bldgs.get(k)));

        }
    }


    /**
     * Normal case: Enter a valid building from outside the current Building (no parms)
     * 
     * @throws InterruptedException
     */
    @Test
    public void test_EnterCurrentBuilding() throws InterruptedException
    {
        MsgCtrl.where(this);

        // Loop for each Building in the BuildingRegistry
        for (int k = 0; k < _bldgs.size(); k++) {
            resetBuildingState();
            
            String bName = _bldgs.get(k);
            _cp.receiveCommand("Approach " + bName);
            _skedder.doOneCommand();
            assertFalse(_bldgCiv.isOnTown());
            assertFalse(_bldgCiv.isInside());

            // TEST
            _cp.receiveCommand("Enter");

            // After Cmd is executed...
            _skedder.doOneCommand();

            // VERIFY
            // Confirm Hero is no longer on town, but is inside a building
            assertFalse(_bldgCiv.isOnTown());
            assertTrue(_bldgCiv.isInside());
            
            // Hero is inside the correct building, now the current building
            String newCurrent = _bldgCiv.getCurrentBuilding();
            MsgCtrl.msgln("\tCurrent Building = " + newCurrent);
            assertEquals("Expected " + _bldgs.get(k) + ", got " + newCurrent, bName, newCurrent);
        } // end of loop
    }


    /**
     * Error case: Attempt to enter a building from inside another Building
     * 
     * @throws InterruptedException
     */
    @Test
    public void test_EnterFromInsideBuilding()
    {
        MsgCtrl.where(this);

        String bName1 = "Ugly Ogre Inn";
        String bName2 = "Arcaneum";

        // Setup: Enter a Building, and then try to enter same building
        _cp.receiveCommand("Enter " + bName1);
        _skedder.doOneCommand();

        assertFalse(_bldgCiv.isOnTown());
        assertTrue(_bldgCiv.isInside());
        assertEquals(bName1, _bldgCiv.getCurrentBuilding());

        // Test1: Try to enter the same building
        _cp.receiveCommand("Enter " + bName1);
        _skedder.doOneCommand();

        // VERIFY that Hero is still inside building
        assertFalse(_bldgCiv.isOnTown());
        assertTrue(_bldgCiv.isInside());
        assertEquals(bName1, _bldgCiv.getCurrentBuilding());

        // Test2: Try to enter a different building
        _cp.receiveCommand("Enter " + bName2);
        _skedder.doOneCommand();

        // VERIFY Test 2 results
        assertFalse(_bldgCiv.isOnTown());
        assertTrue(_bldgCiv.isInside());
        assertEquals(bName1, _bldgCiv.getCurrentBuilding());
    }


    // ============================================================================
    // PRIVATE HELPER METHODS
    // ============================================================================

    /** Hero is onTwon, with not current Building, and not inside one */
    private void resetBuildingState()
    {
        _mfCiv.openTown();
    }


} // end of TA08_EnterBuilding integration test case
