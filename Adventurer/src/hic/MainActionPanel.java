/**
 * MainActionPanel.java Copyright (c) 2018, Alan Cline. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */


package hic;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import chronos.pdc.Chronos;
import civ.MainActionCiv;
import mylib.Constants;
import net.miginfocom.swing.MigLayout;

/**
 * Holds the primary buttons after initialization: loading an Adventure, restoring a Hero from
 * the Dormitory, and creating a Hero.
 * 
 * @author alancline
 * @version Apr 4, 2018
 */
public class MainActionPanel extends ChronosPanel
{
  /** Amount of space in pixels around the frame and image of aesthetics */
  public static final int FRAME_PADDING = 90;

  /** Title of the initial three-button panel on left side */
  private static final String INITIAL_OPENING_TITLE = " Select Your Action ";

  /** Title of the three buttons */
  private final String LOAD_ADVENTURE_TITLE = " Select Your Adventure ";
  private final String SUMMON_HERO_TITLE = " Summon Heroes ";
  private final String CREATE_HERO_TITLE = " Create a New Hero ";

  private final String REGISTRAR_IMAGE = "raw_Register.jpg";
  private final String HALL_IMAGE = "icn_HallOfHeroes.jpg";
  private final String ADV_IMAGE = "icn_Town.jpg";

//  private ChronosPanel _actionPanel;
  private JButton _summonButton;
  private MainActionCiv _actionCiv;

  /**
   * Create the Adventure, Heroes, and Create-Hero buttons, and button panel for them
   */
  public MainActionPanel() 
  {
    super(INITIAL_OPENING_TITLE);
    _actionCiv = new MainActionCiv(this);    
    addActionButtons();
  }

  
  private void addActionButtons()
  {
    JButton adventureButton = createAdventureButton();
    _summonButton = createSummonHeroesButton();
    JButton creationButton = createNewHeroButton();

    // Align all buttons in a single column
    setLayout(new MigLayout("wrap 1"));
    Dimension frame = Mainframe.getWindowSize();
    setPreferredSize(new Dimension((frame.width - FRAME_PADDING) / 2,
        frame.height - FRAME_PADDING));
    setBackground(Constants.MY_BROWN);

    // Buttons are at 25% to allow space for Command Line later
    add(adventureButton, "hmax 25%, grow");
    add(_summonButton, "hmax 25%, grow");
    add(creationButton, "hmax 25%, grow");
  }


  /**
   * Create the behavior for selecting an adventure, which drives the frame update. <br>
   * Warning: Known bug with MigLayout in that {@code float} font sizes can cause overruns on
   * round-up calculations. "Choose your Adventure" overruns the button length, but "Select
   * your Adventure" does not, despite being the same number of characters!
   * 
   * @return the button created
   */
  private JButton createAdventureButton()
  {
    JButton button = createButtonWithTextAndIcon(ADV_IMAGE, LOAD_ADVENTURE_TITLE);
    button.addActionListener(action -> _actionCiv.selectAdventure());
    return button;
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
   * Create the button to call the NewHeroCiv, which will control the NewHeroIOPanel that
   * collects the new Hero data, and calls HeroDisplayCiv that displays the Hero's stats panel
   *
   * @return the button
   */
  private JButton createNewHeroButton()
  {
    JButton button = createButtonWithTextAndIcon(REGISTRAR_IMAGE, CREATE_HERO_TITLE);
    button.addActionListener(action -> _actionCiv.createHero());
    return button;
  }

  private JButton createSummonHeroesButton()
  {
    JButton button = createButtonWithTextAndIcon(HALL_IMAGE, SUMMON_HERO_TITLE);
    button.addActionListener(action -> _actionCiv.summonHero());
    return button;
  }


} // end of MainActionPanel class
