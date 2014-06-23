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

import java.awt.Color;
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
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import mylib.hic.ShuttleList;
import net.miginfocom.swing.MigLayout;
import pdc.registry.AdvRegistryFactory;
import chronos.Chronos;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.MainframeCiv;
import dmc.PersonReadWriter;

public class Mainframe extends JFrame implements MouseListener, MouseMotionListener
{
  /** Reference to this singleton */
  static private Mainframe _mainframe = null;

  /** Reference to GUI Help text */
  static private String HELP_KEY = "INSTRUCTIONS";

  /** Number of pixels on empty border spacing */
  public static final int PAD = 10;
  private static final String HEROLIST_TEXT = "Heroes going on your quest: \n";

  private static final long serialVersionUID = -7749950528568777710L;


  private static final String REGISTRAR_IMAGE = "icn_Register.JPG";
  private static final String PORTAL_IMAGE = "ChronosLogo.JPG";
  private static final String HALL_IMAGE = "icn_HallOfHeroes.JPG";
  private static final String ADV_IMAGE = "icn_Town.JPG";

  public static int USERWIN_WIDTH;
  public static int USERWIN_HEIGHT;
  public static final int FRAME_PADDING = 90;

  private JPanel _contentPane;


  private JTextPane _heroList;
  private JPanel _buttonPanel;
  private JPanel _leftPanel;
  private MainframeCiv _advCiv;
  private InputPanel _inputPanel;
  private List<String> _partyHeros = new ArrayList<String>();
  private List<String> _summonableHeroes;

  private JPanel _leftHolder;
  private JPanel _rightHolder;


  /** Create Mainframe as singleton so other mainframes are not created concurrently.* */
  static public Mainframe getInstance()
  {
    if (_mainframe == null) {
      _mainframe = new Mainframe();
    }
    return _mainframe;
  }

  /**
   * Create the frame.
   */
  protected Mainframe()
  {
    forceDrawOfObjects();
    createCivs();
    createDisplayObjects();
  }

