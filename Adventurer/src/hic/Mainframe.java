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
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import mylib.Constants;
import mylib.hic.HelpDialog;
import mylib.hic.IHelpText;
import mylib.hic.ShuttleList;
import net.miginfocom.swing.MigLayout;
import pdc.Util;
import chronos.Chronos;
import civ.MainframeCiv;

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
 *          Aug 23 2014 // added methods to change titles on either panel, and images in the imaage
 *          pane <br>
 */
// Mainframe serialization unnecessary
@SuppressWarnings("serial")
public class Mainframe extends JFrame implements MouseListener, MouseMotionListener, IHelpText
{
  /** Reference to this singleton */
  static private Mainframe _mainframe = null;

  /** Singleton Help Dialog for all help text */
  private HelpDialog _helpdlg;

  /** Number of pixels on empty border spacing */
  public static final int PAD = 10;

  /** Icons for the left-side buttons */
  private static final String REGISTRAR_IMAGE = "raw_Register.jpg";
  private static final String HALL_IMAGE = "icn_HallOfHeroes.jpg";
  private static final String ADV_IMAGE = "icn_Town.jpg";

  /** Width of the platform user's window frame */
  private static int USERWIN_WIDTH;
  /** Height of the platform user's window frame */
  private static int USERWIN_HEIGHT;
  /** Amount of space in pixels around the frame and image of aesthetics */
  public static final int FRAME_PADDING = 90;

  /** Common content pane for the Mainframe */
  private JPanel _contentPane;
  /** Empty left-side panel holder for initial and standard panels. */
  private JPanel _leftHolder;
  /** Empty right-side panel holder for initial standard panels. */
  private JPanel _rightHolder;
  /** JPanel to hold various images; this panel resides in the _rightHolder */
  private ImagePanel _imagePanel;

  private MainframeCiv _mfCiv;
  private IOPanel _iop;
  private List<String> _partyHeros = new ArrayList<String>();
  private List<String> _summonableHeroes;

  /** Title of the IOPanel of left side */
  private final String IOPANEL_TITLE = " Transcript ";
  /** Title of the initial three-button panel on left side */
  private final String INITIAL_OPENING_TITLE = " Actions ";
  // /** Title of the Image Panel on right side */
  // private final String INITIAL_IMAGE_TITLE = " Chronos Logo ";


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
   * Create Mainframe as singleton so other mainframes are not created concurrently.
   * 
   * @return Mainframe instance
   */
  static public Mainframe getInstance()
  {
    if (_mainframe == null) {
      _mainframe = new Mainframe();
    }
    return _mainframe;
  }


  /**
   * Creates the initial frame layout: left and right panel holders with buttons, and image panel
   * showing chronos logo on right.
   * <P>
   * Creates the {@code HelpDialog} singleton, ready to receive context-senstitive help text when
   * requested.
   * <P>
   * Creates the {@code MainframeCiv} which takes control after an adventure is selected.
   */
  private Mainframe()
  {
    // Create the components of the mainframe
    createFrameAndMenubar(); // contains left and right panel holders
    addImagePanel(); // add image panel on right for adding images later
    createButtons(); // creates three action buttons on panel
    redraw();
    setVisible(true);

    // Create the one time help dialog
    prepareHelpDialog();

    // Create the Civ
    _mfCiv = new MainframeCiv(this);

  }


  /**
   * Create mainframe layout and menubar; add left and right panel holders, which have titled
   * borders
   */
  private void createFrameAndMenubar()
  {
    setupSizeAndBoundaries();
    setupContentPane();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Add menu
    setJMenuBar(new Menubar());

    _leftHolder = makePanelHolder(Constants.MY_BROWN, INITIAL_OPENING_TITLE, Color.WHITE);
    // _rightHolder = makePanelHolder(Constants.MY_BROWN, INITIAL_IMAGE_TITLE, Color.WHITE);
    _rightHolder = makePanelHolder(Constants.MY_BROWN, "", Color.WHITE);

    _contentPane.add(_leftHolder, "cell 0 0, wmax 50%, grow");
    _contentPane.add(_rightHolder, "cell 1 0, wmax 50%, grow");
    _contentPane.setFocusable(true);
  }


