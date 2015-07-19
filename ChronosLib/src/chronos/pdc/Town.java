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

import mylib.ApplicationException;
import mylib.dmc.IRegistryElement;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

/**
 * A Town is the thin container for all Buildings, and some global settings.
 * 
 * @author Alan Cline
 * @version Feb 6, 2013 // original <br>
 *          May 4, 2013 // updated and augmented <br>
 *          Jan 16, 2015  // replaced Building objects with their names only <br>
 */
public class Town implements IRegistryElement
{
  /** Name of the town */
  private String _name;
  /** Description of the town when first entered during the day */
  private String _descDay;
  /** Description of the town when first entered during the night */
  private String _descNight;
  /** Standard cost of living factor. Multiplier for all prices in town. */
  private double _costOfLiving = 1.0;

  /** The name of the Buildings in Town */
  private ArrayList<String> _buildings;

  
  /**
   * Global clock time, in seconds, for the states of the city, arena, buildings, and Hero.
   */
  // TODO: Make this clock an object later
  /*
   * TODO: The clock is triggered to start based on the Inn, which must always be open when the Hero
   * starts. The clock will start randomly between 1 hr after the Inn opens, and 1 hr before the Inn
   * closes.
   */
//  private long _gameClock = 0L;
//  /** Fixed start time when Town awakes: 6am */
//  private int INIT_TIME = 6;

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /** Default constructor */
  public Town()
  {}

  
  /**
   * Constructor. If the descNight is null, the descDay value is used. Some rare towns do not have
   * night time descriptions, so null values are ok.
   * 
   * @param name of the town
   * @param descDay appearance of the town when it is first entered in day
   * @param descNight appearance of the town when it is first entered in evening; can be null
   * @throws NullPointerException if name or descDay is null
   */
  public Town(String name, String descDay, String descNight) throws NullPointerException
  {
    // Guard against null parms
    if ((name == null) || (descDay == null)) {
      throw new NullPointerException("Name or daytime description cannot be null");
    }
    // Set the field data
    _name = name;
    _descDay = descDay;
    _descNight = (descNight == null) ? descDay : descNight;
    // Initialize running clock to INIT_TIME
//    _gameClock = INIT_TIME * Constants.SECS_PER_HOUR;
    
    // Create a place to store in building in town */
    _buildings = new ArrayList<String>();
  }

  /*
   * PUBLIC METHODS
   */

  /**
   * Add the names of {@code Building}s that are in the Town after verifying each one from the
   * {@code BuildingRegistry}.
   * 
   * @param bldgList list of building names to add
   * @throws ApplicationException if building cannot be found
   */
  public void addBuildings(ArrayList<String> bldgList) throws ApplicationException
  {
    BuildingRegistry bReg = 
        (BuildingRegistry) RegistryFactory.getInstance().getRegistry(RegKey.BLDG);
    for (String bName : bldgList) {
      IRegistryElement b = bReg.getUnique(bName); 
      if (b == null) {
        throw new ApplicationException("Cannot find " + bName + " in BuildingRegistry.");
      } else {
        Building bldg = (Building) b;
        _buildings.add(bldg.getName());
      }
    }
  }

  
//  /**
//   * Add a building to the Town list for retrieval for the Building Registry after verifying that
//   * the Building exists in the Building registry
//   * 
//   * @param bldgName of the building to add
//   * @return true if the add was successful
//   */
//  public boolean buildingRegContainsName(String bldgName)
//  {
//    Building b = _bReg.getBuilding(bldgName);
//    return b != null;
//  }

  /** Two Towns are equal if their names are equal */
  @Override
  public boolean equals(IRegistryElement targetTown)
  {
    Town target = (Town) targetTown;
    return _name.equalsIgnoreCase(target.getName());
  }

  /** Get a list of all the Buildings in the town */
  public ArrayList<String> getAllBuildings()
  {
    return _buildings;
  }

  /** Get a Building object from the Building Registry */
  public Building getBuilding(String name)
  {
    BuildingRegistry bReg =
        (BuildingRegistry) RegistryFactory.getInstance().getRegistry(RegKey.BLDG);
    Building bldg = (Building) bReg.getUnique(name);
    return bldg;
  }

  /** Get the cost of living (price adjustment) for this town */
  public double getCostOfLiving()
  {
    return _costOfLiving;
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

  /** Get the name of this town */
  public String getName()
  {
    return _name;
  }

  /** Get the appearance of this town at night */
  public String getNightDescription()
  {
    return _descNight;
  }

  /**
   * Set the standard of living (price factor) for the town
   * 
   * @param col multiplier for price adjustment, but at least 0.1
   */
  public void setCostOfLiving(double col)
  {
    _costOfLiving = col;
  }

  /** Get the name of the town */
  public String toString()
  {
    return _name;
  }

  /*
   * PRIVATE METHODS
   */

} // end of Town class

