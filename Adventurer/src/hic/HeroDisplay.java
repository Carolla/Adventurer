/**
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
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import chronos.Chronos;
import civ.HeroDisplayCiv;
import civ.MiscKeys.ItemCategory;
import civ.PersonKeys;
import mylib.MsgCtrl;
import net.miginfocom.swing.MigLayout;


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
 *          Oct 1 2015 // revised to accommodate new Hero generation rules <br>
 */
public class HeroDisplay extends JPanel
{
  /**
   * Generated using system clock
   */
  private static final long serialVersionUID = 3532162719501483676L;

  private static final int PIX_PER_CHAR = 8;

  /** Help message to show in panels */
  private final String HELP_LABEL = "Press F1 key for help.";

  // Specific file error messages not handled by FileChooser
  private String _saveMsg = "";
  private final String SAVE_ERROR_MSG = "Error! Problem saving ";
  private final String SAVE_CANCEL_MSG = "Overwrite cancelled for ";
  private final String SAVE_ERROR_TITLE = "FILE SAVE ERROR";
  private final String CONFIRM_SAVE_MSG = " is resting in the dormitory until later.";
  private final String CONFIRM_SAVE_TITLE = " Hero is now Registered";

  // Specific file error messages not handled by FileChooser
  private final String DEL_ERROR_MSG = "Error! Problem deleting ";
  private final String DEL_ERROR_TITLE = "HERO DELETE ERROR";
  private final String CONFIRM_DEL_MSG = " is now in a better place.";
  private final String CONFIRM_DEL_TITLE = " Hero is now Deceased";

  /** Tab sizing between inventory columns */
  private final int TAB_SIZE = 6;
  // /** Height needed for the Save and Cancel buttons */
  // private final int BUTTON_HT = 50;
  /** Height of font for vertical spacing */
  private final int FONT_HT = 14;
  /** Normal font height */
  private final int ATT_FONT_HT = 12;
  /** Size for name */
  private final float NAME_HT = 30f;
  /** Border width for main panel */
  private final int SCROLLBAR_SIZE = 20;
  /** Standard height of font and cells in display, but can be changed */
  private int CELL_HEIGHT = 26;
  // /** Standard height of font and cells in display, but can be changed */
  // private int CELL_WIDTH = 12;
  /** Border width for main panel */
  private final int THICK_BORDER = Mainframe.PAD;
  /** Border width for subpanel */
  private final int THIN_BORDER = Mainframe.PAD / 2;
  /** Set the max width of the hero panel at half screen */
  // final int PANEL_WIDTH = Mainframe.USERWIN_WIDTH / 2;
  private final int PANEL_WIDTH = Mainframe.getWindowSize().width / 2;

  /** Set the width of the data panels within the display borders */
  final int DATA_WIDTH = PANEL_WIDTH - SCROLLBAR_SIZE - 2
      * (THICK_BORDER + THIN_BORDER);
  final int DATA_PAD = 50;

  // /** Keep a reference to this scrollpane that contains this display*/
  // private JScrollPane _heroScroll = null;

  /** Size of inventory area */
  final int INVEN_HEIGHT = 30;
  /** Number of cash fields */
  final int CASH_FIELDS = 2;

  // /** HelpDialog reference for all widgets that have context help */
  // private HelpDialog _help = null;
  // /** Set the default HelpKey for the general panel */
  // private HelpKeyListener _helpKey = new HelpKeyListener("HeroDsp");

  /** Preferred (calculated) width for HeroDisplay */
  int _panelWidth = 0;
  /** Preferred (calculated) height for HeroDisplay */
  int _panelHeight = 0;
  // /** Maximum size of cell in attribute panel */
  // private final int CELL_MAX_WIDTH = 17;

  /** Disable SaveAs at some time . */
  private JButton _saveButton = null;
  /** Change Cancel Button to OK Button at some time . */
  private JButton _cancelButton = null;
  /** Gets rid of unused/unwanted characters */
  private JButton _delButton;

  /** Background color inherited from parent */
  private Color _backColor = null;

  /** Tabbed pane to hold inventory stuff */
  JTabbedPane _tab = null;

  /** The backend CIV for this JPanel widget */
  private HeroDisplayCiv _hdCiv = null;

  /** Keys to Hero data to be displayed */
  EnumMap<PersonKeys, String> _ds;

  /** Items to be displayed */
  private ArrayList<String> _itemList = null;

