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
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import chronos.Chronos;

/**
 * The {@code ImagePanel} class extends {@code ChronosPanel} so that it is easy to draw onto. It is a
 * singleton because it is created once and different images are displayed on it for the duration of
 * the program.
 * 
 * @author Al Cline
 * @version Aug 16, 2014 // original <br>
 */
@SuppressWarnings("serial")
public class ImagePanel extends ChronosPanel 
{  
  /** Box to be drawn over the current image */
  private BuildingRectangle _buildingRectangle;

  /** Image shown in panel */
  protected Image _image;

  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /** Private singleton constructor 
   * 
   * @param BaseCiv  base class for all civs that can control this panel 
   */
  public ImagePanel(String imageName, String title)
  {
    _title = title;
    setImageByName(imageName);
  }
  
  // ============================================================
  // Public methods
  // ============================================================
  
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

    // Find top-left corner to image panel to overlay image onto
    int pWidth = getWidth();
    int pHeight = getHeight();

    // Draw the image at the top-left corner of the ImagePanel when JComponent gets its turn
    g.drawImage(_image, 0, 0, pWidth, pHeight, this);

    if (_buildingRectangle != null) {
      // System.out.println("Drawing " + _buildingRectangle._name);
      _buildingRectangle.drawBuildingBox((Graphics2D) g);
    }

  }

  public void setImageByName(String imageName)
  {
    _image = convertToImage(imageName);
  }

  
  
  // ============================================================
  // Private methods
  // ============================================================
  
  /**
   * Create an {@code Image} type from its filename
   * 
   * @param imageName the name of the image to convert
   */
  private Image convertToImage(String imageName)
  {
    Image myImage = null;
    try {
      myImage = ImageIO.read(new File(Chronos.ADV_IMAGE_PATH + imageName));
    } catch (IOException ioex) {
      System.err.println("ImagePanel: problems reading the image file" + imageName);
    }
    return myImage;
  }

}
