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
 */
public class Utilities
{

  /**
   * Replaces the last white space character with a newline character within the designated limit.
   * See {@code Utilities.wordWrap} for paragraphs of text
   * 
   * @param msg the string to insert the newline
   * @param width the limit to crop the string; the remnant is not returned
   * @return the newly cropped string
   */
  static public String cropLine(String msg, int width)
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
    int feet = total / Constants.INCHES_PER_FOOT;
    int inches = total % Constants.INCHES_PER_FOOT;
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
    int pounds = (int) total / Constants.OUNCES_PER_POUND;
    int ounces = (int) total % Constants.OUNCES_PER_POUND;
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
    // float total = Float.parseFloat(strSecs) / Constants.SECS_PER_YEAR;
    double total = Double.parseDouble(strSecs) / Constants.SECS_PER_YEAR;
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
   * Formats a long text line into a left-aligned, ragged-right paragraph within a given width. See
   * {@code Utilities.cropLine} for wrapping single lines
   * 
   * @param msg the string to insert the newline
   * @param width the limit to crop the string; the remnant is not returned
   * @return the new paragraph as a single string, but with inserted newlines
   */
  static public String wordWrap(String msg, int width)
  {
    // Normal single overly long line without internal newline, results in three formatted lines
    StringBuilder finalPara = new StringBuilder(msg.length());
    String result = Utilities.cropLine(msg, width);
    String remStr = msg.substring(result.length());
    finalPara.append(result);

    // Put the remaining end part of the original string back in to crop more off
    while (remStr.length() > 0) {
      result = Utilities.cropLine(remStr, width);
      finalPara.append(result);
      remStr = remStr.substring(result.length());
    }
    // Conver the buffer back to a string
    String finalString = new String(finalPara);
    return finalString;
  }



} // end of Utilities class
