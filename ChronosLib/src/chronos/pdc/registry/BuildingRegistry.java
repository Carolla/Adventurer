/**
 * BuildingRegistry.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.registry;

import java.util.List;

import chronos.pdc.Chronos;
import chronos.pdc.buildings.Bank;
import chronos.pdc.buildings.Building;
import chronos.pdc.buildings.ClericsGuild;
import chronos.pdc.buildings.FightersGuild;
import chronos.pdc.buildings.Inn;
import chronos.pdc.buildings.Jail;
import chronos.pdc.buildings.RoguesGuild;
import chronos.pdc.buildings.Stables;
import chronos.pdc.buildings.Store;
import chronos.pdc.buildings.WizardsGuild;
import mylib.pdc.Registry;

/**
 * All Town Buildings are collected here. The initial (default) Registry contains 8 buildings.
 * 
 * @author Alan Cline
 * @version April 20, 2013 // original <br>
 *          July 15, 2017 // added Stable and Jewelry store to initialization <br>
 *          Aug 9, 2017 // updaed per QATool, and replaced Jewelry store with Stable <br>
 */
public class BuildingRegistry extends Registry<Building>
{

  // ========================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ========================================================================

  /** Called by RegistryFactory class */
  public BuildingRegistry()
  {
    super(Chronos.BuildingRegPath);
  }


  /**
   * Creates the Building Registry with the default buildings and saves it
   */
  @Override
  public void initialize()
  {
    super.add(new Inn()); // Ugly Ogre Inn
    super.add(new Store()); // Rat's Pack
    super.add(new Jail()); // Jail
    super.add(new Bank()); // The Bank
    super.add(new Stables()); // Larry's Livery
    super.add(new FightersGuild()); // Stadium
    super.add(new RoguesGuild()); // Rouge's Tavern
    super.add(new ClericsGuild()); // Monastery
    super.add(new WizardsGuild()); // Arcaneum
  }

  
  /**
   * Retrieve a building by name from the building registry
   * 
   * @param name of the building desired
   * @return the building; else null if not found
   */
  public Building getBuilding(String name)
  {
    Building b = get(name);
    return b;
  }

  /**
   * Retrieve only the Building objects from the Building Registry, which will contain both Building
   * and BuildingMaster (NPC) objects
   * 
   * @return the list of Buildings only
   */
  public List<Building> getBuildingList()
  {
    return getAll();
  }


} // end of BuildingRegistry class
