/**
 * TestPatron.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package chronos.test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import mylib.ApplicationException;
import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.NPC;
import chronos.pdc.NPC.MockPatron;

/**
 *    Ensure that the patron class works correctly
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Jan 21, 2013   // original <DD>
 * </DL>
 */
public class TestNPC
{
    /** test target object */
    private NPC _npc = null;
    /** mock for target object */
    private MockPatron _mock = null;

    /** Msglists and messages */
    ArrayList<String> _rumors = new ArrayList<String>(3);
    ArrayList<String> _retorts = new ArrayList<String>(3);
    final String[] _testMsgs = {"rumor1", "rumor2", "rumor3", "retort1", "retort2", "retort3"};

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        _npc = new NPC("Falsoon", "testing", 5, true, "Far description", "near description");
        assertNotNull(_npc);
        _mock = _npc.new MockPatron();
        assertNotNull(_mock);
        // Initialize msg lists with expected values
        for (int k=0; k < 3; k++) {
            _rumors.add(_testMsgs[k]);
        }        
        for (int k=3; k < 6; k++) {
            _retorts.add(_testMsgs[k]);
        }        
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        _npc = null;
        _mock = null;
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
    }

    /** Patron constructor 
     * @Error  try to create a Patron with null parms, empty parms, and invalid affinity 
     */
    @Test
    public void testPatronErr() 
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\n testPatron()");

        // Ensure non-existent patron
        _npc = null;
        
        // Null name
        try {
            _npc = new NPC(null, null, 5, true, "Far description", "near description");
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
        }
        // Empty name
        try {
            _npc = new NPC("", null, 5, true, "Far description", "near description");
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
        }
        // Null far descrripton
        try {
            _npc = new NPC("Falsoon", null, 5, true, null, "near description");
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
        }
        // Empty far descrripton
        try {
            _npc = new NPC("Falsoon", null, 5, true, " ", "near description");
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
        }
        // Null near descrripton
        try {
            _npc = new NPC("Falsoon", null, 5, true, "far description", null);
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
        }
        // Empty near descrripton
        try {
            _npc = new NPC("Falsoon", null, 5, true, "far description", "        ");
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
        }
        // Error: infinity out of range
        try {
            _npc = new NPC("Falsoon", null, 26, true, "far description", "near description        ");
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
        }
        // Error: infinity out of range
        try {
            _npc = new NPC("Falsoon", null,  -26, true, "far description", "near description        ");
        } catch (ApplicationException ex) {
            MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
        }
        
        // Unexpected success
        if (_npc != null) {
            MsgCtrl.errMsgln("\t Expected exception: but it did not occur");
        }
        assertNull(_npc);
        
    }

    
    /** Set messages into the Patron
     * @Normal  both rumors and retorts are stored
     */
    @Test
    public void testSetMessages() 
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\n testSetMessages()");

        // Initialize list with expected values
        _rumors.clear();
        for (int k=0; k < 3; k++) {
            _rumors.add(_testMsgs[k]);
        }        
        _retorts.clear();
        for (int k=3; k < 6; k++) {
            _retorts.add(_testMsgs[k]);
        }        
        
        // Create a patron with messages
        _npc.setMessages(_rumors, _retorts);
        assertEquals(3, dumpMsgs(_mock.getRumors()));
        assertEquals(3, dumpMsgs(_mock.getRetorts()));
    }
    
    
    /** Set messages into the Patron
     * @Error   message list is empty or null
     */
    @Test
    public void testSetMessagesErr() 
    {
        MsgCtrl.auditMsgsOn(false);
        MsgCtrl.errorMsgsOn(false);
        MsgCtrl.msgln(this, "\n testSetMessagesErr()");
    
        // Create a patron with messages
        ArrayList<String> emptyList = new ArrayList<String>();
        // Error: empty rumors list
        assertFalse(_npc.setMessages(emptyList, _retorts));
        // Error: empty retorts list
        assertFalse(_npc.setMessages(_rumors, emptyList));

    }
        
        
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                  PRIVATE METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Display an arraylist of messages
     * @return the number of messages displayed
     */
    private int dumpMsgs(ArrayList<String> msgList)
    {
        for (String s : msgList) {
            MsgCtrl.msgln("\t\t" + s);
        }
        return msgList.size();
    }
    
    
    
}           // end of TestPatron class
