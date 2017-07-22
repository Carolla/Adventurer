
package chronos.test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.NPC;
import chronos.pdc.NPC.MockNPC;
import mylib.MsgCtrl;

public class TestNPC
{
  private NPC _npc;
  private MockNPC _mock;
  private List<String> _rumors =
      new ArrayList<String>(Arrays.asList("rumor 1", "rumor 2", "rumor 3"));
  private List<String> _retorts =
      new ArrayList<String>(Arrays.asList("retort 1", "retort 2", "retort 3"));
  private final int MAX_AFFINITY = 5;
  private final int MIN_AFFINITY = -5;
  private final int MIN_MESSAGES = 3;


  @Before
  public void setUp()
  {
    _npc = new NPC("Name", 1, "farDesc", "nearDesc", _rumors, _retorts);
    assertNotNull(_npc);
    _mock = _npc.new MockNPC();
    assertNotNull(_mock);

  }

  @After
  public void tearDown()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _mock = null;
    _npc = null;
  }


  // =============================================================================
  // BEGIN TESTS
  // =============================================================================

  /**
   * @Normal.Test {@code NPC(String name, int affinity, String farDesc, String nearDesc, 
   *  List<String> replies, List<String> retorts)} -- echo check
   */
  @Test
  public void testCtor()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Verify public setup info
    assertEquals("Name", _npc.getName());
    assertEquals("Name", _npc.getKey());
    assertEquals("Name", _npc.toString());
    assertEquals("farDesc", _npc.getFarDescription());
    assertEquals("nearDesc", _npc.getNearDescription());
    assertNotNull(_npc.talk(10)); // any number should do
    assertFalse(_npc.isPeacekeeper());

    int aff = _mock.getAffinity();
    assertTrue(aff <= MAX_AFFINITY && aff >= MIN_AFFINITY);
    // assertTrue(_mock.getRetorts().size() >= MIN_MESSAGES);
    // assertTrue(_mock.getRumors().size() >= MIN_MESSAGES);
  }


  /**
   * @Null.Test {@code NPC(String name, int affinity, String farDesc, String nearDesc, 
   *  List<String> replies, List<String> retorts)} -- null for various parms
   */
  @Test
  public void testCtor_NullParms()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    try {
      _npc = new NPC(null, 1, "farDesc", "nearDesc", _rumors, _retorts);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (NullPointerException npx) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + "\tName is null");
    }
    try {
      _npc = new NPC("Name", 1, null, "nearDesc", _rumors, _retorts);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (NullPointerException npx) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + "\tFar Dscription is null");
    }
    try {
      _npc = new NPC("Name", 1, "farDesc", null, _rumors, _retorts);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (NullPointerException npx) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + "\tNear Description is null");
    }
    try {
      _npc = new NPC("Name", 1, "farDesc", "nearDesc", null, _retorts);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (NullPointerException npx) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + "\tRumors list is null");
    }
    try {
      _npc = new NPC("Name", 1, "farDesc", "nearDesc", _rumors, null);
      fail(MsgCtrl.EXCEPTION_NOT_THROWN);
    } catch (NullPointerException npx) {
      MsgCtrl.msgln(MsgCtrl.EXP_EXCEPTION + "\tRetort list is null");
    }
  }


  /**
   * @Error.Test {@code NPC(String name, int affinity, String farDesc, String nearDesc, 
   *    List<String> replies, List<String> retorts)} -- out of range affinity set right
   */
  @Test
  public void testCtor_OutOfRangeAffinity()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Create new NPC and its mock
    _npc = new NPC("Name", -9, "farDesc", "nearDesc", _rumors, _retorts);
    _mock = _npc.new MockNPC();
    assertEquals(MIN_AFFINITY, _mock.getAffinity());

    // Create new NPC and its mock
    _npc = new NPC("Name", 9, "farDesc", "nearDesc", _rumors, _retorts);
    _mock = _npc.new MockNPC();
    assertEquals(MAX_AFFINITY, _mock.getAffinity());
  }


  /**
   * @Error.Test {@code NPC(String name, int affinity, String farDesc, String nearDesc, 
   *    List<String> replies, List<String> retorts)} -- Message range and content test
   */
  @Test
  public void testCtor_Messages()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // New NPC should have three messages of each type
    List<String> goodMsgs = _mock.getRumors();
    assertEquals(MIN_MESSAGES, goodMsgs.size());
    for (int k = 0; k < _rumors.size(); k++) {
      assertEquals(_rumors.get(k), goodMsgs.get(k));
    }
    // Same for retorts
    List<String> badMsgs = _mock.getRetorts();
    assertEquals(MIN_MESSAGES, badMsgs.size());
    for (int k = 0; k < _retorts.size(); k++) {
      assertEquals(_retorts.get(k), badMsgs.get(k));
    }
  }


  /**
   * @Normal.Test boolean equals(Object obj) -- NPC's are equal if name, peacekeeper status, and
   *              descriptions are the same
   */
  @Test
  public void testEquals_defaultObjectsEqual()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Name, peacekeeping, and descriptions are same; messages are different
    NPC npc2 =
        new NPC("Name", 1, "farDesc", "nearDesc", new ArrayList<String>(), new ArrayList<String>());
    assertEquals(_npc, npc2);
    assertEquals(_npc.toString(), npc2.toString());
    assertEquals(_npc.hashCode(), npc2.hashCode());
  }


  /**
   * @Normal.Test boolean equals(Object obj) -- NPC's are unequal if name, peacekeeper status, and
   *              descriptions are different
   */
  @Test
  public void testEquals_differentObjectsNotEqual()
  {
    // Different names
    NPC npc2 = new NPC("Name2", 1, "farDesc", "nearDesc", new ArrayList<String>(),
        new ArrayList<String>());
    assertFalse(_npc.equals(npc2));
    assertFalse(npc2.equals(_npc));
    assertFalse(_npc.hashCode() == npc2.hashCode());
    assertFalse(_npc.toString().equals(npc2.toString()));

    // Different peacekeeper status
    npc2 =
        new NPC("Name", 1, "farDesc", "nearDesc", new ArrayList<String>(), new ArrayList<String>());
    npc2.setPeacekeeper(true);
    assertFalse(_npc.equals(npc2));
    assertFalse(npc2.equals(_npc));
    assertFalse(_npc.hashCode() == npc2.hashCode());

    // Different farDesc
    npc2 = new NPC("Name", 1, "far", "nearDesc", new ArrayList<String>(), new ArrayList<String>());
    npc2.setPeacekeeper(true);
    assertFalse(_npc.equals(npc2));
    assertFalse(npc2.equals(_npc));
    assertFalse(_npc.hashCode() == npc2.hashCode());

    // Different nearDesc
    npc2 = new NPC("Name", 1, "farDesc", "near", new ArrayList<String>(), new ArrayList<String>());
    npc2.setPeacekeeper(true);
    assertFalse(_npc.equals(npc2));
    assertFalse(npc2.equals(_npc));
    assertFalse(_npc.hashCode() == npc2.hashCode());
  }

  /** @Not.Needed String getFarDescription() -- getter */
  public void testGetFarDescription()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }

  /** @Not.Needed String getNearDescription() -- getter */
  public void testGetNearDescription()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }

  /** @Not.Needed String getKey() -- getter */
  public void testGetKey()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }

  /** @Normal.Test int hashCode() -- primitive but tested against self */
  @Test
  public void testHashCode_hashEqualsSelf()
  {
    assertEquals(_npc, _npc);
    assertEquals(_npc.toString(), _npc.toString());
    assertEquals(_npc.hashCode(), _npc.hashCode());
  }

  /** @Not.Needed boolean isPeacekeeper() -- getter */
  public void testIsPeacekeeper()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }

  /** @Not.Needed String toString() -- getter */
  public void testToString()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /** @Not.Needed void setPeacekeeper(boolean peaceFlag) -- setter */
  public void testSetPeacekeeper()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.SETTER);
  }

  /**
   * @Normal.Test String talk() -- get a happy response for a high affinity until out of messages
   */
  @Test
  public void testTalk_happyNpcTalks()
  {
    List<String> replies = new ArrayList<String>();
    // replies.add("Hi");
    // replies.add("What?");
    // replies.add("Okay");
    NPC npc = new NPC("Name", 5, "far", "near", _rumors, _retorts);
    int chr = 10;
    // All 3 response are rumors
    for (int k = 0; k < MIN_MESSAGES; k++) {
      assertTrue(npc.talk(chr).equals(_rumors.get(k)));
    }
    assertEquals(NPC.NO_MORE_RESPONSES, npc.talk(chr));
  }


  /**
   * @Normal.Test String talk() -- get a bad response for a high affinity until out of messages
   */
  @Test
  public void testTalk_madNpcRetorts()
  {
    NPC npc = new NPC("Name", -5, "far", "near", _rumors, _retorts);
    int chr = 10;
    // All 3 response are retorts
    for (int k = 0; k < MIN_MESSAGES; k++) {
      assertTrue(npc.talk(chr).equals(_retorts.get(k)));
    }
    assertEquals(NPC.NO_MORE_RESPONSES, npc.talk(chr));
  }


  /**
   * @Normal.Test String talk() -- alternate between responses
   */
  @Test
  public void testTalk_AlternatingResponses()
  {
    NPC npc = new NPC("Name", 0, "far", "near", _rumors, _retorts);
    // If charisma + affinity >= 10, then rumors are returned
    assertTrue(npc.talk(10).equals(_rumors.get(0)));
    assertTrue(npc.talk(9).equals(_retorts.get(0)));
    assertTrue(npc.talk(11).equals(_rumors.get(1)));
    assertTrue(npc.talk(8).equals(_retorts.get(1)));
    assertTrue(npc.talk(12).equals(_rumors.get(2)));
    assertTrue(npc.talk(7).equals(_retorts.get(2)));
    assertEquals(NPC.NO_MORE_RESPONSES, npc.talk(10));
  }
}
