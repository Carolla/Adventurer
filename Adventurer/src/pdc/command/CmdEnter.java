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
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.BuildingDisplayCiv;


/**
 * Moves the Hero inside a specified Building, displaying its interior image and description.
 * <P>
 * Format: ENTER [current Building name | current Building type] <br>
 * where:
 * <UL>
 * <LI>Building Name is the actual string name of the Building;</LI>
 * <LI>Building Type is the Building class, e.g., Inn, Bank, Jail;</LI>
 * </UL>
 * <P>
 * The Building name (and type) is checked with and without 'the' in the parm list. For example,
 * this command will check for "The Ugly Ogre Inn" and "Ugly Ogre Inn" before trying "Ugly Ogre Inn"
 * as a type. Conversely, if ENTER Inn is entered, it will try try to find a Building with the name
 * "Inn" before trying the type "Inn".
 * <UL>
 * <LI>If the Hero is at the Town view, a Building must be specified else an error message.</LI>
 * <LI>If the Hero is outside a Building, then no name is needed; the currently displayed building
 * is assumed.</LI>
 * <LI>If the Hero is already in the targeted building, then only an info message is displayed.</LI>
 * <LI>If the Hero tries to ENTER from inside one building to inside another, he gets an error
 * message saying he has to LEAVE (or EXIT) one Building before he can enter another. <br>
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
  static private final int DURATION = 10;

  /** Building accesses and displays are controlled by the BuildingDisplayCiv */
  private BuildingDisplayCiv _bldgCiv;

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

  /** Constructor called by the CommandFactory. There is no delay but a 10-second duration. */
  public CmdEnter()
  {
    super("CmdEnter", DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
    System.out.println("\tCmdEnter(): creating ENTER command.");
  }


  // ============================================================
  // Implementation Methods
  // ============================================================

  /**
   * There can be 0 or 1 arg in the arglist. If an arg is not specified, then the current Building
   * is assumed. If an argument is specified, then all words are combined into a building name. If
   * that fails to find a Building, then the name is checked against a Building type.
   * <P>
   * The Building name (or type) is checked with and without the word 'the', in case it is part of
   * the name of the Building. If the Hero specified the building he is already in, then an error
   * message is displayed.
   * 
   * @param args if empty, then use current Building; otherwise gets Building specified;
   * @return true if all worked, else returns false on input error
   */
  @Override
  public boolean init(List<String> args) throws NullPointerException
  {
    System.out.println("\tCmdEnter.init()...");
//    String bldgParm_short = null;   // in case 'the' is used
    
    // The BuildingDisplayCiv must already exist
    _bldgCiv = BuildingDisplayCiv.getInstance();
    _currentBuilding = _bldgCiv.getCurrentBuilding();
    // If no current building and not specified, error
    if ((args.size() == 0) && (_currentBuilding == null)) {
      super._msgHandler.errorOut(ERRMSG_NOBLDG);
      return false;
    }
    // Check if 'the' is used in the arglist; if so, the Building is queries with and without it
//    if (args.get(0).equals("the")) {
//      bldgParm_short = extractArticle(args);
//    }
    String bldgParm = convertArgsToString(args);
    // Check that the building specified actually exists
    BuildingRegistry breg =
        (BuildingRegistry) RegistryFactory.getInstance().getRegistry(RegKey.BLDG);
    // First try building name
    Building b = breg.getBuilding(bldgParm);
    if (b == null) {
      super._msgHandler.errorOut(ERRMSG_NOBLDG);
      return false;
    }
    _targetBldg = b.getName();
    return true;
  }


  /** Enter the designated building, or the current building if displayed */
  public boolean exec()
  {
    System.out.println("\tCmdEnter.exec()...");
    // Null is legal parm for this call
    // super._mfCiv.enterBuilding(_targetBldg);
    _bldgCiv = BuildingDisplayCiv.getInstance();
    _bldgCiv.enterBuilding(_targetBldg);
    return true;
  }


} // end CmdEnter class

