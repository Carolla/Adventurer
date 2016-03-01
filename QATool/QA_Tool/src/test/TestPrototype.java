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

import mylib.Constants;
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
  static private final String ROOT = System.getProperty("user.dir") + "/src/";
  static private final String SRC_ROOT =
      "/Projects/eChronos/QATool/QA_Tool/src/";

  /** Object under test */
  static private Prototype _proto;
  /** Mock for testing */
  static private MockPrototype _mock;

  /** Source directory at absolute path, servces as root for all File work */
  static private File _srcDir;
  /** Test directory to place prototype test file into */
  static private File _testDir;
  /** Helper object */
  static private QATool _qat;

  // /** Target protoType file */
  // private File _target;


  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    _qat = new QATool(ROOT);
    assertNotNull(_qat);

    _proto = new Prototype();
    assertNotNull(_proto);
    _mock = _proto.new MockPrototype();
    assertNotNull(_mock);

    _srcDir = new File(SRC_ROOT);
    assertTrue(_srcDir.isDirectory());
    _testDir = _qat.findTestDir(new File(SRC_ROOT));
    assertTrue(_testDir.isDirectory());
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);

    _testDir = null;
    _srcDir = null;
    _qat = null;
    _proto = null;
    _mock = null;

    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {}

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ===============================================================================
  // TESTS FOR PUBLIC METHODS
  // ===============================================================================

  /**
   * @NORMAL.TEST String makeTestFilename(String srcPath)
   */
  @Test
  public void testMakeTestFilename()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP Doesn't matter if the test file exists or not; this method is a string builder
    final String[] srcPath = {
        "pdc/QATool.java", // contains a test file
        "pdc/FileMap.java", // contains no test file
        "pdc/NoFile.java", // src file doesn't exist but doesn't matter for this test
        "ing_Testing.java", // and something silly
    };
    final String[] expPath = {
        "pdc/TestQATool.java", // contains a test file
        "pdc/TestFileMap.java", // contains no test file
        "pdc/TestNoFile.java", // src file doesn't exist but doesn't matter for this test
        "Testing_Testing.java", // and something silly
    };

    File testDir = _qat.findTestDir(new File(ROOT));
    String testPath = testDir.getAbsolutePath();
    MsgCtrl.msgln("\t Test directory at " + testPath);

    // RUN For a given set of source names, create the correcponding test file names
    for (int k = 0; k < srcPath.length - 1; k++) {
      // Source contains a test file
      String sourcePath = ROOT + srcPath[k];
      String targetPath = _proto.makeTestFilename(sourcePath);
      String expFullPath = testPath + Constants.FS + expPath[k];
      MsgCtrl.msgln("\t Test file name created: " + targetPath);
      assertTrue(expFullPath.equals(targetPath));
    }

    // ERROR Source name is not a java file
    testPath = _proto.makeTestFilename("OtherFile.png");
    assertNull(testPath);
  }

  /**
   * NORMAL.TEST File writeFile(File) uses pdc/subDir/SubDirSource.java for testing
   */
  @Test
  public void testWriteFileMultiples()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP
    String baseName = "SubDirSource.java";
    String testName = "TestSubDirSource.java";
    String srcPath = "pdc" + Constants.FS + "subDir" + Constants.FS + baseName;
    String targetPath = _proto.makeTestFilename(ROOT + srcPath);
    String expTestFile = ROOT + "test" + Constants.FS + "pdc" + Constants.FS + "subDir"
        + Constants.FS + testName;
    assertTrue(targetPath.equals(expTestFile));
    
    // Run the create the target file twice, without duplicating its contents
    File target = _proto.writeFile(new File(targetPath), srcPath);
    MsgCtrl.msgln("\tGenerated test file " + target.getPath());
    MsgCtrl.msgln("\tGenerated test file size = " + target.length());

    // VERIFY
    long expFileLen = 2888;
    printFile(target.getAbsolutePath());
    assertTrue(target.exists());
    assertTrue(target.length() == expFileLen);

    target = _proto.writeFile(new File(targetPath), srcPath);
    printFile(target.getAbsolutePath());
    assertTrue(target.exists());
    assertTrue(target.length() == expFileLen);
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
    // These are the expected methods in the target file after it is written
    String[] expPublics = {
        "File createFile(File, File, String)",
        "String getTestFilename(String, String)",
        "void m1()",
        "File m2()",
        "File writeFile(File, String)"
    };
    String[] expProtecteds = {
        "String m3()"
    };

    String baseName = "SubDirSource.java";
    String testName = "TestSubDirSource.java";
    String srcPath = "pdc" + Constants.FS + "subDir" + Constants.FS + baseName;
    String targetPath = _proto.makeTestFilename(ROOT + srcPath);

    String expTestFile = ROOT + "test" + Constants.FS + "pdc" + Constants.FS + "subDir"
        + Constants.FS + testName;
    assertTrue(targetPath.equals(expTestFile));

    // RUN: The source class file must be passed for methods to be extracted
    // The srcPath cannot include the ROOT
    File target = _proto.writeFile(new File(targetPath), srcPath);
    MsgCtrl.msgln("\tGenerated test file " + target.getPath());
    MsgCtrl.msgln("\tGenerated test file size = " + target.length());

