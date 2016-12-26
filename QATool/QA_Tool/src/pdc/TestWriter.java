/**
 * TestWriter.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */

package pdc;

import java.io.File;
import java.util.ArrayList;

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
   // private File _testRoot;
   private int _filesWritten;
   private int _filesAugmented;
   private int _filesUnchanged;

   private Prototype _proto;


   // ================================================================================
   // CONSTRUCTOR and HELPERS
   // ================================================================================

   /**
    * Create a writer to create or augment test cases
    * 
    * @param srcRoot root of source tree from which to derive test classes and methods
    * @param verbose if true, writes audit messages to console
    * @param nofail if true, writes Not Implemented test stubs instead of failing test stubs
    */
   // public TestWriter(File srcRoot, boolean verbose, boolean nofail)
   // public TestWriter(File srcRoot)
   public TestWriter()
   {
      // _testRoot = makeTestPath(srcRoot);
      // _verbose = verbose;
      // _nofail = nofail;

      // TestWriter needs a Prototype object to create the test file
      _proto = new Prototype();

      _filesWritten = 0;
      _filesAugmented = 0;
      _filesUnchanged = 0;
   }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   // /**
   // * Get a corresponding test file to write into, either an existing file to be checked and
   // * augmented, or an new file to be written into fresh
   // *
   // * @param srcPath from which to find (or generate) the corresponding test file
   // * @return a corresponding test file
   // */
   // private File getTargetTestFile(String srcPath)
   // {
   // PrintWriter out = null;
   //
   // String testPath = makeTestFilename(srcPath);
   // File target = new File(testPath);
   // if (!target.exists()) {
   // // Ensure that all intermediate subdirs exist for the target file
   // makeSubtree(target);
   // try {
   // // Ensure that the target file is created anew
   // target.delete();
   // out = new PrintWriter(target);
   // } catch (FileNotFoundException e) {
   // System.err.println("\twriteFile(): \t" + e.getMessage());
   // return null;
   // }
   // }
   // return target;
   // }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   // ================================================================================
   // Private Methods
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
      _proto.forceUnique(tstList);
      return tstList;
   }


   /**
    * Insert "test" after the "src" dir, capitalize the original filename, then insert "Test" in
    * front of the filename.
    * 
    * @param srcPath full path of source file
    * @return test file name that corresponds to source file
    * @throws IllegalArguementException if "src" dir not found or .java file not found
    */
   public String makeTestFilename(String srcPath)
   {
      // Guard against non-Java files
      if (!srcPath.endsWith(".java")) {
         throw new IllegalArgumentException(".java file not found in " + srcPath);
      }
      // Guard against missing "src" directory
      if (!srcPath.contains(Constants.FS + "src" + Constants.FS)) {
         throw new IllegalArgumentException("'src' directory required to create 'test' subdir");
      }

      StringBuilder sbTest = new StringBuilder(srcPath);
      int srcTextNdx = srcPath.lastIndexOf("src");
      sbTest.insert(srcTextNdx + 4, "test" + Constants.FS);
      int ndx = sbTest.lastIndexOf(Constants.FS);
      sbTest.insert(ndx + 1, "Test");
      String newName = sbTest.toString();
      return newName;
   }


   // /**
   // * Test directory is required to be directly beneath src root
   // *
   // * @param root the directory for all source files
   // * @return the test directory
   // */
   // private File makeTestPath(File root)
   // {
   // File testDir = null;
   // String dirPath = root.getAbsolutePath();
   // dirPath = dirPath.replace("src", "src" + Constants.FS + "test");
   // File dir = new File(dirPath);
   // if (dir.isDirectory()) {
   // testDir = dir;
   // }
   // return testDir;
   // }


   /** Write messages to console. outMsg checks the globl verbose flag */
   public void writeResults()
   {
      QAUtils.verboseMsg("Writing complete: ");
      QAUtils.verboseMsg("\t Files written: " + _filesWritten);
      QAUtils.verboseMsg("\t Files augmented: " + _filesAugmented);
      QAUtils.verboseMsg("\t Files unchanged: " + _filesUnchanged);

   }


   /** Write a new test file if it doesn't exist, else augment an existing test file
    * 
    * @param   target   test file to create or augment
    * @param   srcList source names to use in comments section of test methods
    * @param   tstList test names to add into test file
    */   
   public File writeTestFile(File target, ArrayList<String> srcList, ArrayList<String> tstList)
   {
      File written = null;
      if (!target.exists()) {
         written = _proto.writeNewTestFile(target, srcList, tstList);
      }
      else {
         written = _proto.augmentTestFile(target, srcList, tstList);
      }
      return written;
   }

   
   // /**
   // * Writes to an empty test class if it doesn't exist, or augment an existing test class with
   // new
   // * test methods
   // *
   // * @param target test file to write to
   // * @param srcList list of source file methods to mirror in the test file (if not already there)
   // * @return the test file written
   // * @throws ClassNotFoundException if test file cannot be compiled to get .class file
   // */
   // public File writeTestFile(File testTarget, ArrayList<String> srcList, ArrayList<String>
   // testList)
   // throws ClassNotFoundException
   // {
   // // _proto = new Prototype();
   //
   // long fileLen = testTarget.length();
   // String testPath = testTarget.getPath();
   // // ArrayList<String> convSrcList = convertToTestNames(srcList);
   //
   // if (fileLen == 0L) {
   // QAUtils.verboseMsg("\n\tCreating new test file " + testTarget);
   // QAUtils.outList("\tConverted source methods: ", testList);
   // _proto.writeNewTestFile(testTarget, srcList, testList);
   // // QAUtils.outList("\tConverted source methods: ", convSrcList);
   // // _proto.writeNewTestFile(testTarget, srcList, convSrcList);
   // _filesWritten++;
   // } else {
   // ArrayList<String> existingTestMethods = null;
   // // Find list of existing test methods
   // try {
   // existingTestMethods =
   // QAUtils.collectMethods(testPath, QAUtils.FileType.TEST);
   // } catch (IllegalArgumentException ex1) {
   // QAUtils.verboseMsg("Wrong file type. Source file expected");
   // return null;
   // } catch (ClassNotFoundException ex2) {
   // QAUtils.verboseMsg("Could not find .class file to compile: " + testPath);
   // throw ex2;
   // }
   //
   // if (QAFileScan._verbose) {
   // QAUtils.outList("\tExisting test file methods: ", existingTestMethods);
   // }
   // // Check if any methods are missing from the test list
   // ArrayList<String> augList =
   // (ArrayList<String>) compareLists(testList, existingTestMethods);
   // if (QAFileScan._verbose) {
   // if (augList.size() == 0) {
   // QAUtils.verboseMsg("\t-- No methods missing from test file -- ");
   // _filesUnchanged++;
   // } else {
   // QAUtils.outList("\tMethods missing from test file: ", augList);
   // }
   // }
   // // Update the existing test file
   // testTarget = _proto.augmentTestFile(testTarget, srcList, augList);
   // }
   // return testTarget;
   // }


   // ================================================================================
   // Private Methods
   // ================================================================================

