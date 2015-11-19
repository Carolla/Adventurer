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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.EnumMap;

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
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import civ.HeroDisplayCiv;
import civ.NewHeroCiv;
import civ.NewHeroCiv.ErrorCode;
import civ.NewHeroCiv.HeroInput;
import mylib.Constants;
import mylib.MsgCtrl;
import mylib.hic.HelpKeyListener;
import net.miginfocom.swing.MigLayout;
import pdc.character.Hero;

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
 *          Nov 9 2015  // separated Mainframe and ChronsPanel converns, and their civs <br>
 */
@SuppressWarnings("serial")
public class NewHeroIPPanel extends ChronosPanel 
{
  /** Help message to show in panels */
  private final String HELP_LABEL1 =
      "<Ctrl-Tab> or mouse-click moves focus to new text area.";
  /** Help message to show in panels */
  private final String HELP_LABEL2 =
      "Press F1 key for specific help.";

  /** Replace left-side panel with this title */
  private final String NEW_HERO_TITLE = " Create Your Kind of Hero ";
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

  /** Number of columns in the person's name input window */
  private final int HERO_NAME_WIDTH = 45;
  /** Space between buttons */
  private final String SPACER = "          ";

  /** Title for error message dialog */
  private final String HERO_ERROR_TITLE = "COULDN'T CREATE HERO";
  /** Error message when name (required field) is omitted */
  private final String ERRMSG_NAME_MISSING = "Your Hero must have a name!";
  /** Error message when namefield is too long */
  private final String ERRMSG_NAME_TOO_LONG =
      "Your Hero's name is too long (45 char limit). \nTry perhaps your Hero's nickname?";
      // /** Error message when an unknown error has occurred */
      // private final String ERRMSG_UNKNOWN =
      // "Hero could not be created for some unanticipated reason";

  // TODO: Constant.MY_BROWN needs to be brightened here for some reason. Perhaps a background panel
  // is affecting it?
  /** Background color inherited from parent */
  private Color _backColor = Constants.MY_BROWN;

  /** Input data from user */
  private String _name;
  private String _gender;
  private String _hairColor;
  private String _raceName;
  private String _klassName;

  /** Contains user input field data */
  EnumMap<HeroInput, String> _input;


  /**
   * Captures the name text for the hero and the window title; Needed as attribute because data is
   * extracted outside actionListener
   */
  private JTextField _nameField = null;

  /** Associated validating CIV object */
  private NewHeroCiv _nhCiv;
  private MainframeInterface _mf;

  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /**
   * Creates the panel format and places the action components (e.g., radio buttons and drop down
   * boxes). Also creates the associated CIV object to manage the data. The action components are
   * created in private helper methods.
   * 
   * @param nhCiv controls this ChronosPanel
   * @param mf  mainframe reference needed for displaying the panel
   */
  public NewHeroIPPanel(NewHeroCiv nhCiv, MainframeInterface mf) 
  {
    super(nhCiv);
    setTitle(NEW_HERO_TITLE);
    _nhCiv = nhCiv;
    _mf = mf;
    
    // GENERAL SETUP
    // Set the preferred and max size, adjusting for panel border thickness
    int width = Mainframe.getWindowSize().width;// +MainFrame.PANEL_SHIFT;
    int height = Mainframe.getWindowSize().height;
    setPreferredSize(new Dimension(width, height));

    int pad = Mainframe.PAD;
    Border matte = BorderFactory.createMatteBorder(pad, pad, pad, pad, Color.WHITE);
    // Border titledBorder = BorderFactory.createTitledBorder(matte, PANEL_TITLE,
    // TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
    // setBorder(titledBorder);
    setBorder(matte);
    // _backColor = Constants.MY_BROWN;
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

    // Create the input text field to collect the Hero's name give it default focus
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
    add(new JLabel(HERO_KLASS_PROMPT), "push, align center, wrap");

    /* Add the Race drop-down combo */
    add(makeRaceCombo(), "push, align center");
    /* Add the Occupation drop-down combo */
    add(makeKlassCombo(), "push, align center, wrap");

    /* Add a button panel containing the Submit and Cancel buttons */
    add(makeButtonPanel(), "push, align center, span, gaptop 20%");
    
  } // end NewHeroIPPanel constructor


