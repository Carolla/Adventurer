/**
 * Mainframe.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package hic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import mylib.Constants;
import mylib.hic.HelpDialog;
import net.miginfocom.swing.MigLayout;
import chronos.pdc.Chronos;
import civ.Adventurer;

/**
 * Initial frame displays three buttons and Chronos logo.<br>
 * Standard frame displays input/output panel and town image (or building images) <br>
 * Each has their own context help text. <br>
 * 
 * @author Al Cline
 * @version Jul 21, 2014 // rebuilt to comply with architectural model (lost history) <br>
 *          Jul 28, 2014 // added {@code HelpKeyListener} interface for Help functionality <br>
 *          Aug 3, 2014 // added {@code getWindowSize} to replace public variables <br>
 *          Aug 3, 2014 // added Runic font to the buttons <br>
 *          Aug 6, 2014 // removed innner classes and merged StandardLayout with IOPanel <br>
 *          Aug 18, 2014 // moved {@code ImagePanel} backend methods to {@code MainframeCiv} <br>
 *          Aug 23 2014 // added methods to change titles on either panel, and images in the image
 *          pane <br>
 *          Oct 19, 2015 // repaired Cancel button on Quit prompt window <br>
 *          Nov 6, 2015 // re-architected Mainframe so that widgets are called by civs <br>
 *          Nov 7, 2015 // re-architected Mainframe so no PDC classes are imported into HIC <br>
 *          Nov 14, 2015 // re-architected Mainframe so that MainframeCiv starts first, and has
 *          program control <br>
 */
@SuppressWarnings("serial")
// public class Mainframe extends JFrame implements MainframeInterface, IHelpText
public class Mainframe extends JFrame implements MainframeInterface
{
  /** Width of the platform user's window frame */
  private static int USERWIN_WIDTH;
  /** Height of the platform user's window frame */
  private static int USERWIN_HEIGHT;
  /** Number of pixels on empty border spacing */
  public static final int PAD = 10;

  /** Common content pane for the Mainframe */
  private JPanel _contentPane;
  /** Empty left-side panel holder for initial and standard panels. */
  private ChronosPanel _leftHolder;
  /** Empty right-side panel holder for initial standard panels. */
  private ChronosPanel _rightHolder;

  /** Keep panel states to return to in case CANCEL is hit */
  private Deque<ChronosPanel> _leftPanelStack = new ArrayDeque<ChronosPanel>(5);
  private Deque<ChronosPanel> _rightPanelStack = new ArrayDeque<ChronosPanel>(5);

  /** Singleton Help Dialog for all help text */
  private HelpDialog _helpdlg;
  private ImagePanel _imagePanel;

  /** Help Title for the mainframe */
  private static final String _helpTitle = "GREETINGS ADVENTURER!";
  /** Help Text for the mainframe */
  private static final String _helpText =
      "Greetings Adventurer! \n"
          + "To get started, click on the large button on the left to create a new Hero. "
          + "Then select an Adventure to explore. "
          + "Kill monsters, solve puzzles, and find treasure in the Adventure's Arena to gain "
          + "experience points. The more points you have, the more power and fame you get. "
          + "When you get enough experience, join one of the many Guilds in town. "
          + "Guilds are important to your Adventuring career.\n\n"
          + "Before entering the Arena, have your Hero visit Buildings to prepare for questing. "
          + "You can get important info from patrons at the Inn, buy supplies from the General "
          + "Store, or get a loan from the Bank. "
          + "Don't get in trouble with the townspeople. We also have a jail.\n\n"
          + "If you leave your Hero in town when you exit the game, he or she will be waiting "
          + "in the same building when you return. If you Save your Hero, he or she can be summoned "
          + "from the Hall of Heroes next time. ";


  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /**
   * Creates the initial frame layout: left and right panel holders with buttons, and image panel
   * showing chronos logo on right. Creates the {@code HelpDialog} singleton, ready to receive
   * context-sensitive help text when requested. Creates the {@code MainframeCiv} which takes
   * manages program control at the highest level.
   */
  public Mainframe()
  {
    // Define the graphic elements
    setupSizeAndBoundaries();
    createFrameAndMenubar();

    // Create the one time help dialog
    prepareHelpDialog();

    // Display the Mainframe and panels now
    setVisible(true);
    redraw();
  }


  // ============================================================
  // Public Methods
  // ============================================================

