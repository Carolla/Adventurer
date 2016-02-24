/**
 * TestMetaDie.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package mylib.test.pdc;


import static org.junit.Assert.*;

import java.util.Arrays;

import mylib.MsgCtrl;
import mylib.pdc.MetaDie;

import org.junit.Test;


/**
 * Ensure that the MetaDie class is working properly.
 * 
 * @author Alan Cline
 * @version Dec 12, 2010 // original <br>
 *          Sep 20, 2014 // trying to cut down the number of times a method fails for random value
 *          out of range <br>
 */
public class TestMetaDie
{
  /** Non-repeatable random generator */
  private MetaDie _md = new MetaDie();

  // --------------------------------------------------------------------------------------------------------------
  // Let the Testing Begin!
  // --------------------------------------------------------------------------------------------------------------

  /** Test that rollTrait is returning the proper range and randomness */
  @Test
  public void testRollTraits()
  {
    double COUNT = 100000;
    int trait = 0;

    for (int k = 0; k < COUNT; k++) {
      trait = _md.rollTrait();
      assertTrue("Trait " + trait + " too low", trait > 2);
      assertTrue("Trait " + trait + " too high", trait < 19);
    }
  }

  /**
   * Test selecting a random Gaussian (normal) multiplier
   * 
   * @Normal.Test MetaDie.getGaussian(double mean, double low, double high) ok
   * @Error.Test MetaDie.getGaussian(double mean, double low, double high) ok
   * @Null.Test MetaDie.getGaussian(double mean, double low, double high) compile error
   */
  @Test
  public void testGetGaussian()
  {
    int NBR_LOOPS = 1000;
    
    double[] values = new double[NBR_LOOPS];
    double[] average = {2, 10, 24, 100};

    for (int m = 0; m < average.length; m++) {
      double sum = 0.0;
      double maxDelta = 0.0;
      double maxDP = 0.0;

      int expMinValue = (int) Math.round((1.0 - MetaDie.SIGMA) * average[m]);
      int expMaxValue = (int) Math.round((1.0 + MetaDie.SIGMA) * average[m]);

      for (int k = 0; k < NBR_LOOPS; k++) {
        values[k] = _md.getGaussian(average[m], expMinValue, expMaxValue);
        sum = sum + values[k];
        assertTrue(values[k] + ">=" + expMinValue, values[k] >= expMinValue);
        assertTrue(values[k] + "<=" + expMaxValue, values[k] <= expMaxValue);
      }

      // Calculate the population statistics
      double calcAvg = sum / NBR_LOOPS;
      double expAvg = (expMinValue + expMaxValue) / 2.0;
      double delta = expAvg - calcAvg;
      double percentDelta = (delta / expAvg) * 100.0;
      String deltaStr = String.format("%6.4f", delta);
      String DPStr = String.format("%4.2f", percentDelta);
      maxDelta = Math.max(delta, maxDelta);
      maxDP = Math.max(percentDelta, maxDP);
      MsgCtrl.msg("\nCalculated Average = " + calcAvg);
      MsgCtrl.msg("\tExpected Average = " + expAvg);
      MsgCtrl.msg("\t Delta = " + deltaStr);
      MsgCtrl.msgln("\t Delta = " + DPStr + "%");

      // To confirm proper distribution, sort and categorize each value
      Arrays.sort(values);
      // Fixed sigma divisions for the population
      double[] ranges = {0.25, 0.5, 0.75, 1.00, 1.25, 1.5, 1.75, 2.00};
      int[] counts = new int[ranges.length];
      // Display the ranges for ease of comparing
      MsgCtrl.msg("\n\tRanges ");
      for (int p = 0; p < ranges.length; p++) {
        ranges[p] = ranges[p] * average[m];
        MsgCtrl.msg("\t" + ranges[p]);
      }
      MsgCtrl.msgln("");
      for (int s = 0; s < values.length; s++) {
        // MsgCtrl.msg("\t" + values[s]);
        for (int p = 0; p < ranges.length; p++) {
          if (values[s] <= ranges[p]) {
            counts[p]++;
            // MsgCtrl.msgln("\tAdded to Bucket " + p);
            break;
          }
        }
      }
      // Dump the bucket to get the frequency count
      MsgCtrl.msg("\n\t Bucket List:");
      int tally = 0;
      for (int p = 0; p < counts.length; p++) {
        MsgCtrl.msg("\t\t" + counts[p]);
        tally += counts[p];
      }
      MsgCtrl.msgln("\nTotal values = " + tally);

    } // end of 'm' loop travesing means
    //
    //    // ERROR cases: all cases throw Exceptions, which are caught in the mock
    //    _mock = _md.new MockMetaDie();
    //    // Case 1: invalid mean
    //    assertTrue(_mock.getGaussian(0.0, -1, 1));
    //    assertTrue(_mock.getGaussian(-2.0, -1, 1));
    //    // Case 2: invalid low end
    //    assertTrue(_mock.getGaussian(20.0, -1, 2));
    //    assertTrue(_mock.getGaussian(20.0, 20, 25));
    //    assertTrue(_mock.getGaussian(10.0, 10, 11));
    //    // Case 3: invalid high end
    //    assertTrue(_mock.getGaussian(20.0, 10, 20));
    //    assertTrue(_mock.getGaussian(20.0, 10, 11));
    //    assertTrue(_mock.getGaussian(10.0, 9, 0));
    //    // Case 4: multiple invallid parms (exception will catch first invalid one)
    //    assertTrue(_mock.getGaussian(0.0, 0, 0));
    //    assertTrue(_mock.getGaussian(2.0, -1, -1));
    //    assertTrue(_mock.getGaussian(4.0, 1, 2));

  } // end of test


