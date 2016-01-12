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
import java.util.List;

/**
 * Shows file system as map.
 * 
 * @author Alan CLine
 * @version Jan 1 2016 // original; modified from {@code FileMap.java} found online at
 *          {@code http:/bethecoder/com} <br>
 */
public class FileTree
{
  List<String> _srcWithoutTests;
  List<String> _testWithoutSrc;
  List<String> _matched;

  // delimeter to separate directories from files
  private final String DIR_DELIM = ":";
  // delimeter to separate files from directories
  private final String FILE_DELIM = "/";
  // delimeter to separate files from directories
  private final String PKG_DELIM = ".";

  // Directory containing source file and matching test file
  private File _srcDir;
  private File _targetDir;

  // Useful filters
  DirectoryFilter _dirFilter = new DirectoryFilter();
  SourceFileFilter _srcFilter = new SourceFileFilter();


  // ======================================================================
  // Constructor and Helpers
  // ======================================================================

  /** Default constructor */
  public FileTree()
  {}


  // ======================================================================
  // PUBLIC METHODS
  // ======================================================================

  /** Build a list of source file paths per directory */
  public ArrayList<String> buildComparePaths(File dir)
  {
    ArrayList<String> srcPaths = new ArrayList<String>();
    File[] srcFiles = dir.listFiles(_srcFilter);
    for (File f : srcFiles) {
      String s = f.getAbsolutePath();
      // Remove prefix path to leave subdir and filename
      String firstDelim = s.substring(s.lastIndexOf(FILE_DELIM)+1);
      // Convert to test file name
      String croppedPath = dir.getName() + "/Test" + firstDelim;
      srcPaths.add(croppedPath);
    }
    return srcPaths;
  }


  /**
   * Find all subdirectories that contain one or more {@code .java} source files. Subdirectories are
   * not traversed.
   * 
   * @param root directory in which traversal starts
   * @return a list of directories
   */
  public File[] getSubDirs(File root)
  {
    File[] fileList = root.listFiles(_dirFilter);
    return fileList;
  }


  // /**
  // * Find all matching source and test directories under root
  // *
  // * @param root directory in which traversal starts
  // * @return a list of directories
  // */
  // public boolean findMatchingSources(File srcDir)
  // {
  // boolean retval = false;
  // DirectoryFilter dirFilter = new DirectoryFilter();
  // File[] fileList = dirDir.listFiles(dirFilter);
  // _srcDir = fileList[0];
  // // Traverse to the test directory
  // for (File f : fileList) {
  // if (f.getName().equalsIgnoreCase("test")) {
  // _targetDir = f;
  // }
  // return fileList;
  // }

  /**
   * Get a list of .java source files within the given directory
   * 
   * @param dir directory to be search for source files
   * @return a list of source files
   */
  public File[] getFiles(File dir)
  {
    File[] fileList = dir.listFiles(_srcFilter);
    return fileList;
  }

  /**
   * Get all source files per source directory
   * 
   * @param root starting directory to traverse to retrieve file path names
   * @return a list of sourcec files by directory pathname
   */
  public ArrayList<String> getSourceFilesPerDirectory(File root)
  {
    ArrayList<String> pathList = new ArrayList<String>();
    File[] dirList = getSubDirs(root);
    for (File dir : dirList) {
      File[] srcList = getFiles(dir);
      // Add dir name and file name per directory as one entry in the arraylist
      for (File src : srcList) {
        pathList.add(dir.getName() + ":" + src.getName());
      }
    }
    return pathList;
  }

  /**
   * Convert a source file name to that of a test file by prefixing the word "Test" to it.
   * 
   * @param srcList list of pathnames for the source files
   * @return list of converted src names
   */
  public ArrayList<String> convertSourceToTestNames(File[] srcList)
  {
    ArrayList<String> convertedSrc = new ArrayList<String>();
    // Remove dir prefix and add test for filename
    for (File f : srcList) {
      String s = f.getName();
      // // Remove dir prefix and delim
      // String target = s.substring(s.indexOf(DIR_DELIM) + 1);
      // String dirName = s.substring(0, s.indexOf(DIR_DELIM));
      // Extract filename from path
      int ndx = s.lastIndexOf(FILE_DELIM);
      String testName = (ndx < 0) ? "Test" + s : "Test" + s.substring(ndx);
      // String fullTarget = dirName + FILE_DELIM + testName;
      // int ndx = target.lastIndexOf(FILE_DELIM);
      // String testName = (ndx < 0) ? "Test" + target : "Test" + target.substring(ndx);
      // String fullTarget = dirName + FILE_DELIM + testName;
      // System.out.print("\tSearching for testfile: " + fullTarget);
      convertedSrc.add(testName);
    }
    return convertedSrc;
  }


  /** Given a source file, checks is a test subdirectory exists for it */
  public boolean testDirExists(File dir)
  {
    boolean retval = false;
    File parent = dir.getParentFile();
    File[] dirList = parent.listFiles(_dirFilter);
    // Look for test subdir
    for (File f : dirList) {
      if (f.getName().equalsIgnoreCase("test")) {
        File[] testDirs = getSubDirs(f);
        // Look for test subdir with matching parm name
        for (File f2 : testDirs) {
          if (f2.getName().equalsIgnoreCase(dir.getName())) {
            retval = true;
            break;
          }
        }
      }
    }
    return retval;
  }


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
