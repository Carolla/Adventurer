/**
 * MainframeCiv.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
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
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import pdc.registry.AdvRegistryFactory;
import chronos.pdc.Adventure;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory.RegKey;
import dmc.PersonReadWriter;

/**
 * The main civ behind the Mainframe screen. It also contains all the initialization state.
 * 
 * @author Alan Cline
 * @author Tim Armstrong
 * @version <DL>
 *          <DT>Build 1.0 Nov 2, 2013 // moved from CIV component
 *          <DD>
 *          <DT>Build 1.1 Mar 19 2014 // added current Building for ENTER command
 *          <DD>
 *          </DL>
 */
public class MainframeCiv
{
  private AdventureRegistry _advReg = null;
  private BuildingDisplayCiv _bdCiv = null;
  private CommandParser _cp = null;

  private static final String TOWN_IMAGE = "ext_Quasqueton.JPG";

  /** Default Buildings to initialize registry with */
  public static final String[][] DEFAULT_BUILDINGS = { {"Ugly Ogre Inn", "Bork"},
      {"Rat's Pack General Store", "Dewey N. Howe"}, {"The Bank", "Ogden Moneypenny"}, {"Stadium", "Aragon"},
      {"Arcaneum", "Pendergast"}, {"Monastery", "Balthazar"}, {"Rogues' Den", "Ripper"}, {"Jail", "The Sheriff"}};

  private Map<String, BuildingRectangle> buildingList = new TreeMap<String, BuildingRectangle>();

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

  /**
   * Create the Civ associated with the mainframe
   * 
   * @param frame owner of the widget for which this civ applies
   * @param personRW  -- not sure why this is here??
   * @param advReg  registry that holds the adventures for selection
   */
  public MainframeCiv(Mainframe frame, PersonReadWriter personRW, AdventureRegistry advReg)
  {
    _frame = frame;
    _personRW = personRW;
    _advReg = advReg;
    _bdCiv = new BuildingDisplayCiv(_frame, (BuildingRegistry) AdvRegistryFactory.getRegistry(RegKey.BLDG));
    // initRegistries();
    createBuildingBoxes();
    _cp = new CommandParser(this);
  }

  private void createBuildingBoxes()
  {
    for (int i = 0; i < DEFAULT_BUILDINGS.length; i++) {
      String bName = DEFAULT_BUILDINGS[i][0];
      BuildingRectangle r = new BuildingRectangle(bName, colorArray[i], _frame.getImagePanelSize(), buildingLayouts[i]);
      buildingList.put(bName, r);
    }
  }

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

  public List<String> getAdventures()
  {
    List<String> results = new ArrayList<String>();
    List<Adventure> adventures = _advReg.getAdventureList();
    for (Adventure a : adventures) {
      results.add(a.getKey());
    }
    return results;
  }

  public void handleClick(Point p)
  {
    handleClickIfOnTownReturn(p);
    if (_onTown) {
      handleClickIfOnBuilding(p);
    }
  }

  private void handleClickIfOnBuilding(Point p)
  {
    for (Entry<String, BuildingRectangle> entry : buildingList.entrySet()) {
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

  public void handleMouseMovement(Point p)
  {
    if (_onTown) {
      for (BuildingRectangle rect : buildingList.values()) {
        if (rect.contains(p)) {
          _frame.drawBuilding(rect);
          break;
        }
      }
    }
    _frame.redraw();
  }

  public void loadAdventure(String adventureName)
  {
    _adv = _advReg.getAdventure(adventureName);
    openTown();
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

  public void openTown()
  {
    _onTown = true;
    _bdCiv.exitBuilding();
    _frame.displayTextAndImage(_adv.getOverview(), TOWN_IMAGE);
  }

  public CommandParser getCmdParser()
  {
    return _cp;
  }

  public void handleError(String string)
  {
    _frame.displayText(string);

  }

} // end of AdvMainframeCiv outer class
