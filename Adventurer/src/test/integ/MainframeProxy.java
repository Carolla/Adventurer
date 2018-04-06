/**
 * MockMainframe.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use from Carolla
 * Development, Inc. by email: acline@carolla.com
 */

package test.integ;

import civ.IMainframe;
import hic.ChronosPanel;
import hic.ImagePanel;
import mylib.MsgCtrl;


/**
 * Facade for GUI Mainframe object to send and receive messages for testing.
 * 
 * @author Al Cline
 * @version Feb 7, 2015 // original <br>
 *          March 25, 2018 // revised for integation testing <br>
 */
public class MainframeProxy implements IMainframe
{

  /** Default constructor */
  public MainframeProxy()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
  }


  /**
   * Display a prompt for user to asnwer YES or NO
   * 
   * @param text to be displayed
   */
  @Override
  public boolean displayPrompt(String text)
  {
    MsgCtrl.where(this);
    return true;
  }

  @Override
  public void setTitle(String title)
  {
    MsgCtrl.where(this);
  }


  @Override
  public void showHelp()
  {
    MsgCtrl.where(this);
  }


  @Override
  public void back()
  {
    MsgCtrl.where(this);
  }


  @Override
  public void replaceLeftPanel(ChronosPanel leftPanel)
  {
    MsgCtrl.where(this);
  }

  @Override
  public void replaceRightPanel(ChronosPanel rightPanel)
  {
    MsgCtrl.where(this);
  }


  @Override
  public void backToMain(String string)
  {
    MsgCtrl.where(this);
  }


  @Override
  public void setImagePanel(ImagePanel imPanel)
  {
    MsgCtrl.where(this);
    MsgCtrl.msgln(String.format("Creating the right-side %s image panel", imPanel.getTitle()));
  }


  @Override
  public void displayImage(String title, String imageName)
  {
    MsgCtrl.where(this);
  }


  @Override
  public void setLeftTitle(String title)
  {
    MsgCtrl.where(this);
  }

  
} // end of MainframeProxy class

