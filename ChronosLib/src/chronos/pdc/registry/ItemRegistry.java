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

import mylib.pdc.Registry;
import chronos.pdc.Chronos;
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
  @SuppressWarnings("serial")
  private static List<Item> Inventory = new ArrayList<Item>() {{
      add(new Item(ItemCategory.VALUABLES, "Gold pieces", 1));// 1.875 lb
      add(new Item(ItemCategory.VALUABLES, "Silver pieces", 1));// 0.5 lb
      add(new Item(ItemCategory.EQUIPMENT, "Backpack", 160));// 10.0 lb
      add(new Item(ItemCategory.CLOTHING, "Cloak", 32));// 2.0 lb
      add(new Item(ItemCategory.CLOTHING, "Belt", 5));// 0.3125 lb
      add(new Item(ItemCategory.EQUIPMENT, "Belt pouch", 2));// 0.125 lb
      add(new Item(ItemCategory.CLOTHING, "Breeches", 16));// 1.0 lb
      add(new Item(ItemCategory.CLOTHING, "Boots", 40));// 2.5 lb
      add(new Item(ItemCategory.CLOTHING, "Shirt", 8));// 0.5 lb
      add(new Item(ItemCategory.EQUIPMENT, "Tinderbox", 5));// 0.3125 lb
      add(new Item(ItemCategory.EQUIPMENT, "Torch", 8));// 0.5 lb
      add(new Item(ItemCategory.EQUIPMENT, "Rations", 2));// 0.125 lb
      add(new Item(ItemCategory.EQUIPMENT, "Water skein", 80));// 5.0 lb
      add(new Item(ItemCategory.VALUABLES, "Platinum pieces", 2));
      add(new Item(ItemCategory.VALUABLES, "Gem, topaz", 25));
      add(new Item(ItemCategory.VALUABLES, "Gem, ruby", 100));
      add(new Item(ItemCategory.VALUABLES, "Gem, emerald", 250));
      add(new Item(ItemCategory.EQUIPMENT, "Will and Testament", 1));
      add(new Item(ItemCategory.EQUIPMENT, "Loan", 1));
      add(new Item(ItemCategory.PROVISION, "Ale", 8));
      add(new Item(ItemCategory.PROVISION, "Apple", 3));
      add(new Item(ItemCategory.PROVISION, "Bread", 2));
      add(new Item(ItemCategory.PROVISION, "Cheese", 3));
      add(new Item(ItemCategory.PROVISION, "Goat milk", 8));
      add(new Item(ItemCategory.PROVISION, "Hard candy", 2));
      add(new Item(ItemCategory.PROVISION, "Mead", 8));
      add(new Item(ItemCategory.PROVISION, "Roast Beef", 8));
      add(new Item(ItemCategory.PROVISION, "Roast Boar", 8));
      add(new Item(ItemCategory.PROVISION, "Turkey leg", 12));
      add(new Item(ItemCategory.PROVISION, "Wine", 8));
      add(new Item(ItemCategory.ARMS, "Dagger", 16));
      add(new Item(ItemCategory.ARMS, "Poison (L1)", 4));
      add(new Item(ItemCategory.EQUIPMENT, "Thief's Kit", 8));
      add(new Item(ItemCategory.EQUIPMENT, "Lockpick", 4));
      add(new Item(ItemCategory.EQUIPMENT, "Skeleton Key", 4));
      add(new Item(ItemCategory.EQUIPMENT, "Lantern", 32));
      add(new Item(ItemCategory.EQUIPMENT, "Flask of Oil", 10));
      add(new Item(ItemCategory.EQUIPMENT, "Water/wine skein", 16));
      add(new Item(ItemCategory.ARMS, "Quarterstaff", 48));
      add(new Item(ItemCategory.MAGIC, "Sacred Satchel", 2));
      add(new Item(ItemCategory.MAGIC, "Holy symbol, wooden", 1));
      add(new Item(ItemCategory.ARMS, "Short sword", 102));
      add(new Item(ItemCategory.ARMOR, "Leather Armor", 160));
      add(new Item(ItemCategory.ARMS, "Walking stick", 48));
      add(new Item(ItemCategory.MAGIC, "Spell book", 80));
      add(new Item(ItemCategory.MAGIC, "Magic bag", 2));
  }};

  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */

  /**
   * Private ctor because this singleton is called from getInstance(). Registry filename is used for
   * database
   */
  public ItemRegistry()
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
	  _list.addAll(Inventory);
  }


  /*
   * PUBLIC METHODS
   */

  /**
   * Get a particular Item by name
   * 
   * @param name of the Item
   * @return the Item
   */
  public Item getItem(String name)
  {
    return get(name);
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
} // end of ItemRegistry class

