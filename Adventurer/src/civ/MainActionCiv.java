/**
 * MainActionCiv.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import hic.ChronosPanel;
import hic.Mainframe;
import hic.MainframeInterface;
import hic.NewHeroIPPanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import mylib.Constants;
import mylib.hic.ShuttleList;
import net.miginfocom.swing.MigLayout;
import pdc.command.CommandFactory;
import chronos.Chronos;
import chronos.pdc.Adventure;
import chronos.pdc.Command.Scheduler;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

/**
 * The main civ behind the Mainframe screen. It creates the MainActionPanel consisting of the
 * primary buttons: Select an Adventure, Summon Heroes, and Create a New Hero
 * 
 * @author Alan Cline
 * @version Nov 7, 2015 // original <br>
 */
public class MainActionCiv extends BaseCiv
{
  private MainframeInterface _mf;
  private ChronosPanel _mainButtonPanel;

  private Adventure _adv;
  private AdventureRegistry _advReg;
  private BuildingDisplayCiv _bldgCiv;
  private MainframeCiv _mfCiv;
  private RegistryFactory _rf;

  /** Amount of space in pixels around the frame and image of aesthetics */
  public static final int FRAME_PADDING = 90;

  /** Title of the initial three-button panel on left side */
  private final String INITIAL_OPENING_TITLE = " Select Your Action ";

  // TODO: Why does these need to be static?
  /** Icons for the left-side buttons */
  private static final String REGISTRAR_IMAGE = "raw_Register.jpg";
  private static final String HALL_IMAGE = "icn_HallOfHeroes.jpg";
  private static final String ADV_IMAGE = "icn_Town.jpg";


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

    // Init the registries needed
    _advReg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
    BuildingRegistry bldgReg = (BuildingRegistry) _rf.getRegistry(RegKey.BLDG);
    _bldgCiv = new BuildingDisplayCiv(this, bldgReg);

    // Create the CommandLine support
    Scheduler skedder = new Scheduler(); // Skedder first for injection

    _rf = new RegistryFactory(skedder);
    _rf.initRegistries();
    
    CommandFactory cmdFactory = new CommandFactory(this, _mfCiv, _bldgCiv);
    cmdFactory.initMap();
    new CommandParser(skedder, cmdFactory);
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

  public BuildingDisplayCiv getBuildingDisplayCiv()
  {
    return _bldgCiv;
  }

  public RegistryFactory getRegistryFactory()
  {
    return _rf;
  }


  public ChronosPanel getActionPanel()
  {
    return _mainButtonPanel;
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


  // /** Return to town when icon clicked */
  // // public void handleClick(Point p)
  // public void returnToTown(Point p)
  // {
  // handleClickIfOnTownReturn(p);
  // if (_bldgCiv.isOnTown()) {
  // handleClickIfOnBuilding(p);
  // }
  // }


  // /** Define the building to APPROACH based on where the user clicked */
  // // public void handleMouseMovement(Point p)
  // public void setBuildingSelected(Point p)
  // {
  // if (_bldgCiv.isOnTown()) {
  // for (BuildingRectangle rect : _buildingList.values()) {
  // if (rect.contains(p)) {
  // _mf.setBuilding(rect);
  // break;
  // }
  // }
  // }
  // }

  /**
   * Load the selected adventure from the Adventure registry. Replace the opening button panel with
   * the IOPanel (text and command line)
   * 
   * @param adventureName selected from the Adventure by the user
   */
  public void loadSelectedAdventure(String adventureName)
  {
    // Get the selected adventure
    _adv = _advReg.getAdventure(adventureName);

    // BuildingCiv creates the IOPanel for all user input (commands) and messages (output)
    BuildingRegistry bldgReg = (BuildingRegistry) _rf.getRegistry(RegKey.BLDG);
    _bldgCiv = new BuildingDisplayCiv(this, bldgReg);
    _bldgCiv.openTown();
  }


  // public void msgOut(String msg)
  // {
  // _mf.displayText(msg);
  // }
  //

  // /** Creates the standard layout to display the town image and description */
  // public void openTown()
  // {
  // _bldgCiv.returnToTown();
  // Image townImage = Util.convertToImage(TOWN_IMAGE);
  // _mf.setImage(townImage);
  // if (_adv != null) {
  // String townTitle = " The Town of " + _adv.getTownName();
  // _mf.setImageTitle(townTitle);
  // _ioPanel.displayText(_adv.getOverview());
  // }
  // }


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

    _mainButtonPanel = new ChronosPanel(this);
    _mainButtonPanel.setTitle(INITIAL_OPENING_TITLE);
    // Align all buttons in a single column
    _mainButtonPanel.setLayout(new MigLayout("wrap 1"));
    Dimension frame = Mainframe.getWindowSize();
    _mainButtonPanel.setPreferredSize(new Dimension(
        (int) (frame.width - FRAME_PADDING) / 2, frame.height - FRAME_PADDING));
    _mainButtonPanel.setBackground(Constants.MY_BROWN);

    /** Buttons are at 25% to allow space for Command Line later */
    _mainButtonPanel.add(adventureButton, "hmax 25%, grow");
    _mainButtonPanel.add(summonButton, "hmax 25%, grow");
    _mainButtonPanel.add(creationButton, "hmax 25%, grow");

    return _mainButtonPanel;
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
        _advReg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
        List<Adventure> adventures = _advReg.getAdventureList();
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


  // /** Create the clickable areas on the town view to indicate a selected Building */
  // private void createBuildingBoxes()
  // {
  // for (int i = 0; i < DEFAULT_BUILDINGS.length; i++) {
  // String bName = DEFAULT_BUILDINGS[i][0];
  // BuildingRectangle r =
  // new BuildingRectangle(bName, colorArray[i], _mf.getImagePanelSize(), buildingLayouts[i]);
  // _buildingList.put(bName, r);
  // }
  // }

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


  // private void handleClickIfOnTownReturn(Point p)
  // {
  // if (_townReturn.contains(p)) {
  // openTown();
  // }
  // _mf.redraw();
  // }
  //
  //
  // private void handleClickIfOnBuilding(Point p)
  // {
  // for (Entry<String, BuildingRectangle> entry : _buildingList.entrySet()) {
  // BuildingRectangle rect = entry.getValue();
  // if (rect.contains(p)) {
  // enterBuilding(entry.getKey());
  // return;
  // }
  // }
  // }


  // ============================================================
  // Public methods:
  // ============================================================


} // end of MainframeCiv class
