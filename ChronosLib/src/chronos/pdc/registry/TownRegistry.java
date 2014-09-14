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

import mylib.ApplicationException;
import mylib.dmc.IRegistryElement;
import mylib.pdc.Registry;
import chronos.Chronos;
import chronos.pdc.Town;

/**
 * Contains a collection of all Town objects
 * 
 * @author Alan Cline
 * @version Feb 6, 2013 // original <br>
 */
public class TownRegistry extends Registry
{
  /** List of 8 buildings to add to default Town (Guilds listed first) */
  static private final String[] DEF_BUILDING_LIST = {"Arcaneum", "Jail", "Monastery",
      "Rat's Pack", "Rouge's Den", "Stadium", "The Bank", "Ugly Ogre Inn"};

  // INFO: Types that correspond to the above names: Wizard's Guild, Jail, Cleric's Guild,
  // General Store, Thieves' Guild, Fighter's Guild, Bank, Inn

  // ==============================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ==============================================================================

  /**
   * Private ctor because this singleton is called from getInstance(). Registry filename is used for
   * database
   * 
   * @param init flag to initialize registry for default data if true
   */
  protected TownRegistry()
  {
    super(Chronos.TownRegPath);
  }

  // ==============================================================================
  // PUBLIC METHODS
  // ==============================================================================

  /**
   * Populate the TownRegistry with the default Town, and the names of the buildings and arena.
   * Confirm that the building names represent Registry elements, and that the arena is in its own
   * .dgn file.
   */
  @Override
  public void initialize()
  {
    /*
     * TODO Why call parmless ctor; call ctor with default data like other registries? Or use this
     * model for all registries, and move element default data out of Registries
     */
    // Create the default town and add it to the TownRegistry
    Town town = new Town();
    // Add the default buildings to it
    try {
      town.addBuildings(DEF_BUILDING_LIST);
    } catch (ApplicationException ex) {
      System.err.println(ex.getMessage());
    }
  }


  // /** Close db, destroy the dbReadWriter and set this registry to null
  // */
  // public void closeRegistry()
  // {
  // super.close();
  // // _thisReg = null;
  // }
  //
  // public void deleteRegistry()
  // {
  // super.delete();
  // // _thisReg = null;
  // }

  /**
   * Retrieve all Towns from the Town Registry
   * 
   * @return the list of Towns
   */
  public ArrayList<Town> getTownList()
  {
    // Run the query to retrieve all buildings from the registry
    List<IRegistryElement> result = super.getAll();
    ArrayList<Town> townList = new ArrayList<Town>(result.size());
    for (IRegistryElement e : result) {
      townList.add((Town) e);
    }
    return townList;
  }

  // ==============================================================================
  // PRIVATE METHODS
  // ==============================================================================


} // end of TownRegistry class

