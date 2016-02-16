/**
 * TestInventory.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import mylib.ApplicationException;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.Item;
import chronos.pdc.Item.ItemCategory;
import chronos.pdc.character.Inventory;

/**
 * Tests the Inventory repository, a collection of Item objects.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Jun 8, 2009 // original
 *          <DD>
 *          <DT>Build 1.1 Aug 14 2010 // updated for QA tags and regression
 *          <DD>
 *          <DT>Build 1.2 Apr 11 2011 // TAA updated QA, added mutliple null
 *          tests and ensured function
 *          <DD>
 *          </DL>
 */
public class TestInventory
{
  /** Collection used for testing */
  private Inventory _bag;

  // Two Test items
  private Item _framis;
  private final int FRAMIS_WT = 32;
  private final int FRAMIS_QTY = 1;

  private Item _trobe;
  private final int TROBE_WT = 45;
  private final int TROBE_QTY = 2;

  /** Weight (oz) of the starting inventory list */
  private final int STARTING_WT = 18;
  /** Number of Items in the initial Inventory */
  private final int STARTING_COUNT = 11;

  private static final String GOLD_NAME = "Gold pieces";
  private static final String BOOTS_NAME = "Leather Boots";

  @Before
  public void setUp()
  {
    _bag = new Inventory();
    _framis = new Item(ItemCategory.VALUABLES, "Framis",
        FRAMIS_QTY, FRAMIS_WT);
    _trobe = new Item(ItemCategory.ARMS, "Trobe",
        TROBE_QTY, TROBE_WT);
  }

  @After
  public void tearDown()
  {
    MsgCtrl.auditMsgsOn(false);
  }

  // -------------------------------------------------------------------------
  // BEGIN TESTING
  // -------------------------------------------------------------------------

  /**
   * Add new and old single Items to the Inventory; verify their count and
   * quantities
   * 
   * @Normal Inventory.addItem(Item item) ok
   * @Error Inventory.addItem(Item item) can't think of a test case
   * @Null Inventory.addItem(Item item) ok
   */
  @Test
  public void testAddItem()
  {
    _bag.addItem(_framis);
    assertEquals(1, _bag.size());
  }

  @Test
  public void addingItemThatAlreadyExistsIncreasesCount()
  {
    _bag.addItem(_framis);
    assertEquals(FRAMIS_QTY, _bag.getItem("Framis").getQuantity());
    _bag.addItem(_framis);

    assertEquals(1, _bag.size());
    assertEquals(FRAMIS_QTY * 2, _bag.getItem("Framis").getQuantity());
  }

  @Test
  public void addingNullItemFails()
  {
    assertFalse(_bag.addItem(null));
    assertEquals(0, _bag.size());
  }

  /**
   * Add multiple of same Item to the Inventory; verify their count and
   * quantities
   * 
   * @Normal Inventory.addItem(Item item) ok
   * @Error Inventory.addItem(Item item) N/A
   * @Null Inventory.addItem(Item item) ok
   */
  @Test
  public void addingDifferentItemsIncreasesInventorySize()
  {
    int size = _bag.size();
    _bag.addItem(_trobe);
    int size2 = _bag.size();
    _bag.addItem(_framis);
    assertTrue(size < size2 && size2 < _bag.size());
  }

  /**
   * Adding null in inconvenient places
   * 
   * @Null Inventory.addItem(Item item)
   */
  @Test
  public void testAddNullItems()
  {
  }

  /**
   * Test that the all Items weights remain in sync as items are dropped and
   * added
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
    _bag.addItem(_framis);
    assertEquals(oldWt + FRAMIS_QTY * FRAMIS_WT, _bag.calcInventoryWeight());
  }

  @Test
  public void addingMoreItemsIncreaseWeightMore()
  {
    int oldWt = _bag.calcInventoryWeight();
    _bag.addItem(_trobe);
    assertEquals(oldWt + TROBE_WT * TROBE_QTY, _bag.calcInventoryWeight());
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
    assertEquals(oldWt - (int) boots.getWeight(), _bag.calcInventoryWeight());
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
    String[] goodTarget = {
        "Leather boots",                    // exists as is
        "Belt pouch, small",                // checks for multiword
        "leather boots",                    // lowercase check; uppercase present
    };

    _bag.assignBasicInventory();
    
    // NORMAL Confirm that all expected items are there...
    for (String goodItemName : goodTarget) {
      assertTrue("Failed to find " + goodItemName, _bag.hasItem(goodItemName));
    }
  }

  @Test
  public void cantGetItemNotInInventory()
  {
    String[] badTarget = {
        "shield",                               // not there
        "plate mail",                           // not there
        "pouch",                               // checks for partial item name
        "gold",                       // partial match
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


  /**
   * Test that the default Inventory() ctor created with the proper starting
   * items
   * More test must come later when the Inventory is read from a file.
   * 
   * @Normal Inventory.initStartingInventory() ok
   * @Error Inventory.initStartingInventory() tested by Item elsewhere
   * @Null Inventory.initStartingInventory() N/A
   */
  @Test
  public void testDefaultStartingInventory()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.msgln(this, "\ttestDefaultStartingInventory():");

    // Confirm that both the Inventory and the pre-load item String are
    // equal in number
    List<String> items = _bag.getNameList(ItemCategory.ARMS);
    // NORMAL: Confirm that each starting Item name is in the bag
    for (int k = 0; k < items.size(); k++) {
    }

    for (int k = 0; k < items.size(); k++) {
    }
  }
}		// end of TestInventory class

