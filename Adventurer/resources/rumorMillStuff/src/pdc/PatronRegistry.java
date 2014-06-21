/**
 * PatronRegistry.java
 *
 * Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package rumorMillStuff.src.pdc;

import rumorMillStuff.src.hic.Dgn;
import rumorMillStuff.src.pdc.command.Command;
import rumorMillStuff.src.pdc.command.intCmdEnter;
import rumorMillStuff.src.pdc.command.intCmdLeave;

import mylib.pdc.MetaDie;

import java.util.ArrayList;

/**
 * Contains a holding place for all the <code>Patron</code>s, and the logistics for those 
 * going to and from the <code>Inn</code>.  At initialization, <code>Event</code>s are 
 * assigned to allow a few random <code>Patron</code>s to start inside the 
 * <code>Inn</code>; others enter (and leave) throughout the game.<br>
 * <code>SAXStream</code> reads an XML input file to create <codePatron></code>s and
 * load up <code>PatronRegistry</code>.
 * 
 * @author 	Al Cline
 * @version <DL>
 * <DT>1.0		Oct 25		2006		// original version  <DD>
 * <DT>2.0		Mar 25	2007		// added serialization <DD>
 * <DT>3.0 	Oct 6		2007		// Read XML data directly into memory objects.  <DD>
 * <DT>3.1		 Jul 3		2008		// Final commenting for Javadoc compliance<DD>
 * </DL>
 * @see Scheduler
 * @see Patron
 * @see dmc.SAXStream
 */
public class PatronRegistry
{
		/** Max patrons starting in the Inn */
	    private final int MAX_STARTERS = 2;         	
	    /** Patrons start entering after these few minutes */
	    private final int MIN_DELAY = 3 * 60;       		
	    /**  Last Patron enters after no more than an hour */ 
	    private final int MAX_DELAY = 60 * 60;      		
	    /** Patrons stay at least 10 minutes */
	    private final int MIN_DURATION = 10 * 60;   
	    /** Patrons never stay longer than 30 minutes */
	    private final int MAX_DURATION = 30 * 60;  
	    /** Init to no-value-assigned yet; used to verify all values are assigned */
	    private final int EMPTY = -1;							
	    
	    /** Implemented as an ArrayList<Patron> */
	    private ArrayList<Patron> _patrons;

	    /** Reference to randomizer used globally */
	    private MetaDie _md = null;				

	    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
	    	
		/** This object's singleton reference used in Constructor */ 
	    private static PatronRegistry _patronRegistry = null;
	    		    
