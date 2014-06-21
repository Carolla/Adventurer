/**
 *  CmdHelp.java 
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

import rumorMillStuff.src.hic.CommandParser;

import java.util.ArrayList;

/**
 * Shows the command names to allow the user to understand command options.
 * <p>
 *	Format: HELP <br>
 * The command string is case-insensitive. See <code>init()</code> method.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT>1.0	 	Jun 23 2007		// original <DD>
 * <DT>2.0 	Jan 1, 2008 		// move into command package<DD>
 * <DT>2.1	 	Jul 4 2008	 		// Final commenting for Javadoc compliance<DD>
 * </DL>
 * @see Command
 * @see CommandParser
 */
public class CmdHelp extends Command
{
	// THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
    /** The description of what the command does, used in the <code>help()</code> method. */
	static final String CMD_DESCRIPTION = "List the user command names and their descriptions.";
	/** This command is executed immediately (requires no delay before the command starts). */
	static final int DELAY = 0;
	/** This command adds no time to the game clock. */
	static final int DURATION = 0;


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
		
	/**
	 * Called by the Class class in the CommandFactory. This command has
	 * no delay, duration, or parms (therefore last argument to the super constructor is set to null). 
	 */
	public CmdHelp() {
		super("CmdHelp", DELAY, DURATION, CMD_DESCRIPTION, null);
	}

	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	/** Verifies that there are no parms following this command. <br>
	 * Format: HELP <br>
	 * 
	 * @param args none; a placeholder for the base class requirement
	 * @return false if invalid parm list (that is, parms are given)
	 */
	public boolean init(ArrayList<String> args)
	{
		if (args.size() == 0) {
			return true;
		}
		usage();
		return false;
	}

	
	/**
	 * Calls the <code>help()</code> method of the CommandParser to dump the command 
	 * table, so this method must retrieve the CommandParser's singleton reference first.
	 * The CommandParser invokes a window in which to display the commands and their
	 * brief description. 
	 * 
	 * @return true if command works, else returns false
	 */
	public boolean exec()
	{
		CommandParser.getInstance().help();
		return true;
	}

} // end CmdHelp class

