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

import hic.IOPanel;

import java.util.List;

import civ.MainframeCiv;


/**
 * Stops the user command dialogue and triggers the shutdown methods to end the program.
 * <p>
 * Format: QUIT [or EXIT] <br>
 * The command string is case-insensitive. See <code>init()</code> method.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>1.0 Sep 2 2006 // original <DD> <DT>1.1 Sep 30 2007 // Reconfigure CmdQuit in with
 *          command package <DD> <DT>2.0 Dec 31 2007 // Moved this command into the latest command
 *          package <DD> <DT>2.1 Jul 4 2008 // Final commenting for Javadoc compliance<DD>
 *          </DL>
 * @see Command
 */
public class CmdQuit extends Command
{
  /** Will hold reference to Mainframe Civ **/
  private MainframeCiv _mfCiv;

  // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
  /** The description of what the command does, used in the <code>help()</code> method. */
  static final String CMD_DESCRIPTION = "End the program.";
  /** This command starts immediately, requiring no delay. */
  static final int DELAY = 0; // This command starts immediately on invocation
  /** This command takes no time on the game clock. */
  static final int DURATION = 0;


  // ============================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ============================================================

  /** Constructor called by the CommandFactory. There is no delay nor duration. */
  public CmdQuit()
  {
    super("CmdQuit", DELAY, DURATION, CMD_DESCRIPTION, null);
  }


  // ============================================================
  // PUBLIC METHODS
  // ============================================================

  /**
   * In this case, no parms are needed.
   * 
   * @param args is empty for this command; implemented as required for abstract method
   * 
   * @return true if all worked, else returns false on input error
   */
  @Override
  public boolean init(List<String> args, IOPanel iopanel)
  {
    if (args.size() == 0) {
      return true;
    }
    return false;
  }


  /**
   * Forces the program to end.
   * 
   * @return false always to break out of the Scheduler loop
   */
  @Override
  public boolean exec()
  {
    _mfCiv.handleError("Quit command exec().");
    System.out.println("The Inn is closed for business.");

    // Close down the windowing system
    // MainFrame win = MainFrame.getInstance();
    // if (win != null) {
    // win.quit();
    // }
    return true;
  }

} // end CmdQuit class

