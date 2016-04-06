/**
 * TestPatron.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.test.pdc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.NPC;

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
    _npc = new NPC("Falsoon", "Note", 0, "far description", "near description", new ArrayList<String>(), new ArrayList<String>());
    assertNotNull(_npc);
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
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
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
} // end of TestPatron class
