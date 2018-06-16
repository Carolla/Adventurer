/**
 * TestScheduler.java Copyright (c) 2018, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */

package chronos.test.pdc.command;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chronos.pdc.GameClock;
import chronos.pdc.command.Scheduler;
import mylib.MsgCtrl;

/**
 * @author Al Cline
 * @version June 15, 2018 // original <br>
 */
public class TestScheduler
{
  static private Scheduler _sked;
  static private GameClock _clock;


  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @BeforeAll
  public static void setUpBeforeClass() throws Exception
  {
    _sked = new Scheduler();
    assertNotNull(_sked);
    _clock = GameClock.getInstance();
    assertNotNull(_clock);
  }

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @AfterAll
  public static void tearDownAfterClass() throws Exception
  {
    _sked = null;
    _clock = null;
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
   * @Normal.Testvoid doOneUserCommand()
   */
  @Test
  public void testDoOneUserCommand()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
    fail();
  }


  /**
   * @Normal.Test void sched(Command) -- increment clock after successful cmd.exec()
   */
  @Test
  public void testSched()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Create a command for the scheduler
    CommandProxy cmd = new CommandProxy("TestCmd", 0, 5, "Desc", null);
    _sked.sched(cmd);
    MsgCtrl.msgln("\t Test command scheduled");
  }


} // end of TestScheduler.java class
