/**
 * intCmdPatronEnter.java Copyright (c) 2018, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */

package chronos.pdc.command;

import java.util.List;

import chronos.pdc.NPC;
import chronos.pdc.buildings.Inn;

/**
 * This internal command causes a Patron to enter the Inn. The dungeon Inn and the Room is a
 * common reference contained in the Command base class, so is not needed to be passed in
 * through the arglist.
 * 
 * @author Alan Cline
 * @version Nov 5 2005 // Original <br>
 *          Jul 28, 2007 // Add the randomized Patrons to init() <br>
 *          Oct 20, 2007 // Updated with new Command package after dgnBuilder and DgnRunner
 *          merged <br>
 *          Jul 5, 2008 // Final commenting for Javadoc compliance<br>
 *          Jun 15, 2018 // Revised as needed for new test file <br>
 * 
 * @see Command
 */
public class intCmdPatronEnter extends Command
{
  // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
  /** Moves a Patron into the Room for a certain amount of time. */
  static final String CMD_DESCRIPTION =
      "Move a Patron into the Room for a certain amount of time.";
  /** This command starts immediately, requiring no delay. */
  static final int DELAY = 0;
  /** This command uses up 10 seconds on the game clock. */
  static final int DURATION = 10;

  /** Since the Patron is included in the Command during init, no args are needed */
  private final int NBR_ARGS = 0;

  /** The Patron who is designated to enter the Inn. */
  private final NPC _npc;
  private final Inn _inn;


  // ---------------------------------------------------------------------------
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ---------------------------------------------------------------------------

  /**
   * Overrides default constructor for non-standard internal attributes.
   * 
   * @param delay the length of time before it is executed (Patron is added to Room)
   * @param duration the length of time before the Patron leaves again (spawns CmdLeave)
   * @param inn
   * @param npc
   */
  public intCmdPatronEnter(int delay, int duration, NPC npc, Inn inn)
  {
    super("intCmdEnter", delay, duration, CMD_DESCRIPTION, null);
    _npc = npc;
    _inn = inn;
  }


  // ---------------------------------------------------------------------------
  // PUBLIC METHODS
  // ---------------------------------------------------------------------------

  /**
   * Gets the name of the Patron from the args and calls the PatronRegistry to get the Patron
   * object.
   * 
   * @param args args[0] = name of Patron to add to the current Room
   * @return false if invalid parm list
   */
  @Override
  public boolean init(List<String> args)
  {
    if (args.size() == NBR_ARGS) {
      // Copy the element into the command's array, don't set the reference
      // _parms.add(0, args.get(0));
      return true;
    } else {
      System.err.println("INTERNAL:  intCmdEnter.init() missing Patron parm");
      return false;
    }
  }


  /**
   * Retrieves the Patron object using the Patron's name, then puts him/her into the dungeon
   * Inn for the first time.
   * 
   * @return true if command works
   */
  @Override
  public boolean exec()
  {
    return _inn.add(_npc);
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
   * Get the Patron inside this command to which it applies.
   * 
   * @return the Patron internal to the command
   */
  public NPC getNPC()
  {
    return _npc;
  }

  @Override
  public String toString()
  {
    return _npc.getName() + " will enter the Inn in " + _delay;
  }
  
  
} // end of intCmdEnter class

