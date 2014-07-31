/**
 * Mainframe.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package hic;

import hic.screenConfiguration.ImagePanel;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import mylib.hic.HelpDialog;
import mylib.hic.ShuttleList;
import net.miginfocom.swing.MigLayout;
import chronos.Chronos;
import civ.MainframeCiv;

/**
 * Singleton frame to hold the menu bar and two panels in one of two states: <br>
 * State 1: Before an Adventure is selected, left panel shows the button panel on the left, and the
 * Chronos logo on the right. <br>
 * State 2: After an Adventure is selected, left panel shows an output scrolling-text panel
 * (initially the selected Town description), and the input command line; and on the right, an image
 * display panel (initially showing the Town image with clickable buildings).
 * 
 * @author Al Cline
 * @version Jul 21, 2014 // rebuilt to comply with architectural model (lost history) <br>
 *          Jul 28, 2014 // added HelpKeyListener for Help functionality
 */
// Mainframe serialization unnecessary
@SuppressWarnings("serial")
public class Mainframe extends JFrame implements MouseListener, MouseMotionListener
{
  /** Reference to this singleton */
  static private Mainframe _mainframe = null;

  /** Singleton Help Dialog for all help text */
  private HelpDialog _helpdlg;
//  /** Reference to GUI Help text */
//  private final String HELP_KEY = "INSTRUCTIONS";

  /** Number of pixels on empty border spacing */
  public static final int PAD = 10;

  /** Icons for the left-side buttons */
  private static final String REGISTRAR_IMAGE = "icn_Register.jpg";
  private static final String HALL_IMAGE = "icn_HallOfHeroes.jpg";
  private static final String ADV_IMAGE = "icn_Town.jpg";
  /** Initial right-side image: Chronos logo */
  private static final String INITIAL_IMAGE = "ChronosLogo.jpg";

  /** Width of the platform user's window frame */
  public static int USERWIN_WIDTH;
  /** Height of the platform user's window frame */
  public static int USERWIN_HEIGHT;
  /** Amount of space in pixels around the frame and image of aesthetics */
  public static final int FRAME_PADDING = 90;

  /** Common content pane for the Mainframe */
  private JPanel _contentPane;
  /** Empty left-side panel holder for initial and standard panels. */
  private JPanel _leftHolder;
  /** Empty right-side panel holder for initial standard panels. */
  private JPanel _rightHolder;

  private MainframeCiv _mfCiv;
  private IOPanel _iop;
  private List<String> _partyHeros = new ArrayList<String>();
  private List<String> _summonableHeroes;
  
  /** Help Text for the mainframe */
  static final String _helpText = "Create a new Hero and summon an existing one from the Dormitory.\n "
      + "Select and Adventure to explore and visit Buildings to prepare before questing.\n"
      + "Gain experience points (XP) to get more power and fame. \n"
      + "Kill monsters, solve puzzles, and find treasure to gain more XP.\n";

  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /**
   * Creates the initial frame layout: left and right panel holders with buttons, and image panel
   * showing chronos logo on right.
   */
  private Mainframe()
  {
    createFrameLayout();
    add(new InitialLayout());

    // Create the Civ
    _mfCiv = new MainframeCiv(this);
    
    // Create the one time help dialog
    prepareHelpDialog();
  }

  
  private void prepareHelpDialog()
  {
    _helpdlg = HelpDialog.getInstance(this);
    // for testing
    _contentPane.addKeyListener(new KeyListener() {
      public void keyReleased(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_F1) {
          _helpdlg.setVisible(true);
          showHelpText();
        }
      }
      // Empty methods required to be overridden
      public void keyPressed(KeyEvent e) {}
      public void keyTyped(KeyEvent e) {}
    });
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


  // ============================================================
  // Public Methods
  // ============================================================

  /**
   * Creates the standard layout for displaying town an building images and descriptions
   */
  public void createStandardLayout()
  {
    _leftHolder.removeAll();
    add(new StandardLayout());
    setVisible(true);
  }

  // ============================================================
  // Private Methods
  // ============================================================

