/**
 * MockMainframe.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import hic.BuildingRectangle;
import hic.MainframeInterface;

import java.awt.Dimension;
import java.awt.Image;

import mylib.MsgCtrl;



/**
 * Facade for GUI Mainframe object to send and receive messages for testing.
 * 
 * @author Al Cline
 * @version Feb 7, 2015 // original <br>
 */
public class MainframeProxy implements MainframeInterface
{
  /** Buffer for holding building name */
  private String _bldgName;
//  /** Buffer for holding image path */
//  static private String _imagePath;
  /** Buffer for holding messages for auditing */
  private String _msg;
  private String _errMsg;

  /** Default constructor */
  public MainframeProxy()
  {
	 MsgCtrl.auditMsgsOn(true);
	 MsgCtrl.errorMsgsOn(true);
  }
  
  /* (non-Javadoc)
   * @see hic.IOPanelInterface#setImage(java.lang.String)
   */
  @Override
  public void setImage(Image image)
  {
    MsgCtrl.where(this);
//    _imagePath = image.
//    MsgCtrl.msgln("\timage path = " + _imagePath);
  }

  /* (non-Javadoc)
   * @see hic.IOPanelInterface#setImageTitle(java.lang.String)
   */
  @Override
  public void setImageTitle(String bldgName)
  {
    MsgCtrl.where(this);
    _bldgName = bldgName;
    MsgCtrl.msgln("\tbuilding name = " + bldgName + "\n");
  }

  /** Returns the name and imagepath in array */
  public String getBldgName()
  {
    return _bldgName;
  }


  /** Replace the button panel with the final IOPanel */
  public void addIOPanel()
  {
    System.out.println("\tMainframeProxy.addIOPanel(): ");
  }

  /**
   * Display error text into the output panel
   * 
   * @param imagePath text description of image location
   */
  public void displayErrorText(String errText)
  {
	  MsgCtrl.where(this);
	  _errMsg = errText;
      MsgCtrl.msgln("\t" + errText);
  }

  /**
   * Display the text into the output panel
   * 
   * @param text to be displayed
   */
  public void displayText(String text)
  {
	  MsgCtrl.where(this);
	  _msg = text;
      MsgCtrl.msgln("\t" + text);
  }

  /**
   * Display a prompt for user to asnwer YES or NO
   * 
   * @param text to be displayed
   */
  public boolean displayPrompt(String text)
  {
    System.out.println("\tMainframeProxy.displayPrompt(): " + text);
    return true;
  }

  /**
   * Display a prompt message asking for confirmation
   * 
   * @param mesg question to ask for confirmation
   * @return true if the user selected YES
   */
  public boolean msgPrompt(String msg)
  {
    System.out.println("\tMainframeProxy.msgPrompt(): " + msg);
    return true;
  }

  /** Return the size of the image space; set here temporarily */
  public Dimension getImagePanelSize()
  {
    return new Dimension(800, 600);
  }

  /**
   * Define a bounding rectangle to highlight a building
   * 
   * @param rect rectangle around the building
   */
  public void setBuilding(BuildingRectangle rect)
  {
    System.out.println("\tMainframeProxy.setBuilding(): " + rect);
  }


  /*
   * Misplaced redraw commnad in BuildingDisplayCiv
   */
  @Override
  public void redraw()
  {
    System.out.println("MainframeProxy.redraw(): called.");
  }

  @Override
  public void setOnTown(boolean onTown)
  {
    MsgCtrl.msgln("\tMainframeProxy.ontown set to " + onTown);
  }

  /**
   * Return last message out and clear buffer
   * 
   * @return whatever message was last intended for the GUI
   */
  public String msgOut()
  {
    MsgCtrl.where(this);
    return _msg;
  }
  
  public String errMsgOut()
  {
	 MsgCtrl.where(this);
	 return _errMsg;
  }


} // end of MainframeProxy class

