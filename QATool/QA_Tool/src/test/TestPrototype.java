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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.MsgCtrl;
import pdc.Prototype;
import pdc.QATool;

/**
 * @author Alan Cline
 * @version Jan 29, 2016 // original <br>
 */
public class TestPrototype
{
  /** Root for all source files and subdirectories */
  private final String ROOT = System.getProperty("user.dir") + "/src";
  /** Name of source file from which to derive test file */
  private final String SRCNAME = "pdc/FileMap.java";
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


  // ======================================================================
  // BEGIN TESTS
  // ======================================================================

  /**
   * NORMAL.TEST File createFile(File, String)
   */
  @Test
  public void testCreateFile()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP: Define source root
    MsgCtrl.msgln("\ttestCreateFile ROOT = " + ROOT);

    // RUN: Generate new test file QA_Tool/src/pdc/TestFileMap.java
    _target = _proto.createFile(_testDir, SRCNAME);
    String path = _target.getAbsolutePath();
    MsgCtrl.msgln("\ttestFile to be created: \t" + PROTO_NAME);
    MsgCtrl.msgln("\ttestFile created: \t\t\t" + path);
    assertTrue(_target.getAbsoluteFile().exists());
    assertTrue(path.equals(PROTO_NAME));
    assertEquals(0, _target.length());

    // TEARDOWN: delete test file and parent dir
//    File parent = _target.getParentFile();
//    _target.delete();
//    parent.delete();
  }


//  /**
//   * NORMAL.TEST File writeFile(File)
//   */
//  @Test
//  public void testWriteFile()
//  {
//    MsgCtrl.auditMsgsOn(true);
//    MsgCtrl.errorMsgsOn(true);
//    MsgCtrl.where(this);
//
//    // SETUP: Create the file in the right place
//    _target = _proto.createFile(_testDir, SRCNAME);
//
//    // Create the file and write the copyright notice into it
//    _target = _proto.writeFile();
//    MsgCtrl.msgln("\tGenerated test file " + _target.getAbsolutePath());
//    MsgCtrl.msgln("\tGenerated test file size = " + _target.length());
//    assertTrue(_target.exists());
//    assertEquals(427, _target.length());
//
//    printFile(_target);
//  }


  // ======================================================================
  // PRIVATE HELPER METHODS
  // ======================================================================

  /** Display to the console the prototype as written so far */
  private void printFile(File txtFile)
  {
    BufferedReader in = null;
    try {
      FileReader fr = new FileReader(txtFile.getName());
      in = new BufferedReader(fr);
    } catch (FileNotFoundException fnfEx) {
      MsgCtrl.errMsgln("printFile(): Cannot create the Buffered Reader from " + txtFile.getName());  
      MsgCtrl.errMsgln(fnfEx.getMessage());  
    }
    String line;
    try {
      while ((line = in.readLine()) != null) {
        MsgCtrl.msgln(line);
      }
      in.close();
    } catch (IOException ioEx) {
      MsgCtrl.errMsgln("printFile(): Error while reading from target file " + txtFile.getName());  
      MsgCtrl.errMsgln(ioEx.getMessage());  
    }
  }

  
} // end of TestPrototype class