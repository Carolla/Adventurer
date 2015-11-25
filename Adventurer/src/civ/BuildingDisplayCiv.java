/**
 * BuidlingDisplayCiv.java
 * 
 * Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, post on servers, to reuse, or to redistribute to lists, requires prior
 * specific permission and/or a fee. Request permission to use from Carolla Development, Inc. by
 * email: acline@carolla.com.
 */

package civ;

import chronos.pdc.Adventure;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;

/**
 * Manages the town and buildings displays and text descriptions, both interior and exterior.
 * {@code {@ImagePanel} manages the images, and {@code IOPanel} manages the user text
 * input and output.
 * 
 * @author Al Cline
 * @version Feb 20, 2015 // updated from earlier version by Tim Armstrong <br>
 *          Nov 18 2015 // Moved building stuff from Mainframe into BuildingDisplayCiv <br>
 */
public class BuildingDisplayCiv extends BaseCiv
{
  private MainframeCiv _mfCiv;
  private BuildingRegistry _breg;
  private Adventure _adv;

  private boolean _onTown = true;
  private boolean _insideBldg = false;
  
  /** The building that is currently displayed, either inside or outside */
  protected Building _currentBldg;

  /** Image of the Town containing the Buildings */
  private static final String TOWN_IMAGE = "ext_BiljurBaz.JPG";

  /** Error message if no arguments or multiple arguments specified */
  private final String ERRMSG_NOBLDG =
      "Sure, but you've gotta say WHICH building to approach.";
  /** Error message if building not found in registry */
  private final String ERRMSG_UNKNOWN_BLDG =
      "That some kinda slang, stranger?  WHAT building was that again?";
  /** Message if trying to jump from interior to exterior of buildings */
  private final String ERRMSG_JUMPBLDG =
      "You must leave this building before you approach another.";

  // State for building descriptions and images
  private final boolean INTERIOR = true;
  private final boolean EXTERIOR = false;
  private String _townTitle;

  /** Default Buildings to initialize registry with */
  public static final String[][] DEFAULT_BUILDINGS = { {"Ugly Ogre Inn", "Bork"},
      {"Rat's Pack", "Dewey N. Howe"}, {"The Bank", "Ogden Moneypenny"},
      {"Stadium", "Aragon"}, {"Arcaneum", "Pendergast"}, {"Monastery", "Balthazar"},
      {"Rogues' Den", "Ripper"}, {"Jail", "The Sheriff"}, {"Quasqueton", "Unknown"}};

  // ======================================================================
  // Constructors and constructor helpers
  // ======================================================================

  /**
   * This object takes a MainframeInterface GUI object to receive image and text output. It uses the
   * command {@setOutput()} because not all callers of this object have or know which one it is. In
   * almost all cases, the output GUI is {@code hic.Mainframe}, which implements
   * {@code MainframeInterface}.
   * 
   * @param mainframeCiv handles things
   * @param breg is needed for building registries for loading the town
   * @param adv the adventure selected by the user
   */
  public BuildingDisplayCiv(MainframeCiv mfCiv, Adventure adv, BuildingRegistry breg)
  {
    _mfCiv = mfCiv;
    _breg = breg;
    _adv = adv;
    _townTitle = " The Town of " + _adv.getTownName();
  }

  // ======================================================================
  // Public methods
  // ======================================================================

  public boolean canApproach(String bldgParm)
  {
    // The Hero cannot be inside a building already
    if (isInside() == true) {
      _mfCiv.displayErrorText(ERRMSG_JUMPBLDG);
      return false;
    }

    // Case 1: Building name is given
    if (!bldgParm.isEmpty()) {
      Building b = _breg.getBuilding(bldgParm);

      // Check that the building specified actually exists
      if (b == null) {
        _mfCiv.displayErrorText(ERRMSG_UNKNOWN_BLDG);
        return false;
      } else {
        return true;
      }
    } else {
      // Case 2: No building specified
      _mfCiv.displayErrorText(ERRMSG_NOBLDG);
      return false;
    }
  }

  // ======================================================================
  // Public methods
  // ======================================================================
  
