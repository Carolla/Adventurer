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
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import civ.CommandParser;
import civ.CommandParser.MockCP;
import civ.MainframeCiv;

/**
 * Enter a building from its exterior or from the town, either by name or type. If no name or type
 * is given, then the Hero must be standing outside the "current building". This test enters the CIV
 * from {@code hic.IOPanel.createCmdLinePanel().actionPerformed()} and returns through
 * {@code civ.CommandParser.receiveCommand()}. The methods {@code Enter.init()} and
 * {@code Enter.exec()} must also be tested from the {@code Scheduler}, which runs on its own
 * thread.
 * 
 * @author Alan Cline
 * @version Dec 20, 2014 // original <br>
 */
public class TA08_EnterBuilding
{
  static private CommandParser _cp = null;
  static private MockCP _mock = null;
  static private MainframeCiv _mfc= null;

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    _mfc = new MockMFC();
    assertNotNull(_mfc);
    _cp = CommandParser.getInstance(_mfc);
    assertNotNull(_cp);
    _mock = _cp.new MockCP();
    assertNotNull(_mock);
  }


  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    _cp = null;
  }


  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
  }


  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {}


  // ==========================================================
  // Begin the tests!
  // ==========================================================

  /**
   * Error case: when no building is current
   */
  @Test
  public void test_EnterReceiveCommand()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    String cmd = "Anything at all";
    _cp.receiveCommand(cmd);
    String echo = _mock.getInput();
    MsgCtrl.msgln("Command entered: " + echo);
    assertNotNull(echo);
    // Verify error returned to CmdLine display
    assertEquals(cmd, echo);
   
  }

  
//  /**
//   * Normal: enter building by name
//   */
//  @Test
//  public void test_EnterNamedBuilding()
//  {
//    MsgCtrl.auditMsgsOn(true);
//    MsgCtrl.errorMsgsOn(true);
//    MsgCtrl.where(this);
//
//    MainframeCiv.String[][] DEFAULT_BUILDINGS;
//    String[]  bldgName = {"Ugly Ogre Inn", "The Bank"};
//    
//    String cmd = "ENTER" + bldgName[0];
//    _cp.receiveCommand(cmd);
//    MsgCtrl.msgln("Command entered: " + _mock.getInput());
//    assertNotNull(_mock.getInput());
//  }

}
