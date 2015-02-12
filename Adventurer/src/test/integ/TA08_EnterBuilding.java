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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
  /** Test facade to send and receive messages meant for the GUI */
  static private IOPanelProxy _ioProxy = null;
  // /** Destination that JUnit test module plugs into */
  // static private MainframeCiv _mfCiv= null;
  // /** Internal class to access to the MainframeCiv fields */
  // static private MockMainframeCiv _mockmfc= null;
  /** CommandParser takes in all commands from the CmdLine GUI */
  static private CommandParser _cp = null;
  /** MockCommandParser allows access to CommandParser fields */
  static private MockCP _mockCP = null;


  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    // Replace the GUI object with the proxy
    _ioProxy = new IOPanelProxy();
    assertNotNull(_ioProxy);
    _cp = CommandParser.getInstance(new IOPanelProxy());
    assertNotNull(_cp);
    _mockCP = _cp.new MockCP();
    assertNotNull(_mockCP);

    // // Create the MainframeCiv that calls the CommandParser
    // _mfCiv = new MainframeCiv(_mfProxy);
    // assertNotNull(_mfCiv);
    // // Create the mock civ to check internal state fields
    // _mockmfc = _mfCiv.new MockMainframeCiv();
    // assertNotNull(_mockmfc);

  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    _cp = null;
    _ioProxy = null;
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
   * Normal: enter building by name from town view
   */
  @Test
  public void test_EnterNamedBuilding()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // MainframeCiv.String[][] DEFAULT_BUILDINGS;
    String[] bldgName = {"Ugly Ogre Inn", "The Bank"};

    // State 1: Hero is on town view
    // Set the MainframeCiv field to proper state
    MockMFC mockmfc = new MockMFC();
    mockmfc.setTownView(true);

    String cmd = "ENTER " + bldgName[0];
    _cp.receiveCommand(cmd);
    MsgCtrl.msgln("\tCommand entered: " + _mockCP.getInput());
    assertEquals("ENTER " + bldgName[0], _mockCP.getInput());
    // TownView is now off, current building set
    assertTrue(mockmfc.isOnTown() == false);
    

    // State 2: Hero is outside a current Building

    // State 3: Hero is inside the current Building
  }



  // /**
  // * Error case: invalid command entered <br>
  // * Error case: null command entered <br>
  // */
  // @Test
  // public void test_EnterInvalidInput()
  // {
  // MsgCtrl.auditMsgsOn(false);
  // MsgCtrl.errorMsgsOn(false);
  // MsgCtrl.where(this);
  //
  // // Error: null command
  // _cp.receiveCommand(null);
  // String echo = _mock.getInput();
  // MsgCtrl.msgln("Command entered: " + echo);
  // assertNull(echo);
  //
  // // Error: Invalid command
  // String cmd = "Some User Command";
  // _cp.receiveCommand(cmd);
  // echo = _mock.getInput();
  // MsgCtrl.msgln("Command entered: " + echo);
  // assertNotNull(echo);
  // // Verify error returned to CmdLine display
  // assertEquals(cmd, echo);
  // }



}
/** end of TA08_EnterBuilding integration test case */
