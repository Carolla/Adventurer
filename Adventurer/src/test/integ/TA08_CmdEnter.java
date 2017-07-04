/**
 * TA08_EnterBuilding.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Test;

/**
 * Enter a specified building from the building's exterior or from the town. If the Hero is outside
 * a particular Building, no name is required.
 * <P>
 * Format: {@code ENTER [BuildingName]} enters the specified Building if not already in one, or
 * enters the current building if one is not specified. <br>
 * <P>
 * Implementation Note: The {@code MainFrameProxy}, which implements {@code MainframeInterface},
 * intervenes between the {@code civ.CommandParser} and the {@code hic.Mainframe}. All inputs are
 * sent directly to the {@code civ.CmdParser} and outputs are received by the
 * {@code test.integ.MainframeProxy}.
 * 
 * @author Alan Cline
 * @version Dec 20, 2014 // original <br>
 *          Mar 5, 2015 // updated for more testing <br>
 *          Jun 22, 2015 // replaced {@code hic.integ.IOProxy} with {@code hic.integ.MainframeProxy} <br>
 *          Jun 30, 2015 // adding in remainder of {@code CmdEnter} integration tests <br>
 *          Jul 5, 2015 // adding in error cases <br>
 */
public class TA08_CmdEnter extends IntegrationTest
{

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown()
  {
    resetBuildingState();
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ==========================================================
  // Begin the tests!
  // ==========================================================

  /**
   * Normal case: Enter a valid building from the town (no current building)
   */
  @Test
  public void test_EnterBuildingFromTownOrExterior()
  {
    for (int k = 0; k < _bldgs.size(); k++) {
      resetBuildingState();
      assertTrue(_bldgCiv.isOnTown());
      assertFalse(_bldgCiv.isInside());

      _cp.receiveCommand("Enter " + _bldgs.get(k));
      assertFalse(_bldgCiv.isOnTown());
      assertTrue(_bldgCiv.isInside());

      String bName = _bldgCiv.getCurrentBuilding();
      assertTrue("Expected " + _bldgs.get(k) + ", got " + bName, bName.equals(_bldgs.get(k)));
    }
  }


  /**
   * Normal case: Enter a valid building from outside the current Building (no parms)
   */
  @Test
  public void test_EnterCurrentBuilding()
  {
    for (int k = 0; k < _bldgs.size(); k++) {
      resetBuildingState();

      String bName = _bldgs.get(k);
      _cp.receiveCommand("Approach " + bName);
      assertFalse(_bldgCiv.isOnTown());
      assertFalse(_bldgCiv.isInside());

      // TEST
      _cp.receiveCommand("Enter");

      // VERIFY
      assertFalse(_bldgCiv.isOnTown());
      assertTrue(_bldgCiv.isInside());

      String newCurrent = _bldgCiv.getCurrentBuilding();
      assertEquals("Expected " + _bldgs.get(k) + ", got " + newCurrent, bName, newCurrent);
    }
  }


  /**
   * Error case: Attempt to enter a building from inside another Building
   */
  @Test
  public void test_EnterFromInsideBuilding()
  {
    String bName1 = "Ugly Ogre Inn";
    String bName2 = "Arcaneum";

    _cp.receiveCommand("Enter " + bName1);

    assertFalse(_bldgCiv.isOnTown());
    assertTrue(_bldgCiv.isInside());
    assertEquals(bName1, _bldgCiv.getCurrentBuilding());

    _cp.receiveCommand("Enter " + bName1);

    assertFalse(_bldgCiv.isOnTown());
    assertTrue(_bldgCiv.isInside());
    assertEquals(bName1, _bldgCiv.getCurrentBuilding());

    _cp.receiveCommand("Enter " + bName2);

    assertFalse(_bldgCiv.isOnTown());
    assertTrue(_bldgCiv.isInside());
    assertEquals(bName1, _bldgCiv.getCurrentBuilding());
  }
} // end of TA08_EnterBuilding integration test case