  // ============================================================
  // Public Methods
  // ============================================================

  
  /**
   * The name text field should have the default focus, but that cannot be done until after the
   * panel is realized and visible. Therefore, the NewHeroPanel's caller (MenuBar.NEW Action) must
   * invoke the default component. It calls this method to get which one should be default. This
   * method is created for this because the caller does not know what fields are available in this
   * panel, and it can change with maintenance.
   */
  public void setDefaultFocus()
  {
    _nameField.requestFocusInWindow();
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
    // Clear editFlag and data, then return back to main action panel if Cancel is pressed
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        setVisible(false);
        _nhCiv.back();
      }
    });

    // Create the SUBMIT button
    JButton submitButton = new JButton("SUBMIT");

    // Display error message if received from submit button, or new Hero if OK
    submitButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        // Call the Civ to validate the attributes. If no errors, Hero is created and displayed
        ErrorCode err = submit();
        if (err == ErrorCode.NO_ERROR) {
          setVisible(false);
          // Create the new Hero and display it
           Hero hero = _nhCiv.createHero(_input);
           HeroDisplayCiv hDispCiv = new HeroDisplayCiv(_mf);
           hDispCiv.displayHero(hero, true);    // first time Hero needs true arg
        } else {
          // Display the message
          showErrorMessage(err);
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

    // Create the radio button group
    ButtonGroup groupSex = new ButtonGroup();

    // Define the male radio button (default to Male button selected)
    JRadioButton maleButt = new JRadioButton("Male", true);
    // Default is set in case user does not touch the radio buttons
    _gender = "Male";
    maleButt.setEnabled(true);
    maleButt.setBackground(_backColor);

    // Allow user to explicitly set to Male
    maleButt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        MsgCtrl.traceEvent(event);
        _gender = "Male";
        setEditFlag(true);
      }
    });

    // Define the female radio button
    JRadioButton femaleButt = new JRadioButton("Female", false);
    femaleButt.setBackground(_backColor);
    // Allow user to explicitly set Female gender
    femaleButt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        MsgCtrl.traceEvent(event);
        _gender = "Female";
        setEditFlag(true);
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
    // Populate the drop down selection from CIV data
    final JComboBox hairCombo = new JComboBox(_nhCiv.getHairColors());
    // Build the box with label
    hairCombo.setEditable(false);
    hairCombo.setBackground(Color.WHITE);
    hairCombo.addKeyListener(new HelpKeyListener("HairColor"));

    // Set default hair color in case user does not select anything
    _hairColor = (String) hairCombo.getItemAt(0);

    // Change the visible selection and capture the data
    hairCombo.addActionListener(new ActionListener() {
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
   * Create a combo box of Guilds (Klasses) that the Hero may want to be
   * 
   * @return the JComboBox of Guild options
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private JComboBox makeKlassCombo()
  {
    // Build the box with label, using Civ data
    final JComboBox klassCombo = new JComboBox(_nhCiv.getKlasses());
    klassCombo.setEditable(false);
    klassCombo.setBackground(Color.WHITE);
    klassCombo.addKeyListener(new HelpKeyListener("HeroKlass"));

    // Set default Klass in case user does not select anything
    _klassName = (String) klassCombo.getItemAt(0);

    // Change the visible selection and capture the data
    klassCombo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        // Audit statement
        MsgCtrl.traceEvent(event);
        _klassName = ((String) klassCombo.getSelectedItem()).trim();
        // Set the edit change flag
        setEditFlag(true);
      }
    });
    return klassCombo;
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
  
    // Create DocumentFilter for restricting input length
    AbstractDocument d = (AbstractDocument) _nameField.getDocument();
    d.setDocumentFilter(new NewHeroIPPanel.NameFieldLimiter());
  
    // Set name of the field
    _nameField.setName("heroName");
    // Collect the name when the text field goes out of focus
    _nameField.addFocusListener(new FocusOffListener());
  
    // Extract Hero's name and update Hero's name into MainFrame Title
    // if Enter key is hit or text field loses focus.
    _nameField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
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
   * Create a combo box of Races that the Hero may be
   * 
   * @return the JComboBox of Race options
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private JComboBox makeRaceCombo()
  {
    // Build the box with label
    final JComboBox raceCombo = new JComboBox(_nhCiv.getRaces());
    raceCombo.setEditable(false);
    raceCombo.setBackground(Color.WHITE);
    raceCombo.addKeyListener(new HelpKeyListener("HeroRace"));

    // Set default Race in case user does not select anything
    _raceName = (String) raceCombo.getItemAt(0);

    // Change the visible selection; data is captured elsewhere
    raceCombo.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        // Audit statement
        MsgCtrl.traceEvent(event);
        _raceName = ((String) raceCombo.getSelectedItem()).trim();
        // Set the edit change flag
        setEditFlag(true);
      }
    });
    return raceCombo;
  }

  /**
   * Toggle the edit flag in the owner depending on unsaved changes
   * 
   * @param editState set the flag to true if there are any unsaved changes
   */
  private void setEditFlag(boolean editState)
  {
    // Mainframe mf = Mainframe.getInstance();
    // mf.setEditFlag(editState);
  }

  /** Set the title for this panel */
  @Override
  public void setTitle(String title)
  {
    super._title = NEW_HERO_TITLE;
  }

  
  /**
   * Display the error message received after submitting a new Hero.
   */
  private void showErrorMessage(ErrorCode error)
  {
    // Display missing name error, then set control to name field
    if (error == ErrorCode.NAME_MISSING) {
      JOptionPane.showMessageDialog(null, ERRMSG_NAME_MISSING,
          HERO_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
      _nameField.requestFocusInWindow();
    }
    // Display overly long name error, then set control to name field
    else if (error == ErrorCode.NAME_TOO_LONG) {
      JOptionPane.showMessageDialog(null, ERRMSG_NAME_TOO_LONG,
          HERO_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
      _nameField.requestFocusInWindow();
    }

  }

  /**
   * Get an empty enumMap, pack the input fields into it, and send it to the Civ for validation.
   * 
   * @return One of the ErrorCode enum values (NO_ERROR if all went well)
   */
  private ErrorCode submit()
  {
    _input = _nhCiv.getEmptyMap();
    _input.put(HeroInput.NAME, _name);
    _input.put(HeroInput.GENDER, _gender);
    _input.put(HeroInput.HAIR, _hairColor);
    _input.put(HeroInput.RACE, _raceName);
    // Rogue is the pseudonym for the Thief class
    _klassName = (_klassName.equalsIgnoreCase("Rogue")) ? _klassName = "Thief" : _klassName;
    _input.put(HeroInput.KLASS, _klassName);

    // Call the Civ to validate. If good, Civ creates the Hero; else display error widget
    return _nhCiv.validate(_input);
  }


  // /**
  // * Create a Runic Font.
  // *
  // * @param size - the font size
  // * @return Font - the runic font
  // */
  // private Font makeRunicFont(float size)
  // {
  // Font font = null;
  // try {
  // Font newFont = Font.createFont(Font.TRUETYPE_FONT, new File(
  // Chronos.RUNIC_ENGLISH2_FONT_FILE));
  // font = newFont.deriveFont(size);
  // } catch (Exception e) {
  // MsgCtrl.errMsgln("Could not create font: " + e.getMessage());
  // }
  // return font;
  // }


  // ================================================================
  // INNER CLASS: ChangeKeyAction
  // ================================================================

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


  // =======================================================
  // INNER CLASS: FocusAdapter
  // =======================================================
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


  // =======================================================
  // INNER CLASS: NameFieldLimiter
  // =======================================================
  /**
   * Replaces the default DocumentFilter for the input value of the Hero's name field. It limits the
   * allowed input to the length specified by HERO_NAME_WIDTH and causes an audio "beep" to be
   * sounded by the system when more input is attempted after the limit is reached.
   * 
   * @author OG
   *
   */
  private class NameFieldLimiter extends DocumentFilter
  {
    // @Override
    // public void insertString(DocumentFilter.FilterBypass fb, int offset, String string,
    // AttributeSet attr) throws BadLocationException
    // {
    // if(fb.getDocument().getLength()+string.length()>HERO_NAME_WIDTH)
    // {
    // System.out.println("Insert String: Name Field Limit Reached");
    // return;
    // }
    //
    // fb.insertString(offset, string, attr);
    // }

    @Override
    public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
        throws BadLocationException
    {
      System.out.println("Remove");
      fb.remove(offset, length);
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
        AttributeSet attrs) throws BadLocationException
    {
      if (fb.getDocument().getLength() + text.length() > HERO_NAME_WIDTH) {
        System.out.println("Replace: Name Field Limit Reached");
        java.awt.Toolkit.getDefaultToolkit().beep();
        return;
      }
      fb.insertString(offset, text, attrs);
    }
  } // end NameFieldLimiter

} // end NewHeroIPPanel class


