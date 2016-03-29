/**
 * MockMainframe.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import hic.ChronosPanel;
import hic.ImagePanel;
import hic.MainframeInterface;



/**
 * Facade for GUI Mainframe object to send and receive messages for testing.
 * 
 * @author Al Cline
 * @version Feb 7, 2015 // original <br>
 */
public class MainframeProxy implements MainframeInterface
{
  /** Default constructor */
  public MainframeProxy()
  {
    // MsgCtrl.auditMsgsOn(false);
    // MsgCtrl.errorMsgsOn(false);
  }


  /**
   * Display a prompt for user to asnwer YES or NO
   * 
   * @param text to be displayed
   */
  @Override
  public boolean displayPrompt(String text)
  {
    // MsgCtrl.where(this);
    return true;
  }

  @Override
  public void setTitle(String title)
  {
    // MsgCtrl.where(this);
  }


  @Override
  public void showHelp()
  {
    // MsgCtrl.where(this);
  }


  @Override
  public void back()
  {
    // MsgCtrl.where(this);
  }


  @Override
  public void replaceLeftPanel(ChronosPanel leftPanel)
  {
    // MsgCtrl.where(this);
  }

  @Override
  public void replaceRightPanel(ChronosPanel rightPanel)
  {
    // MsgCtrl.where(this);
  }


  @Override
  public void backToMain()
  {
    // MsgCtrl.where(this);
  }


@Override
public void setImagePanel(ImagePanel imagePanel) { 
// MsgCtrl.where(this);
}


@Override
public void displayImage(String title, String imageName) {
    // MsgCtrl.where(this);
}

} // end of MainframeProxy class

