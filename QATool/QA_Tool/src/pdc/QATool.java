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
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Scanner;

import mylib.Constants;

/**
 * A collection of standalone applications to improve the quality of source code testing, and help
 * ensure that tests are not forgotten. It calls {@code Prototype}, a test class creater, and
 * {@code SuiteBuilder}, which automatically creates a {@code UnitTestSuite} from all test classes.
 * <br>
 * <ul>
 * <li>{@code Prototype} verifies that a test class exists for a given source file, and creates the
 * test class if it doesn't. It populates the test file with test method stubs that correspond to
 * all {@code public} and {@code protected} source methods. Each stub method contains a {@code fail}
 * statement so it doesn't get pushed to Github without further work. {@code Prototype} can be
 * called as a standalone program by giving it a single source class.</li>
 * <li>{@code SuiteBulder} collects the names of all test classes in the test root,
 * {@code "Project"/src/test}, and builds a test suite of those classes, organized by PDC, CIV, DMC,
 * or SIC subdirs. Test classes that are directly under the {@code /src/test} subdir are grouped
 * together as a Base grouping in the suite. {@code SuiteBuilder} can be called as a standalone
 * program by giving it a single {@code /src/test} root.</li>
 * </ul>
 * <P>
 * {@code QA Tool}, given a root source directory, e.g., {@code "Project"/src}, scans the source
 * tree, calling {@code Prototype} repeatedly to build new test classes or augment the methods in
 * existing test classes. All test classes are placed in a {@code /src/test} subdir corresponding to
 * its position in the {@code src} subdir. It then calls {@code SuiteBuilder} to build (and
 * overwrite) the existing {@code /src/test/UnitTestSuite.java} file.
 * <P>
 * Within the {@code Adventurer} project, the regression test suite
 * {@code Adventurer/src/test/RegressionTestSuite.java} contains four line entries:
 * <ul>
 * <li>{@code MyLibrary/src/mylib/test/UnitTestSuite.class}</li>
 * <li>{@code ChronosLib/src/test/UnitTestSuite.class}</li>
 * <li>{@code Adventurer/src/test/UnitTestSuite.class} and</li>
 * <li>{@code Adventurer/src/test/IntegTestSuite.class}</li>
 * </ul>
 * (There should never be a need to change the Regression Test Suite.)
 * <P>
 * As part of a Github prehook, each time someone does a {@code git push}, the {@code QA Tool} is
 * run to generate all unit test suites in the regression suite: {@code MyLibrary, ChronosLib,} and
 * {@code Adventurer} (except for the Integration test suite, which must be built by hand). The
 * Github prehook then runs the regression test suite. Only when all tests pass will the
 * {@code git push} succeed.
 * 
 * 
 * @author alancline
 * @version Dec 30 2015 // original <br>
 *          Mar 23 2016 // link the component programs into a single pipeline <br>
 */
public class QATool
{
  /** Root folder for all source files */
  static private File _rootFile;
  /** File containng directories and files to excliude */
  static private File _excludeFile = null;

  ArrayList<String> _srcWithoutTests = new ArrayList<String>();
  ArrayList<String> _testsWithoutSrc = new ArrayList<String>();
  ArrayList<String> _matched = new ArrayList<String>();

  // Keyword for separating source dir from test subdir
  private final String TEST = "test";
  // Only source files searched
  private final String JAVA = ".java";
  // HIC subdir ignored
  private final String HIC = "hic";
  // Exclusions lists
  ArrayList<String> _excDirs = new ArrayList<String>();
  ArrayList<String> _excFiles = new ArrayList<String>();

  ArrayList<String> _srcPaths = new ArrayList<String>();
  ArrayList<String> _testPaths = new ArrayList<String>();

  /** Root folder for all test files */
  private File _testDir = null;
  /** Creates the actual test case class */
  private Prototype _proto;

  static private int _filesScanned;
  static private int _filesWritten;
  static private int _dirsScanned;

