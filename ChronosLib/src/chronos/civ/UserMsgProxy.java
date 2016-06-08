package chronos.civ;

public class UserMsgProxy implements UserMsgInterface
{
  
  @Override
  public String displayText(String msg)
  {
    return msg;
  }

  @Override
  public String displayErrorText(String errMsg)
  {
    return errMsg;
  }

}
