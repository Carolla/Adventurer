/*
 * NewHeroPanel.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package hic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import mylib.MsgCtrl;
import mylib.civ.DataShuttle;
import mylib.civ.DataShuttle.ErrorType;
import mylib.hic.HelpKeyListener;
import net.miginfocom.swing.MigLayout;
import chronos.Chronos;
import civ.NewHeroCiv;
import civ.NewHeroFields;


/** TODO: [3] Add HelpDialog and widget-specific helpListeners to this panel */


/**
 * Allows the author to create a few key attributes of their Hero. A CIV object is called to
 * validate the data and create the Hero, a {@code Person} object.
 * <P>
 * <BL>
 * <LI>Name: All Persons must have a name by which they are addressed, and are associated with a
 * file by which they are saved.</LI>
 * <LI>Gender: Females are, on average, shorter, lighter, and have less Strength than Males (the
 * default), but have a higher Constitution and Charisma.</LI>
 * <LI>Hair color: A cosmetic attribute for effect. No semantic information.</LI>
 * <LI>Occupation: Although "None" is an option, most occupations reflect some special skill that
 * the Person acquired in earlier life.</LI>
 * <LI>Race: Human is the default, but others are available: Dwarf, Elf, Gnome, Half-Elf, Half-Orc,
 * or Hobbit. Each have special penalties and bonuses compared to the Human.</LI>
 * <LI>Klass (implied): Everyone starts as a Peasant, but can become a Fighter, Cleric, Wizard, or
 * Rogue when they join a Guild. All but Fighters inherit special abilities.</LI>
 * <P>
 * </BL>
 * 
 * @author Alan Cline
 * @version Jan 24 2009 // original <br>
 *          Mar 25 2009 // revised to display visible attributes <br>
 *          Apr 15 2009 // revised buttons and fields to work with HeroDisplay <br>
 *          May 25 2009 // a little esthetic cleanup and reflect Person changes <br>
 *          Oct 15 2009 // set focus on Person name field; add Help actions <br>
 *          Jun 6 2010 // revised to support NewHeroDisplayCiv <br>
 *          Apr 2 2011 // major overhaul with non-Swing MigLayout manager <br>
 */
@SuppressWarnings("serial")
public class NewHeroDisplay extends JPanel
{
  /** Help message to show in panels */
  private final String HELP_LABEL1 =
      "<Ctrl-Tab> or mouse-click moves focus to new text area.";
  /** Help message to show in panels */
  private final String HELP_LABEL2 =
      "Press F1 key for specific help.";

  /** Before the Hero's name is entered */
  private final String GENERIC_TITLE = "Registering New Hero in Town";
  /** Adapt title to Hero's name when entered */
  private final String NEW_HERO_TITLE = GENERIC_TITLE + " -- ";
  /** Prompt for hero's name */
  private final String HERO_NAME_PROMPT = "What is your Hero's Name?";
  /** Hair color prompt */
  private final String HERO_HAIR_PROMPT = "What color is your hair?";
  /** Gender prompt */
  private final String HERO_GENDER_PROMPT = "What gender are you?";
  /** Race prompt */
  private final String HERO_RACE_PROMPT = "Choose your Race";
  /** Occupation prompt */
  private final String HERO_OCCUP_PROMPT = "Choose your Occupation";

  /** Number of columns in the person's name input window */
  private final int HERO_NAME_WIDTH = 40;
  /** Space between buttons */
  private final String SPACER = "          ";

  /** Title for error message dialog */
  private final String PERSON_ERROR_TITLE = "HERO CREATE ERROR";
  /** Error message when name (required field) is omitted */
  private final String ERRMSG_MISSING_NAME = "Your Hero must have a name!";
  /** Error message when some other field is missing (shouldn't happen) */
  private final String ERRMSG_NULL_FIELD = "%s is Null";
  /** Error message when an unknown error has occurred */
  private final String ERRMSG_UNKNOWN =
      "Hero could not be created for some unanticipated reason";

  /** Background color inherited from parent */
  private Color _backColor = null;

  /* Class Members for Score Selectors */
  // private final String STR = "Strength";
  // private final String CON = "Constitution";
  // private final String DEX = "Dexterity";
  // private final String CHA = "Charisma";
  // private final String INT = "Intelligence";
  // private final String WIS = "Wisdom";

