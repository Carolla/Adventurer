/**
 * TestAdventureRegistry.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.test.pdc.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.Adventure;
import chronos.pdc.registry.AdventureRegistry;
import mylib.MsgCtrl;

/**
 * Test the repository for Adventure storage
 * 
 * @author Alan Cline
 * @version Feb 16, 2014 // original <br>
 *          Sep 13, 2014 // cleaned up and added MsgCtrl.where() method <br>
 *          August 9, 2017 // autogen: QA Tool added missing test methods <br>
 */
public class TestAdventureRegistry
{
  /** Default Adventure name to start AdventureRegistry with */
  private final String DEF_ADVENTURE = "The Quest for Rogahn and Zelligar";
  /** Default Town to start AdventureRegistry with */
  private final String DEF_TOWN = "Biljur'Baz";
  /** Default Arena to start AdventureRegistry with */
  private final String DEF_ARENA = "Quasqueton";

  private AdventureRegistry _advReg;


  // ===========================================================================
  // FIXTURES
  // ===========================================================================

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    _advReg = new AdventureRegistry();
    assertNotNull(_advReg);
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _advReg = null;
  }


  // ===========================================================================
  // BEGIN TESTS
  // ===========================================================================

  /**
   * @Normal.Test AdventureRegistry() -- Verify registry created properly
   */
  @Test
  public void testCtor()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // VERIFY AdvReg contains one adventure
    assertEquals(1, _advReg.size());
    Adventure adv = _advReg.getAdventure(DEF_ADVENTURE);
    assertTrue(adv.getName().equals(DEF_ADVENTURE));

    List<String> advList = _advReg.getAdventureList();
    assertEquals(1, advList.size());
  }


  /**
   * @Not.Needed Adventure getAdventure(String name) -- tester in testCtor
   */
  @Test
  public void testGetAdventure()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.PRIMITIVE);
  }


  /**
   * @Error.Test Adventure getAdventure(String name) -- Request a non-existing adventure
   * @Error.Test Adventure getAdventure(String name) -- Request an adventure with an empty null key
   * @Null.Test Adventure getAdventure(String name) -- Request an adventure with a null key
   */
  @Test
  public void testGetAdventure_Nonexisting()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Request a non-existing adventure
    Adventure adv = _advReg.getAdventure("Salazar's Lair");
    assertTrue(adv == null);

    // Request an adventure with an empty key
    adv = _advReg.getAdventure("  ");
    assertTrue(adv == null);

    // Request an adventure with an null key
    adv = _advReg.getAdventure(null);
    assertTrue(adv == null);
  }


  /**
   * @Not.Needed Adventure getAdvnetureList() -- used in testCtor
   */
  @Test
  public void testGetAdventureList()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.PRIMITIVE);
  }


  /**
   * @Normal.Test void initialize() -- verify proper constructor
   */
  @Test
  public void testInitialize()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Get all the adventures, which is one.
    ArrayList<Adventure> advList = (ArrayList<Adventure>) _advReg.getAll();
    assertEquals(1, advList.size());
    Adventure adv = advList.get(0);
    MsgCtrl.msgln("\t Dumping elements of this adventure, initialized by superclass");
    MsgCtrl.msgln("\t Name: \t" + adv.getName());
    assertEquals(DEF_ADVENTURE, adv.getName());

    MsgCtrl.msgln("\t Town: \t" + adv.getTownName());
    assertEquals(DEF_TOWN, adv.getTownName());

    MsgCtrl.msgln("\t Arena: \t" + adv.getArenaName());
    assertEquals(DEF_ARENA, adv.getArenaName());
  }

  
  // ===========================================================================
  // PRIVATE HELPER METHODS
  // ===========================================================================



} // end of TestAdventureRegistry class
