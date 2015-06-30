/**
 * Store.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
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
 * Main building in town for buying and selling supplies. The default constructor creates the
 * default "The Rat's Pack".
 * 
 * @author Alan Cline
 * @version Jan 28, 2013 // original <br>
 */
public class Store extends Building
{
  // Data to initialize the default Store; must be static because it is used in constructor
  /** Name of this fine establishment */
  static private final String STORE_NAME = "Rat's Pack";
  /** Owner of the store */
  static private final String OWNER = "Dewey N. Howe";
  /** General Store */
  static private final String HOVERTEXT = "General store for buying and selling supplies";
  /** What appears as one approaches the building */
  static private final String EXTERIOR = "The sign over the door is in good repair. "
      + "A fresh coat of paint on the lettering spells out: \'General Store, " + OWNER
      + ", Proprietor. Est. 1066\', but all the townspeople still know this shop as the "
      + STORE_NAME + ".";

  /** What one sees when looking around the inside of the Inn when few patrons are here. */
  static private final String INTERIOR =
      "The store is crowded with crates of different sizes, shelves "
          + "loaded with bottles, packages, boxes, and miscellanea. Sacks of grain and flour rest "
          + "on the floor near the wall, coiled ropes hang on the walls. A cat sniffs around a "
          + "barrel of axe handles, hoping for prey. The proprietor, wearing a heavy burlap apron, "
          + "is arranging items below a counter with a pan balance on it.";

  /** Paths to the images for this building **/
  static private final String EXTERIOR_IMAGE = "ext_GeneralStore.jpg";
  static private final String INTERIOR_IMAGE = "int_GeneralStore.jpg";

  /** The Store opens at 6am and closes at 6pm */
  private int OPENTIME = 600;
  private int CLOSETIME = 1800;


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Default Constructor, creates the Store with default data
   * 
   * @throws ApplicationException if the ctor fails
   */
  public Store() throws ApplicationException
  {
    super(STORE_NAME, OWNER, HOVERTEXT, EXTERIOR, INTERIOR, EXTERIOR_IMAGE, INTERIOR_IMAGE);
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
  public Store(String name, String master, String hoverText, String intro, String desc)
      throws ApplicationException
  {
    super(name, master, hoverText, intro, desc);
  }


  /*
   * PUBLIC METHODS
   */

  /*
   * Two Inns are considerd equal if their name and building masters are equal
   * 
   * @see mylib.dmc.IRegistryElement#equals(mylib.dmc.IRegistryElement)
   */
  @Override
  public boolean equals(IRegistryElement target)
  {
    if (target == null) {
      return false;
    }
    Store store = (Store) target;
    boolean bName = this.getKey().equals(store.getKey());
    boolean bMaster = this.getMaster().getName().equals(store.getMaster().getName());
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
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ INNER CLASS: MockStore
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  public class MockStore
  {
    /** default ctor */
    public MockStore()
    {}

    public String getDescrption()
    {
      return _intDesc;
    }

    public String getIntro()
    {
      return _extDesc;
    }


  } // end of MockStore inner class


} // end of Inn class
