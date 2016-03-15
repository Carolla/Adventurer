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
  /** Exlusion file must be directly beneath src root */
  static private final String EXCLUDE_PATH = ROOT + "ScanExclusions.txt";

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


  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    _qat = new QATool(ROOT, EXCLUDE_PATH);
    assertNotNull(_qat);

    _proto = new Prototype();
    assertNotNull(_proto);
    _mock = _proto.new MockPrototype();
    assertNotNull(_mock);

    _srcDir = new File(ROOT);
    assertTrue(_srcDir.isDirectory());
    _testDir = _qat.findTestDir(new File(ROOT));
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
  public void makeTestFilename()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP Doesn't matter if the test file exists or not; this method is a string builder
    final String[] srcPath = {
        "pdc/QATool.java", // contains a test file
        "pdc/FileMap.java", // contains no test file
        "pdc/NoFile.java",
        "pdc/subdir/BottomFile.java", // bottom of file tree
        "ing_Testing.java", // and something at src level
    };
    final String[] expPath = {
        "pdc/TestQATool.java", // contains a test file
        "pdc/TestFileMap.java", // contains no test file
        "pdc/TestNoFile.java", // src file doesn't exist but doesn't matter for this test
        "pdc/subdir/TestBottomFile.java", // bottom of file tree
        "Testing_Testing.java", // and something at src level
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
  public void writeFileMultiples()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP
    String baseName = "SubDirSource.java";
    String testName = "TestSubDirSource.java";
    String srcPath = "pdc" + Constants.FS + "subDir" + Constants.FS + baseName;
    String targetPath = _proto.makeTestFilename(ROOT + srcPath);
    String expTestFile = ROOT + "test" + Constants.FS + "pdc" + Constants.FS + "subDir"
        + Constants.FS + testName;
    assertTrue(targetPath.equals(expTestFile));

    // SETUP Remove the target file fresh copy
    File target = new File(targetPath);
    assertTrue(target.delete());

    // Run the create the target file twice, without duplicating its contents
    target = _proto.writeFile(new File(targetPath), srcPath);
    MsgCtrl.msgln("\tGenerated test file " + target.getPath());
    MsgCtrl.msgln("\tGenerated test file size = " + target.length());

    // VERIFY
    long expFileLen = 3021;
    assertTrue(target.exists());
    assertEquals(expFileLen, target.length());

    target = _proto.writeFile(new File(targetPath), srcPath);
    // printFile(target.getAbsolutePath());
    assertTrue(target.exists());
    assertTrue(target.length() == expFileLen);

  }


  /**
   * NORMAL.TEST File writeFile(File) uses pdc/Prototype.java
   */
  @Test
  public void writeFile()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP: Create the file in the right place
    /* These are the public methods in the test file that will be written as test methods:
        "String getTestFilename(String s1, String s2)",
        "void m(File f)",
        "File m(String s, int x)",
        "String m(String s, int x, long k)",
    */    
    // These are the expected methods in the target file after it is written
    String[] expPublics = {
        "String getTestFilename(String, String)",
        "void m1(File)",
        "File m2(String, int)",
        "String m3(String, int, long)",
    };
    String[] expProtecteds = {
        "File createFile(File, File, String)",
        "File writeFile(File, String)",
    };

    String baseName = "SubDirSource.java";
    String testName = "TestSubDirSource.java";
    String srcPath = "pdc" + Constants.FS + "subDir" + Constants.FS + baseName;
    String targetPath = _proto.makeTestFilename(ROOT + srcPath);

    String expTestFile = ROOT + "test" + Constants.FS + "pdc" + Constants.FS + "subDir"
        + Constants.FS + testName;
    assertTrue(targetPath.equals(expTestFile));

    // SETUP Remove the target file fresh copy
    File target = new File(targetPath);
    target.delete();
    assertTrue(!target.exists());

    // RUN: The source class file must be passed for methods to be extracted
    // The srcPath cannot include the ROOT
    target = _proto.writeFile(target, srcPath);
    MsgCtrl.msgln("\tGenerated test file " + target.getPath());
    MsgCtrl.msgln("\tGenerated test file size = " + target.length());

    // VERIFY
    long expFileLen = 3021;
    assertTrue(target.exists());
    assertEquals(expFileLen, target.length());

    // printFile(target.getAbsolutePath());
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
