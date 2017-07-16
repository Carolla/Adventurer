/**
 * TestFileScanner.java Copyright (c) 2017, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com.
 */

package test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.MsgCtrl;
import pdc.FileScanner;
import pdc.FileScanner.MockFileScanner;

/**
 * @author Alan Cline
 * @version April 30, 2017 // original <br>
 *          July 17, 2017 // updated for command line split: path arg to path + filename arg <br>
 */
public class TestFileScanner
{
  private FileScanner _fs;
  private MockFileScanner _mock;

  // Set up target for parm (unused)
  private String _path = "/Projects/eChronos/QATool/FileScanner/src/";
  private String _filename = "pdc/FileScanner.java";
  private final String VERBOSE = "-verbose"; 
  private final String FAILSTUBS = "-failStubs"; 
  private final String FILEECHO = "-fileEcho"; 
  
  /**
   * @throws java.lang.Exception -- general catchall for exceptions not caught by the tests
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception -- general catchall for exceptions not caught by the tests
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception -- general catchall for exceptions not caught by the tests
   */
  @Before
  public void setUp() throws Exception
  {
    // Create FileScanner and mock for testing
    _fs = new FileScanner();
    _mock = _fs.new MockFileScanner();
  }

  /**
   * @throws java.lang.Exception -- general catchall for exceptions not caught by the tests
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _fs = null;
    _mock = null;
  }


  // ===============================================================================
  // BEGIN TESTING
  // ===============================================================================

  /**
   * Normal Test: String verifyArgs(String[]) One target file and no other parms
   */
  @Test
  public void testVerifyArgs()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP Load command line options
    String[] args = loadCmdLine(null, null, null);

    // RUN
    _mock.verifyArgs(args);

    // VERIFY
    assertFalse(_mock.isVerbose());
    assertFalse(_mock.isFailStubs());
    assertFalse(_mock.isFileEcho());
  }


  /**
   * Normal Test: String verifyArgs(String[]) One target file and -failStubs only
   */
  @Test
  public void testVerifyArgs_AllParms()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
  
    // SETUP Load command line options
    String[] args = loadCmdLine(FAILSTUBS, VERBOSE, FILEECHO);
  
    // RUN
    _mock.verifyArgs(args);
  
    // VERIFY
    assertTrue(_mock.isVerbose());
    assertTrue(_mock.isFailStubs());
    assertTrue(_mock.isFileEcho());
  }

  /**
   * Normal Test: String verifyArgs(String[]) One target file and -fileEcho only
   */
  @Test
  public void testVerifyArgs_FileEcho()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP Load command line options
    String[] args = loadCmdLine(FILEECHO, null, null);

    // RUN
    _mock.verifyArgs(args);

    // VERIFY
    assertFalse(_mock.isVerbose());
    assertFalse(_mock.isFailStubs());
    assertTrue(_mock.isFileEcho());
  }

  /**
   * Normal Test: String verifyArgs(String[]) One target file and all three parms
   */
  @Test
  public void testVerifyArgs_FailStubs()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP Load command line options
    String[] args = loadCmdLine(FAILSTUBS, null, null);

    // RUN
    _mock.verifyArgs(args);

    // VERIFY
    assertFalse(_mock.isVerbose());
    assertTrue(_mock.isFailStubs());
    assertFalse(_mock.isFileEcho());
  }


  /**
   * Error Test: String verifyArgs(String[]) -- command line = null
   */
  @Test
  public void testVerifyArgs_NoCmdLine()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
  
    // SETUP Load command line options
    String ERRMSG_NOCMDLINE = "Missing command line for QAFileScan";
    
    // RUN
    String result = _mock.verifyArgs(null);
  
    // VERIFY
    assertEquals(ERRMSG_NOCMDLINE, result);
  
    // SETUP Load command line options
    String[] args = new String[0];
    assertEquals(0, args.length);
    // RUN
    result = _mock.verifyArgs(args);
    // VERIFY
    assertEquals(ERRMSG_NOCMDLINE, result);
  }

  /**
   * Normal Test: String verifyArgs(String[]) One target file and -verbose only
   */
  @Test
  public void testVerifyArgs_Verbose()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
  
    // SETUP Load command line options
    String[] args = loadCmdLine(VERBOSE, null, null);
  
    // RUN
    _mock.verifyArgs(args);
  
    // VERIFY
    assertTrue(_mock.isVerbose());
    assertFalse(_mock.isFailStubs());
    assertFalse(_mock.isFileEcho());
  }

  

  // ===============================================================================
  // BEGIN TESTING
  // ===============================================================================

  /** Load the path and file name, and one or more of the cmd line options. Options can be
   * in any order. Use null to indicate options not loaded; duplicates are not valid. 
   * 
   * @param opt1: one of the args: -verbose, -failStubs, or -fileEcho
   * @param opt2: one of the args: -verbose, -failStubs, or -fileEcho
   * @param opt3: one of the args: -verbose, -failStubs, or -fileEcho
   * @return the command line string to verify
   */
  private String[] loadCmdLine(String opt1, String opt2, String opt3)
  {
    // ArrayList is used because it is self-adjusting, and the resulting String[] will not
    // contain any null entries.
    ArrayList<String> cmdLine = new ArrayList<>();
    cmdLine.add(_path);
    cmdLine.add(_filename);
    if (opt1 != null) {
      cmdLine.add(opt1);
    }
    if (opt2 != null) {
      cmdLine.add(opt2);
    }
    if (opt3 != null) {
      cmdLine.add(opt3);
    }
    // Now convert to String[] 
    int len = cmdLine.size();
    String[] args = new String[len];
    for (int k=0; k < len; k++) {
      args[k] = cmdLine.get(k);
    }
    return args;
  }
  
  
  
} // end of TestFileScanner.java class
