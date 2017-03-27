
/**
 * SrcReader.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */

package pdc;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

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
   private String _srcPath;


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
   public SrcReader(String srcPath)
   {
      _srcPath = srcPath;
   }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   /**
    * High-level method to get source methods from a given filename
    * 
    * @param srcPath filepath of file from which to extract methods
    * @return list of public and protected method signatures for the target
    */
   public ArrayList<String> getSourceToTestMethods(String srcPath)
   {
      Class<?> clazz = null;
      try {
         clazz = QAUtils.convertFileToClass(srcPath);
      } catch (ClassNotFoundException ex) {
         System.err.println("SrcReader: Source class not found: " + srcPath);
      }

      // Collect the methods from the source file
      ArrayList<String> srcListRaw = extractSrcMethodNames(clazz);

      return srcListRaw;
   }


   // ================================================================================
   // PRIVATE METHODS
   // ================================================================================

   /**
    * Extracts public and protected methods from a source file, then sorts the list.
    * 
    * @param clazzName {@code .class} file for target source file
    * @return list of public and protected method signatures for the target
    */
   private ArrayList<String> extractSrcMethodNames(Class<?> clazzName)
   {
      ArrayList<String> mList = new ArrayList<String>();

      Method[] rawMethodList = clazzName.getDeclaredMethods();
      for (Method method : rawMethodList) {
         int modifiers = method.getModifiers();
         if (modifiers == 0) {
            QAUtils.verboseMsg("WARNING: " + method.getName()
                  + "() has default access; should have a declared access");
         }
         if ((Modifier.isPublic(modifiers)) || (Modifier.isProtected(modifiers))) {
            String mName = QAUtils.extractSignature(method, clazzName.getName());
            if (mName != null) {
               mList.add(mName);
            }
         }
      }
      // Sort methods to keep in sync with test methods later
      QAUtils.sortSignatures(mList);
      return mList;
   }


} // end of SrcReader class
