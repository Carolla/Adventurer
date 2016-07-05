/**
 * Constants.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package mylib;

import java.awt.Color;
import java.io.File;
import java.io.IOException;


/**
 * Contains the global constants and methods that all objects require.
 * 
 * @author Alan Cline
 * @version Aug 5, 2012 // original, most taken from Chronos.java <br>
 *          June 21, 2014 // ABC: Added env var CHRONOS_ROOT install dir location <br>
 */
public class Constants
{
  // Global constants
  public static final int OK = 0;
  public static final int ERROR = -1;
  public static final int NOT_FOUND = -1;
  public static final int UNASSIGNED = -99;
  public static final String SPACE = " ";

  /** General directions, particular for left and right panels */
  public static enum Side {
    LEFT, RIGHT
  };

  /** Platform-dependent constants */
  public static final String NEWLINE = System.getProperty("line.separator");
  public static final String FS = System.getProperty("file.separator");
  // Used for converting internal format to delimited strings
  public static final String DELIM = "|";

  public static final String LEFT_PAREN = "(";
  public static final String RIGHT_PAREN = ")";

  /** Initializer for MYLIB_ROOT */
  private static String findMyLibRootDir()
  {
    String fileName = null;
    try {
      fileName = new File(".").getCanonicalPath();
    } catch (IOException e) {
      System.err.println("Unable to locate MYLIB_ROOT");
      System.exit(-1);
    }
    return fileName;
  }

  /** Copyright notice placed on all files, either manually or by autogen */
  public static final String COPYRIGHT =
      "/** \n * %s Copyright (c) %s, Carolla Development, Inc. All Rights Reserved \n * \n" +
          " * Permission to make digital or hard copies of all or parts of this work for \n" +
          " * commercial use is prohibited. To republish, to post on servers, to reuse, \n" +
          " * or to redistribute to lists, requires prior specific permission and/or a fee. \n" +
          " * Request permission to use from Carolla Development, Inc. by email: \n" +
          " * acline@carolla.com \n */\n";

  /**
   * Current working directory of the user; assumes program is running here. Contains source files
   * in workspace so that all other files can be found relatively. During installation, this root
   * directory is assigned by the user.
   */
  public static final String MYLIB_ROOT_DIR = Constants.findMyLibRootDir();
  // public static final String ROOT_DIR = System.getProperty("user.dir");
  // public static final String HOME_DIR = System.getProperty("user.home");
  // public static final String USER_NAME = System.getProperty("user.name");
  /* Platform-independent delimiter between file names. For Mac, it is "/" */
  // public static final String FILE_SEPARATOR = System.getProperty("file.separator");
  // public static final String WORKSPACE = new File(ROOT_DIR).getParent();
  // public static final String WORKSPACE = HOME_DIR; // + FILE_SEPARATOR + USER_NAME;

  /**
   * Absolute root path to all resources, containing images, characters, and other non-source files
   */
  public static final String MYLIB_RESOURCES = MYLIB_ROOT_DIR + FS + "resources" + FS;
  /** Extension path to user-generating resources, such as the user characters */
  // public static final String USER_RESOURCES = RESOURCES + "user" + FILE_SEPARATOR;

  // LOG FOR TESTING
  // static {
  // System.out.println("MyLib.Constants Log: ");
  // System.out.println("MYLIB_ROOT_DIR = " + MYLIB_ROOT_DIR);
  // System.out.println("MYLIB_RESOURCES = " + MYLIB_RESOURCES);
  // }

  /**
   * Default package name for class files. Package names differ from the directories in that they
   * have a dot (.) separator instead of a slash (/) separator
   */
  public static String DEFAULT_PKG = "pdc.";

  /** The Random Access file mode for read-only files */
  public static final String READ_ONLY = "r";
  /** The Random Access file mode for read-write files */
  public static final String READ_WRITE = "rw";

  /** My own special version of Brown since there is not one for Color */
  static public final Color MY_BROWN = new Color(130, 100, 90).brighter();


  /**
   * Allow the base location of class files to be defined or redirected for testing
   * 
   * @return the current base directory, typically the source directory for the project
   */
  static public String getPackageName()
  {
    return DEFAULT_PKG;
  }

  /**
   * Allow the base location of class files to be defined or redirected for testing
   * 
   * @param pkgName location of file wrto the Chronos root directory
   */
  static public void setPackageName(String pkgName)
  {
    DEFAULT_PKG = pkgName;
  }


} // end of common library Constants global class

