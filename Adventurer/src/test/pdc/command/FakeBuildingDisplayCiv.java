
package test.pdc.command;

import chronos.pdc.buildings.Building;
import civ.BuildingDisplayCiv;

public class FakeBuildingDisplayCiv extends BuildingDisplayCiv
{
  public FakeBuildingDisplayCiv()
  {
    super(new FakeMainframeCiv(), null, null);
  }

  private String _currentBuildingName = "";
  public boolean _canApproach = true;
  public boolean _canEnter = true;
  public String _displayedBldg = "";
  public String _displayedImg = "";
  public String _displayedText = "";

  @Override
  public void enterBuilding(String name)
  {
    _currentBuildingName = name;
    _insideBldg = true;
  }

  public void setBuilding(Building bldg)
  {
    _currentBldg = bldg;
    _currentBuildingName = bldg.getName();
    enterBuilding(_currentBuildingName);
  }

  @Override
  public boolean approachBuilding(String bldg)
  {
    if (_currentBuildingName.isEmpty()) {
      _currentBuildingName = bldg;
      _insideBldg = false;
      return true;
    } else {
      _currentBuildingName = bldg;
      _insideBldg = false;
      return _currentBuildingName.equals(bldg);
    }
  }

  public void setBuildingName(String name)
  {
    _currentBuildingName = name;
  }

  @Override
  public void returnToTown()
  {
    super.returnToTown();
    _currentBuildingName = null;
  }

  @Override
  public String getCurrentBuilding()
  {
    return _currentBuildingName;
  }

  @Override
  public boolean canApproach(String bldgParm)
  {
    return _canApproach;
  }

  @Override
  public boolean canEnter(String bldgParm)
  {
    return _canEnter;
  }

  @Override
  public String inspectTarget(String target)
  {
    String result = super.inspectTarget(target);
    _displayedText = result;
    return result;
  }


  @Override
  public void displayBuildingInterior()
  {
    super.displayBuildingInterior();
    _displayedText = ((FakeMainframeCiv) _mfCiv)._text.get(0);
  }

  @Override
  public boolean talkToTarget(String target)
  {
    if (super.talkToTarget(target)) {
      _displayedText = ((FakeMainframeCiv) _mfCiv)._text.get(0);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void openTown()
  {
    _insideBldg = false;
    _currentBldg = null;
    _currentBuildingName = "";
  }
}
