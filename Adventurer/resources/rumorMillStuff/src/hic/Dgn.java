/**
 * Dgn.java
 *
 * Copyright (c) 2007, Carolla Development, Inc. All Rights Reserved.
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com.  
 */

package rumorMillStuff.src.hic;

import rumorMillStuff.src.pdc.Inn;
import rumorMillStuff.src.pdc.Scheduler;

import java.awt.EventQueue;

import javax.swing.JFrame;


/** TODO: BUG: Fix premptive quit when second Patron retorts. Allow time to see end of 
 * program message. CmdQuit should return naturally back through Scheduler, and not
 * call windows quit() directly. */
/** TODO: Trim white space in Patrons' messages. */
/** TODO: Set the main program window frame title when running outside Eclipse environment. */
/** TODO: Call CmdInspect the first time Hero talks to a Patron. */
/** TODO: Get Hero's name during Innkeeper dialogue and add to Hero object. */
/** TODO: Use normal distribution instead of linear distribution when setting the amount of
 * time that a Patron stays in the Inn. Patron's time should cluster about 30 minutes.
/** TODO: Check DungeonContentLoader for redundant near and far flags. */


/**
 * This is the main module for the program. It contains global static data and the 
 * <code>main()</code> method. 
 * Does the following needed to init and run the <code>Dgn</code> program. 
 * <BL>
 * <LI> Checks the command line arguments for the XML data file and XML Schema file. 
 * 		The input file for the dungeon must have both an .xml and an .xsd file of the same
 * 		name. </LI>
 * <LI> Starts the Swing thread <code>EventQueue</code> and creates a splash window 
 * 		for initializing*. The data file loading and SAX Parsing 	occurs while the Splash screen
 * 	 	is showing. </LI>
 * <LI> Creates the main window system, the Inn (dungeon program itself), and the
 *		<code>Scheduler</code>, which will run under its own thread. The Swing
 * 		<code>EventQueue</code> loops while the <code>Schedule</code> thread
 * 		loops, and they exchange commands and window updates in the 
 * 		<code>CommandParser</code>. </LI>
 * </BL>
 * <p>
 * *Implementation Note: Creates the <code>mainFrame</code> and passes control to it. 
 * All Swing processing occurs from the single <code>EventQueue</code> thread. 
 * Unlike older versions of Java, Swing must now be invoked inside an 
 * <code>EventQueue Runnable</code>.  As "Core Java", p287 states: "For now, you 
 * should simply consider it a magic incantation that is used to start a Swing program."
 * <p>
 * This version has been updated for SE 6.0, and in accordance with 
 * <I>Core Java, Vol 1</I>, 8/e, by Cay Horstmann and Gary Cornell, Sun MicroSystems Press, 2008.
 *  @author Alan Cline
 *  @version <DL>
 *  <DT> 1.0 	May 26 2007		// original version <DD>
 *  <DT> 2.0 	Sep 29 2007		// DgnBuild and DgnRunner joined to make single-pass 
 *													program. <DD>
 *  <DT> 2.1 	Feb 19 2008		// Add introduction when Hero first enters Inn. <DD>
 *  <DT> 3.0 	Feb 23 2008		// Added main window and splash screen around console 
 *  												version of Rumor Inn program. <DD>
 *  <DT> 3.1	Feb 29 2008		// Rearranged for most of file I/O to occur in DgnBuild; made 
 *  												Dgn object a singleton with callbacks. <DD>
 *  <DT> 4.0	Mar 23 2008		// Updated for SE 6.0, and a little multi-threading, particularly 
 *  												to follow Swing's "strict single-thread rule."<DD>
 *  <DT> 4.1	Mar 29 2008		// Moved singleton init methods into DgnBuild.<DD>
 *  <DT> 4.2	May 21 2008		// Add different threads for Scheduler and MainFrame to cycle 
 *  												asynchronously.<DD>
 *  <DT> 4.3 	Jun 29 2008 		// Final commenting for Javadoc compliance<DD>
 * </DL>
 */
public class Dgn
{
	/** Standard copyright notice for About box and Splash screen. */
	public static final String COPYRIGHT = 
			"Copyright (c) 2008 by Carolla Development, Inc.   All Rights Reserved.";  

	/** Flag to turn on debug messages */
	public static boolean DEBUG = false;
	  
