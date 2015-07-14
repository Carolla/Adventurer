package test.pdc.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hic.MainframeInterface;

import java.util.ArrayList;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.command.CmdQuit;
import pdc.command.CmdQuit.MockCmdQuit;
import test.integ.MainframeProxy;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.BuildingDisplayCiv;
import civ.BuildingDisplayCiv.MockBldgCiv;
import civ.MainframeCiv;

public class TestCmdQuit
{
    
    //iVars
    private CmdQuit _cmdQuit;
    private MockCmdQuit _mock;
    private static MainframeCiv _mfCiv;
    private static BuildingDisplayCiv _bdciv;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        MainframeInterface mfInterface = new MainframeProxy();
        _mfCiv = new MainframeCiv(mfInterface);
        _bdciv = BuildingDisplayCiv.getInstance();
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
        _cmdQuit = new CmdQuit(_bdciv);
        _cmdQuit.setMsgHandler(_mfCiv);
        _mock = _cmdQuit.new MockCmdQuit();
    }

    @After
    public void tearDown() throws Exception
    {
        // Clear the command objects
        _mock = null;
        _cmdQuit = null;
        
        // Audit messages are OFF after each test
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);

    }

    /** Tests the Constructor. */
    @Test
    public void testCtor()
    {
        // Message settings for Tests
        MsgCtrl.auditMsgsOn(false);
        // turn these off for expected errors now
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.where(this);
        
        // Vars for tests
        int delay = 0;
        int duration = 0;
        String description = "End the program.";
        
        // Tests
        assertEquals(delay, _mock.getDelay());
        assertEquals(duration, _mock.getDuration());
        assertEquals(description, _mock.getDescription());
        
        /* Error, Boundary and Special Tests deemed unnecessary for Ctor */
    }

    @Test
    public void testInit()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.where(this);
        
        // Setup for Test 1
        ArrayList<String> emptyArgs = new ArrayList<String>();
        
        // Test 1 - Normal
        assertTrue(_cmdQuit.init(emptyArgs));
        
        // Setup for Test 2
        ArrayList<String> addedSpace = new ArrayList<String>();
        addedSpace.add(" ");
        ArrayList<String> addedWord = new ArrayList<String>();
        addedWord.add(" now");
        
        // Test 2 - Error - args present
        assertFalse(_cmdQuit.init(addedSpace));
        assertFalse(_cmdQuit.init(addedWord));
        
        // Setup for Test 3
        BuildingDisplayCiv bdCiv = BuildingDisplayCiv.getInstance();
        MockBldgCiv mock_bdCiv = bdCiv.new MockBldgCiv();
        // Pre-test - show normal first
        assertTrue(_cmdQuit.init(emptyArgs));
        // Set failing condition
        mock_bdCiv.setInsideBldg(true);
        
        // Test 3 - Error - inside building
        assertFalse(_cmdQuit.init(emptyArgs));
    }

    @Test
    public void testExec()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.where(this);
        
        assertTrue(_cmdQuit.exec());
    }

}
