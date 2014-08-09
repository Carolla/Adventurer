/**
 * ImagePanel.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package hic.screenConfiguration;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chronos.Chronos;

/**
 * The {@code ImagePanel} class is intended for use with a GUI using MigLayout. <br>
 * It takes three arguments, one of which is a {@code JPanel}. {@code ImagePanel} will generate an
 * image sized to the exact dimensions of the {@code JPanel}.
 * <P>
 * Precondition 1: The {@code JPanel} should have been previously added to a container and the root
 * frame of the container hierarchy should have had the {@code revalidate} method called or
 * {@code validate} if it is the first call. This ensures proper sizing for when {@code getSize} is
 * called.
 * <P>
 * Precondition 2: The {@code JPanel} should also have MigLayout applied with the appropriate
 * constraints. When the image is added to it, MigLayout constraints will be applied.
 * 
 * @author Dave Campbell
 * @version June 12, 2014 // original <br>
 *          Aug 9, 2014 // abc if panelsize is zero, then preferredSize is used when scaling; also
 *          added a few IllegalArgumentExceptions <br>
 */
@SuppressWarnings("serial")
public class ImagePanel extends JPanel
{

  // /* Private Constants */
  // private static final String IMAGE_PATH = Chronos.RESOURCES_PATH + Constants.FILE_SEPARATOR
  // + "images"
  // + Constants.FILE_SEPARATOR;

  /**
   * Gets the JLabel with the resized Image and adds it to the passed in panel.
   * 
   * @param panel The panel to get covered by the image
   * @param image The image to be resized and displayed
   * @throws IllegalArgumentException if either argument is null or contains no size information
   */
  public ImagePanel(JPanel panel, String origImage) throws IllegalArgumentException
  {
    // Generate the ImageLabel with properly sized Image
    // If fixed size is zero, use preferred size
    if ((panel.getWidth() * panel.getHeight()) <= 0) {
      panel.setSize(new Dimension(panel.getPreferredSize()));
    }
    // System.out.println("ImagePanel ctor \tpanel preferred Size = " + panel.getPreferredSize());
    // System.out.println("ImagePanel ctor \tpanel fixed Size = " + panel.getSize());
    JLabel myImgLabel = makeImageLabel(origImage, panel.getSize());

    // Apply the Image to the Panel
    panel.add(myImgLabel, "");
  }


  /**
   * Generates a JLabel with an Image that has been resized to the dimensions of the passed in
   * panel.
   * 
   * @param imageName name of the Image as a String
   * @param panelSize size for the image to be scaled to
   * @return the Label with properly sized Image
   */
  private JLabel makeImageLabel(String imageName, Dimension panelSize)
  {
    // Make ImageIcon
    String imagePath = Chronos.ADV_IMAGE_PATH + imageName;
    ImageIcon imgIcon = makeImageIcon(imagePath);

    // Resize the Image
    // System.out.println("ImagePanel.makeImageLabel \tpanelSize = \t" + panelSize);
    Image scaledImage = getScaledImage(imgIcon.getImage(), panelSize);

    // Make image label
    JLabel imgLabel = new JLabel(new ImageIcon(scaledImage));

    // Return the label
    return imgLabel;
  }

  /**
   * Creates the Image using the ImageIO class. Uses the Image to create an ImageIcon to allow
   * further manipulation of the Image. Catches an exception if creation of the image fails.
   * 
   * @param imgName name of the image as a String
   * @return an Icon with the Image at its original size
   */
  private ImageIcon makeImageIcon(String imgName)
  {
    ImageIcon myIcon = null;
    try {
      myIcon = new ImageIcon(ImageIO.read(new File(imgName)));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return myIcon;
  }

  /**
   * Here the actual resizing of the Image takes place.
   * 
   * @param image object containing the image to be resized
   * @param pnl size of the panel as a Dimension object
   * @return the resized image as a BufferedImage object
   */
  private Image getScaledImage(Image image, Dimension pnl)
  {
    BufferedImage resizedImg = new BufferedImage(pnl.width, pnl.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = resizedImg.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(image, 0, 0, pnl.width, pnl.height, null);
    g2.dispose();
    return resizedImg;
  }

}
