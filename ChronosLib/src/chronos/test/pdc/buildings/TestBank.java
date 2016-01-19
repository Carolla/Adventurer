/**
 * TestBank.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.test.pdc.buildings;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mylib.MsgCtrl;

/**
 * Test the Bank methods
 * 
 * @author Alan Cline
 * @version Apr 8, 2013 // original <br>
 */
public class TestBank
{
  // ==============================================================================
  // Fixtures
  // ==============================================================================

  /**
   * Creates the test Bank, but many tests in this class create their own different banks
   * 
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
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

  @Test
  public void NoTestsNeeded()
  {
  }

  // ==============================================================================
  // TEST METHODS
  // ==============================================================================

} // end of TestBank class
