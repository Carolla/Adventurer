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
  static private final long MINUTES_PER_HOUR = 60L;
  static private final long SECONDS_PER_MINUTE = 60L;
  static private final long SECONDS_PER_DAY = 86400L;

  /** Set the default game clock time to 6am. */
  final long START_HOUR = 6 * 3600;
  /** Internal: init the game clock */
  long _timeLog = 0L;
  /** Internal: init day counter */
  long _dayCount = 1L;

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
   * @return the time of day formatted as "nn:nn a/pm" or "nnnn hours" (military time)
   */
  public String fmtSecondsToTOD(long secs, boolean miltime)
  {
    double hoursfrac = (double) secs / (double) (SECONDS_PER_MINUTE * MINUTES_PER_HOUR);
    int hours = (int) hoursfrac;
    double minfrac = (hoursfrac - hours) * SECONDS_PER_MINUTE;
    int minutes = Math.round((float) minfrac);

    // Accommodate going into next day
    hours = hours % 24;    // this line should never be needed

    String tod = null;
    if (miltime) {
      tod = fmtMilitaryTime(hours, minutes);
    } else {
      tod = fmtMeridianTime(hours, minutes);
    }
    return tod;
  }
  

  /**
   * Gets the game time in terms of days and hours
   * 
   * @return the time in the GameClock.
   */
  public String getGameTime()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("Day ");
    sb.append(_dayCount);
    sb.append(" ");
    String tsecs = fmtSecondsToTOD(getTimelog(), false);
    sb.append(tsecs);
    return sb.toString();
  }

  
  /**
   * Gets the time, returned to the nearest millisecond. The caller must format as desired.
   * 
   * @return the time in the GameClock.
   */
  public long getTimelog()
  {
    return _timeLog;
  }


  /**
   * Accumulates time to the clock, and adjsuts day counter when more than one day has elapsed
   * negative increments do nothing.
   * 
   * @param seconds the amount of time to add.
   */
  public void increment(long seconds)
  {
    if (seconds > 0L) {
      _timeLog += seconds;
    }
    // Adjust day counter
    if (_timeLog >= SECONDS_PER_DAY) {
      _dayCount++;
      _timeLog = _timeLog - SECONDS_PER_DAY; 
    }
  }


  /**
   * Convert the hours and seconds into meridian (AM/PM) Time Of Day
   * 
   * @param hours number of hours on the game clock
   * @param minutes number of seconds on the game clock
   * @return a TOD formatted im military time: "nnnn hours"
   */
  private String fmtMeridianTime(int hours, int minutes)
  {
    StringBuilder tod = new StringBuilder();
    String mer = (hours >= 12) ? " PM" : " AM";
    // All 0 hour are 12 am
    hours = hours % 12;
    hours = (hours == 0) ? 12 : hours;
      
    tod.append(hours);
    tod.append(":");
    if (minutes < 10) {
      tod.append("0");
    }
    tod.append(minutes);
    tod.append(mer);
  
    return tod.toString();
  }


  /**
   * Convert the hours and seconds into military Time Of Day
   * 
   * @param hours number of hours on the game clock
   * @param minutes number of seconds on the game clock
   * @return a TOD formatted im military time: "nnnn hours"
   */
  private String fmtMilitaryTime(int hours, int minutes)
  {
    StringBuilder tod = new StringBuilder();
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


} // end of GameClock class
