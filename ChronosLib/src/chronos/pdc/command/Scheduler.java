/**
 * Scheduler.java Copyright (c) 2008, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */

package chronos.pdc.command;

import chronos.pdc.GameClock;


/**
 * This {@code Scheduler} singleton is implemented with {@code DeltaCmdList}, a delta priority
 * queue with {@code GameClock}. The {@code Scheduler} pops a command only from the front of
 * the queue, which increments the game clock by the duration of the {@code Command}. This
 * class implements {@code Runnable} because it runs in its own thread to give time to the
 * windowing system.
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
 * @see civ.CommandParser
 * 
 */
public class Scheduler
{
  /** Internal references: command events are queued here */
  private DeltaCmdList _dq;

  /**
   * Creates the {@code Scheduler} and its components. However, if a {@code Scheduler} does not
   * exist, it cannot be created without a {@code CommandParser} (parm), so null is returned.
   * 
   * @param _mfCiv
   */
  public Scheduler()
  {
    _dq = new DeltaCmdList();
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
    if (cmd.isUserInput()) {
      doOneUserCommand();
    }
  }


  /**
   * Process a single loop of Scheduler and Command parsing activity, sleeping between each
   * cycle. Generally, loop through the deltaQ, retrieving commands and calling each
   * {@code Command.exec()} method.
   */
  public void doOneUserCommand()
  {
    GameClock clock = GameClock.getInstance();
    Command cmdToDo = _dq.getNextCmd();
    while (cmdToDo.isInternal()) {
      cmdToDo.exec();
      clock.increment(cmdToDo.getDuration());
      cmdToDo = _dq.getNextCmd();
    }
    cmdToDo.exec();
    clock.increment(cmdToDo.getDuration());
  }
  
  
} // end Scheduler class
