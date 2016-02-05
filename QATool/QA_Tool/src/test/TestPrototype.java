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

  /** Object under test */
  static private Prototype _proto;
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
    String srcName = "pdc/FileMap.java";

    MsgCtrl.msgln("\ttestCreateFile ROOT = " + ROOT);

    // RUN: Generate new test file QA_Tool/src/pdc/TestFileMap.java
    _target = _proto.createFile(_testDir, srcName);

    // VERIFY
    String path = _target.getAbsolutePath();
    MsgCtrl.msgln("\ttestFile to be created: \t" + PROTO_NAME);
    MsgCtrl.msgln("\ttestFile created: \t\t\t" + path);
    assertTrue(_target.getAbsoluteFile().exists());
    assertTrue(path.equals(PROTO_NAME));
    assertEquals(0, _target.length());

    // TEARDOWN: delete test file and parent dir
    File parent = _target.getParentFile();
    _target.delete();
    parent.delete();
  }


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

    _target = _proto.createFile(_testDir, srcName);

    // RUN: The source class file must be passed for methods to be extracted
    _target = _proto.writeFile(_target, srcName);
    MsgCtrl.msgln("\tGenerated test file " + _target.getAbsolutePath());
    MsgCtrl.msgln("\tGenerated test file size = " + _target.length());
    
    // VERIFY
    //    printFile(_target.getAbsolutePath());
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
