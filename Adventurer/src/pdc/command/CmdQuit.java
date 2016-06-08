/**
 * CmdQuit.java
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

import chronos.pdc.command.Command;
import civ.BuildingDisplayCiv;
import civ.MainframeCiv;


/**
 * Stops the user command dialogue and triggers the shutdown methods to end the program.
 * <p>
 * Format: QUIT <br>
 * The command string is case-insensitive. See <code>init()</code> method.
 * 
 * @author Alan Cline
 * @version Sep 2 2006 // original <br>
 *          Sep 30 2007 // Reconfigure CmdQuit in with command package <br>
 *          Dec 31 2007 // Moved this command into the latest command package <br>
 *          Jul 4 2008 // Final commenting for Javadoc compliance <br>
 * @see Command
 */
public class CmdQuit extends Command
{
  // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
  /** The description of what the command does, used in the <code>help()</code> method. */
  static final String CMD_DESCRIPTION = "End the program.";
  /** This command starts immediately, requiring no delay. */
  static final int DELAY = 0; // This command starts immediately on invocation
  /** This command takes no time on the game clock. */
  static final int DURATION = 0;
  /** Format for usage string on input error */
  // static final String FMT = "[takes no parameters]";


  /** Error message if args are input with command */
  private final String ERRMSG_OMIT_ARGS =
      "If you want to exit the program, type \"Quit\" without any additional characters.";

  /** Error message if hero inside when command initialized */
  private final String ERRMSG_IN_BLDG = "To quit, you must be outside.";

  private final BuildingDisplayCiv _bdCiv;
  private final MainframeCiv _mfCiv;

  // ============================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ============================================================

  /** Constructor called by the CommandFactory. There is no delay nor duration. */
  public CmdQuit(MainframeCiv mfCiv, BuildingDisplayCiv bdCiv)
  {
    super("CmdQuit", DELAY, DURATION, CMD_DESCRIPTION, null);
    _mfCiv = mfCiv;
    _bdCiv = bdCiv;
  }


  // ============================================================
  // PUBLIC METHODS
  // ============================================================

  /**
   * In this case, no parms are needed.
   * 
   * @param args is empty for this command; implemented as required for abstract method
   * 
   * @return true if all worked and hero is outside, else returns false on input error
   */
  @Override
  public boolean init(List<String> args)
  {
    if ((args.size() == 0) && (_bdCiv.isInside() == false)) {
      _isInitialized = true;
      return true;
    } else if ((args.size() == 0) && (_bdCiv.isInside() == true)) {
      _output.displayErrorText(ERRMSG_IN_BLDG);
      return false;
    } else {
      _output.displayErrorText(ERRMSG_OMIT_ARGS);
      return false;
    }
  }

  /**
   * Forces the program to end.
   * 
   * @return false always to break out of the Scheduler loop
   */
  @Override
  public boolean exec()
  {
    _mfCiv.quit();
    return true;
  }

} // end CmdQuit class

