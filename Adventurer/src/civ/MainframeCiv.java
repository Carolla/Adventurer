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
import hic.ImagePanel;
import hic.MainframeInterface;
import chronos.civ.UserMsgInterface;
import chronos.civ.UserMsgProxy;

/**
 * The main civ behind the Adventurer program. It initializes the system and brings up the
 * Mainframe, the program's starting screen.
 * 
 * @author Alan Cline
 * @author Tim Armstrong
 * @version Nov 2, 2013 // moved from CIV component <br>
 *          Mar 19 2014 // added current Building for ENTER command <br>
 *          Aug 18 2014 // added {@code displayImage} to show Chronos logo on portal page <br>
 *          Nov 7, 2015 // re-architected HIC.Mainframe to separate better CIV.MainframeCiv <br>
 *          Nov 13, 2015 // allow BuildingDisplayCiv to talk to this object. <br>
 */
// public class MainframeCiv extends BaseCiv implements UserMsgInterface
public class MainframeCiv extends BaseCiv
{
  private MainframeInterface _mf;
  private ImagePanel _imagePanel;

  private static final String INITIAL_IMAGE = "ChronosLogo.jpg";
  private static final String INITIAL_IMAGE_TITLE = "Chronos Logo";

  /** receives user input and command output messages */
  private UserMsgInterface _output = new UserMsgProxy();

  public MainframeCiv(MainframeInterface mf)
  {
    _mf = mf;
    doConstructorWork();
  }

  protected void doConstructorWork()
  {
    _imagePanel = new ImagePanel();
    _mf.setImagePanel(_imagePanel);
    displayImage(INITIAL_IMAGE_TITLE, INITIAL_IMAGE);
    _mf.replaceRightPanel(_imagePanel);
    new MainActionCiv(this);
  }

  


  // ============================================================
  // Public methods
  // ============================================================

  public void back()
  {
    _mf.back();
  }


  public void backToMain(String newFrameTitle)
  {
    _mf.backToMain(newFrameTitle);
  }
  
  public String displayText(String msg)
  {
    _output.displayText(msg);
    return msg;
  }
  
  public String displayErrorText(String msg)
  {
    _output.displayErrorText(msg);
    return msg;
  }
  
  
  public void displayImage(String title, String imageName)
  {
    _mf.displayImage(title, imageName);
  }

  /** Returns the current output device, the IOPanel or a test proxy */
  public UserMsgInterface getOutput()
  {
    return _output;
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
    _mf.replaceLeftPanel(panel);
  }

  /**
   * Allows left-side panel title to be set externally
   * 
   * @param title to set the title for
   */
  public void setLeftPanelTitle(String title)
  {
    _mf.setLeftTitle(title);
  }

  public void setOutput(UserMsgInterface output)
  {
    _output = output;
  }


} // end of MainframeCiv class
