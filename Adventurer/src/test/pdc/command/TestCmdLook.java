package test.pdc.command;

import java.util.ArrayList;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.command.CmdLook;

public class TestCmdLook
{
    
    //iVars
    private CmdLook _cmdLook;
    private static FakeBuildingDisplayCiv _bdciv;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        _bdciv = new FakeBuildingDisplayCiv();
    }

    @Before
    public void setUp() throws Exception
    {
        _cmdLook = new CmdLook(_bdciv);
    }

    @After
    public void tearDown() throws Exception
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }
    
    @Test
    public void whenNoTargetProvidedGenericLooksIsPerformed()
    {
      _cmdLook.init(new ArrayList<String>());
      _bdciv.setInside(false);
      
      _cmdLook.exec();
    }

    @Test
    public void whenTargetNotFoundGenericLookIsPerformed()
    {
      
    }

    @Test
    public void whenInsideBuildingThenBuildingDescriptionGiven()
    {
      
    }
    
    @Test
    public void whenPeopleAreInsideBuildingThenTheirNamesAreGiven()
    {
      
    }

}