//   /**
//    * Identifies each entry in a <code>srcList</code> that is not in a <code>testList</code>, and
//    * adds it to a third list, which is returned. Entries in the <code>testList</code> that are not
//    * in the <code>srcList</code> are ignored. Another way of saying this is <br>
//    * <code>
//    * if (!testList[k].contains(srcList[k]) {
//    *    augList[j] = srcList[k]; 
//    * }
//    * </code>
//    * 
//    * @param srcList contains entries to find and add to the augList
//    * @param testList contains the entries by which to filter the src list
//    * @return aguList which contains <code>srcList</code> elements not in the <code>testList</code>
//    */
//   private ArrayList<String> compareLists(ArrayList<String> srcList, ArrayList<String> testList)
//   {
//      ArrayList<String> augList = new ArrayList<String>();
//
//      // Search in authList for every entry in newList
//      int srcLen = srcList.size();
//      for (int s = 0; s < srcLen; s++) {
//         String name = srcList.get(s);
//         if (!testList.contains(name)) {
//            augList.add(name);
//         }
//      }
//      return augList;
//   }


   // ================================================================================
   // Private Methods
   // ================================================================================


   // /**
   // * Is the method name a standard JUnit test prep methods?
   // *
   // * @param name method name to test
   // * @return true if the name is one of the skippable names
   // */
   // private boolean isPrepMethod(String name)
   // {
   // boolean retval = false;
   // for (int k = 0; k < PREP_METHOD.length; k++) {
   // if (name.contains(PREP_METHOD[k])) {
   // retval = true;
   // break;
   // }
   // }
   // return retval;
   // }


//   /**
//    * Ensure that all subdirs in the long path exist
//    * 
//    * @param target long path of a file to be created
//    * @return the short file name
//    */
//   private String makeSubtree(File target)
//   {
//      // Remove filename from end of path
//      File subtree = target.getParentFile();
//      subtree.mkdirs();
//      return subtree.getName();
//   }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   /**
    * Convert the src method name to a test method name
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


//   /**
//    * Compile a file so that the latest class file is available
//    *
//    * @param filePath file to be compiled
//    */
//   private void updateTestFileClass(String filePath)
//   {
//      try {
//         Process pro1 = Runtime.getRuntime().exec("javac " + filePath);
//         pro1.waitFor();
//      } catch (Exception ex) {
//         System.err.println(ex.getMessage());
//      }
//   }


}  // end of TestWriter class
