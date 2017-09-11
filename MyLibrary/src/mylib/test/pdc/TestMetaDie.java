/**
 * TestMetaDie.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@wowway.com
 */


package mylib.test.pdc;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mylib.ApplicationException;
import mylib.MsgCtrl;
import mylib.pdc.MetaDie;


/**
 * Ensure that the {@code MetaDie} class is working properly. {@code MetaDie} random values are
 * tested by generating a large number of them and checking for the following properties to be true:
 * <ol>
 * <li>The average value of all values in the population is equal to the expected average =
 * (upper-lower)/2, to within a narrow tolerance.</li>
 * <li>The standard deviation (sigma) of all values of a normal population, either generated from
 * {@code getRandomGaussian()} or by summing multiple calls to {@code getRandom()}, is within a
 * narrow tolerance. 68% of all values must lie within 1 sigma; 95% must lie within 2 sigma.</li>
 * <li>The values have no significant trendling, that is, there should not be a long set of
 * closely-valued numbers.</li>
 * </ol>
 * 
 * @author Alan Cline
 * @version Dec 12, 2010 // original <br>
 *          Sep 20, 2014 // trying to cut down the number of times a method fails for random values
 *          out of range <br>
 *          July 4, 2017 // autogen: QA Tool added missing test methods <br>
 *          July 5, 2017 // autogen: QA Tool added missing test methods <br>
 *          July 9, 2017 // autogen: QA Tool added missing test methods <br>
 *          Aug 18, 2017 // added tests for some refactored methods <br>
 *          September 5, 2017 // autogen: QA Tool added missing test methods <br>
 *          September 11, 2017 // added a nextGaussian() call for 0-based normal populations <br>
 */
