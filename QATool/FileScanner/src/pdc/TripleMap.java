/**
 * TripleMap.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */

package pdc;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import mylib.MsgCtrl;

/**
 * Maintain a three-value synchronized list. Neither are key-value pairs, but when one entry is
 * moved, the corresponding entries in the other lists are moved accordingly.
 * 
 * @author Alan Cline
 * @version Apr 16, 2017 // original <br>
 */
public class TripleMap
{
  /** Designator for 1st, 2nd, or 3rd column of the TripleMap */
  public enum NameType {
    SRC, SRC_TO_TEST, TEST
  };

  /** First column for source method names */
  ArrayList<String> _srcNames;
  /** Second column for source names that have been converted to test names */
  ArrayList<String> _srcToTestNames;
  /** Third column for test method names */
  ArrayList<String> _testNames;
  /** Test names (key) matched with source signatures (value) */
  Map<String, String> _augMap;


  // ================================================================================
  // CONSTRUCTOR
  // ================================================================================

  /**
   * Create a triple map to hold source method names, test names generated from the source names,
   * and the actual test names if a corresponding test file exists
   */
  public TripleMap()
  {
    _srcNames = new ArrayList<String>();
    _srcToTestNames = new ArrayList<String>();
    _testNames = new ArrayList<String>();
    _augMap = new TreeMap<String, String>();
  }


  // ================================================================================
  // PUBLIC METHODS
  // ================================================================================

  /**
   * Add a method name into the proper entry of the map
   * 
   */
  public void addEntry(String entry, TripleMap.NameType kind)
  {
    switch (kind)
    {
      case SRC:
        _srcNames.add(entry);
        break;
      case SRC_TO_TEST:
        _srcToTestNames.add(entry);
        break;
      case TEST:
        _testNames.add(entry);
        break;
      default:
        System.err.println("addEntry(): unrecognized NameType ");
    }
  }


  /**
   * Compare the generated test names with the actual test names in the test file, and make a list
   * of the test method names missing. Input data and output data are internal fields, use
   * {@code export} method to retrieve result. <br>
   * NOTE: If there are no test names, then the genned and src names are copied directly into the
   * augMap.
   * 
   * @return a copy of the augMap
   */
  public Map<String, String> buildAugMap()
  {
    // Ensure that the names are sorted and overloaded names handled
    QAUtils.sortSignatures(_srcNames);
    QAUtils.sortSignatures(_srcToTestNames);

    // If no test names, copy src and genned names into the map
    if (_testNames.size() == 0) {
      for (int k = 0; k < _srcToTestNames.size(); k++) {
        _augMap.put(_srcToTestNames.get(k), _srcNames.get(k));
      }
      return _augMap;
    }

    // Ensure that the names are sorted and overloaded names handled
    QAUtils.sortSignatures(_testNames);

    int g = 0;
    int tlen = _testNames.size();
    int genlen = _srcToTestNames.size();
    MsgCtrl.msgln("\n" + tlen + " test methods found; \t" + genlen
        + " generated test methods from source");

    for (int t = 0; t < tlen; t++) {
      // If all generated names are examined, then no more to add to augMap
      if (g >= genlen) {
        break;
      }
      String genNm = _srcToTestNames.get(g);
      String testNm = _testNames.get(t);
      int match = testNm.compareTo(genNm);
      if (match > 0) {
        MsgCtrl.errMsgln("\tMissing from test file: \t [----- ] \t\t => \t" + genNm);
        _augMap.put(genNm, _srcNames.get(g));
        g++;
        t--;
        continue;
      } else if (match < 0) {
        MsgCtrl.msgln("\tExtra method in test file: \t" + testNm + "\t [ ----- ]");
        continue;
      } else {
        MsgCtrl.msgln("\tAlready in test file: \t" + testNm + "\t\t => \t" + genNm);
        g++;
      }
    }
    // If out of test names but still have genned names, copy them into the map
    for (int k = g; k < genlen; k++) {
      _augMap.put(_srcToTestNames.get(k), _srcNames.get(k));
    }

    // Display the result if in audit mode
    MsgCtrl.auditPrintMap("\nTest name (key) and corresponding src signature", _augMap);
    return _augMap;
  }


