/*
 * intCmdPatronLeave.java
 * 
 * Copyright (c) 2007, Carolla Development, Inc. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, post on servers, to reuse, or to redistribute to lists, requires prior
 * specific permission and/or a fee. Request permission to use from Carolla Development, Inc. by
 * email: acline@carolla.com.
 */

package chronos.pdc.command;

import java.util.List;

import chronos.pdc.NPC;
import chronos.pdc.buildings.Inn;



/**
 * This internal command causes a Patron to exit the Inn. The dungeon Inn and the Room is a common
 * reference contained in the Command base class, so is not needed to be passed in through the
 * arglist.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>1.0 Nov 15 2007 // Original
 *          <DD>
 *          <DT>2.0 Dec 31 2007 // Moved this command into the latest command package
 *          <DD>
 *          <DT>2.1 Jul 5 2008 // Final commenting for Javadoc compliance
 *          <DD>
 *          </DL>
 * @see Command
 */
public class intCmdPatronLeave extends Command
{
    // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
    /** Causes a Patron to leave the room after a certain amount of time. */
    // static final String CMD_DESCRIPTION =
    static final String TMP_DESCRIPTION =
            "Cause a Patron to leave the room after a certain amount of time.";
    /** This default value is overwritten by the amount of time taken from CmdEnter. */
    // static final int DELAY = 0;
    /** This command uses up 10 seconds on the game clock. */
    static final int TMP_DURATION = 10;
    /**
     * If the Patron cannot leave for some reason, intCmdLeave is rescheduled for this amount of
     * time later.
     */
    static final int RE_DELAY = 30;

    /** Since the Patron is included in the Command during init, no args are needed */
    private final int NBR_ARGS = 0;

    /** The Patron who is designated to leave the Inn. */
    private NPC _npc;
    private final Inn _inn;

    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Causes a Patron to leave when Inn when his/her time is up. This command cannot be called by
     * the User; it is called by the Class class in the CommandFactory. Requiring intCmdEnter for
     * the constructor ensures that no Patron can leave the Inn without first entering it. Overrides
     * exec() but needs to copy attributes over from intCmdEnter.
     * 
     * @param ce intCmdEnter command to copy its attributes over this command
     */
    public intCmdPatronLeave(intCmdPatronEnter ce, Inn inn)
    {
        super("intCmdLeave", ce.getDuration() + ce.getDelay(), TMP_DURATION, TMP_DESCRIPTION, null);
        _npc = ce.getNPC();
        _inn = inn;
    }


    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    /**
     * Sets all attributes in addition to the ones not set by default in the abstract base class.
     * 
     * @param args String list: args[0] = name of Patron to add to the current Room
     * @return false if invalid parm list
     */
    public boolean init(List<String> args)
    {
        if (args.size() == NBR_ARGS) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isUserInput()
    {
      return false;
    }
    
    @Override
    public boolean isInternal()
    {
      return true;
    }
    
    /**
     * Causes the Patron to leave the room if he/she is not involved in conversation. If the Patron
     * is talking, then intCmdLeave is reset for another RE_DELAY time period.
     * 
     * @return true if command works
     */
    public boolean exec()
    {
        return _inn.remove(_npc);
    }
    

    @Override
    public String toString()
    {
      return _npc.getName() + " will leave the Inn in " + _delay;
    }

} // end of intCmdLeave class

