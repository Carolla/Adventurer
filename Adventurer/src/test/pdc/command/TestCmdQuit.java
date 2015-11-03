package test.pdc.command;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hic.MainframeInterface;

import java.util.ArrayList;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.command.CmdQuit;
import test.integ.MainframeProxy;
import civ.MainframeCiv;

public class TestCmdQuit
{
    
    //iVars
    private CmdQuit _cmdQuit;
    private static MainframeCiv _mfCiv;
    private static FakeBuildingDisplayCiv _bdciv;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        MainframeInterface mfInterface = new MainframeProxy();
        _bdciv = new FakeBuildingDisplayCiv();
        _mfCiv = new MainframeCiv(mfInterface, _bdciv, null);
    }

    @Before
    public void setUp() throws Exception
    {
        _cmdQuit = new CmdQuit(_mfCiv, _bdciv);
    }

    @After
    public void tearDown() throws Exception
    {
        // Audit messages are OFF after each test
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);

    }

    @Test
    public void testInitEmptyArgs()
    {
        MsgCtrl.where(this);
        
        // Setup for Test 1
        ArrayList<String> emptyArgs = new ArrayList<String>();
        _bdciv.setInside(false);
        
        // Test 1 - Normal
        assertTrue(_cmdQuit.init(emptyArgs));
    }
    
    @Test
    public void testInitArgsPresent()
    {        
        // Setup for Test 2
        ArrayList<String> addedSpace = new ArrayList<String>();
        addedSpace.add(" ");
        ArrayList<String> addedWord = new ArrayList<String>();
        addedWord.add(" now");
        _bdciv.setInside(false);
        
        // Test 2 - Error - args present
        assertFalse(_cmdQuit.init(addedSpace));
        assertFalse(_cmdQuit.init(addedWord));
    }
    
    @Test
    public void testInitInsideBuilding()
    {  
        // Setup for Test 3
        ArrayList<String> emptyArgs = new ArrayList<String>();
        
        // Set failing condition
        _bdciv.setInside(true);
        
        // Test 3 - Error - inside building
        assertFalse(_cmdQuit.init(emptyArgs));
    }

//    @Test
//    public void testExec()
//    {
//        MsgCtrl.where(this);
//        
//        assertTrue(_cmdQuit.exec());
//    }

}
