/**
 * MainActionCiv.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import chronos.Chronos;
import chronos.pdc.Adventure;
import chronos.pdc.Command.Scheduler;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import hic.BuildingRectangle;
import hic.ChronosPanel;
import hic.IOPanel;
import hic.Mainframe;
import hic.MainframeInterface;
import hic.NewHeroIPPanel;
import mylib.Constants;
import mylib.hic.ShuttleList;
import net.miginfocom.swing.MigLayout;
import pdc.Util;
import pdc.command.CommandFactory;

/**
 * The main civ behind the Mainframe screen. It creates the MainActionPanel consisting of the
 * primary buttons: Select an Adventure, Summon Heroes, and Create a New Hero
 * 
 * @author Alan Cline
 * @version Nov 7, 2015 // original <br>
 */
public class MainActionCiv
{
  private MainframeInterface _mf;

  private Adventure _adv;
  private AdventureRegistry _advReg;
  private BuildingDisplayCiv _bldgCiv;
  private MainframeCiv _mfCiv;
  private RegistryFactory _rf;
  private Scheduler _skedder;
  private CommandParser _cp;

  /** Amount of space in pixels around the frame and image of aesthetics */
  public static final int FRAME_PADDING = 90;

  /** Title of the initial three-button panel on left side */
  private final String INITIAL_OPENING_TITLE = " Select Your Action ";

  // TODO: Why does these need to be static?
  /** Image of the Town containing the Buildings */
  private static final String TOWN_IMAGE = "ext_BiljurBaz.JPG";
  /** Icons for the left-side buttons */
  private static final String REGISTRAR_IMAGE = "raw_Register.jpg";
  private static final String HALL_IMAGE = "icn_HallOfHeroes.jpg";
  private static final String ADV_IMAGE = "icn_Town.jpg";


  /** Default Buildings to initialize registry with */
  public static final String[][] DEFAULT_BUILDINGS = {{"Ugly Ogre Inn", "Bork"},
      {"Rat's Pack", "Dewey N. Howe"}, {"The Bank", "Ogden Moneypenny"},
      {"Stadium", "Aragon"}, {"Arcaneum", "Pendergast"}, {"Monastery", "Balthazar"},
      {"Rogues' Den", "Ripper"}, {"Jail", "The Sheriff"}, {"Quasqueton", "Unknown"}};

  private Map<String, BuildingRectangle> _buildingList = new TreeMap<String, BuildingRectangle>();

  private float[][] buildingLayouts = new float[][] {
      {0.48f, 0.54f, 0.14f, 0.08f}, // Ugly Ogre Inn
      {0.79f, 0.43f, 0.14f, 0.08f}, // Rat's Pack General Store
      {0.60f, 0.45f, 0.07f, 0.07f}, // The Bank
      {0.5f, 0.37f, 0.25f, 0.09f}, // Stadium
      {0.61f, 0.73f, 0.37f, 0.20f}, // Arcaneum
      {0.0f, 0.35f, 0.22f, 0.13f}, // Monastery
      {0.63f, 0.53f, 0.10f, 0.05f}, // Rouge's Den
      {0.38f, 0.53f, 0.08f, 0.07f}, // Jail
      {0.31f, 0.08f, 0.5f, 0.25f}}; // Quasqueton

  private Color[] colorArray = new Color[] {Color.white, // Ugly Ogre Inn
      Color.white, // Rat's Pack General Store
      Color.white, // The Bank
      Color.white, // Stadium
      Color.white, // Arcaneum
      Color.white, // Monastery
      Color.white, // Rouge's Den
      Color.white, // Jail
      Color.white}; // Quasqueston

  /** Current Building being displayed, and can be entered */
  private final Rectangle _townReturn = new Rectangle(0, 0, 100, 100);


  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /**
   * Create the Civ associated with the mainframe
   * 
   * @param frame owner of the widget for which this civ applies
   * @param personRW supports the Summon Hero and Create Hero buttons
   * @param advReg registry to support the Adventures button
   */
  public MainActionCiv(MainframeInterface mf, MainframeCiv mfciv)
  {
    _mf = mf;
    _mfCiv = mfciv;
    createMembers();
  }


  protected void createMembers()
  {
    // Create the panel with the main buttons: Load Adventure, Summons, and Create Hero
    _mf.replaceLeftPanel(createActionPanel());

    createBuildingBoxes(); // this probably needs to be elsewhere

    // Init the registries needed
    _advReg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
    BuildingRegistry bldgReg = (BuildingRegistry) _rf.getRegistry(RegKey.BLDG);
    _bldgCiv = new BuildingDisplayCiv(_mf, bldgReg);

    // Create the CommandLine support
    _skedder = new Scheduler(); // Skedder first for injection

    _rf = new RegistryFactory(_skedder);
    _rf.initRegistries();
    
    CommandFactory cmdFactory = new CommandFactory(this, _mfCiv, _bldgCiv);
    _cp = new CommandParser(_skedder, cmdFactory);
    cmdFactory.initMap();
  }


