package test.pdc.command;

import hic.ChronosPanel;
import civ.MainActionCiv;

public class FakeMainActionCiv extends MainActionCiv
{

    public FakeMainActionCiv()
    {
        super(null, null);
    }

    @Override
    protected void setActivePanel()
    {
      //Don't use nulls :-)
    }
    
    @Override
    protected ChronosPanel createActionPanel()
    {
      //Sit here
      return null;
    }
}
