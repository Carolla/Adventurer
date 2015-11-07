/**
 * Mainframe.Civ Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import chronos.civ.UserMsg;
import chronos.pdc.Adventure;
import chronos.pdc.Command.Scheduler;
import chronos.pdc.buildings.Inn;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import dmc.HeroReadWriter;
import hic.BuildingRectangle;
import hic.IOPanel;
import hic.MainframeInterface;
import pdc.Util;
import pdc.command.CommandFactory;

/**
 * The main civ behind the Mainframe screen.
 * 
 * @author Alan Cline
 * @author Tim Armstrong
 * @version Nov 2, 2013 // moved from CIV component <br>
 *          Mar 19 2014 // added current Building for ENTER command <br>
 *          Aug 18 2014 // added {@code displayImage} to show Chronos logo on portal page <br>
 */
public class MainframeCiv implements UserMsg
{
  private AdventureRegistry _advReg;
  private BuildingDisplayCiv _bdCiv;

  private MainframeInterface _mf;
  private Adventure _adv;
  private HeroReadWriter _personRW;

  private RegistryFactory _rf;
  private Scheduler _skedder;
  private CommandParser _cp;
  private BuildingDisplayCiv _bldgCiv;

  private static final String TOWN_IMAGE = "ext_BiljurBaz.JPG";
  /** Initial right-side image: Chronos logo */
  private static final String INITIAL_IMAGE = "ChronosLogo.jpg";
  /** Title of initial image */
  private static final String INITIAL_TITLE = "Chronos Logo";

  /** Default Buildings to initialize registry with */
  public static final String[][] DEFAULT_BUILDINGS = {{"Ugly Ogre Inn", "Bork"},
      {"Rat's Pack", "Dewey N. Howe"}, {"The Bank", "Ogden Moneypenny"},
      {"Stadium", "Aragon"}, {"Arcaneum", "Pendergast"}, {"Monastery", "Balthazar"},
      {"Rogues' Den", "Ripper"}, {"Jail", "The Sheriff"}, {"Quasqueton", "Unknown"}};

  private Map<String, BuildingRectangle> _buildingList = new TreeMap<String, BuildingRectangle>();

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

  /** Current Building being displayed, and can be entered */
  private final Rectangle _townReturn = new Rectangle(0, 0, 100, 100);


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

    _skedder = new Scheduler(); // Skedder first for injection
    _rf = new RegistryFactory(_skedder);
    _rf.initRegistries();

    // Build the associated control objects
    constructMembers();
  }


  // ============================================================
  // Constructors and constructor helpers
  // ============================================================
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

    _cp = new CommandParser(_skedder, new CommandFactory(this, _bldgCiv));
    IOPanel iop = new IOPanel(_cp);

    Inn inn = (Inn) breg.getBuilding("Ugly Ogre Inn");
    inn.setMsg(this);
    inn.initPatrons();
  }


  // ============================================================
  // Public methods
  // ============================================================


  /**
   * TODO This is the responsibility of the BuildingDisplayCiv Enter the Building specified. If the
   * Hero is at the Town level, get the {@code BuildingRegistry} and {@ocde BuildingCiv}
   *
   * @param bldName the name of the building to open
   */
  public void enterBuilding(String bldName)
  {
    if (_bdCiv.canApproach(bldName)) {
      _bdCiv.approachBuilding(bldName);
    }
  }


  // ============================================================
  // Constructors and constructor helpers
  // ============================================================


  // ============================================================
  // Public methods
  // ============================================================


  @Override
  public void errorOut(String msg)
  {
    _mf.displayErrorText(msg);
  }


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

  public BuildingDisplayCiv getBuildingDisplayCiv()
  {
    return _bdCiv;
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

  // TODO Move to GUI object
  public void handleClick(Point p)
  {
    handleClickIfOnTownReturn(p);
    if (_bdCiv.isOnTown()) {
      handleClickIfOnBuilding(p);
    }
  }

  // TODO: Mouse action belong in the HIC; not in the CIV
  public void handleMouseMovement(Point p)
  {
    if (_bdCiv.isOnTown()) {
      for (BuildingRectangle rect : _buildingList.values()) {
        if (rect.contains(p)) {
          _mf.setBuilding(rect);
          break;
        }
      }
    }
  }

  /** Set up the GUI elements needed at initialization or throughout the program */
  public void configure()
  {
    _mf.setImage(Util.convertToImage(INITIAL_IMAGE));
    _mf.setImageTitle(INITIAL_TITLE);
    _mf.setRunicFont(Util.makeRunicFont(14f));
    _mf.setStandardFont(new Font("Tahoma", Font.PLAIN, 24));

    
    createBuildingBoxes();
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
    _mf.addIOPanel();
    openTown();
  }

  /** Create a font that pervades the HIC
   * @param fontHt  size of runic font to create
   * @return the font 
   */
  public Font makeRunicFont(float fontHt)
  {
    return Util.makeRunicFont(14f);
  }

  
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


  // ============================================================
  // Public methods
  // ============================================================


  /** Creates the standard layout to display the town image and description */
  public void openTown()
  {
    _bdCiv.returnToTown();
    Image townImage = Util.convertToImage(TOWN_IMAGE);
    _mf.setImage(townImage);
    if (_adv != null) {
      String townTitle = " The Town of " + _adv.getTownName();
      _mf.setImageTitle(townTitle);
      _mf.displayText(_adv.getOverview());
    }
  }


  // ============================================================
  // Private methods
  // ============================================================

  /** Close down the application if user so specified */
  public void quit()
  {
    if (_mf.displayPrompt("Quit Adventurer?") == true) {
      Adventurer.approvedQuit();
    }
  }

  /** Create the clickable areas on the town view to indicate a selected Building */
  private void createBuildingBoxes()
  {
    for (int i = 0; i < DEFAULT_BUILDINGS.length; i++) {
      String bName = DEFAULT_BUILDINGS[i][0];
      BuildingRectangle r =
          new BuildingRectangle(bName, colorArray[i], _mf.getImagePanelSize(),
              buildingLayouts[i]);
      _buildingList.put(bName, r);
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
    _mf.redraw();
  }

} // end of MainframeCiv class
