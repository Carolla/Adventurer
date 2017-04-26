/**
 * Prototype.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */

package pdc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

import mylib.Constants;

/**
 * Write a new test file, or update an existing test file, from a <code>.java</code> source file,
 * matching source methods with test methods.
 * 
 * @author Alan Cline
 * @version Jan 29, 2016 // original <br>
 *          Dec 7, 2016 // revised per integration tests <br>
 */
public class Prototype
{
   private final String COMMA = ",";
   private final String SPACE = " ";
   private final String LEFT_PAREN = "(";
   private final String RIGHT_PAREN = ")";
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
               " * @version %1$tB %1$te, %1$tY // original <br>\n */\n" +
               "public class %2$s\n{";

   /** Class header comments, author, and version and definition */
   private final String AUGMENT_VERSION =
         " * %1$tB %1$te, %1$tY // autogen: QA Tool added missing test methods <br>";

   /** Standard setup and teardown methods */
   private final String PREP_DECLARE =
         "\t/** \n\t * @throws java.lang.Exception\n \t */ \n\t" +
               "%s\n\tpublic %svoid %s throws Exception\n\t{ }\n\n";

   /** BEGIN TESTS Banner */
   private final String DBL_HRULE =
         "// ===============================================================================";
   private final String BANNER =
         "//\t\t BEGIN TESTING";

   /**
    * Test method template: @Normal annotation, @Test annotation, declaration, MsgCtrl block private
    */
   private final String NORMAL_CMT = "\t/**\n \t * @NORMAL_TEST %s\n\t */";
   private final String TEST_ANNOT = "\n\t@Test\n";
   private final String M_DECLARATION = "\tpublic %s\n\t{\n";
   private final String MSGCTRL_BLOCK = "\t\tMsgCtrl.auditMsgsOn(true);\n" +
         "\t\tMsgCtrl.errorMsgsOn(true);\n" +
         "\t\tMsgCtrl.where(this);\n\n";
   private final String NOT_IMPLEMENTED_MSG =
         "\t\tMsgCtrl.errMsgln(\"\\t\\t TEST METHOD NOT YET IMPLEMENTED\");";
   private final String FAILSTUB_STATEMENT =
         "\t\tfail(\"TEST METHOD NOT YET IMPLEMENTED\");";

   // ======================================================================
   // CONSTRUCTOR
   // ======================================================================

   // Default constructor
   public Prototype()
   {}


   // ======================================================================
   // PUBLIC METHODS
   // ======================================================================

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
         target.delete();
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
      String pkgStatement = convertSourceToPackage(target);
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
      fileEcho("\n\n"); // give a little space in the audit trail
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


