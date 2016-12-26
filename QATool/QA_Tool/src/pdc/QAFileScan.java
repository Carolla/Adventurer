
/**
 * QAFileScan.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
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
 * corresponding test case and methods. It is similar to calling {@code QATool} with a single file
 * instead of a root tree and no exclusions file. {@code QAFileScan} takes the flags <br>
 * {@code -verbose :} turns on auditing messages <br>
 * {@code -echoFile :} all file writes are echoed to the console <br>
 * {@code -failStubs :} all testfile stubs will be set to fail instead of printing a safe
 * {@code Not_Implemented} message <br>
 * 
 * @author Alan Cline
 * @version Jul 21, 2016 // original <br>
 *          Aug 2 2016 // slight changes as test cases were added <br>
 *          Nov 13 2016 // better command line arg checking <br>
 *          Nov 16 2016 // modification to align with test cases <br>
 *          Nov 24 2016 // clarify proper -verbose and -fileaudit flags <br>
 *          Dec 11 2016 // refactored and tested verifyArgs <br>
 *          Dec 21 2016 // refactored QAScanner into QAFileScan <br>
 */
public class QAFileScan
{
   /** Usage message displayed with command line error */
   static private final String USAGE_MSG =
         "USAGE: QAFileScan <filepath>.java [-verbose] [-fileEcho] [-failStubs]";

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

   /** If true, turns on audit trail while executing */
   static public boolean _verbose = false;
   /** If true, echoes all file writes to the console */
   static public boolean _fileEcho = false;
   /** If true, write test method stubs that fail instead of printing a message */
   static public boolean _failStubs = false;

   static private TestWriter _testWriter = null;

   /** Target file from which to check source for missing test methods */
   static private File _srcFile = null;


   /**
    * Scan an individual file for missing test methods, and write a corresponding test file with the
    * omitted test methods supplied as stubs. See class description for flags available.
    * 
    * @param args list of args for the command line. First arg is the filepath of the source file to
    *           examine; remaining args are described in the {@code QAFileScan} class description
    */
   static public void main(String[] args)
   {
      // Guard: Check that valid and correct number of args are entered, and activates flags;
      // else calls System exists
      String errorMsg = verifyArgs(args);
      if (!errorMsg.equals(ERRMSG_OK)) {
         System.err.println(errorMsg);
         System.err.println(USAGE_MSG);
         System.exit(-1);
      }

      // Create a TestWriter for the SrcReader
      _testWriter = new TestWriter();
      // A single file scan does not use an exclusions file
      SrcReader sr = new SrcReader(new File(args[0]), null);
      if (!sr.isValidFile(_srcFile)) {
         QAUtils.verboseMsg("Invalid source file " + _srcFile);
         System.exit(-2);
      }
      QAUtils.verboseMsg("Scanning " + _srcFile);
      try {
         ArrayList<String> srcList = sr.fileScan(_srcFile);
         writeTestFile(_srcFile.getPath(), srcList);
      } catch (ClassNotFoundException ex) {
         QAUtils.verboseMsg("Cannot find .class file for " + _srcFile);
         System.exit(-3);
      }
   }

   /**
    * Validates and activates command line arguments, including the filepath.
    * 
    * @param args command line as presented by operating system
    * 
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

   /**
    * Get the test file path corresponding to the source file, and pass the srcList for writing to a
    * new or augmented test file
    * 
    * @param srcPath source file containing source methods
    * @param srcList all source methods in source file
    */
   static private void writeTestFile(String srcPath, ArrayList<String> srcList)
   {
      // Convert src path to test path
      String testPath = _testWriter.makeTestFilename(srcPath);
      // Convert src methods to test methods
      ArrayList<String> testList = _testWriter.convertToTestNames(srcList);
      // Write new test file (or augment existing one) with these method names
      _testWriter.writeTestFile(new File(testPath), srcList, testList);
   }



   // // ===============================================================================
   // // Mock_QAFileScan
   // // ===============================================================================
   //
   // public class Mock_QAFileScan
   // {
   // public Mock_QAFileScan()
   // {};
   //
   // /**
   // * Set the verbose flag for testing purposes
   // *
   // * @param state true or false
   // */
   // public void setVerboseFlag(boolean state)
   // {
   // QAFileScan.this._verbose = state;
   // }
   //
   // }


}  // end of QAFileScan class
