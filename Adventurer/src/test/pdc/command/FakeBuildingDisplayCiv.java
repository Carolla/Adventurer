package test.pdc.command;

import chronos.pdc.buildings.Building;
import civ.BuildingDisplayCiv;

public class FakeBuildingDisplayCiv extends BuildingDisplayCiv
{
    public FakeBuildingDisplayCiv()
    {
        super(null, null);
    }

    private String _currentBuilding = "";
    public boolean _canApproach = true;
    public boolean _canEnter = true;
    private boolean _isInside;
    
    @Override
    public void enterBuilding(String name)
    {
        _currentBuilding = name;
    }
    
    @Override
    public boolean approachBuilding(String bldg)
    {
        if (_currentBuilding.isEmpty()) {
            _currentBuilding = bldg;
            return true;
        } else {
            _currentBuilding = bldg;
            return _currentBuilding.equals(bldg);
        }
    }
    
    public void setCurrentBuilding(String name)
    {
        _currentBuilding = name;
    }
    
    @Override
    public void returnToTown(Building b)
    {
        _currentBuilding = b.getName();
    }
    
    @Override
    public String getCurrentBuilding()
    {
        return _currentBuilding;
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
    
}
