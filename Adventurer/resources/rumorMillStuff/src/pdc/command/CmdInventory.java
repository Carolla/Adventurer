/*
 * CmdInventory.java
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

import java.util.ArrayList;

/**
 *	Gets the amount of money the Hero currently has.
 *	<p>
*	Format: INVENTORY <br>
 * The command string is case-insensitive. See <code>init()</code> method.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT>1.0		 Jan 17 2008 			// original 			<DD>
 * <DT>1.1		Jul 4 2008				// Final commenting for Javadoc compliance<DD>
 * </DL>
 * @see Command
 */
public class CmdInventory extends Command
{
    // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
	/** Name and syntax of the command, used in <code>usage()</code> method. */
	static final String CMD_FORMAT = "INSPECT ( <Name> | INNKEEPER). ";
    /** The description of what the command does, used in the <code>help()</code> method. */
	static final String CMD_DESCRIPTION = "Check how much money the Hero has available.";
	/** The command starts immediately, requiring no delay. */
    static final int DELAY = 0;
	/** The time neded to to rummage through his pockets. */
    static final int DURATION = 5; 

    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
    	
    /** Constructor called by the Class class in the CommandFactory. 
     * There are no parms, so the super constructor takes a null as last argument. 
     */
    public CmdInventory() 
    {
        super("CmdInventory", DELAY, DURATION, CMD_DESCRIPTION, null);
    }
    
    	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

    /** Verifies that there are no parms following the Command. 
     * 
     * @param	args		no parms are needed; implemented as required for abstract method 
     * @return	true 	if no parms are supplied
     */
    public boolean init(ArrayList<String> args)
    {
		if (args.size() == 0) {
			return true;
		}
		else {
			usage();
			return false;
		}
    }

    		
    /** 
     * Displays the amount of money that the Hero has (in gold pieces).
     * .  
     * @return	true if command works, else returns false
     */
    public boolean exec() 
    {
        Hero myHero = Hero.createInstance();
        if (myHero != null) {
        	Dgn.userMsg ("Hero has " + myHero.checkMoney() + " gold pieces.");
        	return true;
        }
        else {
        	Dgn.debugMsg("Can't find Hero here!");
        	return false;
        }
    }
    
}	// end of CmdInventory class
 
