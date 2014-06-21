/**
 * 	CommandParser.java
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

import rumorMillStuff.src.pdc.command.Command;
import rumorMillStuff.src.pdc.command.CommandFactory;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *	Receives a user input string from the command window and converts it to a command 
 * object, which it to the <code>Scheduler</code> for invocation.	
 * <code>Scheduler</code> calls <code>CmdParser</code> when it needs 
 * a new user command.
 * @author		Alan Cline
 * @version <DL>
 * <DT>1.0		Sep 1 2006			// Split application into HIC, PDC, DMC components<DD>
 * <DT>2.0		Jun 5 2007			// Added runtime loop (CmdParser and CmdFactory)<DD> 
 * <DT>3.0		Oct 26 2007		// Revised when DgnRunner and DgnBuild were merged. <DD>
 * <DT>4.0		Jan 1 2008			// Move all commands into command package, and adjust help() accordingly <DD>
 * <DT>4.1		Apr 11 2008		// Added windowing connection via Dgn static field <DD>
 * <DT>4.2		May 30 2008		// Added non-modal resizable Help window <DD>
 * <DT>4.3 	Jul 1 2008	 		// Final commenting for Javadoc compliance<DD>
 * </DL>
 */
public class CommandParser
{
    /** 
     * Command lexicon is sorted alphabetically by command string for easier user readability.
     * The first entry is the (case-insensitive) command String from the user, 
     * the second entry is the <code>CmdToken</code> that creates the 
     * <code>Command</code> object. <br>
     *	NOTE: If the command table gets long, then sort in order of probablity for efficiency 
     * (current look-up algorithm is linear). 
     */
    private final String[][] _cmdTable = {
            { "BRIBE", "CmdBribe" },				// Force a positive message from the Patron	
            { "BUY", "CmdBuy" },					// Buy food or drink for a Patron, or for all	
            { "EXIT", "CmdQuit" },				// End the program	
            { "HELP", "CmdHelp" },				// List the user command names and their descriptions.	
            { "INSPECT", "CmdInspect" },			// Give a close-up description of the Person requested	
            { "INVENTORY", "CmdInventory" },		// Describe the money the Hero has (later, this will tell the items too)	
            { "LOOK", "CmdLook" },				// Give a description of the Room and any People inside it.	
            { "ORDER", "CmdBuy" },					// Buy food or drink for a Patron, or for all	
            { "QUIT", "CmdQuit" },				// End the program.
            { "TALK", "CmdTalk" }, 				// Talk to a Person, either by Name or by Patron Number.
            { "WAIT", "CmdWait" }, 				// Wait a specific amount of time, in hours or minutes.
    };

    /** Internal reference: command-specific data arguments */
    private ArrayList<String> _parms = null;
    /** Internal reference: the factory that creates <code>Command</code> objects. */
    private CommandFactory _cmf = null;
    /** Internal reference: the user input string for the command */
    private String _userInput = null;

    // Special cases
    /** Error message if command cannot be found. */
    private final String CMD_ERROR = "I don't understand. Try again.";
    /** Identify a command string in which only a return key is entered. */
    private final String CMD_EMPTY = "";

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 								CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Internal reference to ensure singleton object. */
    private static CommandParser _cp = null;

    /** 
     * Private constructor which also creates an ArrayList for command-specific parms sent to 
     * a Command, and a <code>CommandFactory</code>.
     */
    private CommandParser()
    {
        _parms = new ArrayList<String>();
        _cmf = new CommandFactory();
    }

    /**  
     * Creates or returns the singleton <code>CommandParser</code> reference. 
     * A <code>CommandFactory</code> is created as part of this class to funnal activity
     * through <code>CommandParser</code>.
     * See <code>getInstance()</code> method when reference is known to exist.
     * 
     * @return Instance of the newly created <code>CommandParser</code>
     */
    public static synchronized CommandParser createInstance()
    {
        if (_cp == null) {
            _cp = new CommandParser();
            if ((_cp == null) || (_cp._parms == null)) {
                Dgn.debugMsg("Trouble creating CommandParser object.");
                _cp = null;
            }
        }
        return _cp;
    }

