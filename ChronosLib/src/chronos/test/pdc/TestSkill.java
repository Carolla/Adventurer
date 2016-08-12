/**
 * TestSkill.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.Skill;
import mylib.ApplicationException;
import mylib.MsgCtrl;

/**
 * Test the Skill class
 *
 * @author Alan Cline
 * @version
 *          <DL>
 *          <DT>Build 1.0 Feb 10, 2013 // original
 *          <DD>
 *          </DL>
 */
public class TestSkill
{
  private Skill _skill;
  private final String NAME = "--TestSkill";
  private final String DESC = "--Test description";


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ SETUP / TEARDOWN
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  @Before
  public void setUp()
  {
    _skill = new Skill(NAME, DESC);
  }

  @After
  public void tearDown()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ BEGIN TESTS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /**
   * chronos.pdc.Skill()
   * 
   * @Normal two-parm constructor
   * @Error null for either or both parms
   * @Error force exception for description too long
   * @Error force exception for name too long
   */
  @Test
  public void testCtor()
  {
    assertEquals(NAME, _skill.getName());
    assertEquals(DESC, _skill.getDescription());
  }

  @Test(expected = ApplicationException.class)
  public void nullNameThrowsException()
  {
    new Skill(null, DESC);
  }


  @Test(expected = ApplicationException.class)
  public void nullDescriptionThrowsException()
  {
    new Skill(NAME, null);
  }


  /**
   * chronos.pdc.Skill()
   * 
   * @Normal Verify same class, name and description return true
   * @Error Verify different names returns false
   * @Error Verify different descriptions return false
   * @Error Verify different classes return false
   * @Error Verify different objects return false
   * @Error Pass null object; expect false
   * @throws ApplicationException unexpected throw from Skill() ctor
   */
  @Test
  public void testEquals()
  {
    // Normal Verify same class, name and description returns true
    Skill tSkill_1 = new Skill(NAME, DESC);
    assertTrue(_skill.equals(tSkill_1));
  }

  @Test
  public void verifyDifferentNamesReturnsFalse()
  {
    String ts1Name = "DM'ing";
    Skill tSkill_1 = new Skill(ts1Name, DESC);
    assertFalse(_skill.equals(tSkill_1));
  }

  @Test
  public void verifyDifferentDescriptionsReturnFalse()
  {
    String ts1Desc = "Able to create mazes and challenging adventures";
    Skill tSkill_1 = new Skill(NAME, ts1Desc);
    assertFalse(_skill.equals(tSkill_1));
  }

  @Test
  public void nullSkillComparesFalse()
  {
    assertFalse(_skill.equals(null));
  }
} // end of TestSkill class
