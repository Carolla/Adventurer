/**
 * Filename: MetaDie.java Copyright (c) 2003, Carolla Development, All Rights Reserved
 */


package mylib.pdc;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mylib.ApplicationException;


/**
 * A custom random generator for rolling dice for standard d20 gaming system rules, including
 * Chronos.
 * <p>
 * {@code MetaDie} has three core methods:
 * <ul>
 * <li>{@code getRandom(int minRange, int maxRange)}, which selects a non-zero positive number from
 * a <i> linear </i> random population within the range, defined by the given limits (inclusive).
 * Use this method only when positive (non-zero) numbers are desired; e.g. rolling dice "2d6". Do
 * not use it for negative offsets, such as "2d6-10". See the method details for other limitations.
 * <P>
 * </li>
 * <li>{@code getRandomGaussian(double maxRange)}, which selects a number from a Gaussian
 * <i> normal </i> random population from 0 to maxRange, inclusive. Use this method whenever
 * non-positive values are possible, or 0 falls within the desired output range. This is the method
 * to use for example, when needing an interval of [0, 20] lb above an average when calculating
 * weight.
 * <P>
 * </li>
 * <li>{@code roll(int nbrDice, int nbrSides)}, which returns the sum of random numbers by adding
 * the given dice together, as in "2d6". A call to this method adds the rolled value from multiple
 * {@code nbrDice} linear distributions to produce a Gaussian distribution. This method should
 * produce the same results as {@code getRandomGaussian(double)} but tests have not been conducted
 * yet to confirm that.
 * <P>
 * </li>
 * </ul>
 * <p>
 * {@code MetaDie} can be created either in debug mode (repeatable numeric sequences) or with the
 * standard randomizer (non-repeatable). <br>
 * 
 *
 * @author Alan Cline
 * @version Dec 10 2006 // re-used from previous programs <br>
 *          Apr 28 2009 // major refactoring to simplify method set <br>
 *          May 23 2017 // revise traits to fall within [8, 18] <br>
 *          Aug 14, 2017 // minor Javadoc adjustments <br>
 *          Aug 15, 2017 // repaired roll(int, int) for 0-based numbers <br>
 *          Aug 18, 2017 // repaired getRandom(int, int) for 1-based numbers <br>
 * 
 * @see java.util.Random
 */
public class MetaDie
{
  /** Standard random generator class that powers the <code>MetaDie</code> class */
  private Random _generator;

  /** 68% of the population is centered around the mean, within 1 standard deviation */
  public static final double SIGMA = 0.3413; // positive side of the mean

  /** Default trait range for a Hero, before gender or racial adjustments */
  private final int MIN_TRAIT = 8;
  private final int MAX_TRAIT = 18;

  /** Characters are generated by 4d6 - lowest d6 method */
  private final int NBR_ROLLS = 4;
  private final int NBR_DICE = 1;
  private final int NBR_SIDES = 6;
  private final int NBR_TRAITS = 6;


  // ============================================================================================
  // Constructors and Helpers
  // ============================================================================================

  /**
   * Creates a Random class for single die throws; pre-defines a sequence of random numbers.
   */
  public MetaDie()
  {
    // _generator = new Random(System.currentTimeMillis());
    _generator = new Random();
  }


  /**
   * Creates a Random class using an input value as a seed. This object pre-defines a sequence of
   * random numbers, but the same seed generates the same sequence of numbers, so this version is
   * useful for debugging.
   *
   * @param seed a number that determines the number sequence generated.
   */
  public MetaDie(long seed)
  {
    _generator = new Random(seed);
  }


  // ============================================================================================
  // Public Methods
  // ============================================================================================

