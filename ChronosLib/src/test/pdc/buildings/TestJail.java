/**
 * TestJail.java
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

import chronos.pdc.buildings.Jail;
import chronos.pdc.buildings.Jail.MockJail;
import chronos.pdc.registry.NPCRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

/**
 *    Test the Jail methods
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Apr 8, 2013   // original <DD>
 * </DL>
 */
public class TestJail
{
    /** Test target object */
    private Jail _jail = null;
    /** Associated mock */
    private MockJail _mock = null;
    
    /** Expected name of inn */
    private final String NAME = "Test Jail";
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
    /** Message for the test bank if the Hero tries to enter during closed hours */
    private final String CLOSED_MSG = "Sorry, the Test Jail is not open now. " +
                    "Return during normal business hours between 10:00 AM and Noon.";    
    
    /** Business opening hour for default Inn */
    private final String DEF_OPEN = "10:00 AM";
    /** Business closing hour for default Inn */
    private final String DEF_CLOSING = "6:00 PM";
    /** Keeper of the Jail */
    private final String DEF_OWNER = "The Sheriff";

    @AfterClass
    public static void cleanUp() 
    {
        ((NPCRegistry) RegistryFactory.getInstance().getRegistry(RegKey.NPC)).closeRegistry();
    }
    
    /**
     * Creates the test Jail, but many tests in this class create their own different banks
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        _jail = new Jail(NAME, OWNER, HOVERTEXT, INTRO,  DESC);
        assertNotNull(_jail);
        _jail.setBusinessHours(TEST_OPEN, TEST_CLOSING);
        _mock = _jail.new MockJail();
        assertNotNull(_mock);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _jail = null;
        _mock = null;
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 *                  TEST METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Chronos.pdc.Jail
     * @Normal ensure that the test Jail has correct data
     * @throws ApplicationException if unexpected ctor error occurs 
     */
    @Test
    public void testJail() throws ApplicationException 
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testJail()");
    
        // NORMAL Dump the test Inn created by setUp()
        dump(_jail);
        // Verify the name, innkeeper, and business hours
        assertEquals(NAME, _jail.getName());
        assertEquals(OWNER,  _jail.getMaster().getName());
        // Verify the standard intro and descrption; there is no busy description yet
        assertEquals(HOVERTEXT, INTRO,  _jail.getExteriorDescription());
        assertEquals(DESC, _jail.getInteriorDescription());
        // Verify business hours in meridian time
        assertEquals(TEST_MEROPEN, _jail.getOpeningTime());
        assertEquals(TEST_MERCLOSING, _jail.getClosingTime());
    }    

    
    /** Chronos.pdc.Jail
     * @Error   trigger exception with building master not in NPC Registry
     * @throws ApplicationException if unexpected error occurs 
     */
    @Test
    public void testJailErrors() throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testJailErrors()");
    
        // Clear out old Inn from setUp()
        _jail = null;
        _mock = null;
        
        // ERROR  Building Master does not exist in Registry
        try {
            _jail = new Jail(NAME, "Unregistered Owner", HOVERTEXT, INTRO,  DESC);
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\tExpected exception: " + ex.getMessage());
        }
        assertNull(_jail);
    }    


    /** Chronos.pdc.Jail
     * @Normal ensure that the default Jail has correct data
     * @throws ApplicationException if unexpected ctor error occurs
     */
    @Test
    public void testDefaultJail()  throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testDefaultJail()");

        // Clear out old Inn from setUp()
        _jail = null;
        _mock = null;
        
        // NORMAL Create the default Inn using the default Constructor
        _jail = new Jail();
        assertNotNull(_jail);
        _mock = _jail.new MockJail();
        assertNotNull(_mock);
        dump(_jail);
        // Verify the name, innkeeper, and business hours
        assertEquals(_mock.getName(), _jail.getName());
        assertEquals(DEF_OWNER, _jail.getMaster().getName());
        // Verify the standard intro and descrption; there is no busy description yet
        String[] s = _mock.getDescs();
        assertEquals(s[0], _jail.getExteriorDescription());
        assertEquals(s[1], _jail.getInteriorDescription());
        // Verify business hours in meridian time
        assertEquals(DEF_OPEN, _jail.getOpeningTime());
        assertEquals(DEF_CLOSING, _jail.getClosingTime());
    }    

    
    /** Chronos.pdc.Inn
     * @Normal check that the Hero can only enter during banking hours
     * @Errpr   closed building message if bank is closed
     */
    @Test
    public void testEnter()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testEnter()");
        
        // Normal   enter during business hours, which are from 9am to 3pm (0900 - 1500)
        int tod = 1100;
        String resp =  _jail.enter(tod);
        MsgCtrl.msgln("\t Entering at " + _jail.getMeridianTime(tod) + ":\t" + resp);    
        assertEquals(DESC, resp);

        // Error    enter at closing time (for the test bank)
        tod = 900;
        resp =  _jail.enter(tod);
        MsgCtrl.msgln("\t Entering at " + _jail.getMeridianTime(tod) + ":\t" + resp);    
        assertEquals(CLOSED_MSG, resp);
    }

    
    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    /** Dump the contents of the Building
     * @param myJail    the bank to display 
     */
    private void dump(Jail myJail)
    {
        MsgCtrl.msg("\t Created: \t" + myJail.getName());
        MsgCtrl.msgln("\t owned by " + myJail.getMaster().getName());
        int[] hours = myJail.getBusinessHours();
        int oTime = hours[0];
        int cTime = hours[1];
        String opening = myJail.getMeridianTime(oTime);
        String closing = myJail.getMeridianTime(cTime);
        MsgCtrl.msgln("\t Open from " + opening + " to " + closing);
        MsgCtrl.msgln("\tENTER: \t" + myJail.getExteriorDescription());
        MsgCtrl.msgln("\tLOOK:\t" + myJail.getInteriorDescription());    
    }    


}           // end of TestJail class
