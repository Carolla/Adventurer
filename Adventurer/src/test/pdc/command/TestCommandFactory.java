
package test.pdc.command;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pdc.command.CommandFactory;
import chronos.pdc.command.Command;

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
    _commandFactory = new CommandFactory(_fakeBdCiv, _fakeMfCiv);
    _commandFactory.initMap();
  }

  @Test
  public void SomeCommandsAreFound()
  {
    for (int i = 0; i < commandNames.length; i++) {
      String cName = commandNames[i];
      Command c = _commandFactory.createCommand(cName);
      assertNotNull("Tried to create command " + cName + " but failed", c);
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
    // CommandInput ci = new CommandInput("RETURN", new ArrayList<String>());
    String cName = "RETURN";
    Command c = _commandFactory.createCommand(cName);
    List<String> argsForCmd = new ArrayList<String>();
    assertTrue(c.init(argsForCmd));
  }
}
