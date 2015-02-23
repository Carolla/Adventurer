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
  {}

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
    System.err.println("\tMainframeProxy.displayErrorText(): " + errText);
  }

  /**
   * Display the text into the output panel
   * 
   * @param text to be displayed
   */
  public void displayText(String text)
  {
    System.out.println("\tMainframeProxy.displayText(): " + text);
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
   * @param rect rectangle around the building
   */
  public void setBuilding(BuildingRectangle rect)
  {
    System.out.println("\tMainframeProxy.setBuilding(): " + rect);
  }

  /**
   * Display the image into the image panel
   * 
   * @param image   acutal image file to display
   */
  public void setImage(Image image)
  {
    // NOT sure what to put here to audit an image display yet
     System.out.println("\tMainframeProxy.setImage(): ");
  }

  /**
   * Display the title of the building above the image panel
   * 
   * @param imagePath text description of image location
   */
  public void setImageTitle(String title)
  {
    System.out.println("MainframeProxy.setImageTitle(): " + title);
  }

  
} // end of MainframeProxy class

