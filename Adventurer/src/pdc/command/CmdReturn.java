/**
 * CmdReturn.java
 * 
 * Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved.
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
 * Moves the Hero from outside the Building to the Town view again. Format: RETURN [to Town] <br>
 * This command requires the Hero to be outside of a Building, otherwise it removes the Hero from
 * the building only. The command string is case-insensitive. See <code>init()</code> method.
 * 
 * @author Alan Cline
 * @version Mar 29 2014 // original <br>
 * @see Command
 */
public class CmdReturn extends Command
{
    // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
    /** The description of what the command does, used in the <code>help()</code> method. */
    static final String CMD_DESCRIPTION = "Return to the Town view.";
    /** This command starts immediately, requiring no delay. */
    static final int DELAY = 0;
    /** This command takes 10 seconds on the game clock. */
    static final int DURATION = 60;
    /** Format for this command */
    static private final String CMDFMT = "RETURN [to Town]";

    private final BuildingDisplayCiv _bdCiv;

    /*
     *CONSTRUCTOR(S) AND RELATED METHODS
     */

    /** Constructor called by the CommandFactory. There is no delay nor duration. */
    public CmdReturn(BuildingDisplayCiv bdCiv)
    {
        super("CmdReturn", DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
        _bdCiv = bdCiv;
    }


    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * 
     */
    @Override
    public boolean init(List<String> args)
    {
      if (args.isEmpty()) {
        return true;
      } else if (args.size() > 1 &&
                 args.get(0).equalsIgnoreCase("to") &&
                 args.get(1).equalsIgnoreCase("town")) {
        return true;
      } else {
        return false;
      }
    }

    /**
     * Returns hero to the town.
     * 
     * @return true if successful return to town
     */
    @Override
    public boolean exec()
    {
        _bdCiv.openTown();
        return true;
    }

} // end CmdReturn class

