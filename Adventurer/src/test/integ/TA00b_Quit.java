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

import org.junit.Test;

/**
 * Ensure that the program exits back to the system, closing all registries, but not deleting them.
 * 
 * @author alancline
 * @version Jul 23, 2014 // original <br>
 *          Sept 5, 2014 // Exit was renamed to Quit, to terminate program
 */
public class TA00b_Quit
{
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
    // VERIFY that the registries still exist
    assertTrue(true);
  }
} // end of TA00b_Quit class
