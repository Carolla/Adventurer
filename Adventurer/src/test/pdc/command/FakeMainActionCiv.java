package test.pdc.command;

import civ.MainActionCiv;
import hic.ChronosPanel;

public class FakeMainActionCiv extends MainActionCiv
{

    public FakeMainActionCiv()
    {
        super(null, null);
    }

    
    protected void setActivePanel()
    {
      //Don't use nulls :-)
    }
    
    
    protected ChronosPanel createActionPanel()
    {
      //Sit here
      return null;
    }
}
