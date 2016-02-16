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
  private final int STARTING_WT = 434;
  /** Number of Items in the initial Inventory */
  private final int STARTING_COUNT = 14;

  private static final String GOLD_NAME = "Gold pieces";
  private static final String SILVER_NAME = "Silver pieces";
  private static final String BOOTS_NAME = "Pair of Boots";

  @Before
  public void setUp()
  {
    _bag = new Inventory();
    _framis = new Item(ItemCategory.VALUABLES, "Framis",
        FRAMIS_WT, FRAMIS_QTY);
    _trobe = new Item(ItemCategory.ARMS, "Trobe",
        TROBE_WT, TROBE_QTY);
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
    _bag.addItem(_framis);

    assertEquals(1, _bag.size());
    int framisQty = _bag.getItem("Framis").getQuantity();
    assertEquals(2, framisQty);
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
  public void testCalcInvWt()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.msgln(this, "\tcalcInventoryWeight(): ");

    // Check starting weight
    int oldWt = _bag.calcInventoryWeight();
    MsgCtrl.msgln("\tBag has initial weight of " + oldWt);
    assertEquals(STARTING_WT, oldWt);

    // Add a Framis Item
    _bag.addItem(_framis);
    int newWt = _bag.calcInventoryWeight();
    int framisWt = FRAMIS_WT * FRAMIS_QTY;
    MsgCtrl.msgln("\tAdding Framis wt (" + framisWt + ") increases Bag from "
        + oldWt + " to " + newWt);
    assertEquals(framisWt + oldWt, newWt);

    // Adding a Trobe Item actually adds 2 Items
    oldWt = newWt;
    _bag.addItem(_trobe);
    newWt = _bag.calcInventoryWeight();
    int trobeWt = TROBE_WT * TROBE_QTY;
    MsgCtrl.msgln("\tAdding Trobe wt (" + trobeWt + ") increases Bag from "
        + oldWt + " to " + newWt);
    assertEquals(trobeWt + oldWt, newWt);

    // Remove Boots (wt = 40) from the bag (qty =1 for pair)
    Item boots = _bag.getItem(BOOTS_NAME);
    assertNotNull(boots);
    // Make copy for later
    Item secondBoots = boots.copy();
    int bootWt = (int) boots.getWeight();
    _bag.dropItems(boots.getName(), 1);
    // Confirm that last set of boots are out of inventory
    // Do not reuse boots Item since its qty = 0;
    assertFalse(_bag.hasItem(boots.getName()));
    oldWt = newWt;
    newWt = _bag.calcInventoryWeight();
    MsgCtrl.msgln("\tDropping Boot wt (" + bootWt + ") reduces Bag from "
        + oldWt + " to " + newWt);
    assertEquals(oldWt - bootWt, newWt);

    // Remove Quarterstaff (wt = 50) from the bag
    Item qstaff = _bag.getItem("Quarterstaff");
    assertNotNull(qstaff);
    // Make copy for later
    Item secondStaff = qstaff.copy();
    int qstaffWt = (int) qstaff.getWeight();
    oldWt = _bag.calcInventoryWeight();
    _bag.dropItems("Quarterstaff", 1);
    // Confirm that quarterstaff is out of inventory
    assertFalse(_bag.hasItem(qstaff.getName()));
    // Do not reuse qstaff Item since its qty = 0;
    oldWt = newWt;
    newWt = _bag.calcInventoryWeight();
    MsgCtrl.msgln("\tDropping Quarterstaff wt (" + qstaffWt + ") reduces Bag from "
        + oldWt + " to " + newWt);
    assertEquals(oldWt - qstaffWt, newWt);

    // Now remove the framis and two trobes; add back the boots and qstaff
    _bag.dropItems("Framis", 1);
    // Confirm that dropped Item is out of inventory
    assertFalse(_bag.hasItem(_framis.getName()));
    oldWt = newWt;
    newWt = _bag.calcInventoryWeight();
    MsgCtrl.msgln("\tDropping Framis wt (" + framisWt + ") reduces Bag from "
        + oldWt + " to " + newWt);
    assertEquals(oldWt - framisWt, newWt);

    // Remove Trobe and confirm that dropped Item is out of inventory
    _bag.dropItems("Trobe", 2);
    assertFalse(_bag.hasItem(_trobe.getName()));
    oldWt = newWt;
    newWt = _bag.calcInventoryWeight();
    MsgCtrl.msgln("\tDropping Trobe wt (" + trobeWt + ") reduces Bag from "
        + oldWt + " to " + newWt);
    assertEquals(oldWt - trobeWt, newWt);

    // Add boots back in again
    _bag.addItem(secondBoots);
    oldWt = newWt;
    newWt = _bag.calcInventoryWeight();
    MsgCtrl.msgln("\tRe-adding Boots wt (" + bootWt + ") increases Bag from "
        + oldWt + " to " + newWt);
    assertEquals(oldWt + bootWt, newWt);

    // Add quarterstaff back in again
    _bag.addItem(secondStaff);
    oldWt = newWt;
    newWt = _bag.calcInventoryWeight();
    MsgCtrl.msgln("\tRe-adding Quarterstaff wt (" + qstaffWt + ") increases Bag from "
        + oldWt + " to " + newWt);
    newWt = _bag.calcInventoryWeight();
    assertEquals(oldWt + qstaffWt, newWt);

    // Bag should weigh its initial weight
    assertEquals(STARTING_WT, newWt);
  }

  /**
   * Test that the total load carried--in ounces--is correct: gold and Items
   * 
   * @Normal Inventory.calcInventoryWeight()
   * @Null Inventory.calcInventoryWeight() N/A -- no input parms
   * @Error Inventory.calcInventoryWeight() N/A -- no input parms
   * @throws ChronosException if test Item fails to be created
   */
  @Test
  public void testCalcTotalCarried() throws ApplicationException
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.msgln(this, "\ttestCalcTotalCarried(): ");

    final int NBR_GOLD1 = 11;
    final int NBR_GOLD2 = 7;
    final int BLATHER_WT = 16;
    final int GP_WT = 2;
    final int ORIGINAL_GOLD = 15;

    // Confirm that starting inventory has correct weight
    int accum = STARTING_WT;
    int load = _bag.calcInventoryWeight();
    MsgCtrl.msgln("\tStarting Inventory weight = " + load);
    assertEquals(STARTING_WT, load);

    // Add an item and check again
    Item blather = new Item(ItemCategory.PROVISION, "Blather", BLATHER_WT, 1);
    _bag.addItem(blather);
    accum += blather.getWeight();
    load = _bag.calcInventoryWeight();
    MsgCtrl.msg("\tAdded " + blather.getWeight() + " oz blather");
    MsgCtrl.msgln("\tCurrent Inventory weight = " + load);
    assertEquals(load, accum);

    // Add multiple gold and check again; biag starts with 15 gp already
    Item gold = new Item(ItemCategory.VALUABLES, GOLD_NAME, GP_WT, NBR_GOLD1);
    // Get the original amount of gold
    Item au = _bag.getItem(GOLD_NAME);
    int auQty = au.getQuantity();
    assertEquals(auQty, ORIGINAL_GOLD);
    _bag.addItem(gold);
    int cashWt = GP_WT * NBR_GOLD1;
    accum += cashWt;      // can't call get weight because of previous gold in
    // bag
    load = _bag.calcInventoryWeight();
    MsgCtrl.msg("\tAdded multiple gold coins: " + cashWt + " oz ");
    MsgCtrl.msgln("\tGold + Inventory weight = " + load);
    assertEquals(load, accum);

    // Drop some of the added gold pieces
    _bag.dropItems(GOLD_NAME, NBR_GOLD2);
    cashWt = NBR_GOLD2 * GP_WT;
    accum -= cashWt;
    load = _bag.calcInventoryWeight();
    MsgCtrl.msgln("\tRemoving " + NBR_GOLD2 + " gold pieces; weight = "
        + cashWt + " oz");
    assertEquals(load, accum);

    // Drop the added item
    _bag.dropItems("Blather", 1);
    accum -= blather.getWeight();
    load = _bag.calcInventoryWeight();
    MsgCtrl.msg("\tRemoving blather, weight " + blather.getWeight() + " oz");
    MsgCtrl.msgln("\tCurrent Inventory weight = " + load);
    assertEquals(load, accum);

    // Drop the rest of the added gold pieces
    int moreCash = NBR_GOLD1 - NBR_GOLD2;
    _bag.dropItems(GOLD_NAME, moreCash);
    cashWt = moreCash * GP_WT;
    accum -= cashWt;
    load = _bag.calcInventoryWeight();
    MsgCtrl.msgln("\tRemoving " + moreCash + " gold pieces; weight = "
        + cashWt + " oz");
    assertEquals(load, accum);

    // Compare with original weight
    MsgCtrl.msgln("\tBack to original weight of " + _bag.calcInventoryWeight() + " oz");
    assertEquals(load, STARTING_WT);
  }

  /**
   * Drop one or more things and compare the final inventory and weights.
   * First, test for dropping single items, then for multiple quantities
   * 
   * @Normal Inventory.dropItems(String thing, int nbrToDrop) ok
   * @Null Inventory.dropItems(String thing, int nbrToDrop) ok
   * @Error Inventory.dropItems(String thing, int nbrToDrop) ok
   *        throws ChronosException if a valid Item cannot be created
   */
  @Test
  public void testDropItems() throws ApplicationException
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.msgln(this, "\ttestDropItems(): ");

    final int SILVER_CNT = 38;
    final int GOLD_CNT = 12;

    // NORMAL
    int count = STARTING_COUNT;
    assertEquals(count, _bag.size());
    // There is only one pair of boots; try dropping them twice
    assertTrue(_bag.dropItems(BOOTS_NAME, 1));
    count -= 1;
    assertEquals(count, _bag.size());
    assertFalse(_bag.dropItems(BOOTS_NAME, 1));
    count -= 0;					// no change since nothing dropped
    assertEquals(count, _bag.size());

    // Add some gold coins to ensure that they can be dropped
    Item gold = new Item(ItemCategory.VALUABLES, GOLD_NAME, 2, GOLD_CNT);
    _bag.addItem(gold);
    // Qty changes but not the number of different items
    assertEquals(count, _bag.size());

    // Add some silver coins to ensure that they can be dropped
    Item silver = new Item(ItemCategory.VALUABLES, SILVER_NAME, 1, SILVER_CNT);
    _bag.addItem(silver);
    // Qty changes but not the number of different items
    assertEquals(count, _bag.size());

    // Confirm that the silver and gold are in the right quantities
    Item au = _bag.getItem(GOLD_NAME);
    Item ag = _bag.getItem(SILVER_NAME);
    int auQty = au.getQuantity();			// s.b. 15
    int agQty = ag.getQuantity();			// s.b. 7

    // Drop 1 gp three times
    assertTrue(_bag.dropItems(GOLD_NAME, 1));
    assertTrue(_bag.dropItems(GOLD_NAME, 1));
    assertTrue(_bag.dropItems(GOLD_NAME, 1));
    auQty -= 3;
    assertTrue(au.getQuantity() == auQty);
    assertTrue(_bag.size() == count);		// there are more gp's in inv.
    // Drop 3 sp at one time
    assertTrue(_bag.dropItems(SILVER_NAME, 3));
    agQty -= 3;
    assertTrue(ag.getQuantity() == agQty);
    assertTrue(_bag.size() == count);		// there are more sp's in inv.

    // Drop a mutliple items to 0 qty and confirm that it is removed
    // completely
    // The trobe is added as a pair, so qty start at 2
    assertFalse(_bag.hasItem(_trobe.getName()));
    int baseCount = _bag.size();
    _bag.addItem(_trobe);
    // Confirm that adding two of an Item increases the item count by 1
    assertTrue(_bag.hasItem(_trobe.getName()));
    assertEquals(_bag.size(), baseCount + 1);
    // Now drop both of them at once to remove the Item
    _bag.dropItems("Trobe", 2);
    assertEquals(_bag.size(), baseCount);
    assertFalse(_bag.hasItem(_trobe.getName()));

    // NULL: try to add a null or invalid item
    assertFalse(_bag.dropItems(null, 1));
    assertTrue(_bag.size() == count);        // no change to count

    // ERROR: Drop negative amounts and more items than is in the Inventory
    assertFalse(_bag.dropItems(GOLD_NAME, -1));
    assertFalse(_bag.dropItems(GOLD_NAME, 0));
    assertFalse(_bag.dropItems(GOLD_NAME, au.getQuantity() + 1));
    // Failed drop attempts do not change the inventory count
    assertTrue(_bag.size() == count);        // no change to count
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
   * Test the Inventory seach method by asking for items that ARE and ARE NOT
   * listed
   * This method takes an Item name and returns a boolan
   * 
   * @Normal Inventory.hasItem(Item item) ok
   * @Error Inventory.hasItem(Item item) ok
   * @Null Inventory.hasItem(Item item) ok
   */
  @Test
  public void testHasItemByItem()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.msgln("\ntestHasItemByItem():");

    // We have two tests Items. Search for them (not there), then add them
    // and
    // check that they are present, then remove them and check that they are
    // mising
    // NORMAL and ERROR cases, depending on if item is in inventory or not
    assertFalse(_bag.hasItem(_framis.getName()));
    assertFalse(_bag.hasItem(_trobe.getName()));
    _bag.addItem(_framis);
    _bag.addItem(_trobe);       // trobes are added in pairs
    _bag.addItem(_framis);
    assertTrue(_bag.hasItem(_framis.getName()));      // 2 framis in bag
    assertTrue(_bag.hasItem(_trobe.getName()));       // 2 trobes in bag
    _bag.dropItems("Framis", 1);
    _bag.dropItems("Trobe", 2);
    // There is no 1 framis left, and 0 trobes
    assertTrue(_bag.hasItem(_framis.getName()));      // 1 framis in bag
    assertFalse(_bag.hasItem(_trobe.getName()));      // 0 trobes in bag
    assertFalse(_bag.hasItem("null"));        // fail gracefully
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