  // /** Minimum display width */
  // private final int MINIMUM_DISPLAY = 500;

  /** Default inset values */
  private final int DEF_INSET = 3;

  /** Five panels in each attribute row */
  private final int PANELS_IN_ROW = 5;

  /** Crude attempt to allocate space for wrappping text */
  private final double WRAP_ADJ = 1.4;

  private final int SPACES_PER_LINE = 27;

  private final int SKILL_LINE_HT = FONT_HT * 4;

  /** Button panel for Save/Delete/Cancel buttons */
  private JPanel _buttonPanel;

  /** Attribute panel where characters atts are displayed */
  private JPanel _attribPanel;

  /** JLabel to hold character's name */
  private JLabel _charName;

  private MainframeInterface _mainframe;


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /** Default constructor */
  public HeroDisplay()
  {
    setupDisplay();
  }


  /**
   * Create the GUI and populate it with various data maps
   * 
   * @param hdCiv the intermediary between this GUI and the Person
   */
  public HeroDisplay(HeroDisplayCiv hdCiv, MainframeInterface mainframe)
  {
    _hdCiv = hdCiv;
    _mainframe = mainframe;
    // _hdCiv.resetLoadState();
    setupDisplay();
    // _mainframe.add(this, BorderLayout.WEST);
  }


  /** Stub until the display is working properly. */
  public boolean displayAttributes(EnumMap<PersonKeys, String> ds)
  {
    // Set private field equal to passed parameter
    _ds = ds;

    // Generate titled border (banner) containing Hero's name
    int mfpad = Mainframe.PAD;
    String mftitle = " Attributes for " + _ds.get(PersonKeys.NAME) + " ";
    Border thisBorder = BorderFactory.createMatteBorder(mfpad, mfpad,
        mfpad, mfpad, Color.WHITE);
    Border titledBorder = BorderFactory.createTitledBorder(thisBorder,
        mftitle, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
    setBorder(titledBorder);

    // ADD HELP MESSAGE TO INSTRUCT HOW TO SHIFT FOCUS
    add(new JLabel(HELP_LABEL), "span");
    //
    // // Use a larger special font for the Hero's name. Extract the label font
    // // size from the cell
    Font nameFont = null;
    try {
      // Returned font is of pt size 1
      Font newFont = Font.createFont(Font.TRUETYPE_FONT, new File(
          Chronos.RUNIC_ENGLISH_FONT_FILE));

      // Derive a 30 pt version:
      nameFont = newFont.deriveFont(NAME_HT);
    } catch (FontFormatException e) {
      MsgCtrl.errMsgln("Could not format font: " + e.getMessage());
    } catch (IOException e) {
      MsgCtrl.errMsgln("Could not create font: " + e.getMessage());
    }
    // NamePlate before Attribute grid: Name, gender, Race, Klass
    String namePlate = _ds.get(PersonKeys.NAME) + ": "
        + _ds.get(PersonKeys.GENDER) + " "
        + _ds.get(PersonKeys.RACENAME) + " "
        + _ds.get(PersonKeys.KLASSNAME);
    // Put some space above and below the namePlate
    _charName = new JLabel(namePlate);

    GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
    String[] fontNames = env.getAvailableFontFamilyNames();
    _charName.setFont(nameFont);

    // TODO: Update this to change the size Tim
    while (_charName.getPreferredSize().width > DATA_WIDTH) {
      // Edit name font down a notch or two
      Font labelFont = _charName.getFont();
      String labelText = _charName.getText();

      int stringWidth = _charName.getFontMetrics(labelFont).stringWidth(
          labelText);
      int componentWidth = DATA_WIDTH;

      // Find out how much the font can grow in width.
      double widthRatio = (double) componentWidth / (double) stringWidth;

      int newFontSize = (int) (labelFont.getSize() * widthRatio);

      // Recreate the new font
      try {
        Font newFont = Font.createFont(Font.TRUETYPE_FONT, new File(
            Chronos.RUNIC_ENGLISH_FONT_FILE));

        // Set the label's font size to the newly determined size.
        Font smallNameFont = newFont.deriveFont(newFontSize);
        _charName.setFont(smallNameFont);
      } catch (FontFormatException e) {
        MsgCtrl.errMsgln("Could not format font: " + e.getMessage());
      } catch (IOException e) {
        MsgCtrl.errMsgln("Could not create font: " + e.getMessage());
      }
    }
    add(_charName, "span, gaptop 5, gapbottom 8");

//    // Display Ability Scores
//    List<Integer> attList = (List<Integer>) ds.get(PersonKeys.ABILITY_SCORES);
//    List<String> attributes = new NewHeroCiv().getAttributes();
//
//    JPanel scorePanel = new JPanel();
//
//    int i = 0;
//    for (String att : attributes) {
//      JLabel scoreLabel = new JLabel(att + ": " + attList.get(i).toString());
//      scorePanel.add(scoreLabel);
//      i++;
//    }
//
//    add(scorePanel, "span");

    // // CREATE THE ATTRIBUTE GRID PANEL AND SIZE IT FOR DISPLAY
    _attribPanel = buildAttributePanel();
    // Ensure that the attribute panel does not exceed the HeroDisplay panel width
    _attribPanel.setPreferredSize(new Dimension(DATA_WIDTH, _attribPanel.getHeight()));
    // attribPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    add(_attribPanel, "span, center, gapbottom 0");// , span, growx");
    //
    // // ADD SAVE & CANCEL BUTTONS TO THE BOTTOM OF THE PANEL
    _buttonPanel = buildButtonPanel();
    _buttonPanel.setPreferredSize(new Dimension(DATA_WIDTH, _buttonPanel.getHeight()));
    add(_buttonPanel, "span, center, gapbottom 5");
    
//     Mainframe frame = Mainframe.getInstance();
//      frame.changeToLeftPanel(this);
//      revalidate();
//      repaint();
    return true;
  }


  /** Stub until the display is working properly. */
  public boolean displayInventory(ArrayList<String> items)
  {
    // Put some space above and below the namePlate
    // Add a non-editable textArea containing all the Inventory
    JTextArea invenHeader = new JTextArea();
    invenHeader.setPreferredSize(new Dimension(DATA_WIDTH - SCROLLBAR_SIZE,
        FONT_HT));
    invenHeader.setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_HT));
    invenHeader.setBackground(_backColor);
    // Start the column headers
    invenHeader.setText("   QTY   ITEM\t\t\t    WEIGHT (ea)\n");
    StringBuilder b = new StringBuilder();
    int numDash = (DATA_WIDTH - SCROLLBAR_SIZE) / PIX_PER_CHAR;
    for (int i = 0; i < numDash; i++) {
      b.append("-");
    }
    invenHeader.append(b.toString());
    invenHeader.setEditable(false);

