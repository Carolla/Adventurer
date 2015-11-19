/**
 * MainframeCiv.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import java.awt.Image;
import java.util.ArrayList;

import chronos.pdc.Adventure;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import hic.ChronosPanel;
import hic.Mainframe;
import hic.MainframeInterface;
import pdc.Util;

/**
 * The main civ behind the Mainframe screen.
 * 
 * @author Alan Cline
 * @author Tim Armstrong
 * @version Nov 2, 2013 // moved from CIV component <br>
 *          Mar 19 2014 // added current Building for ENTER command <br>
 *          Aug 18 2014 // added {@code displayImage} to show Chronos logo on portal page <br>
 *          Nov 7, 2015 // re-architected HIC.Mainframe to separate better CIV.MainframeCiv <br>
 *          Nov 13, 2015 // allow BuildingDisplayCiv to talk to this object. <br>
 */
public class MainframeCiv extends BaseCiv
{
  private MainframeInterface _mf;

  private Adventure _adv;
  private AdventureRegistry _advReg;
  private BuildingDisplayCiv _bldgCiv;
  private RegistryFactory _rf;

  private ChronosPanel _imagePanel;
  private ChronosPanel _mainActionPanel;
  private MainActionCiv _mainActionCiv;

  /** Initial right-side image: Chronos logo */
  private static final String INITIAL_IMAGE = "ChronosLogo.jpg";
  /** Title of initial image */
  private static final String INITIAL_TITLE = "Chronos Logo";


  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /**
   * Create the Civ associated with the mainframe
   * 
   * @param frame owner of the widget for which this civ applies
   * @param personRW supports the Summon Hero and Create Hero buttons
   * @param advReg registry to support the Adventures button
   */
  public MainframeCiv()
  {
    _rf = new RegistryFactory();
    _rf.initRegistries();

    // Create the Mainframe window with holders for subordinate panels */
    _mf = new Mainframe(this);

    // Create the right side image panel to be used by many Civs */
//    _imagePanel = new ImagePanel(this);
    _imagePanel = new ChronosPanel(this);
    _imagePanel.setImage(Util.convertToImage(INITIAL_IMAGE));
    _imagePanel.setTitle(INITIAL_TITLE);
    _mf.replaceRightPanel(_imagePanel);

    // Create the left side panel to hold the main action buttons */
    _mainActionCiv = new MainActionCiv(_mf, this);
    _mainActionPanel = _mainActionCiv.getActionPanel();
    _mf.replaceLeftPanel(_mainActionPanel);

    // Build the associated registries needed
//     BuildingRegistry breg = (BuildingRegistry) _rf.getRegistry(RegKey.BLDG);
//     _bldgCiv = new BuildingDisplayCiv(this, breg);
//     _advReg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
  }


  // /**
  // * Perform construction act. This wires together all the "single instance variables" for the
  // * Adventurer application. None of these constructors should ever be called anywhere outside of
  // * this method and in testing.
  // */
  // private void constructMembers()
  // {
  // BuildingRegistry breg = (BuildingRegistry) _rf.getRegistry(RegKey.BLDG);
  // _bldgCiv = new BuildingDisplayCiv(_mf, breg);
  //
  // _advReg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
  //
  // // TODO: Move this to outside this class, perhaps the BuildingDisplayCiv
  // Inn inn = (Inn) breg.getBuilding("Ugly Ogre Inn");
  // inn.setMsg(this);
  // inn.initPatrons();
  // }


//  /** Create the image panel civ and widget */
//  private ChronosPanel createImagePanel()
//  {
//    // Get the image panel from its civ
//    _imagePanel = new ImagePanel(this);
//    _imagePanel.setImage(getInitialImage());
//    _imagePanel.setTitle(getInitialTitle());
//    return _imagePanel;
//  }


  /**
   * TODO This is the responsibility of the BuildingDisplayCiv Enter the Building specified. If the
   * Hero is at the Town level, get the {@code BuildingRegistry} and {@ocde BuildingCiv}
   *
   * @param bldName the name of the building to open
   */
  public void enterBuilding(String bldName)
  {
    if (_bldgCiv.canApproach(bldName)) {
      _bldgCiv.approachBuilding(bldName);
    }
  }


  // ============================================================
  // Public methods
  // ============================================================


//  /** Set up the GUI elements needed at throughout the program */
//  public void setInitialImage()
//  {
//    _imagePanel.setImage(Util.convertToImage(INITIAL_IMAGE));
//    _imagePanel.setTitle(INITIAL_TITLE);
//  }

  
  // ============================================================
  // Public methods
  // ============================================================

