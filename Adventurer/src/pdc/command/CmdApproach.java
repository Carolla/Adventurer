/**
 * CmdApproach.java
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
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.BuildingDisplayCiv;

/**
 * Moves the Hero from outside a Building or from the Town View to the outside of the Building to be
 * approached. A parameter is required specifying the desired Building. Attempting to use the
 * Approach command from inside another building produces an error. Clicking on the desired building
 * from the Town View produces the same effect as the typed command.<br>
 * <P>
 * Format: &nbsp{@code APPROACH <Building name | Building type>} <br>
 * where:
 * <UL>
 * <LI>Building Name is the actual string name of the Building, and is Adventure specific;</LI>
 * <LI>Building Type is the Building class, e.g., Inn, Bank, Jail;</LI>
 * </UL>
 * 
 * @author Dave Campbell
 * @version Feb 27 2015 // original <br>
 * 
 * @see Command
 */
public class CmdApproach extends Command
{
    
    // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
    /** The description of what the command does, used in the {@code help()} method. */
    static private final String CMD_DESCRIPTION = "Approach the Building of choice.";
    /** Format for this command */
    static private final String CMDFMT = "APPROACH <Building Name>";
    /** This command starts immediately, requiring no delay. */
    static private final int DELAY = 0;
    /** This command takes 30 seconds on the game clock. */
    static private final int DURATION = 30;

    /** Building accesses and displays are controlled by the BuildingDisplayCiv */
    private BuildingDisplayCiv _bldgCiv;
    /** BuildingRegistry from which to retrieve buildings */
    private BuildingRegistry _breg;
    
    /** The building to approach */
    private Building _targetBuilding;
    
    /** Error message if no arguments or multiple arguments specified */
    private final String ERRMSG_NOBLDG =
        "Sure, but you've gotta say WHICH building to approach.";
    /** Error message if building not found in registry */
    private final String ERRMSG_WRONG_BLDG =
            "That some kinda slang, stranger?  WHAT building was that again?";
    
    /** Message if trying to jump from interior to exterior of buildings */
    private final String ERRMSG_JUMPBLDG =
        "You must leave this building before you approach another.";

    
    // ============================================================
    // Constructors and constructor helpers
    // ============================================================

    /** Constructor called by the CommandFactory. */
    public CmdApproach()
    {
      super("CmdApproach", DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
      _breg = (BuildingRegistry) RegistryFactory.getInstance().getRegistry(RegKey.BLDG);
    }

    // ============================================================
    // Implementation Methods
    // ============================================================

    /**
     * There must be 1 arg in the arglist. Argument tokens will be combined into a building name.
     * <P>
     * The Building name is checked with and without the word 'the', in case it is part of the name of
     * the Building. If the Hero specified the building he is already in, then the image is
     * redisplayed, which appears to the user as if nothing happened.
     * 
     * @param args gets Building specified;
     * @return true if all worked, else returns false on input error
     */
    @Override
    public boolean init(List<String> args)
    {
      // The BuildingDisplayCiv must already exist
      _bldgCiv = BuildingDisplayCiv.getInstance();

      // The Hero cannot be inside a building already
      if (_bldgCiv.isInside() == true) {
        _mfCiv.errorOut(ERRMSG_JUMPBLDG);
        return false;
      }
      
      // Case 1: Building name is given
      if (args.size() != 0) {
        String bldgParm = convertArgsToString(args);
        Building b = _breg.getBuilding(bldgParm);
        // Check that the building specified actually exists
        if (b == null) {
          _mfCiv.errorOut(ERRMSG_WRONG_BLDG);
          return false;
        } else {
          _targetBuilding = b;
          return true;
        }
      }
      else {
        // Case 2: No building specified
          _mfCiv.errorOut(ERRMSG_NOBLDG);
          return false;
      }
    }

    @Override
    public boolean exec()
    {
        _bldgCiv.approachBuilding(_targetBuilding);
        return true;
    }
    
    /** INNER CLASS: MOCK */
    public class MockCmdApproach
    {
      /** Ctor */
      public MockCmdApproach()
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
        return CmdApproach.DELAY;
      }

      public int getDuration()
      {
        return CmdApproach.DURATION;
      }
      
      public String getCmdFormat()
      {
          return CmdApproach.CMDFMT;
      }

      
    } // end MockCmdEnter class

} // end CmdApproach Class
