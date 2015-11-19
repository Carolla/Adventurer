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

  /** Platform-dependent constants */
  public static final String NEWLINE = System.getProperty("line.separator");
  public static final String FS = System.getProperty("file.separator");
  // Used for converting internal format to delimited strings
  public static final String DELIM = "|";

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

  /* UNIVERSAL CONSTANTS */
  /** Number of seconds in an hour */
  static public final long SECS_PER_HOUR = 3600L;
  /** Number of seconds in a day */
  static public final long SECS_PER_DAY = 86400L;
  /** Number of days in a year (12 months * 30 days per month) */
  static public final long DAYS_PER_YEAR = 360L;
  /** Number of seconds in a 360-day year */
  static public final long SECS_PER_YEAR = SECS_PER_DAY * DAYS_PER_YEAR;
  // TODO: These should move into the abtract CIV class
  /** Conversion data for ounce to pounds */
  static public final int OUNCES_PER_POUND = 16;
  /** Conversion data for ounce to pounds */
  static public final int INCHES_PER_FOOT = 12;

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

