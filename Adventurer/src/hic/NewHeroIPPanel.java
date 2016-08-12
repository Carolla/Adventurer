/**
 * NewHeroIPPanel.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package hic;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;

import chronos.hic.NameFieldLimiter;
import chronos.pdc.character.Hero;
import chronos.pdc.character.Hero.HeroInput;
import chronos.pdc.race.Race;
import civ.HeroDisplayCiv;
import civ.NewHeroCiv;
import mylib.Constants;
import net.miginfocom.swing.MigLayout;

/**
 * Allows the author to input a few key attributes of their Hero. A CIV object is called to validate
 * the data and create the {@code Hero} object. <BL>
 * <LI>Name: All Persons must have a name by which they are addressed, and are associated with a
 * file in which they are saved.</LI>
 * <LI>Gender: Females are, on average, shorter, lighter, and have less Strength than Males (the
 * default), but have a higher Constitution and Charisma.</LI>
 * <LI>Hair color: A cosmetic attribute for effect. No semantic information.</LI>
 * <LI>Race: Human is the default, but others are available: Dwarf, Elf, Gnome, Half-Elf, Half-Orc,
 * or Hobbit. Each have special penalties and bonuses compared to the Human.</LI>
 * <LI>Klass: Any Person can start as a Level 1 Fighter, Cleric, Wizard, or Rogue. All but Fighters
 * inherit special abilities.</LI>
 * <P>
 * </BL>
 * 
 * @author Alan Cline
 * @version Jan 24 2009 // original <br>
 *          Mar 25 2009 // revised to display visible attributes <br>
 *          Apr 15 2009 // revised buttons and fields to work with HeroDisplay <br>
 *          May 25 2009 // a little esthetic cleanup and reflect Person changes <br>
 *          Oct 15 2009 // set focus on Hero name field; add Help actions <br>
 *          Jun 6 2010 // revised to support NewHeroDisplayCiv <br>
 *          Apr 2 2011 // major overhaul with non-Swing MigLayout manager <br>
 *          Sep 20 2015 // major update for new inputs and revised character generation <br>
 *          Nov 9 2015 // separated Mainframe and ChronsPanel concerns, and their civs <br>
 */
@SuppressWarnings("serial")
public class NewHeroIPPanel extends ChronosPanel
{
  /** Replace left-side panel with this title */
  static private final String NEW_HERO_TITLE = " Create Your Kind of Hero ";
  /** Prompt for hero's name */
  private final String HERO_NAME_PROMPT = "What is your Hero's Name?";
  /** Hair color prompt */
  private final String HERO_HAIR_PROMPT = "What color is your hair?";
  /** Gender prompt */
  private final String HERO_GENDER_PROMPT = "What gender are you?";
  /** Race prompt */
  private final String HERO_RACE_PROMPT = "Choose your Race";
  /** Occupation prompt */
  private final String HERO_KLASS_PROMPT = "What Guild to you belong to?";

  /** Space between buttons */
  private final String SPACER = "          ";

  /** Title for error message dialog */
  private final String HERO_ERROR_TITLE = "COULDN'T CREATE HERO";
  /** Error message when name (required field) is omitted */
  private final String ERRMSG_NAME_MISSING = "Your Hero must have a name!";
  /** Error message when namefield is too long */

  /** Background color inherited from parent */
  private Color _backColor = Constants.MY_BROWN;

  /** Input data from user */
  private String _name;

  /** Contains user input field data */
  EnumMap<HeroInput, String> _input;

  /**
   * Captures the name text for the hero and the window title; Needed as attribute because data is
   * extracted outside actionListener
   */
  private JTextField _nameField = null;

  private JComboBox<String> _hairCombo;
  private JComboBox<String> _klassCombo;
  private JComboBox<String> _raceCombo;


