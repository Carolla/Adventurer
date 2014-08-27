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

import hic.IOPanel;

import java.util.List;

import civ.MainframeCiv;


/**
 * Moves the Hero from outside the Building (or Town) being displayed to inside, and displays its
 * interior description and image.
 * <P>
 * Format: ENTER (current Building) <br>
 * where:
 * <UL>
 * <LI>Building Type is the Building class, e.g., Inn, Bank, Jail;</LI>
 * <LI>Building Name is the actual string name of the Building, and is Adventure specific;</LI>
 * </UL>
 * If no arguments are given, the type of the currently displayed Building is assumed. The command
 * string is case-insensitive. If the user enters 'the' in front of the building name or type, it
 * will check with and without this word; e.g., "ENTER the Jail" is the same as "ENTER Jail". See
 * {@code init()} method.
 * 
 * @author Alan Cline
 * @version Mar 19 2014 // original <br>
 *          Aug 23, 2014 // updated {@code init} method to handle IOPanel for outputs <br>
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
  /** mainframe reference to get buildings */
  MainframeCiv _mfCiv = null;

  /** Error message if no current building to enter */
  private final String ERRMSG_NOBLDG =
      "I see no building here. What building did you want to enter?";

  private IOPanel _op;
  
  /** Constructor called by the CommandFactory. There is no delay nor duration. */
  public CmdEnter()
  {
    super("CmdEnter", DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
  }


  /**
   * Enters the current building. There can be 0 or many args in the arglist. If an arg is not
   * specified, then the current Building is assumed. If more than one argument is specified, then
   * they are all assumed to be part of the name. The word Building is checked with and without the
   * word 'the', in case it is part of the name of the Building.
   * 
   * @param args if empty, then use current Buiilding; otherwise gets Building specified;
   * @param mfCiv
   * @return true if all worked, else returns false on input error
   */
  @Override
  public boolean init(List<String> args)
  {
    // Get the Building parm, or null
    if ((args.size() == 0) && (_mfCiv.isOnTown())) {
      _mfCiv.handleError(ERRMSG_NOBLDG);
    }
    _targetBldg = convertArgsToString(args);
    return true;
  }

  public boolean exec()
  {
//    // Null is legal parm for this call
//    _mfCiv.enterBuilding(_targetBldg);
    // TEMPORARY
    _op.displayText("Trying to enter a Building, but not implemented yet");
    return true;
  }


} // end CmdQuit class

