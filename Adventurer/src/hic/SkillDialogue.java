/**
 * SkillDialogue.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package hic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import mylib.MsgCtrl;
import mylib.civ.DataShuttle;
import net.miginfocom.swing.MigLayout;
import chronos.civ.SkillKeys;
import civ.SkillDisplayCiv;

// /** Allows the user to show their Person's many attributes.

/**
 * @author Timothy Armstrong
 * @version Aug 15 2011 // original <br>
 */
public class SkillDialogue extends JDialog
{
  /**
   * Auto-generated UID
   */
  private static final long serialVersionUID = 43358026325695124L;
  // /** Help message to show in panels */
  // private final String HELP_LABEL = "Press F1 key for help.";
  //
  // // Specific file error messages not handled by FileChooser
  // private final String SAVE_ERROR_MSG = "Error! Problem saving ";
  // private final String SAVE_ERROR_TITLE = "FILE SAVE ERROR";
  // private final String CONFIRM_SAVE_MSG = " is put asleep until later.";
  // private final String CONFIRM_SAVE_TITLE = " Hero is now Registered";
  //
  // /** Tab sizing between inventory columns */
  // private final int TAB_SIZE = 6;
  // /** Height needed for the Save and Cancel buttons */
  // private final int BUTTON_HT = 50;
  // /** Height of font for vertical spacing */
  // private final int FONT_HT = 14;
  // /** Border width for main panel */
  private final int SCROLLBAR_SIZE = 20;
  // /** Standard height of font and cells in display, but can be changed */
  // private int CELL_HEIGHT = 22;
  // /** Standard height of font and cells in display, but can be changed */
  // private int CELL_WIDTH = 12;
  /** Border width for main panel */
  private final int THICK_BORDER = SkillMainFrame.PAD;
  /** Border width for subpanel */
  private final int THIN_BORDER = SkillMainFrame.PAD / 2;
  /** Set the max width of the hero panel at half screen */
  final int PANEL_WIDTH = SkillMainFrame.USERWIN_WIDTH / 2;
  /** Set the width of the data panels within the display borders */
  final int DATA_WIDTH = PANEL_WIDTH - SCROLLBAR_SIZE - 2
      * (THICK_BORDER + THIN_BORDER);
  // /** Keep a reference to this scrollpane that contains this display*/
  // private JScrollPane _heroScroll = null;

  // /** HelpDialog reference for all widgets that have context help */
  // private HelpDialog _help = null;
  // /** Set the default HelpKey for the general panel */
  // private HelpKeyListener _helpKey = new HelpKeyListener("HeroDsp");

  /** Preferred (calculated) width for HeroDisplay */
  int _panelWidth = 0;
  /** Preferred (calculated) height for HeroDisplay */
  int _panelHeight = 0;

  /** Button to delete a skill - disabled initially */
  private JButton _deleteButton = null;
  /** Cancel button - exit without any changes */
  private JButton _cancelButton = null;
  /** Saves a skill to the registry, prompting if pre-existing skill */
  private JButton _saveButton = null;
  /** Disable SaveAs at some time . */
  // private JButton _refreshButton = null;
  // /** Background color inherited from parent */
  private Color _backColor = null;

  /** The backend CIV for this JPanel widget */
  private SkillDisplayCiv _sdCiv = null;

  // /** Keys to Hero data to be displayed */
  // EnumMap<SkillKeys, String> _attribs = null;
  /** Keys to Items data to be displayed */
  DataShuttle<SkillKeys> _ws = null;

  /** Selected item in Skill field */
  private String _skillSelected = null;

  /** Selected item in Race field */
  private String _raceSelected = null;

  /** Selected item in Klass field */
  private String _klassSelected = null;

  /** String array for the Klasses */
  private ArrayList<String> _klasslist = null;

  /** String array for the Races */
  private ArrayList<String> _racelist = null;

  /** String array for the list of Skills */
  private ArrayList<String> _skilllist = null;

  /** Skill name */
  private String _skillName = "";

  /** Skill description */
  private String _skillDesc = "";

  /** Skill action */
  private String _skillAction = "None";

  /** Panel for entering skill action */
  private JPanel _actionPanel;

  /** Panel to hold the buttons (Save, Delete, Refresh, Delete) */
  private JPanel _buttonPanel;

  /** Panel to hold/enter the skill description */
  private JPanel _descPanel;

  /** Panel to hold the klasslist/klass selected */
  @SuppressWarnings("rawtypes")
  private JComboBox _klassBox;

  /** Panel to hold the skill name */
  private JPanel _namePanel;

