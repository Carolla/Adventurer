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

import chronos.Chronos;
import mylib.ApplicationException;
import mylib.pdc.Registry;

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
public class ItemRegistry extends Registry
{
  /**
   * 14 Starting Items, and weight in ounces, for the Hero's inventory: Total weight for this stash
   * is 426 oz + Peasant starting cash (15.8 gp = 32 oz) = 464 oz. The String format is used so that
   * the triple-array can be easily iniitialized until in ItemRegistry. Each Item := Category, Name,
   * Weight (ea), and Quantity. Cash will be set from the Klass initCash() method as Gold and
   * Silver, both of Category CASH
   */
  static private String[][] _heroInventory = {
      {"CASH", "Gold pieces", "2", "15"}, // 1.875 lb
      {"CASH", "Silver pieces", "1", "8"}, // 0.5 lb
      {"GENERAL", "Backpack", "160", "1"}, // 10.0 lb
      {"GENERAL", "Cloak", "32", "1"}, // 2.0 lb
      {"GENERAL", "Belt", "5", "1"}, // 0.3125 lb
      {"GENERAL", "Belt pouch, small", "2", "1"}, // 0.125 lb
      {"GENERAL", "Breeches", "16", "1"}, // 1.0 lb
      {"GENERAL", "Pair of Boots", "40", "1"}, // 2.5 lb
      {"GENERAL", "Shirt", "8", "1"}, // 0.5 lb
      {"GENERAL", "Tinderbox, Flint & Steel", "5", "1"}, // 0.3125 lb
      {"GENERAL", "Torches", "8", "3"}, // 0.5 lb
      {"PROVISION", "Rations", "2", "3"}, // 0.125 lb
      {"PROVISION", "Water skein", "80", "1"}, // 5.0 lb
      {"WEAPON", "Quarterstaff", "48", "1"} // 3.0 lb
  };

  // 6 Items for the Bank's assets, wills, and loans
  static private String[][] _bankAssets = {
      // {"CASH", "Gold pieces", "2", "5000"},
      // {"CASH", "Silver pieces", "1", "1000"},
      {"CASH", "Platinum pieces", "2", "100"},
      {"VALUEABLE", "Gem, topaz", "25", "10"},
      {"VALUEABLE", "Gem, ruby", "100", "8"},
      {"VALUEABLE", "Gem, emerald", "250", "3"},
      {"GENERAL", "Will and Testament", "1", "1"},
      {"GENERAL", "Loan", "1", "1"},
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
      {"WEAPON", "Dagger", "16", "15"},
      {"WEAPON", "Poison (L1)", "4", "3"},
      {"GENERAL", "Thieve's Kit", "8", "4"},
      {"GENERAL", "Lockpick", "4", "2"},
      {"GENERAL", "Skeleton Key", "4", "2"},
  };

  // TODO: Fill in this long list from the Players' Manual
  // 3 Items (for now) for the General Store's inventory
  static private String[][] _storeInventory = {
      {"GENERAL", "Lantern", "32", "5"},
      {"GENERAL", "Flask of Oil", "10", "5"},
      {"GENERAL", "Water/wine skein", "16", "9"},
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
  }


  /**
   * Create the Item Registry with the tables given, converting each element to a Item object and
   * saving it in the database.
   */
  @Override
  public void initialize()
  {
//    // Here are the list of tables to load
//    ArrayList<String[][]> tablz = new ArrayList<String[][]>();
//    tablz.add(_heroInventory);
//    tablz.add(_bankAssets);
//    tablz.add(_innMenu);
//    tablz.add(_rogueEquipment);
//    tablz.add(_storeInventory);
//    try {
//      // Load each of the tables
//      for (int k = 0; k < tablz.size(); k++) {
//        loadTable(tablz.get(k));
//      }
//    } catch (ApplicationException ex) {
//      System.err.println("ItemRegistry.initialize(): " + ex.getMessage());
//    }
  }


  /*
   * PUBLIC METHODS
   */

  // /** Retrieves the Iteml with the requested unique name
  // *
  // * @param name name of the Item to retrieve
  // * @return the Item object; or null if not unique
  // * @throws ApplicationException if trying to retrieve non-unique object
  // */
  // @Override
  // public Item get(final String name) throws ApplicationException
  // {
  // ExtObjectContainer db = _regRW.getDB();
  // // Retrieve all skills that match the skillname; should be only one
  // List<Item> list = db.query(new Predicate<Item>() {
  // public boolean match(Item candidate) {
  // return candidate.getName().equals(name);
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


  // /** Converts the name into a searachable Item, and queries the db for a unique object
  // *
  // * @param itemName name of the Item to retrieve
  // * @return the Item object; or null if not found
  // */
  // public Item getItem(String itemName)
  // {
  // // Db requires an object not a string
  // Object target = new Item(itemName);
  // return (Item) _regRW.get(target);
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
  // public void deleteRegistry()
  // {
  // super.delete();
  // // _thisReg = null;
  // }

//  /**
//   * Get a particlar Item by name
//   * 
//   * @param name of the Item
//   * @return the Item
//   */
//  public Item getItem(String name)
//  {
//    try {
//      return (Item) getUnique(name);
//    } catch (ApplicationException ex) {
//      return null;
//    }
//  }


//  /**
//   * Retrieve all Items in the registry
//   * 
//   * @return the Item List
//   */
//  public ArrayList<Item> getItemList()
//  {
//    List<IRegistryElement> itemSet = super.getAll();
//    ArrayList<Item> itemList = new ArrayList<Item>(itemSet.size());
//    for (IRegistryElement e : itemSet) {
//      itemList.add((Item) e);
//    }
//    return itemList;
//  }

  /*
   *  PRIVATE METHODS
   */

//  /**
//   * Load a table of Items into the ItemRegistry
//   * 
//   * @param table the initial Items to load
//   * @throw ApplicationException if the Item could not be added to the db
//   */
//  private void loadTable(String[][] table) throws ApplicationException
//  {
//    // Save the Items required for the new Hero's inventory
//    for (int k = 0; k < table.length; k++) {
//      ItemCategory cat = ItemCategory.valueOf(table[k][0]);
//      String name = table[k][1];
//      int weight = Integer.valueOf(table[k][2]);
//      int qty = Integer.valueOf(table[k][3]);
//      Item item = new Item(cat, name, weight, qty);
//      if (super.add(item) == false) {
//        throw new ApplicationException("loadTable() error while adding to db " + item.getName());
//      }
//    }
//  }


  /*
   * INNER CLASS: MockItemRegistry for Testing 
   */


  /** Inner class for testing Person */
  public class MockItemRegistry
  {
    /** Default constructor */
    public MockItemRegistry()
    {}


  } // end of MockItemRegistry inner class


} // end of ItemRegistry class

