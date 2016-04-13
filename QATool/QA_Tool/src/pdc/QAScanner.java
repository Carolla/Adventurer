/**
 * QAScanner.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import mylib.Constants;

/**
 * @author Alan Cline
 * @version Apr 11, 2016 // original <br>
 */
public class QAScanner
{
  static final String USAGE_MSG = "USAGE: QA Scanner <src dir tree path> <exclusionsFile>.txt";

  static private File _srcRoot;
  static private File _testRoot;
  static private File _exclusionsFile;

  // Exclusions lists
  ArrayList<String> _excDirs = new ArrayList<String>();
  ArrayList<String> _excFiles = new ArrayList<String>();

  static private int _dirsScanned;
  static private int _filesScanned;
  static private int _filesWritten;
  static private int _filesAugmented;


  /**
   * @param args
   */
  public static void main(String[] args)
  {
    if ((args.length != 2) || (!isValid(args[0], args[1]))) {
      System.err.println(USAGE_MSG);
      System.err.println("Received" + args[0] + " and " + args[1]);
      System.exit(-1);;
    }
    System.out.println("Args valid");
    QAScanner qas = new QAScanner();
    qas.setExclusions(_exclusionsFile, _srcRoot);
    qas.scan(_srcRoot, _srcRoot.getPath().length(), _exclusionsFile);
    
    System.out.println("Source dirs  scanned = " + QAScanner._dirsScanned);
    System.out.println("Source files scanned = " + QAScanner._filesScanned);
    System.out.println("New test files written = " + QAScanner._filesWritten);
    System.out.println("Existing test files expanded = " + QAScanner._filesAugmented);
  }


  // ================================================================================
  // CONSTRUCTOR AND HELPER METHODS
  // ================================================================================

  public QAScanner()
  {
    _dirsScanned = 0;
    _filesScanned = 0;
    _filesWritten = 0;
    _filesAugmented = 0;
  }


  static private boolean isValid(String srcDirPath, String exclusionsFilePath)
  {
    _srcRoot = new File(srcDirPath);
    boolean isSrcDir = _srcRoot.isDirectory();
    _testRoot = new File(srcDirPath + Constants.FS + "test");
    boolean isTestDir = _testRoot.isDirectory();
    boolean isTxtFile = exclusionsFilePath.endsWith(".txt");
    _exclusionsFile = new File(srcDirPath + Constants.FS + exclusionsFilePath);
    boolean isExcFile = _exclusionsFile.isFile();
    return isSrcDir && isTestDir && isTxtFile && isExcFile;
  }


  // ================================================================================
  // PRIVATE METHODS
  // ================================================================================

  /**
   * Traverse the source tree recursively, writing new test class files or augmenting existing test
   * class files accordingly.
   * 
   * @param src starting directory for source files
   * @param srcPathLen length of path prefix to control recursion
   * @param excFile list of directories and files to exclude from test class generation
   * @return true of all worked well
   */
  private boolean scan(File src, int srcPathLen, File excFile)
  {
    // Retrieve all files and subdirs under dir
    File[] allFiles = src.listFiles();
    for (File f : allFiles) {
      // Traverse the source tree until the test dir is found, then stop
      if (f.equals(_testRoot)) {
        break;
      }
      // If file is a directory, recurse down one level
      String s = f.getPath();
      System.out.println("\tExamining " + s);
      if (f.isDirectory()) {
        _dirsScanned++;
        // Skip excluded directories
        if (_excDirs.contains(f)) {
//          System.out.println("SKIPPING directory " + s);
          continue;
        }
        // Recursing one level down when subdirs are found
        scan(f, srcPathLen, excFile);
      } else {
        // Skip non-source files, hic subdir, and any specifically-excluded files
        _filesScanned++;
        if ((!s.endsWith(".java")) || (s.contains("hic"))) {
          continue;
        } else if (_excFiles.contains(s)) {
//          System.out.println("SKIPPING file " + s);
          continue;
        }
      }
    }
    return false;
  }


  
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
      _excDirs.add(rootFile.getPath() + Constants.FS + s);
    }
    // Add excluded files to list
    while (in.hasNext()) {
      s = in.nextLine().trim();
      if (s.equals("FILES") || (s.length() == 0)) {
        continue;
      }
      _excFiles.add(rootFile.getPath() + Constants.FS + s);
    }
  }


} // end of QAScanner class