  /**
   * Remove the current panel and return to the previous panel. This currently works only for the
   * left side of the mainframe.
   */
  public void back()
  {
    // Remove the current panel
    if (!_leftPanelStack.isEmpty()) {
      _leftPanelStack.pop();
    }

    // Pop a second time to get to the previous panel
    if (!_leftPanelStack.isEmpty()) {
      replaceLeftPanel(_leftPanelStack.pop());
    }
  }


  /**
   * Remove the current panel and return to the main action panel, as many levels as it takes
   */
  public void backToMain(String newFrameTitle)
  {
    while (_leftPanelStack.size() > 1) {
      _leftPanelStack.pop();
    }

    if (newFrameTitle == null) {
      replaceLeftPanel(_leftPanelStack.pop());
    } else {
      ChronosPanel panel = _leftPanelStack.pop();
      panel.setTitle(newFrameTitle);
      replaceLeftPanel(panel);
    }
  }

  /**
   * Display an image into the right side panel
   * 
   * @param title to show above the image
   * @param imageName filename of the image to display
   */
  public void displayImage(String title, String imageName)
  {
    _imagePanel.setTitle(title);
    _imagePanel.setImageByName(imageName);
  }


  /**
   * Display a prompt, asking a question of the user
   *
   * @param msg the question to be asked, must be yes or no
   * @return the answer to the prompt
   */
  public boolean displayPrompt(String msg)
  {
    int selection =
        JOptionPane.showConfirmDialog(this, msg, "REALLY?", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE);
    if (selection == JOptionPane.OK_OPTION) {
      return true;
    }
    return false;
  }


  /**
   * Get the size of the main window being displayed, which is used as a standard for laying out
   * subcomponents and panels.
   * 
   * @return {@code Dimension} object; retrieve int values with {@code Dimension.width} and
   *         {@code Dimension.height}
   */
  static public Dimension getWindowSize()
  {
    return new Dimension(USERWIN_WIDTH, USERWIN_HEIGHT);
  }


  /**
   * Replaces a panel on left side of mainframe with the new one provided and displays the panel's
   * title. Saves the state in case the user needs to back out.
   * 
   * @param newPanel that replaces existing panel on left side of Mainframe
   */
  public void replaceLeftPanel(ChronosPanel newPanel)
  {
    setPanelTitle(newPanel.getTitle());
    _leftHolder.removeAll();
    _leftHolder.add(newPanel);

    // Save the state for later
    _leftPanelStack.push(newPanel);

    newPanel.setVisible(true);
    redraw();
  }

  /**
   * Replaces an image on the right side of mainframe with the new one provided and displays the
   * panel's title. Saves the state in case the user needs to back out.
   * 
   * @param newPanel that replaces existing panel on left side of Mainframe
   */
  public void replaceRightPanel(ChronosPanel newPanel)
  {
    setImageTitle(newPanel.getTitle());
    _rightHolder.removeAll();
    _rightHolder.add(newPanel);

    // Save the state for later
    _rightPanelStack.push(newPanel);

    newPanel.setVisible(true);
    redraw();
  }

  public void setImagePanel(ImagePanel imagePanel)
  {
    _imagePanel = imagePanel;
  }


  /**
   * Allows external setting of the title into the border of the left-side panel
   * 
   * @param new title for panel
   */
  public void setLeftTitle(String title)
  {
    setPanelTitle(title);
    redraw();
  }


  /**
   * Display the help text for this mainframe; implements {@code IHelpText}
   */
  public void showHelp()
  {
    _helpdlg.setVisible(true);
    _helpdlg.showHelp(_helpTitle, _helpText);
  }



  // ============================================================
  // Private Methods
  // ============================================================

  /**
   * Create mainframe layout and menubar; add left and right panel holders
   */
  private void createFrameAndMenubar()
  {
    // Define the contentPane and cast as JPanel
    setupContentPane();
    // Add the frame listener to prompt and terminate the application
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    addWindowListener(new Terminator());

    // Add menu
    setJMenuBar(new Menubar(this));

    // Define a left and right ChronosPanel to manage subordinate right- and left-side panels
    _leftHolder = new ChronosPanel(" ");
    _leftHolder.setLayout(new MigLayout("insets 0", "[grow,fill]", "[grow,fill]"));
    _leftHolder = makePanelAsHolder(_leftHolder, Constants.MY_BROWN, Color.WHITE);

    _rightHolder = new ChronosPanel(" ");
    _rightHolder.setLayout(new MigLayout("insets 0", "[grow,fill]", "[grow,fill]"));
    _rightHolder = makePanelAsHolder(_rightHolder, Constants.MY_BROWN, Color.WHITE);

    _contentPane.add(_leftHolder, "cell 0 0, wmax 50%, grow");
    _contentPane.add(_rightHolder, "cell 1 0, wmax 50%, grow");
    _contentPane.setFocusable(true);
  }


