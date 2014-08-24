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

import hic.IOPanel;

import java.util.List;

import chronos.pdc.buildings.Building;
import civ.MainframeCiv;


/**
 * Moves the Hero from inside theBuilding being displayed (current Building) to exterior, and
 * displays its exterior description and image.
 * <P>
 * Format: EXIT <br>
 * where:
 * <UL>
 * <LI>Building Type is the Building class, e.g., Inn, Bank, Jail;</LI>
 * <LI>Building Name is the actual string name of the Building, and is Adventure specific;</LI>
 * </UL>
 * If no arguments are given, the type of the current Building is assumed. The command string is
 * case-insensitive. See <code>init()</code> method.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>1.0 Mar 19 2014 // original <DD>
 *          </DL>
 * @see Command
 */
public class CmdExit extends Command
{
  // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
  /** The description of what the command does, used in the <code>help()</code> method. */
  static final String CMD_DESCRIPTION = "Enter into the Building of choice.";
  /** This command starts immediately, requiring no delay. */
  static final int DELAY = 0;
  /** This command takes 10 seconds on the game clock. */
  static final int DURATION = 30;
  /** Commnand format */
  static private final String CMDFMT = "EXIT";

  /** The building to enter */
  private Building _curBldg = null;
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
   * @param args if empty, then use current Buiilding; otherwise gets Building specified
   * @param mfCiv
   * @return true if all worked, else returns false on input error
   */
  public boolean init(List<String> args, IOPanel iopanel)
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
    _mfCiv.openTown();
    return true;
  }

} // end CmdExit class

