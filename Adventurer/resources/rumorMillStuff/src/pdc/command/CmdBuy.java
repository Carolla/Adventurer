/**
 * CmdBuy.java
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
import rumorMillStuff.src.pdc.Innkeeper;
import rumorMillStuff.src.pdc.Patron;

import java.util.ArrayList;


/**
 * Buy drinks or food for himself (the Hero), a Patron or everyone in the Inn. 
 * Everyone else who receives drink or food increases their affinity value for the Hero.
 * The Innkeeper's affinity increases by 1 for each Patron in the Inn.  
 * Hero must have enough money for the purchase for this command to work.
 * <p>
 *	<br> Format: BUY (DRINKS | FOOD) [FOR (Name | ALL)]  <br>
 * The command string is case-insensitive. See <code>init()</code> method.

 * @author Alan Cline
 * @version <DL>
 * <DT>1.0 		Jan 25 2008 		// original <DD>
 * <DT>1.1 		Feb 16 2008 		// added buyAll option <DD>
 * <DT>1.2 		Feb 18 2008 		// reformatted syntax; added the Hero buying for himself <DD>
 * <DT>1.3	 		Jul 4 2008	 		// Final commenting for Javadoc compliance<DD>
 * </DL>
 * @see Command
*/
public class CmdBuy extends Command
{
	    // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN CONSTRUCTORS. 
		/** The command syntax, used in the <code>usage()</code> method. */
		static final String CMD_FORMAT = "BUY (DRINKS | FOOD) [FOR (Name | ALL)] ";
	    /** The description of what the command does, used in the <code>help()</code> method. */
		static final String CMD_DESCRIPTION =  "Buy drinks or food for a Patron, or for everyone.";
		/** Time (in seconds) needed to order drinks (before the command starts). */
		static final int DRINK_DELAY = 10;									
		/** Time (in seconds) consumed to get the drinks (game clock increments this amount). */
		static final int DRINK_DURATION = 60;							
		/** Time (in seconds) needed to place a food order (before the command starts). */
		static final int FOOD_DELAY = 30;									
		/** Time (in seconds) consumed to get the food (game clock increments this amount). */
		static final int FOOD_DURATION = 300;						

		// Other constants
		/** Drink(s) or Food when buying only for the Hero */
	    private final int MIN_ARGS = 1;						
	    /** If FOR is used, then a Patron's name (or ALL) must follow */
	    private final int MAX_ARGS = 3;						
	    /** Drinks cost 1 gp.. */
	    private final int DRINK_COST = 1;					
	    /**  ...to increase affinity by 1. */
	    private final int DRINK_AFFINITY = 1;			
	    /** Food costs 6 gp... */
	    private final int FOOD_COST = 6;					
	    /** ...to increase affinity by 3. */
	    private final int FOOD_AFFINITY = 3;				
	    /** Innkeeper always gets 1 pt per Patron being served */
	    private final int INNKEEPER_AFFINITY = 1;	

