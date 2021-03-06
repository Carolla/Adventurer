/**
 * Filename: MetaDie.java Copyright (c) 2003, Carolla Development, All Rights Reserved
 */


package mylib.pdc;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math3.random.MersenneTwister;

import mylib.ApplicationException;


/**
 * Simulates rolling dice in various formats for standard d20 gaming system rules, e.g.
 * Chronos.
 * <p>
 * {@code MetaDie} has three core methods:
 * <ul>
 * <li>{@code getRandom(int minRange, int maxRange)}, which selects a non-zero positive number
 * from a <i> linear </i> random population within the range, defined by the given limits
 * (inclusive). Use this method only when positive (non-zero) numbers are desired; e.g. rolling
 * dice "2d6". Do not use it for negative offsets, such as "2d6-10". See the method details for
 * other limitations.
 * <P>
 * </li>
 * <li>{@code getRandomGaussian(double maxRange)}, which selects a number from a Gaussian <i>
 * normal </i> random population from 0 to maxRange, inclusive. Use this method whenever
 * non-positive values are possible, or 0 falls within the desired output range. This is the
 * method to use for example, when needing an interval of [0, 20] pounds above an average when
 * calculating weight.
 * <P>
 * </li>
 * <li>{@code roll(int nbrDice, int nbrSides)}, which returns the sum of random numbers by
 * adding the given dice together, as in "2d6". A call to this method adds the rolled value
 * from multiple {@code nbrDice} linear distributions to produce a Gaussian distribution. This
 * method is best used when generating positive Gaussian distributions, such as rolling a
 * Character's prime traits.
 * <P>
 * </li>
 * </ul>
 * <p>
 * {@code MetaDie} can be created either in debug mode (repeatable numeric sequences) or with
 * the standard randomizer (non-repeatable). <br>
 * 
 *
 * @author Alan Cline
 * @version Dec 10 2006 // re-used from previous programs <br>
 *          Apr 28 2009 // major refactoring to simplify method set <br>
 *          May 23 2017 // revise traits to fall within [8, 18] <br>
 *          Aug 14, 2017 // minor Javadoc adjustments <br>
 *          Aug 15, 2017 // repaired roll(int, int) for 0-based numbers <br>
 *          Aug 18, 2017 // repaired getRandom(int, int) for 1-based numbers <br>
 *          Mar 17, 2018 // getting isOdd() working with new Eclipse and JUnit 5 <br>
 *          Apr 16, 2018 // replaced Java Random class with Apache MersenneTwister class <br>
 */
public class MetaDie
{
  /** Specialized Apache random generator class that powers the <code>MetaDie</code> class */
  private MersenneTwister _generator;