  /** Combo box of races */
  @SuppressWarnings("rawtypes")
  private JComboBox _raceBox;

  /** Panel to hold the skill list */
  private JPanel _skillPanel;

  /** Text for action */
  private JTextArea _actionText;

  /** Text for name of skill */
  private JTextField _nameText;

  /** Text for description */
  private JTextArea _descText;

  /** Panel for displaying racelist */
  private JPanel _racePanel;

  /** Combo box for skill list */
  @SuppressWarnings("rawtypes")
  private JComboBox _skillBox;

  /** Owner of this frame */
  private JFrame _owner;

  /** Panel to hold klass information */
  private JPanel _klassPanel;

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /** Default constuctor */
  public SkillDialogue()
  {}

  /**
   * Creates the dialogue fields
   * 
   * @param owner Owner frame for display of messages
   */
  public SkillDialogue(JFrame owner)
  {
    super(owner, "Skill Dialogue", true);

    // Save the calling civ for later callback
    _sdCiv = new SkillDisplayCiv();
    _owner = owner;

    // this.addWindowListener(new WindowAdapter() {
    // @Override
    // public void windowClosing(WindowEvent e)
    // {
    // int state = new SkillDisplayCiv().saveRegistry();
    // if (state > 0)
    // {
    // JOptionPane.showMessageDialog(_owner, _sdCiv.getSkills().size() +
    // " skills saved to the registry", "Save successful",
    // JOptionPane.WARNING_MESSAGE);
    // }
    // else
    // {
    // JOptionPane.showMessageDialog(_owner, "Error saving registry",
    // "Error", JOptionPane.WARNING_MESSAGE);
    // }
    // }
    // });
    // GENERAL SETUP
    buildFrame();
  }

  /** Draws the panel from scratch */
  private void buildFrame()
  {
    _ws = _sdCiv.getDefaults();
    unpackShuttle(_ws);

    // GENERAL SETUP
    // Set the preferred and max size
    Container contentPane = getContentPane();
    contentPane.removeAll();

    // Set size/colors
    _panelWidth = 640; // 2*screensize.width/3;
    _panelHeight = 320; // 2*screensize.height/3;
    setSize(new Dimension(_panelWidth, _panelHeight));
    _backColor = SkillMainFrame.PANEL_COLOR;
    setBackground(Color.ORANGE);
    contentPane.setBackground(Color.ORANGE);

    // Set the layout
    MigLayout layout = new MigLayout("", "[] 20 [] [] []",
        "[] 10 [] 10 [] 30 []");
    contentPane.setLayout(layout);

    // Row 1
    _skillPanel = buildSkillComboBox(_skilllist
        .toArray(new String[_skilllist.size()]));
    contentPane.add(_skillPanel);
    _namePanel = buildNamePanel("Skill Name");
    contentPane.add(_namePanel, "wrap");

    // Row 2
    _racePanel = buildRaceComboBox(_racelist);
    contentPane.add(_racePanel);
    _descPanel = buildDescPanel("Description");
    contentPane.add(_descPanel, "wrap");

    // Row 3
    _klassPanel = buildKlassComboBox(_klasslist);
    contentPane.add(_klassPanel);
    _actionPanel = buildActionPanel("Action");
    contentPane.add(_actionPanel, "wrap");

    // Row 4 - Button panel
    _buttonPanel = buildButtonPanel();
    contentPane.add(_buttonPanel, "span");

    contentPane.validate();
    contentPane.repaint();

    // JOptionPane.showMessageDialog(_owner,
    // "Skills are not saved (to disk) until "
    // + "the dialogue is closed.\n"
    // + "The cancel button will exit without changes "
    // + "at any time", "Notice!", JOptionPane.WARNING_MESSAGE);

  } // end SkillDialogue constructor

  /**
   * Makes the panel for the name space
   * 
   * @param label Text displayed for the field
   * @return the attribute grid panel
   */
  private JPanel buildActionPanel(String label)
  {
    JPanel actionPanel = new JPanel();
    actionPanel.setBackground(_backColor);
    actionPanel.setLayout(new BorderLayout());

    // Ensure that the attribute panel does not exceed the HeroDisplay panel
    // width
    actionPanel.setMaximumSize(new Dimension(_panelWidth, _panelHeight));
    actionPanel.add(new JLabel(label), BorderLayout.NORTH);

    _actionText = new JTextArea(4, 35);
    _actionText.setLineWrap(true);
    _actionText.setWrapStyleWord(true);
    _actionText.setText(_skillAction);
    _actionText.setEnabled(false);
    actionPanel.add(_actionText, BorderLayout.CENTER);
    return actionPanel;
  } // End of buildActionPanel

