/**
 * TestSingleFileScan.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */


package test.pdc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.MsgCtrl;
import pdc.SingleFileScan;

/**
 * @author Al Cline
 * @version July 26, 2016 // original <br>
 *          July 26, 2016 // autogen: QA Tool added missing test methods <br>
 *          August 1, 2016 // autogen: QA Tool added missing test methods <br>
 *          Nov 24, 2016 // tested flag args <br>
 *          Dec 11, 2016 // further testing of commandLine flags <br>
 *          Dec 21, 2016 // added invalid arg check for commandLine flags <br>
 *          Feb 1, 2016 // renamed from QAFileScan to emphasize single file <br>
 */
public class TestSingleFileScan
{
   // Use this file, which has passed it tests, to check the audit flags
   private final String _targetFilepath =
         "/Projects/eChronos/QATool/QA_Tool/src/pdc/TargetSrcFile.java";

   /**
    * @throws java.lang.Exception for any uncaught exception
    */
   @BeforeClass
   public static void setUpBeforeClass() throws Exception
   {}

   /**
    * @throws java.lang.Exception for any uncaught exception
    */
   @AfterClass
   public static void tearDownAfterClass() throws Exception
   {}

   /**
    * @throws java.lang.Exception for any uncaught exception
    */
   @Before
   public void setUp() throws Exception
   {}

   /**
    * @throws java.lang.Exception for any uncaught exception
    */
   @After
   public void tearDown() throws Exception
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
   }

   // ========================================================================
   // BEGIN TESTING
   // ========================================================================

   /**
    * ERROR_TEST boolean verifyArgs(String[]) invalid command line arguments
    */
   @Test
   public void testVerifyArgs_InvalidArg()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      String[] cmdArgs = {"-verbose", "-invalid"};
      String[] cmdLine = new String[3];
      cmdLine[0] = _targetFilepath;
      cmdLine[1] = cmdArgs[0];
      cmdLine[2] = cmdArgs[1];
      // Run test for invalid arg
      String errorMsg = SingleFileScan.verifyArgs(cmdLine);
      assertTrue(errorMsg.equals(SingleFileScan.ERRMSG_INVALID_ARG));
   }

   
   /**
    * ERROR_TEST boolean verifyArgs(String[]) Too many args
    */
   @Test
   public void testVerifyArgs_BadArgCount()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      String[] cmdArgs = {"-verbose", "-fileEcho", "-stubFail"};
      String[] cmdLine = new String[5];
      cmdLine[0] = _targetFilepath;
      cmdLine[1] = cmdArgs[0];
      cmdLine[2] = cmdArgs[1];
      cmdLine[3] = cmdArgs[2];
      cmdLine[4] = "Stuffing";
      // Run test too many args
      String errorMsg = SingleFileScan.verifyArgs(cmdLine);
      assertTrue(errorMsg.equals(SingleFileScan.ERRMSG_ARGNUMBER));
   }

   
   /**
    * ERROR_TEST boolean verifyArgs(String[]) Null command line <br>
    * ERROR_TEST boolean verifyArgs(String[]) Empty command line <br>
    * ERROR_TEST boolean verifyArgs(String[]) Invalid filename <br>
    * ERROR_TEST boolean verifyArgs(String[]) File doesn't exist <br>
    */
   @Test
   public void testVerifyArgs_FileErrors()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      // Run test with no command line
      String errorMsg = SingleFileScan.verifyArgs(null);
      assertTrue(errorMsg.equals(SingleFileScan.ERRMSG_NOCMDLINE));

      // Run test with no command line
      String[] args = {};
      errorMsg = SingleFileScan.verifyArgs(args);
      assertTrue(errorMsg.equals(SingleFileScan.ERRMSG_NOCMDLINE));

      // Run test with non-java file name
      String[] cmdLine = new String[1];
      cmdLine[0] = "/Projects/eChronos/QATool/QA_Tool/src/pdc/Prototype";
      errorMsg = SingleFileScan.verifyArgs(cmdLine);
      assertTrue(errorMsg.equals(SingleFileScan.ERRMSG_BADFILE));

      // Run test with java file name that cannot be found
      cmdLine = new String[1];
      cmdLine[0] = "/Projects/eChronos/QATool/QA_Tool/src/pdc/Protype.java";
      errorMsg = SingleFileScan.verifyArgs(cmdLine);
      assertTrue(errorMsg.equals(SingleFileScan.ERRMSG_FILE_NOTFOUND));
   }


   /**
    * NORMAL_TEST boolean verifyArgs(String[]) Verify that no flags will set all flags to false
    */
   @Test
   public void testVerifyArgs()
   {
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      MsgCtrl.where(this);

      String[] cmdArgs = {"-verbose", "-fileEcho", "-failStubs"};

      // Run test with no args in command line
      String[] cmdLine = new String[1];
      cmdLine[0] = _targetFilepath;
      SingleFileScan.verifyArgs(cmdLine);
      assertFalse(SingleFileScan._verbose);
      assertFalse(SingleFileScan._fileEcho);
      assertFalse(SingleFileScan._failStubs);

      // Run test with -verbose flag
      cmdLine = new String[2];
      cmdLine[0] = _targetFilepath;
      cmdLine[1] = cmdArgs[0];
      SingleFileScan.verifyArgs(cmdLine);
      assertTrue(SingleFileScan._verbose);
      assertFalse(SingleFileScan._fileEcho);
      assertFalse(SingleFileScan._failStubs);
      clearFlags();

      // Run test with -fileEcho flag
      cmdLine = new String[2];
      cmdLine[0] = _targetFilepath;
      cmdLine[1] = cmdArgs[1];
      SingleFileScan.verifyArgs(cmdLine);
      assertFalse(SingleFileScan._verbose);
      assertTrue(SingleFileScan._fileEcho);
      assertFalse(SingleFileScan._failStubs);
      clearFlags();

      // Run test with -stubFail flag
      cmdLine = new String[2];
      cmdLine[0] = _targetFilepath;
      cmdLine[1] = cmdArgs[2];
      SingleFileScan.verifyArgs(cmdLine);
      assertFalse(SingleFileScan._verbose);
      assertFalse(SingleFileScan._fileEcho);
      assertTrue(SingleFileScan._failStubs);
      clearFlags();

      // Run test with -verbose and -fileEcho flag in reverse order
      cmdLine = new String[3];
      cmdLine[0] = _targetFilepath;
      cmdLine[1] = cmdArgs[1];
      cmdLine[2] = cmdArgs[0];
      SingleFileScan.verifyArgs(cmdLine);
      assertTrue(SingleFileScan._verbose);
      assertTrue(SingleFileScan._fileEcho);
      assertFalse(SingleFileScan._failStubs);
      clearFlags();

      // Run test with all flags set in non-alphabetical order
      cmdLine = new String[4];
      cmdLine[0] = _targetFilepath;
      cmdLine[1] = cmdArgs[1];
      cmdLine[2] = cmdArgs[2];
      cmdLine[3] = cmdArgs[0];
      SingleFileScan.verifyArgs(cmdLine);
      assertTrue(SingleFileScan._verbose);
      assertTrue(SingleFileScan._fileEcho);
      assertTrue(SingleFileScan._failStubs);
      clearFlags();
   }


   // ========================================================================
   // Private Helper Methods
   // ========================================================================

   // Clear the flags set during testing
   private void clearFlags()
   {
      SingleFileScan._verbose = false;
      SingleFileScan._fileEcho = false;
      SingleFileScan._failStubs = false;
   }


} 	// end of TestQAFileScan.java class
