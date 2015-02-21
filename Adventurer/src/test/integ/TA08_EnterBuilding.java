/**
 * TA08_EnterBuilding.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.BuildingDisplayCiv;
import civ.BuildingDisplayCiv.MockBldgCiv;
import civ.CommandParser;
import civ.CommandParser.MockCP;

/**
 * Enter a building from its exterior or from the town, by giving either the Building name or type.
 * If no name or type is given, then the Hero will Enter the "current building". If Hero is at the
 * town view.i.e., no current building, then an error is returned.
 * <P>
 * The methods {@code Enter.init()} and {@code Enter.exec()} must also be tested from the
 * {@code Scheduler}, which runs on its own thread.
 * <P>
 * Implementation Note: This JUnit module intervenes between the {@code civ.CommandParser} and the
 * {@code hic.IOPanel} with {@code test.integ.IOPanelProxy}, which orchestrates between command
 * string sent, and command outputs received.
 * 
 * @author Alan Cline
 * @version Dec 20, 2014 // original <br>
 */
public class TA08_EnterBuilding
{
  /** Facade to send and receive messages meant for the output panel */
  static private IOPanelProxy _ioProxy = null;
  /** CommandParser takes in all commands from the CmdLine of the IOPanel */
  static private CommandParser _cp = null;
  /** MockCommandParser allows access to CommandParser fields */
  static private MockCP _mock = null;
  /** BuildingDisplayCiv controls access and displays of buildings */
  static private BuildingDisplayCiv _bldgCiv = null;
  /** MockBuildingDisplayCiv */
  static private MockBldgCiv _mockBldgCiv = null;
  /** Mainframe Proxy facades the image panel and iopanel; used by BuildingDisplayCiv */
  static private MainframeProxy _mfProxy = null;

  
  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    // Replace the GUI objects with their test facades
    _ioProxy = new IOPanelProxy();
    assertNotNull(_ioProxy);
    _mfProxy = new MainframeProxy();
    assertNotNull(_mfProxy);
    _cp = CommandParser.getInstance(new IOPanelProxy());
    assertNotNull(_cp);
    _mock = _cp.new MockCP();
    assertNotNull(_mock);
    // This will open the BuildingRegistry, which must be closed before exiting
    _bldgCiv = BuildingDisplayCiv.getInstance(_mfProxy);
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
    _mock = null;
    _cp = null;
    _mfProxy = null;
    _ioProxy = null;
    // Close BuildingRegistry, left open from BuildingDisplayCiv
    RegistryFactory regFactory = RegistryFactory.getInstance();
    BuildingRegistry bReg = (BuildingRegistry) regFactory.getRegistry(RegKey.BLDG);
    bReg.closeRegistry();
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
   * Error case: null command <br>
   */
  @Test
  public void test_EnterNullCmd()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Error: null command
    _cp.receiveCommand(null);
    String echo = _mock.getInput();
    MsgCtrl.msgln("Command entered: " + echo);
    assertNull(echo);
    assertNull(_ioProxy.msgOut());
  }

  
  /**
   * Error case: null parm and no current building <br>
   */
  @Test
  public void test_EnterNullParm()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Error: set current building to null
    _mockBldgCiv.setCurrentBldg(null);
    Building _currentBldg = _bldgCiv.getCurrentBuilding();
    assertNull(_currentBldg);
    // Try to enter it, get error message
    _cp.receiveCommand("ENTER");
    String echo = _mock.getInput();
    MsgCtrl.msgln("\tCommand entered: " + echo);
//    assertNull(echo);
//    assertNull(_ioProxy.msgOut());
  }

  


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
