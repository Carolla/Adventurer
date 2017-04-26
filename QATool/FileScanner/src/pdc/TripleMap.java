/**
 * TripleMap.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */

package pdc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
      sortSignatures(_srcNames);
      makeUnique();
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
      sortSignatures(alist);
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
    */
   private void makeUnique()
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


   /**
    * Sort first by method name, then by parm list number and value, using a customized
    * {@code Comparator} object
    * 
    * @param sList collection of method signatures
    */
   private void sortSignatures(ArrayList<String> sList)
   {
      Collections.sort(sList, new Comparator<String>() {
         @Override
         public int compare(String sig1, String sig2)
         {
            // Tokenize into three parts: method name, parm list, return type
            String name1 = sig1.substring(sig1.indexOf(Constants.SPACE) + 1,
                  sig1.indexOf(Constants.LEFT_PAREN));
            String name2 = sig2.substring(sig2.indexOf(Constants.SPACE) + 1,
                  sig2.indexOf(Constants.LEFT_PAREN));
            String parms1 = sig1.substring(sig1.indexOf(Constants.LEFT_PAREN),
                  sig1.indexOf(Constants.RIGHT_PAREN) + 1);
            String parms2 = sig2.substring(sig2.indexOf(Constants.LEFT_PAREN),
                  sig2.indexOf(Constants.RIGHT_PAREN) + 1);
            // System.err.println("\t\t sort loops = " + ++count);

            // Compare method names
            int retval = name1.compareTo(name2); // compare method names
            // Compare number of parms and parms names
            if (retval == 0) {
               String[] nbrParms1 = parms1.split(Constants.COMMA);
               String[] nbrParms2 = parms2.split(Constants.COMMA);
               retval = nbrParms1.length - nbrParms2.length;
               if (retval == 0) {
                  retval = parms1.compareTo(parms2);
               }
            }
            return retval;
         }
      });
   }


}  // end of TripleMap class
