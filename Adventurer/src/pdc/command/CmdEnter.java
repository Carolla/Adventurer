/**
 * CmdEnter.java
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

import chronos.pdc.buildings.Building;
import civ.BuildingDisplayCiv;


/**
 * Moves the Hero from outside the Building (or Town) being displayed to inside, and displays its
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
public class CmdEnter extends Command
{
  // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
  /** The description of what the command does, used in the {@code help()} method. */
  static private final String CMD_DESCRIPTION = "Enter into the Building of choice.";
  /** Format for this command; null building defaults to current building */
  static private final String CMDFMT = "ENTER [Building Name | Building Type]";
  /** This command starts immediately, requiring no delay. */
  static private final int DELAY = 0;
  /** This command takes 10 seconds on the game clock. */
  static private final int DURATION = 30;

  /** Building accesses and displays are controlled by the BuildingDisplayCiv */
  BuildingDisplayCiv _bldgCiv;

  /** The building currently displayed, either inside or outside */
  private Building _currentBuilding = null;
  /** The building to enter */
  private String _targetBldg;

  /** Error message if no current building to enter */
  private final String ERRMSG_NOBLDG =
      "I see no building here. What building did you want to enter?";
  /** Message if already in designated building */
  private final String ERRMSG_SAMEBLDG =
      "You are in that building.";
  /** Message if trying to jump fron interior to interior of buldings */
  private final String ERRMSG_JUMPBLDG =
      "You must leave this building before you enter another.";


  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /** Constructor called by the CommandFactory. There is no delay nor duration. */
  public CmdEnter()
  {
    super("CmdEnter", DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
    System.out.println("\tCmdEnter(): creating ENTER command.");
  }


  // ============================================================
  // Implementation Methods
  // ============================================================

  /**
   * There can be 0 or many args in the arglist. If an arg is not specified, then the current
   * Building is assumed. If more than one argument is specified, then they are all assumed to be
   * part of the name. The word Building is checked with and without the word 'the', in case it is
   * part of the name of the Building. If the Hero specified the building he is already in, invokes
   * on an info message.
   * 
   * @param args if empty, then use current Building; otherwise gets Building specified;
   * @return true if all worked, else returns false on input error
   */
  @Override
  public boolean init(List<String> args) throws NullPointerException
  {
    System.out.println("\tCmdEnter.init()...");
    // THis needs to be a MainframeInterface
//    _bldgCiv = BuildingDisplayCiv.getInstance(Mainframe.getInstance());
    // The BuildingDisplayCiv must already exist
    _bldgCiv = BuildingDisplayCiv.getRef();
    if (_bldgCiv == null) {
      throw new NullPointerException("BuildingDisplayCiv does not yet exist");
    }
    _currentBuilding = _bldgCiv.getCurrentBuilding();
    // If no current building and not specified, error 
    if ((args.size() == 0) && (_currentBuilding == null)) {
      super._msgHandler.errorOut(ERRMSG_NOBLDG);
      return false;
    }
    _targetBldg = convertArgsToString(args);
    return true;
  }


  /** Enter the designated building, or the current building if displayed */
  public boolean exec()
  {
    System.out.println("\tCmdEnter.exec()...");
    // Null is legal parm for this call
    // super._mfCiv.enterBuilding(_targetBldg);
    _bldgCiv.enterBuilding(_targetBldg);
    return true;
  }


} // end CmdEnter class

