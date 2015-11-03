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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import mylib.ApplicationException;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.pdc.command.FakeScheduler;
import chronos.pdc.NPC;
import chronos.pdc.Command.Scheduler;
import chronos.pdc.buildings.Inn;
import chronos.pdc.registry.NPCRegistry;

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
    
    /** Business opening hour for test Inn */
    private final int TEST_OPEN = 1000;
    /** Business closing hour for test Inn */
    private final int TEST_CLOSING = 1200;
    
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
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _inn = null;
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
    public void InnDoesntGetPatronsWithoutInit() 
    {
        assertTrue(_inn.getPatrons().size() == 0);
    }    

    
    /** Chronos.pdc.Inn
     * @Normal ensure that the default Inn has correct data
     * @throws ApplicationException if unexpected ctor error occurs
     */
    @Test
    public void testDefaultInn()  throws ApplicationException
    {
        MsgCtrl.msgln(this, "\t testDefaultInn()");

    }    


    /** Chronos.pdc.Inn
     * @Normal check that the proper descriptions are displayed, depending on the current
     * number of patrons (busy o rnot busy description).
     */
    @Test
    public void testEnter()
    {

    }

    

    
    /** Tests that are not implemented either because tests are not needed, or they haven't
     * been determined to be needed yet
     *  @Not_Needed getKey()                            // getter
     *  @Not_Needed getBusinessHours()          // getter
     *  @Not_Needed getOpeningTime()            // wrapper
     *  @Not_Needed getClosingTime()             // setter
     */
    public void NotNeeded() { }

}       // end of TestInn class
