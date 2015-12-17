/**
 * 
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package hic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import mylib.Constants;
import net.miginfocom.swing.MigLayout;
import chronos.civ.PersonKeys;
import chronos.pdc.MiscKeys.ItemCategory;
import civ.HeroDisplayCiv;


/**
 * Allows the user to show their Person's many attributes. <BL>
 * <LI>Name: All Persons must have a name by which they are addressed, and are associated with a
 * file by which they are saved.</LI>
 * <LI>Experience Points (XP): Reflects the amount of playing experiences encountered by the Person,
 * and gained toward next Level of ability. XP are awarded for gold and treasure recovered, monsters
 * killed, and puzzles solved. All start at XP = 0.</LI>
 * <LI>Level: Reflects the Person's ability, Level 0 being lowest (usually a Peasant), and Level 1
 * usually being the lowest Guild member. Levels may rise to Level 10, master of all they command. A
 * Person may rise in Level (be promoted) in their Guild after obtaining enough XP to achieve that
 * Level. All start at Level = 0.</LI>
 * <LI>Hit Points (HP): Reflects the life of the Person. HP are lost each time the Person is damaged
 * in a fight, and are recovered through healing. When the Person's HP = 0, he or she is dead.
 * (There are other ways of dying too, of course.) HP depend on the Person's Klass and Level;
 * Peasants, lowest of all, start at HP = 10.</LI>
 * <LI>Race: Human is the default, but others are available: Dwarf, Elf, Gnome, Half-Elf, Half-Orc,
 * or Hobbit. Each have special penalties and bonuses compared to the Human.</LI>
 * <LI>Klass: Everyone starts as a Peasant, but can become a Fighter, Cleric, Wizard, or Rogue at
 * the proper Guild. All but Fighters inherit special abilities.</LI>
 * <LI>Armor Class (AC): Reflects defensive ability, mostly due to the armor worn. AC also slightly
 * reflects the Dexterity a Person has to avoid being hit.</LI>
 * <LI>Age: Reflects how old the Person is. As the Person plays, and increases in Age, base traits
 * change, and modifiers adjust accordingly. Age also reflects how long the Person spent in play.
 * </LI>
 * <LI>Gender: Females are, on average, shorter, lighter, and have less Strength than Males, but
 * have a higher Constitution and Charisma.</LI>
 * <LI>Height: The height of the Person, affects speed slightly; also defines how high the Person
 * can reach for something. Height depends on the Person's Race.</LI>
 * <LI>Weight: The weight of the Person, affects non-lethal combat somewhat; also limits the
 * strength of bridges the Person may cross safely. Weight depends on the Person's Race.</LI>
 * <LI>Occupation: Although "No Skills" is an option, most occupations reflect some special skill
 * that the Person acquired in earlier life.</LI>
 * <LI>Gold: The amount of wealth the Person owns, and carries about (adding to his load). The
 * <i>Gold</i> value is the number of gold pieces and silver pieces in G.S format (10 sp = 1 gp).
 * Peasants start with the least gold; Guilds will confer a little wealth on the Person, depending
 * on the Guild.</LI>
 * <LI>Gold Banked: The amount of wealth the Person has stashed away, usually in the local Bank.
 * Banks will charge interest on the amount periodically, but you won't have to carry it around with
 * you, and take the risk of being robbed.</LI>
 * <LI>Weight Carried: The weight the Person is currently carrying. Some weight may slow the Person,
 * and there is a limit to how much the Person can carry. Weight carried affects non-lethal combat
 * somewhat; also limits the strength of bridges the Person may cross safely. Weight carried depends
 * on the Person's Strength.</LI>
 * <LI>Max Languages: The total number of languages a Person can learn, in addition to <i>Common</i>
 * and a Race language.</LI>
 * <LI>Languages: All Persons know the <i>Common</i> language, and perhaps a Race language. Each
 * Person has the ability to learn other languages.</LI>
 * <LI>Description: Deduced from hair color, weight, height, gender, race, Klass and CHR, it follows
 * the formula "A [body type] [gender] [race] [klass] with [color] hair. She | He is [CHR value]."
 * Body type is derived from weight, height, body-mass-index ratio, e.g. "tall and thin", or
 * "short and squat". CHR value ranges from "stunningly beautiful" (18) to "horribly scarred" (8).
 * </LI>
 * <LI>Inventory: The list of Items carried by the Person, usually in their backpack. Items are
 * found in the Dungeon, or purchased in the Store. If you don't have it, you can't use it!</LI>
 * <LI>Special Abilities: The behaviors are specific to a Person's occupation, Race, and Klass.
 * Special abilities may involve detecting secret doors (e.g., Rogues and Elves), throwing spells
 * (e.g. Wizards and Clerics), or just plain luck (e.g, Gamblers).</LI>
 * <LI>Magic Bag (Wizard) or Sacred Satchel (Cleric): If the Person is a spellcaster, then this
 * special container for spell items will appear. Inventory items can be moved to or from the Bag or
 * Satchel to be available for Spells.</LI> </BL>
 * <P>
 * 
 * @see NewHeroIPPanel
 * @see HeroDisplayCiv
 * @see PersonKeys
 * 
 * @author Alan Cline
 * @version Jan 24 2009 // original <br>
 *          Mar 25 2009 // revised to display visible attributes <br>
 *          Apr 15 2009 // allows Saving or canceling after display is shown <br>
 *          May 25 2009 // populates panel with Person's attributes; fixed esthetics <br>
 *          Oct 22 2009 // added satiety and fixed Save/Open options <br>
 *          Nov 18 2009 // made data pane scrollable and reformatted somewhat <br>
 *          Dec 23 2009 // fit panels into pane better and cleaned up cosmetics <br>
 *          May 31 2010 // replaced PDC and DMC with CIV package <br>
 *          May 2 2011 // major rewrite for MigLayout manager <br>
 *          Oct 6 2011 // minor changes to support MVP Stack <br>
 *          Oct 1 2015 // revised to accommodate new Hero character sheet <br>
 *          Oct 17 2015 // added dual tab pane for Spell casters vs non-spell casters <br>
 *          Nov 22 2015 // updated Save, Overwrite, Rename, and Cancel buttons for new Hero <br>
 */
@SuppressWarnings("serial")
public class HeroDisplay extends ChronosPanel
{
  /** Help message to show in panels */
  private final String HELP_LABEL = "Press F1 key for help.";

