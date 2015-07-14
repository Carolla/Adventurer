package test.pdc.command;

import chronos.pdc.buildings.Building;
import civ.BuildingDisplayCiv;

public class FakeBuildingDisplayCiv extends BuildingDisplayCiv
{
    private String _currentBuilding = "";
    public boolean _canApproach = true;
    public boolean _canEnter = true;
    
    @Override
    public void setCurrentBuilding(Building b)
    {
        _currentBuilding = b.getName();
    }
    
    @Override
    public String getCurrentBuilding()
    {
        return _currentBuilding;
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
