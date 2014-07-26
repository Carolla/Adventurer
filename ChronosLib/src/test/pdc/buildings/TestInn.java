/**
 * TestInn.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package test.pdc.buildings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mylib.ApplicationException;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.buildings.Inn;
import chronos.pdc.buildings.Inn.MockInn;
import chronos.pdc.registry.NPCRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

/**
 *    Verify that the Inn exists as a meeting place for Heroes, allows conversation with the
 *    Innkeeper andPatrons, provides sleeping and eating behavior, and can ban Heroes for
 *    certain amounts of time.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0       Jan 28, 2013   // original <DD>
 * </DL>
 */
public class TestInn
{
    /** Test target object */
    private Inn _inn = null;
    /** Associated mock */
    private MockInn _mock = null;
    
    /** Expected name of inn */
    private final String NAME = "Testy Tavern";
    /** Expected name of Innkeeper */
    private final String INNKEEPER = "Bork";
    /** Expected hovertext */
    private final String HOVERTEXT = "Click and see!";
    /** Introduction */
    private final String INTRO = "This is the hover text for the building icon";
    /** Description */
    private final String DESC = "This is the standard description when the Hero LOOKs, " +
                    "which is also called automatically when the Hero ENTERs.";
    /** Busy Description */
    private final String BUSY_DESC = "This is additional text, or alternate text, " +
                    "when there are many patrons in the Inn, and the Innkeeper cannot talk to the Hero.";
    /** Business opening hour for test Inn */
    private final int TEST_OPEN = 1000;
    /** Business closing hour for test Inn */
    private final int TEST_CLOSING = 1200;
    /** Business opening hour for test Inn */
    private final String TEST_MEROPEN = "10:00 AM";
    /** Business closing hour for default Inn */
    private final String TEST_MERCLOSING = "Noon";
    
    
    /** Business opening hour for default Inn */
    private final String DEF_OPEN = "6:00 AM";
    /** Business closing hour for default Inn */
    private final String DEF_CLOSING = "Midnight";

    /** Close down all the secondary registries needed for the Inn */
    @AfterClass
    public static void cleanUp() 
    {
        ((NPCRegistry) RegistryFactory.getInstance().getRegistry(RegKey.NPC)).closeRegistry();
    }
    

    /**
     * Creates the test Inn, but many tests in this class create their own different Inns
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        _inn = new Inn(NAME, INNKEEPER, HOVERTEXT, INTRO, DESC, BUSY_DESC);
        assertNotNull(_inn);
        _inn.setBusinessHours(TEST_OPEN, TEST_CLOSING);
        _mock = _inn.new MockInn();
        assertNotNull(_mock);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _inn = null;
        _mock = null;
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 *                  TEST METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Chronos.pdc.ItemRegistry
     * @Normal ensure that the test inn has correct data
     * @throws ApplicationException if unexpected ctor error occurs 
     */
    @Test
    public void testInn() throws ApplicationException 
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testInn()");
    
        // NORMAL Dump the test Inn created by setUp()
        dump(_inn);
        // Verify the name, innkeeper, and business hours
        assertEquals(NAME, _inn.getName());
        assertEquals(INNKEEPER, _inn.getMaster().getName());
        // Verify the standard intro and descrption; there is no busy description yet
        assertEquals(HOVERTEXT, INTRO, _inn.getExteriorDescription());
        assertEquals(DESC, _inn.getInteriorDescription());
        // Verify business hours in meridian time
        assertEquals(TEST_MEROPEN, _inn.getOpeningTime());
        assertEquals(TEST_MERCLOSING, _inn.getClosingTime());
    
