/**
 * TestQATool.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.Constants;
import mylib.MsgCtrl;
import pdc.QATool;
import pdc.QATool.MockTool;

/**
 * @author Alan Cline
 * @version Dec 31, 2015 // original <br>
 * @version Jan 19 2016 // updated for unchanging file structure instead of live directories <br>
 * @version Feb 23 2016 // updated for unchanging simTree structure and be platform-independent <br>
 */
public class TestQATool
{
  // Setup expected comparisons paths
  final String[] allSrcStr =
      {"Chronos.java", "civ/DefaultLists.java", "civ/DefaultUserMsg.java",
          "civ/HeroDefaults.java", "civ/OccupationKeys.java", "civ/PersonKeys.java",
          "civ/SkillKeys.java", "civ/UserMsg.java",
          "pdc/buildings/Bank.java", "pdc/buildings/Building.java",
          "pdc/buildings/ClericsGuild.java", "pdc/buildings/FightersGuild.java",
          "pdc/buildings/Inn.java", "pdc/buildings/Jail.java",
          "pdc/buildings/RoguesGuild.java", "pdc/buildings/Store.java",
          "pdc/buildings/WizardsGuild.java",
          "pdc/character/Cleric.java", "pdc/character/Fighter.java", "pdc/character/Hero.java",
          "pdc/character/Inventory.java", "pdc/character/Klass.java", "pdc/character/Thief.java",
          "pdc/character/Wizard.java",
          "pdc/command/Command.java", "pdc/command/DeltaCmdList.java", "pdc/command/Event.java",
          "pdc/command/EventTest.java", "pdc/command/intCmdPatronEnter.java",
          "pdc/command/intCmdPatronLeave.java", "pdc/command/NullCommand.java",
          "pdc/command/Scheduler.java", "pdc/command/TestEvent.java",
          "pdc/registry/AdventureRegistry.java", "pdc/registry/BuildingeRegistry.java",
          "pdc/registry/HelpTextObject.java", "pdc/registry/HeroRegistry.java",
          "pdc/registry/ItemRegistry.java", "pdc/registry/NPCRegistry.java",
          "pdc/registry/OccupationRegistry.java", "pdc/registry/RegistryFactory.java",
          "pdc/registry/SkillRegistry.java", "pdc/registry/TownRegistry.java",
          "pdc/Adventure.java", "pdc/Arena.java", "pdc/AttributeList.java",
          "pdc/GameClock.java", "pdc/Item.java", "pdc/MiscKeys.java",
          "pdc/NPC.java", "pdc/NullNPC.java", "pdc/Occupation.java", "pdc/Race.java",
          "pdc/Skill.java", "pdc/Town.java",
      };
  final String[] allTestsStr =
      {"ChronosTestSuite.java", "Template.java",
          "pdc/buildings/BuildingsSuite.java", "pdc/buildings/ConcreteBuilding.java",
          "pdc/buildings/FakeBuilding.java", "pdc/buildings/TestArena.java",
          "pdc/buildings/TestBank.java", "pdc/buildings/TestBuilding.java",
          "pdc/buildings/TestClericsGuild.java", "pdc/buildings/TestFightersGuild.java",
          "pdc/buildings/TestInn.java", "pdc/buildings/TestJail.java",
          "pdc/buildings/TestRgouesGuild.java", "pdc/buildings/TestStore.java",
          "pdc/buildings/WizardsGuild.java",
          "pdc/command/EventTest.java", "pdc/command/FakeScheduler.java",
          "pdc/registry/FakeRegistry.java", "pdc/registry/RegistrySuite.java",
          "pdc/registry/TestAdventureRegistry.java", "pdc/registry/TestRegistryFactory.java",
          "pdc/FakeSkill.java", "pdc/FormatSample.java", "pdc/MockRace.java",
          "pdc/TestAdventure.java", "pdc/TestItem.java", "pdc/TestNPC.java",
          "pdc/TestOccupation.java", "pdc/TestRace.java", "pdc/TestSkill.java",
          "pdc/TestTown.java"
      };
  final String[] matchingSrcStr =
      {"pdc/buildings/Bank.java", "pdc/buildings/Building.java",
          "pdc/buildings/ClericsGuild.java", "pdc/buildings/FightersGuild.java",
          "pdc/buildings/Inn.java", "pdc/buildings/Jail.java", "pdc/buildings/RoguesGuild.java",
          "pdc/buildings/Store.java", "pdc/buildings/WizardsGuild.java",
          "pdc/registry/TestAdventureRegistry.java", "pdc/registry/TestRegistryFactory.java",
          "pdc/Adventure.java", "pdc/Item.java", "pdc/NPC.java", "pdc/Occupation.java",
          "pdc/Race.java", "pdc/Skill.java", "pdc/Town.java"

  };
  final String[] srcWoTestsStr =
      {"civ/DefaultLists.java", "civ/DefaultUserMsg.java", "civ/HeroDefaults.java",
          "civ/OccupationKeys.java", "civ/PersonKeys.java", "civ/SkillKeys.java",
          "civ/UserMsg.java",
          "pdc/character/Cleric.java", "pdc/character/Fighter.java", "pdc/character/Hero.java",
          "pdc/character/Inventory.java", "pdc/character/Klass.java", "pdc/character/Thief.java",
          "pdc/character/Wizard.java",
          "pdc/command/Command.java", "pdc/command/DeltaCmdList.java", "pdc/command/Event.java",
          "pdc/command/Event.java", "pdc/command/intCmdPatronEnter.java",
          "pdc/command/intCmdPatronLeave.java", "pdc/command/NullCommand.java",
          "pdc/command/Scheduler.java", "pdc/command/TestEvent.java",
          "pdc/registry/BuildingeRegistry.java", "pdc/registry/HelpTextObject.java",
          "pdc/registry/HeroRegistry.java", "pdc/registry/ItemRegistry.java",
          "pdc/registry/NPCRegistry.java", "pdc/registry/OccupationRegistry.java",
          "pdc/registry/SkillRegistry.java", "pdc/registry/TownRegistry.java",
          "pdc/Arena.java", "pdc/AttibuteList.java", "pdc/GameClock.java",
          "pdc/MiscKeys", "pdc/NullNPC.java", "Chronos.java"
      };
  final String[] testsWoSrcStr =
      {"ChronosTestSuite.java", "Template.java",
          "pdc/buildings/BuildingsSuite.java", "pdc/buildings/ConcreteBuilding.java",
          "pdc/buildings/FakeBuilding.java", "pdc/buildings/TestArena.java",
          "pdc/command/EventTest.java", "pdc/command/FakeScheduler.java",
          "pdc/registry/FakeRegistry.java", "pdc/registry/RegistrySuite.java",
          "pdc/FakeSkill.java", "pdc/FormatSample", "pdc/MockRace.java"
      };

