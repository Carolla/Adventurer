/**
 * TestUtilities.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package mylib.test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import mylib.MsgCtrl;
import mylib.pdc.Utilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the various and sundry static methods in the Utilities class
 * 
 * @author Alan Cline
 * @version Jan 28, 2013 // original <br>
 *          May 6, 2013 // moved to JUnit 4 <br>
 *          Oct 4, 2014 // added tests to match the new methods in Utilities <br>
 *          Oct 14, 2014 // added more tests for untested methods <br>
 */
public class TestUtilities
{

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {}

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ================================================================================
  // BEGIN TESTS
  // ================================================================================

  /**
   * static String cropLine(String msg, int width) Truncates text line {@code msg} to within
   * {@code width} limit by replacing last space in limit with newline character.
   * 
   * @Normal A line of text is cropped before the last word <br>
   * @Normal A line of text is cropped after the last word, at a white space <br>
   * @Normal A line of text is cropped after a tab (valid) character <br>
   * @Normal A line of text is cropped after a carriage return (\r) character <br>
   * @Normal String contains newline character at crop point. <br>
   * @Normal String contains backspace character <br>
   * @Normal A backspace character (\b) is not counted as whitespace <br>
   * @Normal An escaped double quote (\") is not counted as whitespace <br>
   * @Normal An escaped single quote (\') is not counted as whitespace <br>
   * @Normal An escaped escape char (\\) is not counted as whitespace <br>
   */
  @Test
  public void testCropLine()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Setup Vertical bar shows crop location
    String[] base = {
        "A line of text is cropped before the last word. ",
        "A line of text is cropped after last word. ",
        "A line of text is cropped inside...last word. ",
        "A line of text is cropped eight word-- middle of sentence. "};
    int[] cropPoint = {41, 42, 39, 38};
    int lineLimit = 42;

    // Normal A line of text is cropped at various locations in the string
    for (int k = 0; k < base.length; k++) {
      String s1 = Utilities.cropLine(base[k], lineLimit);
      MsgCtrl.msg("\n\tOld length = " + base[k].length());
      MsgCtrl.msgln("\tNew length = " + s1.length());
      MsgCtrl.msgln("\t" + base[k]);
      MsgCtrl.msgln("\t" + s1);
      assertTrue(s1.length() == cropPoint[k]);
    }

    // Normal contains a (valid) tab character
    String sTest = "This is break...\t due to a tab char.";
    int limit = 17;
    int crop = 16;
    String s1 = Utilities.cropLine(sTest, limit);
    MsgCtrl.msg("\n\tOld length = " + sTest.length());
    MsgCtrl.msgln("\tNew length = " + s1.length());
    MsgCtrl.msgln("\t" + sTest);
    MsgCtrl.msgln("\t" + s1);
    assertTrue(s1.length() == crop);

    // Normal contains a carriage return (\r) character
    sTest = "This is break...\r due to a carriage return (\r) character.";
    limit = 17;
    crop = 16;
    s1 = Utilities.cropLine(sTest, limit);
    MsgCtrl.msg("\n\tOld length = " + sTest.length());
    MsgCtrl.msgln("\tNew length = " + s1.length());
    MsgCtrl.msgln("\t" + sTest);
    MsgCtrl.msgln("\t" + s1);
    assertTrue(s1.length() == crop);

    // Normal last char is already a newline char
    sTest = "This is a pre-truncated character sequence.\n";
    limit = 44;
    int cropLen = 44;
    s1 = Utilities.cropLine(sTest, limit);
    MsgCtrl.msg("\n\tOld length = " + sTest.length());
    MsgCtrl.msgln("\tNew length = " + s1.length());
    MsgCtrl.msgln("\t" + sTest);
    MsgCtrl.msgln("\t" + s1);
    assertTrue(s1.length() == cropLen);

    // Setup Vertical bar shows crop location
    String[] base2 = {
        "This is break...\b due to a backspace char.",
        "This is break...\" due to an escaped double quote.",
        "This is break...\' due to an escaped single quote.",
        "This is break...\\ due to an escaped escape char."};

