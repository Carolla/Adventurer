/**
 * TestDataShuttle.java
 * Copyright (c) 2011, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package mylib.test.civ;


import junit.framework.TestCase;
import mylib.Constants;
import mylib.MsgCtrl;
import mylib.civ.DataShuttle;
import mylib.civ.DataShuttle.ErrorType;

import org.junit.After;
import org.junit.Before;

/**
 *  Tests the DataShuttle, implemented as a EnumMap
 *  
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Jul 3, 2011   // original <DD>
 * </DL>
 */
public class TestDataShuttle extends TestCase
{
    // Target shuttle for testing
    DataShuttle<Items> _ds = null;

    /** Enum of various category of Inventory items */
    public enum Items
    {
        ARMOR, CASH, GENERAL, PROVISION, VALUEABLE, WEAPON;
    };

    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        // Audit messages are OFF at start of each test 
        MsgCtrl.auditMsgsOn(false);
        // Error messages are ON at start of each test 
        MsgCtrl.errorMsgsOn(true);
        // Create empty shuttle mappedto Items test enum
        _ds = new DataShuttle<Items>(Items.class);
        assertNotNull(_ds);
        assertEquals(_ds.size(), 0);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _ds = null;
        // Audit messages are OFF after each test
        MsgCtrl.auditMsgsOn(false);
    }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Verify that the target shuttle was properly created in the constructor 
     * @Normal  DataShuttle(Class<E> enumClassname) check creation and error flags
     * @Null      DataShuttle(Class<E> enumClassname)  invalid null key 
     */
    public void testDataShuttle()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "testDataShuttle()");

        // NORMAL: Check that an empty shuttle returns the right (non)-error flags
        assertNotNull(_ds);
        assertEquals(_ds.size(), 0);
        assertEquals(_ds.getErrorType(), ErrorType.OK);
        assertNull(_ds.getErrorSource());
        assertNull(_ds.getErrorMessage());
        
        // Check that I can actually add something; check against size
        assertEquals(_ds.putField(Items.CASH, "12gp"), 1);
        assertEquals(_ds.putField(Items.ARMOR, "Chain mail"), 2);
        assertEquals(_ds.getField(Items.CASH), "12gp");
        String s = (String) _ds.getField(Items.GENERAL);
        assertNull(s);
        // Getting values does not change shuttle size
        assertEquals(_ds.size(), 2);
        
    }


    /** Verify that the target shuttle was properly created in the constructor 
     * @Normal  DataShuttle.getField(E key) various field values put, get, and checked
     * @Error   DataShuttle.getField(E key)           n/a
     * @Null  DataShuttle.getField(E key)           null values
     */
    public void testGetField()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "testGetField()");
        
        // NORMAL: Try various keys and and sizes
        assertEquals(_ds.putField(Items.CASH, "100gp"), 1);
        assertEquals(_ds.getField(Items.CASH), "100gp");
        assertEquals(_ds.size(), 1);
        
        assertEquals(_ds.putField(Items.ARMOR, "leather"), 2);
        assertEquals(_ds.getField(Items.CASH), "100gp");
        assertEquals(_ds.getField(Items.ARMOR), "leather");
        assertEquals(_ds.size(), 2);
        
        // NULL values do not change the shuttle size
        assertEquals(_ds.putField(Items.GENERAL, "stuff"), 3);
        assertEquals(_ds.putField(Items.CASH, null), 3);
        assertEquals(_ds.size(), 3);
    }
    

    /** Verify that the target shuttle was properly created in the constructor 
     * @Normal  DataShuttle.putField(E key, String value)  various field values put, get, and checked
     * @Error      DataShuttle.putField(E key, String value)   exception handling checked  
     * @Null        DataShuttle.putField(E key, String value)    null values and error flags checked 
     */
    public void testPutField()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "testPutField()");
        
        // NORMAL:  Set null values and check size
        assertEquals(_ds.putField(Items.CASH, null), 1);
        assertEquals(_ds.putField(Items.ARMOR, null), 2);
        assertEquals(_ds.size(),  2);
    
        // NULL: Attempt to set null values into the keys
        assertEquals(_ds.putField(null, "12gp"), Constants.ERROR);
        assertEquals(_ds.getErrorType(), ErrorType.NULL_KEY);
        assertEquals(_ds.getErrorSource(), ErrorType.DATA_SHUTTLE);
        assertEquals(_ds.size(),  2);
    }

    
    
    /** Tests that are not needed for various reasons, mostly setters and getters
     * @Not_Needed DataShuttle.assignKey(E key)                     setter
     * @Not_Needed DataShuttle.clear()                                      wrapper
     * @Not_Needed DataShuttle.clearErrors()                            wrapper
     * @Not_Needed DataShuttle.getErrorMessage()                  getter
     * @Not_Needed DataShuttle.getErrorSource()                     getter
     * @Not_Needed DataShuttle.getErrorType()                         getter
     * @Not_Needed DataShuttle.setErrorMessage()                  setter
     * @Not_Needed DataShuttle.setErrorSource()                     setter
     * @Not_Needed DataShuttle.setErrorType()                         setter
     * @Not_Needed DataShuttle.size()                                        wrapper
     * 
     */
    public void notNeeded() { }
    
    
    
}           // end of TestDataShuttle class