  private HeroDisplayCiv _hdCiv;
  private NewHeroCiv _nhCiv;
  private GenderPanel _genderPanel;

  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /**
   * Creates the panel format and places the action components (e.g., radio buttons and drop down
   * boxes). Also creates the associated CIV object to manage the data. The action components are
   * created in private helper methods.
   * 
   * @param nhCiv controls this ChronosPanel
   * @param mfCiv mainframeCiv needed for displaying the panel
   * @param maCiv
   */
  public NewHeroIPPanel(NewHeroCiv nhCiv, HeroDisplayCiv hdCiv)
  {
    super(NEW_HERO_TITLE);
    _nhCiv = nhCiv;
    _hdCiv = hdCiv;

    // GENERAL SETUP
    setPreferredSize(Mainframe.getWindowSize());

    int pad = Mainframe.PAD;
    Border matte = BorderFactory.createMatteBorder(pad, pad, pad, pad,
        Color.WHITE);
    setBorder(matte);
    setBackground(_backColor);

    // Set Panel layout to special MiGLayout
    setLayout(new MigLayout("", "[center]"));

    /* HERO NAME AND PROMPT COMPONENTS */
    // Add name components to the name subpanel
    // Create a hero name prompt label centered
    add(new JLabel(HERO_NAME_PROMPT), "push, aligncenter, span");

    // Create the input text field to collect the Hero's name give it
    // default focus
    _nameField = makeNameField();
    add(_nameField, "push, align center, span");

    /*
     * THIS GRID POPULATES HORIZONTALLY: Save all Components for later data extraction
     */
    // Prompts for gender radio buttons and air color combo box */
    add(new JLabel(HERO_GENDER_PROMPT), "push, align center, gaptop 5%");
    add(new JLabel(HERO_HAIR_PROMPT), "push, align center, wrap");

    /* Gender radio buttons */
    _genderPanel = new GenderPanel();
    add(_genderPanel.toJPanel());
    /* Hair color drop-down box */
    add(makeHairCombo(), "wrap");

    /* Prompts for Race and Occupation drop-downs */
    add(new JLabel(HERO_RACE_PROMPT), "push, align center, gaptop 5%");
    add(new JLabel(HERO_KLASS_PROMPT), "push, align center, wrap");

    /* Add the Race drop-down combo */
    add(makeRaceCombo(), "push, align center");
    /* Add the Occupation drop-down combo */
    add(makeKlassCombo(), "push, align center, wrap");

    /* Add a button panel containing the Submit and Cancel buttons */
    add(makeButtonPanel(), "push, align center, span, gaptop 20%");

  } // end NewHeroIPPanel constructor

  // ============================================================
  // Private Methods
  // ============================================================

  /**
   * Set the name field to have the default focus NOTE: For some reason, requestFocusInWindow() does
   * not work here
   */
  public void setDefaultFocus()
  {
    _nameField.requestFocus();
  }


