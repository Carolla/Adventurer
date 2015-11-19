/**
 * MainframeCiv.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import hic.ChronosPanel;
import hic.IOPanel;
import hic.Mainframe;
import hic.MainframeInterface;

import java.util.ArrayList;

import pdc.command.CommandFactory;
import chronos.pdc.Adventure;
import chronos.pdc.registry.AdventureRegistry;
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
 *          Nov 13, 2015 // allow BuildingDisplayCiv to talk to this object. <br>
 */
public class MainframeCiv extends BaseCiv
{
  private MainframeInterface _mf;
  private RegistryFactory _rf;
  private MainActionCiv mainActionCiv;
  private CommandParser _cp;
  private IOPanel _ioPanel;

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

    // Create the right side image panel to be used by many Civs */
    ChronosPanel imagePanel = new ImagePanel(INITIAL_IMAGE, INITIAL_TITLE);
    imagePanel.replaceControllerCiv(this);
    _mf.replaceRightPanel(imagePanel);
    
    // Create the left side panel to hold the main action buttons */
    mainActionCiv = new MainActionCiv(_mf, this);    
    _rf = mainActionCiv.getRegistryFactory();
  }

  // ============================================================
  // Public methods
  // ============================================================

  protected void tellFrameToDisplayBuilding(String description, String imagePath, String bldgName)
  {
    _frame.setBuilding(null); // Confusing, I know. This sets the building RECTANGLE
    _frame.setImageByName(imagePath);
    _frame.setImageTitle(bldgName);
    _frame.displayText(description);
  }
  


  /**
   * Create the IOPanel and link it with the command parser for handling user input and output
   * 
   * @return
   */
  private IOPanel initIOPanel()
  {
    // Create the IOPanel for input commands via commandParser and output messages
    _cp = new CommandParser(new CommandFactory(this));
    _ioPanel = new IOPanel(this, _cp);

    // Display the IOPanel on the left side of the mainframe
    _mf.replaceLeftPanel(_ioPanel);

    return _ioPanel;
  }
  
  /**
   * Retrieves the Adventures for selection from the Adventure Registry
   * 
   * @return the list of Adventures
   */
  public ArrayList<String> getAdventures()
  {
   AdventureRegistry aReg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
    
    ArrayList<Adventure> adventures = aReg.getAdventureList();
    ArrayList<String> results = new ArrayList<String>();
    for (Adventure a : adventures) {
      results.add(a.getKey());
    }
    return results;
  }


  /** Close down the application if user so specified */
  public void quit()
  {
    if (_mf.displayPrompt("Quit Adventurer?") == true) {
      Adventurer.approvedQuit();
    }
  }


  public void displayText(String result)
  {
    _ioPanel.displayText(result);    
  }
} // end of MainframeCiv class
