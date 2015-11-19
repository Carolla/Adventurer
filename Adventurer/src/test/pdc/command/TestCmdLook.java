
package test.pdc.command;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.command.CmdLook;
import chronos.pdc.NPC;

public class TestCmdLook
{

  // iVars
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
    fail("not yet implemented");
  }

  @Test
  public void whenTargetNotFoundGenericLookIsPerformed()
  {
    fail("not yet implemented");
  }

  @Test
  public void whenInsideBuildingThenBuildingDescriptionGiven()
  {
    _cmdLook.init(new ArrayList<String>());
    _bdciv.setBuilding(new FakeBuilding("FakeBuilding", "Building Desc"));

    _cmdLook.exec();
    assertTrue(_bdciv._displayedBldg.equals("FakeBuilding"));
    assertTrue(_bdciv._displayedText.equals("Building Desc"));
  }

  @Test
  public void whenPeopleAreInsideBuildingThenTheirNamesAreGiven()
  {
    List<String> target = new ArrayList<String>();
    target.add("Bob");
    _cmdLook.init(target);
    
    FakeBuilding fb = new FakeBuilding("FakeBuilding", "Building Desc");
    fb.add(new FakeNPC("Bob"));
    _bdciv.setBuilding(fb);

    _cmdLook.exec();
    System.out.println(_bdciv._displayedText);
    assertTrue(_bdciv._displayedText.contains("Bob"));
  }

  public class FakeNPC extends NPC
  {
    FakeNPC(String name)
    {
      super(name, "", 0, false, "", "");
    }
  }

}
