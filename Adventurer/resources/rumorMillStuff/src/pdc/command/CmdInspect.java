/*
 * CmdInspect.java
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
 *	Gets the near description of the Person.<br> 
 *NOTE: In the future, Inspect should be called the first time Hero talks to this Patron. It would
 *be automatic in a real-life situation as you get close enough to talk.
 *<p>
 * Format: INSPECT (Name | INNKEEPER) <br>
 * The command string is case-insensitive. See <code>init()</code> method.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT>1.0		Jan 16 2008 		// original 	<DD>
 * <DT>1.1		Jul 4 2008	 		// Final commenting for Javadoc compliance<DD>
 * </DL>
 * @see Command
 */
public class CmdInspect extends Command
{
    // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
	/** Name and syntax of the command, used in <code>usage()</code> method. */
	static final String CMD_FORMAT = "INSPECT ( <Name> | INNKEEPER). ";
    /** The description of what the command does, used in the <code>help()</code> method. */
	static final String CMD_DESCRIPTION =  "Look closely at the Innkeeper or Patron.";
	/** The command starts immediately, requiring no delay. */
    static final int DELAY = 0;
	/** The time (seconds) needed to get a close inspection of the Patron. */
    static final int DURATION = 10;				

    // Other contants
    /** Only the Person being inspected needs to be supplied */
    private final int NBR_ARGS = 1;					

    /** Internal: Person who the Hero is inspecting */
	private String _argName = null;


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	/** Constructor called by the Class class in the CommandFactory. */
    public CmdInspect() 
    {
        super("CmdInspect", DELAY, DURATION, CMD_DESCRIPTION, CMD_FORMAT);
    }
    
        	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

    /** 
	 *  Checks that the parms for this command are valid. The actual Person cannot be retrieved
     * until the command is actually executed because the Person is time-dependent.  
     *	<br> Format: INSPECT (Name | INNKEEPER) <br>
     * The command string is case-insensitive.
     * 
     * @param	args 	args[0] = name of the Patron, or the keyword INNKEEPER
     * @return 	true if parm list is valid
     */
    public boolean init(ArrayList<String> args) 
    {
    	// First check that there are the correct number of parms
    	int nbrParms = args.size();
    	if (nbrParms != NBR_ARGS) {
    		usage();
    		return false;
    	}
    	// Second parm must be keyword INNKEEPER or a Name
    	String parm = (String) args.get(0);
    	if (parm != null) {
    		_argName = parm;
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    	
    		
    /** 
     * Displays the near description of the Patron.
     * @return	true if command works, else returns false
     */
    public boolean exec() 
    {
    	// Get the person requested
    	if (getTalkee(_argName) == null) {
    		System.out.println("I don't see that person here.");
    		return false;
    	}
    	// Display the near description of the Person
    	System.out.println(_talkee.getName() + ": " + _talkee.getNearDesc());
        return true;
    }
   

}	// end of CmdInspect class
 
