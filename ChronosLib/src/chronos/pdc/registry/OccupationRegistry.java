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

import chronos.pdc.Chronos;
import chronos.pdc.Occupation;
import mylib.pdc.Registry;

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
 *          May 13 2012 // TAA updated initTables method <br>
 *          July 21, 2017 // ABC removed trait (or action) from ctor because actions are for Skills,
 *          not Occupations <br>
 */
public class OccupationRegistry extends Registry<Occupation>
{
  // @SuppressWarnings("serial")
  // private static final List<Occupation> _occupTable = new ArrayList<Occupation>() {{
  // add(new Occupation("Academic", "Knows diverse information, court politics and bureaucrats.",
  // "INT+1",
  // new ArrayList<String>() {{ add("General Knowledge"); add("Concentration"); add("Diplomacy");
  // }}));
  // add(new Occupation("Acrobat", "Acrobatic and aerial body control.", "DEX=16",
  // new ArrayList<String>() {{ add("Climb Walls"); add("Balance"); add("Escape Artist");
  // add("Jump"); add("Tumble");}}));
  // add(new Occupation("Alchemist", "Knows chemicals and elixirs. Owns Alchemists' Kit.", "INT+1",
  // new ArrayList<String>() {{ add("Arcane Knowledge");}}));
  // add(new Occupation("Apothecary", "Knows herbs, ointments, and medicines. Owns Alchemists'
  // Kit.", "WIS+1",
  // new ArrayList<String>() {{ add("Natural Knowledge");}}));
  // add(new Occupation("Armorer", "Makes and repairs metal armor, helmets and shields. Owns
  // Metalsmith Kit.", "STR+1",
  // new ArrayList<String>() {{ add("Repair Armor");}}));
  // add(new Occupation("Banker", "You were a financial businessman.", "INT+1",
  // new ArrayList<String>() {{ add("Financial Brokering"); add("Appraise Jewelry");}}));
  // add(new Occupation("Bowyer", "Can make bows and arrows. Owns Woodworking Kit.", "DEX+1",
  // new ArrayList<String>() {{ add("Bowmaking");}}));
  // add(new Occupation("Carpenter", "Knows wood and woodworking tools. Owns Woodworking Kit.",
  // "STR+1",
  // new ArrayList<String>() {{ add("Find Secrets in Woodwork");}}));
  // add(new Occupation("Farmer", "Knows plants, common herbs, greenery.", "WIS+1",
  // new ArrayList<String>() {{ add("Identify Plants"); add("Predict Weather");}}));
  // add(new Occupation("Fisher", "Knows about bodies of fresh water and lakes. Owns Sewing Kit.",
  // "STR+1",
  // new ArrayList<String>() {{ add( "Netmaking"); add("Fast Swim");}}));
  // add(new Occupation("Forester", "Has natural knowledge in wooded areas.", "STR=15;DEX=16",
  // new ArrayList<String>() {{ add("Hide in Shadows"); add("Move Silently"); add("Wilderness
  // Lore"); add("Intuit Outdoor Direction"); add("Spot Details");}}));
  // add(new Occupation("Freighter", "Businessman. Ships cargo in wagons. Owns Woodworking Kit.",
  // "WIS+1",
  // new ArrayList<String>() {{ add("Negotiations"); add("Cargo Transport"); add("Train
  // Animals");}}));
  // add(new Occupation("Gambler", "Skilled in games of chance.", "DEX=16;CHR=16",
  // new ArrayList<String>() {{ add("Luck"); add("Pick Pockets"); add("Open Locks"); add("Bluff");
  // add("Sense Motive");}}));
  // add(new Occupation("Hunter", "Tracks and kills wild animals for food", "DEX=15;CON=16",
  // new ArrayList<String>() {{ add("Hunting"); add("Find/Set Traps"); add("Move Silently");
  // add("Hide in Shadows"); add("Spot Details"); add("Intimidate"); add("Listening");}}));
  // add(new Occupation("Husbandman", "Knows livestock of all kinds (horses, sheep, cattle, pigs)",
  // "WIS+1",
  // new ArrayList<String>() {{ add( "Husbandry"); add("Animal Empathy"); add("Train Animals");}}));
  // add(new Occupation("Innkeeper", "Businessman. Runs crowded places, people-oriented,
  // business-savvy", "CHR=16",
  // new ArrayList<String>() {{ add( "Negotiations"); add("Sense Motive"); add("Gather
  // Information"); add("Read Lips");}}));
  // add(new Occupation("Jeweler", "Recognizes true value of gems, jewelry, etc. Works intricate
  // devices like a watchmaker.", "DEX+1", new ArrayList<String>() {{ add("Appraise Jewelry");
  // add("Open Locks");}}));
  // add(new Occupation("Leatherworker", "Tans hides and makes leather items. Owns Leatherworking
  // Kit", "CON+1",
  // new ArrayList<String>() {{ add( "Leatherworking");}}));
  // add(new Occupation("Painter", "Paints buildings and mixes paint.", "DEX+1;CHR+1",
  // new ArrayList<String>() {{ add( "Painting"); add("Gather Information");}}));
  // add(new Occupation("Mason", "Constructs buildings, works mortar, lays brick; knows stonework.",
  // "STR+1;WIS+1",
  // new ArrayList<String>() {{ add( "Find Secrets in Stonework");}}));
  // add(new Occupation("Miner", "Digs ores from caverns and mines. Knows kinds of rock and ores",
  // "STR+1",
  // new ArrayList<String>() {{ add( "Intuit Underground Direction"); add("Cavern Lore"); add("Find
  // Secrets in Stonework");}}));
  // add(new Occupation("Navigator", "Knows direction at sea, plots water course without getting
  // lost", "WIS+1",
  // new ArrayList<String>() {{ add( "Predict Weather"); add("Water Lore"); add("Intuit Outdoor
  // Direction"); add("Spot Details"); add("Fast Swim");}}));
  // add(new Occupation("Sailor", "Knows ships, has knowledge of bodies of water.", "STR+1;WIS+1",
  // new ArrayList<String>() {{ add( "Make Raft"); add("Fast Swim");}}));
  // add(new Occupation("Shipwright", "Builds ships, knows wood and wood-working tools.",
  // "STR+1;CON+1",
  // new ArrayList<String>() {{ add( "Make Raft"); add("Fast Swim");}}));
  // add(new Occupation("Tailor", "Makes clothing, knows dyes. Owns Sewing Kit", "CHR+1;DEX+1",
  // new ArrayList<String>() {{ add( "Sewing"); add("Gather Information");}}));
  // add(new Occupation("Trader", "Businessman. Familar with transport equipment.", "CHR+1",
  // new ArrayList<String>() {{ add( "Financial Brokering"); add("Sense Motive");
  // add("Diplomacy");}}));
  // add(new Occupation("Trapper", "Catches animals for tanning or money.", "DEX=17",
  // new ArrayList<String>() {{ add( "Trapping"); add("Find/Set Traps"); add("Move Silently");
  // add("Open Locks"); add("Hide in Shadows"); add("Spot Details"); add("Wilderness Lore");
  // add("Disable Device Skill");}}));
  // add(new Occupation("Weaponsmith", "Knows metal weapons of all types and metalworking. Owns
  // Metalsmith Kit.", "STR+1",
  // new ArrayList<String>() {{ add( "Make Weapons");}}));
  // add(new Occupation("Weaver", "Makes tapestries, rugs, bed clothing. Knows dyes. Owns Sewing
  // Kit", "DEX+1;WIS+1",
  // new ArrayList<String>() {{ add( "Appraise Tapestries");}}));
  // add(new Occupation("Woodworker", "Builds wood furniture, cabinets. Knows wood and wood-working
  // tools. Owns Woodworking Kit.", "STR+1;INT+1",
  // new ArrayList<String>() {{ add( "Woodworking"); add("Find Secrets in Woodwork"); add("Disable
  // Device Skill");}}));
  // add(new Occupation("Drifter", "Everyone is running from something. What's your story?",
  // "WIS+3;CHR+2",
  // new ArrayList<String>() {{ add("No special skills");}}));
  // }};

