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
import mylib.MsgCtrl;
import pdc.command.CmdInspect;
import pdc.command.CommandFactory;
import pdc.command.CommandInput;

public class TestCmdInspect
{
    private static CommandFactory _cf;
    private CmdInspect _inspect;
    private static Building _bank;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        _cf = new CommandFactory(null, null);
        _cf.initMap();
        
        _bank = new Bank();
        NPC fred = new NPC("Fred", null, 0, "Far description of Fred", "Near description of Fred",
                null, null);
        _bank.add(fred);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {}

    @Before
    public void setUp() throws Exception
    {
        // setup
        List<String> params = new ArrayList<String>();
        
        // run
        CommandInput ci = new CommandInput("INSPECT", params);
        _inspect = (CmdInspect) _cf.createCommand(ci);
    }

    @After
    public void tearDown() throws Exception
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }

    @Test
    public void test_constructor()
    { 
        assertNotNull(_inspect);
        assertEquals(0, _inspect.getDelay());
        assertEquals(5, _inspect.getDuration());
    }
    
    @Test
    public void test_init()
    {
        // Test no params
        List<String> params = new ArrayList<String>();
        assertFalse(_inspect.init(params));
        
        // Test single-word name
        params.add("James");
        assertTrue(_inspect.init(params));
        
        // Test multi-word name
        params.clear();
        params.add("Falsoon");
        params.add("of");
        params.add("Northwood");
        assertTrue(_inspect.init(params));
    }
    
    @Test
    public void test_exec()
    {
     
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(true);
        MsgCtrl.where(this);
        // setup
        List<String> params = new ArrayList<String>();
        params.add("Grimlock");
        assertTrue(_inspect.init(params));



        assertTrue(_inspect.exec());
        
    }

}
