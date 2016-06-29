/**
 * CmdInspect.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc.command;

import java.util.List;

import chronos.pdc.NPC;
import chronos.pdc.buildings.Building;
import chronos.pdc.command.Command;
import civ.BuildingDisplayCiv;

public class CmdInspect extends Command
{
    // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
    /** The name of the command, mostly for error messages */
    static private final String CMD_NAME = "INSPECT";
    /** The description of what the command does, used in the {@code help()} method. */
    static private final String CMD_DESCRIPTION = "Get the near description of an npc.";
    /** Format for this command */
    static private final String CMDFMT = "INSPECT <NPC Name>";
    /** This command starts immediately, requiring no delay. */
    static private final int DELAY = 0;
    /** This command takes this number of seconds on the game clock. */
    static private final int DURATION = 5;

    /** NPC on which this command is run */
    private NPC _npc;
    /** Parm passed into init, can be multi-word. */
    private String _targetNpcName;
    /** Needed to get current building. */
    private BuildingDisplayCiv _bdCiv;

    public CmdInspect(BuildingDisplayCiv bdc)
            throws NullPointerException
    {
        super(CMD_NAME, DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
        _bdCiv = bdc;
    }

    /**
     * Takes passed in NPC name tokens, combining them into a single token
     *
     * @return single-word String NPC name
     */
    @Override
    public boolean init(List<String> args)
    {
        // Guard
        if (args == null) {
            return false;
        }
        boolean retVal = false;
        if (args.size() >= 1) {
            _targetNpcName = super.convertArgsToString(args).trim();
            retVal = !_targetNpcName.isEmpty();
        }
        return retVal;
    }

    @Override
    public boolean exec()
    {
        boolean retVal = false;
        Building cBuild = _bdCiv.getCurrBuilding();
        if (cBuild == null) {
            _output.displayErrorText("You need to go inside to inspect people.");
            return retVal;
        }
        _npc = null;
        
        if (_targetNpcName.equalsIgnoreCase(cBuild.getProprietor())) {
            _npc = cBuild.getProprietorNPC();
            _output.displayText(_npc.getNearDescription());
            retVal = true;
        } else {
            List<NPC> npcList = (List<NPC>) cBuild.getPatrons();
            for (NPC anNpc : npcList) {
                if (_targetNpcName.equalsIgnoreCase(anNpc.getName())) {
                    _npc = anNpc;
                    _output.displayText(_npc.getNearDescription());
                    retVal = true;
                    break;
                }
            }
        }
        if (retVal == false) {
            _output.displayText("I don't see " + _targetNpcName
                    + ".  Perhaps you should have yourself another look 'round to see who's here.");
        }
        return retVal;
    }
} // End CmdInspect
