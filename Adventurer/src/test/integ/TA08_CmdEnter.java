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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
 * Format: {@code ENTER <BuildingName>} enter the specified Building if not already in one <br>
 * Format: {@code ENTER < >} enters the Building Hero is currently outside of <br>
 * <P>
 * Implementation Note: The {@code MainFrameProxy}, which implements {@code MainframeInterface},
 * intervenes between the {@code civ.CommandParser} and the {@code hic.Mainframe}. All inputs are
 * sent directly to the {@code civ.CmdParser} and outputs are received by the
 * {@code test.integ.MainframeProxy}, which replaces the {@code hic.IOPanel} to writes output
 * messages.
 * 
 * @author Alan Cline
 * @version Dec 20, 2014 // original <br>
 *          Mar 5, 2015 // updated for more testing <br>
 *          Jun 22, 2015 // replaced {@code hic.integ.IOProxy} with {@code hic.integ.MainframeProxy} <br>
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
    RegistryFactory regFactory = RegistryFactory.getInstance();
    BuildingRegistry bReg = (BuildingRegistry) regFactory.getRegistry(RegKey.BLDG);
    bReg.closeRegistry();
    // Close NPCRegistry, left open from BuildingDisplayCiv
    NPCRegistry npcReg = (NPCRegistry) regFactory.getRegistry(RegKey.NPC);
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
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ==========================================================
  // Begin the tests!
  // ==========================================================


  /**
   * Normal case: Enter a valid building from the town
   * 
   * @throws InterruptedException
   */
  @Test
  public void test_EnterBuildingFromTownOrExterior() throws InterruptedException
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    final String[][] bldg = {
        {"Arcaneum", "int_Arcaneum.jpg"},
        {"Bank", "int_Bank.jpg"},
        {"Monastery", "int_Monastery.jpg"},
        {"Stadium", "int_Stadium.jpg"},
        {"Jail", "int_Jail.jpg"},
        {"General Store", "int_GeneralStore.jpg"},
        {"Rouge's Tavern", "int_RoguesDen.jpg"},
        {"Ugly Ogre Inn", "int_Inn.jpg"}
    };
   
    // Try entering all buildings
    for (int k = 0; k < bldg.length; k++) {
      // Setup: onTown must be true, and inBuilding flag must be false
      _mockBldgCiv.setOnTown(true);
      _mockBldgCiv.setInsideBldg(false);
      
      // TEST
      _cp.receiveCommand("Enter " + bldg[k][0]);
      String echo = _mockCP.getInput();
      MsgCtrl.msgln("\nCommand: " + echo);

      // After Cmd is executed...
       Thread.sleep(600);
      // Confirm Hero is no longer on town, but is inside a building
      assertFalse(_bldgCiv.isOnTown());
      assertTrue(_bldgCiv.isInside());


      String bName = _bldgCiv.getCurrentBuilding().getName();
      MsgCtrl.msg("\tBuilding name = " + bName);
      assertTrue("Expected " + bldg[k][0] + ", got " + bName, bName.equals(bldg[k][0]));
    }
  }



  // /**
  // * Error case: empty string command should return CMD_NULL error message <br>
  // * @throws InterruptedException
  // */
  // @Test
  // public void test_EnterEmptyCmd() throws InterruptedException
  // {
  // MsgCtrl.auditMsgsOn(false);
  // MsgCtrl.errorMsgsOn(false);
  // MsgCtrl.where(this);
  //
  // final String EMPTY = " "; // one space
  //
  // // Error: empty command string
  // _cp.receiveCommand(EMPTY);
  // String echo = _mockCP.getInput();
  // MsgCtrl.msgln("\tCommand entered: " + echo);
  // assertNull(_mockCP.getInput());
  // String nullMsg = _mockCP.getERRMSG_CMDNULL();
  // String msgOut = _mfProxy.errMsgOut();
  // MsgCtrl.msgln("\tError message expected: " + nullMsg);
  // MsgCtrl.msgln("\tError message received: " + msgOut);
  // assertTrue(msgOut.equals(nullMsg));
  // }
  //
  // /**
  // * Error case: null parm and no current building <br>
  // */
  // @Test
  // public void test_EnterNullParm()
  // {
  // MsgCtrl.auditMsgsOn(true);
  // MsgCtrl.errorMsgsOn(true);
  // MsgCtrl.where(this);
  //
  // // Error: set current building to null
  // _mockBldgCiv.setCurrentBldg(null);
  // Building _currentBldg = _bldgCiv.getCurrentBuilding();
  // assertNull(_currentBldg);
  // // Try to enter it, get error message
  // _cp.receiveCommand("ENTER");
  // String echo = _mock.getInput();
  // MsgCtrl.msgln("\tCommand entered: " + echo);
  // assertTrue(_ioProxy.msgOut().equals(ERRMSG_NOBLDG));
  // }



  // /**
  // * Error case: invalid command entered <br>
  // */
  // @Test
  // public void test_EnterInvalidCommand()
  // {
  // MsgCtrl.auditMsgsOn(true);
  // MsgCtrl.errorMsgsOn(true);
  // MsgCtrl.where(this);
  //
  // // Error: Invalid command
  // String cmd = "SOMETHING invalid";
  // _cp.receiveCommand(cmd);
  // String echo = _mock.getInput();
  // MsgCtrl.msgln("Command entered: " + echo);
  // assertNotNull(echo);
  // // Verify error returned to CmdLine display
  // assertEquals(cmd, echo);
  // assertEquals(_mock.getErrorMsg(), _ioProxy.msgOut());
  // }



  // /**
  // * Normal: enter building by name from town view
  // */
  // @Test
  // public void test_EnterNamedBuilding()
  // {
  // MsgCtrl.auditMsgsOn(true);
  // MsgCtrl.errorMsgsOn(true);
  // MsgCtrl.where(this);
  //
  // // MainframeCiv.String[][] DEFAULT_BUILDINGS;
  // String[] bldgName = {"Ugly Ogre Inn", "The Bank"};
  //
  // // State 1: Hero is on town view
  // // Set the MainframeCiv field to proper state
  // MockMFC mockmfc = new MockMFC();
  // mockmfc.setTownView(true);
  //
  // String cmd = "ENTER " + bldgName[0];
  // _cp.receiveCommand(cmd);
  // MsgCtrl.msgln("\tCommand entered: " + _mockCP.getInput());
  // assertEquals("ENTER " + bldgName[0], _mockCP.getInput());
  // // TownView is now off, current building set
  // assertTrue(mockmfc.isOnTown() == false);
  //
  //
  // // State 2: Hero is outside a current Building
  //
  // // State 3: Hero is inside the current Building
  // }



} // end of TA08_EnterBuilding integration test case
