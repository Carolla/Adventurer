
package civ;

import hic.Mainframe;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;

public class BuildingDisplayCiv
{

    private final Mainframe _frame;
    private final BuildingRegistry _bReg;
    private Building _bldg;

    private static final String NO_BLDG_FOUND = "Could not find that building.\n";

    /**
     * Constructor
     */
    public BuildingDisplayCiv(Mainframe mainframe, BuildingRegistry bReg)
    {
        _frame = mainframe;
        _bReg = bReg;
    }

    /**
     * Call to show the external building view of the current building.
     * 
     * @param bldName name of the building to enter
     * @return true if the building was approached
     */
    public boolean approachBuilding(String bldName)
    {
        if (findAndSetBuilding(bldName) != null) {
            approachCurrentBuilding();
            return true;
        }
        else {
            _frame.displayText(NO_BLDG_FOUND);
            return false;
        }
    }

    public void enterBuilding()
    {
        if (_bldg != null) {
            String description = _bldg.getInteriorDescription();
            String imagePath = _bldg.getIntImagePath();
            displayBuilding(description, imagePath);
        }
    }

    public void exitBuilding()
    {
        _bldg = null;
    }

    protected Building findAndSetBuilding(String bldName)
    {
        if (_bldg == null) {
            _bldg = _bReg.getBuilding(bldName);
        }
        return _bldg;
    }

    private void approachCurrentBuilding()
    {
        String description = _bldg.getExteriorDescription();
        String imagePath = _bldg.getExtImagePath();
        displayBuilding(description, imagePath);
    }

    private void displayBuilding(String description, String imagePath)
    {
        if (description.length() > 0 && imagePath.length() > 0) {
            _frame.displayTextAndImage(description, imagePath);
        } else {
            System.err.println("Unabled to display building " + _bldg);
        }
    }
}
