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

  private final long SECONDS_PER_MINUTE = 60L; 
  private final long SECONDS_PER_HOUR = 3600L; 
      
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
  // CONSTRUCTOR TESTING
  // ===============================================================================

  /**
   * @Normal.Test getGameTime() -- read day and daily time elapsed
   */
  @Test
  public void testGetGameTime()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP
    // Initial clock time is 6:00am
    String gtime = _clock.getGameTime();
    MsgCtrl.msgln("Game opens at time " + gtime);
    assertEquals("Day 1 6:00 AM", gtime);
    
    // Let 12 hours elapse
    _clock.increment(12 * SECONDS_PER_HOUR);
    gtime = _clock.getGameTime();
    MsgCtrl.msgln("12 hours later game time is " + gtime);
    assertEquals("Day 1 6:00 PM", gtime);
    
    // Let 18 more hours elapse
    _clock.increment(18 * SECONDS_PER_HOUR);
    gtime = _clock.getGameTime();
    MsgCtrl.msgln("18 hours later game time is " + gtime);
    assertEquals("Day 2 12:00 PM", gtime);
    
    // Let 12 more hours elapse
    _clock.increment(12 * SECONDS_PER_HOUR);
    gtime = _clock.getGameTime();
    MsgCtrl.msgln("18 hours later game time is " + gtime);
    assertEquals("Day 3 12:00 AM", gtime);
  }

  
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


  // ===============================================================================
  // BEGIN TESTING
  // ===============================================================================
  
  /**
   * @Normal.Test String fmtSecondsToTOD(long secs, boolean miltime) -- test military time
   */
  @Test
  public void testFmtSecondsToTOD_Military()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
  
    // SETUP
    // Initial clock time is 6:00am
    String tod = _clock.fmtSecondsToTOD(_clock.getTimelog(), true);
    assertEquals("0600 hours", tod);
    
    // Increment the time by a flat 2 hours
    _clock.increment(2 * SECONDS_PER_HOUR);
    tod = _clock.fmtSecondsToTOD(_clock.getTimelog(), true);
    assertEquals("0800 hours", tod);
  
    // Increment the time by 1 hr 13 minutes
    _clock.increment(73 * SECONDS_PER_MINUTE);
    tod = _clock.fmtSecondsToTOD(_clock.getTimelog(), true);
    assertEquals("0913 hours", tod);
  
    // Increment the time by 47 minutes to get to top of the hour
    _clock.increment(47 * SECONDS_PER_MINUTE);
    tod = _clock.fmtSecondsToTOD(_clock.getTimelog(), true);
    assertEquals("1000 hours", tod);
    
    // Increment the time by 13 hours 59 min to get to one minute before midnight
    _clock.increment(13 * SECONDS_PER_HOUR + 59 * SECONDS_PER_MINUTE);
    tod = _clock.fmtSecondsToTOD(_clock.getTimelog(), true);
    assertEquals("2359 hours", tod);
  
    // Increment the time by 2 minutes to get 1 minute into next day
    _clock.increment(2 * SECONDS_PER_MINUTE);
    tod = _clock.fmtSecondsToTOD(_clock.getTimelog(), true);
    assertEquals("0001 hours", tod);
  }


  /**
   * @Normal.Test String fmtSecondsToTOD(long secs, boolean miltime) -- test meridian time
   */
  @Test
  public void testFmtSecondsToTOD()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
  
    // SETUP
    // Initial clock time is 6:00am
    String tod = _clock.fmtSecondsToTOD(_clock.getTimelog(), false);
    assertEquals("6:00 AM", tod);
    
    // Increment the time by a flat 2 hours
    _clock.increment(2 * SECONDS_PER_HOUR);
    tod = _clock.fmtSecondsToTOD(_clock.getTimelog(), false);
    assertEquals("8:00 AM", tod);
  
    // Increment the time by 1 hr 13 minutes
    _clock.increment(73 * SECONDS_PER_MINUTE);
    tod = _clock.fmtSecondsToTOD(_clock.getTimelog(), false);
    assertEquals("9:13 AM", tod);
  
    // Increment the time by 47 minutes to get to top of the hour
    _clock.increment(47 * SECONDS_PER_MINUTE);
    tod = _clock.fmtSecondsToTOD(_clock.getTimelog(), false);
    assertEquals("10:00 AM", tod);
    
    // Increment the time by 13 hours 59 min to get to one minute before midnight
    _clock.increment(13 * SECONDS_PER_HOUR + 59 * SECONDS_PER_MINUTE);
    tod = _clock.fmtSecondsToTOD(_clock.getTimelog(), false);
    assertEquals("11:59 PM", tod);
  
    // Increment the time by 2 minutes to get 1 minute into next day
    _clock.increment(2 * SECONDS_PER_MINUTE);
    tod = _clock.fmtSecondsToTOD(_clock.getTimelog(), false);
    assertEquals("12:01 AM", tod);
  }

  /**
   * @Not.Needed long getTimelog() -- getter
   */
  public void testGetTimelog()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Normal.Test void increment(long) -- increment clock regardless of reference object used
   * @Error.Test void increment(long) -- negative incrment has not effect
   */
  @Test
  public void testIncrement()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    GameClock clock2 = GameClock.getInstance();
    long firstTime = _clock.getTimelog();
    MsgCtrl.msg("Time 1: " + firstTime);
    clock2.increment(42);
    long secondTime = clock2.getTimelog();
    MsgCtrl.msg("\tTime 2: " + secondTime);
    assertEquals(firstTime + 42, secondTime);
    
    clock2.increment(-17);
    long thirdTime = clock2.getTimelog();
    MsgCtrl.msg("\tTime 3: " + thirdTime);
    assertEquals(secondTime, thirdTime);
  }


	/**
 	 * @Not.Implemented void clear() -- getter
	 */
	public void testClear()
	{
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.where(this);
		MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
	}



} // end of TestGameClock.java class
