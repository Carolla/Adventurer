/**
 * Constants.java Copyright (c) 2010, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
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
  public static final int OK = 0;
  public static final int ERROR = -1;
  public static final int NOT_FOUND = -1;
  public static final int UNASSIGNED = -99;

  public static enum Side {
    LEFT, RIGHT
  };

  /** Platform-dependent constants */
  public static final String NEWLINE = System.getProperty("line.separator");
  public static final String FS = System.getProperty("file.separator");
  public static final String DELIM = "|";

  public static final String LEFT_PAREN = "(";
  public static final String RIGHT_PAREN = ")";
  public static final String SPACE = " ";
  public static final String COMMA = ",";
  public static final String DOT = ".";

  /** Root folder for all source files */
  public static final String SRC_PREFIX = "src" + Constants.FS;

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
      "/** \n * %s Copyright (c) %s, Alan Cline. All Rights Reserved. \n * \n" +
          " * Permission to make digital or hard copies of all or parts of this work for \n" +
          " * commercial use is prohibited. To republish, to post on servers, to reuse, \n" +
          " * or to redistribute to lists, requires prior specific permission and/or a fee. \n" +
          " * Request permission to use from acline@carolla.com. \n */";

  /**
   * Current working directory of the user; assumes program is running here. Contains source files
   * in workspace so that all other files can be found relatively. During installation, this root
   * directory is assigned by the user.
   */
  public static final String MYLIB_ROOT_DIR = Constants.findMyLibRootDir();
  public static final String MYLIB_RESOURCES = MYLIB_ROOT_DIR + FS + "resources" + FS;
  
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
} // end of common library Constants global class

