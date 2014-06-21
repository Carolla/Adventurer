/**
 * CmdReturn.java
 *
 * Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved.
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com.  
 */

package pdc.command;

import java.util.List;

import chronos.pdc.buildings.Building;
import civ.MainframeCiv;
import civ.CommandParser;


/**
 *	Moves the Hero from outside the Building to the Town view again. 
*	Format: RETURN [to Town] <br>
*   This command requires the Hero to be outside of a Building, otherwise it removes the Hero
*   from the building only. 
*  The command string is case-insensitive. See <code>init()</code> method.
 *
 * @author     Alan Cline
 * @version 	<DL>
 * <DT>1.0		Mar 29 2014     // original 		<DD>
 * </DL>
 * @see Command
  */
public class CmdReturn extends Command
{
    // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
    /** The description of what the command does, used in the <code>help()</code> method. */
    static final String CMD_DESCRIPTION = "Return to the Town view.";
	/** This command starts immediately, requiring no delay. */
    static final int DELAY = 0;				
	/** This command takes 10 seconds on the game clock. */
    static final int DURATION = 60;				
    /** Format for this command */
    static private final String CMDFMT = "RETURN [to Town]";
    
    /** The building to enter */
    private Building _curBldg = null;
    /** mainframe reference to get buildings */
    private MainframeCiv _mfCiv = null;
    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
    	
    /** Constructor called by the CommandFactory. There is no delay nor duration. */
    public CmdReturn() 
    {
        super("CmdReturn", DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
    }

    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

    /** 
     * Enters the current building.
     * There can be 0 or many args in the arglist. If an arg is not specified, then the current
     * Building is assumed. If more than one argument is specified, then they are all assumed to 
     * be part of the name. 
     * 
     * @param	args		if empty, then use current Buiilding; otherwise gets Building specified  
     * @param mfCiv 
     * @return	true		if all worked, else returns false on input error
     */
    public boolean init(List<String> args, MainframeCiv mfCiv)
    {
        _mfCiv = mfCiv;
        return true;
    }


    /** Forces the program to end. 
     * @return	false 	always to break out of the Scheduler loop
     */
    public boolean exec()
    {
        _mfCiv.openTown();
		return true;
    }

}	// end CmdReturn class
 
