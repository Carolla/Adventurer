/**
 * TestMetaDie.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use from Carolla
 * Development, Inc. by email: acline@wowway.com
 */

package mylib.test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mylib.ApplicationException;
import mylib.MsgCtrl;
import mylib.pdc.MetaDie;
import mylib.pdc.Utilities;

/**
 * {@code MetaDie} random values are tested by generating a large number of them and checking
 * for the following properties to be true:
 * <ol>
 * <li>The average value of all values in the population is equal to the expected average =
 * (upper-lower)/2, to within a narrow tolerance.</li>
 * <li>The standard deviation (sigma) of all values of a normal population, either generated or
 * by summing multiple calls to {@code getRandom()}, is within a narrow tolerance. 95% of the
 * population generated must lie within 2 sigmas.</li>
 * <li>The values have no significant trending, that is, there should not be a long set of
 * closely-valued numbers.</li>
 * </ol>
 * The Apache {@code MersenneTwister} class is used as a randomizer instead of the Java
 * randomizer from the standard library.
 * 
 * @author Alan Cline
 * @version Dec 12, 2010 // original <br>
 *          Sep 20, 2014 // trying to cut down the number of times a method fails for random
 *          values out of range <br>
 *          July 4, 2017 // autogen: QA Tool added missing test methods <br>
 *          July 5, 2017 // autogen: QA Tool added missing test methods <br>
 *          July 9, 2017 // autogen: QA Tool added missing test methods <br>
 *          Aug 18, 2017 // added tests for some refactored methods <br>
 *          Sept 5, 2017 // autogen: QA Tool added missing test methods <br>
 *          Sept 11, 2017 // added a nextGaussian() call for 0-based normal populations <br>
 *          Sept 23, 2017 // added stats to rollVariance() needed with nextGaussion() <br>
 *          Mar 17, 2018 // moved to Eclipse Oxygen, JUnit 5 <br>
 *          Apr 16, 2018 // replaced Java Random class with Apache MersenneTwister class <br>
 */
public class TestMetaDie
{
  /** Non-repeating random generator */
  private MetaDie _md;
  /** Default value for number of values to put into random distribution */
  private int LOOP_COUNT = 100000;

  /** Storage of generated values */
  private int[] _values = new int[LOOP_COUNT];

  /** Default trait range for a Hero, before gender or racial adjustments */
  private final int MIN_TRAIT = 8;
  private final int MAX_TRAIT = 18;


  @BeforeAll
  public static void setUpBeforeClass() throws Exception
  {}

