
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
 *          Nov 13 2016 // better command line arg checking <br>
 *          Nov 16 2016 Nov // modifiecation to align with test cases <br>
 */
public class QAFileScan
{
   static private final String USAGE_MSG = "USAGE: QAFileScan <filepath>.java [-v]";
   static private final String BADFILE_MSG = "Cannot find proper .java file by that name";

   // Command line args to turn on audit trail or file write trail
   static private final String VERBOSE_ARG = "-verbose";
   static private final String DEBUG_ARG = "-debug";

   /** Audit trail of execution created */
   static public boolean _verbose = false;
   /** Echoes the file writes to the console */
   static public boolean _debug = false;

   /**
    * Scan an individual file for missing test methods, and write a corresponding test file with the
    * omitted test methods supplied as failing stubs.
    * 
    * @param args[0] contains the file path to be examined; <br>
    *           optional args contains "-verbose" or "-debug" in any order. The flag "-verbose"
    *           turns on auditing messages for execution. The flag "-debug" turns on "-verbose" and
    *           echoes all files writes too.
    */
   static public void main(String[] args)
   {
      // Guard: Check that valid and correct number of args are entered, and activates flags;
      // else calls System exists
      verifyArgs(args);

      String srcPath = args[0];
      File srcFile = null;
      try {
         srcFile = new File(srcPath);
      } catch (NullPointerException ex) {
         System.err.println(BADFILE_MSG);
         System.err.println(USAGE_MSG);
         System.exit(-3);
      }

      // Create TestWriter and SrcReader
      TestWriter tw = new TestWriter(srcFile, _verbose, true);
      SrcReader sr = new SrcReader(srcFile, tw, _verbose);
      sr.fileScan(srcFile);
   }


   /**
    * Validate and activate command line arguments.
    * 
    * @param args[0] existing and valid <code>.java</code> file path; -verbose and -debug flags are
    *           set for corresponding args
    * 
    * @return true if all args are valid; else method kicks out an <code>System.exit()<.code> with
    *         an error code
    */
   static private void verifyArgs(String[] args)
   {
      // Check that correct number of args are entered
      if ((args.length < 1) || (args.length > 3)) {
         System.err.println(USAGE_MSG);
         System.exit(-1);
      }
      // Check for valid source file path
      if (!args[0].endsWith(".java")) {
         System.err.println(BADFILE_MSG);
         System.err.println(USAGE_MSG);
         System.exit(-2);
      }
      // Set the internal flags for command line args that are set
      for (int k = 1; k < args.length; k++) {
         if (args[k].equals(VERBOSE_ARG)) {
            _verbose = true;
         }
         if (args[k].equals(DEBUG_ARG)) {
            _debug = true;
         }
      }
   }


}  // end of QAFileScan class