  /** Use simulated file structure as a read-only source root */
  static private final String ROOT = System.getProperty("user.dir") + "/src/";

  static private QATool _qat;
  static private MockTool _mock;

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
    /** Create QA Tool using simulated directory structure */
    _qat = new QATool(ROOT);
    assertNotNull(_qat);
    _mock = _qat.new MockTool();
    assertNotNull(_mock);
    assertNotNull(_mock.getRoot());
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _mock = null;
    _qat = null;
  }


  // ======================================================================
  // BEGIN TESTS
  // ======================================================================


  /**
   * @ERROR.TEST QATool(String path)
   */
  @SuppressWarnings("unused")
  @Test
  public void ctorNullParmError()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Path is set to null
    try {
      IllegalArgumentException ex = null;
      QATool badTool = new QATool(null);
    } catch (IllegalArgumentException ex) {
      MsgCtrl.msgln("\ttestCtorError: expected exception caught");
      assertNotNull(ex);
    }
    // Path is not set to a directory
    try {
      IllegalArgumentException ex = null;
      String filePath = ROOT + Constants.FS + "Chronos" + Constants.FS + "civ + Constants.FS" +
          "UserMsg.java";
      QATool badTool = new QATool(filePath);
    } catch (IllegalArgumentException ex) {
      MsgCtrl.msgln("\ttestCtorError: expected exception caught");
      assertNotNull(ex);
    }
  }


  /**
   * @NORMAL.TEST File findTestDir(File root)
   */
  @Test
  public void findTestDir()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Normal case for current directory
    String expPath = ROOT + "test";
    MsgCtrl.msgln("\ttestFindTestDir: expPath = \t" + expPath);
    File tf = _qat.findTestDir(new File(ROOT));
    String resultPath = tf.getPath();
    MsgCtrl.msgln("\ttestFindTestDir: result = \t" + resultPath);
    assertTrue(resultPath.equals(expPath));

    // Normal case for simTree directory
    String simRoot = System.getProperty("user.dir");
    StringBuilder sb = new StringBuilder(simRoot);
    sb.append(Constants.FS);
    sb.append("simTree");
    sb.append(Constants.FS);
    sb.append("src");
    sb.append(Constants.FS);
    String srcPath = sb.toString();
    MsgCtrl.msgln("\tFindTestDir(); simulated source root = " + srcPath);

    expPath = srcPath + "test";  
    MsgCtrl.msgln("\ttestFindTestDir: expPath = \t" + expPath);
    tf = _qat.findTestDir(new File(srcPath));
    resultPath = tf.getPath();
    MsgCtrl.msgln("\ttestFindTestDir: result = \t" + resultPath);
    assertTrue(resultPath.equals(expPath));
  }

  
  /**
   * @ERROR.TEST File findTestDir(File root)
   */
  @Test
  public void findTestDirBadPathError()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
  
    // Error case: cannot find a test dir
    String simPath2 = ROOT + "deadend";
    File tf = _qat.findTestDir(new File(simPath2));
    assertNull(tf);
  
    // Error return null when testDir is found after it is found once
    // First find normal testdir before attempting second one
    tf = _qat.findTestDir(new File(ROOT));
    assertNotNull(tf);
    tf = _qat.findTestDir(new File(simPath2));
    assertNull(tf);
  }

  /**
   * @ERROR.TEST File findTestDir(File root)
   */
  @Test
  public void testDirNotFoundError()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Error case for a testdir not found using simTree
    String simRoot = System.getProperty("user.dir");
    StringBuilder sb = new StringBuilder(simRoot);
    sb.append(Constants.FS);
    sb.append("simTree");     // has not test subdir
    String srcPath = sb.toString();
    MsgCtrl.msgln("\tFindTestDir(); simulated source root = " + srcPath);

    assertNull(_qat.findTestDir(new File(srcPath)));
  }

  
  /**
   * @NORMAL.TEST ArrayList<String> writeNextTestFile(File srcDir, File testDir, String rootPath)
   */
  @Test
  public void writeNextTestFile()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP to traverse the simtree
    String simTreePath =
        System.getProperty("user.dir") + Constants.FS + "simTree" + Constants.FS + "src";
    MsgCtrl.msgln("\tsimTreePath = " + simTreePath);
    File srcDir = new File(simTreePath);
    File testDir = _qat.findTestDir(srcDir);
    int srcLen = srcDir.getPath().length();

    // Run the target method
      ArrayList<String> srcPaths = _qat.writeNextTestFile(srcDir, testDir, srcLen);
      
      for (String s : srcPaths) {
        MsgCtrl.msgln("\t" + s);
      }
  }


  /**
   * @NORMAL.TEST void treeScan(File srcDir)
   */
  @Test
  public void treeScan()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    _qat.treeScan(new File(ROOT));
  }


  // ======================================================================
  // PRIVATE HELPERS
  // ======================================================================

  // /** Convert a String[] to a ArrayList<String> for easier handling */
  // private ArrayList<String> convertToArrayList(String[] strs)
  // {
  // // Setup for arraylist
  // ArrayList<String> alist = new ArrayList<String>();
  // for (int k = 0; k < strs.length; k++) {
  // alist.add(strs[k]);
  // }
  // return alist;
  // }


  /** Display the contents of an arraylist, with intro message */
  private void dumpList(ArrayList<String> plist, String msg)
  {
    MsgCtrl.msgln("\t" + msg);
    for (int k = 0; k < plist.size(); k++) {
      String s = plist.get(k);
      MsgCtrl.msgln("\t\t" + s);
    }
  }


} // end of TestQA_Tool test class
