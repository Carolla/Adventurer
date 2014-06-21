/**
 * TestUtilities.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package test.pdc;

import static org.junit.Assert.*;
import chronos.pdc.Utilities;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *   Test the various and sundry static methods in the Utilities class
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Jan 28, 2013   // original <DD>
 * </DL>
 */
public class TestUtilities
{

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /** Crop a multi-line string to a fixed limit, and replace the last blank space within that limit
     * with the newline sequence.
     * @Normal  single line already within the limit, with and without newlines.  
     * @Normal  single line with no newline characters already in it 
     */
    @Test
    public void testWordWrap()
    {
        MsgCtrl.auditMsgsOn(true);
        MsgCtrl.errorMsgsOn(true);
        MsgCtrl.msgln(this, "\t testWordWrap():" );

        // Set the line length to 30 for convenience
        int WIDTH = 40;
        // Test text
        String s1 = "This is the short test string";
        String s2 = "This is the\n short\n test string";
        String s3 = "This is the test string that is over length and contains no included new lines.";
        String s4 = "This is the test string that has a newline\n  in the second segment of its length.";
        
        // Normal single line already within the limit, without internal newline
        String result = Utilities.wordWrap(s1, WIDTH);
        MsgCtrl.msgln("[1]: " +  result);
        assertTrue(result.equals(s1));
        
        // Normal single line already within the limit, with two internal newlines
        // TODO: Ensure that newlines still wrap the text
        result = Utilities.wordWrap(s2, WIDTH);
        MsgCtrl.msgln("[2]: " + result);
        assertTrue(result.equals(s2));
        
        // Normal single overly long line without internal newline, results in three formatted lines
        result = Utilities.wordWrap(s3, WIDTH);
        MsgCtrl.msgln("[3]: " + result);
        assertEquals(s3.length(), result.length());         // newlines replaced blank chars

        // Normal Overly long line with internal newline, results in three formatted lines
        result = Utilities.wordWrap(s4, WIDTH);
        MsgCtrl.msgln("[4]: " + result);
        assertEquals(s4.length(), result.length());         // newlines replaced blank chars
}


}           // end of TestUtilities class
