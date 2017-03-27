
/**
 * SrcReader.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */

package pdc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import mylib.Constants;

/**
 * Traverses the source tree, providing source files to compare with test files from the
 * {@code TestWriter}.
 * 
 * @author Alan Cline
 * @version Jul 5, 2016 // original <br>
 *          Dec 21, 2016 // refactored QAScanner away; added revisiosn for testing <br>
 */
public class SrcReader
{
   private final String COMMA = ",";
   private final String SPACE = " ";
   private final String LEFT_PAREN = "(";
   private final String RIGHT_PAREN = ")";

   // Path of the tree folder being scanned, used for finding relative paths
   private String _rootPath;
   private TestWriter _testWriter;

   private int _dirsScanned;
   private int _filesScanned;
   private int _dirsSkipped;
   private int _filesSkipped;

   // Exclusions lists
   ArrayList<String> _excDirs = null;
   ArrayList<String> _excFiles = null;


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
//   public SrcReader(File srcRoot, File excFile, TestWriter testWriter)
   public SrcReader(File srcRoot, File excFile)
   {
//      _testWriter = testWriter;

      // Parse the exclusion file and save the designated exclusion files and directories
      if (excFile != null) {
         setExclusions(excFile, srcRoot);
      }
      _rootPath = srcRoot.getPath();

      // Collections of files and directories and their scanning results
      _dirsScanned = 0;
      _filesScanned = 0;
      _dirsSkipped = 0;
      _filesSkipped = 0;
   }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   // /**
   // * Return the class method signature without package context or throws clauses, but with its
   // * return type, formatted as: <br>
   // * {@code methodName(argType, argType) : returnType} <br>
   // * where each of the Types are their simple names.
   // *
   // * @param m the Method object to get full path and properties returned by Class.getMethod()
   // * @param anchorName simple name of the class under reflection
   // * @return the method signature, e.g. as is used in the test method comment
   // */
   // public String extractSignature(Method m, String anchorName)
   // {
   // String s = m.toString();
   // // Skip any method names that do not have the anchorName in it (synthetic classes) and a
   // // 'main'
   // if ((!s.contains(anchorName)) || (s.contains("main("))) {
   // return null;
   // }
   // // Remove any throws clauses
   // if (s.contains("throws")) {
   // s = s.substring(0, s.indexOf("throws"));
   // }
   //
   // // Remove the modifer
   // s = s.substring(s.indexOf(SPACE) + 1);
   // String retType = simplifyReturnType(s);
   // String methodDecl = simplifyDeclaration(s);
   // return (retType + " " + methodDecl);
   // }

   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   
   /**
    * Get the methods in the target file if it exists
    * 
    * @param target source or test file from which to retreive internal method names
    * @param ft indicates whether a source file or a test file
    * @return all extracted methods of the source file
    */
   public ArrayList<String> getMethodList(File target, QAUtils.FileType ft)
   {
      ArrayList<String> mList = new ArrayList<String>();
      try {
         if (ft == QAUtils.FileType.SOURCE) {
            mList = srcFileScan(target);
         } else {
            mList = testFileScan(target);
         }
      } catch (ClassNotFoundException exSrc) {
         QAUtils.verboseMsg(".class file not found for file " + target);
         System.exit(-2);
      }
      return mList;
   }


