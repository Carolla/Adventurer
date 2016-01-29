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
import java.io.FilenameFilter;
import java.util.ArrayList;

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
  ArrayList<String> _srcWithoutTests = new ArrayList<String>();
  ArrayList<String> _testsWithoutSrc = new ArrayList<String>();
  ArrayList<String> _matched = new ArrayList<String>();

  // delimeter to separate files from directories
  private final String FILE_DELIM = "/";
  // Keyword for separating source dir from test subdir
  private final String TEST = "test";
  // Only source files searched
  private final String JAVA = ".java";
  // HIC subdir ignored
  private final String HIC = "hic";

  // Useful filters
  DirectoryFilter _dirFilter = new DirectoryFilter();
  SourceFileFilter _srcFilter = new SourceFileFilter();

  ArrayList<String> _srcPaths = new ArrayList<String>();
  ArrayList<String> _testPaths = new ArrayList<String>();

  /** Root folder for all source files */
  private File _root;
  /** Root folder for all test files */
  private File _testDir;


  // ======================================================================
  // Constructor and Helpers
  // ======================================================================

  /** Default constructor */
  public QATool(String path)
  {
    _root = new File(path);
    if (!_root.isDirectory()) {
      System.err.println("QATool: Path argument must be a directory");
      _root = null;
    }
  }


  // ======================================================================
  // PUBLIC METHODS
  // ======================================================================

  /**
   * Traverse the root dir structure, building paths for all directories that have source files, and
   * excluding the mandatory test subdir. Results are placed into the {@code _srcPaths} field.
   * 
   * @param root starting source directory
   * @param offset the length of the root pathname; required so recusrion doesn't change the root
   *          length
   */
  public ArrayList<String> buildSourceList(File root, int offset)
  {
    // Retrieve all files and subdirs under dir
    File[] allFiles = root.listFiles();
    for (File f : allFiles) {
      // If file is a directory, recurse down one level
      if (f.isDirectory()) {
        buildSourceList(f, offset);
      } else {
        String s = f.getPath();
        // Skip non-source files, and hic and test subdirs
        if ((!s.contains(JAVA)) || (s.contains(HIC)) || s.contains(TEST)) {
          continue;
        } else {
          // Remove root path before adding file path
          _srcPaths.add(s.substring(offset + 1));
          // System.out.println("\tAdding to source list:\t " + s);
        }
      }
    }
    return _srcPaths;
  }


  // ======================================================================
  // PUBLIC METHODS
  // ======================================================================

  /**
   * Traverse the root dir structure to the test subdir, building paths for all test files Results
   * are placed into the {@code _testPaths} field.
   * 
   * @param root starting source directory
   * @param offset the length of the test subdir pathname; required so recursion doesn't change the
   *          root length
   */
  public void buildTestList(File testDir, int offset)
  {
    // Retrieve all files and subdirs under dir
    File[] allFiles = testDir.listFiles();
    for (File f : allFiles) {
      if (f.isDirectory()) {
        buildTestList(f, offset);
      } else {
        String s = f.getPath();
        // Skip non-java files not in the test subdir
        if (s.contains(JAVA)) {
          _testPaths.add(s.substring(offset + 1));
          // System.out.println("\tAdding to test list:\t " + s);
        }
      }
    }
  }


  /**
   * Main driver for the QA Tool, calling subordinate methods
   * 
   * @param root starting directory for source (and test) directories
   */
  public void fileScan(File root)
  {
    _srcPaths.clear();
    _testPaths.clear();
    int srcPrefix = root.getPath().length();
    buildSourceList(root, srcPrefix);
    _testDir = findTestDir(root);
    int testPrefix = _testDir.getPath().length();
    buildTestList(_testDir, testPrefix);
    matchSrcToTest(_srcPaths, _testPaths);
  }


  /** Traverse the root dir and return the test subdir */
  public File findTestDir(File root)
  {
    // Retrieve first layer of normal files and subdirs under dir
    File[] allFiles = root.listFiles();
    // Retrieve TEST subdir
    for (File f : allFiles) {
      // System.out.println("getTestDir(): " + f.getPath());
      if (f.isDirectory()) {
        if (f.getPath().contains(TEST)) {
          _testDir = f;
          break;
        } else {
          f = findTestDir(f);
        }
      }
    }
    return _testDir;
  }


  /**
   * Search a list of test names for its corresponding source file name. Fields are kept to record
   * the matching filenames, source files without test files, and test files with source files. Each
   * match removes the testname from the list, making susbequent searches quicker. After the source
   * list is traversed, all remaining test files name have no source file names.
   * <P>
   * Call the appropriate getters to access the {@code matched} filenames, {@code srcWithoutTests},
   * and {@code testsWithoutSrc} lists, respectively.
   * 
   * @param srcList list of source file names
   * @param testList list of test file names
   */
  public void matchSrcToTest(ArrayList<String> srcList, ArrayList<String> testList)
  {
    // Make a copy because this list is mutable (will decrease)
    _testsWithoutSrc.addAll(testList);
    for (String s : srcList) {
      String compareName = convertToTestname(s);
      while (!_testsWithoutSrc.isEmpty()) {
        if (_testsWithoutSrc.contains(compareName)) {
          _matched.add(s);
          // Any names left in _testsWithoutSrc have no corresponding src name
          _testsWithoutSrc.remove(compareName);
          // System.out.println("\tMatch found. testList size = " + testList.size());
          break; // stop searching test list now that a match was found
        }
        // Add to mismatch list if not found at end of testlist traversal
        _srcWithoutTests.add(s);
        // System.out.println("\tNo test file found for " + s);
        break;
      }
    }
  }


  // ======================================================================
  // Private helper methods
  // ======================================================================

  /** Remove the pathn prefix and add "Test" prefix to filename */
  private String convertToTestname(String s)
  {
    StringBuilder sb = new StringBuilder(s);
    int ndx = s.lastIndexOf(FILE_DELIM);
    if (ndx >= 0) {
      sb.insert(ndx + 1, "Test");
    }
    return sb.toString();
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