  /**
   * Returns a single random number that falls within the linear range [minRange, maxRange]. Ranges
   * represent positive linear distribution: maxRange must be greater than the minRange, and both
   * must be non-negative.
   * <P>
   * WARNING: Average values for a set of high minRanges vary from the expected average; that is,
   * getRandom(1, 100) may produce a difference from the true average by almost 2%
   *
   * @param minRange the smallest positive number requested greater than 0
   * @param maxRange the largest positive number requested (exclusive)
   * @return a random number within the range (always greater than 0)
   * @throws IllegalArgumentException if the input parms are negative or out of range
   */
  public int getRandom(int minRange, int maxRange) throws IllegalArgumentException
  {
    if ((minRange <= 0) || (maxRange <= 0)) {
      throw new IllegalArgumentException("Both minRange and maxRange must be positive");
    }
    if (minRange >= maxRange) {
      throw new IllegalArgumentException("Max range must be larger than min range");
    }
    // Convert from 0-based to 1-based distribution; bound is non-inclusive for the random value
    int bound = maxRange - minRange + 1;
    return _generator.nextInt(bound) + minRange;
  }


  // TODO This method of removing trends seems to be unnecessary
//  public int getRandomAntiTrend(int minRange, int maxRange) throws IllegalArgumentException
//  {
//    // Get a sequence of values, and select the one farthest from the average
//    int TREND = 5;
//    int[] value = new int[TREND];
//    int sum = 0;
//    double avg = 0.0;
//    for (int k = 0; k < TREND; k++) {
//      value[k] = getRandom(minRange, maxRange);
//      sum += value[k];
//    }
//    // Find value farthest from average
//    avg = sum / (double) TREND;
//    double[] diff = new double[TREND];
//    double max = -99;
//    int pick = -1;
//    // Select position of value with the largest difference
//    for (int k = 0; k < TREND; k++) {
//      diff[k] = Math.abs(avg - value[k]);
//      if (diff[k] > max) {
//        max = diff[k];
//        pick = k; 
//      }
//    }
//
//    return value[pick];
//  }



  // TODO This doesn't do what I think it does.
  // /**
  // * Returns a single random number that falls within a "normal" Gaussian population. The result
  // * returned defaults to a range across 0, symmetrically from [-2 * median, +2 * median]. If the
  // * flag is set, the number is forced into a symmetric non-negative range [0, 2 * median].
  // *
  // * @param median the non-negative midpoint of the output result (0 is permitted)
  // * @param positiveRange if true, will force result into range [0, 2 * median]
  // * @return a random number either within the range [-2 * median, +2 * median] inclusive, or
  // within
  // * the range [0, 2 * median] if the positiveRange is true
  // */
  // public double getRandomGaussian(double median, boolean positiveRange)
  // {
  // if (median < 0) {
  // throw new IllegalArgumentException("Input parm maxRange must be non-negative");
  // }
  // // Convert generator output from [-sigma, +sigma] to output to [0, maxRange].
  // double result = 9990.0;
  // if (positiveRange) {
  // // Convert generator output from [-sigma, +sigma] to output to [0, maxRange].
  // while ((result > 2 * median) || (result < 0)) {
  // result = _generator.nextGaussian() * median + median;
  // }
  // } else {
  // // Convert generator output from [-sigma, +sigma] to output to [0, maxRange].
  // while (Math.abs(result) > 2 * median) {
  // result = _generator.nextGaussian() * median;
  // }
  // }
  // return result;
  // }


  /**
   * Returns the sum of rolling the given number of dice, which is similar to selecting a number
   * from a normal (Gaussian) population that has the given dice characteristics. If nbrDice = 1,
   * then this method returns a (linear) number from 1 to nbrSides.
   * 
   * @param nbrDice number of rolls to sum (minimum of 1)
   * @param nbrSides max range of each roll (number of sides of die; minimum of 2)
   * @return the sum of the rolled dice.
   * @throws IllegalArgumentException if parms are invalid
   */
  public int roll(int nbrDice, int nbrSides) throws IllegalArgumentException
  {
    // Guard: Must have at least one die and at least two sides per die
    if (nbrDice < 1) {
      throw new IllegalArgumentException("Must have at least one die to roll");
    }
    if (nbrSides < 2) {
      throw new IllegalArgumentException("Die must have at least two sides");
    }
    int sum = 0;
    for (int k = 0; k < nbrDice; k++) {
      int incr = getRandom(1, nbrSides);
      sum += incr;
    }
    return sum;
  }


