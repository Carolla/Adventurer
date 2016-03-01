/**
 * TestMetaDie.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package mylib.test.pdc;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
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

