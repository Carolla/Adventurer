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
import mylib.dmc.IRegistryElement;

/**
 * Wizards'Guild for spells, magical items, quests, and lodging. The default constructor creates the
 * default "Arcaneum".
 * 
 * @author Alan Cline
 * @version April 17, 2013 // original ,br>
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
  static private final String INTERIOR =
      "It is as if you have stepped into another world. The landscape is bathed in a pearl-white " +
          " luminescence. A white marble orb on the hillside contains a surreal bubble of lights " +
          "and twisted images. ";
  /** For this case, a non-Guild member cannot enter */
  static private final String EXTERIOR =
      "A tall shrouded figure appears suddenly in front of three doors of colored light as you "
          + "try to enter. He says, \"Only Guildmembers may enter the Arcaneum.\"";

  /** Paths to the images for this building **/
  static private final String EXTERIOR_IMAGE = "ext_Arcaneum.JPG";
  static private final String INTERIOR_IMAGE = "int_Arcaneum.jpg";

  /** The Arcaneum opens at noon and closes at 8pm */
  private int OPENTIME = 1200;
  private int CLOSETIME = 2000;


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Default Constructor, create Inn with default data
   * 
   * @throws ApplicationException if the ctor fails
   */
  public WizardsGuild() throws ApplicationException
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
  public WizardsGuild(String name, String master, String hoverText, String intro, String desc)
      throws ApplicationException
  {
    super(name, master, hoverText, intro, desc);
  }


  /*
   * PUBLIC METHODS
   */

  /*
   * Two Guilds are considered equal if their name and building masters are equal
   * 
   * @see mylib.dmc.IRegistryElement#equals(mylib.dmc.IRegistryElement)
   */
  @Override
  public boolean equals(IRegistryElement target)
  {
    // Guards against null target
    if (target == null) {
      return false;
    }
    WizardsGuild wizG = (WizardsGuild) target;
    boolean bName = this.getKey().equals(wizG.getKey());
    boolean bMaster = this.getMaster().getName().equals(wizG.getMaster().getName());
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
   * PRIVATE METHODS
   */


  /*
   * INNER CLASS: MockStore
   */

  public class MockWizardsGuild
  {
    /** default ctor */
    public MockWizardsGuild()
    {}

    public String getDescrption()
    {
      return _intDesc;
    }

    public String getIntro()
    {
      return _extDesc;
    }


  } // end of MockWizardsGuild inner class


} // end of Wizards Guild class
