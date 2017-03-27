/**
 * TestWriter.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */

package pdc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

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
   // private final String COMMA = ",";
   // private final String SPACE = " ";
   // private final String LEFT_PAREN = "(";
   // private final String RIGHT_PAREN = ")";
   private final String LEFT_BRACE = "{";
   private final String RIGHT_BRACE = "}";
   private final String END_CMT = " */";

   /** Set of JUnit import statements */
   private final String JUNIT_IMPORTS =
         "import static org.junit.Assert.*; \n" +
               "import org.junit.After; \n" +
               "import org.junit.AfterClass; \n" +
               "import org.junit.Before; \n" +
               "import org.junit.BeforeClass; \n" +
               "import org.junit.Test; \n\n" +
               "import mylib.MsgCtrl;\n";

   /** Class header comments, author, and version and definition */
   private final String AUTHOR_VERSION =
         "/** \n * @author --generated by QA Tool--\n" +
               " * @version %1$tB %1$te, %1$tY    // original <br>\n */\n" +
               "public class %2$s\n{";

   /** Class header comments, author, and version and definition */
   private final String AUGMENT_VERSION =
         " *          %1$tB %1$te, %1$tY    // autogen: QA Tool added missing test methods <br>";

   /** Standard setup and teardown methods */
   private final String PREP_DECLARE =
         "\t/** \n\t * @throws java.lang.Exception\n \t */ \n\t" +
               "%s\n\tpublic %svoid %s throws Exception\n\t{ }\n\n";

   /** BEGIN TESTS Banner */
   private final String DBL_HRULE =
         "// ===============================================================================";
   private final String BANNER =
         "//\t\t BEGIN TESTING";

   // private File _testRoot;
   private int _filesWritten;
   private int _filesAugmented;
   private int _filesUnchanged;

   // Debugging and audit flags
   private boolean _verbose;
   private boolean _failStubs;
   private boolean _fileEcho;

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
   public TestWriter(boolean verbose, boolean failStubs, boolean fileEcho)
   {
      _verbose = verbose;
      _failStubs = failStubs;
      _fileEcho = fileEcho;

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
    * Copy an existing test file, adding missing test methods from its corresponding source file.
    * This method renames the originalTestFile (input file) to a temp prefix and writes into an
    * outputfile original name. Later, the input file will be deleted and the new file returned.
    * 
    * @param originalTestFile existing test file to update
    * @param srcList signatures of source method names to be used in test file comments
    * @param augList new methods to write to the existing test file (output)
    * @return the test file written
    */
   public File augmentTestFile(File originalTestFile, ArrayList<String> srcList,
         ArrayList<String> augList)
   {
      // Guard: Verify that there are methods to add
      if (augList.size() == 0L) {
         return originalTestFile;
      }
      // Guard: Programmer error. Augment should not have been called without a source file
      // null return should cause a stacktrace dump
      if (srcList.size() == 0L) {
         return null;
      }

      // Rename original file to have temporary suffix. Don't use ".tmp". It is too common and may
      // collide elsewhere.
      String origFilename = originalTestFile.getPath();
      File inFile = setAsTmpFile(originalTestFile);
      File outFile = new File(origFilename);

      // Create new I/O devices to read and copy file
      Scanner in = null;
      PrintWriter out = null;
      try {
         in = new Scanner(inFile);
         out = new PrintWriter(outFile);
      } catch (FileNotFoundException e) {
         System.err.println("\tPrototype.augmentTestFile(): \t" + e.getMessage());
         return null;
      }

      // Add a new version line to the existing file and keep it
      addVersionLine(in, out, AUGMENT_VERSION);

      // Go to the end of class to insert new methods
      String line = findClassEnd(in, out);
      writeCodeBlocks(out, srcList, augList);
      // Now close class with end brace
      out.println(line);
      fileEcho(line);

      // Copy out the class-end and close the file
      in.close();
      out.close();

      // Delete the original file and return the augmented output file as the original filename
      inFile.delete();
      return outFile;
   }

   /**
    * Writes the setUp and tearDown methods at the method and class level
    * 
    * @return a long string containing the method block written
    */
   public String buildPrepMethods()
   {
      String staticStr = "static ";
      String[] arg = {"@BeforeClass", "setUpBeforeClass()", "@AfterClass", "tearDownAfterClass()",
            "@Before", "setUp()", "@After", "tearDown()"};

      StringBuilder block = new StringBuilder();
      for (int k = 0; k < arg.length; k = k + 2) {
         if (k > 2) {
            staticStr = ""; // only the class setup and teardown uses the static qualifier
         }
         block.append(String.format(PREP_DECLARE, arg[k], staticStr, arg[k + 1]));
      }
      // Before returning, turn off audit and errors messages in tearDown()
      // Insert the MsgCtrl statements before the closing brace;
      String msgFlags = "\n\t\tMsgCtrl.auditMsgsOn(false);\n\t\tMsgCtrl.errorMsgsOn(false);\n\t}\n";
      int ndx = block.lastIndexOf("}");
      block.replace(ndx, block.length(), msgFlags);

      return block.toString();
   }

   /**
    * Insert "test" after the "src" dir, capitalize the original filename, then insert "Test" in
    * front of the filename.
    * 
    * @param srcPath full path of source file
    * @return test file name that corresponds to source file
    * @throws IllegalArguementException if "src" dir not found or .java src file not found
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

   /**
    * Rename a file as a temp file
    * 
    * @param targetFile the file to rename
    * @return the file to rename from its tmp name
    */
   public File setAsTmpFile(File targetFile)
   {
      final String TMP_SUFFIX = ".tmp99";
      String targetName = targetFile.getPath();
      String tmpName = targetName.replace(".java", TMP_SUFFIX);
      File tmpFile = new File(tmpName);
      boolean fileSwap = targetFile.renameTo(tmpFile);
      if (!fileSwap) {
         System.err.println("Prototype.setAsTmpFile(): Error trying to rename input filename");
         return null;
      }
      return tmpFile;
   }


   /**
    * Writes a prototype test template with JUnit test stubs and Chronos-specific data
    * 
    * @param target test file to write into
    * @param srcList signatures of source method names used in test file comments
    * @param tstList test method names to write to the output file
    * @return the test file written
    */
   public File writeNewTestFile(File target, ArrayList<String> srcList, ArrayList<String> tstList)
   {
      // Create new output device
      PrintWriter out = null;
      try {
         // Ensure that the target file is created anew
         // target.delete();
         out = new PrintWriter(target);
      } catch (FileNotFoundException e) {
         System.err.println("\twriteFile(): \t" + e.getMessage());
         return null;
      }

      // 1. Write the copyright notice into the prototype
      int year = new GregorianCalendar().get(Calendar.YEAR);
      String copyright = String.format(Constants.COPYRIGHT, target.getName(), year);
      out.println(copyright);

      // 2. Write the package statements for this test class
      String pkgStatement = _proto.convertSourceToPackage(target);
      out.println(pkgStatement);

      // 3. Write the JUnit import statements
      out.println(JUNIT_IMPORTS);

      // 4. Write header comment, author, and version
      // Remove the .java extension from the filename
      String className = target.getName();
      int ndx = className.lastIndexOf(".");
      String name = className.substring(0, ndx);
      String version = String.format(AUTHOR_VERSION, new Date(), name);
      out.println(version);

      // 5. Write the four JUnit setup and teardown methods
      out.println(buildPrepMethods());

      // 6. Write the public methods beneath a public banner
      fileEcho("\n\n");   // give a little space in the audit trail
      writeTestBanner(out);
      writeCodeBlocks(out, srcList, tstList);

      // 7. Write the class closing brace
      out.println(String.format("} \t// end of %s class", target.getName()));
      out.flush();
      out.close();

      // Make sure that the .class file is created with the .java file, for later reflection
      QAUtils.compileFileClass(target.getPath(), QAUtils.FileType.TEST);

      return target;
   }


   /** Write messages to console. outMsg checks the globl verbose flag */
   public void writeResults()
   {
      QAUtils.verboseMsg("Writing complete: ");
      QAUtils.verboseMsg("\t Files written: " + _filesWritten);
      QAUtils.verboseMsg("\t Files augmented: " + _filesAugmented);
      QAUtils.verboseMsg("\t Files unchanged: " + _filesUnchanged);

   }


   // /** Write a new test file if it doesn't exist, else augment an existing test file
   // *
   // * @param target test file to create or augment
   // * @param srcList source names to use in comments section of test methods
   // * @param tstList test names to add into test file
   // */
   // public File writeTestFile(File target, ArrayList<String> srcList, ArrayList<String> tstList)
   // {
   // File written = writeNewTestFile(target, srcList, tstList);
   //// if (!target.exists()) {
   //// written = writeNewTestFile(target, srcList, tstList);
   //// }
   //// else {
   //// written = augmentTestFile(target, srcList, tstList);
   //// }
   // return written;
   // }


   /**
    * Add a new version after the last version line in the class comment block if it is not
    * "today's" date (which could cause many duplicate version lines during testing).
    * 
    * @param in scanner for reading original file
    * @param out output file for receiving new version
    * @param versionLine to insert in the class comment block
    * @return the version line being added, for subsequent date of file length change checks
    */
   private String addVersionLine(Scanner in, PrintWriter out, String versionLine)
   {
      String todaysVersion = String.format(versionLine, new Date());
      // Read until the first version line is found
      String currentLine = copyUntil(in, out, "@version");

      // Read through the version block until the last @version line and end cmt line has been read
      String lineAhead = in.nextLine();
      while (!lineAhead.equals(END_CMT)) {
         out.println(currentLine);
         fileEcho(currentLine);
         currentLine = lineAhead;
         lineAhead = in.nextLine();
      }
      // Current line now holds the latest version
      out.println(currentLine);
      fileEcho(currentLine);
      // If the latest version is not today's version, add it
      if (!currentLine.equals(todaysVersion)) {
         out.println(todaysVersion);
         fileEcho(todaysVersion);
      }
      // Write out the latest version and closing comment line
      out.println(lineAhead);
      fileEcho(lineAhead);
      return todaysVersion;
   }


   /**
    * Copy lines of a file until a given keyWord is reached.
    * 
    * @param in file to read from
    * @param out file to rite to
    * @param keyWord tells input reader to stop copying
    * @return current line of input containing keyword
    */
   private String copyUntil(Scanner in, PrintWriter out, String keyWord)
   {
      String line = null;
      while (in.hasNextLine()) {
         line = in.nextLine();
         if (line.contains(keyWord)) {
            break;
         } else {
            out.println(line);
            fileEcho(line);
         }
      }
      return line;
   }

   private void fileEcho(String msg)
   {
      if (SingleFileScan._fileEcho) {
         QAUtils.verboseMsg(msg);
      }
   }

   /**
    * Find the closing brace at the end of the class so that new methods can be inserted.
    * 
    * @param in scanner for reading original file
    * @param out output file for receiving new version
    * @return line containing the closing brace
    */
   private String findClassEnd(Scanner in, PrintWriter out)
   {
      int delimCnt = 0;
      String line = null;

      while (in.hasNextLine()) {
         line = in.nextLine();
         if (line.contains(LEFT_BRACE)) {
            delimCnt++;
         }
         if (line.contains(RIGHT_BRACE)) {
            delimCnt--;
         }
         // If end of class reached
         if ((line.contains(RIGHT_BRACE)) && (delimCnt == 0)) {
            break;
         } else {
            out.println(line);
            fileEcho(line);
         }
      }
      // Return the line that contains the class closing brace.
      return line;
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

   /**
    * Writes the list of methods into file
    * 
    * @param op PrintWriter or output to write code blocks into
    * @param srcList source method signatures for test file comments
    * @param tstList of all methods to write to the test file
    */
   private void writeCodeBlocks(PrintWriter op, ArrayList<String> srcList,
         ArrayList<String> tstList)
   {
      if (tstList.size() == 0) {
         return;
      }
      ArrayList<String> codeBlock = _proto.buildTestMethods(srcList, tstList);
      int k = 0;
      for (String s : codeBlock) {
         op.println(s);
         fileEcho(s);
         QAUtils.verboseMsg(
               "\tCode block written = " + tstList.get(k) + " -- " + s.length() + " bytes");
         k++;
      }
   }


   /**
    * Write a banner just above the generated list of test methods
    * 
    * @param out output file to write to
    */
   private void writeTestBanner(PrintWriter out)
   {
      out.println("\n" + DBL_HRULE);
      out.println(BANNER);
      out.println(DBL_HRULE + "\n");
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

   // /**
   // * Identifies each entry in a <code>srcList</code> that is not in a <code>testList</code>, and
   // * adds it to a third list, which is returned. Entries in the <code>testList</code> that are
   // not
   // * in the <code>srcList</code> are ignored. Another way of saying this is <br>
   // * <code>
   // * if (!testList[k].contains(srcList[k]) {
   // * augList[j] = srcList[k];
   // * }
   // * </code>
   // *
   // * @param srcList contains entries to find and add to the augList
   // * @param testList contains the entries by which to filter the src list
   // * @return aguList which contains <code>srcList</code> elements not in the
   // <code>testList</code>
   // */
   // private ArrayList<String> compareLists(ArrayList<String> srcList, ArrayList<String> testList)
   // {
   // ArrayList<String> augList = new ArrayList<String>();
   //
   // // Search in authList for every entry in newList
   // int srcLen = srcList.size();
   // for (int s = 0; s < srcLen; s++) {
   // String name = srcList.get(s);
   // if (!testList.contains(name)) {
   // augList.add(name);
   // }
   // }
   // return augList;
   // }


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


   // /**
   // * Ensure that all subdirs in the long path exist
   // *
   // * @param target long path of a file to be created
   // * @return the short file name
   // */
   // private String makeSubtree(File target)
   // {
   // // Remove filename from end of path
   // File subtree = target.getParentFile();
   // subtree.mkdirs();
   // return subtree.getName();
   // }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   // /**
   // * Convert the src method name to a test method name
   // *
   // * @param srcMethodName name of the method to convert to a test method signature
   // * @return the test method signature
   // */
   // private String makeTestMethodName(String srcName)
   // {
   // StringBuilder sb = new StringBuilder();
   // int endNdx = srcName.indexOf("(");
   // int startNdx = srcName.indexOf(" ");
   // sb.append(srcName.substring(startNdx + 1, endNdx));
   // // Uppercase the first letter of the method name for the decl
   // String ch = sb.substring(0, 1);
   // sb.replace(0, 1, ch.toUpperCase());
   // // Add the test prefix
   // sb.insert(0, "void test");
   // sb.append("()");
   // return sb.toString();
   // }


   // /**
   // * Compile a file so that the latest class file is available
   // *
   // * @param filePath file to be compiled
   // */
   // private void updateTestFileClass(String filePath)
   // {
   // try {
   // Process pro1 = Runtime.getRuntime().exec("javac " + filePath);
   // pro1.waitFor();
   // } catch (Exception ex) {
   // System.err.println(ex.getMessage());
   // }
   // }


}  // end of TestWriter class
