/*
 * Scheduler.java
 * 
 * Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc.command;

import java.util.ArrayList;

import pdc.GameClock;


/**
 * This {@code Scheduler} singleton is implemented with {@code DeltaCmdList}, a delta priority queue
 * with {@code GameClock}. The {@code Scheduler} pops a command only from the front of the queue,
 * which increments the game clock by the duration of the {@code Command}. This class implements
 * {@code Runnable} because it runs in its own thread to give time to the windowing system.
 * 
 * @author Alan Cline
 * @version Aug 29 2006 // Original creation <br>
 *          Jan 27 2007 // Command Parser decoupled from Scheduler creation <br>
 *          Oct 26 2007 // Revised for merged DgnRunner and DgnBuild version of program <br>
 *          Apr 11 2008 // Move this class into a Runnable thread to run concurrently with the
 *          windowing system <br>
 *          Jul 3 2008 // Final commenting for Javadoc compliance <br>
 * 
 * @see pdc.command.DeltaCmdList
 * @see GameClock
 * @see hic.CommandParser
 * 
 */
public class Scheduler implements Runnable
{
    /**
     * Internal command token used on the DQ when one command is over and another one is needed.
     */
    private final String CMDEND = "intCmdEnd";

    /** Internal references: command events are queued here */
    private DeltaCmdList _dq = null;

    /*
     * CONSTRUCTOR(S) AND RELATED METHODS
     */

    /**
     * Creates the {@code Scheduler} and its components. However, if a {@code Scheduler} does not
     * exist, it cannot be created without a {@code CommandParser} (parm), so null is returned.
     * 
     * @param deltaCmdList bidirectional association for callbacks to get a user {@code Command} object
     *        for scheduling.
     * @return Scheduler singleton with link to the {@code CommandParser}.
     */
    public Scheduler(DeltaCmdList deltaCmdList)
    {
        _dq = deltaCmdList;
    }


    /*
     * PUBLIC METHODS
     */

    /**
     * This is the main loop to pop the next command and execute it, and required for the
     * {@code Runnable} interface. The {@code Scheduler} is initialized with an {@code intEndCmd} to
     * start off the loop. When {@code intEndCmd} is found, the {@code CommandParser} is called to
     * get the next user command. When a user command is found, the {@code Scheduler} calls its
     * {@code Command.exec()} method, which actually triggers the subcommand's {@code exec()}
     * because {@code Command} is abstract.
     */
    public void run()
    {
        // Unless the system interrupts this thread for some reason, it continues until the Quit
        // command
        // If the thread is interrupted for some reason, we want to return and exit
        while (true) {
            try {
                doOneCommand();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Interrupted exception thrown while trying to doCommands "
                        + e.getMessage());
            }
        }
    }


    /**
     * Wrap the {@code Command} in an Event wrapper and put it on the {@code DeltaQueue}.
     * {@code Command} and {@code intCmdEnd} both are pushed onto the DQ.
     * 
     * @param cmd the user-given {@code Command}
     */
    public void sched(Command cmd)
    {
        _dq.insert(cmd);
    }


    /*
     * PRIVATE METHODS
     */

    /**
     * Process a single loop of Scheduler and Command parsing activity, sleeping between each cycle.
     * Generally, loop through the deltaQ, retrieving commands and calling each
     * {@code Command.exec()} method.
     * 
     * @throws InterruptedException required because a Thread sleeps here
     */
    public void doOneCommand() throws InterruptedException
    {
        Command cmdToDo = _dq.getNextCmd();

        if (cmdToDo.getName().equalsIgnoreCase(CMDEND) == true) {
            sched(makeCmdEnd(1));
        } else {
            cmdToDo.exec();
        }
    } // end of while loop

    /**
     * Make an {@code intCmdEnd} to trigger a new user command. {@code intCmdEnd}s are used as
     * Events to end a {@code Command}. They have a delay equal to the previous Command's duration,
     * and no duration of their own.
     * 
     * @param delay this {@code intCmdEnd}'s delay
     * @return {@code intCmdEnd} to trigger getting a new user command
     */
    private Command makeCmdEnd(int delay)
    {
        // Create endCmd to signal to get another user command
        Command endCmd = new intCmdEnd(delay);
        endCmd.init(new ArrayList<String>());
        return endCmd;
    }
} // end Scheduler class
