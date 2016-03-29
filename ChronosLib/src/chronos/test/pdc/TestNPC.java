/**
 * TestPatron.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.NPC;
import chronos.pdc.NPC.MockPatron;

/**
 * Ensure that the patron class works correctly
 *
 * @author Alan Cline
 * @version Jan 21, 2013 // original
 *          <DD>Mar 29 2016 // reviewed and updated for overall QA <br>
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
    _npc = new NPC("Falsoon", "far description", "near description", 0, true, "Note");
    assertNotNull(_npc);
    _mock = _npc.new MockPatron();
    assertNotNull(_mock);
    // Initialize msg lists with expected values
    for (int k = 0; k < 3; k++) {
      _rumors.add(_testMsgs[k]);
    }
    for (int k = 3; k < 6; k++) {
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


  /**
   * @ERROR.TEST NPC(String name, String farDesc, String nearDesc, int affinity, boolean peaceFlag,
   *             String note) throws NullPointerException, IllegalArgumentException
   */
  @Test
  public void testPatronErr()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.msgln(this, "\n testPatron()");

    // Null note should work because parm is optional
    _npc = new NPC("Falsoon", "far description", "near description", 0, true, null);
    assertNotNull(_npc);
    _npc = null;

    // Null name
    try {
      _npc = new NPC(" ", "far description", "near description", 0, true, "Note");
    } catch (NullPointerException ex) {
      MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
      assertNull(_npc);
    }
    // Empty name
    try {
      _npc = new NPC(" ", "far description", "near description", 0, true, "Note");
    } catch (NullPointerException ex) {
      MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
    }
    // Null far description
    try {
      _npc = new NPC("Falsoon", null, "near description", 0, true, "Note");
    } catch (NullPointerException ex) {
      MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
    }
    // Null near description
    try {
      _npc = new NPC("Falsoon", "far description", null, 0, true, "Note");
    } catch (NullPointerException ex) {
      MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
    }
    // Empty near description
    try {
      _npc = new NPC("Falsoon", "far description", "  ", 0, true, "Note");
    } catch (NullPointerException ex) {
      MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
    }
    // Boundary: affinity in/out of negative range
    _npc = new NPC("Falsoon", "far description", "near description", -5, true, "Note");
    assertNotNull(_npc);
    _npc = null;
    try {
      _npc = new NPC("Falsoon", "far description", "near description", -6, true, "Note");
    } catch (IllegalArgumentException ex) {
      MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
    }
    // Boundary: affinity in/out of positive
    _npc = new NPC("Falsoon", "far description", "near description", 5, true, "Note");
    assertNotNull(_npc);
    _npc = null;
    try {
      _npc = new NPC("Falsoon", "far description", "near description", 6, true, "Note");
    } catch (IllegalArgumentException ex) {
      MsgCtrl.msgln("\t Expected exception: " + ex.getMessage());
    }
  }


  /**
   * Set messages into the Patron
   * 
   * @Normal both rumors and retorts are stored
   */
  @Test
  public void testSetMessages()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.msgln(this, "\n testSetMessages()");

    // Initialize list with expected values
    _rumors.clear();
    for (int k = 0; k < 3; k++) {
      _rumors.add(_testMsgs[k]);
    }
    _retorts.clear();
    for (int k = 3; k < 6; k++) {
      _retorts.add(_testMsgs[k]);
    }

    // Create a patron with messages
    _npc.setMessages(_rumors, _retorts);
    assertEquals(3, dumpMsgs(_mock.getRumors()));
    assertEquals(3, dumpMsgs(_mock.getRetorts()));
  }


  /**
   * Set messages into the Patron
   * 
   * @Error message list is empty or null
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


  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++
   */

  /**
   * Display an arraylist of messages
   * 
   * @return the number of messages displayed
   */
  private int dumpMsgs(ArrayList<String> msgList)
  {
    for (String s : msgList) {
      MsgCtrl.msgln("\t\t" + s);
    }
    return msgList.size();
  }



} // end of TestPatron class
