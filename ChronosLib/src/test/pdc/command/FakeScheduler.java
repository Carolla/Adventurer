package test.pdc.command;

import chronos.civ.DefaultUserMsg;
import chronos.pdc.command.Command;
import chronos.pdc.command.Scheduler;


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