/**
 * TestWriter.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import mylib.Constants;

/**
 * Traverses corresponding test root tree to match files and methods with source files provided by
 * the {@code SrcReader}.
 * 
 * @author Alan Cline
 * @version Jul 5, 2016 // original <br>
 */
public class TestWriter
{
   private File _testRoot;
   private int _filesWritten;
   private int _filesAugmented;
   private int _filesUnchanged;

   private QAScanner _qas;
   private Prototype _proto;

   // Skip over testing prep methods
   static final String[] PREP_METHOD =
         {"setUpBeforeClass", "tearDownAfterClass", "setUp", "tearDown"};


   // ================================================================================
   // CONSTRUCTOR and HELPERS
   // ================================================================================

   public TestWriter(File srcRoot, QAScanner qas)
   {
      _testRoot = makeTestPath(srcRoot);
      _qas = qas;

      _filesWritten = 0;
      _filesAugmented = 0;
      _filesUnchanged = 0;
   }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   // /**
   // * Write new methods to an existing test class
   // *
   // * @param target test file to augment
   // * @param augList method to add to existing test file
   // * @return the test file written
   // */
   // public File augmentTestFile(File target, ArrayList<String> augList)
   // {
   // _proto = new Prototype();
   // _proto.augmentFile(target, augList);
   // return target;
   // }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   /**
    * Convert a source method name to a test method name. Insert "test" in front the of method name
    * of the signature, and ensure that overloaded method names are enumerated to distinguish them
    * and enforce uniqueness.
    * 
    * @param srcList list of all method names in source file
    * @return the list of unique test names generated from the srcList
    */
   public ArrayList<String> convertToTestNames(ArrayList<String> srcList)
   {
      ArrayList<String> tstList = new ArrayList<String>();

      // For each method name, capitalize the name and insert the word "test" in front of it
      for (String sName : srcList) {
         String tName = makeTestName(sName);
         tstList.add(tName);
      }
      _proto.forceUnique(tstList);
      return tstList;
   }


   /**
    * Return the number of new methods that shold be added to an existing test file
    * 
    * @param oldList test methods to augment
    * @param newList all methods, both old and new, in existing test file
    * @return the methods to add to the existing test file
    */
   // public int getNewMethods(File target, ArrayList<String> mlist)
   public ArrayList<String> getNewMethods(ArrayList<String> oldlist, ArrayList<String> newlist)
   {
      _proto = new Prototype(_qas);

      // // Collect existing test methods for comparison
      // ArrayList<String> tmplist = _qas.collectMethods(target.getPath());

      // Remove the standard JUnit prep methods from the list;
      // A copy operation is required to avoid self-modificaiton of the parm
      // for (String m : tmplist) {
      // if (!isPrepMethod(m)) {
      // tlist.add(m);
      // }
      // }
      // Get the new methods that need to be added to the test file
      ArrayList<String> augList = (ArrayList<String>) compareMethods(oldlist, newlist);
      return augList;
   }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   /**
    * Get a corresponding test file to write into, either an existing file to be checked and
    * augmented, or an new file to be written into fresh
    *
    * @param srcPath from which to find (or generate) the corresponding test file
    * @return a corresponding test file
    */
   public File getTargetTestFile(String srcPath)
   {
      PrintWriter out = null;

      String testPath = makeTestFilename(srcPath);
      File target = new File(testPath);
      if (!target.exists()) {
         // Ensure that all intermediate subdirs exist for the target file
         makeSubtree(target);
         try {
            // Ensure that the target file is created anew
            target.delete();
            out = new PrintWriter(target);
         } catch (FileNotFoundException e) {
            System.err.println("\twriteFile(): \t" + e.getMessage());
            return null;
         }
      }
      return target;
   }


   /**
    * Insert "test" after the "src" dir, capitalize the original filename, then insert "Test" in
    * front of the filename.
    * 
    * @param srcPath full path of source file
    * @return test file name that corresponds to source file
    */
   public String makeTestFilename(String srcPath)
   {
      // Guard against non-Java files
      if (!srcPath.endsWith(".java")) {
         return null;
      }
      StringBuilder sbTest = new StringBuilder(srcPath);
      int srcTextNdx = srcPath.lastIndexOf("src");
      sbTest.insert(srcTextNdx + 4, "test" + Constants.FS);
      int ndx = sbTest.lastIndexOf(Constants.FS);
      sbTest.insert(ndx + 1, "Test");

      return sbTest.toString();
   }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   /**
    * Convert the src method name to a test method name
    * 
    * @param srcMethodName name of the method to convert to a test method signature
    * @return the test method signature
    */
   public String makeTestName(String srcName)
   {
      StringBuilder sb = new StringBuilder();
      int endNdx = srcName.indexOf("(");
      int startNdx = srcName.indexOf(" ");
      sb.append(srcName.substring(startNdx + 1, endNdx));
      // Uppercase the first letter of the method name for the decl
      String ch = sb.substring(0, 1);
      sb.replace(0, 1, ch.toUpperCase());
      // Add the test prefix
      sb.insert(0, "test");
      sb.append("()");
      return sb.toString();
   }


