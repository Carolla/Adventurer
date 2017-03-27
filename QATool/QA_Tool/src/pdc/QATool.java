/**
 * QATool.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */

package pdc;

import java.io.File;
import java.util.ArrayList;

import mylib.Constants;

/**
 * Recursively reads through source directories and finds their missing test files. 
 * {@code QATool} takes the root of all source files to be examined, a file containing
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
   static private final String NOFAIL_FLAG = "-nofail";

   static private final String ARGS_OK = "OK";
   static private final String USAGE_MSG =
         "USAGE: QATool <source tree root path> [exclusion filename] [-v] [-nofail]";
   static private final String ERR_SRCDIR_MISSING = "Source directory null or not specified.";
   static private final String ERR_EXCFILE_MISSING = "Exclusion file specified cannot be found.";

   static private final String ROOT_MSG = "Scanning through root tree at %s";
   static private final String NO_EXCLUDE_FILE_MSG = "No exclude file given";
   static private final String EXCLUDE_FILE_MSG = "Using exclude file %s";
   static private final String VERBOSE_MSG = "Writing audit messages to console";
   static private final String NO_VERBOSE_MSG = "Audit messages OFF";
   static private final String NOFAIL_MSG = "Writing test stubs with Not-Implemented statement";
   static private final String FAIL_MSG = "Writing test stubs with fail() statement";

   static private File _root;
   static private File _excFile;
   static private boolean _verbose;
   static private boolean _nofail;


   // ======================================================================
   // Constructor and Helpers
   // ======================================================================

   /**
    * The launcher for the QA Tool.
    * 
    * @param args <br>
    *           <ol>
    *           <li>args[1] contains the source root for the source file tree;</li>
    *           <li>args[2] contains an optional {@code .txt} file containing directory and file
    *           names to be excluded. The exclusions file must be immediately under the source root
    *           in the file system.</li>
    *           <li>args[3] is an optional flag "-v" turns on verbose mode, which sends audit
    *           messages to the console.</li>
    *           <li>args[4] is an optional flag "-nofail" to turn on an "Not Implemetned" message in
    *           the test method stub, intead of writing failing test stubs</li>
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
      String msg = String.format(ROOT_MSG, _root.getAbsolutePath());
      System.out.println(msg);

      msg = (_excFile == null) ? NO_EXCLUDE_FILE_MSG : EXCLUDE_FILE_MSG;
      msg = String.format(EXCLUDE_FILE_MSG, _excFile.getAbsolutePath());
      System.out.println(msg);

      msg = (_verbose) ? VERBOSE_MSG : NO_VERBOSE_MSG;
      System.out.println(msg);
      msg = (_nofail) ? NOFAIL_MSG : FAIL_MSG;
      System.out.println(msg);
      System.out.println("\n\n");

      // Create the scanner and begin scanning through source files
//      QAScanner qas = new QAScanner(_root, _excFile, _verbose, _nofail);
//      qas.treeScan();

      // Produce an report of files found and written
//      qas.qaReport();
//      System.exit(0);
   }


   // ======================================================================
   // STATIC PRIVATE METHODS
   // ======================================================================

   /**
    * Verify that the input args are correct and reference real data.
    * 
    * @param args passed from the command line: srcPath, [the exclusion filenane], [-v], [-nofail]
    * @return ARG_OK or ERRMSG on failure
    */
   static private String verifyArgs(String[] args)
   {
      String retmsg = ARGS_OK;

      // One arg is required; the other three are optional
      if ((args.length < 1) || (args.length > 4)) {
         return USAGE_MSG;
      }
      // Check for legit directory
      String srcPath = args[0];
      File root = new File(srcPath);
      if ((root == null) || (!root.isDirectory())) {
         retmsg = ERR_SRCDIR_MISSING;
      } else {
         _root = root;
      }
      // Check for exclusions text file
      for (String s : args) {
         if (s.endsWith(".txt")) {
            File excFile = new File(srcPath + Constants.FS + s);
            if (!excFile.isFile()) {
               retmsg = ERR_EXCFILE_MISSING;
            } else {
               _excFile = excFile;
            }
         }
      }
      // Check for flags
      for (String s : args) {
         if (s.equals(VERBOSE_FLAG)) {
            _verbose = true;
            continue;
         }
         if (s.equals(NOFAIL_FLAG)) {
            _nofail = true;
            continue;
         }
      }
      return retmsg;
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
