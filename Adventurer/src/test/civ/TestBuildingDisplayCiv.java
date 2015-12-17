
package test.civ;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import chronos.pdc.Adventure;
import chronos.pdc.registry.BuildingRegistry;
import civ.BuildingDisplayCiv;
import civ.MainframeCiv;

public class TestBuildingDisplayCiv
{  
    private static final String building1name = "Ugly Ogre Name";
    private static final String building2name = "Arcaneum";

    private BuildingDisplayCiv _bdCiv;
    private BuildingRegistry _breg;
    private MainframeCiv _mf;
    private Adventure _adv;

    @Before
    public void setup()
    {
        _bdCiv = new BuildingDisplayCiv(_mf, _adv, _breg);
        assertNotNull(_bdCiv);
        assertFalse(building1name.equals(building2name));
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
}
