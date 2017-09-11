package chronos.test.pdc.buildings;

import chronos.pdc.buildings.Building;

public class FakeBuilding extends Building
{
    public FakeBuilding(String name)
    {
        super(name, "Bork", 600, 2300, "", "", "", "", "");
    }

    public FakeBuilding(String name, String interiorDesc)
    {
      super(name, "Bork", 600, 2300, "", "", interiorDesc, "", "");
    }

    @Override
    public String getName()
    {
      return _name;
    }
}
