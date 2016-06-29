
package test.pdc.command;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pdc.command.CmdInspect;
import test.pdc.FakeNPC;
import chronos.pdc.NPC;
import chronos.pdc.buildings.Bank;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;
import civ.BuildingDisplayCiv;
import civ.MainframeCiv;

public class TestCmdInspect
{
  private CmdInspect _inspect;
  private static NPC _fred = new FakeNPC("Fred");
  private MainframeCiv _fakeMfCiv = new FakeMainframeCiv();
  private final BuildingRegistry _breg = new BuildingRegistry();
  private BuildingDisplayCiv _bdCiv = new BuildingDisplayCiv(_fakeMfCiv, null, _breg);
  private Building _bank = _breg.getBuilding(new Bank().getName());

  private List<String> _list = new ArrayList<String>();

  @Before
  public void setUp() throws Exception
  {
    _bank.add(_fred);
    assertTrue(_bdCiv.approachBuilding(_bank.getName()));
    _inspect = new CmdInspect(_bdCiv);
    _inspect.setOutput(_fakeMfCiv .getOutput());
  }

  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }

  @Test
  public void initSingleWordName()
  {
    _list.add("James");
    assertTrue(_inspect.init(_list));
  }

  @Test
  public void initMultiwordName()
  {
    _list.add("Falsoon");
    _list.add("of");
    _list.add("Northwood");
    assertTrue(_inspect.init(_list));
  }

  @Test
  public void initEmptyList()
  {
    assertFalse(_inspect.init(_list));
  }

  @Test
  public void initEmptyString()
  {
    _list.add("");
    assertFalse(_inspect.init(_list));
  }

  @Test
  public void initNull()
  {
    assertFalse(_inspect.init(null));
  }

  @Test
  public void exec_WhereBldgPatronsPresent()
  {
    NPC _wilma = new FakeNPC("Wilma");
    NPC _barney = new FakeNPC("Barney");
    _bank.add(_wilma);
    _bank.add(_barney);

    _list.add(_fred.getName());
    assertTrue(_inspect.init(_list));
    assertTrue(_inspect.exec());
    _list.clear();

    _list.add(_wilma.getName());
    assertTrue(_inspect.init(_list));
    assertTrue(_inspect.exec());
    _list.clear();

    _list.add(_barney.getName());
    assertTrue(_inspect.init(_list));
    assertTrue(_inspect.exec());
  }

  @Test
  public void execError_WhereRequestedPatronMissing()
  {
    _list.add("Betty");

    assertTrue(_inspect.init(_list));
    assertFalse(_inspect.exec());
  }

  @Test
  public void test_exec_WhereBuildingEmptyOfPatrons()
  {
    assertTrue(_bank.remove(_fred));
    _list.add("Betty");

    assertTrue(_inspect.init(_list));
    assertFalse(_inspect.exec());
  }

  @Test
  public void exec_BuildingProprietor()
  {
    _list.add(_bank.getProprietor());

    assertTrue(_inspect.init(_list));
    assertTrue(_inspect.exec());
  }


}
