package test.pdc;

import chronos.pdc.Adventure;

public class FakeAdventure extends Adventure
{
  public FakeAdventure()
  {
    super("","","","");
  }
  
  @Override
  public String getName()
  {
    return "";
  }
}
