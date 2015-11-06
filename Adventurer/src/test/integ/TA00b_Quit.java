/**
 * TA00b_Quit.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;


import static org.junit.Assert.assertTrue;

import java.io.File;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.Chronos;

/**
 * Ensure that the program exits back to the system, closing all registries, but not deleting them.
 * 
 * @author alancline
 * @version Jul 23, 2014 // original <br>
 *          Sept 5, 2014 // Exit was renamed to Quit, to terminate program
 */
public class TA00b_Quit
{

  private final String[] paths = {Chronos.AdventureRegPath, Chronos.BuildingRegPath,
      Chronos.ItemRegPath, Chronos.NPCRegPath, Chronos.OcpRegPath, Chronos.SkillRegPath,
      Chronos.TownRegPath};

  // ============================================================
  // Fixtures
  // ============================================================

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
    MsgCtrl.errorMsgsOn(true);
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ============================================================
  // Integration Test
  // ============================================================

  /**
   * Stub placeholder for TA00b_Exit integration test. It is all GUI testing or has already been
   * tested
   */
  @Test
  public void test_Quit()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.msgln(this, "\t testTA00b_Quit()");

    // VERIFY that the registries still exist
    assertTrue(registryFilesExist());

  }

  // ============================================================
  // Helper Methods
  // ============================================================

  /** Check that all Registry files exist and are of non-zero length */
  private boolean registryFilesExist()
  {
    boolean retval = true;
    for (String s : paths) {
      File f = new File(s);
      retval = (doesExist(s) && (f.length() > 0));
      if (retval == false) {
        break;
      }
    }
    return retval;
  }

  
  /** Check existence of single Registry file */
  private boolean doesExist(String path)
  {
    File rf = new File(path);
    return rf.exists();
  }


} // end of TA00b_Quit class