  /**
   * Traverse through the internal src names and generate test names from them, ensuring that
   * all names within the src-to-test name list are unique by adding a numerical suffix.
   */
  public void convertSrcToTestNames()
  {
    // Convert each srcName into a test method name and store
    for (String sName : _srcNames) {
      String s2tName = makeTestMethodName(sName);
      int suffix = 1;
      // Add numerical suffix for existing test names
      if (_srcToTestNames.contains(s2tName)) {
        // Insert the name suffix before the parens
        _srcToTestNames.remove(s2tName);
        s2tName = s2tName.replace("()", suffix + "()");
        _srcToTestNames.add(s2tName);
        s2tName = s2tName.replace(suffix + "()", ++suffix + "()");
        _srcToTestNames.add(s2tName);
      } else {
        _srcToTestNames.add(s2tName);
        // Reset suffix when new name is added
        suffix = 1;
      }
    }
  }


  /**
   * Return one of the three lists of the map
   * 
   * @param column the list designated by the enum
   * @return the designated list
   */
  public ArrayList<String> export(TripleMap.NameType column)
  {
    switch (column)
    {
      case SRC:
        return _srcNames;
      case SRC_TO_TEST:
        return _srcToTestNames;
      case TEST:
        return _testNames;
      default:
        return null;
    }
  }

  /**
   * Return the map of augment test names with corresponding source signature
   * 
   * @return the designated list
   */
  public Map<String, String> exportAugMap()
  {
    return _augMap;
  }


  /**
   * Set a list of names into one of the map columns
   * 
   * @param column the list designated by the enum
   * @param alist to add into the map
   */
  public void setMapList(TripleMap.NameType column, ArrayList<String> alist)
  {
    switch (column)
    {
      case SRC:
        _srcNames.clear();
        _srcNames.addAll(alist);
        break;
      case SRC_TO_TEST:
        _srcToTestNames.clear();
        _srcToTestNames.addAll(alist);
        break;
      case TEST:
        _testNames.clear();
        _testNames.addAll(alist);
        break;
      default:
        System.err.println("If you got here, you probably entered a bad NameType");
        break;
    }
  }


  // ================================================================================
  // PRIVATE METHODS
  // ================================================================================

  /**
   * Find a String match from within a list
   * 
   * @param src the string to match
   * @param alist the list of candidate strings
   * @return true if src matches an entry in the list, else false
   */
  private boolean find(String src, ArrayList<String> alist)
  {
    for (int j = 0; j < alist.size(); j++) {
      if (src.equals(alist.get(j))) {
        return true;
      }
    }
    return false;
  }


  /**
   * Convert a single src method name to a test method name
   * 
   * @param srcName name of the method to convert
   * @return the test method signature
   */
  private String makeTestMethodName(String srcName)
  {
    StringBuilder sb = new StringBuilder();
    int endNdx = srcName.indexOf("(");
    int startNdx = srcName.indexOf(" ");
    sb.append(srcName.substring(startNdx + 1, endNdx));
    // Uppercase the first letter of the method name for the decl
    String ch = sb.substring(0, 1);
    sb.replace(0, 1, ch.toUpperCase());
    // Add the test prefix
    sb.insert(0, "void test");
    sb.append("()");
    return sb.toString();
  }


  /**
   * Remove the standard prep methods at the beginning of JUnit test files
   * 
   * @param alist of test method namess
   */
  private void removePrepMethods(ArrayList<String> alist)
  {
    String[] arg = {"void setUpBeforeClass()", "void tearDownAfterClass()", "void setUp()",
        "void tearDown()"};

    // Remove the prep methods from the list
    for (int k = 0; k < arg.length; k++) {
      alist.remove(arg[k]);
    }
  }



} // end of TripleMap class
