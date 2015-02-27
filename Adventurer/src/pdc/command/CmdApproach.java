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
    static private final String CMDFMT = "APPROACH <Building Name | Building Type>";
    /** This command starts immediately, requiring no delay. */
    static private final int DELAY = 0;
    /** This command takes 30 seconds on the game clock. */
    static private final int DURATION = 30;

    
    /** Building accesses and displays are controlled by the BuildingDisplayCiv */
    private BuildingDisplayCiv _bldgCiv;
    
    /** Error message if no arguments or multiple arguments specified */
    private final String ERRMSG_NEED_ONE_BLDG =
        "You must specify one (and only one) building to approach.";
    /** Error message if building not found in registry */
    private static final String ERRMSG_NO_BLDG = "We don't seem to have that building in town.";
    
    private Building _buildingToApproach;
    
    // ============================================================
    // Constructors and constructor helpers
    // ============================================================

    /** Constructor called by the CommandFactory. */
    public CmdApproach()
    {
      super("CmdApproach", DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
      System.out.println("\tCmdApproach(): creating APPROACH command.");
    }

    // ============================================================
    // Implementation Methods
    // ============================================================

    /**
     * Verifies the following command format:
     * {@code APPROACH <Building name | Building type>} <br>
     * There should only be one argument specified - the name of the building.
     * 
     * @param args specify desired building; cannot be empty
     * @return true if all worked, else returns false on input error
     */
    @Override
    public boolean init(List<String> args) throws NullPointerException
    {
      System.out.println("\tCmdApproach.init()...");
      // The BuildingDisplayCiv must already exist
//      _currentBuilding = _bldgCiv.getCurrentBuilding();
      // If no current building and not specified, error 
//      if ((args.size() == 0) && (_currentBuilding == null)) {
      if (args.size() != 1) {
        super._msgHandler.errorOut(ERRMSG_NEED_ONE_BLDG);
        return false;
      } else {
          String bldgName = args.get(0);
          RegistryFactory regFact = RegistryFactory.getInstance();
          BuildingRegistry breg = (BuildingRegistry) regFact.getRegistry(RegKey.BLDG);
          Building b = breg.getBuilding(bldgName);
          
          if (b == null) {
              super._msgHandler.errorOut(ERRMSG_NO_BLDG);
              return false;
          }
          _buildingToApproach = b;
          super._msgHandler.messageOut("Params are OK");
          return true;
      }
//      _targetBldg = convertArgsToString(args);
//      super._msgHandler.errorOut(ERRMSG_NEED_ONE_BLDG);
//      return false;
    }

    @Override
    public boolean exec()
    {
        System.out.println("\tCmdApproach.exec()...");
        _bldgCiv = BuildingDisplayCiv.getInstance();
        _bldgCiv.approachBuilding(_buildingToApproach.getName());
        
        return false;
    }

}
