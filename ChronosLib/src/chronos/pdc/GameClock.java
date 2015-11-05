/**
 * GameClock.java
 *
 * Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package chronos.pdc;


/**
 * Keeps track of the time as measured by the duration of each command executed. 
 * Continual updates are made to the windowing system.
 * @author Alan Cline
 * @version <DL>
 * <DT> 1.0	Jun 5 	2008   	// original <DD>
 * <DT> 1.1 	Jul 2		2008 		// Final commenting for Javadoc compliance<DD>
 * </DL>
 */
public class GameClock
{
	/** Conversion constant */
	private final long NBR_SECS_PER_HOUR = 3600L;
	/** Conversion constant */
	private final long NBR_SECS_PER_MINUTE = 60L;

	/** Set the default game clock time to 6am.*/
    final long START_HOUR = 6 * 3600;
	/** Internal: init the game clock */
    long _timeLog = 0L;

    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
    
    /** This object's singleton reference. */ 
    private static GameClock _clock = null;

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
    

/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
    
    /** Formats the total time log from seconds into hours, minutes, and seconds
     * @return	String		timeLog value formatted to HH:MM:SS AM (or PM)
     */
    public String getFormattedTime()
    {
    	final String COLON = ":";
    	long hrs = _timeLog / NBR_SECS_PER_HOUR;
    	long remainder = _timeLog % NBR_SECS_PER_HOUR;
    	long mins = remainder / NBR_SECS_PER_MINUTE;
    	long secs = remainder % NBR_SECS_PER_MINUTE;

    	// Concatenate pieces parts into formatted string
    	StringBuilder timestamp = new StringBuilder();
    	hrs = hrs % 24;
    	if (hrs < 10) {
    		timestamp.append("0");
    	}
    	timestamp.append(hrs);
    	timestamp.append(COLON);
    	if (mins < 10) {
    		timestamp.append("0");
    	}
    	timestamp.append(mins);
    	timestamp.append(COLON);
    	if (secs < 10) {
    		timestamp.append("0");
    	}
    	timestamp.append(secs);
    	// AM or PM qualifier
    	if (hrs < 12) {
    		timestamp.append(" AM");
    	}
    	else {
    		timestamp.append(" PM");
    	}
    	
    	String ts = new String(timestamp);
    	return ts;
    }


    /** Gets the time, returned to the nearest millisecond. 
     * The caller must format as desired.
     * 
     * @return the time in the GameClock.
     */
    public long getTime()
    {
    	return _timeLog;
    }

    
    /** Accumulates time to the clock. 
     * @param seconds 	the amount of time to add. 
     */
    public void increment(long seconds)
    {
    	_timeLog += seconds;
    }


}		// end of GameClock class