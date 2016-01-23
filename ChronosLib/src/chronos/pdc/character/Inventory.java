/**
 * Inventory.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.character;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import mylib.Constants;
import chronos.pdc.Item;
import chronos.pdc.Item.ItemCategory;

/**
 * Contains the Person's collection of Items. All weights are in ounces; the client object must
 * convert to lbs if that is the desired unit. Currently, an Item consists of a category, a name,
 * its weight, and a quantity.
 * 
 * @author Alan Cline
 * @version Mar 18, 2009 // original <br>
 *          Jun 7, 2009 // updated for Item objects instead of Strings <br>
 *          Oct 21, 2009 // changed weights to be oz ints instead of codes <br>
 *          Apr 11, 2011 // TAA uncommented money for testing <br>
 *          Jun 13, 2011 // ABC added data shuttle handling <br>
 *          Nov 25, 2011 // ABC replaced double cash with Items Gold and Silver <br>
 *          Oct 13, 2015 // updated for new Hero generation rules and categories <br>
 */
public class Inventory implements Serializable
{
  /** Recommended serialization constant */
  static final long serialVersionUID = 1111L;

  // METADATA CONSTANTS
  /** The weight increment (in ounces) to increase certain APMods */
  static final int AP_INCREMENT = Constants.OUNCES_PER_POUND * 25;
  /** Metal armor increases AP pummeling, decreases grappling */
  static public final int AP_METAL_ADJ = 4;
  /** Peasant must start less than any of the other Klasses */
  static public final int SILVER_PER_GOLD = 10;

  // TODO: Remove this. It is in Hero attributes
  /** List of mods for Action Points */
  private enum APMODS {
    OVERBEARING, GRAPPLING, PUMMELING, SHIELD_BASH
  }

  // Holds all inventory items
  private ArrayList<Item> _itemList;


  // =============================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // =============================================================================

  /**
   * Some Inventories are meant to be empty, for creating subsets. To populate the inventory with
   * starting Items, call <code>initStartingInventory()</code>.
   */
  public Inventory()
  {
    // Create the map to hold the Items
    // Holds all inventory items
    _itemList = new ArrayList<Item>();

  }

  // =============================================================================
  // PUBLIC METHODS
  // =============================================================================

  // Assign initial inventory to Hero (8 gpw = 1 lb)
  public Inventory assignBasicInventory(Inventory inven)
  {
    // Basic inventory Items: category, name, quantity, weight (each in fractional lb)
    inven.addItem(new Item(ItemCategory.EQUIPMENT, "Backpack", 1, 7.0));
    inven.addItem(new Item(ItemCategory.EQUIPMENT, "Tinderbox", 1, 0.50));
    inven.addItem(new Item(ItemCategory.EQUIPMENT, "Torch", 1, 1.0));
    inven.addItem(new Item(ItemCategory.PROVISION, "Rations", 2, 0.50));
    inven.addItem(new Item(ItemCategory.PROVISION, "Water skein (full)", 1, 1.5));
    inven.addItem(new Item(ItemCategory.CLOTHING, "Belt pouch, small", 1, 0.25));
    inven.addItem(new Item(ItemCategory.CLOTHING, "Leather boots", 1, 6.0));
    inven.addItem(new Item(ItemCategory.CLOTHING, "Belt", 1, 0.25));
    inven.addItem(new Item(ItemCategory.CLOTHING, "Breeches", 1, 0.50));
    inven.addItem(new Item(ItemCategory.CLOTHING, "Shirt", 1, 0.50));
    inven.addItem(new Item(ItemCategory.CLOTHING, "Cloak", 1, 2.0));

    return inven;
  }

  /**
   * Adds a copy of the incoming Item to the Inventory; used to build sub-inventories by
   * ItemCategory. If that kind of Item is already in inventory, then its quantity is increased by
   * the quantity of the incoming item. Item parm is marked <code>final</code> as a protection from
   * editing it because it is manipulated independently of the original incoming Item.
   * 
   * @param item to add (or increase qty count); cannot edit its contents
   * @return false if the copy did not work
   */
  public boolean addItem(final Item item)
  {
    // Check that there is actually something to add
    if (item == null) {
      return false;
    }
    // If Item is already in inventory, increase its item count (qty)
    int ndx = _itemList.indexOf(item);
    if (ndx == Constants.NOT_FOUND) {
      _itemList.add(item);
    } else {
      item.adjustQuantity(1);
      _itemList.set(ndx, item);    // replace the item with updated qty
    } 
    return true;
  }

//  /**
//   * Set the armor class, on a scale of 10 (no armor) to full plate mail and shield (18). Some low
//   * DEX values or cursed items may bring AC less than No_Armor but it can never be less than 1.
//   * Magic armor allows the AC to be higher than 18. For now, until Inventory and Items are
//   * implemented fully, the Person starts with no armor.
//   * 
//   * @param adj armor class adjustment due to Dex and armor worn
//   * @return resulting armor class
//   */
//  public int calcAC(int adj)
//  {
//    _ac = Math.max(NO_ARMOR + adj, 1);
//    return _ac;
//  }

