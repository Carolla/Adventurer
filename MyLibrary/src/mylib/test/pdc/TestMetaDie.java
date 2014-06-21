/**
 * TestMetaDie.java
 * Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package mylib.test.pdc;


import java.util.Arrays;

import junit.framework.TestCase;
import mylib.MsgCtrl;
import mylib.pdc.MetaDie;
import mylib.pdc.MetaDie.MockMetaDie;


/**
 *  Ensure that the MetaDie class is working properly.
 *  
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Dec 12, 2010   // original <DD>
 * </DL>
 */
public class TestMetaDie extends TestCase
{
    /** Non-repeatable random generator */
    private MetaDie _md = null;
    /** Repeatable random generator */
    private MetaDie _mdrep = null;
    /** Mock for testing */
    private MockMetaDie _mock = null;

    /**
     * @throws java.lang.Exception
     * @Before
     */
    
    public void setUp() throws Exception
    {
        // Audit messages are OFF at start of each test 
        MsgCtrl.auditMsgsOn(false);
        // Error messages are ON at start of each test 
        MsgCtrl.errorMsgsOn(true);
        // Create a non-repeatable random generator 
        _md = new MetaDie();
        assertNotNull(_md);
        // Create a repeatable random generator
        _mdrep = new MetaDie(1007L);
        assertNotNull(_mdrep);
    }
    
    /**
     * @throws java.lang.Exception
     * @After
     */
    
    public void tearDown() throws Exception
    {
        _md = null;
        _mdrep = null;
        // Audit messages are OFF after each test
        MsgCtrl.auditMsgsOn(false);
    }

    
    //--------------------------------------------------------------------------------------------------------------
    //          Let the Testing Begin!
    //--------------------------------------------------------------------------------------------------------------

