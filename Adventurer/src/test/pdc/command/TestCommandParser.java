package test.pdc.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import pdc.command.CmdExit;
import pdc.command.Command;
import pdc.command.CommandFactory;
import pdc.command.CommandInput;
import pdc.command.DeltaCmdList;
import pdc.command.NullCommand;
import pdc.command.Scheduler;
import civ.CommandParser;

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
    public void UserInputIsSetWhenCommandReceived()
    {
        String testCmd = "Test command";
        _cp.receiveCommand(testCmd);
        assertEquals(_cp.new MockCP().getInput(), testCmd);
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
        _cp = new CommandParser(_fakeSkedder, new CommandFactory(null, null));
        _cp.receiveCommand("Exit");
        assertEquals(new CmdExit().getName(), _fakeSkedder.command.getName());
    }
    
    @Test
    public void CommandIsScheduledWhenNotFound()
    {
        _cp.receiveCommand("Don't do anything");
        assertEquals(new NullCommand().getName(), _fakeSkedder.command.getName());
    }
    
    public class FakeScheduler extends Scheduler
    {
        public Command command;

        public FakeScheduler()
        {
            super(new DeltaCmdList());
        }
        
        @Override
        public void run() {}

        @Override
        public void sched(Command cmd)
        {
            command = cmd;
        }
    }

    public class FakeCommandFactory extends CommandFactory
    {
        public FakeCommandFactory()
        {
            super(null, null);
        }

        @Override
        public Command createCommand(CommandInput cmdInput)
        {
            return new NullCommand();
        }
    }

}
