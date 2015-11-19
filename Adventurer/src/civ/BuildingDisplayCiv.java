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

import java.awt.Image;

import pdc.Util;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;

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
    /** Reference to socket for Mainframe or test proxy */
    protected MainframeInterface _frame = null;
    /** The Hero is on town, not at any particular building */
    private boolean _onTown = true;
    /** The building that is currently displayed, either inside or outside */
    protected Building _currentBldg;
    /** Flag to indicate whether here is inside the building (ENTER) or outside (APPROACH) */
    private boolean _insideBldg = false;
    /** BuildingDisplayCiv knows about buildings */
    private BuildingRegistry _breg;

    
//    private static final String NO_BLDG_FOUND = "Could not find that building.\n";
    /** Error message if no arguments or multiple arguments specified */
    private final String ERRMSG_NOBLDG =
            "Sure, but you've gotta say WHICH building to approach.";
    /** Error message if building not found in registry */
    private final String ERRMSG_UNKNOWN_BLDG =
            "That some kinda slang, stranger?  WHAT building was that again?";

    /** Message if trying to jump from interior to exterior of buildings */
    private final String ERRMSG_JUMPBLDG =
            "You must leave this building before you approach another.";

    // ======================================================================
    // Constructors and constructor helpers
    // ======================================================================

    /**
     * This object takes a MainframeInterface GUI object to receive image and text output. It uses
     * the command {@setOutput()} because not all callers of this object have or know which one it
     * is. In almost all cases, the output GUI is {@code hic.Mainframe}, which implements
     * {@code MainframeInterface}.
     * 
     * @param mainframe
     */
    public BuildingDisplayCiv(MainframeInterface mainframe, BuildingRegistry breg)
    {
        _frame = mainframe;
        _breg = breg;
        _currentBldg = null;
    }

    // end of MockBldgCiv class

    public boolean canApproach(String bldgParm)
    {
        // The Hero cannot be inside a building already
        if (isInside() == true) {
            _frame.displayErrorText(ERRMSG_JUMPBLDG);
            return false;
        }

        // Case 1: Building name is given
        if (!bldgParm.isEmpty()) {
            Building b = _breg.getBuilding(bldgParm);

            // Check that the building specified actually exists
            if (b == null) {
                _frame.displayErrorText(ERRMSG_UNKNOWN_BLDG);
                return false;
            } else {
                return true;
            }
        }
        else {
            // Case 2: No building specified
            _frame.displayErrorText(ERRMSG_NOBLDG);
            return false;
        }
    }

    public boolean canEnter(String bldgParm)
    {
        // The Hero cannot be inside a building already
        if (isInside()) {
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
            String description = targetBuilding.getExteriorDescription();
            String imagePath = targetBuilding.getExtImagePath();
            Image image = Util.convertToImage(imagePath);
            _frame.setImage(image);
            _frame.displayText(description);
            return true;
        } else {
            _frame.displayText(ERRMSG_NOBLDG);
            return false;
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
            displayBuilding();
        }
        else {
            _frame.displayErrorText(ERRMSG_NOBLDG);
            System.err.println("error case of enterBuilding");
        }
    }

    public boolean isOnTown()
    {
        return _onTown;
    }

    public String getCurrentBuilding()
    {
        return _currentBldg.getName();
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
            String description = _currentBldg.getInteriorDescription();
            String imagePath = _currentBldg.getIntImagePath();
            String bldgName = _currentBldg.getName();
            
            tellFrameToDisplayBuilding(description, imagePath, bldgName);
        }
    }

    protected void tellFrameToDisplayBuilding(String description, String imagePath, String bldgName)
    {
      _frame.setBuilding(null); // Confusing, I know. This sets the building RECTANGLE
      _frame.setImage(Util.convertToImage(imagePath));
      _frame.setImageTitle(bldgName);
      _frame.displayText(description);
      _frame.redraw(); // this is pure GUI, s.b. in Mainframe, not here
    }

    public void inspectTarget(String target)
    {
      if (_currentBldg != null) {
        String result = _currentBldg.inspect(target);
        _frame.displayText(result);
      }
    }

    
} // end of BuildingDisplayCiv class
