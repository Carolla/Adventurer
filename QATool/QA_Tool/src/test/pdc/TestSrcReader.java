/**
 * TestSrcReader.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com.
 */

package test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.MsgCtrl;
import pdc.QAFileScan;
import pdc.SrcReader;
import pdc.SrcReader.MockSrcReader;

/**
 * @author Al Cline
 * @version December 21, 2016 // original <br>
 */
public class TestSrcReader
{
   private SrcReader _srcRdr = null;

   // Name of source file to scan for testing
   static private final String TARGET_PATHNAME =
         "/Projects/eChronos/QATool/QA_Tool/src/pdc/TargetSrcFile.java";
   // Name of source tree root to scan for testing
   static private final String TARGET_ROOTNAME = "/Projects/eChronos/QATool/QA_Tool/src";


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
      // Create the srcReader once for all tests
      _srcRdr = new SrcReader(new File(TARGET_ROOTNAME), null);
      assertNotNull(_srcRdr);
   }

   /**
    * @throws java.lang.Exception
    */
   @After
   public void tearDown() throws Exception
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      _srcRdr = null;
   }


   // ===============================================================================
   // BEGIN TESTING
   // ===============================================================================

   /**
    * @NORMAL_TEST void fileScan(File)
    */
   @Test
   public void testFileScan()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      ArrayList<String> srcList = new ArrayList<String>();
      try {
         srcList = _srcRdr.fileScan(new File(TARGET_PATHNAME));
      } catch (ClassNotFoundException e) {
         fail("\tUnexpected exception thrown");
      }
      assertEquals(2, srcList.size());
   }

   /**
    * @NORMAL_TEST void fileScan(File)
    */
   @Test
   public void testFileScan_MissingFile()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // Null file input
      ArrayList<String> srcList = new ArrayList<String>();
      try {
         srcList = _srcRdr.fileScan(null);
      } catch (ClassNotFoundException e) {
         fail("\tUnexpected exception thrown" + e.getMessage());
      }
      assertNull(srcList);

      // Deliberately invalid file
      File badFile = new File("/Projects/eChronos/QATool/QA_Tool/src/pdc/dummy.java");
      srcList = new ArrayList<String>();
      try {
         srcList = _srcRdr.fileScan(badFile);
      } catch (ClassNotFoundException e) {
         fail("\tUnexpected exception thrown" + e.getMessage());
      }
      assertNull(srcList);

      // A test file instead of a source file
      File testFile =
            new File("/Projects/eChronos/QATool/QA_Tool/src/test/pdc/TestTargetSrcFile.java");
      srcList = new ArrayList<String>();
      try {
         srcList = _srcRdr.fileScan(testFile);
      } catch (IllegalArgumentException ex) {
         MsgCtrl.msgln("\tExpected Exception thrown");
      } catch (ClassNotFoundException e) {
         fail("\tUnexpected exception thrown" + e.getMessage());
      }
      assertNull(srcList);

   }


   /**
    * @NORMAL_TEST void scan(File)
    */
   @Test
   public void testScan()
   {
      boolean state = false;
      MsgCtrl.auditMsgsOn(state);
      MsgCtrl.errorMsgsOn(state);
      MsgCtrl.where(this);
      // Turn audit traisl on or off for testing
      QAFileScan._verbose = state;

      // SETUP
      MockSrcReader mock = _srcRdr.new MockSrcReader();
      File srcRoot = new File(TARGET_ROOTNAME);
      // Scan a tree of files, but turn on audit messages within

      // RUN
      _srcRdr.scan(srcRoot);
      _srcRdr.scanResults();

      // VERIFY
      // Results returned in this order: dirs scanned, files scanned, dir skipped, files skipped
      int[] results = mock.getScanResults();
      assertEquals(1, results[0]);  // dirs scanned: src and src.pdc
      assertEquals(8, results[1]);  // files scanned: Prototype, QAFileScan, QATool, QAUtils,
                                    // SrcReader, SuiteBuilder, TargetSrcFile, TestWriter
      assertEquals(1, results[2]);  // dirs skipped: src.test
      assertEquals(3, results[3]);  // files skipped: _QAScanner, .DS_Store, ScanExclusions.txt

      // TEARDOWN
      QAFileScan._verbose = false;
   }


   /**
    * @NORMAL_TEST void scan(File)
    */
   @Test
   public void testScan_ChronosLib()
   {
      boolean state = true;
      MsgCtrl.auditMsgsOn(state);
      MsgCtrl.errorMsgsOn(state);
      MsgCtrl.where(this);
      // Turn audit traisl on or off for testing
      QAFileScan._verbose = state;

      // SETUP
      MockSrcReader mock = _srcRdr.new MockSrcReader();
      String ChronosLibPath = "/Projects/eChronos/ChronosLib/src/chronos";
      File srcRoot = new File(ChronosLibPath);
      // Scan a tree of files, but turn on audit messages within

      // RUN
      _srcRdr.scan(srcRoot);
      _srcRdr.scanResults();

      // VERIFY
      // Results returned in this order: dirs scanned, files scanned, dir skipped, files skipped
      int[] results = mock.getScanResults();
      assertEquals(8, results[0]);   // dirs scanned: civ, hic, pdc, pdc.buildings, pdc.character,
                                     // pdc.command, pdc.race, pdc.registry
      assertEquals(60, results[1]);  // files scanned: civ(3), hic(1), pdc(9), pdc.buildings(9),
                                     // pdc.character(10), pdc.command(8), pdc.race(10),
                                     // pdc.registry(10)
      assertEquals(1, results[2]);   // dirs skipped: test
      assertEquals(0, results[3]);   // files skipped: 0

      // TEARDOWN
      QAFileScan._verbose = false;
   }


   /**
    * @NORMAL_TEST void scan(File)
    */
   @Test
   public void testScan_MyLibrary()
   {
      boolean state = false;
      MsgCtrl.auditMsgsOn(state);
      MsgCtrl.errorMsgsOn(state);
      MsgCtrl.where(this);
      // Turn audit traisl on or off for testing
      QAFileScan._verbose = state;

      // SETUP
      MockSrcReader mock = _srcRdr.new MockSrcReader();
      String MyLibraryPath = "/Projects/eChronos/MyLibrary/src/mylib";
      File srcRoot = new File(MyLibraryPath);
      // Scan a tree of files, but turn on audit messages within

      // RUN
      _srcRdr.scan(srcRoot);
      _srcRdr.scanResults();

      // VERIFY
      // Results returned in this order: dirs scanned, files scanned, dir skipped, files skipped
      int[] results = mock.getScanResults();
      assertEquals(4, results[0]);  // dirs scanned: dmc, hic, libResources, pdc
      assertEquals(9, results[1]);  // files scanned: mylib(3), dmc(2), hic(1), pdc(3)
      assertEquals(1, results[2]);  // dirs skipped: mylib.test
      assertEquals(5, results[3]);  // files skipped: libResources(5)

      // TEARDOWN
      QAFileScan._verbose = false;
   }


   /**
    * @NORMAL_TEST void scanResults()
    */
   @Test
   public void testScanResults()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      MsgCtrl.errMsgln(
            "\t\tNo need to test this display-only method more than it is verified in testScan()");
   }


} 	// end of TestSrcReader.java class