  /**
   * Add Save and Cancel buttons to the display next
   * 
   * @return button panel
   */
  private JPanel buildButtonPanel()
  {
    // NOTE: Save action is invoked for Skill */
    _saveButton = new JButton("Save");
    _saveButton.setBackground(Color.white);
    _saveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        MsgCtrl.traceEvent(event);
        doSave();
      }
    });

    // Add Delete button */
    _deleteButton = new JButton("Delete");
    _deleteButton.setBackground(Color.white);
    _deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        MsgCtrl.traceEvent(event);
        doDelete();
      }
    });

    /* Add Cancel button */
    _cancelButton = new JButton("Cancel");
    _cancelButton.setBackground(Color.white);

    // Clear data and return back to mainframe if Cancel is pressed
    _cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        MsgCtrl.traceEvent(event);
        doCancel();

      }
    });

    // /* Add Refresh button */
    // _refreshButton = new JButton("Refresh");
    // _refreshButton.setBackground(Color.white);
    // _refreshButton.addActionListener(new ActionListener()
    // {
    // public void actionPerformed(ActionEvent event)
    // {
    // MsgCtrl.traceEvent(event);
    // doRefresh(parent);
    // getContentPane().setVisible(true);
    // }
    // });

    // Add buttons to buttonPanel, and buttonPanel to basePanel
    JPanel buttonPanel = new JPanel(new MigLayout("insets1, wrap 4"));
    buttonPanel.setBackground(_backColor);
    // buttonPanel.setMaximumSize(new Dimension(DATA_WIDTH, BUTTON_HT));

    // Add small space at the top of the button panel
    // buttonPanel.add(_refreshButton, "width :100:, gap 10");
    buttonPanel.add(_saveButton, "width :100:, gap 70");
    // _saveButton.setEnabled(false);
    buttonPanel.add(_deleteButton, "width :100:, gap 90");
    _deleteButton.setEnabled(false);
    buttonPanel.add(_cancelButton, "width :100:, gap 90");

    return buttonPanel;
  }

  /**
   * Makes the panel for the name space
   * 
   * @param label Text displayed for the field
   * @return the attribute grid panel
   */
  private JPanel buildDescPanel(String label)
  {
    JPanel descPanel = new JPanel();// (new MigLayout(
    // "fill, wrap 1", // layout constraints
    // "[right]", // align right
    // "[top]" )); // align top
    descPanel.setLayout(new BorderLayout());
    descPanel.setBackground(_backColor);
    // Ensure that the attribute panel does not exceed the HeroDisplay panel
    // width
    descPanel.setMaximumSize(new Dimension(_panelWidth, _panelHeight));
    descPanel.add(new JLabel(label), BorderLayout.NORTH);

    _descText = new JTextArea(3, 35);
    _descText.setLineWrap(true);
    _descText.setWrapStyleWord(true);
    _descText.setText(_skillDesc);
    descPanel.add(_descText, BorderLayout.CENTER);
    return descPanel;
  } // End of buildDescPanel

  /**
   * Make a panel for the drop down information from Klasslist
   * 
   * @param klasslist Names of all available Klasses
   * @return the panel populated
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private JPanel buildKlassComboBox(ArrayList<String> klasslist)
  {
    _klassPanel = new JPanel(new BorderLayout());
    _klassPanel.setBackground(_backColor);
    JLabel klassLabel = new JLabel("Apply to Klass");
    _klassBox = new JComboBox(
        klasslist.toArray(new String[klasslist.size()]));
    _klassBox.setSelectedIndex(0);
    _klassBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        JComboBox cmb = (JComboBox) e.getSource();
        _klassSelected = (String) cmb.getSelectedItem();
      }

    });
    _klassPanel.add(klassLabel, BorderLayout.NORTH);
    _klassPanel.add(_klassBox, BorderLayout.SOUTH);
    return _klassPanel;
  }

  /**
   * Makes the panel for the name space
   * 
   * @param label Text displayed for the field
   * @return the attribute grid panel
   */
  private JPanel buildNamePanel(String label)
  {
    JPanel namePanel = new JPanel(new MigLayout());
    namePanel.setBackground(_backColor);
    namePanel.add(new JLabel(label));
    _nameText = new JTextField(30);
    _nameText.setText(_skillName);
    // _nameText.addActionListener(new ActionListener()
    // {
    // public void actionPerformed(ActionEvent e)
    // {
    // //JComboBox cmb = (JComboBox) e.getSource();
    // _saveButton.setEnabled(true);
    // }
    //
    // });
    namePanel.add(_nameText, "growx");
    return namePanel;
  } // End of buildNamePanel

  /**
   * Make a panel for the drop down information from Racelist
   * 
   * @param racelist Names of all available Races
   * @return the panel populated
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private JPanel buildRaceComboBox(ArrayList<String> racelist)
  {
    _raceBox = new JComboBox(racelist.toArray(new String[racelist.size()]));
    _racePanel = new JPanel(new BorderLayout());
    _racePanel.setBackground(_backColor);
    JLabel raceLabel = new JLabel("Apply to Race");
    _raceBox.setSelectedIndex(0);
    _raceBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        JComboBox cmb = (JComboBox) e.getSource();
        _raceSelected = (String) cmb.getSelectedItem();
      }
    });
    _racePanel.add(raceLabel, BorderLayout.NORTH);
    _racePanel.add(_raceBox, BorderLayout.SOUTH);
    return _racePanel;
  }

  /**
   * Make a panel for the drop down information from Skilllist
   * 
   * @param skilllist Names of all available Skills
   * @return the panel populated
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private JPanel buildSkillComboBox(String[] skilllist)
  {
    JPanel skillPanel = new JPanel(new BorderLayout());
    skillPanel.setBackground(_backColor);
    JLabel skillLabel = new JLabel("Select Skill");
    _skillBox = new JComboBox(skilllist);
    _skillBox.setSelectedIndex(0);
    _skillBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
        JComboBox cmb = (JComboBox) e.getSource();
        _skillSelected = (String) cmb.getSelectedItem();
        doRefresh();
      }

    });
    skillPanel.add(skillLabel, BorderLayout.NORTH);
    skillPanel.add(_skillBox, BorderLayout.SOUTH);
    return skillPanel;
  }

  /** Button action for Refresh */
  private void doCancel()
  {
    // Do what you need to do to load a skill
    // JOptionPane.showMessageDialog(parent, "Cancel button pressed");
    System.exit(0);
  }

  /** Button action for Refresh */
  private void doDelete()
  {
    if (_skillSelected == null) {
      return;
    }
    int deleteState = 0;
    // Do what you need to do to delete a skill

    Object[] options = {"Yes", "No"};
    int deletePrompt = JOptionPane.showOptionDialog(_owner,
        "You are about to delete a skill: " + _skillSelected,
        "Confirm delete", JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE, null, // do not use a custom Icon
        options, // the titles of buttons
        options[1]); // default button title

    if (deletePrompt == 0) {
      deleteState = _sdCiv.delete(_skillSelected);
    } else {
      JOptionPane.showMessageDialog(_owner, "Delete cancelled", "Cancel",
          JOptionPane.WARNING_MESSAGE);
    }

    if (deleteState > 0) {
      // Despite better judgment, save immediately to disk
      JOptionPane.showMessageDialog(_owner, "There are now "
          + deleteState + " skills in the registry.", "Skill saved",
          JOptionPane.PLAIN_MESSAGE);
    } else if (deleteState < 0) {
      if (deleteState == SkillDisplayCiv.ASSOCATION_ERROR) {
        JOptionPane
            .showMessageDialog(
                _owner,
                "Skill has association with an occupation.\n\n"
                    + "It cannot be deleted until the occupation is\n"
                    + "deleted or associated with another skill",
                "Error", JOptionPane.ERROR_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(_owner, _ws.getErrorMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }

    // Update display of skill
    buildFrame();
  }

  /** Button action for Refresh */
  private void doRefresh()
  {
    if (_skillSelected == null) {
      return;
    } else if (_skillSelected == "NEW") {
      _deleteButton.setEnabled(false);
      // _saveButton.setEnabled(false);
      _skillName = "";
      _skillDesc = "";
      _skillAction = "Not yet implemented";
      _raceSelected = "All";
      _klassSelected = "All";
    } else {
      if (_sdCiv.getSkill(_skillSelected) != null) {
        // Do what you need to do to load a skill
        _deleteButton.setEnabled(true);
        _saveButton.setEnabled(true);
        _ws = _sdCiv.getSkill(_skillSelected);
        unpackShuttle(_ws);
      }
    }
    refreshDisplay();
  }

  /** Button action for Save */
  private void doSave()
  {
    if (_nameText == null) {
      return;
    }

    // Do what you need to do to save a skill
    packShuttle();

    int saveState = 0;
    _sdCiv.submit(_ws);
    if (_ws.getErrorType() == DataShuttle.ErrorType.OK) {
      saveState = _sdCiv.save(_ws);
    } else {
      // Check error conditions
      int overwritePrompt = -1;
      if (_ws.getErrorMessage() == "Skill already exists") {
        Object[] options = {"Yes", "No"};
        overwritePrompt = JOptionPane.showOptionDialog(_owner,
            "Skill already exists, would you like to overwrite?",
            "Skill Exists", JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, // do not use a
            // custom Icon
            options, // the titles of buttons
            options[1]); // default button title

        // Now read overwrite option and execute
        if (overwritePrompt == 0) {
          _sdCiv.delete((String) _ws.getField(SkillKeys.NAME));
          saveState = _sdCiv.save(_ws);
        } else {
          JOptionPane.showMessageDialog(_owner, "Overwite cancelled",
              "Cancel", JOptionPane.WARNING_MESSAGE);
        }
      } else {
        JOptionPane.showMessageDialog(_owner, _ws.getErrorMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }

    if (saveState > 0) {
      // Despite better judgment, save immediately to disk

      JOptionPane.showMessageDialog(_owner, "There are now " + saveState
          + " skills in the registry.", "Skill saved",
          JOptionPane.PLAIN_MESSAGE);
    } else if (saveState < 0) {
      JOptionPane.showMessageDialog(_owner, _ws.getErrorMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
    }

    String selected = _skillSelected;
    // Update display of skill afterwards
    buildFrame();

    _skillSelected = selected;
    doRefresh();
  }

  /** Refreshes the display */
  private void refreshDisplay()
  {
    _actionText.setText(_skillAction);
    _nameText.setText(_skillName);
    _descText.setText(_skillDesc);
    _raceBox.setSelectedIndex(_racelist.indexOf(_raceSelected));
    _klassBox.setSelectedIndex(_klasslist.indexOf(_klassSelected));
    // _skillBox = new JComboBox(_skilllist.toArray(new
    // String[_skilllist.size()]));
    _skillBox.setSelectedIndex(_skilllist.indexOf(_skillSelected));

    // getContentPane().validate();
    // getContentPane().repaint();
  }

  // /** Set the hero display content panels inside a viewable scroll pane
  // * @return HeroDisplay in a (vertical) scrollable pane
  // */
  // private JScrollPane makeScrollable()
  // {
  // // Reset the panel size based on the size of the constituent components
  // int finalHt = getPreferredSize().height;
  // setPreferredSize(new Dimension(DATA_WIDTH, finalHt));
  //
  // /* Put the panel in the viewport, and put the viewport in the scrollpane
  // * If everything sizes correctly, the horizontal scrollbar will not be
  // needed but
  // * it set to show the mis-sizing that might occur */
  // JScrollPane sp = new JScrollPane(this,
  // ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
  // ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
  // sp.setViewportView(this);
  // // Resize the view to include the data width, borders, and vertical
  // scrollbar
  // sp.setPreferredSize(new Dimension(PANEL_WIDTH, finalHt));
  //
  // // Reset the client to the top of the view (instead of the bottom)
  // // ??
  //
  // return sp;
  // }

  // /** Pack the current data into a shuttle for submission to the CIV */
  // private DataShuttle<SkillKeys> packShuttle() {
  // _ws.putField(SkillKeys.NAME, _nameText.getText());
  // _ws.putField(SkillKeys.DESC, _descText.getText());
  // _ws.putField(SkillKeys.ACTION, _actionText.getText());
  // _ws.putField(SkillKeys.RACE, _raceSelected);
  // _ws.putField(SkillKeys.KLASS, _klassSelected);
  // return _ws;
  // }

  /** Pack the current data into a shuttle for submission to the CIV */
  private DataShuttle<SkillKeys> packShuttle()
  {
    return _ws;
  }

  private void unpackShuttle(DataShuttle<SkillKeys> ds)
  {
    // Unpack the data shuttle into individual pieces of info
    // _skillName = (String) ds.getField(SkillKeys.NAME);
    // _skillDesc = (String) ds.getField(SkillKeys.DESC);
    // _skillAction = (String) ds.getField(SkillKeys.ACTION);
    // _raceSelected = (String) ds.getField(SkillKeys.RACE);
    // _klassSelected = (String) ds.getField(SkillKeys.KLASS);
    // _klasslist = (ArrayList<String>) ds.getField(SkillKeys.KLASSLIST);
    // _racelist = (ArrayList<String>) ds.getField(SkillKeys.RACELIST);
    // _skilllist = (ArrayList<String>) ds.getField(SkillKeys.SKILLLIST);
  }
} // end HeroDisplay class

