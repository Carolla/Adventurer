/*
 * Hero.java
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

import rumorMillStuff.src.hic.MainFrame;

import mylib.pdc.MetaDie;


/**
 *	Plays the user's role as adventurer--the major player character. 
 * He or she has only charisma and money for now.
 * 
 * @author Alan Cline
 * @version <DL>
 * <DT>1.0	Nov 5	2005			// Original <DD>
 * <DT>2.0 Jan 1	2007			// Add Hero to newest version of program <DD>
 * <DT>2.1 Jan 16 2008			// Update to support Bribe command <DD>
 * <DT>1.1 Jul 2	2008 			// Final commenting for Javadoc compliance<DD>
 * </DL>
 */
public class Hero
{
    // Static Defaults and Constants for the Constructor
	/** Default friendliness and likeability of the Hero. */
	static final int CHARISMA = 10;
	/** Default money for the Hero. */
    static final int DEFAULT_MONEY = 30;	
    
    // Hero attributes
	/** Current friendliness and likeability of the Hero, although this will not change. */
    private int _charisma;
	/** Current money for the Hero. */
    private int _money;
     
//    MetaDie _dice = new MetaDie(1007L); 			// for debugging
    /** Randomizer that can simulate rolling one or more dice, and summing then if requested. */
    MetaDie _dice = new MetaDie();					// with debugging off		 

	/** The Character for the Player to interact with the game. */
	private static Hero _hero = null;

	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
    
    /** The Hero starts with a Charisma factor and some money. */
    private Hero() 
    {
        _charisma = Hero.CHARISMA;
        _money = Hero.DEFAULT_MONEY;
    }


    /**
     * Creates or obtains an existing reference to the Hero object.
     * @return		Hero reference
     */
    public static synchronized Hero createInstance()
    {
        if (_hero == null) {
        	_hero = new Hero();
        }
        return _hero;
    }

    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
    
    /** Gets the Hero's money. Currency is in gold pieces (gp), but later will
     * be gold pieces and silver pieces (1 gp = 10 sp).
     * 
     * @return the number of gold pieces Hero owns. */
    public int checkMoney()
    {
        return _money;
    }

    
    /** 
     * Checks if the given affinity and the Hero's charisma beat a random number by  
     * rolling a <tt>d20</tt> against Hero's charisma. Random number between 1 and 20,
     * inclusive, plus given affinity is  compared against Hero's Charisma.
     * 
     * @param affinity		the Person's friendliness toward the Hero
     * @return 	true if (random number + afinity)  &le Hero's charisma; considered a success
     */
    public boolean rollCharisma(int affinity )
    {
    	return  (_dice.roll(1,20) < (_charisma + affinity));
    }

    
    /** Decrements the Hero's money by the requested amount.
     * Send update message to status window.
     *  
     * @param amount	spent to reduce the Hero's cash
     * @return  true if the Hero has that amount; else false if requested amount was too much
     */
    public boolean spendMoney(int amount)
    {
    	if (amount <= _money) {
    		_money -= amount;
    		// Update the status display each time the money changes
    		MainFrame mf = MainFrame.getInstance();
    		mf.updateStatus();
    		return true;
    	}
    	else {
    		return false;
    	}
    }


}	// end of Hero class
