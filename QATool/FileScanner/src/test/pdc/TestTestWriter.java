/**
 * TestTestWriter.java Copyright (c) 2017, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com.
 */

package test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.MsgCtrl;
import pdc.TestWriter;

/**
 * @author --generated by QA Tool--
 * @version April 26, 2017 // original <br>
 */
public class TestTestWriter
{
   /** File to use for target pracice */
   private String _targetPath =
         "/Projects/eChronos/QATool/FileScanner/src/test/pdc/targetFile.java";

   /** List of source code names */
   private ArrayList<String> _srcList;
   /** List of test names converted from the source code names */
   private ArrayList<String> _testList;

   /** Map of src signatures to corresponding test method names */
   private Map<String, String> _augmap;


   // Create the target object
   private TestWriter _tw;


   /**
    * @throws java.lang.Exception -- general catchall for exceptions not caught by the tests
    */
   @BeforeClass
   public static void setUpBeforeClass() throws Exception
   {}

   /**
    * @throws java.lang.Exception -- general catchall for exceptions not caught by the tests
    */
   @AfterClass
   public static void tearDownAfterClass() throws Exception
   {}

   /**
    * @throws java.lang.Exception -- general catchall for exceptions not caught by the tests
    */
   @Before
   public void setUp() throws Exception
   {
      // Create the target object
      _tw = new TestWriter(false, false);
      // Initialize a set of src methods
      _srcList = new ArrayList<>(Arrays.asList(
            "int alpha(File)", "boolean beta(boolean)", "String gamma(ArrayList<String>)"));
      _testList = new ArrayList<>(Arrays.asList(
            "void testAlpha()", "void testBeta()", "void testGamma()"));
      // Load a augMap of test names (no nulls or duplicates allowed in a TreeMap)
      _augmap = new TreeMap<>();
      for (int k = 0; k < _srcList.size(); k++) {
         _augmap.put(_srcList.get(k), _testList.get(k));
      }
   }

