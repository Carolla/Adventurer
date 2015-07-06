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

import java.util.List;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.NPCRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.BuildingDisplayCiv;
import civ.BuildingDisplayCiv.MockBldgCiv;
import civ.CommandParser;
import civ.CommandParser.MockCP;
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
  /** CommandParser takes in all commands from the CmdLine of the IOPanel */
  static private CommandParser _cp = null;
  /** MockCommandParser allows access to CommandParser fields */
  static private MockCP _mockCP = null;
  /** BuildingDisplayCiv controls access and displays of buildings */
  static private BuildingDisplayCiv _bldgCiv = null;
  /** MockBuildingDisplayCiv */
  static private MockBldgCiv _mockBldgCiv = null;
  /** MainframeProxy controls inputs and outputs; used by BuildingDisplayCiv */
  static private MainframeProxy _mfProxy = null;
  static private MainframeCiv _mfCiv;

  /** List of valid Buildings that can be entered */
  static private List<String> _bldgs = null;

  /** Support class for obtaining all registries */
  static private RegistryFactory _regFactory = null;
  /** Support class for obtaining particular building */
  static private BuildingRegistry _bReg = null;


  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    // Replace the GUI objects with their test facades
    _mfProxy = new MainframeProxy();
    assertNotNull(_mfProxy);
    // Create the parser to receive commands
    _mfCiv = new MainframeCiv(_mfProxy);
    _cp = CommandParser.getInstance(_mfCiv);
    assertNotNull(_cp);
    _mockCP = _cp.new MockCP();
    assertNotNull(_mockCP);
    // This will open the BuildingRegistry, which must be closed before exiting
    _bldgCiv = BuildingDisplayCiv.getInstance();
    _bldgCiv.setOutput(_mfProxy);
    assertNotNull(_bldgCiv);
    _mockBldgCiv = _bldgCiv.new MockBldgCiv();
    assertNotNull(_mockBldgCiv);

    // Start up the support classes
    _regFactory = RegistryFactory.getInstance();
    _bReg = (BuildingRegistry) _regFactory.getRegistry(RegKey.BLDG);

    // Get list of names for all buildings that can be entered
    _bldgs = _bReg.getElementNames();
  }


  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    _mockBldgCiv = null;
    _bldgCiv = null;
    _mockCP = null;
    _cp = null;
    _mfProxy = null;
    // Close BuildingRegistry, left open from BuildingDisplayCiv
    _bReg.closeRegistry();
    // Close NPCRegistry, left open from BuildingDisplayCiv
    NPCRegistry npcReg = (NPCRegistry) _regFactory.getRegistry(RegKey.NPC);
    npcReg.closeRegistry();
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {}

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
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Try entering all buildings
    for (int k = 0; k < _bldgs.size(); k++) {
      // Setup: onTown must be true, and inBuilding flag must be false
      resetBuildingState();

      // TEST
      _cp.receiveCommand("Enter " + _bldgs.get(k));
      String echo = _mockCP.getInput();
      MsgCtrl.msgln("\nCommand: " + echo);

      // After Cmd is executed...
      Thread.sleep(600);
      // Confirm Hero is no longer on town, but is inside a building
      assertFalse(_bldgCiv.isOnTown());
      assertTrue(_bldgCiv.isInside());

      String bName = _bldgCiv.getCurrentBuilding().getName();
      MsgCtrl.msg("\tBuilding name = " + bName);
      assertTrue("Expected " + _bldgs.get(k) + ", got " + bName, bName.equals(_bldgs.get(k)));

      // Teardown:Return the Hero to the town state after each building
      resetBuildingState();
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
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Loop for each Building in the BuildingRegistry
    for (int k = 0; k < _bldgs.size(); k++) {
      // Setup: Hero must be outside a defined currentBuilding, and not OnTown
      _mockBldgCiv.setOnTown(false);
      _mockBldgCiv.setInsideBldg(false);
      String bName = _bldgs.get(k);
      Building b = _bReg.getBuilding(bName);
      _bldgCiv.setCurrentBuilding(b);

      // TEST
      _cp.receiveCommand("Enter");
      String echo = _mockCP.getInput();
      MsgCtrl.msgln("\n\tCommand: " + echo);

      // After Cmd is executed...
      Thread.sleep(600);

      // VERIFY
      // Confirm Hero is no longer on town, but is inside a building
      assertFalse(_bldgCiv.isOnTown());
      assertTrue(_bldgCiv.isInside());
      // Hero is inside the correct building, now the current building
      String newCurrent = _bldgCiv.getCurrentBuilding().getName();
      MsgCtrl.msgln("\tCurrent Building = " + newCurrent);
      assertTrue("Expected " + _bldgs.get(k) + ", got " + bName, bName.equals(newCurrent));
    }
  }

  
  /**
   * Error case: Attempt to enter a building from inside another Building
   * 
   * @throws InterruptedException
   */
  @Test
  public void test_EnterFromInsideBuilding() throws InterruptedException
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    String bName1 = "Ugly Ogre Inn";
    String bName2 = "Arcaneum";

    // Setup: Enter a Building, and then try to enter same building
    MsgCtrl.msgln("\tEntering " + bName1);
    _cp.receiveCommand("Enter " + bName1);
    // Wait for setup command to execute
    Thread.sleep(600);
    String newCurrent = _bldgCiv.getCurrentBuilding().getName();
    MsgCtrl.msgln("\tCurrent Building = " + newCurrent);
    assertFalse(_bldgCiv.isOnTown());
    assertTrue(_bldgCiv.isInside());
    newCurrent = _bldgCiv.getCurrentBuilding().getName();
    MsgCtrl.msgln("\tBuilding state ok after entering " + newCurrent);

    // Test1: Try to enter the same building
    MsgCtrl.msgln("\nTest1: Attempting to re-enter " + bName1);
    _cp.receiveCommand("Enter " + bName1);
    // After Cmd is executed...
    Thread.sleep(600);

    // VERIFY that Hero is still inside building
    assertFalse(_bldgCiv.isOnTown());
    assertTrue(_bldgCiv.isInside());
    newCurrent = _bldgCiv.getCurrentBuilding().getName();
    MsgCtrl.msgln("\tExpected error: Same building state for " + newCurrent);

    // Test2: Try to enter a different building
    MsgCtrl.msgln("\nTest 2: Attempting to enter " + bName2 + " while still inside " + bName1);
    _cp.receiveCommand("Enter " + bName2);
    // After Cmd is executed...
    Thread.sleep(600);
    assertFalse(_bldgCiv.isOnTown());
    assertTrue(_bldgCiv.isInside());
    newCurrent = _bldgCiv.getCurrentBuilding().getName();
    MsgCtrl.msgln("\tExpected error: Same building state for " + newCurrent);

  }


  //============================================================================
  // PRIVATE HELPER METHODS 
  // ============================================================================

  /** Hero is onTwon, with not current Building, and not inside one */
  private void resetBuildingState()
  {
    _bldgCiv.setOnTown(true);
    _bldgCiv.setCurrentBuilding(null);
    _mockBldgCiv.setInsideBldg(false);
  }


} // end of TA08_EnterBuilding integration test case
