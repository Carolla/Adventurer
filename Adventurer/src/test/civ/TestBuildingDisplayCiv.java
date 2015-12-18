
package test.civ;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import test.pdc.FakeBuilding;
import test.pdc.command.FakeMainframeCiv;
import chronos.pdc.Adventure;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;
import civ.BuildingDisplayCiv;
import civ.MainframeCiv;

public class TestBuildingDisplayCiv
{
  private BuildingDisplayCiv _bdCiv;
  private BuildingRegistry _breg = new FakeBuildingRegistry();
  private MainframeCiv _mf = new FakeMainframeCiv();
  private Adventure _adv;

  @Before
  public void setup()
  {
    _bdCiv = new BuildingDisplayCiv(_mf, _adv, _breg);
    assertNotNull(_bdCiv);
  }

  @Test
  public void canApproachBuildingWhenOnTown()
  {
    fail("not yet implemented");
  }

  @Test
  public void cannotApproachBuildingWhenNotOnTown()
  {
    fail("not yet implemented");
  }

  @Test
  public void canEnterBuildingWhenOnTown()
  {
    fail("not yet implemented");
  }

  @Test
  public void canEnterBuildingWhenApproached()
  {
    fail("not yet implemented");
  }

  @Test
  public void cannotEnterDifferentBuildingWhenApproached()
  {
    fail("not yet implemented");
  }

  @Test
  public void enterCausesBuildingToChange()
  {
    fail("not yet implemented");
  }

  @Test
  public void approachCausesBuildingToChange()
  {
    fail("not yet implemented");
  }

  @Test
  public void returnToTownResetsBuilding()
  {
    fail("not yet implemented");
  }

  public class FakeBuildingRegistry extends BuildingRegistry
  {
    public FakeBuildingRegistry()
    {
      super(null, null);
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
