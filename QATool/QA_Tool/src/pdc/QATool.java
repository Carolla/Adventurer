/**
 * QATool.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc;

import java.io.File;
import java.util.ArrayList;

import mylib.Constants;

/**
 * A utility tool to improve the quality of source code testing by ensuring that tests are not
 * forgotten. {@code QATool} takes the root of all source files to be examined, a file containing
 * directories and folders to exclude from scanning, and an optional verbose flag for audit
 * messages. See the {@code main()} method for details.
 * <p>
 * {@code QATool} recursively calls {@code SrcReader}, which for each source file under the given
 * root, reads and extracts the source file's methods. {@code SrcReader} calls {@code TestWriter} to
 * identify missing test methods and add them to the corresponding test file. A new test file is
 * created if there is no test file corresponding to a source file. {@code QATool} lastly calls
 * {@code SuiteBuilder}, which automatically creates a {@code UnitTestSuite} from all test classes.
 * <P>
 * {@code TestWriter} populates a test file with test method stubs that correspond to all
 * {@code public} and {@code protected} source methods. All test classes are placed in a
 * {@code /src/test} subdir corresponding to the source file's position in the {@code src} subdir.
 * Each stub method contains a {@code fail} statement so it doesn't get pushed to Github without
 * further work. It then calls {@code SuiteBuilder} to build (and overwrite) the existing
 * {@code /src/test/UnitTestSuite.java} file.
 * <P>
 * {@code SuiteBulder} collects the names of all test classes in the test root, i.e.,
 * {@code "Project"/src/test}, and builds a test suite of those classes, organized by PDC, CIV, DMC,
 * or SIC subdirs. Test classes that are directly under the {@code /src/test} subdir are grouped
 * together in the suite. {@code SuiteBuilder} can be called as a standalone program by giving it a
 * single {@code /src/test} root.
 * <P>
 * Within the {@code Adventurer} project, the regression test suite
 * {@code Adventurer/src/test/RegressionTestSuite.java} contains four line entries:
 * <ul>
 * <li>{@code MyLibrary/src/mylib/test/UnitTestSuite.class}</li>
 * <li>{@code ChronosLib/src/test/UnitTestSuite.class}</li>
 * <li>{@code Adventurer/src/test/UnitTestSuite.class} and</li>
 * <li>{@code Adventurer/src/test/IntegTestSuite.class}</li>
 * </ul>
 * <P>
 * There should never be a need to change the Regression Test Suite. Any test files created or
 * changed are added or deleted from their particular {@code UnitTestSuite} file contained within
 * {@code RegressionTestSuite}.
 * <P>
 * As part of a Github prehook, each time someone requests a {@code git push}, the {@code QA Tool}
 * is run to generate all unit test suites in the regression suite: {@code MyLibrary, ChronosLib,}
 * and {@code Adventurer} (except for the Integration test suite, which must be built by hand). The
 * Github prehook then runs the regression test suite. Only when all tests pass will the
 * {@code git push} succeed.
 * 
 * 
 * @author Al Cline
 * @version Dec 30 2015 // original <br>
 *          Mar 23 2016 // link the component programs into a single pipeline <br>
 *          Jul 18 2016 // semi-final version (excludes {@code SuiteBuilder}) <br>
 */
public class QATool
{
   private ArrayList<String> _srcWithoutTests = new ArrayList<String>();
   private ArrayList<String> _testsWithoutSrc = new ArrayList<String>();
   private ArrayList<String> _matched = new ArrayList<String>();

   private ArrayList<String> _srcPaths = new ArrayList<String>();
   private ArrayList<String> _testPaths = new ArrayList<String>();

   static private final String VERBOSE_FLAG = "-v";

   static private final String ARGS_OK = "OK";
   static private final String USAGE_MSG =
         "USAGE: QATool <source tree root path> <exclusion filename> [-v]erbose";
   static private final String ERR_SRCDIR_MISSING = "Source directory null or not specified.";
   static private final String ERR_EXCFILE_MISSING = "Exclusion file specified cannot be found.";
   static private final String ERR_VERBOSE_INVALID = "Extra argument or verbose flag incorrect";


   // ======================================================================
   // Constructor and Helpers
   // ======================================================================

   /**
    * The launcher for the QA Tool.
    * 
    * @param args <br>
    *           <ol>
    *           <li>args[1] contains the source root for the source file tree;</li>
    *           <li>args[2] contains an optional file (default filename {@code ScanExclusions.txt})
    *           containing directory and file names to be excluded. The exclusions file must be
    *           immediately under the source root.</li>
    *           <li>args[3] is the optional flag "-v" to turn on verbose mode, which sends audit
    *           messages to the console.</li>
    *           </ol>
    */
   static public void main(String[] args)
   {
      // Validate agruments and set flag values
      String argMsg = verifyArgs(args);
      if (!argMsg.equals(ARGS_OK)) {
         System.err.println("QATool: " + argMsg);
         System.exit(-1);
      }
      // Create the scanner and begin scanning through source files
      QAScanner qas = new QAScanner(args);
      qas.treeScan();

      // Produce an report of files found and written
      qas.qaReport();

      System.exit(0);
   }


   // ======================================================================
   // STATIC PRIVATE METHODS
   // ======================================================================

   /**
    * Verify that the input args are correct and reference real data.
    * 
    * @param args passed from the command line: srcPath, the exclusion filenane, optional verbose
    *           flag "-v"
    * @return ARG_OK or ERRMSG on failure
    */
   static private String verifyArgs(String[] args)
   {
      String retval = ARGS_OK;
      File someFile = null;

      // Two args are required; verbose flag is optional
      if ((args.length < 2) || (args.length > 3)) {
         retval = USAGE_MSG;
      }
      String srcPath = args[0];
      String excFile = args[1];

      // Guards against missing or bad arguments
      someFile = new File(srcPath);
      if ((srcPath == null) || (!someFile.isDirectory())) {
         retval = ERR_SRCDIR_MISSING;
      }
      // Check that exclusion file actually exists
      someFile = new File(srcPath + Constants.FS + excFile);
      if ((excFile == null) || (!someFile.isFile())) {
         retval = ERR_EXCFILE_MISSING;
      }
      // Check for verbose flag arg
      if (args.length == 3) {
         if (!args[2].equalsIgnoreCase(VERBOSE_FLAG)) {
            retval = ERR_VERBOSE_INVALID;
         }
      }
      return retval;
   }


   // ========================================================================
   // INNER CLASS: MockTool for testing
   // ========================================================================

   /** Provide access to the QATool fields for testing */
   public class MockTool
   {
      public MockTool()
      {}

      public ArrayList<String> getMatched()
      {
         return QATool.this._matched;
      }

      public ArrayList<String> getSrcPaths()
      {
         return QATool.this._srcPaths;
      }

      public ArrayList<String> getSrcWithoutTests()
      {
         return QATool.this._srcWithoutTests;
      }


      public ArrayList<String> getTestPaths()
      {
         return QATool.this._testPaths;
      }

      public ArrayList<String> getTestsWithoutSrc()
      {
         return QATool.this._testsWithoutSrc;
      }

   } // end of MockTool inner class


} // end of QATool class
