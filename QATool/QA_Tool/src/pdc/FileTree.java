/**
 * FileTree.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
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
 * Shows file system as map.
 * 
 * @author Alan CLine
 * @version Jan 1 2016 // original; modified from {@code FileMap.java} found online at
 *          {@code http:/bethecoder/com} <br>
 */
public class FileTree
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


  // ======================================================================
  // Constructor and Helpers
  // ======================================================================

  /** Default constructor */
  public FileTree()
  {}


  // ======================================================================
  // PUBLIC METHODS
  // ======================================================================

  public void qaFileScan(File root)
  {
    _srcPaths.clear();
    _testPaths.clear();
    int srcPrefix = root.getPath().length();
    buildSourceList(root, srcPrefix);
    File testSubdir = getTestDir(root);
    int testPrefix = testSubdir.getPath().length();
    buildTestList(testSubdir, testPrefix);
    matchSrcToTest(_srcPaths, _testPaths);
  }


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
          _testPaths.add(s.substring(offset+1));
//          System.out.println("\tAdding to test list:\t " + s);
        }
      }
    }
  }


  /**
   * Traverse the root dir structure, building paths for all directories that have source files, and
   * excluding the mandatory test subdir. Results are placed into the {@code _srcPaths} field.
   * 
   * @param root starting source directory
   * @param offset the length of the root pathname; required so recusrion doesn't change the root
   *          length
   */
  public void buildSourceList(File root, int offset)
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
        if ((s.contains(JAVA) == false) || (s.contains(HIC) == true) || s.contains(TEST)) {
          continue;
        } else {
          // Remove root path before adding file path
          _srcPaths.add(s.substring(offset+1));
          // System.out.println("\tAdding to source list:\t " + s);
        }
      }
    }
  }


  // /**
  // * Build a list of source file paths per directory
  // *
  // * @param dir directory to use as root for separating source filenames from test filenames
  // * @param offset is the path that will get truncated for easier comparison. It is fixed
  // regardless
  // * of recursion
  // * @param testPaths list for all test filenames
  // */
  // public void buildComparePaths(File dir, int offset)
  // {
  // // Retrieve all files and subdirs under dir
  // File[] allFiles = dir.listFiles();
  // for (File f : allFiles) {
  // // If file is a directory, recurse down one level
  // if (f.isDirectory()) {
  // buildComparePaths(f, offset);
  // } else {
  // String s = f.getPath();
  // // Process only source files
  // if ((s.contains(JAVA) == false) || (s.contains(HIC) == true)) {
  // continue;
  // }
  // // Otherwise, add test files to test list
  // if (s.contains(TEST)) {
  // int ndx = s.indexOf(TEST);
  // String tPath = s.substring(ndx + TEST.length() + 2);
  // _testPaths.add(tPath);
  // System.out.println("\tAdding to test list:\t " + tPath);
  // } else {
  // String sPath = s.substring(offset + 1);
  // _srcPaths.add(sPath);
  // System.out.println("\tAdding to source list:\t " + sPath);
  // }
  // }
  // }
  // }


  /** Traverse the root dir and return the test subdir */
  public File getTestDir(File root)
  {
    // Retrieve first layer of normal files and subdirs under dir
    File[] allFiles = root.listFiles();
    // Retrieve TEST subdir
    for (File f : allFiles) {
      if ((f.getPath().contains(TEST)) && (f.isDirectory())) {
        return f;
      }
    }
    return null;
  }


  public ArrayList<String> getMatched()
  {
    return _matched;
  }


  public ArrayList<String> getSrcPaths()
  {
    return _srcPaths;
  }


  public ArrayList<String> getSrcWithoutTests()
  {
    return _srcWithoutTests;
  }

  public ArrayList<String> getTestPaths()
  {
    return _testPaths;
  }


  public ArrayList<String> getTestsWithoutSrc()
  {
    return _testsWithoutSrc;
  }


  // /**
  // * Traverse a single level beneath a directory to build a list for that subdir
  // *
  // * @param dir diectory on which to start file traversal
  // * @return list of paths for files in dir
  // */
  // public ArrayList<String> getSourceFiles(File dir)
  // {
  // File[] srcFiles = dir.listFiles(_srcFilter);
  // for (File f : srcFiles) {
  // String s = f.getPath();
  // // If file is in a test subdir, then move pathname into test path list
  // _srcPaths.add(s);
  // }
  // return _srcPaths;
  // }


  // /**
  // * Find all subdirectories that contain one or more {@code .java} source files. Subdirectories
  // are
  // * not traversed.
  // *
  // * @param root directory in which traversal starts
  // * @return a list of directories
  // */
  // public File[] getSubDirs(File root)
  // {
  // File[] fileList = root.listFiles(_dirFilter);
  // return fileList;
  // }


  // /**
  // * Get a list of .java source files within the given directory
  // *
  // * @param dir directory to be search for source files
  // * @return a list of source files
  // */
  // public File[] getFiles(File dir)
  // {
  // File[] fileList = dir.listFiles(_srcFilter);
  // return fileList;
  // }
  //
  // /**
  // * Get all source files per source directory
  // *
  // * @param root starting directory to traverse to retrieve file path names
  // * @return a list of sourcec files by directory pathname
  // */
  // public ArrayList<String> getSourceFilesPerDirectory(File root)
  // {
  // ArrayList<String> pathList = new ArrayList<String>();
  // File[] dirList = getSubDirs(root);
  // for (File dir : dirList) {
  // File[] srcList = getFiles(dir);
  // // Add dir name and file name per directory as one entry in the arraylist
  // for (File src : srcList) {
  // pathList.add(dir.getName() + ":" + src.getName());
  // }
  // }
  // return pathList;
  // }


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
    _matched.clear();
    _srcWithoutTests.clear();
    _testsWithoutSrc.clear();
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


  // /** Given a source file, checks is a test subdirectory exists for it */
  // public boolean testDirExists(File dir)
  // {
  // boolean retval = false;
  // File parent = dir.getParentFile();
  // File[] dirList = parent.listFiles(_dirFilter);
  // // Look for test subdir
  // for (File f : dirList) {
  // if (f.getName().equalsIgnoreCase("test")) {
  // File[] testDirs = getSubDirs(f);
  // // Look for test subdir with matching parm name
  // for (File f2 : testDirs) {
  // if (f2.getName().equalsIgnoreCase(dir.getName())) {
  // retval = true;
  // break;
  // }
  // }
  // }
  // }
  // return retval;
  // }


  // /**
  // * Compare two lists of source and test files to find the source files without tests, the test
  // * files without source, and those that match. The files must be in corresponding directories,
  // * e.g., {@code pdc/testObject.java} and {@code test/pdc/testObject.java}. The three lists are
  // * placed into fields for three different getters.
  // *
  // * @param srcRoot list of pathnames for the source files
  // * @param testRoot list of pathnames for the test files
  // * @return
  // */
  // public ArrayList<String> convertSourceToTestNames(File[] srcList, File[] testList)
  // {
  // ArrayList<String> convertedSrc = new ArrayList<String>();
  // // Remove dir prefix and add test for filename
  // for (File f : srcList) {
  // String s = f.getName();
  // // Remove dir prefix and delim
  // String target = s.substring(s.indexOf(DIR_DELIM) + 1);
  // String dirName = s.substring(0, s.indexOf(DIR_DELIM));
  // // Extract filename from path
  // int ndx = target.lastIndexOf(FILE_DELIM);
  // String testName = (ndx < 0) ? "Test" + target : "Test" + target.substring(ndx);
  // String fullTarget = dirName + FILE_DELIM + testName;
  //// System.out.print("\tSearching for testfile: " + fullTarget);
  // convertedSrc.add(fullTarget);
  // }
  // return convertedSrc;
  // }


  // ======================================================================
  // INNER Class SourceFileFilter
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
        // // Check for exluded directories
        // if ((candidate.getName().equalsIgnoreCase("HIC")) ||
        // (candidate.getName().equalsIgnoreCase("TEST"))) {
        // return false;
        // }
        // Check for source files within non-excluded directories
        File[] fileList = candidate.listFiles(srcFilter);
        if (fileList.length != 0) {
          retval = true;
        }
      }
      return retval;
    }


  } // End of DirectoryFilter inner class


} // end of FileMap class