  // ======================================================================
  // Constructor and Helpers
  // ======================================================================

  /**
   * The launcher for the QA Tool.
   * 
   * @param args [1] contains the source root for the source file tree; <br>
   *          [2] contains an optional file of directory and files to be excluded
   */
  static public void main(String[] args)
  {
    if ((args.length == 0) || (args.length > 2)) {
      System.err.println("USAGE: QATool <root source path> <exclusion filename>");
      System.exit(-1);
    }
    System.out.println("QA Tool starting scan of missing test classes: ");

    QATool qat = null;
    try {
      qat = new QATool(args[0], args[1]);
    } catch (IllegalArgumentException ex) {
      System.err.println(ex.getMessage());
      System.err.println("USAGE: QATool <root source path> <exclusion filename>");
      System.exit(-1);

    }
//    qat.treeScan(_rootFile);
    
    System.out.println("Scanning complete: ");
    System.out.println("\t Directories scanned: " + _dirsScanned);
    System.out.println("\t Files scanned: " + _filesScanned);
    System.out.println("\t Test files written: " + _filesWritten);

    // Now run the suite builder to collect the test files created
    // Create proper arg type
    String[] testRootPath = new String[1];
    testRootPath[0]= args[0] + Constants.FS + "test";
    
    SuiteBuilder.main(testRootPath);
    
    System.exit(0);
  }


  /**
   * Default constructor
   * 
   * @param srcPath source directory from which to start
   * @param exclusionPath source directory from which to start
   * @throws IllegalArgumentException if no path is specified, or is not a directory
   */
  public QATool(String srcPath, String exclusionPath) throws IllegalArgumentException
  {
    // Guards against missing or bad arguments
    if (srcPath == null) {
      throw new IllegalArgumentException("QATool: Source directory null.");
    }
    _rootFile = new File(srcPath);
    if (!_rootFile.isDirectory()) {
      throw new IllegalArgumentException("QATool: Source directory not specified.");
    }
    if (exclusionPath == null) {
      throw new IllegalArgumentException("QATool: Exclusion file null.");
    }

    _excludeFile = new File(srcPath + Constants.FS + exclusionPath);
    if (!_excludeFile.isFile()) {
      throw new IllegalArgumentException("QATool: Exclusions file not specified.");
    }
    _rootFile = new File(srcPath);
    _proto = new Prototype();
    setExclusions(_excludeFile, _rootFile);

  }


  // ======================================================================
  // PUBLIC METHODS
  // ======================================================================

  /**
   * Traverse the root dir and return the test subdir beneath it
   * 
   * @param root the directory for all source files
   * @return the test directory
   */
  public File findTestDir(File root)
  {
    File testDir = null;
    String dirPath = root.getAbsolutePath() + Constants.FS + "test";
    File dir = new File(dirPath);
    if (dir.isDirectory()) {
      testDir = dir;
    }
    return testDir;
  }


  /**
   * Traverse the source file tree, writing missing test classes in the corresponding test tree
   * 
   * @param srcDir starting directory for source directories
   */
  public void treeScan(File srcDir)
  {
    _testDir = findTestDir(srcDir);
    writeNextTestFile(srcDir, _testDir, srcDir.getPath().length());
  }