	    /** Who is receiving the purchase? */
	    private String _patronName = null;			
		/** If Hero is buying for all, set to true */
	    private boolean _allFlag = false;
		/** Default price until set by product bought */
	    private int _price = 0;					
		/**  Default affinity until set by product bought */
	    private int _affin = 0;									

	    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	    /** Default constructor, used by CommandFactory.
	     * The attributes must be overridden based on the command arguments
	     */
	    public CmdBuy() 
	    {
	        super("CmdBuy", 0, 0, CMD_DESCRIPTION, CMD_FORMAT);
	    }


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	    /** 
		 * Checks that the parms for this command are valid. The actual Person cannot be 
		 * retrieved until the command is executed because the current Room and Person are 
		 * time-dependent. 
	     *	<br> Format: BUY (DRINKS | FOOD) [FOR (Name | ALL)]  <br>
	     * The command string is case-insensitive.
	     * 
	     * @param	args 	args[0] = the keyword DRINK[S] or FOOD; without more parms, applies to the Hero
	     * 								args[1] =  the keyword FOR
	     * 								args[2] = if FOR is given, then a Patron's Name, or the keyword ALL, is required
	     * @return 	true if parm list is valid
	     */
	    public boolean init(ArrayList<String> args) 
	    {
	    	String parm = null;		// transient local

	    	// Check that the number of parms are either one of two correct choices
	    	int nbrParms = args.size();
	    	if ((nbrParms != MIN_ARGS) && (nbrParms != MAX_ARGS)) {
	    		usage();
	    		return false;
	    	}
	    	// First parm must be the keyword DRINKS or FOOD; Singular "Drink" is also acceptable
	    	parm = (String) args.get(0);
	    	if ((parm.equalsIgnoreCase("DRINKS")) || (parm.equalsIgnoreCase("DRINK"))) {
	    		// Set the delay and duration accordingly
	    		_delay = DRINK_DELAY;
	    		_duration = DRINK_DURATION;
	    		_price = DRINK_COST;
	    		_affin = DRINK_AFFINITY;
	    	}
	    	else if (parm.equalsIgnoreCase("FOOD")) {
	    		// Set the delay and duration accordingly
	    		_delay = FOOD_DELAY;
	    		_duration = FOOD_DURATION;
	    		_price = FOOD_COST;
	    		_affin = FOOD_AFFINITY;
	    	}
	    	else {
	    		usage();
	    		return false;
	    	}

	    	// If there are MIN_ARGS parms, this init() is complete: The Hero is the recipient
	    	if (nbrParms == MIN_ARGS) {
		    	_patronName = null;				// Hero is not a patron
		    	_allFlag = false;						// Hero is not buying for everyone
	    		return true;		
	    	}
	    	
	    	// If there are MAX_ARGS parms...find who is the recipient (or if everyone is)
	    	if (nbrParms == MAX_ARGS) {
	    		// Second parm must be the keyword FOR or else assume Patron's name
	    		parm = (String) args.get(1);
		    	if (parm.equalsIgnoreCase("FOR"))  {
		    		// Next parm must be keyword ALL or assume Person's name
			    	parm = (String) args.get(2);			// looking for keyword ALL
			    	if (parm.equalsIgnoreCase("ALL")) {
				    	_patronName = null;
				    	_allFlag = true;
				    	return true;				// end of case 
			    	}
			    	else {			// Assume next parm is Patron's name
			    		_patronName = parm;
			    		_allFlag = false;
			    		return true;
			    	}
		    	}
	    	}
	    	// If you got here, syntax is incorrect
    		usage();
    		return false;
		}
	    

        /**
         * If the Hero has enough money, he can buy food or drinks for himself, a Patron, or 
         * everyone, and increase the Patron's and Innkeeper's affinity value.
         *  
         * @return	true if command works, else returns false
         */
        public boolean exec() 
        {
        	// if Hero is buying for himself
        	if ((_patronName == null)  && (_allFlag == false)) {
        		return buySelf();
        	}
        	// If buying for all, no need to check for specific patron
        	if (_allFlag == false) {
	        	// Check if the specified Person is present if not buying for all
	        	if ((_talkee = getTalkee(_patronName)) == null) {
	        			System.out.println("Can't find that person to talk with");
	        			return false;
	        	}
	    		// The Innkeeper does not take food or drinks for himself
	        	if (_talkee instanceof Innkeeper) {
	        		System.out.println("I have all I need. How 'bout buying for someone else instead?, Bork says.");
	        		return false;
	        	}
	        	// Buy the product specified for the Patron
	        	else {
	        		return buy((Patron)_talkee);
	        	}
        	}
        	// Buying food or drinks for all Patrons
        	else  {
        		return buyAll();
        	}
       	} 

        
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PRIVATE METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

