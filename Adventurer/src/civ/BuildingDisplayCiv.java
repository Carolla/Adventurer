
package civ;

import hic.Mainframe;
import pdc.Util;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

public class BuildingDisplayCiv
{
  static private BuildingDisplayCiv _bldgDspCiv = null;

  // private final MainframeCiv to send buildings for display and description
  private MainframeCiv _mfc = null;

  // TOD Move connection from Mainframe to MainframeCiv for calls back to the GUI
  private Mainframe _frame = null;

  private final BuildingRegistry _bReg;

  private Building _bldg;
  // The building that is currently displayed, either inside or outside
  private Building _currentBldg;

  private static final String NO_BLDG_FOUND = "Could not find that building.\n";


  /** Constructor */
  private BuildingDisplayCiv()
  {
    // Get the Mainframe for display
    _frame = Mainframe.getInstance();
    RegistryFactory regFactory = RegistryFactory.getInstance();
    // Get the BuildingRegistry for retrieving the proper building
    _bReg = (BuildingRegistry) regFactory.getRegistry(RegKey.BLDG);
    _currentBldg = null;
  }

  /**
   * Is a singleton so that any command can get to it. All commands occur in the context of some
   * building
   */
  static public BuildingDisplayCiv getInstance()
  {
    if (_bldgDspCiv == null) {
      _bldgDspCiv = new BuildingDisplayCiv();
    }
    return _bldgDspCiv;
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
      _currentBldg = _bldg;
    }
    else {
      _frame.displayText(NO_BLDG_FOUND);
    }
  }


  public void exitBuilding()
  {
    _bldg = null;
    _currentBldg = _bldg; // Hero standing just outside of building he exited
  }

  
  protected Building findAndSetBuilding(String bldName)
  {
    if (_bldg == null) {
      _bldg = _bReg.getBuilding(bldName);
    }
    return _bldg;
  }

  public Building getCurrentBuilding()
  {
    return _currentBldg;
  }


}