    /**
          *  Test selecting a random Gaussian (normal) multiplier   
          *   @Normal    MetaDie.getGaussian(double mean, double low, double high)          ok             
          *   @Error        MetaDie.getGaussian(double mean, double low, double high)          ok
          *   @Null          MetaDie.getGaussian(double mean, double low, double high)      compile error
          */
          public void testGetGaussian() throws Exception
          {
              // Audit messages are OFF at start of each test 
              MsgCtrl.auditMsgsOn(false);
              MsgCtrl.errorMsgsOn(false);
              MsgCtrl.msgln(this, "\ttestGetGaussian()");
    
              // Generate a sufficiently large population so that averages are closer to expected averages
//              int NBR_LOOPS = 1000;
              int NBR_LOOPS = 25;
              double[] values = new double[NBR_LOOPS];
//              double[] average = {1, 2, 10, 24, 100, 0.1, 0.5};
              double[] average = {2, 10, 24, 100};
              // Loop through the limit permutations
              for (int m=0; m < average.length; m++) {
                  double sum = 0.0;
                  double maxDelta = 0.0;
                  double maxDP = 0.0;
                  double calcMin = 1000;              // invalid min to be reset
                  double calcMax = 0;                     // invalid max to be reset
                  // Set the lower and upper boundaries: 1 sigma on either side
                  int expMinValue = (int) Math.round((1.0 - MetaDie.SIGMA) * average[m]);
                  int expMaxValue = (int) Math.round((1.0 + MetaDie.SIGMA) * average[m]);
                  MsgCtrl.msg("\n\t(" + expMinValue + ", " + expMaxValue + ") = " );
                  
                  // Generate a population of numbers, plus/minus 32% 
                  for (int k=0; k < NBR_LOOPS; k++) {
                      values[k] = _md.getGaussian(average[m], expMinValue, expMaxValue);
                      sum = sum + values[k];
//                      MsgCtrl.msg("\t" + values[k]);
                      assertTrue((values[k] >= expMinValue) && (values[k] <= expMaxValue)) ;
                      calcMin = Math.min(values[k], expMinValue);
                      calcMax = Math.max(values[k], expMaxValue);
                  }
                  
                  // Confirm that the min and max possible values were got
                  MsgCtrl.msg("\n\t[min " + calcMin + "]");
                  MsgCtrl.msg("\t[max " + calcMax + "]");
                  // Calculate the population statistics
                  double calcAvg = sum/NBR_LOOPS;
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
                  double[] ranges = { 0.25, 0.5, 0.75, 1.00, 1.25, 1.5, 1.75, 2.00};             
                  int[] counts = new int[ranges.length];
                  // Display the ranges for ease of comparing
                  MsgCtrl.msg("\n\tRanges ");
                  for (int p=0; p < ranges.length; p++) {
                      ranges[p] = ranges[p] * average[m];
                      MsgCtrl.msg("\t" + ranges[p]);
                  }
                  MsgCtrl.msgln("");
                  for (int s=0; s < values.length; s++) {
    //                  MsgCtrl.msg("\t" + values[s]);
                      for (int p=0; p < ranges.length; p++) {
                          if (values[s] <= ranges[p]) {
                              counts[p]++; 
    //                          MsgCtrl.msgln("\tAdded to Bucket " + p);
                              break;
                          }
                      }
                  }
                  // Dump the bucket to get the frequency count
                  MsgCtrl.msg("\n\t Bucket List:");
                  int tally = 0;
                  for (int p=0; p < counts.length; p++) {
                      MsgCtrl.msg("\t\t" + counts[p]);
                      tally += counts[p];
                  }
                  MsgCtrl.msgln("\nTotal values = " + tally);
                  
              }     // end of 'm' loop travesing means
    
              // ERROR cases: all cases throw Exceptions, which are caught in the mock
              _mock = _md.new MockMetaDie();
              // Case 1: invalid mean
              assertTrue(_mock.getGaussian(0.0, -1, 1));
              assertTrue(_mock.getGaussian(-2.0, -1, 1));
              // Case 2: invalid low end
              assertTrue(_mock.getGaussian(20.0, -1, 2));
              assertTrue(_mock.getGaussian(20.0, 20, 25));
              assertTrue(_mock.getGaussian(10.0, 10, 11));
              // Case 3: invalid high end
              assertTrue(_mock.getGaussian(20.0, 10, 20));
              assertTrue(_mock.getGaussian(20.0, 10, 11));
              assertTrue(_mock.getGaussian(10.0, 9, 0));
              // Case 4: multiple invallid parms (exception will catch first invalid one)
              assertTrue(_mock.getGaussian(0.0, 0, 0));
              assertTrue(_mock.getGaussian(2.0, -1, -1));
              assertTrue(_mock.getGaussian(4.0, 1, 2));
              
           }    // end of test

          
    /**
     *  Test that random numbers summed are calculated as expected   
     *   @Normal    MetaDie.getRandom(int minRange, maxRange)                ok.   
     *   @Error        MetaDie.getRandom(int minRange, maxRange)                ok
     *   @Null         MetaDie.getRandom(int minRange, maxRange)                 compile error
     */
     public void testGetRandom() 
     {
         // Audit messages are OFF at start of each test 
         MsgCtrl.auditMsgsOn(false);
         MsgCtrl.errorMsgsOn(false);
         MsgCtrl.msgln(this, "\ttestGetRandom()");

         // Some min and max possibilities
         int[] mins = {1, 2, 6, 10};
         int[] maxs = {2, 6, 10, 12, 20, 100};
         int NBR_LOOPS = 1000;

         // NORMAL -- flat distribution
         double maxDelta = 0.0;
         for (int p=0; p < mins.length; p++) {
             for (int m=0; m < maxs.length; m++) {
                 // These will be overwritten by true min and max values on first loop
                 int minValue = 100;
                 int maxValue = 1;
                 double sum = 0.0;             // clear the sum for each iteration
                 // Skip invalid range boundaries
                 if (mins[p] >= maxs[m]) {
                     continue;
                 }
                 // Run the ranges for multiple times to get a good population average
                 for (int k=0; k < NBR_LOOPS; k++) {
                     // Traverse the ranges for 
                     int value = _md.getRandom(mins[p], maxs[m]);
                     // Stats on rolls are average, min value, and max value
                     sum += value;
                     minValue = Math.min(minValue, value);
                     maxValue = Math.max(maxValue, value);
                     assertTrue((value >= mins[p]) && (value <= maxs[m]));
                 }
                 MsgCtrl.msg("\n\t(" + mins[p] + ", " + maxs[m] + ") = " );
                 MsgCtrl.msg("\t[min " +minValue + "]");
                 MsgCtrl.msg("\t[max " +maxValue + "]");
                 double avg = sum/NBR_LOOPS;
                 double expAvg = (mins[p] + maxs[m])/2.0;
                 double delta = expAvg - avg;
                 maxDelta = Math.max(maxDelta, Math.abs(delta));
                 MsgCtrl.msgln("\t\tAverage = " + avg + "\t\tExpected " + expAvg + "\t\tDelta = " + delta);
                 // These are flat distributions, not centralized by adding. The delta is large
                 assertEquals(expAvg, avg, 2.0); 
             }
         }
         MsgCtrl.msgln("Max delta found = " + maxDelta);
         
         // ERROR cases: all cases throw Exceptions, which are caught in the mock
         _mock = _md.new MockMetaDie();
         // Case 1: parms are negative or zero
         assertTrue(_mock.getRandom(-1, 10));
         assertTrue(_mock.getRandom(-2, -1));
         assertTrue(_mock.getRandom(0, 4));
         // Case 2: min > max
         assertTrue(_mock.getRandom(13, 5));
         // Case 3: min = max
         assertTrue(_mock.getRandom(16, 16));
     }

         
    /**
         *  Test that random numbers are rolled and summed expected. Error is maintained within 2%  
         *   @Normal    MetaDie.roll(int nbrDice, int nbrSides)              ok              
         *   @Error        MetaDie.roll(int nbrDice, int nbrSides)              ok
         *   @Null          MetaDie.roll(int nbrDice, int nbrSides)              compiler error
         */
         public void testRoll() 
         {
             // Audit messages are OFF at start of each test 
             MsgCtrl.auditMsgsOn(false);
             MsgCtrl.errorMsgsOn(false);
             MsgCtrl.msgln(this, "\ttestRoll()");
    
             // Generate a sufficiently large population so that averages are closer to expected averages
             int NBR_LOOPS = 10000;               // ten thousand rolls
             // NORMAL -- try a few normal distros from die rolling
             // 1d2, 1d4, 1d6, 1d8, 1d12, 1d20, 1d100
             // 2d6, 2d10, 3d4, 4d6, 2d10
             int[] nbrDice = {1, 2, 3, 4, 6, 100};                          // 6 groups of dice
             int[] nbrSides = {2, 4, 6, 8, 10, 12, 20, 100};            // 8 kinds of dice
             // Loop through the dice permutations
             for (int p=0; p < nbrDice.length; p++) {
                 for (int m=0; m < nbrSides.length; m++) {
                     int value = -1;
                     double sum = 0.0;
                     double maxDelta = 0.0;
                     double maxDP = 0.0;
                     double intMin = 1000;              // invalid min to be reset
                     double intMax = 0;                     // invalid max to be reset
                     
                     int expMinValue = nbrDice[p];
                     int expMaxValue = nbrDice[p] * nbrSides[m];
                     MsgCtrl.msg("\n\t(" + nbrDice[p] + ", " + nbrSides[m] + ") = " );
                     
                     // Generate a population of number from the same dice configuration 
                     for (int k=0; k < NBR_LOOPS; k++) {
                         value = _md.roll(nbrDice[p], nbrSides[m]);
                         sum = sum + value;
    //                     MsgCtrl.msg("\t" + value);
                         assertTrue((value >= expMinValue) && (value <= expMaxValue)) ;
                         intMin = Math.min(value, expMinValue);
                         intMax = Math.max(value, expMaxValue);
                     }
                     // Confirm that the min and max possible values were got
                     MsgCtrl.msg("\t[min " + intMin + "]");
                     MsgCtrl.msg("\t[max " + intMax + "]");
                     // Calculate the population statistics
                     double calcAvg = sum/NBR_LOOPS;
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
                     // Averages must be within 2% error
                     assertTrue(percentDelta < Math.abs(2.0));           
                 }
             }
              // ERROR for IllegalArgumentException
              MockMetaDie mock = _md.new MockMetaDie();
              assertNotNull(mock);
              // nbrDice must be >= 1 and nbrSides >= 2
              // mock method returns true when the exception is thrown
              assertTrue(mock.roll(-1, 6));
              assertTrue(mock.roll(0, 6));
              assertTrue(mock.roll(2, 0)); 
              assertTrue(mock.roll(2, 1)); 
              assertTrue(mock.roll(2, -1)); 
          }

         
    /**
      *  Test that roll method the same as above except use String d20 notation instead of ints
      *  Only test that the string returns the correct number of dice and sides  
      *   @Normal    MetaDie.roll(String notation)                              ok             
      *   @Error        MetaDie.roll(String notation)                              ok
      *   @Null          MetaDie.roll(String notation)                              ok
      */
      public void testRollString() throws Exception
      {
          // Audit messages are OFF at start of each test 
          MsgCtrl.auditMsgsOn(false);
          MsgCtrl.errorMsgsOn(false);
          MsgCtrl.msgln(this, "\ttestRollString()");

          // Generate a sufficiently large population so that averages are closer to expected averages
          int NBR_LOOPS = 10000;               // ten thousand rolls
          // NORMAL -- try a few normal distros from die rolling
          // 1d2, 1d4, 1d6, 1d8, 1d12, 1d20, 1d100
          // 2d6, 2d10, 3d4, 4d6, 2d10
          String[] dice = {"1d2", "1d4", "1d6", "1d8", "d10", "d20", "d100",
                          "2d6", "2d10", "3d4", "4d6", "2d10", "d4-1", "2d4+1", "4d6-10"} ; 
          int[] minVal =          { 1,   1,    1,     1,    1,     1,     1,       2,    2,       3,      4,     2,     1,    3,   1};
          int[] maxVal =          {2,   4,    6,     8,   10,    20,  100,   12,   20,     12,    24,   20,   3,    9,  14};
          // Ensure that the lengths of these init arrays are correct
          assertTrue(dice.length == minVal.length);
          assertTrue(dice.length == maxVal.length);

          // Loop through the dice permutations
          for (int m=0; m < dice.length; m++) {
              int value = -1;
              double sum = 0.0;
              double maxDelta = 0.0;
              double maxDP = 0.0;
              double intMin = 1000;              // invalid min to be reset
              double intMax = 0;                     // invalid max to be reset
              MsgCtrl.msg("\n\t" + dice[m]);
                  
              // Generate a population of number from the same dice configuration 
              for (int k=0; k < NBR_LOOPS; k++) {
                  value = _md.roll(dice[m]);
                  sum = sum + value;
//                      MsgCtrl.msg("\t" + value);
                  assertTrue((value >= minVal[m]) && (value <= maxVal[m])) ;
                  intMin = Math.min(value, minVal[m]);
                  intMax = Math.max(value, maxVal[m]);
              }
              // Confirm that the min and max possible values were got
              MsgCtrl.msg("\t[min " + intMin + "]");
              MsgCtrl.msg("\t[max " + intMax + "]");
              // Calculate the population statistics (expected averages skewed for addons)
              // Only do the statistics for the first 12 numbers of input
              double calcAvg = sum/NBR_LOOPS;
              MsgCtrl.msg("\nCalculated Average = " + calcAvg);
              if (m < 12) {
                  double expAvg = (minVal[m] + maxVal[m]) /2.0; 
                  double delta = expAvg - calcAvg;
                  double percentDelta = (delta / expAvg) * 100.0;
                  String deltaStr = String.format("%6.4f", delta);
                  String DPStr = String.format("%4.2f", percentDelta);
                  maxDelta = Math.max(delta, maxDelta);
                  maxDP = Math.max(percentDelta, maxDP);
                  MsgCtrl.msg("\tExpected Average = " + expAvg);
                  MsgCtrl.msg("\t Delta = " + deltaStr);
                  MsgCtrl.msgln("\t Delta = " + DPStr + "%");
                  // Averages must be within 2%
                  assertTrue(percentDelta < Math.abs(2.0));           
              }
          }
          MsgCtrl.msgln("");
          
           // ERROR for various Exceptions
           MockMetaDie mock = _md.new MockMetaDie();
           assertNotNull(mock);
           // mock method returns true when the exception is thrown
           assertTrue(mock.roll("6"));       // invalid parm lenth
           assertTrue(mock.roll("132d12+12"));       // invalid parm lenth
           assertTrue(mock.roll("-1d6"));       // negative parms
           assertTrue(mock.roll("1d-6"));       // negative parms
           assertTrue(mock.roll("2d6--2"));       // invalid format
           assertTrue(mock.roll("2d6++2"));       // invalid formt
           assertTrue(mock.roll("1D6"));
           assertTrue(mock.roll("2s6"));
           assertTrue(mock.roll("2d6+w"));
           assertTrue(mock.roll("2d6+"));
           assertTrue(mock.roll("2d6-"));
           
           // NULL case
           assertTrue(mock.roll(null));
           assertTrue(mock.roll(" "));
       }

      
     /** Methods that do not need testing 
     * @Not_Needed  MetaDie.MetaDie()                           simple constructor calling library method
     * @Not_Implemented MetaDie.rollPercent()               included in roll(int, int)
     */
    public void Not_Needed() { }

    
}           // end of TestMetaDie class

