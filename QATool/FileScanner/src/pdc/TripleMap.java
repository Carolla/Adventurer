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

import mylib.Constants;

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

   /**
    * Create a triple map of the same size as the data input, and create a list of test names
    * converted from the srcList
    * 
    * @param srcList source methods names from source file
    */
   public TripleMap(ArrayList<String> srcList)
   {
      int length = srcList.size();
      _srcNames = new ArrayList<String>(length);
      _srcToTestNames = new ArrayList<String>(length);
      _testNames = new ArrayList<String>(length);

      // All signatures must be sorted for this to work
      _srcNames.addAll(srcList);
      QAUtils.sortSignatures(_srcNames);
      makeUnique(_srcToTestNames);
   }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   /**
    * Find test names that are missing from the srcToTestNames list so that they can be written to
    * the existing test file. A {@code Map<srcName, srcToTestName>} is returned to be written to the
    * existing test file. The mapping between srcName (signature) and srcToTestName guarantees all
    * entries are unique, non-null, and maintains the keys sorted alphabetically.
    * 
    * @return {@code Map<srcName, srcToTestName>} as test methods with which to augment an existing
    *         test file.
    */
   public Map<String, String> buildAugMap()
   {
      Map<String, String> augMap = new TreeMap<String, String>();
      int testLen = _testNames.size();
      int srcLen = _srcToTestNames.size();
      // Make space to store new names
      while (srcLen > testLen) {
         _testNames.add(Constants.SPACE);
         testLen = _testNames.size();
      }
      // Add missing test names into map
      for (int k = 0; k < srcLen; k++) {
         String s = _srcToTestNames.get(k);
         String t = _testNames.get(k);
         if (t.equals(Constants.SPACE)) {
            augMap.put(_srcNames.get(k), _srcToTestNames.get(k));
         } else if (!find(s, _testNames)) {
            augMap.put(_srcNames.get(k), s);
         }
      }
      return augMap;
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
    * Sort a list of names and set it into one of the map columns
    * 
    * @param column the list designated by the enum
    * @param alist to add into the map
    */
   public void setMapList(TripleMap.NameType column, ArrayList<String> alist)
   {
      // Names must first be sorted
      QAUtils.sortSignatures(alist);
      switch (column)
      {
         case SRC:
            _srcNames = alist;
            break;
         case SRC_TO_TEST:
            _srcToTestNames = alist;
            break;
         case TEST:
            removePrepMethods(alist);
            _testNames = alist;
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
    * Ensure that all names within the src-to-test name list are unique by adding a numerical suffix
    * to duplicates
    * @param alist  the list into which to place the augmented test names
    */
   private void makeUnique(ArrayList<String> alist)
   {
      // Convert each srcName into a test method name and store
      for (String sName : _srcNames) {
         String s2tName = makeTestMethodName(sName);
         int suffix = 1;
         // Add numerical suffix for existing test names
         if (alist.contains(s2tName)) {
            // Insert the name suffix before the parens
            alist.remove(s2tName);
            s2tName = s2tName.replace("()", suffix + "()");
            alist.add(s2tName);
            s2tName = s2tName.replace(suffix + "()", ++suffix + "()");
            alist.add(s2tName);
         } else {
            alist.add(s2tName);
            // Reset suffix when new name is added
            suffix = 1;
         }
      }
      // One last step: replace testName1() with testName(), which is more likely to match
      // the original test method; leaving testName2() as is.
      String testOne = "1()";
      String testNone = "()";
      for (int k=0; k < alist.size(); k++) {
        String s = alist.get(k);
        if (s.contains(testOne)) {
          String ss = s.replace(testOne, testNone);
          alist.set(k, ss);
        }
      }
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



}  // end of TripleMap class