	/** Flag to turn on audit messages.  */
	public static boolean AUDIT = false;      

	/** Common directory for resources */
	public static final String COMMON_FILEBASE = "/Projects/workspace/RumorMill/";    
	/** Base directory for input files */
	public static final String INPUT_FILEBASE = "src/resourceIn/";    
		
	/** XML input file, not passed through the windowing system, but picked up by the SAX loader later */
	public static String dataFile = null;    
	/** XML Schema input file, not passed through the windowing system, but picked up by the SAX loader later */
	public static String schemaFile = null;    

	/** Boolean equivalent to true for some readability uses. */
	public static final boolean POSITIVE = true;      
	/** Boolean equivalent to false for some readability uses. */
	public static final boolean NEGATIVE = false;      
	  
	/** String constants, differentiated from the Dgn boolean constants */
	public static final String STR_POSITIVE = "positive";
	/** String constants, differentiated from the Dgn boolean constants */
	public static final String STR_NEGATIVE = "negative";

	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								STATIC MAIN METHOD
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
	
	/** 
	 * Verifies the command line arguments on the <code>main()</code> thread, 
	 * then starts a separate Swing thread for the GUI's <code>EventQueue</code>, 
	 * per SE 6.0 and Swing's "strict single-thread rule".
	 * <p>
	 * Implementation Note: Do not put a <code>System.exit()</code> statement in 
	 * <code>main()</code> after the GUI starts because it shuts down the windowing 
	 * threads too.
	 * 
	 * @param	args 	command line arguments: See <code>verifyArgs()</code> method. 
	 */
	public static void main(String[] args)
	{
		// Create the Dgn singleton for non-static methods and later reference
		_dgn = Dgn.getInstance();

		if (_dgn.cmdLineChecks(args) == false) {
			System.exit(-1);		// exit on error
		}

        /* Creates the main frame and passes control to it. All Swing processing occurs from 
        * the single EventQueue thread. Unlike older version of Java, Swing must now be 
        * invoked inside an EventQueue Runnable. As "Core Java", p287 states: "For now, you 
        * should simply consider it a magic incantation that is used to start a Swing program."
        */
        EventQueue.invokeLater(new Runnable()
        {
    		public void run()
    		{	
				// Open splash screen and init the domain engine.
				// Warning: Init process needs the window system object reference
				SplashInitWindow splash = new SplashInitWindow();
				splash.setVisible(true);
					
				// Create the dungeon title for the main windowing system 
		    	Inn thisInn = Inn.getInstance();
		    	if (thisInn == null) {
		    		Dgn.debugMsg("Inn not yet created.");
		    		System.exit(-1);
		    	}

		    	// Create the main windowing system
		    	String title = thisInn.getName();
    			MainFrame frame = new MainFrame(title);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				
		    	// Show introduction to the dungeon when Hero first enters, then look around the room. 
		    	thisInn.firstLook();

		    	// Start new thread to start the Scheduler to retrieve and execute commands
		    	Runnable sched = Scheduler.getInstance();
		    	if (sched == null) {
		    		Dgn.debugMsg("Scheduler not yet created.");
		    		System.exit(-2);
		    	}
		    	Thread t = new Thread(sched);
		    	t.start();
    		}
    	});
	}	// end of static main()

	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
		
	/** Internal: this object's singleton reference. */  
	private static Dgn _dgn = null;

	/** Private constructor to ensure a singleton */
	private Dgn() { }
 

	/** 
	 * Creates the <code>Dgn</code> object if it doesn't exist, else returns reference.  
	 * @return the framing singleton object. 
	 */
	public static synchronized Dgn getInstance()
	{
		if (_dgn == null) {
			_dgn = new Dgn();
		}
		return _dgn;
	}

	
/* +++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 												PUBLIC STATIC METHODS
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	/**
	*  	Prints an audit message to stderr if AUDIT flag set to true.
	* @param msg   Message to be displayed
	 */
	public static void auditMsg(String msg)
	{
	    if (Dgn.AUDIT == true) {
	        System.err.println(msg);
	    }
	}
		

