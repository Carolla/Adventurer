/**
 * MainActionPanel.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package hic;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import chronos.Chronos;
import chronos.pdc.Adventure;
import chronos.pdc.character.Hero;
import chronos.pdc.registry.HeroRegistry;
import civ.MainActionCiv;
import civ.MainframeCiv;
import mylib.Constants;
import net.miginfocom.swing.MigLayout;

/**
 * Provides the major action buttons on the opening page:
 * <UL>
 * <LI>Select and load an Adventure</li>
 * <LI>Summon an existing Hero to interact in the town and areda</li>
 * <LI>Create a new Hero</li>
 * </ul>
 * The {@code MainActionPanel} buttons display on the left.
 * 
 * @author Al Cline
 * @version Dec 13, 2015 // original <br>
 */
@SuppressWarnings("serial")
public class MainActionPanel extends ChronosPanel
{
  private MainActionCiv _mac;
  private Mainframe _mf;
  private MainframeCiv _mfCiv;
  
  private List<Hero> _partyHeros = new ArrayList<Hero>();

  /** Controls left side and right side panels */
  private ChronosPanel _actionPanel;

  /** Amount of space in pixels around the frame and image of aesthetics */
  public static final int FRAME_PADDING = 90;

  /** Title of the initial three-button panel on left side */
  private static final String INITIAL_OPENING_TITLE = " Select Your Action ";

  private final String REGISTRAR_IMAGE = "raw_Register.jpg";
  private final String HALL_IMAGE = "icn_HallOfHeroes.jpg";
  private final String ADV_IMAGE = "icn_Town.jpg";

   private final String INITIAL_IMAGE = "ChronosLogo.jpg";
   private final String INITIAL_IMAGE_TITLE = "Chronos Logo";

  
  // ============================================================
  // Constructor and constructor helper methods
  // ============================================================

  /**
   * Create the three major action buttons: Load a selected Adventure, Awaken (load) an existing
   * Hero, or Create a new Hero.
   * 
   * @param mac civ to handle data
   * @param mg  reference to frame that displays resulting input and output panels
   */
  public MainActionPanel(MainActionCiv mac, Mainframe mf, MainframeCiv mfciv)
  {
    super(INITIAL_OPENING_TITLE);
    _mac = mac;
    _mf = mf;
    _mfCiv = mfciv;
    
    JButton adventureButton = createAdventureButton();
    JButton summonButton = createSummonHeroesButton();
    JButton creationButton = createNewHeroButton();

     _actionPanel = new ChronosPanel(INITIAL_OPENING_TITLE);

    // Align all buttons in a single column
    _actionPanel.setLayout(new MigLayout("wrap 1"));
    Dimension frame = Mainframe.getWindowSize();
    _actionPanel.setPreferredSize(
        new Dimension((int) (frame.width - FRAME_PADDING) / 2, frame.height - FRAME_PADDING));
    _actionPanel.setBackground(Constants.MY_BROWN);

    /** Buttons are at 25% to allow space for Command Line later */
    _actionPanel.add(adventureButton, "hmax 25%, grow");
    _actionPanel.add(summonButton, "hmax 25%, grow");
    _actionPanel.add(creationButton, "hmax 25%, grow");

    _mf.replaceLeftPanel(this);
  }

  private void setActivePanel()
  {
     _mf.replaceLeftPanel(_actionPanel);
     _mfCiv.displayImage(INITIAL_IMAGE_TITLE, INITIAL_IMAGE);
//     ChronosPanel actionPanel = new MainActionPanel(this);
     _mf.replaceLeftPanel(_actionPanel);
     _mfCiv.displayImage(INITIAL_IMAGE_TITLE, INITIAL_IMAGE);
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
        List<Adventure> adventures = _mac.getAdventureList();
        Object[] adventuresArr = adventures.toArray();
        Object selectedValue =
            JOptionPane.showInputDialog(null, "Select an Adventure", "Adventures",
                JOptionPane.INFORMATION_MESSAGE, null, adventuresArr, adventuresArr[0]);
        if (selectedValue != null) {
          _mac.loadSelectedAdventure(selectedValue.toString().trim());
          // Create the image panel and civ for the town          
          // Create the IOPanel and civ for the 
        }
      }
    });
    return button;
  }

  // ============================================================
  // Public methods:
  // ============================================================
  
  // NONE
  
  // ============================================================
  // Private methods:
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


  /**
   * Create the button to call the NewHeroCiv, which will control the NewHeroIOPanel that collects
   * the new Hero data, and calls HeroDisplayCiv that displays the Hero's stats panel
   *
   * @return the button
   */
  private JButton createNewHeroButton()
  {
    JButton button = createButtonWithTextAndIcon(REGISTRAR_IMAGE, "Create New Heroes");
    button.addActionListener(action -> _mac.createHero());
    return button;
  }

  
  /* This button code is followed by a series of inner methods */
  private JButton createSummonHeroesButton()
  {
    JButton button = createButtonWithTextAndIcon(HALL_IMAGE, "Summon Heroes");
    button.addActionListener(new ActionListener() {
      // HeroRegistry heroReg = (HeroRegistry) _rf.getRegistry(RegKey.HERO);
      HeroRegistry heroReg = _mac.getDormitory();
      private List<Hero> summonableHeroes;

      // This happens when SummonHeros is clicked
      public void actionPerformed(ActionEvent e)
      {
        summonableHeroes = heroReg.getAll();

        if (_partyHeros.size() == 0) {
          showPartyPickerWhenPartyEmpty();
        } else {
          showPartyPickerWhenMembersAlreadySelected();
        }
      }

      private void showPartyPickerWhenPartyEmpty()
      {
        // padHeroes(_summonableHeroes);
        final ShuttleList slist = new ShuttleList(summonableHeroes);
        setPropsForShuttleList(slist);
      }

      private void showPartyPickerWhenMembersAlreadySelected()
      {
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
            for (Hero s : slist.getSelectedHeroes(heroReg)) {
              list.add(s);
            }
            _partyHeros = list;
            slist.dispose();
          }
        });
        slist.setVisible(true);
      }

      // private void padHeroes(List<String> list)
      // {
      // if (list.size() < 3) {
      // list.add("Gronkhar the Smelly");
      // list.add("Siobhan the Obsiquious");
      // list.add("Sir Will-not-be-appearing-in-this-movie");
      // }
      // }
    });
    return button;
  }


} // end of MainActionPanel class
