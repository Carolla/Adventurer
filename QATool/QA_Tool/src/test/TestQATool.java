/**
 * TestQATool.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test;

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.Constants;
import mylib.MsgCtrl;
import pdc.QATool;
import pdc.QATool.MockTool;


/**
 * @author Alan Cline
 * @version Dec 31, 2015 // original <br>
 *          Jan 19 2016 // updated for unchanging file structure instead of live directories <br>
 *          Feb 23 2016 // updated for unchanging simTree structure and be platform-independent <br>
 *          Mar 8 2016 // removed simTree and debugged platform-independency <br>
 *          Jul 18 2016 // semi-final testing pass <br>
 */
public class TestQATool
{
   /** Root for all source files and subdirectories */
   static private final String ROOT =
         System.getProperty("user.dir") + Constants.FS + "src" + Constants.FS;
   /** Exlusion file must be directly beneath src root */
   static private final String EXCLUDE_FILE = "ScanExclusions.txt";
   /** Optional verbose flag argument */
   static private final String VERBOSE_FLAG = "-v";


   static private QATool _qat;
   static private MockTool _mock;

   /**
    * A list of files for testing: <br>
    * [1] Empty Source file <br>
    * [2] Source file in the root src dir missing a complete test file <br>
    * [3] Source file in a src subdir missing a complete test file <br>
    * [4] Source file with a few missing test cases in its test file <br>
    * [5] Source file with three overloaded methods, differing only by parms <br>
    */
   static private String[] srcFiles = {
         "SrcEmpty,java",   // [1]
         "SrcMissingTests.java", // [2]
         "subDir" + Constants.FS + "TestSubDirSource.java", // [3]
         "SrcMissingSomeTests.java", // [4]
         "SrcWithOverloadedMethods.java",
   };

   /** Expected test files for given source files */
   static private String[] expTestFiles = {
         "TestSrcEmpty,java",   // [1]
         "TestSrcMissingTests.java", // [2]
         "subDir" + Constants.FS + "TestSubDirSource.java", // [3]
         "TestSrcMissingSomeTests.java", // [4]
         "TestSrcWithOverloadedMethods.java",
   };

   static private ArrayList<File> _srcFiles = new ArrayList<File>();
   static private ArrayList<File> _tstFiles = new ArrayList<File>();


   /**
    * @throws java.lang.Exception
    */
   @BeforeClass
   public static void setUpBeforeClass() throws Exception
   {
      // Create a list of fake source files for testing
      for (String s : srcFiles) {
         _srcFiles.add(new File(ROOT + s));
      }
   }

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
   }

   /**
    * @throws java.lang.Exception
    */
   @After
   public void tearDown() throws Exception
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      _mock = null;
      _qat = null;
   }


   // ======================================================================
   // BEGIN TESTS
   // ======================================================================

   /**
    * @ERROR.TEST QATool(String[] args = root, exclusion file, verbose flag)
    */
   @Test
   public void testCtorVerbose()
   {
      MsgCtrl.auditMsgsOn(true);
      MsgCtrl.errorMsgsOn(true);
      MsgCtrl.where(this);

      String[] twoArgs = {ROOT, EXCLUDE_FILE};
      String[] threeArgs = {ROOT, EXCLUDE_FILE, VERBOSE_FLAG};

      // Test the non-verbose version first
      QATool.main(threeArgs);
   }
   
   
   
   
