/**
 * TA00b_Exit.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Ensure that the program exits back to the system, closing all registries
 * 
 * @author alancline
 * @version Jul 23, 2014 // original <br>
 */
public class TA00b_Exit
{

  // ============================================================
  // Fixtures
  // ============================================================

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.msg("Integration test: Adventurer.TA00b_Exit ");
  }

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
  {}

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
  public void testTA00b_Exit()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.msgln(this, ": testTA00b_Exit()");

    MsgCtrl.msgln("Nothing to test for integration--"
        + "it is all GUI testing or has already been tested.");
  }

  // ============================================================
  // Helper Methods
  // ============================================================


} // end of TA00b_Exit class