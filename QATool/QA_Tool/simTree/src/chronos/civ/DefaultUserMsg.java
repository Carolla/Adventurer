package chronos.civ;

public class DefaultUserMsg implements UserMsg
{

  @Override
  public void displayText(String msg)
  {
    System.out.println(msg);
  }

  @Override
  public void displayErrorText(String errMsg)
  {
    System.err.println(errMsg);
  }

}
