/**
 * TestSkill.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.Skill;
import mylib.ApplicationException;
import mylib.MsgCtrl;

/**
 * Test the simple Skill class that comprises the SkillRegistry collection
 * 
 * @author Alan Cline
 * @version Jan 5 2010 // original <br>
 *          Aug 22 2010 // added QA tags <br>
 *          Apr 20 2011 // TAA function/tags confirmed <br>
 *          May 15 2011 // TAA function confirmed <br>
 *          May 22 2011 // TAA added toObject and toRec tests <br>
 *          Jun 13 2011 // TAA tweaked for integration tests <br>
 *          Jun 30 2011 // TAA added unpackShuttle and loadShuttle <br>
 *          Mar 29 2016 // Reviewed and tested for overall QA <br>
 */
public class TestSkill
{
  private Skill _skill = null;

  final int NOT_FOUND = -1;

  // Test skill to use for default
  private final String _skName = "DM'ing";
  private final String _skDesc = "Able to create mazes and challenging adventures";

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    // Error messages are ON at start of each test
    MsgCtrl.errorMsgsOn(true);
    // Audit messages are OFF at start of each test
    MsgCtrl.auditMsgsOn(false);
    // Create an Occupation object, and its mock
    _skill = new Skill(_skName, _skDesc);
  }

  @After
  public void tearDown()
  {
    _skill = null;
    // Audit messages are OFF after each test
    MsgCtrl.auditMsgsOn(false);
  }

  /*
   * BEGIN TESTING
   */

  /**
   * Test the Occupation(String name, String weightCode) ctor
   * 
   * @Normal Skill.Skill(String name, String desc, String action) used in setUp()
   * @Error Skill.Skill(String name, String desc, String action) long desc
   */
  public void testCtorNormalError()
  {
    MsgCtrl.auditMsgsOn(false);
    // turn these off for expected errors now
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.msgln(this, "testCtorNormalError(): ");
    // Create an Occupation and confirm its attributes

    // Normal
    MsgCtrl.msgln("\t" + _skill.getName() + " = " + _skill.getDescription());
    assertTrue(_skill.getName().equals(_skName));
    assertTrue(_skill.getDescription().equals(_skDesc));

    // ERROR
    Skill aSkill = null;
    // Expected exception for overly long description (len = 70)
    String longDesc = "This is a very long description that is testing the Chronos"
        +
        " Exception handling facility of the Skill constructor.";
    try {
      aSkill = new Skill("Skillname", longDesc);
      MsgCtrl.msgln("\t" + aSkill.getName() + " = " + aSkill.getDescription());
      MsgCtrl.errMsgln("\tException not thrown");
      fail();
    } catch (ApplicationException ex) {
      MsgCtrl.errMsgln("\tExpected Exception: " + ex.getMessage());
    }
  }

  /**
   * Test the Occupation(String name, String weightCode) ctor
   * 
   * @Null Skill.Skill(String name, String desc, String action)
   */
  public void testCtorNull() throws ApplicationException
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false); // turn these off for expected errors now
    Skill aSkill = null;

    // NULL name test
    try {
      aSkill = new Skill(null, "Some Desc");
      assertNull(aSkill);
    } catch (ApplicationException ex) {
      MsgCtrl.errMsgln(this, "\tExpected Exception: " + ex.getMessage());
    }
    // NULL Description test
    try {
      aSkill = new Skill("Skillname", null);
      assertNull(aSkill);
    } catch (ApplicationException ex) {
      MsgCtrl.errMsgln(this, "\tExpected Exception: " + ex.getMessage());
    }
    // // NULL action can be null
    // aSkill = new Skill("Skillname", "Skil Description", null);
    // assertTrue(aSkill != null);
    // MsgCtrl.msgln("\t" + aSkill.getName() + " = " + aSkill.getDescription()
    // + " : "
    // + aSkill.getAction());
  }

  @Test
  public void testEquals()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false); // turn these off for expected errors now
    String ts1Name = "DM'ing";
    String ts1Desc = "Able to create mazes and challenging adventures";
    Skill tSkill_1 = null;
    try {
      tSkill_1 = new Skill(ts1Name, ts1Desc);
    } catch (ApplicationException e) {
      MsgCtrl.errMsg(e.getMessage());
      System.exit(0);
    }

    // Normal Test
    assertTrue(_skill.equals(tSkill_1));
  }


  /**
   * Test the conversion from a shuttle to a skill object
   * 
   * @Normal Skill.unpackShuttle (Skill skill) ok
   * @Error Skill.unpackShuttle (Skill skill) ok
   * @Null Skill.unpackShuttle (Skill skill) N/A
   */
  public void testUnpackShuttle()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.msgln(this, "\ttestUnpackShuttle()");

    // NORMAL testing

    // ERROR

    // NULL - N/A

  }

  /**
   * Tests that are not needed for various reasons, mostly setters and getters
   * 
   * @Not_Needed Skill.display() audit method <br>
   * @Not_Needed Skill.new() with 5 parameters, wrapper for constructor <br>
   * @Not_Needed Skill.getAction() simpler getter <br>
   * @Not_Needed Skill.getDescription() simple getter <br>
   * @Not_Needed Skill.getName() simple getter <br>
   * @Not_Needed Skill.getKey() simple getter <br>
   * @Not_Needed Skill.loadShuttle() wrapper for methods tested elsewhere <br>
   * @Not_Needed Skill.unpackShuttle() wrapper for methods tested elsewhere <br>
   */
  public void testNotNeeded()
  {}


} // end of TestSkill class