  // TODO: Arguably, weight carried also affects overbearing potential, but it also affects speed.
  // For now, these two effects are considered to cancel out.
  /**
   * Calculate the real-time value for: Overbearing = AP + (1 per 25 lb weight carried); +4 if
   * wearing metal armor Grappling = AP + DamageMod + ToHitMissileMod; -4 if wearing metal gauntlets
   * Pummeling = AP; +4 if wearing metal gauntlets Shield Bash = AP if the Hero has a shield; else 0
   * These four mods are not saved because they must be calculated each time they are used.
   * 
   * @param ap action points
   * @param damMod damage mod based on STR
   * @param toHitMissileMod based on DEX
   * @param heroWt weight of the Hero in ounces
   * @return array of 4 mods
   */
  public EnumMap<APMODS, Integer> calcAPMods(int ap, int damMod,
      int toHitMissileMod,
      double heroWt)
  {
    EnumMap<APMODS, Integer> mods = new EnumMap<APMODS, Integer>(
        APMODS.class);
    // TODO: Add in +4 if Person has on metal armor
    // Calculate overbearing..
    int overbearing = (int) (ap + heroWt * AP_INCREMENT);
    // TODO: Add in -4 if Person has on metal armor
    // Calculate grappling..
    int grappling = ap + damMod + toHitMissileMod;
    // TODO: Add in +4 if Person has on metal armor
    // Calculate pummeling..
    int pummeling = ap;
    // TODO: Set to AP if Person has a shield
    // Calculate shield bash
    int bash = 0;
    // Collect all mods and return
    mods.put(APMODS.OVERBEARING, new Integer(overbearing));
    mods.put(APMODS.GRAPPLING, new Integer(grappling));
    mods.put(APMODS.PUMMELING, new Integer(pummeling));
    mods.put(APMODS.SHIELD_BASH, new Integer(bash));
    return mods;
  }

  /**
   * Adds up the weight of each Item in inventory, including money. <br>
   * Weight = weight of the Item times the quantity of that Item.
   * 
   * @return total weight of inventory (in ounces)
   */
  public int calcInventoryWeight()
  {
    int weight = 0;
    // Get the weight of each Item in the list
    for (int k = 0; k < _itemList.size(); k++) {
      // Get the weight of the next item in the collection (in ounces)
      Item thing = _itemList.get(k);
      weight += thing.getWeight() * thing.getQuantity();
    }
    return weight;
  }

  /** Inventory objects are self-loading, clear it for testing */
  public void clear()
  {
    _itemList.clear();
  }

  /**
   * Drop (remove) one or more Items of the same kind from Inventory. If there are multiples, then
   * quantity is decremented. If qty = 1, then Item itself is removed from the inventory.
   * 
   * @param thing to drop
   * @param nbrToDrop of those things to drop
   * @return true if Item(s) are removed successfully, else false if drop failed
   */
  public boolean dropItems(String itemName, int nbrToDrop)
  {
    // // Guard against null Items or negative number
    // if ((itemName == null) || (nbrToDrop < 1)) {
    // return false;
    // }
    //
    // // Get Item to work with, if it can be found
    // Item thing = getItem(itemName);
    // // if Item doesn't exist, return false
    // if (thing == null) {
    // return false;
    // }
    // // Get its position for later update
    // int pos = _inventory.indexOf(thing);
    // if (pos == Constants.NOT_FOUND) {
    // return false;
    // }
    //
    // // Find out how many of these things there are
    // int count = thing.getQuantity();
    // // Ensure that there are enough to drop as requested
    // if ((nbrToDrop > count) || (count == 0)) {
    // return false;
    // }
    //
    // // Adjust the qty to account for the drop nbr
    // int newCount = 0;
    // try {
    // newCount = thing.adjustQuantity(-nbrToDrop);
    // } catch (ApplicationException e) {
    // MsgCtrl.errMsg(e.getMessage());
    // System.exit(0);
    // }
    // // If the qty becomes zero, remove the Item from the list
    // if (newCount == 0) {
    // _inventory.remove(pos);
    // }
    // // If qty is not zero, overwrite adjusted Item back into list
    // else {
    // _inventory.set(pos, thing);
    // }
    return true;
  }

  // /** Cash is stored as two items: gold and silver, and kept separate to maintain the weight
  // load.
  // * @return gold in index 0; silver in index 1 of the array
  // */
  // public ArrayList<Integer> getCash()
  // {
  // ArrayList<Integer> cash = new ArrayList<Integer>(2);
  // cash.cash[GOLD] = getItem("Gold").getQuantity();
  // cash[SILVER] = getItem("Silver").getQuantity();
  // return cash;
  // }

//  /**
//   * Get the cash currently in inventory
//   * 
//   * @return the gold and silver pieces as a combined double gp.sp
//   */
//  public double getGoldBanked()
//  {
//    return _goldBanked;
//  }

  /**
   * Get the list of Items in inventory
   * 
   * @return the inventory Items
   */
  public ArrayList<Item> getAll()
  {
    return _itemList;
  }

