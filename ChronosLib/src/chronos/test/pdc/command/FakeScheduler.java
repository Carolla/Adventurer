package chronos.test.pdc.command;

import chronos.pdc.command.Command;
import chronos.pdc.command.Scheduler;


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