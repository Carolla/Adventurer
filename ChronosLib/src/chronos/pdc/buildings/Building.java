/**
 * Building.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.buildings;

import mylib.ApplicationException;
import mylib.dmc.RegistryElement;
import chronos.pdc.NPC;
import chronos.pdc.registry.NPCRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

/**
 * Is base class for all Guilds: Magic-User, Cleric, Fighter, and Rogue; and for all town buildings:
 * Inn, General Store, Bank, and user-defined ones.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Jan 28, 2013 // original
 *          <DD>
 *          </DL>
 */
public abstract class Building extends RegistryElement
{
  /** Default Opening Time for all buildings */
  private final int DEFAULT_OPENHOURS = 900;
  /** Default Closing Time for all buildings */
  private final int DEFAULT_CLOSINGHOURS = 1800;
  /** Error message when trying to enter a building during off-hours */
  protected final String BUILDING_CLOSED = "Sorry, the %s is not open now. "
      + "Return during normal business hours between %s and %s.";

  /** Name of this building */
  protected String _name = null;

  /** The non-player character (NPC) who owns or manages this Building */
  protected NPC _buildingMaster = null;

  /** Buildings have a time in which they are open for business (military time). */
  protected int _openTime = 0;
  /** Buildings have a time in which they are not open for business (military time). */
  protected int _closingTime = 0;

  /** Short phrase of the purpose of the Building (hovertext when clicked) */
  protected String _hoverText = null;
  /** Short description of what the Hero first sees from outside the building. */
  protected String _intDesc = null;
  /** Short description of what the Hero first sees inside the building. */
  protected String _extDesc = null;

  /** Path to External Display Image **/
  protected String _externalImagePath = null;
  /** Path to Internal Display Image **/
  protected String _internalImagePath = null;

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   * ABSTRACT METHODS for Derived Classes
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
   * CONSTRUCTOR(S) AND RELATED METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */


  /**
   * Create the base building; called by the concrete Building object
   * 
   * @param name of this building
   * @param master owner or manager of the building
   * @param hoverText small phrase for purpose of the building
   * @param intro first glance of building
   * @param desc detailed description of building, usually once inside
   * @throws ApplicationException if NPC cannot be found
   **/
  protected Building(String name, String master, String hoverText, String exterior,
      String interior,
      String extImagePath, String intImagePath)
  {
    this(name, master, hoverText, exterior, interior);

    _externalImagePath = extImagePath;
    _internalImagePath = intImagePath;
  }

  protected Building(String name, String masterName, String hoverText, String exterior,
      String interior)
      throws ApplicationException
  {
    if ((name == null) || (masterName == null) || (exterior == null) || (interior == null)) {
      throw new ApplicationException("Null parms in Building ctor");
    }
    _name = name;
    _hoverText = hoverText;
    _extDesc = exterior;
    _intDesc = interior;
    // Set hours of operation, else return false for bad hours
    if (setBusinessHours(DEFAULT_OPENHOURS, DEFAULT_CLOSINGHOURS) == false) {
      throw new ApplicationException("Business hours are invalid.");
    }
    _buildingMaster = findBuildingMaster(masterName);
  }

  protected NPC findBuildingMaster(String masterName)
  {
    NPCRegistry npcReg = (NPCRegistry) RegistryFactory.getRegistry(RegKey.NPC);
    NPC master = npcReg.getNPC(masterName);
    if (master == null) {
      throw new ApplicationException(masterName + " does not exist in the NPC Registry.");
    }
    return master;
  }

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PUBLIC METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /**
   * Enter the building, but only during business hours
   * 
   * @param tod the current time of day to compare against business hours
   * @return description of building closed message
   */
  public String enter(int tod)
  {
    String s = null;
    int[] hrs = getBusinessHours();
    if ((tod > hrs[0]) && (tod < hrs[1])) {
      s = getInteriorDescription();
    } else {
      s =
          String.format(BUILDING_CLOSED, getName(), getMeridianTime(hrs[0]),
              getMeridianTime(hrs[1]));
    }
    return s;
  }