  private Map<String, JTextField> _abilityScoreFields = new HashMap<String, JTextField>();
  private Map<String, JButton> _abilityUpButtons = new HashMap<String, JButton>();
  private Map<String, JButton> _abilityDownButtons = new HashMap<String, JButton>();
  private Map<String, JLabel> _abilityMaxLabel = new HashMap<String, JLabel>();
  private Map<String, JLabel> _abilityMinLabel = new HashMap<String, JLabel>();
  private Map<JButton, String> _abilityAttribUpButtonRef = new HashMap<JButton, String>();
  private Map<JButton, String> _abilityAttribDownButtonRef = new HashMap<JButton, String>();

  private final int _SCORE_MIN = 8;
  private final int _SCORE_MAX = 18;
  private List<String> _attributes;
  private JLabel _totalPointsSpentLabel = new JLabel("Total points remaining:");
  private JTextField _totalPointsValueTextField = new JTextField();

  // TODO: Attach the Help System and Help Listeners o this window
  // /** HelpDialog reference for all widgets that have context help */
  // private HelpDialog _help = null;
  // /** HelpKeyListener (F1) reference */
  // private HelpKeyListener _helpKey = null;

  private String _name = null;
  private String _gender = null;
  private String _raceName = null;
  private String _occup = null;
  private String _hairColor = null;

  /**
   * Captures the name text for the hero and the window title; Needed as attribute because data is
   * extracted outside actionListener
   */
  private JTextField _nameField = null;

