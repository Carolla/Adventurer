/**
 * CmdExit.java
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

import civ.BuildingDisplayCiv;
import civ.MainframeCiv;


/**
 * Moves the Hero from ... to ..., and displays its
 * interior description and image. If no building is specified (no parm), the current building is
 * assumed; if there is no current building, that is, at the town view, then an error msg. If the
 * Hero is already in the targeted building, then only an info message is displaed. If the Hero
 * tries to jump from inside one building to inside another, he gets an error message saying
 * he has to leave one before he can enter another. <br>
 * <P>
 * Format: ENTER [current Building name | current Building type] <br>
 * where:
 * <UL>
 * <LI>Building Name is the actual string name of the Building, and is Adventure specific;</LI>
 * <LI>Building Type is the Building class, e.g., Inn, Bank, Jail;</LI>
 * </UL>
 * 
 * @author Alan Cline
 * @version Mar 19 2014 // original <br>
 *          Aug 23, 2014 // updated {@code init} method to handle IOPanel for outputs <br>
 *          Aug 30, 2014 // updated {@code exec} method to handle current building <br>
 *          Feb 18, 2015 // updated to handle common IOPanel for text outputs <br>
 * 
 * @see Command
 */
public class CmdExit extends Command
{
  // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
  /** The description of what the command does, used in the <code>help()</code> method. */
  static final String CMD_DESCRIPTION = "Exit prompts user to leaves the game.";
  /** This command starts immediately, requiring no delay. */
  static final int DELAY = 0;
  /** This command takes 10 seconds on the game clock. */
  static final int DURATION = 0;
  /** Command format */
  static private final String CMDFMT = "EXIT";

  private MainframeCiv _mfCiv;

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /** Constructor called by the CommandFactory. There is no delay nor duration. */
  public CmdExit()
  {
    super("CmdExit", DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
  }


  // ============================================================
  // Public methods
  // ============================================================

  /**
   * Enters the current building. There can be 0 or 1 args in the arglist. If an arg is specified,
   * then checks if it is a Building class, or the name of a particular Building. It then converts
   * the name into the Building class.
   * 
   * @param args if empty, then use current Building; otherwise get Building specified
   * @return true if all worked, else returns false on input error
   */
  @Override
  public boolean init(List<String> args)
  {
    return true;
  }


  /**
   * Forces the program to end.
   * 
   * @return false always to break out of the Scheduler loop
   */
  public boolean exec()
  {
    BuildingDisplayCiv bdCiv = _mfCiv.getBuildingDisplayCiv();
    bdCiv.leaveBuilding();
    _mfCiv.quit();
    return false;
  }

} // end CmdExit class

