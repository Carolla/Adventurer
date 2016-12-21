/**
 * TestPrototype.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */


package test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.MsgCtrl;
import pdc.Prototype;

/**
 * @author Al Cline
 * @version November 16, 2016 // original <br>
 *          November 24, 2016    // added all test methods but <code>forceUnique</code>  <br>
 *          Dec 20, 2016    // refactored after some minor changes in other files <br>
 */
public class TestPrototype
{
   // Object doing the work
   private Prototype _proto;
   // Test file created from the source file
   private File _targetFile;

   // Name of test file created from the source file
   static private final String TARGET_PATHNAME =
         "/Projects/eChronos/QATool/QA_Tool/src/test/pdc/TestTargetSrcFile.java";

   // Define the src method names for the comment line
   static ArrayList<String> _origSrcList = new ArrayList<String>();
   static ArrayList<String> _origTestList = new ArrayList<String>();
   static ArrayList<String> _augSrcList = new ArrayList<String>();
   static ArrayList<String> _augTestList = new ArrayList<String>();
   static String[] _origSrcMethods = {"File alpha(String)", "String beta(File)"};
   static String[] _origTestMethods = {"void testAlpha()", "void testBeta()"};
   static String[] _augSrcMethods = {"String delta(long)", "void gamma()"};
   static String[] _augTestMethods = {"void testDelta()", "void testGamma()"};

   // File size for the the file containing the above methods
   private final long TARGET_FILELEN = 1917L;
   private final long AUGTARGET_FILELEN = 2454L;


   /**
    * @throws java.lang.Exception
    */
   @BeforeClass
   public static void setUpBeforeClass() throws Exception
   {
      // The target source file method names to use
      _origSrcList.add(_origSrcMethods[0]);
      _origSrcList.add(_origSrcMethods[1]);

      // The target source file method names to use
      _origTestList.add(_origTestMethods[0]);
      _origTestList.add(_origTestMethods[1]);

      // The source file method names to use in comments
      _augSrcList.add(_augSrcMethods[0]);
      _augSrcList.add(_augSrcMethods[1]);

      // The target test file method names to augment into target
      _augTestList.add(_augTestMethods[0]);
      _augTestList.add(_augTestMethods[1]);
   }

   /**
    * @throws java.lang.Exception
    */
   @AfterClass
   public static void tearDownAfterClass() throws Exception
   {
      _augTestList.clear();
      _augSrcList.clear();
      _origTestList.clear();
      _origSrcList.clear();
   }

   /**
    * @throws java.lang.Exception
    */
   @Before
   public void setUp() throws Exception
   {
      MsgCtrl.auditMsgsOn(true);
      MsgCtrl.errorMsgsOn(true);
      _proto = new Prototype();

      // Create a file containing the source file to work with
      _targetFile = new File(TARGET_PATHNAME);
      assertNotNull(_targetFile);
   }

   /**
    * @throws java.lang.Exception
    */
   @After
   public void tearDown() throws Exception
   {
      _proto = null;
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
   }


   // ========================================================================
   // BEGIN TESTING
   // ========================================================================

   /**
    * @NORMAL_TEST File augmentTestFile(File, ArrayList, ArrayList)
    */
   @Test
   public void testAugmentWithoutNewMethods()
   {
      MsgCtrl.auditMsgsOn(true);
      MsgCtrl.errorMsgsOn(true);
      MsgCtrl.where(this);

      // SETUP
      // Create an existing (known) test file
      File newTargetFile = _proto.writeNewTestFile(_targetFile, _origSrcList, _origTestList);
      assertTrue(newTargetFile.exists());

      // Define the methods list for the src
      displayList("\tMethods in " + _targetFile.getName(), _origTestList);
      // Define no methods to be augmented
      ArrayList<String> noAugList = new ArrayList<String>();
      MsgCtrl.msgln("\tNo methods to add to " + _targetFile.getName());

      // EXECUTE
      File augFile = _proto.augmentTestFile(_targetFile, _origSrcList, noAugList);

      // VERIFY
      long augFileLen = augFile.length();
      assertEquals(TARGET_FILELEN, augFileLen);
   }


