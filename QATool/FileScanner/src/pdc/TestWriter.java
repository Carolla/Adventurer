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
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import mylib.Constants;

/**
 * Traverses corresponding test root tree to match files and methods with source files provided by
 * the {@code SrcReader}.
 * 
 * @author Alan Cline
 * @version Jul 5, 2016 // original <br>
 *          Aprl 15, 2016 // refactored for simplification <br>
 */
public class TestWriter
{
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
         "\t/** \n\t * @throws java.lang.Exception -- "
               + "general catchall for exceptions not caught by the tests\n \t */ \n\t"
               + "%s\n\tpublic %svoid %s throws Exception\n\t{ }\n\n";

   /** BEGIN TESTS Banner */
   private final String DBL_HRULE =
         "// ===============================================================================";
   private final String BANNER =
         "//\t\t BEGIN TESTING";

   /**
    * Test method template: @Normal annotation, @Test annotation, declaration, MsgCtrl block private
    */
   private final String NIMPL_CMT = "\t/**\n \t * Not Implemented %s\n\t */";
   private final String NORMAL_CMT = "\t/**\n \t * Normal Test: %s\n\t */";
   private final String TEST_ANNOT = "\n\t@Test\n";
   private final String M_DECLARATION = "\tpublic %s\n\t{\n";
   private final String MSGCTRL_BLOCK = "\t\tMsgCtrl.auditMsgsOn(false);\n" +
         "\t\tMsgCtrl.errorMsgsOn(false);\n" +
         "\t\tMsgCtrl.where(this);\n\n";
   private final String NOT_IMPLEMENTED_MSG =
         "\t\tMsgCtrl.errMsgln(\"\\t\\t TEST METHOD NOT YET IMPLEMENTED\");";
   private final String FAILSTUB_STATEMENT =
         "\t\tfail(\"TEST METHOD NOT YET IMPLEMENTED\");";

   // Debugging and audit flags
   private boolean _failStubs;
   private boolean _fileEcho;


   // ================================================================================
   // CONSTRUCTOR and HELPERS
   // ================================================================================

   /**
    * Create a writer to create or augment test cases
    * 
    * @param failStubs if true, writes test stubs that fail instead of ones that write warning
    *           message
    * @param fileEcho copies to the console all test file writes
    */
   public TestWriter(boolean failStubs, boolean fileEcho)
   {
      _failStubs = failStubs;
      _fileEcho = fileEcho;
   }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   /**
    * Copy an existing test file, adding missing test methods from the corresponding source file.
    * This method renames the originalTestFile (input file) to a temp prefix and writes into an
    * outputfile original name. Later, the temp file is deleted and the new file returned.
    * 
    * @param originalTestFile existing test file to update
    * @param augMap a map of src signature (for comments) and test file names to write to the test
    *           file
    * @return the test file written
    * @throws IllegalArgumentException if original file cannot be found
    */
   public File augmentTestFile(File originalTestFile, Map<String, String> augMap)
         throws IllegalArgumentException
   {
      // Guard: Verify no null parms
      if ((originalTestFile == null) || (augMap == null)) {
         throw new NullPointerException("Null parms not permitted");
      }
      // Guard: Verify that the file exists
      if (!originalTestFile.exists()) {
         throw new IllegalArgumentException("Can't find original test file");
      }
      // Guard: Verify that there are methods to add
      if (augMap.size() == 0L) {
         return originalTestFile;
      }

      /*
       * Rename original file to have temporary suffix. Don't use ".tmp". It is too common and may
       * collide elsewhere.
       */
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
      writeCodeBlocks(out, augMap);
      // Now close class with end brace
      fileWrite(out, line);

      // Copy out the class-end and close the file
      in.close();
      out.close();

      // Delete the original file and return the augmented output file as the original filename
      inFile.delete();
      return outFile;
   }


   /**
    * Insert "test" after the "src" dir, capitalize the original filename, then insert "Test" in
    * front of the filename.
    * 
    * @param srcPath full path of source file
    * @return test file name that corresponds to source file
    * @throws IllegalArgumentException if "src" dir not found or {@code .java} src file not found
    */
   public String makeTestFilename(String srcPath)
   {
      // Guard against null parm
      if (srcPath == null) {
         throw new IllegalArgumentException("Null srcPath found");
      }
      // Guard against non-Java files
      if (!srcPath.endsWith(".java")) {
         throw new IllegalArgumentException(".java file not found in " + srcPath);
      }
      // Guard against missing "src" directory
      if (!srcPath.contains(Constants.FS + "src" + Constants.FS)) {
         throw new IllegalArgumentException("source file must be in 'src' directory");
      }

      StringBuilder sbTest = new StringBuilder(srcPath);
      int srcTextNdx = srcPath.lastIndexOf("src");
      sbTest.insert(srcTextNdx + 4, "test" + Constants.FS);
      int ndx = sbTest.lastIndexOf(Constants.FS);
      sbTest.insert(ndx + 1, "Test");
      String newName = sbTest.toString();
      return newName;
   }


