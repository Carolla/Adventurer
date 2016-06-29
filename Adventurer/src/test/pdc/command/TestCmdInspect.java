
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
import pdc.command.CmdInspect.MockCmdInspect;
import test.pdc.FakeNPC;
import chronos.pdc.NPC;
import chronos.pdc.buildings.Bank;
import chronos.pdc.buildings.Building;
import civ.BuildingDisplayCiv;

public class TestCmdInspect
{
  private CmdInspect _inspect;
  private static Building _bank;
  private List<String> _params = new ArrayList<String>();
  private List<String> _myPatron = new ArrayList<String>();
  private static NPC _fred;
  private static BuildingDisplayCiv _bdCiv;

//  @BeforeClass
//  public static void setUpBeforeClass() throws Exception
//  {
//    // MockBuildingDisplayCiv _mockBDC = _bdCiv.new MockBuildingDisplayCiv();
//    // _mockBDC.setCurrentBuilding(_bank);
//
//    // Add fred
////    , null, 0, "Far description of Fred", "Near description of Fred",
////        new ArrayList<String>(), new ArrayList<String>());
//  }

  @Before
  public void setUp() throws Exception
  {
    _bdCiv = new BuildingDisplayCiv(null, null, null);
    _bank = new Bank();
    _fred = new FakeNPC("Fred");
    _bank.add(_fred);
    _inspect = new CmdInspect(_bdCiv);
  }

  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);

    _bank.remove(_fred);
  }

  @Test
  public void init()
  {
    // Test single-word name
    _params.add("James");
    assertTrue(_inspect.init(_params));
    _params.clear();

    // Test multi-word name
    _params.add("Falsoon");
    _params.add("of");
    _params.add("Northwood");
    assertTrue(_inspect.init(_params));
    _params.clear();
  }

  @Test
  public void initError()
  {
    // Test no params
    // _params is empty
    assertFalse(_inspect.init(_params));

    // Test empty string
    _params.add("");
    assertFalse(_inspect.init(_params));
    _params.clear();

    // Test null params
    assertFalse(_inspect.init(null));

  }

  @Test
  public void exec_WhereBldgPatronsPresent()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    /* Setup */
    // Create mock CmdInspect
    MockCmdInspect mockCI = _inspect.new MockCmdInspect();
    // Add other patrons
    NPC _wilma =
        new NPC("Wilma", null, 0, "Far description of Wilma", "Near description of Wilma",
            new ArrayList<String>(), new ArrayList<String>());
    _bank.add(_wilma);
    NPC _barney = new NPC("Barney", null, 0, "Far description of Barney",
        "Near description of Barney",
        new ArrayList<String>(), new ArrayList<String>());
    _bank.add(_barney);

    /* Execute & Verify Fred */
    _myPatron.add(_fred.getName());
    assertTrue(_inspect.init(_myPatron));
    assertTrue(_inspect.exec());
    MsgCtrl.msgln("Expected Near Des:" + _fred.getNearDescription());
    MsgCtrl.msgln("Actual Near Des:" + mockCI.getNearDes());

    assertTrue(_fred.getNearDescription().equals(mockCI.getNearDes()));
    _myPatron.clear();

    /* Execute & Verify Wilma */
    _myPatron.add(_wilma.getName());
    assertTrue(_inspect.init(_myPatron));
    assertTrue(_inspect.exec());
    MsgCtrl.msgln("Expected Near Des:" + _wilma.getNearDescription());
    MsgCtrl.msgln("Actual Near Des:" + mockCI.getNearDes());
    assertFalse(_fred.getNearDescription().equals(mockCI.getNearDes()));
    assertTrue(_wilma.getNearDescription().equals(mockCI.getNearDes()));
    _myPatron.clear();

    // Check Barney's near description
    _myPatron.add(_barney.getName());
    assertTrue(_inspect.init(_myPatron));
    assertTrue(_inspect.exec());
    MsgCtrl.msgln("Expected Near Des:" + _barney.getNearDescription());
    MsgCtrl.msgln("Actual Near Des:" + mockCI.getNearDes());
    assertFalse(_wilma.getNearDescription().equals(mockCI.getNearDes()));
    assertTrue(_barney.getNearDescription().equals(mockCI.getNearDes()));
    _myPatron.clear();

    // Clear addtl bank patrons
    _bank.remove(_wilma);
    _bank.remove(_barney);
  }


  @Test
  public void execError_WhereRequestedPatronMissing()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    /* Execute & Verify Missing Patron Betty */
    _myPatron.add("Betty");
    assertTrue(_inspect.init(_myPatron));
    assertFalse(_inspect.exec());
    _myPatron.clear();
  }

  @Test
  public void test_exec_WhereBuildingEmptyOfPatrons()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Setup
    _bank.remove(_fred);
    assertTrue(_bank.getPatrons().isEmpty());

    /* Execute & Verify */
    _myPatron.add("Betty");
    assertTrue(_inspect.init(_myPatron));
    assertFalse(_inspect.exec());
    _myPatron.clear();
  }

  @Test
  public void exec_BuildingProprietor()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    NPC proprietor = _bank.getProprietorNPC();
    _myPatron.add(_bank.getProprietor());
    MockCmdInspect mockCI = _inspect.new MockCmdInspect();

    assertTrue(_inspect.init(_myPatron));
    assertTrue(_inspect.exec());
    MsgCtrl.msgln("Expected Near Des:" + proprietor.getNearDescription());
    MsgCtrl.msgln("Actual Near Des:" + mockCI.getNearDes());
    assertTrue(proprietor.getNearDescription().equals(mockCI.getNearDes()));
  }


}