    // Normal text line contains special characters that are not counted backspace character
    limit = 17;
    crop = 17;
    for (int k = 0; k < base2.length; k++) {
      s1 = Utilities.cropLine(base2[k], limit);
      int slen = s1.length();
      MsgCtrl.msg("\n\tOld length = " + base2[k].length());
      MsgCtrl.msgln("\tNew length = " + slen);
      MsgCtrl.msgln("\t" + base2[k]);
      MsgCtrl.msg("\t" + s1);
      assertTrue(slen == crop);
      MsgCtrl.msgln("");
    }
  }


  /**
   * static String cropLine(String msg, int width) Truncates text line {@code msg} to within
   * {@code width} limit by replacing last space in limit with newline character.
   * 
   * @Special Input line is less than crop width permitted; return input string <br>
   * @Error Input line contains a newline within the width; return input string <br>
   * @Null Null input line <br>
   */
  @Test
  public void testCropLineErrors()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Special Message is too short to need cropping
    String sTest = "This is a short line.";
    int limit = 44;
    int cropLen = 21;
    String s1 = Utilities.cropLine(sTest, limit);
    MsgCtrl.msg("\n\tOld length = " + sTest.length());
    MsgCtrl.msgln("\tNew length = " + s1.length());
    MsgCtrl.msgln("\t" + sTest);
    MsgCtrl.msgln("\t" + s1);
    assertTrue(s1.length() == cropLen);
    MsgCtrl.errMsgln("\tExpected msg: No truncation needed");

    // Error Message contains multiple lines
    sTest = "This is a \n pre-truncated character sequence.\n";
    limit = 46;
    cropLen = 46;
    s1 = Utilities.cropLine(sTest, limit);
    MsgCtrl.msg("\n\tOld length = " + sTest.length());
    MsgCtrl.msgln("\tNew length = " + s1.length());
    assertTrue(s1.length() == cropLen);
    MsgCtrl.errMsgln("\tExpected msg: Multiple lines received--truncation skipped");

    // Null Message contains input
    s1 = Utilities.cropLine(null, limit);
    assertNull(s1);
    MsgCtrl.errMsgln("\tExpected msg: Null input line--cannot crop.");
  }


  /**
   * static String formatInches(String) Converts from inches, to feet and inches
   * 
   * @Normal string value must be integer less than, equal to, and greater than one foot <br>
   * @Normal string value contains exactly zero inches <br>
   * @Error string value contains decimal fraction <br>
   * @Error string value contains negative number <br>
   * @Error string value contains empty string <br>
   * @Null string parm is null <br>
   */
  @Test
  public void testFormatInches()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    String[] values = {"11", "12", "13", "27", "101", "0"};
    String[] expValues = {"0' 11\"", "1' 0\"", "1' 1\"", "2' 3\"", "8' 5\"", "0' 0\""};

    // Normal run a series of heights through the converter
    for (int k = 0; k < values.length; k++) {
      String result = Utilities.formatInches(values[k]);
      MsgCtrl.msgln("\t" + values[k] + " inches = " + result);
      assertTrue(result.equals(expValues[k]));
    }

    // Error string value contains decimal fraction
    String fracValue = "12.5";
    try {
      Utilities.formatInches(fracValue);
    } catch (NumberFormatException ex) {
      MsgCtrl.errMsgln("\tExpected NumberFormatException for decimal fraction");
    }

    // Error string value contains negative number
    fracValue = "-24";
    MsgCtrl.errMsgln("\tExpected null return for negative height");
    assertNull(Utilities.formatInches(fracValue));

    // Error string value contains empty string
    fracValue = " ";
    MsgCtrl.errMsgln("\tExpected null return for empty string");
    assertNull(Utilities.formatInches(fracValue));

    // Null string parm is null
    MsgCtrl.errMsgln("\tExpected null return for null parm");
    assertNull(Utilities.formatInches(null));
  }


  /**
   * static String formatOunces(String) Converts from ounces to pounds and ounces format
   * 
   * @Normal string value must be integer less than, equal to, and greater than one pound <br>
   * @Normal string value contains exactly zero ounces <br>
   * @Error string value contains decimal fraction <br>
   * @Error string value contains negative number <br>
   * @Error string value contains empty string <br>
   * @Null string parm is null <br>
   */
  @Test
  public void testFormatOunces()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    String[] values = {"15", "16", "17", "32", "101", "0"};
    String[] expValues =
    {"0 lb. 15 oz.", "1 lb. 0 oz.", "1 lb. 1 oz.", "2 lb. 0 oz.", "6 lb. 5 oz.", "0 lb. 0 oz."};

    // Normal run a series of heights through the converter
    for (int k = 0; k < values.length; k++) {
      String result = Utilities.formatOunces(values[k]);
      MsgCtrl.msgln("\t" + values[k] + " oz = " + result);
      assertTrue(result.equals(expValues[k]));
    }

    // Error string value contains decimal fraction
    String fracValue = "12.5";
    try {
      Utilities.formatOunces(fracValue);
    } catch (NumberFormatException ex) {
      MsgCtrl.errMsgln("\tExpected NumberFormatException for decimal fraction");
    }

    // Error string value contains negative number
    fracValue = "-24";
    MsgCtrl.errMsgln("\tExpected null return for negative height");
    assertNull(Utilities.formatOunces(fracValue));

    // Error string value contains empty string
    fracValue = " ";
    MsgCtrl.errMsgln("\tExpected null return for empty string");
    assertNull(Utilities.formatOunces(fracValue));

    // Null string parm is null
    MsgCtrl.errMsgln("\tExpected null return for null parm");
    assertNull(Utilities.formatOunces(null));
  }


  /**
   * static String formatSecondss(String) Converts from seconds to years and fractional years
   * 
   * @Normal string value must be integer less than, equal to, and greater than one year <br>
   * @Normal string value contains exactly zero seconds <br>
   * @Error string value contains decimal fraction <br>
   * @Error string value contains negative number <br>
   * @Error string value contains empty string <br>
   * @Null string parm is null <br>
   */
  @Test
  public void testFormatSeconds()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    String[] values = {"3600", "86400", "2592000", "31104000", "62208000", "87654321", "0"};
    String[] expValues =
    {"0.000 yrs.", "0.003 yrs.", "0.083 yrs.", "1.000 yrs.", "2.000 yrs.", "2.818 yrs.",
        "0.000 yrs."};
    // String[] expValues =
    // {"0.000115741 yrs.", "0.002777778 yrs.", "0.08333333 yrs.", "1.000 yrs.", "2.000 yrs.",
    // "2.818194456 yrs.", "0 yrs."};

    // Normal run a series of heights through the converter
    for (int k = 0; k < values.length; k++) {
      String result = Utilities.formatSeconds(values[k]);
      MsgCtrl.msgln("\t" + values[k] + " secs = " + result);
      assertTrue(result.equals(expValues[k]));
    }

    // Error string value contains decimal fraction
    String fracValue = "12.5";
    try {
      Utilities.formatSeconds(fracValue);
    } catch (NumberFormatException ex) {
      MsgCtrl.errMsgln("\tExpected NumberFormatException for decimal fraction");
    }

    // Error string value contains negative number
    fracValue = "-24";
    MsgCtrl.errMsgln("\tExpected null return for negative height");
    assertNull(Utilities.formatSeconds(fracValue));

    // Error string value contains empty string
    fracValue = " ";
    MsgCtrl.errMsgln("\tExpected null return for empty string");
    assertNull(Utilities.formatSeconds(fracValue));

    // Null string parm is null
    MsgCtrl.errMsgln("\tExpected null return for null parm");
    assertNull(Utilities.formatSeconds(null));
  }

  /**
   * static boolean isEmptyString(String target) Returns true if {@code target} is only white space
   * 
   * @Normal checks for non-empty string <br>
   * @Normal checks for empty string <br>
   * @Normal checks for all-spaces target <br>
   * @Normal checks for control characters: \n\b\r\t <br>
   * @Null checks for null parameter
   */
  @Test
  public void testIsEmptryString()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Normal checks for non-empty string <br>
    String s1 = "non-empty test string";
    MsgCtrl.msgln("\tfalse:\t " + s1);
    assertFalse(Utilities.isEmptyString(s1));

    // Normal checks for empty string
    s1 = "";
    MsgCtrl.msgln("\ttrue:\t (empty string)");
    assertTrue(Utilities.isEmptyString(s1));

    // Normal checks for empty string
    s1 = "     ";
    MsgCtrl.msgln("\ttrue:\t (five space characters)");
    assertTrue(Utilities.isEmptyString(s1));

    // Normal checks for control characters: \n\b\r\t
    s1 = "\n\b\r\t";
    MsgCtrl.msgln("\ttrue:\t (\n\b\r\t characters)");
    assertTrue(Utilities.isEmptyString(s1));

    // Error checks for null parameter
    MsgCtrl.msgln("\ttrue:\t (null parm)");
    assertTrue(Utilities.isEmptyString(null));

  }


  /**
   * static boolean isTraitsEqual(int[] expValue, int[] testValue) Checks if two int equal-length
   * arrays are equal, element by element. Arrays must be of equal length.
   * 
   * @Normal arrays contain exactly the same elements <br>
   * @Normal arrays contain one value different <br>
   * @Normal arrays contain all values different <br>
   * @Error arrays are of different length <br>
   * @Error arrays are not totally filled but have 0 values in one or more slots <br>
   * @Special arrays have 0's in all slots; legal but useless for comparing prime traits
   * @Null one or both parms are null
   */
  @Test
  public void testIsTraitsEqual()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Setup
    int[] ctrlValues = {12, 12, 12, 13, 13, 18};

    // Normal arrays contain exactly the same elements
    int[] expValue1 = {12, 12, 12, 13, 13, 18};
    MsgCtrl.msgln("\ttrue: identical arrays");
    assertTrue(Utilities.isTraitsEqual(expValue1, ctrlValues));

    // Normal arrays contain one value different
    int[] expValue2 = {12, 12, 12, 13, 11, 18};
    MsgCtrl.msgln("\tfalse: only 5th element is different");
    assertFalse(Utilities.isTraitsEqual(expValue2, ctrlValues));

    // Normal arrays contain one value different
    int[] expValue3 = {15, 15, 15, 15, 15, 15};
    MsgCtrl.msgln("\tfalse: all elements have different values");
    assertFalse(Utilities.isTraitsEqual(expValue3, ctrlValues));

    // Error arrays are of different length
    int[] expValue4 = {12, 12, 12, 13, 13};
    MsgCtrl.msgln("\tfalse: first array parm is shorter than second array parm");
    assertFalse(Utilities.isTraitsEqual(expValue4, ctrlValues));

    MsgCtrl.msgln("\tfalse: second array parm is shorter than first array parm");
    assertFalse(Utilities.isTraitsEqual(ctrlValues, expValue4));

    // Error arrays have 0 in some slots
    int[] expValue5 = {12, 0, 12, 12, 13, 13, 18};
    MsgCtrl.msgln("\tfalse: second element contains a 0 value");
    assertFalse(Utilities.isTraitsEqual(expValue5, ctrlValues));

    // Special arrays have 0's in all slots; legal but useless for comparing prime traits
    int[] expValue6 = {0, 0, 0, 0, 0};
    MsgCtrl.msgln("\tfalse: both arrays are only 5-elements long, and contains all zeroes");
    assertTrue(Utilities.isTraitsEqual(expValue6, expValue6));

    // Null one or both parms are null
    MsgCtrl.msgln("\tfalse: first parm is null; second parm is not");
    assertFalse(Utilities.isTraitsEqual(null, ctrlValues));
    MsgCtrl.msgln("\tfalse: first parm is not null; second parm is null");
    assertFalse(Utilities.isTraitsEqual(ctrlValues, null));
    MsgCtrl.msgln("\tfalse: both parms are null");
    assertFalse(Utilities.isTraitsEqual(null, null));

  }


  /**
   * static {@code ArrayList<String> sort(ArrayList<String> target)} Sort on {@code target}
   * alphabetically, The sort algorithm uses a {@code TreeSet} because it automatically uses an
   * insert-sort algorithm with no duplicates.
   * 
   * @Normal Sort an unsorted list of unique elements <br>
   * @Normal Sort an already sorted list <br>
   * @Normal Sort an unsorted list with duplicate and triplicate elements <br>
   * @Normal Sort an unsorted list with empty elements<br>
   * @Error Input parm has no length <br>
   * @Null  input parm is null
   */
  @Test
  public void testUniqueSort()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP: Upper case, lower case, punctuation
    String[] raw = {"The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog", "."};
    String[] exp = { ".", "brown", "dog", "fox", "jumps", "lazy", "over", "quick", "the", "The"};
    ArrayList<String> ctrlList = convertToArrayList(raw);
    MsgCtrl.msg("\t");
    for (int k = 0; k < ctrlList.size(); k++) {
      MsgCtrl.msg(" " + ctrlList.get(k));
    }

    // Normal Sort an unsorted list of unique elements <br>
    ArrayList<String> sorted = Utilities.uniqueSort(ctrlList);
    MsgCtrl.msg("\n1. \t");
    for (int k = 0; k < sorted.size(); k++) {
      MsgCtrl.msg(" " + sorted.get(k));
      assertTrue(sorted.get(k).equals(exp[k]));
    }

    // Normal Sort an already sorted list <br>
    sorted = Utilities.uniqueSort(sorted);
    MsgCtrl.msg("\n2. \t");
    for (int k = 0; k < sorted.size(); k++) {
      MsgCtrl.msg(" " + sorted.get(k));
      assertTrue(sorted.get(k).equals(exp[k]));
    }

    // Normal Sort an unsorted list with duplicate and triplicate elements <br>
    String[] raw2 = {"The", "quick", "fox", "fox", ".", "the", "over", "the", "lazy", "dog", "."};
    String[] expRaw2 = {".", "dog", "fox", "lazy", "over", "quick", "the", "The"};
    ctrlList = convertToArrayList(raw2);
    sorted = Utilities.uniqueSort(ctrlList);
    MsgCtrl.msg("\n3. \t");
    for (int k = 0; k < sorted.size(); k++) {
      MsgCtrl.msg(" " + sorted.get(k));
      assertTrue(sorted.get(k).equals(expRaw2[k]));
    }

    // Normal Sort an unsorted list with empty elements<br>
    String[] raw3 = {"The", "quick", "   ", "fox", "jumps", "over", "the", "\t", "dog", "."};
    String[] expRaw3 = { "   ", "\t", ".", "dog", "fox", "jumps", "over", "quick", "the", "The"};
    ctrlList = convertToArrayList(raw3);
    sorted = Utilities.uniqueSort(ctrlList);
    MsgCtrl.msg("\n4. \t");
    for (int k = 0; k < sorted.size(); k++) {
      MsgCtrl.msg(" " + sorted.get(k));
      assertTrue(sorted.get(k).equals(expRaw3[k]));
    }

    //  Error Input parm has no length <br>
    ArrayList<String> noData = new ArrayList<String>();
    sorted = Utilities.uniqueSort(noData);
    assertTrue(sorted.size() == 0);
    
    // Null  enter null input parm
    sorted = Utilities.uniqueSort(null);
    assertNull(sorted);
  }

  
  /**
   * static String wordWrap(String msg, int width) Crop a multi-line {@code msg} string to a fixed
   * {@code width} limit, and replace the last blank space within that limit with the newline
   * sequence.
   * 
   * @Normal single line already within the limit, with and without newlines. <br>
   * @Normal single line with no newline characters already in it. <br>
   */
  @Test
  public void testWordWrap()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Set the line length to 30 for convenience
    int WIDTH = 40;
    // Test text
    String s1 = "This is the short test string";
    String s2 = "This is the\n short\n test string";
    String s3 = "This is the test string that is over length and contains no included new lines.";
    String s4 =
        "This is the test string that has a newline\n  in the second segment of its length.";

    // Generate a really long test string
    int LINE_LEN = 3000;
    int SEGMENT = 11;
    StringBuilder sb = new StringBuilder(LINE_LEN);
    for (int k = 0; k < LINE_LEN; k++) {
      // Place a newline every 17 characters
      if ((sb.length() % SEGMENT) == 0) {
        sb.append(' ');
      }
      else {
        sb.append('w');
      }
    }
    String longLine = new String(sb);
    MsgCtrl.msgln("\n" + longLine);

    // Normal single line already within the limit, without internal newline
    String result = Utilities.wordWrap(s1, WIDTH);
    MsgCtrl.msgln("[1]: " + result);
    assertTrue(result.equals(s1));

    // Normal single line already within the limit, with two internal newlines
    // TODO: Ensure that newlines still wrap the text
    result = Utilities.wordWrap(s2, WIDTH);
    MsgCtrl.msgln("[2]: " + result);
    assertTrue(result.equals(s2));

    // Normal single overly long line without internal newline, results in three formatted lines
    result = Utilities.wordWrap(s3, WIDTH);
    MsgCtrl.msgln("[3]: " + result);
    assertEquals(s3.length(), result.length()); // newlines replaced blank chars

    // Normal Overly long line with internal newline, results in three formatted lines
    result = Utilities.wordWrap(s4, WIDTH);
    MsgCtrl.msgln("[4]: " + result);
    assertEquals(s4.length(), result.length()); // newlines replaced blank chars

    // Normal really long string test
    result = Utilities.wordWrap(longLine, WIDTH);
    MsgCtrl.msgln("[5]: " + result);
    assertEquals(longLine.length(), result.length()); // newlines replaced blank chars

  }

  // ================================================================================
  // Private Helper Methods
  // ================================================================================

  /** Convert string[] to ArrayList<String> */
  private ArrayList<String> convertToArrayList(String[] source)
  {
    ArrayList<String> dest = new ArrayList<String>();
    for (int k = 0; k < source.length; k++) {
      dest.add(source[k]);
    }
    return dest;
  }



} // end of TestUtilities class
