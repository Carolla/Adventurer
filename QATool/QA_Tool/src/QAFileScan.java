
/**
 * QAFileScan.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

import java.io.File;

import pdc.SrcReader;
import pdc.TestWriter;

/**
 * @author Alan Cline
 * @version Jul 21, 2016 // original <br>
 */
public class QAFileScan
{
   static private final String USAGE_MSG = "QAFileScan <filepath>";
   /**
    * Scan an individual file for missing test methods, and write a corresponding test file with the
    * omitted test methods supplied as failing stubs.
    * 
    * @param args contains only the file path to be examined
    */
   public static void main(String[] args)
   {
      // Guard: Check that first arg is good file, and second arg is verbose flag
      File srcFile = null;
      try {
         srcFile = new File(args[0]);
      } catch (NullPointerException ex) {
         System.err.println(ex.getMessage());
         System.err.println("QAFileScan: Can't find target file");
         System.err.println(USAGE_MSG);
         System.exit(-10);
      }
      boolean verbose = false;
      if ((args.length == 2) && (args[1].equals("-v"))){
         verbose = true;
      }
      else {
         System.err.println(USAGE_MSG);
         System.exit(-10);
      }
      
      // Create TestWriter and SrcReader
      TestWriter tw = new TestWriter(srcFile, verbose);
      SrcReader sr = new SrcReader(srcFile, tw, verbose);
      sr.fileScan(srcFile);
   }

   
}  // end of QAFileScan class