  // ============================================================
  // Public methods:
  // ============================================================

  // TODO This is the responsibility of the BuildingDisplayCiv
  /**
   * Enter the Building specified. If the Hero is at the Town level, get the
   * {@code BuildingRegistry} and {@ocde BuildingCiv}
   *
   * @param bldName the name of the building to open
   */
  public void enterBuilding(String bldName)
  {
    if (_bldgCiv.canApproach(bldName)) {
      _bldgCiv.approachBuilding(bldName);
    }
  }


  /**
   * Retrieves the Adventures for selection from the Adventure Registry
   * 
   * @return the list of Adventures
   */
  public ArrayList<String> getAdventures()
  {
    ArrayList<Adventure> adventures = _advReg.getAdventureList();
    ArrayList<String> results = new ArrayList<String>();
    for (Adventure a : adventures) {
      results.add(a.getKey());
    }
    return results;
  }
  
  public CommandParser getCmdParser()
  {
    return _cp;
  }

  /**
   * Get the town name
   * 
   * @return the name of the town
   */
  public RegistryFactory getRegistryFactory()
  {
    return _rf;
  }

  /**
   * Get the town name
   * 
   * @return the name of the town
   */
  public String getTownName()
  {
    return _adv.getTownName();
  }


  /** Return to town when icon clicked */
  // public void handleClick(Point p)
  public void returnToTown(Point p)
  {
    handleClickIfOnTownReturn(p);
    if (_bldgCiv.isOnTown()) {
      handleClickIfOnBuilding(p);
    }
  }


  /** Define the building to APPROACH based on where the user clicked */
  // public void handleMouseMovement(Point p)
  public void setBuildingSelected(Point p)
  {
    if (_bldgCiv.isOnTown()) {
      for (BuildingRectangle rect : _buildingList.values()) {
        if (rect.contains(p)) {
          _mf.setBuilding(rect);
          break;
        }
      }
    }
  }

  /**
   * Load the selected adventure from the Adventure registry. Replace the opening button panel with
   * the IOPanel (text and command line)
   * 
   * @param adventureName selected from the Adventure by the user
   */
  public void loadSelectedAdventure(String adventureName)
  {
    _adv = _advReg.getAdventure(adventureName);
    _mf.replaceLeftPanel(new IOPanel(this));
    openTown();
  }

  // /**
  // * Create a font that pervades the HIC
  // *
  // * @param fontHt size of runic font to create
  // * @return the font
  // */
  // public Font makeRunicFont(float fontHt)
  // {
  // return Util.makeRunicFont(14f);
  // }

  public void msgOut(String msg)
  {
    _mf.displayText(msg);
  }


  // TODO: Move this to FormitoryCiv
  // /**
  // * Find a list of heroes that can be summoned
  // *
  // * @return list of the heroes
  // */
  // public List<String> openDormitory()
  // {
  // List<String> heroes = _personRW.wakePeople();
  // return heroes;
  // }


  /** Creates the standard layout to display the town image and description */
  public void openTown()
  {
    _bldgCiv.returnToTown();
    Image townImage = Util.convertToImage(TOWN_IMAGE);
    _mf.setImage(townImage);
    if (_adv != null) {
      String townTitle = " The Town of " + _adv.getTownName();
      _mf.setImageTitle(townTitle);
      _mf.displayText(_adv.getOverview());
    }
  }


  /** Close down the application if user so specified */
  public void quit()
  {
    if (_mf.displayPrompt("Quit Adventurer?") == true) {
      Adventurer.approvedQuit();
    }
  }


  // ============================================================
  // Private methods
  // ============================================================