  @SuppressWarnings("serial")
  private static final List<Occupation> _occupTable = new ArrayList<Occupation>() {
    {
      add(new Occupation("Academic", "Knows diverse information, court politics and bureaucrats.",
          new ArrayList<String>() {
            {
              add("General Knowledge");
              add("Concentration");
              add("Diplomacy");
            }
          }));
      add(new Occupation("Acrobat", "Acrobatic and aerial body control.",
          new ArrayList<String>() {
            {
              add("Climb Walls");
              add("Balance");
              add("Escape Artist");
              add("Jump");
              add("Tumble");
            }
          }));
      add(new Occupation("Alchemist", "Knows chemicals and elixirs. Owns Alchemists' Kit.",
          new ArrayList<String>() {
            {
              add("Arcane Knowledge");
            }
          }));
      add(new Occupation("Apothecary",
          "Knows herbs, ointments, and medicines. Owns Alchemists' Kit.",
          new ArrayList<String>() {
            {
              add("Natural Knowledge");
            }
          }));
      add(new Occupation("Armorer",
          "Makes and repairs metal armor, helmets and shields. Owns Metalsmith Kit.",
          new ArrayList<String>() {
            {
              add("Repair Armor");
            }
          }));
      add(new Occupation("Banker", "You were a financial businessman.",
          new ArrayList<String>() {
            {
              add("Financial Brokering");
              add("Appraise Jewelry");
            }
          }));
      add(new Occupation("Bowyer", "Can make bows and arrows. Owns Woodworking Kit.",
          new ArrayList<String>() {
            {
              add("Bowmaking");
            }
          }));
      add(new Occupation("Carpenter", "Knows wood and woodworking tools. Owns Woodworking Kit.",
          new ArrayList<String>() {
            {
              add("Find Secrets in Woodwork");
            }
          }));
      add(new Occupation("Farmer", "Knows plants, common herbs, greenery.",
          new ArrayList<String>() {
            {
              add("Identify Plants");
              add("Predict Weather");
            }
          }));
      add(new Occupation("Fisher", "Knows about bodies of fresh water and lakes. Owns Sewing Kit.",
          new ArrayList<String>() {
            {
              add("Netmaking");
              add("Fast Swim");
            }
          }));
      add(new Occupation("Forester", "Has natural knowledge in wooded areas.",
          new ArrayList<String>() {
            {
              add("Hide in Shadows");
              add("Move Silently");
              add("Wilderness Lore");
              add("Intuit Outdoor Direction");
              add("Spot Details");
            }
          }));
      add(new Occupation("Freighter", "Businessman. Ships cargo in wagons. Owns Woodworking Kit.",
          new ArrayList<String>() {
            {
              add("Negotiations");
              add("Cargo Transport");
              add("Train Animals");
            }
          }));
      add(new Occupation("Gambler", "Skilled in games of chance.",
          new ArrayList<String>() {
            {
              add("Luck");
              add("Pick Pockets");
              add("Open Locks");
              add("Bluff");
              add("Sense Motive");
            }
          }));
      add(new Occupation("Hunter", "Tracks and kills wild animals for food",
          new ArrayList<String>() {
            {
              add("Hunting");
              add("Find/Set Traps");
              add("Move Silently");
              add("Hide in Shadows");
              add("Spot Details");
              add("Intimidate");
              add("Listening");
            }
          }));
      add(new Occupation("Husbandman", "Knows livestock of all kinds (horses, sheep, cattle, pigs)",
          new ArrayList<String>() {
            {
              add("Husbandry");
              add("Animal Empathy");
              add("Train Animals");
            }
          }));
      add(new Occupation("Innkeeper",
          "Businessman. Runs crowded places, people-oriented, business-savvy",
          new ArrayList<String>() {
            {
              add("Negotiations");
              add("Sense Motive");
              add("Gather Information");
              add("Read Lips");
            }
          }));
      add(new Occupation("Jeweler",
          "Recognizes true value of gems, jewelry, etc. Works intricate devices like a watchmaker.",
          new ArrayList<String>() {
            {
              add("Appraise Jewelry");
              add("Open Locks");
            }
          }));
      add(new Occupation("Leatherworker",
          "Tans hides and makes leather items. Owns Leatherworking Kit",
          new ArrayList<String>() {
            {
              add("Leatherworking");
            }
          }));
      add(new Occupation("Painter", "Paints buildings and mixes paint.",
          new ArrayList<String>() {
            {
              add("Painting");
              add("Gather Information");
            }
          }));
      add(new Occupation("Mason",
          "Constructs buildings, works mortar, lays brick; knows stonework.",
          new ArrayList<String>() {
            {
              add("Find Secrets in Stonework");
            }
          }));
      add(new Occupation("Miner", "Digs ores from caverns and mines. Knows kinds of rock and ores",
          new ArrayList<String>() {
            {
              add("Intuit Underground Direction");
              add("Cavern Lore");
              add("Find Secrets in Stonework");
            }
          }));
      add(new Occupation("Navigator",
          "Knows direction at sea, plots water course without getting lost",
          new ArrayList<String>() {
            {
              add("Predict Weather");
              add("Water Lore");
              add("Intuit Outdoor Direction");
              add("Spot Details");
              add("Fast Swim");
            }
          }));
      add(new Occupation("Sailor", "Knows ships, has knowledge of bodies of water.",
          new ArrayList<String>() {
            {
              add("Make Raft");
              add("Fast Swim");
            }
          }));
      add(new Occupation("Shipwright", "Builds ships, knows wood and wood-working tools.",
          new ArrayList<String>() {
            {
              add("Make Raft");
              add("Fast Swim");
            }
          }));
      add(new Occupation("Tailor", "Makes clothing, knows dyes. Owns Sewing Kit",
          new ArrayList<String>() {
            {
              add("Sewing");
              add("Gather Information");
            }
          }));
      add(new Occupation("Trader", "Businessman. Familar with transport equipment.",
          new ArrayList<String>() {
            {
              add("Financial Brokering");
              add("Sense Motive");
              add("Diplomacy");
            }
          }));
      add(new Occupation("Trapper", "Catches animals for tanning or money.",
          new ArrayList<String>() {
            {
              add("Trapping");
              add("Find/Set Traps");
              add("Move Silently");
              add("Open Locks");
              add("Hide in Shadows");
              add("Spot Details");
              add("Wilderness Lore");
              add("Disable Device Skill");
            }
          }));
      add(new Occupation("Weaponsmith",
          "Knows metal weapons of all types and metalworking. Owns Metalsmith Kit.",
          new ArrayList<String>() {
            {
              add("Make Weapons");
            }
          }));
      add(new Occupation("Weaver",
          "Makes tapestries, rugs, bed clothing. Knows dyes. Owns Sewing Kit",
          new ArrayList<String>() {
            {
              add("Appraise Tapestries");
            }
          }));
      add(new Occupation("Woodworker",
          "Builds wood furniture, cabinets. Knows wood and wood-working tools. Owns Woodworking Kit.",
          new ArrayList<String>() {
            {
              add("Woodworking");
              add("Find Secrets in Woodwork");
              add("Disable Device Skill");
            }
          }));
      add(new Occupation("Drifter", "Everyone is running from something. What's your story?",
          new ArrayList<String>() {
            {
              add("No special skills");
            }
          }));
    }
  };


  // ============================================================================
  // * CONSTRUCTOR(S) AND RELATED METHODS
  // ============================================================================

  /**
   * Private ctor because this singleton is called from getInstance().
   * 
   * Registry filename is used for database
   */
  public OccupationRegistry()
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
    _list.addAll(_occupTable);
  }


  // ============================================================================
  // PUBLIC METHODS
  // ============================================================================

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

  // @Test
  // public void testRegressionAllOccupationsDontExplode()
  // {
  // MsgCtrl.auditMsgsOn(true);
  // MsgCtrl.errorMsgsOn(true);
  // MsgCtrl.where(this);
  //
  // OccupationRegistry occReg = new OccupationRegistry();
  // for (int i = 0; i < Occupation._ocpTable.length; i++) {
  // Occupation o = occReg.getOccupation(Occupation._ocpTable[i]);
  // for (String name : o.getSkillNames()) {
  // assertNotNull(o.getName() + " had null skill\n", name);
  // }
  // }
  // }

} // end of OccupationRegistry class

