/**
 * TestInn.java
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

import java.util.ArrayList;
import java.util.List;

import mylib.ApplicationException;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import test.pdc.command.FakeScheduler;
import chronos.pdc.NPC;
import chronos.pdc.Command.Scheduler;
import chronos.pdc.buildings.Inn;
import chronos.pdc.buildings.Inn.MockInn;
import chronos.pdc.registry.NPCRegistry;
import chronos.pdc.registry.RegistryFactory;

/**
 *    Verify that the Inn exists as a meeting place for Heroes, allows conversation with the
 *    Innkeeper andPatrons, provides sleeping and eating behavior, and can ban Heroes for
 *    certain amounts of time.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0       Jan 28, 2013   // original <DD>
 * </DL>
 */
public class TestInn
{
    /** Test target object */
    private Inn _inn = null;
    /** Associated mock */
    private MockInn _mock = null;
    
    /** Expected name of inn */
    private final String NAME = "Testy Tavern";
    /** Expected name of Innkeeper */
    private final String INNKEEPER = "Bork";
    /** Expected hovertext */
    private final String HOVERTEXT = "Click and see!";
    /** Introduction */
    private final String INTRO = "This is the hover text for the building icon";
    /** Description */
    private final String DESC = "This is the standard description when the Hero LOOKs, " +
                    "which is also called automatically when the Hero ENTERs.";
    /** Busy Description */
    private final String BUSY_DESC = "This is additional text, or alternate text, " +
                    "when there are many patrons in the Inn, and the Innkeeper cannot talk to the Hero.";
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
    private final String DEF_CLOSING = "Midnight";
    
    private Scheduler _fakeScheduler = new FakeScheduler();
    private NPCRegistry fakeNpcRegistry = new FakeNpcRegistry();
    
    public class FakeNpcRegistry extends NPCRegistry
    {
        @Override
        public List<NPC> getNPCList()
        {
            ArrayList<NPC> list = new ArrayList<NPC>();
            list.add(new NPC());
            list.add(new NPC());
            return list; 
        }
    }

    /** Close down all the secondary registries needed for the Inn */
    @AfterClass
    public static void cleanUp() 
    {
      RegistryFactory.getInstance().closeAllRegistries();
    }
    

    /**
     * Creates the test Inn, but many tests in this class create their own different Inns
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        _inn = new Inn(_fakeScheduler, fakeNpcRegistry);
        assertNotNull(_inn);
        _inn.setBusinessHours(TEST_OPEN, TEST_CLOSING);
        _mock = _inn.new MockInn();
        assertNotNull(_mock);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _inn = null;
        _mock = null;
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 *                  TEST METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Chronos.pdc.ItemRegistry
     * @Normal ensure that the test inn has correct data 
     */
    @Test
    public void testInn() 
    {
    }    

    
    /** Chronos.pdc.Inn
     * @Normal ensure that the default Inn has correct data
     * @throws ApplicationException if unexpected ctor error occurs
     */
    @Test
    public void testDefaultInn()  throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testDefaultInn()");

        assertNotNull(_inn);
        _mock = _inn.new MockInn();
        assertNotNull(_mock);

        // Verify the name, innkeeper, and business hours
        assertEquals(_mock.getName(), _inn.getName());
//        assertEquals(_mock.getBuildingMaster().getName(), _inn.getMaster().getName());
        // Verify the standard intro and descrption; there is no busy description yet
        String[] s = _mock.getDescs();
        assertEquals(s[0], _inn.getExteriorDescription());
        assertEquals(s[1], _inn.getInteriorDescription());
        // Verify business hours in meridian time
        assertEquals(DEF_OPEN, _inn.getOpeningTime());
        assertEquals(DEF_CLOSING, _inn.getClosingTime());

        // NORMAL Verify busy hours
        _mock.setCurrentPatrons(6);
        
        // Verify the standard intro and busy descrption; there is no standard description now
        s = _mock.getDescs();
        assertEquals(s[0], _inn.getExteriorDescription());
        assertEquals(s[2], _inn.getInteriorDescription());
    }    


    /** Chronos.pdc.Inn
     * @Normal check that the proper descriptions are displayed, depending on the current
     * number of patrons (busy o rnot busy description).
     */
    @Test
    public void testEnter()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testEnter()");
        
        // Non-busy description
        _mock.setCurrentPatrons(1);
        String testDesc = _inn.getInteriorDescription();
        MsgCtrl.msgln("\t" + testDesc);    
        assertEquals(DESC, testDesc);
        // Busy description
        _mock.setDesc(BUSY_DESC);
        _mock.setCurrentPatrons(6);
        testDesc = _inn.getInteriorDescription();
        MsgCtrl.msgln("\t" + testDesc);    
        assertEquals(BUSY_DESC, testDesc);
        // Again with 3 patrons for non busy desc
        _mock.setDesc(DESC);
        _mock.setCurrentPatrons(3);
        testDesc = _inn.getInteriorDescription();
        MsgCtrl.msgln("\t" + testDesc);    
        assertEquals(DESC, testDesc);
    }

    

    
    /** Tests that are not implemented either because tests are not needed, or they haven't
     * been determined to be needed yet
     *  @Not_Needed getKey()                            // getter
     *  @Not_Needed getBusinessHours()          // getter
     *  @Not_Needed getOpeningTime()            // wrapper
     *  @Not_Needed getClosingTime()             // setter
     */
    public void NotNeeded() { }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

//    /** Dump the contents of the Inn
//     * @param inn   the inn to display 
//     */
//    private void dump(Inn myInn)
//    {
//        MsgCtrl.msg("\t Created: \t" + myInn.getName());
//        MsgCtrl.msgln("\t owned by " + myInn.getMaster().getName());
//        int[] hours = myInn.getBusinessHours();
//        int oTime = hours[0];
//        int cTime = hours[1];
//        String opening = myInn.getMeridianTime(oTime);
//        String closing = myInn.getMeridianTime(cTime);
//        MsgCtrl.msgln("\t Open from " + opening + " to " + closing);
//        MsgCtrl.msgln("\tENTER: \t" + myInn.getExteriorDescription());
//        MsgCtrl.msgln("\tLOOK:\t" + myInn.getInteriorDescription());    
//        MsgCtrl.msgln("\tBUSY:\t" + myInn.getInteriorDescription());    
//    }        
}       // end of TestInn class
