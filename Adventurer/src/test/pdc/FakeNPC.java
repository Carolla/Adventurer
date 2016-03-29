package test.pdc;

import chronos.pdc.NPC;

public class FakeNPC extends NPC
{
  public FakeNPC(String name)
  {
    super(name, "farDesc", "nearDesc", 0, false, "Note");
  }
  
  public FakeNPC(String name, String description)
  {
    super(name, "farDesc", description, 0, false, "Note");
  }
}
