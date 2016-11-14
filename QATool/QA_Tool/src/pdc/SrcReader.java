
/**
 * SrcReader.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import mylib.Constants;
import pdc.QAUtils.FileType;

/**
 * Traverses the source tree, providing source files to compare with test files from the
 * {@code TestWriter}.
 * 
 * @author Alan Cline
 * @version Jul 5, 2016 // original <br>
 */
public class SrcReader
{
   private final String COMMA = ",";
   private final String DOT = ".";
   private final String SPACE = " ";
   private final String LEFT_PAREN = "(";
   private final String RIGHT_PAREN = ")";

   private File _srcRoot;
   private TestWriter _testWriter;
   private boolean _verbose;

   private int _dirsScanned;
   private int _filesScanned;
   private int _dirsSkipped;
   private int _filesSkipped;

   // Exclusions lists
   ArrayList<String> _excDirs;
   ArrayList<String> _excFiles;

   // length of path prefix to control recursion
//   private int _srcPathLen;

   // ================================================================================
   // CONSTRUCTOR and HELPER METHODS
   // ================================================================================

   public SrcReader(File srcRoot, TestWriter testWriter, boolean verbose)
   {
      _srcRoot = srcRoot;
//      _srcPathLen = _srcRoot.getPath().length();
      _testWriter = testWriter;
      _verbose = verbose;
   }


   /**
    * Sets object fields and exclusion files
    * 
    * @param srcRoot path of the tree which contains source files to scan
    * @param excludeFile file directly beneath the src root that contains directories and files not
    *           to scan
    * @param qaScanner contains common utility methods
    */
   public SrcReader(File srcRoot, File excFile, TestWriter testWriter, boolean verbose)
   {
      _srcRoot = srcRoot;
//      _srcPathLen = _srcRoot.getPath().length();
      _testWriter = testWriter;
      _verbose = verbose;

      // Set the exclusion files and folders
      setExclusions(excFile, srcRoot);

      _dirsScanned = 0;
      _filesScanned = 0;
      _dirsSkipped = 0;
      _filesSkipped = 0;
   }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   /**
    * Return the class method signature without package context or throws clauses, but with its
    * return type, formatted as: <br>
    * {@code  methodName(argType, argType) : returnType} <br>
    * where each of the Types are their simple names.
    * 
    * @param m the Method object to get full path and properties returned by Class.getMethod()
    * @param anchorName simple name of the class under reflection
    * @return the method signature, e.g. as is used in the test method comment
    */
   public String extractSignature(Method m, String anchorName)
   {
      String s = m.toString();
      // Skip any method names that do not have the anchorName in it (synthetic classes) and a
      // 'main'
      if ((!s.contains(anchorName)) || (s.contains("main("))) {
         return null;
      }
      // Remove any throws clauses
      if (s.contains("throws")) {
         s = s.substring(0, s.indexOf("throws"));
      }

      // Remove the modifer
      s = s.substring(s.indexOf(SPACE) + 1);
      String retType = simplifyReturnType(s);
      String methodDecl = simplifyDeclaration(s);
      return (retType + " " + methodDecl);
   }

   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   /**
    * Scan a particular file and write (or augment) a test file for it
    * 
    * @param f File to examine for test methods
    */
   public void fileScan(File f)
   {
      ArrayList<String> srcList = new ArrayList<String>();

      String srcPath = f.getPath();
      srcList = QAUtils.collectMethods(srcPath, FileType.SOURCE);
      QAUtils.outMsg(_verbose,
            "\n\tSource file " + srcPath + " contains " + srcList.size() + " eligible methods:");
      if (_verbose) {
         QAUtils.outList("\t\t", srcList);
      }

      // Get corresponding test file
      File testFile = _testWriter.getTargetTestFile(srcPath);

      // Write the test file using src names as a reference to build test names
      _testWriter.writeTestFile(testFile, srcList);
   }


   /**
    * Traverse the source tree recursively, skipping over files in the HIC or non-java files.
    * Directories encountered drops to subdirectory recursively.
    * 
    * @param src starting directory for source files
    * @param excFile list of directories and files to exclude from test class generation
    * @return source file that meets all criteria
    */
   public void scan(File src)
   {
      // Retrieve all files and subdirs under dir
      File[] allFiles = src.listFiles();
      for (File f : allFiles) {
         if (isValidDirectory(f)) {
            scan(f);
         } else if (isValidFile(f)) {
            fileScan(f);
         }
      }
   }