  @AfterAll
  public static void tearDownAfterClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @BeforeEach
  public void setUp() throws Exception
  {
    _md = new MetaDie();
    assertNotNull(_md);
    _values = new int[LOOP_COUNT];
  }

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @AfterEach
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _md = null;
    _values = null;
  }


  // --------------------------------------------------------------------------------------------------------------
  // Let the Testing Begin!
  // --------------------------------------------------------------------------------------------------------------

  /**
   * @Normal.Test getRandom(int minRange, maxRange) -- Verify random numbers summed are
   *              calculated as expected
   */
  @Test
  public void testGetRandom()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Zero values are not appropriate for this method
    int[] mins = {1, 2, 6, 10};
    int[] maxs = {2, 6, 10, 12, 20, 100};

    // Build population first
    for (int p = 0; p < mins.length; p++) {
      for (int m = 0; m < maxs.length; m++) {
        // Ensure that Normal test always uses min less than max
        if (mins[p] < maxs[m]) {
          for (int k = 0; k < LOOP_COUNT; k++) {
            _values[k] = _md.getRandom(mins[p], maxs[m]);
          }
          // Now perform statistics on the population
          double avg = calcAverage(_values);
          double expAvg = (mins[p] + maxs[m]) / 2.0;
          int range = maxs[m] - mins[p];
          printStats(mins[p], maxs[m], avg, expAvg);
          // Verify that delta is less than 2%
          assertEquals(expAvg, avg, range * 0.02);
        }
      }
    }
  }


  /**
   * @Error.Test getRandom(int minRange, maxRange) -- throws exceptions for bad parms
   */
  @Test
  public void testGetRandom_Error()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Lower bound greater than upper bound
    try {
      _md.getRandom(12, 5);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (IllegalArgumentException e) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + e.getMessage());
    }
    // Upper bound not greater than lower bound
    try {
      _md.getRandom(12, 12);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (IllegalArgumentException e) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + e.getMessage());
    }
    // Parms can only be positive
    try {
      _md.getRandom(-1, 12);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (IllegalArgumentException e) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + e.getMessage());
    }
    try {
      _md.getRandom(-5, -10);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (IllegalArgumentException e) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + e.getMessage());
    }
    // Parms can only be positive
    try {
      _md.getRandom(0, 10);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (IllegalArgumentException e) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + e.getMessage());
    }
  }


  /**
   * Not Implemented: Wrapper over randomizer call
   */
  @Test
  public void testIsOdd()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.WRAPPER);
  }


  /**
   * @Normal.Test roll(int nbrDice, int nbrSides) -- Random numbers average as expected for
   *              typical and atypical rolls
   */
  @Test
  public void testRoll()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // The 100 die roll cause excessive variation in distribution, as reflected by average
    int[] nbrDice = {1, 2, 3, 4, 6};
    int[] nbrSides = {2, 4, 6, 8, 10, 12, 20, 100};

    for (int p = 0; p < nbrDice.length; p++) {
      for (int m = 0; m < nbrSides.length; m++) {
        // Ensure that min is always less than max
        int maxVal = 0;
        int minVal = 100;
        int upperBound = nbrDice[p] * nbrSides[m];
        for (int k = 0; k < LOOP_COUNT; k++) {
          _values[k] = _md.roll(nbrDice[p], nbrSides[m]);
          minVal = Math.min(minVal, _values[k]);
          maxVal = Math.max(maxVal, _values[k]);
        }
        // Now perform statistics on the population
        double avg = calcAverage(_values);
        double expAvg = (nbrDice[p] + upperBound) / 2.0;
        printStats(minVal, maxVal, avg, expAvg);
        // Verify that delta is less than 2%
        int range = maxVal - minVal;
        assertEquals(expAvg, avg, range * 0.02);
      }
    }
  }


  /**
   * @Error.Test roll(int nbrDice, int nbrSides) -- parms required: 1 die, 2 sides minimum
   */
  @Test
  public void testRoll_Error()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // There must be at least one die
    Throwable thrown = Assertions.assertThrows(IllegalArgumentException.class,
        () -> _md.roll(0, 6));
    MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + thrown.getMessage());

    thrown = Assertions.assertThrows(IllegalArgumentException.class,
        () -> _md.roll(1, 1));
    MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + thrown.getMessage());

    thrown = Assertions.assertThrows(IllegalArgumentException.class,
        () -> _md.roll(1, -1));
    MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + thrown.getMessage());
  }


  /**
   * @Normal.Test int rollPecent() -- roll linear number between 1 and 100
   */
  @Test
  public void testRollPercent()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    for (int k = 0; k < LOOP_COUNT; k++) {
      _values[k] = _md.rollPercent();
      assertTrue((_values[k] >= 1) && (_values[k] <= 100));
    }
    double avg = calcAverage(_values);
    double expAvg = (1 + 100) / 2.0;
    printStats(1, 100, avg, expAvg);
    assertEquals(avg, expAvg, 99 * 0.02);
  }


  /**
   * @Normal.Test roll(String notation) -- Test that this method is the same as other roll
   *              methods above, except using d20 notation.
   */
  @Test
  public void testRoll_StringNotation()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Most likely die roll choices
    String[] dice =
        {"1d2", "1d4", "1d6", "1d8", "d10", "d20", "d100",
            "2d6", "2d10", "3d4", "4d6", "2d10", "2d4+1"};
    int[] minVal = {1, 1, 1, 1, 1, 1, 1, 2, 2, 3, 4, 2, 3};
    int[] maxVal = {2, 4, 6, 8, 10, 20, 100, 12, 20, 12, 24, 20, 9};
    double[] expAvg = {1.5, 2.5, 3.5, 4.5, 5.5, 10.5, 50.5, 7.0, 11.0, 7.5, 14.0, 11.0, 6.0};

    for (int m = 0; m < dice.length; m++) {
      for (int k = 0; k < LOOP_COUNT; k++) {
        _values[k] = _md.roll(dice[m]);
      }
      double avg = calcAverage(_values);
      // Now perform statistics on the population
      int range = maxVal[m] - minVal[m];
      printStats(minVal[m], maxVal[m], avg, expAvg[m]);
      // Verify that delta is less than 2%
      assertEquals(expAvg[m], avg, range * 0.02);
    }
  }


  /**
   * @Error.Test roll(String notation) -- Catch exception if non-positive range is found
   */
  @Test
  public void testRoll_StringNotation_Error()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Error: Try negative and 0 range numbers
    String[] dice = {"2d6-2", "2d6-10", "d4-1"};

    for (int m = 0; m < dice.length; m++) {
      try {
        _md.roll(dice[m]);
        fail(MsgCtrl.EXCEPTION_NOT_THROWN);
      } catch (ApplicationException ex) {
        MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + ex.getMessage());
      }
    }
  }


  // /**
  // * @Normal.Test int rollVariance(double low, double average) -- Gaussian value about the
  // * average, within the range defined by low
  // */
  // @Test
  // public void testRollVariance()
  // {
  // MsgCtrl.auditMsgsOn(true);
  // MsgCtrl.errorMsgsOn(true);
  // MsgCtrl.where(this);
  //
  // double[] lows = {2.0, 3.0, 80.0, 10.0};
  // double[] avgs = {7.0, 10.5, 100.0, 25.0};
  // int count = LOOP_COUNT;
  //
  // // Traverse the input array
  // int expHigh = -1;
  // for (int n = 0; n < lows.length; n++) {
  // expHigh = (int) (2 * avgs[n] - lows[n]);
  // // Single calls of input arrays
  // for (int k = 0; k < count; k++) {
  // _values[k] = _md.rollVariance(lows[n], avgs[n]);
  // }
  // double avg = Utilities.average(_values);
  // double min = Utilities.min(_values);
  // double max = Utilities.max(_values);
  // double range = max - min;
  // MsgCtrl.msg("\t Expected range\t [" + lows[n] + ", " + (double) expHigh + "]");
  // MsgCtrl.msgln("\t Expected average\t = " + avgs[n]);
  // MsgCtrl.msg("\t Actual range \t[" + min + ", " + max + "]");
  // MsgCtrl.msgln("\t Actual average = \t" + pointFmt(avg, 2));
  // // Values should be within 2%
  // assertEquals(lows[n], min, range * 0.02);
  // assertEquals(expHigh, max, range * 0.02);
  // assertEquals(avgs[n], avg, range * 0.02);
  // }
  // }


  /**
   * @Normal.Test int rollVariance(double low, double average) -- Gaussian value about the
   *              average, within the range defined by low
   */
  @Test
  public void testRollVariance()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    int[] lows = {2, 3, 80, 10};
    int[] highs = {12, 18, 100, 25};
    int count = LOOP_COUNT;

    // Traverse the input array
    double expAvg = 0.0;
    for (int n = 0; n < lows.length; n++) {
      expAvg = (lows[n] + highs[n]) / 2.0;
      // Single calls of input arrays
      for (int k = 0; k < count; k++) {
        _values[k] = _md.rollVariance(lows[n], highs[n]);
      }
      double avg = Utilities.average(_values);
      MsgCtrl.msg("\t Expected range: [" + lows[n] + ", " + highs[n] + "]");
      MsgCtrl.msg("\t Expected average: " + expAvg);
      MsgCtrl.msgln("\t Actual average = " + pointFmt(avg, 2));
      // Values should be within 2%
      assertEquals(expAvg, avg, avg * 0.02);
    }
  }

  /**
   * @Normal.Test int rollVariance(double low, double average) -- parm invalid to throw
   *              exceptions
   */
  @Test
  public void testRollVariance_Error()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Negative parms
    try {
      _md.rollVariance(-5, 20);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (IllegalArgumentException e) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + e.getMessage());
    }
    try {
      _md.rollVariance(-15, -20);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (IllegalArgumentException e) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + e.getMessage());
    }
    // Average lower than low parn
    try {
      _md.rollVariance(25, 20);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (IllegalArgumentException e) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + e.getMessage());
    }
  }


  /**
   * @Normal.Test int rollCharTrait() -- Returns the sum of highest three dice rolls out of
   *              four: 4d6 - (lowest) d6: range [8,18], average = 12.5. <br>
   *              NOTE: For populations less than 300 values, a normality test is inaccurate,
   *              even though it is expected that 4d6 - (lowest) d6 is normal.
   */
  @Test
  public void testRollCharTrait()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    double expAvg = 12.5;
    int trait = 0;
    int min = 99;
    int max = -1;

    // Roll traits and keep min, max and average values
    for (int k = 0; k < LOOP_COUNT; k++) {
      trait = _md.rollCharTrait(MIN_TRAIT, MAX_TRAIT);
      _values[k] = trait;
      min = Math.min(min, trait);
      max = Math.max(max, trait);
      assertTrue("Trait " + trait + " too low", trait >= MIN_TRAIT);
      assertTrue("Trait " + trait + " too high", trait <= MAX_TRAIT);
    }
    double average = calcAverage(_values);
    printStats(min, max, average, expAvg);
    assertEquals(expAvg, average, (max - min) * 0.5);
  }


  /**
   * @Normal.Test rollTraits() -- roll six char traits within [8,18] each
   */
  @Test
  public void testRollTraits()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    int NBR_TRAITS = 6;
    int LOW_TRAIT = 8;
    int HI_TRAIT = 18;

    // int LIMIT = 1000000;
    int[] valueSet = new int[NBR_TRAITS];
    // bin[n] holds count of how many times a particular value [8,18] was rolled
    int[] bin = new int[11];
    double AVG = 12.6;
    double sum = 0;

    // Check each Trait's average and range
    for (int k = 0; k < LOOP_COUNT; k++) {
      // Get a set of traits
      valueSet = _md.rollTraits();
      // Check each value to be in range
      for (int n = 0; n < NBR_TRAITS; n++) {
        assertTrue((valueSet[n] <= HI_TRAIT) && (valueSet[n] >= LOW_TRAIT));
        // Tally all trait values
        sum += valueSet[n];
        // Stick the trait into the bin counter
        int index = valueSet[n] - LOW_TRAIT;
        bin[index]++;
      }
    }
    // Find average trait across all 6 traits rolled LIMIT times
    double avg = sum / (LOOP_COUNT * NBR_TRAITS);
    MsgCtrl.msgln("\t Average of char trait set = " + avg);
    assertEquals(AVG, avg, .5);
    // Print the bin values
    String[] lbl = {"8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18"};
    for (int k = 0; k < 11; k++) {
      MsgCtrl.msg("\t " + lbl[k]);
      double binAvg = (double) bin[k] / LOOP_COUNT;
      MsgCtrl.msg("\t " + String.format("%.3f", binAvg));
    }
  }


  /**
   * @Not.Implemented Only here to cover QA Tool for overloaded methods
   */
  @Test
  public void testRoll1()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.OVERLOADED);
  }

  /**
   * @Not.Implemented Only here to cover QA Tool for overloaded methods
   */
  @Test
  public void testRoll2()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.OVERLOADED);
  }


  // --------------------------------------------------------------------------------------------------------------
  // Private Methods
  // --------------------------------------------------------------------------------------------------------------

  /**
   * Return the average for a given population of numbers
   * 
   * @param population of numbers to process
   * @return the average of the population
   */
  private double calcAverage(int[] population)
  {
    int sum = 0;
    int popsize = population.length;
    for (int k = 0; k < popsize; k++) {
      sum += population[k];
    }
    return (double) sum / popsize;
  }


  /**
   * Utility to format a double variable for readability
   * 
   * @param x the value to format
   * @param decFrac the number of digits to the right of the decimal point
   * @return the formatted resutl
   */
  private String pointFmt(double x, int decFrac)
  {
    String fmtStr = "%." + decFrac + "f";
    return String.format(fmtStr, x);
  }


  /**
   * Print the range, average, expected average, and delta of the two
   * 
   * @param min minimum value for the population
   * @param max maximum value for the population
   * @param avg average value for the population
   * @param expAvg calculated expected average for the population
   */
  private void printStats(int min, int max, double avg, double expAvg)
  {
    double delta = Math.abs(avg - expAvg);
    String fmtDelta = pointFmt(delta, 4);

    MsgCtrl.msg("\t Range: [" + min + ", " + max + "] \t Avg = " + avg);
    MsgCtrl.msgln("\t Expected Avg = " + expAvg + "\tDelta = " + fmtDelta);
  }


} // end of TestMetaDie class
