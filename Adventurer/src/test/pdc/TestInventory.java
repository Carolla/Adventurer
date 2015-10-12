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

import junit.framework.TestCase;
import mylib.ApplicationException;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pdc.Inventory;
import pdc.Inventory.MockInventory;
import chronos.civ.MiscKeys.ItemCategory;
import chronos.pdc.Item;

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
public class TestInventory extends TestCase
{
    /** Collection used for testing */
    private Inventory _bag = null;
    private MockInventory _mock = null;

    // Two Test items
    private Item _framis = null;
    private final int FRAMIS_WT = 32;
    private final int FRAMIS_QTY = 1;

    private Item _trobe = null;
    private final int TROBE_WT = 45;
    private final int TROBE_QTY = 2;

    /** Weight (oz) of the starting inventory list */
    private final int STARTING_WT = 434;
    /** Number of Items in the initial Inventory */
    private final int STARTING_COUNT = 14;
    
    private static final String GOLD_NAME = "Gold pieces";
    private static final String SILVER_NAME = "Silver pieces";
    private static final String BOOTS_NAME = "Pair of Boots";


    /**
     * Create Inventory bag and mock object
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        // Error messages are ON at start of each test
        MsgCtrl.errorMsgsOn(true);
        // Audit messages are OFF at start of each test
        MsgCtrl.auditMsgsOn(false);
        // Create an initial Inventory from scratch, then populate it
        _bag = new Inventory();
        _bag.initStartingInventory();
        // Create a MockInventory for testing
        _mock = _bag.new MockInventory();
        // Create two test Items
        _framis = new Item(ItemCategory.VALUEABLE, "Framis",
                FRAMIS_WT, FRAMIS_QTY);
        _trobe = new Item(ItemCategory.WEAPON, "Trobe",
                TROBE_WT, TROBE_QTY);
    }

    /**
     * Delete Inventory bag and mock object
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _bag = null;
        _mock = null;
        // Audit messages are OFF after each test
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
    public void testAddItem()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.msgln("\ntestAddItem(): ");

        // NORMAL: Add a new Item to the existing inventory

        // Confirm that it doesn't exist in the bag
        assertEquals(_bag.hasItem(_framis), false);
        int count = _bag.getNbrItems();

        // Add the new item
        _bag.addItem(_framis);
        int newCount = _bag.getNbrItems();

        // Verify
        MsgCtrl.msgln("\t Framis added to increase Item count from "
                + count + " to " + newCount);
        assertEquals(newCount, count + 1);
        Item tmp = _bag.getItem("Framis");
        int framisQty = tmp.getQuantity();
        assertEquals(framisQty, FRAMIS_QTY);
        MsgCtrl.msgln("\t Framis quantity now " + framisQty);

        // Normal: Add an Item that already exists

        // Confirm that it already exists
        assertEquals(true, _bag.hasItem(_framis));
        count = _bag.getNbrItems();

        // Add the new item
        _bag.addItem(_framis);
        newCount = _bag.getNbrItems();

        // Verify
        MsgCtrl.msgln("\t Framis added again without changing Item count: from "
                + count + " to " + newCount);
        assertEquals(newCount, count);
        Item tmp2 = _bag.getItem("Framis");
        framisQty = tmp2.getQuantity();
        assertEquals(framisQty, FRAMIS_QTY * 2);
        MsgCtrl.msgln("\t Framis quantity now " + framisQty);

        // Null: Add a null item, should fail gracefull
        MsgCtrl.msgln("\t Null item added should fail gracefully.");
        count = _bag.getNbrItems();
        _bag.addItem(null);
        assertEquals(count, _bag.getNbrItems());
    }

    /**
     * Add multiple of same Item to the Inventory; verify their count and
     * quantities
     * 
     * @Normal Inventory.addItem(Item item) ok
     * @Error Inventory.addItem(Item item) N/A
     * @Null Inventory.addItem(Item item) ok
     */
    public void testAddMultipleItem()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.msgln("\ntestAddMultipleItem(): ");
        // NORMAL: Add a new multiple Item to the existing inventory
        // Confirm that it doesn't exist in the bag
        assertEquals(_bag.hasItem(_trobe), false);
        int count = _bag.getNbrItems();
        // Add the new item
        _bag.addItem(_trobe);
        int newCount = _bag.getNbrItems();
        // Verify
        MsgCtrl.msgln("\t Trobes added to increase Item count from "
                + count + " to " + newCount);
        assertEquals(newCount, count + 1);
        Item tmp = _bag.getItem("Trobe");
        int trobeQty = tmp.getQuantity();
        assertEquals(trobeQty, TROBE_QTY);
        MsgCtrl.msgln("\t Trobe quantity now " + trobeQty);

