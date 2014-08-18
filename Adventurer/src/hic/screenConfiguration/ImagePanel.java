/**
 * ImagePanel.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package hic.screenConfiguration;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import chronos.Chronos;

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
   * Get the image from its filename, and then let {@code paintComponent} override take over
   * 
   * @param imageName the name of the image to display on the {@code ImagePanel}
   */
  public void displayImage(String imageName)
  {
    try {
      _image = ImageIO.read(new File(Chronos.ADV_IMAGE_PATH + imageName));
    } catch (IllegalArgumentException iaex) {
      System.err.println("IMagePanel: null image path given");
    } catch (IOException ioex) {
      System.err.println("IMagePanel: problems reading the image file");
    }
  }


  /**
   * Required override method to draw on a {@code JComponent}, in this case, {@code ImagePanel}
   */
  @Override
  public void paintComponent(Graphics g)
  {
    // Find top-left corner to image panel to overlay image onto
    int pWidth = _imagePanel.getWidth();
    int pHeight = _imagePanel.getHeight();

    // Draw the image at the top-left corner of the ImagePanel when JComponent gets its turn
    g.drawImage(_image, 0, 0, pWidth, pHeight, _imagePanel);
  }


  // ============================================================
  // Private methods
  // ============================================================


}
