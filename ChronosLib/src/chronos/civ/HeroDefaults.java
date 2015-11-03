/**
 * HeroDefaults.java Copyright (c) 2012, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.civ;

import java.util.List;

import mylib.ApplicationException;
import mylib.MsgCtrl;
import chronos.pdc.Item;
import chronos.pdc.MiscKeys.ItemCategory;
import chronos.pdc.Occupation;
import chronos.pdc.Skill;
import chronos.pdc.registry.SkillRegistry;

/**
 * Contains all the default information to support the Hero creation: options for hair colors,
 * races, Occupations and associate Skills, and the Items that make up the default Inventory for a
 * newly created Hero.
 * <P>
 * This single object is the only object stored in the Defaults Registry. It is one way to make all
 * the ArrayLists, which are treated as a single generic-blind by db4o, available by Adventurer.
 * <P>
 * This object is used only when the Registries need to be created from scratch, and contains all
 * the hard-coded initialization data.
 * 
 * @author Alan Cline
 * @version Aug 21, 2012 // original <br>
 */
public class HeroDefaults
{
    // /** List of hair color options */
    // ArrayList<String> _hairColors = null;
    // /** List of Races */
    // ArrayList<String> _raceNames = null;
    /** List of Occupations (one Occupation contains its associated Skill) */
    List<Occupation> _ocpList = null;
    /** List of racial skills */
    List<Skill> _skillList = null;
    /** List of default Items given to new Hero */
    List<Item> _inventory = null;


  /**
   * Skills that are defined for particular races. These skills must be moved to
   * <code>DungeonWizard</code> later.
   */
  static private final String[][] _racialSkillTable = {
      {"Archery", "Extra skillful with a short or long bow."},
      {"Geasing", "Able to detect undeground direction and stone-based traps."},
      {"Infravision", "Can see warm bodies in the dark."},
      {"Move Silently", "Chance of sneaking about undetected."},
      {"Resistance to Charm", "Charm spells rarely effect you."},
      {"Resistance to Poison", "Most poisons only moderately effect you."},
      {"Resistance to Sleep", "Sleep spells rarely effect you."},
      {"Tingling", "Ability to find hidden doors, secret panels, or concealed openings."},
  };

  /**
   * Starting Items, and weight in ounces, for the Hero's inventory: Total weight for this stash is
   * 426 oz + Peasant starting cash (15.8 gp = 32 oz) = 464 oz. The String format is used so that
   * the array can be easily initialized until in ItemRegistry. Each Item := Category, Name, Weight
   * (ea), and Quantity.
   */
  static private String[][] _itemTable = {
      {"CASH", "Gold pieces", "2", "15"}, // 30 oz = 1.875 lb
      {"CASH", "Silver pieces", "1", "8"}, // 8 oz = 0.5 lb
      {"GENERAL", "Backpack", "160", "1"}, // 160 oz = 10.0 lb
      {"GENERAL", "Cloak", "32", "1"}, // 32 oz = 2.0 lb
      {"GENERAL", "Belt", "5", "1"}, // 5 oz = 0.3125 lb
      {"GENERAL", "Belt pouch, small", "2", "1"}, // 2 oz = 0.125 lb
      {"GENERAL", "Breeches", "16", "1"}, // 16 oz = 1.0 lb
      {"GENERAL", "Pair of Boots", "40", "1"}, // 40 oz = 2.5 lb
      {"GENERAL", "Shirt", "8", "1"}, // 8 oz = 0.5 lb
      {"GENERAL", "Tinderbox, Flint & Steel", "5", "1"}, // 5 oz = 0.3125 lb
      {"GENERAL", "Torches", "8", "3"}, // 24 oz = 1.5 lb
      {"PROVISION", "Rations", "2", "3"}, // 6 oz = 0.125 lb
      {"PROVISION", "Water skein", "80", "1"}, // 80 oz = 5.0 lb
      {"WEAPON", "Quarterstaff", "48", "1"} // 48 oz = 3.0 lb
  };


