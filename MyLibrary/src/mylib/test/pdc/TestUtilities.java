/**
 * TestUtilities.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package mylib.test.pdc;

import static org.junit.Assert.*;

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
 *          July 8, 2017    // autogen: QA Tool added missing test methods <br>
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
  public void testIsEmptyString()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Normal checks for non-empty string <br>
    String s1 = "non-empty test string";
    MsgCtrl.msgln("\tfalse:\t " + s1);
    assertFalse(Utilities.isEmptyString(s1));
  }
  
	/**
 	 * Not Implemented String capitalize(String)
	 */
	@Test
	public void testCapitalize()
	{
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.where(this);

		fail("TEST METHOD NOT YET IMPLEMENTED");
	}


	/**
 	 * Not Implemented int[] constrain(int[], int[], int[])
	 */
	@Test
	public void testConstrain()
	{
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.where(this);

		fail("TEST METHOD NOT YET IMPLEMENTED");
	}


	/**
 	 * Not Implemented ArrayList convertToArrayList(String[])
	 */
	@Test
	public void testConvertToArrayList()
	{
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.where(this);

		fail("TEST METHOD NOT YET IMPLEMENTED");
	}


	/**
 	 * Not Implemented double[] formatDistance(double)
	 */
	@Test
	public void testFormatDistance()
	{
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.where(this);

		fail("TEST METHOD NOT YET IMPLEMENTED");
	}


	/**
 	 * Not Implemented String formatHeight(String)
	 */
	@Test
	public void testFormatHeight()
	{
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.where(this);

		fail("TEST METHOD NOT YET IMPLEMENTED");
	}


	/**
 	 * Not Implemented String formatInches(String)
	 */
	@Test
	public void testFormatInches()
	{
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.where(this);

		fail("TEST METHOD NOT YET IMPLEMENTED");
	}


	/**
 	 * Not Implemented String formatOunces(String)
	 */
	@Test
	public void testFormatOunces()
	{
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.where(this);

		fail("TEST METHOD NOT YET IMPLEMENTED");
	}


	/**
 	 * Not Implemented String formatSeconds(String)
	 */
	@Test
	public void testFormatSeconds()
	{
		MsgCtrl.auditMsgsOn(false);
		MsgCtrl.errorMsgsOn(false);
		MsgCtrl.where(this);

		fail("TEST METHOD NOT YET IMPLEMENTED");
	}


}   // end of TestUtilities class
