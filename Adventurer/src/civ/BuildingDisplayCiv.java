
package civ;

import hic.Mainframe;
import pdc.Util;
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
   * Show the interior image and description of the Building
   * 
   * @param bldName name of the building to enter
   */
  public void enterBuilding(String bldName)
  {
    _bldg = findAndSetBuilding(bldName);
    if (_bldg != null) {
      String description = _bldg.getInteriorDescription();
      String imagePath = _bldg.getIntImagePath();
      displayBuilding(description, imagePath);
    }
    else {
      _frame.displayText(NO_BLDG_FOUND);
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


  /**
   * Show the exterior image and description of the Building
   * 
   * @param bldName name of the building to enter
   */
  private void approachBuilding(String bldName)
  {
    _bldg = findAndSetBuilding(bldName);
    if (_bldg != null) {
      String description = _bldg.getExteriorDescription();
      String imagePath = _bldg.getExtImagePath();
      displayBuilding(description, imagePath);
    }
    else {
      _frame.displayText(NO_BLDG_FOUND);
    }
  }


  private void displayBuilding(String description, String imagePath)
  {
    if ((description.length() > 0) && (imagePath.length() > 0)) {
      String bldgName = _bldg.getName();
      _frame.setImage(Util.convertToImage(imagePath));
      _frame.setImageTitle(bldgName);
      _frame.displayText(description);
    } else {
      _frame.displayErrorText("Unabled to display building " + _bldg);
    }
    _frame.redraw();
  }

}