        // NORMAL Verify busy hours
        _mock.setCurrentPatrons(6);
        MsgCtrl.msgln("\nBusy description");
        dump(_inn);
        // Verify the intro and busy descrption; there is no standard description now
        String[] s = _mock.getDescs();
        assertEquals(s[0], _inn.getExteriorDescription());
        assertEquals(s[2], _inn.getInteriorDescription());
    }    


    /** Chronos.pdc.Inn
     * @Error   trigger exception with bad business hours
     * @Error   trigger exception with building master not in NPC Registry
     * @throws ApplicationException if unexpected error occurs 
     */
    @Test
    public void testInnErrors() throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testInnErrors()");
    
        // Clear out old Inn from setUp()
        _inn = null;
        _mock = null;
        
        // ERROR  Building Master does not exist in Registry
        try {
            _inn = new Inn(NAME, "Unregistered Owner", HOVERTEXT, INTRO, DESC);
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\tExpected exception: " + ex.getMessage());
        }
        assertNull(_inn);

        // ERROR  Building hours are invalid
        _inn = new Inn(NAME, INNKEEPER, HOVERTEXT, INTRO, DESC);
        assertNotNull(_inn);
        assertFalse(_inn.setBusinessHours(1300, 1100));
        assertFalse(_inn.setBusinessHours(300, 2500));
    }    

    
    /** Chronos.pdc.Inn
     * @Normal ensure that the default Inn has correct data
     * @throws ApplicationException if unexpected ctor error occurs
     */
    @Test
    public void testDefaultInn()  throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testDefaultInn()");

        // Clear out old Inn from setUp()
        _inn = null;
        _mock = null;
        
        // NORMAL Create the default Inn using the default Constructor
        _inn = new Inn();
        assertNotNull(_inn);
        _mock = _inn.new MockInn();
        assertNotNull(_mock);
        dump(_inn);
        // Verify the name, innkeeper, and business hours
        assertEquals(_mock.getName(), _inn.getName());
//        assertEquals(_mock.getBuildingMaster().getName(), _inn.getMaster().getName());
        // Verify the standard intro and descrption; there is no busy description yet
        String[] s = _mock.getDescs();
        assertEquals(s[0], _inn.getExteriorDescription());
        assertEquals(s[1], _inn.getInteriorDescription());
        // Verify business hours in meridian time
        assertEquals(DEF_OPEN, _inn.getOpeningTime());
        assertEquals(DEF_CLOSING, _inn.getClosingTime());

        // NORMAL Verify busy hours
        _mock.setCurrentPatrons(6);
        MsgCtrl.msgln("\nBusy description");
        dump(_inn);
        // Verify the standard intro and busy descrption; there is no standard description now
        s = _mock.getDescs();
        assertEquals(s[0], _inn.getExteriorDescription());
        assertEquals(s[2], _inn.getInteriorDescription());
    }    


    /** Chronos.pdc.Inn
     * @Normal check that the proper descriptions are displayed, depending on the current
     * number of patrons (busy o rnot busy description).
     */
    @Test
    public void testEnter()
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testEnter()");
        
        // Non-busy description
        _mock.setCurrentPatrons(1);
        String testDesc = _inn.getInteriorDescription();
        MsgCtrl.msgln("\t" + testDesc);    
        assertEquals(DESC, testDesc);
        // Busy description
        _mock.setDesc(BUSY_DESC);
        _mock.setCurrentPatrons(6);
        testDesc = _inn.getInteriorDescription();
        MsgCtrl.msgln("\t" + testDesc);    
        assertEquals(BUSY_DESC, testDesc);
        // Again with 3 patrons for non busy desc
        _mock.setDesc(DESC);
        _mock.setCurrentPatrons(3);
        testDesc = _inn.getInteriorDescription();
        MsgCtrl.msgln("\t" + testDesc);    
        assertEquals(DESC, testDesc);
    }

    
    /** Chronos.pdc.Inn
     * @Normal check that the two inns are equal if their name and innkeepers are the same
     * @throws ApplicationException for unexpected errors
     */
    @Test
    public void testEquals() throws ApplicationException
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\t testEquals()");
        
        // Normal name and master
        Inn secondInn = new Inn(NAME, INNKEEPER, HOVERTEXT, INTRO, DESC);
        assertTrue(secondInn.equals(_inn));
        
        // ERROR different name, same master
        secondInn = null;
        secondInn = new Inn("Slippery Babboon", INNKEEPER, HOVERTEXT, INTRO, DESC);
        assertFalse(secondInn.equals(_inn));
        
        // ERROR different master, same name
        secondInn = null;
        secondInn = new Inn(NAME, "Aragon", HOVERTEXT, INTRO, DESC);
        assertFalse(secondInn.equals(_inn));
        
        // NULL parm should return false
        assertFalse(secondInn.equals(null));        
    }
    
    
    /** Tests that are not implemented either because tests are not needed, or they haven't
     * been determined to be needed yet
     *  @Not_Needed getKey()                            // getter
     *  @Not_Needed getBusinessHours()          // getter
     *  @Not_Needed getOpeningTime()            // wrapper
     *  @Not_Needed getClosingTime()             // setter
     */
    public void NotNeeded() { }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 					PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Dump the contents of the Inn
     * @param inn   the inn to display 
     */
    private void dump(Inn myInn)
    {
        MsgCtrl.msg("\t Created: \t" + myInn.getName());
        MsgCtrl.msgln("\t owned by " + myInn.getMaster().getName());
        int[] hours = myInn.getBusinessHours();
        int oTime = hours[0];
        int cTime = hours[1];
        String opening = myInn.getMeridianTime(oTime);
        String closing = myInn.getMeridianTime(cTime);
        MsgCtrl.msgln("\t Open from " + opening + " to " + closing);
        MsgCtrl.msgln("\tENTER: \t" + myInn.getExteriorDescription());
        MsgCtrl.msgln("\tLOOK:\t" + myInn.getInteriorDescription());    
        MsgCtrl.msgln("\tBUSY:\t" + myInn.getInteriorDescription());    
    }    
    
    
    // TODO Move formatting utlities to a utility class
