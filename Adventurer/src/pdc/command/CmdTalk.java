
package pdc.command;

import java.util.List;

import chronos.pdc.command.Command;
import civ.BuildingDisplayCiv;

public class CmdTalk extends Command
{
  static private final String CMD_NAME = "TALK";
  static private final String CMD_DESCRIPTION = "Talk to a person in the current building.";
  static private final String CMDFMT = null;
  static private final int DELAY = 0;
  static private final int DURATION = 7;

  private final BuildingDisplayCiv _bdciv;
  private String _target = "";

  public CmdTalk(BuildingDisplayCiv bdciv)
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
    _target = _target.replace("to",  "").trim();
    
    return _bdciv.canTalkTo(_target);
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