	/**
	 *  	Prints an error or debug  message to stderr if DEBUG flag set to true.
	 *  This method also prepends the word "DEBUG" to highlight the error message.
	 *  
	 * @param msg   Message to be displayed
	 */
	public static void debugMsg(String msg)
	{
		if (DEBUG == true) {
			System.err.println("DEBUG: " + msg);
	    }
	}

	
	/**
	  *  Prints an error message to the user.
	  *  This method also prepends the word "ERROR" to highlight the error message.
	  *  
	  * @param msg   Message to be displayed
	  */
	public static void errMsg(String msg)
	{
		System.err.println("ERROR: " + msg);
	}
	

	/**
	 *  	Displays a message to the user. These messages are normal and allow user interaction.
	 *  This method is a simple wrapper for <code>System.out</code>. It is redirected to
	 *  the output portion of the main window.
	 *   
	 * @param msg   Message to be displayed
	 */
	public static void userMsg(String msg)
	{
		System.out.println(msg);
	}

	
/* +++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 														PRIVATE METHODS
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	/**
     * Verifies the command line flags and sets the resource files' location;
     * calls the <code>usage()</code> method if the command line is invalid.
     * 
     * @param args		the command line flags: dungeon file name, audit and debug parms
     * @return true if flags are valid and resource files are defined.
     */
    private boolean cmdLineChecks(String[] args)
    {
        // Verify the proper number and type of command line arguments
    	if (_dgn.verifyArgs(args) == false) {
    		_dgn.usage();
    		return false;
    	}
	  
    	// Define the location of the dungeon data, and set the data for the parsing object
        String dgnTitle = args[0];
        schemaFile = INPUT_FILEBASE + dgnTitle + ".xsd";
        dataFile = INPUT_FILEBASE + dgnTitle + ".xml";
        return true;
    }
	    
	     
    /** Displays the usage statement if the command line invocation is incorrect. */
    private void usage()
    {
        System.err.println("USAGE: Dgn InnName -a -d ");
        System.err.println("where -a turns on audit messages; -d turns on debug messages.");
        System.err.println("Do not use the .xml or .xsd file suffixes with the Inn's name.");
    }

    
    /**
     * Verifies the proper number and type of command line arguments. 	    	
	 * If the dataname is incorrect, the file will not be read and returns an error. 
	 * Verifies and sets the audit and debug flags.
     * @param	args 	1 to 3 command line arguments <br>
     * 			args[0]		the name of the Inn, without sufffix, which also names the .xml and 
     * 								.xsd input files; <br>
     * 			args[1]		an optional flag (-a) to turn on audit messages;<br>
     * 			args[2]		an optional flag (-d) to turn on debug messages; <br>
     *  The flags may appear in any order, if at all, and one or both may be present.
     */
    private boolean verifyArgs(String[] args)
    {
    	// Set flags to default
    	AUDIT = false;
    	DEBUG = false;
    	boolean retflag = false;
    	
    	// Verify the proper number of parms:
    	// 	exactly 1, 2 or 3 parms are valid, but the parms must be checked for validity
    	if ((args.length < 1) || (args.length > 3)) {
    		return false;
		}

    	// This is a fancy switch statement. It traverses once, testing for only true cases. 
    	// When test cases are exhausted, the code falls out at bottom and returns retflag.. 
    	while (true)
    	{
	    	// One-parm command line; there is nothing to check further
	    	if (args.length == 1) {
	    		retflag = true;
	    		break;
	    	}

	    	// Two-parm command line
	    	else if (args.length == 2) {
		    	// Check that only the audit flag is requested
	    		if (args[1].equalsIgnoreCase("-a")) {
	    			AUDIT = true;
	    			retflag = true;
	    			break;
	    		}
	    		// Check that only the debug flag is requested
	    		if (args[1].equalsIgnoreCase("-d")) {
	    			DEBUG = true;
	    			retflag = true;
	    			break;
	    		}
	    	}

	    	// Three-parm command line; check for both flags requested
	    	else if (args.length == 3) {
	    		if ((args[1].equalsIgnoreCase("-a")) || (args[2].equalsIgnoreCase("-a")))  {
	    			AUDIT = true;
	    		}
	    		if ((args[1].equalsIgnoreCase("-d")) || (args[2].equalsIgnoreCase("-d")))  {
	    			DEBUG = true;
	    		}
	    		if (AUDIT && DEBUG) {
	    			retflag = true;
	    		}
	    		// whether parms are good or not, break from forever loop now
    			break;
	    	}
    	}		// end of forever loop

    	// If method hasn't returned by now, parms weren't valid
    	return retflag;
    }
	    

}		// end of common Dgn class