  // @Override
  // public void errorOut(String msg)
  // {
  // _ioPanel.displayErrorText(msg);
  // }


  // ============================================================
  // Public methods
  // ============================================================

  /**
   * Retrieves the Adventures for selection from the Adventure Registry
   * 
   * @return the list of Adventures
   */
  public ArrayList<String> getAdventures()
  {
    ArrayList<Adventure> adventures = _advReg.getAdventureList();
    ArrayList<String> results = new ArrayList<String>();
    for (Adventure a : adventures) {
      results.add(a.getKey());
    }
    return results;
  }


  // ============================================================
  // Public methods
  // ============================================================

  public BuildingDisplayCiv getBuildingDisplayCiv()
  {
    return _bldgCiv;
  }


  public ChronosPanel getImagePanel()
  {
    return _imagePanel;
  }


  public Image getInitialImage()
  {
    return Util.convertToImage(INITIAL_IMAGE);
  }

  public String getInitialTitle()
  {
    return INITIAL_TITLE;
  }

  /**
   * Get the Mainframe direct access
   * 
   * @return the mainframe reference
   */
  public MainframeInterface getMainframe()
  {
    return _mf;
  }


  /**
   * Get the town name
   * 
   * @return the name of the town
   */
  public RegistryFactory getRegistryFactory()
  {
    return _rf;
  }


  /**
   * Get the town name
   * 
   * @return the name of the town
   */
  public String getTownName()
  {
    return _adv.getTownName();
  }

  // // TODO Move to GUI object
  // public void handleClick(Point p)
  // {
  // handleClickIfOnTownReturn(p);
  // if (_bldgCiv.isOnTown()) {
  // handleClickIfOnBuilding(p);
  // }
  // }
  //
  // // TODO: Mouse action belong in the HIC; not in the CIV
  // public void handleMouseMovement(Point p)
  // {
  // if (_bldgCiv.isOnTown()) {
  // for (BuildingRectangle rect : _buildingList.values()) {
  // if (rect.contains(p)) {
  // _mf.setBuilding(rect);
  // break;
  // }
  // }
  // }
  // }


  // @Override
  // public void msgOut(String msg)
  // {
  // _mf.displayText(msg);
  // }


  // /**
  // * Display a prompt message asking for confirmation
  // *
  // * @param msg question to ask for confirmation
  // * @return true if the user seleted YES
  // */
  // public boolean msgPrompt(String msg)
  // {
  // return _mf.displayPrompt(msg);
  // }

  // TODO: Move this to MainACtionCiv
  // /**
  // * Find a list of heroes that can be summoned
  // *
  // * @return list of the heroes
  // */
  // public List<String> openDormitory()
  // {
  // List<String> heroes = _personRW.wakePeople();
  // return heroes;
  // }


  // /** Creates the standard layout to display the town image and description */
  // public void openTown()
  // {
  // _bldgCiv.returnToTown();
  // Image townImage = Util.convertToImage(TOWN_IMAGE);
  // _mf.setImage(townImage);
  // if (_adv != null) {
  // String townTitle = " The Town of " + _adv.getTownName();
  // _mf.setImageTitle(townTitle);
  // _mf.displayText(_adv.getOverview());
  // }
  // }

  /** Close down the application if user so specified */
  public void quit()
  {
    if (_mf.displayPrompt("Quit Adventurer?") == true) {
      Adventurer.approvedQuit();
    }
  }


  // ============================================================
  // Private methods
  // ============================================================

  // /** Create the clickable areas on the town view to indicate a selected Building */
  // private void createBuildingBoxes()
  // {
  // for (int i = 0; i < DEFAULT_BUILDINGS.length; i++) {
  // String bName = DEFAULT_BUILDINGS[i][0];
  // BuildingRectangle r =
  // new BuildingRectangle(bName, colorArray[i], _mf.getImagePanelSize(),
  // buildingLayouts[i]);
  // _buildingList.put(bName, r);
  // }
  // }
  //
  // private void handleClickIfOnBuilding(Point p)
  // {
  // for (Entry<String, BuildingRectangle> entry : _buildingList.entrySet()) {
  // BuildingRectangle rect = entry.getValue();
  // if (rect.contains(p)) {
  // enterBuilding(entry.getKey());
  // return;
  // }
  // }
  // }
  //
  //
  // private void handleClickIfOnTownReturn(Point p)
  // {
  // if (_townReturn.contains(p)) {
  // openTown();
  // }
  // _mf.redraw();
  // }

} // end of MainframeCiv class