   // ======================================================================
   // PRIVATE HELPER METHODS
   // ======================================================================

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
    * Writes the setUp and tearDown methods at the method and class level
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
    * @param srcList signatures of source methods
    * @param tstList list of method declaration from which to derive test methods
    * @return list of test method code blocks for each test method
    */
   public ArrayList<String> buildTestMethods(ArrayList<String> srcList, ArrayList<String> tstList)
   {
      // List to return
      ArrayList<String> codeBlock = new ArrayList<String>();

      for (int k = 0; k < tstList.size(); k++) {
         StringBuilder comment = new StringBuilder();
         // CREATE THE NORMAL COMMENT BLOCK containing the source signature
         comment.append(String.format(NORMAL_CMT, srcList.get(k)));
         // Add the @Test annotation
         comment.append(TEST_ANNOT);

         // ADD THE TEST DECLARATIAON
         String m = tstList.get(k);
         String decl = String.format(M_DECLARATION, m);
         comment.append(decl);

         // ADD THE MSGCTRL BLOCK and Fail or Not Impl message
         comment.append(MSGCTRL_BLOCK);
         if (SingleFileScan._failStubs) {
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
         target.delete();
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
      String pkgStatement = convertSourceToPackage(target);
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
      fileEcho("\n\n"); // give a little space in the audit trail
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


   // ======================================================================
   // PRIVATE HELPER METHODS
   // ======================================================================

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
    * Writes the setUp and tearDown methods at the method and class level
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
    * Return the package statement for the given source file
    * 
    * @param target test file to write out
    * @return the package statement path
    */
   public String convertSourceToPackage(File target)
   {
      String s = target.getParentFile().getAbsolutePath();
      s = s.substring(s.lastIndexOf("src" + Constants.FS));
      s = s.substring(4); // remove the src/
      String pathName = s.replaceAll(Pattern.quote(Constants.FS), ".");
      String pkgStatement = String.format("\npackage %s;\n", pathName);

      return pkgStatement;
   }


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
      forceUnique(tstList);
      return tstList;
   }


   /**
    * Find all new src method names that need to be added to an existing test file. Duplicate
    * src-names-as-test names are removed from the existing src list if that name exists in the test
    * list. Both lists must contain unique names in sorted order.
    * 
    * @param existingSrcTestNames list of src methods converted to test names
    * @param testList list of test methods names in the existing test file
    * @return a list of only the existingSrcTestNames that does not already exist in the test file
    */
   public ArrayList<String> findAugList(ArrayList<String> existingSrcTestNames,
         ArrayList<String> testList)
   {
      ArrayList<String> augList = new ArrayList<String>();
      for (String s : existingSrcTestNames) {
         for (String t : testList) {
            int pos = s.compareTo(t);
            if (pos == 0) {
               continue;  // skip name in both lists (do not add anything into auglist)
            } else if (pos < 0) {
               augList.add(s);   // add src name does not exist in test list
            } else { // pos > 0
               ;     // do nothing with src list
            }
            continue;   // increment to next test list item
         }
      }

      return augList;
   }


    /**
    * Sort all method names and number any overloaded methods. The methods are not in any
    particular
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


    /**
    * Writes a prototype test template with JUnit test stubs and Chronos-specific data
    *
    * @param target test file to write into
    * @param srcList signatures of source method names used in test file comments
    * @param tstList test method names to write to the output file
    * @return the test file written
    */
    public File writeNewTestFile(File target, ArrayList<String> srcList, ArrayList<String>
    tstList)
    {
    // Create new output device
    PrintWriter out = null;
    try {
    // Ensure that the target file is created anew
    target.delete();
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
    String pkgStatement = convertSourceToPackage(target);
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
    fileEcho("\n\n"); // give a little space in the audit trail
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


   // ======================================================================
   // PRIVATE HELPER METHODS
   // ======================================================================

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
    * Writes the setUp and tearDown methods at the method and class level
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


   // /**
   // * Copy lines of a file until a given keyWord is reached.
   // *
   // * @param in file to read from
   // * @param out file to rite to
   // * @param keyWord tells input reader to stop copying
   // * @return current line of input containing keyword
   // */
   // private String copyUntil(Scanner in, PrintWriter out, String keyWord)
   // {
   // String line = null;
   // while (in.hasNextLine()) {
   // line = in.nextLine();
   // if (line.contains(keyWord)) {
   // break;
   // } else {
   // out.println(line);
   // fileEcho(line);
   // }
   // }
   // return line;
   // }


   // /**
   // * Pull the method name from the signature to compare uniqueness. Any numeric suffix will be
   // * ignored
   // *
   // * @param mName method signature
   // * @return bare name stripped of return value, parm list, and numeric suffix
   // */
   // private String extractNameOnly(String mName)
   // {
   // // Extract only the method name
   // int pNdx = mName.indexOf(LEFT_PAREN);
   // char c = (char) mName.charAt(pNdx - 1);
   // if (isDigit(c)) {
   // --pNdx;
   // }
   // int spaceNdx = mName.indexOf(SPACE);
   // String shortName = mName.substring(spaceNdx + 1, pNdx);
   // return shortName;
   // }


   // /**
   // * Pull the method name from the signature to compare uniqueness. Any numeric suffix will be
   // * ignored
   // *
   // * @param mName method signature
   // * @return bare name stripped of return value, parm list, and numeric suffix
   // */
   // private String extractNameOnly(String mName)
   // {
   // // Extract only the method name
   // int pNdx = mName.indexOf(LEFT_PAREN);
   // char c = (char) mName.charAt(pNdx - 1);
   // if (isDigit(c)) {
   // --pNdx;
   // }
   // int spaceNdx = mName.indexOf(SPACE);
   // String shortName = mName.substring(spaceNdx + 1, pNdx);
   // return shortName;
   // }


   // /**
   // * Find the closing brace at the end of the class so that new methods can be inserted.
   // *
   // * @param in scanner for reading original file
   // * @param out output file for receiving new version
   // * @return line containing the closing brace
   // */
   // private String findClassEnd(Scanner in, PrintWriter out)
   // {
   // int delimCnt = 0;
   // String line = null;
   //
   // while (in.hasNextLine()) {
   // line = in.nextLine();
   // if (line.contains(LEFT_BRACE)) {
   // delimCnt++;
   // }
   // if (line.contains(RIGHT_BRACE)) {
   // delimCnt--;
   // }
   // // If end of class reached
   // if ((line.contains(RIGHT_BRACE)) && (delimCnt == 0)) {
   // break;
   // } else {
   // out.println(line);
   // fileEcho(line);
   // }
   // }
   // // Return the line that contains the class closing brace.
   // return line;
   // }
   //

   // /**
   // * Determine if the given character is a digit from 1 to 9
   // *
   // * @param ch character to check
   // * @return true if c is a numeric digit
   // */
   // private boolean isDigit(char ch)
   // {
   // return (ch >= '1' && ch <= '9');
   // }


   // /**
   // * Number two sorted signatures, then give the second a higher numerical suffix than the first
   // *
   // * @param firstName method name for the first of a set of duplicates
   // * @param secondName method name for the second of a set of duplicates
   // * @return the numbered signatures first, then the new one, made unique, to add
   // */
   // private String[] numerateNames(String firstName, String secondName)
   // {
   // String[] mNames = new String[2];
   // StringBuilder sb1 = new StringBuilder(firstName);
   // StringBuilder sb2 = new StringBuilder(secondName);
   // // Get number of first name
   // int paren1 = firstName.indexOf(LEFT_PAREN);
   // int paren2 = secondName.indexOf(LEFT_PAREN);
   // char c1 = sb1.charAt(paren1 - 1);
   // char c2 = '-';
   // if (isDigit(c1)) {
   // c2 = (char) (c1 + 1);
   // sb2.insert(paren2, c2);
   // } else {
   // // bare name has no numeric suffix
   // sb1.insert(paren2, "1");
   // sb2.insert(paren2, "2");
   // }
   // mNames[0] = sb1.toString();
   // mNames[1] = sb2.toString();
   // return mNames;
   // }


   // /**
   // * Rename a file as a temp file
   // *
   // * @param targetFile the file to rename
   // * @return the file to rename from its tmp name
   // */
   // private File setAsTmpFile(File targetFile)
   // {
   // final String TMP_SUFFIX = ".tmp99";
   // String targetName = targetFile.getPath();
   // String tmpName = targetName.replace(".java", TMP_SUFFIX);
   // File tmpFile = new File(tmpName);
   // boolean fileSwap = targetFile.renameTo(tmpFile);
   // if (!fileSwap) {
   // System.err.println("Prototype.setAsTmpFile(): Error trying to rename input filename");
   // return null;
   // }
   // return tmpFile;
   // }

   //
   // /**
   // * Sort first by method name, then by parm list number and value
   // *
   // * @param sList collection of method signatures
   // */
   // private void sortSignatures(ArrayList<String> sList)
   // {
   // Collections.sort(sList, new Comparator<String>() {
   // @Override
   // public int compare(String sig1, String sig2)
   // {
   // // Tokenize into three parts: method name, parm list, return type
   // String name1 = sig1.substring(sig1.indexOf(SPACE) + 1, sig1.indexOf(LEFT_PAREN));
   // String name2 = sig2.substring(sig2.indexOf(SPACE) + 1, sig2.indexOf(LEFT_PAREN));
   // String parms1 = sig1.substring(sig1.indexOf(LEFT_PAREN), sig1.indexOf(RIGHT_PAREN) + 1);
   // String parms2 = sig2.substring(sig2.indexOf(LEFT_PAREN), sig2.indexOf(RIGHT_PAREN) + 1);
   // // System.err.println("\t\t sort loops = " + ++count);
   //
   // // Compare method names
   // int retval = name1.compareTo(name2); // compare method names
   // // Compare number of parms and parms names
   // if (retval == 0) {
   // String[] nbrParms1 = parms1.split(COMMA);
   // String[] nbrParms2 = parms2.split(COMMA);
   // retval = nbrParms1.length - nbrParms2.length;
   // if (retval == 0) {
   // retval = parms1.compareTo(parms2);
   // }
   // }
   // return retval;
   // }
   // });
   // }
   //

   // /**
   // * Writes the list of methods into file
   // *
   // * @param op PrintWriter or output to write code blocks into
   // * @param srcList source method signatures for test file comments
   // * @param tstList of all methods to write to the test file
   // */
   // private void writeCodeBlocks(PrintWriter op, ArrayList<String> srcList,
   // ArrayList<String> tstList)
   // {
   // if (tstList.size() == 0) {
   // return;
   // }
   // ArrayList<String> codeBlock = buildTestMethods(srcList, tstList);
   // int k = 0;
   // for (String s : codeBlock) {
   // op.println(s);
   // fileEcho(s);
   // QAUtils.verboseMsg(
   // "\tCode block written = " + tstList.get(k) + " -- " + s.length() + " bytes");
   // k++;
   // }
   // }


   // ======================================================================
   // INNER CLASS FOR TESTING
   // ======================================================================

   // ======================================================================
   // PRIVATE HELPER METHODS
   // ======================================================================

   // /**
   // * Write a banner just above the generated list of test methods
   // *
   // * @param out output file to write to
   // */
   // private void writeTestBanner(PrintWriter out)
   // {
   // out.println("\n" + DBL_HRULE);
   // out.println(BANNER);
   // out.println(DBL_HRULE + "\n");
   // }


   public class MockPrototype
   {
      public MockPrototype()
      {}

      // public void sortSignatures(ArrayList<String> myList)
      // {
      // Prototype.this.sortSignatures(myList);
      // }

   } // end of MockPrototype inner class


} // end of Prototype class
