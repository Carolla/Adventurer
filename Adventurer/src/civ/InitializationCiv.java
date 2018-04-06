/**
 * InitializationCiv.java Copyright (c) 2018, Alan Cline. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */

package civ;

import chronos.pdc.buildings.Inn;
import chronos.pdc.command.Scheduler;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import hic.Mainframe;
import pdc.command.CommandFactory;

/**
 * Contains initial non-GUI start-up processes. It creates all dependencies, mostly the
 * registries, registry factory, and command parser needed for <it>Adventurer{\it}. It also
 * creates the HIC-CIV connector and passes control to the MainframeCiv.
 * 
 * @author Alan Cline
 * @version March 25, 2018 // refactored for better TestProxy support <br>
 */

public class InitializationCiv
{
  private MainframeCiv _mfCiv;
  private BuildingDisplayCiv _bldgCiv;
  private RegistryFactory _rf;
  private Scheduler _skedder;
  private CommandParser _parser;

  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /**
   * Create the Civs to display and handle the MainActionPanel of buttons
   * 
   * @param mfCiv handler for the mainframe
   */
  public InitializationCiv()
  {
    // Construct visible main frame and its support civ
    _mfCiv = new MainframeCiv(new Mainframe());
    
    // Create the registries 
    _rf = new RegistryFactory();
    _rf.initRegistries();
  }

  /** Create the registry factory and required registries for Adventurer */
  private void constructCoreMembers()
  {
    CommandFactory cmdFac = new CommandFactory(_bldgCiv, _mfCiv);
    cmdFac.initMap();
    _skedder = new Scheduler();

    CommandParser parser = new CommandParser(_skedder, cmdFac);

    // TODO This should go nearer the BuildingRegistry init or BuildingDisplayCiv
    // Set up times for the patrons to visit the Inn
    ((Inn) ((BuildingRegistry) _rf.getRegistry(RegKey.BLDG)).getBuilding("Ugly Ogre Inn"))
        .initPatrons(_skedder);

  }


  // ============================================================
  // Public methods
  // ============================================================

  /**
   * Load the selected adventure from the Adventure registry. Replace the opening button panel
   * with the IOPanel (text and command line)
   * 
   * @param adventureName selected from the Adventure by the user
   */
  public void loadSelectedAdventure(String adventureName)
  {}


} // end of InitializationCiv class
