
package test.pdc.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import pdc.command.CmdReturn;
import test.pdc.buildings.FakeBuilding;

public class TestCmdReturn
{
  private static final String FAKE_BUILDING = "Fake Building";
  private CmdReturn _cmdReturn;
  private FakeBuildingDisplayCiv _bdCiv;

  @Before
  public void setUp()
  {
    _bdCiv = new FakeBuildingDisplayCiv();
    _cmdReturn = new CmdReturn(_bdCiv);
  }

  @Test
  public void testInit()
  {
    assertTrue(_cmdReturn.init(new ArrayList<String>()));
  }

  @Test
  public void testExec()
  {
    _bdCiv.setBuilding(new FakeBuilding(FAKE_BUILDING, ""));
    assertFalse(_bdCiv.isOnTown());
    assertEquals(FAKE_BUILDING, _bdCiv.getCurrentBuilding());
    
    _cmdReturn.exec();
    
    assertTrue(_bdCiv.isOnTown());
    assertEquals("", _bdCiv.getCurrentBuilding());
  }

}
