/**
 * OccupationRegistry.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.registry;

import java.util.ArrayList;
import java.util.List;

import mylib.ApplicationException;
import mylib.dmc.IRegistryElement;
import mylib.pdc.Registry;
import chronos.Chronos;
import chronos.pdc.Occupation;
import chronos.pdc.Skill;
import chronos.pdc.registry.RegistryFactory.RegKey;

/**
 * Contains a set of occupations and associated Skills that a player may assign to his Hero. It also
 * contains a method to allow more occupations to be added. <code>OccupationRegistry</code> is a
 * read-only singleton and is only initialized once.
 * 
 * @author Alan Cline
 * @version Jan 1 2010 // original <br>
 *          Jan 9 2010 // simplified by removing Occupation class and using a String[][] table
 *          directly. <br>
 *          Feb 5 2011 // TAA: converted to OccupRegistry <br>
 *          Apr 11 2011 // TAA renamed occReg to thisReg <br>
 *          May 14 2011 // TAA fixed some bugs <br>
 *          Jun 13 2011 // Redesigned to use load method from file <br>
 *          Nov 4 2011 // ABC Few minor bugs to stop recursive loading, and to use the Chronos
 *          library file for pathing <br>
 *          May 13 2012 // TAA updated initTables method </DL>
 */
public class OccupationRegistry extends Registry
{
  /** Quick reference to the RegistryFactory to avoid repeated calls */
  private RegistryFactory _rf;
  /** Quick reference to the SkillRegistry to avoid repeated calls */
  private SkillRegistry _skReg;

  
  /**
   * Use this table to init the OccupTable with the occupation and skill names. Put occupations into
   * alphabetical order, which is how the names will be displayed. Each skill must be in the
   * SkillRegistry. See the SkillRegistry for a definition of what each of the Skills does. 28
   * Occupations in table
   */
  static private final String[][] _occupTable = {
      {"None", "No Occupational Skills"},
      // Reduce damage d3 if falling off walls; also can pass enemy spaces
      // +2 AC
      {"Acrobat", "Tumbling"},
      // Increased chance of recognition (+1 INT) to identify substance or
      // thing
      {"Alchemist", "Arcane Knowledge"},
      // Increased chance of recognition (+1 WIS) to identify substance or
      // potion
      {"Apothecary", "Natural Knowledge"},
      // Repair armor to -1 original AC; make small wooden or metal
      // shields
      {"Armorer", "Repair Armor"},
      // Gets 10% discounts on financial transactions, and no-fee banking
      {"Banker", "Brokering"},
      // Make short bow (-1 to hit) and arrows (-1 damage adj)
      {"Bowyer", "Bowmaking"},
      // +10% chance to find secret doors in wall paneling, cabinets, etc.
      {"Carpenter", "Find Secret Doors in Wood"},
      // +2 CHR factor when negotiating
      {"Courtesan", "Charm Person"},
      // Predict next day weather at +2 WIS
      {"Farmer", "Predict Weather"},
      // With 50' rope, can make 50' x 50' net, can catch fish or NPCs
      {"Fisher", "Netmaking"},
      // +1 WIS to know compass directions when outside
      {"Forester", "Intuit Direction"},
      // +1 CHR when negotiating money deals
      {"Freighter", "Negotations"},
      // +1 on all Saving throws involving risk-taking; +2 on all game
      // rolls
      {"Gambler", "Luck"},
      // 20% increased chance of finding and catching wild game
      {"Hunter", "Hunting"},
      // +1 WIS to determine if Person is lying or bluffing
      {"Innkeeper", "Sense Motive"},
      // Can know base selling value of jewelry and gems
      {"Jeweler", "Appraise"},
      // Can make leather armor, gloves or boots
      {"Leatherworker", "Leatherworking"},
      // +10 chance to find secret doors in stone work, walls, fireplaces,
      // floors
      {"Mason", "Find Secret Openings in Stonework"},
      // +1 WIS to know direction underground
      {"Miner", "Intuit Underground Direction"},
      // +1 movement (normal = half Movement) when swimming over or
      // underwater
      {"Sailor", "Fast Swim"},
      // With hand axe and wood, can make sailing raft
      {"Shipwright", "Make Raft"},
      // Can make clothing, belt, boots, cloak, hat, etc.
      {"Tailor", "Sewing"},
      // Gets 10% discounts on financial transactions, and no-fee banking
      {"Trader", "Brokering"},
      // Set, find, or remove simple mechanical traps as if L1 Rogue
      {"Trapper", "Trapping"},
      // Make or repair small specific weapons (-1 normal)
      {"Weaponsmith", "Make Weapons"},
      // Can know base selling value of tapestries
      {"Weaver", "Appraise Tapestries"},
      // Repair or modify wooden items, e.g. repair xbows, add secret
      // compartment to chest
      {"Woodworker", "Woodworking"}};

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Private ctor because this singleton is called from getInstance().
   * 
   * Registry filename is used for database
   * 
   * @param init flag to initialize registry for default data if true
   */
  protected OccupationRegistry()
  {
    super(Chronos.OcpRegPath);
  }


