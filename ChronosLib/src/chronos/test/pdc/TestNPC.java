package chronos.test.pdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import chronos.pdc.NPC;

public class TestNPC
{
  NPC npc = new NPC("Name", 1, "far", "near", new ArrayList<String>(), new ArrayList<String>());
  
  @Test
  public void defaultObjectsEqual()
  {
    NPC npc2 = new NPC("Name", 1, "far", "near", new ArrayList<String>(), new ArrayList<String>());
    assertEquals(npc, npc2);
    assertEquals(npc.toString(), npc2.toString());
    assertEquals(npc.hashCode(), npc2.hashCode());
  }

  @Test
  public void differentObjectsNotEqual()
  {
    NPC npc2 = new NPC("Name2", 1, "far", "near", new ArrayList<String>(), new ArrayList<String>());
    assertFalse(npc.equals(npc2));
    assertFalse(npc2.equals(npc));
    assertFalse(npc.hashCode() == npc2.hashCode());
    assertFalse(npc.toString().equals(npc2.toString()));
  }
  
  @Test
  public void hashEqualsSelf()
  {
    assertEquals(npc, npc);
    assertEquals(npc.toString(), npc.toString());
    assertEquals(npc.hashCode(), npc.hashCode());
  }
  
  @Test
  public void happyNpcTalks()
  {
    List<String> replies = new ArrayList<String>();
    replies.add("Hi");
    replies.add("What?");
    replies.add("Okay");
    NPC npc = new NPC("Name", 5, "far", "near", replies, new ArrayList<String>());

    assertFalse(npc.talk().equals(NPC.NO_MORE_RESPONSES));
    assertFalse(npc.talk().equals(NPC.NO_MORE_RESPONSES));
    assertFalse(npc.talk().equals(NPC.NO_MORE_RESPONSES));
    assertEquals(NPC.NO_MORE_RESPONSES, npc.talk());
  }
  
  @Test
  public void madNpcRetorts()
  {
    List<String> retorts = new ArrayList<String>();
    retorts.add("Grr");
    retorts.add("What?!");
    retorts.add("...");
    NPC npc = new NPC("Name", -5, "far", "near", new ArrayList<String>(), retorts);

    assertFalse(npc.talk().equals(NPC.NO_MORE_RESPONSES));
    assertFalse(npc.talk().equals(NPC.NO_MORE_RESPONSES));
    assertFalse(npc.talk().equals(NPC.NO_MORE_RESPONSES));
    assertEquals(NPC.NO_MORE_RESPONSES, npc.talk());
  }
}
