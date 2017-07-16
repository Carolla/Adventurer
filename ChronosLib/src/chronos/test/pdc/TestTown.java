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
 *          July 17, 2017 // added more tests per QA File Scanner tool <br>
 *          July 15, 2017    // autogen: QA Tool added missing test methods <br>
 */
public class TestTown
{
  private Town _town;
  private static final String NAME = "Test Town";
  private static final String DESC_DAY =
      "Of all the towns in all the world, she had to walk into mine.";
  private static final String DESC_NIGHT =
      "The town square was eerily quiet, except for the chirp of evil crickets.";
  private static final String[] _bldgNames = {"Arcaneum", "Jail", "Monastery",
      "Rat's Pack General Store",
      "Rogues' Den", "Stadium", "The Bank", "Ugly Ogre Inn"};

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
  }

  @Test
  public void testTown()
  {
    assertEquals(_bldgNames.length, _town.getAllBuildings().size());
  }

  @Test(expected = AssertionError.class)
  public void nullNightDescThrows()
  {
    new Town(NAME, DESC_DAY, null);
    fail();
  }

  @Test(expected = AssertionError.class)
  public void nullDescThrows()
  {
    new Town(NAME, null, DESC_NIGHT);
    fail();
  }

  @Test(expected = AssertionError.class)
  public void nullNameThrows()
  {
    new Town(null, DESC_DAY, DESC_NIGHT);
    fail();
  }

  @Test
  public void sameTownIsEqualsHashString()
  {
    assertEquals(_town, _town);
    assertEquals(_town.hashCode(), _town.hashCode());
    assertEquals(_town.toString(), _town.toString());
  }

  @Test
  public void equalsHashStringSameOther()
  {
    Town town2 = new Town(NAME, DESC_DAY, DESC_NIGHT);

    assertEquals(_town, town2);
    assertEquals(_town.hashCode(), town2.hashCode());
    assertEquals(_town.toString(), town2.toString());
  }

  @Test
  public void differentNameDifferentTown()
  {
    Town town2 = new Town("Not the same", DESC_DAY, DESC_NIGHT);

    assertFalse(_town.equals(town2));
    assertFalse(_town.hashCode() == town2.hashCode());
    assertFalse(_town.toString().equals(town2.toString()));
  }


  @Test
  public void differentDescriptionSameTown()
  {
    Town town2 = new Town(NAME, "Day desc", DESC_NIGHT);
    Town town3 = new Town(NAME, DESC_DAY, "Night desc");

    assertEquals(_town, town2);
    assertEquals(town2, town3);
  } 
  
  
	/**
 	 * Not Implemented void addBuildings(List)
	 */
	@Test
	public void testAddBuildings()
	{
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.where(this);

		fail("TEST METHOD NOT YET IMPLEMENTED");
	}


	/**
 	 * Not Implemented boolean equals(Object)
	 */
	@Test
	public void testEquals()
	{
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.where(this);

		fail("TEST METHOD NOT YET IMPLEMENTED");
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