        // Normal: Add the multiple Item again
        // Confirm that it already exists
        assertEquals(true, _bag.hasItem(_trobe));
        count = _bag.getNbrItems();
        // Add the new item
        _bag.addItem(_trobe);
        newCount = _bag.getNbrItems();
        // Verify
        MsgCtrl.msgln("\t Trobe added again without changing Item count: from "
                + count + " to " + newCount);
        assertEquals(newCount, count);
        Item tmp2 = _bag.getItem("Trobe");
        trobeQty = tmp2.getQuantity();
        assertEquals(trobeQty, TROBE_QTY * 2);
        MsgCtrl.msgln("\t Trobe quantity now " + trobeQty);
    }

    /**
     * Adding null in inconvenient places
     * 
     * @Null Inventory.addItem(Item item)
     */
    public void testAddNullItems()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.msgln("\ntestAddNullItems(): ");
        int count = _bag.getNbrItems();
        // Add the new item
        _bag.addItem(null);
        int newCount = _bag.getNbrItems();
        // Verify
        assertEquals(newCount, count);

        // NULL case returns a false
        assertFalse(_bag.addItem(null));
    }

    /**
     * Test that the all Items weights remain in sync as items are dropped and
     * added
     * 
     * @Normal Inventory.calcInventoryWeight()
     * @Error Inventory.calcInventoryWeight() N/A -- no input parms
     * @Null Inventory.calcInventoryWeight() N/A -- no input parms
     */
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
        int bootWt = boots.getWeight();
        _bag.dropItems(boots.getName(), 1);
        // Confirm that last set of boots are out of inventory
        // Do not reuse boots Item since its qty = 0;
        assertFalse(_bag.hasItem(boots));
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
        int qstaffWt = qstaff.getWeight();
        oldWt = _bag.calcInventoryWeight();
        _bag.dropItems("Quarterstaff", 1);
        // Confirm that quarterstaff is out of inventory
        assertFalse(_bag.hasItem(qstaff));
        // Do not reuse qstaff Item since its qty = 0;
        oldWt = newWt;
        newWt = _bag.calcInventoryWeight();
        MsgCtrl.msgln("\tDropping Quarterstaff wt (" + qstaffWt + ") reduces Bag from "
                + oldWt + " to " + newWt);
        assertEquals(oldWt - qstaffWt, newWt);

        // Now remove the framis and two trobes; add back the boots and qstaff
        _bag.dropItems("Framis", 1);
        // Confirm that dropped Item is out of inventory
        assertFalse(_bag.hasItem(_framis));
        oldWt = newWt;
        newWt = _bag.calcInventoryWeight();
        MsgCtrl.msgln("\tDropping Framis wt (" + framisWt + ") reduces Bag from "
                + oldWt + " to " + newWt);
        assertEquals(oldWt - framisWt, newWt);

        // Remove Trobe and confirm that dropped Item is out of inventory
        _bag.dropItems("Trobe", 2);
        assertFalse(_bag.hasItem(_trobe));
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
        Item blather = new Item(ItemCategory.GENERAL, "Blather", BLATHER_WT, 1);
        _bag.addItem(blather);
        accum += blather.getWeight();
        load = _bag.calcInventoryWeight();
        MsgCtrl.msg("\tAdded " + blather.getWeight() + " oz blather");
        MsgCtrl.msgln("\tCurrent Inventory weight = " + load);
        assertEquals(load, accum);

        // Add multiple gold and check again; biag starts with 15 gp already
        Item gold = new Item(ItemCategory.CASH, GOLD_NAME, GP_WT, NBR_GOLD1);
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
    public void testDropItems() throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.msgln(this, "\ttestDropItems(): ");

        final int SILVER_CNT = 38;
        final int GOLD_CNT = 12;
        
        // NORMAL
        int count = STARTING_COUNT;
        assertEquals(count, _bag.getNbrItems());
        // There is only one pair of boots; try dropping them twice
        assertTrue(_bag.dropItems(BOOTS_NAME, 1));
        count -= 1;
        assertEquals(count, _bag.getNbrItems());
        assertFalse(_bag.dropItems(BOOTS_NAME, 1));
        count -= 0;					// no change since nothing dropped
        assertEquals(count, _bag.getNbrItems());

        // Add some gold coins to ensure that they can be dropped
        Item gold = new Item(ItemCategory.CASH, GOLD_NAME, 2, GOLD_CNT);
        _bag.addItem(gold);
        // Qty changes but not the number of different items
        assertEquals(count, _bag.getNbrItems());

        // Add some silver coins to ensure that they can be dropped
        Item silver = new Item(ItemCategory.CASH, SILVER_NAME, 1, SILVER_CNT);
        _bag.addItem(silver);
        // Qty changes but not the number of different items
        assertEquals(count, _bag.getNbrItems());

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
        assertTrue(_bag.getNbrItems() == count);		// there are more gp's in inv.
        // Drop 3 sp at one time
        assertTrue(_bag.dropItems(SILVER_NAME, 3));
        agQty -= 3;
        assertTrue(ag.getQuantity() == agQty);
        assertTrue(_bag.getNbrItems() == count);		// there are more sp's in inv.

        // Drop a mutliple items to 0 qty and confirm that it is removed
        // completely
        // The trobe is added as a pair, so qty start at 2
        assertFalse(_bag.hasItem(_trobe));
        int baseCount = _bag.getNbrItems();
        _bag.addItem(_trobe);
        // Confirm that adding two of an Item increases the item count by 1
        assertTrue(_bag.hasItem(_trobe));
        assertEquals(_bag.getNbrItems(), baseCount + 1);
        // Now drop both of them at once to remove the Item
        _bag.dropItems("Trobe", 2);
        assertEquals(_bag.getNbrItems(), baseCount);
        assertFalse(_bag.hasItem(_trobe));

        // NULL: try to add a null or invalid item
        assertFalse(_bag.dropItems(null, 1));
        assertTrue(_bag.getNbrItems() == count);        // no change to count

        // ERROR: Drop negative amounts and more items than is in the Inventory
        assertFalse(_bag.dropItems(GOLD_NAME, -1));
        assertFalse(_bag.dropItems(GOLD_NAME, 0));
        assertFalse(_bag.dropItems(GOLD_NAME, au.getQuantity() + 1));
        // Failed drop attempts do not change the inventory count
        assertTrue(_bag.getNbrItems() == count);        // no change to count
    }

    // /**
    // * Retrieve (copy) Items from the inventory by index position
    // *
    // * @Normal Inventory.getItem(int index) ok
    // * @Error Inventory.getItem(int index) ok
    // * @Null Inventory.getItem(int index) N/A: cannot call int with null
    // */
    // public void testGetItemByIndex()
    // {
    // MsgCtrl.auditMsgsOn(false);
    // MsgCtrl.msgln(this, "\ttestGetItemByIndex(): ");
    // // NORMAL
    // // Get the 1st, last, and middle item from starting Inventory
    // Item thing = _bag.getItem(0);
    // assertEquals(thing.getName(), "Backpack");
    // thing = _bag.getItem(STARTING_COUNT - 1);
    // assertEquals(thing.getName(), "Silver");
    // thing = _bag.getItem(8);
    // assertEquals(thing.getName(), "Torches");
    // // Add an item and check that it was appended
    // _bag.addItem(_framis);
    // assertEquals(_bag.getNbrItems(), STARTING_COUNT + 1);
    // assertEquals(_bag.getItem(STARTING_COUNT), _framis);
    //
    // // ERROR Get invalid position; method should handle it
    // assertNull(_bag.getItem(-1));
    // assertNull(_bag.getItem(STARTING_COUNT + 2));
    // }

    /**
     * Retrieve (by copy) Items from the inventory by name
     * 
     * @Normal Inventory.getItem(String name) ok
     * @Error Inventory.getItem(String name) ok
     * @Null Inventory.getItem(String name) ok
     */
    public void testGetItemByName()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.msgln("\ntestGetItemByName():");
        String[] goodTarget = {
                "Pair of Boots",         // exists as is
                "Belt pouch, small",                // checks for multiword
                "pair of boots",                    // lowercase check; uppercase present
                "gold pieces"                                   // lowercase match
        };

        String[] badTarget = {
                "shield",                               // not there
                "plate mail",                           // not there
                "pouch",                               // checks for partial item name
                "gold",                       // partial match
        };

        // NORMAL Confirm that all expected items are there...
        for (int k = 0; k < goodTarget.length; k++) {
            Item thing = _bag.getItem(goodTarget[k]);
            assertNotNull(thing);
            MsgCtrl.msg("\t \"" + goodTarget[k] + "\"  sought");
            MsgCtrl.msgln("\t \"" + thing.getName() + " \" retrieved");
            assertTrue(thing.getName().equalsIgnoreCase(goodTarget[k]));
        }

        // ERROR Confirm that some Items are not found because they are missing
        // or
        // the name is a partial match
        for (int k = 0; k < badTarget.length; k++) {
            MsgCtrl.msg("\t \"" + badTarget[k] + "\"  sought");
            Item thing = _bag.getItem(badTarget[k]);
            assertNull(thing);
            MsgCtrl.msgln("\t not found");
        }

        // NULL Retrieving an Item by null gets a null
        assertNull(_bag.getItem(null));
    }

    // /** Retrieve a list of Items of the same category
    // *
    // * @Normal Inventory.getItemByCategory(ItemCategory category)
    // * @Error Inventory.getItemByCategory(ItemCategory category) N/A compile
    // error
    // * @Null Inventory.getItemByCategory(ItemCategory category) N/A compile
    // error
    // */
    // public void testGetItemByCategory()
    // {
    // MsgCtrl.auditMsgsOn(false);
    // MsgCtrl.msgln("\ntestGetItemBCategory():" );
    // int[] counts = {0, 2, 9, 2, 0, 1};
    // String[] cats = {"ARMOR", "CASH", "GENERAL", "PROVISION",
    // "VALUEABLE", "WEAPON"};
    // final String firstGen = "Backpack";
    // final String lastGen = "Torches";
    //
    // // NORMAL Count each of the Items per Category in first test
    // // Use the expected values from the starting inventory
    // Inventory[] itemList = new Inventory[6];
    // for (int k=0; k < cats.length; k++) {
    // // Get the nth enum name and create the itemList from that
    // ItemCategory ic = ItemCategory.valueOf(cats[k]);
    // itemList[k] = _bag.getItemByCategory(ItemCategory.valueOf(cats[k]));
    // int nbr = itemList[k].getNbrItems();
    // MsgCtrl.msgln("\t" + nbr + " items found in category " + cats[k]);
    // assertEquals(nbr, counts[k]);
    // }
    // // Confirm that first and last item of GENERAL are correct names
    // itemList[0] = _bag.getItemByCategory(ItemCategory.GENERAL);
    // int nbr = itemList[0].getNbrItems();
    // Item firstThing = itemList[0].getItem(0);
    // Item lastThing = itemList[0].getItem(nbr-1);
    // MsgCtrl.msgln("\t First provision is " + firstThing.getName());
    // MsgCtrl.msgln("\t Last provision is " + lastThing.getName());
    // assertEquals( firstThing.getName(), firstGen);
    // assertEquals( lastThing.getName(), lastGen);
    // }

    /**
     * Test the Inventory seach method by asking for items that ARE and ARE NOT
     * listed
     * This method takes an Item name and returns a boolan
     * 
     * @Normal Inventory.hasItem(Item item) ok
     * @Error Inventory.hasItem(Item item) ok
     * @Null Inventory.hasItem(Item item) ok
     */
    public void testHasItemByItem()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.msgln("\ntestHasItemByItem():");

        // We have two tests Items. Search for them (not there), then add them
        // and
        // check that they are present, then remove them and check that they are
        // mising
        // NORMAL and ERROR cases, depending on if item is in inventory or not
        assertFalse(_bag.hasItem(_framis));
        assertFalse(_bag.hasItem(_trobe));
        _bag.addItem(_framis);
        _bag.addItem(_trobe);       // trobes are added in pairs
        _bag.addItem(_framis);
        assertTrue(_bag.hasItem(_framis));      // 2 framis in bag
        assertTrue(_bag.hasItem(_trobe));       // 2 trobes in bag
        _bag.dropItems("Framis", 1);
        _bag.dropItems("Trobe", 2);
        // There is no 1 framis left, and 0 trobes
        assertTrue(_bag.hasItem(_framis));      // 1 framis in bag
        assertFalse(_bag.hasItem(_trobe));      // 0 trobes in bag
        assertFalse(_bag.hasItem((Item) null));        // fail gracefully
    }

    /**
     * Test the Inventory seach method by asking for items that ARE and ARE NOT
     * listed
     * This method takes an Item name and returns a boolan
     * 
     * @Normal Inventory.hasItem(String target) ok
     * @Error Inventory.hasItem(String target) ok
     * @Null Inventory.hasItem(String target) ok
     */
    public void testHasItemByName()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.msgln("\ntestHasItemByName():");

        String[] target = { "Pair of Boots",			// exists as is
                "Belt pouch, small", 		        // checks for multiword
                "shield", 						        // not there
                "pair of boots", 				    // lowercase check; uppercase present
                "plate mail",					        // not there
                "pouch",                               // checks for partial item name
                "gold pieces",                       // lower case version
                "gold"                                   // partial match
        };

        // NORMAL and ERROR cases, depending on if item is in inventory or not
        assertTrue(_bag.hasItem(target[0]) == true);
        assertTrue(_bag.hasItem(target[1]) == true);
        assertTrue(_bag.hasItem(target[2]) == false);		// not in inventory
        assertTrue(_bag.hasItem(target[3]) == true);
        assertTrue(_bag.hasItem(target[4]) == false);		// not in inventory
        assertTrue(_bag.hasItem(target[5]) == false);    // partial match
        assertTrue(_bag.hasItem(target[7]) == false);     // lowercase match
        assertTrue(_bag.hasItem(target[6]) == true);    // partial match
        assertTrue(_bag.hasItem((Item) null) == false);
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
    public void testDefaultStartingInventory()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.msgln(this, "\ttestDefaultStartingInventory():");
        // Confirm that each starting Item is in the bag
        assertEquals(_bag.getNbrItems(), _mock.startSize());
        // Confirm that both the Inventory and the pre-load item String are
        // equal in number
        String[][] items = _bag.getItemList();
        assertEquals(items.length, _mock.startSize());

        // NORMAL: Confirm that each starting Item name is in the bag
        for (int k = 0; k < items.length; k++) {
            MsgCtrl.msgln("\tChecking for " + items[k][1]);
            assertTrue(_bag.hasItem(items[k][1]));
        }
        
        for (int k = 0; k < items.length; k++) {
            MsgCtrl.msgln("\tChecking quantity for " + items[k][1]);
            assertEquals(Integer.parseInt(items[k][3]), _bag.getItem(items[k][1]).getQuantity());
        }
    }

    /** Not yet implemented */
    @Test
    public void testNotImplemented()
    {
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.errMsgln("\n\nAll methods properly implemented.");
    }

    /**
     * Default constructor
     * 
     * @Not_Needed Inventory.Inventory() default constructor
     */
    @Test
    public void testNotNeeded()
    {
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.errMsgln("\nInventory methods Not Needing Tests: ");
        MsgCtrl.errMsgln("\tInventory() \t\t\t-- empty constructor");
        MsgCtrl.errMsgln("\t Inventory.getNbrItems() -- wrapper getter");
    }

}		// end of TestInventory class

