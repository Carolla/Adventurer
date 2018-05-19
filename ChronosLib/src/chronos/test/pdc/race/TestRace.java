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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
 *          Sept 24, 2017 // Put statistics into calcHeight() and calcWeight() methods instead of
 *          subclass methods <br>
 */
public class TestRace
{
  // Generate a bunch of values in a loop for stat calculation
  private int NBR_LOOPS = 1000;
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
  {
    // Create storage for stats
    _values = new int[NBR_LOOPS];
  }

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
   * @Normal.Test int calcHeight(double low, double average) -- ensure that proper distribution of
   *              heights and means are generated for a few sample Heights. MockRace is called to
   *              bypass the overridden data-driven methods.
   */
  @Test
  public void testCalcVariance_Stats()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    int maleMin = 49;
    int maleMax = 59;
    double expMaleAvg = (maleMin + maleMax) / 2.0;

    // Height range for males [49, 59]
    // Generate a number of weights to check central tendency
    MsgCtrl.msg("\t Expected male height in inches [" + maleMin + ", " + maleMax + "];");
    MsgCtrl.msgln("\t Expected Average = " + expMaleAvg);
    for (int k = 0; k < NBR_LOOPS; k++) {
      _values[k] = _mockHim.calcVariance(maleMin, maleMax);
      assertTrue((_values[k] >= maleMin) && (_values[k] <= maleMax));
    }
    double[] results = Utilities.getStats(_values);
    double avg = results[0];
    int min = (int) results[1];
    int max = (int) results[2];
    MsgCtrl.msgln("\t Actual [Min, Max] = [" + min + ", " + max + "]; Average = " + avg);
    assertEquals(expMaleAvg, results[0], 0.5);
    assertEquals(maleMin, (int) results[1]);
    assertEquals(maleMax, (int) results[2]);

    // Height range for females = 90% of males: [44, 53]
    int femaleMin = (int) (Math.round(0.90 * maleMin));
    int femaleMax = (int) (Math.round(0.90 * maleMax));
    double expFemaleAvg = (femaleMin + femaleMax) / 2.0;
    MsgCtrl.msg("\t Expected female height in inches [" + femaleMin + ", " + femaleMax + "];");
    MsgCtrl.msgln("\t Expected Average = " + expFemaleAvg);
    for (int k = 0; k < NBR_LOOPS; k++) {
      // Use male values since females values are automatically adjusted
      _values[k] = _mockHer.calcVariance(maleMin, maleMax);
      assertTrue((_values[k] >= femaleMin) && (_values[k] <= femaleMax));
    }
    results = Utilities.getStats(_values);
    avg = results[0];
    min = (int) results[1];
    max = (int) results[2];
    MsgCtrl.msgln("\t Actual [Min, Max] = [" + min + ", " + max + "]; Average = " + avg);
    assertEquals(expFemaleAvg, results[0], 0.5);
    assertEquals(femaleMin, (int) results[1]);
    assertEquals(femaleMax, (int) results[2]);
  }