  /**
   * Recursively traverse the root dir structure, writing test files
   * 
   * @param srcDir source directory root
   * @param testDir test directory root, subdir of srcDir
   * @param rootLen length of the original srcDir, a constant throughout recursion
   * @return a list of source paths traversed
   */
  public ArrayList<String> writeNextTestFile(File srcDir, File testDir, int rootLen)
  {
    // Retrieve all files and subdirs under dir
    File[] allFiles = srcDir.listFiles();
    for (File f : allFiles) {
      // If file is a directory, recurse down one level
      String s = f.getPath();
      // System.out.println("\tExamining " + s);
      if (f.isDirectory()) {
        _dirsScanned++;
        // Skip directories
        if ((_excDirs.contains(s) || (s.contains(TEST)))) {
          // System.err.println("\tSKIPPING ENTIRE DIRECTORY");
          continue;
        }
        writeNextTestFile(f, testDir, rootLen);
      } else {
        _filesScanned++;
        // Skip non-source files, hic subdir, and any specifically-excluded file
        if ((!s.contains(JAVA)) || (s.contains(HIC)) || (_excFiles.contains(s))) {
          // System.err.println("\tSKIPPING");
          continue;
        } else {
          // Check if test file exists for current source file
          String testFileName = _proto.makeTestFilename(s);
          File target = new File(testFileName);
          if (target.exists()) {
            // System.err.println("\tEXISTS: \t" + testFileName);
          } else {
            // Remove the root to get the relative pathname
            String srcName = s.substring(s.indexOf("src") + 3);
            // System.err.println("\tWRITING: \t" + testFileName);
            if (_proto.writeFile(target, srcName) == null) {
              System.err.println("Cannot find .class file for " + target.getPath());
            } else {
              _filesWritten++;
            }
          }
        }
      }
    }
    return _srcPaths;
  }


  // ======================================================================
  // Private helper methods
  // ======================================================================

  /**
   * Read and build the list of directory and files that should be exluded when scanning the source
   * file tree. Exclusions are saved as path names relative to source root directory
   * 
   * @param excFile contains files and directories to exclude from the search
   * @param rootFile location of the exclusion file
   */
  private void setExclusions(File excFile, File rootFile)
  {
    Scanner in = null;
    try {
      in = new Scanner(excFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    String s = in.nextLine();
    if (!s.equals("DIRECTORIES")) {
      System.err.println("\n setExclusions(): " + excFile.getName() + " has invalid format");
    }
    // Add excluded directories to list
    while (in.hasNext()) {
      s = in.nextLine().trim();
      if (s.equals("FILES") || (s.length() == 0)) {
        break;
      }
      _excDirs.add(_rootFile.getPath() + Constants.FS + s);
    }
    // Add excluded files to list
    while (in.hasNext()) {
      s = in.nextLine().trim();
      if (s.equals("FILES") || (s.length() == 0)) {
        continue;
      }
      _excFiles.add(_rootFile.getPath() + Constants.FS + s);
    }
  }


  // ======================================================================
  // INNER Class SourceFileFilter
  // ======================================================================

  /** Filter for selecting restricted files names from a list */
  class SourceFileFilter implements FilenameFilter
  {
    // Constructor
    public SourceFileFilter()
    {}

    /**
     * Only java source files name are extracted
     * 
     * @param f File is unused; required as part of interface FilenameFilter
     * @param name of file being verified
     * @return true if name matches .java file extension
     */
    public boolean accept(File f, String name)
    {
      return name.contains(".java");
    }

  } // End of SourceFileFilter inner class


  // ======================================================================
  // INNER Class DirectoryFilter
  // ======================================================================

  /** Filter for selecting restricted files names from a list */
  class DirectoryFilter implements FileFilter
  {
    // Constructor
    public DirectoryFilter()
    {}

    /**
     * Accept only directories not named TEST OR HIC that contain source files.
     * 
     * @param candidate checked for directory and if it contains source files
     * @return true if directory contains .java files and is not part of excluded set
     */
    public boolean accept(File candidate)
    {
      boolean retval = false;
      SourceFileFilter srcFilter = new SourceFileFilter();
      if (candidate.isDirectory()) {
        // Check for source files within non-excluded directories
        File[] fileList = candidate.listFiles(srcFilter);
        if (fileList.length != 0) {
          retval = true;
        }
      }
      return retval;
    }

  } // End of DirectoryFilter inner class


  // ========================================================================
  // INNER CLASS: MockTool for testing
  // ========================================================================

  /** Provide access to the QATool fields for testing */
  public class MockTool
  {
    public MockTool()
    {}

    public File getRoot()
    {
      return QATool._rootFile;
    }

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
