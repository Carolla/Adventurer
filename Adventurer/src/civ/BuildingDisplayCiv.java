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

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import chronos.pdc.Adventure;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;
import hic.BuildingRectangle;
import hic.ChronosPanel;
import hic.IOPanel;
import hic.MainframeInterface;
import mylib.Constants.Side;
import pdc.Util;
import pdc.command.CommandFactory;

/**
 * Manages the town and buildings displays and text descriptions, both interior and exterior.
 * {@code {@ImagePanel} manages the images, and {@code IOPanel} manages the user text input and
 * output.
 * 
 * @author Al Cline
 * @version Feb 20, 2015 // updated from earlier version by Tim Armstrong <br>
 *          Nov 18 2015 // Moved building stuff from Mainframe into BuildingDisplayCiv <br>
 */
public class BuildingDisplayCiv extends BaseCiv
{
  /** Reference to socket for Mainframe or test proxy */
  private MainframeInterface _mf;
  /** MainframeCiv to access frame actions */
  private MainframeCiv _mfCiv;
  /** Handles text input and output */
  private IOPanel _ioPanel;
  /** Handles building images and the town image */
  private ChronosPanel _imagePanel;

  /** The Hero is on town, not at any particular building */
  private boolean _onTown = true;
  /** The building that is currently displayed, either inside or outside */
  private Building _currentBldg;
  /** Flag to indicate whether here is inside the building (ENTER) or outside (APPROACH) */
  private boolean _insideBldg = false;

  /** BuildingDisplayCiv knows about buildings */
  private BuildingRegistry _breg;
  /** CommandParser to handle user's commands */
  private CommandParser _cp;
  /** Adventure that defines the town and buildings */
  private Adventure _adv;

  /** Image of the Town containing the Buildings */
  private static final String TOWN_IMAGE = "ext_BiljurBaz.JPG";
  /** Title for town Image */

  // private static final String NO_BLDG_FOUND = "Could not find that building.\n";
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
  public static final String[][] DEFAULT_BUILDINGS = {{"Ugly Ogre Inn", "Bork"},
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
   * @param mainframe connects to the frame that holds all panels
   * @param mainActionCiv handles the main button panel
   * @param breg is needed for building registries for loading the town
   * @param adv the adventure selected by the user
   */
  public BuildingDisplayCiv(MainframeInterface mf, MainActionCiv mainActionCiv,
      BuildingRegistry breg, Adventure adv)
  {
    _breg = breg;
    _mf = mf;
    _adv = adv;
    
    // Create the IOPanel for input commands via commandParser and output messages
    _cp = new CommandParser(new CommandFactory(this));
    _ioPanel = new IOPanel(this, _cp);
    _mf.replaceLeftPanel(_ioPanel);

    // Create the town and building image display panel
//    _imagePanel = new ChronosPanel(this, "<town name>", Side.RIGHT);
    _imagePanel = new ChronosPanel(this, _adv.getTownName(), Side.RIGHT);
    _mf.replaceRightPanel(_imagePanel);

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
      // Show the building description and image
      String description = targetBuilding.getExteriorDescription();
      String imagePath = targetBuilding.getExtImagePath();
      Image image = Util.convertToImage(imagePath);
      _imagePanel.setImage(image);
      _ioPanel.displayText(description);
      return true;
    } else {
      _ioPanel.displayText(ERRMSG_NOBLDG);
      return false;
    }
  }


  public boolean canApproach(String bldgParm)
  {
    // The Hero cannot be inside a building already
    if (isInside() == true) {
      _ioPanel.displayErrorText(ERRMSG_JUMPBLDG);
      return false;
    }

    // Case 1: Building name is given
    if (!bldgParm.isEmpty()) {
      Building b = _breg.getBuilding(bldgParm);

      // Check that the building specified actually exists
      if (b == null) {
        _ioPanel.displayErrorText(ERRMSG_UNKNOWN_BLDG);
        return false;
      } else {
        return true;
      }
    } else {
      // Case 2: No building specified
      _ioPanel.displayErrorText(ERRMSG_NOBLDG);
      return false;
    }
  }

  public boolean canEnter(String bldgParm)
  {
    // The Hero cannot be inside a building already
    if (isInside()) {
      _ioPanel.displayErrorText(ERRMSG_JUMPBLDG);
      return false;
    }

    // Case 1: Building name is given
    if (bldgParm.length() > 0) {
      // Check that the building specified actually exists
      Building b = _breg.getBuilding(bldgParm);
      if (b == null) {
        _ioPanel.displayErrorText(ERRMSG_NOBLDG);
        return false;
      } else {
        return true;
      }
    } else {
      // Case 2: Building defaults to current building
      if (_currentBldg == null) {
        _ioPanel.displayErrorText(ERRMSG_NOBLDG);
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
      _ioPanel.displayErrorText(ERRMSG_NOBLDG);
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
    // Set up the IOPanel
    _ioPanel = initIOPanel();
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

  public void mouseEntered(MouseEvent e)
  {}

  public void mouseExited(MouseEvent e)
  {}



  /** Creates the standard layout to display the town image and description */
  public void openTown()
  {
    returnToTown();
    Image townImage = Util.convertToImage(TOWN_IMAGE);
    _imagePanel.setImage(townImage);
    if (_adv != null) {
      String townTitle = " The Town of " + _adv.getTownName();
      _imagePanel.setTitle(townTitle);
      _ioPanel.displayText(_adv.getOverview());
    }
  }

  /** Set a building's rectangle onto the panel showing the town's image */
  public void setBuilding(BuildingRectangle rect)
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
  // public void handleClick(Point p)
  public void returnToTown(Point p)
  {
    handleClickIfOnTownReturn(p);
    if (isOnTown()) {
      handleClickIfOnBuilding(p);
    }
  }


  /** Define the building to APPROACH based on where the user clicked */
  // public void handleMouseMovement(Point p)
  public void setBuildingSelected(Point p)
  {
    if (isOnTown()) {
      for (BuildingRectangle rect : _buildingList.values()) {
        if (rect.contains(p)) {
          setBuilding(rect);
          break;
        }
      }
    }
  }


  /** Create the clickable areas on the town view to indicate a selected Building */
  private void createBuildingBoxes()
  {
    for (int i = 0; i < DEFAULT_BUILDINGS.length; i++) {
      String bName = DEFAULT_BUILDINGS[i][0];
      BuildingRectangle r =
          new BuildingRectangle(bName, colorArray[i], _imagePanel.getPreferredSize(),
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

      setBuilding(null); // Confusing, I know. This sets the building RECTANGLE
      _imagePanel.setImage(Util.convertToImage(imagePath));
      _imagePanel.setTitle(bldgName);
      _ioPanel.displayText(description);
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


  /**
   * Create the IOPanel and link it with the command parser for handling user input and output
   * 
   * @return
   */
  private IOPanel initIOPanel()
  {
    // // Create the IOPanel for input commands via commandParser and output messages
    // _cp = new CommandParser(new CommandFactory(this));
    // _ioPanel = new IOPanel(this, _cp);

    // // Display the IOPanel on the left side of the mainframe
    // _mf = _mfCiv.getMainframe();
    // _mf.replaceLeftPanel(_ioPanel);

    return _ioPanel;
  }


} // end of BuildingDisplayCiv class