//    /** Display a multiline message with a given characters width.
//     * Newlines are placed at word boundaries such that the width is not exceeded.
//     * Produces a string array with each element being one properly-restricted line of the original 
//     * message, using embedded newlines so that the text is left-aligned, ragged-right. 
//     * 
//     * @param msg    message to display
//     * @param width   limiting width for display
//     * @return the string array, one line per array element 
//     */
//    private String[] formattedMsg(String msg, int width)
//    {
//        // Define white space character
//        Character SPACE_CHAR = ' '; 
//        String[] sarray = null; 
//        // Ensure that end of string is longer than width 
//        if (msg.length() <= width) {
//            sarray = new String[1];
//            sarray[0] = msg;
////            return sarray;
//        }
//        return sarray;
//    }
//    
//        // Estimate the number of lines for the string buffer
//        int estLen = msg.length() % width;
//        // Create a Stringbuilder to accommodate estLen number of newline characters
//        String[] sa = new String[estLen+1];
//        // Find first white space char before the limit
//        int index = 0; 
//        for (int k=width; k < width; k--) {
//            if (sb.charAt(k) == SPACE_CHAR) {
//                sb.insert(k, Chronos.NEWLINE);
//                break;
//            }
//        }
//    }

    
//    /** Helper method to replace the last white space character with a newline character within
//     * the designated limit
//     *  
//     * @param msg         the string to insert the newline
//     * @param width  the limit to crop the string; the remnant is not returned
//     * @return the newly cropped string  
//     */
//    private String replaceSpace(String msg, int width)
//    {
//        // Ensure that no msg needs to be cropped
//        if (msg.length() <= width) {
//            return msg;
//        }
//        // Ensure that no newlines are already in the msg
//        if (msg.contains(Chronos.NEWLINE)) {
//            return msg;
//        }
//        
//        Character SPACE =  ' ';
//        int pos = msg.lastIndexOf(SPACE, width);
//        StringBuilder sb = new StringBuilder(width+1);
//        // Copy the substring found into the retaining buffer without the Space char
//        sb.append(msg.substring(0, pos-1));
//        // Add the newline
//        sb.append(Chronos.NEWLINE);
//        // Conver the buffer back to a string
//        String result = new String(sb); 
//        return result;
//    }
    
    
}       // end of TestInn class
