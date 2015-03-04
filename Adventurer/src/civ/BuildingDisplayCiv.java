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
  /** All buildings are stored in this registry */
  private final BuildingRegistry _bReg;
  // The building that is currently displayed, either inside or outside
  private Building _currentBldg;

  private static final String NO_BLDG_FOUND = "Could not find that building.\n";

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
    RegistryFactory regFactory = RegistryFactory.getInstance();
    // Get the BuildingRegistry for retrieving the proper building
    _bReg = (BuildingRegistry) regFactory.getRegistry(RegKey.BLDG);
    _currentBldg = null;
  }

  /**
   * Set the hic output device (or a test proxy after the object is created
   * 
   * @param mf the generic socket for receiving image and text outputs
   */
  public void setOutput(MainframeInterface mf)
  {
    _frame = mf;
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
   * @param bldName name of the building to approach
   */
  public void approachBuilding(String bldName)
  {
    Building bldg = _bReg.getBuilding(bldName);
    if (bldg != null) {
      _currentBldg = bldg;
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
   * Display the bulding's image (exterior or interior) in the frame's image panel and
   * 
   * @param description description of the building's interior or exterior
   * @param imagePath image of the building's exterior or interior room
   */
  private void displayBuilding(String description, String imagePath)
  {
    if ((description.length() > 0) && (imagePath.length() > 0)) {
      String bldgName = _currentBldg.getName();
      _frame.setImage(Util.convertToImage(imagePath));
      _frame.setImageTitle(bldgName);
      _frame.displayText(description);
    } else {
      _frame.displayErrorText("Unable to display building " + _currentBldg);
    }
    _frame.redraw(); // this is pure GUI, s.b. in Mainframe, not here
  }

  /**
   * Show the interior image and description of the Building
   * 
   * @param bldName name of the building to enter
   */
  public void enterBuilding(String bldName)
  {
    Building bldg = _bReg.getBuilding(bldName);
    if (bldg != null) {
      _currentBldg = bldg;
      String description = bldg.getInteriorDescription();
      String imagePath = bldg.getIntImagePath();
      displayBuilding(description, imagePath);
      _frame.setOnTown(false);
    }
    else {
      _frame.displayText(NO_BLDG_FOUND);
    }
  }


  public Building getCurrentBuilding()
  {
    return _currentBldg;
  }

  
  // ======================================================================
  // Inner Class MockBldgCiv
  // ======================================================================

  public class MockBldgCiv
  {

    public MockBldgCiv()
    {}

    public void setCurrentBldg(Building b)
    {
      BuildingDisplayCiv.this._currentBldg = b;
    }


  } // end of MockBldgCiv class



} // end of BuildingDisplayCiv class
