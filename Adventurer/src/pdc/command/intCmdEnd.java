/*
 * intCmdEnd.java
 * 
 * Copyright (c) 2007, Carolla Development, Inc. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, post on servers, to reuse, or to redistribute to lists, requires prior
 * specific permission and/or a fee. Request permission to use from Carolla Development, Inc. by
 * email: acline@carolla.com.
 */

package pdc.command;

import java.util.List;


/**
 * This internal command indicates the end of execution of a user command. It's <code>exec()</code>
 * method tells the Scheduler to get another user command. This command is created each time a user
 * command is created and put on the Schedule queue as an event after the duration of the user
 * command.
 * 
 * @author Alan Cline
 * @version Aug 29 2006 // original <br>
 *          Oct 26 2007 // revised when DgnRunner and DgnBuild were merged <br>
 *          Jan 15 2007 // grouped all internal (non-user) commands together <br>
 *          Jul 5 2008 // Final commenting for Javadoc compliance <br>
 * 
 * @see Command
 */
public class intCmdEnd extends Command
{
  // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
  /**
   * An internal command to trigger getting another user command. It cannot be called by the user.
   */
  static final String CMD_DESCRIPTION = "This is an internal command to trigger getting " +
      "another user command. It cannot be called by the user.";
  /** This command starts immediately, requiring no delay. */
  static final int DELAY = 0;
  /** This command adds no time to the game clock. */
  static final int DURATION = 0;

  /** Needs no args because the delay is defined in the override constuctor. */
  final int NBR_ARGS = 0;


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Loads up default values that may be overwritten, depending on the previous command. The
   * previous Command's duration is this intCmdEnd's delay, and must be overridden by the init()
   * method
   */
  public intCmdEnd()
  {
    super("intCmdEnd", DELAY, DURATION, CMD_DESCRIPTION, null);
  }


  /**
   * Creates an intCmdEnd with a new delay based on the user command it is ending. The previous
   * Command's duration is this intCmdEnd's delay
   * 
   * @param delay the duration of the command that implies this intCmdEnd command
   */
  public intCmdEnd(int delay)
  {
    super("intCmdEnd", delay, DURATION, CMD_DESCRIPTION, "internal CmdEnd");
  }

  /*
   * PUBLIC METHODS
   */

  /**
   * Takes the previous Command's duration as String args[0] and converts it to a base class
   * attribute <code>Command._delay</code>. Unlike the user command, does not create a paired
   * intCmdEnd, so does not need a duration attribute. It does need to override the base class
   * abstract methods.
   * 
   * @param args ArrayList of Strings; only delay parm is needed
   * @param mfciv delegated to handle displahing info and error messages
   * @return true if there are no parms
   */
  public boolean init(List<String> args)
  {
    if (args.size() == NBR_ARGS) {
      // Integer prevCmdDuration = (Integer) args.get(0);
      // _delay = prevCmdDuration.intValue();
      return true;
    }
    else {
      System.err.println("INTERNAL:  intCmdEnd.init()  missing parm");
      return false;
    }
  }


  /**
   * Called only in error: the Scheduler should have already called for a new user command.
   * 
   * @return false always because it should not have been called.
   */
  public boolean exec()
  {
    System.err.println("intCmdEnd.exec(): Invalid code--the Scheduler should have called " +
        " for a new userCommand already");
    return false;
  }


} // end of intCmdEnd class

