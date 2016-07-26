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

import mylib.Constants;
import pdc.QAUtils.FileType;

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
   private boolean _verbose;
   
   private Prototype _proto;

   // Skip over testing prep methods
   static final String[] PREP_METHOD =
         {"setUpBeforeClass", "tearDownAfterClass", "setUp", "tearDown"};


   // ================================================================================
   // CONSTRUCTOR and HELPERS
   // ================================================================================
 
   public TestWriter(File srcRoot, boolean verbose)
   {
      _testRoot = makeTestPath(srcRoot);
      _verbose = verbose;
      
      _filesWritten = 0;
      _filesAugmented = 0;
      _filesUnchanged = 0;
   }


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
    * Test directory is required to be directly beneath src root
    * 
    * @param root the directory for all source files
    * @return the test directory
    */
   public File makeTestPath(File root)
   {
      File testDir = null;
      String dirPath = root.getAbsolutePath();
      dirPath = dirPath.replace("src", "src" + Constants.FS + "test");
      File dir = new File(dirPath);
      if (dir.isDirectory()) {
         testDir = dir;
      }
      return testDir;

//      File testDir = null;
//      String dirPath = root.getAbsolutePath() + Constants.FS + "test";
//      File dir = new File(dirPath);
//      if (dir.isDirectory()) {
//         testDir = dir;
//      }
//      return testDir;
   }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   public void writeResults()
   {
      QAUtils.outMsg(_verbose, "Writing complete: ");
      QAUtils.outMsg(_verbose, "\t Files written: " + _filesWritten);
      QAUtils.outMsg(_verbose, "\t Files augmented: " + _filesAugmented);
      QAUtils.outMsg(_verbose, "\t Files unchanged: " + _filesUnchanged);
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
      _proto = new Prototype();
      
      long fileLen = testTarget.length();
      ArrayList<String> convSrcList = convertToTestNames(srcList);
      QAUtils.outList(_verbose, "\tConverted source methods: ", convSrcList);

      QAUtils.outMsg(_verbose, "\t" + testTarget + " contains " + fileLen + " characters");
      if (fileLen == 0L) {
         _proto.writeNewTestFile(testTarget, srcList, convSrcList);
         _filesWritten++;
      } else {
         ArrayList<String> testFileList = collectTestMethods(testTarget.getPath());
         QAUtils.outList(_verbose, "\tTest file methods: ", testFileList);
         ArrayList<String> augList = (ArrayList<String>) compareLists(convSrcList, testFileList);
         QAUtils.outList(_verbose, "\tMethods missing from test file: ", augList);
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

      ArrayList<String> testMethods = QAUtils.collectMethods(filePath, FileType.TEST);
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
