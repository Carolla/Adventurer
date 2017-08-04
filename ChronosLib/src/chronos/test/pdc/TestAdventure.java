/**
 * TestAdventure.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
 *          July 29, 2017 // autogen: QA Tool added missing test methods <br>
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
  {}

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
   * @Normal.Test Adventurer(String, String, String, String) -- constructor test
   */
  @Test
  public void testCtor()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Create a simple adventure with legal Town
    Adventure adv = new Adventure(DEF_ADVENTURE, DEF_TOWN, DEF_ARENA, DEF_OVERVIEW);
    assertNotNull(adv);
  }


  /**
   * @Null.Test Adventurer(String, String, String, String) -- various null parms
   */
  @Test
  public void testCtor_NullParms()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // RUN
    // Trigger exceptions for null parms
    Adventure adv = null;
    try {
      adv = new Adventure(null, DEF_TOWN, DEF_ARENA, DEF_OVERVIEW);
      fail(MsgCtrl.EXP_EXCEPTION + "for null Adventure name");
    } catch (NullPointerException ex) {
      MsgCtrl.errMsgln(MsgCtrl.EXP_EXCEPTION + ex.getMessage());
      assertNull(adv);
    }
    try {
      adv = new Adventure(DEF_ADVENTURE, null, DEF_ARENA, DEF_OVERVIEW);
      fail(MsgCtrl.EXP_EXCEPTION + "for null Town name");
    } catch (NullPointerException ex) {
      MsgCtrl.errMsgln(MsgCtrl.EXP_EXCEPTION + ex.getMessage());
      assertNull(adv);
    }
    try {
      adv = new Adventure(DEF_ADVENTURE, DEF_TOWN, null, DEF_OVERVIEW);
      fail(MsgCtrl.EXP_EXCEPTION + "for null Arena name");
    } catch (NullPointerException ex) {
      MsgCtrl.errMsgln(MsgCtrl.EXP_EXCEPTION + ex.getMessage());
      assertNull(adv);
    }
    try {
      adv = new Adventure(DEF_ADVENTURE, DEF_TOWN, DEF_ARENA, null);
      fail(MsgCtrl.EXP_EXCEPTION + "for null overview");
    } catch (NullPointerException ex) {
      MsgCtrl.errMsgln(MsgCtrl.EXP_EXCEPTION + ex.getMessage());
      assertNull(adv);
    }
  }


  /**
   * @Normal.Test boolean equals() -- compare two Adventures as equal if their name, adventure name,
   *              and arena name are the same
   */
  @Test
  public void testEquals()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    Adventure adv1 = new Adventure(DEF_ADVENTURE, DEF_TOWN, DEF_ARENA, DEF_OVERVIEW);
    assertNotNull(adv1);
    Adventure adv2 = new Adventure(DEF_ADVENTURE, DEF_TOWN, DEF_ARENA, DEF_OVERVIEW);
    assertNotNull(adv2);

    assertTrue(adv1.equals(adv2));
    assertEquals(adv1.toString(), adv2.toString());
    assertEquals(adv1.hashCode(), adv2.hashCode());
  }

  /**
   * @Error.Test boolean equals() -- compare two different Adventures
   */
  @Test
  public void testEquals_DifferentObjectsNotEqual()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Test 1: Adventure name is different
    Adventure adv1 = new Adventure(DEF_ADVENTURE, DEF_TOWN, DEF_ARENA, DEF_OVERVIEW);
    assertNotNull(adv1);
    Adventure adv2 = new Adventure("Big Adventure", DEF_TOWN, DEF_ARENA, DEF_OVERVIEW);
    assertNotNull(adv2);

    assertNotEquals(adv1, adv2);
    assertFalse(adv1.equals(adv2));
    assertFalse(adv1.hashCode() == adv2.hashCode());
    assertFalse(adv1.toString().equals(adv2.toString()));

    // Test 2: Town name is different
    adv1 = new Adventure(DEF_ADVENTURE, DEF_TOWN, DEF_ARENA, DEF_OVERVIEW);
    assertNotNull(adv1);
    adv2 = new Adventure(DEF_ADVENTURE, "Eltonville", DEF_ARENA, DEF_OVERVIEW);
    assertNotNull(adv2);

    assertNotEquals(adv1, adv2);
    assertFalse(adv1.equals(adv2));
    assertFalse(adv1.hashCode() == adv2.hashCode());
    assertTrue(adv1.toString().equals(adv2.toString())); // adventure name the same

    // Test 3: Arena name is different
    adv1 = new Adventure(DEF_ADVENTURE, DEF_TOWN, DEF_ARENA, DEF_OVERVIEW);
    assertNotNull(adv1);
    adv2 = new Adventure(DEF_ADVENTURE, DEF_TOWN, "Deathly Maze", DEF_OVERVIEW);
    assertNotNull(adv2);

    assertNotEquals(adv1, adv2);
    assertFalse(adv1.equals(adv2));
    assertFalse(adv1.hashCode() == adv2.hashCode());
    assertTrue(adv1.toString().equals(adv2.toString())); // adventure name the same

    // Test 4: Overview being different should not cause the object to be different
    adv1 = new Adventure(DEF_ADVENTURE, DEF_TOWN, DEF_ARENA, DEF_OVERVIEW);
    assertNotNull(adv1);
    adv2 = new Adventure(DEF_ADVENTURE, DEF_TOWN, DEF_ARENA, "Some other overview here");
    assertNotNull(adv2);

    assertEquals(adv1, adv2);
    assertTrue(adv1.equals(adv2));
    assertTrue(adv1.hashCode() == adv2.hashCode());
    assertTrue(adv1.toString().equals(adv2.toString())); // adventure name the same
  }

  /**
   * @Not.Needed String getName() -- simple getter
   */
  public void testGetName()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }

  /**
   * @Not.Needed String getTownName() -- simple getter
   */
  public void testGetTownName()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }

  /**
   * @Normal.Test int hashCode() -- generate a unique ID for the Item object
   */
  @Test
  public void testHashCode()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
  
    Adventure adv1 = new Adventure(DEF_ADVENTURE, DEF_TOWN, DEF_ARENA, DEF_OVERVIEW);
    assertNotNull(adv1);
    Adventure adv2 = new Adventure(DEF_ADVENTURE, DEF_TOWN, DEF_ARENA, DEF_OVERVIEW);
    assertNotNull(adv2);
  
    assertEquals(adv1, adv2);
    assertEquals(adv1.toString(), adv2.toString());
    assertEquals(adv1.hashCode(), adv2.hashCode());
  }

  /**
   * @Not.Needed String getKey() -- simple getter
   */
  public void testKey()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed String getOverview() -- simple getter
   */
  public void testOverview()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }

  /**
   * @Not.Needed String toString() -- simple getter
   */
  public void testToString()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


}
