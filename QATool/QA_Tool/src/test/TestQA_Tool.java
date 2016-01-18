/**
 * TestQA_Tool.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.MsgCtrl;
import pdc.FileTree;

/**
 * @author Alan Cline
 * @version Dec 31, 2015 // original <br>
 */
public class TestQA_Tool
{
  /** Use MyLibrary file structure as a read-only test root */
  static private final String MYLIB_ROOT = "/Projects/eChronos/MyLibrary/src/mylib";
  static private final String CHRONOSLIB_ROOT = "/Projects/eChronos/ChronosLib/src/chronos";
  static private FileTree _tree;

  // private final int MYLIB_SRC = 10;
  // private final int MYLIB_TEST = 7;


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
    _tree = new FileTree();
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _tree = null;
  }


  // ======================================================================
  // BEGIN TESTS
  // ======================================================================

  // /**
  // * @NORMAL.TEST Get all source files under the given root dir
  // */
  // @Test
  // public void testBuildSourceList()
  // {
  // MsgCtrl.auditMsgsOn(false);
  // MsgCtrl.errorMsgsOn(false);
  // MsgCtrl.where(this);
  //
  // File root = new File(ROOT);
  // int offset = root.getPath().length();
  // _tree.buildSourceList(new File(ROOT), offset);
  // ArrayList<String> srcList = _tree.getSrcPaths();
  // dumpList(srcList, "SOURCE FILES");
  // assertEquals(MYLIB_SRC, srcList.size());
  // }


  // /**
  // * @NORMAL.TEST Get all test files under the test subdir of the given root dir
  // */
  // @Test
  // public void testBuildTestList()
  // {
  // MsgCtrl.auditMsgsOn(false);
  // MsgCtrl.errorMsgsOn(false);
  // MsgCtrl.where(this);
  //
  // File testDir = _tree.getTestDir(new File(ROOT));
  // int offset = testDir.getPath().length();
  // assertNotNull(testDir);
  // _tree.buildTestList(testDir, offset);
  // ArrayList<String> testList = _tree.getTestPaths();
  // dumpList(testList, "TEST FILES");
  // assertEquals(MYLIB_TEST, testList.size());
  // }


  /**
   * @NORMAL.TEST Using ChronosLib, scan for matching and missing test and source files
   */
  @Test
  public void testChronosLibFileScan()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Define root directory for file comparisons
    File root = new File(CHRONOSLIB_ROOT);

    // Setup expected comparisons paths
    final String[] allSrcStr =
        {"Chronos.java", "civ/DefaultLists.java", "civ/DefaultUserMsg.java",
            "civ/HeroDefaults.java", "civ/OccupationKeys.java", "civ/PersonKeys.java",
            "civ/SkillKeys.java", "civ/UserMsg.java", "pdc/buildings/Bank.java",
            "pdc/buildings/Building.java", "pdc/buildings/ClericsGuild.java",
            "pdc/buildings/FightersGuild.java", "pdc/buildings/Inn.java", "pdc/buildings/Jail.java",
            "pdc/buildings/RoguesGuild.java", "pdc/buildings/Store.java",
            "pdc/buildings/WizardsGuild.java", "pdc/character/Cleric.java",
            "pdc/character/Fighter.java", "pdc/character/Hero.java", "pdc/character/Inventory.java",
            "pdc/character/Klass.java", "pdc/character/Thief.java", "pdc/character/Wizard.java",
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
        {"pdc/buildings/BuildingsSuite.java", "pdc/buildings/ConcreteBuilding.java",
            "pdc/buildings/FakeBuilding.java", "pdc/buildings/TestArena.java",
            "pdc/buildings/TestBank.java", "pdc/buildings/TestBuilding.java",
            "pdc/buildings/TestClericsGuild.java",
            "pdc/buildings/TestFightersGuild.java", "pdc/buildings/TestInn.java",
            "pdc/buildings/TestJail.java", "pdc/buildings/TestRgouesGuild.java",
            "pdc/buildings/TestStore.java", "pdc/buildings/WizardsGuild.java",
            "pdc/character/CharacterSuite.java", "pdc/command/EventTest.java",
            "pdc/command/FakeScheduler.java", "pdc/registry/FakeRegistry.java",
            "pdc/registry/RegistrySuite.java", "pdc/registry/TestAdventureRegistry.java",
            "pdc/registry/TestRegistryFactory.java",
            "pdc/FakeSkill.java", "pdc/FormatSample.java", "pdc/MockRace.java",
            "pdc/TestAdventure.java", "pdc/TestItem.java", "pdc/TestNPC.java",
            "pdc/TestOccupation.java", "pdc/TestRace.java", "pdc/TestSkill.java",
            "pdc/TestTown.java", "ChronosTestSuite.java", "Template.java"
        };
    final String[] matchingSrcStr =
        {"pdc/Adventure.java", "pdc/buildings/Bank.java", "pdc/buildings/Building.java",
            "pdc/buildings/ClericsGuild.java", "pdc/buildings/FightersGuild.java",
            "pdc/buildings/Inn.java", "pdc/buildings/Jail.java", "pdc/buildings/RoguesGuild.java",
            "pdc/buildings/Store.java", "pdc/buildings/WizardsGuild.java",
            "pdc/registry/TestAdventureRegistry.java", "pdc/registry/TestRegistryFactory.java",
            "pdc/Item.java", "pdc/NPC.java", "pdc/OccupationC.java", "pdc/Race.java",
            "pdc/Skill.java", "pdc/Town.java"
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
        {"ChronosTestSuite.java",
            "pdc/buildings/BuildingsSuite.java", "pdc/buildings/ConcreteBuilding.java",
            "pdc/buildings/FakeBuilding.java", "pdc/buildings/TestArena.java",
            "pdc/character/CharacterSuite.java",
            "pdc/command/EventTest.java", "pdc/command/FakeScheduler.java",
            "pdc/registry/FakeRegistry.java", "pdc/registry/RegistrySuite.java",
            "pdc/FakeSkill.java", "pdc/FormatSample", "pdc/MockRace.java",
            "Template.java"
        };

    // Setup for arraylist
    ArrayList<String> expAllSrc = convertToArrayList(allSrcStr);
    ArrayList<String> expAllTests = convertToArrayList(allTestsStr);
    ArrayList<String> expMatchingSrc = convertToArrayList(matchingSrcStr);
    ArrayList<String> expSrcWoTests = convertToArrayList(srcWoTestsStr);
    ArrayList<String> expTestsWoSrc = convertToArrayList(testsWoSrcStr);


    // RUN an entire directory from root
    _tree.qaFileScan(root);

    // Verify: for MyLib, there are 10 source files and 4 test files
    ArrayList<String> srcPaths = _tree.getSrcPaths();
    dumpList(srcPaths, "ALL SOURCE FILES = " + srcPaths.size());
    assertEquals(expAllSrc.size(), srcPaths.size());

    ArrayList<String> testPaths = _tree.getTestPaths();
    dumpList(testPaths, "ALL TEST FILES = " + testPaths.size());
    assertEquals(expAllTests.size(), testPaths.size());

    ArrayList<String> matched = _tree.getMatched();
    dumpList(matched, "SOURCE FILE WITH MATCHING TESTS = " + matched.size());
    assertEquals(expMatchingSrc.size(), matched.size());

    ArrayList<String> srcWoTests = _tree.getSrcWithoutTests();
    dumpList(srcWoTests, "SRC FILES WITHOUT TESTS = " + srcWoTests.size());
    assertEquals(expSrcWoTests.size(), srcWoTests.size());

    ArrayList<String> testsWoSrc = _tree.getTestsWithoutSrc();
    dumpList(testsWoSrc, "TEST FILES WITHOUT SOURCE = " + testsWoSrc.size());
    assertEquals(expTestsWoSrc.size(), testsWoSrc.size());
  }


  /**
   * @NORMAL.TEST Using MyLib, scan for matching and missing test and source files
   */
  @Test
  public void testMyLibraryFileScan()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Define root directory for file comparisons
    File root = new File(MYLIB_ROOT);

    // Setup expected comparisons paths
    final String[] allSrcStr =
        {"ApplicationException.java", "Constants.java", "MsgCtrl.java", "civ/BaseCiv.java",
            "civ/DataShuttle.java", "dmc/DbReadWriter.java", "dmc/IRegistryElement.java",
            "pdc/MetaDie.java", "pdc/Registry.java", "pdc/Utilities.java"};
    final String[] allTestsStr =
        {"MyLibraryTestSuite.java", "dmc/oldSomeObject.java", "dmc/TestDbReadWriter",
            "pdc/ConcreteRegistry.java", "pdc/TestMetaDie.java", "pdc/TestRegistry.java",
            "pdc/TestUtilities.java"};

    final String[] matchingSrcStr =
        {"dmc/DbReadWriter.java", "pdc/MetaDie.java", "pdc/Registry.java", "pdc/Utilities.java"};
    final String[] srcWoTestsStr =
        {"ApplicationException.java", "Constants.java", "MsgCtrl.java", "civ/BaseCiv.java",
            "civ/DataShuttle.java", "dmc/IRegistryElement.java"};
    final String[] testsWoSrcStr =
        {"MyLibraryTestSuite.java", "dmc/oldSomeObject.java", "pdc/ConcreteRegistry.java"};

    // Setup for arraylist
    ArrayList<String> expAllSrc = convertToArrayList(allSrcStr);
    ArrayList<String> expAllTests = convertToArrayList(allTestsStr);
    ArrayList<String> expMatchingSrc = convertToArrayList(matchingSrcStr);
    ArrayList<String> expSrcWoTests = convertToArrayList(srcWoTestsStr);
    ArrayList<String> expTestsWoSrc = convertToArrayList(testsWoSrcStr);


    // RUN an entire directory from root
    _tree.qaFileScan(root);

    // Verify: for MyLib, there are 10 source files and 4 test files
    ArrayList<String> srcPaths = _tree.getSrcPaths();
    dumpList(srcPaths, "ALL SOURCE FILES = " + srcPaths.size());
    assertEquals(expAllSrc.size(), srcPaths.size());

    ArrayList<String> testPaths = _tree.getTestPaths();
    dumpList(testPaths, "ALL TEST FILES = " + testPaths.size());
    assertEquals(expAllTests.size(), testPaths.size());

    ArrayList<String> matched = _tree.getMatched();
    dumpList(matched, "SOURCE FILE WITH MATCHING TESTS = " + matched.size());
    assertEquals(expMatchingSrc.size(), matched.size());

    ArrayList<String> srcWoTests = _tree.getSrcWithoutTests();
    dumpList(srcWoTests, "SRC FILES WITHOUT TESTS = " + srcWoTests.size());
    assertEquals(expSrcWoTests.size(), srcWoTests.size());

    ArrayList<String> testsWoSrc = _tree.getTestsWithoutSrc();
    dumpList(testsWoSrc, "TEST FILES WITHOUT SOURCE = " + testsWoSrc.size());
    assertEquals(expTestsWoSrc.size(), testsWoSrc.size());
  }


  /**
   * @NORMAL.TEST Build a set of source filepaths for each dir
   */
  @Test
  public void testMatchSrcToTest()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
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
    _tree.matchSrcToTest(fakeSrc, fakeTests);
    ArrayList<String> matched = _tree.getMatched();
    dumpList(matched, "MATCHED SOURCE FILES = " + matched.size());
    assertEquals(2, matched.size());

    ArrayList<String> srcWithoutTests = _tree.getSrcWithoutTests();
    dumpList(srcWithoutTests, "SOURCE WITHOUT TESTS = " + srcWithoutTests.size());
    assertEquals(2, matched.size());

    ArrayList<String> testsWithoutSrc = _tree.getTestsWithoutSrc();
    dumpList(testsWithoutSrc, "TESTS WITHOUT SOURCE FILES = " + testsWithoutSrc.size());
    assertEquals(2, matched.size());
  }


  /**
   * @NORMAL.TEST Build a set of source filepaths for each dir
   */
  @Test
  public void testBuildComparePaths()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // ArrayList<String> srcPaths = new ArrayList<String>();
    // ArrayList<String> testPaths = new ArrayList<String>();
    //
    // // _tree.buildComparePaths(new File(ROOT), srcPaths, testPaths);
    // _tree.buildComparePaths(new File(ROOT), ROOT.length());
    // srcPaths = _tree.getSrcPaths();
    // assertEquals(MYLIB_SRC, srcPaths.size());
    // dumpList(srcPaths, "SOURCE FILES");
    // testPaths = _tree.getTestPaths();
    // assertEquals(MYLIB_TEST, testPaths.size());
    // dumpList(testPaths, "TEST FILES");
  }


  // ========================================================================
  // PRIVATE HELPERS
  // ========================================================================

  /** Convert a String[] to a ArrayList<String> for easier handling */
  private ArrayList<String> convertToArrayList(String[] strs)
  {
    // Setup for arraylist
    ArrayList<String> alist = new ArrayList<String>();
    for (int k = 0; k < strs.length; k++) {
      alist.add(strs[k]);
    }
    return alist;
  }


  private void dumpList(ArrayList<String> plist, String msg)
  {
    MsgCtrl.msgln("\t" + msg);
    for (int k = 0; k < plist.size(); k++) {
      String s = plist.get(k);
      MsgCtrl.msgln("\t\t" + s);
    }
  }


} // end of TestQA_Tool test class
