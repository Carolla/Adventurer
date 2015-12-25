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

import chronos.pdc.command.Command;
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
    
    /** The building to approach */
    private String _targetBuilding = "";
    
    // ============================================================
    // Constructors and constructor helpers
    // ============================================================

    /** Constructor called by the CommandFactory. */
    public CmdApproach(BuildingDisplayCiv bdCiv)
    {
      super("CmdApproach", DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
      _bldgCiv = bdCiv;
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
      _targetBuilding = convertArgsToString(args);
      _isInitialized = _bldgCiv.canApproach(_targetBuilding);
      return _isInitialized;
    }

    @Override
    public boolean exec()
    {
        _bldgCiv.approachBuilding(_targetBuilding);
        return true;
    }
} // end CmdApproach Class
