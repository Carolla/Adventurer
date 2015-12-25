package test.pdc.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import chronos.pdc.command.Command;
import chronos.pdc.command.NullCommand;
import civ.CommandParser;
import pdc.command.CmdReturn;
import pdc.command.CommandFactory;
import pdc.command.CommandInput;

public class TestCommandParser
{

    private CommandParser _cp;
    private FakeScheduler _fakeSkedder;

    @Before
    public void setup()
    {
        _fakeSkedder = new FakeScheduler();
        _cp = new CommandParser(_fakeSkedder, new FakeCommandFactory());
    }
    
    @Test
    public void FirstStringIsCommandTokenInCommandInput()
    {
        String firstWord = "Test";
        String testCmd = firstWord + " command";
        CommandInput ci = _cp.createCommandInput(testCmd);
        assertTrue(firstWord.equalsIgnoreCase(ci.commandToken));
    }
    
    @Test
    public void FirstStringIsInUpperCase()
    {
        String testCmd = "Test";
        CommandInput ci = _cp.createCommandInput(testCmd);
        assertEquals(testCmd.toUpperCase(), ci.commandToken);        
    }
    
    @Test
    public void SecondStringIsInListOfParams()
    {
        String secondWord = "command";
        String testCmd = "Test " + secondWord;
        CommandInput ci = _cp.createCommandInput(testCmd);
        assertTrue(ci.parameters.contains(secondWord));
    }
    
    @Test
    public void AllRemainingStringsAreInListOfParams()
    {
        Random rand = new Random(System.currentTimeMillis());
        int numberOfWords = rand.nextInt(10);
        String testCmd = "Test ";
        for (int i = 0; i < numberOfWords; i++) {
            testCmd += " word" + i;
        }
        CommandInput ci = _cp.createCommandInput(testCmd);
        for (int i = 0; i < numberOfWords; i++) {
            assertTrue(ci.parameters.contains("word" + i));
        }
    }
    
    @Test
    public void CommandIsScheduledWhenFound()
    {
        CommandFactory realCommandFactory = new CommandFactory(null, null);
        realCommandFactory.initMap();
        _cp = new CommandParser(_fakeSkedder, realCommandFactory);
        _cp.receiveCommand("Return");
        CmdReturn expectedCommand = new CmdReturn(new FakeBuildingDisplayCiv());
        assertEquals(expectedCommand.getName(), _fakeSkedder.command.getName());
    }
    
    @Test
    public void CommandIsScheduledWhenNotFound()
    {
        _cp.receiveCommand("Don't do anything");
        assertEquals(new NullCommand().getName(), _fakeSkedder.command.getName());
    }

    public class FakeCommandFactory extends CommandFactory
    {
        public FakeCommandFactory()
        {
            super(null, null);
            initMap();
        }

        @Override
        public Command createCommand(CommandInput cmdInput)
        {
            Command command = new NullCommand();
            command.init(null);
            return command;
        }
    }

}
