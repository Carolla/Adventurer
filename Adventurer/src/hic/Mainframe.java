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
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import chronos.Chronos;
import civ.Adventurer;
import civ.MainActionCiv;
import civ.MainframeCiv;
import mylib.Constants;
import mylib.hic.HelpDialog;
import mylib.hic.IHelpText;
import net.miginfocom.swing.MigLayout;

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
 */
@SuppressWarnings("serial")
public class Mainframe extends JFrame implements MainframeInterface, MouseListener,
    MouseMotionListener, IHelpText
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
  private Deque<ChronosPanel> _panelStack = new ArrayDeque<ChronosPanel>();


  /** JPanel to hold various images; this panel resides in the _rightHolder */
  private ImagePanel _imagePanel;

  private MainframeCiv _mfCiv;
  private MainActionCiv _mainActionCiv;
  private IOPanel _iop;

  private List<String> _partyHeros = new ArrayList<String>();
  private List<String> _summonableHeroes;

  /** Title of the IOPanel of left side */
  private final String IOPANEL_TITLE = " Transcript ";

  // /** Runic Font that pervades the text of the screens */
  // private final Font _runicFont = Chronos.RUNIC_FONT;
  // /** Standard Font for buttons, help, etc */
  // private final Font _stdFont = Chronos.STANDARD_FONT;

  /** Singleton Help Dialog for all help text */
  private HelpDialog _helpdlg;


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
   * Creates the initial frame layout: left and right panel holders with buttons, and image panel
   * showing chronos logo on right. Creates the {@code HelpDialog} singleton, ready to receive
   * context-sensitive help text when requested. Creates the {@code MainframeCiv} which takes
   * manages program control at the highest level.
   */
  public Mainframe()
  {
    // Define the graphic elements
    setupSizeAndBoundaries();
    createFrameAndMenubar(); // Depends on class members not being NULL
    addImagePanel(); // add image panel on right for adding images later
    redraw();

    // Initiate the MainframeCiv's state (validation and data management) within those elements
    _mfCiv = new MainframeCiv(this);
    // Set external elements
    setImage(_mfCiv.getInitialImage());
    setImageTitle(_mfCiv.getInitialTitle());

    // Create the one time help dialog
    prepareHelpDialog();

    // Display the Mainframe and panels now
    setVisible(true);

    // Create the mainActionCiv to manage the action buttons and town view
    _mainActionCiv = new MainActionCiv(this, _mfCiv);

  }


  // ============================================================
  // Public Methods
  // ============================================================

  // TODO: Replace this method with replaceLeftPanel()
  /**
   * Layout the IOPanel on the left: scrolling text window and working Comandline input area. The
   * IOPanel contains a transcript of user inputs and output on top, and a commandline for commands
   * on the bottom.
   * 
   * @param mac the controller for this widget
   */
  public void addIOPanel(MainActionCiv mac)
  {
    _mainActionCiv = mac;
    _iop = new IOPanel(_mainActionCiv);
    _leftHolder.removeAll();
    setLeftPanelTitle(IOPANEL_TITLE);
    _leftHolder.add(_iop);
    redraw();

    // Save the panel state for later
    _panelStack.push(_iop);
  }


  // NOTE: Check replacePanel() first if you are adding to the mainframe panel holders
  @Override
  public void addPanel(JComponent component)
  {
    add(component);
  }

  public boolean approvedQuit()
  {
    Adventurer.approvedQuit();
    return true;
  }


  /**
   * Remove the current panel and return to the previous panel. This currently works only for the
   * left side of the mainframe.
   */
  public void back()
  {
    // Remove the current panel
    ChronosPanel oldPanel = null;
    if (!_panelStack.isEmpty()) {
      oldPanel = _panelStack.pop(); // remove the current panel
      // System.out.println("PanelStack after pop: " + oldPanel.getTitle());
      // } else {
      // System.out.println("PanelStack is empty");
    }

    // Pop a second time to get to the previous panel
    if (!_panelStack.isEmpty()) {
      replaceLeftPanel(_panelStack.pop());
    }
  }


  /**
   * Display error text onto the scrolling output panel
   *
   * @param msg text to append to existing text in panel
   */
  public void displayErrorText(String msg)
  {
    _iop.displayErrorText(msg);
  }


  /**
   * Display image and associated test to the IOPanel
   *
   * @param msg text to append to text in IOPanel
   * @param image to display in Image Panel
   */
  public void displayImageAndText(String msg, Image image)
  {
    displayText(msg);
    _imagePanel.setImage(image);
    redraw();
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
   * Display text onto the scrolling output panel
   *
   * @param msg text to append to existing text in panel
   */
  public void displayText(String msg)
  {
    _iop.displayText(msg);
    _iop.setFocusOnCommandWindow();
  }


  public Dimension getImagePanelSize()
  {
    return _rightHolder.getSize();
  }

  public void mouseClicked(MouseEvent e)
  {
    // _mfCiv.handleClick(e.getPoint());
    _mainActionCiv.returnToTown(e.getPoint());
  }

  public void mouseDragged(MouseEvent e)
  {
    // _mfCiv.handleMouseMovement(e.getPoint());
    _mainActionCiv.setBuildingSelected(e.getPoint());
  }

  public void mouseEntered(MouseEvent e)
  {}

  public void mouseExited(MouseEvent e)
  {}

  public void mouseMoved(MouseEvent e)
  {
    // _mfCiv.handleMouseMovement(e.getPoint());
    _mainActionCiv.setBuildingSelected(e.getPoint());
  }

  public void mousePressed(MouseEvent e)
  {}

  public void mouseReleased(MouseEvent e)
  {}


  public void redraw()
  {
    validate();
    repaint();
  }


  /**
   * Replaces the left panel with the new one provided and displays the panel's title. Saves the
   * state in case the user needs to back out.
   * 
   * @param newPanel that replaces existing panel on left side of Mainframe
   */
  public void replaceLeftPanel(ChronosPanel newPanel)
  {
    // Replace the panel with the existing one
    _leftHolder.removeAll();
    _leftHolder.add(newPanel);
    setLeftPanelTitle(newPanel.getTitle());

    // Save the state for later
    _panelStack.push(newPanel);
//    System.out.println("PanelStack after push: " + _panelStack.peekFirst().getTitle());

    redraw();
  }


  public void setBuilding(BuildingRectangle rect)
  {
    _imagePanel.setRectangle(rect);
    redraw();
  }


  public void setHeroList(List<String> list)
  {
    _partyHeros = list;
    // setHeroPartyText();
  }


  /**
   * Display an image in the Image panel
   *
   * @param image to display on the rightside
   */
  public void setImage(Image image)
  {
    _imagePanel.setImage(image);
    redraw();
  }


  /**
   * Display a title onto the border of the right side image panel. Add one space char on either
   * side for aesthetics
   *
   * @param title of the panel to set
   */
  public void setImageTitle(String title)
  {
    TitledBorder border = (TitledBorder) _rightHolder.getBorder();
    border.setTitle(" " + title + " ");
  }


  /**
   * Display a title onto the border of the left side IO Panel
   *
   * @param title of the panel to set
   */
  public void setLeftPanelTitle(String title)
  {
    TitledBorder border = (TitledBorder) _leftHolder.getBorder();
    border.setTitle(title);
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
   * Layout the image panel on the right side of the frame, with mouse listeners.
   */
  private void addImagePanel()
  {
    _imagePanel = ImagePanel.getInstance();

    _rightHolder.addMouseListener(Mainframe.this);
    _rightHolder.addMouseMotionListener(Mainframe.this);
    _rightHolder.add(_imagePanel);
  }


  // private String getBorderTitle()
  // {
  // TitledBorder border = (TitledBorder) _leftHolder.getBorder();
  // return border.getTitle();
  // }

  // /**
  // * Create the behavior for selecting an adventure, which drives the frame update. <br>
  // * Warning: Known bug with MigLayout in that {@code float} font sizes can cause overruns on
  // * round-up calculations. "Choose your Adventure" overruns the button length, but
  // * "Select your Adventure" does not, despite being the same number of characters!
  // *
  // * @return the button created
  // */
  // private JButton createAdventureButton()
  // {
  // JButton button = createButtonWithTextAndIcon(ADV_IMAGE, "Select your Adventure ");
  // button.addActionListener(new ActionListener() {
  // public void actionPerformed(ActionEvent e)
  // {
  // ArrayList<String> adventures = _mfCiv.getAdventures();
  // Object[] adventuresArr = adventures.toArray();
  // Object selectedValue = JOptionPane.showInputDialog(
  // Mainframe.this, "Select an Adventure", "Adventures",
  // JOptionPane.INFORMATION_MESSAGE, null, adventuresArr, adventuresArr[0]);
  // if (selectedValue != null) {
  // // System.out.println("Adventure selected was: " + selectedValue);
  // // _mfCiv.loadSelectedAdventure(selectedValue.toString());
  // _mainActionCiv.loadSelectedAdventure(selectedValue.toString());
  // }
  // }
  // });
  // return button;
  // }
  //
  // /**
  // * Create the Adventure, Heroes, and Create-Hero buttons, and button panel for them
  // */
  // private void createActionPanel()
  // {
  // JButton adventureButton = createAdventureButton();
  // JButton summonButton = createSummonHeroesButton();
  // JButton creationButton = createNewHeroButton();
  //
  // // Create the Civ to handle the main action buttons
  // _mainActionCiv = new MainActionCiv(this);
  //
  // JPanel buttonPanel = new JPanel();
  // // Align all buttons in a single column
  // buttonPanel.setLayout(new MigLayout("wrap 1"));
  // buttonPanel.setPreferredSize(new Dimension(
  // (int) (USERWIN_WIDTH - FRAME_PADDING) / 2, USERWIN_HEIGHT - FRAME_PADDING));
  // buttonPanel.setBackground(Constants.MY_BROWN.brighter());
  //
  // /** Buttons are at 25% to allow space for Command Line later */
  // buttonPanel.add(adventureButton, "hmax 25%, grow");
  // buttonPanel.add(summonButton, "hmax 25%, grow");
  // buttonPanel.add(creationButton, "hmax 25%, grow");
  //
  // _leftHolder.add(buttonPanel);
  // _leftPanelState = buttonPanel;
  // _leftTitleState = getBorderTitle();
  // }

  // public JButton createButtonWithTextAndIcon(String imageFilePath, String buttonText)
  // {
  // JButton button = new JButton(buttonText);
  // button.setBackground(Constants.MY_BROWN.brighter().brighter());
  //
  // button.setFont(_stdFont);
  // button.setIcon(new ImageIcon(Chronos.ADV_IMAGE_PATH + imageFilePath));
  // button.setIconTextGap(40);
  // return button;
  // }

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
    setJMenuBar(new Menubar(this, _mfCiv));

    // Define a left and right ChronosPanel to manage subordinate right- and left-side panels
    _leftHolder = new ChronosPanel();
    _leftHolder.setLayout(new MigLayout("insets 0", "[grow,fill]", "[grow,fill]"));
    _leftHolder = makePanelAsHolder(_leftHolder, Constants.MY_BROWN, Color.WHITE);

    _rightHolder = new ChronosPanel();
    _rightHolder.setLayout(new MigLayout("insets 0", "[grow,fill]", "[grow,fill]"));
    _rightHolder.setTitle(" ");
    _rightHolder = makePanelAsHolder(_rightHolder, Constants.MY_BROWN, Color.WHITE);

    _contentPane.add(_leftHolder, "cell 0 0, wmax 50%, grow");
    _contentPane.add(_rightHolder, "cell 1 0, wmax 50%, grow");
    _contentPane.setFocusable(true);
    // TODO Add backout state for right side also
    // . . .
  }


  /**
   * Create a holder for the left or right side of the frame, with all cosmetics. Holders will have
   * same layout manager, size, border type, and runic font title. <br>
   * TODO: Currently, each ChronosPanel requires a title, and is accessible by {@code getTitle()}.
   * 
   * @param borderColor background Color for the border
   * @param title to be positioned top center in Runic font
   * @param backColor background Color for the panel
   * 
   * @return the JPanel that is assigned to the left or the right
   */
  // private ChronosPanel makePanelHolder(ChronosPanel holder, Color borderColor, String title,
  // Color backColor)
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
    // TODO Move call to pdc.Util to mfCiv so pdc is not imported; and duplicate calls are
    // avoided
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
      _mfCiv.quit();
    }

  } // end of Terminator inner class


} // end of Mainframe outer class
