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

import hic.BuildingRectangle;
import hic.Mainframe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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
  /** MainframeCiv to access frame actions */
  private MainframeCiv _mfCiv;
  /** BuildingDisplayCiv knows about buildings */
  private BuildingRegistry _breg;
  /** Adventure that defines the town and buildings */
  private Adventure _adv;

  /** The Hero is on town, not at any particular building */
  private boolean _onTown = true;
  /** The building that is currently displayed, either inside or outside */
  protected Building _currentBldg;
  /** Flag to indicate whether here is inside the building (ENTER) or outside (APPROACH) */
  private boolean _insideBldg = false;



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

  /** Default Buildings to initialize registry with */
  public static final String[][] DEFAULT_BUILDINGS = { {"Ugly Ogre Inn", "Bork"},
      {"Rat's Pack", "Dewey N. Howe"}, {"The Bank", "Ogden Moneypenny"},
      {"Stadium", "Aragon"}, {"Arcaneum", "Pendergast"}, {"Monastery", "Balthazar"},
      {"Rogues' Den", "Ripper"}, {"Jail", "The Sheriff"}, {"Quasqueton", "Unknown"}};

  private float[][] buildingLayouts = new float[][] {
      {0.48f, 0.54f, 0.14f, 0.08f}, // Ugly Ogre Inn
      {0.79f, 0.43f, 0.14f, 0.08f}, // Rat's Pack General Store
      {0.60f, 0.45f, 0.07f, 0.07f}, // The Bank
      {0.5f, 0.37f, 0.25f, 0.09f}, // Stadium
      {0.61f, 0.73f, 0.37f, 0.20f}, // Arcaneum
      {0.0f, 0.35f, 0.22f, 0.13f}, // Monastery
      {0.63f, 0.53f, 0.10f, 0.05f}, // Rouge's Den
      {0.38f, 0.53f, 0.08f, 0.07f}, // Jail
      {0.31f, 0.08f, 0.5f, 0.25f}}; // Quasqueton

  private Color[] colorArray = new Color[] {Color.white, // Ugly Ogre Inn
      Color.white, // Rat's Pack General Store
      Color.white, // The Bank
      Color.white, // Stadium
      Color.white, // Arcaneum
      Color.white, // Monastery
      Color.white, // Rouge's Den
      Color.white, // Jail
      Color.white}; // Quasqueston

  private Map<String, BuildingRectangle> _buildingList = new TreeMap<String, BuildingRectangle>();
  /** Current Building being displayed, and can be entered */
  private final Rectangle _townReturn = new Rectangle(0, 0, 100, 100);


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

  public String getAdventureName()
  {
    return _adv.getName();
  }

  
  public String getCurrentBuilding()
  {
    return _currentBldg.getName();
  }


  /** Init the town display and IOPanel with adventure overview. */
  public void initAdventure()
  {
    // Create the boxes that highlight the building in the town image
    createBuildingBoxes();

    // Display the town and description for the selected adventure
    openTown();
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

  public void mouseMoved(MouseEvent e)
  {
    // _mfCiv.handleMouseMovement(e.getPoint());
    setBuildingSelected(e.getPoint());
  }

  public void mouseClicked(MouseEvent e)
  {
    returnToTown(e.getPoint());
  }

  public void mouseDragged(MouseEvent e)
  {
    setBuildingSelected(e.getPoint());
  }

  /** Creates the standard layout to display the town image and description */
  public void openTown()
  {
    returnToTown();
    if (_adv != null) {
      String townTitle = " The Town of " + _adv.getTownName();
      _mfCiv.displayImage(townTitle, TOWN_IMAGE);
      _mfCiv.displayText(_adv.getOverview());
    }
  }

  /** Set a building's rectangle onto the panel showing the town's image */
  public void setBuildingRectangle(BuildingRectangle rect)
  {
    // _imagePanel.setRectangle(rect);
  }


  /** Exit the program */
  public void quit()
  {
    _mfCiv.quit();
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


  /** Return to town when icon clicked */
  public void returnToTown(Point p)
  {
    handleClickIfOnTownReturn(p);
    if (isOnTown()) {
      handleClickIfOnBuilding(p);
    }
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

  /** Define the building to APPROACH based on where the user clicked */
  public void setBuildingSelected(Point p)
  {
    if (isOnTown()) {
      for (BuildingRectangle rect : _buildingList.values()) {
        if (rect.contains(p)) {
          setBuildingRectangle(rect);
          break;
        }
      }
    }
  }


  /** Create the clickable areas on the town view to indicate a selected Building */
  private void createBuildingBoxes()
  {
    Dimension displaySize = Mainframe.getWindowSize();
    Dimension halfDisplaySize = new Dimension(displaySize.height, displaySize.width / 2);
    for (int i = 0; i < DEFAULT_BUILDINGS.length; i++) {
      String bName = DEFAULT_BUILDINGS[i][0];
      BuildingRectangle r =
          new BuildingRectangle(bName, colorArray[i], halfDisplaySize,
              buildingLayouts[i]);
      _buildingList.put(bName, r);
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

      setBuildingRectangle(null);
      _mfCiv.displayImage(bldgName, imagePath);
      _mfCiv.displayText(description);
    }
  }


  private void handleClickIfOnBuilding(Point p)
  {
    for (Entry<String, BuildingRectangle> entry : _buildingList.entrySet()) {
      BuildingRectangle rect = entry.getValue();
      if (rect.contains(p)) {
        enterBuilding(entry.getKey());
        return;
      }
    }
  }


  private void handleClickIfOnTownReturn(Point p)
  {
    if (_townReturn.contains(p)) {
      openTown();
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
