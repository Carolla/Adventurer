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
 * @version April 17, 2013 // original <br>
 */
public class RoguesGuild extends Building
{
  // Data to initialize the default Store; must be static because it is used in constructor
  /** Name of this fine establishment */
  public static final String DEFAULT_GUILD_NAME = "Rouge's Tavern";
  /** Owner of this fine establishment */
  public static final String DEFAULT_OWNER = "Ripper";
  /** Thieve's Guild */
  static private final String HOVERTEXT = "Where all the thieves party";
  /** What appears as one enters the building */
  public static final String DEFAUL_TEXTERIOR =
      "A rowdy bar filled with unseemly characters of the night " +
          "obscures these headquarters for thieves and murderers.";
  /** For this case, a non-Guild member cannot enter */
  public static final String DEFAULT_INTERIOR =
      "The grizzled barman opens a secret door behind a large " +
          "cask of wine. He motions you to proceed into the darkness. As you step inside, you " +
          "hear laughter and can make out more seedy characters rolling dice, throwing short " +
          "daggers at a dart board, and practicing picking locks. ";

  /** Paths to the images for this building **/
  static private final String EXTERIOR_IMAGE = "ext_RoguesDen.jpg";
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
    super(DEFAULT_GUILD_NAME, DEFAULT_OWNER, HOVERTEXT, DEFAUL_TEXTERIOR, DEFAULT_INTERIOR,
        EXTERIOR_IMAGE, INTERIOR_IMAGE);
    setBusinessHours(OPENTIME, CLOSETIME);
  }
} // end of Rogues Guild class
