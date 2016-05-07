package pdc.command;

import java.util.ArrayList;
import java.util.List;

public class CommandInput
{
    public CommandInput(String cmdToken, List<String> params)
    {
        commandToken = cmdToken;
        parameters = params != null ? params : new ArrayList<String>();
    }

    @Override
    public String toString()
    {
        return "CommandInput [" + commandToken + " " + parameters + "]";
    }

    public final String commandToken;
    public final List<String> parameters;
}
