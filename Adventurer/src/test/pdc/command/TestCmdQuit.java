package test.pdc.command;

import static org.junit.Assert.*;

import java.util.ArrayList;

import hic.MainframeInterface;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.BuildingDisplayCiv;
import civ.BuildingDisplayCiv.MockBldgCiv;
import civ.MainframeCiv;
import pdc.command.CmdQuit;
import pdc.command.CmdQuit.MockCmdQuit;
import pdc.command.CommandFactory;
import test.integ.MainframeProxy;

public class TestCmdQuit
{
    
    //iVars
    private CmdQuit _cmdQuit;
    private MockCmdQuit _mock;
    
    private static CommandFactory _cmdFac = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        MainframeInterface mfInterface = new MainframeProxy();
        MainframeCiv mfCiv = new MainframeCiv(mfInterface);
        _cmdFac = new CommandFactory(mfCiv);
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
        _cmdQuit = (CmdQuit) _cmdFac.createCommand("CmdQuit");
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
