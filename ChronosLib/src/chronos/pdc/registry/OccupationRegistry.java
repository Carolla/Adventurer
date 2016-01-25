/**
 * OccupationRegistry.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.registry;

import java.util.List;

import mylib.ApplicationException;
import mylib.pdc.Registry;
import chronos.Chronos;
import chronos.pdc.Occupation;
import chronos.pdc.Skill;

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
  /** Quick reference to the SkillRegistry to avoid repeated calls */
  private SkillRegistry _skillRegistry;

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
  switch (_occupation)
  {
    case ("Academic"): {
      ocpDesc = "Knows diverse information, court politics and bureaucrats.";
      if (INT > 14) {
        skills.add(extractSkillSet("General Knowledge"));
        skills.add(extractSkillSet("Concentration"));
        if (CHR > 14) {
          skills.add(extractSkillSet("Diplomacy"));
        }
      }
      break;
    }
    case ("Acrobat"): {
      ocpDesc = "Acrobatic and aerial body control.";
      if (DEX > 14) {
        skills.add(extractSkillSet("Climb Walls"));
        skills.add(extractSkillSet("Balance"));
        skills.add(extractSkillSet("Escape Artist"));
        skills.add(extractSkillSet("Jump"));
        skills.add(extractSkillSet("Tumble"));
      }
      break;
    }
    case ("Alchemist"): {
      ocpDesc = "Knows chemicals and elixirs. Owns Alchemists' Kit.";
      // _inventory.add(kits[KitNdx.ALCHEMIST.ordinal()]);
      if (INT > 14) {
        skills.add(extractSkillSet("Arcane Knowledge"));
      }
      break;
    }
    case ("Apothecary"): {
      ocpDesc = "Knows herbs, ointments, and medicines. Owns Alchemists' Kit.";
      // _inventory.add(kits[KitNdx.ALCHEMIST.ordinal()]);
      if (WIS > 14) {
        skills.add(extractSkillSet("Natural Knowledge"));
      }
      break;
    }
    case ("Armorer"): {
      ocpDesc = "Makes and repairs metal armor, helmets and shields. Owns Metalsmith Kit.";
      // _inventory.add(kits[KitNdx.METAL.ordinal()]);
      skills.add(extractSkillSet("Repair Armor"));
      break;
    }
    case ("Banker"): {
      ocpDesc = "You were a financial businessman.";
      skills.add(extractSkillSet("Financial Brokering"));
      if (INT > 15) {
        skills.add(extractSkillSet("Appraise Jewelry"));
      }
      break;
    }
    case ("Bowyer"): {
      ocpDesc = "Can make bows and arrows. Owns Woodworking Kit.";
      // _inventory.add(kits[KitNdx.WOOD.ordinal()]);
      skills.add(extractSkillSet("Bowmaking"));
      break;
    }
    case ("Carpenter"): {
      ocpDesc = "Knows wood and woodworking tools. Owns Woodworking Kit.";
      // _inventory.add(kits[KitNdx.WOOD.ordinal()]);
      skills.add(extractSkillSet("Find Secrets in Woodwork"));
      break;
    }
    case ("Farmer"): {
      ocpDesc = "Knows plants, common herbs, greenery.";
      skills.add(extractSkillSet("Identify Plants"));
      skills.add(extractSkillSet("Predict Weather"));
      break;
    }
    case ("Fisher"): {
      ocpDesc = "Knows about bodies of fresh water and lakes. Owns Sewing Kit.";
      // _inventory.add(kits[KitNdx.SEWING.ordinal()]);
      skills.add(extractSkillSet("Netmaking"));
      if (STR > 14) {
        skills.add(extractSkillSet("Fast Swim"));
      }
      break;
    }
    case ("Forester"): {
      ocpDesc = "Has natural knowledge in wooded areas.";
      skills.add(extractSkillSet("Hide in Shadows"));
      skills.add(extractSkillSet("Move Silently"));
      skills.add(extractSkillSet("Wilderness Lore"));
      skills.add(extractSkillSet("Intuit Outdoor Direction"));
      skills.add(extractSkillSet("Spot Details"));
      if (STR > 14) {
        skills.add(extractSkillSet("Fast Swim"));
      }
      break;
    }
    case ("Freighter"): {
      ocpDesc = "Businessman. Ships cargo in wagons. Owns Woodworking Kit.";
      // _inventory.add(kits[KitNdx.WOOD.ordinal()]);
      skills.add(extractSkillSet("Negotiations"));
      skills.add(extractSkillSet("Cargo Transport"));
      if (WIS > 14) {
        skills.add(extractSkillSet("Train Animals"));
      }
      break;
    }
    case ("Gambler"): {
      ocpDesc = "Skilled in games of chance.";
      skills.add(extractSkillSet("Luck"));
      skills.add(extractSkillSet("Pick Pockets"));
      skills.add(extractSkillSet("Open Locks"));
      skills.add(extractSkillSet("Bluff"));
      skills.add(extractSkillSet("Sense Motive"));
      break;
    }
    case ("Hunter"): {
      ocpDesc = "Tracks and kills wild animals for food";
      skills.add(extractSkillSet("Hunting"));
      skills.add(extractSkillSet("Find/Set Traps"));
      skills.add(extractSkillSet("Move Silently"));
      skills.add(extractSkillSet("Hide in Shadows"));
      skills.add(extractSkillSet("Spot Details"));
      if (CHR > 14) {
        skills.add(extractSkillSet("Intimidate"));
      }
      if (CON > 14) {
        skills.add(extractSkillSet("Listening"));
      }
      break;
    }
    case ("Husbandman"):
      ocpDesc = "Knows livestock of all kinds (horses, sheep, cattle, pigs)";
      skills.add(extractSkillSet("Husbandry"));
      if (WIS > 14) {
        skills.add(extractSkillSet("Animal Empathy"));
        skills.add(extractSkillSet("Train Animals"));
      }
      break;
    case ("Innkeeper"): {
      ocpDesc = "Businessman. Runs crowded places, people-oriented, business-savvy";
      skills.add(extractSkillSet("Negotiations"));
      skills.add(extractSkillSet("Sense Motive"));
      if (CHR > 14) {
        skills.add(extractSkillSet("Gather Information"));
      }
      if (INT > 14) {
        skills.add(extractSkillSet("Read Lips"));
      }
      break;
    }
    case ("Jeweler"): {
      ocpDesc = "Recognizes true value of gems, jewelry, etc. " +
          "Works intricate devices like a watchmaker.";
      skills.add(extractSkillSet("Appraise Jewelry"));
      if (DEX > 14) {
        skills.add(extractSkillSet("Open Locks"));
      }
      break;
    }
    case ("Leatherworker"): {
      ocpDesc = "Tans hides and makes leather items. Owns Leatherworking Kit";
      // _inventory.add(kits[KitNdx.LEATHER.ordinal()]);
      skills.add(extractSkillSet("Leatherworking"));
      break;
    }
    case ("Painter"): {
      ocpDesc = "Paints buildings and mixes paint.";
      skills.add(extractSkillSet("Painting"));
      if (CHR > 14) {
        skills.add(extractSkillSet("Gather Information"));
      }
      break;
    }
    case ("Mason"): {
      ocpDesc = "Constructs buildings, works mortar, lays brick; knows stonework.";
      if (INT > 14) {
        skills.add(extractSkillSet("Find Secrets in Stonework"));
      }
      break;
    }
    case ("Miner"): {
      ocpDesc = "Digs ores from caverns and mines. Knows kinds of rock and ores";
      skills.add(extractSkillSet("Intuit Underground Direction"));
      skills.add(extractSkillSet("Cavern Lore"));
      if (INT > 14) {
        skills.add(extractSkillSet("Find Secrets in Stonework"));
      }
      break;
    }
    case ("Navigator"): {
      ocpDesc = "Knows direction at sea, plots water course without getting lost";
      skills.add(extractSkillSet("Predict Weather"));
      skills.add(extractSkillSet("Water Lore"));
      skills.add(extractSkillSet("Intuit Outdoor Direction"));
      skills.add(extractSkillSet("Spot Details"));
      if (STR > 14) {
        skills.add(extractSkillSet("Fast Swim"));
      }
      break;
    }
    case ("Sailor"): {
      ocpDesc = "Knows ships, has knowledge of bodies of water.";
      skills.add(extractSkillSet("Make Raft"));
      if (STR > 14) {
        skills.add(extractSkillSet("Fast Swim"));
      }
      break;
    }
    case ("Shipwright"): {
      ocpDesc = "Builds ships, knows wood and wood-working tools.";
      skills.add(extractSkillSet("Make Raft"));
      if (STR > 14) {
        skills.add(extractSkillSet("Fast Swim"));
      }
      break;
    }
    case ("Tailor"): {
      ocpDesc = "Makes clothing, knows dyes. Owns Sewing Kit";
      // _inventory.add(kits[KitNdx.SEWING.ordinal()]);
      skills.add(extractSkillSet("Sewing"));
      if (CHR > 14) {
        skills.add(extractSkillSet("Gather Information"));
      }
      break;
    }
    case ("Trader"): {
      ocpDesc = "Businessman. Familar with transport equipment.";
      skills.add(extractSkillSet("Financial Brokering"));
      skills.add(extractSkillSet("Sense Motive"));
      if (CHR > 14) {
        skills.add(extractSkillSet("Diplomacy"));
      }
      break;
    }
    case ("Trapper"): {
      ocpDesc = "Catches animals for tanning or money.";
      skills.add(extractSkillSet("Trapping"));
      skills.add(extractSkillSet("Find/Set Traps"));
      skills.add(extractSkillSet("Move Silently"));
      skills.add(extractSkillSet("Open Locks"));
      skills.add(extractSkillSet("Hide in Shadows"));
      skills.add(extractSkillSet("Spot Details"));
      skills.add(extractSkillSet("Wilderness Lore"));
      if ((DEX > 14) && (INT > 14)) {
        skills.add(extractSkillSet("Disable Device Skill"));
      }
      break;
    }
    case ("Weaponsmith"): {
      ocpDesc = "Knows metal weapons of all types and metalworking. Owns Metalsmith Kit.";
      // _inventory.add(kits[KitNdx.METAL.ordinal()]);
      skills.add(extractSkillSet("Make Weapons"));
      break;
    }
    case ("Weaver"): {
      ocpDesc = "Makes tapestries, rugs, bed clothing. Knows dyes. Owns Sewing Kit";
      // _inventory.add(kits[KitNdx.SEWING.ordinal()]);
      skills.add(extractSkillSet("Appraise Tapestries"));
      break;
    }
    case ("Woodworker"): {
      ocpDesc = "Builds wood furniture, cabinets. Knows wood and wood-working tools. " +
          "Owns Woodworking Kit.";
      // _inventory.add(kits[KitNdx.WOOD.ordinal()]);
      skills.add(extractSkillSet("Woodworking"));
      skills.add(extractSkillSet("Find Secrets in Woodwork"));
      if ((DEX > 14) && (INT > 14)) {
        skills.add(extractSkillSet("Disable Device Skill"));
      }
      break;
    }
    case ("Drifter"): {
      ocpDesc = "Everyone is running from something. What's your story?";
      skills.add("No special skills");
      break;
    }
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
    _skillRegistry = skillRegistry;
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
        Occupation occup =
            new Occupation(_occupTable[k][0],
                _skillRegistry.getSkill(_occupTable[k][1]));
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
    return (Occupation) get(ocpName);
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

