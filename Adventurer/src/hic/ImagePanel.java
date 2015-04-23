/**
 * ImagePanel.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package hic;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JComponent;

/**
 * The {@code ImagePanel} class extends {@code JComponent} so that it is easy to draw onto. It is a
 * singleton because it is created once and different images are displayed on it for the duration of
 * the program.
 * 
 * @author Al Cline
 * @version Aug 16, 2014 // original <br>
 */
@SuppressWarnings("serial")
public class ImagePanel extends JComponent
{
  /** Reference to singleton ImagePanel */
  static private ImagePanel _imagePanel;

  /** The actual image to be displayed */
  private Image _image;

  /** Box to be drawn over the current image */
  private BuildingRectangle _buildingRectangle;


  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /** Private singleton constructor */
  private ImagePanel()
  {}


  /**
   * Singleton constructor for placing images
   * 
   * @return the singleton ImagePanel so other images can be displayed on it
   */
  static public ImagePanel getInstance()
  {
    if (_imagePanel == null) {
      _imagePanel = new ImagePanel();
    }
    return _imagePanel;
  }


  // ============================================================
  // Public methods
  // ============================================================


  /**
   * Set the image into the panel and let {@code paintComponent} take over
   * 
   * @param image the picture to display, or {@code image} type
   * @param title the caption over the image being displayed
   */
  public void setImage(Image image)
  {
    _image = image;
  }

  public void setRectangle(BuildingRectangle rect)
  {
      _buildingRectangle = rect;
  }
  
  /**
   * Required override method to draw on {@code JComponent.ImagePanel}
   */
  @Override
  public void paintComponent(Graphics g)
  {
      super.paintComponent(g);
//      System.out.println("ImagePanel.paintComponent");
      // Find top-left corner to image panel to overlay image onto
      int pWidth = _imagePanel.getWidth();
      int pHeight = _imagePanel.getHeight();

      // Draw the image at the top-left corner of the ImagePanel when JComponent gets its turn
      g.drawImage(_image, 0, 0, pWidth, pHeight, _imagePanel);

      if (_buildingRectangle != null) {
//          System.out.println("Drawing " + _buildingRectangle._name);
          _buildingRectangle.drawBuildingBox((Graphics2D) g);
      }
 
  }


  // ============================================================
  // Private methods
  // ============================================================


}
