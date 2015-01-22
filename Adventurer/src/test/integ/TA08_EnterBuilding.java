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
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import civ.CommandParser;
import civ.CommandParser.MockCP;

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

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    // Create the CommandParser object to receive the command
    _cp = CommandParser.getInstance();
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
  {}


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
  public void test_EnterCurrentBuilding()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    _cp.receiveCommand("ENTER");
    MsgCtrl.msgln("Command entered: " + _mock.getInput());
    assertNotNull(_mock.getInput());

  }

}