  /**
   * Create a button panel containing Submit and Cancel buttons
   * 
   * @return the JPanel
   */
  private JPanel makeButtonPanel()
  {
    // Create the containing panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(_backColor);

    // Create the CANCEL button
    JButton cancelButton = new JButton("CANCEL");
    // Clear editFlag and data, then return back to main action panel if
    // Cancel is pressed
    cancelButton.addActionListener((a) -> _hdCiv.back());

    // Create the SUBMIT button
    JButton submitButton = new JButton("SUBMIT");

    // Display error message if received from submit button, or new Hero if
    // OK
    submitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        // Call the Civ to validate the attributes. If no errors, Hero
        // is created and displayed
        EnumMap<HeroInput, String> input = submit();
        if ((input != null) && (input.size() > 0)) {
          // Create the new Hero and display it
          Hero hero = _nhCiv.createHero(input);
          _hdCiv.displayHero(hero, true); // initial Hero needs true
          // arg to check overwriting
        }
      }
    });

    // Create a little space between buttons
    JLabel space = new JLabel(SPACER);
    // Add the two buttons to the panel
    buttonPanel.add(submitButton);
    buttonPanel.add(space);
    buttonPanel.add(cancelButton);
    return buttonPanel;
  }

  /**
   * Create a combo box of hair colors from which the user may select
   * 
   * @return the JComboBox<String> of hair color options
   */
  private JComboBox<String> makeHairCombo()
  {
    _hairCombo = new JComboBox<String>(NewHeroCiv.HAIR_COLOR_LIST);
    _hairCombo.setEditable(false);
    _hairCombo.setBackground(Color.WHITE);
    return _hairCombo;
  }

  /**
   * Create a combo box of Guilds (Klasses) that the Hero may want to be
   * 
   * @return the JComboBox<String> of Guild options
   */
  private JComboBox<String> makeKlassCombo()
  {
    _klassCombo = new JComboBox<String>(NewHeroCiv.KLASS_LIST);
    _klassCombo.setEditable(false);
    _klassCombo.setBackground(Color.WHITE);
    return _klassCombo;
  }

  /**
   * Create the Hero's name input field and associated action before placement
   * 
   * @return the JTextField
   */
  private JTextField makeNameField()
  {
    _nameField = new JTextField(50);

    // Create DocumentFilter for restricting input length
    AbstractDocument d = (AbstractDocument) _nameField.getDocument();
    d.setDocumentFilter(new NameFieldLimiter(NewHeroCiv.MAX_NAMELEN));
    _nameField.setName("heroName");

    // Extract Hero's name and update Hero's name into MainFrame Title
    // if Enter key is hit or text field loses focus.
    _nameField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        _name = _nameField.getText().trim();
        setHeroBorder();
      }
    });

    return _nameField;
  }

  /**
   * Create a combo box of Races that the Hero may be
   * 
   * @return the JComboBox<String> of Race options
   */
  private JComboBox<String> makeRaceCombo()
  {
    // Build the box with label
    _raceCombo = new JComboBox<String>(Race.RACE_LIST);
    _raceCombo.setEditable(false);
    _raceCombo.setBackground(Color.WHITE);
    return _raceCombo;
  }

  private void setHeroBorder()
  {
    Border matte = BorderFactory.createMatteBorder(Mainframe.PAD,
        Mainframe.PAD, Mainframe.PAD, Mainframe.PAD, Color.WHITE);
    Border heroBorder = BorderFactory.createTitledBorder(matte,
        NEW_HERO_TITLE + _name, TitledBorder.CENTER,
        TitledBorder.DEFAULT_POSITION);
    setBorder(heroBorder);
  }

  /**
   * Display the error message received after submitting a new Hero.
   */
  private void showErrorMessage(String errorMessage)
  {
    JOptionPane.showMessageDialog(null, ERRMSG_NAME_MISSING,
        HERO_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    _nameField.requestFocusInWindow();
  }

  /**
   * Get an empty enumMap, pack the input fields into it, and send it to the Civ for validation.
   * 
   * @return One of the ErrorCode enum values (NO_ERROR if all went well)
   */
  private EnumMap<HeroInput, String> submit()
  {
    EnumMap<HeroInput, String> input = new EnumMap<HeroInput, String>(
        HeroInput.class);

    // Name is mandatory user input
    String name = (_nameField.getText()).trim();
    if (name == null || name.isEmpty()) {
      input.clear(); // empty input is a failure
      showErrorMessage(ERRMSG_NAME_MISSING);
      return null;
    }
    input.put(HeroInput.NAME, name);
    input.put(HeroInput.GENDER, _genderPanel.getSelectedGender());
    input.put(HeroInput.HAIR, String.valueOf(_hairCombo.getSelectedItem()));
    input.put(HeroInput.RACE, String.valueOf(_raceCombo.getSelectedItem()));
    input.put(HeroInput.KLASS, String.valueOf(_klassCombo.getSelectedItem()));

    return input;
  }


} // end NewHeroIPPanel class

