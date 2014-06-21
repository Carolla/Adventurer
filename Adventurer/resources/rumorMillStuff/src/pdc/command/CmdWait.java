/*
 * CmdWait.java
 *
 * Copyright (c) 2007, Carolla Development, Inc. All Rights Reserved.
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com.  
 */

package rumorMillStuff.src.pdc.command;

import rumorMillStuff.src.hic.Dgn;

import java.util.ArrayList;

/**
 *	Allow the Hero to wait for a certain amount of time. The Wait command defaults to 5 minutes if no parms are specified.
 *	<p>
 *	Format: WAIT [N [H[ours] | M[inutes]] <br>
 * The command string is case-insensitive. See <code>init()</code> method.
 * 
 * @author Alan Cline
 * @version <DL>
 * <DT> 1.0 	 Jun 21	2007		// original		<DD> 
 * <DT> 1.1	Jul 4			2008		// Final commenting for Javadoc compliance<DD>
 * </DL>
 * @see Command
 * @see pdc.GameClock
*/
public class CmdWait extends Command
{
	// THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
	/** Syntax of the command, used by the <code>usage()</code> method. */
	private static final String CMD_FORMAT = "WAIT [N M[inutes]] | [N H[ours]] \n " +
			"where 1 <= N <= 59 minutes; or 1 <= N <= 24 hours";
    /** The description of what the command does, used in the <code>help()</code> method. */
	private static final String CMD_DESCRIPTION = "Do nothing for the given amount of time";
	/** This command starts immediately, requiring no delay. */
	private static final int DELAY = 0;				
	/** Default wait time is 5 minutes, but can be overridden when desired. */
	private static final int DURATION = 300;					

    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
    	
    /**
     * Default constructor, used by CommandFactory.
     */
    public CmdWait() 
    {
        super("CmdWait", DELAY, DURATION, CMD_DESCRIPTION, CMD_FORMAT);
    }

    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
    
    /**
     * Increases the clock while the Hero waits. <P>
     *	Format: WAIT [N [H[ours] | M[inutes]], where <DL>
     *<DT> N is any number between 1 and 24 if Hours are specified; else 1 to 60 if Minutes are specified.<DD>
     *<DT> H represents Hours to Wait. (H or Hours are acceptable.) <DD>
     *<DT> M represents Minutes to Wait. (M or Minutes are acceptable.) <DD>
     *</DL>  
     * The Wait command without any arguments defaults to 5 minutes..
     * The command string is case-insensitive.
     * 
     * @param	args		<br> args[0] = number of units to increment the game clock, 1 <= N <= 24 hours or 1 <= N <= 60 minutes
     * 								<br> args[1] = unit (duration) for amount of time elapsed during the command's execution
     * @return	true if parm list ok, else false. 
     */
    public boolean init(ArrayList<String> args)
    {
    	// Guard against incorrect parmlist
    	int nbrParms = args.size();
    	// Allow default condition to pass
    	if (nbrParms == 0) {
    		return true;
    	}
    	// Non-default command has only two parms: Nbr and Units of time
    	if (nbrParms != 2) {
    		usage();
    		return false;
    	}
    	// Validate the two parms and save valid data into Command
    	try {
            // Convert the first _parm to the int duration
    		String num = (String) args.get(0);
    		int lag = Integer.parseInt(num);

    		// Convert the second _parm to the unit of time
            String unit = (String) args.get(1);
            if (unit.equalsIgnoreCase("H") || unit.equalsIgnoreCase("Hr") || unit.equalsIgnoreCase("Hours"))  {
            	if ((lag >=1) && (lag <= 24)) {
            		// Convert hours to seconds and save in Command
            		super._duration = lag * 3600;
            		return true;
            	}
            }
            else if  (unit.equalsIgnoreCase("M") || unit.equalsIgnoreCase("Min") || unit.equalsIgnoreCase("Minutes"))  {
        		// Convert minutes to seconds
            	if ((lag >= 1) && (lag < 60)) {
            		super._duration = lag * 60;
            		return true;
            	}
            }
            // For whatever reason, the input line doesn't parse, so try again
		} catch (Exception e) {
			// fall through to usage() and false exit
    	}
    	// Fall through due to bad parms
    	usage();
    	return false;
    }

        
    /** 
     * Increments the game clock by the wait time requested
     * 
     * @return	true always after incrementing the game clock.
     */
    public boolean exec() 
    {
        System.out.println("CmdWait.exec(): TimeLog is incremented " + super._duration);
        return true;
    }
    
}	// end of CmdWait class
 
