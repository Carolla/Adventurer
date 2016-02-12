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
   * NORMAL.TEST File createFile(File, String)
   */
  @Test
  public void testCreateFile()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP: Define source root
    String[] srcFile = {
        "pdc/NoFile.java", // src file doesn't exist
        "pdc/FileMap.java", // contains no test file
        "pdc/Prototype.java", // contains a test file
        "pdc/subDir/Prototype.java", // contains no test file in this subdirectory
    };
    String[] expFile = {
        "null", // src file doesn't exist
        "test/pdc/TestFileMap.java", // contains no test file
        "test/pdc/TestPrototype.java", // contains a test file
        "test/pdc/subDir/TestPrototype.java", // contains no test file in this subdirectory
    };

    // Generate new test file QA_Tool/src/pdc/TestFileMap.java
    MsgCtrl.msgln("\ttestCreateFile SRC_ROOT = " + SRC_ROOT);
    File srcDir = new File(SRC_ROOT);

    // 0. RUN Error: Missing source file returns null
    MsgCtrl.msgln("\tsource file to examine = " + SRC_ROOT + srcFile[0]);
    _target = _proto.createFile(srcDir, _testDir, srcFile[0]);
    assertNull(_target);

    // 1. RUN No test file exists for source target
    MsgCtrl.msgln("\tsource file to examine = " + SRC_ROOT + srcFile[1]);
    _target = _proto.createFile(srcDir, _testDir, srcFile[1]);
    assertNotNull(_target);
    String path = _target.getAbsolutePath();
    MsgCtrl.msgln("\ttestFile to be created: \t" + expFile[1]);
    assertTrue(_target.getAbsoluteFile().exists());
    assertTrue(path.equals(SRC_ROOT + expFile[1]));

    // 2. RUN Test file already exists for the source target
    MsgCtrl.msgln("\tsource file to examine = " + SRC_ROOT + srcFile[2]);
    _target = _proto.createFile(srcDir, _testDir, srcFile[2]);
    assertNotNull(_target);
    path = _target.getAbsolutePath();
    MsgCtrl.msgln("\ttestFile to be created: \t" + expFile[2]);
    assertTrue(_target.getAbsoluteFile().exists());
    assertTrue(path.equals(SRC_ROOT + expFile[2]));

    // 3. RUN Test file exists but not in target subdirectory, should proceed
    MsgCtrl.msgln("\tsource file to examine = " + SRC_ROOT + srcFile[3]);
    _target = _proto.createFile(srcDir, _testDir, srcFile[3]);
    assertNotNull(_target);
    path = _target.getAbsolutePath();
    MsgCtrl.msgln("\ttestFile to be created: \t" + expFile[3]);
    assertTrue(_target.getAbsoluteFile().exists());
    assertTrue(path.equals(SRC_ROOT + expFile[3]));

    // TEARDOWN: delete newly created test files but leave existing ones
    for (int k = 1; k < expFile.length; k++) {
      File f = new File(SRC_ROOT + expFile[k]);
      if (f.length() == 0) {
        f.delete();
      }
    }
  }


  /**
   * @NORMAL.TEST String makeTestFilename(String, String)
   */
  @Test
  public void testGetTestFilename()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP Doesn't matter if the test file exists or not; this method is a string builder
    String[] srcPath = {
        "pdc/QATool.java", // contains a test file
        "pdc/FileMap.java", // contains no test file
        "pdc/NoFile.java", // src file doesn't exist but doesn't matter for this test
        "ing_Testing.java", // and something silly
        "not a Java file" // should return null
    };
    String[] expPath = {
        "pdc/TestQATool.java", // contains a test file
        "pdc/TestFileMap.java", // contains no test file
        "pdc/TestNoFile.java", // src file doesn't exist but doesn't matter for this test
        "Testing_Testing.java", // and something silly
        "", // not a java file, should reutrn null
    };

    File testDir = _qat.findTestDir(new File(SRC_ROOT));
    String testRoot = testDir.getAbsolutePath() + "/";
    MsgCtrl.msgln("\t Test directory at " + testRoot);

    // RUN For a given set of source names, create the correcponding test file names
    for (int k = 0; k < 3; k++) {
      // Source contains a test file
      String testPath = _proto.makeTestFilename(testRoot, srcPath[k]);
      File tf = new File(testPath);
      MsgCtrl.msgln("\t Test file name created: " + tf.getAbsolutePath());
      String fullPath = testRoot + expPath[k];
      assertTrue(fullPath.equals(tf.getAbsolutePath()));
    }

    // ERROR Source name is not a java file
    String testPath = _proto.makeTestFilename(testRoot, srcPath[4]);
    assertNull(testPath);
  }


  /**
   * @NORMAL.TEST
   */
  @Test
  public void testSrcList()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // For the given root, list all source files (relative path)
    File srcDir = new File(SRC_ROOT);
    int rootLen = SRC_ROOT.length() - 1; // length of the path to skip
    ArrayList<String> srcList = _qat.buildSourceList(srcDir, rootLen);
    File testDir = _qat.findTestDir(srcDir);
    String testRoot = testDir.getAbsolutePath() + "/";
    MsgCtrl.msgln("\t Test directory at " + testRoot + "\n");

    // RUN For each source file, collect its test name and check if that File exists
    ArrayList<File> testList = new ArrayList<File>(srcList.size());
    MsgCtrl.msgln("\t" + srcList.size() + " total files found.");
    int count = 0;
    for (String src : srcList) {
      // Source contains a test file
      String testName = _proto.makeTestFilename(SRC_ROOT, src);
      MsgCtrl.msgln("\tSource file:" + src);
      File testFile = new File(testRoot + testName);
      if (!testFile.exists()) {
        MsgCtrl.msgln("\t\tCreating new test file " + testName);
        _proto.createFile(srcDir, testDir, src);
        count++;
        testList.add(testFile); // save the test files for later deletion during cleanup
      }
    }
    MsgCtrl.msgln("\t" + count + " new files created.");

    // TEARDOWN Delete all files in testDir with zero length
    for (File f : testList) {
      if (f.length() == 0) {
        MsgCtrl.msgln("\t\tCleaning up...Deleting test file " + f.getName());
        f.delete();
      }
    }

  }


  /**
   * NORMAL.TEST File writeFile(File) uses pdc/Prototype.java
   */
  @Test
  public void testWriteFile()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP: Create the file in the right place
    String srcName = "pdc/Prototype.java";
    String testFile = "TestPrototype.java";
    int NBR_PUBLIC_TESTS = 3;
    int NBR_PROTECTED_TESTS = 0;
    String[] expSigs = {
        "File createFile(File, File, String)",
        // "void fake1()",
        // "long fake1(String)",
        // "File fake1(File, long)",
        // "File fake2(File, long)",
        "String makeTestFilename(String, String)",
        "File writeFile(File, String)",
    };

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
    MsgCtrl.msgln("\tPUBLIC METHODS");
    for (int k = 0; k < publix.size(); k++) {
      MsgCtrl.msgln("\t\t" + publix.get(k));
      assertEquals(expSigs[k], publix.get(k));
    }

    ArrayList<String> protex = _mock.getProtectedMethods();
    assertEquals(NBR_PROTECTED_TESTS, protex.size());
    MsgCtrl.msgln("\tPROTECTED METHODS");
    for (String s : protex) {
      MsgCtrl.msgln("\t" + s);
    }

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
