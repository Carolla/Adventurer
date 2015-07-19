package test.pdc.command;

import test.integ.MainframeProxy;
import civ.MainframeCiv;

public class FakeMainframeCiv extends MainframeCiv
{

    public FakeMainframeCiv()
    {
        super(new MainframeProxy(), null);
    }

}
