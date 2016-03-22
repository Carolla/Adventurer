/**
 * TestTemplate.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import mylib.pdc.Utilities;

/**
 * Scan a target source file for all methods, then scan its associated test file for corresponding
 * test methods. If there is no corresponding test file, creates one.
 * <p>
 * For each missing test method, creates a failing stub in the test file. If the source method is
 * prefixed with a {@code @NO_TESTGEN} annotation, skips creating a test stub for it.
 * 
 * @author Alan Cline
 * @version Jan 22, 2016 // original <br>
 */
public class TestTemplate
{
  // General purpose path for recursive storage
  String _path;
  
  ArrayList<String> _srcPaths = new ArrayList<String>();
  ArrayList<String> _testPaths = new ArrayList<String>();

  
  // ======================================================================
  // CONSTRUCTOR AND HELPERS
  // ======================================================================

  public TestTemplate()
  {}


  // ======================================================================
  // PUBLIC METHODS
  // ======================================================================
  

  /**
   * Return a alphabetically sorted list of public and protected methods of the given class
   * 
   * @param clazz target source file
   * @return list of method names for the target (includes arguments to methods)
   */
  public ArrayList<String> getMethods(Class<?> clazz)
  {
    ArrayList<String> result = new ArrayList<String>();
    String clazzName = clazz.getSimpleName();
    for (Method method : clazz.getMethods()) {
      int modifiers = method.getModifiers();
      if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
        String mName = extractSignature(method.toString(), clazzName);
        if (mName != null) {
          result.add(mName);
        }
      }
    }
    // Remove names that are expected or that should be excluded
    result = extractExclusions(result);
    // Sort result using default (alphabetical) sort Comparator
    result.sort(null);
    return result;
  }

  /**
   * Return a alphabetically sorted list of public and protected methods of the given class
   * 
   * @param clazz target source file
   * @return list of method names for the target (includes arguments to methods)
   */
  public ArrayList<String> getMethods(String path)
  {
    // Guard against unknown class path
    Class<?> clazz = null;
    try {
      clazz = Class.forName(path);
    } catch (Exception ex) {
      System.err.println("TestTemplate.getMethods(): " + ex.getMessage());
    }

    ArrayList<String> result = new ArrayList<String>();
    String clazzName = clazz.getSimpleName();
    for (Method method : clazz.getMethods()) {
      int modifiers = method.getModifiers();
      if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
        String mName = extractSignature(method.toString(), clazzName);
        if (mName != null) {
          result.add(mName);
        }
      }
    }
    // Remove names that are expected or that should be excluded
    result = extractExclusions(result);
    // Sort result using default (alphabetical) sort Comparator
    result.sort(null);
    return result;
  }


  /**
   * Match a source file from with its corresponding test file in the test subdir. The test file
   * must have a "Test" prefix on the source file name
   * 
   * @param source tree root to start looking for the source file
   * @param test tree root to start looking for the test file
   * @param source filename (with {@code .java} extension) within the root from which the test file
   *          is derived
   * @return true if a corresponding test file is found for the given source file name         
   */
  public boolean hasMatch(File srcRoot, File testRoot, String srcFilename)
  {
    // Guard against non-existing srcFilename
    String srcPath = getTargetFile(srcRoot, srcFilename);
    if (srcPath == null) {
      System.err.println("\t hasMatch(): " + srcFilename + " does not exist");
      return false;
    }
//    String path = getTargetFile(testRoot, "Test" + srcFilename);
    return (srcPath == null) ? false : true; 
  }


  /**
   * Gets a named file by traversing the file tree from the root down
   * 
   * @param startDir directory from which to start traversing the tree
   * @param fileName short name of the file to find
   * @return the path of the file found, else null if not found
   */
  public String getTargetFile(File startDir, String fileName)
  {
    // Guard against null input
    if ((startDir == null) || (fileName == null)) {
      System.err.println("getTargetFile(): input parms cannot be null");
      return null;
    }
    // Guard against startDir not being a directory
    if (!startDir.isDirectory()) {
      System.err.println("getTargetFile(): startDir parm must be a directory");
      return null;
    }
    // Retrieve first layer of normal files and subdirs under startDir
    File[] allFiles = startDir.listFiles();
    for (File f : allFiles) {
       System.out.println("getTargetFile(): " + f.getPath());
      if (f.isDirectory()) {
        _path = getTargetFile(f, fileName);
      } else {
        if (f.getName().equals(fileName)) {
          _path = f.getPath();
          break;
        }
      }
    }
    return _path;
  }


  // ======================================================================
  // PRIVATE METHODS
  // ======================================================================

  /** Remove any of the files in the list that are intended to be missing */
  private ArrayList<String> extractExclusions(ArrayList<String> authList)
  {
    String[] exclusions = {
        "setUp()", "tearDown", "setUpBeforeClass()", "tearDownAfterClass()", "NotNeeded"
    };
    ArrayList<String> excList = Utilities.convertToArrayList(exclusions);

    for (String s : excList) {
      authList.remove(s);
    }
    return authList;
  }


  /** Return the class method signature without packagine contxt */
  private String extractSignature(String path, String anchorName)
  {
    String s = null;
    int ndx = path.indexOf(anchorName, 0);
    if (ndx > 0) {
      s = path.substring(ndx);
    }
    return s;
  }


} // end of MakeTestTemplate class

