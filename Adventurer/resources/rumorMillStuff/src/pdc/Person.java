/*
 * Person.java
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


/**
 *	Abstract class that holds common and generic information for Non-Player Characters 
 * (NPCs). The <code>Innkeeper</code> and all <code>Patron</code>s are instances of 
 * <code>Person</code>.	
 * 
 * @author Alan Cline
 * @version <DL>
 * <DT>1.0	 	Oct 25 2006		// original version <DD>
 * <DT>2.0 	Sep 29 2007		// Merged DgnBuild with DgnRunner   	<DD>
 * <DT>2.1 	Feb 16 2008		// Add affinity methods	<DD>
 * <DT>3.1		 Jul 3	2008		// Final commenting for Javadoc compliance<DD>
 * </DL>
 * @see Innkeeper
 * @see Patron
 */
public abstract class Person
{
	/** All Persons have a name */
	protected String _name = null;				

	/** What the Hero sees of this <code>Person</code> from a distance (usually with the 
	 * LOOK Command) */
    protected String _farDescription = null;		

    /** What the Hero sees close up (usually with the INSPECT Command) */
    protected String _nearDescription = null;

    /** Affinity is the probability that the <code>Person</code> will talk positively with the 
     * <code>Hero</code>--it reflects the <code>Person</code>'s friendliness or 
     * suspiciousness. Affinities range from -5 to +5 (-25% to +25%), similar to the 
     * <code>Hero</code>'s Charisma factor, which ranges from 8 to 18 .  Both numbers are 
     * added to get the next <code>Message</code>, whether postive or negative. <br>
     * Implementation Note: Currently, the range is enforced by the XSD Schema, and nothing
     * within this program. It would be safer to use restraints in the program, and cross-check
     * them against the XML values coming in.
    */
    protected int _affinity = 0; 

    /** Collection of positive and negative messages, each in their own queue. */
    protected MessageTable _msgTbl = null; 

    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
        	
    /** Unused default constructor. */
    public Person() { }

    
    /** 
     * Creates a <code>Person</code> of the specified name, and a container to hold all his 
     * <code>Message</code>s.
     * 
     * @param   name		of the <code>Person</code>
     * @param	affin			affinity, or probabilty to be friendly to the Hero, ranges from -5 to +5 
     */
    public Person(String name, int affin)
    {
        _name = name;
        _affinity = affin;
        _msgTbl = new MessageTable();
    }

    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								ABSTRACT METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
    
    /**
     *  Each <code>Person</code> must return a positive or negative message when asked. 
     *  The <code>Innkeeper</code> behaves differently when his messages are exhausted 
     *  than the <code>Patron</code>s. <code>Patron</code>s leave the <code>Inn</code>
     *  when they are out of messages, but the <code>Innkeeper</code> cannot leave, 
     *  so he recycles through his messages indefinitely. 
     *  
     *  @param 	polarity	true (positive messages) or false (negative messages)
     *  @return 		next message in table, or null if no more messages
     */
    public abstract String getNextMsg(boolean polarity); 

        
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	    /** 
	     * Adds a positive or negative <code>Message</code> into the 
	     * <code>MessageTable</code>. Called by the SAX Parser.
	     * 
	     * @param 	msg		the <code>Message</code> to save for this <code>Person</code>
	     * @param	polarity	positive or negative to determine which queue to store it in
	     */
	    public void addMsg(String msg, boolean polarity)
	    {
	   	 _msgTbl.add(msg, polarity);
	   }

    
	    /** Increases or decreases the <code>Person</code>'s affinity for Charisma rolls. 
	     * 
	     * @param affin		adjust the <code>Person</code>'s current affinity by a positive or 
	     * 				negative amount (currently must be in range of 
	     */
		public void adjustAffinity(int affin)
		{
		    _affinity += affin;
		}
	
	
	    /** Gets <code>Person</code>'s affinity value 
	     * @return affinity. 
	     */
		public int getAffinity()
		{
		    return _affinity;
		}
	

		/** Gets the <code>Person</code>'s apearance as seen from a distance.
		 * @return the description (far-off) 
		*/
    	public String getFarDesc()
	    {
	        return _farDescription;
	    }
	
    	
		/** Gets the <code>Person</code>'s name
		 * @return name 
		*/
		public String getName()
		{
		    return _name;
		}

	
		/** Gets the <code>Person</code>'s apearance, smells, details, when seen close up.
		 * @return the description (near) 
		*/
	     public String getNearDesc()
	    {
	        return _nearDescription;
	    }
	    

	    /** 
	     * Sets the far or near description of the <code>Person</code>, depending on the param.
	     * Called by the SAX Parser.  
	     * 
	     * @param desc	what the person looks like from afar, or up close, depending on the type parameter
	     * @param type		if true, then far-description; if false, then near-description
	     */
	    public void setDescription(String desc, boolean type)
	    {
	    	if (type == true) {
	    		_farDescription = desc;
	    	}
	    	else if (type == false) {
	    		 _nearDescription = desc;
	    	}
	    }
		    
//		    /** DEBUG: Dump the Person's message table for debugging. */
//		    public void dumpMsgs()
//		    {
//		    	_msgTbl.dumpMsgs();
//		   	}

}	   // end Person class
