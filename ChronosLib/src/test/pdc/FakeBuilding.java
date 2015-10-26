package test.pdc;

import mylib.dmc.IRegistryElement;
import chronos.pdc.buildings.Building;

public class FakeBuilding extends Building
{
    protected FakeBuilding(String name, String masterName, String hoverText, String exterior,
            String interior, String extImagePath, String intImagePath)
    {
        super(name, masterName, hoverText, exterior, interior, extImagePath, intImagePath);
    }
    
    public FakeBuilding(String name)
    {
        super(name, "", "", "", "", "", "");
    }

    @Override
    public boolean equals(IRegistryElement target)
    {
        return false;
    }
}
