package test.pdc.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import chronos.pdc.command.Command;
import chronos.pdc.command.NullCommand;
import pdc.command.CommandFactory;
import pdc.command.CommandInput;

public class TestCommandFactory
{
    private CommandFactory _commandFactory;
    private FakeBuildingDisplayCiv _fakeBdCiv;
    private FakeMainframeCiv _fakeMfCiv;
    private static final String[] commandNames = { "APPROACH", "ENTER", "LEAVE", "EXIT", "RETURN", "QUIT" };

    @Before
    public void setup()
    {        
        _fakeBdCiv = new FakeBuildingDisplayCiv();
        _fakeMfCiv = new FakeMainframeCiv();
        _commandFactory = new CommandFactory(_fakeMfCiv, _fakeBdCiv);
        _commandFactory.initMap();
    }

    @Test
    public void SomeCommandsAreFound()
    {
        Random r = new Random(System.currentTimeMillis());
        int position = r.nextInt(commandNames.length);
        CommandInput ci = new CommandInput(commandNames[position], new ArrayList<String>());
        Command c = _commandFactory.createCommand(ci);
        assertFalse(new NullCommand().getName() == c.getName());
    }
    
    @Test
    public void FactoryKnowsWhichCommandCanBeCreated()
    {
        for (String name : commandNames) {
            CommandInput ci = new CommandInput(name, null);
            assertTrue(_commandFactory.canCreateCommand(ci));
        }
    }
    
    @Test
    public void MadeUpCommandCannotBeCreated()
    {
        CommandInput ci = new CommandInput("Fake", null);
        assertFalse(_commandFactory.canCreateCommand(ci));
    }
    
    @Test
    public void MadeUpCommandIsNotFound()
    {
        CommandInput ci = new CommandInput("Fake", new ArrayList<String>());
        Command c = _commandFactory.createCommand(ci);
        assertEquals(new NullCommand().getName(), c.getName());   
    }
    
    @Test
    public void FakeCommandIsNotInitialized()
    {
        CommandInput ci = new CommandInput("Fake", new ArrayList<String>());
        NullCommand c = (NullCommand) _commandFactory.createCommand(ci);
        assertFalse(c.isInitialized());   
    }
    
    @Test
    public void RealCommandIsInitialized()
    {
        CommandInput ci = new CommandInput("RETURN", new ArrayList<String>());
        Command c = _commandFactory.createCommand(ci);
        assertTrue(c.isInitialized());   
    }
}
