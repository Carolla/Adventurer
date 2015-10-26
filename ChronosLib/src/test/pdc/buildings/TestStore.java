/**
 * TestStore.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package test.pdc.buildings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import mylib.ApplicationException;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.buildings.Store;
import chronos.pdc.buildings.Store.MockStore;
import chronos.pdc.registry.RegistryFactory;

/**
 *    Tests the various store-specific methods.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0       April 16, 2013   // original <DD>
 * </DL>
 */
public class TestStore
{
    /** Test target object */
    private Store _store = null;
    /** Associated mock */
    private MockStore _mock = null;
    
    /** Expected name of inn */
    private final String NAME = "Acme Supplies";
    /** Expected name of Innkeeper */
    private final String OWNER = "Dewey N. Howe";
    /** Expected hovertext */
    private final String HOVERTEXT = "Click and see!";
    /** Introduction */
    private final String INTRO = "This is the hover text for the building icon";
    /** Description */
    private final String DESC = "This is the standard description when the Hero LOOKs, " +
                    "which is also called automatically when the Hero ENTERs.";
    /** Business opening hour for test Inn */
    private final int TEST_OPEN = 1000;
    /** Business closing hour for test Inn */
    private final int TEST_CLOSING = 1200;
    /** Business opening hour for test Inn */
    private final String TEST_MEROPEN = "10:00 AM";
    /** Business closing hour for default Inn */
    private final String TEST_MERCLOSING = "Noon";
    
    /** Business opening hour for default Inn */
    private final String DEF_OPEN = "6:00 AM";
    /** Business closing hour for default Inn */
    private final String DEF_CLOSING = "6:00 PM";
    /** Name of default store name */
    private final String DEF_NAME = "Rat's Pack";
   

    /**
     * Creates the test Store, but many tests in this class create their own different stores
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        _store = new Store();
        assertNotNull(_store);
        _store.setBusinessHours(TEST_OPEN, TEST_CLOSING);
        _mock = _store.new MockStore();
        assertNotNull(_mock);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _store = null;
        _mock = null;
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 *                  TEST METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Chronos.pdc.Store
     * @Normal ensure that the test target has correct data
     * @throws ApplicationException if unexpected ctor error occurs 
     */
    @Test
    public void testStore() throws ApplicationException 
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testInn()");
    
        // NORMAL Dump the test Inn created by setUp()
        dump(_store);
        // Verify the name, innkeeper, and business hours
        assertEquals(NAME, _store.getName());
        assertEquals(OWNER, _store.getMaster());
        // Verify the standard intro and descrption; there is no busy description yet
        assertEquals(HOVERTEXT, INTRO,  _store.getExteriorDescription());
        assertEquals(DESC, _store.getInteriorDescription());
        // Verify business hours in meridian time
        assertEquals(TEST_MEROPEN, _store.getOpeningTime());
        assertEquals(TEST_MERCLOSING, _store.getClosingTime());
    }    

    
    /** Chronos.pdc.Store
     * @Normal ensure that the default Inn has correct data
     * @throws ApplicationException if unexpected ctor error occurs
     */
    @Test
    public void testDefaultStore()  throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testDefaultStore()");

        // Clear out existing Store from setUp()
        _store = null;
        _mock = null;
        
        // NORMAL Create the default Inn using the default Constructor
        _store = new Store();
        assertNotNull(_store);
        _mock = _store.new MockStore();
        assertNotNull(_mock);
        dump(_store);
        // Verify the name, innkeeper, and business hours
        assertEquals(DEF_NAME, _store.getName());
        assertEquals(OWNER, _store.getMaster());
        // Verify the standard intro and descrption; there is no busy description yet
        String intro =  _mock.getIntro();
        String desc =  _mock.getDescrption();
        assertEquals(intro, _store.getExteriorDescription());
        assertEquals(desc, _store.getInteriorDescription());
        // Verify business hours in meridian time
        assertEquals(DEF_OPEN, _store.getOpeningTime());
        assertEquals(DEF_CLOSING, _store.getClosingTime());
    }    

    
    /** Tests that are not implemented either because tests are not needed
     *  @Not_Needed getKey()                            // getter
     */
    public void NotNeeded() { }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Dump the contents of the Building
     * @param myStore   the building to display 
     */
    private void dump(Store myStore)
    {
        MsgCtrl.msg("\t Created: \t" + myStore.getName());
        MsgCtrl.msgln("\t owned by " + myStore.getMaster());
        int[] hours = myStore.getBusinessHours();
        int oTime = hours[0];
        int cTime = hours[1];
        String opening = myStore.getMeridianTime(oTime);
        String closing = myStore.getMeridianTime(cTime);
        MsgCtrl.msgln("\t Open from " + opening + " to " + closing);
        MsgCtrl.msgln("\tENTER: \t" + myStore.getExteriorDescription());
        MsgCtrl.msgln("\tLOOK:\t" + myStore.getInteriorDescription());    
    }    
    
    
}       // end of TestStore class
