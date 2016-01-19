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

import chronos.Chronos;
import chronos.pdc.buildings.Bank;
import chronos.pdc.buildings.Building;
import chronos.pdc.buildings.ClericsGuild;
import chronos.pdc.buildings.FightersGuild;
import chronos.pdc.buildings.Inn;
import chronos.pdc.buildings.Jail;
import chronos.pdc.buildings.RoguesGuild;
import chronos.pdc.buildings.Store;
import chronos.pdc.buildings.WizardsGuild;
import chronos.pdc.command.Scheduler;
import mylib.pdc.Registry;

/**
 * All Town Buildings are collected here. The initial (default) Registry contains 8 buildings.
 * 
 * @author Alan Cline
 * @version April 20, 2013 // original <br>
 */
public class BuildingRegistry extends Registry<Building>
{

  // ========================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ========================================================================

  /** Called by RegistryFactory class */
  protected BuildingRegistry()
  {
    super(Chronos.BuildingRegPath);
  }


  /**
   * Creates the Building Registry with the default buildings and saves it
   */
  public void initialize(Scheduler skedder)
  {
    // Create each of the default buildings and save to registry
    // The constructors load the default data
    Inn inn = new Inn();
    inn.initPatrons(skedder);
    super.forceAdd(inn); // Ugly Ogre Inn
    super.forceAdd(new Store()); // Rat's Pack
    super.forceAdd(new Jail()); // Jail
    super.forceAdd(new Bank()); // The Bank
    super.forceAdd(new FightersGuild()); // Stadium
    super.forceAdd(new RoguesGuild()); // Rouge's Tavern
    super.forceAdd(new ClericsGuild()); // Monastery
    super.forceAdd(new WizardsGuild()); // Arcaneum
  }

  @Override
  protected void initialize()
  {

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
    System.out.println("getBuilding " + name + " got " + System.identityHashCode(b));
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
