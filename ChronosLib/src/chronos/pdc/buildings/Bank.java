/**
 * Bank.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.buildings;


import mylib.ApplicationException;


/**
 * The Bank is a location for making wills, buying and selling valuables, and depositing wealth for
 * later.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Apr 8, 2013 // original
 *          <DD>
 *          </DL>
 */
public class Bank extends Building
{
  // Data to initialize the default Bank; must be static because it is used in constructor
  /** Name of this fine establishment */
  public static final String DEFAULT_BANK_NAME = "The Bank";
  /** Owner of this fine establishment */
  public static final String DEFAULT_OWNER = "J. P. Pennypacker";
  /** What appears as one enters the building */
  public static final String DEFAULT_EXTERIOR =
      "The Bank is more stylish than the other buildings in this town.";
  /** What one senses when looking around the inside of the Inn when few patrons are here. */
  public static final String DEFAULT_INTERIOR =
      "You face a baldish middle-aged man on the other side of an old counter about four feet high."
          + " A dark metal grill partitions his side of the counter from your side. A large man with a"
          + " large axe stands silently in the corner, eyeing you suspiciously.";

  /** Paths to the images for this building **/
  private static final String DEFAULT_HOVERTEXT = "The Bank, for deposits, wills, and loans";
  private static final String EXTERIOR_IMAGE = "ext_Bank.JPG";
  private static final String INTERIOR_IMAGE = "raw_int_Bank.jpg";


  /** The bank opens at 9am and closes at 3pm */
  private int OPENTIME = 900;
  private int CLOSETIME = 1500;


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Default Constructor creates the default Bank
   * 
   * @throws ApplicationException if the ctor fails
   */
  public Bank() throws ApplicationException
  {
    super(DEFAULT_BANK_NAME, DEFAULT_OWNER, DEFAULT_HOVERTEXT, DEFAULT_EXTERIOR, DEFAULT_INTERIOR,
        EXTERIOR_IMAGE, INTERIOR_IMAGE);
    setBusinessHours(OPENTIME, CLOSETIME);
  }
} // end of Bank class