        /** 
         * Buys food or drink for the specified Patron and increases his/her affinity according to 
         * the product. Increases the Innkeeper's affinity only by 1, the number of Patrons being served.
         * The Hero must be able to afford the purchase for the command to work.
         * 
         * @param p			the Patron being served
         * @return true 	if the purchase was completed; else false 
         */
        private boolean buy(Patron p)
        {
        	// Checking purchase price
        	Hero myHero = Hero.createInstance();
        	if (myHero.spendMoney(_price) == false) {
        			System.out.println("You don't have enough money!");
        			return false;
        		}
        	// Set the Patron busy so he or she doesn't leave prematurely
    		_curRoom.setBusy(p);
        	p.adjustAffinity(_affin);
        	System.out.println(p.getName() + "[" + p.getAffinity() + "]: Oh, thanks! I can really use that!");
        	// Increase the Innkeeper's affinity because of the sale. It is independent of product sold.
        	Innkeeper owner = _curRoom.getInnkeeper();
        	owner.adjustAffinity(INNKEEPER_AFFINITY);
        	return true;
        }

        
        /** 
         * Buys food or drink for all Patrons in the Room and increases their affinity according to 
         * the product. Increases the Innkeeper's affinity by 1 for each Patron served.
         * The Hero must be able to afford the purchase for the command to work.
         * 
         * @return true 	if the purchase was completed; else false 
         */
        private boolean buyAll()
        {
        	int nbrPatrons = _curRoom.getNumberPatrons();
        	if (nbrPatrons == 0) {
        		System.out.println("There are " + nbrPatrons + " Patrons in the Inn right now.");
        		return true;
        	}
    		// Get the Innkeeper and Hero 
    		Innkeeper owner = _curRoom.getInnkeeper();
        	Hero myHero = Hero.createInstance();
        	// Checking purchase price
        	int fullPrice = nbrPatrons * _price; 
        	if (myHero.spendMoney(fullPrice) == false) {
    			System.out.println("You don't have enough money!");
        		// Give all Patrons a decrease in their affinity
    			_curRoom.adjustAllAffinities(_affin * -1);
    			System.out.println("All the people Boo!, including the Innkeeper!");
    			return false;
       		}
        	else {
        		System.out.println("All the people cheer, including the Innkeeper!");
        		// Give all Patrons an increase in their (positive) affinity
        		_curRoom.adjustAllAffinities(_affin);
        		// Give the Innkeeper an increase 
        		owner.adjustAffinity(INNKEEPER_AFFINITY*nbrPatrons);
        		System.out.println(owner.getName() + "[" + owner.getAffinity() + "]: The Innkeeper smiles broadly.");
        		return true;
            }
        }


        /** 
         * Buys food or drink for the Hero, and increases the Innkeeper's affinity by 1. 
         * The Hero must be able to afford the purchase for the command to work. If Hero does
         * not have enough money, decreases Innkeeper's affinity.
         * 
         * @return true 	if the purchase was completed; else false 
         */
        private boolean buySelf()
        {
        	// Get primary players
        	Hero myHero = Hero.createInstance();
        	Innkeeper owner = _curRoom.getInnkeeper();
        	if ((myHero == null) || (owner == null)) {
        		Dgn.debugMsg("Can't find Hero or Innkeeper here!)");
        		return false;
        	}

         	// Ensure that no one is busy if the Hero is buying for himself
    		_curRoom.setBusy(null);

           	// Increase the Innkeeper's affinity because of the sale. It is independent of product sold.
    		if (myHero.spendMoney(_price) == true) {
    			System.out.println("Hero says: Aaaaaahh! That's good!");
	        	owner.adjustAffinity(INNKEEPER_AFFINITY);
	        	System.out.println(owner.getName() + "[" + owner.getAffinity() + "]: Good doing business with you.");
        	}
    		// Decrease the Innkeeper's affinity if the Hero does not have enough money.
    		else if (myHero.spendMoney(_price) == false) {
       			System.err.println("You don't have enough money!");
               	owner.adjustAffinity(INNKEEPER_AFFINITY * -1);
            	System.out.println(owner.getName() + "[" + owner.getAffinity() + "]: Get on wit' you, and bother someone else, you penniless beggar!");
        	}
    		// In either case, this command worked
        	return true;
         }

        	    	    
}		// end of CmdBuy class