   // /**
   // * Reduce a fully qualified class name to it simplified name by removing the dot-delimited full
   // * name to yield the suffix, the simple name. This is used for return types and argument types
   // * that occur in the method declaration.<br>
   // * The method declaration has format, where each type is a fully qualified type: <br>
   // * {@code return-type methodName(argType, argType,...) <br>
   // * For example, {@code java.lang.String extractSignature(java.io.File, java.lang.String)}
   // becomes
   // * {@code String extractSignature(File, String)}.<br>
   // * Note: The ellipsis in the signature example refers to a fixed but indefinite number of
   // * arguments, not to a varargs set.
   // *
   // * @param decl the fully-qualified method declaration
   // * @return the method name and simple argname list
   // */
   // public String simplifyDeclaration(String decl)
   // {
   // // Discard the the return type
   // decl = decl.trim();
   // int rtNdx = decl.indexOf(SPACE);
   // decl = decl.substring(rtNdx + 1);
   //
   // // Setup buffers to allow characer movement
   // StringBuilder sbIn = new StringBuilder(decl);
   // StringBuilder sbOut = new StringBuilder();
   //
   // // To simplify arguments, walk backwards from the right paren, removing prefixes
   // boolean skip = false;
   // int in = sbIn.length() - 1;
   // for (; in >= 0; in--) {
   // char ch = sbIn.charAt(in);
   // // Add space character to follow each comma
   // if (ch == ',') {
   // sbOut.insert(0, SPACE); // new char is placed in front of existing chars
   // skip = false;
   // } else if (ch == '(') {
   // skip = false;
   // }
   // // Skip all characters between previous comma or left paren and the dot
   // else if ((ch == '.') || (skip == true)) {
   // skip = true;
   // continue;
   // }
   // sbOut.insert(0, ch);
   // // System.out.println(String.format("\tCharacter written: %c", sbOut.charAt(0)));
   // }
   // String result = sbOut.toString().trim();
   // return result;
   // }
   
   
   // /**
   // * Convert the fully qualifed return type of a signature into its simple type. Also removes the
   // * method modifier (public, private, static, protected).
   // *
   // * @param decl fully qualifed method signature, with parm types and return type
   // * @return only the simple return type
   // */
   // public String simplifyReturnType(String decl)
   // {
   // // Remove trailing and leading white space then make a destination String
   // decl = decl.trim();
   // String dest = new String(decl);
   //
   // int retNdx = decl.indexOf(SPACE); // return type
   // String retSig = decl.substring(0, retNdx);
   // int lastDot = retSig.lastIndexOf(DOT);
   // dest = decl.substring(lastDot + 1, retNdx);
   //
   // return dest;
   // }
   
   
   // ================================================================================
   // PRIVATE METHODS
   // ================================================================================
   
   /**
    * Checks if file is a non-excluded java source file or directory
    * 
    * @param f file under examination
    * @return true if file is a valid source file
    */
   public boolean isValidFile(File f)
   {
      boolean retval = false;
      String s = f.getPath();
   
      // Guard: Skip non-files
      if (!f.isFile()) {
         return false;
      }
      // Audit trail for files in the exclusion file (if there is one)
      if ((_excFiles != null) && (_excFiles.contains(s))) {
         retval = false;
      }
      // Count and audit trail for java files
      if (s.endsWith(".java")) {
         retval = true;
      }
      // Count and audit trail for non-java files
      else {
         QAUtils.verboseMsg("Skipping ineligible file: " + s);
         retval = false;
      }
      return retval;
   }


   /**
       * Traverse the source tree recursively, skipping over files in the HIC or non-java files.
       * Directories encountered drops to subdirectory recursively.
       * 
       * @param srcRoot starting directory for source files
       * @return source file that meets all criteria
       */
      public void scan(File srcRoot)
      {
         // Retrieve all files and subdirs under dir
         File[] allFiles = srcRoot.listFiles();
         for (File f : allFiles) {
            QAUtils.verboseMsg("\n\t" + f.getName());
            // Validate directories for scanning
            if (isValidDirectory(f)) {
               scan(f);
               _dirsScanned++;
            } else if (f.isDirectory()) {
               _dirsSkipped++;
            }
            // Validate files for scanning
            if (isValidFile(f)) {
               QAUtils.verboseMsg("\tScanning file " + f.getName());
               try {
                  ArrayList<String> srcList = srcFileScan(f);
                  _filesScanned++;
                  // For each source file scanned, write to its test file
   //               writeTestFile(f.getPath(), srcList);
               } catch (ClassNotFoundException ex) {
                  _filesSkipped++;
               }
            } else if (f.isFile()) {
               _filesSkipped++;
            }
         }
      }


   /** Display a report of the directories and files scanned or skipped */
   public void scanResults()
   {
      QAUtils.verboseMsg("Scanning complete: ");
      QAUtils.verboseMsg("\t Directories scanned: " + _dirsScanned);
      QAUtils.verboseMsg("\t Files scanned: " + _filesScanned);
      QAUtils.verboseMsg("\t Directories skipped: " + _dirsSkipped);
      QAUtils.verboseMsg("\t Files skipped: " + _filesSkipped);
   }


   /**
    * Scans a Java source file and returns a list of its methods
    * 
    * @param f File to examine for methods
    * @return a list of the methods found in the file
    * @throws ClassNotFoundExceptio if .class file not found
    */
   public ArrayList<String> srcFileScan(File f) 
         throws ClassNotFoundException
   {
      // Guard against missing file
      if ((f == null) || (!f.exists())) {
         return null;
      }
      ArrayList<String> srcList = new ArrayList<String>();
      String srcPath = f.getPath();
      try {
         srcList = QAUtils.collectSrcMethods(srcPath);
      } catch (IllegalArgumentException ex1) {
         QAUtils.verboseMsg("Wrong file type. Source file expected");
         return null;
      } catch (ClassNotFoundException ex2) {
         QAUtils.verboseMsg("Could not find .class file to compile: " + srcPath);
         throw ex2;
      }
      if (SingleFileScan._verbose) {
         if (srcList.size() != 0) {
            QAUtils.outList(null, srcList);
         } else {
            QAUtils.verboseMsg("\tNo eligible methods.");
         }
      }
      return srcList;
   }

