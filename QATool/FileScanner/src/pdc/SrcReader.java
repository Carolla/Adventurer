
/**
 * SrcReader.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
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

import javax.script.SimpleBindings;

import mylib.Constants;

/**
 * Traverses the source tree, providing source files to compare with test files from the
 * {@code TestWriter}.
 * 
 * @author Alan Cline
 * @version Jul 5, 2016 // original <br>
 *          Dec 21, 2016 // refactored QAScanner away; added revisiosn for testing <br>
 *          Mar 27 2017 // manjor refactoring to simplify single file scanning <br>
 */
public class SrcReader
{
   // Path of the tree folder being scanned, used for finding relative paths
   // private String _srcPath;


   // ================================================================================
   // CONSTRUCTOR and HELPER METHODS
   // ================================================================================

   /**
    * Sets object fields and exclusion files
    * 
    * @param srcRoot path of the tree which contains source files to scan
    * @param excludeFile file directly beneath the src root that contains directories and files not
    *           to scan
    * @param testWriter
    */
   public SrcReader()
   {}

   // public SrcReader(String srcPath)
   // {
   // _srcPath = srcPath;
   // }

   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   /**
    * Convert a list of source method names to a map of their test method names with original
    * signatures. For each source name, insert "test" in front the of method name of the signature,
    * and ensure that overloaded method names are enumerated to distinguish them and enforce
    * uniqueness. Add the new name to the map with its original signature
    * 
    * @param srcList list of all method names in source file
    * @return a map of unique test names with corresponding source signatures
    */
   public ArrayList<String> convertToTestNames(ArrayList<String> srcList)
   {
      // Guard null input
      if (srcList == null) {
         return null;
      }
      ArrayList<String> tstList = new ArrayList<String>();

      // For each method name, capitalize the name and insert the word "void test" in front of it
      for (String sName : srcList) {
         String tName = makeTestMethodName(sName);
         tstList.add(tName);
      }
      forceUnique(tstList);
      return tstList;
   }

   // /**
   // * High-level method to get source methods from a given filename
   // *
   // * @param srcPath filepath of file from which to extract methods
   // * @return list of public and protected method signatures for the target
   // */
   // public ArrayList<String> getSourceMethods(String srcPath)
   // {
   // Class<?> clazz = null;
   // try {
   // clazz = QAUtils.getForeignClass(srcPath);
   // } catch (ClassNotFoundException ex) {
   // System.err.println("SrcReader: Source class not found: " + srcPath);
   // }
   // // Collect the methods from the source file
   // ArrayList<String> srcListRaw = QAUtils.extractMethodNames(clazz);
   // return srcListRaw;
   // }


   // ================================================================================
   // PRIVATE METHODS
   // ================================================================================

   /**
    * Pull the method name from the signature to compare uniqueness. Any numeric suffix will be
    * ignored
    * 
    * @param mName method signature
    * @return bare name stripped of return value, parm list, and numeric suffix
    */
   private String extractNameOnly(String mName)
   {
      // Extract only the method name
      int pNdx = mName.indexOf(Constants.LEFT_PAREN);
      char c = (char) mName.charAt(pNdx - 1);
      if (QAUtils.isDigit(c)) {
         --pNdx;
      }
      int spaceNdx = mName.indexOf(Constants.SPACE);
      String shortName = mName.substring(spaceNdx + 1, pNdx);
      return shortName;
   }


   // /**
   // * Extracts public and protected methods from a source file, then sorts the list.
   // *
   // * @param clazzName {@code .class} file for target source file
   // * @return list of public and protected method signatures for the target
   // */
   // private ArrayList<String> extractSrcMethodNames(Class<?> clazzName)
   // {
   // ArrayList<String> mList = new ArrayList<String>();
   //
   // Method[] rawMethodList = clazzName.getDeclaredMethods();
   // for (Method method : rawMethodList) {
   // int modifiers = method.getModifiers();
   // if (modifiers == 0) {
   // QAUtils.verboseMsg("WARNING: " + method.getName()
   // + "() has default access; should have a declared access");
   // }
   // if ((Modifier.isPublic(modifiers)) || (Modifier.isProtected(modifiers))) {
   // String mName = QAUtils.extractSignature(method, clazzName.getName());
   // if (mName != null) {
   // mList.add(mName);
   // }
   // }
   // }
   // // Sort methods to keep in sync with test methods later
   // QAUtils.sortSignatures(mList);
   // return mList;
   // }


   /**
    * Sort all method names and number any overloaded methods. The methods are not in any particular
    * order, so the bare method name must be sorted a little first. Duplicate method names are
    * renamed to start with 1, 2, etc.
    * 
    * @param mList list of method names to check
    * @return the sorted list
    */
   private ArrayList<String> forceUnique(ArrayList<String> mList)
   {
      if (mList.size() <= 1) {
         return mList;
      }
      // All signatures must be sorted for this to work
      sortSignatures(mList);

      // Get first sig to get started
      for (int k = 0; k < mList.size() - 1; k++) {
         String firstSig = mList.get(k);
         String firstName = extractNameOnly(firstSig);
         String nextSig = mList.get(k + 1);
         String nextName = extractNameOnly(nextSig);
         // First get bare name for comparison
         // Check if overloaded methods are in list
         if (nextName.equals(firstName)) {
            String[] names = numerateNames(firstSig, nextSig);
            // Replace old names with modified names
            mList.remove(k);
            mList.add(k, names[0]);
            mList.remove(k + 1);
            mList.add(k + 1, names[1]);
         } else {
            continue;
         }
      }
      return mList;
   }


   /**
    * Convert a single src method name to a test method name
    * 
    * @param srcMethodName name of the method to convert to a test method signature
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
    * Number two sorted signatures, then give the second a higher numerical suffix than the first
    * 
    * @param firstName method name for the first of a set of duplicates
    * @param secondName method name for the second of a set of duplicates
    * @return the numbered signatures first, then the new one, made unique, to add
    */
   private String[] numerateNames(String firstName, String secondName)
   {
      String[] mNames = new String[2];
      StringBuilder sb1 = new StringBuilder(firstName);
      StringBuilder sb2 = new StringBuilder(secondName);
      // Get number of first name
      int paren1 = firstName.indexOf(Constants.LEFT_PAREN);
      int paren2 = secondName.indexOf(Constants.LEFT_PAREN);
      char c1 = sb1.charAt(paren1 - 1);
      char c2 = '-';
      if (QAUtils.isDigit(c1)) {
         c2 = (char) (c1 + 1);
         sb2.insert(paren2, c2);
      } else {
         // bare name has no numeric suffix
         sb1.insert(paren2, "1");
         sb2.insert(paren2, "2");
      }
      mNames[0] = sb1.toString();
      mNames[1] = sb2.toString();
      return mNames;
   }


   /**
    * Sort first by method name, then by parm list number and value
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


} // end of SrcReader class
