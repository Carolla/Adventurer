/*
 * intCmdPatronLeave.java
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

import rumorMillStuff.src.pdc.Patron;
import rumorMillStuff.src.pdc.Scheduler;


/**
 *	This internal command causes a Patron to exit the Inn.
 * The dungeon Inn and the Room is a common reference contained in the Command base class,
 *	so is not needed to be passed in through the arglist.
 * @author Alan Cline
 * @version <DL>
 * <DT>1.0 	Nov 15 2007		// Original <DD>
 * <DT>2.0  	Dec 31 2007	  	// Moved this command into the latest command package <DD>
 * <DT>2.1		Jul 5		2008		// Final commenting for Javadoc compliance<DD>
 * </DL>
 * @see Command
 */
// TODO: This may be better if it extended Command, and not one of its subclasses 
public class intCmdLeave extends intCmdEnter
{
		// THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
		/** Causes a Patron to leave the room after a certain amount of time. */
//    static final String CMD_DESCRIPTION = 
    static final String TMP_DESCRIPTION = 
		                "Cause a Patron to leave the room after a certain amount of time.";
		/** This default value is overwritten by the amount of time taken from CmdEnter. */
//	    static final int DELAY = 0;				
	    /** This command uses up 10 seconds on the game clock. */
	    static final int TMP_DURATION = 10;
	    /** If the Patron cannot leave for some reason, intCmdLeave is rescheduled for this amount of time later. */
	    static final int RE_DELAY = 30;
	    	
	    /** Since the Patron is included in the Command during init, no args are needed */
//	    private final int NBR_ARGS = 0;
	    
		/** The Patron who is designated to leave the Inn. */ 
		private Patron _npc = null;
    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
	    		    
	    /** 
	     * Causes a Patron to leave when Inn when his/her time is up. This command cannot be 
	     * called by the User; it is called by the Class class in the CommandFactory.
	     * Requiring intCmdEnter for the constructor ensures that no Patron can leave the Inn
	     * without first entering it.  Overrides exec() but needs to copy attributes over from 
	     * intCmdEnter.
	     * 
	     * @param ce 	intCmdEnter command to copy its attributes over this command
	     */ 
	    public intCmdLeave(intCmdEnter ce)
	    {
	    	_name = "intCmdLeave";
//            _description = CMD_DESCRIPTION;
            _description = TMP_DESCRIPTION;
	    	_delay = ce.getDuration()+ ce.getDelay();
	    	_duration = TMP_DURATION;
	    	setPatron(ce.getPatron());
	    }

	    
//	    /** 
//	     * Causes a Patron to leave when Inn when his/her time is up. This command cannot be 
//	     * called by the User; it is called by the Class class in the CommandFactory.  
//	     * Uses fixed DELAY and DURATION values. 
//	     */ 
//	    public intCmdLeave()
//	    {
//	        super("intCmdLeave", DELAY, DURATION, CMD_DESCRIPTION, null);
//	    }
	    
//	    /** 
//	     * Overrides the default constructor for non-standard internal attributes for the 
//	     * intCmdLeave command
//	     *  
//	     * @param delay	the length of time before it is executed (Patron leaves the Room)
//	     */ 
//	    public intCmdLeave(int delay)
//	    {
//	        super("intCmdLeave", delay, DURATION, CMD_DESCRIPTION, null);
//	    }
	
	    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

//	    /** 
//	     * Sets all attributes in addition to the ones not set by default in the abstract base class.
//	     *  
//	     * @param	args			String list: args[0] = name of Patron to add to the current Room
//	     * @return	false		if invalid parm list 
//	     */
//	    public boolean init(ArrayList args)
//	    {
//	    	if (args.size() == NBR_ARGS) {
//	    		// Copy the element into the command's array, don't set the reference
////	    		super._parms.add(0, (String) args.get(0));
//	   			return true;
//	    	} 
//	    	else {
//	    		return false;
//	    	}
//	    }
	
	        
	    /** 
	     * Causes the Patron to leave the room if he/she is not involved in conversation.
	     * If the Patron is talking, then intCmdLeave is reset for another RE_DELAY time period.
	     * 
	     * @return	true if command works
	     */
	    public boolean exec() 
	    {
	    	boolean success = true;
	    	
	    	// Get the Patron from the parms list
//	    	String patronName = _parms.get(0);
	    	// Remove the Patron from the room; do not add him back to the PatronRegistry
//	    	Patron p = (Patron) super._curRoom.getPerson(patronName);
//	    	if (p != null) {
//	    		success = super._curRoom.remove(p);
//	    	}
	    	if (_npc != null) {
	    		success = super._curRoom.remove(_npc);
	    	}
	    	// If patron was not removed from the Room, reset the intCmdLeave delay and reschedule
	    	if (success == false) {
	    		_delay = RE_DELAY;
	    		// Get the Scheduler reference so this command can reschedule itself
	    		Scheduler scheduler = Scheduler.getInstance();			
	    		scheduler.sched(this);			
	    	}
	    	return true;
	    }

	    	    
//	    /** Get the Patron inside this command to which it applies. 
//	     * @return the Patron internal to the command
//	     */
//	    public Patron getPatron()
//	    {
//	    	return _npc;
//	    }
//
//	    
//	    /** Assign the Patron into the Command, so he/she can intCmdEnter later 
//	     * @param p internal Patron who will leave the Inn later 
//	     */
//	    public void setPatron(Patron p)
//	    {
//	    	_npc = p;
//	    }

	    
//	    /** DEBUGGING: Show the Patron for this command, and the delay and duration. */
//	    public void show()
//	    {
//	    	Patron p = getPatron();
//	    	Dgn.auditMsg(p.getName() + "\tdelay = " + _delay + ", duration = " + _duration
//					+ ";\tTIME OUT = " + (_delay+_duration));
//	    }

	    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PRIVATE METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

//	    /** Gets the Patron assigned into this command.
//	     * @return the Patron from the parms list to which this command applies. 
//	     * */ 
//	    private Patron revealPatron()
//	    {
//	    	return _npc;
//	    }

	    	    
//	    /** DEBUGGING: Show the Patron for this command, and the delay and duration. */
//	    public void show()
//	    {
//	    	Patron p = (Patron) super._parms.get(0);
//	    	Dgn.debugMsg(p.getName() + "\tdelay = " + _delay + ", duration = " + _duration
//	    				+ ";\tTIME OUT = " + (_delay+_duration));
//	    }
   
    
}	// end of intCmdLeave class