   /**
    * Scans a JUnit test file and returns a list of its methods
    * 
    * @param f File to examine for methods
    * @return a list of the methods found in the file
    * @throws ClassNotFoundException if .class file not found
    */
   public ArrayList<String> testFileScan(File f) 
         throws ClassNotFoundException
   {
      // Guard against missing file
      if ((f == null) || (!f.exists())) {
         return null;
      }
      ArrayList<String> srcList = new ArrayList<String>();
      String srcPath = f.getPath();
      try {
         srcList = QAUtils.collectTestMethods(srcPath);
//      } 
//      catch (IllegalArgumentException ex1) {
//         QAUtils.verboseMsg("Wrong file type. Source file expected");
//         return null;
      } catch (ClassNotFoundException ex2) {
         QAUtils.verboseMsg("Could not find .class file to compile: " + srcPath);
         throw ex2;
      }
      if (SingleFileScan._verbose) {
         if (srcList.size() != 0) {
            QAUtils.outList(null, srcList);
         } else {
            QAUtils.verboseMsg("\tNo eligible methods.");
         }
      }
      return srcList;
   }


   /**
    * Shorten a long pathname by replacing the root path name with a tilde, Unix style. For example,
    * for a root path of {@code /Projects/eChronos/QATool/src}, and long path of
    * {@code /Projects/eChronos/QATool/src/pdc/SrcReader.java} will be shortened to
    * {@code ~/pdc/SrcReader.java}.
    * 
    * @param longPath the absolute path of a file or directory
    * @return the shortened relative path name
    */
   private String getRelative(String longPath)
   {
      // Split the filename from the root path
      int ndx = longPath.lastIndexOf(Constants.FS);
      String s = longPath.substring(ndx+1);
      return s;
   }


   // /**
   // * Reduce a fully qualified class name to it simplified name by removing the dot-delimited full
   // * name to yield the suffix, the simple name. This is used for return types and argument types
   // * that occur in the method declaration.<br>
   // * The method declaration has format, where each type is a fully qualified type: <br>
   // * {@code return-type methodName(argType, argType,...) <br>
   // * For example, {@code java.lang.String extractSignature(java.io.File, java.lang.String)}
   // becomes
   // * {@code String extractSignature(File, String)}.<br>
   // * Note: The ellipsis in the signature example refers to a fixed but indefinite number of
   // * arguments, not to a varargs set.
   // *
   // * @param decl the fully-qualified method declaration
   // * @return the method name and simple argname list
   // */
   // public String simplifyDeclaration(String decl)
   // {
   // // Discard the the return type
   // decl = decl.trim();
   // int rtNdx = decl.indexOf(SPACE);
   // decl = decl.substring(rtNdx + 1);
   //
   // // Setup buffers to allow characer movement
   // StringBuilder sbIn = new StringBuilder(decl);
   // StringBuilder sbOut = new StringBuilder();
   //
   // // To simplify arguments, walk backwards from the right paren, removing prefixes
   // boolean skip = false;
   // int in = sbIn.length() - 1;
   // for (; in >= 0; in--) {
   // char ch = sbIn.charAt(in);
   // // Add space character to follow each comma
   // if (ch == ',') {
   // sbOut.insert(0, SPACE); // new char is placed in front of existing chars
   // skip = false;
   // } else if (ch == '(') {
   // skip = false;
   // }
   // // Skip all characters between previous comma or left paren and the dot
   // else if ((ch == '.') || (skip == true)) {
   // skip = true;
   // continue;
   // }
   // sbOut.insert(0, ch);
   // // System.out.println(String.format("\tCharacter written: %c", sbOut.charAt(0)));
   // }
   // String result = sbOut.toString().trim();
   // return result;
   // }


