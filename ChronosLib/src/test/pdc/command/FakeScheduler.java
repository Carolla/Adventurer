package test.pdc.command;

import chronos.pdc.Command.Command;
import chronos.pdc.Command.Scheduler;


public class FakeScheduler extends Scheduler
{
    public Command command;

    public FakeScheduler()
    {
        super();
    }
    
    @Override
    public void sched(Command cmd)
    {
        command = cmd;
    }
}