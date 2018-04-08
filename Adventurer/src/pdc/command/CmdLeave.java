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

import chronos.pdc.command.Command;
import civ.BuildingDisplayCiv;


/**
 * Allows the hero the exit the interior of a building, displaying its exterior image and
 * description.
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

    // ============================================================
    // Constructors and constructor helpers
    // ============================================================

    /** Constructor called by the CommandFactory. There is no delay but a 10-second duration. */
    public CmdLeave(BuildingDisplayCiv bdCiv)
    {
        super(CMD_NAME, DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
        _bldgCiv = bdCiv;
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
      // set good parms state
      boolean isInitialized = _bldgCiv.isInside();
      return isInitialized;    
    }


    /** Enter the designated building, or the current building if displayed */
    @Override
    public boolean exec()
    {
        if (_bldgCiv.isInside()) {
            _bldgCiv.leaveBuilding();
        }
        return true;
    }


} // end CmdEnter class

