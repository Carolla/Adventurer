/**
 * QAUtils.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */

package pdc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

import mylib.Constants;

/**
 * @author Alan Cline
 * @version Mar 27, 2017 // major refactoring from existing utility class <br>
 */
public class QAUtils
{

   /**
    * Return the {@code Class} for the given java file. If the .class file is not found, null is
    * returned.
    * 
    * @param path the fully-qualifed (with package name) {@code .java} source filename
    * @return the equivalent {@.class} file, or null if .class file could not be found
    * @throws ClassNotFoundException if the compile target cannot be found
    */
   static public Class<?> convertFileToClass(String path) throws ClassNotFoundException
   {
      // Guard: get qualifed package name of class file
      String className = convertPathToPackageName(path);
      Class<?> sourceClass = Class.forName(className);
      return sourceClass;
   }

   /**
    * Convert a path name to its package name equivalent so it can be used to retrieve its class
    * names; path cannot be null.
    * 
    * @param path path of file to convert
    * @return the dot-delimited package name corresponding to the file name
    */
   static private String convertPathToPackageName(String path)
   {
      // Guard: null prohibited
      if (path == null) {
         return null;
      }
      // Separate the file and the root part from the path
      String fname = null;
      String[] parts = path.split(Constants.SRC_PREFIX);
      // String root = parts[0];
      fname = parts[1];
      // Pull off the java suffix
      fname = fname.split(".java")[0];
      // Convert file separators to package format
      fname = fname.replaceAll(Pattern.quote(Constants.FS), ".");
      return fname;
   }

   /**
    * Return the class method signature without package context or throws clauses, but with its
    * return type, formatted as: <br>
    * {@code  methodName(argType, argType) : returnType} <br>
    * where each of the Types are their simple names.
    * 
    * @param m the Method object to get full path and properties returned by Class.getMethod()
    * @param anchorName simple name of the class under reflection, e.g, constructor names
    * @return the method signature, e.g. as is used in the test method comment
    */
   static public String extractSignature(Method m, String anchorName)
   {
      String s = m.toString();
      // Skip any method name without an anchorName in it (synthetic classes) and a 'main()'
      if ((!s.contains(anchorName)) || (s.contains("main("))) {
         return null;
      }
      // Remove any throws clauses
      if (s.contains("throws")) {
         s = s.substring(0, s.indexOf("throws"));
      }

      // Remove the modifer
      s = s.substring(s.indexOf(Constants.SPACE) + 1);
      String retType = simplifyReturnType(s);
      String methodDecl = simplifyDeclaration(s);
      return (retType + " " + methodDecl);
   }

   /**
    * Convert the fully qualifed return type of a signature into its simple type. Also removes the
    * method modifier (public, private, static, protected).
    * 
    * @param decl fully qualifed method signature, with parm types and return type
    * @return only the simple return type
    */
   static private String simplifyReturnType(String decl)
   {
      // Remove trailing and leading white Constants.SPACE then make a destination String
      decl = decl.trim();
      String dest = new String(decl);

      // Remove 'static' keyword if it exists
      dest = dest.replace("static ", "");
      int retNdx = dest.indexOf(Constants.SPACE); // return type
      String retSig = dest.substring(0, retNdx);
      int lastDot = retSig.lastIndexOf(Constants.DOT);
      retSig = retSig.substring(lastDot + 1, retNdx);

      return retSig;
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
   static private String simplifyDeclaration(String decl)
   {

      // Discard the the return type
      decl = decl.trim();
      int rtNdx = decl.indexOf(Constants.SPACE);
      decl = decl.substring(rtNdx + 1);

      // Setup buffers to allow characer movement
      StringBuilder sbIn = new StringBuilder(decl);
      StringBuilder sbOut = new StringBuilder();

      // To simplify arguments, walk backwards from the right paren, removing prefixes
      boolean skip = false;
      int in = sbIn.length() - 1;
      for (; in >= 0; in--) {
         char ch = sbIn.charAt(in);
         // Add Constants.SPACE character to follow each Constants.COMMA
         if (ch == ',') {
            sbOut.insert(0, Constants.SPACE); // new char is placed in front of existing chars
            skip = false;
         } else if (ch == '(') {
            skip = false;
         }
         // Skip all characters between previous Constants.COMMA or left paren and the dot
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
    * Sort first by method name, then by parm list number and value
    * 
    * @param sList collection of method signatures
    */
   static public void sortSignatures(ArrayList<String> sList)
   {
      Collections.sort(sList, new Comparator<String>() {
         @Override
         public int compare(String sig1, String sig2)
         {
            // Tokenize into three parts: method name, parm list, return type
            String name1 = sig1.substring(sig1.indexOf(Constants.SPACE) + 1, sig1.indexOf(Constants.LEFT_PAREN));
            String name2 = sig2.substring(sig2.indexOf(Constants.SPACE) + 1, sig2.indexOf(Constants.LEFT_PAREN));
            String parms1 = sig1.substring(sig1.indexOf(Constants.LEFT_PAREN), sig1.indexOf(Constants.RIGHT_PAREN) + 1);
            String parms2 = sig2.substring(sig2.indexOf(Constants.LEFT_PAREN), sig2.indexOf(Constants.RIGHT_PAREN) + 1);
            // System.err.println("\t\t sort loops = " + ++count);

            // Compare method names
            int retval = name1.compareTo(name2); // compare method names
            // Compare number of parms and parms names
            if (retval == 0) {
               String[] nbrParms1 = parms1.split(Constants.COMMA);
               String[] nbrParms2 = parms2.split(Constants.COMMA);
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
    * Display a message to the console if the verbose flag is set
    * 
    * @param verbose only display the msg if this param is true
    * @param msg message to display
    */
   static public void verboseMsg(String msg)
   {
      System.out.println(msg);
//      if (trueVERBOSE) {
//         System.out.println(msg);
//      }
   }

}
