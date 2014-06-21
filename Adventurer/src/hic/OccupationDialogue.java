/**
 * OccupationDialogue.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package hic;

import civ.OccupationDisplayCiv;

import chronos.civ.OccupationKeys;

import mylib.MsgCtrl;
import mylib.civ.DataShuttle;

import net.miginfocom.swing.MigLayout;

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

///** Allows the user to show their Person's many attributes. 

/**
 * @author Timothy Armstrong
 * @version <DL>
 *          <DT>Build 1.0 Sept 12 2011 // original
 *          <DD>
 *          </DL>
 */
@SuppressWarnings("serial")
public class OccupationDialogue extends JDialog {
	// /** Help message to show in panels */
	// private final String HELP_LABEL = "Press F1 key for help.";
	private final int SCROLLBAR_SIZE = 20;

	/** Border width for main panel */
	private final int THICK_BORDER = SkillMainFrame.PAD;
	/** Border width for subpanel */
	private final int THIN_BORDER = SkillMainFrame.PAD / 2;
	/** Set the max width of the hero panel at half screen */
	final int PANEL_WIDTH = SkillMainFrame.USERWIN_WIDTH / 2;
	/** Set the width of the data panels within the display borders */
	final int DATA_WIDTH = PANEL_WIDTH - SCROLLBAR_SIZE - 2
			* (THICK_BORDER + THIN_BORDER);

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

	private Color _backColor = null;

	/** The backend CIV for this JPanel widget */
	private OccupationDisplayCiv _occCiv = null;

	/** Keys to Items data to be displayed */
	DataShuttle<OccupationKeys> _ws = null;

	/** Selected item in Occupation field */
	private String _occSelected = null;

	/** Selected item in Skill field */
	private String _skillSelected = null;
	//
	// /** Selected item in Klass field */
	// private String _klassSelected = null;
	//
	// /** String array for the Klasses */
	// private ArrayList<String> _klasslist = null;
	//
	// /** String array for the Races */
	// private ArrayList<String> _racelist = null;

	/** String array for the list of Skills */
	private ArrayList<String> _skilllist = null;

	/** String array for the list of Occupations */
	private ArrayList<String> _occList = null;

	/** Occupation name */
	private String _occName = "";

	/** Occupation description */
	private String _occDesc = "";

	/** Panel to hold the buttons (Save, Cancel, Delete) */
	private JPanel _buttonPanel;

	/** Panel to hold/enter the Occupation description */
	private JPanel _descPanel;

	/** Panel to hold the occupation list/occupation selected */
	@SuppressWarnings("rawtypes")
    private JComboBox _occBox;

	/** Panel to hold the skill name */
	private JPanel _namePanel;
	//
	// /** Combo box of races */
	// private JComboBox _raceBox;

	/** Panel to hold the skill list */
	private JPanel _skillPanel;

	/** Panel to hold the occupation list */
	private JPanel _occPanel;

	/** Text for name of skill */
	private JTextField _nameText;

	/** Text for description */
	private JTextArea _descText;
	//
	// /** Panel for displaying racelist */
	// private JPanel _racePanel;

	/** Combo box for skill list */
	@SuppressWarnings("rawtypes")
    private JComboBox _skillBox;

	/** Owner of this frame */
	private JFrame _owner;

	//
	// /** Panel to hold klass information */
	// private JPanel _klassPanel;

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ CONSTRUCTOR(S) AND
	 * RELATED METHODS ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 */

	/** Default constuctor */
	public OccupationDialogue() {
	}

	/**
	 * Creates the dialogue fields
	 * 
	 * @param owner
	 *            Owner frame for display of messages
	 */
	public OccupationDialogue(JFrame owner) {
		super(owner, "Occupation Dialogue", true);

		// Save the calling civ for later callback
		_occCiv = new OccupationDisplayCiv();
		_owner = owner;

		// GENERAL SETUP
		buildFrame();
	}

	/** Draws the panel from scratch */
	private void buildFrame() {
		_ws = _occCiv.getDefaults();
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
		MigLayout layout = new MigLayout("", "[] 10 [] [] []", "[] 10 [] [] []");
		contentPane.setLayout(layout);

		// Row 1
		_occPanel = buildOccupationComboBox(_occList
				.toArray(new String[_occList.size()]));
		contentPane.add(_occPanel);
		_namePanel = buildNamePanel("Occupation Name");
		contentPane.add(_namePanel, "wrap");

		// Row 2
		_skillPanel = buildSkillComboBox(_skilllist
				.toArray(new String[_skilllist.size()]));
		contentPane.add(_skillPanel);
		_descPanel = buildDescPanel("Description");
		contentPane.add(_descPanel, "wrap");
		_descText.setText("Not implemented yet");

		// Row 3 - Button panel
		_buttonPanel = buildButtonPanel();
		contentPane.add(_buttonPanel, "span");

		contentPane.validate();
		contentPane.repaint();

	} // end OccupationDialogue constructor

