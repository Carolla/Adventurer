/**
 * QAScanner.java Copyright (c) 2016, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import mylib.Constants;

/**
 * @author Alan Cline
 * @version Apr 11, 2016 // original <br>
 */
public class QAScanner
{
   private final String COMMA = ",";
   private final String DOT = ".";
   private final String SPACE = " ";
   private final String LEFT_PAREN = "(";
   private final String RIGHT_PAREN = ")";

   // True if verbose messages should be written (audit trail)
   static private boolean _verbose;

   private File _srcRoot;

   private SrcReader _srcReader;
   private TestWriter _testWriter;


   // ================================================================================
   // CONSTRUCTOR AND HELPER METHODS
   // ================================================================================

   public QAScanner(String[] args)
   {
      // Convenience variables
      _srcRoot = new File(args[0]);
      _verbose = (args.length == 3) ? true : false;

      _testWriter = new TestWriter(_srcRoot, this);
      _srcReader = new SrcReader(_srcRoot, args[1], _testWriter, this);

   }


   // ================================================================================
   // PUBLIC METHODS
   // ================================================================================

   // Display number of dirs and files scanned
   public void QAReport()
   {
      outMsg("\n\n");
      _srcReader.scanResults();
      _testWriter.writeResults();
   }


   /**
    * Extracts public and protected methods from the source file, sorts each list
    * 
    * @param clazz target source file
    * @return list of public and protected method signatures for the target
    */
   public ArrayList<String> collectMethods(String filePath)
   {
      ArrayList<String> mList = new ArrayList<String>();

      Class<?> clazz = convertSourceToClass(filePath);
      String clazzName = clazz.getSimpleName();

      Method[] rawMethodList = clazz.getDeclaredMethods();
      for (Method method : rawMethodList) {
         int modifiers = method.getModifiers();
         if (modifiers == 0) {
            System.err.println("WARNING: " + method.getName()
                  + "() has default access; should have a declared access");
         }
         if ((Modifier.isPublic(modifiers)) || (Modifier.isProtected(modifiers))) {
            String mName = extractSignature(method, clazzName);
            if (mName != null) {
               mList.add(mName);
            }
         }
      }
      // Sort methods to keep in sync with test methods later
      sortSignatures(mList);
      return mList;
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

   
   /**
    * Return the {@code Class} for the given java file
    * 
    * @param path the fully-qualifed (with package name) {@code .java} source filename
    * @param root the directory prefix to be removed to get the sourceText file
    * @return the equivalent {@.class} file
    */
   public Class<?> convertSourceToClass(String path)
   {
      // Remove the file extension
      String className = path.split(".java")[0];
      // Convert the file path format to package format by replacing the "/" with "." or "\" with
      // Windoze
      className = className.replaceAll(Pattern.quote(Constants.FS), ".");
      // Remove any prefix that ends with "src/"
      className = className.substring(className.lastIndexOf("src.") + 4);
      // Replace src with bin
      Class<?> sourceClass = null;
      try {
         sourceClass = Class.forName(className);
      } catch (ClassNotFoundException ex) {
         System.err.println("\tconvertSourceToClass(): " + className + ".class file not found");
      }
      if (sourceClass == null) {
         System.err.println(
               "\tEnsure that " + path + " has been compiled and exists in the bin directory");
      }
      return sourceClass;
   }


   // ================================================================================
   // PROTECTED METHODS
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
   // public METHODS
   // ================================================================================

   /**
    * Display a list to the console if the verbose flag is set
    * 
    * @param msg message to display
    */
   public void outList(String msg, List<String> aList)
   {
      if (_verbose) {
         for (String s : aList) {
            System.out.println("\t\t" + s);
         }
      }
   }


   /**
    * Display a message to the console if the verbose flag is set
    * 
    * @param msg message to display
    */
   public void outMsg(String msg)
   {
      if (_verbose) {
         System.out.println(msg);
      }
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

   public void treeScan()
   {
      _srcReader.scan(_srcRoot);
   }


   // ================================================================================
   // PRIVATE METHODS
   // ================================================================================


} // end of QAScanner class