  // Specific file error messages not handled by FileChooser
  private String _saveMsg = "";
  
  private final String PROMPT_HERO_EXISTS_MSG =
      "Do you want to overwrite, rename, or create a new Hero?";
  private final String PROMPT_HERO_EXISTS_TITLE = "Hero already exists.";
  
  private final String SAVE_ERROR_TITLE = "FILE SAVE ERROR";
  
  private final String CONFIRM_SAVE_MSG = " is resting in the dormitory until later.";
  private final String CONFIRM_SAVE_TITLE = " Hero is now Registered";
  private final String CONFIRM_OVERWRITE_MSG = " has been overwritten in the dormitory.";
  private final String CONFIRM_OVERWRITE_TITLE = "Overwriting Hero";
  private final String CONFIRM_RENAME_MSG = " has been renamed";
  private final String CONFIRM_RENAME_TITLE = "Renaming Hero";

  // Specific file error messages not handled by FileChooser
  private final String DEL_ERROR_MSG = "Error! Problem deleting ";
  private final String DEL_ERROR_TITLE = "HERO DELETE ERROR";
  private final String CONFIRM_DEL_MSG = " is now in a better place.";
  private final String CONFIRM_DEL_TITLE = " Hero is now Deceased";

  /** Option to overwrite a new Hero or not */
  private final boolean OVERWRITE = true;
  private final boolean NO_OVERWRITE = false;

  /** Height of font for vertical spacing */
  private final int FONT_HT = 14;

  /** Set the max width of the hero panel at half screen */
  private final int PANEL_WIDTH = Mainframe.getWindowSize().width / 2;
  /** Set the max height of the hero panel */
  private final int PANEL_HEIGHT = Mainframe.getWindowSize().height;

  /** Set the width of the two data panels within the display borders */
  // private final int DATA_WIDTH = PANEL_WIDTH / 2 - 2 * (THICK_BORDER + THIN_BORDER);
  private final int DATA_WIDTH = PANEL_WIDTH / 2 - Mainframe.PAD;

  /** Size of inventory area */
  final int INVEN_HEIGHT = 30;
  /** Number of cash fields */
  final int CASH_FIELDS = 2;

  // /** HelpDialog reference for all widgets that have context help */
  // private HelpDialog _help = null;
  // /** Set the default HelpKey for the general panel */
  // private HelpKeyListener _helpKey = new HelpKeyListener("HeroDsp");

  /** Disable SaveAs at some time . */
  private JButton _saveButton;
  /** Change Cancel Button to OK Button at some time . */
  private JButton _cancelButton;
  /** Gets rid of unused/unwanted characters */
  private JButton _delButton;

  /** Background color inherited from parent */
  private Color _backColor = Constants.MY_BROWN;

  /** The backend CIV for this panel */
  private HeroDisplayCiv _hdCiv = null;

  /** Keys to Hero data to be displayed */
  EnumMap<PersonKeys, String> _ds;

  /** Six panels in each attribute row */
  private final int PANELS_IN_ROW = 6;

  // /** Crude attempt to allocate space for wrapping text */
  // private final int SPACES_PER_LINE = 27;

  /** Button panel for Save/Delete/Cancel buttons */
  private JPanel _buttonPanel;

  // /** Codes to handle dispensation of the Hero */
  // private enum WriteOption {
  // CANCEL, SAVE, OVERWRITE, RENAMRE, ERROR
  // };

  // ===============================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ===============================================================

  /**
   * Create the GUI and populate it with various data maps
   * 
   * @param hdCiv the intermediary between this GUI and the Person
   * @param _mf
   * @param firstTime Hero activates buttons differently
   */
  public HeroDisplay(HeroDisplayCiv hdCiv, boolean firstTime)
  {
    super("Hero nameplate goes here");

    _hdCiv = hdCiv;
    _ds = _hdCiv.getAttributes();

    // _hdCiv.resetLoadState();
    // Define the overall tabbed pane and button layout
    setupDisplay(firstTime);
  }



  // TODO: THis may not be needed, but keep the old code here anyway. It needs to be debugged
  // private Font shrinkFontSize()
  // {
  // while (_charName.getPreferredSize().width > DATA_WIDTH) {
  // // Edit name font down a notch or two
  // Font labelFont = _charName.getFont();
  // String labelText = _charName.getText();
  //
  // int stringWidth = _charName.getFontMetrics(labelFont).stringWidth(
  // labelText);
  // int componentWidth = DATA_WIDTH;
  //
  // // Find out how much the font can grow in width.
  // double widthRatio = (double) componentWidth / (double) stringWidth;
  //
  // int newFontSize = (int) (labelFont.getSize() * widthRatio);
  //
  // // Recreate the new font
  // try {
  // Font newFont = Font.createFont(Font.TRUETYPE_FONT, new File(
  // Chronos.RUNIC_ENGLISH_FONT_FILE));
  //
  // // Set the label's font size to the newly determined size.
  // Font smallNameFont = newFont.deriveFont(newFontSize);
  // _charName.setFont(smallNameFont);
  // } catch (FontFormatException e) {
  // MsgCtrl.errMsgln("Could not format font: " + e.getMessage());
  // } catch (IOException e) {
  // MsgCtrl.errMsgln("Could not create font: " + e.getMessage());
  // }
  // }
  // return newFont;
  // }


  // /**
  // * Put the HeroDisplay panel into a scrollpane and add to the MainFrame
  // *
  // * @return the scrollpane for the Person's attributes
  // */
  // public JScrollPane display()
  // {
  // // Make the display scrollable to add to the MainFrame
  // JScrollPane heroScroll = makeScrollable();
  //
  // // Add the non-static scrolling panel to the main JFrame
  // _mainframe.addPanel(heroScroll);
  // _mainframe.repaint();
  // // Get the focus so the arrow keys will work
  // requestFocusInWindow();
  // return heroScroll;
  // }


  /**
   * The name text field should have the default focus, butt that cannot be done until after the
   * panel is realized and visible. Therefore, the HeroDisplay's caller (MenuBar.NEW Action) must
   * invoke the default component. It calls this method to get which one should be default. This
   * method is created for this because the caller does not know what fields are available in this
   * panel, and it can change with maintenance.
   * 
   * @return the JTextField, in this case
   */
  public JButton getDefaultFocus()
  {
    return _cancelButton;
  }


  // =================================================================
  // PRIVATE METHODS
  // =================================================================