  /**
   * Convert a dice notation string to two numbers, then roll the dice as indicated by the string.
   * This method should only be called if the lowest possible value is positive. In cases where a
   * non-positive result may occur (e.g., 2d4-2), an exception is thrown directing the caller to
   * call {@code getRandomGaussian()}. <br>
   * Notation format: {[n][n] 'd' n[n][n] ['+' | '-'] n]}, that is, 1-100 required letter 'd', and
   * 1-100, e.g. 2d10, 1d8+1, or 2d4-1. Optional +n to provide minimums. <br>
   * 
   * @param notation format as explained above
   * @return the sum of the rolled dice; else 1 if a zero occurred
   * @throws NumberFormatException if notation cannot be numerically parsed
   * @throws ApplicationException if d20 format is invalid, or non-positive range
   */
  public int roll(String notation) throws ApplicationException
  {
    Pattern p = Pattern.compile("(?<A>\\d*)d(?<B>\\d+)(?>(?<ADD>[+-])(?<D>\\d+))?");
    Matcher matcher = p.matcher(notation);

    if (matcher.matches()) {
      int rolls = getInt(matcher, "A", 1);
      int faces = getInt(matcher, "B", -1);
      int addValue = getInt(matcher, "D", 0);
      int sign = getSign(matcher, "ADD", "+");

      // If non-positive number is a possibility, then this is not the appropriate call
      if ((rolls + sign * addValue) <= 0) {
        throw new ApplicationException("Non-positive lower range encountered.");
      }
      return roll(rolls, faces) + sign * addValue;
    }
    System.err.println("Failed to match");
    return -1;
  }


  /**
   * Returns a single trait using the 4d6-1 algorithm: Four d6 dice are rolled, and the lowest die
   * roll is removed from the list, giving an average of 12.5 for a Person's trait. Imposes the
   * constraint that all traits must be greater than a certain minimum value MIN_TRAIT.
   * 
   * @return value from 8 (constrained) to 18 (three 6's)
   */
  public int rollCharTrait()
  {
    // Collect the rolls
    int[] trait = new int[NBR_ROLLS];
    int rawRoll = 0;
    int sum = 0;

    // Roll 4 d6 and tally results
    for (int k = 0; k < NBR_ROLLS; k++) {
      rawRoll = roll(NBR_DICE, NBR_SIDES);
      trait[k] = rawRoll;
      sum += rawRoll;
    }
    // Find smallest roll in set
    int min = 99;
    for (int k = 0; k < NBR_ROLLS; k++) {
      min = Math.min(trait[k], min);
    }
    // Remove the smallest roll from the sum of all rolls
    sum -= min;

    // If char trait is out of bounds, reroll trait
    if ((sum < MIN_TRAIT) || (sum > MAX_TRAIT)) {
      sum = rollCharTrait();
    }
    return sum;
  }


  /**
   * Returns a linear random number in terms of percent. Same as {@code getRandom(1, 100)}.
   * 
   * @return value from 1 to 100
   */
  public int rollPercent()
  {
    return getRandom(1, 100);
  }


  /**
   * Rolls six random numbers for Hero traits, each constrained to fall within 8 and 18, inclusive
   * 
   * @return the six traits for the Hero
   */
  public int[] rollTraits()
  {
    int traits[] = new int[NBR_TRAITS];
    for (int i = 0; i < traits.length; i++) {
      traits[i] = rollCharTrait();
    }
    return traits;
  }


  // ============================================================================================
  // Private Methods
  // ============================================================================================

  private static boolean isEmpty(String str)
  {
    return str == null || str.trim().isEmpty();
  }

  private static Integer getInt(Matcher matcher, String group, int defaultValue)
  {
    String groupValue = matcher.group(group);
    return isEmpty(groupValue) ? defaultValue : Integer.valueOf(groupValue);
  }

  private static Integer getSign(Matcher matcher, String group, String positiveValue)
  {
    String groupValue = matcher.group(group);
    return isEmpty(groupValue) || groupValue.equals(positiveValue) ? 1 : -1;
  }

} // end of MetaDie class

