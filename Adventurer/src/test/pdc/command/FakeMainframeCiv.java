
package test.pdc.command;

import java.util.ArrayList;
import java.util.List;

import civ.MainframeCiv;
import hic.MainframeInterface;

public class FakeMainframeCiv extends MainframeCiv
{
  List<String> _errors = new ArrayList<String>();

  public FakeMainframeCiv()
  {
    super();
  }
  

//  public FakeMainframeCiv(MainframeInterface mf)
//  {
//    super(mf);
//  }

  
  protected void constructMembers()
  {
    // None to make :-)
  }
  
  @Override
  public void displayErrorText(String msg)
  {
    _errors.add(msg);
  }
  
  @Override
  public void quit()
  {
    // Don't exit the program!
  }
}
