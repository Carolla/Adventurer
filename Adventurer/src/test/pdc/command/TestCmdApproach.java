package test.pdc.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import hic.IOPanelInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTextArea;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.command.CmdApproach;
import pdc.command.CmdEnter;
import pdc.command.CommandFactory;
import pdc.command.CmdApproach.MockCmdApproach;
import pdc.command.Command;
import test.integ.IOPanelProxy;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.BuildingDisplayCiv;
import civ.CommandParser;
import civ.BuildingDisplayCiv.MockBldgCiv;

public class TestCmdApproach
{
    // iVars
    private CmdApproach _cmdApproach;
    private MockCmdApproach _mock;
    
    private static CommandParser _cp = null;
    private static IOPanelProxy _iopx = null;
    private static CommandFactory _cmdFac = null;
    private static BuildingDisplayCiv _bdciv = null;
    private static MockBldgCiv _mockbdciv = null;
    
    private static RegistryFactory _regfac = null;
    private static BuildingRegistry _breg = null;
    private static List<Building> _bList = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        _iopx = new IOPanelProxy();
        _cp = CommandParser.getInstance(_iopx);
        _cmdFac = new CommandFactory(_cp);
        _bdciv = BuildingDisplayCiv.getInstance(); // for CmdApproach context
        _mockbdciv = _bdciv.new MockBldgCiv();
        
        // Get a list of all buildings to enter
        _regfac = RegistryFactory.getInstance();
        _breg = (BuildingRegistry) _regfac.getRegistry(RegKey.BLDG);
        _bList = _breg.getBuildingList();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
        // Command Approach object creates registry that must be closed
        RegistryFactory regFact = RegistryFactory.getInstance();
        BuildingRegistry bReg = (BuildingRegistry) regFact.getRegistry(RegKey.BLDG);
        bReg.closeRegistry();
    }

    @Before
    public void setUp() throws Exception
    {
//        _cmdApproach = new CmdApproach();
        _cmdApproach = (CmdApproach) _cmdFac.createCommand("CmdApproach");
        
        _mock = _cmdApproach.new MockCmdApproach();
        // Error messages are ON at start of each test 
        MsgCtrl.errorMsgsOn(true);
        // Audit messages are OFF at start of each test
        MsgCtrl.auditMsgsOn(false);
    }

    @After
    public void tearDown() throws Exception
    {
        // Clear targetBldg from CmdEnter
        _mock.clearTargetBldg();
        _mock = null;
        _cmdApproach = null;
        
        // Ensure that current building is null to end
        _bdciv.setCurrentBuilding(null);
        _mockbdciv.setInsideBldg(false);
        
        // Audit messages are OFF after each test
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }

    /** Normal verify CmdApproach constructor */
    @Test
    public void testCtorVerified()
    {
        MsgCtrl.auditMsgsOn(false);
        // turn these off for expected errors now
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.where(this);
        
        // Normal Tests
        assertTrue(_cmdApproach.getClass().getSimpleName().equals("CmdApproach"));
        
        int delay = 0;
        int duration = 30;
        String cmdFormat = "APPROACH <Building Name>";
        
        MsgCtrl.msgln("\t" + _mock.getCmdFormat());
        assertEquals(delay, _mock.getDelay());
        assertEquals(duration, _mock.getDuration());
        assertEquals(cmdFormat, _mock.getCmdFormat());
        
        // Error Tests - ctor prevents by taking no params
        // Boundary Tests - ctor prevents by taking no params
        // Special case Tests - ctor prevents by taking no params
    }
    
    /** Normal CmdApproach given building(s) */
    @Test
    public void testInitValidBuilding()
    {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      List<String> bNames = new ArrayList<String>();

      // For each building, approach it and check its attributes
      for (int k = 0; k < _bList.size(); k++) {
        String name = _bList.get(k).getName();
        bNames.add(0, name);
        MsgCtrl.msgln("\tApproaching Building:\t" + bNames.get(0));
        assertTrue(_cmdApproach.init(bNames));

        // Verify target building
        Building tBldg = _mock.getTargetBldg();
        assertEquals(tBldg, _bList.get(k));
        // Clear out arglist
        bNames.remove(0);
      }
      MsgCtrl.msgln("\tAll buildings approached successfully.");
    }
    
    /** Error: Approach invalid building for ERRMSG_WRONG_BLDG */
    @Test
    public void testInitInvalidBuilding()
    {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      List<String> bNames = new ArrayList<String>();
      // Place an invalid building as the parm
      bNames.add("Winery");
      MsgCtrl.errMsg("\tExpected error: ");
      assertFalse(_cmdApproach.init(bNames));
    } 
    
    /** Error CmdApproach no building specified */
    @Test
    public void testInitWithoutParms()
    {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      List<String> bNames = new ArrayList<String>();

      // Now try to approach current Building without a parm
      bNames.clear();
      assertFalse(_cmdApproach.init(bNames));
    }
    
    /** Error: Trying to approach one building from inside another gives ERRMSG_JUMPBLDG */
    @Test
    public void initJumpBuilding()
    {
      MsgCtrl.auditMsgsOn(true);
      MsgCtrl.errorMsgsOn(true);
      MsgCtrl.where(this);

      // Set context to be inside valid building: Jail
      List<String> bNames = new ArrayList<String>();
      
      Building b = _breg.getBuilding("Jail");
      bNames.add("Jail");

      _bdciv.setCurrentBuilding(b);
      _mockbdciv.setInsideBldg(true);
      
      MsgCtrl.errMsg("\tExpected error: ");
      assertFalse(_cmdApproach.init(bNames));
    } 

}
