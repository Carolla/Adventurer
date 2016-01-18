/**
 * TestAdventure.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import mylib.ApplicationException;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.Adventure;
import chronos.pdc.registry.RegistryFactory;

/**
 * Unit tests for {@code chronos.pdc.Adventure}
 * 
 * @author Al Cline
 * @version Sep 21, 2014 // original <br>
 */

/*
 * close() dump() equals(Object) getKey() getName() getTownName() getArenaName() getOverview()
 * getArena() getTown() isOpen() open() toString()
 */


public class TestAdventure
{
  static private RegistryFactory _rf;
  // static private TownRegistry _treg;


  /** Default Adventure */
  private final String DEF_ADVENTURE = "The Quest for Rogahn and Zelligar";
  /** Default Town to start AdventureRegistry with */
  private final String DEF_TOWN = "Biljur'Baz";
  /** Default Arena to start AdventureRegistry with */
  private final String DEF_ARENA = "Quasqueton";
  /** Overview for Quasqueton Adventure */
  private final String DEF_OVERVIEW = "Some overtext goes here";

  // ============================================================
  // Fixtures
  // ============================================================

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void _setUpBeforeClass() throws Exception
  {
    // _rf = RegistryFactory.getInstance();
    // _treg = (TownRegistry) _rf.getRegistry(RegKey.TOWN);
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void _setUp() throws Exception
  {}

  /**
   * @throws java.lang.Exception
   */
  @After
  public void _tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ============================================================
  // BEGIN TESTS
  // ============================================================

  /**
   * @Normal Adventurer(String, String, String, String) -- constructor
   */
  @Test
  public void testAdventureCtor()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP Dump the names in the Town Registry
//    dump(_treg);

    // DO create a simple adventure with legal Town
    Adventure adv = new Adventure(DEF_ADVENTURE, DEF_TOWN, DEF_ARENA, DEF_OVERVIEW);
    assertNotNull(adv);
  }


  /**
   * @Null Adventurer(String, String, String, String) -- various null parms
   */
  @Test
  public void testAdventureNulls()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP None
    // DO create a simple adventure with legal Town
    try {
      Adventure adv = new Adventure(null, DEF_TOWN, DEF_ARENA, DEF_OVERVIEW);
      assertNull(adv);
    } catch (ApplicationException ex) {
      MsgCtrl.errMsgln("Experted msg: " + ex.getMessage());
    }
    try {
      Adventure adv = new Adventure(DEF_ADVENTURE, null, DEF_ARENA, DEF_OVERVIEW);
      assertNull(adv);
    } catch (ApplicationException ex) {
      MsgCtrl.errMsgln("Experted msg: " + ex.getMessage());
    }
    try {
      Adventure adv = new Adventure(DEF_ADVENTURE, DEF_TOWN, null, DEF_OVERVIEW);
      assertNull(adv);
    } catch (ApplicationException ex) {
      MsgCtrl.errMsgln("Experted msg: " + ex.getMessage());
    }
    try {
      Adventure adv = new Adventure(DEF_ADVENTURE, DEF_TOWN, DEF_ARENA, null);
      assertNull(adv);
    } catch (ApplicationException ex) {
      MsgCtrl.errMsgln("Experted msg: " + ex.getMessage());
    }
  }



  /**
   * List of class methods that do not need unit tests
   * 
   */
  @Test
  public void _notNeeded()
  {}

}
