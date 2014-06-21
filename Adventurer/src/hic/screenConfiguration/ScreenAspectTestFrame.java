package hic.screenConfiguration;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

/*
 * The big issue with getting actual sizes of panels/components seems to have centered around the
 * order of adding the panels/components. In the previous example (ScreenAspectSelector) a separate
 * object was created to be the inner "AppPanel". This object was created and then passed back to
 * the original object, only then to be added to the main content area. The late addition of this
 * panel seems to have kept it's size inaccessible to the call 'getSize()'.
 * 
 * Here the issue is resolved by adding the components in order - from back to front, calling
 * validate or revalidate on the underlying frame object, and then using 'getSize()'.
 */

@SuppressWarnings("serial")
public class ScreenAspectTestFrame extends JFrame {

  public static void main(String[] args) {
    ScreenAspectTestFrame frame = new ScreenAspectTestFrame();
    frame.showSizes();
  }

  private static final String TOWN_IMAGE = "ext_Quasqueton.JPG";
  // Instance Vars
  private final JPanel _contentPane;
  private final JPanel _appPanel;

  public ScreenAspectTestFrame() {
    setupMainFrame(); // remove for use with Adventurer
    _contentPane = setupContentPane();
    _appPanel =
        new JPanel(new MigLayout("insets 0", "[:20%:20%][:80%:80%]", "[:70%:70%][:30%:30%]"));
    super.add(_appPanel, "w 80%, h 80%");
    printJPanelInfo("AppPanel", _appPanel);
    ImageDisplayPanel.createImagePanel(TOWN_IMAGE, _appPanel, this);
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
    System.out.println(panelName + " Visible: " + panel.isVisible());
    System.out.println(panelName + " Size: " + panel.getSize() + "\n");
  }


  /**
   * Shows both Mainframe (content pane) and AppPanel
   */
  public void showSizes() {
    printJPanelInfo("MainFrame", _contentPane);
    printJPanelInfo("AppPanel", _appPanel);
  }

}
