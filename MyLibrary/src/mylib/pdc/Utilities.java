/**
 * Utilities.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package mylib.pdc;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import mylib.Constants;

/**
 * Collection of miscellaneous static methods to support the application classes.
 * 
 * @author Alan Cline
 * @version Dec 28, 2010 // original <br>
 *          Jan 29, 2013 // moved from Chronos to MyLibary <br>
 *          Oct 5, 2014 // cleaned up cropLine and added tests into {@code TestUtilities} <br>
 *          Oct 16, 2014 // renamed {@code sort} to {@code uniqueSort} to emphasize dups are removed
 *          during sort <br>
 *          Jan 22 2016 // added {@code convertToArrayList(String[])} <br>
 */
public class Utilities
{
  public static final int SECONDS_PER_HOUR = 3600;
  public static final int SECONDS_PER_DAY = 86400;
  public static final int DAYS_PER_YEAR = 360;
  public static final long SECONDS_PER_YEAR = SECONDS_PER_DAY * DAYS_PER_YEAR;
  public static final int OUNCES_PER_POUND = 16;
  public static final int INCHES_PER_FOOT = 12;


  /**
   * Convert a string to all lower case, then capitalize the first letter
   * 
   * @param str string to modify
   * @return the capitalized string
   */
  static public String capitalize(String str)
  {
    str = Character.toUpperCase(str.charAt(0)) + str.substring(1);
    return str;
  }


  /**
   * Constrain an integer array to fall within a given range for each element of the set by setting
   * any value to its upper or lower limit
   * 
   * @param values the original set to check
   * @param lowLimit the lower bound per value
   * @param upLimit the upper bound per value
   * @return the (possible revised) values arrays
   */
  static public int[] constrain(int[] values, int[] lowLimit, int[] upLimit)
  {
    for (int k = 0; k < values.length; k++) {
      values[k] = (values[k] < lowLimit[k]) ? lowLimit[k] : values[k];
      values[k] = (values[k] > upLimit[k]) ? upLimit[k] : values[k];
    }
    return values;
  }


  /**
   * Convert a @Code String[]} to an {@code ArrayList<String>} for easier handling
   * 
   * @param strs string array to convert
   * @return the converted ArrayList
   */
  public static ArrayList<String> convertToArrayList(String[] strs)
  {
    // Setup for arraylist
    ArrayList<String> alist = new ArrayList<String>();
    for (int k = 0; k < strs.length; k++) {
      alist.add(strs[k]);
    }
    return alist;
  }

  /**
   * Replaces the last white space character with a newline character within the designated limit.
   * See {@code Utilities.wordWrap} for paragraphs of text
   * 
   * @param msg the string to insert the newline
   * @param width the limit to crop the string; the remnant is not returned
   * @return the newly cropped string
   */
  public static String cropLine(String msg, int width)
  {
    if (msg == null) {
      return null;
    }

    // Ensure that msg needs to be cropped
    if (msg.length() <= width) {
      return msg;
    }
    // Ensure that msg contain no newlines already
    if ((msg.contains(Constants.NEWLINE)) && (msg.indexOf(Constants.NEWLINE) < width)) {
      return msg;
    }
    StringBuilder sb = new StringBuilder(msg);
    char nl = '\n';
    String result = null;
    // Any whitespace gets replaced with the newline char
    int k = width;
    char ch = sb.charAt(k);
    // Traverse text until white space is found...
    while (!Character.isWhitespace(ch)) {
      ch = sb.charAt(--k);
    }
    // ...then remove all white space in front of it....
    while (Character.isWhitespace(ch)) {
      ch = sb.charAt(--k);
    }
    // ...finally replacing first whitespace in trailing group with newline
    sb.setCharAt(++k, nl);
    // Convert the buffer back to a truncated string
    result = sb.substring(0, k);
    return result;
  }


  /**
   * Formats a double representing height in feet to one representing feet and inches
   * 
   * @param heightInFeet to convert into feet and inches
   * @return the converted input; else null if empty
   */
  public static double[] formatDistance(double heightInFeet)
  {
    // initial values
    double[] convertedHeight = {0.0, 0.0};
    // calculate total number of inches
    double totalInches = heightInFeet * INCHES_PER_FOOT;
    // calculate feet
    double feet = (int) (totalInches / INCHES_PER_FOOT);
    // forces calculation to round up
    double rounder = 0.5;
    // calculate inches rounded up to the nearest whole number
    double inches = (int) (totalInches + rounder) % 12;
    // assign calculated values
    convertedHeight[0] = feet;
    convertedHeight[1] = inches;
    // return
    return convertedHeight;
  }


  /**
   * Formats a String representing height in feet to one representing feet and inches
   * 
   * @param strHeight to convert into feet and inches
   * @return the converted input; else null if empty
   */
  public static String formatHeight(String strHeight)
  {
    // Guard: verify inut parm has data in it
    if (isEmptyString(strHeight)) {
      return null;
    }

    // Get integer and ensure that it is a positive number
    int total = Integer.parseInt(strHeight);
    if (total < 0) {
      return null;
    }
    int feet = total / INCHES_PER_FOOT;
    int inches = total % INCHES_PER_FOOT;
    String fullWt = String.format("%s' %s\"", feet, inches);
    return fullWt;
  }


