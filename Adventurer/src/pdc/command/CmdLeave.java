/**
 * CmdLeave.java
 * 
 * Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, post on servers, to reuse, or to redistribute to lists, requires prior
 * specific permission and/or a fee. Request permission to use from Carolla Development, Inc. by
 * email: acline@carolla.com.
 */

package pdc.command;

import java.util.List;

import chronos.pdc.buildings.Building;
import civ.BuildingDisplayCiv;


/**
 * Allows the hero the exit the interior of a building, displaying its exterior image and description.
 * <P>
 * Format: LEAVE <br>
 * <P>
 * 
 * @author Alan Cline
 * @version Mar 1 2015 // original <br>
 * 
 * @see Command
 */
public class CmdLeave extends Command
{
  // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
  /** The name of the command, mostly for error messages */
  static private final String CMD_NAME = "LEAVE";
  /** The description of what the command does, used in the {@code help()} method. */
  static private final String CMD_DESCRIPTION = "Leave the current Building.";
  /** Format for this command; null building defaults to current building */
  static private final String CMDFMT = null;
  /** This command starts immediately, requiring no delay. */
  static private final int DELAY = 0;
  /** This command takes 10 seconds on the game clock. */
  static private final int DURATION = 10;

  /** Building accesses and displays are controlled by the BuildingDisplayCiv */
  protected BuildingDisplayCiv _bldgCiv;
  /** The building currently displayed, either inside or outside */
  private Building _currentBuilding = null;
//  /** The building just exited */
//  private Building _targetBldg;

//  /** Error message if no current building to enter */
//  private final String ERRMSG_NOBLDG =
//      "I see no building here. What building did you want to enter?";
//  /** Message if already in designated building */
//  private final String ERRMSG_SAMEBLDG =
//      "You are in that building.";
//  /** Message if trying to jump fron interior to interior of buldings */
//  private final String ERRMSG_JUMPBLDG =
//      "You must leave this building before you enter another.";

  
  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /** Constructor called by the CommandFactory. There is no delay but a 10-second duration. */
  public CmdLeave()
  {
    super(CMD_NAME, DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
//    System.out.println("\tCmdLeave(): creating LEAVE command.");
    // The BuildingDisplayCiv must already exist
    _bldgCiv = BuildingDisplayCiv.getInstance();
  }


  // ============================================================
  // Implementation Methods
  // ============================================================

  /**
   * There are no args for this command. 
   * 
   * @param args if not empty, then error message
   * @return true if all worked, else returns false on input error
   */
  @Override
  public boolean init(List<String> args) throws NullPointerException
  {
    _currentBuilding = _bldgCiv.getCurrentBuilding();
      return true;
  }


  /** Enter the designated building, or the current building if displayed */
  public boolean exec()
  {
    _bldgCiv.approachBuilding(_currentBuilding);
    return true;
  }


} // end CmdEnter class
