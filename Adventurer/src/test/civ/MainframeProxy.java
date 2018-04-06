/**
 * MainframeProxy.java Copyright (c) 2018, Alan Cline. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */


package test.civ;

import civ.IMainframe;
import hic.ChronosPanel;
import hic.ImagePanel;
import mylib.MsgCtrl;

/**
 * Test the various methods in AdvMainframeCiv
 * 
 * @author Alan Cline
 * @version Dec 21, 2013 // original <br>
 *          Apr 6, 2018 // major rewrite using MVP Proxies <br>
 */
public class MainframeProxy implements IMainframe
{

  // @Test
  // public void allMethodsArePassthroughToGuiClasses()
  // {}

  /**
   * List of methods that do not need JUnit test because they are too trivial, or some other
   * test method tests them equally well. <br>
   * <code> back() </code> -- calls GUI <code> backToMain() </code> -- calls GUI
   * <code> displayErrorText() </code> -- calls GUI <code> displayImage() </code> -- calls GUI
   * <code> displayText() </code> -- calls GUI <code> replaceLeftPanel(ChronosPanel) </code> --
   * calls GUI <code> replaceLeftPanel(IOPanel) </code> -- calls GUI <code> quit() </code> --
   * quits
   */
  public void notNeeded()
  {}

  
  @Override
  public void back()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.where(this);
  }


  @Override
  public void backToMain(String newFrameTitle)
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.where(this);
  }

  @Override
  public boolean displayPrompt(String msg)
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.where(this);
    MsgCtrl.msgln(msg);
    return false;
  }

  @Override
  public void replaceLeftPanel(ChronosPanel leftPanel)
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.where(this);
  }

  @Override
  public void replaceRightPanel(ChronosPanel rightPanel)
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.where(this);
  }

  @Override
  public void setTitle(String title)
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.where(this);
  }

  @Override
  public void setLeftTitle(String title)
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.where(this);
  }

  @Override
  public void showHelp()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.where(this);
  }

  @Override
  public void setImagePanel(ImagePanel imagePanel)
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.where(this);
  }

  @Override
  public void displayImage(String title, String imageName)
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.where(this);
  }


} // end of TestMainFrameCiv class
