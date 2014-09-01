/**
 * BuildingRegistry.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
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
import mylib.MsgCtrl;
import mylib.dmc.IRegistryElement;
import mylib.pdc.Registry;
import chronos.Chronos;
import chronos.pdc.NPC;
import chronos.pdc.buildings.Bank;
import chronos.pdc.buildings.Building;
import chronos.pdc.buildings.ClericsGuild;
import chronos.pdc.buildings.FightersGuild;
import chronos.pdc.buildings.Inn;
import chronos.pdc.buildings.Jail;
import chronos.pdc.buildings.RoguesGuild;
import chronos.pdc.buildings.Store;
import chronos.pdc.buildings.WizardsGuild;

/**
 * All Town Buildings are collected here. The initlal (default) Registry contains 8 buildings.
 * 
 * @author Alan Cline
 * @version 1.0 April 20, 2013 // original <br>
 */
public class BuildingRegistry extends Registry
{
  /*
   * CONSTRUCTOR(S) AND RELATED METHODS 
   */

  /** Called by RegistryFactory class */
  protected BuildingRegistry()
  {
    super(Chronos.BuildingRegPath);
  }

  /** Called in testing */
  public BuildingRegistry(String testMode)
  {
    super();
  }

  /**
   * Creates the Building Registry with the default buildings and saves it
   */
  @Override
  public void initialize()
  {
    // Create each of the default buildings and save to registry
    // The constructors load the default data
    try {
      super.add(new WizardsGuild()); // Arcaneum
      super.add(new Jail()); // Jail
      super.add(new ClericsGuild()); // Monastery
      super.add(new Store()); // Rat's Pack
      super.add(new RoguesGuild()); // Rouge's Den
      super.add(new FightersGuild()); // Stadium
      super.add(new Bank()); // The Bank
      super.add(new Inn()); // Ugly Ogre Inn
    } catch (ApplicationException ex) {
      MsgCtrl.errMsgln(this, ex.getMessage());
    }
  }

  /**
   * Get all the Buildings of the registry, which will also include NPCs unfortunately as an element
   */
  @Override
  public int getNbrElements()
  {
    return getBuildingList().size();
  }

  /**
   * Retrieve a building by name from the building registry
   * 
   * @param name name of the building desired
   * @return the building; else null if not found
   */
  public Building getBuilding(String name)
  {
    List<IRegistryElement> buildingList = super.get(name);
    if (buildingList.size() == 0) {
      return null;
    }
    Building aBuilding = (Building) buildingList.get(0);
    return aBuilding;
  }

  /**
   * Retrieve only the Building objects from the Building Registry, which will contain both Building
   * and BuildingMaster (NPC) objects
   * 
   * @return the list of Buildings only
   */
  public ArrayList<Building> getBuildingList()
  {
    // Run the query to retrieve all buildings from the registry
    List<IRegistryElement> result = super.getAll();
    ArrayList<Building> bldgList = new ArrayList<Building>(result.size());
    for (int k = 0; k < result.size(); k++) {
      // Check against all building subtypes
      IRegistryElement elem = result.get(k);
      if (elem.getClass().equals(NPC.class) == false) {
        bldgList.add((Building) elem);
      }
    }
    return bldgList;
  }

} // end of BuildingRegistry class
