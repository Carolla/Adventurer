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

import civ.MainActionCiv;


/**
 * Used as socket for production and testing code to allow civ classes to support both.
 * 
 * @author Al Cline
 * @version Feb 21, 2015 // original <br>
 */
public interface MainframeInterface
{
  /** Replace the main action button panel with the final IOPanel */
  public void addIOPanel(MainActionCiv mac);

  public void addPanel(JComponent component);

  public void back();

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

  /** Redraw a Panel on the left side of the mainframe */
  public void replaceLeftPanel(ChronosPanel leftPanel);

  /** Redraws the view */
  public void repaint();

  /**
   * Define a bounding rectangle to highlight a building
   * 
   * @param rect rectangle around the building
   */
  public void setBuilding(BuildingRectangle rect);

  /** Display the image into the image panel */
  public void setImage(Image image);

  /** Display the title of the building aboce the image panel */
  public void setImageTitle(String title);

  /** Place a title on the left side of the mainframe */
  public void setLeftPanelTitle(String title);

//  /** Set the runic font to be used for user interactions */
//  public void setRunicFont(Font stdFont);
//
//  /** Set the standard font to be used buttons, help, etc. */
//  public void setStandardFont(Font stdFont);

  /** Put a title into the frame's border */
  public void setTitle(String title);

  public void showHelp();


} // end of MainframeInterface
