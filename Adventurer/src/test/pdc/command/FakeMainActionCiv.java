package test.pdc.command;

import civ.MainActionCiv;

public class FakeMainActionCiv extends MainActionCiv
{

    public FakeMainActionCiv()
    {
        super(null, null);
    }

    @Override
    protected void createMembers()
    {
      //Don't use nulls :-)
    }
}
