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

import mylib.Constants;
import mylib.MsgCtrl;

/**
 * @author Alan Cline
 * @version Mar 27, 2017 // major refactoring from existing utility class <br>
 */
public class QAUtils
{
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
   * Ensure that signatures contains a return value before sorting
   * 
   * @param sig signature: returnValue methodName(parms)
   * @throws IllegalArgumentException if returnValue is missing
   */
  static private void checkReturns(String sig) throws IllegalArgumentException
  {
    int delim1 = sig.indexOf(Constants.SPACE);
    int delim2 = sig.indexOf(Constants.LEFT_PAREN);
    if ((delim1 == -1) || (delim1 > delim2)) {
      throw new IllegalArgumentException(
          "Signatures must contain a return value for this method");
    }
  }


  /**
   * Reformats a filepath name into two pieces to represent a URL "server" and a {@code .class}
   * filename. The {@code URLClassLoader} will search the "server" for the target classfile.
   * <P>
   * The first piece is formatted to make the local file system look like a web server, delimited by
   * slashes. The second piece is a package-formatted classname, delimited by dots. It is important
   * to indicate file directories with a terminating slash, otherwise the {@code URLClassLoader}
   * will expect to find a {@code .jar} file.
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
   * @return URL "server" part in {@code String[0]} and the filename part in {@code String[1]}
   * @see #getForeignClass(String)
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
   * Extracts public and protected methods from a class file, then sorts the list.
   * 
   * @param clazzName {@code .class} file for target file
   * @return list of public and protected method signatures for the target
   */
  static private ArrayList<String> extractMethodNames(Class<?> clazzName)
  {
    // Guard : Null pointer check
    if (clazzName == null) {
      return null;
    }
    ArrayList<String> mList = new ArrayList<String>();
    Method[] rawMethodList = clazzName.getDeclaredMethods();
    for (Method method : rawMethodList) {
      int modifiers = method.getModifiers();
      if (modifiers == 0) {
        MsgCtrl.auditErrorMsg("WARNING: " + method.getName()
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
    s = s.substring(s.indexOf(Constants.SPACE) + 1);
    String retType = simplifyReturnType(s);
    String methodDecl = simplifyDeclaration(s);
    return (retType + " " + methodDecl);
  }


  /**
   * Return the {@code .class} file for a given java file. The file may be in the user's project
   * space or outside it. A {@code .class} file that is not in the application's workspace is
   * referred to as a <i>foreign class</i>.
   * 
   * @param srcPath the absolute path of the {@code .java} source filename
   * @return the corresponding {@code .class} file
   * @throws ClassNotFoundException if the target {@code .class} file cannot be found
   * @see #convertPathnameToURLFormat
   */
  static private Class<?> getForeignClass(String srcPath) throws ClassNotFoundException
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
   * Reduce a fully qualified class name to it simplified name by removing the dot-delimited full
   * name to yield the suffix, the simple name. This is used for return types and argument types
   * that occur in the method declaration.<br>
   * The method declaration has format, where each type is a fully qualified type: <br>
   * {@code return-type methodName(argType, argType,...)} <br>
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


  /**
   * Sort first by method name, then by parm list number and value
   * 
   * @param sList collection of method signatures
   * @throws IllegalArgumentException if any signature does not have a return value
   */
  static public void sortSignatures(ArrayList<String> sList) throws IllegalArgumentException
  {
    // Now sort the list
    Collections.sort(sList, new Comparator<String>() {
      @Override
      public int compare(String sig1, String sig2)
      {
        checkReturns(sig1);
        checkReturns(sig2);

        // Tokenize into three parts: method name, parm list, return type
        String name1 = sig1.substring(sig1.indexOf(Constants.SPACE) + 1,
            sig1.indexOf(Constants.LEFT_PAREN));
        String name2 = sig2.substring(sig2.indexOf(Constants.SPACE) + 1,
            sig2.indexOf(Constants.LEFT_PAREN));
        String parms1 = sig1.substring(sig1.indexOf(Constants.LEFT_PAREN),
            sig1.indexOf(Constants.RIGHT_PAREN) + 1);
        String parms2 = sig2.substring(sig2.indexOf(Constants.LEFT_PAREN),
            sig2.indexOf(Constants.RIGHT_PAREN) + 1);

        // Compare method names
        int retval = name1.compareTo(name2); // compare method names
        int retval2 = parms1.compareTo(parms2); // compare parm list
        // Guard against duplicate method+parm names
        if ((retval == 0) && (retval2 == 0)) {
          throw new IllegalArgumentException(
              "Duplicate signatures not allowed: " + name1 + parms1);
        }
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

} // end of QAUtils class
