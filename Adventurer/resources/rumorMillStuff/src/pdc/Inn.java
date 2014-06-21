/*
 * Inn.java
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
 *	The <code>Inn</code> is a dungeon singleton container for a collection of Rooms. 
 * Currently, the <code>Inn</code> consists of a single <code>Room</code>.
 *  
 * @author Alan Cline
 * @version <DL>
 * <DT>1.0 Nov 5  2005			// Original functionality on laptop <DD>
 * <DT>2.0 Nov 30 2005			// Moved to Linux for XML file input <DD>
 * <DT>3.0 Jan 22 2006    		// Removed XML Loader for proprietary parser-loader <DD>
 * <DT>4.0 Mar 25 2007			// Added serialization to save the file during Build parsing. <DD>
 * <DT>5.0 May 17 2007			// Split into subclass and serialized this base class, the only 
 * 		part needed when playing. <DD>
 * <DT>5.1 May 20 2007			// Split into common package and placed here.<DD>
 * <DT>5.2 May 26 2007			// Added non-default serialization for better flexibility in 
 * 		writing attributes separately, expecting to encrypt long text blocks to keep user from 
 * 		reading answers in the datafile. <DD> 
 * <DT>6.0 Sep 29 2007			// Combine subclass InnBuilder into Inn.<DD>
 * <DT>6.1 Jul 3	2008 			// Final commenting for Javadoc compliance<DD>
 * </DL>
 */
public class Inn
{	
    /** The Innkeeper will end the game if this value is reached. */
    private final int REBUKE_LIMIT = 2;

    // Internal constants and references
    /**  Name of this dungeon (Inn is a 1-room dungeon) */
    private String _dgnName = null;			
    /** Description of this Dungeon when first entered. */
    private String _intro = null;           		
    /**  The main and only room in this Inn (so far) */
    private Room _mainRoom = null;     	
    /**  Counts the Patrons who have left negatively. */
    private int _rebukes = 0;					   	

    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

    /** This object's singleton reference. */ 
    protected static Inn _thisInn = null;
	
    /** Default constructor */
    private Inn() { }

    
    /** Creates an <code>Inn</code> singleton and sets the name. 
     * @param name  of the Dungeon (same as the Inn, for now)
     */
    private Inn(String name) 
    { 
    	_dgnName = name;
    } 

    
    /**
     * The Inn's real constructor, called by the SAX Parser.
     * @param name of the Inn
     */
    public static synchronized Inn createInstance(String name)
    {
        if (_thisInn == null) {
            _thisInn = new Inn(name);
        }
        return _thisInn;
    }


    /**
     * Obtains the reference to an existing <code>Inn</code> object. 
     * Displays an error message if the <code>Inn</code> doesn't already exist.
     */
    public static synchronized Inn getInstance()
    {
        if (_thisInn == null) {
           Dgn.debugMsg("The Inn object does not yet exist.");
        }
        return _thisInn;
    }

    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

    	/** 
    	 * Called by SAX Parser.
    	 * Adds a <code>Room</code> to the Inn; in this case, it just sets the 
    	 * <code>Room</code> to the only one in the Inn 
    	 * 
    	 * @param rm		<code>Room</code> to add to the Inn. 
    	 */
		public void addRoom(Room rm)
	    {
	    	_mainRoom = rm;
	    }

	    
	    /** 
	     * Displays the name and introduction to the dungeon when the Hero first enters.
	     * This method also calls the <code>look()</code> method of <code>Room</code>. 
	     */
	    public void firstLook()
	    {
	    	System.out.println("You have just entered " + _dgnName + ".");
	        System.out.println(_intro);
	    }

		
	    /** 
	     * Gets the current <code>Room</code> in which the Hero is standing.
	     * For this dungeon, there is only one room.
	     * 
	     * @return the current room in which the Hero finds himself
	     */
	    public Room getCurrentRoom()
	    {
	    	return _mainRoom;
	    }

	    
	    /** Gets the dungeon's name
	     * @return the name of the dungeon 
	     */
	    public String getName()
	    {
	    	return _dgnName;
    	}

		    
	    /** 
	    * Removes a Patron from the current Room, and checks if the rebuke limit has been
	    * reached. If so, the game is over so a <code>CmdQuit</code> is scheduled.
	    * 
	    * @param 	p		The patron to remove (who leaves the Room)
	    * @return	true if the Patron was removed for negative reasons; 
	    * 					false if Patron left for otherwise
	    */
	   public boolean removePatron(Patron p)
	   {
		   // Was Patron removed, or left on their own?
		   if (_mainRoom.remove(p) == true) {
	   			_rebukes++;
	   			if (_rebukes >= REBUKE_LIMIT) {
	   				System.out.println("The Innkeeper has ejected you from the Inn for scaring away his customers!");
	   				System.out.println("GAME OVER!");
	   				// Create and schedule a Quit command to end the game
	   				Scheduler sked = Scheduler.getInstance();
	   				sked.schedEndGame();
	   			}
	   			return true;
	   		}
		   else {
			   return false;
		   }
	   	}

	   
	    /** 
	     * Called by SAX Parser to assign the intro text into the Inn
	     * @param   intro	the text that is displayed the first time the Hero "Enters" the dungeon
	     */
	      public void setIntro(String intro) 		// not to be serialized; for loading only
	      {   
	          _intro = intro;
	      }


//	    /** 
//	     * For DEBUGGING: Display attributes of the Inn: Name, Intro text, and 
//	     * <code>Room</code> description.
//	     */
//	    private void dump()
//	    {
//        	Dgn.auditMsg("\nDUNGEON DUMP: ");
//	        Dgn.auditMsg(_dgnName);
//	        Dgn.auditMsg(_intro);
//	        _mainRoom.dump();
//	    }


}   // end of Inn class

