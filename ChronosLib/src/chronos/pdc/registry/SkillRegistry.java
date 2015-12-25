/**
 * SkillRegistry.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.registry;

import java.util.ArrayList;
import java.util.List;

import chronos.Chronos;
import chronos.pdc.Skill;
import mylib.ApplicationException;
import mylib.MsgCtrl;
import mylib.dmc.IRegistryElement;
import mylib.pdc.Registry;

/**
 * Contains a set of skills that a player may assign to his Hero. {@code SkillRegistry} is a
 * read-only singleton and is only initialized once.
 * 
 * @author Alan Cline
 * @version Jan 1 2010 // original <br>
 */
public class SkillRegistry extends Registry<Skill>
{
  /** 8 Skills that are defined for particular races: Name, Description. */
  static private final String[][] _racialSkillTable = {
      {"Archery", "Extra skillful with a short or long bow."},
      {"Geasing", "Able to detect undeground direction and stone-based traps."},
      {"Infravision", "Can see warm bodies in the dark."},
      {"Move Silently", "Chance of sneaking about undetected."},
      {"Resistance to Charm", "Charm spells rarely effect you."},
      {"Resistance to Poison", "Most poisons only moderately effect you."},
      {"Resistance to Sleep", "Sleep spells rarely effect you."},
      {"Tingling", "Ability to find hidden doors, secret panels, or concealed openings."},};

  /** 27 Skills that are associated with particular occupations */
  static private final String[][] _occupSkillTable = {
      {"No Occupational Skills", "Nothing from your past gives you any special skill"},
      {"Appraise", "Can know base selling value of jewelry and gems"},
      {"Appraise Tapestries", "Can know base selling value of tapestries"},
      {"Arcane Knowledge", "+1 INT chance to identify substance or thing"},
      {"Bowmaking", "Make -1 short bow and arrows (-1 damage adj)"},
      {"Brokering", "Ten percent discount on transactions; free banking"},
      {"Charm Person", "+2 CHR factor when negotiating"},
      {"Fast Swim", "Increased speed when swimming over or underwater"},
      {"Find Secret Doors in Wood", "+10% chance to find secret doors in wood surfaces"},
      {"Find Secret Openings in Stonework", "+10% chance to find secrets in stone construction"},
      {"Hunting", "+20% chance of finding and catching wild game"},
      {"Intuit Direction", "+1 WIS to know compass directions when outside"},
      {"Intuit Underground Direction", "+1 WIS to know direction underground"},
      {"Leatherworking", "Can make leather armor, gloves or boots"},
      {"Luck", "+2 on all Saving throws involving risk-taking"},
      {"Make Raft", "With hand axe and wood, can make sailing raft"},
      {"Make Weapons", "Make or repair small specific weapons (-1 normal)"},
      {"Natural Knowledge", "+1 WIS chance to identify substance or potion"},
      {"Negotations", "+1 CHR when negotiating money deals"},
      {"Netmaking", "Can make 50' x 50' net to catch fish or NPCs"},
      {"Predict Weather", "Predict next day weather at +2 WIS"},
      {"Repair Armor", "Repair armor to -1 original AC"},
      {"Sense Motive", "+1 WIS to determine if Person is lying or bluffing"},
      {"Sewing", "Can make clothing, belt, boots, cloak, or hat"},
      {"Trapping", "Set, find, or remove simple mechanical traps"},
      {"Tumbling", "Reduce damage up to 6 HP if falling off walls"},
      {"Woodworking", "Repair wooden items, e.g. xbows"}};


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Private ctor because this singleton is called from getInstance(). Registry filename is used for
   * database
   * 
   * @param init flag to initialize registry for default data if true
   */
  protected SkillRegistry()
  {
    super(Chronos.SkillRegPath);
    if (_shouldInitialize) {
      initialize();
    }
  }


  /**
   * PUBLIC METHODS
   */


  /**
   * Create the Skill Registry with the tables given, converting each element to a Skill object and
   * saving it in the database.
   */
  @Override
  protected void initialize()
  {
    // Create new Skills and save to registry
    try {
      for (int k = 0; k < _racialSkillTable.length; k++) {
        Skill skill = new Skill(_racialSkillTable[k][0], _racialSkillTable[k][1]);
        super.add(skill); // super is used to highlight inheritance
      }
      System.out.println("\n");
      for (int k = 0; k < _occupSkillTable.length; k++) {
        Skill skill = new Skill(_occupSkillTable[k][0], _occupSkillTable[k][1]);
        super.add(skill); // super is used to highlight inheritance
      }
    } catch (ApplicationException ex) {
      MsgCtrl.errMsgln(this, ex.getMessage());
    }
  }


  /**
   * Get a particlar Skill by name
   * 
   * @param name of the Item
   * @return the Item
   */
  public Skill getSkill(String name)
  {
    return (Skill) get(name);
  }


  /**
   * Retrieve all Skills in the SkillRegistry
   * 
   * @return the skillList
   */
  @SuppressWarnings("serial")
  public List<Skill> getSkillList()
  {
    List<IRegistryElement> skillSet = getAll();
    // Convert to Skill list
    List<Skill> skillList = new ArrayList<Skill>(skillSet.size());
    for (IRegistryElement elem : skillSet) {
      skillList.add((Skill) elem);
    }
    return skillList;
  }


  /*
   * INNER CLASS: MockSkillRegistry for Testing
   */

  /** Inner class for testing Person */
  public class MockSkillRegistry
  {
    /** Default constructor */
    public MockSkillRegistry()
    {}


    /** Diagnostic to dump all skills in the Registry */
    public void dump()
    {
      // Get all skills by using null argument
      List<Skill> sklist = getSkillList();
      for (Skill s : sklist) {
        System.out.println("\t" + s.getName() + "\t\t" + s.getDescription());
      }
    }


    /** Total number of objects expected to be stored in the db */
    public int getExpectedNbrElements()
    {
      return _occupSkillTable.length + _racialSkillTable.length;
    }

    /** Return the path for the registry file */
    public String getPath()
    {
      return Chronos.SkillRegPath;
    }

    /** Return the path for the registry file */
    public void setPath(String testPath)
    {
      Chronos.SkillRegPath = testPath;
    }

    // /** Get the Registry object */
    // public SkillRegistry getRegistry()
    // {
    // return (SkillRegistry) _thisReg;
    // }

  } // end of MockSkillRegistry class


} // end of SkillRegistry class

