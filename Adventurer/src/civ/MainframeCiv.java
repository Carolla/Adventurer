/**
 * MainframeCiv.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import chronos.civ.UserMsg;
import chronos.pdc.command.Scheduler;
import chronos.pdc.registry.RegistryFactory;
import hic.ChronosPanel;
import hic.IOPanel;
import hic.ImagePanel;
import hic.Mainframe;
import hic.MainframeInterface;

/**
 * The main civ behind the Adventurer program. 
 * It initializes the system and brings up the Mainframe, the program's starting screen. 
 * 
 * @author Alan Cline
 * @author Tim Armstrong
 * @version Nov 2, 2013 // moved from CIV component <br>
 *          Mar 19 2014 // added current Building for ENTER command <br>
 *          Aug 18 2014 // added {@code displayImage} to show Chronos logo on portal page <br>
 *          Nov 7, 2015 // re-architected HIC.Mainframe to separate better CIV.MainframeCiv <br>
 *          Nov 13, 2015 // allow BuildingDisplayCiv to talk to this object. <br>
 */
public class MainframeCiv extends BaseCiv implements UserMsg
{
  private MainframeInterface _mf;
  private RegistryFactory _rf;
  private IOPanel _ioPanel;
  private ImagePanel _imagePanel;

  private final String INITIAL_IMAGE = "ChronosLogo.jpg";
  private final String INITIAL_IMAGE_TITLE = "Chronos Logo";


  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  public MainframeCiv()
  {
    initializeAll();
  }

  public MainframeCiv(MainframeInterface mf)
  {
    _mf = mf;
    initializeAll();
    displayMainframe();
  }

  protected void displayMainframe()
  {
	_mf.setImagePanel(new ImagePanel());
    displayImage(INITIAL_IMAGE_TITLE, INITIAL_IMAGE);
    _mf = new Mainframe();
    _mf.replaceRightPanel(_imagePanel);
    new MainActionCiv(this, _rf);
  }

  private void initializeAll()
  {
    // Create the background thread for command line processing
    Scheduler _skedder = new Scheduler(null); 
    _rf = new RegistryFactory();
    _rf.initRegistries();
  }


  // ============================================================
  // Public methods
  // ============================================================

  public void back()
  {
    _mf.back();
  }


  public void backToMain()
  {
    _mf.backToMain();
  }


  @Override
  public void displayErrorText(String msg)
  {
    _ioPanel.displayErrorText(msg);
  }


  public void displayImage(String title, String imageName)
  {
	_mf.displayImage(title, imageName);	  
  }


  @Override
  public void displayText(String result)
  {
    _ioPanel.displayText(result);
  }


  /** Close down the application if user so specified */
  public void quit()
  {
    System.exit(0);
  }


  public void replaceLeftPanel(ChronosPanel panel)
  {
    _mf.replaceLeftPanel(panel);
  }


  public void replaceLeftPanel(IOPanel panel)
  {
    _ioPanel = panel;
    _mf.replaceLeftPanel(panel);
  }


} // end of MainframeCiv class
