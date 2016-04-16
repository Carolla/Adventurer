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
import hic.IOPanel;
import hic.Mainframe;
import hic.NewHeroIPPanel;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import mylib.Constants;
import net.miginfocom.swing.MigLayout;
import pdc.command.CommandFactory;
import chronos.pdc.Adventure;
import chronos.pdc.Chronos;
import chronos.pdc.buildings.Inn;
import chronos.pdc.character.Hero;
import chronos.pdc.command.Scheduler;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.HeroRegistry;
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
  private AdventureRegistry _advReg;
  private HeroRegistry _dorm;
  private MainframeCiv _mfCiv;
  private HeroDisplayCiv _hdCiv;
  private RegistryFactory _rf;
  private Scheduler _skedder;

  /** Amount of space in pixels around the frame and image of aesthetics */
  public static final int FRAME_PADDING = 90;

  /** Title of the initial three-button panel on left side */
  private final String INITIAL_OPENING_TITLE = " Select Your Action ";

  /** Title of the three buttons */
  private final String LOAD_ADVENTURE_TITLE = " Select Your Adventure ";
  private final String SUMMON_HERO_TITLE = " Summon Heroes ";
  private final String CREATE_HERO_TITLE = " Create a New Hero ";

  private final String REGISTRAR_IMAGE = "raw_Register.jpg";
  private final String HALL_IMAGE = "icn_HallOfHeroes.jpg";
  private final String ADV_IMAGE = "icn_Town.jpg";

  private ChronosPanel _actionPanel;
  private JButton _summonButton;

  // Button ToolTips
  private static final String SUMMON_DISABLED_HOVER = "Create heroes to enable summoning";
  private static final String SUMMON_ENABLED_HOVER = " Heroes waiting to be summoned";
  private static final String SMN_ENBLD_SINGL_HOVER = " Hero waiting to be summoned";

  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /**
   * Create the Civ to display and handle the MainActionPanel of buttons
   * 
   * @param mfCiv handler for the mainframe
   */
  public MainActionCiv(MainframeCiv mfciv)
  {
    _mfCiv = mfciv;
    _hdCiv = new HeroDisplayCiv(_mfCiv);
    constructCoreMembers();
    _mfCiv.replaceLeftPanel(createActionPanel());
  }

  public void constructCoreMembers()
  {
    _skedder = new Scheduler(_mfCiv); // Skedder first for injection

    _rf = new RegistryFactory();
    _rf.initRegistries(_skedder);

    ((Inn) ((BuildingRegistry) _rf.getRegistry(RegKey.BLDG)).getBuilding("Ugly Ogre Inn"))
        .initPatrons(_skedder);
    _advReg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
    _dorm = (HeroRegistry) _rf.getRegistry(RegKey.HERO);
  }

  // ============================================================
  // Public methods
  // ============================================================

  private void createHero()
  {
    NewHeroCiv nhCiv = new NewHeroCiv(_mfCiv, _dorm);
    NewHeroIPPanel ipPanel = new NewHeroIPPanel(nhCiv, _hdCiv);
    _mfCiv.replaceLeftPanel(ipPanel);
    ipPanel.setDefaultFocus(); // only works after panel is displayed
    toggleSummonEnabled();
  }

  /**
   * Sets Summon Heroes button to enabled or disabled based on the presence of saved heroes in the
   * dormitory.
   */
  private void toggleSummonEnabled()
  {
    int nbrHeroes = _dorm.getNbrElements();
    if (nbrHeroes > 0) {
      _summonButton.setEnabled(true);
      if (nbrHeroes == 1) {
        _summonButton.setToolTipText(nbrHeroes + SMN_ENBLD_SINGL_HOVER);
      } else {
        _summonButton.setToolTipText(nbrHeroes + SUMMON_ENABLED_HOVER);
      }
    } else {
      _summonButton.setEnabled(false);
      _summonButton.setToolTipText(SUMMON_DISABLED_HOVER);
    }
  }

  // ============================================================
  // Private methods
  // ============================================================

  /**
   * Load the selected adventure from the Adventure registry. Replace the opening button panel with
   * the IOPanel (text and command line)
   * 
   * @param adventureName selected from the Adventure by the user
   */
  private void loadSelectedAdventure(String adventureName)
  {
    Adventure adv = _advReg.getAdventure(adventureName);

    // Create all the objects used in town
    BuildingDisplayCiv bldgCiv = new BuildingDisplayCiv(_mfCiv, adv,
        (BuildingRegistry) _rf.getRegistry(RegKey.BLDG));

    CommandFactory cmdFac = new CommandFactory(_mfCiv, bldgCiv);
    cmdFac.initMap();
    CommandParser parser = new CommandParser(_skedder, cmdFac);

    IOPanel iop = new IOPanel(parser);
    _mfCiv.replaceLeftPanel(iop);
    iop.requestFocusInWindow();

    // Wait until everything created to finally display the town
    bldgCiv.openTown();
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
    _summonButton = createSummonHeroesButton();
    JButton creationButton = createNewHeroButton();

    // Set status of Summon Button
    toggleSummonEnabled();

    _actionPanel = new ChronosPanel(INITIAL_OPENING_TITLE);

    // Align all buttons in a single column
    _actionPanel.setLayout(new MigLayout("wrap 1"));
    Dimension frame = Mainframe.getWindowSize();
    _actionPanel
        .setPreferredSize(new Dimension((int) (frame.width - FRAME_PADDING) / 2,
            frame.height - FRAME_PADDING));
    _actionPanel.setBackground(Constants.MY_BROWN);

    /** Buttons are at 25% to allow space for Command Line later */
    _actionPanel.add(adventureButton, "hmax 25%, grow");
    _actionPanel.add(_summonButton, "hmax 25%, grow");
    _actionPanel.add(creationButton, "hmax 25%, grow");

    return _actionPanel;
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
    JButton button = createButtonWithTextAndIcon(ADV_IMAGE, LOAD_ADVENTURE_TITLE);
    button.addActionListener(action -> selectAdventure());
    return button;
  }
  
  private void selectAdventure()
  {
    List<String> adventures = _advReg.getAdventureList();
    Object[] adventuresArr = adventures.toArray();
    Object selectedValue =
        JOptionPane.showInputDialog(null, "Select an Adventure", "Adventures",
            JOptionPane.INFORMATION_MESSAGE, null, adventuresArr,
            adventuresArr[0]);
    if (selectedValue != null) {
      loadSelectedAdventure((String) selectedValue);
    }
  }


  private JButton createButtonWithTextAndIcon(String imageFilePath, String buttonText)
  {
    JButton button = new JButton(buttonText);
    button.setBackground(Constants.MY_BROWN);

    button.setFont(Chronos.STANDARD_FONT);
    button.setIcon(new ImageIcon(Chronos.ADV_IMAGE_PATH + imageFilePath));
    button.setIconTextGap(40);
    return button;
  }


  /**
   * Create the button to call the NewHeroCiv, which will control the NewHeroIOPanel that collects
   * the new Hero data, and calls HeroDisplayCiv that displays the Hero's stats panel
   *
   * @return the button
   */
  private JButton createNewHeroButton()
  {
    JButton button = createButtonWithTextAndIcon(REGISTRAR_IMAGE, CREATE_HERO_TITLE);
    button.addActionListener(action -> createHero());
    return button;
  }


  /* This button code is followed by a series of inner methods */
  private JButton createSummonHeroesButton()
  {
    JButton button = createButtonWithTextAndIcon(HALL_IMAGE, SUMMON_HERO_TITLE);
    button.addActionListener(action -> summonHero());
    return button;
  }

  private void summonHero()
  {
    List<Hero> heroList = _dorm.getAll();
    List<String> plateList = new ArrayList<String>(heroList.size());
    for (Hero hero : heroList) {
      plateList.add(hero.toNamePlate());
    }

    Object[] plateArray = plateList.toArray();

    String selectedPlate =
        (String) JOptionPane.showInputDialog(null, "Select your Hero", "Heroes",
            JOptionPane.PLAIN_MESSAGE, null, plateArray, plateArray[0]);

    if (selectedPlate != null) {
      Hero selectedHero = heroList.get(plateList.indexOf(selectedPlate));
      _hdCiv.displayHero(selectedHero, false);
    }
  }
} // end of MainActionCiv class
