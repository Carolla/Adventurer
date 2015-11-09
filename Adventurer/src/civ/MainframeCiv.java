/**
 * MainframeCiv.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;

import chronos.civ.UserMsg;
import chronos.pdc.Adventure;
import chronos.pdc.buildings.Inn;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
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
 */
public class MainframeCiv implements UserMsg
{
  private MainframeInterface _mf;

  private Adventure _adv;
  private AdventureRegistry _advReg;
  private BuildingDisplayCiv _bldgCiv;

  private RegistryFactory _rf;

  /** Initial right-side image: Chronos logo */
  private static final String INITIAL_IMAGE = "ChronosLogo.jpg";
  /** Title of initial image */
  private static final String INITIAL_TITLE = "Chronos Logo";


//  /** Default Buildings to initialize registry with */
//  public static final String[][] DEFAULT_BUILDINGS = {{"Ugly Ogre Inn", "Bork"},
//      {"Rat's Pack", "Dewey N. Howe"}, {"The Bank", "Ogden Moneypenny"},
//      {"Stadium", "Aragon"}, {"Arcaneum", "Pendergast"}, {"Monastery", "Balthazar"},
//      {"Rogues' Den", "Ripper"}, {"Jail", "The Sheriff"}, {"Quasqueton", "Unknown"}};
//
//  private Map<String, BuildingRectangle> _buildingList = new TreeMap<String, BuildingRectangle>();
//
//  private float[][] buildingLayouts = new float[][] {
//      {0.48f, 0.54f, 0.14f, 0.08f}, // Ugly Ogre Inn
//      {0.79f, 0.43f, 0.14f, 0.08f}, // Rat's Pack General Store
//      {0.60f, 0.45f, 0.07f, 0.07f}, // The Bank
//      {0.5f, 0.37f, 0.25f, 0.09f}, // Stadium
//      {0.61f, 0.73f, 0.37f, 0.20f}, // Arcaneum
//      {0.0f, 0.35f, 0.22f, 0.13f}, // Monastery
//      {0.63f, 0.53f, 0.10f, 0.05f}, // Rouge's Den
//      {0.38f, 0.53f, 0.08f, 0.07f}, // Jail
//      {0.31f, 0.08f, 0.5f, 0.25f}}; // Quasqueton
//
//  private Color[] colorArray = new Color[] {Color.white, // Ugly Ogre Inn
//      Color.white, // Rat's Pack General Store
//      Color.white, // The Bank
//      Color.white, // Stadium
//      Color.white, // Arcaneum
//      Color.white, // Monastery
//      Color.white, // Rouge's Den
//      Color.white, // Jail
//      Color.white}; // Quasqueston
//
//  /** Current Building being displayed, and can be entered */
//  private final Rectangle _townReturn = new Rectangle(0, 0, 100, 100);


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
  public MainframeCiv(MainframeInterface mf)
  {
    _mf = mf;
//    _rf = new RegistryFactory(_skedder);
    
    _rf = new RegistryFactory();
    _rf.initRegistries();

    // Build the associated control objects
    constructMembers();
  }


  /**
   * Perform construction act. This wires together all the "single instance variables" for the
   * Adventurer application. None of these constructors should ever be called anywhere outside of
   * this method and in testing.
   */
  private void constructMembers()
  {
    BuildingRegistry breg = (BuildingRegistry) _rf.getRegistry(RegKey.BLDG);
    _bldgCiv = new BuildingDisplayCiv(_mf, breg);

    _advReg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);

    // TODO: Move this to outside this class, perhaps the BuildingDisplayCiv
    Inn inn = (Inn) breg.getBuilding("Ugly Ogre Inn");
    inn.setMsg(this);
    inn.initPatrons();
  }

  
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


  //  /** Set up the GUI elements needed at initialization or throughout the program */
//  public void configure()
//  {
//    _mf.setImage(Util.convertToImage(INITIAL_IMAGE));
//    _mf.setImageTitle(INITIAL_TITLE);
//    _mf.setRunicFont(Util.makeRunicFont(14f));
//    _mf.setStandardFont(new Font("Tahoma", Font.PLAIN, 24));
//  }

  // ============================================================
  // Public methods
  // ============================================================
  
  @Override
  public void errorOut(String msg)
  {
    _mf.displayErrorText(msg);
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


  public Image getInitialImage()
  {
    return Util.convertToImage(INITIAL_IMAGE);
  }

  public String getInitialTitle()
  {
    return INITIAL_TITLE;
  }
    
  // ============================================================
  // Public methods
  // ============================================================
  
  /**
   * Get the town name
   * 
   * @return the name of the town
   */
  public RegistryFactory getRegistryFactory()
  {
    return _rf;
  }


  // ============================================================
  // Public methods
  // ============================================================

  /**
   * Get the town name
   * 
   * @return the name of the town
   */
  public String getTownName()
  {
    return _adv.getTownName();
  }

//  // TODO Move to GUI object
//  public void handleClick(Point p)
//  {
//    handleClickIfOnTownReturn(p);
//    if (_bldgCiv.isOnTown()) {
//      handleClickIfOnBuilding(p);
//    }
//  }
//
//  // TODO: Mouse action belong in the HIC; not in the CIV
//  public void handleMouseMovement(Point p)
//  {
//    if (_bldgCiv.isOnTown()) {
//      for (BuildingRectangle rect : _buildingList.values()) {
//        if (rect.contains(p)) {
//          _mf.setBuilding(rect);
//          break;
//        }
//      }
//    }
//  }

  
  @Override
  public void msgOut(String msg)
  {
    _mf.displayText(msg);
  }


  /**
   * Display a prompt message asking for confirmation
   * 
   * @param msg question to ask for confirmation
   * @return true if the user seleted YES
   */
  public boolean msgPrompt(String msg)
  {
    return _mf.displayPrompt(msg);
  }

  // TODO: Move this to FormitoryCiv
//  /**
//   * Find a list of heroes that can be summoned
//   * 
//   * @return list of the heroes
//   */
//  public List<String> openDormitory()
//  {
//    List<String> heroes = _personRW.wakePeople();
//    return heroes;
//  }


//  /** Creates the standard layout to display the town image and description */
//  public void openTown()
//  {
//    _bldgCiv.returnToTown();
//    Image townImage = Util.convertToImage(TOWN_IMAGE);
//    _mf.setImage(townImage);
//    if (_adv != null) {
//      String townTitle = " The Town of " + _adv.getTownName();
//      _mf.setImageTitle(townTitle);
//      _mf.displayText(_adv.getOverview());
//    }
//  }


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

//  /** Create the clickable areas on the town view to indicate a selected Building */
//  private void createBuildingBoxes()
//  {
//    for (int i = 0; i < DEFAULT_BUILDINGS.length; i++) {
//      String bName = DEFAULT_BUILDINGS[i][0];
//      BuildingRectangle r =
//          new BuildingRectangle(bName, colorArray[i], _mf.getImagePanelSize(),
//              buildingLayouts[i]);
//      _buildingList.put(bName, r);
//    }
//  }
//
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
//
//
//  private void handleClickIfOnTownReturn(Point p)
//  {
//    if (_townReturn.contains(p)) {
//      openTown();
//    }
//    _mf.redraw();
//  }

} // end of MainframeCiv class
