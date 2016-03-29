
package test.pdc.command;

import java.util.ArrayList;
import java.util.List;

import civ.MainframeCiv;

public class FakeMainframeCiv extends MainframeCiv
{
  List<String> _errors = new ArrayList<String>();
  List<String> _text = new ArrayList<String>();

  public FakeMainframeCiv()
  {
    super(null);
  }
  
  protected void doConstructorWork()
  {
    // None to make :-)
  }
  
  @Override
  public void displayErrorText(String msg)
  {
    _errors.add(msg);
  }

  @Override
  public void displayText(String msg)
  {
    _text.add(msg);
  }
  
  @Override
  public void displayImage(String title, String imageName)
  {
	  //Do nothing
  }
  
  @Override
  public void quit()
  {
    // Don't exit the program!
  }
}
