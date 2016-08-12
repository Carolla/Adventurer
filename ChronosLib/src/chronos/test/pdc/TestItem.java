/**
 * TestItem.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import chronos.pdc.Item;
import chronos.pdc.Item.ItemCategory;

/**
 * Tests the Item class
 *
 * @author Alan Cline
 * @version
 *          <DL>
 *          <DT>Build 1.0 Feb 11, 2013 // original
 *          <DD>
 *          </DL>
 */
public class TestItem
{
  private Item item;

  @Before
  public void setUp()
  {
    item = new Item(ItemCategory.VALUABLES, "FakeItem", 0);
  }


  /**
   * Create one of each Item and confirm
   * 
   * @Normal add and delete quantites for the Item
   * @Error try to drop more than the Item contains
   */
  @Test
  public void adjustQuantityWithPositiveIncreasesCount()
  {
    int expQty = item.getQuantity();

    int WINDFALL = 97;
    assertEquals(expQty + 97, item.adjustQuantity(WINDFALL));
  }

  @Test
  public void adjustQuantityNegativeDecreaseCount()
  {
    int expQty = item.adjustQuantity(120);

    int CASHGONE = -111;
    assertEquals(expQty + CASHGONE, item.adjustQuantity(CASHGONE));
  }

  @Test
  public void adjustQuantityWithLargeNegativeZerosCount()
  {
    item.adjustQuantity(Integer.MIN_VALUE);
    assertEquals(0, item.getQuantity());
  }

  @Test
  public void defaultObjectsEqual()
  {
    Item item2 = new Item(ItemCategory.VALUABLES, "FakeItem", 0);
    assertEquals(item, item2);
    assertEquals(item.toString(), item2.toString());
    assertEquals(item.hashCode(), item.hashCode());
  }

  @Test
  public void differentObjectsNotEqual()
  {
    Item item2 = new Item(ItemCategory.ARMS, "FakeItem", 0);
    assertFalse(item.equals(item2));
    assertFalse(item2.equals(item));
    assertFalse(item.hashCode() == item2.hashCode());
    assertFalse(item.toString().equals(item2.toString()));
  }
  
  @Test
  public void hashEqualsSelf()
  {
    assertEquals(item, item);
    assertEquals(item.toString(), item.toString());
    assertEquals(item.hashCode(), item.hashCode());
  }
}
// end of TestItem class
