/**
 * MainframeCiv.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import hic.Mainframe;
import hic.MainframeInterface;

import java.util.ArrayList;

import pdc.Util;
import chronos.civ.UserMsg;
import chronos.pdc.Adventure;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

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

  private AdventureRegistry _advReg;
  private BuildingDisplayCiv _bldgCiv;

  private RegistryFactory _rf;

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
   */
  public MainframeCiv()
  {
    // Build the associated registries needed
    constructMembers();
  }


  /**
   * Perform construction act. This wires together all the "single instance variables" for the
   * Adventurer application. None of these constructors should ever be called anywhere outside of
   * this method and in testing.
   */
  protected void constructMembers()
  {
    _mf = new Mainframe(this);
    
    // Set external elements
    _mf.setImage(Util.convertToImage(INITIAL_IMAGE));
    _mf.setImageTitle(INITIAL_TITLE);
    
    // Create the mainActionCiv to manage the action buttons and town view
    _mainActionCiv = new MainActionCiv(_mf, this);
    _rf = _mainActionCiv.getRegistryFactory();
    
    BuildingRegistry breg = (BuildingRegistry) _rf.getRegistry(RegKey.BLDG);
    _bldgCiv = new BuildingDisplayCiv(_mf, breg);

    _advReg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
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

  // ============================================================
  // Public methods
  // ============================================================

  
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

  // TODO: Move this to MainACtionCiv
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


  /** Close down the application if user so specified */
  public void quit()
  {
    if (_mf.displayPrompt("Quit Adventurer?") == true) {
      Adventurer.approvedQuit();
    }
  }

} // end of MainframeCiv class
