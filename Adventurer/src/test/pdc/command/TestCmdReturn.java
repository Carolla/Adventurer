package test.pdc.command;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pdc.command.CmdReturn;

public class TestCmdReturn
{
    private CmdReturn _cmdReturn;
    private FakeMainActionCiv _mfCiv;

    @Before
    public void setUp()
    {
        _mfCiv = new FakeMainActionCiv();
        _cmdReturn = new CmdReturn(_mfCiv);
    }

    @After
    public void tearDown()
    {}

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
