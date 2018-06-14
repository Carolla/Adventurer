/**
 * TestDeltaCmdList.java Copyright (c) 2018, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */

package chronos.test.pdc.command;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chronos.pdc.command.DeltaCmdList;
import mylib.MsgCtrl;

/**
 * @author Al Cline
 * @version June 10, 2018 // original <br>
 */
public class TestDeltaCmdList
{
  static private DeltaCmdList _dq;

  /** Test Command with flexible parms */
  static private final String CMD_NAME = "CMD_PROXY";
  static private final int DELAY = 3;
  static private final int DURATION = 10;
  static private final String CMD_DESCRIPTION =
      "A simple no-parm command used for testing the Command class";
  static private final String CMDFMT = CMD_NAME + " [Keyword1] [Keyword2]";


  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @BeforeAll
  public static void setUpBeforeClass() throws Exception
  {
    _dq = new DeltaCmdList();
    assertNotNull(_dq);
    assertTrue(_dq.isEmpty());
    _dq.dump();
  }

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @AfterAll
  public static void tearDownAfterClass() throws Exception
  {
    _dq = null;
  }

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @BeforeEach
  public void setUp() throws Exception
  {}

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @AfterEach
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ===============================================================================
  // BEGIN TESTING
  // ===============================================================================

  /**
   * @Not.Implemented void dump() -- debugging tool
   */
  public void testDump()
  {}


  /**
   * @Normal.Test Command getNextCmd()
   */
  @Test
  public void testGetNextCmd()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Place a couple commands on the queue
    FakeCommand shortCmd = new FakeCommand("Short Command", 3, 10, CMD_DESCRIPTION, null);
    assertNotNull(shortCmd);
    // Place in the DQ
    _dq.insert(shortCmd);
    _dq.dump();

    FakeCommand parmCmd =
        new FakeCommand("Long Command with Parms", 10, 30, "Cmd with Parms",
            CMD_NAME + " Keyword1, Keyword2");
    assertNotNull(shortCmd);
    // Place in the DQ
    _dq.insert(shortCmd);
    _dq.dump();
  }


  /**
   * @Not.Implemented void insert(Command)
   */
  @Test
  public void testInsert()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOT_IMPLEMENTED);
  }


  /**
   * @Not.Implemented boolean isEmpty()
   */
  @Test
  public void testIsEmpty()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOT_IMPLEMENTED);
  }


  /**
   * @Not.Implemented int timeToNextCmd()
   */
  @Test
  public void testTimeToNextCmd()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOT_IMPLEMENTED);
  }


  // ===============================================================================
  // PRIVATE HELPERS
  // ===============================================================================



} // end of TestDeltaCmdList.java class
