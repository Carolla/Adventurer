
package test.civ;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import test.mock.AdvObjectMother;
import test.mock.AdvObjectMother.MockBuilding;
import test.mock.AdvObjectMother.MockBuildingRegistry;
import test.mock.AdvObjectMother.MockMainframe;
import civ.BuildingDisplayCiv;

public class TestBuildingDisplayCiv
{
    private static final String building1name = "Ugly Ogre Name";
    private static final String building2name = "Arcaneum";

    private MockMainframe _mf;
    private MockBuilding _mb;
    private BuildingDisplayCiv _bdCiv;
    private MockBuildingRegistry _breg;

    @Before
    public void setup()
    {
        AdvObjectMother mother = new AdvObjectMother();
        _mf = mother.getMockMainframe();
        _breg = mother.getMockBuildingRegistry();
        _mb = (MockBuilding) _breg.getBuilding("");
        _bdCiv = new BuildingDisplayCiv(_mf, _breg);
    }

    @Test
    public void itShouldApproachCurrentBuilding()
    {
        _bdCiv.approachBuilding(building1name);

        assertTrue(currentBuildingApproached());
    }

    @Test
    public void itShouldNotEnterCurrentBuildingWhenItHasntBeenApproached()
    {
        _bdCiv.enterBuilding();

        assertTrue(currentBuildingNotEntered());
    }

    @Test
    public void itShouldEnterCurrentBuildingWhenItHasBeenApproached()
    {
        _bdCiv.approachBuilding(building2name);
        _bdCiv.enterBuilding();

        assertTrue(currentBuildingApproached());
        assertTrue(currentBuildingEntered());
    }

    @Test
    public void buildingShouldNotBeEnteredAfterExit()
    {
        _bdCiv.approachBuilding(building2name);
        _bdCiv.enterBuilding();
        _bdCiv.exitBuilding();

        assertTrue(currentBuildingNotEntered());
    }

    private boolean currentBuildingEntered()
    {
        assertTrue("Interior image not retrieved", _mb._intImageDisplayed);
        assertTrue("Interior description not retrieved", _mb._intDescriptionDisplayed);
        assertTrue("Image and descriptions not displayed", _mf._displayed);
        return true;
    }

    private boolean currentBuildingNotEntered()
    {
        assertFalse("Interior image was retrieved", _mb._intImageDisplayed);
        assertFalse("Interior description was retrieved", _mb._intDescriptionDisplayed);
        assertFalse("Image and descriptions was displayed", _mf._displayed);
        return true;
    }

    private boolean currentBuildingApproached()
    {
        assertTrue("Exterior image not retrieved", _mb._extImageDisplayed);
        assertTrue("Exterior description not retrieved", _mb._extDescriptionDisplayed);
        assertTrue("Image and descriptions not displayed", _mf._displayed);
        return true;
    }
}
