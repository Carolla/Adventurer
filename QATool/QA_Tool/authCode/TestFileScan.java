/**
 * 
 */

package test;

import javax.swing.tree.DefaultMutableTreeNode;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.TestCase;
import mylib.MsgCtrl;
import pdc.FileScan;


/**
 * @author alancline
 * @version Dec 30 2015 // original <br>
 */
public class TestFileScan extends TestCase
{
  /** Use MyLibrary file structure as a read-only test root */
  static private final String ROOT = "/Projects/eChronos/MyLibrary/src/mylib";

  static private FileScan _fs;

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    _fs = new FileScan(ROOT);
    assertNotNull(_fs.getRoot());
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _fs = null;
  }


  // ========================================================================
  // BEGIN TESTS
  // ========================================================================

  /**
   * @ERROR.TEST Set root to null if it is not a file
   */
  @Test
  public void testErrorNonFileRoot()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    String NONFILE_ROOT = "/Projects/eChronos/QATool/QA_Tool/src/notaFile";
    _fs = new FileScan(NONFILE_ROOT);
    assertNull(_fs.getRoot());
  }

  /**
   * @ERROR.TEST Set root to null if it is not a directory
   */
  @Test
  public void testErrorNonDirRoot()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    String NONDIR_ROOT = "/Projects/eChronos/QATool/QA_Tool/src/authCode/QASpec.java";
    _fs = new FileScan(NONDIR_ROOT);
    assertNull(_fs.getRoot());
  }

  /**
   * @NORMAL.TEST Root contains a PDC and a TEST subdirectory
   */
  @Test
  public void testRequiredDirs()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    _fs = new FileScan(ROOT);
    assertTrue(_fs.containsRequiredSubdirs());
  }

  /**
   * @ERROR.TEST Root does not contains a TEST subdirectory. This is done by setting the root to the
   *             test directory itself
   */
  @Test
  public void testErrorReqSubdirs()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    String MISSING_REQD_SUBDIRS = "/Projects/eChronos/MyLibrary/src/mylib/test";
    _fs = new FileScan(MISSING_REQD_SUBDIRS);
    assertFalse(_fs.containsRequiredSubdirs());
  }


  /**
   * @NORMAL.TEST Make a list of source files per directory
   */
  @Test
  public void testGetDirectories()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    _fs = new FileScan(ROOT);
    MsgCtrl.msgln("\tSource subdirectories under root");
    DefaultMutableTreeNode srcMap = _fs.makeSourceLists();
//    dumpTree(srcMap);
    
  }
  
  
  /** Dump the elements of a tree */
  private void dumpTree(DefaultMutableTreeNode top)
  {
    
    
    
    
  }


} // end of TestFileScan class
