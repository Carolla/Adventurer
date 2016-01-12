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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
  static private final String ROOT = "/Projects/eChronos/MyLibrary/src/mylib";
  static private File _root;
  static private FileTree _tree;


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
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    _root = new File(ROOT);
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
    _root = null;
    _tree = null;
  }


  // ======================================================================
  // BEGIN TESTS
  // ======================================================================

  /**
   * @NORMAL.TEST Build a set of source filepaths for each dir
   */
  @Test
  public void testBuildSourcePaths()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    String[] expPaths = {"pdc/TestMetaDie.java", "pdc/TestRegistry.java", "pdc/TestUtilities.java"}; 
    
    ArrayList<String> paths = _tree.buildComparePaths(new File(ROOT + "/pdc"));
    dumpList(paths, "PDC Converted Source Paths");
    assertEquals(3, paths.size());
    for (int k=0; k < expPaths.length; k++) {
      assertTrue(expPaths[k].equals(paths.get(k)));
    }

  }


  /**
   * @NORMAL.TEST Make a list of src and corresponding test files per directory.
   */
  @Test
  public void testGetMyLibDirectories()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SOURCE: mylib has 3 source directories (hic, test, and libResources subdirs are excluded)
    File[] srcDirList = _tree.getSubDirs(_root);
    assertEquals(5, srcDirList.length);
    // Source file names within each directory
    for (File dir : srcDirList) {
      File[] srcFiles = _tree.getFiles(dir);
      dumpFileList(dir, srcFiles, "\nSource files per directory");
    }
    // Get and convert 2 civ source files to test filenames
    File[] srcFiles = _tree.getFiles(srcDirList[0]);
    assertEquals(2, srcFiles.length);
    ArrayList<String> convertedSrc = _tree.convertSourceToTestNames(srcFiles);

    // TEST: mylib.test has 2 subdirs and 1 test file but does not match any source file
    File testDir = new File(ROOT + "/test");
    File[] testDirList = _tree.getSubDirs(testDir);
    assertEquals(2, testDirList.length);
    // Source file names within each directory
    for (File dir : testDirList) {
      File[] testFiles = _tree.getFiles(dir);
      dumpFileList(dir, testFiles, "\nTest files per directory");
    }
  }


  /**
   * @NORMAL.TEST Verify that both the source dir and the test directory exist
   */
  @Test
  public void testMatchDirectories()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // There is a test dir for pdc
    File pdc = new File(ROOT + "/pdc");
    assertTrue(_tree.testDirExists(pdc));

    // There are no civ test directories
    File civ = new File(ROOT + "/civ");
    assertFalse(_tree.testDirExists(civ));

    // There is a test dir for dmc
    File dmc = new File(ROOT + "/dmc");
    assertTrue(_tree.testDirExists(dmc));
  }


  // /**
  // * @NORMAL.TEST Make a list of directories that contain at least one source file, excluding the
  // * HIC directory
  // */
  // @Test
  // public void testGetSourceDirectories()
  // {
  // MsgCtrl.auditMsgsOn(false);
  // MsgCtrl.errorMsgsOn(false);
  // MsgCtrl.where(this);
  //
  // File[] srcDirs = _tree.getSourceDirectories(_root);
  // dumpFileList(_root, srcDirs);
  // assertEquals(3, srcDirs.length);
  // }
  //
  //
  // /**
  // * @NORMAL.TEST Get a list of source files for a given directory
  // */
  // @Test
  // public void testGetSourceFiles()
  // {
  // MsgCtrl.auditMsgsOn(true);
  // MsgCtrl.errorMsgsOn(true);
  // MsgCtrl.where(this);
  //
  // File[] srcList = _tree.getFiles(_root);
  // dumpFileList(_root, srcList);
  // assertEquals(3, srcList.length);
  // }
  //
  // /**
  // * @NORMAL.TEST Get a list of source files for each directory
  // */
  // @Test
  // public void testGetSourceFilesPerDirectory()
  // {
  // MsgCtrl.auditMsgsOn(false);
  // MsgCtrl.errorMsgsOn(false);
  // MsgCtrl.where(this);
  //
  // List<String> allSource = _tree.getSourceFilesPerDirectory(_root);
  // for (String s : allSource) {
  // MsgCtrl.msgln("\tSource paths: " + s);
  // }
  // assertEquals(7, allSource.size());
  // }
  //
  //
  // /**
  // * @NORMAL.TEST Get a list of test files for each directory
  // */
  // @Test
  // public void testGetTestFilesPerDirectory()
  // {
  // MsgCtrl.auditMsgsOn(false);
  // MsgCtrl.errorMsgsOn(false);
  // MsgCtrl.where(this);
  //
  // File testRoot = new File(ROOT + "/test");
  // List<String> testSource = _tree.getSourceFilesPerDirectory(testRoot);
  // for (String s : testSource) {
  // MsgCtrl.msgln("\tTest paths: " + s);
  // }
  // assertEquals(6, testSource.size());
  // }
  //
  //
  // /**
  // * @NORMAL.TEST Compare source files with testfiles
  // */
  // @Test
  // public void testConvertSourceToTestNames()
  // {
  // MsgCtrl.auditMsgsOn(true);
  // MsgCtrl.errorMsgsOn(true);
  // MsgCtrl.where(this);
  //
  // File testRoot = new File(ROOT + "/test");
  // ArrayList<String> testList = _tree.getSourceFilesPerDirectory(testRoot);
  // assertEquals(6, testList.size());
  //
  // ArrayList<String> srcList = _tree.getSourceFilesPerDirectory(_root);
  // assertEquals(7, srcList.size());
  //
  // ArrayList<String> converted = _tree.convertSourceToTestNames(srcList, testList);
  // dumpList(converted);
  // }
  //
  //
  // /**
  // * @NORMAL.TEST Compare converted source files with testfiles
  // */
  // @Test
  // public void testCompareSrcTestFiles()
  // {
  // MsgCtrl.auditMsgsOn(true);
  // MsgCtrl.errorMsgsOn(true);
  // MsgCtrl.where(this);
  //
  // // Get an converted source list from a given source directory
  // File pdcDir = new File(ROOT + "/pdc");
  // File[] dirSrcList = _tree.getFiles(pdcDir);
  // // Get an file list from a given test directory
  // File testPdcDir = new File(ROOT + "/test/pdc");
  // File[] testList = _tree.getFiles(testPdcDir);
  //
  // assertEquals(3, dirSrcList.length);
  // assertEquals(4, testList.length);
  // }
  //

  // List<String> srcWoTests = _tree.getSourceWithoutTests();
  // assertEquals(3, srcWoTests.size());
  // List<String> testsWoSrc = _tree.getTestWithoutSource();
  // assertEquals(2, srcWoTests.size());
  // List<String> matchList = _tree.getMatchList();
  // assertEquals(3, matchList.size());


  // ========================================================================
  // PRIVATE HELPERS
  // ========================================================================

  private void dumpFileList(File root, File[] list, String msg)
  {
    MsgCtrl.msgln(msg);
    for (File f : list) {
      MsgCtrl.msgln("\t" + root.getName() + ": " + f.getName());
    }
  }

  private void dumpList(ArrayList<String> list, String msg)
  {
    MsgCtrl.msgln(msg);
    for (String s : list) {
      MsgCtrl.msgln("\t" + s);
    }
  }

} // end of TestQA_Tool test class
