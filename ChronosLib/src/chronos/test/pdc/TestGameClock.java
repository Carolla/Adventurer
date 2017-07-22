/**
 * TestGameClock.java Copyright (c) 2017, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com.
 */

package chronos.test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.GameClock;
import mylib.MsgCtrl;

/**
 * @author Alan Cline
 * @version July 22, 2017 // initially updated per QATool <br>
 */
public class TestGameClock
{
  private GameClock _clock;

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @Before
  public void setUp() throws Exception
  {
    _clock = GameClock.getInstance();
  }

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _clock.clear();
  }


  // ===============================================================================
  // BEGIN TESTING
  // ===============================================================================

  /**
   * @Normal.Test getInstance() -- create singleton
   */
  @Test
  public void testGetInstance()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    GameClock clock2 = GameClock.getInstance();
    assertNotNull(clock2);
    // Verify that same object is return in both cases
    assertTrue(clock2 == _clock);
  }


  /**
   * @Not.Needed long getTime() -- getter
   */
  @Test
  public void testGetTime()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Normal.Test void increment(long) -- increment clock regardless of reference object used
   */
  @Test
  public void testIncrement()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    GameClock clock2 = GameClock.getInstance();

    long firstTime = _clock.getTime();
    MsgCtrl.msg("Time 1: " + firstTime);
    clock2.increment(42);
    long secondTime = clock2.getTime();
    MsgCtrl.msg("\tTime 2: " + secondTime);
    assertEquals(firstTime + 42, secondTime);
  }


  /**
   * @Normal.Test String formatSecondsToTOD(long secs, boolean miltime) -- some typical times
   */
  @Test
  public void testFormatSecondsToTOD()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP
    // Initial clock time is 6:00am
    String tod = GameClock.formatSecondsToTOD(_clock.getTime(), false);
    assertEquals("0600 hours", tod);
    
    // Increment the time by 1 hr 13 minutes
    _clock.increment(3613);
    tod = GameClock.formatSecondsToTOD(_clock.getTime(), false);
    assertEquals("0713 hours", tod);

    // Increment the time by 47 minutes to get to top of the hour
    _clock.increment(6433);
    tod = GameClock.formatSecondsToTOD(_clock.getTime(), false);
    assertEquals("0800 hours", tod);
  }



} // end of TestGameClock.java class
