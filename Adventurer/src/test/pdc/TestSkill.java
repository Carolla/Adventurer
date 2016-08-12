/**
 * TestSkill.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc;

import static org.junit.Assert.assertTrue;

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
  private Skill _skill;

  private final String _skName = "DM'ing";
  private final String _skDesc = "Able to create mazes and challenging adventures";

  @Before
  public void setUp()
  {
    _skill = new Skill(_skName, _skDesc);
  }

  @After
  public void tearDown()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  @Test(expected = ApplicationException.class)
  public void testCtorNull()
  {
    new Skill(null, "Some Desc");
  }

  @Test(expected = ApplicationException.class)
  public void testCtorNullDescription()
  {
    new Skill("Skillname", null);
  }

  @Test
  public void testEquals()
  {
    Skill tSkill_1 = new Skill(_skName, _skDesc);
    assertTrue(_skill.equals(tSkill_1));
  }
} // end of TestSkill class

