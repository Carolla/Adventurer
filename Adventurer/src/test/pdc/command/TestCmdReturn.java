package test.pdc.command;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import civ.MainframeCiv;
import pdc.command.CmdReturn;
import pdc.command.CmdReturn.MockCmdReturn;
import pdc.command.CommandFactory;
import test.integ.MainframeProxy;

public class TestCmdReturn
{
    
    private static CommandFactory _cmdFac = null;
    
    private CmdReturn _cmdReturn;
    private MockCmdReturn _mock;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        MainframeProxy mfProxy = new MainframeProxy();
        MainframeCiv mfCiv = new MainframeCiv(mfProxy);
        _cmdFac = new CommandFactory(mfCiv);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {}

    @Before
    public void setUp() throws Exception
    {
        _cmdReturn = (CmdReturn) _cmdFac.createCommand("cmdReturn");
        _mock = _cmdReturn.new MockCmdReturn();
    }

    @After
    public void tearDown() throws Exception
    {}
    
    @Test
    public void testCtor()
    {
        
        
        fail("Not yet implemented");
    }

    @Test
    public void testInit()
    {
        /* Case 1: Normal - Hero is outside building */
        // Case 1: setup
        // Case 1: test
        
        /* Case 2: Normal - Hero is inside non-Arena building */
        // Case 2: setup
        // Case 2: test
        
        /* Case 3: Error - Hero is inside Arena */
        // Case 3: setup
        // Case 3: test
        
        /* Case 4: Error - Bad params */
        // Case 4: setup
        // Case 4: test
        fail("Not yet implemented");
    }
    
    @Test
    public void testExec()
    {
        fail("Not yet implemented");
    }

}