  /**
   * Creates mainframe layout, menu, and adds left and right panel holders
   */
  private void createFrameLayout()
  {
    setupSizeAndBoundaries();
    setupContentPane();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Add menu
    setJMenuBar(new Menubar());

    // Add left and right holders
    _leftHolder = new JPanel(new MigLayout("insets 0", "[grow,fill]", "[grow,fill]"));
    _rightHolder = new JPanel(new MigLayout("insets 0", "[grow,fill]", "[grow,fill]"));
    _contentPane.add(_leftHolder, "cell 0 0, wmax 50%, grow");
    _contentPane.add(_rightHolder, "cell 1 0, wmax 50%, hmax 90%, grow");
    _contentPane.setFocusable(true);
    setVisible(true);
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
   * Layout the image panel on the right
   * 
   * @return the image panel with no image
   */
  private JPanel addImagePanel()
  {
    JPanel panel = new JPanel(new MigLayout("", "[grow,fill]", "[grow,fill]"));
    panel.setPreferredSize(new Dimension(
        (int) (USERWIN_WIDTH - FRAME_PADDING) / 2, USERWIN_HEIGHT - FRAME_PADDING));
    _rightHolder.addMouseListener(Mainframe.this);
    _rightHolder.addMouseMotionListener(Mainframe.this);
    return panel;
  }


  /**
   * Create the Adventure, Heroes, and Create-Hero buttons, and button panel for them
   * 
   * @return buttonpanel containing the initial buttons
   */
  private JPanel createButtons()
  {
    JButton adventureButton = createAdventureButton();
    JButton summonButton = createSummonHeroesButton();
    JButton creationButton = createHeroCreationButton();

    JPanel buttonPanel = new JPanel();
    // Align all buttons in a single column
    buttonPanel.setLayout(new MigLayout("wrap 1"));
    buttonPanel.setPreferredSize(new Dimension(
        (int) (USERWIN_WIDTH - FRAME_PADDING) / 2, USERWIN_HEIGHT - FRAME_PADDING));

    /** Buttons are at 25% to allow space for Command Line later */
    buttonPanel.add(adventureButton, "hmax 25%, grow");
    buttonPanel.add(summonButton, "hmax 25%, grow");
    buttonPanel.add(creationButton, "hmax 25%, grow");
    return buttonPanel;
  }

  /**
   * Create the behavior for selecting an adventure, which drives the frame update
   * 
   * @return the button created
   */
  private JButton createAdventureButton()
  {
    JButton button = createButtonWithTextAndIcon(ADV_IMAGE, "Choose your Adventure");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        List<String> adventures = _mfCiv.getAdventures();
        Object[] adventuresArr = adventures.toArray();
        Object selectedValue = JOptionPane.showInputDialog(
            Mainframe.this, "Select an Adventure", "Adventures",
            JOptionPane.INFORMATION_MESSAGE, null, adventuresArr, adventuresArr[0]);
        if (selectedValue != null) {
          System.out.println("Adventure selected was: " + selectedValue);
          _mfCiv.loadSelectedAdventure(selectedValue.toString());
        }
      }
    });
    return button;
  }



  /**
   * Display the image on the right side
   * 
   * @param imagePath what to display
   */
  private void displayImage(String imagePath)
  {
    _rightHolder.removeAll();
    new ImagePanel(_rightHolder, imagePath);
  }



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
    button.setFont(new Font("Tahoma", Font.PLAIN, 24));
    button.setIcon(new ImageIcon(Chronos.ADV_IMAGE_PATH + imageFilePath));
    button.setIconTextGap(40);
    return button;
  }

  /*------------------------------------------------------------------------
   * Temporary SECONDARY CODE BELOW THIS LINE while refactoring
   *------------------------------------------------------------------------*/

  /** Temporary marker because there are too many methods to read clearly otherwise */
  protected void CODE_DIVIDER()
  {}


  // /**
  // * Create the CommandLine input area
  // */
  // private void createCommandLine()
  // {
  // _cmdLine = new InputPanel(_mfCiv.getCmdParser());
  // }


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


  private JButton createHeroCreationButton()
  {
    JButton button = createButtonWithTextAndIcon(REGISTRAR_IMAGE,
        "Create New Heroes");
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
        // changeToLeftPanel(nhd);
      }
    });
    return button;
  }


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



  // private JPanel setupLeftPanel()
  // {
  // JPanel panel = setupRightPanel();
  // panel.setLayout(new MigLayout("", "[grow,fill]",
  // "[0:0,grow 25,fill]10[0:0,grow 25,fill]10[0:0,grow 25,fill]10[0:0,grow 25,fill]"));
  // return panel;
  // }


  // public void changeToLeftPanel(JPanel panel)
  // {
  // _leftHolder.remove(_leftPanel);
  // _leftHolder.add(panel, "");
  // _leftPanel = panel;
  // redraw();
  // }

  /**
   * Display the image and text of a Building or the Town
   * 
   * @param description - the description to display in the output panel
   * @param image - the image to display in the right-side image panel
   */
  public void displayTextAndImage(String description, String image)
  {
    displayText(description);
    displayImage(image);
    // changeToLeftPanel(_cmdLine);
    // _cmdLine.setDescription(description);
  }

  /**
   * Display text onto the scrolling output panel
   * 
   * @param msg text to append to existing text in panel
   */
  public void displayText(String msg)
  {
    _iop.appendText(msg);
  }


  public void drawBuilding(BuildingRectangle rect)
  {
    Graphics2D g = (Graphics2D) _rightHolder.getGraphics();
    rect.drawBuildingBox(g);
  }

