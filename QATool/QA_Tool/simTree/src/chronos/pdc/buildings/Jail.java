/**
 * Jail.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.buildings;

import mylib.ApplicationException;

/**
 * The Jail is where a Hero is contained until fines are paid, or he/she becomes conscious.
 * 
 * @author Alan Cline
 * @version Apr 21, 2013 // original <br>
 */
public class Jail extends Building
{
  // Data to initialize the default Jail; must be static because it is used in constructor
  /** Name of this fine establishment */
  public static final String DEFAULT_JAIL_NAME = "Jail";
  /** Owner of this fine establishment */
  public static final String DEFAULT_OWNER = "The Sheriff";
  /** Town Jail */
  static private final String HOVERTEXT = "The Jail. You'll end up here if you cause trouble";
  /** What appears as one enters the building */
  public static final String DEFAULT_EXTERIOR =
      "The Jail is not much more than a stone box with a heavy wooden door in its side.";
  /** What one senses when looking around the inside of the Inn when few patrons are here. */
  public static final String DEFAULT_INTERIOR = "A hallway extends past the Sherriff's area down to " +
      "a wooden door with a barred window facing you.";

  /** Paths to the images for this building **/
  static private final String EXTERIOR_IMAGE = "ext_jail.JPG";
  static private final String INTERIOR_IMAGE = "int_jail.jpg";


  /** The jail opens at 10am and closes at 6pm */
  private int OPENTIME = 1000;
  private int CLOSETIME = 1800;


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR(S) AND RELATED METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /**
   * Default Constructor creates the default Jail
   * 
   * @throws ApplicationException if the ctor fails
   */
  public Jail() throws ApplicationException
  {
    super(DEFAULT_JAIL_NAME, DEFAULT_OWNER, HOVERTEXT, DEFAULT_EXTERIOR, DEFAULT_INTERIOR, EXTERIOR_IMAGE, INTERIOR_IMAGE);
    setBusinessHours(OPENTIME, CLOSETIME);
  }
} // end of Jail class

