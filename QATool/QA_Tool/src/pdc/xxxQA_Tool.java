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
 * {@code "Project"/src}) has corresponding test classes in its mandatory {@code src/test} subdir.
 * The program builds a prototype test class for each missing test file using reflection to
 * auto-generate (failing) test method stubs. Finally, the program builds a unit test suite with a
 * unit test entry for each test file. Implementation details follow:
 * <P>
 * <ol>
 * <li>Builds a list of paths of sub-directories and source files contained within each subdir. Each
 * root directory must contain {@code src/pdc} and {@code src/test} subdirs. If the optional
 * {@code src/hic}, {@code src/civ} or {@code src/dmc} subdirs are missing, the program issues a
 * warning.</li>
 * <li>Traverses the {@code src} pathname list and looks for a matching test file path that matches
 * (or doesn't match) in the {@code src/test} pathname list. Each {@code subdir/Classname.java} file
 * is expected to have a {@code test/subdir/TestClassname.java} test file. Conversely, if a test
 * class exists that does not have a source file, the program issues a warning.</li>
 * <li>Creates a "prototype" test class for all missing test classes. Each method in the prototype
 * test class will contain an auto-generated (failing) test method stub.</li>
 * <li>Reviews all existing test classes to ensure that all class methods have at least one test
 * method, with the exception of the constructor and method names that are prefixed with
 * {@code get, set} or suffixed with {@code wrapper}. It is also possible to annotate the source
 * method with a {@code @No_TestGen Annotation} to indicate not to generate a test stub for that
 * method.</li>
 * <li>Creates {@code UnitTestSuite.java} with an entry for each unit test file, organized by
 * subdir, and commented as such.</li>
 * </ol>
 * <P>
 * The regression test suite {@code Adventurer/src/test/RegressionTestSuite.java} contains four line
 * entries:
 * <ol>
 * <li>{@code MyLibrary/src/mylib/test/MyLibraryTestSuite.class}</li>
 * <li>{@code ChronosLib/src/test/ChronsLib/UnitTestSuite.class}</li>
 * <li>{@code Adventurer/src/test/UnitTestSuite.class} and</li>
 * <li>{@code Adventurer/src/test/IntegTestSuite.class}</li>
 * </ol>
 * <P>
 * As part of a Github prehook, each time someone does a {@code git push}, the QA_Tool is run to
 * generate all unit test suites in the regression suite, except for the Integration test suite,
 * which must be built by hand. The Github prehook also then runs the regression test suite.
 * 
 * @author alancline
 * @version Dec 30 2015 // original <br>
 *
 */
public class xxxQA_Tool
{
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
  public xxxQA_Tool(String path)
  {
    _root = new File(path);
    if (!_root.isDirectory()) {
      System.err.println("QA_Tool: Path argument must be a directory");
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
      File f = fileList[k]; // convenience shorthand
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


  // ========================================================================
  // INNER CLASS: MockTool
  // ========================================================================

  /** Provide access to the QA_Tool fields for testing */
  public class MockTool
  {
    public MockTool()
    {}

    public File getRoot()
    {
      return _root;
    }

  } // end of MockTool inner class


} // end of FileScan class