//  public String getHelpID()
//  {
//    return HELP_KEY;
//  }

  public void showHelpText()
  {
    _helpdlg.showHelp(_helpText);
  }
  
  
  public void redraw()
  {
    validate();
    repaint();
  }

  public void resetPanels()
  {
    // TODO Hold for later 21040704
    // changeToLeftPanel(_buttonPanel);
    // setImageToBeDisplayed(PORTAL_IMAGE);
  }

  public void setHeroList(List<String> list)
  {
    _partyHeros = list;
    // setHeroPartyText();
  }


  // /**
  // * Checks for Help Key to display Help text
  // *
  // * @param e any key released
  // */
  // public void keyReleased(KeyEvent e)
  // {
  // System.out.println("keyReleased(): Extended key code received = " + e.getExtendedKeyCode());
  // if (e.getKeyCode() == KeyEvent.VK_F1) {
  // System.out.println("F1 Help key pressed");
  // }
  // }
  //
  // /** Required implementation for KeyListener, does nothing */
  // public void keyPressed(KeyEvent e)
  // {
  // System.out.println("keyPressed(): Extended key code received = " + e.getExtendedKeyCode());
  // if (e.getKeyCode() == KeyEvent.VK_F1) {
  // System.out.println("F1 Help key pressed");
  // }
  // }
  //
  // /** Required implementation for KeyListener, does nothing */
  // public void keyTyped(KeyEvent e)
  // {
  // System.out.println("keyTyped(): Extended key code received = " + e.getExtendedKeyCode());
  // if (e.getKeyCode() == KeyEvent.VK_F1) {
  // System.out.println("F1 Help key pressed");
  // }
  // }

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


  /*-------------------------------------------------
   * INNER CLASS: InitialLayout 
   *-------------------------------------------------*/

  /**
   * Inner class to hold the initial mainframe widgets before an Adventure is selected: the buttons
   * on the left side, and the chronos logo on the right.
   */
  public class InitialLayout extends JPanel
  {
    /**
     * Create the layout for the mainframe at start up time
     */
    public InitialLayout()
    {
      addInitialLeftPanel();
      addImagePanel();
      displayImage(INITIAL_IMAGE);
    }

    /**
     * Create initial left panel with buttons and add it to the left holder
     */
    private void addInitialLeftPanel()
    {
      JPanel buttonPanel = createButtons();
      _leftHolder.add(buttonPanel);
    }

  } // end of InitialLayout inner class


  /*-------------------------------------------------
   * INNER CLASS: StandardLayout 
   *-------------------------------------------------*/

  /**
   * Inner class to hold the secondary mainframe widgets after an Adventure is selected: output
   * description panel and command line widget on the left; town or building image on the right.
   * Replace the buttons with the output descriptive pane and the command line; replace the image
   * panel with a picture of the Town
   */
  public class StandardLayout extends JPanel
  {
    public StandardLayout()
    {
      Mainframe.this._iop = new IOPanel();
      _leftHolder.add(_iop);
      addImagePanel();
      redraw();
    }


  } // end of StandardLayout inner class


  // ============================================================
  // Inner Mock for Testing
  // ============================================================
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
