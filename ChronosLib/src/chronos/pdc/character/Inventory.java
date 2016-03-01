/**
 * Inventory.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.character;

import java.util.ArrayList;
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
public class Inventory
{
  // METADATA CONSTANTS
  /** The weight increment (in ounces) to increase certain APMods */
  static final int AP_INCREMENT = Constants.OUNCES_PER_POUND * 25;
  /** Metal armor increases AP pummeling, decreases grappling */
  static public final int AP_METAL_ADJ = 4;
  /** Peasant must start less than any of the other Klasses */
  static public final int SILVER_PER_GOLD = 10;

  // Keys to all occupational kits
  public enum KitNdx {
    ALCHEMIST, LEATHER, METAL, SEWING, WOOD, THIEVES
  };

  // Name (value) | wt (gpw) ...(8 gp = 1 lb)
  final String[] kits = {
      "Alchemists Kit (100 gp) | 40", // 5 lb
      "Leatherworking Kit (50 gp) | 64", // 8 lb
      "Metalsmith Kit (50 gp) | 80", // 10 lb
      "Sewing Kit (30 gp) | 16", // 2 lb
      "Woodworking Kit (50 gp) | 64", // 8 lb
      "Thieves Kit (50 gp) | 8" // 1 lb
  };

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
    _itemList = new ArrayList<Item>();
  }

  // =============================================================================
  // PUBLIC METHODS
  // =============================================================================

  // Assign initial inventory to Hero (8 gpw = 1 lb)
  public void assignBasicInventory()
  {
    // Basic inventory Items: category, name, quantity, weight (each in fractional lb)
    addItem(new Item(ItemCategory.EQUIPMENT, "Backpack", 1, 7.0));
    addItem(new Item(ItemCategory.EQUIPMENT, "Tinderbox", 1, 0.50));
    addItem(new Item(ItemCategory.EQUIPMENT, "Torch", 1, 1.0));
    addItem(new Item(ItemCategory.PROVISION, "Rations", 2, 0.50));
    addItem(new Item(ItemCategory.PROVISION, "Water skein (full)", 1, 1.5));
    addItem(new Item(ItemCategory.CLOTHING, "Belt pouch, small", 1, 0.25));
    addItem(new Item(ItemCategory.CLOTHING, "Leather boots", 1, 6.0));
    addItem(new Item(ItemCategory.CLOTHING, "Belt", 1, 0.25));
    addItem(new Item(ItemCategory.CLOTHING, "Breeches", 1, 0.50));
    addItem(new Item(ItemCategory.CLOTHING, "Shirt", 1, 0.50));
    addItem(new Item(ItemCategory.CLOTHING, "Cloak", 1, 2.0));
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
    
    if (_itemList.contains(item)) {
      _itemList.get(_itemList.indexOf(item)).adjustQuantity(item.getQuantity());
    } else {
      _itemList.add(item);
    }
    return true;
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
    for (Item i : _itemList) {
      weight += i.getWeight() * i.getQuantity();
    }
    return weight;
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
    for (Item i : _itemList) {
      if (i.getName().equalsIgnoreCase(itemName)) {
        i.adjustQuantity(-1 * nbrToDrop);
      }
    }
    return true;
  }


  /**
   * Get the list of Items in inventory
   * 
   * @return the inventory Items
   */
  public List<Item> getAll()
  {
    return _itemList;
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
    for (Item it : _itemList) {
      if (it.getName().equalsIgnoreCase(itemName)) {
        return it;
      }
    }
    return null;
  }

  /**
   * Get a list of Items by which category they are in
   * 
   * @param category which of the available categories should be selected
   * @return an Item list of only the requested category
   */
  public List<Item> getItemsByCategory(ItemCategory category)
  {
    List<Item> catList = new ArrayList<Item>();
    for (Item i : _itemList) {
      if (i.getCategory() == category) {
        catList.add(i);
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
    List<String> nameList = new ArrayList<String>();
    for (Item i : getItemsByCategory(cat)) {
      nameList.add(i.getName());
    }
    return nameList;
  }

  /**
   * Count the number of Items (number of unique Items, not quantities) in the Inventory
   * 
   * @return the count
   */
  public int size()
  {
    return _itemList.size();
  }

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
    for (Item i : _itemList) {
      if (i.getName().equalsIgnoreCase(target)) {
        return i.getQuantity() > 0;
      }
    }
    return false;
  }
} // end of Inventory class

