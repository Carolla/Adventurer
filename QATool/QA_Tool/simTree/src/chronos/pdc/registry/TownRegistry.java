/**
 * TownRegistry.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.registry;

import java.util.ArrayList;
import java.util.List;

import mylib.pdc.Registry;
import chronos.Chronos;
import chronos.pdc.Town;
import chronos.pdc.buildings.Building;

/**
 * Contains a collection of all Town objects
 * 
 * @author Alan Cline
 * @version Feb 6, 2013 // original <br>
 */
public class TownRegistry extends Registry<Town>
{
  /** Name of default town */
  private final static String TOWN_NAME = "Biljur'Baz";
  /** Description of town when entered in the daytime */
  private final static String DESC_DAY =
      "A country road divides a few dilapidated buildings from the "
          + "apple treees and grain fields on the other side. One of the larger buildings catches "
          + "your glance in the center of the town--the Inn.  You can also see a Bank, a Store, "
          + "and a few others. Larger, more foreboding buildings decorate the slopes around the town. "
          + "At the edge of town, nestled among a mountain foothill, is a huge and ominious "
          + "fortress of dark rock. Even by day, it says, \"Stay away.\"";

  /** Description of town when entered in the evening */
  private final static String DESC_NIGHT =
      "Yellow lights burn in a line along a country road, casting "
          + "more shadow than illumination. You see small squares of light from the windows of "
          + "largest grey building near the center of town--probably the town's Inn. ";

  /** List of 8 buildings to add to default Town (Guilds listed first) */
  public static final String[] DEF_BUILDING_LIST = {"Arcaneum", "Jail", "Monastery",
      "Rat's Pack", "Rouge's Tavern", "Stadium", "The Bank", "Ugly Ogre Inn"};
  private BuildingRegistry _buildingRegistry;


  // ==============================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ==============================================================================

  /**
   * Private ctor because this singleton is called from getInstance(). Registry filename is used
   * for database
   * 
   * @param init flag to initialize registry for default data if true
   */
  protected TownRegistry(BuildingRegistry buildingRegistry)
  {
    super(Chronos.TownRegPath);
    _buildingRegistry = buildingRegistry;
    if (_shouldInitialize) {
      initialize();
    }
  }

  // ==============================================================================
  // PUBLIC METHODS
  // ==============================================================================

  /**
   * Populate the TownRegistry with the default Town, and the names of the buildings and arena.
   * Confirm that the building names represent Registry elements.
   */
  @Override
  public void initialize()
  {
    // Create the default town
    Town town = new Town(TOWN_NAME, DESC_DAY, DESC_NIGHT);

    // Convert string[] to arraylist
    int bSize = DEF_BUILDING_LIST.length;
    List<Building> bldgList = new ArrayList<Building>(bSize);
    for (int k = 0; k < bSize; k++) {
      bldgList.add(_buildingRegistry.getBuilding(DEF_BUILDING_LIST[k]));
    }
    // Now add all buildings' names into the Town
    town.addBuildings(bldgList);

    // Add the default town to the TownRegistry
    super.add(town);
  }

  /**
   * Retrieve all Towns from the Town Registry
   * 
   * @return the list of Towns
   */
  public List<Town> getTownList()
  {
    return getAll();
  }
} // end of TownRegistry class