  /**
   * Create the person's attributes in a grid panel for display. Attributes are unpacked from the
   * outputmap as they are needed, row by row.
   * <P>
   * HEURISTIC LAYOUT: Character attibutes, e.g. XP and Level, go toward the left side; Personal
   * attributes, such as gender, weight, and height, go toward the right side. The more important
   * attributes, e.g. HP and Klass, go toward the top; lesser ones, e.g. description and gold
   * banked, toward the bottom.
   * <P>
   * {@code MigLayout} uses cryptic parameters so their use is explained as they are used in this
   * method. The {@code MigLayout} constructor grid is divided into cells, 12 rows with 6 cells for
   * each row. This is shown by the {@code []0[]} parms. The {@code ins 0} parm means no space is
   * inserted between cells or rows. <br>
   * The {@code attribPanel.add()} method contains a call to {@code gridCell()}, followed by
   * {@code span n} when the cell should span multiple columns; <br>
   * {@code growx} parm enlarges each cell to the max column width. <br>
   * Each row is terminated by a {@code wrap} parm to trigger the start of a new row. <BR>
   * Empty lines are filled with one {@code span 6} cell instead of a series of empty cells.
   * 
   * @param panelLimits size of the parent panel into which this panel must fit
   * @return the attribute grid panel
   */
  // private JPanel buildAttributePanel(Dimension panelLimits)
  private JPanel buildAttributePanel()
  {
    // Create a layout that spans the entire width of the panel
    JPanel attribPanel = new JPanel(new MigLayout("ins 0", // layout constraints
        "[]0[]0[]0[]0[]0[][left]", // align horizontally 6 cell-widths
        "[]0[]0[]0[]0[]0[]0[]0[]0[]0[]0[]0[][bottom]")); // align vertically 12 rows

    attribPanel.setBackground(_backColor);
    attribPanel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

    // Row 1: Level, Current/Max HP (2), AC, Hunger (2)
    attribPanel.add(gridCell("Level: ", _ds.get(PersonKeys.LEVEL)), "growx");
    String HP_valueStr = _ds.get(PersonKeys.HP) + " / " + _ds.get(PersonKeys.HP_MAX);
    attribPanel.add(gridCell("Current/Max HP: ", HP_valueStr), "span 2, growx");
    attribPanel.add(gridCell("AC: ", _ds.get(PersonKeys.AC)), "growx");
    attribPanel.add(gridCell("Hunger: ", _ds.get(PersonKeys.HUNGER)), "span 2, growx, wrap");

    // Row 2: XP, Speed, Gold Banked, Gold (gp/sp) (in hand) (3)
    attribPanel.add(gridCell("XP: ", _ds.get(PersonKeys.XP)), "growx");
    attribPanel.add(gridCell("Speed: ", _ds.get(PersonKeys.SPEED)), "growx");
    attribPanel.add(gridCell("Gold Banked: ", _ds.get(PersonKeys.GOLD_BANKED)), "growx");
    // attribPanel.add(gridCell("Gold Banked: ", "999999.09"), "");
    // Convert from two quantities into single string gp/sp
    String gp = _ds.get(PersonKeys.GOLD);
    String sp = _ds.get(PersonKeys.SILVER);
    String inHand = gp + " gp / " + sp + " sp";
    attribPanel.add(gridCell("Gold in Hand: ", inHand), "span 3, growx, wrap");

    // Row 3: Personal description (full line, possibly multiline)
    attribPanel.add(gridCell("Description: ", _ds.get(PersonKeys.DESCRIPTION)),
        "span 6, growx, wrap");

    // Row 4: STR trait, ToHitMelee, Damage, Wt Allowance (gpw), Load Carried (gpw)
    attribPanel.add(gridCell("STR: ", _ds.get(PersonKeys.STR)), "growx");
    attribPanel.add(gridCell("To Hit (Melee): ", _ds.get(PersonKeys.TO_HIT_MELEE)), "growx");
    attribPanel.add(gridCell("Damage: ", _ds.get(PersonKeys.DAMAGE)), "growx");
    attribPanel.add(gridCell("Wt Allowance (gpw): ", _ds.get(PersonKeys.WT_ALLOW)), "growx");
    attribPanel.add(gridCell("Load (gpw): ", _ds.get(PersonKeys.LOAD)), "span 2, growx, wrap");

    // Row 5: INT and INT mods: percent to know spell, current/max MSP, MSPs/Level, Spells in Book
    attribPanel.add(gridCell("INT: ", _ds.get(PersonKeys.INT)), "growx");
    String klassname = _ds.get(PersonKeys.KLASSNAME);
    if (klassname.equalsIgnoreCase("WIZARD")) {
      attribPanel.add(gridCell("% to Know: ", _ds.get(PersonKeys.TO_KNOW)), "growx");
      String MSP_valueStr = _ds.get(PersonKeys.CURRENT_MSP) + " / " + _ds.get(PersonKeys.MAX_MSP);
      attribPanel.add(gridCell("Current/Max MSP: ", MSP_valueStr), "growx");
      attribPanel.add(gridCell("Bonus MSP/Level: ", _ds.get(PersonKeys.MSP_PER_LEVEL)), "growx");
      attribPanel.add(gridCell("Spells in Book: ", _ds.get(PersonKeys.SPELLS_KNOWN)),
          "span 2, growx, wrap");
      // attribPanel.add(gridCell(" ", ""), "span 1, growx, wrap"); // remainder of line is empty
    } else {
      attribPanel.add(gridCell(" ", ""), "span 5, growx, wrap");
    }

    // Row 6: WIS and Wis Mods: Magic Attack Mod, Current/Max CSPs, CSP/Level, Turn Undead
    attribPanel.add(gridCell("WIS: ", _ds.get(PersonKeys.WIS)), "growx");
    attribPanel.add(gridCell("Magic Attack Mod: ", _ds.get(PersonKeys.MAM)), "growx");
    if (klassname.equalsIgnoreCase("CLERIC")) {
      String CSP_valueStr = _ds.get(PersonKeys.CURRENT_CSP) + " / " + _ds.get(PersonKeys.MAX_CSP);
      attribPanel.add(gridCell("Current/Max CSP: ", CSP_valueStr), "growx");
      attribPanel.add(gridCell("Bonus CSP/Level: ", _ds.get(PersonKeys.CSP_PER_LEVEL)), "growx");
      attribPanel.add(gridCell("Turn Undead: ", _ds.get(PersonKeys.TURN_UNDEAD)),
          "span 2, growx, wrap");
      // attribPanel.add(gridCell(" ", ""), "span 1, growx, wrap"); // remainder of line is empty
    } else {
      attribPanel.add(gridCell(" ", ""), "span 4, growx, wrap");
    }

    // Row 7: CON and HP Mod
    attribPanel.add(gridCell("CON: ", _ds.get(PersonKeys.CON)), "growx");
    attribPanel.add(gridCell("HP Mod: ", _ds.get(PersonKeys.HP_MOD)), "growx");
    attribPanel.add(gridCell(" ", ""), "span 4, growx, wrap"); // remainder of line is empty

    // Row 8: DEX and DEX mods: To Hit Missle, AC Mod
    attribPanel.add(gridCell("DEX: ", _ds.get(PersonKeys.DEX)), "growx");
    attribPanel.add(gridCell("To Hit (missile): ", _ds.get(PersonKeys.TO_HIT_MISSLE)), "growx");
    attribPanel.add(gridCell("AC Mod: ", _ds.get(PersonKeys.AC_MOD)), "growx");
    attribPanel.add(gridCell(" ", ""), "span 3, growx, wrap"); // remainder of line is empty

    // Row 9: CHR, Weight, and Height
    attribPanel.add(gridCell("CHR: ", _ds.get(PersonKeys.CHR)), "growx");
    attribPanel.add(gridCell("Weight (lbs): ", _ds.get(PersonKeys.WEIGHT)), "growx");
    int intHeight = new Integer(_ds.get(PersonKeys.HEIGHT));
    int feet = intHeight / 12;
    int inches = intHeight - feet * 12;
    String heightStr = String.format("%s' %s\"", feet, inches);
    attribPanel.add(gridCell("Height: ", heightStr), "growx");
    attribPanel.add(gridCell(" ", ""), "span 3, growx, wrap"); // remainder of line is empty

    // Row 10: AP, overbearing, grappling, pummeling, and shield bash
    attribPanel.add(gridCell("AP: ", _ds.get(PersonKeys.AP)), "growx");
    attribPanel.add(gridCell("Overbearing: ", _ds.get(PersonKeys.OVERBEARING)), "growx");
    attribPanel.add(gridCell("Grappling: ", _ds.get(PersonKeys.GRAPPLING)), "growx");
    attribPanel.add(gridCell("Pummeling: ", _ds.get(PersonKeys.PUMMELING)), "growx");
    attribPanel.add(gridCell("Shield Bash: ", _ds.get(PersonKeys.SHIELD_BASH)),
        "span 2, growx, wrap");
        // attribPanel.add(gridCell(" ", ""), "span 1, growx, wrap"); // remainder of line is empty

    // Row 11: Languages label, max langs, list of languages
    String langStr = String.format("Languages (can learn %s more):",
        _ds.get(PersonKeys.MAX_LANGS));
    attribPanel.add(gridCell(langStr, ""), "span 2, growx");
    String langList = String.format("%s", _ds.get(PersonKeys.LANGUAGES));
    attribPanel.add(gridCell(langList, ""), "span 4, growx, wrap");

    // Row 12: Occupation (full line, possibly multiline)
    attribPanel.add(gridCell("Former Occupation: ", _ds.get(PersonKeys.OCCUPATION)),
        "span 6, growx, wrap");

    return attribPanel;

  } // End of buildAttribute panel



