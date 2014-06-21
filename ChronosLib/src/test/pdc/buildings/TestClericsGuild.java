/**
 * TestClericsGuild.java
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
import static org.junit.Assert.assertNull;
import mylib.ApplicationException;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.buildings.ClericsGuild;
import chronos.pdc.buildings.ClericsGuild.MockClericsGuild;
import chronos.pdc.registry.NPCRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

/**
 *    Tests the various Guild-specific methods.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0       April 16, 2013   // original <DD>
 * </DL>
 */
public class TestClericsGuild
{
    /** Test target object */
    private ClericsGuild  _clericGuild = null;
    /** Associated mock */
    private MockClericsGuild _mock = null;
    
    /** Expected name of inn */
    private final String NAME = "House on Haunted Hill";
    /** Expected name of Innkeeper */
    private final String OWNER = "Ripper";
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
    private final String DEF_CLOSING = "7:00 PM";
    /** Name of default store name */
    private final String DEF_NAME = "Monastery";
    /** Name of default Guildmaster */
    private final String DEF_MASTER = "Balthazar";

    /** Close down all the secondary registries needed */
    @AfterClass
    public static void cleanUp() 
    {
        ((NPCRegistry) RegistryFactory.getRegistry(RegKey.NPC)).closeRegistry();
    }
    

    /**
     * Creates the test Store, but many tests in this class create their own different stores
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        _clericGuild = new ClericsGuild(NAME, OWNER, HOVERTEXT, INTRO, DESC);
        assertNotNull(_clericGuild);
        _clericGuild.setBusinessHours(TEST_OPEN, TEST_CLOSING);
        _mock = _clericGuild.new MockClericsGuild();
        assertNotNull(_mock);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _clericGuild = null;
        _mock = null;
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 *                  TEST METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Chronos.pdc.ClericsGuild
     * @Normal ensure that the test target has correct data
     * @throws ApplicationException if unexpected ctor error occurs 
     */
    @Test
    public void testClericsGuild() throws ApplicationException 
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testClericsGuild()");
    
        // NORMAL Dump the test Inn created by setUp()
        dump(_clericGuild);
        // Verify the name, innkeeper, and business hours
        assertEquals(NAME, _clericGuild.getName());
        assertEquals(OWNER, _clericGuild.getMaster().getName());
        // Verify the standard intro and descrption; there is no busy description yet
        assertEquals(INTRO, _clericGuild.getExteriorDescription());
        assertEquals(DESC, _clericGuild.getInteriorDescription());
        // Verify business hours in meridian time
        assertEquals(TEST_MEROPEN, _clericGuild.getOpeningTime());
        assertEquals(TEST_MERCLOSING, _clericGuild.getClosingTime());
    }    


    /** Chronos.pdc.ClericsGuild
     * @Error   trigger exception with bad business hours
     * @Error   trigger exception with building master not in NPC Registry
     * @throws ApplicationException if unexpected error occurs 
     */
    @Test
    public void testClericsGuildErrors() throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testClericsGuildErrors()");
    
        // Clear out old Inn from setUp()
        _clericGuild = null;
        _mock = null;
        
        // ERROR  Building Master does not exist in Registry
        try {
            _clericGuild = new ClericsGuild(NAME, "Unregistered Owner", HOVERTEXT, INTRO, DESC);
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\tExpected exception: " + ex.getMessage());
        }
        assertNull(_clericGuild);
    }    

    
    /** Chronos.pdc.ClericsGuild
     * @Normal ensure that the default Inn has correct data
     * @throws ApplicationException if unexpected ctor error occurs
     */
    @Test
    public void testDefaultClericsGuild()  throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testDefaultClericsGuild()");

        // Clear out existing ClericsGuild from setUp()
        _clericGuild = null;
        _mock = null;
        
        // NORMAL Create the default Inn using the default Constructor
        _clericGuild = new ClericsGuild();
        assertNotNull(_clericGuild);
        _mock = _clericGuild.new MockClericsGuild();
        assertNotNull(_mock);
        dump(_clericGuild);
        // Verify the name, innkeeper, and business hours
        assertEquals(DEF_NAME, _clericGuild.getName());
        assertEquals(DEF_MASTER, _clericGuild.getMaster().getName());
        // Verify the standard intro and descrption; there is no busy description yet
        String intro =  _mock.getIntro();
        String desc =  _mock.getDescrption();
        assertEquals(intro, _clericGuild.getExteriorDescription());
        assertEquals(desc, _clericGuild.getInteriorDescription());
        // Verify business hours in meridian time
        assertEquals(DEF_OPEN, _clericGuild.getOpeningTime());
        assertEquals(DEF_CLOSING, _clericGuild.getClosingTime());
    }    

    
    /** Tests that are not implemented either because tests are not needed
     *  @Not_Needed getKey()                            // getter
     */
    public void NotNeeded() { }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Dump the contents of the Building
     * @param guild   the building to display 
     */
    private void dump(ClericsGuild guild)
    {
        MsgCtrl.msg("\t Created: \t" + guild.getName());
        MsgCtrl.msgln("\t owned by " + guild.getMaster().getName());
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
