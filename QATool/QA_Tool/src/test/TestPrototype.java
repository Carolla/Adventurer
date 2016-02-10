/**
 * TestPrototype.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.MsgCtrl;
import pdc.Prototype;
import pdc.Prototype.MockPrototype;
import pdc.QATool;

/**
 * @author Alan Cline
 * @version Jan 29, 2016 // original <br>
 */
public class TestPrototype
{
  /** Root for all source files and subdirectories */
  private final String ROOT = System.getProperty("user.dir") + "/src";
  /** Name of test prototype file generated */
  private final String PROTO_NAME =
      "/Projects/eChronos/QATool/QA_Tool/src/test/pdc/TestFileMap.java";
  private final String SRC_ROOT =
      "/Projects/eChronos/QATool/QA_Tool/src/";

  /** Object under test */
  static private Prototype _proto;
  /** Source directory at absolute path, servces as root for all File work */
  static private File _srcDir;
  /** Test directory to place prototype test file into */
  static private File _testDir;
  /** Helper object */
  static private QATool _qat;

  /** Target protoType file */
  private File _target;
  /** Mock for testing */
  private MockPrototype _mock;


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
    _qat = new QATool(ROOT);
    assertNotNull(_qat);
    _proto = new Prototype();
    assertNotNull(_proto);
    _srcDir = new File(SRC_ROOT);
    assertTrue(_srcDir.isDirectory());
    _testDir = _qat.findTestDir(new File(ROOT));
    assertTrue(_testDir.isDirectory());
    _mock = _proto.new MockPrototype();
    assertNotNull(_mock);
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);

    _proto = null;
    _testDir = null;
    _srcDir = null;
    _mock = null;
  }


  // ===============================================================================
  // TESTS FOR PUBLIC METHODS
  // ===============================================================================

  /**
   * @NORMAL.TEST
   */
//   @Test
  public void testGetTestFilename()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP
    String[] srcFile = {
        "pdc/QATool.java", // contains a test file
        "pdc/FileMap.java", // contains no test file
        "pdc/Fakeroonie.java", // src file doesn't exist
    };
    String[] expFile = {
        "pdc/TestQATool.java", // contains a test file
        "pdc/TestFileMap.java", // contains no test file
        "" // src file doesn't exist
    };

    File testDir = _qat.findTestDir(new File(SRC_ROOT));
    String testRoot = testDir.getAbsolutePath() + "/";
    MsgCtrl.msgln("\t Test directory at " + testRoot);

    // RUN For a given source file, find its corresponding test file (or absence of)
    // Source contains a test file
    String testPath = _proto.getTestFilename(SRC_ROOT, srcFile[0]);
    File tf = new File(testRoot + testPath);
    MsgCtrl.msgln("\t Test file found: " + tf.getAbsolutePath());
    assertTrue(tf.exists());
    assertEquals(testRoot + expFile[0], tf.getAbsolutePath());

    // Source contains no test file
    testPath = _proto.getTestFilename(SRC_ROOT, srcFile[1]);
    tf = new File(testRoot + testPath);
    assertFalse(tf.exists());
    MsgCtrl.msgln("\t Test file found: " + tf.getAbsolutePath());
    assertEquals(testRoot + expFile[1], tf.getAbsolutePath());

    // Source does not exist
    testPath = _proto.getTestFilename(SRC_ROOT, srcFile[2]);
    assertNull(testPath);

  }

  /**
   * @NORMAL.TEST
   */
//  @Test
  public void testSrcList()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // For the given root, list all source files (relative path)
    File srcDir = new File(SRC_ROOT);
    int rootLen = SRC_ROOT.length()-1;    // length of the path to skip
    ArrayList<String> srcList = _qat.buildSourceList(srcDir, rootLen);
    File testDir = _qat.findTestDir(srcDir);
    String testRoot = testDir.getAbsolutePath() + "/";
    MsgCtrl.msgln("\t Test directory at " + testRoot + "\n");

    // RUN For each source file, collect its test name and check if that File exists
    ArrayList<File> testList = new ArrayList<File>(srcList.size());
    MsgCtrl.msgln("\t" +  srcList.size() +  " total files found.");
    int count = 0;
    for (String src : srcList) {
      // Source contains a test file
      String testName = _proto.getTestFilename(SRC_ROOT, src);
      MsgCtrl.msgln("\tSource file:" +  src);
      File testFile = new File(testRoot + testName);
      if (!testFile.exists()) {
        MsgCtrl.msgln("\t\tCreating new test file " + testName);
        _proto.createFile(srcDir, testDir, src);
        count++;
        testList.add(testFile);   // save the test files for later deletion during cleanup
      }
    }
    MsgCtrl.msgln("\t" +  count + " new files created.");
    
    // TEARDOWN Delete all files in testDir with zero length
    for (File f : testList) {
      if (f.length() == 0) {
        MsgCtrl.msgln("\t\tCleaning up...Deleting test file " + f.getName());
        f.delete();
      }
    }
    
  }


