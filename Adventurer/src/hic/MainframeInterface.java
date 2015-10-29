/**
 * MainframeInterface.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package hic;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JComponent;


/**
 * Used as socket for production and testing code to allow civ classes to support both.
 * 
 * @author Al Cline
 * @version Feb 21, 2015 // original <br>
 */
public interface MainframeInterface
{
  /** Replace the button panel with the final IOPanel */
  public void addIOPanel();

  public void addPanel(JComponent component);

//  /** Exit the system */
//  public boolean approvedQuit();

  /** Display error text into the output panel */
  public void displayErrorText(String errText);

  /** Display a prompt message asking for confirmation */
  public boolean displayPrompt(String msg);

  /** Display the text into the output panel */
  public void displayText(String s);

  /** Get the size of the panel that holds the image */
  public Dimension getImagePanelSize();

  /** Redraw the mainframe to re-init the graphics outputs */
  public void redraw();

  /** Redraws the view */
  public void repaint();

  /** Display the image into the image panel */
  public void setImage(Image image);

  /** Display the title of the building aboce the image panel */
  public void setImageTitle(String title);

  /**
   * Define a bounding rectangle to highlight a building
   * 
   * @param rect rectangle around the building
   */
  public void setBuilding(BuildingRectangle rect);

  /**
   * @param object
   */
  public void setTitle(String title);


} // end of MainframeInterface
