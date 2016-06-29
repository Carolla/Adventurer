
package test.pdc.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import pdc.command.CmdReturn;
import pdc.command.CommandFactory;
import chronos.test.pdc.command.FakeScheduler;
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
  public void CommandIsScheduledWhenFound()
  {
    CommandFactory realCommandFactory = new CommandFactory(null, new FakeMainframeCiv());
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
    assertNull(_fakeSkedder.command);
  }

  public class FakeCommandFactory extends CommandFactory
  {
    public FakeCommandFactory()
    {
      super(null, new FakeMainframeCiv());
      initMap();
    }

  }
}
