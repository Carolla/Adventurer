
/**
 * FileScanner.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */

package pdc;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import mylib.MsgCtrl;

/**
 * Searches a single source file for its methods and compares them against the source file's
 * corresponding test case and methods. Takes the following flags <br>
 * &nbsp; &nbsp; &nbsp; {@code -verbose:} turns on auditing messages <br>
 * &nbsp; &nbsp; &nbsp;{@code -echoFile:} all file writes are echoed to the console <br>
 * &nbsp; &nbsp; &nbsp; {@code -failStubs:} all testfile stubs will be set to {@code fail} instead
 * of printing the default {@code Not_Implemented} message <br>
 * <P>
 * {@code FileScanner} can be used by {@code QATool} to traverse and scan a tree of source files.
 * 
 * @author Alan Cline
 * @version Mar 27, 2017 // major refactor from older version to more compact program <br>
 */
public class FileScanner
{
   /** Usage message displayed with command line error */
   static private final String USAGE_MSG =
         "USAGE: FileScanner <filepath>.java [-verbose] [-fileEcho] [-failStubs]\n"
               + "\t[-verbose]: audit trail of actions taken\n"
               + "\t[-failStubs]: test stubs will fail instead of printing message.\n"
               + "\t[-fileEcho]: copy file output to console\n";

   /** Error messages for various command line errors */
   static private final String ERRMSG_OK = "Command line OK";
   static private final String ERRMSG_NOCMDLINE = "Missing command line for QAFileScan";
   static private final String ERRMSG_BADFILE = "Cannot find proper .java file by that name";
   static private final String ERRMSG_ARGNUMBER = "Too many arguments in command line";
   static private final String ERRMSG_FILE_NOTFOUND = "Cannot find file specified in command line";
   static private final String ERRMSG_INVALID_ARG = "Invalid arg entered in command line";

   /** Command line args to turn on audit trail or file write trail */
   static private final String VERBOSE_ARG = "-verbose";
   static private final String FILEECHO_ARG = "-fileEcho";
   static private final String FAILSTUBS_ARG = "-failStubs";

   /** Command line arg to echo all file writes to the console */
   static private boolean _fileEcho = false;
   /** Command line arg to write test method stubs that fail instead of printing a message */
   static private boolean _failStubs = false;


   // ===============================================================================
   // Launcher
   // ===============================================================================

   /**
    * Creates a SrcReader to scan a source file and its corresponding test file for missing test
    * methods, and write a corresponding test file with the omitted test methods supplied as stubs.
    * 
    * @param args list of args for the command line. First arg is the filepath of the source file to
    *           examine; remaining args are described in the class description
    */
   static public void main(String[] args)
   {
      // Guard: Checks that valid and correct number of args are entered, and activates flags;
      // else calls System exits
      String errorMsg = verifyArgs(args);
      if (!errorMsg.equals(ERRMSG_OK)) {
         System.err.println(errorMsg);
         System.out.println(USAGE_MSG);
         System.exit(-1);
      }

      String srcPath = args[0];
      MsgCtrl.auditMsg("Scanning " + srcPath);
      // Get all source methods
      ArrayList<String> srcList = QAUtils.getMethods(srcPath);
      // Create a triple map to maintain lists for src names, src-to-test names, and test names
      TripleMap tMap = new TripleMap(srcList);
      MsgCtrl.auditPrintList("Source list: ", tMap.export(TripleMap.NameType.SRC));
      MsgCtrl.auditPrintList("Converted test names from source: ",
            tMap.export(TripleMap.NameType.SRC_TO_TEST));

      // Create a TestWriter for test file output
      TestWriter testWriter = new TestWriter(_failStubs, _fileEcho);
      String testPath = null;
      try {
         testPath = testWriter.makeTestFilename(srcPath);
      } catch (IllegalArgumentException ex) {
         MsgCtrl.auditErrorMsg("Corresponding test file not found.");
         System.exit(-1);
      }
      // Find the corresponding test file if it exists...
      File testFile = new File(testPath);
      // If corresponding test file exists, get existing test methods
      ArrayList<String> testList = null;
      MsgCtrl.errMsgln("Checking: \t" + testFile.getPath());
      if (testFile.exists()) {
         MsgCtrl.errMsgln("\t\t FOUND IT");
         testList = QAUtils.getMethods(testPath);
         tMap.setMapList(TripleMap.NameType.TEST, testList);
         // Find only test names that don't already exist in the test file
         Map<String, String> augMap = tMap.buildAugMap();
         MsgCtrl.auditPrintList("Existing test method names: ",
               tMap.export(TripleMap.NameType.TEST));
         if (augMap.size() == 0) {
            MsgCtrl.auditMsg("\nNo new methods to add to test file.");
         } else {
            MsgCtrl.auditPrintMap("Test methods to add to existing test file: ", augMap);
            // Write the new test method stubs to the existing test file
            testWriter.augmentTestFile(testFile, augMap);
         }
      } else {
        MsgCtrl.errMsgln("  NOT FOUND. Creating new test file");
         // Write new file using the converted method names
         Map<String, String> augMap = tMap.buildAugMap();
         testWriter.writeNewTestFile(testFile, augMap);
      }
   }


   /**
    * Validates and activates command line arguments, including the filepath.
    * 
    * @param args command line as presented by operating system
    * @return OK message if command Line is valid, else error message related to kind of error
    */
   static public String verifyArgs(String[] args)
   {
      if ((args == null) || (args.length == 0)) {
         return ERRMSG_NOCMDLINE;
      }
      // Check that correct number of args are entered
      if ((args.length < 1) || (args.length > 4)) {
         return ERRMSG_ARGNUMBER;
      }
      // Check for valid source file path
      if (!args[0].endsWith(".java")) {
         return ERRMSG_BADFILE;
      }
      // Verify file exists
      File tmp = new File(args[0]);
      if (!tmp.exists()) {
         return ERRMSG_FILE_NOTFOUND;
      }

      // Set defaults
      MsgCtrl.auditMsgsOn(false);
      MsgCtrl.errorMsgsOn(false);
      _fileEcho = false;
      _failStubs = false;

      // Set the internal flags for command line args that are set
      for (int k = 1; k < args.length; k++) {
         if (args[k].equals(VERBOSE_ARG)) {
            MsgCtrl.auditMsgsOn(true);
            MsgCtrl.errorMsgsOn(true);
         } else if (args[k].equals(FILEECHO_ARG)) {
            _fileEcho = true;
         } else if (args[k].equals(FAILSTUBS_ARG)) {
            _failStubs = true;
         } else {
            return ERRMSG_INVALID_ARG;
         }
      }
      return ERRMSG_OK;
   }


   // ===============================================================================
   // Inner Class for testing
   // ===============================================================================

   /**
    * Mock innner class to get FileScanner state
    */
   public class MockFileScanner
   {
      public MockFileScanner()
      {};

      /** Is the program's audit flag on? */
      public boolean isVerbose()
      {
         return MsgCtrl.isAuditOn();
      }

      /** Is the program's flag to echo file writes? */
      public boolean isFileEcho()
      {
         return _fileEcho;
      }

      /** Is the program's flag on to create test stubs that fail, instead of print messages? */
      public boolean isFailStubs()
      {
         return _failStubs;
      }

   }      // end of MockFileScanner class


}  // end of SingleFileScan class
