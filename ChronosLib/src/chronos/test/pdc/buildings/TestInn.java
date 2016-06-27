/**
 * TestInn.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.test.pdc.buildings;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.buildings.Inn;

/**
 * Verify that the Inn exists as a meeting place for Heroes, allows conversation with the Innkeeper
 * andPatrons, provides sleeping and eating behavior, and can ban Heroes for certain amounts of
 * time.
 *
 * @author Alan Cline
 * @version Jan 28, 2013 // original <br>
 */
public class TestInn
{
  /** Test target object */
  private Inn _inn = null;

  /** Business opening hour for test Inn */
  private final int TEST_OPEN = 1000;
  /** Business closing hour for test Inn */
  private final int TEST_CLOSING = 1200;

  /**
   * Creates the test Inn, but many tests in this class create their own different Inns
   * 
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    _inn = new Inn();
    assertNotNull(_inn);
    _inn.setBusinessHours(TEST_OPEN, TEST_CLOSING);
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    _inn = null;
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ TEST METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /**
   * Chronos.pdc.ItemRegistry
   * 
   * @Normal ensure that the test inn has correct data
   */
  @Test
  public void InnDoesntGetPatronsWithoutInit()
  {
    assertTrue(_inn.getPatrons().size() == 0);
  }


  /**
   * Tests that are not implemented either because tests are not needed, or they haven't been
   * determined to be needed yet
   * 
   * @Not_Needed getKey() // getter
   * @Not_Needed getBusinessHours() // getter
   * @Not_Needed getOpeningTime() // wrapper
   * @Not_Needed getClosingTime() // setter
   */
  public void NotNeeded()
  {}

} // end of TestInn class
