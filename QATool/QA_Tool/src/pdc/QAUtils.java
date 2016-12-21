/**
 * QAUtils.java Copyright (c) 2016, Alan Cline All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */


package pdc;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import mylib.Constants;

/**
 * Contains a static collection of utility methods used by both SrcReader and TestWriter, or may be
 * independent enough to be used by other classes.
 * 
 * @author Alan Cline
 * @version Jul 21, 2016 // original <br>
 *          Nov 15, 2016 // modified as test cases were added <br>
 */
public class QAUtils
{
   static private final String COMMA = ",";
   static private final String DOT = ".";
   static private final String SPACE = " ";
   static private final String LEFT_PAREN = "(";
   static private final String RIGHT_PAREN = ")";

   /** All compiled classes are stored in this bin directory */
   static private final String _srcBin = "/Projects/eChronos/QATool/QA_Tool/bin";
   static private final String _testBin = "/Projects/eChronos/QATool/QA_Tool/bin/test";
   static private final String _compileLine = "javac -d ";

   public enum FileType {
      SOURCE, TEST
   };

   static private final String SRC_PREFIX = "src" + Constants.FS;
   static private final String TEST_PREFIX = "test" + Constants.FS;

   // Skip over testing prep methods
   static final String[] PREP_METHOD =
         {"setUpBeforeClass", "tearDownAfterClass", "setUp", "tearDown"};


   /**
    * Extracts public and protected methods from a source or test file, then sorts each list.
    * 
    * @param clazz target source file
    * @param ft enum FileType.SOURCE or FileType.TEST to know more about the file being examined
    * @return list of public and protected method signatures for the target
    */
   static public ArrayList<String> collectMethods(String filePath, FileType ft)
   {
      ArrayList<String> mList = new ArrayList<String>();

      Class<?> clazz = null;
      try {
         clazz = convertFileToClass(filePath, ft);
      } catch (ClassNotFoundException ex) {
         System.err.println("QAUtils.collectMethods() " + ex.getMessage());
      }

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
            if ((mName != null) && (!isPrepMethod(mName))) {
               mList.add(mName);
            }
         }
      }

      // Sort methods to keep in sync with test methods later
      sortSignatures(mList);
      return mList;
   }


   /**
    * Display a method list. Set the return type on the other side of the signature to easier read
    * the method name: <br>
    * <code> "returnType methodName(parms...)"</code> becomes
    * <code> "methodName(parms...) -> returnType"</code>.
    * 
    * @param msg message to display
    * @param aList of method signatures to reformat and display
    * @return list of reformatted method names that were displayed
    */
   static public List<String> outList(String msg, List<String> aList)
   {
      ArrayList<String> outList = new ArrayList<String>(aList.size());
      verboseMsg(msg);
      // Extract the return type
      String returnType = null;
      String methodSig = null;
      for (String s : aList) {
         int wsChar = s.indexOf(" ");
         if (wsChar == -1) {
            wsChar = 0;
         } else {
            returnType = s.substring(0, wsChar);
         }
         methodSig = s.substring(wsChar+1);
         String outFmt = methodSig + " -> " + returnType;
         outList.add(outFmt);
         verboseMsg("\t\t" + outFmt);
      }
      return outList;
   }


   /**
    * Display a message to the console if the verbose flag is set
    * 
    * @param verbose only display the msg if this param is true
    * @param msg message to display
    */
   static public void verboseMsg(String msg)
   {
      if (QAFileScan._verbose) {
         System.out.println(msg);
      }
   }


   // ===============================================================================
   // Private Helper Methods
   // ===============================================================================
   
   /**
    * Compile a file so that its latest class file is available. This is a recovery method, so if it
    * fails, nothing to do but printStackTrace
    * 
    * @param filePath file to be compiled
    * @param ft source or test determines into which bin file the compiler class is stored
    */
   static private void compileFileClass(String filePath, QAUtils.FileType ft)
   {
      // Determine where compiler output (.class file) will be stored
      String binPath = (ft == QAUtils.FileType.SOURCE) ? QAUtils._srcBin : QAUtils._testBin;
      String compileLine = _compileLine + SPACE + binPath + SPACE + filePath;
      try {
         Process pro1 = Runtime.getRuntime().exec(compileLine);
         pro1.waitFor();
      } catch (Exception ex) {
         ex.printStackTrace();
         System.err.println(ex.getMessage());
      }
   }


   /**
    * Return the {@code Class} for the given java file. If the .class file is not found, recovery
    * code wil compile it into a predefined bin directory.
    * 
    * @param path the fully-qualifed (with package name) {@code .java} source filename
    * @param ft indentify where SOURCE or TEST file is located
    * @return the equivalent {@.class} file
    * @throws ClassNotFoundException if the compile target cannot be found
    */
   static private Class<?> convertFileToClass(String path, FileType ft) throws ClassNotFoundException
   {
      // Seperate the file and the root part from the path
      String fname = null;
      // String cutPoint = (ft == FileType.SOURCE) ? SRC_PREFIX : TEST_PREFIX;
      String[] parts = path.split(SRC_PREFIX);
      // String root = parts[0];
      fname = parts[1];
      // Pull of the java suffix
      fname = fname.split(".java")[0];
      // Convert file separators to package format
      fname = fname.replaceAll(Pattern.quote(Constants.FS), ".");
      String className = fname;

      Class<?> sourceClass = null;
      try {
         sourceClass = Class.forName(className);
      } catch (ClassNotFoundException ex) {
         // If class cannot be found, compile a new one and try again
         compileFileClass(path, ft);
         try {
            sourceClass = Class.forName(className);
         } catch (ClassNotFoundException ex2) {
            throw new ClassNotFoundException(
                  "convertFileToClass() could not find java file to compile: " + path);
         }
      }
      return sourceClass;
   }


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
   static private String extractSignature(Method m, String anchorName)
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
      s = s.substring(s.indexOf(SPACE) + 1);
      String retType = simplifyReturnType(s);
      String methodDecl = simplifyDeclaration(s);
      return (retType + " " + methodDecl);
   }


   /**
    * Is the method name a standard JUnit test prep methods?
    * 
    * @param name method name to test
    * @return true if the name is one of the skippable names
    */
   static private boolean isPrepMethod(String name)
   {
      boolean retval = false;
      for (int k = 0; k < PREP_METHOD.length; k++) {
         if (name.contains(PREP_METHOD[k])) {
            retval = true;
            break;
         }
      }
      return retval;
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
   static private String simplifyReturnType(String decl)
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


   /**
    * Sort first by method name, then by parm list number and value
    * 
    * @param sList collection of method signatures
    */
   static private void sortSignatures(ArrayList<String> sList)
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


}     // end QAUtils class
