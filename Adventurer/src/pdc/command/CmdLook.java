
package pdc.command;

import java.util.List;

import chronos.pdc.Command.Command;
import civ.BuildingDisplayCiv;

public class CmdLook extends Command
{
  // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
  /** The name of the command, mostly for error messages */
  static private final String CMD_NAME = "LOOK";
  /** The description of what the command does, used in the {@code help()} method. */
  static private final String CMD_DESCRIPTION = "Look around the current Building.";
  /** Format for this command; null building defaults to current building */
  static private final String CMDFMT = null;
  /** This command starts immediately, requiring no delay. */
  static private final int DELAY = 0;
  /** This command takes 10 seconds on the game clock. */
  static private final int DURATION = 10;

  private final BuildingDisplayCiv _bdciv;
  private String _target = "";

  public CmdLook(BuildingDisplayCiv bdciv)
  {
    super(CMD_NAME, DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
    _bdciv = bdciv;
  }

  @Override
  public boolean init(List<String> args)
  {
    for (String s : args) {
      _target += s + " ";
    }
    _isInitialized = true;
    return _isInitialized;
  }

  @Override
  public boolean exec()
  {
    if (_target.isEmpty()) {
      _bdciv.displayBuildingInterior();
    } else {
      _bdciv.inspectTarget(_target);
    }
    return true;
  }

}