   // /**
   // * Convert the fully qualifed return type of a signature into its simple type. Also removes the
   // * method modifier (public, private, static, protected).
   // *
   // * @param decl fully qualifed method signature, with parm types and return type
   // * @return only the simple return type
   // */
   // public String simplifyReturnType(String decl)
   // {
   // // Remove trailing and leading white space then make a destination String
   // decl = decl.trim();
   // String dest = new String(decl);
   //
   // int retNdx = decl.indexOf(SPACE); // return type
   // String retSig = decl.substring(0, retNdx);
   // int lastDot = retSig.lastIndexOf(DOT);
   // dest = decl.substring(lastDot + 1, retNdx);
   //
   // return dest;
   // }


   // ================================================================================
   // PRIVATE METHODS
   // ================================================================================

   /**
    * Checks if file is a non-excluded directory
    * 
    * @param f file under examination
    * @return true if file is a non-excluded directory
    */
   private boolean isValidDirectory(File f)
   {
      // Guard: Skip non-directory files
      if (!f.isDirectory()) {
         return false;
      }
      // Guard: do not step into, or count, the "test" subdir
      String s = f.getPath();
      String relName = getRelative(s);
      // if (s.contains("src" + Constants.FS + "test")) {
      if (s.contains(Constants.FS + "test")) {
         QAUtils.verboseMsg("Skipping test directory: " + relName);
         return false;
      }
      // Scan all directories except those in the optional exclude directory
      boolean retval = false;

      // Audit trail for directories in the exclusion file (if there is one)
      if ((_excDirs != null) && (_excDirs.contains(s))) {
         // QAUtils.verboseMsg("Skipping excluded directory: " + relName);
         // _dirsSkipped++;
         QAUtils.verboseMsg("Skipping excluded directory: " + relName);
         retval = false;
      }
      // Count and audit trail for non-java files
      else {
         // QAUtils.verboseMsg("\tDirectory " + relName + ":");
         // _dirsScanned.add(relName);
         // _dirsScanned++;
         QAUtils.verboseMsg("\tScanning directory: " + relName);
         retval = true;
      }
      return retval;
   }


   /**
    * Read and build the list of directories and files that should be excluded when scanning the
    * source file tree. Exclusions are saved as path names relative to source root directory
    * 
    * @param excFile contains text names of files and directories to exclude from the search
    * @param rootFile location of the exclusion file
    */
   private void setExclusions(File excFile, File rootFile)
   {
      // Ensures that both exclusion lists are empty
      _excDirs = new ArrayList<String>();
      _excFiles = new ArrayList<String>();

      Scanner in = null;
      try {
         in = new Scanner(excFile);
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      String s = in.nextLine();
      if (!s.equals("DIRECTORIES")) {
         System.err.println("\n setExclusions(): " + excFile.getName() + " has invalid format");
      }
      // Add excluded directories to list
      while (in.hasNext()) {
         s = in.nextLine().trim();
         if (s.equals("FILES") || (s.length() == 0)) {
            break;
         }
         _excDirs.add(rootFile.getPath() + Constants.FS + s);
      }
      // Add excluded files to list
      while (in.hasNext()) {
         s = in.nextLine().trim();
         if (s.equals("FILES") || (s.length() == 0)) {
            continue;
         }
         _excFiles.add(rootFile.getPath() + Constants.FS + s);
      }
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
            String name1 = sig1.substring(sig1.indexOf(SPACE) + 1, sig1.indexOf(LEFT_PAREN));
            String name2 = sig2.substring(sig2.indexOf(SPACE) + 1, sig2.indexOf(LEFT_PAREN));
            String parms1 = sig1.substring(sig1.indexOf(LEFT_PAREN), sig1.indexOf(RIGHT_PAREN) + 1);
            String parms2 = sig2.substring(sig2.indexOf(LEFT_PAREN), sig2.indexOf(RIGHT_PAREN) + 1);
            // System.err.println("\t\t sort loops = " + ++count);

            // Compare method names
            int retval = name1.compareTo(name2); // compare method names
            // Compare number of parms and parms names
            if (retval == 0) {
               String[] nbrParms1 = parms1.split(COMMA);
               String[] nbrParms2 = parms2.split(COMMA);
               retval = nbrParms1.length - nbrParms2.length;
               if (retval == 0) {
                  retval = parms1.compareTo(parms2);
               }
            }
            return retval;
         }
      });
   }


   // ================================================================================
   // MockSrcReader
   // ================================================================================

   public class MockSrcReader
   {
      public MockSrcReader()
      {}

      public int[] getScanResults()
      {
         int[] results = new int[4];
         results[0] = SrcReader.this._dirsScanned;
         results[1] = SrcReader.this._filesScanned;
         results[2] = SrcReader.this._dirsSkipped;
         results[3] = SrcReader.this._filesSkipped;
         return results;
      }

   }


} // end of SrcReader class
