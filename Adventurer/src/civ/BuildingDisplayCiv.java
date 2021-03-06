/**
 * BuildingDisplayCiv.java
 * 
 * Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development,
 * Inc. by email: acline@carolla.com.
 */

package civ;

import chronos.pdc.Adventure;
import chronos.pdc.NPC;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;

/**
 * Manages the town aspects. 
 * 
 * @author Al Cline
 * 
 * @version Feb 20, 2015 // updated from earlier version by Tim Armstrong <br>
 *          Nov 18 2015 // Moved building stuff from Mainframe into BuildingDisplayCiv <br>
 */
public class BuildingDisplayCiv
{
  protected MainframeCiv _mfCiv;
  // private UserMsgInterface _output;
  private BuildingRegistry _breg;
  private Adventure _adv;

  protected boolean _insideBldg = false;

  /** The building that is currently displayed, either inside or outside */
  protected Building _currentBldg;

  private static final String TOWN_NAME_LEADER = " The Town of ";
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

  /** Default Buildings to initialize registry with */
  public static final String[][] DEFAULT_BUILDINGS = {{"Ugly Ogre Inn", "Bork"},
      {"Rat's Pack", "Dewey N. Howe"}, {"The Bank", "J. P. Pennypacker"},
      {"Stadium", "Aragon"}, {"Arcaneum", "Pendergast"}, {"Monastery", "Balthazar"},
      {"Rouge's Tavern", "Ripper"}, {"Jail", "The Sheriff"}};

  // ======================================================================
  // Constructors and constructor helpers
  // ======================================================================

  /**
   * This object takes a MainframeInterface GUI object to receive image and text output. It
   * uses the command {@code setOutput()} because not all callers of this object have or know
   * which one it is. In almost all cases, the output GUI is {@code hic.Mainframe}, which
   * implements {@code MainframeInterface}.
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

      displayBuildingExterior();
      return true;
    } else {
      _mfCiv.displayText(ERRMSG_NOBLDG);
      return false;
    }
  }

  // ======================================================================
  // Public methods
  // ======================================================================

  public boolean canApproach(String bldgParm)
  {
    // The Hero cannot be inside a building already
    if (isInside() == true) {
      _mfCiv.displayErrorText(ERRMSG_JUMPBLDG);
      // _output.displayErrorText(ERRMSG_JUMPBLDG);
      return false;
    }

    // Case 1: Building name is given
    if (!bldgParm.isEmpty()) {
      Building b = _breg.getBuilding(bldgParm);

      // Check that the building specified actually exists
      if (b == null) {
        _mfCiv.displayErrorText(ERRMSG_UNKNOWN_BLDG);
        // _output.displayErrorText(ERRMSG_UNKNOWN_BLDG);
        return false;
      } else {
        return true;
      }
    } else {
      // Case 2: No building specified
      _mfCiv.displayErrorText(ERRMSG_NOBLDG);
      // _output.displayErrorText(ERRMSG_NOBLDG);
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
   * Display the bulding's interior
   */
  public void displayBuildingInterior()
  {
    if (_currentBldg != null) {
      _mfCiv.displayImage(_currentBldg.getName(), _currentBldg.getIntImagePath());
      _mfCiv.displayText(_currentBldg.getInteriorDescription());
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
      displayBuildingInterior();
    } else {
      _mfCiv.displayErrorText(ERRMSG_NOBLDG);
      System.err.println("error case of enterBuilding");
    }
  }

  public boolean canTalkTo(String target)
  {
    if (_currentBldg == null || !isInside()) {
      return false;
    } else {
      return _currentBldg.contains(target);
    }
  }

  public String getCurrentBuilding()
  {
    return (_currentBldg == null) ? "" : _currentBldg.getName();
  }

  /** This should replace "String getCurrentBuilding()" */
  public Building getCurrBuilding()
  {
    return _currentBldg;
  }

  public Building getBuildingObject()
  {
    String bldgName = getCurrentBuilding();
    Building curBldg = _breg.getBuilding(bldgName);
    return curBldg;
  }

  public boolean isOnTown()
  {
    return !_insideBldg && _currentBldg == null;
  }

  /** Is Hero is inside a Building? */
  public boolean isInside()
  {
    return _insideBldg;
  }

  /** Go to the outside of the building */
  public void leaveBuilding()
  {
    if (isInside()) {
      // approach puts us back outside
      approachBuilding(_currentBldg.getName());
    }
  }

  /** Creates the standard layout to display the town image and description */
  public void openTown()
  {
    returnToTown();
    _mfCiv.displayImage(TOWN_NAME_LEADER + _adv.getTownName(), TOWN_IMAGE);
    _mfCiv.displayText(_adv.getOverview());
  }

  /**
   * Provides a way to clear the current Building
   */
  public void returnToTown()
  {
    _currentBldg = null;
    _insideBldg = false;
  }

  /**
   * Display the bulding's image exterior
   */
  private void displayBuildingExterior()
  {
    if (_currentBldg != null) {
      _mfCiv.displayImage(_currentBldg.getName(), _currentBldg.getExtImagePath());
      _mfCiv.displayText(_currentBldg.getExteriorDescription());
    }
  }

  public String inspectTarget(String target)
  {
    String result = "";
    if (isInside()) {
      result = _currentBldg.inspect(target);
      _mfCiv.displayText(result);
      return result;
    }
    return result;
  }

  public boolean talkToTarget(String target)
  {
    if (isInside()) {
      for (NPC npc : _currentBldg.getPatrons()) {
        if (npc.getName().equalsIgnoreCase(target)) {
          String answer = npc.talk(12);
          _mfCiv.displayText(answer);
          return true;
        }
      }
    }
    return false;
  }

} // end of BuildingDisplayCiv class
