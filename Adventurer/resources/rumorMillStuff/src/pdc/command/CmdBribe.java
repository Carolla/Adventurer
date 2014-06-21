/**
 * CmdBribe.java
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
 * Forces a positive message from the Person by giving money to that Person. 
 * Hero must have at least DEFAULT_BRIBE gp for this command to work.
 *  <p>
 * <br> Format: BRIBE (Name | INNKEEPER) <br>
 * The command string is case-insensitive. See <code>init()</code> method.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT>1.0 	Jan 16 2008 		// original <DD>
 * <DT>1.1 	Feb 23 2008 		// cost of a bribe change to an affinity-based calculation. <DD>
 * <DT>1.2 	Jul 4 2008	 		// Final commenting for Javadoc compliance<DD>
 * </DL>
 * @see Command
 */
public class CmdBribe extends Command
{
	    // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
		/** Name and syntax of the command, used in <code>usage()</code> method. */
		static final String CMD_FORMAT = "BRIBE <Name>";
	    /** The description of what the command does, used in the <code>help()</code> method. */
		static final String CMD_DESCRIPTION =  "Bribe a Patron to get a positive message.";
		/** Time (in seconds) needed to get the money and propose the bribe (before the command starts). */
		static final int DELAY = 10;					// it takes 10 seconds to get the money and propose the bribe				
		/** Time (in seconds) needed to offer and complete the bribe. */
	    static final int DURATION = 30;			// talking to someone consumes 30 seconds				
	    
	    // Other constants
	    /** Parameters that follow the command on the command window */
	    private final int NBR_ARGS = 1;					
	    /**	Amount needed for success depends on the Patron's negative affinity*/
	    private final int DEFAULT_BRIBE = 2;			

	    /** Internal: Name and Person who the Hero is talking to */
		private String _argName = null;

		
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
			
		/** Default constructor, used by CommandFactory. */
	    public CmdBribe() 
	    {
	        super("CmdBribe", DELAY, DURATION, CMD_DESCRIPTION, CMD_FORMAT);
	    }

	    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	    /** Checks that the parms for this command are valid. The actual Person cannot be 
	     * retrieved until the command is actually executed because the current Room and Person
	     * are time-dependent.
	     * <br> Format: BRIBE (Name | INNKEEPER) <br> 
	     * The command string is case-insensitive.
	     * 
	     * @param	args 	args[0] = name of the Person OR the keyword INNKEEPER, 
	     * 				although the Innkeeper is not bribable
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
	    	String parm = args.get(0);
    		_argName = parm;
	    	return true;
	    }
	    

        /**
         * Gets the Patron and next positive message, if the Hero has enough money.
		 * If the Person gives his/her rumor (last positive message), then she/he will leave
		 * the Inn.
		 * 
         * @return	true if command works, else returns false
         */
        public boolean exec() 
        {
        	// Get the Hero for his Charisma roll
        	Hero myHero = Hero.createInstance();
 
        	// Get the Person being talked to
        	if (getTalkee(_argName) == null) {
        		System.out.println("Can't find that person to talk with");
        		return false;
        	}
        	Dgn.auditMsg("\tTrying to bribe " + _talkee.getName() + "....");
        	// Set busy flag so person doesn't leave prematurely
        	_curRoom.setBusy(_talkee);

        	// The Hero must have enough money for the bribe. 
        	// If the Patron's affinity is negative, the bribe amount is the negative affnity value, or the default, whichever is greater
        	if (myHero.spendMoney(calcBribe()) == false) {
        		System.out.println("You don't have enough money for the bribe!.");
        		return false;
        	}
    		// Bribing (at least for now) always forces a positive message
    		String response = 	_talkee.getNextMsg(Dgn.POSITIVE);
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

        
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PRIVATE METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

        /** Calculates the bribe needed for a postive message. 
         * If the Patron's affinity is negative, then the bribe is the inverse of that.
         * If the Patron's affinity is non-negative, the bribe is the default value.
         * 
         * @return amount of bribe calculated
         */
        private int calcBribe()
        {
        	int amount = 0;
        	int affin = _talkee.getAffinity();
        	if (affin < 0 ) {
        		amount = affin * -1;
        	}
        	// Find the grater of the two
        	amount = (amount > DEFAULT_BRIBE) ? amount : DEFAULT_BRIBE ;
        	return amount;
        }
        
}		// end of CmdBribeclass
