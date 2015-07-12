package test.pdc.command;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import pdc.command.CmdLeave;
import pdc.command.Command;
import pdc.command.CommandFactory;
import pdc.command.CommandInput;
import pdc.command.NullCommand;

public class TestCommandFactory
{

    private CommandFactory _commandFactory;

    @Before
    public void setup()
    {
        _commandFactory = new CommandFactory();
    }
    
    @Test
    public void SomeCommandsAreFound()
    {
        CommandInput ci = new CommandInput("LEAVE", new ArrayList<String>());
        Command c = _commandFactory.createCommand(ci);
        assertEquals(new CmdLeave().getName(), c.getName());
    }
    
    @Test
    public void MadeUpCommandIsNotFound()
    {
        CommandInput ci = new CommandInput("Fake", new ArrayList<String>());
        Command c = _commandFactory.createCommand(ci);
        assertEquals(new NullCommand().getName(), c.getName());   
    }
    
    @Test
    public void CommandIsInitialized()
    {
        CommandInput ci = new CommandInput("Fake", new ArrayList<String>());
        NullCommand c = (NullCommand) _commandFactory.createCommand(ci);
        assertTrue(c.isInitialized());   
    }

}
