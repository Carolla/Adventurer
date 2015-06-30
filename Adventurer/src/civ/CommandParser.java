/**
 * CommandParser.java
 * 
 * Copyright (c) 2007, Carolla Development, Inc. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, post on servers, to reuse, or to redistribute to lists, requires prior
 * specific permission and/or a fee. Request permission to use from Carolla Development, Inc. by
 * email: acline@carolla.com.
 */

package civ;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pdc.command.Command;
import pdc.command.CommandFactory;
import pdc.command.Scheduler;

/**
 * Receives a user input string from the command window and converts it to a command object, which
 * it to the {@code Scheduler} for invocation. {@code Scheduler} calls {@code CmdParser} when it
 * needs a new user command.
 * 
 * @author Alan Cline
 * @version Sep 1 2006 // Split application into HIC, PDC, DMC components <br>
 *          Jun 5 2007 // Added runtime loop (CmdParser and CmdFactory) <br>
 *          Oct 26 2007 // Revised when DgnRunner and DgnBuild were merged. <br>
 *          Jan 1 2008 // Move all commands into command package, and adjust help() accordingly <br>
 *          Apr 11 2008 // Added windowing connection via Dgn static field <br>
 *          May 30 2008 // Added non-modal resizable Help window <br>
 *          Jul 1 2008 // Final commenting for Javadoc compliance <br>
 *          Aug 5 2014 // Linked CmdWindow with {@code Mainframe.StandardLayout} <br>
 *          Feb 15 2014 // Replaced ctor parm with {@code IOPanelInterface} for testing <br>
 */
public class CommandParser
{
    /**
     * Command lexicon is sorted alphabetically by command string for easier user readability. The
     * first entry is the (case-insensitive) command String from the user, the second entry is the
     * {@code CmdToken} that creates the {@code Command} object. <br>
     * NOTE: If the command table gets long, then sort in order of probablity for efficiency
     * (current look-up algorithm is linear).
     */
    private final static String[][] _cmdTable = {
            {"APPROACH", "CmdApproach"},// Display the description and image of Building exterior
            {"ENTER", "CmdEnter"}, // Display the description and image of Building interior
            {"EXIT", "CmdLeave"}, // Synonym for Leave
            {"GOTO", "CmdGoTo"}, // If parm is a Building or Building type, "Approach" building;
                                 // if parm = Town, goes to Town view; if null parm, info msg
            {"LEAVE", "CmdLeave"}, // Leave the interior and go to building's exterior
            {"QUIT", "CmdQuit"}, // End the program.
            {"RETURN", "CmdReturn"}, // Return to town view
    // {"TO TOWN", "CmdReturn"}, // Return to Town View
            // { "HELP", "CmdHelp" }, // List the user command names and their descriptions.
            // { "INVENTORY", "CmdInventory" }, // Describe the money the Hero has (later, this will
            // tell
            // the items too)
            // { "LOOK", "CmdLook" }, // Give a description of the Room and any People inside it.
            // { "WAIT", "CmdWait" }, // Wait a specific amount of time, in hours or minutes.
    };
    
    /** List of commands that we can look up */
    private static final Map<String, String> _commandMap = new TreeMap<String, String>();
    static {
        for (String[] s : _cmdTable) {
            _commandMap.put(s[0], s[1]);
        }
    }

    private String _userInput = ""; // buffer to hold user input string

    /** Error message if command cannot be found. */
    public static final String ERRMSG_UNKNOWN = "I don't understand what you want to do.";
    /** Error message if command cannot be found. */
    public static final String ERRMSG_INIT_FAILURE = "Failed to initialize command from user input";
    
    /** Reference to GUI panel for input and output messages, and their interactions */
    private MainframeCiv _mfCiv;

    /** Start the Scheduler up when the CommandPaarser starts */
    static private Scheduler _skedder = null;

    // ============================================================
    // Constructors and constructor helpers
    // ============================================================

    /**
     * Creates the singleton CommandParser, and connects to the {@code CommandFactory} and the
     * {@code MainframeCiv} for displaying parser output to {@code IOPanel}.
     * 
     * @param ioPanel handles input commands and output and error messages, and the interactions
     *        between command line input and output messages
     */
    public CommandParser(MainframeCiv mfCiv, Scheduler skedder)
    {
        _mfCiv = mfCiv;
        _skedder = skedder;
    }

