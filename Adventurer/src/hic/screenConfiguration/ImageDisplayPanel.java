
package hic.screenConfiguration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mylib.Constants;
import net.miginfocom.swing.MigLayout;
import chronos.Chronos;

@SuppressWarnings("serial")
public class ImageDisplayPanel extends JPanel
{

  // Private Constants
  private static final String IMAGE_PATH = Chronos.CHRONOS_LIB_RESOURCES_PATH + "images"
      + Constants.FS;
  private static JFrame _frame;

  /**
   * Static singleton generator allows return of newly generated image panel
   * 
   * @param imageName picture to display
   * @param panel display area not be be exceeded by image size, panel must be visible or sizing
   *        will fail
   * @param frame
   * @return
   * @return JPanel with image, ready for display JPanel chronosPanel = createImagePanel(String
   *         imageName, JPanel somePanel)
   */
  public static final JPanel createImagePanel(String imageName, JPanel panel, JFrame frame)
  {
    _frame = frame; // TODO: remove reference to frame ( change to MainFrame.getInstance()? )
    new ImageDisplayPanel(imageName, panel);
    return panel;
  }

  /**
   * Display a picture in a relative size panel
   * 
   * @param imageName picture to display
   * @param panel display area not be be exceeded by image size, panel must be visible or sizing
   *        will fail
   * @return
   * @return JPanel with image, ready for display JPanel chronosPanel = createImagePanel(String
   *         imageName, JPanel somePanel)
   */
  private ImageDisplayPanel(String imageName, JPanel panel)
  {
    if (!panel.isShowing()) {
      throw new IllegalArgumentException("Panel must be showing in order to be used.");
    }

    JPanel imgDispAreaPanel = createImgDispAreaPanel();
    forceDrawingOfPanel(panel, imgDispAreaPanel);
    createAndAddImage(imageName, imgDispAreaPanel);
  }

  private JPanel createImgDispAreaPanel()
  {
    JPanel imgDispAreaPanel = new JPanel(new MigLayout("insets 0", "", ""));
    imgDispAreaPanel.setBackground(Color.BLUE);
    return imgDispAreaPanel;
  }

  /**
   * Force image to have a size
   * 
   * @param panel parent panel to draw on
   * @param imgDispAreaPanel display panel
   */
  private void forceDrawingOfPanel(JPanel panel, JPanel imgDispAreaPanel)
  {
    panel.add(imgDispAreaPanel, "cell 1 0, top, right, w 95%, h 95%");
    printJPanelInfo("ImgDispAreaPanel", imgDispAreaPanel);
  }

  /**
   * Create Label with image as icon
   * 
   * @param imageName name of image file
   * @param imgDispAreaPanel display panel for image
   */
  private void createAndAddImage(String imageName, JPanel imgDispAreaPanel)
  {
    JLabel image = makeImageLabel(imgDispAreaPanel.getSize(), imageName);
    imgDispAreaPanel.add(image);
    System.out.println("After Revalidate and Add:");
    printJPanelInfo("Image Label", image);
  }

  private JLabel makeImageLabel(Dimension d, String imageName)
  {
    ImageIcon myIcon = getImageIcon(IMAGE_PATH + imageName);
    Image scaledImage = getScaledImage(myIcon.getImage(), d);
    return new JLabel(new ImageIcon(scaledImage));
  }

  private ImageIcon getImageIcon(String imageName)
  {
    ImageIcon myIcon = null;
    try {
      myIcon = new ImageIcon(ImageIO.read(new File(imageName)));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return myIcon;
  }

  private Image getScaledImage(Image image, Dimension d)
  {
    BufferedImage resizedImg = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = resizedImg.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(image, 0, 0, d.width, d.height, null);
    g2.dispose();
    return resizedImg;
  }

  /**
   * Displays attributes of a JComponent
   * 
   * @param panelName object description
   * @param panel object itself
   * 
   */
  private void printJPanelInfo(String panelName, JComponent panel)
  {
    _frame.revalidate(); // TODO: replace _frame.revalidate with
                         // Mainframe.getInstance().revalidate()
    System.out.println(panelName + " Visible: " + panel.isVisible());
    System.out.println(panelName + " Size: " + panel.getSize() + "\n");
  }
}
