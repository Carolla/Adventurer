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


package test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import mylib.ApplicationException;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.civ.MiscKeys.ItemCategory;
import chronos.pdc.Item;

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
        {"CASH", "Gold pieces", "2", "15"},                 //  1.875 lb 
        {"GENERAL", "Backpack", "160", "1"},            //  10.0 lb 
        {"GENERAL", "Pair of Boots", "40", "1"},         //   2.5 lb
        {"GENERAL", "Tinderbox, Flint & Steel", "5", "1"}, //   0.3125 lb  
        {"GENERAL", "Torches", "8", "3"},                  //   0.5 lb
        {"PROVISION", "Rations", "2", "3"},                //   0.125 lb 
        {"PROVISION", "Water skein", "80", "1"},       //   5.0 lb
        {"WEAPON", "Quarterstaff", "48", "1"},            //   3.0 lb
        {"VALUEABLE", "Ruby", "4", "1"}                     //   0.25 lb
    };  
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        // Create set of Items for testing
        _itemList = new ArrayList<Item>(_itemTable.length);
        for (int k=0; k < _itemTable.length; k++) {
            ItemCategory cat = ItemCategory.valueOf(_itemTable[k][0]);
            String name =  _itemTable[k][1];
            int weight = Integer.valueOf(_itemTable[k][2]);
            int qty = Integer.valueOf(_itemTable[k][3]);
            // Finally we can create the Item object
            Item item = new Item(cat, name, weight, qty);
            assertNotNull(item);
            _itemList.add(item);
        }
        assertEquals(_itemTable.length, _itemList.size());
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _itemList = null;
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
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
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testCtor()");
        
        for(int k=0; k<_itemList.size(); k++) {
            // Get an Item to verify its values
            Item target = _itemList.get(k);
            MsgCtrl.msgln("\t" + target.toString());
            //Verify item category built correctly
            assertEquals(_itemTable[k][0], target.getCategory().toString());
            // Verify name built correctly
            assertEquals(_itemTable[k][1], target.getName());
            // Verify weight built correctly
            assertEquals(Integer.parseInt(_itemTable[k][2]), target.getWeight());
            // Verify quantity built correctly
            assertEquals(Integer.parseInt(_itemTable[k][3]), target.getQuantity());
        }
    }

    
    /** Create one of each Item and confirm 
     * @Error  null parms in ctor
     * @Error  invalid parms in ctor
     */
    @Test
    public void testCtorError()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testCtorError()");
     
        ItemCategory.valueOf(_itemTable[4][0]);
        // Error null item category 
        try {
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
        }
        // Error null item name 
        try {
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
        }
        // Error zero item weight 
        try {
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
        }
        // Error negative quantity
        try {
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
        }
        // Error fake out name length with empty string
        try {
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
        }
    }

    
    /** Create one of each Item and confirm 
     * @Normal      add and delete quantites for the Item
     * @Error  try to drop more than the Item contains
     */
    @Test
    public void testAdjustQuantity()

    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testAdjustQuantity()");
     
        // NORMAL Add and delete quantities from the item
        // Get cash
        Item item = _itemList.get(1);
        int expQty = Integer.parseInt(_itemTable[1][3]);
        MsgCtrl.msgln("\t" + item.getName() + ": qty = " + item.getQuantity());
        assertEquals(expQty, item.getQuantity());
        // Hero gets a windfall of cash
        int WINDFALL = 97;
        try {
            item.adjustQuantity(WINDFALL);
            MsgCtrl.msgln("\t" + item.getName() + ": qty = " + item.getQuantity());
            assertEquals(expQty+WINDFALL, item.getQuantity());
            // Hero loses it gamling
            int CASHGONE = -111;
            item.adjustQuantity(CASHGONE);
            MsgCtrl.msgln("\t" + item.getName() + ": qty = " + item.getQuantity());
            assertEquals(expQty+WINDFALL+CASHGONE, item.getQuantity());
    
            // Normal Check that money can go to zero but not less
            item.adjustQuantity(-1);
            MsgCtrl.msgln("\t" + item.getName() + ": qty = " + item.getQuantity());
            assertEquals(0, item.getQuantity());

            // Error Check that money can go to zero but not less
            item.adjustQuantity(-1);
            MsgCtrl.msgln("\t" + item.getName() + ": qty = " + item.getQuantity());
            assertEquals(0, item.getQuantity());
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
        }
        
    }
    
    
    /** Tests that are not implemented either because tests are not needed, or they haven't
     * been determined to be needed yet
     * @TBD    copy()                                   not sure why it is needed
     * @TBD    equals(Object)                      not sure why it is needed 
     * @Not_Needed    getCategory()                        simple wrapper                    
     * @Not_Needed    getName()                            simple wrapper
     * @Not_Needed    getQuantity()                        simple wrapper
     * @Not_Needed    getWeight()                          simple wrapper
     * @Not_Needed    packShuttle(EnumMap<ItemFields, String>)    no longer used
     * @Not_Needed    setName(String)                  simple wrapper
     * @Not_Needed    toString()                                simple diagnostic statement     
     */
    public void NotNeeded() { }

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

}
    		// end of TestItem class
