/**
 * TestFightersGuild.java
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

import chronos.pdc.buildings.FightersGuild;
import chronos.pdc.buildings.FightersGuild.MockFightersGuild;
import chronos.pdc.registry.RegistryFactory;

/**
 *    Tests the various Guild-specific methods.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0       April 16, 2013   // original <DD>
 * </DL>
 */
public class TestFightersGuild
{
    /** Test target object */
    private FightersGuild  _ftrsGuild = null;
    /** Associated mock */
    private MockFightersGuild _mock = null;
    
    /** Expected name of inn */
    private final String NAME = "Acme Supplies";
    /** Expected name of Innkeeper */
    private final String OWNER = "Ripper";
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
    private final String DEF_OPEN = "5:00 AM";
    /** Business closing hour for default Inn */
    private final String DEF_CLOSING = "4:00 PM";
    /** Name of default store name */
    private final String DEF_NAME = "Stadium";
    /** Name of default Guildmaster */
    private final String DEF_MASTER = "Aragon";

    /**
     * Creates the test Store, but many tests in this class create their own different stores
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        _ftrsGuild = new FightersGuild();
        assertNotNull(_ftrsGuild);
        _ftrsGuild.setBusinessHours(TEST_OPEN, TEST_CLOSING);
        _mock = _ftrsGuild.new MockFightersGuild();
        assertNotNull(_mock);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _ftrsGuild = null;
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
        MsgCtrl.msgln(this, "\t testFightersGuild()");
    
        // NORMAL Dump the test Inn created by setUp()
        dump(_ftrsGuild);
        // Verify the name, innkeeper, and business hours
        assertEquals(NAME, _ftrsGuild.getName());
        assertEquals(OWNER, _ftrsGuild.getMaster());
        // Verify the standard intro and descrption; there is no busy description yet
        assertEquals(INTRO, _ftrsGuild.getExteriorDescription());
        assertEquals(DESC, _ftrsGuild.getInteriorDescription());
        // Verify business hours in meridian time
        assertEquals(TEST_MEROPEN, _ftrsGuild.getOpeningTime());
        assertEquals(TEST_MERCLOSING, _ftrsGuild.getClosingTime());
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
        _ftrsGuild = null;
        _mock = null;
        
        // NORMAL Create the default Inn using the default Constructor
        _ftrsGuild = new FightersGuild();
        assertNotNull(_ftrsGuild);
        _mock = _ftrsGuild.new MockFightersGuild();
        assertNotNull(_mock);
        dump(_ftrsGuild);
        // Verify the name, innkeeper, and business hours
        assertEquals(DEF_NAME, _ftrsGuild.getName());
        assertEquals(DEF_MASTER, _ftrsGuild.getMaster());
        // Verify the standard intro and descrption; there is no busy description yet
        String intro =  _mock.getIntro();
        String desc =  _mock.getDescrption();
        assertEquals(intro, _ftrsGuild.getExteriorDescription());
        assertEquals(desc, _ftrsGuild.getInteriorDescription());
        // Verify business hours in meridian time
        assertEquals(DEF_OPEN, _ftrsGuild.getOpeningTime());
        assertEquals(DEF_CLOSING, _ftrsGuild.getClosingTime());
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
    private void dump(FightersGuild guild)
    {
        MsgCtrl.msg("\t Created: \t" + guild.getName());
        MsgCtrl.msgln("\t owned by " + guild.getMaster());
        int[] hours = guild.getBusinessHours();
        int oTime = hours[0];
        int cTime = hours[1];
        String opening = guild.getMeridianTime(oTime);
        String closing = guild.getMeridianTime(cTime);
        MsgCtrl.msgln("\t Open from " + opening + " to " + closing);
        MsgCtrl.msgln("\tENTER: \t" + guild.getExteriorDescription());
        MsgCtrl.msgln("\tLOOK:\t" + guild.getInteriorDescription());    
    }    
    
    
}       // end of TestStore class