  /**
   * Return the business hours of operation
   * 
   * @return open time in [0]; closing time in [1]
   */
  public int[] getBusinessHours()
  {
    int[] hrs = new int[2];
    hrs[0] = _openTime;
    hrs[1] = _closingTime;
    return hrs;
  }

  /**
   * When does the Inn close for business?
   * 
   * @return closing hours in meridian time
   */
  public String getClosingTime()
  {
    return getMeridianTime(_closingTime);
  }


  /**
   * Called when the Hero first enters, or asks to look around inside the building
   * 
   * @return the inside description of the building
   */
  public String getInteriorDescription()
  {
    return _intDesc;
  }


  /**
   * Called when the Hero is looking at the building
   * 
   * @return the outside description of the building
   */
  public String getExteriorDescription()
  {
    return _extDesc;
  }


  /**
   * Called when the building needs to be displayed
   * 
   * @return the image for the outside of the building.
   */
  public String getExtImagePath()
  {
    return _externalImagePath;
  }

  /**
   * Get the text to display when user clicks on icon
   * 
   * @return the hover text
   */
  public String getHoverText()
  {
    return _hoverText;
  }

  /**
   * Called when the building needs to be displayed
   * 
   * @return the image for the inside of the building.
   */
  public String getIntImagePath()
  {
    return _internalImagePath;
  }

  /*
   * Gets the NPC object that runs this Building
   * 
   * @returns Gets the NPC object that runs this Building
   * 
   * @see mylib.dmc.IRegistryElement#getKey()
   */
  public NPC getMaster()
  {
    return _buildingMaster;
  }


  /**
   * Convert from military time to am-pm time String. Noon is 12 pm but returns "noon; midnight is
   * 12 am but returns "midnight".
   * 
   * @param time time in military time to nearest second
   * @return the String form in am/pm time; invalid times outside 0 - 2400 return null.
   */
  public String getMeridianTime(int time)
  {
    // Guard against invalid numbers
    if ((time > 2400) || (time < 0)) {
      return null;
    }
    // Handle special case of noon
    if (time == 1200) {
      return "Noon";
    }
    // Handle special case of midnight
    if ((time == 0) || (time == 2400)) {
      return "Midnight";
    }
    // Set AM or PM time
    String meridian = "AM";
    if (time > 1200) {
      time -= 1200;
      meridian = "PM";
    }
    // Convert int to StringBuffer for formatting
    StringBuffer sb = new StringBuffer(Integer.valueOf(time).toString());
    // Insert a colon in the third position from end
    int index = sb.length() - 2;
    sb.insert(index, ":");
    sb.append(" " + meridian);
    // Convert to String for returning
    String meridianTime = new String(sb);
    return meridianTime;
  }


  /**
   * Get the name of this building
   * 
   * @return the building name
   */
  public String getName()
  {
    return _name;
  }


  /**
   * When does the Inn open for business?
   * 
   * @return opening hours in meridian time
   */
  public String getOpeningTime()
  {
    return getMeridianTime(_openTime);
  }


  /**
   * Set the business hours for this Building. Must be between 0000 and 2400, and closing must be
   * later than opening. Midnight is considered to be 2400.
   * 
   * @param open time building is available
   * @param closed time building is not available
   * @return false if parms are invalid
   */
  public boolean setBusinessHours(int open, int closed)
  {
    // Guards
    boolean bOpen = (open > 0) && (open < 2400) ? true : false;
    boolean bClose = (closed > 0) && (closed <= 2400) ? true : false;
    boolean bFlip = (open < closed) ? true : false;
    if ((bOpen && bClose && bFlip) == false) {
      return false;
    }
    // Clear out old values
    _openTime = open;
    _closingTime = closed;
    return true;
  }

  /** Return the name of the building */
  public String toString()
  {
    return _name;
  }


} // end of abstract Building class