   /**
    * @ERROR_TEST File augmentTestFile(File, ArrayList, ArrayList)
    */
   @Test
   public void testAugmentWithoutSrc()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // Augment an existing file without adding any methods and without changing it
      // Define the methods list for the src
      ArrayList<String> nonSrcList = new ArrayList<String>();
      MsgCtrl.msgln("\tNo source methods.");

      // EXECUTE & VERIFY
      assertNull(_proto.augmentTestFile(_targetFile, nonSrcList, _augTestList));
   }


   /**
    * @NORMAL_TEST File augmentTestFile(File, ArrayList, ArrayList)
    * Add two methods to the existing test file
    */
   @Test
   public void testAugmentTestFile()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // Create an existing (known) test file
      File newTargetFile = _proto.writeNewTestFile(_targetFile, _origSrcList, _origTestList);
      assertTrue(newTargetFile.exists());

      // The version of the file will change by two method blocks and a version line
      // These are the source signature
      displayList("Original Source methods:", _origSrcList);
      displayList("Original Test methods:", _origTestList);
      displayList("Source methods to add into comments:", _augSrcList);
      displayList("Attempting to add new Test methods:", _augTestList);
      MsgCtrl.msgln("\n\n");

      // EXECUTE
      File augFile = _proto.augmentTestFile(_targetFile, _augSrcList, _augTestList);
      assertNotNull(augFile);
      MsgCtrl.msgln("\n\t augmented file created: " + augFile.getPath());
      MsgCtrl.msgln("\tsize = " + augFile.length());

      // VERIFY
      // The version of the file will change by two method blocks and a version line
      assertEquals(AUGTARGET_FILELEN, augFile.length());
   }

   
   /**
    * @NORMAL_TEST ArrayList<String> forceUnique(ArrayList)
    */
   @Test
   public void testForceUnique()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // SETUP
      // Put the expected results into an arraylist
      String[] expMethods = {"File alpha1(String)", "File alpha2(String)", "String beta1(File)", 
            "String beta2(File)"};
      ArrayList<String> expList = new ArrayList<String>();
      for (int k=0; k < expMethods.length; k++) {
         expList.add(expMethods[k]);
      }
      // Put two copies of srclist into the forceUnique method
      ArrayList<String> dupList = new ArrayList<String>();
      for (String s : _origSrcList) {
         dupList.add(s);
         dupList.add(s);
      }
      displayList("Input list", dupList);

      // RUN
      // Sort the existing fields first
      ArrayList<String> uniqSrcList = _proto.forceUnique(dupList);

      // VERIFY
      displayList("Output list", uniqSrcList);
      assertTrue(uniqSrcList.size() == expList.size());
      for (int k=0; k < uniqSrcList.size(); k++) {
         assertTrue(uniqSrcList.get(k).equals(expList.get(k)));
      }
   }


   /**
    * @NORMAL_TEST File writeNewTestFile(File, ArrayList, ArrayList)
    */
   @Test
   public void testWriteNewTestFile()
   {
      MsgCtrl.auditMsgsOn(true);
      MsgCtrl.errorMsgsOn(true);
      MsgCtrl.where(this);

      // SETUP
      // Delete the test file created in setUp()
      if (_targetFile.exists()) {
         _targetFile.delete();
      }
      assertFalse(_targetFile.exists());

      displayList("Source methods:", _origSrcList);
      displayList("Converted to test methods:", _origTestList);
      MsgCtrl.msgln("\n\n");

      // RUN
      File newTarget = _proto.writeNewTestFile(_targetFile, _origSrcList, _origTestList);

      // VERIFY
      assertTrue(newTarget.exists());
      assertEquals(TARGET_FILELEN, newTarget.length());
   }

   
   // ========================================================================
   // Private Helper Methods
   // ========================================================================

   private void displayList(String msg, ArrayList<String> aList)
   {
      MsgCtrl.msgln(msg);
      for (String s : aList) {
         MsgCtrl.msgln("\t " + s);
      }
   }


} 	// end of TestPrototype.java class
