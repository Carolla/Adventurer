/*
 * Patron.java
 *
 * Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package pdc.character;


/**
 *	Provides conversational dialog with the Hero, and possibly rumors of information.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT>1.0		Oct 25 2006		// original version <DD>
 * <DT>2.0		Mar 25 2007		// added serialization <DD>
 * <DT>3.0		Sep 30 2007		// removed serialization when DgnBuild and DgnRunner was merged. <DD>
 * <DT>3.1		 Jul 3	2008		// Final commenting for Javadoc compliance<DD>
 * </DL>
 * @see Person
 * @see Innkeeper
 * @see Hero
 */
@SuppressWarnings("serial")
public class Patron extends Person
{
	/** Internal: Indicates if <code>Patron</code> left after a rumor, or if they rebuked the 
	 * <code>Hero</code> and stomped off. 
	 */
	private boolean _rebuke = false;
	    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 								CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
    	
	/** Default constructor */
    public Patron() { }
	

    /** 
     * Creates a <code>Person</code> of the specified name, and a container to hold all his
     * or her messages.
     * 
     * @param   name        of the Person
     * @param	affin			affinity, or probabilty to be friendly to the Hero
     */
    public Patron(String name, int affin) 
    { 
        // TODO: Fix this soon
//        super(name, affin);
    }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 								PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
    
    /**
     * Gets the <code>Patron</code>'s next message, either positive or negative.
     * If <code>Patron</code> is out of negative messages, set the rebuke flag for exiting; 
     * if <code>Patron</code> is out of positive messages, <code>Patron</code> will leave 
     * normally (no rebuke).
     * <p>
     * There may be a problem with the way this works. 
     * See <code>MessageTable. viewCurrentMsg()</code>.
     * 
     * @param	polarity	<code>POSITIVE</code> (true) for positive message; 
     * 									<code>NEGATIVE</code> (false) for negative
     * @return  message from <code>Person</code>; else null if no more messages
     */ 
    public String getNextMsg(boolean polarity)
    {
//        String msg = _msgTbl.viewCurrentMsg(polarity);
//        // Set rebuke flag if all negative messages are given
//        if ((msg == null) && (polarity == false)) {
//            setRebuke();
//        }
//        // TODO placeholder for now
        String msg = "Fix this soon";
        return msg;
    }


    /** Checks if the <code>Person</code> is annoyed, and rebuked the 
     * <code>Hero</code>. 
     * 
     * @return 	true if the Patron is annoyed with the Hero
     */
    public boolean hasRebuke() 
    {
    	return _rebuke;
    }

    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PRIVATE METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

//    /** Sets the flag to indicate the <code>Patron</code> is annoyed and leaving. 
//     * The <code>Innkeeper</code> will eject the <code>Hero</code> from the game when 
//     * the rebuke count is high enough.
//     */
//    private void setRebuke() 
//    {
//    	_rebuke = true;
//    }

    
}   // end Patron class
