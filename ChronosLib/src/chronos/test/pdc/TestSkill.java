/**
 * TestSkill.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@wowway.com
 */


package chronos.test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

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
 * @version Feb 10, 2013 // original <br>
 *          July 17, 2017 // minor changes per QATool <br>
 *          July 21, 2017 // per QATool, and to use customized Javadoc tags <br>
 */
public class TestSkill
{
  private Skill _skill;
  private final String NAME = "--TestSkill";
  private final String DESC = "--Test description";


  // =============================================================================
  // SETUP/TEARDOWN
  // =============================================================================

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


  // =============================================================================
  // BEGIN TESTS
  // =============================================================================

  /**
   * @Normal.Test Skill(String name, String desc) -- two-parm constructor
   */
  @Test
  public void testCtor()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    assertEquals(NAME, _skill.getName());
    assertEquals(DESC, _skill.getDescription());
  }

  
  /**
   * @Null.Test Skill(String name, String desc) -- null parms to constructor <br>
   * @Error.Test Skill(String name, String desc) -- name or desc overly long
   */
  @Test
  public void testCtor_nullInputParms()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    try {
      new Skill(null, DESC);
      MsgCtrl.errMsgln(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (ApplicationException apex) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + apex.getMessage());
    }
    try {
      new Skill(NAME, null);
      MsgCtrl.errMsgln(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (ApplicationException apex2) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + apex2.getMessage());
    }
    try {
      new Skill("This name is way too long to be valid", DESC);
      MsgCtrl.errMsgln(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (ApplicationException apex3) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + apex3.getMessage());
    }
    try {
      String overlongDesc = "This is an overly long description of one of the skills."
          + "It justs runs on an one. ";
      new Skill(NAME, overlongDesc);
      MsgCtrl.errMsgln(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (ApplicationException apex4) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + apex4.getMessage());
    }
  }


  /**
   * @Normal.Test boolean equals(Object) -- Verify same class, name and description return true
   */
  @Test
  public void testEquals()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Normal Verify same class, name and description returns true
    Skill tSkill_1 = new Skill(NAME, DESC);
    assertTrue(_skill.equals(tSkill_1));
  }

  
  /**
   * @Error.Test boolean equals(Object) -- Verify different names returns false
   */
  @Test
  public void testEquals_DifferentNamesReturnsFalse()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    String ts1Name = "DM'ing";
    Skill tSkill_1 = new Skill(ts1Name, DESC);
    assertFalse(_skill.equals(tSkill_1));
  }

  
  /**
   * @Error.Test boolean equals(Object) -- Verify different descriptions return false
   */
  @Test
  public void testEquals_DifferentDescriptionsReturnFalse()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    String ts1Desc = "Able to create mazes and challenging adventures";
    Skill tSkill_1 = new Skill(NAME, ts1Desc);
    assertFalse(_skill.equals(tSkill_1));
  }

  
  /**
   * @Error.Test boolean equals(Object) -- Verify different classes return false <br>
   * @Error.Test boolean equals(Object) -- Verify different objects return false
   */
  @Test
  public void testEquals_DifferentObjectsReturnFalse()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Object is a Skill object
    String ts1Desc = "Able to create mazes and challenging adventures";
    Skill tSkill_1 = new Skill(NAME, ts1Desc);
    assertFalse(_skill.equals(tSkill_1));
    
    // Object is not a Skill object
    ArrayList<String> bait = new ArrayList<>();
    assertFalse(_skill.equals(bait));
  }

  
  /**
   * @Error.Test boolean equals(Object) -- Pass null object; expect false
   */
  @Test
  public void testEquals_nullSkillComparesFalse()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    assertFalse(_skill.equals(null));
  }


  /**
   * @Not.Needed String getDescription() -- simple getter
   */
  public void testGetDescription()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed String getKey() -- simple getter
   */
  public void testGetKey()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
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
   * @Not.Needed Skill getSkill(String) -- simple wrapper
   */
  public void testGetSkill()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.WRAPPER);
  }


  /**
   * @Not.Needed int hashCode() -- primitive operation
   */
  public void testHashCode()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.PRIMITIVE);
  }


  /**
   * @Not.Needed void setSkillRegistry(SkillRegistry skreg) -- simple setter
   */
  public void testSetSkillRegistry()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.SETTER);
  }


  /**
   * @Not.Needed String toString() -- primitive
   */
  public void testToString()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.PRIMITIVE);
  }


} // end of TestSkill class
