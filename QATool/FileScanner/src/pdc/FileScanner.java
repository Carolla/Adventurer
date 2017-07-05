
/**
 * FileScanner.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@wowway.com
 */

package pdc;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import mylib.MsgCtrl;

/**
 * Searches a given source file for its public and protected methods and compares them against the
 * source file's corresponding test file (class methods). A test method stub is produced for each
 * source method {@code srcMethodName} that does not have a corresponding {@code testSrcMethodName}
 * in the test file. If the test file does not exist, then the corresponding test file is created
 * with all corresponding test stubs.
 * <P>
 * Takes the following command line args: <br>
 * &nbsp; &nbsp; &nbsp; {@code ROOT:} the directory containing all source files, and the parent of
 * the test directory, which contains test files. The root must end with a '/' suffix to indicate
 * that it's a directory; the test subdir must be named {@code test}. <br>
 * &nbsp; &nbsp; &nbsp; {@code FILE:} the pathname, relative to the ROOT, of the source file to be
 * examined, <br>
 * &nbsp; &nbsp; &nbsp; {@code -verbose:} turns on auditing messages <br>
 * &nbsp; &nbsp; &nbsp;{@code -echoFile:} copies all file writes to the console <br>
 * &nbsp; &nbsp; &nbsp; {@code -failStubs:} sets all test method stubs to {@code fail} instead of
 * printing the default {@code Not_Implemented} message <br>
 * <P>
 * {@code FileScanner} assumes that the test folders mirror the source folders, and that all
 * corresponding test files start with the name {@code Test[srcname].java}. For example, the test
 * file for {@code /Project/eChronos/src/mylib/pdc/target.java} must be at location <br>
 * {@code /Project/eChronos/src/mylib/test/pdc/testTarget.java}. <br>
 * The command line arguments would then be: <br>
 * {@code FileScanner /Project/eChronos/src/mylib/ pdc/target.java -[flags]}.
 * <P>
 * {@code FileScanner} is used by {@code QATool} to traverse and scan a tree of multiple source
 * files.
 * 
 * @author Alan Cline
 * @version Mar 27, 2017 // major refactor from older version to more compact program <br>
 *          July 4, 2017 // modified to remove assumption that {@code test} dirs are directly under
 *          the {@code src} root dir. <br>
 */
public class FileScanner
{
  /** Usage message displayed with command line error */
  static private final String USAGE_MSG =
      "USAGE: FileScanner <rootDir> <filepath>.java [-verbose] [-fileEcho] [-failStubs]\n";

  /** Error messages for various command line errors */
  static private final String ERRMSG_OK = "Command line OK";
  static private final String ERRMSG_NOCMDLINE = "Missing command line for QAFileScan";
  static private final String ERRMSG_BADROOT = "Root directory not found";
  static private final String ERRMSG_BADFILE = "Cannot find proper .java file by that name";
  static private final String ERRMSG_ARGNUMBER = "Too many arguments in command line";
  static private final String ERRMSG_FILE_NOTFOUND =
      "Cannot find source file specified in command line";
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
   *        examine; remaining args are described in the class description
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

    String root = args[0];
    String srcRelPath = args[1];
    String srcPath = root + srcRelPath;
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
      testPath = testWriter.makeTestFilename(root, srcRelPath);
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
    // Check that correct number of args are entered. Command line must contains at least
    // the ROOT and target FILE, and up to three flags
    if ((args.length < 2) || (args.length > 5)) {
      return ERRMSG_ARGNUMBER;
    }
    // Check for valid Root directory
    if (!args[0].endsWith("/")) {
      return ERRMSG_BADROOT;
    }
    // Check for valid source file path
    if (!args[1].endsWith(".java")) {
      return ERRMSG_BADFILE;
    }
    // Verify root exists and is a directory
    File root = new File(args[0]);
    if (!root.isDirectory()) {
      return ERRMSG_BADROOT;
    }
    // Verify file exists
    File target = new File(args[0] + args[1]);
    if (!target.exists()) {
      return ERRMSG_FILE_NOTFOUND;
    }

    // Set defaults
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _fileEcho = false;
    _failStubs = false;

    // Set the internal flags for command line args that are set
    for (int k = 2; k < args.length; k++) {
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

  } // end of MockFileScanner class


} // end of SingleFileScan class
