/**
 * TestRoguesGuild.java
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

import chronos.pdc.buildings.RoguesGuild;
import chronos.pdc.buildings.RoguesGuild.MockRoguesGuild;
import chronos.pdc.registry.RegistryFactory;

/**
 *    Tests the various Guild-specific methods.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0       April 16, 2013   // original <DD>
 * </DL>
 */
public class TestRoguesGuild
{
    /** Test target object */
    private RoguesGuild  _thfGuild = null;
    /** Associated mock */
    private MockRoguesGuild _mock = null;
    
    /** Expected name of inn */
    private final String NAME = "Naughty Bawdy Bar";
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
    private final String DEF_OPEN = "9:00 PM";
    /** Business closing hour for default Inn */
    private final String DEF_CLOSING = "4:00 AM";  // the next day
    /** Name of default store name */
    private final String DEF_NAME = "Rogues' Den";
    /** Name of default Guildmaster */
    private final String DEF_MASTER = "Ripper";
    

    /** Close down all the secondary registries needed */
    @AfterClass
    public static void cleanUp() 
    {
      RegistryFactory.getInstance().closeAllRegistries();
    }
    

    /**
     * Creates the test RoguesGuild, but many tests in this class create their own different stores
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        _thfGuild = new RoguesGuild(NAME, OWNER, HOVERTEXT, INTRO,  DESC);
        assertNotNull(_thfGuild);
        _thfGuild.setBusinessHours(TEST_OPEN, TEST_CLOSING);
        _mock = _thfGuild.new MockRoguesGuild();
        assertNotNull(_mock);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _thfGuild = null;
        _mock = null;
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 *                  TEST METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Chronos.pdc.RoguesGuild
     * @Normal ensure that the test target has correct data
     * @throws ApplicationException if unexpected ctor error occurs 
     */
    @Test
    public void testRoguesGuild() throws ApplicationException 
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testRoguesGuild()");
    
        // NORMAL Dump the test Inn created by setUp()
        dump(_thfGuild);
        // Verify the name, innkeeper, and business hours
        assertEquals(NAME, _thfGuild.getName());
        assertEquals(OWNER, _thfGuild.getMaster().getName());
        // Verify the standard intro and descrption; there is no busy description yet
        assertEquals(HOVERTEXT, INTRO,  _thfGuild.getExteriorDescription());
        assertEquals(DESC, _thfGuild.getInteriorDescription());
        // Verify business hours in meridian time
        assertEquals(TEST_MEROPEN, _thfGuild.getOpeningTime());
        assertEquals(TEST_MERCLOSING, _thfGuild.getClosingTime());
    }    


    /** Chronos.pdc.RoguesGuild
     * @Error   trigger exception with bad business hours
     * @Error   trigger exception with building master not in NPC Registry
     * @throws ApplicationException if unexpected error occurs 
     */
    @Test
    public void testRoguesGuildErrors() throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testRoguesGuildErrors()");
    
        // Clear out old Inn from setUp()
        _thfGuild = null;
        _mock = null;
        
        // ERROR  Building Master does not exist in Registry
        try {
            _thfGuild = new RoguesGuild(NAME, "Unregistered Owner", HOVERTEXT, INTRO,  DESC);
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\tExpected exception: " + ex.getMessage());
        }
        assertNull(_thfGuild);
    }    

    
    /** Chronos.pdc.RoguesGuild
     * @Normal ensure that the default Inn has correct data
     * @throws ApplicationException if unexpected ctor error occurs
     */
    @Test
    public void testDefaultRoguesGuild()  throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testDefaultRoguesGuild()");

        // Clear out existing RoguesGuild from setUp()
        _thfGuild = null;
        _mock = null;
        
        // NORMAL Create the default Inn using the default Constructor
        _thfGuild = new RoguesGuild();
        assertNotNull(_thfGuild);
        _mock = _thfGuild.new MockRoguesGuild();
        assertNotNull(_mock);
        dump(_thfGuild);
        // Verify the name, innkeeper, and business hours
        assertEquals(DEF_NAME, _thfGuild.getName());
        assertEquals(DEF_MASTER, _thfGuild.getMaster().getName());
        // Verify the standard intro and descrption; there is no busy description yet
        String intro =  _mock.getIntro();
        String desc =  _mock.getDescrption();
        assertEquals(intro, _thfGuild.getExteriorDescription());
        assertEquals(desc, _thfGuild.getInteriorDescription());
        // Verify business hours in meridian time
        assertEquals(DEF_OPEN, _thfGuild.getOpeningTime());
        assertEquals(DEF_CLOSING, _thfGuild.getClosingTime());
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
    private void dump(RoguesGuild guild)
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
    
    
}       // end of TestRoguesGuild class
