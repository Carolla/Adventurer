/**
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
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
import mylib.pdc.Registry;
import chronos.Chronos;
import chronos.pdc.Item;
import chronos.pdc.Item.ItemCategory;

/**
 * Contains all Items in the game: inventory, furniture, weapons, and even dead monsters or NPCs
 * that get treated like Items. <code>ItemRegistry</code> is a read-only singleton and is only
 * initialized once.
 * 
 * @author Tim Armstrong
 * @version Apr 13, 2012 // original <br>
 *          Dec 31, 2012 // updated to follow other Registries and db4o <br>
 *          Feb 12, 2013 // updated to allow all items for all registries <br>
 *          May 6, 2013 // updated getInstance(boolean) <br>
 */
public class ItemRegistry extends Registry<Item>
{
  /**
   * 14 Starting Items, and weight in ounces, for the Hero's inventory: Total weight for this stash
   * is 426 oz + Peasant starting cash (15.8 gp = 32 oz) = 464 oz. The String format is used so that
   * the triple-array can be easily iniitialized until in ItemRegistry. Each Item := Category, Name,
   * Weight (ea), and Quantity. Cash will be set from the Klass initCash() method as Gold and
   * Silver, both of Category CASH
   */
  static private String[][] _heroInventory = {
      {Item.ItemCategory.VALUABLES.name(), "Gold pieces", "2", "15"}, // 1.875 lb
      {Item.ItemCategory.VALUABLES.name(), "Silver pieces", "1", "8"}, // 0.5 lb
      {Item.ItemCategory.EQUIPMENT.name(), "Backpack", "160", "1"}, // 10.0 lb
      {Item.ItemCategory.CLOTHING.name(), "Cloak", "32", "1"}, // 2.0 lb
      {Item.ItemCategory.CLOTHING.name(), "Belt", "5", "1"}, // 0.3125 lb
      {Item.ItemCategory.EQUIPMENT.name(), "Belt pouch, small", "2", "1"}, // 0.125 lb
      {Item.ItemCategory.CLOTHING.name(), "Breeches", "16", "1"}, // 1.0 lb
      {Item.ItemCategory.CLOTHING.name(), "Pair of Boots", "40", "1"}, // 2.5 lb
      {Item.ItemCategory.CLOTHING.name(), "Shirt", "8", "1"}, // 0.5 lb
      {Item.ItemCategory.EQUIPMENT.name(), "Tinderbox, Flint & Steel", "5", "1"}, // 0.3125 lb
      {Item.ItemCategory.EQUIPMENT.name(), "Torches", "8", "3"}, // 0.5 lb
      {Item.ItemCategory.EQUIPMENT.name(), "Rations", "2", "3"}, // 0.125 lb
      {Item.ItemCategory.EQUIPMENT.name(), "Water skein", "80", "1"}, // 5.0 lb
      {Item.ItemCategory.ARMS.name(), "Quarterstaff", "48", "1"} // 3.0 lb
  };

  // 6 Items for the Bank's assets, wills, and loans
  static private String[][] _bankAssets = {
      // {"CASH", "Gold pieces", "2", "5000"},
      // {"CASH", "Silver pieces", "1", "1000"},
      {Item.ItemCategory.VALUABLES.name(), "Platinum pieces", "2", "100"},
      {Item.ItemCategory.VALUABLES.name(), "Gem, topaz", "25", "10"},
      {Item.ItemCategory.VALUABLES.name(), "Gem, ruby", "100", "8"},
      {Item.ItemCategory.VALUABLES.name(), "Gem, emerald", "250", "3"},
      {Item.ItemCategory.EQUIPMENT.name(), "Will and Testament", "1", "1"},
      {Item.ItemCategory.EQUIPMENT.name(), "Loan", "1", "1"},
  };

  // 11 Items for the Inn's menu
  static private String[][] _innMenu = {
      {"PROVISION", "Ale", "8", "25"},
      {"PROVISION", "Apple", "3", "12"},
      {"PROVISION", "Bread", "2", "25"},
      {"PROVISION", "Cheese", "3", "25"},
      {"PROVISION", "Goat milk", "8", "25"},
      {"PROVISION", "Hard candy", "2", "10"},
      {"PROVISION", "Mead", "8", "10"},
      {"PROVISION", "Roast Beef, serving", "8", "25"},
      {"PROVISION", "Roast Boar, serving", "8", "25"},
      {"PROVISION", "Turkey leg", "12", "25"},
      {"PROVISION", "Wine", "8", "25"},
  };

  // 5 Items for the Thieves' Den equipment for the Rogues' Den building
  static private String[][] _rogueEquipment = {
      {Item.ItemCategory.ARMS.name(), "Dagger", "16", "15"},
      {Item.ItemCategory.ARMS.name(), "Poison (L1)", "4", "3"},
      {Item.ItemCategory.EQUIPMENT.name(), "Thieve's Kit", "8", "4"},
      {Item.ItemCategory.EQUIPMENT.name(), "Lockpick", "4", "2"},
      {Item.ItemCategory.EQUIPMENT.name(), "Skeleton Key", "4", "2"},
  };