  /**
   * Return the Item for the given index. This is a convenience method for traversing the inventory
   * easily from within.
   * 
   * @param index position in the list of the requested Item (zero-based)
   * @return the requested Item; else null
   */
  public Item getItem(int index)
  {
    if ((index < 0) || (index >= _itemList.size())) {
      return null;
    }
    return _itemList.get(index);
  }

  /**
   * Retrieve an Item reference by name (does not remove it from Inventory but does allow it to be
   * modified and replaced)
   * 
   * @param itemName the name, or adjective, of the Item being searched; it should find "small"
   *        equally as well as "shield"
   * @return the Item if the name of the Item was found; else null
   */
  public Item getItem(String itemName)
  {
    // // Guard against null input
    // if (itemName == null) {
    // return null;
    // }
    //
    // for (Item it : _inventory) {
    // if (itemName.equalsIgnoreCase(it.getName())) {
    // return it;
    // }
    // }
    return null;
  }

  /**
   * Get a list of Items by which category they are in
   * 
   * @param category which of the available categories should be selected
   * @return an Item list of only the requested category
   */
  public Inventory getItemsByCategory(ItemCategory category)
  {
    int invSize = _itemList.size();
    Inventory catList = new Inventory();
    for (int k = 0; k < invSize; k++) {
      Item thing = _itemList.get(k);
      if (thing.getCategory() == category) {
        catList.addItem(thing);
      }
    }
    return catList;
  }

  /** Retrieve a list of all items in the given invenotry by name
   * 
   * @param cat     category of item to build a subset from
   * @return the list of names for the subset inventory
   */
  public List<String> getNameList(ItemCategory cat)
  {
    Inventory aList = getItemsByCategory(cat);
    List<String> nameList = new ArrayList<String>();
    for (int k = 0; k < aList.getNbrItems(); k++) {
      String itemName = aList.getItem(k).getName();
      nameList.add(itemName);
    }
    return nameList;
  }

  /**
   * Count the number of Items (number of unique Items, not quantities) in the Inventory
   * 
   * @return the count
   */
  public int getNbrItems()
  {
    return _itemList.size();
  }

  // /**
  // * Confirm that the String of inventory items are properly loaded Later, the Inventory will be
  // * read from the <code>DungeonWizard</code> file
  // *
  // * @return the list of items before they populate the Inventory
  // */
  // public String[][] getItemList()
  // {
  // return _startList;
  // }

  // /**
  // * Examine the Item list for requested Items.
  // *
  // * @param target the Item being searched
  // * @return the Item if the name of the Item was found; else null
  // */
  // public boolean hasItem(Item target)
  // {
  // return _inventory.contains(target);
  // }

  /**
   * Traverse the inventory list, looking for Items by name. This method uses the equalsIgnoreCase
   * method implemented in the Item class. For now, the Item must match the full multi-word name of
   * the target, but the search is case-insensitive. later, "small belt pouch" and "large pouch"
   * should be found when looking for "pouch".
   * 
   * @param target the name, or adjective, of the Item being searched
   * @return true if the Item was found, else false
   */
  public boolean hasItem(String target)
  {
    for (int k = 0; k < _itemList.size(); k++) {
      // Extract the Item from the Inventory and lower-case it
      Item thing = _itemList.get(k);
      if (target.equalsIgnoreCase(thing.getName())) {
        return true;
      }
    }
    return false;
  }

  // ===========================================================================
  // INNER CLASS: MockInventory
  // ===========================================================================

  /** Inner class for testing Inventory */
  public class MockInventory
  {
    /** Default constructor */
    public MockInventory()
    {}

    /** Search for a few select items to see if they can be found */
    public boolean searchTest()
    {
      boolean retval = false;
      String[] target = {"Boots", // exists as is
          "Belt pouch, small", // checks for multiword
          "shield", // not there
          "small", // lowercase check; uppercase present
          "plate mail", // not there
          "pouch"}; // checks for partial item name

      for (int k = 0; k < target.length; k++) {
        if (hasItem(target[k]) == true) {
          System.out.println("Inventory search: " + target[k]
              + " found!");
          retval = true;
        } else {
          System.out.println("Inventory search: " + target[k]
              + " missing!");
          retval = true;
        }
      }
      return retval;
    }

//    /**
//     * @return the size of the starting list
//     */
//    public int startSize()
//    {
//      return _startList.length;
//    }

//    /**
//     * Test that the starting inventory, when a different string list is used, still works. Replace
//     * the default list with the given new list
//     * 
//     * @param alternate item list
//     * @return the inventory newly createed
//     */
//    public Inventory replaceStartList(String[][] newList)
//    {
//      // Clear out the statically-defined startlist
//      Inventory.this._inventory.clear();
//      // Replace the old list that got initialized inherently
//      Inventory.this._startList = newList;
//
//      // Inventory is populated by the starting inventory method
//      Inventory.this.initStartingInventory();
//      return Inventory.this;
//    }

  } // end of MockInventory inner class

} // end of Inventory class

