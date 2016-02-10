/**
 * TestQATool.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.MsgCtrl;
import mylib.pdc.Utilities;
import pdc.QATool;
import pdc.QATool.MockTool;

/**
 * @author Alan Cline
 * @version Dec 31, 2015 // original <br>
 * @version Jan 19 2016 // updated for unchanging file structure instead of live directories <br>
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
  static private final String ROOT_PATH = "/Projects/eChronos/QATool/QA_Tool/simTree/src/chronos";
  /** Use simulated file structure as a read-only test root */
  static private final String TEST_PATH =
      "/Projects/eChronos/QATool/QA_Tool/simTree/src/chronos/test";

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
    _qat = new QATool(ROOT_PATH);
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
   * @NORMAL.TEST Using simTree, a snapshot of ChronosLib, scan for all source file paths
   */
  @Test
  public void testBuildSourceList()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP
    File root = new File(ROOT_PATH);
    int offset = ROOT_PATH.length();

    // RUN: target method returns nothing
    ArrayList<String> srcPaths = _qat.buildSourceList(root, offset);
    assertEquals(srcPaths.size(), allSrcStr.length);

    // VERIFY
    ArrayList<String> paths = _mock.getSrcPaths();
    assertEquals(allSrcStr.length, paths.size());
  }


  /**
   * @NORMAL.TEST Using simTree, a snapshot of ChronosLib, scan for all test file paths
   */
  @Test
  public void testBuildTestList()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP
    File testDir = new File(TEST_PATH);
    int offset = TEST_PATH.length();

    // RUN: target method returns nothing
    _qat.buildTestList(testDir, offset);

    // VERIFY
    ArrayList<String> paths = _mock.getTestPaths();
    assertEquals(allTestsStr.length, paths.size());
  }


  /**
   * @NORMAL.TEST Using simTree, a snapshot of ChronosLib, scan for matching and missing test and
   *              source files
   */
  @Test
  public void testFileScan()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Setup for arraylist
    ArrayList<String> expAllSrc = Utilities.convertToArrayList(allSrcStr);
    ArrayList<String> expAllTests = Utilities.convertToArrayList(allTestsStr);
    ArrayList<String> expMatchingSrc = Utilities.convertToArrayList(matchingSrcStr);
    ArrayList<String> expSrcWoTests = Utilities.convertToArrayList(srcWoTestsStr);
    ArrayList<String> expTestsWoSrc = Utilities.convertToArrayList(testsWoSrcStr);


    // Run test on simulated directory tree
    _qat.fileScan(new File(ROOT_PATH));


    // Verify: for MyLib, there are 10 source files and 4 test files
    ArrayList<String> srcPaths = _mock.getSrcPaths();
    int nbrSrc = srcPaths.size();
    // dumpList(srcPaths, "ALL SOURCE FILES = " + nbrSrc);
    assertEquals(expAllSrc.size(), nbrSrc);

    ArrayList<String> testPaths = _mock.getTestPaths();
    int nbrTests = testPaths.size();
    // dumpList(testPaths, "ALL TEST FILES = " + nbrTests);
    assertEquals(expAllTests.size(), nbrTests);

    ArrayList<String> matched = _mock.getMatched();
    int nbrMatching = matched.size();
    // dumpList(matched, "SOURCE FILE WITH MATCHING TESTS = " + nbrMatching);
    assertEquals(expMatchingSrc.size(), nbrMatching);

    ArrayList<String> srcWoTests = _mock.getSrcWithoutTests();
    int nbrMissingTests = srcWoTests.size();
    // dumpList(srcWoTests, "SRC FILES WITHOUT TESTS = " + nbrMissingTests);
    assertEquals(expSrcWoTests.size(), nbrMissingTests);

    ArrayList<String> testsWoSrc = _mock.getTestsWithoutSrc();
    int nbrMissingSource = testsWoSrc.size();
    // dumpList(testsWoSrc, "TEST FILES WITHOUT SOURCE = " + nbrMissingSource );
    assertEquals(expTestsWoSrc.size(), nbrMissingSource);

    int unaccountedSrc = nbrSrc - nbrMatching - nbrMissingTests;
    int unaccountedTests = nbrTests - nbrMatching - nbrMissingSource;
    assertEquals(0, unaccountedSrc);
    assertEquals(0, unaccountedTests);
  }


  /**
   * @NORMAL.TEST Using simTree, a snapshot of ChronosLib, scan for all source file paths
   */
  @Test
  public void testFindTestDir()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP
    File root = new File(ROOT_PATH);

    // RUN: target method returns nothing
    File testDir = _qat.findTestDir(root);

    // VERIFY
    assertEquals(testDir.getPath(), TEST_PATH);
  }

  
  /**
   * @NORMAL.TEST Build a set of source filepaths for each dir
   */
  @Test
  public void testMatchSrcToTest()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
  
    String[] fakeSrcPaths =
        {"this/dir/Filename.java", "this/dir/File1.java", "this/subir/File2.java"};
  
    String[] fakeTestPaths =
        {"this/dir/TestFilename.java", "this/dir/TestFile1.java", "this/subir/File2.java",
            "this/subir2/TestFile2.java"};
  
    // Setup for arraylist
    ArrayList<String> fakeSrc = new ArrayList<String>();
    for (int k = 0; k < fakeSrcPaths.length; k++) {
      fakeSrc.add(fakeSrcPaths[k]);
    }
    ArrayList<String> fakeTests = new ArrayList<String>();
    for (int k = 0; k < fakeTestPaths.length; k++) {
      fakeTests.add(fakeTestPaths[k]);
    }
    // Find matching test files for given source files
    _qat.matchSrcToTest(fakeSrc, fakeTests);
    ArrayList<String> matched = _mock.getMatched();
    dumpList(matched, "MATCHED SOURCE FILES = " + matched.size());
    assertEquals(2, matched.size());
  
    ArrayList<String> srcWithoutTests = _mock.getSrcWithoutTests();
    dumpList(srcWithoutTests, "SOURCE WITHOUT TESTS = " + srcWithoutTests.size());
    assertEquals(2, matched.size());
  
    ArrayList<String> testsWithoutSrc = _mock.getTestsWithoutSrc();
    dumpList(testsWithoutSrc, "TESTS WITHOUT SOURCE FILES = " + testsWithoutSrc.size());
    assertEquals(2, matched.size());
  }

  
  // ======================================================================
  // PRIVATE HELPERS
  // ======================================================================

//  /** Convert a String[] to a ArrayList<String> for easier handling */
//  private ArrayList<String> convertToArrayList(String[] strs)
//  {
//    // Setup for arraylist
//    ArrayList<String> alist = new ArrayList<String>();
//    for (int k = 0; k < strs.length; k++) {
//      alist.add(strs[k]);
//    }
//    return alist;
//  }

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
