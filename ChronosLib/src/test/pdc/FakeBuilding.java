package test.pdc;

import mylib.dmc.IRegistryElement;
import chronos.pdc.buildings.Building;

public class FakeBuilding extends Building
{
    public FakeBuilding(String name)
    {
        super(name, "", "", "", "", "", "");
    }

    public FakeBuilding(String name, String interiorDesc)
    {
      super(name, "", "", "", interiorDesc, "", "");
    }

    @Override
    public boolean equals(IRegistryElement target)
    {
      return _name.equals(target.getKey());
    }

    @Override
    public String getName()
    {
      return _name;
    }
}