  // TODO: Fill in this long list from the Players' Manual
  // 3 Items (for now) for the General Store's inventory
  static private String[][] _storeInventory = {
      {Item.ItemCategory.EQUIPMENT.name(), "Lantern", "32", "5"},
      {Item.ItemCategory.EQUIPMENT.name(), "Flask of Oil", "10", "5"},
      {Item.ItemCategory.EQUIPMENT.name(), "Water/wine skein", "16", "9"},
  };

  private static String[][] _characterInventory = {
      // Basic inventory Items: category, name, quantity, weight (each in fractional lb)
      {ItemCategory.ARMS.name(), "Quarterstaff", "1", "3.0"},
      {ItemCategory.MAGIC.name(), "Sacred Satchel", "1", "0.25"},
      {ItemCategory.MAGIC.name(), "Holy symbol, wooden", "1", "0.50"},
      {ItemCategory.SPELL_MATERIAL.name(), "Rosemary sprig", "1", "0.125"},
      {ItemCategory.SPELL_MATERIAL.name(), "Wolfsbane", "2", "0.25"},
      {ItemCategory.ARMS.name(), "Short sword", "1", "7.0"},
      {ItemCategory.ARMOR.name(), "Leather (AC=12)", "1", "10.0"},
      {ItemCategory.EQUIPMENT.name(), "Thieves' kit", "1", "1.0"},
      {ItemCategory.ARMS.name(), "Dagger", "1", "3.0"},
      {ItemCategory.ARMS.name(), "Walking stick", "1", "3.0"},
      {ItemCategory.MAGIC.name(), "Spell book", "1", "5.0"},
      {ItemCategory.MAGIC.name(), "Magic bag", "1", "0.25"},
      {ItemCategory.SPELL_MATERIAL.name(), "Live spider", "1", "0.125"},
      {ItemCategory.EQUIPMENT.name(), "Backpack", "1", "7.0"},
      {ItemCategory.EQUIPMENT.name(), "Tinderbox", "1", "0.50"},
      {ItemCategory.EQUIPMENT.name(), "Torch", "1", "1.0"},
      {ItemCategory.PROVISION.name(), "Rations", "2", "0.50"},
      {ItemCategory.PROVISION.name(), "Water skein (full)", "1", "1.5"},
      {ItemCategory.CLOTHING.name(), "Belt pouch, small", "1", "0.25"},
      {ItemCategory.CLOTHING.name(), "Leather boots", "1", "6.0"},
      {ItemCategory.CLOTHING.name(), "Belt", "1", "0.25"},
      {ItemCategory.CLOTHING.name(), "Breeches", "1", "0.50"},
      {ItemCategory.CLOTHING.name(), "Shirt", "1", "0.50"},
      {ItemCategory.CLOTHING.name(), "Cloak", "1", "2.0"}
  };

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Private ctor because this singleton is called from getInstance(). Registry filename is used for
   * database
   */
  protected ItemRegistry()
  {
    super(Chronos.ItemRegPath);
    if (_shouldInitialize) {
      initialize();
    }
  }


  /**
   * Create the Item Registry with the tables given, converting each element to a Item object and
   * saving it in the database.
   */
  @Override
  public void initialize()
  {
    // Here are the list of tables to load
    List<String[][]> tablz = new ArrayList<String[][]>();
    tablz.add(_heroInventory);
    tablz.add(_bankAssets);
    tablz.add(_innMenu);
    tablz.add(_rogueEquipment);
    tablz.add(_storeInventory);
    tablz.add(_characterInventory);

    for (int k = 0; k < tablz.size(); k++) {
      loadTable(tablz.get(k));
    }
  }


  /*
   * PUBLIC METHODS
   */

  /**
   * Get a particlar Item by name
   * 
   * @param name of the Item
   * @return the Item
   */
  public Item getItem(String name)
  {
    return (Item) get(name);
  }


  /**
   * Retrieve all Items in the registry
   * 
   * @return the Item List
   */
  public List<Item> getItemList()
  {
    return super.getAll();
  }

  /*
   *  PRIVATE METHODS
   */

  /**   
   * Load a table of Items into the ItemRegistry
   * 
   * @param table the initial Items to load
   * @throw ApplicationException if the Item could not be added to the db
   */
  private void loadTable(String[][] table)
  {
    // Save the Items required for the new Hero's inventory
    for (int k = 0; k < table.length; k++) {
      ItemCategory cat = ItemCategory.valueOf(ItemCategory.class, table[k][0]);
      String name = table[k][1];
      int weight = Integer.valueOf(table[k][2]);
      int qty = Integer.valueOf(table[k][3]);
      Item item = new Item(cat, name, weight, qty);
      if (super.add(item) == false) {
        throw new ApplicationException("loadTable() error while adding to db " + item.getName());
      }
    }
  }
} // end of ItemRegistry class

