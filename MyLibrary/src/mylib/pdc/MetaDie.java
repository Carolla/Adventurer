/**
 * Filename: MetaDie.java Copyright (c) 2003, Carolla Development, All Rights Reserved
 */


package mylib.pdc;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mylib.ApplicationException;


/**
 * A custom random generator biased toward rolling dice for mapping to standard d20 gaming system
 * rules, including Chronos. <br>
 * <code>MetaDie</code> has three core methods:
 * <UL>
 * <LI><code>getRandom(int min, int max)</code>, which selects a single number from a linear random
 * population within the range, defined by the given min, max limits (inclusive).</LI>
 * <LI><code>getGaussian(double mean, double lowEnd, double highEnd)</code>, which retrieves a
 * number from a Gaussian distribution adjusted to fit the given mean.</LI>
 * <LI><code>roll(int nbrSide, int nbrDice)</code>, which returns the sum of random numbers by
 * adding the given dice together. A call to this method is similar to retrieving numbers from a
 * Gaussian distribution.</LI>
 * </UL>
 * <code>MetaDie</code> can be created either in debug mode (repeatable numeric sequences) or with
 * the standard randomizer (non-repeatable). <br>
 * 
 *
 * @author Alan Cline
 * @version Dec 10 2006 // re-used from previous programs <br>
 *          Apr 28 2009 // major refactoring to simplify method set <br>
 * @see java.util.Random
 */
public class MetaDie
{

  /** Standard random generator class that powers the <code>MetaDie</code> class */
  private Random _generator;
  /** Generic constant if something requested is not found */
  //  private static final int NOT_FOUND = -1; // some String method returns

  /** 68% of the population is centered around the mean, within 1 standard deviation */
  public static final double SIGMA = 0.3413; // positive side of the mean

