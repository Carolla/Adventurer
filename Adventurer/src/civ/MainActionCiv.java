/**
 * MainActionPanel.java Copyright (c) 2018, Alan Cline. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */

package civ;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import chronos.pdc.Adventure;
import chronos.pdc.buildings.Inn;
import chronos.pdc.character.Hero;
import chronos.pdc.command.Scheduler;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.HeroRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import hic.IOPanel;
import hic.MainActionPanel;
import hic.NewHeroIPPanel;
import pdc.command.CommandFactory;

/**
 * Creates the MainActionPanel consisting of the primary buttons: Select an Adventure, Summon
 * Heroes, and Create a New Hero
 * 
 * @author Alan Cline
 * @version Nov 7, 2015 // original <br>
 *          Apr 4, 2018 // moved GUI methods into HIC <br>
 */

public class MainActionCiv
{
  private AdventureRegistry _advReg;
  private HeroRegistry _dorm;
  private MainframeCiv _mfCiv;
  private HeroDisplayCiv _hdCiv;
  private RegistryFactory _rf;
  private Scheduler _skedder;

  // /** Amount of space in pixels around the frame and image of aesthetics */
  // public static final int FRAME_PADDING = 90;
  //
  // /** Title of the initial three-button panel on left side */
  // private final String INITIAL_OPENING_TITLE = " Select Your Action ";
  //
  // /** Title of the three buttons */
  // private final String LOAD_ADVENTURE_TITLE = " Select Your Adventure ";
  // private final String SUMMON_HERO_TITLE = " Summon Heroes ";
  // private final String CREATE_HERO_TITLE = " Create a New Hero ";
  //
  // private final String REGISTRAR_IMAGE = "raw_Register.jpg";
  // private final String HALL_IMAGE = "icn_HallOfHeroes.jpg";
  // private final String ADV_IMAGE = "icn_Town.jpg";
  //
  // protected static MainActionPanel _actionPanel;
  // private JButton _summonButton;

  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /**
   * Create the Civ to display and handle the MainActionPanel of buttons
   * 
   * @param mfCiv handler for the mainframe
   */
  public MainActionCiv(MainActionPanel actionPanel, MainframeCiv mfc)
  {
    _mfCiv = mfc;
    // _actionPanel = actionPanel;
    constructCoreMembers();
  }

  /** Create the registries required by the three main action buttons */
  public void constructCoreMembers()
  {
    // Create the registries, scheduler, and command parser
    _rf = new RegistryFactory();
    _rf.initRegistries();
    _skedder = new Scheduler();

    // Set up times for the patrons to visit the Inn
    ((Inn) ((BuildingRegistry) _rf.getRegistry(RegKey.BLDG)).getBuilding("Ugly Ogre Inn"))
        .initPatrons(_skedder);

    _dorm = (HeroRegistry) _rf.getRegistry(RegKey.HERO);
    _hdCiv = new HeroDisplayCiv(_mfCiv, _dorm);
    _advReg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
  }


  // ============================================================
  // Public methods
  // ============================================================

  /**
   * Load the selected adventure from the Adventure registry. Replace the opening button panel
   * with the IOPanel (text and command line)
   * 
   * @param adventureName selected from the Adventure by the user
   */
  public void loadSelectedAdventure(String adventureName, MainframeCiv mfCiv)
  {
    _mfCiv = mfCiv;

    Adventure adv = _advReg.getAdventure(adventureName);

    // Create all the objects used in town
    BuildingDisplayCiv bldgCiv = new BuildingDisplayCiv(_mfCiv, adv,
        (BuildingRegistry) _rf.getRegistry(RegKey.BLDG));

    CommandFactory cmdFac = new CommandFactory(bldgCiv, _mfCiv);
    cmdFac.initMap();

    CommandParser parser = new CommandParser(_skedder, cmdFac);

    IOPanel iop = new IOPanel(parser);
    _mfCiv.replaceLeftPanel(iop);
    iop.requestFocusInWindow();

    // Wait until everything created to finally display the town
    bldgCiv.openTown();
  }

