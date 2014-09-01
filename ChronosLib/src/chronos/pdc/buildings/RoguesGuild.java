/**
 * RoguesGuild.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.buildings;

import mylib.ApplicationException;

/**
 * Rogues' Guild for lock picking, skill practicing, and other tricks-of-the-sleeve The default
 * constructor creates the default "Rogues' Den".
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 April 17, 2013 // original
 *          <DD>
 *          </DL>
 */
public class RoguesGuild extends Building
{
  // Data to initialize the default Store; must be static because it is used in constructor
  /** Name of this fine establishment */
  static private final String GUILD_NAME = "Rouge's Den";
  /** Owner of this fine establishment */
  static private final String OWNER = "Ripper";
  /** Thieve's Guild */
  static private final String HOVERTEXT = "Where all the thieves party";
  /** What appears as one enters the building */
  static private final String EXTERIOR =
      "A rowdy bar filled with unseemly characters of the night " +
          "obscures these headquarters for thieves and murderers.";
  /** For this case, a non-Guild member cannot enter */
  static private final String INTERIOR = "The grizzled barman opens a secret door behind a large " +
      "cask of wine. He motions you to proceed into the darkness. As you step inside, you " +
      "hear laughter and can make out more seedy characters rolling dice, throwing short " +
      "daggers at a dart board, and practicing picking locks. The obvious leader of this pack " +
      "sits on a large wooden chair, almost like a throne. A particularly lovely half-dressed " +
      "woman sits on his lap. ";

  /** Paths to the images for this building **/
  static private final String EXTERIOR_IMAGE = "raw_ext_Rogues tavern.JPG";
  static private final String INTERIOR_IMAGE = "int_RoguesDen.jpg";

  /**
   * The Rogues' Den opens at 9pm and closes in the wee hours at 4am. This is a special case of
   * overnight hours when the Guild closed the day after it opens, continuing through midnight.
   */
  private int OPENTIME = 2100;
  private int CLOSETIME = 400;


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Default Constructor, create Inn with default data
   * 
   * @throws ApplicationException if the ctor fails
   */
  public RoguesGuild() throws ApplicationException
  {
    super(GUILD_NAME, OWNER, HOVERTEXT, EXTERIOR, INTERIOR, EXTERIOR_IMAGE, INTERIOR_IMAGE);
    setBusinessHours(OPENTIME, CLOSETIME);
  }


  /**
   * Constructor for typical general store with default business hours
   * 
   * @param name of this building
   * @param master who runs this building
   * @param hoverText quick phrase for purpose of building
   * @param intro first glance outside, or when entering
   * @param desc detailed look of building, inside or out
   * @throws ApplicationException if the ctor fails
   */
  public RoguesGuild(String name, String master, String hoverText, String intro, String desc)
      throws ApplicationException
  {
    super(name, master, hoverText, intro, desc);
  }


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PUBLIC METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /*
   * Two Guilds are considerd equal if their name and building masters are equal
   * 
   * @see mylib.dmc.IRegistryElement#equals(mylib.dmc.IRegistryElement)
   */
  @Override
  public boolean equals(Object target)
  {
    // Guards against null target
    if (target == null) {
      return false;
    }
    RoguesGuild thfG = (RoguesGuild) target;
    boolean bName = this.getKey().equals(thfG.getKey());
    boolean bMaster = this.getMaster().getName().equals(thfG.getMaster().getName());
    return (bName && bMaster);
  }


  /*
   * Get the key, which is the name of the Building
   * 
   * @see mylib.dmc.IRegistryElement#getKey()
   */
  @Override
  public String getKey()
  {
    return _name;
  }

  /**
   * Set the business hours for this Building. This override allows the closing time to be the day
   * after it opens.
   * 
   * @param open time building is available
   * @param closed time building is not available
   * @return false if parms are invalid
   */
  public boolean setBusinessHours(int open, int closed)
  {
    // Guards
    boolean bOpen = (open > 0) && (open < 2400) ? true : false;
    boolean bClose = (closed > 0) && (closed <= 2400) ? true : false;
    // boolean bFlip = (open < closed) ? true : false;
    // if ((bOpen && bClose && bFlip) == false) {
    if ((bOpen && bClose) == false) {
      return false;
    }
    // Clear out old values
    _openTime = open;
    _closingTime = closed;
    return true;
  }


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ INNER CLASS: MockStore
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  public class MockRoguesGuild
  {
    /** default ctor */
    public MockRoguesGuild()
    {}

    public String getDescrption()
    {
      return _intDesc;
    }

    public String getIntro()
    {
      return _extDesc;
    }


  } // end of MockRoguesGuild inner class


} // end of Wizards Guild class