   /**
    * Create a new JUnit test file with test stubs
    * 
    * @param target destination of test file to write into
    * @param augMap src method sigatures with corresponding test names
    * @return the test file written
    * @throws IllegalArgumentException if target file already exists
    */
   public File writeNewTestFile(File target, Map<String, String> augMap)
         throws IllegalArgumentException
   {
      // Guard: File cannot exist already
      if (target.exists()) {
         throw new IllegalArgumentException("Cannot create a new file; it already exists");
      }
      // Guard: augMap must contains some methods to write
      if (augMap.size() == 0) {
         return target;
      }

      // Create new output device
      PrintWriter out = null;
      try {
         // Ensure that the target file is created anew
         out = new PrintWriter(target);
      } catch (FileNotFoundException e) {
         System.err.println("\twriteFile(): \t" + e.getMessage());
         return null;
      }

      // 1. Write the copyright notice into the prototype
      int year = new GregorianCalendar().get(Calendar.YEAR);
      String copyright = String.format(Constants.COPYRIGHT, target.getName(), year);
      fileWrite(out, copyright);

      // 2. Write the package statements for this test class
      String pkgStatement = convertSourceToPackage(target);
      fileWrite(out, pkgStatement);

      // 3. Write the JUnit import statements
      fileWrite(out, JUNIT_IMPORTS);

      // 4. Write header comment, author, and version
      // Remove the .java extension from the filename
      String className = target.getName();
      int ndx = className.lastIndexOf(".");
      String name = className.substring(0, ndx);
      String version = String.format(AUTHOR_VERSION, new Date(), name);
      fileWrite(out, version);

      // 5. Write the four JUnit setup and teardown methods
      fileWrite(out, buildPrepMethods());

      // 6. Write the public methods beneath a public banner
      writeTestBanner(out);
      writeCodeBlocks(out, augMap);

      // 7. Write the class closing brace
      fileWrite(out, String.format("} \t// end of %s class", target.getName()));
      out.flush();
      out.close();

      return target;
   }


   // ================================================================================
   // PRIVATE METHODS
   // ================================================================================

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
      String lineAhead = in.nextLine().trim();
      while (!lineAhead.equals(Constants.END_CMT)) {
         fileWrite(out, currentLine);
         currentLine = lineAhead;
         lineAhead = in.nextLine();
      }
      // Current line now holds the latest version; get the last version date
      int cmtStart = currentLine.indexOf("//");
      String latestDate = currentLine.substring(12, cmtStart);
      fileWrite(out, currentLine);

      // If the latest version is not today's version, add it
      if (!todaysVersion.contains(latestDate)) {
         fileWrite(out, todaysVersion);
      }
      // Write out the latest version and closing comment line
      fileWrite(out, lineAhead);
      return todaysVersion;
   }


   /**
    * Writes the {@code setUp} and {@code tearDown} methods at the method and class level
    * 
    * @return a long string containing the method block written
    */
   private String buildPrepMethods()
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
    * Write test methods for each of the source methods, with the source methods signature in a
    * preceding comment.
    * 
    * @param augMap a mapping for src names with corresponding test method names
    * @return list of test method code blocks for each test method
    */
   private ArrayList<String> buildTestMethods(Map<String, String> augMap)
   {
      // List to return
      ArrayList<String> codeBlock = new ArrayList<String>();

      // Walk the map and extract the src name (key) and the test name (value) as needed
      for (Map.Entry<String, String> entry : augMap.entrySet()) {
         StringBuilder comment = new StringBuilder();
         // CREATE THE NORMAL COMMENT BLOCK containing the source signature
         // comment.append(String.format(NORMAL_CMT, entry.getKey()));
         comment.append(String.format(NIMPL_CMT, entry.getKey()));
         // Add the @Test annotation
         comment.append(TEST_ANNOT);

         // ADD THE TEST DECLARATIAON
         String m = entry.getValue();
         String decl = String.format(M_DECLARATION, m);
         comment.append(decl);

         // ADD THE MSGCTRL BLOCK and Fail or Not Impl message
         comment.append(MSGCTRL_BLOCK);
         if (_failStubs) {
            comment.append(FAILSTUB_STATEMENT + "\n\t}\n\n");
         } else {
            comment.append(NOT_IMPLEMENTED_MSG + "\n\t}\n\n");
         }

         // WRITE OUT THE TEST METHOD
         codeBlock.add(comment.toString());
      }
      return codeBlock;
   }


   /**
    * Creates a package statement for the given source file
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
            fileWrite(out, line);
         }
      }
      return line;
   }


   /**
    * Write a line of text to the output file
    * 
    * @param out the output file
    * @param msg the test to write
    */
   private void fileWrite(PrintWriter out, String msg)
   {
      out.println(msg);
      if (_fileEcho) {
         System.out.println(msg);
      }
   }


   /**
    * Find the closing brace at the end of the class so that new methods can be inserted before it,
    * within the class.
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
         if (line.contains(Constants.LEFT_BRACE)) {
            delimCnt++;
         }
         if (line.contains(Constants.RIGHT_BRACE)) {
            delimCnt--;
         }
         // If end of class reached
         if ((line.contains(Constants.RIGHT_BRACE)) && (delimCnt == 0)) {
            break;
         } else {
            fileWrite(out, line);
         }
      }
      // Return the line that contains the class closing brace.
      return line;
   }


   /**
    * Rename a file as a temp file
    * 
    * @param targetFile the file to rename
    * @return the file to rename from its tmp name
    */
   private File setAsTmpFile(File targetFile)
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
    * Builds test methods and writes them to the output file
    * 
    * @param op PrintWriter or output to write code blocks into
    * @param augMap mapping of src name signatures to corresponding test name
    */
   private void writeCodeBlocks(PrintWriter op, Map<String, String> augMap)
   {
      if (augMap.size() == 0) {
         return;
      }
      ArrayList<String> codeBlock = buildTestMethods(augMap);
      for (String s : codeBlock) {
         fileWrite(op, s);
      }
   }


   /**
    * Write a banner just above the generated list of test methods
    * 
    * @param out output file to write to
    */
   private void writeTestBanner(PrintWriter out)
   {
      fileWrite(out, "\n" + DBL_HRULE);
      fileWrite(out, BANNER);
      fileWrite(out, DBL_HRULE + "\n");
   }


}  // end of TestWriter class
