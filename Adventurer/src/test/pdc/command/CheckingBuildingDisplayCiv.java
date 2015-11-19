package test.pdc.command;

import civ.BuildingDisplayCiv;



public class CheckingBuildingDisplayCiv extends BuildingDisplayCiv
{
    public CheckingBuildingDisplayCiv()
    {
        super(null, null, null);
    }

    private String _currentBuilding;

    @Override
    public void enterBuilding(String name)
    {
        _currentBuilding = name;
    }
    
    @Override
    public void returnToTown()
    {
        _currentBuilding = null;
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
