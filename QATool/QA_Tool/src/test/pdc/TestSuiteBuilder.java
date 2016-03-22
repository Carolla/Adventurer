/**
 * TestSuiteBuilder.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import pdc.SuiteBuilder;

/**
 * @author Al Cline
 * @version March 21, 2016 // original <br>
 */
public class TestSuiteBuilder
{
  /** Root for all source files and subdirectories */
  private final String TESTDIR =
      System.getProperty("user.dir") + Constants.FS + "src" + Constants.FS + "test";

  /** SuiteBuilder class under test */
  static private SuiteBuilder _sb;

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
    _sb = new SuiteBuilder();
    assertNotNull(_sb);
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);

    _sb = null;
  }


  // ===============================================================================
  // TESTS FOR PUBLIC METHODS
  // ===============================================================================

  /**
   * @NORMAL.TEST ArrayList<String> collectTestFiles(String testDirPath)
   */
  @Test
  public void testCollectTestFiles()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP Expected file names
    String[] expFiles = {"pdc/TestSuiteBuilder.java", "TestProtype.java", "TestQATool.java"};

    ArrayList<String> expected = new ArrayList<String>(expFiles.length);
    for (int k = 0; k < expFiles.length; k++) {
      expected.add(TESTDIR + Constants.FS + expFiles[k]);
    }

    ArrayList<String> fileList = _sb.collectTestFileNames(new File(TESTDIR));
    assertEquals(expFiles.length, fileList.size());
    for (int k = 0; k < expFiles.length; k++) {
      MsgCtrl.msgln("\tTest file : " + fileList.get(k));
      assertTrue(expFiles[k].equals(fileList.get(k)));
    }

  }


  /**
   * @ERROR.TEST void main(String[] args)
   */
  @Test
  public void testMain()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // ERROR: Too many arguments
    String[] args = new String[2];
    args[0] = TESTDIR;
    args[1] = "Stuffing";
    try {
      SuiteBuilder.main(args);
    } catch (IllegalArgumentException ex1) {
      MsgCtrl.errMsgln("\tExpected exception: " + ex1.getMessage());
    }
    // ERROR: Test directory not given
    String[] newArgs = new String[1];
    newArgs[0] = TESTDIR + Constants.FS + "NotAFile";
    try {
      SuiteBuilder.main(newArgs);
    } catch (IllegalArgumentException ex1) {
      MsgCtrl.errMsgln("\tExpected exception: " + ex1.getMessage());
    }

  }


} // end of TestSuiteBuilder.java class