    _itemList = items;

    // Set up scrolling area
    JTextArea inventoryArea = buildInventoryArea();
    // IMPORTANT: This select recenters screen to the top/left
    inventoryArea.select(0, 0);
    JScrollPane inventoryScroll = new JScrollPane(inventoryArea);
    inventoryScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
    inventoryScroll
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    Dimension scrollSize = new Dimension(
        inventoryArea.getPreferredSize().width,
        inventoryArea.getPreferredSize().height + FONT_HT);
    inventoryScroll.setPreferredSize(scrollSize);

    // inventoryScroll.getVerticalScrollBar().setValue(inventoryScroll.getVerticalScrollBar().getMinimum());
    JPanel wholeInventory = new JPanel(new MigLayout("fill, wrap 5, ins 2", // layout
                                                                            // constraints
        "[]0[]0[]0[]0[]0[][left]", // align left
        "[]0[]0[]0[]0[]0[][bottom]"));
    wholeInventory.setPreferredSize(new Dimension(DATA_WIDTH, inventoryArea
        .getHeight() + invenHeader.getHeight()));
    wholeInventory.setBackground(_backColor);
    wholeInventory.setBorder(new EmptyBorder(0, 0, 0, 0));
    wholeInventory.add(invenHeader, "span, gapbottom 0");
    wholeInventory.add(inventoryScroll, "span, gaptop 0, gapbottom 0");

    // Put onto tabs
    _tab = new JTabbedPane();
    _tab.addTab("Inventory", wholeInventory);
    // _tab.setBorder(new EmptyBorder(0,0,0,0));
    // _tab.setPreferredSize(new Dimension (DATA_WIDTH,_panelHeight/3));
    _tab.setBackground(_backColor);

