/**
 * ClericsGuild.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.buildings;

import mylib.ApplicationException;

/**
 * Clerics' Guild for spells, magical items, quests, and lodging The default constructor creates the
 * default "Monastery".
 * 
 * @author Alan Cline
 * @version April 17, 2013 // original <br>
 */
public class ClericsGuild extends Building
{
  // Data to initialize the default Store; must be static because it is used in constructor
  /** Name of this fine establishment */
  public static final String DEFAULT_GUILD_NAME = "Monastery";
  /** Owner of this fine establishment */
  public static final String DEFAULT_OWNER = "Balthazar";
  /** Monastery */
  static private final String HOVERTEXT = "Clerical Guild for spiritual guidance and powers";
  /** What appears as one enters the building */
  public static final String DEFAULT_EXTERIOR = "Beautiful arches lead the way into a white stone" +
      " building. The entire area permeates peacefulness.";

  /** For this case, a non-Guild member cannot enter */
  public static final String DEFAULT_INTERIOR = "A cheerful looking man in a brown robe greets you. " +
      "His hood lays back on his shoulders. \"How can I serve you? \" he asks. ";

  /** Paths to the images for this building **/
  static private final String EXTERIOR_IMAGE = "ext_Monastery.JPG";
  static private final String INTERIOR_IMAGE = "int_Monastery.JPG";


  /** The Monastery opens at dawn (6am) and closes at dusk (7pm) */
  private int OPENTIME = 600;
  private int CLOSETIME = 1900;


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Default Constructor, create Inn with default data
   * 
   * @throws ApplicationException if the ctor fails
   */
  public ClericsGuild() throws ApplicationException
  {
    super(DEFAULT_GUILD_NAME, DEFAULT_OWNER, HOVERTEXT, DEFAULT_EXTERIOR, DEFAULT_INTERIOR, EXTERIOR_IMAGE, INTERIOR_IMAGE);
    setBusinessHours(OPENTIME, CLOSETIME);
  }
} // end of ClericsGuild class
