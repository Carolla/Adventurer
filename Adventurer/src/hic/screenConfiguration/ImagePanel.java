/*
 * File: PretendImagePanel.java
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import mylib.Constants;
import chronos.Chronos;

/**
 * The PretendImagePanel class is intended for use with a GUI using MigLayout. It takes three
 * arguments, one of which is a JPanel. PretendImagePanel will generate an image sized to the exact
 * dimensions of the JPanel.
 * 
 * Precondition 1: The JPanel should have been previously added to a container and the root Frame of
 * the container hierarchy should have had the revalidate() method called or validate() if it is the
 * first call. This ensures proper sizing for when getSize() is called.
 * 
 * Precondition 2: The JPanel should also have MigLayout applied with the appropriate constraints.
 * When the image is added to it, MigLayout constraints will be applied.
 * 
 * @author DaveC
 * 
 */
@SuppressWarnings("serial")
public class ImagePanel extends JPanel {

  /* Private Constants */
  private static final String IMAGE_PATH = Chronos.WORKSPACE + Constants.FILE_SEPARATOR
      + "Adventurer" + Constants.FILE_SEPARATOR + "resources" + Constants.FILE_SEPARATOR + "images"
      + Constants.FILE_SEPARATOR;

  /**
   * Gets the JLabel with the resized Image and adds it to the passed in panel.
   * 
   * @param panel The panel to get covered by the image
   * @param image The image to be resized and displayed
   */
  public ImagePanel(JPanel panel, String origImg) {
    // Generate the ImageLabel with properly sized Image
    JLabel myImgLabel = makeImageLabel(origImg, panel.getSize());

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
  private JLabel makeImageLabel(String imageName, Dimension panelSize) {
    // Make ImageIcon
    ImageIcon imgIcon = makeImageIcon(IMAGE_PATH + imageName);

    // Resize the Image
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
  private ImageIcon makeImageIcon(String imgName) {
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
  private Image getScaledImage(Image image, Dimension pnl) {
    BufferedImage resizedImg = new BufferedImage(pnl.width, pnl.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = resizedImg.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(image, 0, 0, pnl.width, pnl.height, null);
    g2.dispose();
    return resizedImg;
  }

}
