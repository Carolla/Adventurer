/**
 * Town.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc;

import java.util.ArrayList;
import java.util.List;

import chronos.pdc.buildings.Building;
import mylib.dmc.IRegistryElement;

/**
 * A Town is the thin container for all Buildings, and some global settings.
 * 
 * @author Alan Cline
 * @version Feb 6, 2013 // original <br>
 *          May 4, 2013 // updated and augmented <br>
 *          Jan 16, 2015 // replaced Building objects with their names only <br>
 */
public class Town implements IRegistryElement
{
  /** Name of the town */
  private String _name;
  /** Description of the town when first entered during the day */
  private String _descDay;
  /** Description of the town when first entered during the night */
  private String _descNight;

  /** The name of the Buildings in Town */
  private List<Building> _buildings;

  private final String ERR_NULL_NAME = "The town must have a name; cannot be null or empty";
  private final String ERR_NULL_DESC = 
      "The town must have a daytime description; cannot be null or empty";
  
  // ================================================================================
  // CONSTRUCTOR AND HELPERS
  // ================================================================================
  
  /**
   * Constructor. If the descNight is null, the descDay value is used. Some rare towns do not have
   * night time descriptions, so null values are ok.
   * 
   * @param name of the town
   * @param descDay appearance of the town when it is first entered in day
   * @param descNight appearance of the town when it is first entered in evening; can be null
   * @throws NullPointerException if name or descDay is null
   */
  public Town(String name, String descDay, String descNight)
  {
    // Guard: Name is required
    if ((name == null) || (name.isEmpty())) {
      throw new IllegalArgumentException(ERR_NULL_NAME);
    }
    // Guard: Daytime description is required
    if ((descDay == null) || (descDay.isEmpty())) {
      throw new IllegalArgumentException(ERR_NULL_DESC);
    }
    _name = name;
    _descDay = descDay;
    _descNight = descNight;
    _buildings = new ArrayList<Building>();
  }


  /**
   * Add the names of {@code Building}s that are in the Town after verifying each one from the
   * {@code BuildingRegistry}.
   * 
   * @param bldgList list of building names to add
   */
  public void addBuildings(List<Building> bldgList)
  {
    _buildings.addAll(bldgList);
  }

  
  // ================================================================================
  // PUBLIC METHODS
  // ================================================================================
  
  /** Get a list of all the Buildings in the town */
  public List<Building> getAllBuildings()
  {
    return _buildings;
  }

  /** Get the appearance of this town during the day */
  public String getDayDescription()
  {
    return _descDay;
  }

  /*
   * @see mylib.dmc.IRegistryElement#getKey()
   */
  @Override
  public String getKey()
  {
    return _name;
  }

  /** Get the appearance of this town at night */
  public String getNightDescription()
  {
    return _descNight;
  }

  /** Get the name of this town */
  public String getName()
  {
    return _name;
  }

  /** Get the name of the town */
  public String toString()
  {
    return _name;
  }

  // ================================================================================
  // SPECIALITY: PRIMITIVE SUPPORT METHODS
  // ================================================================================

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Town other = (Town) obj;
    if (_name == null) {
      if (other._name != null)
        return false;
    } else if (!_name.equals(other._name))
      return false;
    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_name == null) ? 0 : _name.hashCode());
    return result;
  }

  
  // ================================================================================
  // PRIVATE METHODS
  // ================================================================================

} // end of Town class

