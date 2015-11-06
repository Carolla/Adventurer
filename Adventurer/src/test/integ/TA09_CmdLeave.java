/**
 * TA08_EnterBuilding.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.command.CommandFactory;
import chronos.pdc.Command.Scheduler;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.NPCRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.BuildingDisplayCiv;
import civ.CommandParser;
import civ.MainframeCiv;

/**
 * Leave the interior of a Building. The EXIT command is a synonym.
 * <P>
 * Format: {@code LEAVE} moves the Bulding's exterior.
 * 
 * @author Alan Cline
 * @version August 10, 2015 // original <br>
 */
public class TA09_CmdLeave
{
  static private CommandParser _cp = null;
  static private BuildingDisplayCiv _bldgCiv = null;
  static private MainframeProxy _mfProxy = null;
  static private MainframeCiv _mfCiv;
  static private RegistryFactory _regFactory = null;
  static private BuildingRegistry _bReg = null;

  /** List of valid Buildings that can be entered */
  private static Scheduler _skedder;


  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);

    // Start up the support classes
    _skedder = new Scheduler();
    _regFactory = new RegistryFactory(_skedder);
    _regFactory.initRegistries();
    _bReg = (BuildingRegistry) _regFactory.getRegistry(RegKey.BLDG);

    // Replace the GUI objects with their test facades
    _mfProxy = new MainframeProxy();
    // This will open the BuildingRegistry, which must be closed before exiting
    _bldgCiv = new BuildingDisplayCiv(_mfProxy, _bReg);

    _mfCiv = new MainframeCiv(_mfProxy, _bldgCiv, (AdventureRegistry) _regFactory.getRegistry(RegKey.ADV));
    _cp = new CommandParser(_skedder, new CommandFactory(_mfCiv, _bldgCiv));

    // // Get list of names for all buildings that can be entered
    // _bldgs = _bReg.getElementNames();
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
   * Error: Leave the Building when there is not current
   * Building
   */


  /**
   * Normal case: Leave the current Building
   * 
   * @throws InterruptedException
   */ 
  @Test (timeout=5000)
  public void test_LeaveBuilding() throws InterruptedException
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Setup: must be inside Building (onTown = false, currentBuilding !null; isInside = true)
    _cp.receiveCommand("Enter the Bank");
    _skedder.doOneUserCommand();

    assertFalse(_bldgCiv.isOnTown());
    assertTrue(_bldgCiv.isInside());
    assertNotNull(_bldgCiv.getCurrentBuilding());

    // TEST
    assertTrue(_cp.receiveCommand("Leave "));
    _skedder.doOneUserCommand();

    // Confirm Hero is no longer on town or in building
    assertFalse(_bldgCiv.isOnTown());
    assertTrue(_bldgCiv.isInside());
  }


  /**
   * Error: Attempt to LEAVE the currentBuilding when outside 
   * 
   * @throws InterruptedException
   */
  @Test
  public void test_LeaveBuildingWhenOutside() throws InterruptedException
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Setup: outside Building (onTown = false, currentBuilding !null; isInside = false)
    _cp.receiveCommand("Approach the Bank");
    _skedder.doOneUserCommand();

    assertFalse(_bldgCiv.isOnTown());
    assertNotNull(_bldgCiv.getCurrentBuilding());
    assertFalse(_bldgCiv.isInside());

    // TEST
    assertFalse(_cp.receiveCommand("Leave "));
    
    
    // Expected error message
    assertFalse(_bldgCiv.isOnTown());
    assertFalse(_bldgCiv.isInside());

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
