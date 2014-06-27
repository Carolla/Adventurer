/**
 * TestAdvRegistry.java
 * Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package test.pdc.registry;

import static org.junit.Assert.assertNotNull;
import mylib.MsgCtrl;
import mylib.pdc.Registry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.registry.AdvHelpRegistry;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.ItemRegistry;
import chronos.pdc.registry.NPCRegistry;
import chronos.pdc.registry.OccupationRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import chronos.pdc.registry.SkillRegistry;
import chronos.pdc.registry.TownRegistry;

/**
 *    <Class Description>
 *
 * @author Alan Cline
 * @version 	Feb 16, 2014   // original 
 */
public class TestAdvRegistry
{

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
    }


    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
    }


    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
    }


    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        MsgCtrl.auditMsgsOn(false);
    }


//    @Test
//    public void testCloseRegistry()
//    {        
//        BuildingRegistry breg = (BuildingRegistry) AdvRegistryFactory.getRegistry(RegKey.BLDG);
//        breg.closeRegistry();
//        BuildingRegistry breg2 = (BuildingRegistry) AdvRegistryFactory.getRegistry(RegKey.BLDG);
//        assertNotSame(breg, breg2);
//        breg2.closeRegistry();
//    }


    @Test
    public void testRegistryList()
    {        
        AdventureRegistry areg = (AdventureRegistry) RegistryFactory.getRegistry(RegKey.ADV);
        assertNotNull(areg);
        areg.closeRegistry();
        
        BuildingRegistry breg = (BuildingRegistry) RegistryFactory.getRegistry(RegKey.BLDG);
        assertNotNull(breg);
        breg.closeRegistry();
        
        ItemRegistry ireg = (ItemRegistry) RegistryFactory.getRegistry(RegKey.ITEM);
        assertNotNull(ireg);
        ireg.closeRegistry();
        
        NPCRegistry nreg = (NPCRegistry) RegistryFactory.getRegistry(RegKey.NPC);
        assertNotNull(nreg);
        nreg.closeRegistry();
        
        OccupationRegistry oreg = (OccupationRegistry) RegistryFactory.getRegistry(RegKey.OCP);
        assertNotNull(oreg);
        oreg.closeRegistry();
        
        SkillRegistry sreg = (SkillRegistry) RegistryFactory.getRegistry(RegKey.SKILL);
        assertNotNull(sreg);
        sreg.closeRegistry();

        TownRegistry treg = (TownRegistry) RegistryFactory.getRegistry(RegKey.TOWN);
        assertNotNull(treg);
        treg.closeRegistry();
        // Close secondary registry
        breg = (BuildingRegistry) RegistryFactory.getRegistry(RegKey.BLDG);
        breg.closeRegistry();


        AdvHelpRegistry hreg = (AdvHelpRegistry) RegistryFactory.getRegistry(RegKey.HELP);
        assertNotNull(hreg);
        hreg.closeRegistry();
     }

    
//    @Test
//    public void testGetInstance()
//    {
//        MsgCtrl.auditMsgsOn(true);
//
//        // Create all registries by traversing the enum key list
//        for (RegKey key : RegKey.values()) {
//            MsgCtrl.msgln(key.toString());
//            Registry reg =   AdvRegistryFactory.getRegistry(key);
//            assertNotNull(reg);
//            reg.closeRegistry();
//        }
//    }
//
//
//    @Test
//    public void testGetInstanceFails()
//    {
//        // Get a local adv help registry
//        AdvHelpRegistry advReg = (AdvHelpRegistry) 
//                        AdvRegistryFactory.getRegistry(null);
//        assertNull(advReg);
//    }

    
    // PRIVATE HELPER METHODS
    private void closeAllRegistries()
    {
        MsgCtrl.auditMsgsOn(true);
        
        int count = 0;
        for (RegKey key : RegKey.values()) {
            Registry reg = RegistryFactory.getRegistry(key);
            MsgCtrl.msgln("Closing registry " + key);
            reg.closeRegistry();
            count++;
        }
        MsgCtrl.msgln("\t" + count + " registries closed");
    }
    
    
}           // end of TestAdvHelp class