  /**
   * Create a holder for the left or right side of the frame, with all cosmetics. Holders will have
   * same layout manager, size, border type, and runic font title. <br>
   * 
   * @param borderColor background Color for the border
   * @param title to be positioned top center in Runic font
   * @param backColor background Color for the panel
   * 
   * @return the JPanel that is assigned to the left or the right
   */
  private ChronosPanel makePanelAsHolder(ChronosPanel holder, Color borderColor, Color backColor)
  {
    Dimension holderSize = new Dimension(USERWIN_WIDTH / 2, USERWIN_HEIGHT);
    holder.setPreferredSize(holderSize);
    holder.setBackground(borderColor);

    Border matte = BorderFactory.createMatteBorder(5, 5, 5, 5, borderColor);
    Border titled = BorderFactory.createTitledBorder(matte, holder.getTitle(),
        TitledBorder.CENTER, TitledBorder.TOP, Chronos.RUNIC_FONT, Color.BLACK);
    holder.setBorder(titled);
    holder.setBackground(backColor);

    return holder;
  }

  /**
   * Prepare the HelpDialog and HelpKey event responder
   */
  private void prepareHelpDialog()
  {
    _helpdlg = HelpDialog.getInstance(this);
    _helpdlg.setMyFont(Chronos.RUNIC_FONT);
    _contentPane.addKeyListener(new KeyListener() {
      public void keyReleased(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_F1) {
          showHelp();
        }
      }

      // Empty methods required to be overridden
      public void keyPressed(KeyEvent e)
      {}

      public void keyTyped(KeyEvent e)
      {}
    });
  }

  private void redraw()
  {
    validate();
    repaint();
  }

  /**
   * Display a title onto the border of the right side image panel. Add one space char on either
   * side for aesthetics
   *
   * @param title of the panel to set
   */
  private void setImageTitle(String title)
  {
    TitledBorder border = (TitledBorder) _rightHolder.getBorder();
    border.setTitle(" " + title + " ");
  }


  /**
   * Display a title onto the border of the left side command panel. Add one space char on either
   * side for aesthetics
   *
   * @param title of the panel to set
   */
  private void setPanelTitle(String title)
  {
    TitledBorder border = (TitledBorder) _leftHolder.getBorder();
    border.setTitle(" " + title + " ");
  }


  /** Apply the layout manager to the content pane */
  private void setupContentPane()
  {
    // Update the content pane with borders and layout manager
    _contentPane = (JPanel) getContentPane();
    _contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(_contentPane);
    _contentPane.setLayout(new MigLayout("", "[grow, fill]10[grow]", "[grow]"));
  }

  /** Define the mainframe layout characteristics */
  private void setupSizeAndBoundaries()
  {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    USERWIN_WIDTH = screenSize.width;
    USERWIN_HEIGHT = screenSize.height;
    setSize(USERWIN_WIDTH, USERWIN_HEIGHT);
    setLocationByPlatform(true); // Operating System specific windowing
    setExtendedState(Frame.MAXIMIZED_BOTH);
    setResizable(false);
  }


  // ============================================================
  // Inner Class for catching frame events
  // ============================================================
  /**
   * This is an inner class only to inherit from the {@code WindowAdaptor} class instead of
   * implementing a lot of unneeded methods by implementing {@code WindowListener} interface.
   *
   * @author alancline
   * @version Aug 29, 2014 // original <br>
   */
  public class Terminator extends WindowAdapter
  {
    /** Default constructor */
    public Terminator()
    {}

    /**
     * Close the window and the application by calling the {@code Adventurer.quit} method.
     *
     * @param event when user closes application frame
     */
    public void windowClosing(WindowEvent evt)
    {
      if (Mainframe.this.displayPrompt("Quit Adventurer?") == true) {
        Adventurer.approvedQuit();
      }
    }

  } // end of Terminator inner class

} // end of Mainframe outer class
