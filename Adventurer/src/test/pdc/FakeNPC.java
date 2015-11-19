package test.pdc;

import chronos.pdc.NPC;

public class FakeNPC extends NPC
{
  public FakeNPC(String name)
  {
    super(name, "", 0, false, "", "");
  }
  
  public FakeNPC(String name, String description)
  {
    super(name, "", 0, false, description, description);
  }
}
