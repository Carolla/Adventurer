/**
 * IntegrationTest.java Copyright (c) 2018, Alan Cline. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */

package test.integ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.Adventure;
import chronos.pdc.buildings.Building;
import chronos.pdc.buildings.Inn;
import chronos.pdc.command.Scheduler;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.BuildingDisplayCiv;
import civ.CommandParser;
import civ.MainActionCiv;
import civ.MainframeCiv;
import hic.MainActionPanel;
import mylib.MsgCtrl;
import pdc.command.CommandFactory;


public class IntegrationTest
{
  /** List of valid Buildings that can be entered */
  protected static final List<String> _bldgs = new ArrayList<String>();
  protected static final Scheduler _skedder = new Scheduler();
  protected static final MainframeCiv _mfCiv = new MainframeCiv(new MainframeProxy());
  protected static final MainActionCiv _maCiv = new MainActionCiv(new MainActionPanel());
  protected static final RegistryFactory _regFactory = new RegistryFactory();

  protected static BuildingDisplayCiv _bldgCiv;
  protected static CommandFactory _cmdFac;
  protected static CommandParser _cp;

  @BeforeClass
  public static void setUpBeforeClass()
  {
    _regFactory.initRegistries();

    BuildingRegistry bReg = (BuildingRegistry) _regFactory.getRegistry(RegKey.BLDG);
    ((Inn) bReg.getBuilding("Ugly Ogre Inn")).initPatrons(_skedder);
    AdventureRegistry advReg = (AdventureRegistry) _regFactory.getRegistry(RegKey.ADV);
    Adventure adv = advReg.getAll().get(0);

    _maCiv.loadSelectedAdventure(adv.getName());
    _bldgCiv = new BuildingDisplayCiv(_mfCiv, adv, bReg);
    _cmdFac = new CommandFactory(_bldgCiv, _mfCiv);
    _cmdFac.initMap();
    _cp = new CommandParser(_skedder, _cmdFac);

    for (Building b : bReg.getAll()) {
      _bldgs.add(b.getName());
    }
  }

  @AfterClass
  public static void tearDownAfterClass()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ============================================================================
  // HELPER METHODS
  // ============================================================================

  /** Hero is on-Town, not with current Building, and not inside one */
  @Test
//  protected void resetBuildingState()
  public void resetBuildingState()
  {
    _bldgCiv.openTown();
    assertTrue(_bldgCiv.isOnTown());
    assertFalse(_bldgCiv.isInside());
    assertEquals("", _bldgCiv.getCurrentBuilding());
  }

}