  /** 34% of the population centered one side of the mean, within 1 sigma. */
  public static final double SIGMA = 0.3413; // positive side of the mean
  /** 68% of the population is centered around the mean, within 1 standard deviation */
  public static final double ONE_SIGMA = 0.6827;
  /** 95% of the population (additional 27.18%) is within the second standard deviation */
  public static final double TWO_SIGMA = 0.9545;
  /** Smallest population of numbers on which to properly apply Guassian statistics */
  private static int VALID_STAT_SIZE = 25;

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
    _generator = new MersenneTwister();
  }


  /**
   * Creates a Random class using an input value as a seed. This object pre-defines a sequence
   * of random numbers, but the same seed generates the same sequence of numbers, so this
   * version is useful for debugging.
   *
   * @param seed a number that determines the number sequence generated.
   */
  public MetaDie(long seed)
  {
    _generator = new MersenneTwister(seed);
  }


  // ============================================================================================
  // Public Methods
  // ============================================================================================

  /**
   * Returns a single random number that falls within the linear range [minRange, maxRange].
   * Ranges represent positive linear distribution: maxRange must be greater than the minRange,
   * and both must be non-negative.
   *
   * @param minRange the smallest positive number requested greater than 0
   * @param maxRange the largest positive number requested
   * @return a random number within the range inclusive (always greater than 0)
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
    // Convert from 0-based to 1-based distribution
    int bound = maxRange - minRange + 1;
    return _generator.nextInt(bound) + minRange;
  }


  // /***
  // * Theoretically, an infinite number of values in the input {@code population} should
  // contain
  // * 68% of its values in the range within one standard deviation (sigma) about the mean. It
  // * should contains 95% of its values in the range of two standard deviations about the
  // mean,
  // * which means 27% each on either side and outside the first-sigma range. The remaining
  // * values (2.5%) are split evenly on either side of the second-sigma range.
  // * <P>
  // * For example, for a large set of numbers in the range of [8, 18], the population has a
  // mean
  // * = 13.0 and sigma = 1.67. Sixty-five percent should fall within [11.3, 14.7]. Another 27%
  // * should fall within [9.7, 11.3) and 27% within (14.7, 16.3]. The other 5% of the values
  // * should fall between [8, 9.7) and (16.3, 18].
  // * <P>
  // * Practically, this would be difficult to measure or verify for randomly generated
  // numbers,
  // * especially for small populations. {@code isGaussian()} will verify that the majority of
  // * values occur within the first-sigma range, and the second most populous values both
  // * second-sigma ranges. The remaining values from those are split evenly on either side of
  // * the second-sigma range, should contain the least populous number of values. If the curve
  // * is not strictly Gaussian, it at least will be triangular, which is good enough for our
  // * purposes, i.e., game character generation.
  // * <P>
  // * <i> Implementation</i>: The mean is found from (max + min)/2.0; one sigma is defined as
  // * (max-min)/6.0. The population is distributed within three buckets. Bucket 1 contains all
  // * data within the first-sigma range; bucket 2 contains all value within the second-sigma
  // * range but outside the first-sigma range, and bucket 3 contains the remaining data. The
  // * actual percentages may not always occur as they should theoretically, but at least the
  // * buckets should contain the most in bucket 1, the least in bucket 3, and a medium number
  // in
  // * bucket 2.
  // * <P>
  // * LIMITATION: The population must have at least 25 values in it, else returns false. <br>
  // * LIMITATION: The population must have at least 300 values before a normallity test can be
  // * accurate.
  // *
  // * @param population of numbers to process
  // * @param min smallest value in the population to help define the range, average, and sigma
  // * @param max largest value in the population to help define the range, average, and sigma
  // * @return true if more number are within 1 sigma, second most numbers within 2 sigma
  // (first
  // * sigma numers removed) and the least in bucket 3.
  // */
  // public boolean isGaussian(int[] population, int min, int max)
  // {
  // // Guard against too-small populations
  // if (population.length < VALID_STAT_SIZE) {
  // return false;
  // }
  // ArrayList<Double> bucket1 = new ArrayList<Double>();
  // ArrayList<Double> bucket2 = new ArrayList<Double>();
  // ArrayList<Double> bucket3 = new ArrayList<Double>();
  //
  // // Define the bucket boundaries
  // double sigma = (max - min) / 6.0;
  // double avg = (max + min) / 2.0;
  // double minRange1 = avg - sigma;
  // double maxRange1 = avg + sigma;
  // double minRange2 = minRange1 - sigma;
  // double maxRange2 = maxRange1 + sigma;
  // // Populate the buckets; note that the order of the comparison is important
  // for (int k = 0; k < population.length; k++) {
  // double target = population[k];
  // // Place values close to the mean in bucket 1
  // if ((target >= minRange1) && (target <= maxRange1)) {
  // bucket1.add(target);
  // // System.out.println("\t target = " + target + " => [11.3, 14.7]");
  // } else {
  // // Place values in the second-sigma split ranges into bucket 2
  // boolean upperRange = ((target > maxRange1) && (target <= maxRange2));
  // boolean lowerRange = ((target >= minRange2) && (target < minRange1));
  // if (upperRange || lowerRange) {
  // bucket2.add(target);
  // // System.out.println("\t target = " + target + " => [9.7, 11.3) || (14.7, 16.3]");
  // } else {
  // bucket3.add(target);
  // // System.out.println("\t target = " + target + " => [8, 9.7) || 16.3, 18]");
  // }
  // }
  // }
  // // Verify that the proper percentages are in the buckets
  // int firstSigma = bucket1.size();
  // int secondSigma = bucket2.size();
  // int thirdSigma = bucket3.size();
  // System.out.print("\tbucket 1 = " + firstSigma);
  // System.out.print("\tbucket 2 = " + secondSigma);
  // System.out.print("\tbucket 3 = " + thirdSigma);
  // return ((firstSigma > secondSigma) && (secondSigma > thirdSigma));
  // }


  /**
   * Generate a random boolean as true or false.
   * 
   * @return random true or false value
   */
  public boolean isOdd()
  {
    return _generator.nextBoolean();
  }


  /**
   * Returns the sum of rolling the given number of dice, which is similar to selecting a
   * number from a normal (Gaussian) population that has the given dice characteristics. If
   * nbrDice = 1, then this method returns a (linear) number from 1 to nbrSides.
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


//  /**
//   * Pull a random number from a Gaussian population to add or subtract from the given average.
//   * The range is calculated from the low and average parms.
//   * 
//   * @param low the low end of the range
//   * @param average the middle of the range
//   * @return a value within range, centered on the average
//   */
//  public int rollVariance(double low, double average) throws IllegalArgumentException
//  {
//    // Guards: only positive values are valid, and average must be greater than low
//    if ((low < 0) || (average < 0)) {
//      throw new IllegalArgumentException("rollVariance(): Parms must have positive values");
//    }
//    if (low >= average) {
//      throw new IllegalArgumentException(
//          "rollVariance(): Low parm must be less than average parm");
//    }
//
////    double GAUSSIAN_RANGE = 3.1;
//    boolean inRange = false;
//    double halfRange = average - low;
//    double high = average + halfRange;
//    double result = -100.0;
//    while (!inRange) {
//      result = _generator.nextGaussian() + average;
//      // result = (_generator.nextGaussian() / GAUSSIAN_RANGE) * halfRange + average;
//      if ((result <= high) && (result >= low)) {
//        inRange = true;
//      }
//    }
//    return (int) Math.round(result);
//  }

  /**
   * Pull a random number from a Gaussian population to add or subtract from the given average.
   * The range and average are calculated from the low and high parms.
   * 
   * @param low the low end of the range
   * @param high the high end of the range
   * @return a value within range, centered on the average
   */
  public int rollVariance(int low, int high) throws IllegalArgumentException
  {
    // Guards: only positive values are valid, and average must be greater than low
    if ((low < 0) || (high < 0)) {
      throw new IllegalArgumentException("rollVariance(): Parms must have positive values");
    }
    if (low >= high) {
      throw new IllegalArgumentException(
          "rollVariance(): Low parm must be less than high parm");
    }

    double avg = (high + low)/2.0;
    double result = - high; // some invalid value to start
    boolean inRange = false;
    while (!inRange) {
      result = _generator.nextGaussian() + avg;
      if ((result <= high) && (result >= low)) {
        inRange = true;
      }
    }
    return (int) Math.round(result);
  }

  
  /**
   * Convert a dice notation string to two numbers, then roll the dice as indicated by the
   * string. This method should only be called if the lowest possible value is positive. In
   * cases where a non-positive result may occur (e.g., 2d4-2), an exception is thrown
   * directing the caller to call {@code getRandomGaussian()}. <br>
   * Notation format: {[n][n] 'd' n[n][n] ['+' | '-'] n]}, that is, 1-100 required letter 'd',
   * and 1-100, e.g. 2d10, 1d8+1, or 2d4-1. Optional +n to provide minimums. <br>
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
   * Returns a single trait using the "4d6-1" algorithm: Four d6 dice are rolled, and the
   * lowest die roll is removed from the list, giving an average of 11.5 for a Person's trait.
   * Imposes the constraint that all traits must be within a specified range inclusive.
   * 
   * @param minTrait the lower boundary of the character trait allowed
   * @param maxTrait the upper boundary of the character trait allowed
   * @return 4d6 - lowest roll within given range [minTrait, maxTrait]
   */
  public int rollCharTrait(int minTrait, int maxTrait)
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
    if ((sum < minTrait) || (sum > maxTrait)) {
      sum = rollCharTrait(minTrait, maxTrait);
    }
    return sum;
  }


  /**
   * Returns a linear random number in terms of percent, generated by {@code d10 * 10 + d10}.
   * 
   * @return value from 1 to 100
   */
  public int rollPercent()
  {
    // roll 0 - 9
    int digits = getRandom(1, 10) - 1; // cannot have 0 as parm
    // roll 0 - 90
    int tens = 10 * (getRandom(1, 10) - 1);
    int result = tens + digits + 1;
    return result;
  }


  /**
   * Rolls six random numbers for Hero traits, each constrained to fall within 8 and 18,
   * inclusive
   * 
   * @return the six traits for the Hero
   */
  public int[] rollTraits()
  {
    int traits[] = new int[NBR_TRAITS];
    for (int i = 0; i < traits.length; i++) {
      traits[i] = rollCharTrait(MIN_TRAIT, MAX_TRAIT);
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

