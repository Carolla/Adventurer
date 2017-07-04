/**
 * TestInventory.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.Item;
import chronos.pdc.character.Inventory;

/**
 * Tests the Inventory repository, a collection of Item objects.
 * 
 * @author Alan Cline
 * @version Jun 8, 2009 // original <br>
 *          Aug 14 2010 // updated for QA tags and regression <br>
 *          Apr 11 2011 // TAA updated QA, added multiple null tests and ensured function <br>
 *          Mar 29 2016 // Reviewed and updated for overall QA testing <br>
 *          July 3, 2017 // revised for integration tests
 */
public class TestInventory
{
  /** Collection used for testing */
  private Inventory _bag;

  private final int STARTING_WT = 334;
  private final int STARTING_COUNT = 12;

  private static final String BOOTS_NAME = "Boots";

  @Before
  public void setUp()
  {
    // Initial inventory is empty
    _bag = new Inventory();
  }

  @After
  public void tearDown()
  {
    MsgCtrl.auditMsgsOn(false);
    _bag.emptyAll();
  }

  // -------------------------------------------------------------------------
  // BEGIN TESTING
  // -------------------------------------------------------------------------

  /**
   * Add new and old single Items to the Inventory; verify their count and quantities
   * 
   * @Normal Inventory.addItem(Item item) ok
   * @Error Inventory.addItem(Item item) can't think of a test case
   * @Null Inventory.addItem(Item item) ok
   */
  @Test
  public void testAddItem()
  {
    _bag.addItem("Belt");
    assertEquals(1, _bag.size());
  }

  
  @Test
  public void addingItemThatAlreadyExistsQuantityCount()
  {
    _bag.addItem("Belt");
    _bag.addItem("Belt");
    assertEquals(1, _bag.size());
    assertEquals(2, _bag.getItem("Belt").getQuantity());
  }

  
  @Test
  public void addingNullItemFails()
  {
    int size = _bag.size();
    assertFalse(_bag.addItem(null));
    assertEquals(size, _bag.size());
  }

  /**
   * Add multiple of same Item to the Inventory; verify their count and quantities
   * 
   * @Normal Inventory.addItem(Item item) ok
   * @Error Inventory.addItem(Item item) N/A
   * @Null Inventory.addItem(Item item) ok
   */
  @Test
  public void addingDifferentItemsIncreasesInventorySize()
  {
    // Item added must exist in the ItemRegistry
    String NEWITEM = "Quarterstaff";
    
    int size0 = _bag.size();
    _bag.addItem(NEWITEM);
    int size1 = _bag.size();
    // Bag size increases by 1 for new item
    assertTrue(size1 == size0 +1);
    
    // Quantity increases by 1 but bug size does not for same item
    _bag.addItem(NEWITEM);
    int size2 = _bag.size();
    assertTrue(size2 == size1);
    assertEquals(2, _bag.getItem(NEWITEM).getQuantity());
  }

  /**
   * Adding null in inconvenient places
   * 
   * @Null Inventory.addItem(Item item)
   */
  @Test
  public void testAddNullItems()
  {}

  /**
   * Test that the all Items weights remain in sync as items are dropped and added
   * 
   * @Normal Inventory.calcInventoryWeight()
   * @Error Inventory.calcInventoryWeight() N/A -- no input parms
   * @Null Inventory.calcInventoryWeight() N/A -- no input parms
   */
  @Test
  public void inventoryStartsProperly()
  {
    _bag.assignBasicInventory();
    assertEquals(STARTING_WT, _bag.calcInventoryWeight());
    assertEquals(STARTING_COUNT, _bag.size());
  }

  @Test
  public void addingItemIncreasesWeight()
  {
    int oldWt = _bag.calcInventoryWeight();
    _bag.addItem("Belt");
    assertTrue(oldWt <= _bag.calcInventoryWeight());
  }

  @Test
  public void removingItemsDecreasesWeight()
  {
    _bag.assignBasicInventory();
    int oldWt = _bag.calcInventoryWeight();

    Item boots = _bag.getItem(BOOTS_NAME);
    assertNotNull(boots);

    assertTrue(_bag.dropItems(boots.getName(), 1));
    assertFalse(_bag.hasItem(boots.getName()));
    assertTrue(oldWt >= _bag.calcInventoryWeight());
  }

  /**
   * Retrieve (by copy) Items from the inventory by name
   * 
   * @Normal Inventory.getItem(String name) ok
   * @Error Inventory.getItem(String name) ok
   * @Null Inventory.getItem(String name) ok
   */
  @Test
  public void testGetItemByName()
  {
    String[] goodTarget = {"Boots", // exists as is
        "Belt pouch", // checks for multiword
        "boots", // lowercase check; uppercase present
    };

    _bag.assignBasicInventory();

    // NORMAL Confirm that all expected items are there...
    for (String goodItemName : goodTarget) {
      assertTrue("Failed to find " + goodItemName,
          _bag.hasItem(goodItemName));
    }
  }

  @Test
  public void cantGetItemNotInInventory()
  {
    String[] badTarget = {"shield", // not there
        "plate mail", // not there
        "pouch", // checks for partial item name
        "gold", // partial match
    };

    // ERROR Confirm that some Items are not found because they are missing
    // or the name is a partial match
    for (String badItemName : badTarget) {
      assertNull(_bag.getItem(badItemName));
    }
  }

  @Test
  public void cantGetNullItem()
  {
    assertNull(_bag.getItem(null));
  }
} // end of TestInventory class