    // ============================================================
    // Public Methods
    // ============================================================

    /**
     * Receives and holds the command string from the command window. It will be retrieved by the
     * {@code Scheduler} when it is ready for another user command.<br>
     * 
     * @param textIn the input the user entered onto the command line
     */
    public void receiveCommand(String textIn)
    {
        _userInput = textIn;
        CommandInput cmdInput = createCommandInput(textIn);
        Command cmd = createCommand(cmdInput);

        _skedder.sched(cmd);
    }


    // ============================================================
    // Private helper methods
    // ============================================================

    public Command createCommand(CommandInput commandInput)
    {
        Command cmd = CommandFactory.createCommand(commandInput.commandToken);

        if (cmd != null) {
            cmd.setMsgHandler(_mfCiv);
            if (cmd.init(commandInput.parameters) == false) {            
                _mfCiv.errorOut(ERRMSG_INIT_FAILURE);
            }
        } else {
            _mfCiv.errorOut(ERRMSG_UNKNOWN);
        }
        return cmd;
    }


    /**
     * Searches through the command table for the first word in the user input string and returns
     * the canonical {@code Command } name associated with it.
     * 
     * @return canonical name of the {@code Command} (concrete subCommand) to {@code Schedule}, or
     *         null.
     */
    public String lookup(String cmdString)
    {
        for (int k = 0; k < _cmdTable.length; k++) {
            if (cmdString.equalsIgnoreCase(_cmdTable[k][0])) {
                return _cmdTable[k][1];
            }
        }
        return null;
    }

    public CommandInput createCommandInput(String textIn)
    {
        List<String> tokens = Arrays.asList(textIn.split(" "));
        String commandToken = null;
        
        if (tokens.size() > 0) {
            String s = tokens.remove(0).toUpperCase();
            commandToken = _commandMap.get(s);
        }
        return new CommandInput(commandToken, tokens);
    }
    
    private class CommandInput
    {
        public final String commandToken;
        public final List<String> parameters;

        CommandInput(String cmdToken, List<String> params)
        {
            commandToken = cmdToken;
            parameters = params;
        }
    }



    // /**
    // * Traverses the command table and displays the command names, and their description,
    // * in a non-modal {@code HelpWindow} as a reminder to the user.
    // * This method is called by {@code CmdHelp.exec()}.
    // */
    // public void help()
    // {
    // // Conversion from line of text to pixel height for window
    // final int LINE_HEIGHT = 24;
    // final int CHAR_WIDTH = 7;
    //
    // // Create the Help dialog
    // HelpDialog helpWin = HelpDialog.createInstance(MainFrame.getInstance());
    //
    // // Identify indexes for convenience
    // final int NAME = 0;
    // final int CMD = 1;
    //
    // // Walk the command table, creating the Commands to get their descriptions
    // int longestDesc = 0;
    // int nbrCmds = 0;
    //
    // // Display the text for the help command, counting number of commands and longest description
    // length
    // helpWin.append("\n ");
    // for (int k = 0; k < _cmdTable.length; k++) {
    // String cmdString = _cmdTable[k][NAME];
    // Command cmd = _cmf.createCommand(_cmdTable[k][CMD]);
    // String desc = cmd.getDescription();
    // longestDesc = longestDesc > desc.length() ? longestDesc : desc.length();
    // nbrCmds++;
    // helpWin.append("  " + cmdString + "\t" + desc + "\n");
    // }
    // helpWin.append("\n ");
    // helpWin.lockText(true); // Lock the window to disallow redundant messaging
    //
    // // FInd how large to make the Help window
    // int height = nbrCmds * LINE_HEIGHT;
    // int width = (longestDesc + 12) * CHAR_WIDTH;
    // helpWin.setSize(width, height);
    //
    // helpWin.setVisible(true);
    // }
    // ============================================================
    // Mock inner class
    // ============================================================
    public class MockCP
    {
        /** Get the input command */
        public String getInput()
        {
            return CommandParser.this._userInput;
        }

    } // end of MockCP inner class



} // end of CommandParser class

