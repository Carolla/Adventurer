/**
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
import java.util.Map.Entry;
import java.util.TreeMap;

import pdc.Util;
import chronos.pdc.Adventure;
import chronos.pdc.registry.AdventureRegistry;
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
  private AdventureRegistry _advReg = null;
  private BuildingDisplayCiv _bdCiv = null;
//  private CommandParser _cp = null;

  // private static final String TOWN_IMAGE = "ext_Quasqueton.JPG";
  private static final String TOWN_IMAGE = "ext_BiljurBaz.JPG";

  /** Default Buildings to initialize registry with */
  public static final String[][] DEFAULT_BUILDINGS = { {"Ugly Ogre Inn", "Bork"},
      {"Rat's Pack", "Dewey N. Howe"}, {"The Bank", "Ogden Moneypenny"},
      {"Stadium", "Aragon"}, {"Arcaneum", "Pendergast"}, {"Monastery", "Balthazar"},
      {"Rogues' Den", "Ripper"}, {"Jail", "The Sheriff"}};

  private Map<String, BuildingRectangle> _buildingList = new TreeMap<String, BuildingRectangle>();

  private float[][] buildingLayouts = new float[][] { {0.3f, 0.7f, 0.09f, 0.09f}, // Ugly Ogre Inn
      {0.4f, 0.7f, 0.09f, 0.09f}, // Rat's Pack General Store
      {0.5f, 0.7f, 0.09f, 0.09f}, // The Bank
      {0.6f, 0.7f, 0.09f, 0.09f}, // Stadium
      {0.35f, 0.8f, 0.09f, 0.09f}, // Arcaneum
      {0.45f, 0.8f, 0.09f, 0.09f}, // Monastery
      {0.55f, 0.8f, 0.09f, 0.09f}, // Rogue's Den
      {0.7f, 0.65f, 0.09f, 0.09f}, // Jail
      {0.8f, 0.1f, 0.2f, 0.15f}}; // Quasqueton

  private Color[] colorArray = new Color[] {Color.red, // Ugly Ogre Inn
      Color.red, // Rat's Pack General Store
      Color.red, // The Bank
      Color.red, // Stadium
      Color.red, // Arcaneum
      Color.red, // Monastery
      Color.red, // Rogue's Den
      Color.red, // Jail
      Color.red}; // Quasqueston

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
    _frame.setImageTitle(INITIAL_TITLE);
    // _personRW = new PersonReadWriter();
    // _advReg = (AdventureRegistry) RegistryFactory.getInstance().getRegistry(RegKey.ADV);
    // _bdCiv =
    // new BuildingDisplayCiv(_frame, (BuildingRegistry)
    // RegistryFactory.getInstance().getRegistry(RegKey.BLDG));
    // createBuildingBoxes();
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
  public List<String> getAdventures()
  {
    _advReg = (AdventureRegistry) RegistryFactory.getInstance().getRegistry(RegKey.ADV);
    List<Adventure> adventures = _advReg.getAdventureList();
    List<String> results = new ArrayList<String>();
    for (Adventure a : adventures) {
      results.add(a.getKey());
    }
    return results;
  }


  // ============================================================
  // Public methods
  // ============================================================

  /**
   * Try to open the building passed in
   * 
   * @param bldName the name of the building to open
   */
  public void enterBuilding(String bldName)
  {
    if (_onTown) {
      if (_bdCiv.approachBuilding(bldName)) {
        _onTown = false;
      }
    } else {
      _bdCiv.enterBuilding();
    }
  }


  public void handleClick(Point p)
  {
    handleClickIfOnTownReturn(p);
    if (_onTown) {
      handleClickIfOnBuilding(p);
    }
  }

  
  public void handleError(String string)
  {
    _frame.displayText(string);
  }


  /** Display a prompt message asking for confirmation 
   * 
   * @param mesg  question to ask for confirmation
   * @return true if the user seleted YES
   */
  public boolean msgPrompt(String msg)
  {
    return _frame.displayPrompt(msg);
  }

  
  /** Close down the application */
  public void quit()
  {
    Adventurer.quit();
  }
  
  
  public void handleMouseMovement(Point p)
  {
    if (_onTown) {
      for (BuildingRectangle rect : _buildingList.values()) {
        if (rect.contains(p)) {
          _frame.drawBuilding(rect);
          break;
        }
      }
    }
    _frame.redraw();
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
    _frame.redraw();
  }

} // end of AdvMainframeCiv outer class
