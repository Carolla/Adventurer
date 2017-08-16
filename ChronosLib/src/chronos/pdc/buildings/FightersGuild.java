/**
 * FightersGuild.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.buildings;

import mylib.ApplicationException;

/**
 * Fighters' Guild, for practice, quests, and lodging The default constructor creates the default
 * "Stadium".
 * 
 * @author Alan Cline
 * @version
 *          <DL>
 *          <DT>Build 1.0 Jan 28, 2013 // original
 *          <DD>
 *          </DL>
 */
public class FightersGuild extends Building
{
  // Data to initialize the default Store; must be static because it is used in constructor
  /** Name of this fine establishment */
  public static final String DEFAULT_GUILD_NAME = "Stadium";
  /** Owner of this fine establishment */
  public static final String DEFAULT_OWNER = "Aragon";
  /** Fighter's Guild */
  static private final String HOVERTEXT = "Fighters' Guild, for martial training and weaponry";
  /** What appears as one enters the building */
  public static final String DEFAULT_EXTERIOR =
      "You stand outside a large coliseum-style building. " +
          "You can see a courtyard through the entranceway, and hear the clashing " +
          "of sword practice inside.";
  /** What one senses when looking around the inside of the Inn when few patrons are here. */
  public static final String DEFAULT_INTERIOR =
      "The Guildmaster meets you at the archway into the Guild. " +
          "Tiers of doors lead to the squire and knight lodgings. Brawny men and women are " +
          "in the middle of heavy exercise, some armed with wooden swords, others with halberds, " +
          "pikes, axes, shields and armor.";

  /** Paths to the images for this building **/
  static private final String EXTERIOR_IMAGE = "ext_Stadium.JPG";
  static private final String INTERIOR_IMAGE = "int_Stadium.JPG";


  /** The Store opens at 6am and closes at 6pm */
  static private final int OPENTIME = 500;
  static private final int CLOSETIME = 1600;


  // ================================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ================================================================================

  /**
   * Default Constructor, create Inn with default data
   * 
   * @throws ApplicationException if the ctor fails
   */
  public FightersGuild() throws ApplicationException
  {
    super(DEFAULT_GUILD_NAME, DEFAULT_OWNER, OPENTIME, CLOSETIME, HOVERTEXT, DEFAULT_EXTERIOR,
        DEFAULT_INTERIOR, EXTERIOR_IMAGE, INTERIOR_IMAGE);
    // setBusinessHours(OPENTIME, CLOSETIME);
  }


} // end of FightersGuild class
