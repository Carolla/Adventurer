
package test.pdc.command;

import java.util.ArrayList;
import java.util.List;

import civ.MainframeCiv;

public class FakeMainframeCiv extends MainframeCiv
{
  List<String> _errors = new ArrayList<String>();
  List<String> _text = new ArrayList<String>();
  private String _title;
  private String _image;

  public FakeMainframeCiv()
  {
    super();
  }

  @Override
  protected void init()
  {
    //Don't create GUI
  }
  
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
  public void displayText(String msg)
  {
    _text.add(msg);
  }
  
  @Override
  public void displayImage(String title, String imageName)
  {
    _image = imageName;
    _title = title;
  }
  
  @Override
  public void quit()
  {
    // Don't exit the program!
  }
}
