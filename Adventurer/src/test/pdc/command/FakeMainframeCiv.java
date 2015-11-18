
package test.pdc.command;

import civ.MainframeCiv;

public class FakeMainframeCiv extends MainframeCiv
{
  
  @Override
  protected void constructMembers()
  {
    // None to make :-)
  }
  
  @Override
  public void quit()
  {
    // Don't exit the program!
  }
  
  @Override
  public void errorOut(String msg)
  {
    System.err.println(msg);
  }
}
