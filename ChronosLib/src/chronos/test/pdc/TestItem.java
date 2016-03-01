/**
 * TestItem.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.test.pdc;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import chronos.pdc.Item;
import chronos.pdc.Item.ItemCategory;

/**
 *    Tests the Item class
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Feb 11, 2013   // original <DD>
 * </DL>
 */
public class TestItem
{
  /** Target object */
  private ArrayList<Item> _itemList = null;

  /** Samples of Items for testing: category, name, weight, quantity */
  static private final String[][] _itemTable = {
      {"ARMOR", "Sword", "80", "1"},                     //   0.25 lb
      {"VALUABLES", "Gold pieces", "2", "150"},                 //  1.875 lb 
      {"EQUIPMENT", "Backpack", "160", "1"},            //  10.0 lb 
      {"EQUIPMENT", "Pair of Boots", "40", "1"},         //   2.5 lb
      {"EQUIPMENT", "Tinderbox, Flint & Steel", "5", "1"}, //   0.3125 lb  
      {"EQUIPMENT", "Torches", "8", "3"},                  //   0.5 lb
      {"PROVISION", "Rations", "2", "3"},                //   0.125 lb 
      {"PROVISION", "Water skein", "80", "1"},       //   5.0 lb
      {"ARMS", "Quarterstaff", "48", "1"},            //   3.0 lb
      {"VALUABLES", "Ruby", "4", "1"}                     //   0.25 lb
  };

  @Before
  public void setUp()
  {
    // Create set of Items for testing
    _itemList = new ArrayList<Item>(_itemTable.length);
    for (String[] itemFields : _itemTable) {
      ItemCategory cat = ItemCategory.valueOf(itemFields[0]);
      String name = itemFields[1];
      int weight = Integer.valueOf(itemFields[2]);
      int qty = Integer.valueOf(itemFields[3]);
      // Finally we can create the Item object
      Item item = new Item(cat, name, qty, weight);
      _itemList.add(item);
    }
    assertEquals(_itemTable.length, _itemList.size());
  }

  /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
   *                  TESTS 
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

  /** Create one of each Item and confirm 
   * @Normal verify Item is built correctly
   */
  @Test
  public void testCtor()
  {
    int k = 0;
    for (String[] itemFields : _itemTable) {
      Item target = _itemList.get(k++);

      assertEquals(itemFields[0], target.getCategory().toString());
      assertEquals(itemFields[1], target.getName());
      assertEquals(Integer.parseInt(itemFields[2]), (int) target.getWeight());
      assertEquals(Integer.parseInt(itemFields[3]), target.getQuantity());
    }
  }


  /** Create one of each Item and confirm 
   * @Error  null parms in ctor
   * @Error  invalid parms in ctor
   */
  @Test(expected = NullPointerException.class)
  public void nullNameIsInvalid()
  {
    new Item(null, null, 0, 0);
  }


  /** Create one of each Item and confirm 
   * @Normal      add and delete quantites for the Item
   * @Error  try to drop more than the Item contains
   */
  @Test
  public void adjustQuantityWithPositiveIncreasesCount()
  {
    Item item = _itemList.get(1);
    int expQty = Integer.parseInt(_itemTable[1][3]);

    int WINDFALL = 97;
    assertEquals(expQty + 97, item.adjustQuantity(WINDFALL));
  }

  @Test
  public void adjustQuantityNegativeDecreaseCount()
  {
    Item item = _itemList.get(1);
    int expQty = Integer.parseInt(_itemTable[1][3]);

    int CASHGONE = -111;
    assertEquals(expQty + CASHGONE, item.adjustQuantity(CASHGONE));
  }

  @Test
  public void adjustQuantityWithLargeNegativeZerosCount()
  {
    Item item = _itemList.get(1);
  
    item.adjustQuantity(Integer.MIN_VALUE);
    assertEquals(0, item.getQuantity());
  }
}
// end of TestItem class
