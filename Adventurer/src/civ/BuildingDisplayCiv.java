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

import hic.MainframeInterface;
import pdc.Util;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

/**
 * Maintain displays and text descriptions of all Buildings, both interior and exterior. It shows
 * building images in an image panel, and text descriptions of the buildings in the output portion
 * of an IOPanel.
 * 
 * @author Al Cline
 * @version Feb 20, 2015 // updated from earlier version by Tim Armstrong <br>
 */
public class BuildingDisplayCiv
{
  /** Reference to self singleton */
  static private BuildingDisplayCiv _bldgDspCiv = null;
  /** Reference to socket for Mainframe or test proxy */
  private MainframeInterface _frame = null;
  /** The Hero is on town, not at any particular building */
  private boolean _onTown;
  /** The building that is currently displayed, either inside or outside */
  private Building _currentBldg;
  /** Flag to indicate whether here is inside the building (ENTER) or outside (APPROACH) */
  private boolean _insideBldg = false;
  /** BuildingDisplayCiv knows about buildings */
  private BuildingRegistry _breg;

  private static final String NO_BLDG_FOUND = "Could not find that building.\n";

  /** Error message if no current building to enter */
  private final String ERRMSG_NOBLDG =
      "I don't know that building. What building did you want to enter?";
  /** Message if trying to jump from interior to interior of buildings */
  private final String ERRMSG_JUMPBLDG =
      "You must leave (exit) one building before you enter another.";

  // ======================================================================
  // Constructors and constructor helpers
  // ======================================================================

  /**
   * This object takes a MainframeInterface GUI object to receive image and text output. It uses the
   * command {@setOutput()} because not all callers of this object have or know which one it is. In
   * almost all cases, the output GUI is {@code hic.Mainframe}, which implements
   * {@code MainframeInterface}.
   */
  private BuildingDisplayCiv()
  {
    _breg = (BuildingRegistry) RegistryFactory.getInstance().getRegistry(RegKey.BLDG);
    _currentBldg = null;
  }

  public boolean canEnter(String bldgParm)
  {
    // The Hero cannot be inside a building already
    if (isInside()) {
      System.err.println("BuildingDisplayCiv.canenter(): Expected message: " + ERRMSG_JUMPBLDG);
      _frame.displayErrorText(ERRMSG_JUMPBLDG);
      return false;
    }

    // Case 1: Building name is given
    if (bldgParm.length() > 0) {
      // Check that the building specified actually exists
      Building b = _breg.getBuilding(bldgParm);
      if (b == null) {
        _frame.displayErrorText(ERRMSG_NOBLDG);
        return false;
      } else {
        return true;
      }
    } else {
      // Case 2: Building defaults to current building
      if (_currentBldg == null) {
        _frame.displayErrorText(ERRMSG_NOBLDG);
        return false;
      } else {
        return true;
      }
    }
  }

  /**
   * Is a singleton so that any command can get to it. All commands occur in the context of some
   * building. The output display object is set through {@code setOutput(MainframeInterface)}.
   */
  static public BuildingDisplayCiv getInstance()
  {
    if (_bldgDspCiv == null) {
      _bldgDspCiv = new BuildingDisplayCiv();
    }
    return _bldgDspCiv;
  }



  // ======================================================================
  // Public methods
  // ======================================================================

  /**
   * Show the exterior image and description of the Building
   * 
   * @param bldg Building object
   */
  public void approachBuilding(Building bldg)
  {
    if (bldg != null) {
      _currentBldg = bldg;
      _insideBldg = false;
      _onTown = false;
      String description = bldg.getExteriorDescription();
      String imagePath = bldg.getExtImagePath();
      displayBuilding(description, imagePath);
    }
    else {
      _frame.displayText(NO_BLDG_FOUND);
    }
  }


  // =============================================================
  // Mock inner class for testing
  // =============================================================

  /**
   * Show the interior image and description of the Building
   * 
   * @param bldg to enter
   */
  public void enterBuilding(Building bldg)
  {
    if (bldg != null) {
      _currentBldg = bldg;
      _insideBldg = true;
      _onTown = false;
      String description = bldg.getInteriorDescription();
      String imagePath = bldg.getIntImagePath();
      displayBuilding(description, imagePath);
    }
    else {
      _frame.displayErrorText(NO_BLDG_FOUND);
      System.err.println("error case of enterBuilding");
    }
  }

  public boolean isOnTown()
  {
    return _onTown;
  }

  public Building getCurrentBuilding()
  {
    return _currentBldg;
  }

  /** Is Hero is inside a Building? */
  public boolean isInside()
  {
    return _insideBldg;
  }

  /**
   * Provides a way to clear the current Building
   * 
   * @param b the Building that this currently displayed, exterior or interior; may be null
   */
  public void setCurrentBuilding(Building b)
  {
    if (b == null) {
      System.err.println("BuildingDisplayCiv.setCurrentBuilding to " + b);
    } else {
      System.err.println("BuildingDisplayCiv.setCurrentBuilding to " + b.getName());
    }
    _currentBldg = b;
  }

  /**
   * Set the Hero to no building, but at the town view
   * 
   * @param state = true if Hero is on town, else false is Hero is inside or outside a current
   *        building
   */
  public void setOnTown(boolean state)
  {
    _onTown = state;
  }


  // ======================================================================
  // Inner Class MockBldgCiv
  // ======================================================================

  /**
   * Set the hic output device (or a test proxy after the object is created
   * 
   * @param mf the generic socket for receiving image and text outputs
   */
  public void setOutput(MainframeInterface mf)
  {
    _frame = mf;
  }

  // =============================================================
  // Mock inner class for testing
  // =============================================================

  /**
   * Display the bulding's image (exterior or interior) in the frame's image panel
   * 
   * @param description description of the building's interior or exterior
   * @param imagePath image of the building's exterior or interior room
   */
  private void displayBuilding(String description, String imagePath)
  {
    if ((description.length() > 0) && (imagePath.length() > 0)) {
      String bldgName = _currentBldg.getName();
      _frame.setBuilding(null); // Confusing, I know. This sets the building RECTANGLE
      _frame.setImage(Util.convertToImage(imagePath));
      _frame.setImageTitle(bldgName);
      _frame.displayText(description);
    } else {
      _frame.displayErrorText("Unable to display building " + _currentBldg);
    }
    _frame.redraw(); // this is pure GUI, s.b. in Mainframe, not here
  }

  public class MockBldgCiv
  {

    public MockBldgCiv()
    {}

    public void setInsideBldg(boolean state)
    {
      _insideBldg = state;
    }

    public void setOnTown(boolean state)
    {
      _onTown = state;
    }


  } // end of MockBldgCiv class



} // end of BuildingDisplayCiv class
