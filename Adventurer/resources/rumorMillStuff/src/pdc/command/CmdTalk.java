/**
 * CmdTalk.java
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
import rumorMillStuff.src.pdc.Hero;
import rumorMillStuff.src.pdc.Inn;
import rumorMillStuff.src.pdc.Patron;

import java.util.ArrayList;


/**
 * Allow the Hero to talk to a Person in the Room (including the Innkeeper).
 *	<p>
 * Format: TALK (TO | WITH) ( Name | INNKEEPER ) <br>
 * The command string is case-insensitive. See <code>init()</code> method.
 * 
 * @author Alan Cline
 * @version <DL>
 * <DT>1.0		Jun 30		2007 		// original			<DD>
 * <DT>2.0		Jan 1		2008 		// Move all commands into single package
*  <DT>2.1		Jul 4 		2008		// Final commenting for Javadoc compliance<DD>
 * </DL>
 * @see Command
 */
public class CmdTalk extends Command
{
		// THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
		/** Syntax of the command, used by the <code>usage()</code> method. */
		private static final String CMD_FORMAT = "TALK (TO | WITH) ( <Name> | INNKEEPER). ";
	    /** The description of what the command does, used in the <code>help()</code> method. */
		private static final String CMD_DESCRIPTION =  "Talk to the Innkeeper, or talk to a Person by Name.";
		/** This command starts immediately, requiring no delay. */
		private static final int DELAY = 0;						// This command starts immediately on invocation
		/** Talking to someone consumes 30 seconds	on the game clock. */
		private static final int DURATION = 30;				
	    
	    /** Example: TALK TO INNKEEPER or TALK TO Boren (for single Patron only) */
	    private final int NBR_ARGS = 2;				

	    /** Name and Person who the Hero is talking to */
		private String _argName = null;
		
		
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
			
		/**
	     * Default constructor, used by CommandFactory.
	     */
	    public CmdTalk() 
	    {
	        super("CmdTalk", DELAY, DURATION, CMD_DESCRIPTION, CMD_FORMAT);
	    }
			

/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	    /** 
		*  Checks that the parms for this command are valid. The actual Person cannot be retrieved
	     * until the command is executed because the current Room and Person is time-dependent. 
	     *	<br> Format: TALK (TO | WITH) ( Name | INNKEEPER ) <br>
	     * The command string is case-insensitive.
	     * 
	     * @param	args 	args[0] = TO or WITH keyword is required. <br>
	     * 								args[1] = name of the Person OR the keyword INNKEEPER <br>
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
	    	// First parm must be keyword TO or WITH
    		String parm1 = (String) args.get(0);
    		if ((parm1.equalsIgnoreCase("TO") == false) && (parm1.equalsIgnoreCase("WITH") == false)) {
    			usage();
    			return false;
    		}
	    	// Second parm must be keyword INNKEEPER or a Name
	    	String parm2 = (String) args.get(1);

	    	// Case 1:  Talk to INNKEEPER
	    	if (parm2.equalsIgnoreCase("INNKEEPER")) {
	    		_argName = parm2;
		    	return true;
		    	}
	    	
	    	// Case 2: We check that this perm is NOT a keyword, and assume it must be a name
	    	if ((parm2.equalsIgnoreCase("PATRON") == true) ||  (parm2.equalsIgnoreCase("INNKEEPER") == true)) {
	    		usage();
	    		return false;			// last parm must be a non-keyword
	    	}
	    	else {
	    		_argName = parm2;	// parm2 unidentifed, so assume that it was a Person's name to be checked during exec()
	    		return true;
		    }
	    }
	    

        /**
         * Gets the next message of the Person, depending on the Hero's Charisma
         * and the Person's affinity value.
         *  
         * @return	true if command works, else returns false
         */
        public boolean exec() 
        {
        	// Get the Person being talked to
        	if (getTalkee(_argName) == null) {
        		System.out.println("Can't find that person to talk with");
        		return false;
        	}
        	Dgn.auditMsg("\tTalking with " + _talkee.getName() + "....");
        	// Set busy flag so person doesn't leave prematurely
        	_curRoom.setBusy(_talkee);
        	
        	// Get the Hero for his Charisma roll
        	Hero myHero = Hero.createInstance();
        	// Get the Patron's affinity
        	int mod = _talkee.getAffinity();
        	// Check if Hero's charisma is enough to get him a friendly response
        	boolean polarity = myHero.rollCharisma(mod);

        	// Get positive or negative message from the Person
        	String response = 	_talkee.getNextMsg(polarity); 
    		// If the Patron has no more messages, he or she will leave the Inn.
    		// (The Innkeeper will reset his message count and always return a message.)
    		if (response == null) {
        		// Clear the busy flag so no one is busy, and person can leave immediately
        		_curRoom.setBusy(null);
    			Inn _myInn = Inn.getInstance();
        		_myInn.removePatron((Patron)_talkee);
    		}
    		// Display the Person's message
    		else {
        			System.out.println(_talkee.getName() + ":\t " + response);
        	}
        	return true;
        } 
                
	    	    
}		// end of CmdTalk class
