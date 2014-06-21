
package chronos.pdc.buildings;

import chronos.pdc.NPC;
import chronos.pdc.NullNPC;

public class NullBuilding extends Building
{
    private static final String notNull = "";
    public NullBuilding()
    {
        super(notNull, notNull, notNull, notNull, notNull, notNull, notNull);
    }
    
    @Override
    protected NPC findBuildingMaster(String masterName)
    {
        return new NullNPC();
    }

    @Override
    public String getKey()
    {
        return notNull;
    }

}
