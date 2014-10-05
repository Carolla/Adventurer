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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

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
   * Truncates text line to within limit by replacing last space in limit with newline character.
   * 
   * @Normal A line of text is cropped before the last word
   * @Normal A line of text is cropped after the last word, at a white space
   * @Normal A line of text is cropped after a tab (valid) character
   * @Normal A line of text is cropped after a carriage return (\r) character
   * @Normal String contains newline character at crop point.
   * @Normal String contains backspace character
   * @Normal A backspace character (\b) is not counted as whitespace
   * @Normal An escaped double quote (\") is not counted as whitespace
   * @Normal An escaped single quote (\') is not counted as whitespace
   * @Normal An escaped escape char (\\) is not counted as whitespace
   */
  @Test
  public void cropLine()
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
   * Error cases for mylib.pdc.Utilities.cropLine()
   * 
   * @Special Input line is less than crop width permitted; return input string
   * @Error Input line contains a newline within the width; return input string
   * @Null Null input line
   */
  @Test
  public void cropLineErrors()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
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
   * Crop a multi-line string to a fixed limit, and replace the last blank space within that limit
   * with the newline sequence.
   * 
   * @Normal single line already within the limit, with and without newlines.
   * @Normal single line with no newline characters already in it
   */
  @Test
  public void testWordWrap()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.msgln(this, "\t testWordWrap():");

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


} // end of TestUtilities class
