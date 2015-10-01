package hic.screenConfiguration;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

/*
 * Note about MigLayout and Resizing Images
 * ----------------------------------------
 * To make Images on a Swing JPanel resizable when using MigLayout
 *     First there has to be a Panel in place of the image
 *     Then validate() or revalidate() must be called on the base Frame
 *     Then getSize() can be called on the Panel
 *     Then the size can be passed to the Image
 */

/**
 * This class exists to demonstrate and test the PretendImagePanel class.
 * 
 * @author DaveC
 */

@SuppressWarnings("serial")
public class PretendAdventurerFrame extends JFrame {
  
  /* Instance variables */
  private final JPanel _contentPane;
  private final JPanel _appPanel;

  public static void main(String[] args) {
    PretendAdventurerFrame frame = new PretendAdventurerFrame();
    frame.showContainerInfo();
  }

  public PretendAdventurerFrame() {
    setupMainFrame(); // remove for use with Adventurer
    _contentPane = setupContentPane();
    _appPanel = new JPanel(new MigLayout( "insets 0", 
            "[:19.5%:19.5%]:1%:1%[:79.5%:79.5%]", "[:69.5%:69.5%]:1%:1%[:29.5%:29.5%]"));
    super.add(_appPanel, "w 80%, h 80%");
    printJPanelInfo("AppPanel", _appPanel);
    
    // Make the back panel for the image
    JPanel imageBackPanel = new JPanel(new MigLayout("insets 0, fill", "[center]", "[center]"));
    imageBackPanel.setBackground(Color.PINK);
    _appPanel.add(imageBackPanel, "cell 1 0, grow");
    printJPanelInfo("Image Back Panel", imageBackPanel);
    
//    JPanel myImgPanel = new ImagePanel(imageBackPanel, TOWN_IMAGE);

    // Make stand-in Panel for Button Area
    makeButtonPanel();
  }

  private void makeButtonPanel() {
    // Make button area back panel
    JPanel btnBackPanel = new JPanel(new MigLayout("insets 0", "", ""));
    btnBackPanel.setBackground(Color.CYAN);
    _appPanel.add(btnBackPanel, "cell 0 0, grow");
  }

  /**
   * Set Frame Dimensions to actual Screen Size and DCO
   */
  private void setupMainFrame() {
    super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    super.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    super.setExtendedState(Frame.MAXIMIZED_BOTH);
    super.setResizable(false);
    super.setVisible(true);
  }

  /**
   * Create content pane, with appropriate settings
   * 
   * @return content pane
   */
  private JPanel setupContentPane() {
    JPanel panel = (JPanel) getContentPane();
    panel.setLayout(new MigLayout("", "[grow,center]", "[grow,center]"));
    panel.setBackground(Color.LIGHT_GRAY);

    // Check visibility & dimensions
    System.out.println("MainFrame Visible: " + isVisible());
    printJPanelInfo("ContentPanel", panel);
    return panel;
  }


  /**
   * Displays attributes of a JComponent
   * 
   * @param panelName object description
   * @param panel object itself
   */
  private void printJPanelInfo(String panelName, JComponent panel) {
    this.revalidate();
    System.out.println("\t" + panelName);
    System.out.println("\t\t" + "Visible: " + panel.isVisible());
    System.out.println("\t\t" + "Size: " + panel.getSize());
  }


  /**
   * Shows both Mainframe (content pane) and AppPanel
   */
  public void showContainerInfo() {
    System.out.println("Container Info\n" + "--------------");
    printJPanelInfo("ContentPane", _contentPane);
    printJPanelInfo("AppPanel", _appPanel);
  }

}