  /**
   * Contains same occupations as the Occupation Registry for now. Put occupations into alphabetical
   * order, which is how the names will be displayed. Each skill must be in the SkillRegistry. See
   * the SkillRegistry for a definition of what each of the Skills does.
   */
  private final String[][] _occupTable = {
      // No Skills is a placeholder if no other skills apply (Literacy does not count)
      {"None", "No Occupational Skills"},
      // Reduce damage d3 if falling off walls; also can pass enemy spaces +2 AC
      {"Acrobat", "Tumbling"},
      // Increased chance of recognition (+1 INT) to identify substance or thing
      {"Alchemist", "Arcane Knowledge"},
      // Increased chance of recognition (+1 WIS) to identify substance or potion
      {"Apothecary", "Natural Knowledge"},
      // Repair armor to -1 original AC; make small wooden or metal shields
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
      // +1 on all Saving throws involving risk-taking; +2 on all game rolls
      {"Gambler", "Luck"},
      // 20% increased chance of finding and catching wild game
      {"Hunter", "Hunting"},
      // +1 WIS to determine if Person is lying or bluffing
      {"Innkeeper", "Sense Motive"},
      // Can know base selling value of jewelry and gems
      {"Jeweler", "Appraise"},
      // Can make leather armor, gloves or boots
      {"Leatherworker", "Leatherworking"},
      // +10 chance to find secret doors in stone work, walls, fireplaces, floors
      {"Mason", "Find Secret Openings in Stonework"},
      // +1 WIS to know direction underground
      {"Miner", "Intuit Underground Direction"},
      // +1 movement (normal = half Movement) when swimming over or underwater
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
      // Repair or modify wooden items, e.g. repair xbows, add secret compartment to chest
      {"Woodworker", "Woodworking"}};
  
    private SkillRegistry _skillRegistry;


    /*
     * CONSTRUCTOR(S) AND RELATED METHODS
     */

    /** Constructor */
    public HeroDefaults(SkillRegistry skillRegistry)
    {
        _skillRegistry = skillRegistry;
    }


    /** Create a default Registry and load it with constant data */
    public void initialize()
    {
        // // Load the Hair Colors list
        // for (int k=0; k< _hairColorList.length; k++) {
        // _hairColors.add(_hairColorList[k]);
        // }

        // // Load the Race names list
        // for (int k=0; k< _races.length; k++) {
        // _raceNames.add(_races[k]);
        // }

        // Load the Occupation names
        try {
            for (int k = 0; k < _occupTable.length; k++) {
                Occupation occup =
                        new Occupation(_occupTable[k][0],
                                _skillRegistry.getSkill(_occupTable[k][1]));
                _ocpList.add(occup);
            }
        } catch (ApplicationException ex) {
            MsgCtrl.errMsgln(this, ex.getMessage());
        }

        // Load the Racial Skill names
        try {
            for (int k = 0; k < _racialSkillTable.length; k++) {
                Skill skill = new Skill(_racialSkillTable[k][0], _racialSkillTable[k][1]);
                _skillList.add(skill);
            }
        } catch (ApplicationException ex) {
            MsgCtrl.errMsgln(this, ex.getMessage());
        }

        // Load the starting inventory Items
        try {
            for (int k = 0; k < _itemTable.length; k++) {
                Item it = new Item(ItemCategory.valueOf(_itemTable[k][0]), _itemTable[k][1],
                        Integer.parseInt(_itemTable[k][2]),
                        Integer.parseInt(_itemTable[k][3]));
                _inventory.add(it);
            }
        } catch (ApplicationException ex) {
            MsgCtrl.errMsgln(this, ex.getMessage());
        }

    }


    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
     */

    // /** Retrieve default hair color options */
    // public ArrayList<String> getHairColors()
    // {
    // return _hairColors;
    // }

    // /** Retrieve default hair color options */
    // public ArrayList<String> getRacenames()
    // {
    // return _raceNames;
    // }

    // /** Retrieve default Occupation options */
    // public ArrayList<Occupation> getOccupationList()
    // {
    // return _ocpList;
    // }

    /** Retrieve racial skills for Hero */
    public List<Skill> getSkillList()
    {
        return _skillList;
    }

    /** Retrieve the Hero's default Inventory */
    public List<Item> getDefaultInventory()
    {
        return _inventory;
    }

}
// end of HeroDefaults class
