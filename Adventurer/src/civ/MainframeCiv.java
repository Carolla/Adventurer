/**
 * Mainframe.Civ  Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import hic.BuildingRectangle;
import hic.Mainframe;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pdc.Util;
import chronos.pdc.Adventure;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import dmc.PersonReadWriter;

/**
 * The main civ behind the Mainframe screen. It also contains all the initialization state.
 * 
 * @author Alan Cline
 * @author Tim Armstrong
 * @version Nov 2, 2013 // moved from CIV component <br>
 *          Mar 19 2014 // added current Building for ENTER command <br>
 *          Aug 18 2014 // added {@code displayImage} to show Chronos logo on portal page <br>
 */
public class MainframeCiv
{
  private AdventureRegistry _advReg;
  private BuildingDisplayCiv _bdCiv;
  private BuildingRegistry _bReg;

  private static final String TOWN_IMAGE = "ext_BiljurBaz.JPG";

  /** Default Buildings to initialize registry with */
  public static final String[][] DEFAULT_BUILDINGS = { {"Ugly Ogre Inn", "Bork"},
      {"Rat's Pack", "Dewey N. Howe"}, {"The Bank", "Ogden Moneypenny"},
      {"Stadium", "Aragon"}, {"Arcaneum", "Pendergast"}, {"Monastery", "Balthazar"},
      {"Rogues' Den", "Ripper"}, {"Jail", "The Sheriff"}, {"Quasqueton", "Unknown"}};

  private Map<String, BuildingRectangle> _buildingList = new TreeMap<String, BuildingRectangle>();

  private float[][] buildingLayouts = new float[][] { {0.48f, 0.54f, 0.14f, 0.08f}, // Ugly Ogre Inn
      {0.79f, 0.43f, 0.14f, 0.08f}, // Rat's Pack General Store
      {0.60f, 0.45f, 0.07f, 0.07f}, // The Bank
      {0.5f, 0.37f, 0.25f, 0.09f}, // Stadium
      {0.61f, 0.73f, 0.37f, 0.20f}, // Arcaneum
      {0.0f, 0.35f, 0.22f, 0.13f}, // Monastery
      {0.63f, 0.53f, 0.10f, 0.05f}, // Rouge's Den
      {0.38f, 0.53f, 0.08f, 0.07f}, // Jail
      {0.31f, 0.08f, 0.5f, 0.25f}}; // Quasqueton

  private Color[] colorArray = new Color[] {Color.red, // Ugly Ogre Inn
      Color.orange, // Rat's Pack General Store
      Color.yellow, // The Bank
      Color.green, // Stadium
      Color.blue, // Arcaneum
      Color.magenta, // Monastery
      Color.black, // Rouge's Den
      Color.cyan, // Jail
      Color.white}; // Quasqueston

  private Mainframe _frame;
  private Adventure _adv;
  private PersonReadWriter _personRW;
  private boolean _onTown;

  /** Current Building being displayed, and can be entered */
  private final Rectangle _townReturn = new Rectangle(0, 0, 100, 100);

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
  public MainframeCiv(Mainframe frame)
  {
    _frame = frame;
    _frame.setImage(Util.convertToImage(INITIAL_IMAGE));
    // TODO Why is this in the civ, and not the hic.Mainframe?
    _frame.setImageTitle(INITIAL_TITLE);
    // _personRW = new PersonReadWriter();
    // _advReg = (AdventureRegistry) RegistryFactory.getInstance().getRegistry(RegKey.ADV);
    createBuildingBoxes();
  }

  private void createBuildingBoxes()
  {
    for (int i = 0; i < DEFAULT_BUILDINGS.length; i++) {
      String bName = DEFAULT_BUILDINGS[i][0];
      BuildingRectangle r =
          new BuildingRectangle(bName, colorArray[i], _frame.getImagePanelSize(),
              buildingLayouts[i]);
      _buildingList.put(bName, r);
    }
  }


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
    _advReg = (AdventureRegistry) RegistryFactory.getInstance().getRegistry(RegKey.ADV);
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

  /**
   * Enter the Building specified. If the Hero is at the Town level, get the
   * {@code BuildingRegistry} and {@ocde BuildingCiv}
   * 
   * @param bldName the name of the building to open
   */
  public void enterBuilding(String bldName)
  {
    if (_onTown) {
      _bReg = (BuildingRegistry) RegistryFactory.getInstance().getRegistry(RegKey.BLDG);
      _bdCiv = new BuildingDisplayCiv(_frame, _bReg);
      _onTown = false;
    }
    // Always enter the building on request
    _bdCiv.enterBuilding(bldName);
  }


  public void handleClick(Point p)
  {
    handleClickIfOnTownReturn(p);
    if (_onTown) {
      //handleClickIfOnBuilding(p);
    }
  }


  /**
   * Display an error message in a different font color than normal messages
   * 
   * @param msg the error message to display
   */
  public void errorOut(String msg)
  {
    _frame.displayErrorText(msg);
  }


  /**
   * Display a prompt message asking for confirmation
   * 
   * @param mesg question to ask for confirmation
   * @return true if the user seleted YES
   */
  public boolean msgPrompt(String msg)
  {
    return _frame.displayPrompt(msg);
  }


  /**
   * Close down the application
   * 
   * @param msg text to display
   */
  public void msgOut(String msg)
  {
    _frame.displayText(msg);
  }


  /** Close down the application */
  public void quit()
  {
    if (msgPrompt("Quit Adventurer?") == true) {
      Adventurer.approvedQuit();
    }
  }


  public void handleMouseMovement(Point p)
  {
    if (_onTown) {
      for (BuildingRectangle rect : _buildingList.values()) {
        if (rect.contains(p)) {
            _frame.setBuilding(rect);
            break;
        } 
      }
    }
  }

  /**
   * Is a building displayed, or is the Hero at the Town view?
   * 
   * @return true if there is no current building displayed
   */
  public boolean isOnTown()
  {
    return _onTown;
  }


  /**
   * Load the selected adventure from the Adventure registry. Replace the opening button panel with
   * the IOPanel (text and command line)
   * 
   * @param adventureName selected from the Adventure by the user
   */
  public void loadSelectedAdventure(String adventureName)
  {
    _adv = _advReg.getAdventure(adventureName);
    _frame.addIOPanel();
    openTown();
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


  /**
   * Find a list of heroes that can be summoned
   * 
   * @return list of the heroes
   */
  public List<String> openDormitory()
  {
    List<String> heroes = _personRW.wakePeople();
    return heroes;
  }


  /** Creates the standard layout to display the town image and description */
  public void openTown()
  {
    _onTown = true;
    Image townImage = Util.convertToImage(TOWN_IMAGE);
    _frame.setImage(townImage);
    String townTitle = " The Town of " + _adv.getTownName();
    _frame.setImageTitle(townTitle);
    _frame.displayText(_adv.getOverview());
  }


  // ============================================================
  // Private methods
  // ============================================================

//  private void handleClickIfOnBuilding(Point p)
//  {
//    for (Entry<String, BuildingRectangle> entry : _buildingList.entrySet()) {
//      BuildingRectangle rect = entry.getValue();
//      if (rect.contains(p)) {
//        enterBuilding(entry.getKey());
//        return;
//      }
//    }
//  }


  private void handleClickIfOnTownReturn(Point p)
  {
    if (_townReturn.contains(p)) {
      openTown();
    }
    _frame.redraw();
  }

} // end of MainframeCiv class