//  /**
//   * @Normal.Test int calcWeight(double low, double average) -- ensure that proper distribution of
//   *              weights and means are generated for a few sample weights. MockRace is called to
//   *              bypass the overridden data-driven methods.
//   */
//  @Test
//  public void testCalcWeight()
//  {
//    MsgCtrl.auditMsgsOn(false);
//    MsgCtrl.errorMsgsOn(false);
//    MsgCtrl.where(this);
//
//    int maleMin = 100;
//    int maleMax = 250;
//    double expMaleAvg = (maleMin + maleMax) / 2.0;
//
//    // Weight range for males [100, 250]
//    // Generate a number of weights to check central tendency
//    MsgCtrl.msg("\t Expected male weight in lbs [" + maleMin + ", " + maleMax + "];");
//    MsgCtrl.msgln("\t Expected Average = " + expMaleAvg);
//    for (int k = 0; k < NBR_LOOPS; k++) {
//      _values[k] = _mockHim.calcWeight(maleMin, expMaleAvg);
//      assertTrue((_values[k] >= maleMin) && (_values[k] <= maleMax));
//    }
//    double[] results = Utilities.getStats(_values);
//    double avg = results[0];
//    int min = (int) results[1];
//    int max = (int) results[2];
//    MsgCtrl.msgln("\t Actual [Min, Max] = [" + min + ", " + max + "]; Average = " + avg);
//    // Check avg within 1%
//    assertEquals(expMaleAvg, results[0], expMaleAvg / 100.0);
//    assertEquals(maleMin, (int) results[1]);
//    assertEquals(maleMax, (int) results[2]);
//
//    // Weight range for females = 90% of males: [90, 225]
//    int femaleMin = (int) (Math.round(0.90 * maleMin));
//    int femaleMax = (int) (Math.round(0.90 * maleMax));
//    double expFemaleAvg = (femaleMin + femaleMax) / 2.0;
//    MsgCtrl.msg("\t Expected female weight in lb [" + femaleMin + ", " + femaleMax + "];");
//    MsgCtrl.msgln("\t Expected Average = " + expFemaleAvg);
//    for (int k = 0; k < NBR_LOOPS; k++) {
//      // Use male values since females values are automatically adjusted
//      _values[k] = _mockHer.calcWeight(maleMin, expMaleAvg);
//      assertTrue((_values[k] >= femaleMin) && (_values[k] <= femaleMax));
//    }
//    results = Utilities.getStats(_values);
//    avg = results[0];
//    min = (int) results[1];
//    max = (int) results[2];
//    MsgCtrl.msgln("\t Actual [Min, Max] = [" + min + ", " + max + "]; Average = " + avg);
//    // Check avg within 1%
//    assertEquals(expFemaleAvg, results[0], expFemaleAvg / 100.0);
//    assertEquals(femaleMin, (int) results[1]);
//    assertEquals(femaleMax, (int) results[2]);
//  }


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


  /**
   * Check a subrace's height, providing the desired range and checking against the expected range
   * Tests are run in a loop several times for thoroughness. The parms allow the subrace's protected
   * methods to be used with test values, instead of the values characteristic of the subrace.
   * Asserts are used to confirm that the height falls within the proper subrace range.
   * 
   * @param race a male or female so that the object's method can be called
   * @param min minimum value allowed
   * @param max maximum value allowed
   */
  static public void baseTestCalcHeight(Race race, int min, int max)
  {
    // Create a mock race to call the test method in the Race class
    MockRace mock = race.new MockRace();
    assertNotNull(mock);
    
    // Adjust exp values for female races
    int adjMin = min;
    int adjMax = max;
    
    // Adjust upward to compensate for the gender-adjusting calcHeight() method
    if (race.getGender().equals(Gender.FEMALE_STRING)) {
      adjMin =  (int) (min / 0.90);    // int arithmetic puts value on lower side
      adjMax = (int) Math.round(max / 0.90);  // rounding puts value on higher side
    }
    // RUN Call a few times to ensure proper ranges are maintained
    MsgCtrl.msgln("\t Expected range [" + min + ", " + max + "]");
    for (int k = 0; k < 10; k++) {
      int var = mock.calcVariance(adjMin, adjMax);  // method adjusts downward for females
      MsgCtrl.msg("\t" + var);
      assertTrue((var >= min) && (var <= max));
    }
    MsgCtrl.msgln("");
  }


  /**
   * Check a subrace's height, providing the desired range and checking against the expected range
   * Tests are run in a loop several times for thoroughness. The parms allow the subrace's protected
   * methods to be used with test values, instead of the values characteristic of the subrace.
   * Asserts are used to confirm that the height falls within the proper subrace range.
   * 
   * @param race a male or female so that the object's method can be called
   * @param min minimum value allowed
   * @param max maximum value allowed
   */
  static public void baseTestCalcVariance(Race race, int min, int max)
  {
    // Create a mock race to call the test method in the Race class
    MockRace mock = race.new MockRace();
    assertNotNull(mock);
    
    // Adjust exp values for female races
    int expMin = min;
    int expMax = max;
    
    // Adjust upward to compensate for the gender-adjusting calcHeight() method
    if (race.getGender().equals(Gender.FEMALE_STRING)) {
      expMin =  (int) (min / 0.90);    // int arithmetic puts value on lower side
      expMax = (int) Math.round(max / 0.90);  // rounding puts value on higher side
    }
    // RUN Call a few times to ensure proper ranges are mainained
    double avg = (expMin + expMax) / 2.0;
    MsgCtrl.msgln("\t Expected range [" + min + ", " + max + "]");
    for (int k = 0; k < 10; k++) {
      int height = mock.calcVariance(expMin, expMax); 
      MsgCtrl.msg("\t" + height);
      assertTrue((height >= min) && (height <= max));
    }
    MsgCtrl.msgln("");
  }

  
 
} // end of TestRace class
