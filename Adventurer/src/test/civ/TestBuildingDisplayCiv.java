
package test.civ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.Adventure;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;
import chronos.test.pdc.buildings.FakeBuilding;
import civ.BuildingDisplayCiv;
import civ.MainframeCiv;
import test.pdc.command.FakeMainframeCiv;

public class TestBuildingDisplayCiv
{
  private static Random _rand;
  private static final int numBuildings = BuildingDisplayCiv.DEFAULT_BUILDINGS.length;
  private BuildingDisplayCiv _bdCiv;
  private BuildingRegistry _breg = new FakeBuildingRegistry();
  private MainframeCiv _mf = new FakeMainframeCiv();
  private Adventure _adv;

  @BeforeClass
  public static void doOnce()
  {
    _rand = new Random(System.currentTimeMillis());
  }

  @Before
  public void setup()
  {
    _bdCiv = new BuildingDisplayCiv(_mf, _adv, _breg);
    assertNotNull(_bdCiv);
  }

  @Test
  public void canApproachBuildingWhenOnTown()
  {
    assertTrue(_bdCiv.isOnTown());
    for (int i = 0; i < BuildingDisplayCiv.DEFAULT_BUILDINGS.length; i++) {
      String testName = BuildingDisplayCiv.DEFAULT_BUILDINGS[i][0];
      assertTrue(_bdCiv.canApproach(testName));
    }
  }

  @Test
  public void cannotApproachBuildingWhenNotOnTown()
  {
    String sampleBuilding = BuildingDisplayCiv.DEFAULT_BUILDINGS[_rand.nextInt(numBuildings)][0];
    _bdCiv.enterBuilding(sampleBuilding);
    for (int i = 0; i < BuildingDisplayCiv.DEFAULT_BUILDINGS.length; i++) {
      String testName = BuildingDisplayCiv.DEFAULT_BUILDINGS[i][0];
      assertFalse("Approaching " + testName, _bdCiv.canApproach(testName));
    }
  }

  @Test
  public void canApproachOtherBuildingWhenOutsideABuilding()
  {
    for (int i = 0; i < BuildingDisplayCiv.DEFAULT_BUILDINGS.length; i++) {
      String buildingApproached = BuildingDisplayCiv.DEFAULT_BUILDINGS[i][0];
      _bdCiv.approachBuilding(buildingApproached);
      for (int j = i; j < BuildingDisplayCiv.DEFAULT_BUILDINGS.length; j++) {
        String buildingNowApproaching = BuildingDisplayCiv.DEFAULT_BUILDINGS[j][0];
        assertTrue("Approaching " + buildingNowApproaching + " from " + buildingApproached,
            _bdCiv.canApproach(buildingNowApproaching));
      }
    }
  }

  @Test
  public void canEnterBuildingWhenOnTown()
  {
    assertTrue(_bdCiv.isOnTown());
    for (int i = 0; i < BuildingDisplayCiv.DEFAULT_BUILDINGS.length; i++) {
      String testName = BuildingDisplayCiv.DEFAULT_BUILDINGS[i][0];
      assertTrue(_bdCiv.canEnter(testName));
    }
  }

  @Test
  public void canEnterBuildingWhenApproached()
  {
    for (int i = 0; i < BuildingDisplayCiv.DEFAULT_BUILDINGS.length; i++) {
      String buildingApproached = BuildingDisplayCiv.DEFAULT_BUILDINGS[i][0];
      _bdCiv.approachBuilding(buildingApproached);
      assertTrue(_bdCiv.canEnter(buildingApproached));
    }
  }

  @Test
  public void cannotEnterDifferentBuildingWhenBuildingEntered()
  {
    for (int i = 0; i < BuildingDisplayCiv.DEFAULT_BUILDINGS.length; i++) {
      String buildingEntered = BuildingDisplayCiv.DEFAULT_BUILDINGS[i][0];
      _bdCiv.enterBuilding(buildingEntered);
      for (int j = 0; j < BuildingDisplayCiv.DEFAULT_BUILDINGS.length; j++) {
        String buildingNowEntering = BuildingDisplayCiv.DEFAULT_BUILDINGS[j][0];
        if (!buildingEntered.equals(buildingNowEntering)) {
          assertFalse("Trying to enter " + buildingNowEntering + " from " + buildingEntered,
              _bdCiv.canEnter(buildingNowEntering));
        }
      }
      _bdCiv.returnToTown();
    }
  }

  @Test
  public void enterCausesBuildingToChange()
  {
    for (int i = 0; i < BuildingDisplayCiv.DEFAULT_BUILDINGS.length; i++) {
      assertEquals(_bdCiv.getCurrentBuilding(), "");
      
      String buildingEntered = BuildingDisplayCiv.DEFAULT_BUILDINGS[i][0];
      _bdCiv.enterBuilding(buildingEntered);

      assertEquals(_bdCiv.getCurrentBuilding(), buildingEntered);
      
      _bdCiv.returnToTown();
    }
  }

  @Test
  public void approachCausesBuildingToChange()
  {
    for (int i = 0; i < BuildingDisplayCiv.DEFAULT_BUILDINGS.length; i++) {
      assertEquals(_bdCiv.getCurrentBuilding(), "");
      
      String buildingApproached = BuildingDisplayCiv.DEFAULT_BUILDINGS[i][0];
      _bdCiv.approachBuilding(buildingApproached);

      assertEquals(_bdCiv.getCurrentBuilding(), buildingApproached);
      
      _bdCiv.returnToTown();
    }
  }

  @Test
  public void returnToTownResetsBuilding()
  {
    for (int i = 0; i < BuildingDisplayCiv.DEFAULT_BUILDINGS.length; i++) {
      String buildingApproached = BuildingDisplayCiv.DEFAULT_BUILDINGS[i][0];
      _bdCiv.approachBuilding(buildingApproached);
      assertEquals(_bdCiv.getCurrentBuilding(), buildingApproached);
      
      _bdCiv.returnToTown();
      assertEquals(_bdCiv.getCurrentBuilding(), "");
      
    }
  }

  public class FakeBuildingRegistry extends BuildingRegistry
  {
    public FakeBuildingRegistry()
    {
      super(null);
    }

    @Override
    public void initialize()
    {
      //Don't do it
    }

    @Override
    public Building getBuilding(String name)
    {
      return new FakeBuilding(name);
    }
  }
}
