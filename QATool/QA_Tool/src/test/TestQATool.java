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
 * @version Mar 8 2016 // removed simTree and debugged platform-independency <br>
 */
public class TestQATool
{

  /** Root for all source files and subdirectories */
  static private final String ROOT =
      System.getProperty("user.dir") + Constants.FS + "src" + Constants.FS;
  /** Exlusion file must be directly beneath src root */
  static private final String EXCLUDE_FILE = "ScanExclusions.txt";

  static private QATool _qat;
  static private MockTool _mock;

  private String[] expPaths = {"subDir" + Constants.FS + "TestSubDirSource.java",
      "TestFileMap.java", "TestTestTemplate.java", "TestPrototype.java", "TestQATool.java"
  };
  private ArrayList<File> _files;


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
    _qat = new QATool(ROOT, EXCLUDE_FILE);
    assertNotNull(_qat);
    _mock = _qat.new MockTool();
    assertNotNull(_mock);
    assertNotNull(_mock.getRoot());

    // Create a list of test files
    _files = new ArrayList<File>();
    for (String s : expPaths) {
      _files.add(new File(ROOT + "test/pdc/" + s));
    }
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
      QATool badTool1 = new QATool(null, null);
    } catch (IllegalArgumentException ex) {
      MsgCtrl.msgln("\ttestCtorError: expected exception caught");
      assertNotNull(ex);
    }
    // Exclusion path is set to null
    try {
      IllegalArgumentException ex = null;
      QATool badTool1 = new QATool(ROOT, null);
    } catch (IllegalArgumentException ex) {
      MsgCtrl.msgln("\ttestCtorError: expected exception caught");
      assertNotNull(ex);
    }

    // Path is not set to a directory
    try {
      IllegalArgumentException ex = null;
      String filePath = ROOT + Constants.FS + "Chronos" + Constants.FS + "civ + Constants.FS" +
          "UserMsg.java";
      QATool badTool = new QATool(filePath, EXCLUDE_FILE);
    } catch (IllegalArgumentException ex) {
      MsgCtrl.msgln("\ttestCtorError: expected exception caught");
      assertNotNull(ex);
    }
  }


  /**
   * @NORMAL.TEST File findTestDir(File root)
   */
  @Test
  public void testFindTestDir()
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
  }


  /**
   * @ERROR.TEST File findTestDir(File root)
   */
  @Test
  public void testFindTestDirBadPathError()
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

    // Error case for a testdir not found
    String simRoot = System.getProperty("user.dir");
    StringBuilder sb = new StringBuilder(simRoot);
    sb.append(Constants.FS);
    sb.append("simTree"); // has not test subdir
    String srcPath = sb.toString();
    MsgCtrl.msgln("\tFindTestDir(); simulated source root = " + srcPath);

    assertNull(_qat.findTestDir(new File(srcPath)));
  }


  /**
   * @NORMAL.TEST ArrayList<String> writeNextTestFile(File srcDir, File testDir, String rootPath)
   */
  @Test
  public void testWriteNextTestFile()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP to traverse the src tree
    MsgCtrl.msgln("\tnavigatinf " + ROOT);
    File srcDir = new File(ROOT);
    File testDir = _qat.findTestDir(srcDir);
    int srcLen = srcDir.getPath().length();

    // Run the target method
    ArrayList<String> srcPaths = _qat.writeNextTestFile(srcDir, testDir, srcLen);
    for (String s : srcPaths) {
      MsgCtrl.msgln("\t" + s);
    }
  }


  /**
   * @throws InterruptedException
   * @NORMAL.TEST void treeScan(File srcDir)
   */
  @Test
  public void testTreeScan() throws InterruptedException
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // RUN
    _qat.treeScan(new File(ROOT));

    // VERIFY
    for (File f : _files) {
      MsgCtrl.msgln("\t " + f.getPath() + "\t\t\t" + f.length());
      assertTrue(f.exists());
    }

    // TEARDOWN Remove test/pdc tree
    for (File f : _files) {
      assertTrue(f.delete());
    }
    // Remove the new subdirectories
    new File(ROOT + "test/pdc/subDir").delete();
    new File(ROOT + "test/pdc").delete();
  }
} // end of TestQA_Tool test class
