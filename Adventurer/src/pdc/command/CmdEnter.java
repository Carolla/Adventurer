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

import civ.BuildingDisplayCiv;


/**
 * Moves the Hero from outside the Building (or Town) being displayed to inside, and displays its
 * interior description and image.
 * <P>
 * Format: ENTER (current Building name | current Building type) <br>
 * where:
 * <UL>
 * <LI>Building Name is the actual string name of the Building, and is Adventure specific;</LI>
 * <LI>Building Type is the Building class, e.g., Inn, Bank, Jail;</LI>
 * </UL>
 * If no arguments are given, the type of the currently displayed Building is assumed. The command
 * string is case-insensitive. If the user enters 'the' in front of the building name or type, it
 * will check with and without this word; e.g., "ENTER the Jail" is the same as "ENTER Jail". See
 * {@code init()} method, unless the word "the" is part of the building's name.
 * 
 * @author Alan Cline
 * @version Mar 19 2014 // original <br>
 *          Aug 23, 2014 // updated {@code init} method to handle IOPanel for outputs <br>
 *          Aug 30, 2014 // updated {@code exec} method to handle current building <br>
 * 
 * @see Command
 */
public class CmdEnter extends Command
{
  // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
  /** The description of what the command does, used in the {@code help()} method. */
  static final String CMD_DESCRIPTION = "Enter into the Building of choice.";
  /** This command starts immediately, requiring no delay. */
  static final int DELAY = 0;
  /** This command takes 10 seconds on the game clock. */
  static final int DURATION = 30;
  /** Format for this command */
  static private final String CMDFMT = "ENTER [Building Name | Building Type]";

  /** The building to enter */
  private String _targetBldg = null;

  /** Error message if no current building to enter */
  private final String ERRMSG_NOBLDG =
      "I see no building here. What building did you want to enter?";


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
   * Enters the current building. There can be 0 or many args in the arglist. If an arg is not
   * specified, then the current Building is assumed. If more than one argument is specified, then
   * they are all assumed to be part of the name. The word Building is checked with and without the
   * word 'the', in case it is part of the name of the Building.
   * 
   * @param args if empty, then use current Building; otherwise gets Building specified;
   * @param mfCiv
   * @return true if all worked, else returns false on input error
   */
  @Override
  public boolean init(List<String> args)
  {
    System.out.println("\tCmdEnter.init()...");
    BuildingDisplayCiv bdciv = BuildingDisplayCiv.getInstance();
    // Get the Building parm, or null
      if ((args.size() == 0) || (bdciv.getCurrentBuilding() == null)) {
      super._cp.errorOut(ERRMSG_NOBLDG);
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
    super._mfCiv.enterBuilding(_targetBldg);
    return true;
  }


} // end CmdQuit class

