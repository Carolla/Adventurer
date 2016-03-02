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
  private final String ROOT = System.getProperty("user.dir") + "/src/";
  private final String EXCLUDE_FILE = ROOT + "ScanExclusions.txt";

  ArrayList<String> _srcWithoutTests = new ArrayList<String>();
  ArrayList<String> _testsWithoutSrc = new ArrayList<String>();
  ArrayList<String> _matched = new ArrayList<String>();

  // delimeter to separate files from directories
  private final String FILE_DELIM = Constants.FS;
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

  /** Root folder for all source files */
  private File _root;
  /** Root folder for all test files */
  private File _testDir = null;

  private Prototype _proto;

  // ======================================================================
  // Constructor and Helpers
  // ======================================================================

  /**
   * Default constructor
   * 
   * @param path source directory from which to start
   * @throws InvalidArgumentException if no path is specified, or is not a directory
   */
  public QATool(String path) throws IllegalArgumentException
  {
    if (path == null) {
      throw new IllegalArgumentException("QATool: No source directory specified");
    }
    _root = new File(path);
    if (!_root.isDirectory()) {
      _root = null;
      throw new IllegalArgumentException("QATool: Path argument must be a directory");
    }
    _proto = new Prototype();

    setExclusions(EXCLUDE_FILE, ROOT);
  }


  // ======================================================================
  // PUBLIC METHODS
  // ======================================================================

  // /**
  // * Traverse the root dir structure, building paths for all directories that have source files,
  // and
  // * excluding the mandatory 'test' subdir. Results are placed into the {@code _srcPaths} field.
  // *
  // * @param root starting source directory
  // * @param offset the length of the root pathname; required so recursion doesn't change the root
  // * length
  // */
  // public ArrayList<String> buildSourceList(File root, int offset)
  // {
  // // Retrieve all files and subdirs under dir
  // File[] allFiles = root.listFiles();
  // for (File f : allFiles) {
  // // If file is a non-excluded directory, recurse down one level
  // if (f.isDirectory()) {
  // if (isExcludedDir(f)) {
  // continue;
  // } else {
  // buildSourceList(f, offset);
  // }
  // }
  // // If file is a non-excluded file, add the file path
  // else if (f.isFile()) {
  // // Skip non-source files, and hic and test subdirs
  // if (isExcludedFile(f)) {
  // continue;
  // } else {
  // // Remove root path before adding file path
  // String s = f.getPath();
  // _srcPaths.add(s.substring(offset + 1));
  // // System.out.println("\tAdding to source list:\t " + s);
  // }
  // }
  // }
  // return _srcPaths;
  // }
  //
  //
  // /**
  // * Traverse the root dir structure to the test subdir, building paths for all test files Results
  // * are placed into the {@code _testPaths} field.
  // *
  // * @param root starting source directory
  // * @param offset the length of the test subdir pathname; required so recursion doesn't change
  // the
  // * root length
  // */
  // public void buildTestList(File testDir, int offset)
  // {
  // // Retrieve all files and subdirs under dir
  // File[] allFiles = testDir.listFiles();
  // for (File f : allFiles) {
  // if (f.isDirectory()) {
  // buildTestList(f, offset);
  // } else {
  // String s = f.getPath();
  // // Skip non-java files not in the test subdir
  // if (s.contains(JAVA)) {
  // _testPaths.add(s.substring(offset + 1));
  // // System.out.println("\tAdding to test list:\t " + s);
  // }
  // }
  // }
  // }
  //
  //
  // /**
  // * Main driver for the QA Tool, calling subordinate methods
  // *
  // * @param root starting directory for source (and test) directories
  // */
  // public void fileScan(File root)
  // {
  // _srcPaths.clear();
  // _testPaths.clear();
  // int srcPrefix = root.getPath().length();
  // buildSourceList(root, srcPrefix);
  // _testDir = findTestDir(root);
  // int testPrefix = _testDir.getPath().length();
  // buildTestList(_testDir, testPrefix);
  // matchSrcToTest(_srcPaths, _testPaths);
  // }


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
    // writeNextTestFile(srcDir, _testDir, srcDir.getPath().length());

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
        // Skip directories
        if ((s.contains("authCode") || (s.contains(TEST)))) {
          // System.err.println("\tSKIPPING ENTIRE DIRECTORY");
          continue;
        }
        writeNextTestFile(f, testDir, rootLen);
      } else {
        // Skip non-source files and hic subdir
        if ((!s.contains(JAVA)) || (s.contains(HIC))) {
          // System.err.println("\tSKIPPING");
          continue;
        } else {
          // Check if test file exists for current source file
          String testFileName = _proto.makeTestFilename(s);
          String s2 = testFileName.substring(s.indexOf("src") + 3);
          // System.out.println("\tWritable file name: \t" + s2);
          // For readability, remove the root prefixes
          File target = new File(testFileName);
          if (target.exists()) {
//            System.err.println("\tEXISTS: \t" + testFileName);
          } else {
            // Remove the root to get the relative pathname
//            System.err.println("\tWRITING: \t" + s2);
            _proto.writeFile(target, s2);
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
   * Check if to skip this directory from scanning
   *
   * @param f directory to check for exclusion
   * @return true if directory should be excluded
   */
  private boolean isExcludedDir(File dir)
  {
    boolean retval = false;
    // Test files are not made from HIC classes or other Test classes
    String path = dir.getAbsolutePath();
    if ((path.contains(HIC)) || (path.contains(TEST))) {
      retval = true;
    } else {
      // Now do normal check of exclusion list
      retval = _excDirs.contains(path);
    }
    return retval;
  }


  /**
   * Check if to skip this file it is not a test file candidate
   *
   * @param file to check for exclusion
   * @return true if file should be excluded
   */
  private boolean isExcludedFile(File file)
  {
    boolean retval = false;
    String path = file.getAbsolutePath();
    if (!path.contains(JAVA)) {
      retval = true;
    } else {
      // Now do normal check of exclusion list
      retval = _excFiles.contains(path);
    }
    return retval;
  }


  /**
   * Read and build the list of directory and files that should be exluded when scanning the source
   * file tree. Exclusions are saved as path names relative to source root directory
   * 
   * @param filePath location of exclusion file
   * @param root platform-dependent absolute path for excluded file
   */
  private void setExclusions(String filePath, String root)
  {
    Scanner in = null;
    File f = null;
    try {
      f = new File(filePath);
      in = new Scanner(f);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String s = in.nextLine();
    if (!s.equals("DIRECTORIES")) {
      System.err.println("\n setExclusions(): " + f.getName() + " has invalid format");
    }
    // Add excluded directories to list
    while (in.hasNext()) {
      s = in.nextLine();
      if (s.equals("FILES") || (s == null)) {
        break;
      }
      _excDirs.add(root + s);
    }
    // Add excluded files to list
    while (in.hasNext()) {
      s = in.nextLine();
      _excFiles.add(root + s);
    }
  }


  // /** Remove the path prefix and add "Test" prefix to filename */
  // private String convertToTestname(String s)
  // {
  // StringBuilder sb = new StringBuilder(s);
  // int ndx = s.lastIndexOf(FILE_DELIM);
  // if (ndx >= 0) {
  // sb.insert(ndx + 1, "Test");
  // }
  // return sb.toString();
  // }


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
      return QATool.this._root;
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