    // System.out.println("invenHeader width: " +
    // invenHeader.getPreferredSize());
    // System.out.println("invenArea width: " +
    // inventoryArea.getPreferredSize());
    // System.out.println("invenScroll width: " +
    // inventoryScroll.getPreferredSize());
    // System.out.println("wholeInventory width: " +
    // wholeInventory.getPreferredSize());
    // System.out.println("tab width: " + _tab.getPreferredSize());

    // Make magic tab
    JTextArea magicArea = new JTextArea();
    magicArea.setBackground(_backColor);
    magicArea.setEditable(false);
    _tab.addTab("Magic", magicArea);
    _tab.setEnabledAt(_tab.indexOfTab("Magic"), false);
    // add(_tab,"span, center");

    // MainFrame.getInstance().validate();
    // MainFrame.getInstance().repaint();
    return true;
  }


  /** Stub until the display is working properly. */
  public boolean displaySkills(ArrayList<String> skillList)
  {
    JTextArea skillHeader = new JTextArea();
    skillHeader.setPreferredSize(new Dimension(DATA_WIDTH - SCROLLBAR_SIZE,
        FONT_HT));
    skillHeader.setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_HT));
    skillHeader.setBackground(_backColor);
    // Start the column headers
    skillHeader.setText("NAME   DESCRIPTION\n");

    StringBuilder b = new StringBuilder();
    int numDash = (DATA_WIDTH - SCROLLBAR_SIZE) / PIX_PER_CHAR;
    for (int i = 0; i < numDash; i++) {
      b.append("-");
    }
    skillHeader.append(b.toString());
    skillHeader.setEditable(false);

    // Make skill tab
    JTextArea skillArea = new JTextArea(skillList.size(), 0);
    // Set up area to wrap on long descriptions
    skillArea.setLineWrap(true);
    skillArea.setWrapStyleWord(true);

    // Format remaining agrea
    skillArea.setBackground(_backColor);
    skillArea.setEditable(false);
    skillArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_HT));
    skillArea.setBackground(_backColor);

    // Set size of skill Area prior to adding to ScrollPane
    skillArea.setPreferredSize(new Dimension(DATA_WIDTH, skillList.size()
        * SKILL_LINE_HT));

    // Get all the skills and descriptions
    for (int i = 0; i < skillList.size(); i++) {
      String str = skillList.get(i);
      StringTokenizer st = new StringTokenizer(str, "|");

      // First get skill name and append it
      String skillName = st.nextToken();
      skillArea.append(skillName + "\n");

      // Then discard unneeded contents
      // st.nextToken(); // Race
      // st.nextToken(); // Klass

      // Then get description and append it
      String skillDesc = st.nextToken();
      skillArea.append("       " + skillDesc + "\n");
    }

    // IMPORTANT: This select recenters screen to the top/left
    skillArea.select(0, 0);

    JPanel allSkills = new JPanel(new MigLayout("fill, wrap 5, ins 2", // layout
                                                                       // constraints
        "[]0[]0[]0[]0[]0[][left]", // align left
        "[]0[]0[]0[]0[]0[][bottom]"));
    allSkills.setPreferredSize(new Dimension(DATA_WIDTH, (int) ((skillArea
        .getRows() + skillHeader.getRows()) * FONT_HT * WRAP_ADJ)));
    allSkills.setBackground(_backColor);
    allSkills.setBorder(new EmptyBorder(0, 0, 0, 0));
    allSkills.add(skillHeader, "span, gapbottom 0");

    // Put skills onto scroll panel
    JScrollPane skillScroll = new JScrollPane(skillArea);
    skillScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
    skillScroll
        .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    Dimension scrollSize = new Dimension(
        skillArea.getPreferredSize().width,
        (int) (skillArea.getPreferredSize().height * WRAP_ADJ)
            + FONT_HT);
    skillScroll.setPreferredSize(scrollSize);
    allSkills.add(skillScroll, "span, gaptop 0, gapbottom 0");

    _tab.addTab("Skills", allSkills);
    _tab.setEnabledAt(_tab.indexOfTab("Skills"), true);
    // _tab.setPreferredSize(new Dimension (DATA_WIDTH,_panelHeight/3));

    // System.out.println("skillHeader width: " +
    // skillHeader.getPreferredSize());
    // System.out.println("skillArea width: " +
    // skillArea.getPreferredSize());
    // System.out.println("skillScroll width: " +
    // skillScroll.getPreferredSize());
    // System.out.println("allSKills width: " +
    // allSkills.getPreferredSize());
    // System.out.println("tab width: " + _tab.getPreferredSize());

    add(_tab, "span, grow, push, center");

    _mainframe.repaint();

    return true;
  }


  /**
   * Put the HeroDisplay panel into a scrollpane and add to the MainFrame
   * 
   * @return the scrollpane for the Person's attributes
   */
  public JScrollPane display()
  {
    // Make the display scrollable to add to the MainFrame
    JScrollPane heroScroll = makeScrollable();

    // Add the non-static scrolling panel to the main JFrame
    _mainframe.add(heroScroll, BorderLayout.WEST);
    _mainframe.repaint();
    // Get the focus so the arrow keys will work
    requestFocusInWindow();
    return heroScroll;
  }


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
   * Create the person's attributes in a grid panel for display Attributes are unpacked from the
   * data shuttle as they are needed, row by row HEURISTIC LAYOUT: Character attibutes, e.g. XP and
   * Level, go toward the left side; Personal attributes, such as gender, weight, and height, go
   * toward the right side. The more important attributes, e.g. HP and Klass, go toward the top;
   * lesser ones, e.g. description and gold banked, toward the bottom.
   * 
   * @return the attribute grid panel
   */
  private JPanel buildAttributePanel()
  {
    // Create a layout that spans the entire width of the panel
    JPanel attribPanel = new JPanel(new MigLayout("fill, wrap 5, ins 0", // layout constraints
        "[]0[]0[]0[]0[]0[][left]", // align left
        "[]0[]0[]0[]0[]0[][bottom]")); // align bottom

    attribPanel.setBackground(_backColor);

    // // Row 1: XP, Level, Hit Points, Occupation, Hunger state
    // attribPanel.add(gridCell("XP: ", (String) _ds.getField(PersonKeys.XP)), "sg r1");
    // attribPanel.add(
    // gridCell("Level: ", (String) _ds.getField(PersonKeys.LEVEL)), "sg r1");
    // attribPanel.add(gridCell("HP: ", (String) _ds.getField(PersonKeys.HP)), "sg r1");
    // attribPanel.add(gridCell("", (String) _ds.getField(PersonKeys.OCCUPATION)), "sg r1");
    // attribPanel.add(gridCell("", (String) _ds.getField(PersonKeys.HUNGER)), "sg r1");

    // // Row 2: Armor Class, Speed, Age, Height, and Weight
    // attribPanel.add(gridCell("AC: ", (String) _ds.getField(PersonKeys.AC)), "sg r1");
    // attribPanel.add(gridCell("Age: ", (String) _ds.getField(PersonKeys.AGE)), "sg r1");
    // attribPanel.add(gridCell("Speed: ", (String) _ds.getField(PersonKeys.SPEED)), "sg r1");
    // attribPanel.add(gridCell("Ht: ", (String) _ds.getField(PersonKeys.HEIGHT)), "sg r1");
    // attribPanel.add(gridCell("Wt: ", ((String) _ds.getField(PersonKeys.WEIGHT)) + " lbs"), "sg
    // r1");
    //
    // // Row 3: Gold, Silver, Gold Banked, and Load
    // attribPanel.add(gridCell("Gold: ", ((String) _ds.getField(PersonKeys.GOLD)) + " gp"), "sg
    // r1");
    // attribPanel.add(gridCell("Silver: ", ((String) _ds.getField(PersonKeys.SILVER)) + " sp"),
    // "sg r1");
    // attribPanel.add(gridCell("Gold Banked: ", (String) _ds.getField(PersonKeys.GOLD_BANKED)),
    // "span 2, growx");
    // attribPanel.add(gridCell("Load: ", ((String) _ds.getField(PersonKeys.LOAD)) + " lbs"), "sg
    // r1");
    //
    // // Row 4: MaxLangs, Languages (remaining width of the panel)
    // JPanel maxLangCell = gridCell("Max Langs: ", (String) _ds.getField(PersonKeys.MAX_LANGS));
    // // maxLangCell.setSize(minCellWidth, 10);
    // attribPanel.add(maxLangCell, "sg r1"); // gridCell("Max Langs: ",
    // // (String)
    // // _ds.getField(PersonKeys.MAX_LANGS)),
    // // "sg r1");
    // attribPanel.add(gridCell("Known Languages: ", (String) _ds.getField(PersonKeys.LANGUAGES)),
    // "span 4, growx");
    //
    // // Row 5: Character Description
    // // Need special handling of "long" description
    // JPanel p = new JPanel(new MigLayout("left, inset 3"));
    // p.setBackground(_backColor);
    // p.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    //
    // JTextArea descPanel = new JTextArea("Description: "
    // + (String) _ds.getField(PersonKeys.DESCRIPTION));
    // descPanel.setPreferredSize(new Dimension(DATA_WIDTH, descPanel
    // .getLineCount() * FONT_HT));
    // descPanel.setBackground(_backColor);
    // descPanel.setEditable(false);
    // descPanel.setFont(new Font(Font.DIALOG, Font.PLAIN, ATT_FONT_HT));
    // descPanel.setLineWrap(true);
    // descPanel.setWrapStyleWord(true);
    // p.add(descPanel);
    // attribPanel.add(p, "span 5");

    return attribPanel;

  } // End of buildAttribute panel


  /**
   * Add Save and Cancel buttons to the display next
   * 
   * @return button panel
   */
  private JPanel buildButtonPanel()
  {
    final JPanel hd = this;
    // NOTE: Save action is invoked for new Characters */
    _saveButton = new JButton("Save");
    // _saveButton.setBackground(_backColor);
    _saveButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        // Audit statement
        MsgCtrl.traceEvent(event);
        if (savePerson() == true) {
          // Display confirmation message
          JOptionPane.showMessageDialog(null,
              _ds.get(PersonKeys.NAME) + CONFIRM_SAVE_MSG,
              CONFIRM_SAVE_TITLE, JOptionPane.INFORMATION_MESSAGE);
        } else {
          // Display an error message with a Sorry button instead of
          // OK
          String[] sorry = new String[1];
          sorry[0] = "SORRY!";
          JOptionPane.showOptionDialog(null,
              _saveMsg + _ds.get(PersonKeys.NAME),
              SAVE_ERROR_TITLE, JOptionPane.DEFAULT_OPTION,
              JOptionPane.ERROR_MESSAGE, null, sorry, null);
        }
        // Mainframe.getInstance().setEditFlag(false);
        // Reset menu options to original
        // Menubar.getInstance().resetMenus();
        // Remove this panel and ignore any changes in either case
        // Mainframe.getInstance().
        setVisible(false);
        // Mainframe frame = Mainframe.getInstance();
        // frame.resetPanels();
      }
    });

    _delButton = new JButton("Delete");
    _delButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        // Audit statement
        MsgCtrl.traceEvent(event);
        if (deletePerson() == true) {
          // Display confirmation message
          JOptionPane.showMessageDialog(null,
              _ds.get(PersonKeys.NAME) + CONFIRM_DEL_MSG,
              CONFIRM_DEL_TITLE, JOptionPane.INFORMATION_MESSAGE);
        } else {
          // Display an error message with a Sorry button instead of
          // OK
          String[] sorry = new String[1];
          sorry[0] = "You dropped your scythe!!";
          JOptionPane.showOptionDialog(null,
              DEL_ERROR_MSG + _ds.get(PersonKeys.NAME),
              DEL_ERROR_TITLE, JOptionPane.DEFAULT_OPTION,
              JOptionPane.ERROR_MESSAGE, null, sorry, null);
        }
        // Remove this panel and ignore any changes in either case
        // Mainframe.getInstance().setEditFlag(false);
        // Reset menu options to original
        // MenuBar.getInstance().resetMenus();
        // Remove this panel
        // Mainframe frame = Mainframe.getInstance();
        // frame.resetPanels();
        hd.setVisible(false);
      }

    });

    /* ADD A CANCEL BUTTON TO THE BOTTOM OF THE PANEL */
    _cancelButton = new JButton("Cancel");
    // _cancelButton.setBackground(_backColor);

    // Clear data and return back to mainframe if Cancel is pressed
    _cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        // Audit statement
        MsgCtrl.traceEvent(event);
        // Collect all the attributes and save to a new Hero file
        setVisible(false);
        // Mainframe frame = Mainframe.getInstance();
        // frame.resetPanels();
        // int response = mf.confirmQuit();
        // if (response == Mainframe.NOSAVE) {
        // Mainframe.getInstance().setEditFlag(false);
        // Reset menu options to original
        // MenuBar.getInstance().resetMenus();
        // Remove this panel and ignore any changes
        // hd.setVisible(false);
        // } else {
        // return;
      }
      // }
    });

    // Add buttons to buttonPanel, and buttonPanel to basePanel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(_backColor);
    // buttonPanel.setMaximumSize(new Dimension(DATA_WIDTH-DATA_PAD,
    // BUTTON_HT));

    // Add small space at the top of the button panel
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

  /**
   * Builds the inventory "table", showing Category, Items, quantity, and weight (in lbs) and weight
   * (in oz) of each; calls the display civ to pack a data shuttle
   * 
   * @return the output-only JTextArea contining inventory data
   */
  private JTextArea buildInventoryArea()
  {
    // Get the Inventory items to display
    int invenLen = _itemList.size() - CASH_FIELDS; // _hdCiv.getInventorySize();

    // Add a non-editable textArea containing all the Inventory
    JTextArea invenArea = new JTextArea(invenLen, 0); // rows by columns
    // Make area restricted to displayable size (without scrollbar)
    invenArea.setPreferredSize(new Dimension(DATA_WIDTH, invenLen
        * CELL_HEIGHT));
    invenArea.setEditable(false);
    invenArea.setBackground(_backColor);
    invenArea.setTabSize(TAB_SIZE);
    invenArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_HT));
    invenArea.setMargin(new Insets(DEF_INSET, DEF_INSET, DEF_INSET,
        DEF_INSET));
    int j = 0;
    int size = ItemCategory.values().length;
    // Read in item groups by category, input like GENERAL|Backpack|1|10|0"\
    for (int i = 0; i < size; i++) {
      if (j < _itemList.size()) {
        String str = _itemList.get(j);
        int firstBar = str.indexOf('|');
        String cat = str.substring(0, firstBar);
        String subcat = cat;
        if (cat.compareTo("CASH") != 0) {
          invenArea.append(cat + "\n");
        }
        while (subcat.compareTo(cat) == 0) {
          // Display and format item
          String item = str.substring(firstBar + 1, str.length());
          item = formatItemString(item);
          if (subcat.compareTo("CASH") != 0) {
            invenArea.append(item);
          }
          j++;

          // Now get next item
          if (j < _itemList.size()) {
            str = _itemList.get(j);
            firstBar = str.indexOf('|');
            subcat = str.substring(0, firstBar);
          } else {
            subcat = "NEWCAT";
          }
        }
      }
    }
    return invenArea;
  }


  /**
   * Format a fixed-field space-filled display string for the Inventory list If values are zero, the
   * String is empty FIELDS: Quantity, Name (left-justified), wt (lb), wt (oz)
   * 
   * @param name the name of the item
   * @param qty the number of such items
   * @param lbWt the weight of the thing in pounds
   * @param ozWt the fractional pound-weight in ounces
   * @return the Item's fields in a fixed-length formatted string
   */
  private String formatItemString(String str)
  // private String formatItemString(String qty, String name, String lbWt,
  // String ozWt)
  {
    // Given format: Backpack|1|10|0
    // Pull out the 4 values, Qty, Name, Weight(lb), and Weight(oz)
    int bar = str.indexOf('|');
    String itemName = str.substring(0, bar);
    int nextBar = str.indexOf('|', bar + 1);
    String itemQty = str.substring(bar + 1, nextBar);
    bar = nextBar;
    nextBar = str.indexOf('|', bar + 1);
    String itemLbWt = str.substring(bar + 1, nextBar);
    bar = nextBar;
    String itemOzWt = str.substring(bar + 1, str.length());

    // Now build the string
    String initialSpaces = "   ";
    StringBuilder lineItem = new StringBuilder(initialSpaces + itemQty
        + "\t   " + itemName);

    // Append an appropriate number of tabs
    for (int i = (SPACES_PER_LINE - itemName.length()); i > 0; i--) {
      lineItem.append(" ");
    }
    if (itemLbWt.length() < 2) {
      lineItem.append(" ");
    }
    lineItem.append(itemLbWt + " lb " + itemOzWt + " oz\n");
    return lineItem.toString();
  }


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /**
   * Creates a lined bordered cell containing a label and its value. The label and its value can be
   * aligned within the component. A JPanel type is returned so that the cell can also get the focus
   * for help listeners.
   * 
   * @param label the text of the label, can be null
   * @param value the stringified value to display after the label
   * @return bordered grid cell as Component
   */
  private JPanel gridCell(String label, String value)
  {
    // Guard against null values
    if ((label == null) || (value == null)) {
      return null;
    }
    // Create the grid cell as a panel to hold to JLabels within a border
    JPanel p = new JPanel(new MigLayout("left, inset 3"));
    p.setBackground(_backColor);

    p.setPreferredSize(new Dimension(DATA_WIDTH / PANELS_IN_ROW, FONT_HT));
    // JLabel left = new JLabel(label);
    // Font plainLabel = new
    // Font(UIManager.getFont("Label.font").getFontName(),
    // Font.PLAIN,UIManager.getFont("Label.font").getSize());
    // left.setFont(plainLabel);
    // p.add(left);
    // JLabel right = new JLabel(value);
    // right.setFont(plainLabel);
    // p.add(right);
    p.add(new JLabel(label), "left");
    p.add(new JLabel(value), "right");

    // Create the border for each grid cell
    int cpad = 1;
    Border cellBorder = BorderFactory.createLineBorder(Color.BLACK, cpad);
    p.setBorder(cellBorder);

    return p;
  }


  /**
   * Set the hero display content panels inside a viewable scroll pane
   * 
   * @return HeroDisplay in a (vertical) scrollable pane
   */
  private JScrollPane makeScrollable()
  {
    // Reset the panel size based on the size of the constituent components
    int finalHt = getPreferredSize().height;
    // setPreferredSize(new Dimension(DATA_WIDTH, finalHt));
    setPreferredSize(new Dimension(_panelWidth - 2 * (SCROLLBAR_SIZE),
        finalHt));

    /*
     * Put the panel in the viewport, and put the viewport in the scrollpane If everything sizes
     * correctly, the horizontal scrollbar will not be needed but it set to show the mis-sizing that
     * might occur
     */
    JScrollPane sp = new JScrollPane(this,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    sp.setViewportView(this);
    // Resize the view to include the data width, borders, and vertical
    // scrollbar
    // sp.setPreferredSize(new Dimension(PANEL_WIDTH, finalHt));

    // Reset the client to the top of the view (instead of the bottom)
    // sp.getVerticalScrollBar().setValue(sp.getVerticalScrollBar().getMinimum());

    return sp;
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


  /**
   * Save the Person currently being displayed into a new file.
   * 
   * @return true if the Person was created and saved successfully, else false
   */
  private boolean savePerson()
  {
    // Try to save without overwriting
    boolean noOverwrite = false;
    boolean overwrite = true;
    _saveMsg = this.SAVE_ERROR_MSG;

    // Check if person exists
    if (_hdCiv.savePerson(noOverwrite)) {
      return true;
    }
    // If person exists, prompt for overwrite/(rename?)
    else {
      // Custom button text
      Object[] options = {"Yes", "No", "Rename"};
      int n = JOptionPane.showOptionDialog(this,
          "Hero already exists. Do you want to overwrite?",
          "Overwrite prompt", JOptionPane.YES_NO_CANCEL_OPTION,
          JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

      if (n == JOptionPane.YES_OPTION) {
        return _hdCiv.savePerson(overwrite);
      } else if (n == JOptionPane.CANCEL_OPTION) {
        return renamePerson();
      } else {
        // change save message to avoid throwing error message
        _saveMsg = this.SAVE_CANCEL_MSG;
        return false;
      }
    }
  }


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
   * Displays the Person attributes in the shuttle, including inventory and skill set
   * 
   * @param ws shuttle of PersonKeys containing only String data
   */
  private boolean renamePerson()
  {
    // Pop up window for rename
    String newName = JOptionPane.showInputDialog(this,
        "Please input new name: ");

    // Call hdCiv.renamePerson()
    if (_hdCiv.renamePerson(newName)) {
      // Now update the display
      _ds.put(PersonKeys.NAME, newName);
    }

    this.displayAttributes(_ds);

    return this.savePerson();
  }


  /**
   * Creates the panel and container fields for the given Hero, using a data map from the model to
   * populate the widget.
   */
  private boolean setupDisplay()
  {
    // GENERAL SETUP
    // Set the preferred and max size
    _panelWidth = PANEL_WIDTH;
    _panelHeight = Mainframe.getWindowSize().height;
    setPreferredSize(new Dimension(_panelWidth, _panelHeight));
    _backColor = Color.GRAY;
    setBackground(_backColor);

    // Set Panel layout to special MiGLayout
    setLayout(new MigLayout("fill", "[center]"));
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

