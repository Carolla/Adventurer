/**
 * MessageTable.java
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

import java.util.ArrayList;

/** Maintains two lists of positive and negative messages. 
 * If a <code>Patron</code> runs out of messages, he/she leaves the <code>Inn</code>. 
 * The <code>Innkeeper</code> never leaves, so has more messages than the Patrons, and 
 * cycles messages when his list of messages are exhausted. <br>
 * This class is implemented as two separate <code>String ArrayLists</code>; there is no
 * limit on how many messages each Patron may have. .
 * 
 * @author 	Al Cline
 * @version <DL>
 * <DT>1.0 Oct 28 	2006			// original <DD>
 * <DT>2.0 Sep 29	2007			// added Innkeeper recycling <DD>
 * <DT>2.1 Jul 3		2008			// Final commenting for Javadoc compliance<DD>
 * </DL>
 */
public class MessageTable
{
	/** Reserve of positive messages when Patron is friendly. */
    private ArrayList<String> _posMsgs;
	/** Reserve of negative messages when Patron is unfriendly. */
    private ArrayList <String>_negMsgs;
    /** Internal: Index into the postive message list. */
    private int _posNdx;            // the current positive message
    /** Internal: Index into the postive negative list. */
    private int _negNdx;            // the current negative message    

    private final boolean POSITIVE = true;
    private final boolean NEGATIVE = false;
    
    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  	
    	
    /** 
     * Create two list of messages. The table grows as more messages are added. 
     */
    public MessageTable() 
    { 
    	// Create the two empty message arrays
    	_posMsgs = new ArrayList<String>();
    	_negMsgs = new ArrayList<String>();
    	
        // Init the state flags; each keeps track of the last message used
        _posNdx = 0;
        _negNdx = 0;
    }

/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

    /** 
     * Adds a message into the positive or negative message list (used by the SAX Parser).
     *  
     * @param 	msg				the message to save for this Person
     * @param	polarity		positive or negative message to determine which list to store it in
     * @return	false if formal parms are invalid
     */
    public boolean add(String msg, boolean polarity)
    {
       // Append messages to the appropriate message list
       if (polarity == POSITIVE) { 
       	_posMsgs.add(msg);
       	return true;
       }
       else if (polarity == NEGATIVE) {
       	_negMsgs.add(msg);
       	return true;
       }
       return false;
   }

    
	 /**
	  * Resets the index counter of a message table to the beginning to recycle the messages.
	  * Used by the <code>Innkeeper</code> because he doesn't leave, so he cannot exhaust 
	  * his messages.
	  * 
	  * @param polarity		true for positive messages; false for negative. 
	  */
	 public void reset(boolean polarity) 
	 {
		 if (polarity == POSITIVE) {
			 _posNdx = 0;
		 }
		 else if (polarity == NEGATIVE) {
			 _negNdx = 0;
		 }
	 }
	 

	  /**
	  * Gets the current message in the list of the given polarity without incrementing the 
	  * message count index. If the message list is empty, a null is returned. 
	  * <p>
	  * NOTE: This needs to be investigated--it might be a bug. Should be able to run through the
	  * message list without getting nulls back. 
	  *  
	  * @param   polarity    true for POSITIVE message; false for NEGATIVE message 
	  * @return  the message indicated
	  */
	 public String viewCurrentMsg(boolean polarity)
	 {
		 String retMsg = null;
		 int pSize = _posMsgs.size();
		 int nSize = _negMsgs.size();
	
		 // If either one of the message lists are exhausted, then return null
		 if ( (_posNdx >= pSize) || (_negNdx >= nSize)) {
			 return null;
		 }
		 // Get current message from appropriate message list 
     		try {
     			if (polarity == POSITIVE) { 
     				retMsg = (String) _posMsgs.get(_posNdx++);			// Zero-based indexing for ArrayLists
     			}
     			// Increment negative index to next message after retrieval
     			else if (polarity == NEGATIVE)  {
     				retMsg = (String) _negMsgs.get(_negNdx++);				// Zero-based indexing for ArrayLists
     			}
	    	} catch (IndexOutOfBoundsException e) {
	    		System.err.println("Message requested out of bounds " + e.getMessage());
	    	}
	        return retMsg;
	 }
	 

//		 /**
//		  * DEBUG: Dump all the messages in the positive and negative lists. Do not increment the current message pointer.
//		  */
//		 public void dumpMsgs()
//		 {
//			 for (int k=0; k < _posMsgs.size(); k++) {
//				 Dgn.auditMsg("Positive " + k + ": \t" + (String) _posMsgs.get(k));
//			 }
//			 for (int k=0; k < _negMsgs.size(); k++) {
//				 Dgn.auditMsg("Negative " + k + ": \t" + (String) _negMsgs.get(k));
//			 }
//		 }

		 
}   	// end of MessageTable class

