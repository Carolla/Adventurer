/**
 * MainActionCiv.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import chronos.pdc.Adventure;
import chronos.pdc.Chronos;
import chronos.pdc.character.Hero;
import chronos.pdc.command.Scheduler;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.HeroRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import hic.ChronosPanel;
import hic.IOPanel;
import hic.Mainframe;
import hic.NewHeroIPPanel;
import hic.ShuttleList;
import mylib.Constants;
import net.miginfocom.swing.MigLayout;
import pdc.command.CommandFactory;

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
  private RegistryFactory _rf;
  private Scheduler _skedder;

  private List<Hero> _partyHeros;

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
     constructCoreMembers();
    _mfCiv.replaceLeftPanel(createActionPanel());
  }

    public void constructCoreMembers()
    {
      _skedder = new Scheduler(_mfCiv); // Skedder first for injection
  
      _rf = new RegistryFactory();
//      _rf.initRegistries(_skedder);
      _rf.initRegistries();
  
       _advReg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
       _dorm = (HeroRegistry) _rf.getRegistry(RegKey.HERO);
     }
  
  // ============================================================
  // Public methods
  // ============================================================

  public void createHero()
  {
    NewHeroCiv nhCiv = new NewHeroCiv(_mfCiv, _dorm);
    HeroDisplayCiv hdCiv = new HeroDisplayCiv(_mfCiv, _dorm);
    NewHeroIPPanel ipPanel = new NewHeroIPPanel(nhCiv, hdCiv, _mfCiv);
    _mfCiv.replaceLeftPanel(ipPanel);
    ipPanel.setDefaultFocus(); // default focus only works after panel is displayed
  }

  public List<String> getAdventureList()
  {
    List<String> adventures = _advReg.getAdventureList();
    return adventures;
  }

  public List<Hero> getAllHeroes()
  {
    return _dorm.getAll();
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
  public void loadSelectedAdventure(String adventureName)
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
    JButton summonButton = createSummonHeroesButton();
    JButton creationButton = createNewHeroButton();

    ChronosPanel actionPanel = new ChronosPanel(INITIAL_OPENING_TITLE);

    // Align all buttons in a single column
    actionPanel.setLayout(new MigLayout("wrap 1"));
    Dimension frame = Mainframe.getWindowSize();
    actionPanel.setPreferredSize(
        new Dimension((int) (frame.width - FRAME_PADDING) / 2, frame.height - FRAME_PADDING));
    actionPanel.setBackground(Constants.MY_BROWN);

    /** Buttons are at 25% to allow space for Command Line later */
    actionPanel.add(adventureButton, "hmax 25%, grow");
    actionPanel.add(summonButton, "hmax 25%, grow");
    actionPanel.add(creationButton, "hmax 25%, grow");

    return actionPanel;
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
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        List<String> adventures = _advReg.getAdventureList();
        Object[] adventuresArr = adventures.toArray();
        Object selectedValue =
            JOptionPane.showInputDialog(null, "Select an Adventure", "Adventures",
                JOptionPane.INFORMATION_MESSAGE, null, adventuresArr, adventuresArr[0]);
        if (selectedValue != null) {
          loadSelectedAdventure((String) selectedValue);
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
    JButton button = createButtonWithTextAndIcon(REGISTRAR_IMAGE, CREATE_HERO_TITLE);
    button.addActionListener(action -> createHero());
    return button;
  }

  // ============================================================
  // Public methods:
  // ============================================================

  /* This button code is followed by a series of inner methods */
  private JButton createSummonHeroesButton()
  {
    JButton button = createButtonWithTextAndIcon(HALL_IMAGE, SUMMON_HERO_TITLE);
    button.addActionListener(new ActionListener() {
      private List<String> summonableHeroes;

      // This happens when SummonHeros is clicked
      public void actionPerformed(ActionEvent e)
      {
        // Make sure the dorm is most recent
        summonableHeroes = _dorm.getNamePlates();
        // Add a few fake names 
//        padHeroes(summonableHeroes);

        System.out.println(String.format("Printing %s Hero Names:", summonableHeroes.size()));
        for (String s : summonableHeroes) {
          System.out.println(s);
        }
        
         if (_partyHeros == null) {
         _partyHeros = new ArrayList<Hero>();
         }
        
         if (_partyHeros.size() == 0) {
         showPartyPickerWhenPartyEmpty();
         } else {
         showPartyPickerWhenMembersAlreadySelected();
         }
      }

//      private void padHeroes(List<String> list)
//      {
//        if (list.size() < 3) {
//          list.add("Gronkhar the Smelly: male gnome thief");
//          list.add("Siobhan the Obsiquious: female human wizard");
//          list.add("Sir Will-not-be-appearing-in-this-movie: male Spiderman fan");
//        }
//      }

       private void showPartyPickerWhenPartyEmpty()
       {
       System.out.println("Party Picker displayed with currently empty party.");
       // padHeroes(_summonableHeroes);
       final ShuttleList slist = new ShuttleList(summonableHeroes);
       setPropsForShuttleList(slist);
       }
      
       private void showPartyPickerWhenMembersAlreadySelected()
       {
       System.out.println("Party Picker displayed with members already selected.");
       final ShuttleList slist = new ShuttleList(summonableHeroes, _partyHeros);
       setPropsForShuttleList(slist);
       }

       // Additional settings for the ShuttleList
       private void setPropsForShuttleList(final ShuttleList slist)
       {
       slist.setTitle("Choose your Adventurers!");
       slist.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent arg0)
       {
       List<Hero> list = new ArrayList<Hero>();
       for (Hero s : slist.getSelectedHeroes(_dorm)) {
       list.add(s);
       }
       _partyHeros = list;
       slist.dispose();
       }
       });
       slist.setVisible(true);
       }

    });
    return button;
  }


} // end of MainActionCiv class
