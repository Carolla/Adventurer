/**

 * TestDeltaCmdList.java Copyright (c) 2018, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */

package chronos.test.pdc.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chronos.pdc.command.Command;
import chronos.pdc.command.DeltaCmdList;
import chronos.pdc.command.Event;
import chronos.pdc.command.NullCommand;
import mylib.MsgCtrl;

/**
 * @author Al Cline
 * @version June 10, 2018 // original <br>
 *          June 15, 2018 // Added more test helpers and beefed up some of the tests. The
 *          {@code PriorityBlockingQueue} is proving to be problematic. <br>
 */
public class TestDeltaCmdList
{
  static private DeltaCmdList _dq;
  private String[] _expCmds = {"NO_WAIT", "SMALL_WAIT", "MEDIUM_WAIT", "LONG_WAIT",
      "SAME_WAIT"};

  /** Create three commands to put into DQ for testing */
  static private CommandProxy _noWaitCmd;
  static private CommandProxy _smallWaitCmd;
  static private CommandProxy _mediumWaitCmd;
  static private CommandProxy _longWaitCmd;
  static private CommandProxy _sameWaitCmd;

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @BeforeAll
  public static void setUpBeforeClass() throws Exception
  {
    _dq = new DeltaCmdList();
    assertNotNull(_dq);
    assertTrue(_dq.isEmpty());

    _noWaitCmd = new CommandProxy("NO_WAIT", 0, 5,
        "Cmd starts immediately and runs for 5 seconds", null);
    assertNotNull(_noWaitCmd);
    _smallWaitCmd = new CommandProxy("SMALL_WAIT", 3, 17,
        "Cmd starts after 3 sec and runs for 17 seconds", null);
    assertNotNull(_smallWaitCmd);
    _mediumWaitCmd = new CommandProxy("MEDIUM_WAIT", 5, 13,
        "Cmd starts after 5 sec and runs for 13 seconds", null);
    assertNotNull(_mediumWaitCmd);
    _longWaitCmd = new CommandProxy("LONG_WAIT", 7, 3,
        "Cmd starts after 7 sec and runs for 3 seconds", null);
    assertNotNull(_longWaitCmd);
    _sameWaitCmd = new CommandProxy("SAME_WAIT", 7, 20,
        "Cmd starts after 7 sec and runs for 20 seconds", null);
    assertNotNull(_sameWaitCmd);
  }

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @AfterAll
  public static void tearDownAfterClass() throws Exception
  {
    _dq = null;
    _noWaitCmd = null;
    _smallWaitCmd = null;
    _mediumWaitCmd = null;
    _longWaitCmd = null;
    _sameWaitCmd = null;
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
    // Clear the DQ of elements
    _dq.clear();
  }


  // ===============================================================================
  // BEGIN TESTING
  // ===============================================================================

  /**
   * @Normal.Test Command getNextCmd() -- Return commands in proper order from DQ
   */
  @Test
  public void testGetNextCmd()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Place some commands in th DQ
    loadQueue();
    assertEquals(_expCmds.length, _dq.size());
    snapshot(_dq);
    // Assure they are pulled from the DQ in the proper order
    int k = 0;
    while (!_dq.isEmpty()) {
      Command cmd = _dq.getNextCmd();
      MsgCtrl.msgln("\t Cmd pulled = " + cmd.getName());
      assertEquals(_expCmds[k++], cmd.getName());
    }
  }


  /**
   * @Error.Test void getNextCmd() -- Pulling from empty queue
   */
  @Test
  public void testGetNextCmd_Error()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    assertTrue(_dq.isEmpty());
    Command cmd = _dq.getNextCmd();
    MsgCtrl.msgln("\t" + cmd.getName());
    assertEquals("NullCommand", cmd.getName());
  }


  /**
   * @Normal.Test void insert(Command)
   */
  @Test
  public void testInsert()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Place some commands in the DQ
    loadQueue();
    assertEquals(5, _dq.size());
    snapshot(_dq); // looks but does not delete the DQ elements
    // Reset
    _dq.clear();
    assertEquals(0, _dq.size());

    // Duplicate commands are allowed in the DQ
    _dq.insert(_smallWaitCmd);
    _dq.insert(_noWaitCmd);
    _dq.insert(_smallWaitCmd);
    snapshot(_dq); // looks but does not delete the DQ elements
    assertEquals(3, _dq.size());
    // Reset
    _dq.clear();
    assertEquals(0, _dq.size());

    // Add a NullCommand onto DQ
    _dq.insert(_smallWaitCmd);
    _dq.insert(new NullCommand());
    snapshot(_dq); // looks but does not delete the DQ elements
    assertEquals(2, _dq.size());
  }


  /**
   * @Error.Test void insert(Command) -- Contained in test for testGetNextCmd()
   */
  @Test
  public void testInsert_Error()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    Assertions.assertThrows(NullPointerException.class, () -> {
      _dq.insert(null);
    });
  }


  /**
   * @Not.Implemented boolean isEmpty() -- wrapper
   */
  @Test
  public void testIsEmpty()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.WRAPPER);
  }


  /**
   * @Not.Implemented boolean isEmpty() -- wrapper
   */
  @Test
  public void testClear()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.WRAPPER);
  }


//  /**
//   * @Normal.Test int timeToNextCmd()
//   */
//  @Test
//  public void testTimeToNextCmd()
//  {
//    MsgCtrl.auditMsgsOn(true);
//    MsgCtrl.errorMsgsOn(true);
//    MsgCtrl.where(this);
//
//    int[] expDeltas = {0, 3, 6, 0, -1};
//
//    // Place some commands in the DQ
//    loadQueue();
//    assertEquals(_expCmds.length, _dq.size());
//
//    // Assure they have the proper deltas
//    for (int k = 0; k < _dq.size(); k++) {
//      int delta = _dq.timeToNextCmd();
//      MsgCtrl.msgln("\t delta = " + delta);
//      // assertEquals(expDeltas[k++], delta);
//    }
//  }


  // ===============================================================================
  // PRIVATE HELPERS
  // ===============================================================================

  /** Load the DQ with the test command */
  private void loadQueue()
  {
    // Place some commands in the DQ in no particular order
    _dq.insert(_mediumWaitCmd);
    _dq.insert(_longWaitCmd);
    _dq.insert(_noWaitCmd);
    _dq.insert(_smallWaitCmd);
    _dq.insert(_sameWaitCmd);
  }


  /**
   * Takes a snapshot of the contents of the queue, but does not remove any elements
   * 
   * @param clist The DQ to examine
   */
  private void snapshot(DeltaCmdList clist)
  {
    if (clist.isEmpty()) {
      MsgCtrl.msgln("\tDeltaQueue is empty");
    } else {
      Object[] cary = clist.toArray();
      for (Object obj : cary) {
        Event evt = (Event) obj;
        Command cmd = evt.getCommand();
        MsgCtrl
            .msgln("\t" + cmd.getName() + " (" + cmd.getDelay() + ", " + cmd.getDuration() + ")");
      }
      MsgCtrl.msgln("\t--------------------------------\n");
    }
  }


} // end of TestDeltaCmdList.java class