//  /**
//   * NORMAL.TEST File createFile(File, String)
//   */
//  // @Test
//  public void testCreateFile()
//  {
//    MsgCtrl.auditMsgsOn(true);
//    MsgCtrl.errorMsgsOn(true);
//    MsgCtrl.where(this);
//
//    // SETUP: Define source root
//    String[] srcFile = {
//        "pdc/Fakeroonie.java", // src file doesn't exist
//        "pdc/FileMap.java", // contains no test file
//        "Prototype.java", // contains a test file
//        "pdc/subDir/Prototype.java", // contains no test file in this subdirectory
//    };
//
//    MsgCtrl.msgln("\ttestCreateFile SRC_ROOT = " + SRC_ROOT);
//
//    // RUN: Generate new test file QA_Tool/src/pdc/TestFileMap.java
//    for (int k = 1; k < srcFile.length; k++) {
//      // RUN: Generate new test file QA_Tool/src/pdc/TestFileMap.java
//      _target = _proto.createFile(_testDir, srcFile[k]);
//
//      // VERIFY
//      String path = _target.getAbsolutePath();
//      MsgCtrl.msgln("\ttestFile to be created: \t" + PROTO_NAME);
//      MsgCtrl.msgln("\ttestFile created: \t\t\t" + path);
//      assertTrue(_target.getAbsoluteFile().exists());
//      assertTrue(path.equals(PROTO_NAME));
//      assertEquals(0, _target.length());
//    }
//
//    // Error test: source file does not exist
//    _target = _proto.createFile(_testDir, ROOT + srcFile[0]);
//    assertNull(_target);
//
//
//    // TEARDOWN: delete test file and parent dir
//    // File parent = _target.getParentFile();
//    // _target.delete();
//    // parent.delete();
//  }


  /**
   * NORMAL.TEST File writeFile(File) uses pdc/Prototype.java
   */
  @Test
  public void testWriteFile()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP: Create the file in the right place
    String srcName = "pdc/Prototype.java";
    String testFile = "TestPrototype.java";
    int NBR_PUBLIC_TESTS = 2;
    int NBR_PROTECTED_TESTS = 0;
    String[] rawSigs = {
        "public java.io.File pdc.Prototype.createFile(java.io.File,java.lang.String)",
        "public java.io.File pdc.Prototype.writeFile(java.io.File,java.lang.String)"};
    String[] expSigs = {
        "File createFile(File, String)", "File writeFile(File, String)"};

    File srcDir = new File(SRC_ROOT);
    _target = _proto.createFile(srcDir, _testDir, srcName);

    // RUN: The source class file must be passed for methods to be extracted
    _target = _proto.writeFile(_target, srcName);
    MsgCtrl.msgln("\tGenerated test file " + _target.getAbsolutePath());
    MsgCtrl.msgln("\tGenerated test file size = " + _target.length());

    // VERIFY
    // printFile(_target.getAbsolutePath());
    assertTrue(_target.exists());
    assertEquals(testFile, _target.getName());

    ArrayList<String> publix = _mock.getPublicMethods();
    assertEquals(NBR_PUBLIC_TESTS, publix.size());
    assertEquals(expSigs[0], publix.get(0));
    assertEquals(expSigs[1], publix.get(1));

    ArrayList<String> protex = _mock.getProtectedMethods();
    assertEquals(NBR_PROTECTED_TESTS, protex.size());

  }


  // ======================================================================
  // PRIVATE HELPER METHODS
  // ======================================================================

  /** Display to the console the prototype as written so far */
  private void printFile(String fName)
  {
    Scanner in = null;
    try {
      in = new Scanner(new FileReader(fName));
    } catch (FileNotFoundException e) {
      MsgCtrl.errMsgln("\tprintFile: Could not find file " + fName);
      MsgCtrl.errMsgln("\t" + e.getMessage());
    }
    String line = null;
    MsgCtrl.msgln("\nPRINTING FILE " + fName + ":\n");
    try {
      while ((line = in.nextLine()) != null) {
        MsgCtrl.msgln(line);
      }
      // This exception ends the file read
    } catch (NoSuchElementException e2) {
      ;
    }
    in.close();
  }


} // end of TestPrototype class
