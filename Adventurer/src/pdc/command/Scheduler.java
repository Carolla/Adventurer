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
import java.util.NoSuchElementException;

import pdc.GameClock;
import civ.CommandParser;


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
  /** Internal references: list for command parms to be passed */
  private ArrayList<String> _parms = null;
  /** Internal references: get next user commands */
  private CommandParser _cp = null; // needed to get next User Command


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */
  /** Internal reference to ensure singleton */
  private static Scheduler _sched = null;


  /**
   * Creates its component delta list and an ArrayList of Strings to handle command parameters.
   */
  private Scheduler()
  {
    _dq = new DeltaCmdList();
    _parms = new ArrayList<String>();
  }


  /**
   * Creates the {@code Scheduler} and its components. However, if a {@code Scheduler} does not
   * exist, it cannot be created without a {@code CommandParser} (parm), so null is returned.
   * 
   * @param parser bidirectional association for callbacks to get a user {@code Command} object for
   *        scheduling.
   * @return Scheduler singleton with link to the {@code CommandParser}.
   */
  public static synchronized Scheduler getInstance(CommandParser parser)
  {
    if (_sched == null) {
      _sched = new Scheduler();
      _sched._cp = parser;
    }
    return _sched;
  }


//  /**
//   * Gets an existing {@code Scheduler} reference, which will be used to schedule commands triggered
//   * by other commands. If the {@code Scheduler} doesn't exist, an error message is displayed.
//   * 
//   * @return Scheduler reference to singleton object
//   */
//  public static synchronized Scheduler getInstance()
//  {
//    if (_sched == null) {
//      System.err.println("The Scheduler object does not yet exist.");
//    }
//    return _sched;
//  }


  /*
   * PUBLIC METHODS
   */

  /**
   * This is the main loop to pop the next command and execute it, and required for the
   * {@code Runnable} interface. The {@code Scheduler} is initialized with an {@code intEndCmd} to
   * start off the loop. When {@code intEndCmd} is found, the {@code CommandParser} is called to get
   * the next user command. When a user command is found, the {@code Scheduler} calls its
   * {@code Command.exec()} method, which actually triggers the subcommand's {@code exec()} because
   * {@code Command} is abstract.
   */
  public void run()
  {
//    System.out.println("Scheduler started");
    // Put on a 0-delay command to trigger collecting user commands
    sched(makeCmdEnd(0));

    // Unless the system interrupts this thread for some reason, it continues until the Quit
    // command
    try {
      doCommands();

      // If the thread is interrupted for some reason, we want to return and exit
    } catch (InterruptedException e) {
      System.err.println("Interrupted exception thrown while trying to doCommands "
          + e.getMessage());
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
   * Generally, loop through the deltaQ, retrieving commands and calling each {@code Command.exec()}
   * method.
   * @throws InterruptedException   required because a Thread sleeps here
   */
  private void doCommands() throws InterruptedException
  {
    // Loop through the deltaQ, retrieving commands and calling their exec() method.
    while (true) {
      Command cmdToDo = null;
      try {
        // Retrieve next Command in queue
        cmdToDo = _dq.getNextCmd();
        if (cmdToDo == null) {
          throw new IllegalStateException("Scheduler: DeltaCmdList is unexpectedly empty"); 
        }
        // If CmdEnd, then return to CommandParser to get another User command;
        // then add a CmdEnd to trigger a new user command after newCmd is executed
        if (cmdToDo.getName().equalsIgnoreCase(CMDEND) == true) {
          Command newUserCmd = _cp.getUserCommand();
          // Wait and cycle again if nothing has been input
          if (newUserCmd == null) {
            Thread.sleep(500);
            sched(makeCmdEnd(0));
            continue;
          }
          sched(newUserCmd);
          sched(makeCmdEnd(newUserCmd.getDuration() + newUserCmd.getDelay()));
          // dump();
        } else {
          cmdToDo.exec();
        }
      } catch (NoSuchElementException e) {
        throw new IllegalStateException(e.getMessage()); 
      }
      
    } // end of while loop
  }


  /**
   * Make an {@code intCmdEnd} to trigger a new user command. {@code intCmdEnd}s are used as Events
   * to end a {@code Command}. They have a delay equal to the previous Command's duration, and no
   * duration of their own.
   * 
   * @param delay this {@code intCmdEnd}'s delay
   * @return {@code intCmdEnd} to trigger getting a new user command
   */
  private Command makeCmdEnd(int delay)
  {
    // Create endCmd to signal to get another user command
    Command endCmd = new intCmdEnd(delay);
    _parms.clear(); 
    endCmd.init(_parms);
    return endCmd;
  }


  /**
   * Dump the delta list for debugging purposes.
   */
  public void dump()
  {
    System.err.println("\nScheduler DQ snapshot: ");
    _dq.dump();
  }


} // end Scheduler class