  /**
   * Creates a Random class for single die throws, using time as the seed. This object pre-defines a
   * sequence of random numbers.
   */
  public MetaDie()
  {
    _generator = new Random(System.currentTimeMillis());
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


  /**
   * Returns a Gaussian value within the given range centered about the given average. Numbers
   * outside the low and high ends are excluded.
   * <p>
   * For example, for variance = one sigma (.3413), all numbers will fall between .6587 and 1.3413
   * of a 1.0 mean, so the call is <code>getGaussian(1.0, .6587, 1.3413)</code>, or using the
   * standard constants, the call is <code>getGaussian(1.0, 1.0-SIGMA, 1.0+SIGMA)</code>. Dissimilar
   * <code>lowEnd</code> and <code>highEnd</code> values will alllow asymmetrical distributions,
   * such as Poisson distributions. The value returned is rounded to the nearest integer.
   * 
   * @param mean the average for the distribution, must be greater than 0.0
   * @param lowEnd lowest permitted deviation, must be less than the mean but greater than 0.0
   * @param highEnd highest permitted deviation, must be greater than the mean
   * @return a random value from the distribution defined by the mean and ranges
   * @throws ApplicationException for any invalid parms
   */
  public int getGaussian(double mean, int lowEnd, int highEnd)
      throws ApplicationException
  {
    if (mean <= 0) {
      mean = 0;
    }

    if ((lowEnd >= mean) || (lowEnd <= 0)) {
      throw new ApplicationException("lowEnd must be less than the mean, but greater than 0.0");
    }

    if ((highEnd <= mean)) {
      throw new ApplicationException("highEnd must be greater than the mean.");
    }

    int value = -1;

    double multiplier;
    do { // exclude end points to sharpen (narrow) the distribution a bit
      multiplier = Math.abs(_generator.nextGaussian() + 1.0);
      // Apply multiplier to given mean
      value = (int) Math.round(multiplier * mean);
    } while ((value <= lowEnd) || (value >= highEnd));

    return value;
  }


  /**
   * Returns a single random number that falls within the linear range requested. Ranges represent
   * linear distributions with inclusive boundaries, must be positive, and the maxRange must be
   * greater than the minRange.
   *
   * @param minRange the smallest positive number requested > 0 (inclusive)
   * @param maxRange the largest positive number requested (inclusive)
   * @return the random number within the range (always greater than 0)
   * @throws IllegalArgumentException if the input parms are negative or out of range
   */
  public int getRandom(int minRange, int maxRange) throws IllegalArgumentException
  {
    if (minRange >= maxRange) {
      throw new IllegalArgumentException("Max range must be larger than min range");
    }

    return _generator.nextInt(maxRange - minRange) + minRange + 1;
  }


  /**
   * Returns the sum of rolling the given number of dice, which is similar to selecting a number
   * from a normal (Gaussian) population that has the given dice characteristics. If nbrDice = 1,
   * then this method returns a (linear) number from 1 to nbrSides. The generator uses zero-based
   * numbers, so is offset by 1 for the caller.
   * 
   * @param nbrDice number of rolls to sum
   * @param nbrSides max range of each roll (number of sides of die)
   * @return the sum of the rolled dice.
   */
  public int roll(int nbrDice, int nbrSides)
  {
    if (nbrSides < 1) {
      nbrSides = 1;
    }

    int sum = 0;
    for (int k = 0; k < nbrDice; k++) {
      sum += _generator.nextInt(nbrSides) + 1;
    }
    return (sum);
  }


  /**
   * Convert a dice notation string to two numbers, then roll the dice as indicated by the string.
   * <br>
   * Notation format: {[n][n] 'd' n[n][n] ['+' | '-'] n]}, that is, 1-100 required letter 'd', and
   * 1-100, e.g. 2d10, 1d8+1, or 2d4-1. Optional +n to provide minimums. <br>
   * In cases where a zero result occurrs, e.g., 2d4-2 has two chances of zero result, the value 1
   * is returned
   * 
   * @param notation format as explained above
   * @return the sum of the rolled dice; else 1 if a zero occurred
   * @throws NumberFormatException if notation cannot be numerically parsed
   * @throws ApplicationException if d20 format is invalid
   */
  public int roll(String notation) throws ApplicationException
  {
    Pattern p = Pattern.compile("(?<A>\\d*)d(?<B>\\d+)(?>(?<ADD>[+-])(?<D>\\d+))?");
    //    Pattern p = Pattern.compile("(\\d*)?d(\\d+)([+-]\\d)");
    Matcher matcher = p.matcher(notation);

    if (matcher.matches()) {
      int rolls = getInt(matcher, "A", 1);
      int faces = getInt(matcher, "B", -1);
      int addValue = getInt(matcher, "D", 0);
      int sign = getSign(matcher, "ADD", "+");

      return roll(rolls, faces) + sign * addValue;
    } else {
      System.out.println("Failed to match");
      return -1;
    }
  }

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

  //    
  //    } else {
  //      return -1;
  //    }
  //    
  //    int MIN_LEN = 2; // e.g., d6
  //    int MAX_LEN = 8; // e.g. 10d100-4
  //    int len = notation.length();
  //    if ((len < MIN_LEN) || (len > MAX_LEN)) {
  //      throw new ApplicationException("Invalid length in d20 string notation");
  //    }
  //
  //    // Default to no addon minimum values
  //    int addon = 0;
  //    int minVal = 0;
  //    StringBuffer sb = new StringBuffer(notation);
  //    int delim = sb.indexOf("d");
  //    if (delim == NOT_FOUND) {
  //      throw new ApplicationException("Cannot find the delimiter 'd' in " + notation);
  //    }
  //
  //    // Search for addons
  //    int plusIndex = sb.indexOf("+");
  //    // If addon value given, parse it first and adjust length
  //    // quirk of parseInt: cannot parse '+' sign, but can parse '-' sign
  //    if (plusIndex != NOT_FOUND) {
  //      addon = Integer.parseInt(sb.substring(plusIndex + 1, len));
  //      len = len - plusIndex + 1;
  //    } else {
  //      plusIndex = sb.indexOf("-");
  //      if (plusIndex != NOT_FOUND) {
  //        addon = Integer.parseInt(sb.substring(plusIndex, len));
  //        len = len - plusIndex;
  //      }
  //    }
  //    // Case: Default coefficient of notation is 1, e.g. "d8" = "1d8"
  //    int nbrDice = (delim == 0) ? 1 : Integer.parseInt(sb.substring(0, delim));
  //
  //    // Get the number following the 'd', ignoring the addon if it exists
  //    int nbrSides = Integer.parseInt(sb.substring(delim + 1, len));
  //    // Roll and add the numeric equivalent of the input d20 string
  //    int sum = roll(nbrDice, nbrSides);
  //    // Addjust for any addons that exist
  //    minVal = addon;
  //    int finalValue = sum + minVal;
  //    finalValue = (finalValue <= 0) ? 1 : finalValue;
  //    return finalValue;
  //  }


  /**
   * Returns a linear random number in terms of percent. Same as <code>getRandom(1, 100)</code>.
   * 
   * @return value from 1 to 100
   */
  public int rollPercent()
  {
    return getRandom(1, 100);
  }


  public int[] rollTraits()
  {
    int traits[] = new int[6];
    for (int i = 0; i < traits.length; i++) {
      traits[i] = rollTrait();
    }
    return traits;
  }


  /**
   * Returns a single trait using the 4d6-1 algorithm: Four d6 dice are rolled, and the lowest die
   * roll is removed from the list, giving an average of 12.25 for a Person's trait. Imposes the
   * constraint that all traits must be greater than a certain minimum value.
   * 
   * @return value from 3 (three 1's) to 18 (three 6's)
   */
  public int rollTrait()
  {
    // Constants to define the algorithm
    final int NBR_ROLLS = 4;
    final int NBR_DICE = 1;
    final int NBR_SIDES = 6;
    // Collect the rolls
    int[] tally = new int[NBR_ROLLS];
    for (int k = 0; k < NBR_ROLLS; k++) {
      tally[k] = roll(NBR_DICE, NBR_SIDES);
    }
    // Find smallest roll in set
    int min = 99;
    int sum = 0;
    for (int k = 0; k < NBR_ROLLS; k++) {
      min = Math.min(tally[k], min);
      sum += tally[k];
    }
    // Remove the smallest roll from the sum of all rolls
    sum -= min;
    return sum;
  }
} // end of MetaDie class

