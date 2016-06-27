
package test.pdc.command;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import chronos.pdc.command.Command;
import chronos.pdc.command.NullCommand;
import pdc.command.CommandFactory;

public class TestCommandFactory
{
  private CommandFactory _commandFactory;
  private FakeBuildingDisplayCiv _fakeBdCiv;
  private FakeMainframeCiv _fakeMfCiv;
  private static final String[] commandNames =
      {"APPROACH", "ENTER", "LEAVE", "EXIT", "RETURN", "QUIT"};

  @Before
  public void setup()
  {
    _fakeBdCiv = new FakeBuildingDisplayCiv();
    _fakeMfCiv = new FakeMainframeCiv();
    _commandFactory = new CommandFactory(_fakeBdCiv);
    _commandFactory.initMap();
  }

    @Test
    public void SomeCommandsAreFound()
    {
        Random r = new Random(System.currentTimeMillis());
        int position = r.nextInt(commandNames.length);
        String cName = commandNames[position];
        Command c = _commandFactory.createCommand(cName);
        assertFalse(new NullCommand().getName() == c.getName());
    }
    
    @Test
    public void FactoryKnowsWhichCommandCanBeCreated()
    {
        for (String cName : commandNames) {
            assertTrue(_commandFactory.canCreateCommand(cName));
        }
    }
    
    @Test
    public void MadeUpCommandCreatesCommandThatIsNull()
    {
        String fakeName = "FakeCommand";
        Command cmd = _commandFactory.createCommand(fakeName);
        assertNull(cmd);
    }
    
    @Test
    public void RealCommandIsInitialized()
    {
//        CommandInput ci = new CommandInput("RETURN", new ArrayList<String>());
        String cName = "RETURN";
        Command c = _commandFactory.createCommand(cName);
        List<String> argsForCmd = new ArrayList<String>();
        argsForCmd.add("");
        assertTrue(c.init(argsForCmd));   
    }
  }