  // =================================================================
  // PRIVATE METHODS
  // =================================================================

  /**
   * Create Save, Delete, and Cancel buttons, then add then to a JPanel
   * 
   * @param firstTime Hero disables DELETE button; old Hero disables CANCEL button
   * @return button panel
   */
  private JPanel buildButtonPanel(boolean firstTime)
  {
    // SAVE a new Hero to the Dormitory */
    _saveButton = new JButton("Save");
    _saveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        // Try to save the new Hero
        if (savePerson()) {
          // Display confirmation message
          JOptionPane.showMessageDialog(null, _ds.get(PersonKeys.NAME) + CONFIRM_SAVE_MSG,
              CONFIRM_SAVE_TITLE, JOptionPane.INFORMATION_MESSAGE);
          // Return two levels back to main action
          _hdCiv.backToMain();
        } else {
          // Respond to save attempt failure to Rename or Overwrite the Hero
          doAlternateSaveAction();
        }
        setVisible(false);
      }
    });


    // switch (choice)
    // {
    // case PROMPT_CANCEL:
    // System.out.println("Cancel chosen");
    // _hdCiv.back();
    // break;
    // case PROMPT_OVERWRITE:
    // System.out.println("Overwrite chosen");
    // break;
    // case PROMPT_RENAME:
    // System.out.println("Rename chosen");
    // break;
    // default:
    // System.out.println("Default case reached");
    // _hdCiv.backToMain();
    // break;
    // }
    // }


    // else {
    // // Display an error message with a Sorry button instead of OK
    // String[] sorry = new String[1];
    // sorry[0] = "SORRY!";
    // JOptionPane.showOptionDialog(null, _saveMsg + _ds.get(PersonKeys.NAME),
    // SAVE_ERROR_TITLE, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
    // null, sorry, null);
    // }

    // DELETE an existing Hero from the Dormitory */
    _delButton = new JButton("Delete");
    _delButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        if (deletePerson() == true) {
          // Display confirmation message
          JOptionPane.showMessageDialog(null,
              _ds.get(PersonKeys.NAME) + CONFIRM_DEL_MSG,
              CONFIRM_DEL_TITLE, JOptionPane.INFORMATION_MESSAGE);
        } else {
          // Display an error message with a Sorry button instead of OK
          String[] sorry = new String[1];
          sorry[0] = "You dropped your scythe!!";
          JOptionPane.showOptionDialog(null,
              DEL_ERROR_MSG + _ds.get(PersonKeys.NAME),
              DEL_ERROR_TITLE, JOptionPane.DEFAULT_OPTION,
              JOptionPane.ERROR_MESSAGE, null, sorry, null);
        }
        setVisible(false);
      }
    });

    /* ADD A CANCEL BUTTON TO THE BOTTOM OF THE PANEL */
    _cancelButton = new JButton("Cancel");

    // Clear data and return back to mainframe if Cancel is pressed
    _cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        // Collect all the attributes and save to a new Hero file
        setVisible(false);
        // Return two levels back to main action
        _hdCiv.backToMain();
      }
    });

    // Add buttons to buttonPanel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(_backColor);

    // Disable DELETE or CANCEL buttons depending on old or new Hero
    if (firstTime) {
      _delButton.setEnabled(false);
    } else {
      _cancelButton.setEnabled(false);
    }

    buttonPanel.add(_saveButton);
    buttonPanel.add(_delButton);
    buttonPanel.add(_cancelButton);

    // // Enabled/disable buttons as needed
    // if (_hdCiv.LOADING_CHAR) {
    // _saveButton.setEnabled(false);
    // } else if (_hdCiv.NEW_CHAR) {
    // _delButton.setEnabled(false);
    // }
    return buttonPanel;
  }


  // /**
  // * Create the Help Panel and help message for HeroDisplay
  // *
  // * @return the HelpPanel
  // */
  // private JPanel buildHelpPanel() {
  // JPanel helpPanel = new JPanel();
  // helpPanel.setBackground(_backColor);
  // // // Set panel size to three text lines high
  // // helpPanel.setPreferredSize(new Dimension(DATA_WIDTH-DATA_PAD, 3 *
  // // FONT_HT));
  //
  // // Add the help text directly onto the base panel (defaults to Center)
  // JLabel helpLabel = new JLabel(HELP_LABEL);
  // helpLabel.setForeground(Color.RED);
  // helpLabel.setVerticalAlignment(SwingConstants.CENTER);
  // helpPanel.add(helpLabel); // help key message
  // add(helpPanel);
  //
  // // Add the help text subpanel to the basePanel
  // return helpPanel;
  // }

  // /**
  // * Builds the inventory "table", showing Category, Items, quantity, and weight (in lbs) and
  // weight
  // * (in oz) of each; calls the display civ to pack a data shuttle
  // *
  // * @return the output-only JTextArea contining inventory data
  // */
  // private JTextArea buildInventoryArea()
  // {
  // // Get the Inventory items to display
  // int invenLen = _itemList.size() - CASH_FIELDS; // _hdCiv.getInventorySize();
  //
  // // Add a non-editable textArea containing all the Inventory
  // JTextArea invenArea = new JTextArea(invenLen, 0); // rows by columns
  // // Make area restricted to displayable size (without scrollbar)
  // invenArea.setPreferredSize(new Dimension(DATA_WIDTH, invenLen * CELL_HEIGHT));
  // invenArea.setEditable(false);
  // invenArea.setBackground(_backColor);
  // invenArea.setTabSize(TAB_SIZE);
  // invenArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_HT));
  // invenArea.setMargin(new Insets(DEF_INSET, DEF_INSET, DEF_INSET,
  // DEF_INSET));
  // int j = 0;
  // int size = ItemCategory.values().length;
  // // Read in item groups by category, input like GENERAL|Backpack|1|10|0"\
  // for (int i = 0; i < size; i++) {
  // if (j < _itemList.size()) {
  // String str = _itemList.get(j);
  // int firstBar = str.indexOf('|');
  // String cat = str.substring(0, firstBar);
  // String subcat = cat;
  // if (cat.compareTo("CASH") != 0) {
  // invenArea.append(cat + "\n");
  // }
  // while (subcat.compareTo(cat) == 0) {
  // // Display and format item
  // String item = str.substring(firstBar + 1, str.length());
  // item = formatItemString(item);
  // if (subcat.compareTo("CASH") != 0) {
  // invenArea.append(item);
  // }
  // j++;
  //
  // // Now get next item
  // if (j < _itemList.size()) {
  // str = _itemList.get(j);
  // firstBar = str.indexOf('|');
  // subcat = str.substring(0, firstBar);
  // } else {
  // subcat = "NEWCAT";
  // }
  // }
  // }
  // }
  // return invenArea;
  // }


  /**
   * Builds the inventory "table", showing Category, Items, quantity, weight (in lbs & oz of each,
   * and total. Wielded Weapon and Armor Worn is at top. Inventory is organized by category: ARMS,
   * ARMOR, CLOTHING, EQUIPMENT, PROVISIONS, LIVESTOCK, TRANSPORT : item, quantity, weight, total
   * weight. See also Sacred Satchel and Magic Bag
   */
  private JPanel buildInventoryPanel()
  {
    JPanel invenPanel = new JPanel(new MigLayout("fillx, ins 0"));
    invenPanel.setBackground(_backColor);

    // Get various items from the civ
    // Inventory inventory = _hdCiv.getInventory();
    List<String> nameList = new ArrayList<String>();

    // Active arms and armor: initially, none
    invenPanel.add(gridCell("Wielded Weapon:", "None"), "gaptop 10, span 6, growx, wrap 0");
    invenPanel.add(gridCell("Armor Worn: ", " None"), "span 6, growx, wrap 0");
    // Blank line before data
    JPanel blankLine = gridCell("", "");
    blankLine.setBackground(Color.DARK_GRAY);
    invenPanel.add(blankLine, "span 6, growx, wrap 0");

    // List of Arms: name, damage, fire rate, qty, wt, total wt
    nameList = _hdCiv.getInventoryNames(ItemCategory.ARMS);
    invenPanel.add(buildMultiCell(ItemCategory.ARMS.toString(), nameList), "growx, wrap");

    // List of Armor
    nameList = _hdCiv.getInventoryNames(ItemCategory.ARMOR);
    invenPanel.add(buildMultiCell(ItemCategory.ARMOR.toString(), nameList), "growx, wrap");

    // List of Equipment
    nameList = _hdCiv.getInventoryNames(ItemCategory.EQUIPMENT);
    invenPanel.add(buildMultiCell(ItemCategory.EQUIPMENT.toString(), nameList), "growx, wrap");

    // List of Provisions
    nameList = _hdCiv.getInventoryNames(ItemCategory.PROVISION);
    invenPanel.add(buildMultiCell(ItemCategory.PROVISION.toString(), nameList), "growx, wrap");

    // List of Clothing
    nameList = _hdCiv.getInventoryNames(ItemCategory.CLOTHING);
    invenPanel.add(buildMultiCell(ItemCategory.CLOTHING.toString(), nameList), "growx, wrap");

    // List of Valueables
    nameList = _hdCiv.getInventoryNames(ItemCategory.VALUABLES);
    invenPanel.add(buildMultiCell(ItemCategory.VALUABLES.toString(), nameList), "growx, wrap");

    return invenPanel;
  }

  /**
   * Builds the inventory of magic items (not spell materials)
   */
  private JPanel buildMagicPanel()
  {
    JPanel magicPanel = new JPanel(new MigLayout("fillx, ins 0"));
    magicPanel.setBackground(_backColor);

    // Get maigc items from the civ
    List<String> nameList = _hdCiv.getInventoryNames(ItemCategory.MAGIC);

    magicPanel.add(buildMultiCell("MAGIC ITEMS", nameList), "growx, wrap 0");

    return magicPanel;
  }


  /**
   * Builds the inventory of (non-magical) spell material used for spells
   */
  private JPanel buildMaterialsPanel()
  {
    JPanel magicPanel = new JPanel(new MigLayout("fillx, ins 0"));
    magicPanel.setBackground(_backColor);

    // Get maigc items from the civ
    List<String> itemList = _hdCiv.getInventoryNames(ItemCategory.SPELL_MATERIAL);

    magicPanel.add(buildMultiCell("SPELL MATERIALS", itemList), "growx, wrap 0");

    return magicPanel;
  }


  /**
   * Add a JTextArea into a multiline grid
   * 
   * @param title of the section for a group, e.g., racial, occupational, or klass
   * @param nameList of Strings to display in this multi-line cell
   */
  private JPanel buildMultiCell(String title, List<String> nameList)
  {
    JTextArea msgArea = new JTextArea(nameList.size() + 1, DATA_WIDTH);
    msgArea.setPreferredSize(new Dimension(DATA_WIDTH, nameList.size() + 1));
    msgArea.setBackground(_backColor);
    msgArea.setEditable(false);
    msgArea.setTabSize(1);
    msgArea.setLineWrap(true); // auto line wrapping doesn't seem to work
    msgArea.setWrapStyleWord(true);

    // Display the title
    msgArea.append(" " + title + Constants.NEWLINE);

    // Display the detailed skill list
    if (nameList.size() == 0) {
      nameList.add(" None");
    }
    for (int k = 0; k < nameList.size(); k++) {
      msgArea.append(" + " + nameList.get(k) + Constants.NEWLINE);
    }

    // Add the text area into a JPanel cell
    JPanel cell = new JPanel(new MigLayout("ins 0"));
    cell.add(msgArea, "growx, wrap");

    gridSetCellBorder(cell);
    cell.validate();
    return cell;
  }



  // Build panel of skills: literacy, racial skills, occupational skills, and klass skills
  private JPanel buildSkillsPanel()
  {
    JPanel skillPanel = new JPanel(new MigLayout("fillx, ins 0"));
    skillPanel.setBackground(_backColor);

    // Get various skills from the civ
    List<String> racialSkills = _hdCiv.getRaceSkills();
    List<String> ocpSkills = _hdCiv.getOcpSkills();
    List<String> klassSkills = _hdCiv.getKlassSkills();

    // Section 1: Literacy
    skillPanel.add(gridCell("", _ds.get(PersonKeys.LITERACY)), "gaptop 10, span 6, growx, wrap");

    // Section 2: Occupational Skills title
    String occupation = _ds.get(PersonKeys.OCCUPATION);
    int nameIndex = occupation.indexOf(":");
    String ocpName = occupation.substring(0, nameIndex);
    String ocpDesc = occupation.substring(nameIndex + 1);
    String ocpTitle = ocpName.toUpperCase() + " SKILLS: " + ocpDesc;
    skillPanel.add(buildMultiCell(ocpTitle, ocpSkills), "growx, wrap");

    // Section 3: Racial skills
    skillPanel
        .add(buildMultiCell("SPECIAL " + _ds.get(PersonKeys.RACENAME).toUpperCase() + " SKILLS: ",
            racialSkills), "growx, wrap");

    // Section 4: Klass skills
    skillPanel.add(buildMultiCell(_ds.get(PersonKeys.KLASSNAME).toUpperCase() + " SKILLS: ",
        klassSkills), "growx, wrap");

    return skillPanel;
  }

  /**
   * Builds the list of spells that spellcaster knows
   */
  private JPanel buildSpellsPanel()
  {
    JPanel spellPanel = new JPanel(new MigLayout("fillx, ins 0"));
    spellPanel.setBackground(_backColor);

    // Get maigc items from the civ
    List<String> spellList = _hdCiv.getSpellBook();
    int known = spellList.size();
    spellPanel.add(buildMultiCell(known + " SPELLS KNOWN", spellList), "growx, wrap 0");

    // Prompt Wizard (only) to get more spells if he/she can
    if (_ds.get(PersonKeys.KLASSNAME).equalsIgnoreCase("Wizard")) {
      int allowed = Integer.parseInt(_ds.get(PersonKeys.MAX_MSP));
      int diff = allowed - known;
      if (diff != 0) {
        String prompt =
            String.format("Go to your local Wizard's Guild to get %d more spells", diff);
        spellPanel.add(gridCell("", ""), "span 6, growx, wrap 0");
        spellPanel.add(gridCell(prompt, " "), "span 6, growx, wrap 0");
      }
    }

    return spellPanel;
  }


  /**
   * Delete the Person currently being displayed into a new file.
   * 
   * @return true if the Person was removed successfully, else false
   */
  private boolean deletePerson()
  {
    // Custom button text
    Object[] options = {"Yes", "No"};
    int n = JOptionPane.showOptionDialog(this,
        "Are you sure you want to delete?", "Delete confirmation",
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
        options, options[1]);
    if (n == JOptionPane.YES_OPTION) {
      return _hdCiv.deletePerson();
    } else {
      return false;
    }
  }


  // /**
  // * Format a fixed-field space-filled display string for the Inventory list If values are zero,
  // the
  // * String is empty FIELDS: Quantity, Name (left-justified), wt (lb), wt (oz)
  // *
  // * @param name the name of the item
  // * @param qty the number of such items
  // * @param lbWt the weight of the thing in pounds
  // * @param ozWt the fractional pound-weight in ounces
  // * @return the Item's fields in a fixed-length formatted string
  // */
  // private String formatItemString(String str)
  // // private String formatItemString(String qty, String name, String lbWt,
  // // String ozWt)
  // {
  // // Given format: Backpack|1|10|0
  // // Pull out the 4 values, Qty, Name, Weight(lb), and Weight(oz)
  // int bar = str.indexOf('|');
  // String itemName = str.substring(0, bar);
  // int nextBar = str.indexOf('|', bar + 1);
  // String itemQty = str.substring(bar + 1, nextBar);
  // bar = nextBar;
  // nextBar = str.indexOf('|', bar + 1);
  // String itemLbWt = str.substring(bar + 1, nextBar);
  // bar = nextBar;
  // String itemOzWt = str.substring(bar + 1, str.length());
  //
  // // Now build the string
  // String initialSpaces = " ";
  // StringBuilder lineItem = new StringBuilder(initialSpaces + itemQty
  // + "\t " + itemName);
  //
  // // Append an appropriate number of tabs
  // for (int i = (SPACES_PER_LINE - itemName.length()); i > 0; i--) {
  // lineItem.append(" ");
  // }
  // if (itemLbWt.length() < 2) {
  // lineItem.append(" ");
  // }
  // lineItem.append(itemLbWt + " lb " + itemOzWt + " oz\n");
  // return lineItem.toString();
  // }


  // ======================================================================
  // PRIVATE METHODS
  // ======================================================================

  // Ask to Overwrite, Cancel, or Rename, the do it=e
  private void doAlternateSaveAction()
  {
    /** Code for user's secltion of saving a new Hero */
    final int PROMPT_CANCEL = 0;
    final int PROMPT_RENAME = 1;
    final int PROMPT_OVERWRITE = 2;

    Object[] options = {"Cancel", "Rename", "Overwrite"};
    // For some reason, buttons are produced in reverse order of the options[]
    int choice = JOptionPane.showOptionDialog(null,
        PROMPT_HERO_EXISTS_MSG, PROMPT_HERO_EXISTS_TITLE, JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

    // Perform requested action
    switch (choice)
    {
      case PROMPT_CANCEL:
        System.out.println("Cancel selected, choice = " + choice);
        // Return one level to create new Hero
        _hdCiv.back();
        break;
      case PROMPT_RENAME:
        System.out.println("Rename selected, choice = " + choice);
        renameHero();
        JOptionPane.showMessageDialog(null, _ds.get(PersonKeys.NAME) + CONFIRM_RENAME_MSG,
            CONFIRM_RENAME_TITLE, JOptionPane.INFORMATION_MESSAGE);
        // Return to main action buttons
        _hdCiv.backToMain();
        break;
      case PROMPT_OVERWRITE:
        System.out.println("Overwrite selected, choice = " + choice);
        _hdCiv.savePerson(OVERWRITE);
        JOptionPane.showMessageDialog(null, _ds.get(PersonKeys.NAME) + CONFIRM_OVERWRITE_MSG,
            CONFIRM_OVERWRITE_TITLE, JOptionPane.INFORMATION_MESSAGE);
        // Return to main action buttons
        _hdCiv.backToMain();
        break;
      default:
        _hdCiv.backToMain();
        break;
    }
  }



  /**
   * Creates a lined bordered cell containing a label and its value. The label and its value can be
   * aligned within the component. A JPanel type is returned so that the cell can also get the focus
   * for help listeners.
   * 
   * @param label the left-aligned text of the label, cannot be null but can be empty
   * @param value the right-aligned stringified value to display after the label, cannot be null but
   *        can be empty
   * @return bordered grid cell as Component
   */
  private JPanel gridCell(String label, String value)
  {
    // Guard against null values
    if ((label == null) || (value == null)) {
      return null;
    }
    // Create the grid cell as a panel to hold two JLabels within a border
    JPanel p = new JPanel(new MigLayout("inset 3")); // space between text and cell border
    p.setBackground(_backColor);
    gridSetCellBorder(p);

    int cellWidth = DATA_WIDTH / PANELS_IN_ROW;
    p.setPreferredSize(new Dimension(cellWidth, FONT_HT));

    int datalen = label.length() + value.length() + 1;
    if (datalen > cellWidth) {
      String multiline = "<html>" + label + "<br>" + value + "<br></html>";
      p.add(new JLabel(multiline, SwingConstants.LEFT));
    } else {
      p.add(new JLabel(label, SwingConstants.LEFT));
      p.add(new JLabel(value, SwingConstants.RIGHT));
    }

    return p;
  }


  /** Whether single cell or double-height cell, set a border around it */
  private void gridSetCellBorder(JPanel p)
  {
    int cpad = 1;
    Border cellBorder = BorderFactory.createLineBorder(Color.BLACK, cpad);
    p.setBorder(cellBorder);
  }


  // /**
  // * Set the hero display content panels inside a viewable scroll pane
  // *
  // * @return HeroDisplay in a (vertical) scrollable pane
  // */
  // private JScrollPane makeScrollable()
  // {
  // // Reset the panel size based on the size of the constituent components
  // int finalHt = getPreferredSize().height;
  // // setPreferredSize(new Dimension(DATA_WIDTH, finalHt));
  // setPreferredSize(new Dimension(PANEL_WIDTH - 2 * (SCROLLBAR_SIZE),
  // finalHt));
  //
  // /*
  // * Put the panel in the viewport, and put the viewport in the scrollpane If everything sizes
  // * correctly, the horizontal scrollbar will not be needed but it set to show the mis-sizing that
  // * might occur
  // */
  // JScrollPane sp = new JScrollPane(this,
  // ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
  // ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
  // sp.setViewportView(this);
  // // Resize the view to include the data width, borders, and vertical
  // // scrollbar
  // // sp.setPreferredSize(new Dimension(PANEL_WIDTH, finalHt));
  //
  // // Reset the client to the top of the view (instead of the bottom)
  // // sp.getVerticalScrollBar().setValue(sp.getVerticalScrollBar().getMinimum());
  //
  // return sp;
  // }


  // // WRITING TO PERSON FILE (either original or new file name)
  // if (_hdCiv.savePerson() == true) {
  // JOptionPane.showMessageDialog(null,
  // CONFIRM_SAVE_MSG, CONFIRM_SAVE_TITLE,
  // JOptionPane.INFORMATION_MESSAGE);
  // setVisible(false);
  // return true;
  // }
  // else {
  // JOptionPane.showMessageDialog(null, OPEN_ERROR_MSG,
  // OPEN_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
  // return false;
  // }
  // }

  /**
   * Prompts for a new name for the Hero
   */
  private void renameHero()
  {
    final String RENAME_MSG = "Enter new name: ";
    final String RENAME_TITLE = "RENAME HERO";
    
    // Pop up window for rename
    String newName = JOptionPane.showInputDialog(this, RENAME_MSG, RENAME_TITLE, 
        JOptionPane.QUESTION_MESSAGE);

    _hdCiv.renamePerson(newName);
    // Replace the Hero in storage Update the display
    _hdCiv.savePerson(OVERWRITE);
//    _hdCiv.redisplayHero();
  }


  /**
   * Saves the Person currently being displayed into the Dormitory
   * 
   * @return true if the Person was created and saved successfully, else false
   */
  private boolean savePerson()
  {
    boolean retflag = false;
    // Save Hero is he/she doesn't exist in the Dormitory
    if (_hdCiv.savePerson(NO_OVERWRITE)) {
      retflag = true;
    }
    return retflag;
  }


  // // User chose to OVERWRITE
  // if (choice == JOptionPane.YES_OPTION) {
  // boolean retflag = _hdCiv.savePerson(overwrite);
  // // Display confirmation message
  // JOptionPane.showMessageDialog(null, _ds.get(PersonKeys.NAME) + CONFIRM_OVERWRITE_MSG,
  // CONFIRM_OVERWRITE_TITLE, JOptionPane.INFORMATION_MESSAGE);
  // return retflag;
  // }
  // // User chose to CANCEL
  // else if (choice == JOptionPane.NO_OPTION) {
  // _hdCiv.back();
  // return false;
  // }
  // // User chose to RENAME
  // else {
  // return renamePerson();
  // // // change save message to avoid throwing error message
  // // _saveMsg = this.SAVE_CANCEL_MSG;
  // }
  // }}


  // // WRITING TO PERSON FILE (either original or new file name)
  // if (_hdCiv.savePerson() == true) {
  // JOptionPane.showMessageDialog(null,
  // CONFIRM_SAVE_MSG, CONFIRM_SAVE_TITLE,
  // JOptionPane.INFORMATION_MESSAGE);
  // setVisible(false);
  // return true;
  // }
  // else {
  // JOptionPane.showMessageDialog(null, OPEN_ERROR_MSG,
  // OPEN_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
  // return false;
  // }
  // }

  // // WRITING TO PERSON FILE (either original or new file name)
  // if (_hdCiv.savePerson() == true) {
  // JOptionPane.showMessageDialog(null,
  // CONFIRM_SAVE_MSG, CONFIRM_SAVE_TITLE,
  // JOptionPane.INFORMATION_MESSAGE);
  // setVisible(false);
  // return true;
  // }
  // else {
  // JOptionPane.showMessageDialog(null, OPEN_ERROR_MSG,
  // OPEN_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
  // return false;
  // }
  // }

  /** Swap the main panel title with the HeroDisplay title */
  private void setHeroAsTitle()
  {
    // Two-row namePlate before Attribute grid: Name, Gender, Race, Klass
    String namePlate = _ds.get(PersonKeys.NAME) + ": "
        + _ds.get(PersonKeys.GENDER) + " "
        + _ds.get(PersonKeys.RACENAME) + " "
        + _ds.get(PersonKeys.KLASSNAME);

    super._title = namePlate;
  }


  /**
   * Creates the panel and container fields for the given Hero, using a data map from the model to
   * populate the widget. The HeroDisplay panel contains three tabs: one for attributes, one for
   * inventory, and one for magic spells
   * 
   * @param firstTime Hero activates buttons differently than dormitory hero
   */
  private boolean setupDisplay(boolean firstTime)
  {
    // GENERAL SETUP
    setLayout(new MigLayout());
    // Set the preferred and max size
    // setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    setBackground(_backColor);

    // Display Hero in title in Runic Font
    setHeroAsTitle();

    // Add the tabbed pane for attributes, inventory and magic tab displays
    JTabbedPane tabPane = new JTabbedPane();

    // Create HeroDisplay panel tabs
    tabPane.addTab("Attributes", null, buildAttributePanel(),
        "View Hero's personal characteristics");
    // tabPane.addTab("Skills & Abilities", null, buildSkillsPanel(),
    // "View Hero's special skills and abilities");
    tabPane.addTab("Skills & Abilities", null, buildSkillsPanel(),
        "View Hero's special skills and abilities");
    tabPane.addTab("Inventory", null, buildInventoryPanel(),
        "View Hero's items owned, worn, or wielded");
    tabPane.addTab("Magic Items", null, buildMagicPanel(),
        "View Hero's enchanted items");
    tabPane.setSelectedIndex(0); // set default tab

    // Create the conditional tab for magic items for all klasses
    String klassname = _ds.get(PersonKeys.KLASSNAME);
    // Only for Clerics
    if (klassname.equalsIgnoreCase("Cleric")) {
      tabPane.addTab("Sacred Satchel", null, buildMaterialsPanel(),
          "View Hero's materials needed for spells.");
      tabPane.addTab("Spell Book", null, buildSpellsPanel(), "View Hero's known spells.");
    }
    // Only for Wizards
    if (klassname.equalsIgnoreCase("Wizard")) {
      tabPane.addTab("Magic Bag", null, buildMaterialsPanel(),
          "View Hero's materials needed for spells.");
      tabPane.addTab("Spell Book", null, buildSpellsPanel(), "View Hero's known spells.");
    }

    // Add the tabs to the HeroDisplay panel
    add(tabPane, "center, wrap");

    // ADD SAVE, DELETE, & CANCEL BUTTONS TO THE BOTTOM OF THE PANEL
    _buttonPanel = buildButtonPanel(firstTime);
    _buttonPanel.setPreferredSize(new Dimension(PANEL_WIDTH, _buttonPanel.getHeight()));
    add(_buttonPanel, "span, center, gapbottom 20");

    // ADD HELP MESSAGE TO INSTRUCT HOW TO SHIFT FOCUS
    add(new JLabel(HELP_LABEL), "span, center, gapbottom 5");

    return true;
  }


  // //TODO: Redo showInventoryByCategory
  // /** Retrieve a list of items for a particular category and display. All
  // Items in
  // * this list have the same category name.
  // *
  // * @param itemMap the Item fields for a particular category
  // * @param zone the text area in which to display the inventory sublist
  // */
  // private void showInventoryByCategory(
  // EnumMap<ItemFields, String> itemMap, JTextArea zone)
  // {
  // // Guard: Default entry for no items in the category
  // if (itemMap.isEmpty() == true) {
  // zone.append(" None\n");
  // return;
  // }
  // // Get the category name of the Item group to be displayed
  // String catName = itemMap.get(ItemFields.CATEGORY);
  // MsgCtrl.msgln("\tCategory to display = " + catName);
  // zone.append(" " + catName + "\n");
  // }

} // end HeroDisplay class

