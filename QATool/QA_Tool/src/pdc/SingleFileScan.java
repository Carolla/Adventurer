
/**
 * SingleFileScan.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */

package pdc;

import java.io.File;
import java.util.ArrayList;

/**
 * Searches a single source file for its methods and compares them against the source file's
 * corresponding test case and methods. Takes the following flags <br>
 * &nbsp; &nbsp; &nbsp; {@code -verbose:} turns on auditing messages <br>
 * &nbsp; &nbsp; &nbsp;{@code -echoFile:} all file writes are echoed to the console <br>
 * &nbsp; &nbsp; &nbsp; {@code -failStubs:} all testfile stubs will be set to {@code fail} instead
 * of printing the default {@code Not_Implemented} message <br>
 * <P>
 * {@code SingleFileScan} is similar to {@code QATool} with a single file instead of a root tree;
 * and no exclusion file is available.
 * 
 * @author Alan Cline
 * @version Jul 21, 2016 // original <br>
 *          Aug 2 2016 // slight changes as test cases were added <br>
 *          Nov 13 2016 // better command line arg checking <br>
 *          Nov 16 2016 // modification to align with test cases <br>
 *          Nov 24 2016 // clarify proper -verbose and -fileaudit flags <br>
 *          Dec 11 2016 // refactored and tested verifyArgs <br>
 *          Dec 21 2016 // refactored old {@code QAScanner} into {@code SingleFileScan} <br>
 *          Feb 12 2017 // changed error msg for command line errors <br>
 */
public class SingleFileScan
{
   /** Usage message displayed with command line error */
   static private final String USAGE_MSG =
         "USAGE: QAFileScan <filepath>.java [-verbose] [-fileEcho] [-failStubs]\n"
               + "\t[-verbose]: audit trail of actions taken\n"
               + "\t[-failStubs]: test stubs will fail instead of printing message.\n"
               + "\t[-fileEcho]: copy file output to console\n";

   // Error messages for various command line errors
   static public final String ERRMSG_OK = "Command line OK";
   static public final String ERRMSG_NOCMDLINE = "Missing command line for QAFileScan";
   static public final String ERRMSG_BADFILE = "Cannot find proper .java file by that name";
   static public final String ERRMSG_ARGNUMBER = "Too many arguments in command line";
   static public final String ERRMSG_FILE_NOTFOUND = "Cannot find file specified in command line";
   static public final String ERRMSG_INVALID_ARG = "Invalid arg entered in command line";

   // Command line args to turn on audit trail or file write trail
   static public final String VERBOSE_ARG = "-verbose";
   static public final String FILEECHO_ARG = "-fileEcho";
   static public final String FAILSTUBS_ARG = "-failStubs";

   private final String COMMA = ",";
   private final String SPACE = " ";
   private final String LEFT_PAREN = "(";
   private final String RIGHT_PAREN = ")";

   /** Command line arg to turn on audit trail while executing */
   static public boolean _verbose = false;
   /** Command line arg to echo all file writes to the console */
   static public boolean _fileEcho = false;
   /** Command line arg to write test method stubs that fail instead of printing a message */
   static public boolean _failStubs = false;

   /** Main object that reads and parses the source file */
   static private SrcReader _srcRdr;
   /** Main object that writes the test file */
   static private TestWriter _testWriter;

   /** Target file from which to check source for missing test methods */
   static private File _srcFile;
   /** Associated test file */
   static private File _testFile;
   /** Associated prototype object to handle syntactical considerations, e.g., methods names */
   static private Prototype _proto;


   /**
    * Scan an individual file for missing test methods, and write a corresponding test file with the
    * omitted test methods supplied as stubs. See class description for flags available.
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
         System.err.println(USAGE_MSG);
         System.exit(-1);
      }
      // Create a prototype to handle syntactical naming considerations later
      Prototype _proto = new Prototype();

      // Create a SrcReader for file input
      _srcRdr = new SrcReader(new File(args[0]), null);
      QAUtils.verboseMsg("Scanning " + _srcFile);
      // Get all source methods
      ArrayList<String> srcList = _srcRdr.getMethodList(_srcFile, QAUtils.FileType.SOURCE);
      // Convert src method names to test method names
      ArrayList<String> srcToTestNameList = _proto.convertToTestNames(srcList);

      // Create a TestWriter for test file output
      _testWriter = new TestWriter(_verbose, _failStubs, _fileEcho);
      String testPath = null;
      try {
         testPath = _testWriter.makeTestFilename(_srcFile.getPath());
      } catch (IllegalArgumentException ex) {
         QAUtils.verboseMsg("Corresponding test file not found.");
         System.exit(-3);
      }
      // Find the corresponding test file if it exists...
      _testFile = new File(testPath);
      // If corresponding test file exists, get existing test methods
      ArrayList<String> testList = null;
      if (_testFile.exists()) {
         testList = _srcRdr.getMethodList(_testFile, QAUtils.FileType.TEST);
         // Remove existing test methods that match src methods
         ArrayList<String> augList = _proto.findAugList(srcToTestNameList, testList);
         _testWriter.augmentTestFile(_testFile, srcList, augList);
      }
      // ...else write a new test file from the source list if it doesn't
      _testWriter.writeNewTestFile(_testFile, srcList, testList);
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
      _srcFile = tmp;

      // Set the internal flags for command line args that are set
      for (int k = 1; k < args.length; k++) {
         if (args[k].equals(VERBOSE_ARG)) {
            _verbose = true;
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
   // Private Methods
   // ===============================================================================



}  // end of SingleFileScan class
