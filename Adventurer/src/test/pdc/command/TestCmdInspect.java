package test.pdc.command;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.NPC;
import chronos.pdc.buildings.Bank;
import chronos.pdc.buildings.Building;
import chronos.test.pdc.buildings.ConcreteBuilding;
import civ.BuildingDisplayCiv;
import civ.BuildingDisplayCiv.MockBuildingDisplayCiv;
import mylib.MsgCtrl;
import pdc.command.CmdInspect;
import pdc.command.CmdInspect.MockCmdInspect;
import pdc.command.CommandFactory;
import pdc.command.CommandInput;

public class TestCmdInspect
{
    private static CommandFactory _cf;
    private CmdInspect _inspect;
    private static Building _bank;
    private List<String> _params;
    private static NPC _fred;
    private static BuildingDisplayCiv _bdCiv;
    private List<String> _myPatron;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        _cf = new CommandFactory(null, null);
        _cf.initMap();
        _bdCiv = new BuildingDisplayCiv(null, null, null);
        _bank = new Bank();
        MockBuildingDisplayCiv _mockBDC = _bdCiv.new MockBuildingDisplayCiv();
        _mockBDC.setCurrentBuilding(_bank);
        
        // Add fred
        _fred = new NPC("Fred", null, 0, "Far description of Fred", "Near description of Fred",
                new ArrayList<String>(), new ArrayList<String>());
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {}

    @Before
    public void setUp() throws Exception
    {
        // setup
        _params = new ArrayList<String>();
        _myPatron = new ArrayList<String>();
        _bank.add(_fred);
        
        // run
        CommandInput ci = new CommandInput("INSPECT", _params);
//        _inspect = (CmdInspect) _cf.createCommand(ci);
        _inspect = new CmdInspect(_bdCiv);
        _inspect.setOutput(new FakeMainframeCiv());
        
    }

    @After
    public void tearDown() throws Exception
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        
        _bank.remove(_fred);
    }

    @Test
    public void test_constructor()
    { 
        assertNotNull(_inspect);
        assertEquals(0, _inspect.getDelay());
        assertEquals(5, _inspect.getDuration());
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
        
        /* Execute & Verify Fred*/
        _myPatron.add(_fred.getName());
        assertTrue(_inspect.init(_myPatron));
        assertTrue(_inspect.exec());
        MsgCtrl.msgln("Expected Near Des:" + _fred.getNearDescription());
        MsgCtrl.msgln("Actual Near Des:" + mockCI.getNearDes());
        
        assertTrue(_fred.getNearDescription().equals(mockCI.getNearDes()));
        _myPatron.clear();
        
        /* Execute & Verify Wilma*/
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
