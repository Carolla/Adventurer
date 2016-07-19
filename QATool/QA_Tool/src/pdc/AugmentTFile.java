/**
 * AugmentTFile.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
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
import java.util.List;


/**
 * Add missing test methods to an existing test file based on associated src file
 * 
 * @author Alan Cline
 * @version Apr 7, 2016 // original <br>
 */
public class AugmentTFile
{
  /** Public and protected methods for src file */
  private List<String> _srcMethods;
  /** Public and protected methods for already in test file */
  private List<String> _testMethods;

  private Prototype _proto;

  // ======================================================================
  // CONSTRUCTOR
  // ======================================================================

  public AugmentTFile(QAScanner qas)
  {
    _proto = new Prototype(qas);
    _srcMethods = new ArrayList<String>();
    _testMethods = new ArrayList<String>();
  }


  // ======================================================================
  // PUBLIC METHODS
  // ======================================================================

  public void augment(String srcPath, String testPath)
  {
    // Start off with fresh method list
    _srcMethods.clear();
    _testMethods.clear();

    // Guard
    File srcFile = new File(srcPath);
    if (!srcFile.exists()) {
      return;
    }
    // Find methods in source and existing test file
    else {
      Class<?> srcClass = _proto.convertSourceToClass(srcPath);
      if (srcClass != null) {
        _srcMethods = getMethods(srcClass);
      }
      Class<?> testClass = _proto.convertSourceToClass(testPath);
      if (testClass != null) {
        _testMethods = getMethods(testClass);
        int nbrToWrite = diffMethods();
      }
    }
  }


  /** Compare the src and test methods to deteremine which ones to add to the test files */
  private int diffMethods()
  {
    // Remove the prep methds
    _testMethods.remove("void setUp()");
    _testMethods.remove("void tearDown()");
    _testMethods.remove("static setUpBeforeClass()");
    _testMethods.remove("static tearDownAfterClass()");

    // Remove each test method found in the test file that matches a src method
    for (String sm : _srcMethods) {
      String testName = makeTestName(sm);
      _testMethods.remove(testName);
    }
    return _testMethods.size();
  }

  /**
   * Convert the src method name to a test method name
   * 
   * @param srcMethodName name of the method to convert to a test method signature
   * @return the test method signature
   */
  private String makeTestName(String srcName)
  {
    StringBuilder sb = new StringBuilder();
    int endNdx = srcName.indexOf("(");
    int startNdx = srcName.indexOf(" ");
    sb.append(srcName.substring(startNdx + 1, endNdx));
    // Uppercase the first letter of the method name for the decl
    String ch = sb.substring(0, 1);
    sb.replace(0, 1, ch.toUpperCase());
    // Add the void test prefix
    sb.insert(0,  "void test");
    sb.append("()");
    return sb.toString();
  }


  /**
   * Extracts public and protected methods from the source file, sorts each list
   * 
   * @param clazz target source file
   * @return list of public method names for the target (includes arguments to methods)
   */
  private ArrayList<String> getMethods(Class<?> clazz)
  {
    ArrayList<String> nameList = new ArrayList<String>();

    String clazzName = clazz.getSimpleName();
    Method[] rawMethodList = clazz.getDeclaredMethods();
    for (Method method : rawMethodList) {
      int modifiers = method.getModifiers();
      if (modifiers == 0) {
        System.err.println("WARNING: " + method.getName()
            + "() has default access; should have a declared access");
      }
      if ((Modifier.isPublic(modifiers)) || (Modifier.isProtected(modifiers))) {
        String mName = _proto.extractSignature(method, clazzName);
        if (mName != null) {
          nameList.add(mName);
        }
      }
    }
    // Ensure that overloaded method are sorted then distinguished by number
    nameList = _proto.forceUnique(nameList);
    return nameList;
  }


  // ======================================================================
  // Inner Class: MockAugmentTFile
  // ======================================================================

  public class MockAugmentTFile
  {
    public MockAugmentTFile()
    {}

    public List<String> getSourceMethods()
    {
      return AugmentTFile.this._srcMethods;
    }

    public List<String> getTestMethods()
    {
      return AugmentTFile.this._testMethods;
    }

  }

} // end of AugmentTFile class
