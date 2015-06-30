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
 * Format: ENTER [Building name] <br>
 * where: <br>
 * Building Name is the actual string name of the Building;
 * <P>
 * The Building name is checked with and without 'the' in the parm list. For example, this command
 * will check for "The Ugly Ogre Inn" and "Ugly Ogre Inn" before trying "Ugly Ogre Inn" as a type.
 * Conversely, if ENTER Inn is entered, it will try try to find a Building with the name "Inn"
 * before trying the type "Inn".
 * <UL>
 * <LI>If the Hero is at the Town view, a Building must be specified else an error message.</LI>
 * <LI>If the Hero is outside a Building, then no name is needed; the currently displayed building
 * is assumed.</LI>
 * <LI>If the Hero is already in the targeted building, then interior is redisplayed, and nothing
 * different appears to the user.</LI>
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
  static private final String CMDFMT = "ENTER [Building Name]";
  /** This command starts immediately, requiring no delay. */
  static private final int DELAY = 0;
  /** This command takes 10 seconds on the game clock. */
  static private final int DURATION = 10;

  /** Building accesses and displays are controlled by the BuildingDisplayCiv */
  private BuildingDisplayCiv _bldgCiv;
  /** BuildingRegistry from which to retrieve buildings and properties */
  private BuildingRegistry _breg = null;

  /** The building to enter */
  private Building _targetBuilding;



  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /** Constructor called by the CommandFactory. There is no delay but a 10-second duration. */
  public CmdEnter()
  {
    super("CmdEnter", DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
    _breg = (BuildingRegistry) RegistryFactory.getInstance().getRegistry(RegKey.BLDG);
  }


  // ============================================================
  // Implementation Methods
  // ============================================================

  /**
   * There can be 0 or 1 arg in the arglist. If an arg is not specified, then the current Building
   * is assumed. If an argument is specified, then all words are combined into a building name.
   * <P>
   * The Building name is checked with and without the word 'the', in case it is part of the name of
   * the Building. If the Hero specified the building he is already in, then the image is
   * redisplayed, which appears to the user as if nothing happened.
   * 
   * @param args if empty, then use current Building; otherwise gets Building specified;
   * @return true if all worked, else returns false on input error
   */
  @Override
  public boolean init(List<String> args) 
  {
    String bldgParm = convertArgsToString(args);
      
    // The BuildingDisplayCiv must already exist
    _bldgCiv = BuildingDisplayCiv.getInstance();
    if (_bldgCiv.canEnter(bldgParm)) {
    	if (bldgParm.length() > 0) {
        	_targetBuilding = _breg.getBuilding(bldgParm);
    	} else {
    		_targetBuilding = _bldgCiv.getCurrentBuilding();
    	}
    	System.err.println("cmdEnter.init() has building " + _targetBuilding);
    	return true;
    } else {
    	return false;
    }
  }


  /** Enter the designated building, or the current building if displayed */
  @Override
  public boolean exec()
  {
    _bldgCiv.enterBuilding(_targetBuilding);
    System.err.println("attempting to enter " + _targetBuilding);
    return true;
  }


  /** INNER CLASS: MOCK */
  public class MockCmdEnter
  {
    /** Ctor */
    public MockCmdEnter()
    {}

    public void clearTargetBldg()
    {
      _targetBuilding = null;
    }

    public Building getTargetBldg()
    {
      return _targetBuilding;
    }

    public int getDelay()
    {
      return CmdEnter.DELAY;
    }

    public int getDuration()
    {
      return CmdEnter.DURATION;
    }

    public String getCmdFormat()
    {
      return CmdEnter.CMDFMT;
    }

  } // end MockCmdEnter class



} // end CmdEnter class

