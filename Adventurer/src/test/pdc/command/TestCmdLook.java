
package test.pdc.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.command.CmdLook;
import test.pdc.FakeNPC;

public class TestCmdLook
{

  private static final String EXAMPLE_DESC = "A fat man";
  private static final String EXAMPLE_NAME = "Bob";
  private static final FakeNPC BOB = new FakeNPC(EXAMPLE_NAME, EXAMPLE_DESC);
  private static final String BUILDING_DESC = "Building Desc";
  private static final String FAKE_BUILDING = "FakeBuilding";
  private static final FakeBuilding BUILDING = new FakeBuilding(FAKE_BUILDING, BUILDING_DESC);
  private static final FakeBuilding BOBLESS_BUILDING = new FakeBuilding(FAKE_BUILDING, BUILDING_DESC);
  
  private CmdLook _cmdLook;
  private static FakeBuildingDisplayCiv _bdciv;
  private final List<String> bobList = new ArrayList<String>();
  private final List<String> fredList = new ArrayList<String>();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    _bdciv = new FakeBuildingDisplayCiv();
    BUILDING.add(BOB);
  }

  @Before
  public void setUp() throws Exception
  {
    _cmdLook = new CmdLook(_bdciv);
    bobList.add(EXAMPLE_NAME);
    fredList.add("Fred");
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
    _bdciv.setBuilding(BOBLESS_BUILDING);

    _cmdLook.exec();
    assertEquals(BUILDING_DESC, _bdciv._displayedText);
  }

  @Test
  public void whenTargetNotFoundGenericLookIsPerformed()
  {
    _cmdLook.init(fredList);
    _bdciv.setBuilding(BUILDING);

    _cmdLook.exec();
    assertEquals(BUILDING_DESC, _bdciv._displayedText);
  }

  @Test
  public void whenPeopleAreInsideBuildingThenTheirNamesAreGiven()
  {
    _cmdLook.init(new ArrayList<String>());
    _bdciv.setBuilding(BUILDING);

    _cmdLook.exec();
    assertTrue(_bdciv._displayedText.contains(EXAMPLE_NAME));
  }
  
  @Test
  public void whenPersonIsTargetAndInsideBuildingThenDescriptionGiven()
  {
    _cmdLook.init(bobList); 
    _bdciv.setBuilding(BUILDING);

    _cmdLook.exec();
    assertTrue(_bdciv._displayedText.contains(EXAMPLE_DESC));
  }



}