  public void createHero()
  {
    NewHeroCiv nhCiv = new NewHeroCiv();
    NewHeroIPPanel ipPanel = new NewHeroIPPanel(nhCiv, _hdCiv);
    _mfCiv.replaceLeftPanel(ipPanel);
    ipPanel.setDefaultFocus(); // only works after panel is displayed
  }

  // /**
  // * Create the Adventure, Heroes, and Create-Hero buttons, and button panel for them
  // */
  // private ChronosPanel createActionPanel()
  // {
  // JButton adventureButton = createAdventureButton();
  // _summonButton = createSummonHeroesButton();
  // JButton creationButton = createNewHeroButton();
  //
  // _actionPanel = new ChronosPanel(INITIAL_OPENING_TITLE);
  //
  // // Align all buttons in a single column
  // _actionPanel.setLayout(new MigLayout("wrap 1"));
  // Dimension frame = Mainframe.getWindowSize();
  // _actionPanel.setPreferredSize(new Dimension((frame.width - FRAME_PADDING) / 2,
  // frame.height - FRAME_PADDING));
  // _actionPanel.setBackground(Constants.MY_BROWN);
  //
  // /** Buttons are at 25% to allow space for Command Line later */
  // _actionPanel.add(adventureButton, "hmax 25%, grow");
  // _actionPanel.add(_summonButton, "hmax 25%, grow");
  // _actionPanel.add(creationButton, "hmax 25%, grow");
  //
  // return _actionPanel;
  // }

  // /**
  // * Create the behavior for selecting an adventure, which drives the frame update. <br>
  // * Warning: Known bug with MigLayout in that {@code float} font sizes can cause overruns on
  // * round-up calculations. "Choose your Adventure" overruns the button length, but "Select
  // * your Adventure" does not, despite being the same number of characters!
  // *
  // * @return the button created
  // */
  // private JButton createAdventureButton()
  // {
  // JButton button = createButtonWithTextAndIcon(ADV_IMAGE, LOAD_ADVENTURE_TITLE);
  // button.addActionListener(action -> selectAdventure());
  // return button;
  // }

  public void selectAdventure()
  {
    List<String> adventures = _advReg.getAdventureList();
    Object[] adventuresArr = adventures.toArray();
    Object selectedValue =
        JOptionPane.showInputDialog(null, "Select an Adventure", "Adventures",
            JOptionPane.INFORMATION_MESSAGE, null, adventuresArr,
            adventuresArr[0]);
    if (selectedValue != null) {
      loadSelectedAdventure((String) selectedValue, _mfCiv);
    }
  }


  // private JButton createButtonWithTextAndIcon(String imageFilePath, String buttonText)
  // {
  // JButton button = new JButton(buttonText);
  // button.setBackground(Constants.MY_BROWN);
  //
  // button.setFont(Chronos.STANDARD_FONT);
  // button.setIcon(new ImageIcon(Chronos.ADV_IMAGE_PATH + imageFilePath));
  // button.setIconTextGap(40);
  // return button;
  // }


  // /**
  // * Create the button to call the NewHeroCiv, which will control the NewHeroIOPanel that
  // * collects the new Hero data, and calls HeroDisplayCiv that displays the Hero's stats
  // panel
  // *
  // * @return the button
  // */
  // private JButton createNewHeroButton()
  // {
  // JButton button = createButtonWithTextAndIcon(REGISTRAR_IMAGE, CREATE_HERO_TITLE);
  // button.addActionListener(action -> createHero());
  // return button;
  // }
  //
  // private JButton createSummonHeroesButton()
  // {
  // JButton button = createButtonWithTextAndIcon(HALL_IMAGE, SUMMON_HERO_TITLE);
  // button.addActionListener(action -> summonHero());
  // return button;
  // }

  public void summonHero()
  {
    List<Hero> heroList = _dorm.getAll();

    if (heroList.size() > 0) {
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
        // _hdCiv.displayHero(selectedHero, false);
      }
    } else {
      JOptionPane.showMessageDialog(null, "No heros");
    }
  }


} // end of MainActionCiv class
