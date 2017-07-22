/**
 * GameClock.java
 *
 * Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc;


/**
 * Keeps track of the time as measured by the duration of each command executed. Continual updates
 * are made to the windowing system.
 * 
 * @author Alan Cline
 * @version June 5 2008 // original <br>
 *          July 2 2008 // Final commenting for Javadoc compliance <br>
 *          July 22, 2017 // updated per QATool <br>
 */
public class GameClock
{
  static private final double MINUTES_PER_HOUR = 60L;
  static private final double SECONDS_PER_MINUTE = 60L;

  /** Set the default game clock time to 6am. */
  final long START_HOUR = 6 * 3600;
  /** Internal: init the game clock */
  long _timeLog = 0L;

  /** This object's singleton reference. */
  private static GameClock _clock = null;


  // ================================================================================
  // CONSTRUCTOR AND RELATED METHODS
  // ================================================================================

  /** Default constructor */
  private GameClock()
  {
    _timeLog = START_HOUR;
  }


  /**
   * Obtain the reference to an existing clock, else create one.
   */
  public static synchronized GameClock getInstance()
  {
    if (_clock == null) {
      _clock = new GameClock();
    }
    return _clock;
  }


  // ================================================================================
  // PUBLIC METHODS
  // ================================================================================

  /**
   * Clear the clock and remove it for all instances, as in to start over
   */
  public void clear()
  {
    _clock = null;
  }



  /**
   * Format seconds into time of day, with midnight (0:00am) being intial point
   * 
   * @param secs time interval, in seconds, past midnight
   * @param miltime true if military is to be returned; else meridian time
   * @return the time of day formatted as nn:nn a/pm or nnnn (military time)
   */
  static public String formatSecondsToTOD(long secs, boolean miltime)
  {
    StringBuilder tod = new StringBuilder();
    int hours = (int) (secs / (SECONDS_PER_MINUTE * MINUTES_PER_HOUR));
    int minutes = (int) (secs - hours * (SECONDS_PER_MINUTE * MINUTES_PER_HOUR));
    if (hours < 10) {
      tod.append("0");
    }
    tod.append(hours);
    if (minutes < 10) {
      tod.append("0");
    }
    tod.append(minutes);
    tod.append(" hours");
    return tod.toString();
  }


  /**
   * Gets the time, returned to the nearest millisecond. The caller must format as desired.
   * 
   * @return the time in the GameClock.
   */
  public long getTime()
  {
    return _timeLog;
  }


  /**
   * Accumulates time to the clock; negative increments do nothing.
   * 
   * @param seconds the amount of time to add.
   */
  public void increment(long seconds)
  {
    if (seconds > 0L) {
      _timeLog += seconds;
    }
  }


} // end of GameClock class
