/**
 * ImagePanel.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package hic.screenConfiguration;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import pdc.Util;

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
  static private Image _image;
  /** The border around the image */
  static private TitledBorder _border;


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
   * Set the border of the ImagePanel
   * 
   * @param borderColor color of the titled border, usually CONSTANTS.MY_BROWN
   */
  static public void setImageBorder(Color borderColor)
  {
    Border matte = BorderFactory.createMatteBorder(5, 5, 5, 5, borderColor);
    _border = BorderFactory.createTitledBorder(matte, "",
        TitledBorder.CENTER, TitledBorder.TOP, Util.makeRunicFont(14f), Color.BLACK);
//    _imagePanel.setBorder(_border);
  }


  /**
   * Set the image into the panel and let {@code paintComponent} take over
   * 
   * @param image the picture to display, or {@code image} type
   * @param title the caption over the image being displayed
   */
  static public void setDisplay(Image image, String title)
  {
    _image = image;
    _border.setTitle(title);
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