	/**
	 * Add Save and Cancel buttons to the display next
	 * 
	 * @return button panel
	 */
	private JPanel buildButtonPanel() {
		// NOTE: Save action is invoked for Skill */
		_saveButton = new JButton("Save");
		_saveButton.setBackground(Color.white);
		_saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				MsgCtrl.traceEvent(event);
				doSave();
			}
		});

		// Add Delete button */
		_deleteButton = new JButton("Delete");
		_deleteButton.setBackground(Color.white);
		_deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				MsgCtrl.traceEvent(event);
				doDelete();
			}
		});

		/* Add Cancel button */
		_cancelButton = new JButton("Cancel");
		_cancelButton.setBackground(Color.white);

		// Clear data and return back to mainframe if Cancel is pressed
		_cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				MsgCtrl.traceEvent(event);
				doCancel();

			}
		});

		// Add buttons to buttonPanel, and buttonPanel to basePanel
		JPanel buttonPanel = new JPanel(new MigLayout("insets1, wrap 4"));
		buttonPanel.setBackground(_backColor);
		// buttonPanel.setMaximumSize(new Dimension(DATA_WIDTH, BUTTON_HT));

		// Add small space at the top of the button panel
		buttonPanel.add(_saveButton, "width :100:, gap 70");
		buttonPanel.add(_deleteButton, "width :100:, gap 90");
		_deleteButton.setEnabled(false);
		buttonPanel.add(_cancelButton, "width :100:, gap 90");

		return buttonPanel;
	}

	/**
	 * Makes the panel for the name space
	 * 
	 * @param label
	 *            Text displayed for the field
	 * @return the attribute grid panel
	 */
	private JPanel buildDescPanel(String label) {
		JPanel descPanel = new JPanel();// (new MigLayout(
		descPanel.setLayout(new BorderLayout());
		descPanel.setBackground(_backColor);
		// Ensure that the attribute panel does not exceed the HeroDisplay panel
		// width
		descPanel.setMaximumSize(new Dimension(_panelWidth, _panelHeight));
		descPanel.add(new JLabel(label), BorderLayout.NORTH);

		_descText = new JTextArea(3, 35);
		_descText.setLineWrap(true);
		_descText.setWrapStyleWord(true);
		_descText.setText(_occDesc);
		descPanel.add(_descText, BorderLayout.CENTER);
		return descPanel;
	} // End of buildDescPanel

	/**
	 * Make a panel for the drop down information from Klasslist
	 * 
	 * @param klasslist
	 *            Names of all available Klasses
	 * @return the panel populated
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    private JPanel buildOccupationComboBox(String[] occlist) {
		_occPanel = new JPanel(new BorderLayout());
		_occPanel.setBackground(_backColor);
		JLabel occLabel = new JLabel("Select Occupation");
		_occBox = new JComboBox(occlist);
		_occBox.setSelectedIndex(0);
		_occBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cmb = (JComboBox) e.getSource();
				_occSelected = (String) cmb.getSelectedItem();
				doRefresh();
			}

		});
		_occPanel.add(occLabel, BorderLayout.NORTH);
		_occPanel.add(_occBox, BorderLayout.SOUTH);
		return _occPanel;
	}

	/**
	 * Makes the panel for the name space
	 * 
	 * @param label
	 *            Text displayed for the field
	 * @return the attribute grid panel
	 */
	private JPanel buildNamePanel(String label) {
		JPanel namePanel = new JPanel(new MigLayout());
		namePanel.setBackground(_backColor);
		namePanel.add(new JLabel(label));
		_nameText = new JTextField(30);
		_nameText.setText(_occName);
		namePanel.add(_nameText, "growx");
		return namePanel;
	} // End of buildNamePanel

	/**
	 * Make a panel for the drop down information from Skilllist
	 * 
	 * @param skilllist
	 *            Names of all available Skills
	 * @return the panel populated
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    private JPanel buildSkillComboBox(String[] skilllist) {
		JPanel skillPanel = new JPanel(new BorderLayout());
		skillPanel.setBackground(_backColor);
		JLabel skillLabel = new JLabel("Skill for Occupation");
		_skillBox = new JComboBox(skilllist);
		_skillBox.setSelectedIndex(0);
		// _skillBox.addActionListener(new ActionListener()
		// {
		// public void actionPerformed(ActionEvent e)
		// {
		// JComboBox cmb = (JComboBox) e.getSource();
		// _skillSelected = (String) cmb.getSelectedItem();
		// doRefresh();
		// }
		//
		// });
		skillPanel.add(skillLabel, BorderLayout.NORTH);
		skillPanel.add(_skillBox, BorderLayout.SOUTH);
		return skillPanel;
	}

	/** Button action for Refresh */
	private void doCancel() {
		// Do what you need to do to load a skill
		// JOptionPane.showMessageDialog(parent, "Cancel button pressed");
		System.exit(0);
	}

	/** Button action for Refresh */
	private void doDelete() {
		if (_occSelected == null) {
			return;
		}
		int deleteState = 0;
		// Do what you need to do to delete a skill

		Object[] options = { "Yes", "No" };
		int deletePrompt = JOptionPane.showOptionDialog(_owner,
				"You are about to delete an occupation: " + _occSelected,
				"Confirm delete", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, // do not use a custom Icon
				options, // the titles of buttons
				options[1]); // default button title

		if (deletePrompt == 0) {
			deleteState = _occCiv.delete(_occSelected);
		} else {
			JOptionPane.showMessageDialog(_owner, "Delete cancelled", "Cancel",
					JOptionPane.WARNING_MESSAGE);
		}

		if (deleteState > 0) {

			JOptionPane.showMessageDialog(_owner, "There are now "
					+ deleteState + " occupations in the registry.",
					"Occupation saved", JOptionPane.PLAIN_MESSAGE);

		} else if (deleteState < 0) {
			JOptionPane.showMessageDialog(_owner, _ws.getErrorMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}

		// Update display of skill
		buildFrame();
	}

	/** Button action for Refresh */
	private void doRefresh() {
		if (_occSelected == null) {
			return;
		} else if (_occSelected == "NEW") {
			_deleteButton.setEnabled(false);
			// _saveButton.setEnabled(false);
			// _skillName = "";
			_occName = "";
			_descText.setText("Not implemented yet");
			_skillSelected = "Appraise";
		} else {
			if (_occCiv.getOccupation(_occSelected) != null) {
				// Do what you need to do to load a occ
				_deleteButton.setEnabled(true);
				_saveButton.setEnabled(true);
				unpackShuttle(_ws);
			}
		}
		refreshDisplay();
	}

	/** Button action for Save */
	private void doSave() {
		if (_nameText == null) {
			return;
		}

		// Do what you need to do to save an occupation
		packShuttle();

		int saveState = 0;
		_occCiv.submit(_ws);
		if (_ws.getErrorType() == DataShuttle.ErrorType.OK) {
			saveState = _occCiv.save(_ws);
		} else {
			// Check error conditions
			int overwritePrompt = -1;
			if (_ws.getErrorMessage() == "Occupation already exists") {
				Object[] options = { "Yes", "No" };
				overwritePrompt = JOptionPane
						.showOptionDialog(
								_owner,
								"Occupation already exists, would you like to overwrite?",
								"Occupation Exists", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, // do not
																	// use a
																	// custom
																	// Icon
								options, // the titles of buttons
								options[1]); // default button title

				// Now read overwrite option and execute
				if (overwritePrompt == 0) {
					_occCiv.delete((String) _ws.getField(OccupationKeys.NAME));
					saveState = _occCiv.save(_ws);
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
			if (saveState == _occCiv.getSize()) {
				JOptionPane.showMessageDialog(_owner, "There are now "
						+ saveState + " occupations in the registry.",
						"Occupation saved", JOptionPane.PLAIN_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(_owner, "Error saving to disk",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (saveState < 0) {
			JOptionPane.showMessageDialog(_owner, _ws.getErrorMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}

		String selected = _occSelected;
		// Update display of occupation afterwards
		buildFrame();

		_occSelected = selected;
		doRefresh();
	}

	/** Refreshes the display */
	private void refreshDisplay() {
		_nameText.setText(_occName);
		_descText.setText("Not implemented yet");
		_occBox.setSelectedIndex(_occList.indexOf(_occSelected));
		_skillBox.setSelectedIndex(_skilllist.indexOf(_skillSelected));
	}

	/** Pack the current data into a shuttle for submission to the CIV */
	private DataShuttle<OccupationKeys> packShuttle() {
		_ws.putField(OccupationKeys.NAME, _nameText.getText());
		_ws.putField(OccupationKeys.DESC, _descText.getText());
		_ws.putField(OccupationKeys.SKILL, _skillSelected);
		return _ws;
	}

	@SuppressWarnings({ "unchecked" })
    private void unpackShuttle(DataShuttle<OccupationKeys> ds) {
		// Unpack the data shuttle into individual pieces of info
		_occName = (String) ds.getField(OccupationKeys.NAME);
		_occDesc = (String) ds.getField(OccupationKeys.DESC);
		_occList = (ArrayList<String>) ds.getField(OccupationKeys.OCCLIST);
		_skillSelected = (String) ds.getField(OccupationKeys.SKILL);
		_skilllist = (ArrayList<String>) ds.getField(OccupationKeys.SKILLLIST);
	}
} // end OccupationDialogue class

