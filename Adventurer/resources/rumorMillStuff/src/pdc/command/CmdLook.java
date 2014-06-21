/*
 * CmdLook.java
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

import java.util.ArrayList;

/**
 *	Gets the description of the current room, and all the Persons in it.
 *	<p>
*	Format: LOOK <br>
 * The command string is case-insensitive. See <code>init()</code> method.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT>1.0		Aug 29	2006			// original 			 <DD>
 * <DT>2.0		Jan 1		2008 			// move commands into single package <DD>
 * <DT>2.1		Jul 4 		2008			// Final commenting for Javadoc compliance<DD>
 * </DL>
 * @see Command
 */
public class CmdLook extends Command
{
    // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
    /** The description of what the command does, used in the <code>help()</code> method. */
	static final String CMD_DESCRIPTION = "Give a rough description of the Room and any People inside it.";
	/** The command starts immediately, requiring no delay. */
    static final int DELAY = 0;						// This command starts immediately on invocation
	/** The time the Hero takes to get a close inspection of the Room. */
    static final int DURATION = 4;				// (seconds) It takes this long to look around the room

    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
    	
    /** Constructor called by the Class class in the CommandFactory. 
     * There are no parameters so the super constructor takes a null last argument. 
     */
    public CmdLook() 
    {
        super("CmdLook", DELAY, DURATION, CMD_DESCRIPTION, null);
    }
    	
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 								PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

    /** 
     * Verifies that no parms follow the command. 
     * 
     * @param	args		is empty for this command; implemented as required for abstract method 
     * @return	true		if no parms given; else show <code>usage()</code>	
     */
    public boolean init(ArrayList<String> args)
    {
		if (args.size() == 0) {
			return true;
		}
		else {
			usage();
			return false;
		}
    }

    		
    /** 
     * Invokes the <code>look()</code> method of the current Room
     * @return	true if command works, else returns false
     */
    public boolean exec() 
    {
        if (_curRoom != null) {
            _curRoom.look();
        }
        return true;
    }
    
}	// end of CmdLook class
 
