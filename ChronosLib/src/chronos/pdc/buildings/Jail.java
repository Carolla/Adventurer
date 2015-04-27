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
import mylib.dmc.IRegistryElement;

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
  static private final String JAIL_NAME = "Jail";
  /** Owner of this fine establishment */
  static private final String OWNER = "The Sheriff";
  /** Town Jail */
  static private final String HOVERTEXT = "The Jail. You'll end up here if you cause trouble";
  /** What appears as one enters the building */
  static private final String EXTERIOR =
      "The Jail is not much more than a stone box with a heavy wooden door in its side.";
  /** What one senses when looking around the inside of the Inn when few patrons are here. */
  static private final String INTERIOR = "A hallway extends past the Sherriff's area down to " +
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
    super(JAIL_NAME, OWNER, HOVERTEXT, EXTERIOR, INTERIOR, EXTERIOR_IMAGE, INTERIOR_IMAGE);
    setBusinessHours(OPENTIME, CLOSETIME);
  }


  /**
   * Constructor for typical inn with default business hours and no busy description
   * 
   * @param name of this building
   * @param master who runs this building
   * @param hoverText quick phrase for purpose of building
   * @param intro first glance outside, or when entering
   * @param desc detailed look of building, inside or out
   * @throws ApplicationException if the ctor fails
   */
  public Jail(String name, String master, String hoverText, String intro, String desc)
      throws ApplicationException
  {
    super(name, master, hoverText, intro, desc);
  }


  /*
   * Two Jails are considerd equal if their name and building masters are equal
   * 
   * @see mylib.dmc.IRegistryElement#equals(mylib.dmc.IRegistryElement)
   */
  @Override
  public boolean equals(IRegistryElement target)
  {
    if (target == null) {
      return false;
    }
    Jail bank = (Jail) target;
    boolean bName = this.getKey().equals(bank.getKey());
    boolean bMaster = this.getMaster().getName().equals(bank.getMaster().getName());
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


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR(S) AND RELATED METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PUBLIC METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ INNER CLASS: MockJail
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  public class MockJail
  {
    /** default ctor */
    public MockJail()
    {}

    /** Get the building's name */
    public String getName()
    {
      return _name;
    }


    /** Get the intro and description statements */
    public String[] getDescs()
    {
      String[] s = new String[3];
      s[0] = _extDesc;
      s[1] = _intDesc;
      return s;
    }

  } // end of MockJail inner class


} // end of Jail class

