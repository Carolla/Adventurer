/**
 * TestTestWriter.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com.
 */

package test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.MsgCtrl;
import pdc.QAUtils;
import pdc.TestWriter;

/**
 * @author Al Cline
 * @version December 26, 2016 // original <br>
 */
public class TestTestWriter
{
   private TestWriter _tw;

   private String TEST_PATHNAME =
         "/Projects/eChronos/QATool/QA_Tool/src/test/pdc/TestTargetSrcFile.java";

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
      _tw = new TestWriter();
      assertNotNull(_tw);
   }

   /**
    * @throws java.lang.Exception
    */
   @After
   public void tearDown() throws Exception
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);

      _tw = null;
   }


   // ===============================================================================
   // BEGIN TESTING
   // ===============================================================================

   /**
    * @NORMAL_TEST ArrayList convertToTestNames(ArrayList)
    */
   @Test
   public void testConvertToTestNames()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // SETUP
      String[] srcAry = {"void alpha(String)", "File beta()",
            "ArrayList<String> gamma(File x, String y)", "int alpha(int)", "String alpha(int)"};
      String[] testAry = {"void testAlpha1()", "void testAlpha2()", "void testAlpha3()",
            "void testBeta()", "void testGamma()"};
      ArrayList<String> resultsList = new ArrayList<String>();
      ArrayList<String> srcList = QAUtils.createList(srcAry);
      ArrayList<String> testList = QAUtils.createList(testAry);

      // RUN
      resultsList = _tw.convertToTestNames(srcList);

      // VERIFY test names in alphabetical order
      assertTrue(testList.size() == resultsList.size());
      for (int k = 0; k < resultsList.size(); k++) {
         MsgCtrl.msgln("\t" + resultsList.get(k));
         assertTrue(resultsList.get(k).equals(testList.get(k)));
      }
   }


   /**
    * @NORMAL_TEST ArrayList convertToTestNames(ArrayList)
    */
   @Test
   public void testConvertToTestNames_NoList()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // Run Null List
      ArrayList<String> emptyList = new ArrayList<String>();
      emptyList = _tw.convertToTestNames(null);
      assertNull(emptyList);

      // Run empty list
      emptyList = new ArrayList<String>();
      emptyList = _tw.convertToTestNames(emptyList);
      assertEquals(0, emptyList.size());
   }


   /**
    * @NORMAL_TEST String makeTestFilename(String)
    */
   @Test
   public void testMakeTestFilename()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // Setup
      // Filenames do not need to be capitalized for this method to work
      String[] pathNames = {"/project/name/src/File.java", "/project/name/src/Filex.java",
            "/project/src/subdir/dir2/X.java", "/project/name/src/lower.java"};
      String[] testNames = {"/project/name/src/test/TestFile.java",
            "/project/name/src/test/TestFilex.java", "/project/src/test/subdir/dir2/TestX.java",
            "/project/name/src/test/Testlower.java"};

      // Run & Verify
      for (int k = 0; k < pathNames.length; k++) {
         String expName = _tw.makeTestFilename(pathNames[k]);
         MsgCtrl.msgln("\t" + pathNames[k] + "\t->\t" + expName);
         assertTrue(testNames[k].equals(expName));
      }
   }

   /**
    * @NORMAL_TEST String makeTestFilename(String)
    */
   @Test
   public void testMakeTestFilename_BadInput()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // Setup
      // Missing src keyword inside path
      String[] badPath = {"/project/name/srcdir/File.java", "/project/name/src/SomeFile"};

      // Run & Verify
      for (int k = 0; k < badPath.length; k++) {
         try {
            _tw.makeTestFilename(badPath[k]);
            fail("\tException expected, not received");
         } catch (IllegalArgumentException ex) {
            MsgCtrl.errMsgln("\tExpected exception: \t" + ex.getMessage());
         }
      }
   }


   /**
    * @NORMAL_TEST void writeResults()
    */
   @Test
   public void testWriteResults()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      MsgCtrl.errMsgln("\t\t No need to test wrapper methods");
   }


   /**
    * @NORMAL_TEST File writeTestFile(File, ArrayList, ArrayList)
    */
   @Test
   public void testWriteTestFile()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // SETUP
      String testPath = "/Projects/eChronos/QATool/QA_Tool/src/test/pdc/TestTargetSrcFile.java";
      long targetLength = 2619L;
      String[] srcAry = {"void alpha(String)", "File beta()",
            "ArrayList<String> gamma(File x, String y)", "int alpha(int)", "String alpha(int)"};
      String[] testAry = {"void testAlpha1()", "void testAlpha2()", "void testAlpha3()",
            "void testBeta()", "void testGamma()"};
      ArrayList<String> srcList = QAUtils.createList(srcAry);
      ArrayList<String> testList = QAUtils.createList(testAry);

      // Ensure that this file does not exist
      File target = new File(testPath);
      target.delete();
      assertTrue(!target.exists());

      // RUN to create new test file
      _tw.writeTestFile(target, srcList, testList);

      // Verify file exists
      assertTrue(target.exists());
      assertEquals(targetLength, target.length());

      // TEARDOWN
      target.delete();

   }


   /**
    * @NORMAL_TEST File writeTestFile(File, ArrayList, ArrayList)
    */
   @Test
   public void testWriteTestFile_Augment()
   {
      MsgCtrl.auditMsgsOn(true);
      MsgCtrl.errorMsgsOn(true);
      MsgCtrl.where(this);

      // SETUP
      String testPath = "/Projects/eChronos/QATool/QA_Tool/src/test/pdc/TestTargetSrcFile.java";
      long existingTargetLength = 2134L;
      long newTargetLength = 2702L;
      // Methods existing in source and test files :
      String[] oldSrc = {"void alpha(String)", "File beta(int z)", "int beta(String x)"};
      String[] oldTests = {"void alpha()", "void beta1()", "void beta2()"};
      // Create an "existing" file to augment
      File target = _tw.writeTestFile(new File(testPath), QAUtils.createList(oldSrc),
            QAUtils.createList(oldTests));
      assertEquals(existingTargetLength, target.length());

      // Methods to add:
      String[] newSrc = {"String gamma(String[] s", "ArrayList<String> gamma(File x, String y)"};
      String[] newTests = {"void testGamma1()", "void gamma2()"};

      // Ensure that this file does exists
      target = _tw.writeTestFile(new File(TEST_PATHNAME), QAUtils.createList(newSrc),
            QAUtils.createList(newTests));

      // Verify file exists
      assertTrue(target.exists());
      assertEquals(newTargetLength, target.length());

      // TEARDOWN
      target.delete();

   }


   // ===============================================================================
   // Private Helper methods
   // ===============================================================================


} 	// end of TestTestWriter.java class