   public void scanResults()
   {
      QAUtils.outMsg(_verbose, "Scanning complete: ");
      QAUtils.outMsg(_verbose, "\t Directories scanned: " + _dirsScanned);
      QAUtils.outMsg(_verbose, "\t Files scanned: " + _filesScanned);
      QAUtils.outMsg(_verbose, "\t Directories skipped per exclusion file: " + _dirsSkipped);
      QAUtils.outMsg(_verbose, "\t Files skipped per exclusion file: " + _filesSkipped);
   }


   /**
    * Reduce a fully qualified class name to it simplified name by removing the dot-delimited full
    * name to yield the suffix, the simple name. This is used for return types and argument types
    * that occur in the method declaration.<br>
    * The method declaration has format, where each type is a fully qualified type: <br>
    * {@code return-type methodName(argType, argType,...) <br>
    * For example, {@code java.lang.String extractSignature(java.io.File, java.lang.String)} becomes
    * {@code String extractSignature(File, String)}.<br>
    * Note: The ellipsis in the signature example refers to a fixed but indefinite number of
    * arguments, not to a varargs set.
    * 
    * @param decl the fully-qualified method declaration
    * @return the method name and simple argname list
    */
   public String simplifyDeclaration(String decl)
   {
      // Discard the the return type
      decl = decl.trim();
      int rtNdx = decl.indexOf(SPACE);
      decl = decl.substring(rtNdx + 1);

      // Setup buffers to allow characer movement
      StringBuilder sbIn = new StringBuilder(decl);
      StringBuilder sbOut = new StringBuilder();

      // To simplify arguments, walk backwards from the right paren, removing prefixes
      boolean skip = false;
      int in = sbIn.length() - 1;
      for (; in >= 0; in--) {
         char ch = sbIn.charAt(in);
         // Add space character to follow each comma
         if (ch == ',') {
            sbOut.insert(0, SPACE); // new char is placed in front of existing chars
            skip = false;
         } else if (ch == '(') {
            skip = false;
         }
         // Skip all characters between previous comma or left paren and the dot
         else if ((ch == '.') || (skip == true)) {
            skip = true;
            continue;
         }
         sbOut.insert(0, ch);
         // System.out.println(String.format("\tCharacter written: %c", sbOut.charAt(0)));
      }
      String result = sbOut.toString().trim();
      return result;
   }


   /**
    * Convert the fully qualifed return type of a signature into its simple type. Also removes the
    * method modifier (public, private, static, protected).
    * 
    * @param decl fully qualifed method signature, with parm types and return type
    * @return only the simple return type
    */
   public String simplifyReturnType(String decl)
   {
      // Remove trailing and leading white space then make a destination String
      decl = decl.trim();
      String dest = new String(decl);

      int retNdx = decl.indexOf(SPACE); // return type
      String retSig = decl.substring(0, retNdx);
      int lastDot = retSig.lastIndexOf(DOT);
      dest = decl.substring(lastDot + 1, retNdx);

      return dest;
   }


   // ================================================================================
   // PRIVATE METHODS
   // ================================================================================

   /**
    * Checks if file is a non-excluded java source file and not in the HIC directory
    * 
    * @param f file under examination
    * @return true if file is a valid source file
    */
   private boolean isValidFile(File f)
   {
      boolean retval = false;
      String s = f.getPath();
      if (_excFiles.contains(s)) {
         // _qas.outMsg("\tSKIPPING file " + s + "\n");
         _filesSkipped++;
      } else if ((s.endsWith(".java")) && (!s.contains("hic"))) {
         _filesScanned++;
         retval = true;
      }
      return retval;
   }


   /**
    * Checks if file is a non-excluded directory
    * 
    * @param f file under examination
    * @return true if file is a non-excluded directory
    */
   private boolean isValidDirectory(File f)
   {
      // Guards: do not step into, or count, the "test" subdir
      if (!f.isDirectory()) {
         return false;
      }
      String s = f.getPath();
      if (s.contains("src" + Constants.FS + "test")) {
         return false;
      }

      boolean retval = false;
      if (!_excDirs.contains(f.getPath())) {
         QAUtils.outMsg(_verbose, "\tRECURSING into directory " + f.getPath() + "\n");
         _dirsScanned++;
         retval = true;
      } else {
         _dirsSkipped++;
      }
      return retval;
   }


   /**
    * Read and build the list of directory and files that should be exluded when scanning the source
    * file tree. Exclusions are saved as path names relative to source root directory
    * 
    * @param excFile contains files and directories to exclude from the search
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

} // end of SrcReader class
