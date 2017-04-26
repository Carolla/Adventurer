/**
 * QAUtils.java Copyright (c) 2016, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com
 */

package pdc;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.regex.Pattern;

import mylib.Constants;

/**
 * @author Alan Cline
 * @version Mar 27, 2017 // major refactoring from existing utility class <br>
 */
public class QAUtils
{
   // From the web: http://www.java2s.com/Tutorial/Java/
   // 0125__Reflection/LoadingaClassThatIsNotontheClasspath.htm
   // A URLClassLoader can be used to load classes in any directory.
   //
   // import java.io.File;
   // import java.net.URL;
   // import java.net.URLClassLoader;
   //
   // public class Main {
   // public static void main(String[] argv) throws Exception {
   // File file = new File("c:\\");
   //
   // URL url = file.toURI().toURL();
   // URL[] urls = new URL[] { url };
   //
   // ClassLoader cl = new URLClassLoader(urls);
   //
   // Class cls = cl.loadClass("com.mycompany.MyClass");
   // }
   // }


   /**
    * Return the {@code Class} for the given java file. If the .class file is not found, null is
    * returned.
    * 
    * @param path the absolute path (with package name) {@code .java} source filename
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
    * Extracts public and protected methods from a class file, then sorts the list.
    * 
    * @param clazzName {@code .class} file for target file
    * @return list of public and protected method signatures for the target
    */
   static public ArrayList<String> extractMethodNames(Class<?> clazzName)
   {
      ArrayList<String> mList = new ArrayList<String>();

      Method[] rawMethodList = clazzName.getDeclaredMethods();
      for (Method method : rawMethodList) {
         int modifiers = method.getModifiers();
         if (modifiers == 0) {
            QAUtils.verboseMsg("WARNING: " + method.getName()
                  + "() has default access; should have a declared access");
         }
         if ((Modifier.isPublic(modifiers)) || (Modifier.isProtected(modifiers))) {
            String mName = QAUtils.extractSignature(method, clazzName.getName());
            if (mName != null) {
               mList.add(mName);
            }
         }
      }
      // Sort methods to keep in sync with test methods later
      QAUtils.sortSignatures(mList);
      return mList;
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


   // From the web: http://www.java2s.com/Tutorial/Java/
   // 0125__Reflection/LoadingaClassThatIsNotontheClasspath.htm
   // A URLClassLoader can be used to load classes in any directory.
   //
   // import java.io.File;
   // import java.net.URL;
   // import java.net.URLClassLoader;
   //
   // public class Main {
   // public static void main(String[] argv) throws Exception {
   // File file = new File("c:\\");
   //
   // URL url = file.toURI().toURL();
   // URL[] urls = new URL[] { url };
   //
   // ClassLoader cl = new URLClassLoader(urls);
   //
   // Class cls = cl.loadClass("com.mycompany.MyClass");
   // }
   // }


   /**
    * Return the {@code .class} file for a given java file that is not in the project folder. A
    * {@code .class} file that is not in the application's workspace is referred to as a <i>foreign
    * class</i>.
    * 
    * @param srcPath the absolute path of the {@code .java} source filename
    * @return the corresponding {@.class} file
    * @throws ClassNotFoundException if the target {@code .class} file cannot be found
    * @see convertPathnameToURLFormat()
    */
   static public Class<?> getForeignClass(String srcPath) throws ClassNotFoundException
   {
      Class<?> cls = null;
      URL url = null;
      String[] parts = convertPathnameToURLFormat(srcPath);
      String server = parts[0];
      String filename = parts[1];

      try {
         url = new URL(server);
      } catch (MalformedURLException e) {
         e.printStackTrace();
      }
      URL[] urls = new URL[] {url};
      @SuppressWarnings("resource")
      ClassLoader cl = new URLClassLoader(urls);
      cls = cl.loadClass(filename);
      return cls;
   }


   /**
    * High-level method to get methods names from a given filename
    * 
    * @param targetPath filepath of file from which to extract methods
    * @return list of public and protected method signatures for the target
    */
   static public ArrayList<String> getMethods(String targetPath)
   {
      Class<?> clazz = null;
      try {
         clazz = QAUtils.getForeignClass(targetPath);
      } catch (ClassNotFoundException ex) {
         System.err.println("Target class not found: " + targetPath);
      }
      // Collect the methods from the source file
      ArrayList<String> mList = QAUtils.extractMethodNames(clazz);
      return mList;
   }


   /**
    * Determine if the given character is a digit from 1 to 9
    * 
    * @param ch character to check
    * @return true if c is a numeric digit
    */
   static public boolean isDigit(char ch)
   {
      return (ch >= '1' && ch <= '9');
   }


   /**
    * Send a list to the console as audit trail
    * 
    * @param msg message to be printed above list dump
    * @param alist some list to be printed
    */
   static public void printList(String msg, ArrayList<String> alist)
   {
      System.out.println("\n" + msg);
      for (String s : alist) {
         System.out.println("\t" + s);
      }
   }


   /**
    * Send map entries to the console as audit trail
    * 
    * @param msg message to be printed above list dump
    * @param amap some map to be printed as key, value
    */
   static public void printMap(String msg, Map<String, String> amap)
   {
      System.out.println("\n" + msg);
      for (String key : amap.keySet()) {
         System.out.println("\t" + key + "\t \\\\  " + amap.get(key));
      }
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
            String name1 = sig1.substring(sig1.indexOf(Constants.SPACE) + 1,
                  sig1.indexOf(Constants.LEFT_PAREN));
            String name2 = sig2.substring(sig2.indexOf(Constants.SPACE) + 1,
                  sig2.indexOf(Constants.LEFT_PAREN));
            String parms1 = sig1.substring(sig1.indexOf(Constants.LEFT_PAREN),
                  sig1.indexOf(Constants.RIGHT_PAREN) + 1);
            String parms2 = sig2.substring(sig2.indexOf(Constants.LEFT_PAREN),
                  sig2.indexOf(Constants.RIGHT_PAREN) + 1);
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
      // if (trueVERBOSE) {
      // System.out.println(msg);
      // }
   }


   // From the web: http://www.java2s.com/Tutorial/Java/
   // 0125__Reflection/LoadingaClassThatIsNotontheClasspath.htm
   // A URLClassLoader can be used to load classes in any directory.
   //
   // import java.io.File;
   // import java.net.URL;
   // import java.net.URLClassLoader;
   //
   // public class Main {
   // public static void main(String[] argv) throws Exception {
   // File file = new File("c:\\");
   //
   // URL url = file.toURI().toURL();
   // URL[] urls = new URL[] { url };
   //
   // ClassLoader cl = new URLClassLoader(urls);
   //
   // Class cls = cl.loadClass("com.mycompany.MyClass");
   // }
   // }
   
   
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


   // From the web: http://www.java2s.com/Tutorial/Java/
   // 0125__Reflection/LoadingaClassThatIsNotontheClasspath.htm
   // A URLClassLoader can be used to load classes in any directory.
   //
   // import java.io.File;
   // import java.net.URL;
   // import java.net.URLClassLoader;
   //
   // public class Main {
   // public static void main(String[] argv) throws Exception {
   // File file = new File("c:\\");
   //
   // URL url = file.toURI().toURL();
   // URL[] urls = new URL[] { url };
   //
   // ClassLoader cl = new URLClassLoader(urls);
   //
   // Class cls = cl.loadClass("com.mycompany.MyClass");
   // }
   // }
   
   
   /**
    * Reformats a filepath name into two pieces to represent a URL "server" and a {@code .class}
    * filename. The {@code URLClassLoader} will search the "server" for the target classfile.
    * <P>
    * The first piece is formatted to make the local file system look like a web server, delimited
    * by slashes. The second piece is a package-formatted classname, delimited by dots. It is
    * important to indicate file directories with a terminating slash, otherwise the
    * {@code URLClassLoader} will expect to find a {@code .jar} file.
    * <P>
    * Since the {@code URLClassLoader} is looking for a {@code .class} file, and not a source
    * filename, the "/src/" part of the srcPath is replaced with the "/bin/" subdirectory, where the
    * {@code .class} name must be stored.
    * <P>
    * Example: {@code srcpath = "/Projects/QATool/src/pdc/targetFile.java"} is converted to
    * {@code url = "file:///Projects/QATool/bin/"} and {@code filename = "pdc.targetFile"} (the
    * {@code .class} extension is assumed by the {@code classLoader}).
    * 
    * @param srcPath long pathname of java source file
    * @returns URL "server" part in {@code String[0]} and the filename part in {@code String[1]}
    * @see QAUtls.getForeignClass()
    */
   static private String[] convertPathnameToURLFormat(String srcPath)
   {
      String[] parts = new String[2];
      String CUTPOINT = "/bin/";
      String PROTOCOL = "file://";
      // Convert srcPath into URL server format
      String s1 = srcPath.replace("/src/", CUTPOINT);
      int cutPoint = s1.lastIndexOf(CUTPOINT) + CUTPOINT.length();
      parts[0] = PROTOCOL + s1.substring(0, cutPoint);
   
      // Convert srcPath into URL format: server and filename
      String s2 = srcPath.substring(cutPoint);
      s2 = s2.replace(".java", "");
      parts[1] = s2.replaceAll("/", ".");
      return parts;
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

}
