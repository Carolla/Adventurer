/**
 * TestBuilding.java
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import test.pdc.ConcreteBuilding;
import test.pdc.ConcreteBuilding.MockConcreteBuilding;

import chronos.pdc.buildings.Bank;
import chronos.pdc.buildings.Building;
import chronos.pdc.buildings.ClericsGuild;
import chronos.pdc.buildings.FightersGuild;
import chronos.pdc.buildings.Inn;
import chronos.pdc.buildings.Jail;
import chronos.pdc.buildings.RoguesGuild;
import chronos.pdc.buildings.Store;
import chronos.pdc.buildings.WizardsGuild;
import chronos.pdc.registry.NPCRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

import mylib.ApplicationException;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/** 
 *  Tests the abstract class Building
 *  
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Apr 5, 2013   // original <DD>
 * </DL>
 */
public class TestBuilding
{
    /** test target object */
    private ConcreteBuilding _cb = null;
    /** mock for target object */
    private MockConcreteBuilding _mock = null;
    /** Secondary registry for NPCs, who are Building Masters */
    static NPCRegistry _npcReg = null;

    /** Building key */
    private final String NAME = "Concrete Building";
    /** Building master */
    private final String MASTER = "Gorbal";
    /** Expected hovertext */
    private final String HOVERTEXT = "Click and see!";
    /** Building intro: seen by Enter command */
    private final String INTRO = "Introduction";
    /** Building description: seen by Look command */
    private final String DESC = "Internal description";
    
    
    /** Set up secondary registries needed */
    @BeforeClass
    public static void runOnce()
    {
        // Create a Registry object, which will be initialized if new
        assertNotNull((NPCRegistry) RegistryFactory.getInstance().getRegistry(RegKey.NPC));
    }
    
    /** Close down all the secondary registries needed for the Inn */
    @AfterClass
    public static void cleanUp() 
    {
        // Close down the secondary, but don't delete the registry file
        ((NPCRegistry) RegistryFactory.getInstance().getRegistry(RegKey.NPC)).closeRegistry();
    }
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        _cb = new ConcreteBuilding(NAME, MASTER, HOVERTEXT, INTRO, DESC);
        assertNotNull(_cb);
        _mock = _cb.new MockConcreteBuilding();
        assertNotNull(_mock);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _mock = null;
        _cb = null;
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  BEGIN TESTS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Chronos.pdc.Building 
     * @Normal  create a concrete building
     * @Error  try to create a Building with differing null ctor parms 
     * @Error  master is not in NPC Registry
     * @throws ApplicationException if unexpected error occurs 
     */
    @Test
    public void testBuilding()  throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\n testBuilding()");

        // Normal: Ensure Building is created as expected
        dump(_cb);

        // Normal: HoverText can be null
        ConcreteBuilding b0 = new ConcreteBuilding(NAME, MASTER, HOVERTEXT, INTRO, DESC);
        assertNotNull(b0);
        assertEquals(HOVERTEXT, b0.getHoverText());
        dump(b0);


        // Error: null ctor parms
        try {
            new ConcreteBuilding(null, MASTER, HOVERTEXT, INTRO, DESC);
            assertNotNull(_cb);
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\n\tExpected exception: " + ex.getMessage());
        }
        // Error: null ctor parms
        try {
            new ConcreteBuilding(null, MASTER, HOVERTEXT, INTRO, DESC);
            assertNotNull(_cb);
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\tExpected exception: " + ex.getMessage());
        }
        try {
            new ConcreteBuilding(NAME, null, HOVERTEXT, INTRO, DESC);
            assertNotNull(_cb);
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\tExpected exception: " + ex.getMessage());
        }
        
