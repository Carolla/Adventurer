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

import java.util.ArrayList;
import java.util.List;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.command.CommandFactory;
import chronos.civ.DefaultUserMsg;
import chronos.pdc.Adventure;
import chronos.pdc.buildings.Building;
import chronos.pdc.command.Scheduler;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.BuildingDisplayCiv;
import civ.CommandParser;
import civ.MainActionCiv;
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
  private static CommandParser _cp = null;
  private static BuildingDisplayCiv _bldgCiv = null;

  /** List of valid Buildings that can be entered */
  private static final List<String> _bldgs = new ArrayList<String>();
  private static final Scheduler _skedder = new Scheduler(new DefaultUserMsg());
  private static MainframeCiv _mfCiv;

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);

    RegistryFactory regFactory = new RegistryFactory();
    regFactory.initRegistries(_skedder);
    
    BuildingRegistry bReg = (BuildingRegistry) regFactory.getRegistry(RegKey.BLDG);
    AdventureRegistry advReg = (AdventureRegistry) regFactory.getRegistry(RegKey.ADV);
    Adventure adv = advReg.getAll().get(0);
    
    _mfCiv = new MainframeCiv().init(new MainframeProxy());
    MainActionCiv maCiv = new MainActionCiv(_mfCiv);
    maCiv.loadSelectedAdventure(adv.getName());
    _bldgCiv = new BuildingDisplayCiv(_mfCiv, adv, bReg);
    
    CommandFactory cmdFac = new CommandFactory(_mfCiv, _bldgCiv);
    cmdFac.initMap();
    _cp = new CommandParser(_skedder, cmdFac);

    // Get list of names for all buildings that can be entered
    for (Building b : bReg.getAll()) {
      _bldgs.add(b.getName());
    }
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
      // User Cmd is executed automatically

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
      assertFalse(_bldgCiv.isOnTown());
      assertFalse(_bldgCiv.isInside());

      // TEST
      _cp.receiveCommand("Enter");
      // User Cmd is executed automatically

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
    // User Cmd is executed automatically

    assertFalse(_bldgCiv.isOnTown());
    assertTrue(_bldgCiv.isInside());
    assertEquals(bName1, _bldgCiv.getCurrentBuilding());

    // Test1: Try to enter the same building
    _cp.receiveCommand("Enter " + bName1);

    // VERIFY that Hero is still inside building
    assertFalse(_bldgCiv.isOnTown());
    assertTrue(_bldgCiv.isInside());
    assertEquals(bName1, _bldgCiv.getCurrentBuilding());

    // Test2: Try to enter a different building
    _cp.receiveCommand("Enter " + bName2);

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
    _bldgCiv.openTown();
  }


} // end of TA08_EnterBuilding integration test case