  /**
   * Create a holder for the left or right side of the frame, with all cosmetics. Holders will have
   * same layout manager, size, border type, and runic font title is a title is supplied.
   * 
   * @param borderColor background Color for the border
   * @param title to be positioned top center in Runic font
   * @param backColor background Color for the panel
   * 
   * @return the JPanel that is assigned to the left or the right
   */
  private JPanel makePanelHolder(Color borderColor, String title, Color backColor)
  {
    JPanel holder = new JPanel(new MigLayout("insets 0", "[grow,fill]", "[grow,fill]"));
    Dimension holderSize = new Dimension(USERWIN_WIDTH / 2, USERWIN_HEIGHT);
    holder.setPreferredSize(holderSize);
    holder.setBackground(borderColor);

    Border matte = BorderFactory.createMatteBorder(5, 5, 5, 5, borderColor);
    Border titled = BorderFactory.createTitledBorder(matte, title,
        TitledBorder.CENTER, TitledBorder.TOP, Util.makeRunicFont(14f), Color.BLACK);
    holder.setBorder(titled);
    holder.setBackground(backColor);

    return holder;
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


  /** Apply the layout manager to the content pane */
  private void setupContentPane()
  {
    _contentPane = (JPanel) getContentPane();
    _contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(_contentPane);
    _contentPane.setLayout(new MigLayout("", "[grow, fill]10[grow]", "[grow]"));
  }


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


  /**
   * Prepare the HelpDialog and HelpKey event responder
   */
  private void prepareHelpDialog()
  {
    _helpdlg = HelpDialog.getInstance(this);
    _helpdlg.setMyFont(Util.makeRunicFont(14f));
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



  // ============================================================
  // Public Methods
  // ============================================================

  /**
   * Display the help text for this mainframe; implements {@code IHelpText}
   */
  public void showHelp()
  {
    _helpdlg.setVisible(true);
    _helpdlg.showHelp(_helpTitle, _helpText);
  }


  /**
   * Layout the IOPanel on the left: scrolling text window and working Comandline input area
   */
  public void addIOPanel()
  {
    _leftHolder.removeAll();
    _iop = new IOPanel();
    setTranscriptTitle(IOPANEL_TITLE);
    _leftHolder.add(_iop);
  }


  // ============================================================
  // Private Methods
  // ============================================================

  /**
   * Create the Adventure, Heroes, and Create-Hero buttons, and button panel for them
   */
  private void createButtons()
  {
    JButton adventureButton = createAdventureButton();
    JButton summonButton = createSummonHeroesButton();
    JButton creationButton = createHeroCreationButton();

    JPanel buttonPanel = new JPanel();
    // Align all buttons in a single column
    buttonPanel.setLayout(new MigLayout("wrap 1"));
    buttonPanel.setPreferredSize(new Dimension(
        (int) (USERWIN_WIDTH - FRAME_PADDING) / 2, USERWIN_HEIGHT - FRAME_PADDING));
    buttonPanel.setBackground(Constants.MY_BROWN.brighter());

    /** Buttons are at 25% to allow space for Command Line later */
    buttonPanel.add(adventureButton, "hmax 25%, grow");
    buttonPanel.add(summonButton, "hmax 25%, grow");
    buttonPanel.add(creationButton, "hmax 25%, grow");

    _leftHolder.add(buttonPanel);
  }


  /**
   * Create the behavior for selecting an adventure, which drives the frame update. <br>
   * Warning: Known bug with MigLayout in that {@code float} font sizes can cause overruns on
   * round-up calculations. "Choose your Adventure" overruns the button length, but
   * "Select your Adventure" does not, despite being the same number of characters!
   * 
   * @return the button created
   */
  private JButton createAdventureButton()
  {
    JButton button = createButtonWithTextAndIcon(ADV_IMAGE, "Select your Adventure ");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        List<String> adventures = _mfCiv.getAdventures();
        Object[] adventuresArr = adventures.toArray();
        Object selectedValue = JOptionPane.showInputDialog(
            Mainframe.this, "Select an Adventure", "Adventures",
            JOptionPane.INFORMATION_MESSAGE, null, adventuresArr, adventuresArr[0]);
        if (selectedValue != null) {
          // System.out.println("Adventure selected was: " + selectedValue);
          _mfCiv.loadSelectedAdventure(selectedValue.toString());
        }
      }
    });
    return button;
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



