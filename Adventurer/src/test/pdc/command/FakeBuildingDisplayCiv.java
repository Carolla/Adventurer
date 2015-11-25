package test.pdc.command;

import test.pdc.FakeAdventure;
import chronos.pdc.buildings.Building;
import civ.BuildingDisplayCiv;

public class FakeBuildingDisplayCiv extends BuildingDisplayCiv
{
    public FakeBuildingDisplayCiv()
    {
        super(new FakeMainframeCiv(), new FakeAdventure(), null);
    }

    private String _currentBuildingName = "";
    public boolean _canApproach = true;
    public boolean _canEnter = true;
    private boolean _isInside;
    public String _displayedBldg;
    public String _displayedImg;
    public String _displayedText;
    
    @Override
    public void enterBuilding(String name)
    {
        _currentBuildingName = name;
    }
    
    public void setBuilding(Building bldg)
    {
      _currentBldg = bldg;
    }
        
    @Override
    public boolean approachBuilding(String bldg)
    {
        if (_currentBuildingName.isEmpty()) {
            _currentBuildingName = bldg;
            return true;
        } else {
            _currentBuildingName = bldg;
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
        _currentBuildingName = null;
    }
    
    @Override
    public String getCurrentBuilding()
    {
        return _currentBuildingName;
    }
    
    void setInside(boolean inside)
    {
        _isInside = inside;
    }
    
    @Override
    public boolean isInside()
    {
        return _isInside;
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
}
