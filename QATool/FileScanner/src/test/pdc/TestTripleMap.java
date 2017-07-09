/**
 * TestTripleMap.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */

package test.pdc;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.MsgCtrl;
import pdc.TripleMap;

/**
 * @author Alan Cline
 * @version Apr 22, 2017 // original <br>
 *          April 23, 2017 // autogen: QA Tool added missing test methods <br>
 */
public class TestTripleMap
{
  /**
   * @throws java.lang.Exception -- catchall for what tests don't catch
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception -- catchall for what tests don't catch
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception -- catchall for what tests don't catch
   */
  @Before
  public void setUp() throws Exception
  {}

  /**
   * @throws java.lang.Exception -- catchall for what tests don't catch
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ================================================================================
  // PUBLIC METHODS
  // ================================================================================

  /**
   * @Normal.Test {@code Map<String, String> buildAugMap()} -- Add new tests to test file
   */
  @Test
  public void testBuildAugListMoreTestsThanSource()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    ArrayList<String> srcNames = new ArrayList<String>(Arrays.asList(
        "File alpha(String)", "String beta(File)", "int epsilon(int)"));

    ArrayList<String> testNames = new ArrayList<String>(Arrays.asList(
        "void testAlpha()", "void testBeta1()", "void testBeta2()", "void testGamma()"));

    TripleMap tMap = new TripleMap();
    tMap.setMapList(TripleMap.NameType.TEST, testNames);

    printList("Source list: ", tMap.export(TripleMap.NameType.SRC));
    printList("Converted test names from source: ", tMap.export(TripleMap.NameType.SRC_TO_TEST));
    printList("Test names found: ", tMap.export(TripleMap.NameType.TEST));

//    Map<String, String> augMap = tMap.buildAugMap();
//    printMap("\nNew test methods to write", augMap);
//    assertEquals(2, augMap.size());
//    assertEquals("void testEpsilon()", augMap.get("int epsilon(int)"));
//    assertEquals("void testBeta()", augMap.get("String beta(File)"));
  }


  /**
   * @Normal.Test {@code Map<String, String> buildAugMap()} -- Initial test: no new tests
   */
  @Test
  public void testBuildAugList()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    ArrayList<String> srcNames = new ArrayList<String>(Arrays.asList(
        "File alpha(String)", "String beta(File)", "int beta(int)"));

    TripleMap tMap = new TripleMap();
    printList("Source list: ", tMap.export(TripleMap.NameType.SRC));
    printList("Converted test names from source: ",
        tMap.export(TripleMap.NameType.SRC_TO_TEST));

    // NORMAL: No test names at all
    assertEquals(srcNames.size(), tMap.buildAugMap().size());
  }


  /**
   * @Normal.Test {@code Map<String, String> buildAugMap()} -- no new tests to add to src names
   */
  @Test
  public void testBuildAugListNoNewTests()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    ArrayList<String> srcNames = new ArrayList<String>(Arrays.asList(
        "File alpha(String)", "String beta(File)", "int beta(int)"));
    // NORMAL: No NEW test names, so auglist should be 0
    ArrayList<String> testNames = new ArrayList<String>(Arrays.asList(
        "void testAlpha()", "void testBeta1()", "void testBeta2()"));

    TripleMap tMap = new TripleMap();
    printList("Source list: ", tMap.export(TripleMap.NameType.SRC));
    printList("Converted test names from source: ",
        tMap.export(TripleMap.NameType.SRC_TO_TEST));

    tMap.setMapList(TripleMap.NameType.TEST, testNames);
    Map<String, String> augMap = tMap.buildAugMap();
    assertEquals(0, augMap.size());
  }


  /**
   * @Normal.Test {@code Map<String, String> buildAugMap()} -- one new src method creates one test
   *              method
   */
  @Test
  public void testBuildAugListOneNewSrcMethod()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    ArrayList<String> srcNames = new ArrayList<String>(Arrays.asList(
        "File alpha(String)", "String beta(File)", "int beta(int)", "boolean gamma(boolean)"));
    // NORMAL: No NEW test names, so auglist should be 0
    ArrayList<String> testNames = new ArrayList<String>(Arrays.asList(
        "void testAlpha()", "void testBeta1()", "void testBeta2()"));

    TripleMap tMap = new TripleMap();
    printList("Source list: ", tMap.export(TripleMap.NameType.SRC));
    printList("Converted test names from source: ",
        tMap.export(TripleMap.NameType.SRC_TO_TEST));

    tMap.setMapList(TripleMap.NameType.TEST, testNames);
    Map<String, String> augMap = tMap.buildAugMap();
    assertEquals(1, augMap.size());

    printMap("\nNew test method to write", augMap);
  }


  /**
   * @Normal.Test ArrayList export(TripleMap$NameType)
   */
  @Test
  public void testExport()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    MsgCtrl.errMsgln("\t\t Implemented by other tests in this file");
  }


  /**
   * @Normal.Test void setMapList(TripleMap$NameType, ArrayList)
   */
  @Test
  public void testSetMapList()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    MsgCtrl.errMsgln("\t\t Simple setter. No need to test");
  }


  // ================================================================================
  // PRIVATE HELPER METHODSS
  // ================================================================================

  /**
   * Send a list to the console as audit trail
   * 
   * @param msg message to be printed above list dump
   * @param alist some list to be printed
   */
  private void printList(String msg, ArrayList<String> alist)
  {
    MsgCtrl.msgln("\n" + msg);
    for (String s : alist) {
      MsgCtrl.msgln("\t" + s);
    }
  }


  /**
   * Send map entries to the console as audit trail
   * 
   * @param msg message to be printed above list dump
   * @param amap some map to be printed as key, value
   */
  private void printMap(String msg, Map<String, String> amap)
  {
    MsgCtrl.msgln("\n" + msg);
    for (String key : amap.keySet()) {
      MsgCtrl.msgln("\t" + key + "\t \\\\  " + amap.get(key));
    }
  }


} // end of TestTripleMap test class