   /**
    * Traverse the root dir and return the test subdir beneath it
    * 
    * @param root the directory for all source files
    * @return the test directory
    */
   public File makeTestPath(File root)
   {
      File testDir = null;
      String dirPath = root.getAbsolutePath() + Constants.FS + "test";
      File dir = new File(dirPath);
      if (dir.isDirectory()) {
         testDir = dir;
      }
      return testDir;
   }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   public void writeResults()
   {
      _qas.outMsg("Writing complete: ");
      _qas.outMsg("\t Files written: " + _filesWritten);
      _qas.outMsg("\t Files augmented: " + _filesAugmented);
      _qas.outMsg("\t Files unchanged: " + _filesUnchanged);
   }


   /**
    * Writes to an empty test class if it doesn't exist, or augment an existing test class with new
    * test methods
    * 
    * @param target test file has been created but is empty
    * @param srcList list of source file methods to mirror in the test file
    * @return the test file written
    */
   public File writeTestFile(File testTarget, ArrayList<String> srcList)
   {
      _proto = new Prototype(_qas);
      
      long fileLen = testTarget.length();
      ArrayList<String> convSrcList = convertToTestNames(srcList);
      _qas.outList("\tConverted source methods: ", convSrcList);

      _qas.outMsg("\t" + testTarget + " contains " + fileLen + " characters");
      if (fileLen == 0L) {
         _proto.writeNewTestFile(testTarget, srcList, convSrcList);
         _filesWritten++;
      } else {
         ArrayList<String> testFileList = collectTestMethods(testTarget.getPath());
         _qas.outList("\tTest file methods: ", testFileList);
         ArrayList<String> augList = (ArrayList<String>) compareLists(convSrcList, testFileList);
         _qas.outList("\tMethods in missing from test file: ", augList);
          _proto.augmentTestFile(testTarget, srcList, augList);
         if (augList.size() == 0) {
            _filesUnchanged++;
         }
      }
      return testTarget;
   }


   /**
    * Compare list1 with list2, identifying all entries in list2 that is not in list2. Entries in
    * list2 that are not in list1 are ignored. If an entry exists in list1 that is not in list2,
    * then it is added to the output list and returned.
    * 
    * @param list1 authority list for comparison
    * @param list2 contains entries not in list1
    * @return Entries that are missing from list2.
    */
   private ArrayList<String> compareLists(ArrayList<String> list1, ArrayList<String> list2)
   {
      ArrayList<String> augList = new ArrayList<String>();

      // Search in list2 for every entry in list1
      int srcLen = list1.size();
      for (int s=0; s < srcLen; s++) {
         String name = list1.get(s);
         if (!list2.contains(name)) {
            augList.add(name);
         }
      }         
      return augList;
   }


   /**
    * Collect the methods of the test file, then remove the JUnit prep methods and the void prefix
    * 
    * @param filePath of the target test file to extract methods from
    */
   private ArrayList<String> collectTestMethods(String filePath)
   {
      ArrayList<String> finalList = new ArrayList<String>();
      String unwanted = "void ";

      ArrayList<String> testMethods = _qas.collectMethods(filePath);
      for (String s : testMethods) {
         if (!isPrepMethod(s)) {
            int start = s.indexOf(unwanted);
            String name = s.substring(start + unwanted.length());
            finalList.add(name);
         }
      }
      return finalList;
   }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   /**
    * Ensure that all subdirs in the long path exist
    * 
    * @param target long path of a file to be created
    * @return the short file name
    */
   private String makeSubtree(File target)
   {
      // Remove filename from end of path
      File subtree = target.getParentFile();
      subtree.mkdirs();
      return subtree.getName();
   }


   /**
    * Compare two lists, and returns those that are in list1 but not in list 2. It assumes that
    * list2 is a superset of list1. Entries in list1 that are not in list2 are ignored.
    * 
    * @param list1 the original list of entries
    * @param list2 new entries to compare against the old list
    * @return a list of entries that exist in list2 but not in list1
    */
   private List<String> compareMethods(List<String> list1, List<String> list2)
   {
      ArrayList<String> diffList = new ArrayList<String>();

      // Sort both lists using the default Comparator
      if (list1.size() > 1) {
         list1.sort(null);
      }
      if (list2.size() > 1) {
         list2.sort(null);
      }
      // For each entry in list2 not in list 1, add to the diffList
      for (String s : list2) {
         if (!list1.contains(s)) {
            diffList.add(s);
         }
      }

      return diffList;
   }


   /**
    * Return the package statement for the given source file
    * 
    * @param target test file to write out
    * @return the package statement path
    */
   private String convertSourceToPackage(File target)
   {
      String s = target.getParentFile().getAbsolutePath();
      s = s.substring(s.lastIndexOf("src" + Constants.FS));
      s = s.substring(4); // remove the src/
      String pathName = s.replaceAll(Pattern.quote(Constants.FS), ".");
      String pkgStatement = String.format("\npackage %s;\n", pathName);

      return pkgStatement;
   }


   /**
    * Is the method name a standard JUnit test prep methods?
    * 
    * @param name method name to test
    * @return true if the name is one of the skippable names
    */
   private boolean isPrepMethod(String name)
   {
      boolean retval = false;
      for (int k = 0; k < PREP_METHOD.length; k++) {
         if (name.contains(PREP_METHOD[k])) {
            retval = true;
            break;
         }
      }
      return retval;
   }

}
