
package test.pdc;

import java.util.ArrayList;

import chronos.pdc.NPC;

public class FakeNPC extends NPC
{
  public FakeNPC(String name)
  {
    super(name, "Note", 0, "farDesc", "nearDesc", new ArrayList<String>(), new ArrayList<String>());
  }

  public FakeNPC(String name, String description)
  {
    super(name, "Note", 0, "farDesc", description, new ArrayList<String>(), new ArrayList<String>());
  }
}
