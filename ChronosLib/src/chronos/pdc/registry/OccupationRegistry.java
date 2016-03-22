/**
 * OccupationRegistry.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.registry;

import java.util.Arrays;
import java.util.List;

import mylib.ApplicationException;
import mylib.pdc.Registry;
import chronos.pdc.Chronos;
import chronos.pdc.Occupation;

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
public class OccupationRegistry extends Registry<Occupation>
{
//  /**
//   * Use this table to init the OccupTable with the occupation and skill names. Put occupations
//   * into alphabetical order, which is how the names will be displayed. Each skill must be in the
//   * SkillRegistry. See the SkillRegistry for a definition of what each of the Skills does. 28
//   * Occupations in table
//   */
//  static private final String[][] _occupTzable = {
//      {"None", "No Occupational Skills"},
//      // Reduce damage d3 if falling off walls; also can pass enemy spaces
//      // +2 AC
//      {"Acrobat", "Tumbling"},
//      // Increased chance of recognition (+1 INT) to identify substance or
//      // thing
//      {"Alchemist", "Arcane Knowledge"},
//      // Increased chance of recognition (+1 WIS) to identify substance or
//      // potion
//      {"Apothecary", "Natural Knowledge"},
//      // Repair armor to -1 original AC; make small wooden or metal
//      // shields
//      {"Armorer", "Repair Armor"},
//      // Gets 10% discounts on financial transactions, and no-fee banking
//      {"Banker", "Brokering"},
//      // Make short bow (-1 to hit) and arrows (-1 damage adj)
//      {"Bowyer", "Bowmaking"},
//      // +10% chance to find secret doors in wall paneling, cabinets, etc.
//      {"Carpenter", "Find Secret Doors in Wood"},
//      // +2 CHR factor when negotiating
//      {"Courtesan", "Charm Person"},
//      // Predict next day weather at +2 WIS
//      {"Farmer", "Predict Weather"},
//      // With 50' rope, can make 50' x 50' net, can catch fish or NPCs
//      {"Fisher", "Netmaking"},
//      // +1 WIS to know compass directions when outside
//      {"Forester", "Intuit Direction"},
//      // +1 CHR when negotiating money deals
//      {"Freighter", "Negotations"},
//      // +1 on all Saving throws involving risk-taking; +2 on all game
//      // rolls
//      {"Gambler", "Luck"},
//      // 20% increased chance of finding and catching wild game
//      {"Hunter", "Hunting"},
//      // +1 WIS to determine if Person is lying or bluffing
//      {"Innkeeper", "Sense Motive"},
//      // Can know base selling value of jewelry and gems
//      {"Jeweler", "Appraise"},
//      // Can make leather armor, gloves or boots
//      {"Leatherworker", "Leatherworking"},
//      // +10 chance to find secret doors in stone work, walls, fireplaces,
//      // floors
//      {"Mason", "Find Secret Openings in Stonework"},
//      // +1 WIS to know direction underground
//      {"Miner", "Intuit Underground Direction"},
//      // +1 movement (normal = half Movement) when swimming over or
//      // underwater
//      {"Sailor", "Fast Swim"},
//      // With hand axe and wood, can make sailing raft
//      {"Shipwright", "Make Raft"},
//      // Can make clothing, belt, boots, cloak, hat, etc.
//      {"Tailor", "Sewing"},
//      // Gets 10% discounts on financial transactions, and no-fee banking
//      {"Trader", "Brokering"},
//      // Set, find, or remove simple mechanical traps as if L1 Rogue
//      {"Trapper", "Trapping"},
//      // Make or repair small specific weapons (-1 normal)
//      {"Weaponsmith", "Make Weapons"},
//      // Can know base selling value of tapestries
//      {"Weaver", "Appraise Tapestries"},
//      // Repair or modify wooden items, e.g. repair xbows, add secret
//      // compartment to chest
//      {"Woodworker", "Woodworking"}};

  // Get all conditional skills for each occupation
  static private final String[][] _occupTable = {
    {"Academic", "Knows diverse information, court politics and bureaucrats.", "INT+1", "General Knowledge", "Concentration", "Diplomacy"},
    {"Acrobat", "Acrobatic and aerial body control.", "DEX=16", "Climb Walls", "Balance","Escape Artist","Jump","Tumble"},
    {"Alchemist", "Knows chemicals and elixirs. Owns Alchemists' Kit.", "INT+1", "Arcane Knowledge"},
    {"Apothecary", "Knows herbs, ointments, and medicines. Owns Alchemists' Kit.", "WIS+1", "Natural Knowledge"},
    {"Armorer", "Makes and repairs metal armor, helmets and shields. Owns Metalsmith Kit.", "STR+1", "Repair Armor"}, 
    {"Banker", "You were a financial businessman.", "INT+1", "Financial Brokering", "Appraise Jewelry"},
    {"Bowyer", "Can make bows and arrows. Owns Woodworking Kit.", "DEX+1","Bowmaking"},
    {"Carpenter", "Knows wood and woodworking tools. Owns Woodworking Kit.", "STR+1", "Find Secrets in Woodwork"},
    {"Farmer", "Knows plants, common herbs, greenery.", "WIS+1","Identify Plants", "Predict Weather"},
    {"Fisher", "Knows about bodies of fresh water and lakes. Owns Sewing Kit.", "STR+1", "Netmaking", "Fast Swim"},
    {"Forester", "Has natural knowledge in wooded areas.", "STR=15;DEX=16", "Hide in Shadows","Move Silently","Wilderness Lore","Intuit Outdoor Direction","Spot Details"},
    {"Freighter", "Businessman. Ships cargo in wagons. Owns Woodworking Kit.", "WIS+1", "Negotiations","Cargo Transport","Train Animals"},
    {"Gambler", "Skilled in games of chance.", "DEX=16;CHR=16", "Luck", "Pick Pockets", "Open Locks", "Bluff", "Sense Motive"},
    {"Hunter", "Tracks and kills wild animals for food", "DEX=15;CON=16","Hunting", "Find/Set Traps","Move Silently","Hide in Shadows","Spot Details","Intimidate","Listening"},
    {"Husbandman", "Knows livestock of all kinds (horses, sheep, cattle, pigs)", "WIS+1", "Husbandry","Animal Empathy","Train Animals"},
    {"Innkeeper", "Businessman. Runs crowded places, people-oriented, business-savvy", "CHR=16", "Negotiations", "Sense Motive", "Gather Information", "Read Lips"},
    {"Jeweler", "Recognizes true value of gems, jewelry, etc. Works intricate devices like a watchmaker.", "DEX+1", "Appraise Jewelry", "Open Locks"},
    {"Leatherworker", "Tans hides and makes leather items. Owns Leatherworking Kit", "CON+1", "Leatherworking"},
    {"Painter", "Paints buildings and mixes paint.", "DEX+1;CHR+1", "Painting", "Gather Information"},
    {"Mason", "Constructs buildings, works mortar, lays brick; knows stonework.", "STR+1;WIS+1", "Find Secrets in Stonework"},
    {"Miner", "Digs ores from caverns and mines. Knows kinds of rock and ores", "STR+1", "Intuit Underground Direction", "Cavern Lore", "Find Secrets in Stonework"},
    {"Navigator", "Knows direction at sea, plots water course without getting lost", "WIS+1", "Predict Weather", "Water Lore", "Intuit Outdoor Direction", "Spot Details", "Fast Swim"},
    {"Sailor", "Knows ships, has knowledge of bodies of water.", "STR+1;WIS+1", "Make Raft", "Fast Swim"},
    {"Shipwright", "Builds ships, knows wood and wood-working tools.", "STR+1;CON+1", "Make Raft", "Fast Swim"},
    {"Tailor", "Makes clothing, knows dyes. Owns Sewing Kit", "CHR+1;DEX+1", "Sewing","Gather Information"},
    {"Trader", "Businessman. Familar with transport equipment.", "CHR+1", "Financial Brokering", "Sense Motive", "Diplomacy"},
    {"Trapper", "Catches animals for tanning or money.", "DEX=17", "Trapping", "Find/Set Traps", "Move Silently", "Open Locks", "Hide in Shadows", "Spot Details", "Wilderness Lore", "Disable Device Skill"},
    {"Weaponsmith", "Knows metal weapons of all types and metalworking. Owns Metalsmith Kit.", "STR+1", "Make Weapons"},
    {"Weaver", "Makes tapestries, rugs, bed clothing. Knows dyes. Owns Sewing Kit", "DEX+1;WIS+1", "Appraise Tapestries"},
    {"Woodworker", "Builds wood furniture, cabinets. Knows wood and wood-working tools. Owns Woodworking Kit.", "STR+1;INT+1", "Woodworking","Find Secrets in Woodwork", "Disable Device Skill"},
    {"Drifter", "Everyone is running from something. What's your story?", "WIS+3;CHR+2","No special skills"}};
    
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
  protected OccupationRegistry(SkillRegistry skillRegistry)
  {
    super(Chronos.OcpRegPath);
    if (_shouldInitialize) {
      initialize();
    }
  }


  /**
   * Creates the Occupation Registry with the static tables given, converting each element to an
   * Occupation object and saving it in the database. Each Occupation has a Skill that must exist
   * in the Skill registry, so this also is checked.
   */
  @Override
  public void initialize()
  {
    // Create new Occupations and save to registry
    try {
      for (int k = 0; k < _occupTable.length; k++) {
        String occupationName = _occupTable[k][0];
        String occupationDesc = _occupTable[k][1];
        String occupationTrait = _occupTable[k][2];
        List<String> occupationSkills = Arrays.asList(Arrays.copyOfRange(_occupTable[k], 4, _occupTable[k].length));
        Occupation occup = new Occupation(occupationName, occupationDesc, occupationTrait, occupationSkills);
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
   * Converts the name into a searachable Occupation, and queries the db
   * 
   * @param ocpName name of the Occupation to retrieve
   * @return the Occupation object; or null if not found
   */
  public Occupation getOccupation(String ocpName)
  {
    return get(ocpName);
  }


  /**
   * Retrieve all Occupations in the registry
   * 
   * @return the Occupation List
   */
  public List<Occupation> getOccupationList()
  {
    return getAll();
  }

} // end of OccupationRegistry class

