/**
 * TestAdvRegistry.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
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
 * Test the repository for Adventure storage
 * 
 * @author Alan Cline
 * @version Feb 16, 2014 // original <br>
 *          Sep 13, 2014 // cleaned up and added MsgCtrl.where() method <br>
 */
public class TestAdvRegistry
{
  /** Factory that retrieves or creates all registries (singletons) */
  static private RegistryFactory _rf;

  // ===========================================================================
  // FIXTURES
  // ===========================================================================

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    _rf = RegistryFactory.getInstance();
  }


  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {}


  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {}


  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _rf = null;
  }


  // ===========================================================================
  // BEGIN TESTS
  // ===========================================================================

  @Test
  public void testRegistryList()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    AdventureRegistry areg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
    assertNotNull(areg);
    areg.closeRegistry();

    BuildingRegistry breg = (BuildingRegistry) _rf.getRegistry(RegKey.BLDG);
    assertNotNull(breg);
    breg.closeRegistry();

    TownRegistry treg = (TownRegistry) _rf.getRegistry(RegKey.TOWN);
    assertNotNull(treg);
    treg.closeRegistry();

    ItemRegistry ireg = (ItemRegistry) _rf.getRegistry(RegKey.ITEM);
    assertNotNull(ireg);
    ireg.closeRegistry();

    NPCRegistry nreg = (NPCRegistry) _rf.getRegistry(RegKey.NPC);
    assertNotNull(nreg);
    nreg.closeRegistry();

    OccupationRegistry oreg = (OccupationRegistry) _rf.getRegistry(RegKey.OCP);
    assertNotNull(oreg);
    oreg.closeRegistry();

    SkillRegistry sreg = (SkillRegistry) _rf.getRegistry(RegKey.SKILL);
    assertNotNull(sreg);
    sreg.closeRegistry();

//    TownRegistry treg = (TownRegistry) _rf.getRegistry(RegKey.TOWN);
//    assertNotNull(treg);
//    treg.closeRegistry();
    
    // Close secondary registry
    breg = (BuildingRegistry) _rf.getRegistry(RegKey.BLDG);
    breg.closeRegistry();

  }


  // @Test
  // public void testGetInstance()
  // {
  // MsgCtrl.auditMsgsOn(true);
  //
  // // Create all registries by traversing the enum key list
  // for (RegKey key : RegKey.values()) {
  // MsgCtrl.msgln(key.toString());
  // Registry reg = AdvRegistryFactory.getInstance().getRegistry(key);
  // assertNotNull(reg);
  // reg.closeRegistry();
  // }
  // }
  //
  //
  // @Test
  // public void testGetInstanceFails()
  // {
  // // Get a local adv help registry
  // AdvHelpRegistry advReg = (AdvHelpRegistry)
  // AdvRegistryFactory.getInstance().getRegistry(null);
  // assertNull(advReg);
  // }


  // PRIVATE HELPER METHODS
  private void closeAllRegistries()
  {
    MsgCtrl.auditMsgsOn(true);

    int count = 0;
    for (RegKey key : RegKey.values()) {
      Registry reg = _rf.getRegistry(key);
      MsgCtrl.msgln("Closing registry " + key);
      reg.closeRegistry();
      count++;
    }
    MsgCtrl.msgln("\t" + count + " registries closed");
  }

  /**
   * 1
   * 
   * @NonNeeded getAdventure(String) -- wrapper to mylib.pdc.Registry
   */
  void _testsNotNeeded()
  {}


} // end of TestAdvHelp class
