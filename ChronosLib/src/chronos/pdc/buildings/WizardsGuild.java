/**
 * WizardsGuild.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.buildings;

import mylib.ApplicationException;

/**
 * Wizards'Guild for spells, magical items, quests, and lodging. The default constructor creates the
 * default "Arcaneum".
 * 
 * @author Alan Cline
 * @version April 17, 2013 // original <br>
 */
public class WizardsGuild extends Building
{
  // Data to initialize the default Store; must be static because it is used in constructor
  /** Name of this fine establishment */
  static private final String GUILD_NAME = "Arcaneum";
  /** Owner of this fine establishment */
  static private final String OWNER = "Pendergast";
  /** Wizards' Guild */
  static private final String HOVERTEXT = "Wizards' Guild for magic, spells, and enchantments";
  /** What appears as one enters the building */
  static private final String EXTERIOR =
      "As you round the corner, it is as if you have stepped into another world. The landscape is "
          + "bathed in a pearl-white luminescence. A white marble orb on the hillside contains a "
          + "surreal bubble of light and twisted images. ";
  /** For this case, a non-Guild member cannot enter */
  static private final String INTERIOR =
      "A tall shrouded figure appears suddenly in front of three doors of colored light as you "
          + "try to enter. He says, \"Only Guildmembers may enter the Arcaneum.\"";

  /** Paths to the images for this building **/
  static private final String EXTERIOR_IMAGE = "ext_Arcaneum.jpg";
  static private final String INTERIOR_IMAGE = "int_Arcaneum.jpg";

  /** The Arcaneum opens at noon and closes at 8pm */
  static private final int OPENTIME = 1200;
  static private final int CLOSETIME = 2000;


  // ================================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ================================================================================

  /**
   * Default Constructor, create Inn with default data
   * 
   * @throws ApplicationException if the ctor fails
   */
  public WizardsGuild() throws ApplicationException
  {
    super(GUILD_NAME, OWNER, OPENTIME, CLOSETIME, HOVERTEXT, EXTERIOR, INTERIOR, EXTERIOR_IMAGE,
        INTERIOR_IMAGE);
    // setBusinessHours(OPENTIME, CLOSETIME);
  }


} // end of Wizards Guild class
