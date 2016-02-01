/**
 * TestTestTemplate.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
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
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.registry.RegistryFactory;
import mylib.MsgCtrl;
import mylib.pdc.Utilities;
import pdc.TestTemplate;

/**
 * @author Alan Cline
 * @version Jan 22, 2016 // original <br>
 */
public class TestTestTemplate
{
  static private TestTemplate _testTpl;

  /** Target source path for testing */
  private final String SOURCE_PATH =
      "/Projects/eChronos/QATool/QA_Tool/simTree/src/chronos/pdc/registry/RegistryFactory";
  /** Target source path for testing */
  private final String SOURCE_ROOT =
      "/Projects/eChronos/QATool/QA_Tool/simTree/src/chronos";
  /** Target source file for testing */
  private final Class SRC_TARGET = RegistryFactory.class;
  private final String TEST_ROOT =
      "/Projects/eChronos/QATool/QA_Tool/simTree/src/chronos/test/";


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
    _testTpl = new TestTemplate();
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _testTpl = null;
  }


  // ======================================================================
  // BEGIN TESTS
  // ======================================================================


  /**
   * NORMAL.TEST Find a corresponding test file for a given source file
   */
  @Test
  public void testHasMatch()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP: Define mathod parms
    File srcRoot = new File(SOURCE_ROOT);
    File testRoot = new File(TEST_ROOT);
    String targetName = "Bank.java";
    String badName = "Foobar.java";

    // RUN and VERIFY
    boolean result = _testTpl.hasMatch(srcRoot, testRoot, targetName);
    assertTrue(result);
    
//    // VERIFY that missing file returns false
//    result = _testTpl.hasMatch(srcRoot, testRoot, badName);
//    assertFalse(result);
    

  }


  /**
   * NORMAL.TEST Get a named file by traversing the file tree from the root down
   */
  @Test
  public void testGetTargetFile()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP
    File root = new File(TEST_ROOT);
    String targetName = "TestRegistryFactory.java";
    String targetPath = "/Projects/eChronos/QATool/QA_Tool/simTree/src/chronos/test/"
        + "pdc/registry/TestRegistryFactory.java";

    // RUN: Retrieve the requested file starting from test root
    String path = _testTpl.getTargetFile(root, targetName);
    assertNotNull(path);
    MsgCtrl.msgln("\t path found: " + path);

    // VERIFY:
    assertTrue(targetPath.equals(path));

  }


  // /**
  // * NORMAL.TEST TestTemplate.getMethods(Class<T>) get simple method names from target source
  // */
  // @Test
  // public void testGetTestMethods()
  // {
  // MsgCtrl.auditMsgsOn(true);
  // MsgCtrl.errorMsgsOn(true);
  // MsgCtrl.where(this);
  //
  // // SETUP: test methods from test/registry/TestRegistryFactory
  // String[] testListStr = {"notNeeded", "testGetRegistry_Errors()", "testGetRegistry_Exists()"};
  // // ArrayList<String> testList = Utilities.convertToArrayList(testListStr);
  //
  // // RUN: Get all methods in target test file
  // Class<?> clazz = null;
  //// try {
  //// // clazz = Class.forName("test.pdc.registry.TestRegistryFactory");
  //// clazz = Class.forName("TestRegistryFactory");
  //// } catch (ClassNotFoundException ex) {
  //// MsgCtrl.errMsgln("\t" + ex.getMessage());
  //// }
  // TestRegistryFactory trfac = new TestRegistryFactory();
  // assertNotNull(trfac);
  // ArrayList<String> methods = _testTpl.getMethods(trfac.getClass());
  // assertNotNull(methods);
  //
  // // VERIFY: get test method list
  // for (int k = 0; k < methods.size(); k++) {
  // String s = "TestRegistryFactory." + testListStr[k];
  // MsgCtrl.msgln("\t" + s);
  // MsgCtrl.msgln("\t\t" + methods.get(k));
  // assertEquals(s, methods.get(k));
  // }
  // }


  // /**
  // * NORMAL.TEST TestTemplate.getMethods(String path) get simple method names from target source
  // */
  // @Test
  // public void testGetMethodsFromSource()
  // {
  // MsgCtrl.auditMsgsOn(false);
  // MsgCtrl.errorMsgsOn(false);
  // MsgCtrl.where(this);
  //
  // // SETUP: source methods from RegistryFactory
  // String[] srcListStr = {"getNumberOfRegistries()",
  // "getRegistry(chronos.pdc.registry.RegistryFactory$RegKey)",
  // "initRegistries(chronos.pdc.command.Scheduler)"
  // };
  // ArrayList<String> srcList = Utilities.convertToArrayList(srcListStr);
  // assertEquals(srcListStr.length, srcList.size());
  //
  // // RUN: get source method list
  // ArrayList<String> methods = _testTpl.getMethods(SOURCE_PATH);
  //
  // // VERIFY: get source method list
  // assertEquals(srcListStr.length, methods.size());
  // for (int k = 0; k < methods.size(); k++) {
  // String s = "RegistryFactory." + srcListStr[k];
  // MsgCtrl.msgln("\t" + s);
  // MsgCtrl.msgln("\t\t" + methods.get(k));
  // assertEquals(s, methods.get(k));
  // }
  // }


  /**
   * NORMAL.TEST TestTemplate.getMethods(Class<T>) get simple method names from target source
   */
  @Test
  public void testGetSrcMethods()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP: source methods from RegistryFactory
    String[] srcListStr = {"getNumberOfRegistries()",
        "getRegistry(chronos.pdc.registry.RegistryFactory$RegKey)",
        "initRegistries(chronos.pdc.command.Scheduler)"
    };
    ArrayList<String> srcList = Utilities.convertToArrayList(srcListStr);
    assertEquals(srcListStr.length, srcList.size());

    // RUN: get source method list
    ArrayList<String> methods = _testTpl.getMethods(SRC_TARGET);

    // VERIFY: get source method list
    assertEquals(srcListStr.length, methods.size());
    for (int k = 0; k < methods.size(); k++) {
      String s = "RegistryFactory." + srcListStr[k];
      MsgCtrl.msgln("\t" + s);
      MsgCtrl.msgln("\t\t" + methods.get(k));
      assertEquals(s, methods.get(k));
    }
  }

  // ======================================================================
  // PRIVATE HELPERS
  // ======================================================================

  // /** Convert a String[] to a ArrayList<String> for easier handling */
  // private ArrayList<String> convertToArrayList(String[] strs)
  // {
  // // Setup for arraylist
  // ArrayList<String> alist = new ArrayList<String>();
  // for (int k = 0; k < strs.length; k++) {
  // alist.add(strs[k]);
  // }
  // return alist;
  // }


} // end of TestMakeTestTemplate class