  /**
   * Converts inches to feet and inches format
   * 
   * @param strInches to convert into feet and inches
   * @return the converted input
   */
  static public String formatInches(String strInches)
  {
    // Guard: input parm exists
    if (isEmptyString(strInches) == true) {
      return null;
    }

    // Get integer and ensure that it is a positive number
    int total = Integer.parseInt(strInches);
    if (total < 0) {
      return null;
    }
    // Format line into something presentantable
    int feet = total / INCHES_PER_FOOT;
    int inches = total % INCHES_PER_FOOT;
    String result = String.format("%s' %s\"", feet, inches);
    return result;
  }


  /**
   * Converts ounces to pounds and ounces format
   * 
   * @param strOunces to convert into pounds and ounces
   * @return the converted input; else null if empty
   */
  static public String formatOunces(String strOunces)
  {
    // Guard: verify inut parm has data in it
    if (isEmptyString(strOunces)) {
      return null;
    }

    // Get integer and ensure that it is a positive number
    double total = Double.parseDouble(strOunces);
    if (total < 0) {
      return null;
    }
    int pounds = (int) total / OUNCES_PER_POUND;
    int ounces = (int) total % OUNCES_PER_POUND;
    String fullWt = String.format("%s lb. %s oz.", pounds, ounces);
    return fullWt;
  }


  /**
   * Format seconds into years and fractional years
   * 
   * @param strSecs to convert into years
   * @return the converted input
   */
  static public String formatSeconds(String strSecs)
  {
    // Guard against empty string
    if (isEmptyString(strSecs)) {
      return null;
    }

    // Format line into something presentantable
    // float total = Float.parseFloat(strSecs) / SECS_PER_YEAR;
    double total = Double.parseDouble(strSecs) / SECONDS_PER_YEAR;
    // Only positive numbers are returned
    if (total < 0) {
      return null;
    }
    String age = String.format("%2.3f yrs.", total);
    return age;
  }


  /**
   * Checks that a string exists (not null) and contains more than white space
   * 
   * @param target to verify for existence
   * @return true if target is null or contains only white space
   */
  static public boolean isEmptyString(String target)
  {
    // String is assumed not empty
    boolean retval = false;

    // Guard against null target
    if (target == null) {
      return true;
    }
    // Remove any white space
    String s = target.trim();
    // Check for existence, including no characters in string
    if (s.length() == 0) {
      retval = true;
    }
    return retval;
  }


  /**
   * Checks if two int arrays are equal, element by element. Arrays must be of equal length.
   * 
   * @param expValue array of 6 traits to compare
   * @param testValue array of 6 traits to compare
   * @return true if all match; else false
   */
  static public boolean isEqual(int[] expValue, int[] testValue)
  {
    // Guard against non-existent trait array
    if ((expValue == null) || (testValue == null)) {
      return false;
    }

    // Guard against overrunning the array
    if (expValue.length != testValue.length) {
      return false;
    }
    // Compare each element of the two arrays
    boolean retval = false;
    // Traverse both arrays for equality
    for (int k = 0; k < testValue.length; k++) {
      if (testValue[k] != expValue[k]) {
        retval = false;
        break;
      } else {
        retval = true;
      }
    }
    return retval;
  }


  /**
   * Sort an {@code ArrayList<String>} alphabetically, which then can be accessed by the {@code get}
   * method sequentially. The sort algorithm uses a {@code TreeSet} because it automatically uses an
   * insert-sort algorithm with no duplicates. Whitespace and control chars, e.g., '\t' are sorted
   * before punctuation, before lower case, before upper case.
   * 
   * @param target unsorted list
   * @return the arraylist sorted, accessible sequentially by the get() method.
   */
  static public ArrayList<String> uniqueSort(ArrayList<String> target)
  {
    // Guard against null input
    if (target == null) {
      return null;
    }
    // Use the TreeSet collection and allow the insert-sort to work
    Collection<String> tree = new TreeSet<String>(Collator.getInstance());
    tree.addAll(target);

    // Now convert the TreeSet back to an ArrayList t
    ArrayList<String> result = new ArrayList<String>(target.size());
    for (String s : tree) {
      result.add(s);
    }

    return result;
  }


  /**
   * Break a long line into a series of list items. Each item is broken just before a white space,
   * and is not longer than the given length
   *
   * @param line the long string to wrap
   * @param maxLen the farthest position at which to break the line
   * @return a list of line segments that are less than maxLen
   */
  static public ArrayList<String> wordWrap(String line, int maxLen)
  {
    ArrayList<String> result = new ArrayList<>();
    StringBuffer sb = new StringBuffer();

    // Segment a long line into an array of strings separated by the delimiter SPACE or HYPHEN
    String[] words = line.split(Constants.SPACE);

    // Append words until maxLen would be exceeded
    for (int k = 0; k < words.length; k++) {
      if (sb.length() + words[k].length() <= maxLen) {
        sb.append(words[k]);
        sb.append(Constants.SPACE);
      } else {
        // Remove the white space at the end
        result.add(sb.toString().trim());
        sb.delete(0, sb.length());
        k = k - 1; // backstep one to get the skipped word
      }
    }
    // Remove the white space at the end
    result.add(sb.toString().trim());
    return result;
  }


} // end of Utilities class
