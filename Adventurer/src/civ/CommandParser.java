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
import java.util.StringTokenizer;

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
   * NOTE: If the command table gets long, then sort in order of probablity for efficiency (current
   * look-up algorithm is linear).
   */
  private final String[][] _cmdTable = {
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
      // { "INVENTORY", "CmdInventory" }, // Describe the money the Hero has (later, this will tell
      // the items too)
      // { "LOOK", "CmdLook" }, // Give a description of the Room and any People inside it.
      // { "WAIT", "CmdWait" }, // Wait a specific amount of time, in hours or minutes.
  };

  /** Internal reference: command-specific data arguments */
  private ArrayList<String> _parms = null;
  /** Internal reference: the factory that creates {@code Command} objects. */
  private CommandFactory _cmf = null;

  private String _userInput = null; // buffer to hold user input string

  // Special cases
  /** Error message if command cannot be found. */
  private final String ERRMSG_UNKNOWN = "I don't understand what you want to do.";
  /** A null or empty string was entered */
  private final String ERRMSG_CMDNULL = "Nothing was entered. Please try again.";

  /** Identify a command string in which only a return key is entered. */
  private final String CMD_EMPTY = "";

  /** Reference to GUI panel for input and output messages, and their interactions */
  private MainframeCiv _mfCiv;

  /** Start the Scheduler up when the CommandPaarser starts */
  static private Scheduler _skedder = null;

  private static CommandParser _this;

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
  private CommandParser(MainframeCiv mfCiv)
  {
    _parms = new ArrayList<String>();
    _mfCiv = mfCiv;
    _cmf = new CommandFactory(_mfCiv);

    // Start the scheduler off on its own thread
    _skedder = Scheduler.getInstance(this);
    new Thread(_skedder).start();
  }
  
  public static CommandParser getInstance(MainframeCiv mfCiv)
  {
    if (_this == null) {
    	_this = new CommandParser(mfCiv);
      }
      return _this;
  }

  // ============================================================
  // Public Methods
  // ============================================================

  /**
   * Collects user input and converts the first token into a {@code Command} token. Sends the
   * {@code Command} token to the {@code CommandFactory } to create the specific {@code Command}
   * subclass, and passes the remainder of the parms to the command for further parsing. This method
   * is called as part of the {@code Scheduler}'s loop.
   * 
   * @return one of the many user command subclasses available
   */
  public Command getUserCommand()
  {
    String cmdString = extractCommandLine(_userInput);
    String cmdToken = getCommandToken(cmdString);
    Command cmd = createCommand(cmdToken);
    return cmd;
  }


  /**
   * Receives and holds the command string from the command window. It will be retrieved by the
   * {@code Scheduler} when it is ready for another user command.<br>
   * 
   * @param textIn the input the user entered onto the command line
   */
  public void receiveCommand(String textIn)
  {
	// Guard against null parm
	  if (textIn == null) {
	    _mfCiv.errorOut(ERRMSG_CMDNULL);
	  }
	  else {
	    // Guard against empty string
	    String cmdIn = textIn.trim();
	    if (cmdIn.length() == 0) {
	      _mfCiv.errorOut(ERRMSG_CMDNULL);
	    }
	    else {
	      _userInput = cmdIn; 
	    }
	  }
  }

  
  // ============================================================
  // Private helper methods
  // ============================================================

  private Command createCommand(String token)
  {
    if (token == null) {
      return null;
    }
    Command cmd = _cmf.createCommand(token);
    if (cmd == null) {
      System.err.println(token + " command could not be created.");
      cmd = _cmf.createCommand("intCmdEnd");
    }
    // Each command takes care of their own parm-error message
    if (cmd.init(_parms) == false) {
      cmd = _cmf.createCommand("intCmdEnd");
    }
    return cmd;
  }

  /**
   * Retrieve the user-entered command, parse it, and clear the input buffer for more commands.
   * 
   * @param ip user-entered command string
   * @return the command and optional parms that go with it
   */
  private String extractCommandLine(String ip)
  {
    String cmdString = parse(ip);
    _userInput = null;
    return cmdString;
  }


  /**
   * Find command in command table, and return error message if not found
   * 
   * @param s the string to lookup in the command table
   * @return the Command to execute, else null if not found
   */
  private String getCommandToken(String s)
  {
    if (s == null) {
      return null;
    }
    // Get first token and check it against the command table
    String token = lookup(s);
    // If command cannot be found, ask user to try again
    if (token == null) {
      _mfCiv.errorOut(ERRMSG_UNKNOWN);
    }
    return token;
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

  // private boolean hasUserInput(String ip)
  // {
  // System.out.println("CommandPaser.hasUserInput() has been called.");
  // return (ip == null) ? false : true;
  // }


  /**
   * Searches through the command table for the first word in the user input string and returns the
   * canonical {@code Command } name associated with it.
   * 
   * @return canonical name of the {@code Command} (concrete subCommand) to {@code Schedule}, or
   *         null.
   */
  private String lookup(String cmdString)
  {
    String cmdToken = null;
    if (cmdString != null) {
      // Walk the command table. When found, break from loop.
      for (int k = 0; k < _cmdTable.length; k++) {
        if (cmdString.equalsIgnoreCase(_cmdTable[k][0])) {
          cmdToken = _cmdTable[k][1];
          break;
        }
      }
    }
    return cmdToken;
  }

  /**
   * Tokenizes the input line into its tokens and parms list.
   * 
   * @param ipLine String taken in from command window
   * @return @code Command} token, which is always the first token, and saves parms list (if any)
   *         for later.
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
      return null;
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


  // ============================================================
  // Mock inner class
  // ============================================================
  public class MockCP
  {
    public MockCP()
    {}

    /** Get the input command */
    public String getInput()
    {
      return CommandParser.this._userInput;
    }

    /**
     * Get the static Scheduler currently running
     */
    public Scheduler getScheduler()
    {
      return _skedder;
    }

    
    /** @return the specified message for testing comparisons
     */
    public String getERRMSG_CMDNULL()
    {
      return ERRMSG_CMDNULL;
    }


  } // end of MockCP inner class



} // end of CommandParser class

