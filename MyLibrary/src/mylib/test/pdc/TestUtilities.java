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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mylib.MsgCtrl;
import mylib.pdc.Utilities;


/**
 * Test the various and sundry static methods in the Utilities class
 * 
 * @author Alan Cline
 * @version Jan 28, 2013 // original <br>
 *          May 6, 2013 // moved to JUnit 4 <br>
 *          Oct 4, 2014 // added tests to match the new methods in Utilities <br>
 *          Oct 14, 2014 // added more tests for untested methods <br>
 *          June 4 2017 // added test for multiline() <br>
 *          July 2, 2017 // cleanupped after major refactoring in apps <br>
 * 
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
   * @Normal.Test A line of text is cropped before the last word <br>
   * @Normal.Test A line of text is cropped after the last word, at a white space <br>
   * @Normal.Test A line of text is cropped after a tab (valid) character <br>
   * @Normal.Test A line of text is cropped after a carriage return (\r) character <br>
   * @Normal.Test String contains newline character at crop point. <br>
   * @Normal.Test String contains backspace character <br>
   * @Normal.Test A backspace character (\b) is not counted as whitespace <br>
   * @Normal.Test An escaped double quote (\") is not counted as whitespace <br>
   * @Normal.Test An escaped single quote (\') is not counted as whitespace <br>
   * @Normal.Test An escaped escape char (\\) is not counted as whitespace <br>
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
   * @Special.Test Input line is less than crop width permitted; return input string <br>
   * @Error.Test Input line contains a newline within the width; return input string <br>
   * @Null.Test Null input line <br>
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
   * static boolean isEmptyString(String target) Returns true if {@code target} is only white space
   * 
   * @Normal.Test checks for non-empty string <br>
   * @Normal.Test checks for empty string <br>
   * @Normal.Test checks for all-spaces target <br>
   * @Normal.Test checks for control characters: \n\b\r\t <br>
   * @Null.Test checks for null parameter
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
   * static String formatInches(String) Converts from inches, to feet and inches
   * 
   * @Normal.Test string value must be integer less than, equal to, and greater than one foot <br>
   * @Normal.Test string value contains exactly zero inches <br>
   * @Error.Test string value contains decimal fraction <br>
   * @Error.Test string value contains negative number <br>
   * @Error.Test string value contains empty string <br>
   * @Null.Test string parm is null <br>
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
   * Converts from feet to feet and inches format
   * 
   * @Normal.Test zero <br>
   * @Normal.Test various whole numbers <br>
   * @Normal.Test various floating-point value <br>
   */
  @Test
  public void testFormatDistance()
  {
    double[] expected = {0.0, 0.0};
    double[] actual = {0.0, 0.0};
    double numFeet = 0.0;

    // Normal - zero
    numFeet = 0.0;
    actual = Utilities.formatDistance(numFeet);
    assertEquals(expected[0], actual[0], 0.1);

    // Normal - whole numbers
    numFeet = 1.0;
    expected[0] = 1.0;
    actual = Utilities.formatDistance(numFeet);
    assertEquals(expected[0], actual[0], 0.1);
    // 2nd trial
    numFeet = 2.0;
    expected[0] = 2.0;
    actual = Utilities.formatDistance(numFeet);
    // 3rd trial
    numFeet = 3.0;
    expected[0] = 3.0;
    actual = Utilities.formatDistance(numFeet);
  }



  /**
   * static String formatOunces(String) Converts from ounces to pounds and ounces format
   * 
   * @Normal.Test string value must be integer less than, equal to, and greater than one pound <br>
   * @Normal.Test string value contains exactly zero ounces <br>
   * @Error.Test string value contains decimal fraction <br>
   * @Error.Test string value contains negative number <br>
   * @Error.Test string value contains empty string <br>
   * @Null.Test string parm is null <br>
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
   * @Normal.Test string value must be integer less than, equal to, and greater than one year <br>
   * @Normal.Test string value contains exactly zero seconds <br>
   * @Error.Test string value contains decimal fraction <br>
   * @Error.Test string value contains negative number <br>
   * @Error.Test string value contains empty string <br>
   * @Null.Test string parm is null <br>
   */
  @Test
  public void testFormatSeconds()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    String[] values = {"3600", "86400", "2592000", "31104000", "62208000", "87654321", "0"};
    String[] expValues = {"0.000 yrs.", "0.003 yrs.", "0.083 yrs.", "1.000 yrs.", "2.000 yrs.",
        "2.818 yrs.", "0.000 yrs."};

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
   * static boolean isTraitsEqual(int[] expValue, int[] testValue) Checks if two int equal-length
   * arrays are equal, element by element. Arrays must be of equal length.
   * 
   * @Normal.Test arrays contain exactly the same elements <br>
   * @Normal.Test arrays contain one value different <br>
   * @Normal.Test arrays contain all values different <br>
   * @Error.Test arrays are of different length <br>
   * @Error.Test arrays are not totally filled but have 0 values in one or more slots <br>
   * @Special.Test arrays have 0's in all slots; legal but useless for comparing prime traits
   * @Null.Test one or both parms are null
   */
  @Test
  public void testIsEqual()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Setup
    int[] ctrlValues = {12, 12, 12, 13, 13, 18};

    // Normal arrays contain exactly the same elements
    int[] expValue1 = {12, 12, 12, 13, 13, 18};
    MsgCtrl.msgln("\ttrue: identical arrays");
    assertTrue(Utilities.isEqual(expValue1, ctrlValues));

    // Normal arrays contain one value different
    int[] expValue2 = {12, 12, 12, 13, 11, 18};
    MsgCtrl.msgln("\tfalse: only 5th element is different");
    assertFalse(Utilities.isEqual(expValue2, ctrlValues));

    // Normal arrays contain one value different
    int[] expValue3 = {15, 15, 15, 15, 15, 15};
    MsgCtrl.msgln("\tfalse: all elements have different values");
    assertFalse(Utilities.isEqual(expValue3, ctrlValues));

    // Error arrays are of different length
    int[] expValue4 = {12, 12, 12, 13, 13};
    MsgCtrl.msgln("\tfalse: first array parm is shorter than second array parm");
    assertFalse(Utilities.isEqual(expValue4, ctrlValues));

    MsgCtrl.msgln("\tfalse: second array parm is shorter than first array parm");
    assertFalse(Utilities.isEqual(ctrlValues, expValue4));

    // Error arrays have 0 in some slots
    int[] expValue5 = {12, 0, 12, 12, 13, 13, 18};
    MsgCtrl.msgln("\tfalse: second element contains a 0 value");
    assertFalse(Utilities.isEqual(expValue5, ctrlValues));

    // Special arrays have 0's in all slots; legal but useless for comparing prime traits
    int[] expValue6 = {0, 0, 0, 0, 0};
    MsgCtrl.msgln("\tfalse: both arrays are only 5-elements long, and contains all zeroes");
    assertTrue(Utilities.isEqual(expValue6, expValue6));

    // Null one or both parms are null
    MsgCtrl.msgln("\tfalse: first parm is null; second parm is not");
    assertFalse(Utilities.isEqual(null, ctrlValues));
    MsgCtrl.msgln("\tfalse: first parm is not null; second parm is null");
    assertFalse(Utilities.isEqual(ctrlValues, null));
    MsgCtrl.msgln("\tfalse: both parms are null");
    assertFalse(Utilities.isEqual(null, null));
  }


  /**
   * Break a long line into fixed line segments (word wrap) at a white space just prior to the given
   * position.
   */
  @Test
  public void testWordWrap()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // For a fixed length of 50
    int len = 50;
    String oneLiner = "This is not an overly long line.";
    String twoLiner = "This is a single line that is greater than fifty lower case characters.";
    String threeLiner = "This is an overly long line that needs to be word-wrapped into three"
        + " segments of no greater than fifty characters.";

    ArrayList<String> expResult = new ArrayList<String>();

    // First a single line, no wrapping needed
    ArrayList<String> result = Utilities.wordWrap(oneLiner, len);
    assertEquals(oneLiner, result.get(0));
    expResult.clear();
    result.clear();

    // A two-line wrappable
    expResult.add("This is a single line that is greater than fifty");
    expResult.add("lower case characters.");
    result = Utilities.wordWrap(twoLiner, len);
    assertEquals(expResult.get(0), result.get(0));
    assertEquals(expResult.get(1), result.get(1));
    expResult.clear();
    result.clear();

    // A three-line wrappable
    expResult.add("This is an overly long line that needs to be");
    expResult.add("word-wrapped into three segments of no greater");
    expResult.add("than fifty characters.");
    result = Utilities.wordWrap(threeLiner, len);
    assertEquals(expResult.get(0), result.get(0));
    assertEquals(expResult.get(1), result.get(1));
    assertEquals(expResult.get(2), result.get(2));
    expResult.clear();
    result.clear();
  }


  /**
   * static {@code ArrayList<String> sort(ArrayList<String> target)} Sort on {@code target}
   * alphabetically, The sort algorithm uses a {@code TreeSet} because it automatically uses an
   * insert-sort algorithm with no duplicates.
   * 
   * @Normal.Test Sort an unsorted list of unique elements <br>
   * @Normal.Test Sort an already sorted list <br>
   * @Normal.Test Sort an unsorted list with duplicate and triplicate elements <br>
   * @Normal.Test Sort an unsorted list with empty elements<br>
   * @Error.Test Input parm has no length <br>
   * @Null.Test input parm is null
   */
  @Test
  public void testUniqueSort()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP: Upper case, lower case, punctuation
    String[] raw = {"The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog", "."};
    String[] exp = {".", "brown", "dog", "fox", "jumps", "lazy", "over", "quick", "the", "The"};
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
    String[] expRaw3 = {"   ", "\t", ".", "dog", "fox", "jumps", "over", "quick", "the", "The"};
    ctrlList = convertToArrayList(raw3);
    sorted = Utilities.uniqueSort(ctrlList);
    MsgCtrl.msg("\n4. \t");
    for (int k = 0; k < sorted.size(); k++) {
      MsgCtrl.msg(" " + sorted.get(k));
      assertTrue(sorted.get(k).equals(expRaw3[k]));
    }

    // Error Input parm has no length <br>
    ArrayList<String> noData = new ArrayList<String>();
    sorted = Utilities.uniqueSort(noData);
    assertTrue(sorted.size() == 0);

    // Null enter null input parm
    sorted = Utilities.uniqueSort(null);
    assertNull(sorted);
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
