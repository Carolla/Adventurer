/**
 * TestRace.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use from Carolla
 * Development, Inc. by email: acline@wowway.com
 */

package chronos.test.pdc.race;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.Chronos;
import chronos.pdc.character.Gender;
import chronos.pdc.race.Human;
import chronos.pdc.race.Race;
import chronos.pdc.race.Race.MockRace;
import mylib.MsgCtrl;
import mylib.pdc.Utilities;


/**
 * Tests the abstract Race class by implementing a concrete subclass from which the base class
 * methods are called.
 * 
 * @author Alan Cline
 * @version Jun 4, 2009 // original <br>
 *          Jan 18, 2010 // add in non-Human racial tests <br>
 *          August 10, 2017 // updated per QA Tool <br>
 *          August 15, 2017 // protected methods are better tested by subclasses <br>
 *          Sept 24, 2017 // Put statistics into calcHeight() and calcWeight() methods instead
 *          of subclass methods <br>
 *          May 23, 2018 // refactored specific subclass methods into common base class <br>
 */
public class TestRace
{
  // Generate a bunch of values in a loop for stat calculation
  static private int NBR_LOOPS = 1000;
  // Storage of generated values
  private int[] _values = new int[NBR_LOOPS];

  // Create a test object for the Race methods to be called
  static private Human _him;
  static private Human _her;
  // Create a mock to bypass the protected method calls that differs by only data
  static private MockRace _mockHim;
  static private MockRace _mockHer;


  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    _him = (Human) Race.createRace("Human", new Gender("male"), "black");
    assertNotNull(_him);
    _mockHim = _him.new MockRace();
    assertNotNull(_mockHim);

    _her = (Human) Race.createRace("Human", new Gender("female"), "blonde");
    assertNotNull(_her);
    _mockHer = _her.new MockRace();
    assertNotNull(_mockHer);
  }

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    _mockHer = null;
    _mockHer = null;
    _him = null;
    _her = null;
  }

  @Before
  public void setUp()
  {}

  @After
  public void tearDown()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _values = null;
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
   * @Normal.Test double calcVariance(int min, int max) -- Extract a random number from a
   *              Gaussian population.
   * 
   * @param race a male or female so that the object's method can be called
   * @param min minimum value allowed
   * @param max maximum value allowed
   */
  @Test
  public void testCalcVariance()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
    
    // Test values for the sum of two dice
    int min = 2;
    int max = 12;
    
    // Create a mock Human object to call the test method in the Race class
    MockRace mock = _him.new MockRace();
    assertNotNull(mock);
  
    for (int k = 0; k < NBR_LOOPS; k++) {
      _values[k] = mock.calcVariance(min, max); // method adjusts downward for females
      assertTrue((_values[k] >= min) && (_values[k] <= max));
    }
    double expAvg = (max + min) / 2.0;
    double average = Utilities.average(_values);
    int actualMin = Utilities.min(_values);
    int actualMax = Utilities.max(_values);
    MsgCtrl.msg("\t Expected range [" + min + ", " + max + "]");
    MsgCtrl.msgln("\t Expected average = " + expAvg);
    // Verify that average is within one standard deviation, or 68.2%
    MsgCtrl.msg("\t Actual range [" + actualMin + ", " + actualMax + "]");
    MsgCtrl.msgln("\t Actual average = " + average);
    assertEquals(expAvg, average, average * .682);
  }

  /**
   * @Normal.Test Race createRace(String raceName, Gender gender, String hairColor) -- create
   *              Race superclass that can be cast to specific subclasses, like Human
   * @Error.Test Race createRace(String raceName, Gender gender, String hairColor) -- request
   *             to create an unknown Race subclass; throws NullPointerException
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


//  /**
//   * Implementation of a method callable by Race subclass tests
//   * 
//   * @param expMin expected minimum for the population array
//   * @param expMax expected maximum for the population array
//   * @param values population to verify
//   */
//  @Test
//  public void testMinMaxAverage(int expMin, int expMax, int[] values)
//  {
//    MsgCtrl.auditMsgsOn(true);
//    MsgCtrl.errorMsgsOn(true);
//    MsgCtrl.where(this);
//
//    // Create a mock Human object to call the test method in the Race class
//    MockRace mock = _him.new MockRace();
//    assertNotNull(mock);
//
//    // Generate a population to check central tendency
//    for (int k = 0; k < NBR_LOOPS; k++) {
//      assertTrue((values[k] >= expMin) && (values[k] <= expMax));
//    }
//    int min = Utilities.min(values);
//    int max = Utilities.max(values);
//    double avg = Utilities.average(values);
//    double expAvg = (max + min) / 2.0;
//    MsgCtrl.msgln("\t Actual [Min, Max] = [" + min + ", " + max + "]; Average = " + avg);
//    assertEquals(expMin, min);
//    assertEquals(expMax, max);
//    assertEquals(expAvg, avg, avg * Chronos.TOLERANCE);
//  }



} // end of TestRace class