  private JButton createHeroCreationButton()
  {
    JButton button = createButtonWithTextAndIcon(REGISTRAR_IMAGE, "Create New Heroes");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0)
      {
        NewHeroDisplay nhd = null;
        try {
          nhd = new NewHeroDisplay();
        } catch (InstantiationException e) {
          e.printStackTrace();
          System.exit(0);
        }
        // changeToLeftPanel(nhd); // This my still be needed
      }
    });
    return button;
  }


  private JButton createSummonHeroesButton()
  {
    JButton button = createButtonWithTextAndIcon(HALL_IMAGE, "Summon Heroes");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        if (_partyHeros.size() == 0) {
          _summonableHeroes = _mfCiv.openDormitory();
          showPartyPickerWhenPartyEmpty();
        } else {
          showPartyPickerWhenMembersAlreadySelected();
        }
      }

      private void showPartyPickerWhenPartyEmpty()
      {
        padHeroes(_summonableHeroes);
        final ShuttleList slist = new ShuttleList(_summonableHeroes);
        setPropsForShuttleList(slist);
      }

      private void showPartyPickerWhenMembersAlreadySelected()
      {
        final ShuttleList slist = new ShuttleList(_summonableHeroes, _partyHeros);
        setPropsForShuttleList(slist);
      }

      private void setPropsForShuttleList(final ShuttleList slist)
      {
        slist.setTitle("Choose your Adventurers!");
        slist.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent arg0)
          {
            List<String> list = new ArrayList<String>();
            for (Object s : slist.getSelectedItems()) {
              list.add(s.toString());
            }
            setHeroList(list);
            slist.dispose();
          }
        });
        slist.setVisible(true);
      }

      private void padHeroes(List<String> list)
      {
        if (list.size() < 3) {
          list.add("Gronkhar the Smelly");
          list.add("Siobhan the Obsiquious");
          list.add("Sir Will-not-be-appearing-in-this-movie");
        }
      }
    });
    return button;
  }


  private JButton createButtonWithTextAndIcon(String imageFilePath, String buttonText)
  {
    JButton button = new JButton(buttonText);
    button.setBackground(Constants.MY_BROWN.brighter().brighter());

    // button.setFont(new Font("Tahoma", Font.PLAIN, 24));
    button.setFont(Util.makeRunicFont(14f));
    button.setIcon(new ImageIcon(Chronos.ADV_IMAGE_PATH + imageFilePath));
    button.setIconTextGap(40);
    return button;
  }


  // ============================================================
  // Deprecated Methods Temporarily
  // ============================================================

  /**
   * Retrieve the MainframeCiv
   */
  public MainframeCiv getMainframeCiv()
  {
    return _mfCiv;
  }


  // /**
  // * Create menu, mouse options, right panel image
  // */
  // private void createPanelsLayout()
  // {
  // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  // setJMenuBar(new Menubar());
  //
  // setImageToBeDisplayed(PORTAL_IMAGE);
  //
  // _rightHolder.addMouseListener(this);
  // _rightHolder.addMouseMotionListener(this);
  //
  // _buttonPanel = setupLeftPanel();
  // _leftPanel = _buttonPanel;
  //
  // createButtons();
  //
  // _leftHolder.add(_buttonPanel);
  // }



  // private JTextPane createHeroListTextArea()
  // {
  // JTextPane pane = new JTextPane();
  // pane.setText(HEROLIST_TEXT);
  // pane.setFont(new Font("Serif", Font.PLAIN, 24));
  // pane.setBackground(Color.WHITE);
  // return pane;
  // }

  // private void setHeroPartyText()
  // {
  // StringBuilder sb = new StringBuilder(HEROLIST_TEXT);
  // for (String s : _partyHeros) {
  // sb.append(s + ", ");
  // }
  // sb.deleteCharAt(sb.length() - 1);
  // sb.append("\n");
  // // _heroList.setText(sb.toString());
  // }


  // /**
  // * Create the Left Holder to contain the buttons, output panel, and command line
  // *
  // * @return the leftside panel
  // */
  // private JPanel createLeftPanel()
  // {
  // _leftHolder = new JPanel();
  // _buttonPanel = setupEmptyLeftPanel();
  // createButtons();
  // leftHolder.add(_buttonPanel);
  // return leftHolder;

  // JPanel leftHolder = new JPanel();
  // _buttonPanel = setupEmptyLeftPanel();
  // createButtons();
  // leftHolder.add(_buttonPanel);
  // return leftHolder;
  // }


  // /**
  // * Create the right Panel to contain the Images
  // *
  // * @return the right side panel
  // */
  // private JPanel createDisplayPanelr()
  // {
  // JPanel rightHolder = new JPanel();
  // rightHolder.addMouseListener(this);
  // rightHolder.addMouseMotionListener(this);
  // setImageToBeDisplayed(PORTAL_IMAGE);
  // return rightHolder;
  // }


  // /**
  // * Display the image and text of a Building or the Town
  // *
  // * @param description - the description to display in the output panel
  // * @param image - the image to display in the right-side image panel
  // */
  // public void displayTextAndImage(String description, String image)
  // {
  // displayText(description);
  // _imagePanel.setDisplay(image);
  // // _mfCiv.displayImage(image);
  // }


  /**
   * Display text onto the scrolling output panel
   * 
   * @param msg text to append to existing text in panel
   */
  public void displayText(String msg)
  {
    // _iop.appendText(msg);
    _iop.displayText(msg);
  }


  /**
   * Display an image in the Image panel
   * 
   * @param image to display on the rightside
   */
  public void setImage(Image image)
  {
    _imagePanel.setImage(image);
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
  public void setTranscriptTitle(String title)
  {
    TitledBorder border = (TitledBorder) _leftHolder.getBorder();
    border.setTitle(title);
  }


  public void drawBuilding(BuildingRectangle rect)
  {
    Graphics2D g = (Graphics2D) _rightHolder.getGraphics();
    rect.drawBuildingBox(g);
  }


  public void redraw()
  {
    validate();
    repaint();
  }


  public void setHeroList(List<String> list)
  {
    _partyHeros = list;
    // setHeroPartyText();
  }



  public void mouseClicked(MouseEvent e)
  {
    _mfCiv.handleClick(e.getPoint());
  }

  public void mouseEntered(MouseEvent e)
  {}

  public void mouseExited(MouseEvent e)
  {}

  public void mousePressed(MouseEvent e)
  {}

  public void mouseReleased(MouseEvent e)
  {}

  public void mouseDragged(MouseEvent e)
  {
    _mfCiv.handleMouseMovement(e.getPoint());
  }

  public void mouseMoved(MouseEvent e)
  {
    _mfCiv.handleMouseMovement(e.getPoint());
  }

  public Dimension getImagePanelSize()
  {
    return _rightHolder.getSize();
  }


  // ============================================================
  // Inner Mock for Testing
  // ============================================================

  /** Inner class for testing */
  public class MockMF
  {
    public MockMF()
    {}

    public boolean hasCiv()
    {
      return (_mfCiv != null) ? true : false;
    }

  } // end of MockMF inner class


} // end of Mainframe outer class
