/**
 * QAScanner.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */

package pdc;

import java.io.File;

/**
 * @author Alan Cline
 * @version Apr 11, 2016 // original <br>
 */
public class _QAScanner
{
   private File _srcRoot;
   private SrcReader _srcReader;
   private TestWriter _testWriter;


   // ================================================================================
   // CONSTRUCTOR AND HELPER METHODS
   // ================================================================================

   public _QAScanner(File root, File excFile, boolean verbose, boolean nofail)
   {
      _srcRoot = root;
      _testWriter = new TestWriter(root);
      _srcReader = new SrcReader(root, excFile, _testWriter);
   }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   /** Display number of dirs and files scanned */
   public void qaReport()
   {
      // QAUtils.auditMsg("\n\n");
      _srcReader.scanResults();
      _testWriter.writeResults();
   }

   /**
    * Recursively scans a tree of source files to create test files in directories that correspond
    * with the source directories.
    */
   public void treeScan()
   {
      _srcReader.scan(_srcRoot);
   }


} // end of QAScanner class