	    /** Private constructor for singleton. */
	    private PatronRegistry() 
	    {
	        _patrons = new ArrayList<Patron>();     // defaults to 16 entries
	    }

	    
	    /**
	     * Create or return the reference to singleton Registry. 
	     * @return <code>PatronRegistry</code> reference
	     */
	    public static synchronized PatronRegistry getInstance()
	    {
	        if (_patronRegistry == null) {
	            _patronRegistry = new PatronRegistry();
	        }
	        return _patronRegistry;
	    }


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	    /** 
	     * Gets a designated <code>Patron</code> from the PatronRegistry
	     *  
	     * @param index	0-based location of the <code>Patron</code> in the Registry
	     * @return the <code>Patron</code> requested
	     */
	    public Patron get(int index)
	    {
	        return (Patron) _patrons.get(index);
	    }

	    
	    /**
	     * Assigns the <code>Patron</code>s' delay and duration, then randomly assigns a few 
	     * <code>Patrons</code> (MAX_STARTERS) a zero-delay so they are present when the 
	     * <code>Hero</code> enters. The "time-loaded" <code>Patron</code>s are then put
	     * on the <code>Scheduler</code> queue for self-entering during runtime.<br>
	     * 
	     * @param   md  		<code>MetaDie</code> random generator for the game
	     * @param   skedder	<code>Scheduler</code> singleton for scheduling 
	     * 					<code>Patron</code>s
	     */
	    public void initPatrons(MetaDie md, Scheduler skedder)
	    {
	    	// Save the randomizer for later reference by private methods
	    	_md = md;
	    	
            // Create the intCmdEnter list for each Patron in the Registry
            // The starterList has no zero-delay intCmdEnter commands, each containing the
	    	// Patron who shall enter at the designated delay time.
            ArrayList<Command> starterList = createStarterList();
            
//          for DEBUGGING
//            // Show starterList command attributes, including internal Patron
//            for (int k=0; k < starterList.size(); k++) {
//            	intCmdEnter ce = (intCmdEnter) starterList.get(k);
//	            ce.show();				
//            }
            
            // Reassign MAX_STARTERS number of Patrons (inside the command) to delay = 0 so 
            // they are in the Inn when the Hero enters. 
            ArrayList<Command> enterList = createEnterList(starterList);
            
            // Show enterList command attributes, including internal Patron and new delays
            Dgn.auditMsg("\n");
            for (int k=0; k < enterList.size(); k++) {
            	intCmdEnter ce = (intCmdEnter) enterList.get(k);
//	            ce.show();			// FOR DEBUGGING
            	// Schedule the Patrons to ENTER the Inn
            	skedder.sched(ce);
            }
            
            // Create the corresponding intCmdLeave command list to match the intCmdEnter list
            ArrayList<Command> leaveList = createLeaveList(enterList);

            // Show leaveList command attributes, including internal Patron and new delays
            Dgn.auditMsg("\n");
            for (int k=0; k < leaveList.size(); k++) {
	            intCmdLeave clv = (intCmdLeave) leaveList.get(k);		// WHAT IS THIS??
//	            clv.show();			// FOR DEBUGGING
	            // Schedule the Patrons leaving the Inn
            	skedder.sched(clv);
            }
            // DEBUGGING: Dump the current state of the DQ
//            skedder.dump();
	    }
    
	    
	    /**
	     * Called by the <code>SAX Parser</code> when loading. 
	     * Registers a new <code>Patron</code> with the <code>PatronRegistry</code>
	     * 
	     * @param   p   the Patron to add
	     * @return  the number of <code>Patron</code>s in the Rregistry after the add
	     */
	    public int register(Patron p)
	    {
	        _patrons.add(p);
//	        Dgn.auditMsg(p.getName() + " is Patron " + _patrons.size()+ ", now registered.");
	        return _patrons.size();
	    }

	    
	    /** 
	     * Lists the names of all <code>Patron</code>s in the <code>PatronRegistry</code>.  
		 */
	    public void showPatrons()
	    {
	    	for (int k=0; k < _patrons.size(); k++)  {
	    		Patron p = (Patron) _patrons.get(k);
	    		System.out.println(p.getName() + ": " + p.getFarDesc());
	    	}
	    }
	    
	    
	    /** 
	     * How many <code>Patron</code>s are in this <code>PatronRegistry</code>?
	     * @return	the number of <code>Patron</code>s 
	     */
	    public int size()
	    {
	        return _patrons.size();
	    }
	    
	    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PRIVATE METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	    /**
	     * Sets 1 to MAX_STARTER number of <code>Patron</code>s with a delay value of 0. 
	     * Starts with the <code>startList</code>, putting the internal <code>Patron</code>s 
	     * in the <code>Inn</code> before either the <code>Hero</code> or the other 
	     * <code>Patron</code>s enter.
	     * 
	     * @param startList		the list of <code>intCmdEnter</code>s with non-zero 
	     * 							<code>intCmdEnter</code> times
	     * @return 	enterList - updated <code>startList</code> with a few zero 
	     * 							<code>intCmdEnter</code> times
	     */
	    private ArrayList<Command> createEnterList(ArrayList<Command> startList)
	    {
	    	int enterIndex = EMPTY;

	    	// Generate a zero-entry Patron up to MAX_STARTER times
	    	for (int k=0; k < MAX_STARTERS; k++) {
	    		// Generate the index of the starting Patrons; adjust for zero-based indexing
	    		enterIndex = _md.roll(1, _patrons.size()) - 1;

	    		// Get the command and reset the delay for that Command
	    		intCmdEnter cmd = (intCmdEnter) startList.get(enterIndex);
	    		cmd.setDelay(0);
	            
	    		// Put the cmd back into the startList (as update, not insertion)
	    		startList.set(enterIndex, cmd);
	    	}

	    	// Return the updated starter list
	    	return startList;
	    }

	    
	    /**
		 * Creates a matching set of <code>intCmdLeave</code> commands to go with the 
		 * <code>intCmdEnter</code> commands.
	     * <code>intCmdEnter.delay</code> puts a <code>Patron</code> in after a random 
	     * time, and its duration attribute spawns the <code>intCmdLeave.delay</code>, where 
	     * <code>intCmdLeave.delay = intCmdEnter.duration</code>,  which removes the 
	     * <code>Patron</code> from the <code>Inn</code>.
	     * <p> 
	     * Both the Enter event and Leave event are put on the <code>Scheduler</code> queue
	     * at the approximately the same time.
	     * 
	     * @param enterList		the list of <code>intCmdEnter</code>s from which the 
	     * 				durations are extracted to create the <code>intCmdLeave</code> delays
	     * @return 	<code>intCmdLeave</code> list matching <code>intCmdEnter</code> list
	     */
	    private ArrayList<Command> createLeaveList(ArrayList<Command> enterList)
	    {
	        // Create a list to hold the Leave commands
	        ArrayList<Command> leaveList = new ArrayList<Command>();		
	    	
	    	// Traverse the enterList, extract the durations, and set the intCmdLeave delays
	    	for (int k=0; k < enterList.size(); k++) {
	    		// Get the intCmdEnter duration and generate the intCmdLeave delay
	    		intCmdEnter cmdIn = (intCmdEnter) enterList.get(k);
	    		intCmdLeave cmdOut = new intCmdLeave(cmdIn);

	    		// Assign the leave command to the Patron
	    		cmdOut.setPatron(cmdIn.getPatron());

	    		// Now add the finished intCmdLeave command to the list
	    		leaveList.add(cmdOut);
	    	}

	    	// Return the new leaveList list
	    	return leaveList;
	    }


