/*
 * Innkeeper.java
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


/**
 *	Owner of the <code>Inn</code>, a subclass of <code>Person</code>.
 *	If too many <code>Patrons</code> leave the Inn, the <code>Innkeeper</code> will kick 
 * the <code>Hero</code> out for scaring off his customers. The game ends.
 *  
 * @author Alan Cline
 * @version <DL>
 * <DT>1.0 Nov 13 2005		// original version <DD>
 * <DT>1.1 Mar 20 2006   	// minor updates <DD>
 * <DT>2.0 Mar 25 2007		// added serialization <DD>
 * <DT>3.0 Sep 29 2007		// removed serialization when DgnBuild and DgnRunner merged <DD>
 * <DT>3.1 Jul 3	2008 		// Final commenting for Javadoc compliance<DD>
 * </DL>
 * @see Patron
 * @see Inn
 * @see Person
 * @see Hero
 */
public class Innkeeper extends Person
{
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  		

    /** Default constructor */
    public Innkeeper()  { } 

    /** 
     * Creates a <code>Person</code> of the specified name (the Innkeeper Bork), and a 
     * container to hold all his messages.
     * 
     * @param   name        of the Person
     * @param	affin			affinity, or probabilty to be friendly to the Hero
     */
    public Innkeeper(String name, int affin) 
    { 
        super(name, affin);
    } 

    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

    /**
    * Gets the Innkeeper's next message, either positive or negative. 
    * Resets the message index if he is out of messages of a certain polarity.
    * 
    * @param	polarity	<code>Dgn.POSITIVE</code> (true) for positive message; 
    * 									<code>Dgn.NEGATIVE</code> (false) for negative
    * @return  message from <code>Innkeeper</code>
    */ 
   public String getNextMsg(boolean polarity)
   {
       String msg = _msgTbl.viewCurrentMsg(polarity);
       // If out of messages of the given polarity, reset and try again
       if (msg == null ) {
    	   _msgTbl.reset(polarity);
           msg = _msgTbl.viewCurrentMsg(polarity);
           if (msg == null) {
        	   // Now there's a problem...
        	   Dgn.debugMsg("Can't restart Innkeeper's message table for " + polarity + " messages.");
           }
       }
       return msg;
   }

   
//	   /** 
//	    * For DEBUGGING: List the name and affinity of the Innkeeper 
//	    * Give his far and near description 
//	    */
//	   private void dump()
//	   {
//	   	Dgn.auditMsg("\nINNKEEPER DUMP: ");
//	   		Dgn.auditMsg(getName() + "[" + getAffinity() + "]: ");
//	   		Dgn.auditMsg("\t    [FAR]:\t\t" + getFarDesc());
//	   		Dgn.auditMsg("\t[CLOSE]:\t\t" + getNearDesc());
//	//   		p.dumpMsgs();    		
//	   	}


}		// end of Innkeeper class
