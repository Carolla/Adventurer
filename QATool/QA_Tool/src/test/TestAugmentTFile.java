/**
 * TestAugmentTFile.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
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
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.Constants;
import mylib.MsgCtrl;
import pdc.AugmentTFile;
import pdc.AugmentTFile.MockAugmentTFile;
import pdc.Prototype;
import pdc.Prototype.MockPrototype;

/**
 * @author Alan Cline
 * @version Apr 7, 2016 // original <br>
 */
public class TestAugmentTFile
{

  private AugmentTFile _aug;
  private MockAugmentTFile _mock;

  static private final String ROOT =
      System.getProperty("user.dir") + Constants.FS + "src" + Constants.FS;
  static final String _srcPath = ROOT + "pdc/subDir/SubDirSource.java";
  static final String _testPath = ROOT + "test/pdc/subDir/TestSubDirSource.java";

  private static File _srcFile;
  private static File _testFile;

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    _srcFile = new File(_srcPath);
    assertNotNull(_srcFile);
    assertTrue(_srcFile.exists());
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    _srcFile = null;
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    _aug = new AugmentTFile();
    _mock = _aug.new MockAugmentTFile();
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    _mock = null;
    _aug = null;
  }

  // ===============================================================================
  // TESTS FOR PUBLIC METHODS
  // ===============================================================================

  /**
   * @NORMAL_TEST void augment(File srcFile, File tfile)
   */
  @Test
  public void testAugmentNoTestFile()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP ensure that test file does not yet exist
    assertTrue(_srcFile.exists());
    File testFile = new File(_testPath);
    testFile.delete();
    assertFalse(testFile.exists());

    // RUN
    _aug.augment(_srcPath, _testPath);

    // VERIFY test file created with same number of methods as source file (plus 4 prep methods)
    assertTrue(_srcFile.exists());
    assertTrue(testFile.exists());

    Prototype proto = new Prototype();
    MockPrototype protoMock = proto.new MockPrototype();
    int allSrcMethods = protoMock.getPublicMethods().size() 
        + protoMock.getProtectedMethods().size();
    assertEquals(allSrcMethods, _mock.getSourceMethods().size());
    assertTrue(testFile.exists());
  }


  /**
   * @NORMAL_TEST void augment(File srcFile, File tfile)
   */
  @Test
  public void testAugmentPartialTestFile()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP ensure that test file exists but may have missing methods
    assertTrue(_srcFile.exists());
    File tfile = new File(_testPath);
    assertTrue(tfile.exists());

    // RUN existing empty file to augment
    _aug.augment(_srcPath, _testPath);

    // VERIFY
    assertTrue(_srcFile.exists());
    assertEquals(6, _mock.getSourceMethods().size());
    assertTrue(tfile.exists());
    assertEquals(0, _mock.getTestMethods().size());
  }


} // end of TestAugmentTFile class
