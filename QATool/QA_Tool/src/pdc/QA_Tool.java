/**
 * 
 */

package pdc;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Verifies that all {@code .java} source files within a given "root" directory (
 * {@code "Project"/src}) has corresponding test classes in its (mandatory) {@code src/test} subdir.
 * The program builds a prototype test class for each missing test file using reflection to
 * auto-generate test method stubs. Finally, the program builds a unit test suite with a unit test
 * entry for each test file. Implementation details follow:
 * <P>
 * <ol>
 * <li>Builds a tree of sub-directories, and source files contained within each subdir. Each root
 * directory must contain {@code src/pdc} and {@code src/test} subdirs; if {@code src/hic},
 * {@code src/civ} or {@code src/dmc} subdirs are missing, the program issues a warning.</li>
 * <li>Traverses the {@code src/test} subdir of root for all test classes that match (or don't
 * match) a corresponding source file. Each {@code subdir/Classname.java} file is expected to have a
 * {@code test/subdir/TestClassname.java} test file. If a test class exists that does not have a
 * source file, the program issues a warning.</li>
 * <li>Creates a "prototype" test class for all missing test classes. Each method in the prototype
 * test class will contain an auto-generated test method stub, with a default {@code fail} message
 * and an auto-generate message within.</li>
 * <li>Reviews all existing test classes to ensure that all class methods have at least one test
 * method, with the exception of the constructor and method names that are prefixed with
 * {@code get, set} or suffixed with {@code wrapper}. It is also possible to annotate the source
 * method with a {@code @No_TestGen Annotation} to indicate not to generate a test stub for that
 * method.</li>
 * <li>Creates {@code UnitTestSuite.java} with an entry for each unit test file, organized by
 * subdir, and commented as such.</li>
 * </ol>
 * <P>
 * The regression test suite {@code test/RegressionTestSuite.java} can contain two line entries:
 * {@code test/IntegTestSuite.class} and {@code test/UnitTestSuite.class}.
 * 
 * @author alancline
 * @version Dec 30 2015 // original <br>
 *
 */
public class QA_Tool
{
  /** Map of source file that need test files */
  private ArrayList<File> _map = new ArrayList<File>();

  /** Root folder containing source files */
  static private File _root;


  // ========================================================================
  // CONSTRUCTOR AND HELPERS
  // ========================================================================

  /**
   * Creates an object to scan through a tree of source folders
   * 
   * @param path top level folder containing source directories and files
   */
  public QA_Tool(String path)
  {
    _root = new File(path);
    if (!_root.isDirectory()) {
      _root = null;
    }
  }


  // ========================================================================
  // PUBLIC METHODS
  // ========================================================================

  /**
   * Map the directories and files within them as authority strcuture for test files. The pdc and
   * test directories are required.
   */
  public boolean containsRequiredSubdirs()
  {
    String[] rawmap = _root.list();
    ArrayList<String> map = new ArrayList<String>(rawmap.length);
    // Move all filenames into the map
    for (String s : rawmap) {
      map.add(s);
    }
    return map.contains("pdc") && (map.contains("test"));
  }


  public File getRoot()
  {
    return _root;
  }


  /**
   * Return a tree of only= directories
   *
   * @return a tree of directories and source files
   */
  public DefaultMutableTreeNode makeDirTree(DefaultMutableTreeNode top)
  {
    DefaultMutableTreeNode subDirNode = top;
    // Get all files at certain level
    File[] fileList = _root.listFiles();
    for (int k = 0; k < fileList.length; k++) {
      File f = fileList[k];   // convenience shorthand
      // Add directories to the dir tree...
      if (f.isDirectory()) {
        subDirNode = new DefaultMutableTreeNode(f);
        top.add(subDirNode);
        top = subDirNode;
      } else {
        DefaultMutableTreeNode srcNode = new DefaultMutableTreeNode(f);
        top.add(srcNode);
      }

    }
    // Return the root of this tree since it progressed to the bottom leaf
    return (DefaultMutableTreeNode) top.getRoot();
  }


  // ========================================================================
  // INNER CLASS: SourceFilter
  // ========================================================================


  /** Define a file filter to select only source files from a candidate */
  class SourceFilter implements FilenameFilter
  {
    public SourceFilter()
    {}

    /**
     * File is acceptable only if the extension is .java
     * 
     * @param f file object is ignored
     * @param nm filename to search of .java extension
     * @return true if the .java extension exists for the name
     */
    public boolean accept(File f, String nm)
    {
      // look at extension from last period to end of string
      String sub = nm.substring(nm.lastIndexOf("."));
      return sub.equalsIgnoreCase("java");
    }

  } // end of SourceFilter inner class


  // /** Define a file filter to select only source files from a candidate */
  // class DirFilter implements FilenameFilter
  // {
  // public DirFilter()
  // {}
  //
  // /**
  // * File is acceptable only if the extension is .java
  // *
  // * @param f directory in which the file is found
  // * @param nm filename to search for acceptance match
  // * @return true if nm represents a directory name
  // */
  // public boolean accept(File f, String nm)
  // {
  // // look at extension from last period to end of string
  // File candidate = new File(nm);
  // boolean retval = candidate.isDirectory();
  // return retval;
  // }
  //
  // } // end of DirFilter inner class


} // end of FileScan class

