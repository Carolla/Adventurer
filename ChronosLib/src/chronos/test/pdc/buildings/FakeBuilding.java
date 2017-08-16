package chronos.test.pdc.buildings;

import chronos.pdc.buildings.Building;

public class FakeBuilding extends Building
{
    public FakeBuilding(String name)
    {
        super(name, "Bork", 0, 2400, "", "", "", "", "");
    }

    public FakeBuilding(String name, String interiorDesc)
    {
      super(name, "Bork", 0, 2400, "", "", interiorDesc, "", "");
    }

    @Override
    public String getName()
    {
      return _name;
    }
}
