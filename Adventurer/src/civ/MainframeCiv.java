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
import hic.ChronosPanel;
import hic.IOPanel;
import hic.ImagePanel;
import hic.Mainframe;
import hic.MainframeInterface;

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
public class MainframeCiv extends BaseCiv implements UserMsg
{
  private MainframeInterface _mf;
  private IOPanel _ioPanel;
  private ImagePanel _imagePanel;

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
    
    _imagePanel = new ImagePanel();
    _mf.replaceRightPanel(_imagePanel);
    
    new MainActionCiv(this);
  }

  // ============================================================
  // Public methods
  // ============================================================

  /** Close down the application if user so specified */
  public void quit()
  {
    if (_mf.displayPrompt("Quit Adventurer?") == true) {
      Adventurer.approvedQuit();
    }
  }

  @Override
  public void displayText(String result)
  {
    _ioPanel.displayText(result);
  }

  @Override
  public void displayErrorText(String msg)
  {
    _ioPanel.displayErrorText(msg);
  }

  public void displayImage(String title, String imageName)
  {
    _imagePanel.setTitle(title);
    _imagePanel.setImageByName(imageName);
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


  public void back()
  {
    _mf.back();
  }
  
  public void backToMain()
  {
    _mf.backToMain();
  }
} // end of MainframeCiv class
