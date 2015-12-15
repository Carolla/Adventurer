package test.pdc.command;

import chronos.civ.DefaultUserMsg;
import chronos.pdc.Command.Command;
import chronos.pdc.Command.Scheduler;


public class FakeScheduler extends Scheduler
{
    public Command command;

    public FakeScheduler()
    {
        super(new DefaultUserMsg());
    }
    
    @Override
    public void sched(Command cmd)
    {
        command = cmd;
    }
}