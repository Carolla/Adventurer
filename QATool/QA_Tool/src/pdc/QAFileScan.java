
/**
 * QAFileScan.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc;

import java.io.File;

/**
 * Searches a single source file for its methods and compares them against the source file's
 * corresponding test case and methods. It is similar to calling {@code QATool} with a single file
 * instead of a root tree and no exclusions file. {@code QAFileScan} takes the flags <br>
 * {@code -v :} turns on auditing messages <br>
 * {@code -e :} all file writes are echoed to the console <br>
 * {@code -f :} all testfile stubs will be set to fail instead of printing a safe
 * {@code Not_Implemented} message <br>
 * 
 * @author Alan Cline
 * @version Jul 21, 2016 // original <br>
 *          Aug 2 2016 // slight changes as test cases were added <br>
 *          Nov 13 2016 // better command line arg checking <br>
 *          Nov 16 2016 // modification to align with test cases <br>
 *          Nov 24 2016 // clarify proper -verbose and -fileaudit flags <br>
 *          Dec 11 2016 // refactored and tested verifyArgs <br>
 */
public class QAFileScan
{
   /** Usage message displayed with command line error */
   static private final String USAGE_MSG =
         "USAGE: QAFileScan <filepath>.java [-v] [-e [-f]";

   // Error messages for various command line errors
   static public final String ERRMSG_OK = "Command line OK";
   static public final String ERRMSG_NOCMDLINE = "Missing command line for QAFileScan";
   static public final String ERRMSG_BADFILE = "Cannot find proper .java file by that name";
   static public final String ERRMSG_ARGNUMBER = "Too many arguments in command line";
   static public final String ERRMSG_FILE_NOTFOUND = "Cannot find file specified in command line";

   // Command line args to turn on audit trail or file write trail
   static private final String VERBOSE_ARG = "-verbose";
   static private final String FILEECHO_ARG = "-fileEcho";
   static private final String STUBFAIL_ARG = "-stubFail";

   /** If true, turns on audit trail while executing */
   static public boolean _verbose = false;
   /** If true, echoes all file writes to the console */
   static public boolean _fileEcho = false;
   /** If true, write test method stubs that fail instead of printing a message */
   static public boolean _stubFail = false;

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

      // Create TestWriter and SrcReader
      TestWriter tw = new TestWriter(_srcFile);
      SrcReader sr = new SrcReader(_srcFile, tw);
      sr.fileScan(_srcFile);
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
         }
         if (args[k].equals(FILEECHO_ARG)) {
            _fileEcho = true;
         }
         if (args[k].equals(STUBFAIL_ARG)) {
            _stubFail = true;
         }
      }
      return ERRMSG_OK;
   }


}  // end of QAFileScan class
