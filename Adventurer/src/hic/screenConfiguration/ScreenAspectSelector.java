package hic.screenConfiguration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ScreenAspectSelector extends JFrame {

  private JPanel contentPane;
  private JPanel appPanel;
  private int userWinWidth;
  private int userWinHeight;
  private int screenAspect;
  private double contentSize;
  private int appPanWidth = 80;
  private int appPanHeight = 80;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          ScreenAspectSelector frame = new ScreenAspectSelector();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the frame.
   */
  public ScreenAspectSelector() {

    /* Create dummyMainFrame to mimic Adventurer Mainframe and
     * set frame defaults */
    JFrame dummyMainFrame = createMainFrame();
    
    // Creates the main menu bar at the top of the frame
    createMenuBar();
    
    // Sets up main content area within the frame (under the menu bar)
    setupContentPane();

    // Create resizable appPanel to demonstrate scalability of layout
//    JPanel appPanel = createAppPanel();

    // Create scalable image panel for use with any set of Swing containers
//    ScalableImagePanel myImagePanel = new ScalableImagePanel();
    /*
     * The previous stamtement might be better as follows to allow for more setup within a method:
     * ScalableImagePanel myImagePanel = createImagePanel();
     */




    // Sets up a panel overlying the content pane that can be replaced
    // depending on the screen aspect
    
    setupAppPanel();

    setVisible(true);
  }

  
  /**
   * Creates and displays the underlying application frame and
   * initializes it's defaults
   * @return 
   */
  private JFrame createMainFrame() {
    
    // Set frame defaults
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setExtendedState(Frame.MAXIMIZED_BOTH);
    setResizable(false);
    
    // Get height and width of user screen
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    userWinWidth = screenSize.width;
    userWinHeight = screenSize.height;
    setSize(userWinWidth, userWinHeight);
    
    // Get aspect ratio of user screen
    screenAspect = getScreenAspect();

    // Set frame to visible
    setVisible(true);
    
    return this;
  }

  /**
   * Calculates aspect ratio of user screen and decides whether it
   * is closer to "wide" or "standard" screen
   * @return the aspect ratio
   */
  private int getScreenAspect() {
    int aspect = 0;
    if (((userWinWidth / (double) userWinHeight) < TARGET_ASPECT)) {
      aspect = STANDARD_ASPECT;
    } else {
      aspect = WIDE_ASPECT;
    }
    return aspect;
  }

  /**
   * Creates the menu bar at the top of the frame
   */
  private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenu mn_AspectSelect = new JMenu("Select Aspect");
    menuBar.add(mn_AspectSelect);

    JMenuItem menuItem1 = new JMenuItem("Standard - 4:3");
    menuItem1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        contentPane.remove(appPanel);
        contentPane.revalidate();
        contentPane.repaint();
        appPanel = new StandardScreenPanel();
        contentPane.add(appPanel, "w 950, h 750");
      }
    });
    mn_AspectSelect.add(menuItem1);

    JMenuItem menuItem2 = new JMenuItem("Wide - 16:9");
    menuItem2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        contentPane.remove(appPanel);
        contentPane.revalidate();
        contentPane.repaint();
        appPanel = new WideScreenPanel();
        contentPane.add(appPanel, "w 1240, h 750");
      }
    });
    mn_AspectSelect.add(menuItem2);
  }

  /**
   * Sets up main content area within the frame (under the menu bar)
   */
  private void setupContentPane() {
    
    // Make reference for content pane
    contentPane = (JPanel)getContentPane();
    
    // Create an interior border that doesn't affect the menuBar
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    
    /* Set Layout to 1 row & 1 column that take up all available
     * space within the frame */
    contentPane.setLayout(new MigLayout("", "[grow,center]", "[grow,center]"));
    
    // Set background color
    contentPane.setBackground(Color.LIGHT_GRAY);
    
    // Set content pane to visible
    contentPane.setVisible(true);

    revalidate();

    // check size
    System.out.println("After revalidate:");
    System.out.println("\tContent Pane Size: " + this.getContentPane().getSize().toString());
    System.out.println();
  }

  
  private void setupAppPanel() {
    if (screenAspect == WIDE_ASPECT) {
      appPanel = new WideScreenPanel();
      // getContentPane().add(appPanel, "w 1240, h 750");
      // String hwString = "w 80%, h 80%";
      String hwCalcString = "w " + appPanWidth + "%, h " + appPanHeight + "%";
      // getContentPane().add(appPanel, "w 90%, h 90%");
      // getContentPane().add(appPanel, hwString);
      getContentPane().add(appPanel, hwCalcString);
    } else if (screenAspect == STANDARD_ASPECT) {
      appPanel = new StandardScreenPanel();
      // getContentPane().add(appPanel, "w 950, h 750");
    } else {
      System.out.println("Error: Screen Aspect = " + screenAspect);
    }

  }

  /* Private Constants */
  // over target is wide screen, under is standard screen
  private final double TARGET_ASPECT = 1.7;
  private final int WIDE_ASPECT = 1;
  private final int STANDARD_ASPECT = 2;

}
