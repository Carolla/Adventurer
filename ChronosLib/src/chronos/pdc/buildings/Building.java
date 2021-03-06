/**
 * Building.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.buildings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import chronos.pdc.NPC;
import chronos.pdc.registry.NPCRegistry;
import mylib.ApplicationException;
import mylib.dmc.IRegistryElement;


/**
 * Is base class for all Guilds: Magic-User, Cleric, Fighter, and Rogue; and for all town buildings:
 * Inn, General Store, Bank, and user-defined ones.
 * 
 * @author Alan Cline
 * @version Jan 28, 2013 // original <br>
 */
public abstract class Building implements IRegistryElement
{
  /** Default Buildings to initialize registry with */
  public static final String[][] DEFAULT_BUILDINGS = {{"Ugly Ogre Inn", "Bork"},
      {"Rat's Pack", "Dewey N. Howe"}, {"The Bank", "J. P. Pennypacker"},
      {"Stadium", "Aragon"}, {"Arcaneum", "Pendergast"}, {"Monastery", "Balthazar"},
      {"Rouge's Tavern", "Ripper"}, {"Jail", "The Sheriff"}, {"Larry's Livery", "Larry"}};

  public static final String MISSING_PERSON = "I don't see that person here";
  // /** Default Opening Time for all buildings */
  // private final int DEFAULT_OPENHOURS = 900;
  // /** Default Closing Time for all buildings */
  // private final int DEFAULT_CLOSINGHOURS = 1800;
  /** Error message when trying to enter a building during off-hours */
  protected final String BUILDING_CLOSED = "Sorry, the %s is not open now. "
      + "Return during normal business hours between %s and %s.";

  /** Name of this building */
  protected final String _name;

  /** The non-player character (NPC) who owns or manages this Building */
  protected final NPC _proprietor;

  // protected final String _buildingOwner;

  /** Buildings have a time in which they are open for business (military time). */
  protected int _openTime;
  /** Buildings have a time in which they are not open for business (military time). */
  protected int _closingTime;

  /** Short phrase of the purpose of the Building (hovertext when clicked) */
  protected final String _hoverText;
  /** Short description of what the Hero first sees from outside the building. */
  protected String _intDesc;
  /** Short description of what the Hero first sees inside the building. */
  protected final String _extDesc;

  /** Path to External Display Image **/
  protected final String _externalImagePath;
  /** Path to Internal Display Image **/
  protected final String _internalImagePath;
  protected final List<NPC> _patrons = new ArrayList<NPC>();
  private static final NPCRegistry _npcRegistry = new NPCRegistry();

  
  // ================================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ================================================================================

  /**
   * Create the base building; called by the concrete Building object
   * 
   * @param name of this building
   * @param masterName owner or manager of the building
   * @param openHours when this Building is open for business
   * @param closeHours when this Building is closed for business
   * @param hoverText small phrase for purpose of the building
   * @param exterior first glance of building
   * @param interior detailed description of building, usually once inside
   * @throws ApplicationException if NPC cannot be found
   **/
  protected Building(String name, String masterName, int openHours, int closeHours,
      String hoverText, String exterior, String interior, String extImagePath, String intImagePath)
  {
    if ((name == null) || (masterName == null) || (exterior == null) || (interior == null)) {
      throw new ApplicationException("Null parms in Building ctor");
    }
    _name = name;
    _proprietor = _npcRegistry.getNPC(masterName);
    if (_proprietor == null) {
      throw new ApplicationException("Cannot find proprietor in registry.");
    }
//    System.out.print("\t Creating Building " + _name);
//    System.out.println("\t owned by " + masterName);

    _hoverText = hoverText;
    _extDesc = exterior;
    _intDesc = interior;
    _externalImagePath = extImagePath;
    _internalImagePath = intImagePath;

    // Set hours of operation, else return false for bad hours
    if (setBusinessHours(openHours, closeHours) == false) {
      throw new ApplicationException("Business hours are invalid.");
    }
    
  }

  /*
   * PUBLIC METHODS
   */

  public boolean add(NPC npc)
  {
    return _patrons.add(npc);
  }

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
      s = String.format(BUILDING_CLOSED, getName(), getMeridianTime(hrs[0]),
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
   * Called when the Hero first enters, or asks to look around inside the building
   * 
   * @return the inside description of the building
   */
  public String getInteriorDescription()
  {
    String description = _intDesc;
    if (_patrons.size() > 0) {
      description += "\n\nYou see patrons inside:\n";
      for (NPC npc : _patrons) {
        description += npc.getName() + "\n\t" + npc.getFarDescription() + "\n\n";
      }
    }

    return description;
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


  /**
   * Get the lookup key for building
   * 
   * @return the building name as key
   */
  public String getKey()
  {
    return _name;
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

  public Collection<NPC> getPatrons()
  {
    return _patrons;
  }

  /**
   * @return the proprietor name associated with this building
   */
  public String getProprietor()
  {
    return _proprietor.getName();
  }

  /**
   * @return the proprietor NPC object
   */
  public NPC getProprietorNPC()
  {
    return _proprietor;
  }

  public boolean remove(NPC npc)
  {
    return _patrons.remove(npc);
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

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_proprietor == null) ? 0 : _proprietor.hashCode());
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Building other = (Building) obj;
    if (_proprietor == null) {
      if (other._proprietor != null)
        return false;
    } else if (!_proprietor.equals(other._proprietor))
      return false;
    if (_name == null) {
      if (other._name != null)
        return false;
    } else if (!_name.equals(other._name))
      return false;
    return true;
  }

  public String inspect(String target)
  {
    for (NPC npc : _patrons) {
      String name = npc.getName();
      if (name.equalsIgnoreCase(target)) {
        return npc.getFarDescription();
      }
    }
    return MISSING_PERSON;
  }

  public boolean contains(String target)
  {
    return _proprietor.getName().equalsIgnoreCase(target)
        || _patrons.contains(_npcRegistry.getNPC(target));
  }
} // end of abstract Building class
