/**
 * TestBank.java
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

import chronos.pdc.buildings.Bank;
import chronos.pdc.buildings.Bank.MockBank;
import chronos.pdc.registry.NPCRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

/**
 *    Test the Bank methods
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Apr 8, 2013   // original <DD>
 * </DL>
 */
public class TestBank
{
    /** Test target object */
    private Bank _bank = null;
    /** Associated mock */
    private MockBank _mock = null;
    
    /** Expected name of inn */
    private final String NAME = "Ready-Cache Bank";
    /** Expected hovertext */
    private final String HOVERTEXT = "Click and see!";
    /** Expected name of Innkeeper */
    private final String OWNER = "J.P. Pennypacker";
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
    private final String CLOSED_MSG = "Sorry, the Ready-Cache Bank is not open now. " +
                    "Return during normal business hours between 10:00 AM and Noon.";    
    
    /** Business opening hour for default Inn */
    private final String DEF_OPEN = "9:00 AM";
    /** Business closing hour for default Inn */
    private final String DEF_CLOSING = "3:00 PM";

    @AfterClass
    public static void cleanUp()
    {
        ((NPCRegistry) RegistryFactory.getInstance().getRegistry(RegKey.NPC)).closeRegistry();
    }
    
    /**
     * Creates the test Bank, but many tests in this class create their own different banks
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        _bank = new Bank(NAME, OWNER, HOVERTEXT, INTRO, DESC);
        assertNotNull(_bank);
        _bank.setBusinessHours(TEST_OPEN, TEST_CLOSING);
        _mock = _bank.new MockBank();
        assertNotNull(_mock);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _bank = null;
        _mock = null;
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 *                  TEST METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Chronos.pdc.Bank
     * @Normal ensure that the test Bank has correct data
     * @throws ApplicationException if unexpected ctor error occurs 
     */
    @Test
    public void testBank() throws ApplicationException 
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testBank()");
    
        // NORMAL Dump the test Inn created by setUp()
        dump(_bank);
        // Verify the name, innkeeper, and business hours
        assertEquals(NAME, _bank.getName());
        assertEquals(OWNER,  _bank.getMaster().getName());
        // Verify the standard intro and descrption; there is no busy description yet
        assertEquals(INTRO, _bank.getExteriorDescription());
        assertEquals(DESC, _bank.getInteriorDescription());
        // Verify business hours in meridian time
        assertEquals(TEST_MEROPEN, _bank.getOpeningTime());
        assertEquals(TEST_MERCLOSING, _bank.getClosingTime());
    }    

    
    /** Chronos.pdc.Bank
     * @Error   trigger exception with building master not in NPC Registry
     * @throws ApplicationException if unexpected error occurs 
     */
    @Test
    public void testBankErrors() throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testBankErrors()");
    
        // Clear out old Inn from setUp()
        _bank = null;
        _mock = null;
        
        // ERROR  Building Master does not exist in Registry
        try {
            _bank = new Bank(NAME, "Unregistered Owner", HOVERTEXT, INTRO, DESC);
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\tExpected exception: " + ex.getMessage());
        }
        assertNull(_bank);
    }    


    /** Chronos.pdc.Bank
     * @Normal ensure that the default Bank has correct data
     * @throws ApplicationException if unexpected ctor error occurs
     */
    @Test
    public void testDefaultBank()  throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testDefaultBank()");

        // Clear out old Inn from setUp()
        _bank = null;
        _mock = null;
        
        // NORMAL Create the default Inn using the default Constructor
        _bank = new Bank();
        assertNotNull(_bank);
        _mock = _bank.new MockBank();
        assertNotNull(_mock);
        dump(_bank);
        // Verify the name, innkeeper, and business hours
        assertEquals(_mock.getName(), _bank.getName());
        assertEquals("J.P. Pennypacker", _bank.getMaster().getName());
        // Verify the standard intro and descrption; there is no busy description yet
        String[] s = _mock.getDescs();
        assertEquals(s[0], _bank.getExteriorDescription());
        assertEquals(s[1], _bank.getInteriorDescription());
        // Verify business hours in meridian time
        assertEquals(DEF_OPEN, _bank.getOpeningTime());
        assertEquals(DEF_CLOSING, _bank.getClosingTime());
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
        String resp =  _bank.enter(tod);
        MsgCtrl.msgln("\t Entering at " + _bank.getMeridianTime(tod) + ":\t" + resp);    
        assertEquals(DESC, resp);

        // Error    enter at closing time (for the test bank)
        tod = 1200;
        resp =  _bank.enter(tod);
        MsgCtrl.msgln("\t Entering at " + _bank.getMeridianTime(tod) + ":\t" + resp);    
        assertEquals(CLOSED_MSG, resp);
    }

    
    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    /** Dump the contents of the Building
     * @param myBank    the bank to display 
     */
    private void dump(Bank myBank)
    {
        MsgCtrl.msg("\t Created: \t" + myBank.getName());
        MsgCtrl.msgln("\t owned by " + myBank.getMaster().getName());
        int[] hours = myBank.getBusinessHours();
        int oTime = hours[0];
        int cTime = hours[1];
        String opening = myBank.getMeridianTime(oTime);
        String closing = myBank.getMeridianTime(cTime);
        MsgCtrl.msgln("\t Open from " + opening + " to " + closing);
        MsgCtrl.msgln("\tENTER: \t" + myBank.getExteriorDescription());
        MsgCtrl.msgln("\tLOOK:\t" + myBank.getInteriorDescription());    
    }    


}           // end of TestBank class
