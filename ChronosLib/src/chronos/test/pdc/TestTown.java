/**
 * TestTown.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.Town;
import chronos.pdc.buildings.Building;
import chronos.test.pdc.buildings.FakeBuilding;
import mylib.MsgCtrl;

/**
 * Verify that the Town Class works as expected
 * 
 * @author Alan Cline
 * @version Feb 6, 2013 // original <br>
 *          Oct 17, 2014 // added more tests <br>
 *          July 16, 2017 // added more tests per QA File Scanner tool; major refactoring to put
 *          file into standard (Chronos) coding style <br>
 */
public class TestTown
{
  private Town _town;
  private static final String NAME = "Test Town";
  private static final String DESC_DAY =
      "Of all the towns in all the world, she had to walk into mine.";
  private static final String DESC_NIGHT =
      "The town square was eerily quiet, except for the chirp of evil crickets.";
  private static final String[] _bldgNames = {"Arcaneum", "Jail", "Jewelry Store", "Monastery",
      "Rat's Pack General Store", "Rogues' Den", "Larry's Livery", "Stadium", "The Bank",
      "Ugly Ogre Inn"};

  private final List<Building> _bldgList = new ArrayList<Building>();


  @Before
  public void setUp() throws Exception
  {
    for (int k = 0; k < _bldgNames.length; k++) {
      _bldgList.add(new FakeBuilding(_bldgNames[k]));
    }
    _town = new Town(NAME, DESC_DAY, DESC_NIGHT);
    _town.addBuildings(_bldgList);
  }

  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _town = null;
    _bldgList.clear();
  }


  // ================================================================================
  // CONSTRUCTOR TESTING
  // ================================================================================

  /**
   * @NORMAL.TEST Town(String name, String descDay, String descNight) -- includes adding all
   *              buildings into the Town
   */
  @Test
  public void testTown()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    assertEquals(_bldgNames.length, _town.getAllBuildings().size());
  }


  /**
   * @NORMAL.TEST Town(String name, String descDay, String descNight) -- Town are only
   *              differentiated by their name, not their descriptions
   */
  @Test
  public void testCtor_DifferentDescriptionSameTown()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    Town town2 = new Town(NAME, "Day desc", DESC_NIGHT);
    Town town3 = new Town(NAME, DESC_DAY, "Night desc");

    assertEquals(_town, town2);
    assertEquals(town2, town3);
  }

  /**
   * @ERROR.TEST Town(String name, String descDay, String descNight) -- Null parms for various
   *             inputs to force an exception or error
   */
  @SuppressWarnings("unused")
  @Test(expected = AssertionError.class)
  public void testCtor_NullNightDesc()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Night description can be null
    assertNotNull(new Town(NAME, DESC_DAY, null));
    // Day description is required
    try {
      new Town(NAME, null, DESC_NIGHT);
    } catch (IllegalArgumentException ex1) {
      MsgCtrl.errMsg("\tExpected exception on null daytime description");
    }
    // Town name is required
    try {
      new Town(null, DESC_DAY, DESC_NIGHT);
    } catch (IllegalArgumentException ex2) {
      MsgCtrl.errMsg("\tExpected exception on null town name");
    }
  }


  /**
   * @Normal.Test Town(String name, String descDay, String descNight) -- Same town have same name
   */
  @Test
  public void testCtor_SameNameTowns()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    Town t1 = new Town(NAME, "Daytime description", DESC_NIGHT);
    Town t2 = new Town(NAME, DESC_DAY, "Optional nighttime description");

    assertEquals(t1.getName(), t2.getName());
    
  }

  
  // ================================================================================
  // TEST METHODS
  // ================================================================================
  
  /**
   * @Not.Needed void addBuildings(List) -- wrapper; part of ctor testing
   */
  @Test
  public void testAddBuildings()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msg("\tSimple wrapper method; part of constructor testing");
  }

  



  /**
   * Not Implemented List getAllBuildings()
   */
  @Test
  public void testGetAllBuildings()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    fail("TEST METHOD NOT YET IMPLEMENTED");
  }


  /**
   * Not Implemented String getDayDescription()
   */
  @Test
  public void testGetDayDescription()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    fail("TEST METHOD NOT YET IMPLEMENTED");
  }


  /**
   * Not Implemented String getKey()
   */
  @Test
  public void testGetKey()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    fail("TEST METHOD NOT YET IMPLEMENTED");
  }


  /**
   * Not Implemented String getNightDescription()
   */
  @Test
  public void testGetNightDescription()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    fail("TEST METHOD NOT YET IMPLEMENTED");
  }


  /**
   * Not Implemented int hashCode()
   */
  @Test
  public void testHashCode()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    fail("TEST METHOD NOT YET IMPLEMENTED");
  }


  @Test
  public void testHashCode_SameTownEqual()
  {
    assertEquals(_town, _town);
    assertEquals(_town.hashCode(), _town.hashCode());
    assertEquals(_town.toString(), _town.toString());
  }

  @Test
  public void testHashCode_DupTownEquals()
  {
    Town town2 = new Town(NAME, DESC_DAY, DESC_NIGHT);

    assertEquals(_town, town2);
    assertEquals(_town.hashCode(), town2.hashCode());
    assertEquals(_town.toString(), town2.toString());
  }

  @Test
  public void testHashCode_DifferentNameDifferentTown()
  {
    Town town2 = new Town("Not the same", DESC_DAY, DESC_NIGHT);

    assertFalse(_town.equals(town2));
    assertFalse(_town.hashCode() == town2.hashCode());
    assertFalse(_town.toString().equals(town2.toString()));
  }

  /**
   * Not Implemented String toString()
   */
  @Test
  public void testToString()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    fail("TEST METHOD NOT YET IMPLEMENTED");
  }


} // end of TestTown class
