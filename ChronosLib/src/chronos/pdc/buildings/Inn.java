/**
 * Inn.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.buildings;

import java.util.ArrayList;
import java.util.List;

import mylib.ApplicationException;
import mylib.Constants;
import mylib.pdc.MetaDie;
import chronos.pdc.NPC;
import chronos.pdc.command.Scheduler;
import chronos.pdc.command.intCmdPatronEnter;
import chronos.pdc.command.intCmdPatronLeave;
import chronos.pdc.registry.NPCRegistry;

/**
 * Main building in town for rest, food, conversation, and sometimes even a bar brawl. Heroes can be
 * temporarily banned from the Inn for fighting. The default constructor creates "Ugly Ogre Inn".
 *
 * @author Alan Cline
 * @version Jan 28, 2013 // original <br>
 */
public class Inn extends Building
{
  // Data to initialize the default Inn; must be static because it is used in constructor
  /** Name of this fine establishment */
  static private final String INN_NAME = "Ugly Ogre Inn";
  /** Owner of this fine establishment */
  static private final String INNKEEPER = "Bork";
  /** Inn */
  static private final String HOVERTEXT = "Center for social events, food, and lodging";
  /** What appears as one enters the building */
  static private final String EXTERIOR = "You stand outside the door beneath the old " +
      "weather-beaten sign, with words barely readable:  " + INN_NAME;
  /** What one senses when looking around the inside of the Inn when few patrons are here. */
  static private final String INTERIOR =
      "You find yourself inside a darkened and smokey "
          + "room. As your eyes adjust to the dimness, you see a burly bartender filling mugs from "
          + "behind a solid oak bar along the north wall. ";
  /** What one senses when looking around the inside of the Inn when many patrons are here. */
  static private final String BUSY_DESC =
      "Serving women are delivering food to the patrons at the "
          +
          "various tables scattered about. The clatter of crockery and the bustling of dinner "
          +
          "activity is loud, enough to make attempts at conversation difficult. " +
          "The patrons notice you, and suddenly it becomes much quieter. ";

  /** Paths to the images for this building **/
  static private final String EXTERIOR_IMAGE = "raw_ext_Ugly Ogre Inn.jpg";
  static private final String INTERIOR_IMAGE = "int_Inn.jpg";

  /** The Inn opens at 6am and closes at midnight */
  private int OPENTIME = 600;
  private int CLOSETIME = 2400;

  /** Patrons start entering right away */
  private final int MIN_DELAY = 1;
  /** Last Patron enters after no more than 6 hours */
  private final int MAX_DELAY = (int) (6 * Constants.SECS_PER_HOUR);

  /** Patrons stay at least 10 minutes */
  private final int MIN_DURATION = 10 * 60;
  /** Patrons never stay longer than 2 hour*/
  private final int MAX_DURATION = (int) (2 * Constants.SECS_PER_HOUR);

  /** Minimum number of patrons that indicate if the Inn is busy or not */
  private int NBR_PATRONS_TO_BE_BUSY = 3;

  /** Randomizer */
  private static final MetaDie _md = new MetaDie();

  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /**
   * Default Constructor, create Inn with default data
   * 
   * @throws ApplicationException if the ctor fails
   */
  public Inn() throws ApplicationException
  {
    super(INN_NAME, INNKEEPER, HOVERTEXT, EXTERIOR, INTERIOR, EXTERIOR_IMAGE, INTERIOR_IMAGE);
    setBusinessHours(OPENTIME, CLOSETIME);
  }


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   *                      PUBLIC METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /**
   * Assigns the <code>Patron</code>s' delay and duration, then randomly assigns a few
   * <code>Patrons</code> (MAX_STARTERS) a zero-delay so they are present when the <code>Hero</code>
   * enters. The "time-loaded" <code>Patron</code>s are then put on the <code>Scheduler</code> queue
   * for self-entering during runtime.<br>
   * 
   * @param md <code>MetaDie</code> random generator for the game
   * @param skedder <code>Scheduler</code> singleton for scheduling <code>Patron</code>s
   */
  public void initPatrons(Scheduler skedder)
  {
    NPCRegistry npcReg = new NPCRegistry();
    List<NPC> patrons = npcReg.getAll();
    patrons.remove(npcReg.getNPC(INNKEEPER)); // Bork is always here
    
    // The starterList has no zero-delay intCmdEnter commands, each containing the
    // Patron who shall enter at the designated delay time.
    List<intCmdPatronEnter> starterList = createStarterList(patrons);

    for (intCmdPatronEnter ce : starterList) {
      // Use the generated enter commands to create the leave commands
      intCmdPatronLeave cl = new intCmdPatronLeave(ce, this);
      skedder.sched(ce);
      skedder.sched(cl);
    }
  }

  /**
   * Creates an interim list <code>of intCmdEnter</code> commands with a random delay and duration
   * for all the <code>Patron</code>s in the <code>PatronRegistry</code>. Each intCmdEnter is
   * assigned a Patron to enter the Inn when it is their time. <br>
   * A CommandList is created instead of a Patron list because Events wrap Commands, not Patrons.
   * @param patrons 
   * 
   * @return List of random non-zero <code>intCmdEnter</code> commands for each <code>Patron</code>
   */
  private List<intCmdPatronEnter> createStarterList(List<NPC> patrons)
  {
    // Create a list to hold the Enter commands
    List<intCmdPatronEnter> cmdStarterList = new ArrayList<intCmdPatronEnter>();

    // Walk the PatronRegistry for all Patrons and assign their Enter commands
    for (NPC npc : patrons) {
      int delay = _md.getRandom(MIN_DELAY, MAX_DELAY);
      int duration = _md.getRandom(MIN_DURATION, MAX_DURATION);
      intCmdPatronEnter cmd = new intCmdPatronEnter(delay, duration, npc, this);

      // Set the arg list into the command; the Scheduler will call Command.exec()
      cmd.init(new ArrayList<String>());
      cmdStarterList.add(cmd);
    }

    return cmdStarterList;
  }

  @Override
  public boolean add(NPC npc)
  {
    boolean added = super.add(npc);

    if (_patrons.size() >= NBR_PATRONS_TO_BE_BUSY) {
      _intDesc = BUSY_DESC;
    }
    
    return added;
  }

  //TODO(timothyarm) When patrons leave, they should be rescheduled (if there is time?)
  @Override
  public boolean remove(NPC npc)
  {
    System.out.println(npc.getName() + " left the Inn");
    boolean removed = super.remove(npc);
    
    if (_patrons.size() < NBR_PATRONS_TO_BE_BUSY) {
      _intDesc = INTERIOR;
    }
    
    return removed;
  }

} // end of Inn class
