
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
import java.util.ArrayList;
import java.util.Scanner;

import mylib.Constants;

/**
 * Traverses the source tree, providing source files to compare with test files from the
 * {@code TestWriter}.
 * 
 * @author Alan Cline
 * @version Jul 5, 2016 // original <br>
 */
public class SrcReader
{
   private File _srcRoot;
   private TestWriter _testWriter;
   private QAScanner _qas;

   private int _dirsScanned;
   private int _filesScanned;
   private int _dirsSkipped;
   private int _filesSkipped;

   // Exclusions lists
   ArrayList<String> _excDirs;
   ArrayList<String> _excFiles;

   // length of path prefix to control recursion
   private int _srcPathLen;


   // ================================================================================
   // CONSTRUCTOR and HELPER METHODS
   // ================================================================================

   /**
    * Sets object fields and exclusion files
    * 
    * @param srcRoot path of the tree which contains source files to scan
    * @param excludeFile file directly beneath the src root that contains directories and files not
    *           to scan
    * @param qaScanner contains common utility methods
    */
   public SrcReader(File srcRoot, String excludeFilename, TestWriter testWriter, QAScanner qas)
   {
      _srcRoot = srcRoot;
      _srcPathLen = _srcRoot.getPath().length();
      _testWriter = testWriter;
      _qas = qas;

      // Set the exclusion files and folders
      File excludeFile = new File(_srcRoot + Constants.FS + excludeFilename);
      setExclusions(excludeFile, srcRoot);

      _dirsScanned = 0;
      _filesScanned = 0;
      _dirsSkipped = 0;
      _filesSkipped = 0;
   }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

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
      ArrayList<String> srcList = new ArrayList<String>();

      // Retrieve all files and subdirs under dir
      File[] allFiles = src.listFiles();
      for (File f : allFiles) {
         if (isValidDirectory(f)) {
            scan(f);
         } else if (isValidFile(f)) {
            String srcPath = f.getPath();
            srcList = _qas.collectMethods(srcPath);
            _qas.outMsg("\n\tEligible source file " + srcPath + " contains " + srcList.size() + " methods.");
            _qas.outList("\t\t", srcList);

            // Get corresponding test file
            File testFile = _testWriter.getTargetTestFile(f.getPath());
            _qas.outMsg("\tCorresponding test file: " + testFile.getPath());
            
            // Write the test file using src names as a reference to build test names
            _testWriter.writeTestFile(testFile, srcList);
         }
      }
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
//         _qas.outMsg("\tSKIPPING file " + s + "\n");
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
         _qas.outMsg("\tRECURSING into directory " + f.getPath() + "\n");
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

   public void scanResults()
   {
      _qas.outMsg("Scanning complete: ");
      _qas.outMsg("\t Directories scanned: " + _dirsScanned);
      _qas.outMsg("\t Files scanned: " + _filesScanned);
      _qas.outMsg("\t Directories skipped per exclusion file: " + _dirsSkipped);
      _qas.outMsg("\t Files skipped per exclusion file: " + _filesSkipped);
   }

} // end of SrcReader class
