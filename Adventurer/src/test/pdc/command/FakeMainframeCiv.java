package test.pdc.command;

import test.integ.MainframeProxy;
import civ.MainframeCiv;

public class FakeMainframeCiv extends MainframeCiv
{

    public FakeMainframeCiv()
    {
        super(new MainframeProxy(), null);
    }
    
    @Override
    protected void doConstructorWork()
    {
        //Do nothing, and FAST!
    }

}