/**
 * Stables.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.buildings;

import mylib.ApplicationException;

/**
 * The Stables is where a Hero can rent horses, sleep for the night, and find menial labor
 * 
 * @author Alan Cline
 * @version Aug 9, 2017 // original <br>
 */
public class Stables extends Building
{
  /** Name of this fine establishment */
  public static final String DEFAULT_NAME = "Larry's Livery";
  /** Owner of this fine establishment */
  public static final String DEFAULT_OWNER = "Larry";
  /** Stables */
  static private final String HOVERTEXT = "The Stables. You can sleep or work here";
  /** What appears as one enters the building */
  public static final String DEFAULT_EXTERIOR =
      "The Stables is a large, two-story wooden building with a few stalls for horses and ponies.";
  /** What one senses when looking around the inside of the Inn when few patrons are here. */
  public static final String DEFAULT_INTERIOR =
      "You can see six stalls, one with a horse and one with a pony. A ladder to the side leads "
          + "to a hay-covered loft. An old wooden cart is in an large openspace near the large "
          + "double-wide doors.";

  /** Paths to the images for this building **/
  static private final String EXTERIOR_IMAGE = "ext_stables.JPG";
  static private final String INTERIOR_IMAGE = "int_stables.jpg";


  /** Stables are open 24-hours. */
  static private final int OPENTIME = 0001;   // cannot use 0000
  static private final int CLOSETIME = 2400;


  // ==============================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ==============================================================================

  /**
   * Default Constructor creates the default Jail
   * 
   * @throws ApplicationException if the ctor fails
   */
  public Stables() throws ApplicationException
  {
    super(DEFAULT_NAME, DEFAULT_OWNER, OPENTIME, CLOSETIME, HOVERTEXT, DEFAULT_EXTERIOR,
        DEFAULT_INTERIOR, EXTERIOR_IMAGE, INTERIOR_IMAGE);
    // setBusinessHours(OPENTIME, CLOSETIME);
  }


} // end of Stables class

