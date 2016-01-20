/**
 * TA08_EnterBuilding.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Test;

/**
 * Leave the interior of a Building.
 * <P>
 * Format: {@code LEAVE} moves the Bulding's exterior.
 * 
 * @author Alan Cline
 * @version August 10, 2015 // original <br>
 */
public class TA09_CmdLeave extends IntegrationTest
{
  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    // Set Hero back to town with no current Building
    resetBuildingState();
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ==========================================================
  // Begin the tests!
  // ==========================================================

  /**
   * Normal case: Leave the current Building
   */ 
  @Test
  public void test_LeaveBuilding()
  {
    // Setup: must be inside Building (onTown = false, currentBuilding !null; isInside = true)
    _cp.receiveCommand("Enter the Bank");

    assertFalse(_bldgCiv.isOnTown());
    assertTrue(_bldgCiv.isInside());
    assertNotNull(_bldgCiv.getCurrentBuilding());

    // TEST
    assertTrue(_cp.receiveCommand("Leave "));

    // Confirm Hero is no longer on town or in building
    assertFalse(_bldgCiv.isOnTown());
    assertFalse(_bldgCiv.isInside());
  }


  /**
   * Error: Attempt to LEAVE the currentBuilding when outside
   */
  @Test
  public void test_LeaveBuildingWhenOutside()
  {
    // Setup: outside Building (onTown = false, currentBuilding !null; isInside = false)
    _cp.receiveCommand("Approach the Bank");

    assertFalse(_bldgCiv.isOnTown());
    assertNotNull(_bldgCiv.getCurrentBuilding());
    assertFalse(_bldgCiv.isInside());

    // TEST
    assertFalse(_cp.receiveCommand("Leave "));
    
    // Expected error message
    assertFalse(_bldgCiv.isOnTown());
    assertFalse(_bldgCiv.isInside());

  }
} // end of TA09_CmdLeave integration test case
