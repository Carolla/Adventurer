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
 *          July 16, 2017 // revised mostly for buildMap changes <br>
 */
public class TestTripleMap
{
  // Target object to test 
  private TripleMap _tmap;
  
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
  {
    _tmap = new TripleMap();
  }

  /**
   * @throws java.lang.Exception -- catchall for what tests don't catch
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _tmap = null;
  }


  // ================================================================================
  // PUBLIC METHODS
  // ================================================================================

  /**
   * @Not.Needed void addEntry(String, TripleMap$NameType) -- Switch case wrapper
   */
  @Test
  public void testAddEntry()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
  
    MsgCtrl.errMsgln("\t\t Switch case wrapper. No need to test");
  }

  
  /**
   * @Normal.Test {@code Map<String, String> buildAugMap()} -- Initial test: no new tests
   */
  @Test
  public void testBuildAugMap()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP Populate TripleMap; all three types are needed for augmap
    ArrayList<String> srcNames = new ArrayList<String>(Arrays.asList(
        "String alpha(int)", "void beta(String)", "File epsilon(String)"));
    ArrayList<String> gennedNames = new ArrayList<String>(Arrays.asList(
        "void testAlpha()", "void testBeta()", "void testEpsilon()"));
    ArrayList<String> testNames = new ArrayList<String>(Arrays.asList(
        "void testAlpha()", "void testBeta()", "void testEpsilon()"));
    TripleMap tMap = loadTripleMap(srcNames, gennedNames, testNames);

    // RUN Find the difference between the actual test names and genned names
    Map<String, String> augMap = tMap.buildAugMap();

    // VERIFY
    printMap("Methods to add to test file: ", augMap);

    // NORMAL: No test names at all
    assertEquals(0, augMap.size());
  }


  /**
   * @Normal.Test {@code Map<String, String> buildAugMap()} -- All new tests (no existing ones)
   */
  @Test
  public void testBuildAugMap_AllNewTests()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP Populate TripleMap; all three types are needed for augmap
    ArrayList<String> srcNames = new ArrayList<String>(Arrays.asList(
        "String alpha(int)", "void beta(String)", "File epsilon(String)"));
    ArrayList<String> gennedNames = new ArrayList<String>(Arrays.asList(
        "void testAlpha()", "void testBeta()", "void testEpsilon()"));
    ArrayList<String> testNames = new ArrayList<String>();
    TripleMap tMap = loadTripleMap(srcNames, gennedNames, testNames);

    // RUN If there are no test names, then there is no augMap
    Map<String, String> augMap = tMap.buildAugMap();

    // VERIFY
    assertEquals(srcNames.size(), augMap.size());
  }


  /**
   * @Normal.Test {@code Map<String, String> buildAugMap()} -- one new src method creates one test
   *              method
   */
  @Test
  public void testBuildAugList_OneNewSrcMethodAtEnd()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    ArrayList<String> srcNames = new ArrayList<String>(Arrays.asList(
        "String alpha(int)", "void beta(String)", "File epsilon(String)", "long gamma(File)"));
    ArrayList<String> gennedNames = new ArrayList<String>(Arrays.asList(
        "void testAlpha()", "void testBeta()", "void testEpsilon()", "void testGamma()"));
    ArrayList<String> testNames = new ArrayList<String>(Arrays.asList(
        "void testAlpha()", "void testBeta()", "void testEpsilon()"));
    TripleMap tMap = loadTripleMap(srcNames, gennedNames, testNames);

    // RUN Find the difference between the actual test names and genned names
    Map<String, String> augMap = tMap.buildAugMap();

    // VERIFY
    printMap("Methods to add to test file: ", augMap);

    // NORMAL: One new test name to add
    assertEquals(1, augMap.size());
  }


  /**
   * @Normal.Test {@code Map<String, String> buildAugMap()} -- one new src method creates one test
   *              method
   */
  @Test
  public void testBuildAugList_TwoSrcMethodsAtEnd()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    ArrayList<String> srcNames = new ArrayList<String>(Arrays.asList(
        "String alpha(int)", "void beta(String)", "File epsilon(String)", "long gamma(File)",
        "File zeta(String)"));
    ArrayList<String> gennedNames = new ArrayList<String>(Arrays.asList(
        "void testAlpha()", "void testBeta()", "void testEpsilon()", "void testGamma()",
        "void testZeta()"));
    ArrayList<String> testNames = new ArrayList<String>(Arrays.asList(
        "void testAlpha()", "void testBeta()", "void testEpsilon()"));
    TripleMap tMap = loadTripleMap(srcNames, gennedNames, testNames);

    // RUN Find the difference between the actual test names and genned names
    Map<String, String> augMap = tMap.buildAugMap();

    // VERIFY
    printMap("Methods to add to test file: ", augMap);

    // NORMAL: One new test name to add
    assertEquals(2, augMap.size());
  }

  
  /**
   * @Normal.Test void ConvertSrcToTestNames() -- convert srcNames list to srcToTestNames list,
   *              ensuring there are no duplicates (e.g. overloaded method names)
   */
  @Test
  public void testConvertSrcToTestNames()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP  Three overloaded names are included
    ArrayList<String> srcNames = new ArrayList<String>(Arrays.asList(
        "String alpha(int)", "void alphaDog(String)", "File gamma(String)", "long gamma(File)",
        "File gamma2(String)"));
    ArrayList<String> expNames = new ArrayList<String>(Arrays.asList(
        "void testAlpha()", "void testAlphaDog()", "void testGamma1()", "void testGamma21()",
        "void testGamma22()"));
    TripleMap tmap = new TripleMap();

    // RUN
    ArrayList<String> genList = tmap.convertSrcToTestNames(srcNames, new ArrayList<>());

    // VERIFY
    assertEquals(srcNames.size(), genList.size());
    assertEquals(expNames.size(), genList.size());
    for (int k = 0; k < expNames.size(); k++) {
      assertEquals(expNames.get(k), genList.get(k));
    }
  }


  /**
   * @Not.Needed ArrayList<String> addExport(TripleMap$NameType) -- Switch case wrapper
   */
  @Test
  public void testExport()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
  
    MsgCtrl.errMsgln("\t\t Switch case wrapper. No need to test");
  }


  /**
   * @Not.Needed Map<String, String> exportAugMap() -- Simple wrapper
   */
  @Test
  public void testExportAugMap()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
  
    MsgCtrl.errMsgln("\t\t Wrapper. No need to test");
  }

  /**
   * @Not.Needed void setMapList(TripleMap$NameType, ArrayList) -- Simple setter
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
   * Load a triple map with the three name lists
   * 
   * @param srcList list of source signatures
   * @param gennedList list of test names generated from source signatures
   * @param testList list of test names from test file
   * @@return the loaded TripleMap
   */
  private TripleMap loadTripleMap(ArrayList<String> srcList, ArrayList<String> gennedList,
      ArrayList<String> testList)
  {
    TripleMap tMap = new TripleMap();
    tMap.setMapList(TripleMap.NameType.SRC, srcList);
    tMap.setMapList(TripleMap.NameType.SRC_TO_TEST, gennedList);
    tMap.setMapList(TripleMap.NameType.TEST, testList);

    return tMap;
  }


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
    if (amap.isEmpty()) {
      MsgCtrl.msgln("\n Map is empty");
      return;
    }
    MsgCtrl.msgln("\n" + msg);
    for (String key : amap.keySet()) {
      MsgCtrl.msgln("\t" + key + "\t \\\\  " + amap.get(key));
    }
  }


} // end of TestTripleMap test class