  /**
   * Test that random numbers summed are calculated as expected
   * 
   * @Normal.Test MetaDie.getRandom(int minRange, maxRange) ok.
   * @Error.Test MetaDie.getRandom(int minRange, maxRange) ok
   * @Null.Test MetaDie.getRandom(int minRange, maxRange) compile error
   */
  @Test
  public void testGetRandom()
  {
    int[] mins = {1, 2, 6, 10};
    int[] maxs = {2, 6, 10, 12, 20, 100};
    int NBR_LOOPS = 10000;

    for (int p = 0; p < mins.length; p++) {
      for (int m = 0; m < maxs.length; m++) {
        
        if (mins[p] >= maxs[m]) {
          try {
            _md.getRandom(mins[p], maxs[m]);
            fail("Expected exception with min " + mins[p] + " and max " + maxs[m]);
          } catch (IllegalArgumentException e) {
            continue;
          }
        }
        
        for (int k = 0; k < NBR_LOOPS; k++) {
          int value = _md.getRandom(mins[p], maxs[m]);
          assertTrue((value >= mins[p]) && (value <= maxs[m]));
        }
      }
    }
  }


  /**
   * Test that random numbers are rolled and summed expected. Error is maintained within 2%
   * 
   * @Normal.Test MetaDie.roll(int nbrDice, int nbrSides) ok
   * @Error.Test MetaDie.roll(int nbrDice, int nbrSides) ok
   * @Null.Test MetaDie.roll(int nbrDice, int nbrSides) compiler error
   */
  @Test
  public void testRoll()
  {
    int NBR_LOOPS = 10000;
    int[] nbrDice = {1, 2, 3, 4, 6, 100}; // 6 groups of dice
    int[] nbrSides = {2, 4, 6, 8, 10, 12, 20, 100}; // 8 kinds of dice
   
    for (int p = 0; p < nbrDice.length; p++) {
      for (int m = 0; m < nbrSides.length; m++) {
        int value = -1;

        int expMinValue = nbrDice[p];
        int expMaxValue = nbrDice[p] * nbrSides[m];
        MsgCtrl.msg("\n\t(" + nbrDice[p] + ", " + nbrSides[m] + ") = ");

        // Generate a population of number from the same dice configuration
        for (int k = 0; k < NBR_LOOPS; k++) {
          value = _md.roll(nbrDice[p], nbrSides[m]);
          assertTrue(value >= expMinValue && value <= expMaxValue);
        }
      }
    }
  }

  /**
   * Test that roll method the same as above except use String d20 notation instead of ints Only
   * test that the string returns the correct number of dice and sides
   * 
   * @Normal.Test MetaDie.roll(String notation) ok
   * @Error.Test MetaDie.roll(String notation) ok
   * @Null.Test MetaDie.roll(String notation) ok
   */
  @Test
  public void testRollString()
  {
    String[] dice = {"1d2", "1d4", "1d6", "1d8", "d10", "d20", "d100",
        "2d6", "2d10", "3d4", "4d6", "2d10", "d4-1", "2d4+1", "4d6-10"};
    int[] minVal = {1, 1, 1, 1, 1, 1, 1, 2, 2, 3, 4, 2, 0, 3, -6};
    int[] maxVal = {2, 4, 6, 8, 10, 20, 100, 12, 20, 12, 24, 20, 3, 9, 14};

    for (int m = 0; m < dice.length; m++) {
      int value = -1;
      for (int k = 0; k < 1000; k++) {
        value = _md.roll(dice[m]);
        assertTrue("Rolled " + dice[m] + "and " + value + " < " + minVal[m], value >= minVal[m]);
        assertTrue("Rolled " + dice[m] + "and " + value + " > " + maxVal[m], value <= maxVal[m]);
      }
    }
  }

} // end of TestMetaDie class