  /**
   * Creates the Occupation Registry with the static tables given, converting each element to an
   * Occupation object and saving it in the database. Each Occupation has a Skill that must exist in
   * the Skill registry, so this also is checked.
   */
  @Override
  public void initialize()
  {
    _rf = RegistryFactory.getInstance();
    _rf.getRegistry(RegKey.SKILL);

    // Open Skill Registry for verification purposes
    _skReg = (SkillRegistry) _rf.getRegistry(RegKey.SKILL);

    // Create new Occupations and save to registry
    try {
      for (int k = 0; k < _occupTable.length; k++) {
        // Throw exception if Skill is not in Skill Registry
        Occupation occup = new Occupation(_occupTable[k][0], _occupTable[k][1]);
        super.add(occup); // super is used to highlight inheritance
      }
    } catch (ApplicationException ex) {
      System.err.println(ex.getMessage());
    }
  }


  /*
   * PUBLIC METHODS
   */

  /**
   * Add a new Occupation to the Registry
   * 
   * @param occup new Occupation to add. It must a a registered Skill and not null
   * @throws ApplicationException if the Skill does not exist in the Skill Registry, or Occupation
   *         is null
   */
  public void add(Occupation occup) throws ApplicationException
  {
    if (occup == null) {
      throw new ApplicationException("add(): Received illegal null Occupation");
    }
    
    // Create new Occupations and save to registry
    if (verifySkill(_skReg, occup.getSkillName()) == true) {
      super.add(occup); // super is used to highlight inheritance
    } else {
      throw (new ApplicationException("Skill not found in Skill Registry"));
    }
  }



  // /** Retrieves the Occupation with the requested unique name
  // *
  // * @param name name of the Skill to retrieve
  // * @return the Occupation; or null if not unique
  // * @throws ApplicationException if trying to retrieve non-unique object
  // */
  // @Override
  // public Occupation get(final String skillName) throws ApplicationException
  // {
  // ExtObjectContainer db = _regRW.getDB();
  // // Retrieve all skills that match the skillname; should be only one
  // List<Occupation> list = db.query(new Predicate<Occupation>() {
  // public boolean match(Occupation candidate) {
  // return candidate.getName().equals(skillName);
  // }
  // });
  // // Ensure uniqueness
  // if (list.size() == 1) {
  // return list.get(0);
  // }
  // else {
  // throw new ApplicationException(Registry.DBREG_NOT_UNIQUE);
  // }
  // }


  // /** Close db, destroy the dbReadWriter and set this registry to null
  // * @param eraseFile if true, erase registry file; else not
  // */
  // public void closeRegistry()
  // {
  // super.close();
  // // _thisReg = null;
  // }
  //
  //
  // public void deleteRegistry()
  // {
  // super.delete();
  // // _thisReg = null;
  // }


  /**
   * Converts the name into a searachable Occupation, and queries the db
   * 
   * @param ocpName name of the Occupation to retrieve
   * @return the Occupation object; or null if not found
   */
  public Occupation getOccupation(String ocpName)
  {
    try {
      return (Occupation) getUnique(ocpName);
    } catch (ApplicationException ex) {
      return null;
    }

  }


  /**
   * Retrieve all Occupations in the registry
   * 
   * @return the Occupation List
   */
  public List<Occupation> getOccupationList()
  {
    List<IRegistryElement> itemSet = super.getAll();
    List<Occupation> itemList = new ArrayList<Occupation>(itemSet.size());
    for (IRegistryElement e : itemSet) {
      itemList.add((Occupation) e);
    }
    return itemList;
  }

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /**
   * Verify that an Occuational skill exists in the Skill registry, or this Occupation is invalid.
   * To verify against the database, a Skill object is created with the matching skillname. This is
   * required because the registry matches by example.
   * 
   * @param skillReg db access where skills reside
   * @param skillName to verify
   * @param true is the skill exists in the registry, else false
   */
  private boolean verifySkill(SkillRegistry skillReg, String skillName)
  {
    boolean retval = false;
    if (skillReg != null) {
      Skill foundSkill = skillReg.getSkill(skillName);
      if (foundSkill == null) {
        retval = false;
      } else {
        retval = foundSkill.getName().equals(skillName);
      }
    }
    return retval;
  }

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ INNER CLASS: MockOccupationRegistry for
   * Testing ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /** Inner class for testing Person */
  public class MockOccupationRegistry
  {
    /** Default constructor */
    public MockOccupationRegistry()
    {}

    // /** Get the Registry object */
    // public OccupationRegistry getRegistry()
    // {
    // return (OccupationRegistry) _thisReg;
    // }

    /** Get number of skills for all tables */
    public int getNbrOccupations()
    {
      int totalOccupations = OccupationRegistry._occupTable.length;
      return totalOccupations;
    }

    /** Get the skill names and descriptions for comparing expected values */
    public String[][] getOccupationTable()
    {
      return _occupTable;
    }

  } // end of MockOccupationRegistry class

} // end of OccupationRegistry class

