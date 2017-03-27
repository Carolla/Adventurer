/**
 * TestQAUtils.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */


package test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.MsgCtrl;
import pdc.QAUtils;

/**
 * @author Al Cline
 * @version December 13, 2016 // original <br>
 *          December 20, 2016 // autogen: QA Tool added missing test methods <br>
 *          December 26, 2016 // Revised to allow unit test suite to pass <br>
 *          Jan 30, 2016 // {@code collectMethods()} returns exception sooners <br>
 */
public class TestQAUtils
{
   // Target src file
   static private final String SRC_PATHNAME =
         "/Projects/eChronos/QATool/QA_Tool/src/pdc/TargetSrcFile.java";
   // Target test file
   static private final String TEST_PATHNAME =
         "/Projects/eChronos/QATool/QA_Tool/src/test/pdc/TestTargetSrcFile.java";
   // Target test class file
   static private final String TEST_CLASSPATH =
         "/Projects/eChronos/QATool/QA_Tool/bin/test/pdc/TestTargetSrcFile.class";


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
   {}

   /**
    * @throws java.lang.Exception
    */
   @After
   public void tearDown() throws Exception
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
   }


   // ===============================================================================
   // BEGIN TESTING
   // ===============================================================================

   /**
    * @NORMAL_TEST static collectMethods(String, QAUtils$FileType)
    */
   @Test
   public void testCollectMethods()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      MsgCtrl.errMsgln("\t\t Test method implemented elsewhere in variation.");
   }


   /**
    * @NORMAL_TEST static collectMethods(String, QAUtils$FileType)
    */
   @Test
   public void testCollectSrcMethods()
   {
      MsgCtrl.auditMsgsOn(true);
      MsgCtrl.errorMsgsOn(true);
      MsgCtrl.where(this);

      // Expected methods
      String[] expList = {"File alpha(String)", "String beta(File)"};

      // Extract methods from a source file
      ArrayList<String> srcList = null;
      try {
         srcList = QAUtils.collectSrcMethods(SRC_PATHNAME);
      } catch (ClassNotFoundException ex) {
         fail("\tUnexpected exception thrown");
      }
      displayList("Source methods found", srcList);
      assertTrue(expList[0].equals(srcList.get(0)));
      assertTrue(expList[1].equals(srcList.get(1)));
   }


//   /**
//    * @NORMAL_TEST static collectMethods(String, QAUtils$FileType)
//    */
//   @Test
//   public void testCollectTestMethods()
//   {
//      MsgCtrl.auditMsgsOn(true);
//      MsgCtrl.errorMsgsOn(true);
//      MsgCtrl.where(this);
//
//      // SETUP
//      // Expected methods
//      String[] srcAry = {"File alpha(String x)", "String beta(File f)"};
//      String[] expAry = {"void testAlpha()", "void testBeta()"};
//
//      // Create a new test file from which to extract methods
//      Prototype proto = new Prototype();
//      File srcFile = new File(TEST_PATHNAME);
//      File target = writeNewTestFile(srcFile, QAUtils.createList(srcAry),
//            QAUtils.createList(expAry));
//      // Both source and test .class file should now exist.
//      assertTrue(target.exists());
//      File clazz = new File(TEST_CLASSPATH);
//      assertTrue(clazz.exists());
//
//      // RUN Extract methods from the test .class file
//      ArrayList<String> mList = null;
//      try {
//         mList = QAUtils.collectTestMethods(TEST_PATHNAME);
//      } catch (IllegalArgumentException ex1) {
//         fail("\tUnexpected exception thrown: " + ex1.getMessage());
//      } catch (ClassNotFoundException ex2) {
//         fail("\tUnexpected exception thrown:" + ex2.getMessage());
//      }
//      displayList("Test methods found", mList);
//      assertTrue(expAry[0].equals(mList.get(0)));
//      assertTrue(expAry[1].equals(mList.get(1)));
//
//   }


   /**
    * @NORMAL_TEST static outList(String, List)
    */
   @Test
   public void testOutList()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // SETUP
      // Methods names to convert to outList
      String[] inMethods =
            {"void zeta()", "File alpha()", "String beta(File x)", "void gamma(int y)",
                  "ArrayList<String> delta(String msg, ArrayList<String> alist)"};
      String[] expMethods =
            {"zeta() -> void", "alpha() -> File", "beta(File x) -> String", "gamma(int y) -> void",
                  "delta(String msg, ArrayList<String> alist) -> ArrayList<String>"};
      // Push the string elements into a list to use as parm
      ArrayList<String> inList = QAUtils.createList(inMethods);
      ArrayList<String> expList = QAUtils.createList(expMethods);

      // RUN
      ArrayList<String> returnList = new ArrayList<String>(inList.size());
      returnList = (ArrayList<String>) QAUtils.outList("\tTesting QAUtils.outList()", inList);

      // VERIFY
      for (int k = 0; k < expList.size(); k++) {
         String retSig = returnList.get(k);
         String inSig = inList.get(k);
         MsgCtrl.msgln("\t" + inSig + ": \t" + retSig);
         assertTrue(retSig.equals(expList.get(k)));
      }
   }


   /**
    * @ERROR_TEST static outList(String, List)
    */
   @Test
   public void testOutList_EmptyList()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // RUN
      ArrayList<String> inList = new ArrayList<String>();
      ArrayList<String> returnList = new ArrayList<String>();
      returnList = (ArrayList<String>) QAUtils
            .outList("\tTesting QAUtils.outList() with empty inList", inList);

      // VERIFY
      assertEquals(0, returnList.size());
   }


   /**
    * @NORMAL_TEST static verboseMsg(String)
    */
   @Test
   public void testVerboseMsg()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      MsgCtrl.msgln("\t Too trivial to test");
   }


   // ===============================================================================
   // Private Helper Methods
   // ===============================================================================

   /**
    * Display the elements of an List<String>
    * 
    * @param msg a caption message above the list
    * @param aList the collection to be printed
    */
   private void displayList(String msg, ArrayList<String> aList)
   {
      MsgCtrl.msgln(msg);
      for (String s : aList) {
         MsgCtrl.msgln("\t " + s);
      }
   }


} 	// end of TestQAUtils.java class
