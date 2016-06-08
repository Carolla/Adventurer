
package pdc.command;

import java.util.ArrayList;
import java.util.List;

/*
 * TODO Probably should not be an object, but a method in the CommandParser, because there are no
 * public services, only data
 */
public class CommandInput
{
  @Override
  public String toString()
  {
    return "CommandInput [" + commandToken + " " + parameters + "]";
  }

  public final String commandToken;
  public final List<String> parameters;

  public CommandInput(String cmdToken, List<String> params)
  {
    commandToken = cmdToken;
    parameters = params != null ? params : new ArrayList<String>();
  }
}