  private JButton createAdventureButton()
  {
    JButton button = createButtonWithTextAndIcon(ADV_IMAGE, "Choose your Adventure");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        List<String> adventures = _advCiv.getAdventures();
        Object[] adventuresArr = adventures.toArray();
        Object selectedValue =
            JOptionPane.showInputDialog(Mainframe.this, "Select an Adventure",
                "Adventures",
                JOptionPane.INFORMATION_MESSAGE, null, adventuresArr,
                adventuresArr[0]);

        if (selectedValue != null) {
          System.out.println("Adventure selected was " + selectedValue);
          _advCiv.loadAdventure(selectedValue.toString());
        }
      }
    });
    return button;
  }

  private void createButtons()
  {
    JButton summonButton = createSummonHeroesButton();
    JButton adventureButton = createAdventureButton();
    JButton creationButton = createHeroCreationButton();
    _heroList = createHeroListTextArea();

    _buttonPanel.add(adventureButton, "cell 0 0, hmax 25%,grow");
    _buttonPanel.add(summonButton, "cell 0 1, hmax 25%,grow");
    _buttonPanel.add(creationButton, "cell 0 2, hmax 25%, grow");
    _buttonPanel.add(_heroList, "cell 0 3,grow");
  }


  private JButton createButtonWithTextAndIcon(String imageFilePath, String buttonText)
  {
    JButton button = new JButton(buttonText);
    button.setFont(new Font("Tahoma", Font.PLAIN, 24));
    button.setIcon(new ImageIcon(Chronos.ADV_IMAGE_PATH + imageFilePath));
    button.setIconTextGap(40);
    return button;
  }

  protected void createCivs()
  {
    _advCiv =
        new MainframeCiv(this, new PersonReadWriter(),
            (AdventureRegistry) AdvRegistryFactory.getRegistry(RegKey.ADV));
  }

  protected void createDisplayObjects()
  {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setJMenuBar(new Menubar());

    setImageToBeDisplayed(PORTAL_IMAGE);

    _rightHolder.addMouseListener(this);
    _rightHolder.addMouseMotionListener(this);

    _buttonPanel = setupLeftPanel();
    _inputPanel = new InputPanel(_advCiv.getCmdParser());
    _leftPanel = _buttonPanel;

    createButtons();

    _leftHolder.add(_buttonPanel);
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
        changeToLeftPanel(nhd);
      }
    });
    return button;
  }

  private JTextPane createHeroListTextArea()
  {
    JTextPane pane = new JTextPane();
    pane.setText(HEROLIST_TEXT);
    pane.setFont(new Font("Serif", Font.PLAIN, 24));
    pane.setBackground(Color.WHITE);
    return pane;
  }

  private JButton createSummonHeroesButton()
  {
    JButton button = createButtonWithTextAndIcon(HALL_IMAGE, "Summon Heroes");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        if (_partyHeros.size() == 0) {
          _summonableHeroes = _advCiv.openDormitory();
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

  protected void forceDrawOfObjects()
  {
    setupSizeAndBoundaries();
    setupContentPane();

    _leftHolder = new JPanel(new MigLayout("insets 0", "[grow,fill]", "[grow,fill]"));
    _rightHolder = new JPanel(new MigLayout("insets 0", "[grow,fill]", "[grow,fill]"));
    _contentPane.add(_leftHolder, "cell 0 0, wmax 50%, grow");
    _contentPane.add(_rightHolder, "cell 1 0, wmax 50%, hmax 97%, grow");
    setVisible(true);
  }

  private void setHeroPartyText()
  {
    StringBuilder sb = new StringBuilder(HEROLIST_TEXT);
    for (String s : _partyHeros) {
      sb.append(s + ", ");
    }
    sb.deleteCharAt(sb.length() - 1);
    sb.append("\n");
    _heroList.setText(sb.toString());
  }

  private void setImageToBeDisplayed(String imagePath)
  {
    _rightHolder.removeAll();
    new ImagePanel(_rightHolder, imagePath);
  }

  private void setupContentPane()
  {
    _contentPane = (JPanel) getContentPane();
    _contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(_contentPane);
    _contentPane.setLayout(new MigLayout("", "[grow, fill]10[grow]", "[grow]"));
  }


  private JPanel setupLeftPanel()
  {
    JPanel panel = setupRightPanel();
    panel.setLayout(new MigLayout("", "[grow,fill]",
        "[0:0,grow 25,fill]10[0:0,grow 25,fill]10[0:0,grow 25,fill]10[0:0,grow 25,fill]"));
    return panel;
  }

  private JPanel setupRightPanel()
  {
    JPanel panel = new JPanel(new MigLayout("", "[grow,fill]", "[grow,fill]"));
    panel.setPreferredSize(new Dimension((int) (USERWIN_WIDTH - FRAME_PADDING) / 2,
        USERWIN_HEIGHT
            - FRAME_PADDING));
    return panel;
  }


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

  public void changeToLeftPanel(JPanel panel)
  {
    _leftHolder.remove(_leftPanel);
    _leftHolder.add(panel, "");
    _leftPanel = panel;
    redraw();
  }

  /**
   * Called by the AdvMainframeCiv to display appropriate building information
   * 
   * @param description - the description to display in the input panel
   * @param icon - the image to display in the right panel
   */
  public void displayTextAndImage(String description, String imagePath)
  {
    setImageToBeDisplayed(imagePath);

    changeToLeftPanel(_inputPanel);
    _inputPanel.setDescription(description);
  }

  public void displayText(String msg)
  {
    _inputPanel.appendText(msg);
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
    changeToLeftPanel(_buttonPanel);
    setImageToBeDisplayed(PORTAL_IMAGE);
  }

  public void setHeroList(List<String> list)
  {
    _partyHeros = list;
    setHeroPartyText();
  }

  public void mouseClicked(MouseEvent e)
  {
    _advCiv.handleClick(e.getPoint());
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
    _advCiv.handleMouseMovement(e.getPoint());
  }


  public void mouseMoved(MouseEvent e)
  {
    _advCiv.handleMouseMovement(e.getPoint());
  }

  public Dimension getImagePanelSize()
  {
    return _rightHolder.getSize();
  }
}