public class TestMetaDie
{
  /** Non-repeating random generator */
  private MetaDie _md;
  /** Default value for number of values to put into random distribution */
  private int LOOP_COUNT = 100000;

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @Before
  public void setUp() throws Exception
  {
    _md = new MetaDie();
    assertNotNull(_md);
  }

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _md = null;
  }


  // --------------------------------------------------------------------------------------------------------------
  // Let the Testing Begin!
  // --------------------------------------------------------------------------------------------------------------

  /**
   * @Normal.Test MetaDie.getRandom(int minRange, maxRange) -- Verify random numbers summed are
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

    for (int p = 0; p < mins.length; p++) {
      for (int m = 0; m < maxs.length; m++) {
        // Ensure that Normal test always uses min less than max
        if (mins[p] < maxs[m]) {
          double sum = 0;
          int maxVal = 0;
          int minVal = 100;
          for (int k = 0; k < LOOP_COUNT; k++) {
            int value = _md.getRandom(mins[p], maxs[m]);
            assertTrue((value >= mins[p]) && (value <= maxs[m]));
            minVal = Math.min(minVal, value);
            maxVal = Math.max(maxVal, value);
            sum += value;
          }
          double expAvg = (maxs[m] + mins[p]) / 2.0;
          double actAvg = sum / LOOP_COUNT;
          MsgCtrl.msg("\t Rolling (" + mins[p] + ", " + maxs[m] + "): ");
          MsgCtrl.msg("\t Lo/Hi [" + minVal + ", " + maxVal + "] ");
          MsgCtrl.msg("\t Average = " + actAvg);
          MsgCtrl.msgln(" (" + expAvg + ")");
          assertEquals(expAvg, actAvg, 0.3);
        }
      }
    }
  }


  // /**
  // * @Normal.Test MetaDie.getRandomAnitTrend(int minRange, int maxRange) -- Verify that random
  // * numbers do not trend, and fluctuate appropriately
  // */
  // @Test
  // public void testGetRandom_AntiTrend()
  // {
  // MsgCtrl.auditMsgsOn(false);
  // MsgCtrl.errorMsgsOn(false);
  // MsgCtrl.where(this);
  //
  // // Hold latest five values
  // int NBR_VALUES = 1000;
  // int[] linVal = new int[NBR_VALUES];
  // int[] diff = new int[NBR_VALUES - 1];
  // int min = 99;
  // int max = -1;
  // int sum = 0;
  // double avg = 0.0;
  // double expAvg = 50.5; // for a population from [1,100]
  //
  // // Generate many numbers
  // MsgCtrl.msg("\t " + NBR_VALUES + " values generated");
  // for (int k = 0; k < linVal.length; k++) {
  // linVal[k] = _md.getRandomAntiTrend(1, 100);
  // min = Math.min(linVal[k], min);
  // max = Math.max(linVal[k], max);
  // sum += linVal[k];
  // }
  // avg = sum / NBR_VALUES;
  // MsgCtrl.msgln(" with average of " + point3Fmt(avg) + " in range [" + min + ", " + max + "]");
  // // Average tolerance may be large when NBR_VALUES is small
  // assertEquals(expAvg, avg, 1.0);
  // assertEquals(min, 1);
  // assertEquals(max, 100);
  //
  // int diffSum = 0;
  // for (int k = 0; k < linVal.length - 1; k++) {
  // diff[k] = linVal[k + 1] - linVal[k];
  // diffSum += diff[k];
  // }
  // double diffAvg = diffSum / diff.length;
  // MsgCtrl.msgln("\t Sum of all sequential differences = " + diffAvg);
  // // Check for less than 1% variance
  // assertEquals(diffAvg, 0, 1.0);
  // }


  /**
   * @Error.Test MetaDie.getRandom(int minRange, maxRange) -- throws exceptions for bad parms
   */
  @Test
  public void testGetRandom_Error()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Use lower bound greater than upper bound
    try {
      _md.getRandom(12, 5);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (IllegalArgumentException e) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + e.getMessage());
    }
    // Use lower bound equal to upper bound
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
    try {
      _md.getRandom(0, 10);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (IllegalArgumentException e) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + e.getMessage());
    }
  }


  /**
   * @Normal.Test MetaDie.getRandom(int minRange, int maxRange) -- Verify numbers fluctuate
   *              properly, without trending; e.g., sum of differences between values are 0.
   */
  @Test
  public void testGetRandom_TrendCheck()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Hold latest five values
    int NBR_VALUES = LOOP_COUNT;
    int[] linVal = new int[NBR_VALUES];
    int[] diff = new int[NBR_VALUES - 1];
    int min = 99;
    int max = -1;
    int sum = 0;
    double avg = 0.0;
    double expAvg = 50.5; // for a population from [1,100]

    // Generate many numbers
    MsgCtrl.msg("\t " + NBR_VALUES + " values generated");
    for (int k = 0; k < linVal.length; k++) {
      linVal[k] = _md.getRandom(1, 100);
      min = Math.min(linVal[k], min);
      max = Math.max(linVal[k], max);
      sum += linVal[k];
    }
    avg = sum / (double) NBR_VALUES;
    MsgCtrl.msgln(" with average of " + pointFmt(avg, 3) + " in range [" + min + ", " + max + "]");
    // Average tolerance may be large when NBR_VALUES is small
    assertEquals(expAvg, avg, 1.0);
    assertEquals(min, 1);
    assertEquals(max, 100);

    int diffSum = 0;
    for (int k = 0; k < linVal.length - 1; k++) {
      diff[k] = linVal[k + 1] - linVal[k];
      diffSum += diff[k];
    }
    double diffAvg = (double) diffSum / (double) diff.length;
    MsgCtrl.msgln("\t Sum of all sequential differences = " + pointFmt(diffAvg, 4));
    // Check for less than 1% variance
    assertEquals(diffAvg, 0, 0.1);
  }


  // /**
  // * @Normal.Test double getRandomGaussian(double maxRange) -- produce random results
  // symmetrically
  // * around the median given.
  // */
  // @Test
  // public void testGetRandomGaussian()
  // {
  // MsgCtrl.auditMsgsOn(false);
  // MsgCtrl.errorMsgsOn(false);
  // MsgCtrl.where(this);
  //
  // double[] median = {0.0, 1.0, 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 20.0, 100.0};
  //
  // for (int p = 0; p < median.length; p++) {
  // double sum = 0;
  // double maxVal = 0;
  // double minVal = 100;
  // for (int k = 0; k < LOOP_COUNT; k++) {
  // double value = _md.getRandomGaussian(median[p], false);
  // minVal = Math.min(minVal, value);
  // maxVal = Math.max(maxVal, value);
  // sum += value;
  // }
  // double actAvg = sum / LOOP_COUNT;
  // String actAvgStr = String.format("%.3f", actAvg);
  // String minValStr = String.format("%.3f", minVal);
  // String maxValStr = String.format("%.3f", maxVal);
  // MsgCtrl.msg("\t Median " + median[p] + " : ");
  // MsgCtrl.msg("\t Lo/Hi [" + minValStr + ", " + maxValStr + "] ");
  // MsgCtrl.msgln("\t Average = " + actAvgStr);
  //
  // assertTrue(Math.abs(actAvg) <= 0.50);
  // }
  // }
  //
  //
  // /**
  // * @Error.Test double getRandomGaussian(double maxRange) -- negative and zero median
  // */
  // @Test
  // public void testGetRandomGaussian_Error()
  // {
  // MsgCtrl.auditMsgsOn(false);
  // MsgCtrl.errorMsgsOn(false);
  // MsgCtrl.where(this);
  //
  //// // Median is zero should be ok
  //// double median = 0.0;
  //// try {
  //// _md.getRandomGaussian(median, false);
  //// fail(MsgCtrl.EXCEPTION_NOT_THROWN);
  //// } catch (IllegalArgumentException e) {
  //// MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + e.getMessage());
  //// }
  //
  // // Median is negative
  // double median = -1.0;
  // try {
  // _md.getRandomGaussian(median, false);
  // fail(MsgCtrl.EXCEPTION_NOT_THROWN);
  // } catch (IllegalArgumentException e) {
  // MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + e.getMessage());
  // }
  // }
  //
  //
  // /**
  // * Check which is better: summing multiple {@code getRandom()} values or a
  // * {@code getRandomGaussian()} calls.
  // */
  // @Test
  // public void testRandomSumVsGaussian()
  // {
  // MsgCtrl.auditMsgsOn(false);
  // MsgCtrl.errorMsgsOn(false);
  // MsgCtrl.where(this);
  //
  // int NBR_TRIES = 100;
  // // Init linear stats
  // int sumLinear = 0;
  // int linVal = 0;
  // int linMin = 99;
  // int linMax = -1;
  // // Init Gaussian stats
  // double normVal = 0.0;
  // double sumGaussian = 0.0;
  // double normMin = 99.0;
  // double normMax = -1.0;
  //
  // // Add the sum of two linear calls to get an Gaussian distribution
  // for (int k = 0; k < NBR_TRIES; k++) {
  // // Sum of two dice
  // linVal = _md.getRandom(1, 6) + _md.getRandom(1, 6);
  // linMin = Math.min(linVal, linMin);
  // linMax = Math.max(linVal, linMax);
  // sumLinear += linVal;
  //
  // normVal = _md.getRandomGaussian(7.0, true);
  // normMin = Math.min(normVal, normMin);
  // normMax = Math.max(normVal, normMax);
  // sumGaussian += normVal;
  // }
  // double avgLinear = sumLinear / NBR_TRIES;
  // MsgCtrl.msgln("\t Linear average of " + point3Fmt(avgLinear)
  // + " in range [" + linMin + ", " + linMax + "]");
  // double avgGaussian = sumGaussian / NBR_TRIES;
  // MsgCtrl.msgln("\t Gaussian average of " + point3Fmt(avgGaussian)
  // + " in range [" + point3Fmt(normMin) + ", " + point3Fmt(normMax) + "]");
  //
  // }


  /**
   * @Normal.Test roll(int nbrDice, int nbrSides) -- Random numbers average as expected for typical
   *              and atypical rolls
   */
  @Test
  public void testRoll()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    // int NBR_LOOPS = 10000;

    // The 100 die roll cause excessive variation in distribution, as reflected by average
    // int[] nbrDice = {1, 2, 3, 4, 6, 20, 100}; // 6 groups of dice
    int[] nbrDice = {1, 2, 3, 4, 6, 10, 20};
    int[] nbrSides = {2, 4, 6, 8, 10, 12, 20, 100};

    for (int p = 0; p < nbrDice.length; p++) {
      for (int m = 0; m < nbrSides.length; m++) {
        // Ensure that min is always less than max
        double sum = 0;
        int maxVal = 0;
        int minVal = 100;
        int upperBound = nbrDice[p] * nbrSides[m];
        for (int k = 0; k < LOOP_COUNT; k++) {
          int value = _md.roll(nbrDice[p], nbrSides[m]);
          assertTrue((value >= nbrDice[p]) && (value <= upperBound));
          minVal = Math.min(minVal, value);
          maxVal = Math.max(maxVal, value);
          sum += value;
        }
        double expAvg = (nbrDice[p] + upperBound) / 2.0;
        double actAvg = sum / LOOP_COUNT;
        MsgCtrl.msg("\t Rolling " + nbrDice[p] + "d" + nbrSides[m] + " : ");
        MsgCtrl.msg("\t Lo/Hi [" + minVal + ", " + maxVal + "] ");
        MsgCtrl.msg("\t Average = " + actAvg);
        MsgCtrl.msg(" (" + expAvg + ")");
        // Average in distribution may be off by 2%
        double diff = Math.abs(expAvg - actAvg);
        String diffStr = String.format("%.3f", diff);
        MsgCtrl.msg("\t diff = " + diffStr);
        // Less than 1.0% variance approach constrains large numbers more than small ones
        double variance = diff / actAvg * 100.0;
        String varStr = String.format("%.3f", variance);
        MsgCtrl.msgln("\t (var = " + varStr + " %)");
        assertTrue(variance <= 2.0);
      }
    }
  }


  /**
   * @Error.Test MetaDie.roll(int nbrDice, int nbrSides) -- parms required: 1 die, 2 sides minimum
   */
  @SuppressWarnings("unused")
  @Test
  public void testRoll_Error()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Die must have at least 2 sides
    try {
      int value = _md.roll(1, -1);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (IllegalArgumentException ex1) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + ex1.getMessage());
    }
    // There must be at least one die
    try {
      int value = _md.roll(0, 6);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (IllegalArgumentException ex2) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + ex2.getMessage());
    }
  }


  /**
   * @Normal.Test MetaDie.roll(String notation) -- Test that this method is the same as above other
   *              roll methods, except using d20 notation. Only test that the string returns the
   *              correct number of dice and sides
   */
  @Test
  public void testRoll_StringNotation()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // First ten, then four more on next line
    String[] dice = {"1d2", "1d4", "1d6", "1d8", "d10", "d20", "d100", "2d6", "2d10", "3d4",
        "4d6", "2d10", "2d4+1"};
    int[] minVal = {1, 1, 1, 1, 1, 1, 1, 2, 2, 3, 4, 2, 3};
    int[] maxVal = {2, 4, 6, 8, 10, 20, 100, 12, 20, 12, 24, 20, 9};
    double[] expAvg = {1.5, 2.5, 3.5, 4.5, 5.5, 10.5, 50.5, 7.0, 11.0, 7.5, 14.0, 11.0, 6.0};

    for (int m = 0; m < dice.length; m++) {
      int value = -1;
      int sum = 0;
      for (int k = 0; k < LOOP_COUNT; k++) {
        value = _md.roll(dice[m]);
        sum += value;
        assertTrue("Rolled " + dice[m] + "and " + value + " < " + minVal[m], value >= minVal[m]);
        assertTrue("Rolled " + dice[m] + "and " + value + " > " + maxVal[m], value <= maxVal[m]);
      }
      double actAvg = (double) sum / LOOP_COUNT;
      double diff = Math.abs(expAvg[m] - actAvg);
      MsgCtrl.msg("\t " + dice[m] + " has average of " + actAvg + " (" + expAvg[m] + ")");
      String diffStr = String.format("%.3f", diff);
      MsgCtrl.msgln("\t diff = " + diffStr);
      assertEquals(expAvg[m], actAvg, 0.5);
    }
  }


  /**
   * @Error.Test MetaDie.roll(String notation) -- Catch exception if non-positive range is found
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


  /**
   * @Normal.Test int rollVariance(double low, double average) -- Gaussian value about the average,
   *              within the range defined by low
   */
  @Test
  public void testRollVariance()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    double[] lows = {2, 3, 80, 10};
    double[] avgs = {7, 10.5, 100, 25};
    int tmpCount = LOOP_COUNT;

    // Traverse the input array
    for (int n = 0; n < lows.length; n++) {
      double min = 200;
      double max = -1;
      double sum = 0;
      // Single calls of input arrays
      for (int k = 0; k < tmpCount; k++) {
        double result = _md.rollVariance(lows[n], avgs[n]);
        sum += result;
        min = Math.min(result, min);
        max = Math.max(result, max);
      }
      double avg = sum / tmpCount;
      MsgCtrl.msg("\t Range [" + min + ", " + max + "]");
      MsgCtrl.msgln("\t Avg = " + pointFmt(avg, 2));
      assertEquals(avgs[n], avg, 0.5);
    }
  }


  /**
   * @Normal.Test int rollVariance(double low, double average) -- parm invalid to throw exceptions
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
   * @Normal.Test int rollCharTrait() -- Returns the sum of highest three in 4d6: range [8,18],
   *              average = 12.5
   */
  @Test
  public void testRollCharTrait()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    // int NBR_LOOPS = 10000;

    double AVG = 12.5;
    int trait = 0;
    int sum = 0;
    int min = 99;
    int max = -1;

    // Roll traits and keep min, max and average values
    for (int k = 0; k < LOOP_COUNT; k++) {
      trait = _md.rollCharTrait();
      min = Math.min(min, trait);
      max = Math.max(max, trait);
      // Print every 20th traits
      // if ((k % 20) == 0) {
      // MsgCtrl.msgln("\t Trait: " + trait);
      // }
      assertTrue("Trait " + trait + " too low", trait >= 8);
      assertTrue("Trait " + trait + " too high", trait <= 18);
      sum += trait;
    }
    // MsgCtrl.msg("\t Min = " + min + "\t Max = " + max);
    // MsgCtrl.msg("\t Min = " + min + "\t Max = " + max);
    double avg = (double) sum / LOOP_COUNT;
    MsgCtrl.msgln("\t Avg = " + avg);
    assertEquals(AVG, avg, 0.5);
  }


  /**
   * @Normal.Test int rollPercent() -- Confirm that average is about 50%
   */
  @Test
  public void testRollPercent()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    // int LIMIT = 100000;

    double AVG = 50.500;
    double sum = 0;

    for (int k = 0; k < LOOP_COUNT; k++) {
      int value = _md.rollPercent();
      assertTrue((value <= 100) && (value >= 1));
      sum += value;
    }
    double actAvg = sum / LOOP_COUNT;
    MsgCtrl.msgln("\t Average for " + LOOP_COUNT + " rolls = " + actAvg);
    assertEquals(AVG, actAvg, 1.0);
  }


  /**
   * @Normal.Test int[] rollTraits -- roll six char traits within [8,18] each
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
   * Not Implemented Only here to cover QA Tool for overloaded methods
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
   * Not Implemented Only here to cover QA Tool for overloaded methods
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

  /** Utility to format a double variable for readability
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


} // end of TestMetaDie class
