/**
 * TA10_ReturnToTown.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
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

import org.junit.Before;
import org.junit.Test;

import civ.BuildingDisplayCiv;

/**
 * Leave the interior of a Building.
 * <P>
 * Format: {@code LEAVE} moves the Bulding's exterior.
 * 
 * @author Alan Cline
 * @version August 10, 2015 // original <br>
 */
public class TA10_ReturnToTown extends IntegrationTest
{
  @Before
  public void setup()
  {
    resetBuildingState();
  }


  // ==========================================================
  // Begin the tests!
  // ==========================================================

  @Test
  public void TA10_LeaveFromInsideBuilding()
  {
    for (int i = 0; i < BuildingDisplayCiv.DEFAULT_BUILDINGS.length - 1; i++) {
      String building = BuildingDisplayCiv.DEFAULT_BUILDINGS[i][0];
      _cp.receiveCommand("Enter " + building);

      assertEquals(building, _bldgCiv.getCurrentBuilding());
      assertFalse(_bldgCiv.isOnTown());
      assertTrue(_bldgCiv.isInside());

      // TEST
      assertTrue(_cp.receiveCommand("Return"));

      // Confirm Hero is no longer on town or in building
      assertTrue(_bldgCiv.isOnTown());
      assertFalse(_bldgCiv.isInside());
      assertEquals("", _bldgCiv.getCurrentBuilding());
    }
  }


  @Test
  public void TA10_LeaveFromOutsideBuilding()
  {
    for (int i = 0; i < BuildingDisplayCiv.DEFAULT_BUILDINGS.length - 1; i++) {
      String building = BuildingDisplayCiv.DEFAULT_BUILDINGS[i][0];
      _cp.receiveCommand("Approach " + building);

      assertEquals(building, _bldgCiv.getCurrentBuilding());
      assertFalse(_bldgCiv.isOnTown());
      assertFalse(_bldgCiv.isInside());

      // TEST
      assertTrue(_cp.receiveCommand("Return"));

      // Confirm Hero is no longer on town or in building
      assertTrue(_bldgCiv.isOnTown());
      assertFalse(_bldgCiv.isInside());
      assertEquals("", _bldgCiv.getCurrentBuilding());
    }
  }
}