   /**
    * @throws java.lang.Exception -- general catchall for exceptions not caught by the tests
    */
   @After
   public void tearDown() throws Exception
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      // _targetFile.delete();
      _testList = null;
      _srcList = null;
      _tw = null;
   }


   // ===============================================================================
   // BEGIN TESTING
   // ===============================================================================

   /**
    * @Null.Test File augmentTestFile(File, Map) -- Throws NullPointerException for Null parms
    */
   @Test
   public void testAugmentTestFileNullParms()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // RUN Null File parm
      Map<String, String> augmap = new TreeMap<>();
      try {
         _tw.augmentTestFile(null, augmap);
      } catch (NullPointerException ex) {
         MsgCtrl.errMsgln("\tExpected exception: " + ex.getMessage());
      }
      // RUN Null Map parm
      File target = new File(_targetPath);
      try {
         _tw.augmentTestFile(target, null);
      } catch (NullPointerException ex) {
         MsgCtrl.errMsgln("\tExpected exception: " + ex.getMessage());
      }

      // TEARDOWN
      target.delete();
   }


   /**
    * @Error.Test File augmentTestFile(File, Map) -- If file doesn't exist, throws
    *             IllegalArgumentException
    */
   @Test
   public void testAugmentTestNoFile()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // SETUP: Try to augment a non-existent file
      Map<String, String> augmap = new TreeMap<>();
      File target = new File(_targetPath);

      // RUN
      try {
         _tw.augmentTestFile(target, augmap);
      } catch (IllegalArgumentException ex) {
         MsgCtrl.errMsgln("\tExpected exception: " + ex.getMessage());
      }

      // TEARDOWN
      target.delete();
   }


   /**
    * @Error.Test File augmentTestFile(File, Map) -- Empty map returns original file
    */
   @Test
   public void testAugmentTestEmptyMap()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // SETUP
      Map<String, String> augmap = new TreeMap<>();
      File target = new File(_targetPath);
      try {
         target.createNewFile();
      } catch (IOException ex) {
         fail("Unexpeced IOException error trying to create target file");
      }
      long len = target.length();
      File resultFile = null;

      // RUN
      try {
         resultFile = _tw.augmentTestFile(target, augmap);
      } catch (IllegalArgumentException ex) {
         MsgCtrl.errMsgln("\tExpected exception: " + ex.getMessage());
      }
      assertEquals(len, resultFile.length());

      // TEARDOWN
      resultFile.delete();
   }


   /**
    * @Normal.Test File augmentTestFile(File, Map) -- Add another method to an existing test file
    */
   @Test
   public void testAugmentTestFile()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // SETUP: Create an exsiting test file with the test methods
      File target = _tw.writeNewTestFile(new File(_targetPath), _augmap);
      long resultLen = target.length();
      MsgCtrl.msgln("\tOriginal test file length = " + resultLen);
      // Augmap should contain only 1 method addition for this test
      _augmap.clear();
      _augmap.put("String newMethod(String)", "void testNewMethod()");

      // RUN: Add a new method to the existing file
      File augFile = _tw.augmentTestFile(target, _augmap);

      // VERIFY
      long augLen = augFile.length();
      MsgCtrl.msgln("\tAugmented file length = " + augLen);
      // Should be incremented by single method block length
      assertEquals(resultLen + 244, augLen, 25);

      // TEARDOWN
      target.delete();
   }


   /**
    * @Normal.Test File writeNewTestFile(File, Map) -- create a file and write to it
    */
   @Test
   public void testWriteNewTestFile()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // SETUP: Create an exsiting test file with the test methods
      File target = new File(_targetPath);
      assertTrue(!target.exists());

      // Run
      target = _tw.writeNewTestFile(target, _augmap);
      MsgCtrl.msgln("New file created of length " + target.length());
      assertEquals(2379, target.length(), 25);
   }


   /**
    * @Error.Test File writeNewTestFile(File, Map) -- Throws IllegalArgumentException if file to be
    *             created already exists
    */
   @Test
   public void testWriteNewTestFileExists()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // SETUP: Create an existing test file with the test methods
      File target = new File(_targetPath);
      if (!target.exists()) {
         try {
            target.createNewFile();
         } catch (IOException exio) {
            MsgCtrl.errMsgln("Cannot create the test file to setup the test");
         }
      }
      assertTrue(target.exists());

      // RUN
      try {
         target = _tw.writeNewTestFile(target, _augmap);
      } catch (IllegalArgumentException ex) {
         MsgCtrl.errMsgln("\tExpected Exception: " + ex.getMessage());
      }
      // TEARDOWN
      target.delete();
   }


   /**
    * @Error.Test File writeNewTestFile(File, Map) -- Empty map returns empty File
    */
   @Test
   public void testWriteNewTestFileNoMap()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // SETUP Clear the map for the error test and ensure no file exists
      _augmap.clear();
      File target = new File(_targetPath);
      target.delete();

      // RUN
      File resultFile = _tw.writeNewTestFile(target, _augmap);
      assertEquals(resultFile, target);
      // TEARDOWN
      target.delete();
      resultFile.delete();
   }


   /**
    * @Normal.Test String makeTestFilename(String) -- convert a source path in the {@code src}
    *              folder to a corresponding test path in the {@code test} directory.
    */
   @Test
   public void testMakeTestFilename()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // SETUP
      String srcPath = "/Projects/eChronos/QATool/FileScanner/src/pdc/QAUtils.java";

      // RUN
      String testName = _tw.makeTestFilename(srcPath);
      assertEquals("/Projects/eChronos/QATool/FileScanner/src/test/pdc/TestQAUtils.java", testName);
   }


   /**
    * @Error.Test String makeTestFilename(String) -- throws IllegalArgumentException if target file
    *             not within {@code src} folder
    */
   @Test
   public void testMakeTestFilenameNoSrc()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // SETUP
      String notSrcPath = "/Projects/eChronos/QATool/FileScanner/QAUtils.java";
      // RUN
      try {
         _tw.makeTestFilename(notSrcPath);
      } catch (IllegalArgumentException ex1) {
         MsgCtrl.errMsgln("\tExpected Exception: " + ex1.getMessage());
      }
   }


   /**
    * @Error.Test String makeTestFilename(String) -- throws IllegalArgumentException if target file
    *             is not a {@code .java} file
    */
   @Test
   public void testMakeTestFilenameNotJava()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // SETUP
      String notJavaPath = "/Projects/eChronos/QATool/FileScanner/QAUtils.docx";
      // RUN
      try {
         _tw.makeTestFilename(notJavaPath);
      } catch (IllegalArgumentException ex2) {
         MsgCtrl.errMsgln("\tExpected Exception: " + ex2.getMessage());
      }
   }


   /**
    * @Null.Test String makeTestFilename(String) -- throws IllegalArgumentException if srcPath is
    *            null
    */
   @Test
   public void testMakeTestFilenameNullPath()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // RUN
      try {
         _tw.makeTestFilename(null);
      } catch (IllegalArgumentException ex2) {
         MsgCtrl.errMsgln("\tExpected Exception: " + ex2.getMessage());
      }
   }


   // ===============================================================================
   // PRIVATE HELPERS
   // ===============================================================================



} 	// end of TestTestWriter.java class
