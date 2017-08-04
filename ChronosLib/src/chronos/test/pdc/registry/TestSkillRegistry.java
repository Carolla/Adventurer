/**
 * TestSkillRegistry.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.test.pdc.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.Skill;
import chronos.pdc.registry.SkillRegistry;
import mylib.MsgCtrl;


/**
 * Make sure all the right skills are there.
 */
public class TestSkillRegistry
{
  // private static final String[] skills = {"General Knowledge", "Concentration", "Diplomacy",
  // "Climb Walls", "Balance", "Escape Artist", "Jump", "Tumble", "Arcane Knowledge",
  // "Natural Knowledge", "Repair Armor", "Financial Brokering", "Appraise Jewelry", "Bowmaking",
  // "Find Secrets in Woodwork", "Identify Plants", "Predict Weather", "Netmaking", "Fast Swim",
  // "Hide in Shadows", "Move Silently", "Wilderness Lore", "Intuit Outdoor Direction",
  // "Spot Details", "Negotiations", "Cargo Transport", "Train Animals", "Luck", "Pick Pockets",
  // "Open Locks", "Bluff", "Sense Motive", "Hunting", "Find/Set Traps", "Move Silently",
  // "Hide in Shadows", "Spot Details", "Intimidate", "Listening", "Husbandry", "Animal Empathy",
  // "Train Animals", "Negotiations", "Sense Motive", "Gather Information", "Read Lips",
  // "Appraise Jewelry", "Open Locks", "Leatherworking", "Painting", "Gather Information",
  // "Find Secrets in Stonework", "Intuit Underground Direction", "Cavern Lore",
  // "Find Secrets in Stonework", "Predict Weather", "Water Lore", "Intuit Outdoor Direction",
  // "Spot Details", "Fast Swim", "Make Raft", "Fast Swim", "Make Raft", "Fast Swim", "Sewing",
  // "Gather Information", "Financial Brokering", "Sense Motive", "Diplomacy", "Trapping",
  // "Find/Set Traps", "Move Silently", "Open Locks", "Hide in Shadows", "Spot Details",
  // "Wilderness Lore", "Disable Device Skill", "Make Weapons", "Appraise Tapestries",
  // "Woodworking", "Find Secrets in Woodwork", "Disable Device Skill"}; // , "No special skills"};

  /** Create the Skill Registry once for all tests, as default */
  static private SkillRegistry _skReg;


  @BeforeClass
  public static void setUpBeforeClass()
  {
    // Create the registry
    _skReg = new SkillRegistry();
    assertNotNull(_skReg);
  }

  @AfterClass
  public static void tearDownAfterClass()
  {
    _skReg = null;
  }

  @Before
  public void setUp()
  {}

  @After
  public void tearDown()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // =============================================================================
  // CONSTRUCTOR TESTS
  // =============================================================================

  /**
   * @Normal.Test SkillRegistry() -- constructor is created properly
   */
  @Test
  public void testCtor()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Confirm it has the right number of skills in it
    int nbrSkills = _skReg.size();
    MsgCtrl.msgln("\t Skill Registry contains " + nbrSkills + " skills");
    ArrayList<Skill> skillList = (ArrayList<Skill>) _skReg.getAll();
    assertEquals("Archery", skillList.get(0).getName());
    assertEquals("Animal Empathy", skillList.get(8).getName());
    assertEquals("No special skills", skillList.get(nbrSkills - 1).getName());
  }


  /*
   * TODO Currently, the in-memory table is initialized for each ctor call. In the future, it must
   * be read once from a file and probably be a singleton
   */
  /**
   * @Normal.Test SkillRegistry() -- constructor should be created only once, but for now, multiple
   *              initializations will be sufficient
   */
  @Test
  public void testCtor_SingleInstance()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SkillRegisty should be created only once
//    SkillRegistry otherSkReg = new SkillRegistry();
//    MsgCtrl.msgln("\t Original skill registry = " + _skReg);
//    MsgCtrl.msgln("\t Second skill registry = " + otherSkReg);
//    assertEquals(_skReg, otherSkReg);
  }


  // =============================================================================
  // BEGIN TESTING
  // =============================================================================

  /**
   * @Not.Needed void initialize() -- tested in the ctor testing
   */
  public void testToString()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.PRIMITIVE);
  }


  /**
   * @Not.Needed Skill getSkill(String name) -- getter
   */
  public void testGetSkill()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }

  
  /**
   * @Not.Needed List<Skill> getSkillList() -- getter tested in ctor tests
   */
  public void testGetSkillList()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


} // end of TestSkillRegistry
