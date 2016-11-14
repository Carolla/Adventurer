
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
 * corresponding test case and methods. It is the similar to calling {@code QATool} with a single
 * file instead of a root tree and no exclusions file. {@code QAFileScan} still takes the {@code -v}
 * flag, but automatically sets the {@code -nofail} flag.
 * 
 * @author Alan Cline
 * @version Jul 21, 2016 // original <br>
 *          Aug 2 2016 // slight changes as test cases were added <br>
 *          Nov 13 2017 // better command line arg checking
 */
public class QAFileScan
{
   static private final String USAGE_MSG = "USAGE: QAFileScan <filepath>.java [-v]";
   static private final String BADFILE_MSG = "Cannot find proper .java file by that name";

   /** Audit trail of execution created */
   static public boolean _verbose = false;         
   /** <code>_verbose</code> turned on and copy of files writes displayed in console */
   static public boolean _debugOn = true;         

   /**
    * Scan an individual file for missing test methods, and write a corresponding test file with the
    * omitted test methods supplied as failing stubs.
    * 
    * @param args[0] contains the file path to be examined; <br>
    *           optional args[1] contains "-v" verbose flag
    */
   static public void main(String[] args)
   {
      // Guard: Check that valid and correct number of args are entered
      int errorCode = verifyArgs(args);
      boolean verbose = false;
      String srcPath = null;
      if (errorCode == 1) {
         verbose = true;
      }
      if (errorCode >= 0) {
         srcPath = args[0];
      }

      File srcFile = null;
      try {
         srcFile = new File(srcPath);
      } catch (NullPointerException ex) {
         System.err.println(BADFILE_MSG);
         System.err.println(USAGE_MSG);
         System.exit(-3);
      }

      // Create TestWriter and SrcReader
      TestWriter tw = new TestWriter(srcFile, verbose, true);
      SrcReader sr = new SrcReader(srcFile, tw, verbose);
      sr.fileScan(srcFile);
   }


   /**
    * Validate command line arguments.
    * 
    * @param args[0] existing and valid <code>.java</code> file path; <br>
    *           args[1] optional verbose "-v" flag
    * 
    * @return 0 if all args good, 1 if all args are good and the verbose flag was set; else method
    *         kicks out an <code>System.exit()<.code> with an error code
    */
   static private int verifyArgs(String[] args)
   {
      int retval = 0;
      // Check that correct number of args are entered
      if ((args.length < 1) || (args.length > 2)) {
         System.err.println(USAGE_MSG);
         System.exit(-1);
      }
      // Check for valid source file path
      if (!args[0].endsWith(".java")) {
         System.err.println(BADFILE_MSG);
         System.err.println(USAGE_MSG);
         System.exit(-2);
      }
      // Set verbose flag if present
      if ((args.length == 2) && (args[1].equals("-v"))) {
         retval++;
      }
      return retval;
   }


}  // end of QAFileScan class
