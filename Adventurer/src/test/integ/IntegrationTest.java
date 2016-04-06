package test.integ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;

import chronos.civ.DefaultUserMsg;
import chronos.pdc.Adventure;
import chronos.pdc.buildings.Building;
import chronos.pdc.command.Scheduler;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.HeroRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.BuildingDisplayCiv;
import civ.CommandParser;
import civ.MainActionCiv;
import civ.MainframeCiv;
import mylib.MsgCtrl;
import pdc.command.CommandFactory;

public class IntegrationTest
{
  /** List of valid Buildings that can be entered */
  protected static final List<String> _bldgs = new ArrayList<String>();
  protected static final Scheduler _skedder = new Scheduler(new DefaultUserMsg());
  protected static final MainframeCiv _mfCiv = new MainframeCiv(new MainframeProxy());
  protected static final MainActionCiv _maCiv = new MainActionCiv(_mfCiv);
  protected static final RegistryFactory _regFactory = new RegistryFactory();

  protected static BuildingDisplayCiv _bldgCiv;
  protected static CommandFactory _cmdFac;
  protected static CommandParser _cp;

  @BeforeClass
  public static void setUpBeforeClass()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.msgln("Integration test base class setUpBeforeClass()");
//    _regFactory.initRegistries(_skedder);
    _regFactory.initRegistries();
    
    BuildingRegistry bReg = (BuildingRegistry) _regFactory.getRegistry(RegKey.BLDG);
    AdventureRegistry advReg = (AdventureRegistry) _regFactory.getRegistry(RegKey.ADV);
    Adventure adv = advReg.getAll().get(0);
    
    _maCiv.loadSelectedAdventure(adv.getName());
    _bldgCiv = new BuildingDisplayCiv(_mfCiv, adv, bReg);
    _cmdFac = new CommandFactory(_mfCiv, _bldgCiv);
    _cmdFac.initMap();
    _cp = new CommandParser(_skedder, _cmdFac);

    // Get list of names for all buildings
    for (Building b : bReg.getAll()) {
      _bldgs.add(b.getName());
    }
  }

  @BeforeClass
  public static void tearDownAfterClass()
  {
    // This registry has persistence, so must be closed
    HeroRegistry heroReg = (HeroRegistry) _regFactory.getRegistry(RegKey.HERO);
    heroReg.close();

    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
  }

  
  // ============================================================================
  //              HELPER METHODS
  // ============================================================================

  /** Hero is onTwon, with not current Building, and not inside one */
  protected void resetBuildingState()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    _bldgCiv.openTown();
    assertTrue(_bldgCiv.isOnTown());
    assertFalse(_bldgCiv.isInside());
    assertEquals("", _bldgCiv.getCurrentBuilding());
  }

}
