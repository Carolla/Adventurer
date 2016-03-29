/**
 * TestAdventure.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.test.pdc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.Adventure;
import mylib.MsgCtrl;

/**
 * Unit tests for {@code chronos.pdc.Adventure}
 * 
 * @author Al Cline
 * @version Sep 21, 2014 // original <br>
 *          Mar 29 2016 // reviewed and updated for overall QA <br>
 */
public class TestAdventure
{

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
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
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
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP None
    
    // RUN 
    // Trigger exceptions for null parms
    Adventure adv = null;
    try {
      adv = new Adventure(null, DEF_TOWN, DEF_ARENA, DEF_OVERVIEW);
    } catch (NullPointerException ex) {
      MsgCtrl.errMsgln("Expected: " + ex.getMessage());
      assertNull(adv);
    }
    try {
      adv = new Adventure(DEF_ADVENTURE, null, DEF_ARENA, DEF_OVERVIEW);
    } catch (NullPointerException ex) {
      MsgCtrl.errMsgln("Expected: " + ex.getMessage());
      assertNull(adv);
    }
    try {
      adv = new Adventure(DEF_ADVENTURE, DEF_TOWN, null, DEF_OVERVIEW);
    } catch (NullPointerException ex) {
      MsgCtrl.errMsgln("Expected: " + ex.getMessage());
      assertNull(adv);
    }
    try {
      adv = new Adventure(DEF_ADVENTURE, DEF_TOWN, DEF_ARENA, null);
    } catch (NullPointerException ex) {
      MsgCtrl.errMsgln("Expected: " + ex.getMessage());
      assertNull(adv);
    }
  }



  /**
   * List of class methods that do not need unit tests
   * 
   */
  public void _notNeeded()
  {}

}