  /** Associated validating CIV object */
  private NewHeroCiv _nhCiv = null;
  /** Parm object for passing and receiving field data */
  private DataShuttle<NewHeroFields> _shuttle = null;


  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /**
   * Creates the panel format and places the action components (e.g., radio buttons and drop down
   * boxes). Also creates the associated CIV object to manage the data. The action components are
   * created in private helper methods.
   * 
   * @throws InstantiationException if some problem occurs
   */
  public NewHeroDisplay() throws InstantiationException
  {
    // Create the associated Civ object for validating the input data (no model yet)
    _nhCiv = new NewHeroCiv();
    _attributes = _nhCiv.getAttributes();

    // Grab the default data for display from the civ, specifying the enum keys
    _shuttle = _nhCiv.getDefaults();

    // GENERAL SETUP
    // Set the preferred and max size, adjusting for panel border thickness
    int width = Mainframe.getWindowSize().width;// +MainFrame.PANEL_SHIFT;
    int height = Mainframe.getWindowSize().height;
    setPreferredSize(new Dimension(width, height));

    int pad = Mainframe.PAD;
    Border matte = BorderFactory.createMatteBorder(pad, pad, pad, pad, Color.WHITE);
    Border titledBorder = BorderFactory.createTitledBorder(matte, GENERIC_TITLE,
        TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
    setBorder(titledBorder);
    // _backColor = Mainframe.PANEL_COLOR;
    setBackground(_backColor);

    // Define context controls
    // Default help text when no widget is in focus
    new HelpKeyListener("NewHero");

    // Set Panel layout to special MiGLayout
    setLayout(new MigLayout("", "[center]"));

    /* ADD HELP LABEL TO INSTRUCT HOW TO SHIFT FOCUS TO DIFFERENT TEXT FIELDS */
    // Labels are centered horizontal atop one another
    add(new JLabel(HELP_LABEL1), "span"); // focus traversal message
    add(new JLabel(HELP_LABEL2), "span"); // help key message

    /* HERO NAME AND PROMPT COMPONENTS */
    // Add name components to the name subpanel
    // Create a hero name prompt label centered
    add(new JLabel(HERO_NAME_PROMPT), "push, aligncenter, span");

    // Create the input text field to collect the Hero's name
    _nameField = makeNameField();
    add(_nameField, "push, align center, span");

    /* THIS GRID POPULATES HORIZONTALLY: Save all Components for later data extraction */
    // Prompts for gender radio buttons and air color combo box */
    add(new JLabel(HERO_GENDER_PROMPT), "push, align center, gaptop 5%");
    add(new JLabel(HERO_HAIR_PROMPT), "push, align center, wrap");

    /* Gender radio buttons */
    add(makeGenderPanel());
    /* Hair color drop-down box */
    add(makeHairCombo(), "wrap");

    /* Prompts for Race and Occupation drop-downs */
    add(new JLabel(HERO_RACE_PROMPT), "push, align center, gaptop 5%");
    add(new JLabel(HERO_OCCUP_PROMPT), "push, align center, wrap");

    /* Add the Race drop-down combo */
    add(makeRaceCombo(), "push, align center");
    /* Add the Occupation drop-down combo */
    add(makeOccupationCombo(), "push, align center, wrap");

    /* Add total points label and field */
    add(_totalPointsSpentLabel);
    _totalPointsValueTextField = makeTotalPointsValueField();
    add(_totalPointsValueTextField, "wrap");

    add(makeScoreSelectorPanel(), "span");

    setScoreRanges();

    /* Add a button panel containing the Submit and Cancel buttons */
    add(makeButtonPanel(), "push, align center, span, gaptop 20%");

  } // end NewHeroPanel constructor


  // ============================================================
  // Public Methods
  // ============================================================

  /**
   * The name text field should have the default focus, butt that cannot be done until after the
   * panel is realized and visible. Therefore, the NewHeroPanel's caller (MenuBar.NEW Action) must
   * invoke the default component. It calls this method to get which one should be default. This
   * method is created for this because the caller does not know what fields are available in this
   * panel, and it can change with maintenance.
   * 
   * @return the JTextField, in this case
   */
  public JTextField getDefaultFocus()
  {
    return _nameField;
  }


  // ============================================================
  // Private Methods
  // ============================================================

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

    // Clear editFlag and data, then return back to mainframe if Cancel is pressed
    cancelButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        // Audit statement
        MsgCtrl.traceEvent(event);
        // Reset menu options to original
        // MenuBar.getInstance().resetMenus();
        // Collect all the attributes and save to a new Hero file
        setEditFlag(false);
        // Remove this panel and ignore any changes
//        Mainframe frame = Mainframe.getInstance();
//        frame.resetPanels();
        setVisible(false);
      }
    });

    // Create the SUBMIT button
    JButton submitButton = new JButton("SUBMIT");

    // Clear data and return back to mainframe if Cancel is pressed
    submitButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        // Audit statement
        MsgCtrl.traceEvent(event);
        // Pack the data, call the Civ to validate the attributes
        // and display the Person in HeroDisplay dialog
        if (submit() == true) {
          // Reset menu options to original
          // MenuBar.getInstance().resetMenus();
          // Collect all the attributes and save to a new Hero file
          setEditFlag(false);
          // Remove this panel and ignore any changes
          setVisible(false);
        }
        else {
          // DIsplay the message
          showErrorMessage();
          _shuttle.clearErrors();
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
   * Create the gender radiobuttons, bordered panel and associated action before placement
   * 
   * @return the JPanel of radiobuttons
   */
  private JPanel makeGenderPanel()
  {
    // Create the panel to contain the radio button group
    JPanel radioPanel = new JPanel();
    Border border = BorderFactory.createLineBorder(Color.WHITE);
    radioPanel.setBorder(border);
    radioPanel.setBackground(_backColor);

    // Create the radiobutton group
    ButtonGroup groupSex = new ButtonGroup();

    // Define the male radio button (default to Male button selected)
    JRadioButton maleButt = new JRadioButton("Male", true);
    // Default is set in case user does not touch the radiobuttons
    _gender = "Male";
    maleButt.setEnabled(true);
    maleButt.setBackground(_backColor);

    // Allow user to explicitly set to Male
    maleButt.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        MsgCtrl.traceEvent(event);
        _gender = "Male";
        setEditFlag(true);
        setScoreRanges();
      }
    });

    // Define the female radio button
    JRadioButton femaleButt = new JRadioButton("Female", false);
    femaleButt.setBackground(_backColor);
    // Allow user to explicitly set Female gender
    femaleButt.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        MsgCtrl.traceEvent(event);
        _gender = "Female";
        setEditFlag(true);
        setScoreRanges();
      }
    });

    // Add the help key listener to the gender buttons
    HelpKeyListener genderHelp = new HelpKeyListener("HeroGender");
    maleButt.addKeyListener(genderHelp);
    femaleButt.addKeyListener(genderHelp);
    // Buttons must be added to BOTH the group and to the panel
    // Add buttons to button group
    groupSex.add(maleButt);
    groupSex.add(femaleButt);
    radioPanel.add(maleButt);
    radioPanel.add(femaleButt);
    return radioPanel;
  }


  /**
   * Create a combo box of hair colors from which the user may select
   * 
   * @return the JComboBox of hair color options
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private JComboBox makeHairCombo()
  {
    // Build the box with label
    final JComboBox hairCombo = new JComboBox();
    hairCombo.setEditable(false);
    hairCombo.setBackground(Color.WHITE);
    hairCombo.addKeyListener(new HelpKeyListener("HairColor"));

    // Populate the dropdown box from CIV data
    List<String> hairColorList = (List<String>) NewHeroFields.HAIR_COLOR_OPTIONS.getDefault();
    for (int k = 0; k < hairColorList.size(); k++) {
      hairCombo.addItem(hairColorList.get(k) + " ");
    }
    _hairColor = hairColorList.get(0);

    // Change the visible selection and capture the data
    hairCombo.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        MsgCtrl.traceEvent(event);
        _hairColor = ((String) hairCombo.getSelectedItem()).trim();
        setEditFlag(true);
      }
    });
    return hairCombo;
  }


  /**
   * Create the Hero's name input field and associated action before placement
   * 
   * @return the JTextField
   */
  private JTextField makeNameField()
  {
    // Create the text field to collect the Hero's name
    _nameField = new JTextField(HERO_NAME_WIDTH);
    // Collect the name when the text field goes out of focus
    _nameField.addFocusListener(new FocusOffListener());
    _nameField.setName("heroName");

    // Extract Hero's name and update Hero's name into MainFrame Title
    // if Enter key is hit or text field loses focus.
    _nameField.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        // Audit statement
        MsgCtrl.traceEvent(event);
        // Name data is captured on submit in case mouse clicks were used
        _name = _nameField.getText().trim();
        // Rename the pane with the new Hero's name
        int pad = Mainframe.PAD;
        Border matte = BorderFactory.createMatteBorder(pad, pad, pad, pad, Color.WHITE);
        Border heroBorder = BorderFactory.createTitledBorder(matte,
            NEW_HERO_TITLE + _name, TitledBorder.CENTER,
            TitledBorder.DEFAULT_POSITION);
        setBorder(heroBorder);
        // Set the edit change flag
        setEditFlag(true);
      }
    });

    return _nameField;
  }


  /**
   * Create a combo box of Occupations that the Hero may bring to the game
   * 
   * @return the JComboBox of Occupation options
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private JComboBox makeOccupationCombo()
  {
    // Build the box with label
    final JComboBox occupCombo = new JComboBox();
    occupCombo.setEditable(false);
    occupCombo.setBackground(Color.WHITE);
    occupCombo.addKeyListener(new HelpKeyListener("HeroOccup"));

    List<String> occupations =
        (List<String>) _shuttle.getField(NewHeroFields.OCCUPATION_OPTIONS);
    for (int k = 0; k < occupations.size(); k++) {
      occupCombo.addItem(occupations.get(k) + " ");
    }
    _occup = occupations.get(0);

    // Change the visible selection and capture the data
    occupCombo.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        // Audit statement
        MsgCtrl.traceEvent(event);
        _occup = ((String) occupCombo.getSelectedItem()).trim();
        // Set the edit change flag
        setEditFlag(true);
      }
    });
    return occupCombo;
  }


  /**
   * Create a combo box of Races that the Hero may be
   * 
   * @return the JComboBox of Race options
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private JComboBox makeRaceCombo()
  {
    // Build the box with label
    final JComboBox raceCombo = new JComboBox();
    raceCombo.setEditable(false);
    raceCombo.setBackground(Color.WHITE);
    raceCombo.addKeyListener(new HelpKeyListener("HeroRace"));

    // Populate the dropdown box from CIV data
    List<String> raceList =
        (List<String>) _shuttle.getField(NewHeroFields.RACE_OPTIONS);
    for (int k = 0; k < raceList.size(); k++) {
      // Add padding by putting a space at end of item
      raceCombo.addItem(raceList.get(k) + " ");
    }

    // Set default in case nothing is selected
    _raceName = ((String) raceCombo.getSelectedItem()).trim();

    // Change the visible selection; data is captured elsewhere
    raceCombo.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        // Audit statement
        MsgCtrl.traceEvent(event);
        _raceName = ((String) raceCombo.getSelectedItem()).trim();
        // Set the edit change flag
        setEditFlag(true);
        setScoreRanges();
      }
    });
    return raceCombo;
  }

  /**
   * Create a text field to display the number of points remaining
   * 
   * @return the JTextField for points remaining display.
   */
  private JTextField makeTotalPointsValueField()
  {
    JTextField newField = new JTextField();

    newField.setText("24");
    newField.setFont(makeRunicFont(24f));
    newField.setPreferredSize(new Dimension(42, 30));
    newField.setHorizontalAlignment(JTextField.CENTER);
    newField.setEditable(false);

    return newField;
  }

  /**
   * Create the ability score selector panel.
   * 
   * @return JPanel containing the ability score selectors.
   */
  private JPanel makeScoreSelectorPanel()
  {
    JPanel scorePanel = new JPanel();
    for (String attrib : _attributes) {
      JPanel score = makeScoreSelector(attrib);
      scorePanel.add(score);
    }
    // scorePanel.setBackground(Mainframe.PANEL_COLOR);
    return scorePanel;
  }

  /**
   * Creates an ability score selector control.
   * 
   * @param attrib - the attribute associated with the control
   * @return the JPanel with the ability score selector controls
   */
  private JPanel makeScoreSelector(String attrib)
  {
    JPanel textPanel = new JPanel();
    textPanel.setLayout(new BorderLayout());

    JLabel scoreLabel = new JLabel(attrib);
    JLabel maxLabel = new JLabel(String.valueOf(_SCORE_MAX));
    JLabel minLabel = new JLabel(String.valueOf(_SCORE_MIN));

    maxLabel.setHorizontalAlignment(JTextField.RIGHT);
    minLabel.setHorizontalAlignment(JTextField.RIGHT);
    scoreLabel.setHorizontalAlignment(JTextField.CENTER);

    JTextField scoreField = new JTextField(String.valueOf(_SCORE_MIN));
    _abilityScoreFields.put(attrib, scoreField);
    _abilityMaxLabel.put(attrib, maxLabel);
    _abilityMinLabel.put(attrib, minLabel);
    textPanel.add(maxLabel, BorderLayout.NORTH);
    // textPanel.add(scoreLabel, BorderLayout.WEST);
    textPanel.add(minLabel, BorderLayout.SOUTH);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BorderLayout());

    JButton upButton = new JButton("+");
    JButton downButton = new JButton("-");
    _abilityUpButtons.put(attrib, upButton);
    _abilityDownButtons.put(attrib, downButton);
    _abilityAttribUpButtonRef.put(upButton, attrib);
    _abilityAttribDownButtonRef.put(downButton, attrib);

    upButton.setPreferredSize(new Dimension(24, 24));
    downButton.setPreferredSize(new Dimension(24, 24));

    Font font = new Font("Tahoma", Font.BOLD, 24);
    upButton.setFont(font);
    downButton.setFont(font);

    upButton.setMargin(new Insets(1, 1, 1, 1));
    downButton.setMargin(new Insets(1, 1, 1, 1));

    upButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        incrementScore((JButton) event.getSource());
      }
    });

    downButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        decrementScore((JButton) event.getSource());
      }
    });
    downButton.setEnabled(false);

    font = makeRunicFont(24f);
    scoreField.setFont(font);
    scoreField.setPreferredSize(new Dimension(36, 30));
    scoreField.setHorizontalAlignment(JTextField.RIGHT);
    scoreField.setEditable(false);

    buttonPanel.add(upButton, BorderLayout.NORTH);
    buttonPanel.add(scoreField, BorderLayout.CENTER);
    buttonPanel.add(downButton, BorderLayout.SOUTH);

    JPanel returnPanel = new JPanel();
    returnPanel.setLayout(new BorderLayout());

    returnPanel.add(scoreLabel, BorderLayout.NORTH);
    returnPanel.add(textPanel, BorderLayout.WEST);
    returnPanel.add(buttonPanel, BorderLayout.EAST);

    _nhCiv.makeAbilityScoreSelector(attrib);

    returnPanel.setPreferredSize(new Dimension(70, 100));

    return returnPanel;
  }


  /**
   * Toggle the edit flag in the owner depending on unsaved changes
   * 
   * @param editState set the flag to true if there are any unsaved changes
   */
  private void setEditFlag(boolean editState)
  {
//    Mainframe mf = Mainframe.getInstance();
    // mf.setEditFlag(editState);
  }


  /**
   * Display the various kinds of messages that might happen after submitting a new Hero The type of
   * error, defined in HeroKeys.ErrorType, can be found by checking the suttle's errorType() method.
   * If an exception occurs, the exception message is found in errorMessage(); The source of error
   * for invalid data is found in errorSource().
   */
  private void showErrorMessage()
  {
    ErrorType error = _shuttle.getErrorType();
    // Find what kind of error has occurred first
    if (error == ErrorType.NULL_FIELD) {
      // Display invalid name field or...
      if (_shuttle.getErrorSource() == NewHeroFields.NAME) {
        JOptionPane.showMessageDialog(null, ERRMSG_MISSING_NAME,
            PERSON_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
      }
      // ...display invalid comboBox selection
      else {
        JOptionPane.showMessageDialog(null,
            String.format(ERRMSG_NULL_FIELD, _shuttle.getErrorSource()),
            PERSON_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
      }
    }
    // If the Person aborted, the reason will be in the errorMsg field
    else if (error == ErrorType.CREATION_EXCEPTION) {
      JOptionPane.showMessageDialog(null,
          _shuttle.getErrorMessage(), // + Chronos.NEWLINE + ERRMSG_NOT_IMPLEMENTED,
          PERSON_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }
    // Don't know what else to check, so throw a generic I-don't-know message
    else {
      JOptionPane.showMessageDialog(null, ERRMSG_UNKNOWN,
          PERSON_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }
  }


  /**
   * Pack the internal fields into a shuttle and send to the Civ to be validated.
   * 
   * @return true for valid data; else false
   */
  private boolean submit()
  {
    // Package each of these into a data shuttle
    _shuttle = packFields();
    // Call the Civ to validate; process any errocodes
    _shuttle = _nhCiv.submit(_shuttle);

    return (_shuttle.getErrorType() == ErrorType.OK) ? true : false;
  }


  /**
   * Pack the internal fields then send the new Hero data to the Civ to be validated.
   * 
   * @return data shuttle of fields for this widget
   */
  private DataShuttle<NewHeroFields> packFields()
  {
    // Package each of these into the data shuttle
    _shuttle.putField(NewHeroFields.NAME, _name);
    _shuttle.putField(NewHeroFields.GENDER, _gender);
    _shuttle.putField(NewHeroFields.HAIR_COLOR, _hairColor);
    _shuttle.putField(NewHeroFields.OCCUPATION, _occup);
    _shuttle.putField(NewHeroFields.RACENAME, _raceName);
    _shuttle.putField(NewHeroFields.KLASSNAME, NewHeroFields.KLASSNAME.getDefault());
    _shuttle.putField(NewHeroFields.ABILITY_SCORES, _nhCiv.getAllScores());
//
//    _shuttle.removeKey(NewHeroFields.HAIR_COLOR_OPTIONS);
//    _shuttle.removeKey(NewHeroFields.OCCUPATION_OPTIONS);
//    _shuttle.removeKey(NewHeroFields.RACE_OPTIONS);

    // Call the Civ to validate; process any errocodes
    return _shuttle;
  }

  /**
   * Add one to the associated score.
   * 
   * @param button - the button that was clicked.
   */
  public void incrementScore(JButton button)
  {
    String attrib = (String) _abilityAttribUpButtonRef.get(button);
    _nhCiv.increaseScore(attrib);
    refreshScoreControls();
  }


  /**
   * Decrease associated score by one.
   * 
   * @param button - the button that was clicked.
   */
  public void decrementScore(JButton button)
  {
    String attrib = (String) _abilityAttribDownButtonRef.get(button);
    _nhCiv.decreaseScore(attrib);
    refreshScoreControls();
  }

  /**
   * Updates the ranges for each ability control. Called during instantiation and when race or
   * gender changes. *
   */
  private void setScoreRanges()
  {
    _nhCiv.updateScores(_raceName, _gender);
    for (String attrib : _attributes) {
      int min = _nhCiv.getMin(attrib);
      int max = _nhCiv.getMax(attrib);
      int current = _nhCiv.getCurrent(attrib);
      int total = min + current;

      ((JLabel) _abilityMinLabel.get(attrib)).setText(String.valueOf(min));
      ((JLabel) _abilityMaxLabel.get(attrib)).setText(String.valueOf(max));

      ((JTextField) _abilityScoreFields.get(attrib)).setText(String.valueOf(total));
    }
    refreshScoreControls();
  }

  /**
   * Updates the score controls to the correct values. Called after any adjustment to scores is
   * made.
   */
  private void refreshScoreControls()
  {
    int pointsRemaining = _nhCiv.getTotalRemaining();
    boolean zeroPointsRemaining = pointsRemaining <= 0;

    _totalPointsValueTextField.setText(String.valueOf(pointsRemaining));
    _totalPointsValueTextField.setForeground(Color.black);
    if (zeroPointsRemaining) {
      _totalPointsValueTextField.setForeground(Color.red);
    }

    for (String attrib : _attributes) {
      int current = _nhCiv.getCurrent(attrib);
      int max = _nhCiv.getMax(attrib);
      int min = _nhCiv.getMin(attrib);
      int value = min + current;

      JButton up = (JButton) _abilityUpButtons.get(attrib);
      JButton down = (JButton) _abilityDownButtons.get(attrib);
      JTextField scoreField = (JTextField) _abilityScoreFields.get(attrib);

      up.setEnabled(true);
      down.setEnabled(true);
      scoreField.setForeground(Color.black);
      scoreField.setText(String.valueOf(value));

      if (value >= max || zeroPointsRemaining) {
        up.setEnabled(false);
        if (value > max) {
          scoreField.setForeground(Color.red);
        }
      }
      if (value <= min) {
        down.setEnabled(false);
        if (value < min) {
          scoreField.setForeground(Color.red);
        }
      }
    }
  }

  /**
   * Create a Runic Font.
   * 
   * @param size - the font size
   * @return Font - the runic font
   */
  private Font makeRunicFont(float size)
  {
    Font font = null;
    try {
      Font newFont = Font.createFont(Font.TRUETYPE_FONT, new File(
          Chronos.RUNIC_ENGLISH2_FONT_FILE));
      font = newFont.deriveFont(size);
    } catch (Exception e) {
      MsgCtrl.errMsgln("Could not create font: " + e.getMessage());
    }
    return font;
  }


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ INNER CLASS: ChangeKeyAction
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  // /** Indicates if any changes have been made inside a text field or text area;
  // * if so, the edit flag is set to indicate that a change has been made,
  // * so that a Save prompt can be displayed when the Panel closes.
  // */
  // class ChangeKeyListener extends KeyAdapter
  // {
  // /** Default constructor */
  // public ChangeKeyListener() { }
  //
  // /** If any key is pressed inside of a text field or text area, the edit flag is set to indicate
  // * that a change has been made.
  // * @param key the key pressed by the user.
  // */
  // public void keyPressed(KeyEvent key)
  // {
  // // Set the change flag
  // setEditFlag(true);
  // }
  //
  // } // end ChangeKeyListener


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ INNER CLASS: FocusAdapter
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /**
   * Indicates when the source component loses focus. In this case, when the Hero name JTextField
   * loses focus so that the data can be extracted. This is necessary only for widgets that cannot
   * have a default.
   */
  class FocusOffListener extends FocusAdapter
  {
    /** Default constructor creates a permanent focus event by default */
    public FocusOffListener()
    {}


    /**
     * If any focus of the source Component is lost, the data is extracted
     * 
     * @param event looking for a FOCUS_LOST event
     * @overrides FocusListener.focusLost()
     */
    public void focusLost(FocusEvent event)
    {
      MsgCtrl.traceEvent(event);
      _name = _nameField.getText().trim();
      // Set the change flag
      setEditFlag(true);
    }
  } // end FocusLostListener

} // end NewHeroPanel class

