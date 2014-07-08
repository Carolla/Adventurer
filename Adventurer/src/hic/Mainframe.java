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
 * @version 2.0 // rebuilt to comply with architectural model (lost history)
 */
// Mainframe serialization unnecessary
@SuppressWarnings("serial")
public class Mainframe extends JFrame implements MouseListener, MouseMotionListener
{
  /** Reference to this singleton */
  static private Mainframe _mainframe = null;

  /** Reference to GUI Help text */
  static private final String HELP_KEY = "INSTRUCTIONS";

  /** Number of pixels on empty border spacing */
  public static final int PAD = 10;
  // private static final String HEROLIST_TEXT = "Heroes going on your quest: \n";

  /** Icons for the left-side buttons */
  private static final String REGISTRAR_IMAGE = "icn_Register.JPG";
  private static final String HALL_IMAGE = "icn_HallOfHeroes.JPG";
  private static final String ADV_IMAGE = "icn_Town.JPG";
  /** Initial right-side image: Chronos logo */
  private static final String INITIAL_IMAGE = "ChronosLogo.JPG";

  public static int USERWIN_WIDTH;
  public static int USERWIN_HEIGHT;
  public static final int FRAME_PADDING = 90;

  /** Common content pane for the Mainframe */
  private JPanel _contentPane;
  /** Empty left-side panel holder for initial and standard panels. */
  private JPanel _leftHolder;
  /** Empty right-side panel holder for initial standard panels. */
  private JPanel _rightHolder;

  // private JTextPane _heroList;
  // private JPanel _buttonPanel;
  // private JPanel _leftPanel;
  private MainframeCiv _mfCiv;
  private InputPanel _cmdLine;
  private List<String> _partyHeros = new ArrayList<String>();
  private List<String> _summonableHeroes;

  /**
   * Create Mainframe as singleton so other mainframes are not created concurrently.*
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
   */
  private Mainframe()
  {
    createFrameLayout();
    addInitialLeftPanel();
    addImagePanel();
    displayImage(INITIAL_IMAGE);

    // Create the Civ
    _mfCiv = new MainframeCiv(this);

    // TODO CommandLine is not part of initial layout
    // Create the command line widget, part of the left panel
    // createCommandLine();
  }

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
    _contentPane.add(_rightHolder, "cell 1 0, wmax 50%, hmax 97%, grow");
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
   * Create initial left panel with buttons and add it to the left holder
   */
  private void addInitialLeftPanel()
  {
    JPanel buttonPanel = createButtons();
    _leftHolder.add(buttonPanel);
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

  /** Create the behavior for selecting an adventure, which drives the frame update
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
          _mfCiv.loadAdventure(selectedValue.toString());
        }
      }
    });
    return button;
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
    _rightHolder.addMouseListener(this);
    _rightHolder.addMouseMotionListener(this);
    return panel;
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

  /**
   * ======================================================================================
   * SECONDARY CODE BELOW THIS LINE
   * ======================================================================================
   */
  protected void CODE_DIVIDER()
  {}



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


  /**
   * Create the CommandLine input area
   */
  private void createCommandLine()
  {
    _cmdLine = new InputPanel(_mfCiv.getCmdParser());
  }



  // /**
  // * Create menu, mouse options, right panel image
  // */
  // protected void createDisplayObjects()
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
  // _inputPanel = new InputPanel(_advCiv.getCmdParser());
  // _leftPanel = _buttonPanel;
  //
  // createButtons();
  //
  // _leftHolder.add(_buttonPanel);
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



  // /**
  // * Create menu, mouse options, right panel image
  // */
  // protected void createDisplayObjects()
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
  // _inputPanel = new InputPanel(_advCiv.getCmdParser());
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
        // TODO Hold for later
        // changeToLeftPanel(nhd);
      }
    });
    return button;
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



  // /**
  // * Create menu, mouse options, right panel image
  // */
  // protected void createDisplayObjects()
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
  // _inputPanel = new InputPanel(_advCiv.getCmdParser());
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



  /**
   * Create the mainframe civ, PersonReadWriter for Heroes, and the AdventureRegistry
   */
  private void createMFCiv()
  {
//    AdventureRegistry advreg = (AdventureRegistry) RegistryFactory.getRegistry(RegKey.ADV);
//    _mfCiv = new MainframeCiv(this, new PersonReadWriter(), advreg);
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



  // private JPanel setupLeftPanel()
  // {
  // JPanel panel = setupRightPanel();
  // panel.setLayout(new MigLayout("", "[grow,fill]",
  // "[0:0,grow 25,fill]10[0:0,grow 25,fill]10[0:0,grow 25,fill]10[0:0,grow 25,fill]"));
  // return panel;
  // }



  public void changeToLeftPanel(JPanel panel)
  {
    // TODO Hold for later 21040704
    // _leftHolder.remove(_leftPanel);
    // _leftHolder.add(panel, "");
    // _leftPanel = panel;
    // redraw();
  }


  /************************************************************************************
   * PUBLIC METHODS
   ************************************************************************************/

  /**
   * Called by the AdvMainframeCiv to display appropriate building information
   * 
   * @param description - the description to display in the input panel
   * @param icon - the image to display in the right panel
   */
  public void displayTextAndImage(String description, String imagePath)
  {
    // TODO Hold for later 21040704
//    setImageToBeDisplayed(imagePath);
    displayImage(imagePath);
     changeToLeftPanel(_cmdLine);
    _cmdLine.setDescription(description);
  }

  public void displayText(String msg)
  {
    _cmdLine.appendText(msg);
  }

  public void drawBuilding(BuildingRectangle rect)
  {
    Graphics2D g = (Graphics2D) _rightHolder.getGraphics();
    rect.drawBuildingBox(g);
  }

  public String getHelpID()
  {
    return HELP_KEY;
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


} // end of Mainframe outer class
