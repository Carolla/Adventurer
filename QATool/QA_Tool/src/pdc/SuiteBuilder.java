/**
 * SuiteBuilder.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc;

import java.io.File;
import java.util.ArrayList;

/**
 * Traverse test tree and add test classes found into a test suite, organized by the primary MVP
 * components: PDC, CIV, DMC, and SIC (but not HIC at this time).
 * 
 * @author Alan Cline
 * @version Mar 21, 2016 // original <br>
 */
public class SuiteBuilder
{
  // Error messages
  static private String USAGE_MSG = "USAGE: argument should be root directory of all test files.";
  static private String WRONGARGS_MSG = "Wrong number of arguments given";
  static private String BADDIR_MSG = "Directory given is not a test directory";

  static private int _filesScanned;
  static private int _filesWritten;
  static private int _dirsScanned;

  /** Collection of file names to write into the Test Suite */
  ArrayList<String> _filenames;

  /** This object */
  static private SuiteBuilder _sb;


  /** Default constructor */
  public SuiteBuilder()
  {
    _filenames = new ArrayList<String>();
  }


  /**
   * Given the root test tree, scan all test files from the test directory downward
   * 
   * @param args
   */
  public static void main(String[] args) throws IllegalArgumentException
  {
    // Guards for invalid argument
    if (args.length != 1) {
      throw new IllegalArgumentException(WRONGARGS_MSG);
    }
    File testRoot = new File(args[0]);
    if (!testRoot.isDirectory()) {
      throw new IllegalArgumentException(BADDIR_MSG);
    }
    _sb = new SuiteBuilder();
  }


  /**
   * Recursively traverse the root dir structure, collecting test files
   * 
   * @param testDir test directory root, subdir of srcDir
   * @param rootLen length of the original srcDir, a constant throughout recursion
   * @return a list of test classes by subdirectory
   */
  public ArrayList<String> collectTestFileNames(File testDir)
  {
    // Retrieve all files and subdirs under dir
    File[] allFiles = testDir.listFiles();
    for (File f : allFiles) {
      // If file is a directory, recurse down one level
      String s = f.getPath();
      // System.out.println("\tExamining " + s);
      if (f.isDirectory()) {
        _dirsScanned++;
        collectTestFileNames(f);
      } else {
        _filesScanned++;
        // Skip HIC subdir and other test suits, and any file that does not start with Test prefix
        if ((!s.contains("hic")) && (s.endsWith(".java") && (s.startsWith("Test")))) {
            _filenames.add(s);
        } else {
          continue;
        }
      }
    }
    return _filenames;
  }

  
}   // end of SuiteBuilder class