//    target = _proto.writeFile(new File(targetPath), srcPath);
//    MsgCtrl.msgln("\tGenerated test file " + target.getPath());
//    MsgCtrl.msgln("\tGenerated test file size = " + target.length());

    // VERIFY
    //printFile(target.getAbsolutePath());
    assertTrue(target.exists());
    assertEquals(expTestFile, target.getPath());

    ArrayList<String> publix = _mock.getPublicMethods();
    assertEquals(expPublics.length, publix.size());
    MsgCtrl.msgln("\n\tPUBLIC METHODS");
    for (int k = 0; k < publix.size(); k++) {
      MsgCtrl.msgln("\t\t" + publix.get(k));
      assertEquals(expPublics[k], publix.get(k));
    }

    ArrayList<String> protex = _mock.getProtectedMethods();
    assertEquals(expProtecteds.length, protex.size());
    MsgCtrl.msgln("\tPROTECTED METHODS");
    for (int k = 0; k < protex.size(); k++) {
      MsgCtrl.msgln("\t\t" + protex.get(k));
      assertEquals(expProtecteds[k], protex.get(k));
    }

  }

  /// **
  // * Insert "test" after the "src" dir and insert "Test" in front of the filename
  // *
  // * @param srcPath full path of source file
  // * @return test file name that corresponds to source file
  // */
  // public String makeTestFilename(String srcPath)
  // {
  // // Guard against non-Java files
  // if (!srcPath.contains(JAVA)) {
  // return null;
  // }
  // StringBuilder sbTest = new StringBuilder(srcPath);
  // // Insert the prefix "test" subdir after the src subdir
  // // int ndx = srcPath.indexOf("/src");
  // // sbTest.insert(ndx+4, "/test");
  // sbTest.insert(0, "test/");
  // // Insert the prefix "Test" to the src file name
  // // Replace name with Test<Name>
  // int ndx = sbTest.lastIndexOf("/");
  // sbTest.insert(ndx + 1, "Test");
  //
  // return sbTest.toString();
  // }


  // /**
  // * @NORMAL.TEST Using simTree, a snapshot of ChronosLib, scan for all source file paths
  // */
  // @Test
  // public void testFindTestDir()
  // {
  // MsgCtrl.auditMsgsOn(false);
  // MsgCtrl.errorMsgsOn(false);
  // MsgCtrl.where(this);
  //
  // // SETUP
  // File root = new File(SRC_ROOT);
  //
  // // RUN: target method returns nothing
  // File testDir = _qat.findTestDir(root);
  //
  // // VERIFY
  // assertEquals(testDir.getPath(), SRC_ROOT + "test");
  // }

  // /**
  // * @NORMAL.TEST
  // */
  // @Test
  // public void testSrcList()
  // {
  // MsgCtrl.auditMsgsOn(false);
  // MsgCtrl.errorMsgsOn(false);
  // MsgCtrl.where(this);
  //
  // // For the given root, list all source files (relative path)
  // File srcDir = new File(SRC_ROOT);
  // int rootLen = SRC_ROOT.length() - 1; // length of the path to skip
  // ArrayList<String> srcList = _qat.buildSourceList(srcDir, rootLen);
  // File testDir = _proto.findTestDir(srcDir);
  // String testRoot = testDir.getAbsolutePath() + "/";
  // MsgCtrl.msgln("\t Test directory at " + testRoot + "\n");
  //
  // // RUN For each source file, collect its test name and check if that File exists
  // ArrayList<File> testList = new ArrayList<File>(srcList.size());
  // MsgCtrl.msgln("\t" + srcList.size() + " total files found.");
  // int count = 0;
  // for (String src : srcList) {
  // // Source contains a test file
  // String testName = _proto.makeTestFilename(SRC_ROOT, src);
  // MsgCtrl.msgln("\tSource file:" + src);
  // File testFile = new File(testRoot + testName);
  // if (!testFile.exists()) {
  // MsgCtrl.msgln("\t\tCreating new test file " + testName);
  // _proto.createFile(srcDir, testDir, src);
  // count++;
  // testList.add(testFile); // save the test files for later deletion during cleanup
  // }
  // }
  // MsgCtrl.msgln("\t" + count + " new files created.");
  //
  // // TEARDOWN Delete all files in testDir with zero length
  // for (File f : testList) {
  // if (f.length() == 0) {
  // MsgCtrl.msgln("\t\tCleaning up...Deleting test file " + f.getName());
  // f.delete();
  // }
  // }
  //
  // }


  // ======================================================================
  // PRIVATE HELPER METHODS
  // ======================================================================

  /** Clear all zero-length files from the root down */
  private static void clearEmptyFiles(File root)
  {
    MsgCtrl.msgln("Runnning tearDownAfterClass:");

    // Guard against non-directory root
    if ((root == null) || (!root.isDirectory())) {
      MsgCtrl.errMsgln("\tclearEmptyFiles(): root is not a directory");
      return;
    }
    // Retrieve first layer of normal files and subdirs under dir
    File[] allFiles = root.listFiles();
    for (File f : allFiles) {
      if (f.isDirectory()) {
        if (f.listFiles().length == 0) {
          f.delete();
        }
        clearEmptyFiles(f);
      } else if (f.length() <= 1) {
        MsgCtrl.msgln("\tDeleting empty file " + f.getPath());
        f.delete();
      }
    }
    // Remove empty directory if all its files have been deleted
    allFiles = root.listFiles();
    if (allFiles.length == 0) {
      MsgCtrl.msgln("\t\tDeleting empty directory " + root.getPath());
      root.delete();
    }
  }


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
