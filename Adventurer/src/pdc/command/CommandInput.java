package pdc.command;

import java.util.ArrayList;
import java.util.List;

public class CommandInput
{
    public final String commandToken;
    public final List<String> parameters;

    public CommandInput(String cmdToken, List<String> params)
    {
        commandToken = cmdToken;
        parameters = params != null ? params : new ArrayList<String>();
    }
}
