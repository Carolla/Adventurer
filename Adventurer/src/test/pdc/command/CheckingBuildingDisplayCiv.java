package test.pdc.command;

import chronos.pdc.buildings.Building;
import civ.BuildingDisplayCiv;



public class CheckingBuildingDisplayCiv extends BuildingDisplayCiv
{
    public CheckingBuildingDisplayCiv()
    {
        super(null, null);
    }

    private String _currentBuilding;

    @Override
    public void setCurrentBuilding(Building b)
    {
        _currentBuilding = b.getName();
    }
    
    @Override
    public boolean canEnter(String bldgName)
    {
        return _currentBuilding.equals(bldgName);
    }
    
    @Override
    public boolean canApproach(String bldgName)
    {
        return _currentBuilding.isEmpty();
    }
}
