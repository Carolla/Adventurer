/**
 * TestRace.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@wowway.com
 */

package chronos.test.pdc.race;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;
import chronos.pdc.race.Race;
import chronos.pdc.race.Race.MockRace;
import mylib.MsgCtrl;


/**
 * Tests the abstract Race class by implementing a concrete subclass from which the base class
 * methods are called.
 * 
 * @author Alan Cline
 * @version Jun 4, 2009 // original <br>
 *          Jan 18, 2010 // add in non-Human racial tests <br>
 *          August 10, 2017 // updated per QA Tool <br>
 *          August 15, 2017 // protected methods are better tested by subclasses <br>
 */
public class TestRace
{

  @Before
  public void setUp()
  {}

  @After
  public void tearDown()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // =============================================================================
  // BEGIN TESTING
  // =============================================================================

  /**
   * @Not.Needed Race() -- wrapper for subclass constructors
   */
  @Test
  public void testCtor()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.WRAPPER);
  }


  /**
   * @Not.Needed TraitList buildRace() -- group of protected setters pancaked together. Methods
   *             within this one are tested individually, usually at subclass level.
   */
  @Test
  public void testBuildRace()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.SETTER);
  }


  /**
   * @Not.Needed int calcHeight(int low, String range) -- parms depends on subclass
   */
  @Test
  public void testCalcHeight()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.BASECLASS);
  }


  /**
   * @Not.Needed int calcWeight(int low, String range) -- parms depends on subclass
   */
  @Test
  public void testCalcWeight()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.BASECLASS);
  }


  /**
   * @Normal.Test TraitList constrainTo(TraitList, int[], int[]) -- force all values to 1
   */
  @Test
  public void testConstrainTo()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP
    Race race = Race.createRace("Human", new Gender("male"), "brown");
    MockRace mock = race.new MockRace();
    TraitList traits = new TraitList();

    int[] lower = {1, 1, 1, 1, 1, 1};
    int[] upper = {1, 1, 1, 1, 1, 1};

    // RUN
    traits = mock.constrainTo(traits, lower, upper);
    int[] values = traits.toArray();

    // VERIFY All traits are set to 1
    for (int k = 0; k < 6; k++) {
      assertEquals(1, values[k]);
    }
    MsgCtrl.msgln("\t All traits bounded to [1,1]");
  }


  /**
   * @Normal.Test TraitList constrainTo(TraitList, int[], int[]) -- force all values to [6,12]
   */
  @Test
  public void testConstrainTo_Range()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP
    Race race = Race.createRace("Human", new Gender("male"), "brown");
    MockRace mock = race.new MockRace();
    TraitList traits = new TraitList();

    int[] lower = {6, 6, 6, 6, 6, 6};
    int[] upper = {12, 12, 12, 12, 12, 12};

    // RUN
    traits = mock.constrainTo(traits, lower, upper);
    int[] values = traits.toArray();

    // VERIFY All traits are set to range
    for (int k = 0; k < 6; k++) {
      assertTrue((values[k] >= 6) && (values[k] <= 12));
    }
    MsgCtrl.msgln("\t All traits bounded to [6,12]");
  }

  /**
   * @Error.Test TraitList constrainTo(TraitList, int[], int[]) -- illegal values are used
   */
  @Test
  public void testConstrainTo_Error()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP
    Race race = Race.createRace("Human", new Gender("male"), "brown");
    MockRace mock = race.new MockRace();
    int[] errors = {0, -1, 2, 24, 19, 12};

    TraitList traits = new TraitList(errors);
    int[] lower = {8, 8, 8, 8, 8, 8};
    int[] upper = {18, 18, 18, 18, 18, 18};
    // With these bounds, all traits are out of bounds except the last

    // RUN
    traits = mock.constrainTo(traits, lower, upper);
    int[] values = traits.toArray();

    // VERIFY All traits are set to range
    for (int k = 0; k < 6; k++) {
      assertTrue((values[k] >= 8) && (values[k] <= 18));
    }
    MsgCtrl.msgln("\t All traits bounded to [8,18]");
  }


  /**
   * @Normal.Test Race createRace(String raceName, Gender gender, String hairColor) -- create Race
   *              superclass that can be cast to specific subclasses, like Human
   * @Error.Test Race createRace(String raceName, Gender gender, String hairColor) -- request to
   *             create an unknown Race subclass; throws NullPointerException
   */
  @Test
  public void testCreateRace()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // All Normal subclasses
    for (int k = 0; k < Race.RACE_LIST.length; k++) {
      Race race = Race.createRace(Race.RACE_LIST[k], new Gender("male"), "brown");
      assertNotNull(race);
      String raceName = race.getClass().getName();
      String subraceName = "chronos.pdc.race." + Race.RACE_LIST[k];
      int ndx = subraceName.indexOf("-");
      // Classes cannot have hyphens, so remove it before comparing
      if (ndx >= 0) {
        StringBuilder sb = new StringBuilder(); // new sb to clear old one
        sb.append(subraceName.substring(0, ndx));
        sb.append(subraceName.substring(ndx + 1));
        subraceName = sb.toString();
      }
      MsgCtrl.msgln("\t Creating " + raceName);
      assertEquals(raceName, subraceName);
    }

    // Error case: no known subclass
    try {
      Race.createRace("Half-Gnome", new Gender("male"), "brown");
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (NullPointerException ex) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + ex.getMessage());
    }
  }


  /**
   * @Normal.Test boolean equals(Object) -- two Race objects are the same: subrace and gender
   */
  @Test
  public void testEquals()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Race object Human
    Race humanMale1 = Race.createRace(Race.RACE_LIST[0], new Gender("Male"), "brown");
    Race humanMale2 = Race.createRace(Race.RACE_LIST[0], new Gender("Male"), "brown");
    assertTrue(humanMale1.equals(humanMale2));

    // Dwarf created
    Race dwarfMale = Race.createRace(Race.RACE_LIST[1], new Gender("Male"), "brown");
    assertFalse(dwarfMale.equals(humanMale1));

    // Human differs by gender
    Race humanFemale = Race.createRace(Race.RACE_LIST[0], new Gender("Female"), "brown");
    assertFalse(humanMale1.equals(humanFemale));
    // Here the String.equals is used
    String her = humanFemale.getGender();
    String him = humanMale1.getGender();
    assertFalse(her.equals(him));
  }


  /**
   * @Not.Needed String getGender() -- simple getter
   */
  @Test
  public void testGetGender()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed String getHeight() -- simple getter
   */
  @Test
  public void testGetHeight()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed ArrayList getLanguages() -- simple getter
   */
  @Test
  public void testGetLanguages()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed String getName() -- simple getter
   */
  @Test
  public void testGetName()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed String getRaceDescriptor() -- simple getter
   */
  @Test
  public void testGetRaceDescriptor()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed List getSkills() -- simple getter
   */
  @Test
  public void testGetSkills()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed int getWeight() -- simple getter
   */
  @Test
  public void testGetWeight()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Normal.Test int hashCode() -- generate a unique ID for the Item object
   */
  @Test
  public void testHashCode()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    Race race1 = Race.createRace("Human", new Gender("female"), "blond");
    Race race2 = Race.createRace("Human", new Gender("female"), "blond");
    assertEquals(race1, race2);
    assertEquals(race1.toString(), race2.toString());
    assertEquals(race1.hashCode(), race2.hashCode());

    Race race3 = Race.createRace("Human", new Gender("male"), "blond");
    assertNotEquals(race1, race3);
    assertNotEquals(race1.toString(), race3.toString());
    assertNotEquals(race1.hashCode(), race3.hashCode());

  }


  /**
   * @Not.Needed void loadRaceKeys(Map<PersonKeys, String> map) -- requires subclasses to test
   */
  @Test
  public void testLoadRaceKeys()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.BASECLASS);
  }



} // end of TestRace class
