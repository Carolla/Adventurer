/**
 * TestAdventureRegistry.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package test.pdc.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import mylib.MsgCtrl;
import mylib.dmc.IRegistryElement;

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
public class TestAdventureRegistry
{
  /** Factory that retrieves or creates all registries (singletons) */
  static private RegistryFactory _rf;

  private String DEF_ADVENTURE = "The Quest for Rogahn and Zelligar";

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
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
    _rf = RegistryFactory.getInstance();
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    _rf.closeAllRegistries();
    _rf = null;
  }


  // ===========================================================================
  // BEGIN TESTS
  // ===========================================================================

  /**
   * @Normal open and close all 7 registries
   */
  @Test
  public void testRegistryList()
  {
    MsgCtrl.where(this);

    // DO
    AdventureRegistry areg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
    assertNotNull(areg);
    assertFalse(areg.isClosed());

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

    int regNum = _rf.getNumberOfRegistries();
    MsgCtrl.msgln("Registires open and stored in RegistryFactory collection = " + regNum);
    assertTrue(regNum == RegKey.values().length);

    MsgCtrl.msgln("Closing all Registires");
    _rf.closeAllRegistries();
    regNum = _rf.getNumberOfRegistries();
    MsgCtrl.msgln("Registires open and stored in RegistryFactory collection = " + regNum);
    assertEquals(0, regNum);
  }


  /**
   * @Normal Add a new Adventure into the AdvReg, then retrieve it without recreating it
   */
  @Test
  public void testNewInstance()
  {
    MsgCtrl.where(this);

    // SETUP None

    // DO
    // Add a new Adventure to the AdventureRegistry
    AdventureRegistry areg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
    assertFalse(areg.isClosed());
    assertNotNull(areg);

    // VERIFY AdvReg contains single element
    assertEquals(1, areg.getNbrElements());
    // and the element is the recent adventure
    Adventure adv = areg.getAdventure(DEF_ADVENTURE);
    assertTrue(adv.getName().equals(DEF_ADVENTURE));
  }


  /**
   * @Normal Try to duplicate an AdventureRegisty in the collection
   */
  @Test
  public void testNewInstance_Dup()
  {
    MsgCtrl.where(this);

    // SETUP None

    // DO
    // Add a new Adventure to the AdventureRegistry
    AdventureRegistry areg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
    assertFalse(areg.isClosed());
    assertNotNull(areg);
    MsgCtrl.msgln("Registry " + areg + " open and stored");

    int regNum = _rf.getNumberOfRegistries();
    MsgCtrl.msgln("Registries open and stored in RegistryFactory collection = " + regNum);
    assertEquals(1, regNum);

    // Try to add another AdventureRegistry
    AdventureRegistry areg2 = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
    assertFalse(areg2.isClosed());
    assertNotNull(areg2);
    MsgCtrl.msgln("Registry " + areg2 + " open and stored");

    // VERIFY RegistryFactory contains same single element
    assertEquals(areg, areg2);
    regNum = _rf.getNumberOfRegistries();
    assertEquals(1, regNum);
    // ...and the element is the recent adventure
    assertEquals(1, areg.getAdventureList().size());
  }


  /**
   * @Normal Return an adventure by name
   */
  @Test
  public void testGetAdventure()
  {
    MsgCtrl.where(this);

    // SETUP None
    MsgCtrl.msgln("Creating default adventure in Registry ");
    AdventureRegistry areg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
    assertNotNull(areg);

    // DO
    MsgCtrl.msgln("Retrieving adventure from Registry ");
    List<IRegistryElement> advList = areg.get(DEF_ADVENTURE);
    assertNotNull(advList);
    assertEquals(1, advList.size());

    // VERIFY
    MsgCtrl.msgln("Retrieving adventure name from Adventure");
    Adventure adv = (Adventure) advList.get(0);
    assertEquals(DEF_ADVENTURE, adv.getName());
  }


  /**
   * @Error Return an non-existing adventure
   * @Error Return an adventure with an empty null key
   * @Null Return an adventure with a null key
   * @Error Retrieve an adventure from a non-existing AdventureRegistry
   */
  @Test
  public void testGetAdventure_Nonexisting()
  {
    MsgCtrl.where(this);

    // SETUP None
    MsgCtrl.msgln("Creating default adventure in Registry ");
    AdventureRegistry areg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
    assertNotNull(areg);

    // DO
    MsgCtrl.msgln("Retrieving non-existing adventure from Registry ");
    List<IRegistryElement> advList = areg.get("Salazar's Lair");

    // VERIFY
    assertEquals(0, advList.size());
    MsgCtrl.msgln("Received an empty list");
  }


  /**
   * @Error Attempt to return an adventure with an empty key
   * @Null Return an adventure with a null key -- compile error
   */
  @Test
  public void testGetAdventure_EmptyKey()
  {
    MsgCtrl.where(this);

    // SETUP None
    MsgCtrl.msgln("Creating default adventure in Registry ");
    AdventureRegistry areg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
    assertNotNull(areg);

    // DO
    MsgCtrl.msgln("Attempting to retrieve a adventure with an empty key");
    List<IRegistryElement> advList = areg.get("   ");

    // VERIFY
    assertEquals(0, advList.size());
    MsgCtrl.msgln("Received an empty list");
  }


  /**
   * @Normal  Get a list of all adventures in the AdventureRegistry
   */
  @Test
  public void testGetAdventureList()
  {
    MsgCtrl.where(this);

    // SETUP None
    MsgCtrl.msgln("Creating default adventure in Registry ");
    AdventureRegistry areg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
    assertNotNull(areg);

    // DO
    MsgCtrl.msgln("Dumping all Adventures in AdventureRegistry, which is 1");
    ArrayList<Adventure> advList = areg.getAdventureList();

    // VERIFY
    assertEquals(1, advList.size());
    MsgCtrl.msgln("Received a list of one: " + advList.get(0).getName());
  }


  // ===========================================================================
  // PRIVATE HELPER METHODS
  // ===========================================================================

  /**
   * @Null Return an adventure with a null key
   */

  /**
   * All methods tested
   */
  void _testsNotNeeded()
  {}


} // end of TestAdvHelp class
