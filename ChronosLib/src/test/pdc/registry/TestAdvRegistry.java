/**
 * TestAdvRegistry.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package test.pdc.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import mylib.MsgCtrl;
import mylib.pdc.Registry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.Adventure;
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
  {}

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
  {
    _rf = RegistryFactory.getInstance();
  }

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

  /**
   * @Normal open all 7 registries
   */
  @Test
  public void testRegistryList()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP: None

    // DO
    AdventureRegistry areg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
    assertNotNull(areg);

    BuildingRegistry breg = (BuildingRegistry) _rf.getRegistry(RegKey.BLDG);
    assertNotNull(breg);

    ItemRegistry ireg = (ItemRegistry) _rf.getRegistry(RegKey.ITEM);
    assertNotNull(ireg);

    NPCRegistry nreg = (NPCRegistry) _rf.getRegistry(RegKey.NPC);
    assertNotNull(nreg);

    OccupationRegistry oreg = (OccupationRegistry) _rf.getRegistry(RegKey.OCP);
    assertNotNull(oreg);

    SkillRegistry sreg = (SkillRegistry) _rf.getRegistry(RegKey.SKILL);
    assertNotNull(sreg);

    TownRegistry treg = (TownRegistry) _rf.getRegistry(RegKey.TOWN);
    assertNotNull(treg);

    // Teardown: Close all registries
    closeAllRegistries();

  }


  /**
   * @Normal Add a new Adventure into the AdvReg, then retrieve it without recreating it
   * @Error Add an existing Adventure into AdvReg (attempt duplicated)
   */
  @Test
  public void testNewInstance()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP None
    String DEF_ADVENTURE = "The Quest for Rogahn and Zelligar";

    // DO
    // Add a new Adventure to the AdvRegistry
    AdventureRegistry areg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
    assertNotNull(areg);

    // VERIFY AdvReg contains single element
    assertEquals(1, areg.getNbrElements());
    // and the element is the recent adventure
    Adventure adv = areg.getAdventure(DEF_ADVENTURE);
    MsgCtrl.msgln("candidate key = " + adv.getKey());
    assertTrue(adv.getName().equals(DEF_ADVENTURE));

    // TEARDOWN
    closeAllRegistries();
  }


  // @Test
  // public void testGetInstanceFails()
  // {
  // // Get a local adv help registry
  // AdvHelpRegistry advReg = (AdvHelpRegistry)
  // AdvRegistryFactory.getInstance().getRegistry(null);
  // assertNull(advReg);
  // }


  // ===========================================================================
  // PRIVATE HELPER METHODS
  // ===========================================================================

  private void closeAllRegistries()
  {
    _rf = RegistryFactory.getInstance();
    for (RegKey key : RegKey.values()) {
      Registry reg = _rf.getRegistry(key);
      reg.closeRegistry();
    }
  }

  /**
   * 1
   * 
   * @NonNeeded getAdventure(String) -- wrapper to mylib.pdc.Registry
   */
  void _testsNotNeeded()
  {}


} // end of TestAdvHelp class
