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
 * Verifies that all {@code .java} source files within a given "root" directory (
 * {@code "Project"/src}) has corresponding test classes in its mandatory {@code src/test} subdir.
 * The program builds a prototype test class for each missing test file using reflection to
 * auto-generate (failing) test method stubs. Finally, the program builds a unit test suite with a
 * unit test entry for each test file. Implementation details follow:
 * <P>
 * <ol>
 * <li>Builds a list of paths of sub-directories and source files contained within each subdir. Each
 * root directory must contain {@code src/pdc} and {@code src/test} subdirs. If the optional
 * {@code src/hic}, {@code src/civ} or {@code src/dmc} subdirs are missing, the program issues a
 * warning.</li>
 * <li>Traverses the {@code src} pathname list and looks for a matching test file path that matches
 * (or doesn't match) in the {@code src/test} pathname list. Each {@code subdir/Classname.java} file
 * is expected to have a {@code test/subdir/TestClassname.java} test file. Conversely, if a test
 * class exists that does not have a source file, the program issues a warning.</li>
 * <li>Creates a "prototype" test class for all missing test classes. Each method in the prototype
 * test class will contain an auto-generated (failing) test method stub.</li>
 * <li>Reviews all existing test classes to ensure that all class methods have at least one test
 * method, with the exception of the constructor and method names that are prefixed with
 * {@code get, set} or suffixed with {@code wrapper}. It is also possible to annotate the source
 * method with a {@code @No_TestGen Annotation} to indicate not to generate a test stub for that
 * method.</li>
 * <li>Creates {@code UnitTestSuite.java} with an entry for each unit test file, organized by
 * subdir, and commented as such.</li>
 * </ol>
 * <P>
 * The regression test suite {@code Adventurer/src/test/RegressionTestSuite.java} contains four line
 * entries:
 * <ol>
 * <li>{@code MyLibrary/src/mylib/test/MyLibraryTestSuite.class}</li>
 * <li>{@code ChronosLib/src/test/ChronsLib/UnitTestSuite.class}</li>
 * <li>{@code Adventurer/src/test/UnitTestSuite.class} and</li>
 * <li>{@code Adventurer/src/test/IntegTestSuite.class}</li>
 * </ol>
 * <P>
 * As part of a Github prehook, each time someone does a {@code git push}, the QA Tool is run to
 * generate all unit test suites in the regression suite, except for the Integration test suite,
 * which must be built by hand. The Github prehook also then runs the regression test suite.
 * 
 * @author alancline
 * @version Dec 30 2015 // original <br>
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

  /** The launcher for the QA Tool.
   * 
   * @param args [1] contains the source root for the source file tree; <br>
   *             [2] contains an optional file of directory and files to be excluded
   */
  static public void main(String[] args) 
  {
    if ((args.length == 0) || (args.length > 2)) {
      System.err.println("USAGE: QATool <root source path> <exclusion filename>");
      System.exit(-1);  
    }
    System.out.println("QA Tool starting scan of missing test classes: ");
    
    QATool qat = new QATool(args[0], args[1]);
    qat.treeScan(_rootFile);
    
    System.out.println("QA Tool complete: ");
    System.out.println("\t Directories scanned: " + _dirsScanned);
    System.out.println("\t Files scanned: " + _filesScanned);
    System.out.println("\t Test files written: " + _filesWritten);
    
    System.exit(0);
  }
  
  
  /**
   * Default constructor
   * 
   * @param path source directory from which to start
   * @throws InvalidArgumentException if no path is specified, or is not a directory
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
   * @param testDir subdirectory to srcDir, for test directories
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
   * @throws ClassNotFound exception if cannot find .class file in bin directory
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
//            System.err.println("\tEXISTS: \t" + testFileName);
          } else {
            // Remove the root to get the relative pathname
            String srcName = s.substring(s.indexOf("src") + 3);
//            System.err.println("\tWRITING: \t" + testFileName);
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
   * @param filePath location of exclusion file
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
     * @param f File is checked for directory and if it contains source files
     * @param name of file being verified within the directory
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
