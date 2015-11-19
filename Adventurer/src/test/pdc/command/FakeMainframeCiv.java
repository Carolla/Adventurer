
package test.pdc.command;

import java.util.ArrayList;
import java.util.List;

import civ.MainframeCiv;

public class FakeMainframeCiv extends MainframeCiv
{
  List<String> _errors = new ArrayList<String>();
  
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
}