  /**
   * Show the exterior image and description of the Building
   * 
   * @param _targetBuilding Building object
   */
  public boolean approachBuilding(String bldg)
  {
    Building targetBuilding = _breg.getBuilding(bldg);
    if (targetBuilding == null) {
      targetBuilding = _currentBldg;
    }
  
    if (targetBuilding != null) {
      _currentBldg = targetBuilding;
      _insideBldg = false;
      _onTown = false;
  
      displayBuilding(targetBuilding, EXTERIOR);
      return true;
    } else {
      _mfCiv.displayText(ERRMSG_NOBLDG);
      return false;
    }
  }

  public boolean canEnter(String bldgParm)
  {
    // The Hero cannot be inside a building already
    if (isInside()) {
      _mfCiv.displayErrorText(ERRMSG_JUMPBLDG);
      return false;
    }

    // Case 1: Building name is given
    if (bldgParm.length() > 0) {
      // Check that the building specified actually exists
      Building b = _breg.getBuilding(bldgParm);
      if (b == null) {
        _mfCiv.displayErrorText(ERRMSG_UNKNOWN_BLDG);
        return false;
      } else {
        return true;
      }
    } else {
      // Case 2: Building defaults to current building
      if (_currentBldg == null) {
        _mfCiv.displayErrorText(ERRMSG_NOBLDG);
        return false;
      } else {
        return true;
      }
    }
  }

  /**
   * Show the interior image and description of the Building
   * 
   * @param name of building to enter, if not provided, will attempt to open current building
   */
  public void enterBuilding(String name)
  {
    Building targetBuilding = _breg.getBuilding(name);
    if (targetBuilding == null) {
      targetBuilding = _currentBldg;
    }
    if (targetBuilding != null) {
      _currentBldg = targetBuilding;
      _insideBldg = true;
      _onTown = false;
      displayBuilding(targetBuilding, INTERIOR);
    } else {
      _mfCiv.displayErrorText(ERRMSG_NOBLDG);
      System.err.println("error case of enterBuilding");
    }
  }

  public String getCurrentBuilding()
  {
    return _currentBldg.getName();
  }

  public boolean isOnTown()
  {
    return _onTown;
  }

  /** Is Hero is inside a Building? */
  public boolean isInside()
  {
    return _insideBldg;
  }

  /** Go to the outside of the building */
  public void leaveBuilding()
  {
    _insideBldg = false;
  }

  /** Creates the standard layout to display the town image and description */
  public void openTown()
  {
    returnToTown();
    _mfCiv.displayImage(_townTitle, TOWN_IMAGE);
    _mfCiv.displayText(_adv.getOverview());
  }

  /**
   * Provides a way to clear the current Building
   */
  public void returnToTown()
  {
    _currentBldg = null;
    _onTown = true;
    _insideBldg = false;
  }

  /**
   * Display the bulding's image (exterior or interior) in the frame's image panel
   * 
   * @param description description of the building's interior or exterior
   * @param imagePath image of the building's exterior or interior room
   */
  public void displayBuilding()
  {
    if (_currentBldg != null) {
      _mfCiv.displayImage(_currentBldg.getName(), _currentBldg.getIntImagePath());
      _mfCiv.displayText(_currentBldg.getInteriorDescription());
    }
  }

  /**
   * Display the bulding's image (exterior or interior) in the frame's image panel
   * 
   * @param building to be displayed
   * @param interior if flag is true, else exterior, is displayed
   */
  private void displayBuilding(Building building, boolean interior)
  {
    if (building != null) {
      String description =
          interior ? building.getInteriorDescription() : building.getExteriorDescription();
      String imagePath = interior ? building.getIntImagePath() : building.getExtImagePath();
      String bldgName = building.getName();

      _mfCiv.displayImage(bldgName, imagePath);
      _mfCiv.displayText(description);
    }
  }

  public String inspectTarget(String target)
  {
    String result = "";
    if (_currentBldg != null) {
      result = _currentBldg.inspect(target);
      _mfCiv.displayText(result);
      return result;
    }
    return result;
  }
} // end of BuildingDisplayCiv class
