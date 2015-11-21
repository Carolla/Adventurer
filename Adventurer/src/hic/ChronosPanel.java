/**
 * ChronosPanel.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package hic;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import civ.BaseCiv;
import mylib.Constants;
import mylib.Constants.Side;

/**
 * Adds a title method to JPanel for Chronos panels. Instead of using various constructor parms, the
 * attributes are added as set() functions. <br>
 * For example, instead of
 * <P>
 * {@code JPanel myPanel = new JPanel(LayoutManager lm);}
 * <P>
 * use
 * <P>
 * {@code ChronosPanel myPanel = new ChronosPanel();} <br>
 * {@code myPanel.set(LayoutManagerlm);}.
 * <P>
 * 
 * @see {@code Mainframe.setFrameAndMenubar() for a better example.
 * 
 * @author Al Cline
 * @version Nov 9, 2015 // original <br>
 */
@SuppressWarnings("serial")
public class ChronosPanel extends JPanel
{
  /** Default title field for all ChronosPanels, to be overwritten */
  protected String _title;

  /** Image shown in panel */
  protected Image _image;

  /** Civ that manages the images and mouse action in this display */
  protected BaseCiv _ctrlCiv;

  // ====================================================================================
  // Constructor
  // ====================================================================================

  /** Default constructor sets the controlling civ and whether this panel is left or right side */
  public ChronosPanel(BaseCiv ctrlCiv, String title, Side side)
  {
    _ctrlCiv = ctrlCiv;
    replaceControllerCiv(_ctrlCiv);
    _ctrlCiv.setPanel(this, side);
    setTitle(title);
    System.out.println("Creating ChronosPanal " + _title + "; controlled by " + ctrlCiv);
  }


  // ====================================================================================
  // Default methods in common for a base class of ChronosPanels
  // ====================================================================================

  /** Get the panel's title for display */
  public String getTitle()
  {
    return _title;
  }

  // ====================================================================================
  // Default methods in common for a base class of ChronosPanels
  // ====================================================================================

  /**
   * Required override method to draw on {@code JComponent.ImagePanel}
   */
  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    // System.out.println("ImagePanel.paintComponent");
    // Find top-left corner to image panel to overlay image onto
    int pWidth = getWidth();
    int pHeight = getHeight();

    // Draw the image at the top-left corner of the ImagePanel when JComponent gets its turn
    g.drawImage(_image, 0, 0, pWidth, pHeight, this);

    // if (_buildingRectangle != null) {
    // // System.out.println("Drawing " + _buildingRectangle._name);
    // _buildingRectangle.drawBuildingBox((Graphics2D) g);
    // }
  }


  // ====================================================================================
  // Default methods in common for a base class of ChronosPanels
  // ====================================================================================

  public void replaceControllerCiv(BaseCiv newCiv)
  {
    // No action if listeners are null
    removeMouseListener(_ctrlCiv);
    removeMouseMotionListener(_ctrlCiv);

    // Add new control civ and save for later
    addMouseListener(newCiv);
    addMouseMotionListener(newCiv);
    _ctrlCiv = newCiv;
  }


  /**
   * Force ChronosPanel implementers to set a title, either in the Panel itself of in the Panel's
   * border.
   * 
   * @param title of the panel
   */
  public void setTitle(String title)
  {
    if (!title.startsWith(Constants.SPACE)) {
      title = Constants.SPACE + title;
    }
    if (!title.endsWith(Constants.SPACE)) {
      title = title + Constants.SPACE;
    }
    _title = title;
  }


  /**
   * Display the image in the calling panel using JPanel's method
   * 
   * @param image to be displayed
   */
  public void setImage(Image image)
  {
    _image = image;
  }



} // end of ChronosPanel