        // Error: master is not in NPC Registry
        try {
            new ConcreteBuilding(NAME, "Blockhead", HOVERTEXT, INTRO, DESC);
            assertNotNull(_cb);
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\tExpected exception: " + ex.getMessage());
        }
    }

    /** Chronos.pdc.Building 
     * @Normal  get description when hero enters during business hours
     * @Error      get error message when hero tries to enter in off hours.
     * @throws ApplicationException if unexpected error occurs 
     */
    @Test
    public void testEnter() throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\n testEnter()");
        
        // NORMAL Enter during business hours of 9am - 6pm
        MsgCtrl.msgln("\tHero enters at " + _cb.getMeridianTime(1000));
        assertEquals(_cb.getInteriorDescription(), _cb.enter(1000));
        
        // ERROR Enter before opening hours, at 830
        MsgCtrl.msg("\tHero enters at " + _cb.getMeridianTime(830));
        MsgCtrl.msgln("\t" +_mock.getClosedMsg());
        assertEquals(_mock.getClosedMsg(), _cb.enter(830));
        
        // ERROR Enter after closing houurs, at 915pm
        MsgCtrl.msg("\tHero enters at " + _cb.getMeridianTime(2115));
        MsgCtrl.msgln("\t" +_mock.getClosedMsg());
        assertEquals(_mock.getClosedMsg(), _cb.enter(2115));
        
    }
        
    
    /** Chronos.pdc.Building 
     * @Normal  compare two identical buildings but different instances (but both in NPC Registry)
     * @Normal  compare two buildings with only same name and master
     * @Error       compare building with different master
     * @throws ApplicationException if unexpected error occurs 
     */
    @Test
    public void testEquals() throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\n testEquals()");
    
        // NORMAL compare two identical buildings but different instances
        ConcreteBuilding cb2 = new ConcreteBuilding(NAME, MASTER, HOVERTEXT, INTRO, DESC);
        assertTrue(cb2.equals(_cb));
        
        // NORMAL Compare two identical buildings but different instances (but both in NPC Registry)
        cb2 = new ConcreteBuilding(NAME, "Aragon", HOVERTEXT, INTRO, DESC);
        MsgCtrl.msgln("\n\t" + _cb.getKey() + " has master " + _cb.getMaster());
        MsgCtrl.msgln("\n\t" + _cb.getKey() + " has master " + cb2.getMaster());
        assertFalse(cb2.equals(_cb));
    }
    
    
    /** Chronos.pdc.Building 
     * @Normal  enter valid times and get valid meridiant times
     * @Error   enter invalid times and get false return values  
     * @throws ApplicationException if unexpected error occurs 
     */
    @Test
    public void testGetMeridianTime() throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\n testGetMeridiantTime()");
    
        // NORMAL
        int testOpen = 800;
        int testClose = 1700;
        String merOpen = "8:00 AM";
        String merClose = "5:00 PM";
        MsgCtrl.msg("\t time [" + testOpen + ", " + testClose + "] = ");
        assertTrue(_cb.setBusinessHours(testOpen, testClose));
        int[] hrs = _cb.getBusinessHours();
        MsgCtrl.msg("\tfrom " + _cb.getMeridianTime(hrs[0]));
        MsgCtrl.msgln("\tto " + _cb.getMeridianTime(hrs[1]));
        assertEquals (merOpen, _cb.getMeridianTime(hrs[0]));
        assertEquals (merClose, _cb.getMeridianTime(hrs[1]));

        // Normal try noon to midnight
        testOpen = 1200;
        testClose = 2400;
        merOpen = "Noon";
        merClose = "Midnight";
        MsgCtrl.msg("\t time [" + testOpen + ", " + testClose + "] = ");
        assertTrue(_cb.setBusinessHours(testOpen, testClose));
        hrs = _cb.getBusinessHours();
        MsgCtrl.msg("\tfrom " + _cb.getMeridianTime(hrs[0]));
        MsgCtrl.msgln("\tto " + _cb.getMeridianTime(hrs[1]));
        assertEquals (merOpen, _cb.getMeridianTime(hrs[0]));
        assertEquals (merClose, _cb.getMeridianTime(hrs[1]));

        // Normal try noon to midnight
        testOpen = 1330;
        testClose = 2355;
        merOpen = "1:30 PM";
        merClose = "11:55 PM";
        MsgCtrl.msg("\t time [" + testOpen + ", " + testClose + "] = ");
        assertTrue(_cb.setBusinessHours(testOpen, testClose));
        hrs = _cb.getBusinessHours();
        MsgCtrl.msg("\tfrom " + _cb.getMeridianTime(hrs[0]));
        MsgCtrl.msgln("\tto " + _cb.getMeridianTime(hrs[1]));
        assertEquals (merOpen, _cb.getMeridianTime(hrs[0]));
        assertEquals (merClose, _cb.getMeridianTime(hrs[1]));

        // Error Building closes the day after it opens, e.g., from 11:45 PM to 12:15 am
        testOpen = 2345;
        testClose = 15;
        merOpen = "11:45 PM";
        merClose = "12:15 AM";
        MsgCtrl.msg("\t time [" + testOpen + ", " + testClose + "] = ");
        assertFalse(_cb.setBusinessHours(testOpen, testClose));
        MsgCtrl.msgln("\t <invalid time parms>");
 
        // ERROR   
        // open is after closing
        testOpen = 1400;
        testClose = 1000;
        assertFalse(_cb.setBusinessHours(testOpen, testClose));
        // open is negative
        testOpen = -0400;
        assertFalse(_cb.setBusinessHours(testOpen, testClose));
        // open is zero
        testOpen = 0;
        assertFalse(_cb.setBusinessHours(testOpen, testClose));
        // closing is greater than 2400
        testOpen = 1400;
        testClose = 2600;
        assertFalse(_cb.setBusinessHours(testOpen, testClose));
        // closing is at midnight, represented as 0
        testOpen = 1400;
        testClose = 0;
        assertFalse(_cb.setBusinessHours(testOpen, testClose));
    }
    

    @Test
    public void testAllBuildingDescs() throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(true);
        MsgCtrl.msgln(this, "\n testAllBuildingDescs()");
    
        Building b = null;
        // Create the buildings and dump their contents
        b = new Bank();
        assertNotNull(b);
        dump(b);
        MsgCtrl.msgln("\n");
        
        b = new ClericsGuild();
        assertNotNull(b);
        dump(b);
        MsgCtrl.msgln("\n");

        b = new FightersGuild();
        assertNotNull(b);
        dump(b);
        MsgCtrl.msgln("\n");
        
        b = new Inn();
        assertNotNull(b);
        dump(b);
        MsgCtrl.msgln("\n");
        
        b = new Jail();
        assertNotNull(b);
        dump(b);
        MsgCtrl.msgln("\n");
        
        b = new RoguesGuild();
        assertNotNull(b);
        dump(b);
        MsgCtrl.msgln("\n");
        
        b = new Store();
        assertNotNull(b);
        dump(b);
        MsgCtrl.msgln("\n");
        
        b = new WizardsGuild();
        assertNotNull(b);
        dump(b);
        MsgCtrl.msgln("\n");

    }
    
    
    /** Chronos.pdc.Building
     * @Not_Needed  getKey()                                     // simple getter
     * @Not_Needed  getMaster()                                // simple getter
     * @Not_Needed  getName()                                  // simple getter
     * @Not_Needed  getDescription()                         // simple getter
     * @Not_Needed  getIntro()                                    // simple getterr
     * @Not_Needed  setBusinessHourse()                  // verified as part of getMeridianTime()
     */
    public void NotNeeded() { } 


    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Dump the contents of the Building
     * @param bldg      the building to display the contents of 
     */
    private void dump(Building bldg)
    {
        MsgCtrl.msg("\t Created: \t" + bldg.getName());
        MsgCtrl.msgln("\t managed by " + bldg.getMaster().getName());
        int[] hours = bldg.getBusinessHours();
        int oTime = hours[0];
        int cTime = hours[1];
        String opening = bldg.getMeridianTime(oTime);
        String closing = bldg.getMeridianTime(cTime);
        MsgCtrl.msgln("\t Open from " + opening + " to " + closing);
        MsgCtrl.msgln("\tClick:  \t" + bldg.getHoverText());
        MsgCtrl.msgln("\tENTER: \t" + bldg.getExteriorDescription());
        MsgCtrl.msgln("\tLOOK:\t" + bldg.getInteriorDescription());    
    }    
        
        
        
}           // end of TestBuilding class