  /**
   * Create the Adventure, Heroes, and Create-Hero buttons, and button panel for them
   */
  private ChronosPanel createActionPanel()
  {
    JButton adventureButton = createAdventureButton();
    JButton summonButton = createSummonHeroesButton();
    JButton creationButton = createNewHeroButton();

    ChronosPanel buttonPanel = new ChronosPanel();
    buttonPanel.setTitle(INITIAL_OPENING_TITLE );
    // Align all buttons in a single column
    buttonPanel.setLayout(new MigLayout("wrap 1"));
    Dimension frame = Mainframe.getWindowSize();
    buttonPanel.setPreferredSize(new Dimension(
        (int) (frame.width - FRAME_PADDING) / 2, frame.height - FRAME_PADDING));
    buttonPanel.setBackground(Constants.MY_BROWN);

    /** Buttons are at 25% to allow space for Command Line later */
    buttonPanel.add(adventureButton, "hmax 25%, grow");
    buttonPanel.add(summonButton, "hmax 25%, grow");
    buttonPanel.add(creationButton, "hmax 25%, grow");
    
    return buttonPanel;
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
        ArrayList<String> adventures = _mfCiv.getAdventures();
        Object[] adventuresArr = adventures.toArray();
        Object selectedValue =
            JOptionPane.showInputDialog(null, "Select an Adventure", "Adventures",
                JOptionPane.INFORMATION_MESSAGE, null, adventuresArr, adventuresArr[0]);
        if (selectedValue != null) {
          loadSelectedAdventure(selectedValue.toString());
        }
      }
    });
    return button;
  }


  // ============================================================
  // Public methods:
  // ============================================================
  
  private JButton createButtonWithTextAndIcon(String imageFilePath, String buttonText)
  {
    JButton button = new JButton(buttonText);
    button.setBackground(Constants.MY_BROWN);
  
    button.setFont(Chronos.STANDARD_FONT);
    button.setIcon(new ImageIcon(Chronos.ADV_IMAGE_PATH + imageFilePath));
    button.setIconTextGap(40);
    return button;
  }


  /** Create the clickable areas on the town view to indicate a selected Building */
  private void createBuildingBoxes()
  {
    for (int i = 0; i < DEFAULT_BUILDINGS.length; i++) {
      String bName = DEFAULT_BUILDINGS[i][0];
      BuildingRectangle r =
          new BuildingRectangle(bName, colorArray[i], _mf.getImagePanelSize(), buildingLayouts[i]);
      _buildingList.put(bName, r);
    }
  }

  // ============================================================
  // Public methods:
  // ============================================================

  /**
   * Create the button to call the NewHeroCiv, which will control the NewHeroIOPanel that collects
   * the new Hero data, and calls HeroDisplayCiv that displays the Hero's stats panel
   *
   * @return the button
   */
  private JButton createNewHeroButton()
  {
    JButton button = createButtonWithTextAndIcon(REGISTRAR_IMAGE, "Create New Heroes");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0)
      {
        try {
          NewHeroCiv nhCiv = new NewHeroCiv(_mf, _rf);
          NewHeroIPPanel ipPanel = nhCiv.createNewHeroPanel();
          _mf.replaceLeftPanel(ipPanel);
          // Can set focus on default field (nameField) only after it is displayed
          ipPanel.setDefaultFocus();
        } catch (Exception e) {
          e.printStackTrace();
          System.exit(0);
        }
      }
    });
    return button;
  }


  // ============================================================
  // Public methods:
  // ============================================================

  /* This button code is followed by a series of inner methods */
  private JButton createSummonHeroesButton()
  {
    JButton button = createButtonWithTextAndIcon(HALL_IMAGE, "Summon Heroes");
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        // if (_partyHeros.size() == 0) {
        // // _summonableHeroes = _mfCiv.openDormitory();
        // showPartyPickerWhenPartyEmpty();
        // } else {
        // showPartyPickerWhenMembersAlreadySelected();
        // }
      }

      private void showPartyPickerWhenPartyEmpty()
      {
        // padHeroes(_summonableHeroes);
        // final ShuttleList slist = new ShuttleList(_summonableHeroes);
        // setPropsForShuttleList(slist);
      }

      private void showPartyPickerWhenMembersAlreadySelected()
      {
        // final ShuttleList slist = new ShuttleList(_summonableHeroes, _partyHeros);
        // setPropsForShuttleList(slist);
      }

      private void setPropsForShuttleList(final ShuttleList slist)
      {
        // slist.setTitle("Choose your Adventurers!");
        // slist.addActionListener(new ActionListener() {
        // public void actionPerformed(ActionEvent arg0)
        // {
        // List<String> list = new ArrayList<String>();
        // for (Object s : slist.getSelectedItems()) {
        // list.add(s.toString());
        // }
        // setHeroList(list);
        // slist.dispose();
        // }
        // });
        // slist.setVisible(true);
      }

      private void padHeroes(List<String> list)
      {
        // if (list.size() < 3) {
        // list.add("Gronkhar the Smelly");
        // list.add("Siobhan the Obsiquious");
        // list.add("Sir Will-not-be-appearing-in-this-movie");
        // }
      }
    });
    return button;
  }


  private void handleClickIfOnTownReturn(Point p)
  {
    if (_townReturn.contains(p)) {
      openTown();
    }
    _mf.redraw();
  }


  private void handleClickIfOnBuilding(Point p)
  {
    for (Entry<String, BuildingRectangle> entry : _buildingList.entrySet()) {
      BuildingRectangle rect = entry.getValue();
      if (rect.contains(p)) {
        enterBuilding(entry.getKey());
        return;
      }
    }
  }


  // ============================================================
  // Public methods:
  // ============================================================


} // end of MainframeCiv class