//   /**
//    * @ERROR.TEST QATool(String path)
//    */
//   @SuppressWarnings("unused")
//   //@Test
//   public void testErrorCtorNullParm()
//   {
//      MsgCtrl.auditMsgsOn(true);
//      MsgCtrl.errorMsgsOn(true);
//      MsgCtrl.where(this);
//
//      String[] TwoArgs = {ROOT, EXCLUDE_FILE};
//      String[] ThreeArgs = {ROOT, EXCLUDE_FILE, VERBOSE_FLAG};
//
//      // Path is set to null
//      try {
//         QATool badTool1 = new QATool(ROOT, EXCLUDE_FILE, null);
//      } catch (IllegalArgumentException ex) {
//         MsgCtrl.msgln("\ttestCtorError: expected exception caught");
//         assertNotNull(ex);
//      }
//      // Exclusion path is set to null
//      try {
//         IllegalArgumentException ex = null;
//         QATool badTool1 = new QATool(ROOT, null);
//      } catch (IllegalArgumentException ex) {
//         MsgCtrl.msgln("\ttestCtorError: expected exception caught");
//         assertNotNull(ex);
//      }
//
//      // Path is not set to a directory
//      try {
//         IllegalArgumentException ex = null;
//         String filePath = ROOT + Constants.FS + "Chronos" + Constants.FS + "civ + Constants.FS" +
//               "UserMsg.java";
//         QATool badTool = new QATool(filePath, EXCLUDE_FILE);
//      } catch (IllegalArgumentException ex) {
//         MsgCtrl.msgln("\ttestCtorError: expected exception caught");
//         assertNotNull(ex);
//      }
//   }
//
//
//   /**
//    * @NORMAL.TEST File findTestDir(File root)
//    */
//   // @Test
//   public void testFindTestDir()
//   {
//      MsgCtrl.auditMsgsOn(false);
//      MsgCtrl.errorMsgsOn(false);
//      MsgCtrl.where(this);
//
//      // Normal case for current directory
//      String expPath = ROOT + "test";
//      MsgCtrl.msgln("\ttestFindTestDir: expPath = \t" + expPath);
//      File tf = _qat.findTestDir(new File(ROOT));
//      String resultPath = tf.getPath();
//      MsgCtrl.msgln("\ttestFindTestDir: result = \t" + resultPath);
//      assertTrue(resultPath.equals(expPath));
//   }
//
//
//   /**
//    * @ERROR.TEST File findTestDir(File root)
//    */
//   // @Test
//   public void testErrorFindTestDirBadPath()
//   {
//      MsgCtrl.auditMsgsOn(false);
//      MsgCtrl.errorMsgsOn(false);
//      MsgCtrl.where(this);
//
//      // Error case: cannot find a test dir
//      String simPath2 = ROOT + "deadend";
//      File tf = _qat.findTestDir(new File(simPath2));
//      assertNull(tf);
//
//      // Error return null when testDir is found after it is found once
//      // First find normal testdir before attempting second one
//      tf = _qat.findTestDir(new File(ROOT));
//      assertNotNull(tf);
//      tf = _qat.findTestDir(new File(simPath2));
//      assertNull(tf);
//   }
//
//
//   /**
//    * @ERROR.TEST File findTestDir(File root)
//    */
//   // @Test
//   public void testErrorFindTestDirNotFound()
//   {
//      MsgCtrl.auditMsgsOn(false);
//      MsgCtrl.errorMsgsOn(false);
//      MsgCtrl.where(this);
//
//      // Error case for a testdir not found
//      String simRoot = System.getProperty("user.dir");
//      StringBuilder sb = new StringBuilder(simRoot);
//      sb.append(Constants.FS);
//      sb.append("simTree"); // has not test subdir
//      String srcPath = sb.toString();
//      MsgCtrl.msgln("\tFindTestDir(); simulated source root = " + srcPath);
//
//      assertNull(_qat.findTestDir(new File(srcPath)));
//   }
//
//
//   /**
//    * @NORMAL.TEST ArrayList<String> writeNextTestFile(File srcDir, File testDir, String rootPath)
//    */
//   // @Test
//   public void testWriteNextTestFile()
//   {
//      MsgCtrl.auditMsgsOn(false);
//      MsgCtrl.errorMsgsOn(false);
//      MsgCtrl.where(this);
//
//      // SETUP to traverse the src tree
//      MsgCtrl.msgln("\tnavigatinf " + ROOT);
//      File srcDir = new File(ROOT);
//      File testDir = _qat.findTestDir(srcDir);
//      int srcLen = srcDir.getPath().length();
//
//      // Run the target method
//      ArrayList<String> srcPaths = _qat.writeNextTestFile(srcDir, testDir, srcLen);
//      for (String s : srcPaths) {
//         MsgCtrl.msgln("\t" + s);
//      }
//   }
//
//
//   /**
//    * @throws InterruptedException
//    * @NORMAL.TEST void treeScan(File srcDir)
//    */
//   // @Test
//   public void testTreeScan() throws InterruptedException
//   {
//      MsgCtrl.auditMsgsOn(false);
//      MsgCtrl.errorMsgsOn(false);
//      MsgCtrl.where(this);
//
//      // RUN
//      _qat.treeScan(new File(ROOT));
//
//      // VERIFY
//      for (File f : _files) {
//         MsgCtrl.msgln("\t " + f.getPath() + "\t\t\t" + f.length());
//         assertTrue(f.exists());
//      }
//
//      // TEARDOWN Remove test/pdc tree
//      for (File f : _files) {
//         assertTrue(f.delete());
//      }
//      // Remove the new subdirectories
//      new File(ROOT + "test/pdc/subDir").delete();
//      new File(ROOT + "test/pdc").delete();
//   }
//   
   
} // end of TestQA_Tool test class
