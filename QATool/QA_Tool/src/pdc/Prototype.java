/**
 * Prototype.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Alan Cline
 * @version Jan 29, 2016 // original <br>
 */
public class Prototype
{
  /** The prototype test class actually is written by a text-savvy output stream */
  PrintWriter _writer;
  /** The File to handle I/O stats */
  File _protoFile;

  /** Prototype data to put into the test class */
  private final String COPYRIGHT =
      "/** %s Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved \n * \n" +
          " * Permission to make digital or hard copies of all or parts of this work for \n" +
          " * commercial use is prohibited. To republish, to post on servers, to reuse, \n" +
          " * or to redistribute to lists, requires prior specific permission and/or a fee. \n" +
          " * Request permission to use from Carolla Development, Inc. by email: \n" +
          " * acline@carolla.com */\n";


  // ======================================================================
  // CONSTRUCTORS AND ITS HELPER
  // ======================================================================

  // Default constructor
  public Prototype()
  {}


  // ======================================================================
  // PUBLIC METHODS
  // ======================================================================


  /**
   * Create an empty test file in a corresponding directory as the source's directory. <br>
   * 
   * @param testDir highest directory in which to place the test file created
   * @param srcName path of the source file that will correspond to the target test file created
   * @return the File created
   */
  public File createFile(File testDir, String srcName)
  {
    // Guard against bad combination of directory and source file
    if (!testDir.isDirectory()) {
      System.err.println("createFile: testDir must be a directory");
      return null;
    }
    // Guard against bad combination of directory and source file
    if (new File(srcName).isDirectory()) {
      System.err.println("createFile: srcName cannot be a directory");
      return null;
    }

    // Extract then traverse subdirectories to 'write' destination
    String[] subdirList = srcName.split("/");
    File parent = testDir;
    int k = 0;
    // Create subdirectories as traversing to the srcName
    for (; k < subdirList.length - 1; k++) {
      File subdir = new File(parent, subdirList[k]);
      subdir.mkdir(); // make the subdir if it doesn't exist
      parent = subdir;
    }
    // Last file in subdirList is the prototype's filename
    String targetName = parent.getAbsolutePath() + "/Test" + subdirList[k];
    _protoFile = new File(targetName);
    try {
      _writer = new PrintWriter(_protoFile);
    } catch (IOException e) {
      System.err.println("\tcreateFile(): Problems creating the new protoFile. " + e.getMessage());
    }
    _writer.close();
    return _protoFile;
  }


//  /**
//   * Write the target file with JUnit test stubs and Chronos-specific data
//   * 
//   * @param target prototype test file to write into
//   * @return the File written
//   */
//  public File writeFile()
//  {
//    _writer.open();
//
//    // Write the copyright notice into the prototype
//    try {
//      String copyright = String.format(COPYRIGHT, _protoFile.getName());
//      _writer.append(copyright);
//      _writer.close();
//    } catch (IOException e) {
//      System.err
//          .println("\twriteCopyright(): Problems appending to the protoFile. " + e.getMessage());
//    }
//    return _protoFile;
//
//  }


} // end of Prototype class
