/**
 * Constants.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is prohibited. To
 * republish, to post on servers, to reuse, or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. by email: acline@carolla.com
 */

package mylib;

import java.io.File;

/**
 * Contains the global constants and methods that all objects require.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 5 Aug 2012 // original, most taken from Chronos.java
 *          <DD>
 *          </DL>
 */
public class Constants
{
  // Global constants
  public static final int OK = 0;
  public static final int ERROR = -1;
  public static final int NOT_FOUND = -1;
  public static final int UNASSIGNED = -99;

  /**
   * Test flag that is sometimes needed to turn off GUI production features during testing
   */
  public static boolean IN_TEST = false;

  /**
   * Current working directory of the user; assumes program is running here. Contains source files in workspace so that
   * all other files can be found relatively. During installation, this root directory is assigned by the user.
   */
  public static final String ROOT_DIR = System.getProperty("user.dir");
  public static final String HOME_DIR = System.getProperty("user.home");
  public static final String USER_NAME = System.getProperty("user.name");
  /** Platform-independent delimeter between file names. For Mac, it is "/" */
  public static final String FILE_SEPARATOR = System.getProperty("file.separator");
  /** Platform-dependent line separator */
  public static final String NEWLINE = System.getProperty("line.separator");
//  public static final String WORKSPACE = new File(ROOT_DIR).getParent();
  public static final String WORKSPACE = HOME_DIR; // + FILE_SEPARATOR + USER_NAME;


  /**
   * Absolute root path to all resources, containing images, characters, and other non-source files
   */
  public static final String RESOURCES = ROOT_DIR + FILE_SEPARATOR + "resources" + FILE_SEPARATOR;
  /** Extension path to user-generating resources, such as the user characters */
  public static final String USER_RESOURCES = RESOURCES + "user" + FILE_SEPARATOR;

  /**
   * Default package name for class files. Package names differ from the directories in that they have a dot (.)
   * separator instead of a slash (/) separator
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

