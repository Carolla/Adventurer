/**
 * TestQAUtils.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */


package test.pdc;

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

import mylib.MsgCtrl;
import pdc.QAUtils;

/**
 * @author Al Cline
 * @version December 13, 2016 // original <br>
 *          December 20, 2016    // autogen: QA Tool added missing test methods <br>
 */
public class TestQAUtils
{
   // Target src file
   static private final String SRC_PATHNAME =
         "/Projects/eChronos/QATool/QA_Tool/src/pdc/TargetSrcFile.java";
   // Target test file
   static private final String TEST_PATHNAME =
         "/Projects/eChronos/QATool/QA_Tool/src/test/pdc/TestTargetSrcFile.java";


   /**
    * @throws java.lang.Exception
    */
   @BeforeClass
   public static void setUpBeforeClass() throws Exception
   {
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
    * @NORMAL_TEST static convertFileToClass(String, QAUtils$FileType)
    */
   @Test
   public void testConvertSrcFileToClass()
   {
      MsgCtrl.auditMsgsOn(true);
      MsgCtrl.errorMsgsOn(true);
      MsgCtrl.where(this);

      // SETUP for Source path
      // Expected class path
      String expPath = "pdc.TargetSrcFile";
      // RUN a source path to find a corresponding class file
      Class<?> clazz = null;
      try {
         clazz = QAUtils.convertFileToClass(SRC_PATHNAME, QAUtils.FileType.SOURCE);
      } catch (ClassNotFoundException ex) {
         MsgCtrl.errMsgln("\t not expecting unfound class");
      }
      
      // VERIFY
      assertNotNull(clazz);
      String clzName = clazz.getName();
      MsgCtrl.msgln("\t .class file created: " + clzName);
      assertTrue(clzName.equals(expPath));
   }

   
   /**
    * @NORMAL_TEST static convertFileToClass(String, QAUtils$FileType)
    */
   @Test
   public void testConvertTestFileToClass()
   {
      MsgCtrl.auditMsgsOn(true);
      MsgCtrl.errorMsgsOn(true);
      MsgCtrl.where(this);
      
      // SETUP for Test path
      // Expected class path
      String expPath = "test.pdc.TestTargetSrcFile";
      // RUN a source path to find a corresponding class file
      Class<?> clazz = null;
      try {
         clazz = QAUtils.convertFileToClass(TEST_PATHNAME, QAUtils.FileType.TEST);
      } catch (ClassNotFoundException ex) {
         MsgCtrl.errMsgln("\t Not expecting unfound class");
      }
      
      // VERIFY
      assertNotNull(clazz);
      String clzName = clazz.getName();
      MsgCtrl.msgln("\t .class file created: " + clzName);
      assertTrue(clzName.equals(expPath));
   }
   

   /**
    * @NORMAL_TEST static collectMethods(String, QAUtils$FileType)
    */
   @Test
   public void testCollectMethodsAsSrc()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // Expected methods
      String[] expList = {"File alpha(String)", "String beta(File)"};

      // Extract methods from a source file
      ArrayList<String> srcList = QAUtils.collectMethods(SRC_PATHNAME, QAUtils.FileType.SOURCE);
      displayList("Source methods found", srcList);
      assertTrue(expList[0].equals(srcList.get(0)));
      assertTrue(expList[1].equals(srcList.get(1)));
   }


   /**
    * @NORMAL_TEST static collectMethods(String, QAUtils$FileType)
    */
//   @Test
   public void testCollectMethodsAsTest()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // Expected methods
      String[] expList = {"void testAlpha()", "void testBeta()"};

      // Extract methods from a test file
      ArrayList<String> mList = QAUtils.collectMethods(TEST_PATHNAME, QAUtils.FileType.TEST);
      displayList("Test methods found", mList);
      assertTrue(expList[0].equals(mList.get(0)));
      assertTrue(expList[1].equals(mList.get(1)));

   }

   /**
    * @ERROR_TEST static collectMethods(String, QAUtils$FileType)
    */
   @Test
   public void testCollectFileToClass_NoClassFileFound()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // SETUP Delete existing src file
      String clazzPath = "/Projects/eChronos/QATool/QA_Tool/bin/pdc/TargetSrcFile.class";
      File tmp = new File(clazzPath);
      if (tmp.exists()) {
         tmp.delete();
      }
      assertFalse(tmp.exists());

      // Create a Class file from a given pathname
      Class<?> newClass = null;
      try {
         newClass = QAUtils.convertFileToClass(SRC_PATHNAME, QAUtils.FileType.SOURCE);
      } catch (ClassNotFoundException ex) {
         MsgCtrl.errMsgln("\t Unexpected exception; expected recovery ");
      }

      // VERIFY New class file created
      assertNotNull(newClass);
      
   }


   /**
    * @NORMAL_TEST static extractSignature(Method, String)
    */
   @Test
   public void testExtractSignature()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
   }


   /**
    * @NORMAL_TEST static isPrepMethod(String)
    */
   @Test
   public void testIsPrepMethod()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
   }


   /**
    * @NORMAL_TEST static outList(String, List)
    */
   @Test
   public void testOutList()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
   }


   /**
    * @NORMAL_TEST static simplifyDeclaration(String)
    */
   @Test
   public void testSimplifyDeclaration()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
   }


   /**
    * @NORMAL_TEST static simplifyReturnType(String)
    */
   @Test
   public void testSimplifyReturnType()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
   }


   /**
    * @NORMAL_TEST static sortSignatures(ArrayList)
    */
   @Test
   public void testSortSignatures()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
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

      MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
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


	/**
 	 * @NORMAL_TEST static collectMethods(String, QAUtils$FileType)
	 */
	@Test
	public void testCollectMethods()
	{
		MsgCtrl.auditMsgsOn(true);
		MsgCtrl.errorMsgsOn(true);
		MsgCtrl.where(this);

		MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
	}


	/**
 	 * @NORMAL_TEST static convertFileToClass(String, QAUtils$FileType)
	 */
	@Test
	public void testConvertFileToClass()
	{
		MsgCtrl.auditMsgsOn(true);
		MsgCtrl.errorMsgsOn(true);
		MsgCtrl.where(this);

		MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
	}


} 	// end of TestQAUtils.java class
