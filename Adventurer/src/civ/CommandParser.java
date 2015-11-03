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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pdc.command.CommandFactory;
import pdc.command.CommandInput;
import chronos.pdc.Command.Command;
import chronos.pdc.Command.Scheduler;

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
    private String _userInput = ""; // buffer to hold user input string

    private final CommandFactory _factory;

    /** Start the Scheduler up when the CommandPaarser starts */
    static private Scheduler _skedder = null;

    // ============================================================
    // Constructors and constructor helpers
    // ============================================================

    /**
     * Creates the singleton CommandParser, and connects to the {@code CommandFactory} and the
     * {@code MainframeCiv} for displaying parser output to {@code IOPanel}.
     * 
     * @param skedder Command Scheduler executes the commands
     * @param factory the CommandFactory for creating commands
     * 
     */
    public CommandParser(Scheduler skedder, CommandFactory factory)
    {
        _skedder = skedder;
        _factory = factory;
    }

    // ============================================================
    // Public Methods
    // ============================================================

    /**
     * Receives and holds the command string from the command window. It will be retrieved by the
     * {@code Scheduler} when it is ready for another user command.<br>
     * 
     * @param textIn the input the user entered onto the command line
     * @return true if the command is properly initialized
     */
    public boolean receiveCommand(String textIn)
    {
        _userInput = textIn;
        CommandInput cmdInput = createCommandInput(textIn);
        Command cmd = _factory.createCommand(cmdInput);

        boolean isInit = cmd.isInitialized();
        if (isInit) {
            _skedder.sched(cmd);
        }

        return isInit;
    }

    public CommandInput createCommandInput(String textIn)
    {
        List<String> tokens = new ArrayList<String>(Arrays.asList(textIn.split(" ")));
        String commandToken = null;

        if (tokens.size() > 0) {
            commandToken = tokens.remove(0).toUpperCase();
        }

        return new CommandInput(commandToken, tokens);
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
    // helpWin.append(" " + cmdString + "\t" + desc + "\n");
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