	    /**
	     * Creates an interim list <code>of intCmdEnter</code> commands with a random 
	     * delay and duration for all the <code>Patron</code>s in the
	     * <code>PatronRegistry</code>.  Each intCmdEnter is assigned a Patron to enter
	     * the Inn when it is their time. <br> 
	     * A CommandList is created instead of a Patron list because Events wrap Commands, 
	     * not Patrons. 
	     * 
	     * @return	List of random non-zero <code>intCmdEnter</code> commands for each 
	     * 			<code>Patron</code>
	     */
	    private ArrayList<Command> createStarterList()
	    {
	    	// Default variables for set up.
	        int delay = EMPTY;
	        int duration = EMPTY;
	        intCmdEnter cmd = null;
	
	        // Create a list to hold the Enter commands
	        ArrayList<Command> cmdStarterList = new ArrayList<Command>();		

	        //	This ArrayList<String> is required as a wrapper for the cmd.init() method
	        // intCmdEnter takes only 1 arg = Patron entering
	    	ArrayList<String> args = new ArrayList<String>(1);					

	    	// Walk the PatronRegistry for all Patrons and assign their Enter commands
	    	for (int k=0; k < _patrons.size(); k++) {
		        // Create random delay and duration parms to associate Patron with Enter command
	        	// Create the Patron's random intCmdEnter command 
	        	delay = _md.roll(MIN_DELAY, MAX_DELAY);
	            duration = _md.roll(MIN_DURATION, MAX_DURATION);
	            cmd = new intCmdEnter(delay, duration);
	            // Create the arg list with destination room (Inn) and Patron who enters it
//	            _Patron p = (Patron) _patrons.get(k);

	            // Assign the Patron into the command
	            cmd.setPatron(_patrons.get(k));  	
	            // Clear out old patrons and reuse this ArrayList<Patron> for the single parm needed
	            args.clear();
	            // Add the Patron's name as the argument (Patron object cannot go in the arg list)
//	            args.add(0, p.getName());				
	        	// Set the arg list into the command; the Scheduler will call Command.exec()
	        	cmd.init(args);			 
	    		cmdStarterList.add(k, cmd);		
	        }   
	    	return cmdStarterList;
	    }
	    
	        
	    /** 
	     * For DEBUGGING: List the names of all the Patrons in the Registry. 
	     * Give their far and near descriptions, and all their messages.
	     */
	    public void dump()
	    {
        	Dgn.auditMsg("\nPATRON REGISTRY DUMP: ");
	    	for (int k=0; k < _patrons.size(); k++)  {
	    		Patron p = (Patron) _patrons.get(k);
	    		Dgn.auditMsg(p.getName() + "[" + p.getAffinity() + "]: ");
	    		Dgn.auditMsg("\t    [FAR]:\t\t" + p.getFarDesc());
	    		Dgn.auditMsg("\t[CLOSE]:\t\t" + p.getNearDesc());
//	    		p.dumpMsgs();    		
	    	}
	    }

}   // end of PatronRegistry singleton class