    /**
     * Obtains an existing reference to the <code>CommandParser</code> singleton without 
     * creating it. Lightweight version of <code>createInstance()</code> method.
     * 
     * @return previously created <code>CommandParser </code>reference.
     */
    public static synchronized CommandParser getInstance()
    {
        if (_cp == null) {
            Dgn.debugMsg("The command parser object does not yet exist.");
        }
        return _cp;
    }

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 								PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /**
      * Collects user input and converts the first token into a <code>Command</code> token. 
      * Sends the <code>Command</code> token to the <code>CommandFactory </code>
      * to create the specific <code>Command</code> subclass,
      * and passes the remainder of the parms to the command for further parsing. 
      * This method is called as part of the <code>Scheduler</code>'s loop.
      * 
      * @return 	one of the many user command subclasses available
      */
    public Command getUserCommand()
    {
        String cmdToken = null;		// name of the command from the command table
        Command cmd = null;			// command object created by the CommandFactory
        String cmdString = null;		// the input line from the console that contains the name of the command as first token

        // Get user command until a valid command string is found; break on Quit command
        while (true) {
            if (_userInput == null) {
                //Wait for user input
                continue; //FIXME: Busy waiting
            }
            // Pull off first token and load _parms with remaining command arguments (if any)
            cmdString = parse(_userInput);
            // Clear the user input of its command string
            _userInput = null;

            // Get first token and check it against the command table
            cmdToken = lookup(cmdString);
            // If command cannot be found, ask user to try again
            if (cmdToken == null) {
                Dgn.auditMsg(CMD_ERROR);
                continue;
            }
            // If command was something else, then create it and return to scheduler
            else {
                cmd = _cmf.createCommand(cmdToken);
                if (cmd == null) {
                    Dgn.debugMsg(cmdToken + " command could not be created.");
                    continue;
                }
                if (cmd.init(_parms) == true) {
                    break;
                }
            }
        }
        return cmd;
    }

    /** 
     * Traverses the command table and displays the command names, and their description,
     * in a non-modal <code>HelpWindow</code> as a reminder to the user. 
     *	 This method is called by <code>CmdHelp.exec()</code>.
     */
    public void help()
    {
        // Conversion from line of text to pixel height for window
        final int LINE_HEIGHT = 24;
        final int CHAR_WIDTH = 7;

        // Create the Help dialog
        HelpDialog helpWin = HelpDialog.createInstance(MainFrame.getInstance());

        // Identify indexes for convenience
        final int NAME = 0;
        final int CMD = 1;

        // Walk the command table, creating the Commands to get their descriptions
        int longestDesc = 0;
        int nbrCmds = 0;

        // Display the text for the help command, counting number of commands and longest description length
        helpWin.append("\n ");
        for (int k = 0; k < _cmdTable.length; k++) {
            String cmdString = _cmdTable[k][NAME];
            Command cmd = _cmf.createCommand(_cmdTable[k][CMD]);
            String desc = cmd.getDescription();
            longestDesc = longestDesc > desc.length() ? longestDesc : desc.length();
            nbrCmds++;
            helpWin.append("  " + cmdString + "\t" + desc + "\n");
        }
        helpWin.append("\n ");
        helpWin.lockText(true);		// Lock the window to disallow redundant messaging

        // FInd how large to make the Help window
        int height = nbrCmds * LINE_HEIGHT;
        int width = (longestDesc + 12) * CHAR_WIDTH;
        helpWin.setSize(width, height);

        helpWin.setVisible(true);
    }

    /** 
     * Receives and holds the command string from the windows command window.
     * It will be retrieved from the <code>Scheduler </code>when it is ready for another user 
     * command.
     *  
     * @param cmdIn		the input the user entered as a command
     */
    public void receiveCommand(String cmdIn)
    {
        // Pass the command line into the parser to generate the command token
        _userInput = cmdIn;
    }

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 								PRIVATE METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** 
     * Searches through the command table for the first word in the user input string and 
     * returns the canonical <code>Command </code> name associated with it.
     * 
     * @return	canonical name of the <code>Command</code> (concrete subCommand) 
     * to <code>Schedule</code>, or null.
     */
    private String lookup(String cmdString)
    {
        // Walk the command table. When found, break from loop.
        String cmdToken = null;
        for (int k = 0; k < _cmdTable.length; k++) {
            if (cmdString.equalsIgnoreCase(_cmdTable[k][0])) {
                cmdToken = _cmdTable[k][1];
                break;
            }
        }
        return cmdToken;
    }

    /**
     * Tokenizes the input line into its tokens and parms list.
     * 
     * @param ipLine		String taken in from command window
     * @return <code>Command</code> token, which is always the first token, 
     * and saves parms list (if any) for later. 
     */
    private String parse(String ipLine)
    {
        String cmdToken = null;
        if (ipLine == null) {
            return null;
        }
        // Clear out parms from command line before adding new ones.
        _parms.clear();
        // Check for an empty carriage return entered first;
        // StringTokenizer throws exception on it
        if (ipLine.equalsIgnoreCase(CMD_EMPTY)) {
            return CMD_EMPTY;
        }
        // StringTokenizer class defaults to whitespace as delimiter
        StringTokenizer line = new StringTokenizer(ipLine);
        // Get first token and store for later return
        cmdToken = line.nextToken();

        while (line.hasMoreTokens()) {
            _parms.add(line.nextToken());
        }
        return cmdToken;
    }

}	// end of CommandParser class

