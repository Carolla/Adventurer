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
 * The {@code ImagePanel} class extends {@code JComponent} so that it is easy to draw
 * onto. It is a singleton because it is created once and different images are displayed on it for 
 * the duration of the program.
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


  // ============================================================
  // Deprecated methods temporarily
  // ============================================================


  // /**
  // * Resizes the image to fit in a label the size of the panel
  // *
  // * @param panel The panel to get covered by the image
  // * @param image The image to be resized and displayed
  // */
  // public ImagePanel(JPanel panel, String origImage)
  // {
  // // Generate the ImageLabel with properly sized Image
  // // If fixed size is zero, use preferred size
  // if ((panel.getWidth() * panel.getHeight()) <= 0) {
  // panel.setSize(new Dimension(panel.getPreferredSize()));
  // }
  // // System.out.println("ImagePanel ctor \tpanel preferred Size = " + panel.getPreferredSize());
  // // System.out.println("ImagePanel ctor \tpanel fixed Size = " + panel.getSize());
  // JLabel myImgLabel = makeImageLabel(origImage, panel.getSize());
  //
  // // Apply the Image to the Panel
  // panel.add(myImgLabel, "");
  // }
  //
  //
  // /**
  // * Generates a JLabel with an Image that has been resized to the dimensions of the passed in
  // * panel.
  // *
  // * @param imageName short name of the Image
  // * @param panelSize size for the image to be scaled to
  // * @return the Label with properly sized Image
  // */
  // public JLabel makeImageLabel(String imageName, Dimension panelSize)
  // {
  // // Make ImageIcon
  // String imagePath = Chronos.ADV_IMAGE_PATH + imageName;
  // validate();
  // ImageIcon imgIcon = makeImageIcon(imagePath);
  // // System.out.println("ImagePanel.makeImageLabel \timgIcon = \n"
  // // + "\t\t" + imgIcon.getIconHeight() + "\n"
  // // + "\t\t" + imgIcon.getIconWidth());
  //
  // // Resize the Image
  // Image scaledImage = getScaledImage(imgIcon.getImage(), panelSize);
  // validate();
  //
  // System.out.println("ImagePanel.makeImageLabel \tscaledImage = \n"
  // + "\t\t" + scaledImage.getHeight(null) + "\n"
  // + "\t\t" + scaledImage.getWidth(null));
  //
  // // Make image label
  // ImageIcon imgSizedIcon = new ImageIcon(scaledImage);
  // System.out.println("ImagePanel.makeImageLabel \timgSizedIcon = \n"
  // + "\t\t" + imgSizedIcon.getIconHeight() + "\n"
  // + "\t\t" + imgSizedIcon.getIconWidth());
  //
  // JLabel imgLabel = new JLabel(imgSizedIcon);
  // imgLabel.setSize(imgSizedIcon.getIconHeight(), imgSizedIcon.getIconWidth());
  // System.out.println("ImagePanel.makeImageLabel \timgLabel = \n"
  // + "\t\t" + imgLabel.getSize());
  //
  // // Return the label
  // return imgLabel;
  // }
  //
  // /**
  // * Creates the Image using the ImageIO class. Uses the Image to create an ImageIcon to allow
  // * further manipulation of the Image. Catches an exception if creation of the image fails.
  // *
  // * @param imgPath long path name of the image as a String
  // * @return an Icon with the Image at its original size
  // */
  // public ImageIcon makeImageIcon(String imgPath)
  // {
  // ImageIcon myIcon = null;
  // try {
  // myIcon = new ImageIcon(ImageIO.read(new File(imgPath)));
  // // System.err.println("ImagePanel.makeImageIcon \tmyIcon = \n"
  // // + "\t\t" + myIcon.getIconHeight() + "\n"
  // // + "\t\t" + myIcon.getIconWidth());
  // } catch (Exception e) {
  // e.printStackTrace();
  // }
  // return myIcon;
  // }
  //
  // /**
  // * Here the actual resizing of the Image takes place.
  // *
  // * @param image object containing the image to be resized
  // * @param pnl size of the panel as a Dimension object
  // * @return the resized image as a BufferedImage object
  // */
  // private Image getScaledImage(Image image, Dimension pnl)
  // {
  // if ((pnl.width * pnl.height) <= 0) {
  // throw new IllegalArgumentException("ImagePanel.getScaledImage(): has unknown size");
  // }
  // BufferedImage resizedImg = new BufferedImage(pnl.width, pnl.height,
  // BufferedImage.TYPE_INT_RGB);
  // Graphics2D g2 = resizedImg.createGraphics();
  // g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
  // RenderingHints.VALUE_INTERPOLATION_BILINEAR);
  // g2.drawImage(image, 0, 0, pnl.width, pnl.height, null);
  // g2.dispose();
  // return resizedImg;
  // }

}
