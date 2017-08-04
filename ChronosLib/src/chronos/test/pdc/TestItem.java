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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.Item;
import chronos.pdc.Item.ItemCategory;
import mylib.MsgCtrl;


/**
 * Tests the Item class
 *
 * @author Alan Cline
 * @version Feb 11, 2013 // original <br>
 *          July 23, 2017 // autogen: QA Tool added missing test methods <br>
 *          July 29, 2017 // refactored per QATool <br>
 */
public class TestItem
{
  private Item _item;

  @Before
  public void setUp() throws Exception
  {
    _item = new Item(ItemCategory.VALUABLES, "FakeItem", 0);
    assertNotNull(_item);
  }

  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _item = null;
  }


  // ===============================================================================
  // VERIFY CONSTRUCTOR
  // ===============================================================================

  /**
   * @Normal.Test verify constructor created properly
   */
  @Test
  public void testCtor()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Verify
    assertEquals(ItemCategory.VALUABLES, _item.getCategory());
    assertEquals("FakeItem", _item.getName());
    assertEquals(_item.getWeight(), 0.0, .01);
    assertEquals(_item.getQuantity(), 1);
  }

  /**
   * @Error.Test weight is negative
   */
  @Test
  public void testCtor_ErrorWeight()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Setup
    _item = new Item(ItemCategory.VALUABLES, "FakeItem", -5.2);
    assertNotNull(_item);

    // Verify
    assertEquals(_item.getWeight(), 0.0, .01);
    assertEquals(_item.getQuantity(), 1);
  }

  /**
   * @Null.Test input parms are null
   */
  @Test
  public void testCtor_NullParms()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    try {
      _item = new Item(null, "FakeItem", 0);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (NullPointerException npx) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + "ItemCategory was null");
    }

    try {
      _item = new Item(ItemCategory.ARMS, null, 0);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (NullPointerException npx) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + "Name was null");
    }

    try {
      _item = new Item(ItemCategory.ARMOR, null, 0);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (NullPointerException npx) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + "Name was empty");
    }
  }


  // ===============================================================================
  // BEGIN TESTING
  // ===============================================================================


  /**
   * @Normal.Test add and delete quantities for the Item
   */
  @Test
  public void testAdjustQuantity()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Start with 1 item
    assertEquals(1, _item.getQuantity());

    // Add 1
    assertEquals(2, _item.adjustQuantity(1));

    // Add 1
    assertEquals(3, _item.adjustQuantity(1));

    // Add 0
    assertEquals(3, _item.adjustQuantity(0));

    // Deduct -0
    assertEquals(3, _item.adjustQuantity(-0));

    // Add a bunch: 97
    assertEquals(100, _item.adjustQuantity(97));

    // Deduct them all
    assertEquals(0, _item.adjustQuantity(-100));
  }


  /**
   * @Error.Test remove more items than you have; no change in quantity
   */
  @Test
  public void testAdjustQuantity_ErrorOverDeduction()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Start with 1 item
    assertEquals(1, _item.getQuantity());

    // Deduct 3 (more than owned)
    assertEquals(1, _item.adjustQuantity(-3));
  }


  /**
   * @Normal.Test boolean equals() -- compare two Items as equal in various ways
   */
  @Test
  public void testEquals()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
  
    Item item2 = new Item(ItemCategory.VALUABLES, "FakeItem", 0);
    assertEquals(_item, item2);
    assertEquals(_item.toString(), item2.toString());
    assertEquals(_item.hashCode(), _item.hashCode());
  }

  /**
   * @Error.Test boolean equals() -- compare two Items as equal in various ways
   */
  @Test
  public void testEquals_DifferentObjectsNotEqual()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
  
    Item item2 = new Item(ItemCategory.ARMS, "FakeItem", 0);
    assertFalse(_item.equals(item2));
    assertFalse(item2.equals(_item));
    assertFalse(_item.hashCode() == item2.hashCode());
    assertFalse(_item.toString().equals(item2.toString()));
  }

  /**
   * @Not.Needed String getCategory() -- simple getter
   */
  public void testGetCategory()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }

  /**
   * @Not.Needed String getKey() -- simple getter
   */
  public void testGetKey()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed double getName() -- simple getter
   */
  public void testGetName()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }

  /**
   * @Not.Needed int getQuantity() -- simple getter
   */
  public void testGetQuantity()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Normal.Test int hashCode() -- generate a unique ID for the Item object
   */
  @Test
  public void testHashCode()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    assertEquals(_item, _item);
    assertEquals(_item.toString(), _item.toString());
    assertEquals(_item.hashCode(), _item.hashCode());
  }


  /**
   * @Normal.Test String toString() -- converts an Item to a multifield record
   */
  @Test
  public void testToString()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Precondition Item verified
    String expected = "FakeItem (VALUABLES) : 1 : 0.0";
    MsgCtrl.msgln("\tExpected = " + expected);
    MsgCtrl.msgln("\t Received = " + _item.toString());
    assertTrue(expected.equals(_item.toString()));
    
    // Second item created and checked
    expected = "Torch (EQUIPMENT) : 9 : 11.4";
    Item newItem = new Item(ItemCategory.EQUIPMENT, "Torch", 11.4);
    newItem.adjustQuantity(8);
    MsgCtrl.msgln("\tExpected = " + expected);
    MsgCtrl.msgln("\t Received = " + newItem.toString());
    assertTrue(expected.equals(newItem.toString()));
  }


}   // end of TestItem class
