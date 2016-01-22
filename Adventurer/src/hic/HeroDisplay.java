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

import mylib.Constants;
import net.miginfocom.swing.MigLayout;
import chronos.civ.PersonKeys;
import chronos.pdc.MiscKeys.ItemCategory;
import chronos.pdc.character.Inventory;
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
  private final String PROMPT_HERO_EXISTS_MSG =
      "Do you want to overwrite, rename, or create a new Hero?";
  private final String PROMPT_HERO_EXISTS_TITLE = "Hero already exists.";
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

  private final int PANEL_WIDTH = Mainframe.getWindowSize().width / 2;
  private final int PANEL_HEIGHT = Mainframe.getWindowSize().height;
  private final int DATA_WIDTH = PANEL_WIDTH / 2 - Mainframe.PAD;

  /** Background color inherited from parent */
  private Color _backColor = Constants.MY_BROWN;

  /** The backend CIV for this panel */
  private HeroDisplayCiv _hdCiv = null;

  /** Keys to Hero data to be displayed */
  EnumMap<PersonKeys, String> _ds;
  private String _heroName;
  private JPanel _skillPanel;
  private JPanel _invenPanel;
  private JPanel _magicPanel;
  private JTabbedPane _tabPane;

  // ===============================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ===============================================================

  /**
   * Create the GUI and populate it with various data maps
   * 
   * @param hdCiv the intermediary between this GUI and the Person
   * @param ds 
   */
  public HeroDisplay(HeroDisplayCiv hdCiv, EnumMap<PersonKeys, String> ds)
  {
    super(heroNameplate(ds));

    _hdCiv = hdCiv;
    _ds = ds;
    _heroName = ds.get(PersonKeys.NAME);

    _tabPane = new JTabbedPane();
    _skillPanel = createTabPanel();
    _invenPanel = createTabPanel();
    _magicPanel = createTabPanel();

    setupDisplay();
  }

  

  // =================================================================
  // PRIVATE METHODS
  // =================================================================

  /**
   * Creates the panel and container fields for the given Hero, using a data map from the model to
   * populate the widget. The HeroDisplay panel contains three tabs: one for attributes, one for
   * inventory, and one for magic spells
   * 
   * @param firstTime Hero activates buttons differently than dormitory hero
   */
  private void setupDisplay()
  {
    // GENERAL SETUP
    setLayout(new MigLayout());
    setBackground(_backColor);

    JTabbedPane tabPane = buildTabPane();
    add(tabPane, "center, wrap");

    JPanel buttonPanel = buildButtonPanel();
    buttonPanel.setPreferredSize(new Dimension(PANEL_WIDTH, buttonPanel.getHeight()));
    add(buttonPanel, "span, center, gapbottom 20");
  }

  // Add the tabbed pane for attributes, inventory and magic tab displays
  private JTabbedPane buildTabPane()
  {
    _tabPane.addTab("Attributes", null, buildAttributePanel(),
        "View Hero's personal characteristics");

    _tabPane.addTab("Skills & Abilities", null, _skillPanel, "View Hero's special skills and abilities");
    _tabPane.addTab("Inventory", null, _invenPanel, "View Hero's items owned, worn, or wielded");
    _tabPane.addTab("Magic Items", null, _magicPanel, "View Hero's enchanted items");
    _tabPane.setSelectedIndex(0);

    return _tabPane;
  }

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

  }

  /**
   * Create Save, Delete, and Cancel buttons, then add then to a JPanel
   * 
   * @param firstTime Hero disables DELETE button; old Hero disables CANCEL button
   * @return button panel
   */
  private JPanel buildButtonPanel()
  {
    JButton saveButton = new JButton("Save");
    saveButton.addActionListener(new SaveActionListener());

    JButton delButton = new JButton("Delete");
    delButton.addActionListener(new DeleteActionListener());

    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(new CancelActionListener());

    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(_backColor);

    delButton.setEnabled(false);
    cancelButton.setEnabled(true);

    buttonPanel.add(saveButton);
    buttonPanel.add(delButton);
    buttonPanel.add(cancelButton);

    return buttonPanel;
  }


  // Build panel of skills: literacy, racial skills, occupational skills, and klass skills
  public void addSkills(List<String> ocpSkills, List<String> racialSkills,
      List<String> klassSkills)
  {
    // Section 1: Literacy
    _skillPanel.add(gridCell("", _ds.get(PersonKeys.LITERACY)), "gaptop 10, span 6, growx, wrap");

    // Section 2: Occupational Skills title
    String occupation = _ds.get(PersonKeys.OCCUPATION);
    int nameIndex = occupation.indexOf(":");
    String ocpName = occupation.substring(0, nameIndex);
    String ocpDesc = occupation.substring(nameIndex + 1);
    String ocpTitle = ocpName.toUpperCase() + " SKILLS: " + ocpDesc;
    _skillPanel.add(buildMultiCell(ocpTitle, ocpSkills), "growx, wrap");

    // Section 3: Racial skills
    _skillPanel
        .add(buildMultiCell("SPECIAL " + _ds.get(PersonKeys.RACENAME).toUpperCase() + " SKILLS: ",
            racialSkills), "growx, wrap");

    // Section 4: Klass skills
    _skillPanel.add(buildMultiCell(_ds.get(PersonKeys.KLASSNAME).toUpperCase() + " SKILLS: ",
        klassSkills), "growx, wrap");
  }

  /**
   * Builds the inventory "table", showing Category, Items, quantity, weight (in lbs & oz of each,
   * and total. Wielded Weapon and Armor Worn is at top. Inventory is organized by category: ARMS,
   * ARMOR, CLOTHING, EQUIPMENT, PROVISIONS, LIVESTOCK, TRANSPORT : item, quantity, weight, total
   * weight. See also Sacred Satchel and Magic Bag
   * @param inventory 
   */
  public void addInventory(Inventory inventory)
  {  
    _invenPanel.add(gridCell("Wielded Weapon:", "None"), "gaptop 10, span 6, growx, wrap 0");
    _invenPanel.add(gridCell("Armor Worn: ", " None"), "span 6, growx, wrap 0");
  
    JPanel blankLine = gridCell("", "");
    blankLine.setBackground(Color.DARK_GRAY);
    _invenPanel.add(blankLine, "span 6, growx, wrap 0");
  
    for (ItemCategory category : ItemCategory.values()) {
      List<String> nameList = inventory.getNameList(category);
      _invenPanel.add(buildMultiCell(category.toString(), nameList), "growx, wrap");
    }
  }

  /**
   * Builds the inventory of magic items (not spell materials)
   * @param nameList 
   */
  public void addMagicItem(List<String> nameList)
  {
    _magicPanel.add(buildMultiCell("MAGIC ITEMS", nameList), "growx, wrap 0");
  }

  /**
   * Builds the inventory of (non-magical) spell material used for spells
   */
  public void addMaterials(List<String> itemList)
  {
    JPanel materialPanel = createTabPanel();
    materialPanel.add(buildMultiCell("SPELL MATERIALS", itemList), "growx, wrap 0");
    _tabPane.addTab("Magic Bag", null, materialPanel, "View Hero's materials needed for spells.");
  }

  /**
   * Builds the list of spells that spellcaster knows
   */
  public void addSpell(List<String> spellList)
  {
    JPanel spellPanel = createTabPanel();

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

    _tabPane.addTab("Spell Book", null, spellPanel, "View Hero's known spells.");
  }


  

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
        _hdCiv.back();
        break;

      case PROMPT_RENAME:
        renameHero();
        JOptionPane.showMessageDialog(this, _heroName + CONFIRM_RENAME_MSG,
            CONFIRM_RENAME_TITLE, JOptionPane.INFORMATION_MESSAGE);
        _hdCiv.backToMain();
        break;

      case PROMPT_OVERWRITE:
        _hdCiv.savePerson(OVERWRITE);
        JOptionPane.showMessageDialog(this, _heroName + CONFIRM_OVERWRITE_MSG,
            CONFIRM_OVERWRITE_TITLE, JOptionPane.INFORMATION_MESSAGE);
        // Return to main action buttons
        _hdCiv.backToMain();
        break;

      default:
        _hdCiv.backToMain();
        break;
    }
  }



  private JPanel createTabPanel()
  {
    JPanel panel = new JPanel(new MigLayout("fillx, ins 0"));
    panel.setBackground(_backColor);
    return panel;
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
    msgArea.setLineWrap(true); // auto line wrapping doesn't seem to work
    msgArea.setWrapStyleWord(true);
  
    // Display the title
    msgArea.append(" " + title + Constants.NEWLINE);
  
    // Display the detailed skill list
    if (nameList.size() == 0) {
      msgArea.append(" + None");
    } else {
      for (String name : nameList) {
        msgArea.append(" + " + name + Constants.NEWLINE);
      }
    }
  
    // Add the text area into a JPanel cell
    JPanel cell = new JPanel(new MigLayout("ins 0"));
    cell.add(msgArea, "growx, wrap");
    cell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    return cell;
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
    JPanel p = new JPanel(new MigLayout("inset 3"));
    p.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

    //    int cellWidth = DATA_WIDTH / PANELS_IN_ROW;
    //    p.setPreferredSize(new Dimension(cellWidth, FONT_HT));

    //    int datalen = label.length() + value.length() + 1;
    //    if (datalen > cellWidth) {
    //      String multiline = "<html>" + label + "<br>" + value + "<br></html>";
    //      p.add(new JLabel(multiline, SwingConstants.LEFT));

    p.add(new JLabel(label, SwingConstants.LEFT));
    p.add(new JLabel(value, SwingConstants.RIGHT));

    return p;
  }

  /**
   * Delete the Person currently being displayed into a new file.
   * 
   * @return true if the Person was removed successfully, else false
   */
  private boolean deletePerson()
  {
    Object[] options = {"Yes", "No"};
    int n = JOptionPane.showOptionDialog(this, //parent
        "Are you sure you want to delete?",    //message
        "Delete confirmation",                 //title
        JOptionPane.YES_NO_OPTION,             //option type
        JOptionPane.QUESTION_MESSAGE,          //message type
        null,                                  //icon
        options,                               //options
        options[1]);                           //initial
    if (n == JOptionPane.YES_OPTION) {
      return _hdCiv.deletePerson();
    } else {
      return false;
    }
  }

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
    _hdCiv.savePerson(OVERWRITE);
  }

  /** Swap the main panel title with the HeroDisplay title 
   * @param ds 
   * @return */
  private static String heroNameplate(EnumMap<PersonKeys, String> ds)
  {
    // Two-row namePlate before Attribute grid: Name, Gender, Race, Klass
    String namePlate = ds.get(PersonKeys.NAME) + ": "
        + ds.get(PersonKeys.GENDER) + " "
        + ds.get(PersonKeys.RACENAME) + " "
        + ds.get(PersonKeys.KLASSNAME);

    return namePlate;
  }


  private final class CancelActionListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      _hdCiv.backToMain();
    }
  }


  private final class DeleteActionListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      if (deletePerson()) {
        JOptionPane.showMessageDialog(
            HeroDisplay.this,
            _heroName + CONFIRM_DEL_MSG,
            CONFIRM_DEL_TITLE,
            JOptionPane.INFORMATION_MESSAGE);
      } else {
        String[] sorry = new String[1];
        sorry[0] = "You dropped your scythe!!";
        JOptionPane.showOptionDialog(
            HeroDisplay.this,
            DEL_ERROR_MSG + _heroName,
            DEL_ERROR_TITLE,
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.ERROR_MESSAGE,
            null,
            sorry,
            null);
      }
    }
  }


  private final class SaveActionListener implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      if (_hdCiv.savePerson(NO_OVERWRITE)) {
        JOptionPane.showMessageDialog(
            HeroDisplay.this,
            _heroName + CONFIRM_SAVE_MSG,
            CONFIRM_SAVE_TITLE,
            JOptionPane.INFORMATION_MESSAGE);
        _hdCiv.backToMain();
      } else {
        // Respond to save attempt failure to Rename or Overwrite the Hero
        doAlternateSaveAction();
      }
    }
  }
} // end HeroDisplay class

