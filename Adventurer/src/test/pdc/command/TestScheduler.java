
package test.pdc.command;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pdc.command.Scheduler;

public class TestScheduler
{
//    TODO(timothyarm) This method is blocking, and SHOULD block rather than complete
//    @Test(timeout=1000,expected = Exception.class)
//    public void RunningWithoutCommandBlocks()
//    {
//        Scheduler s = new Scheduler();
//        s.doOneCommand();
//    }

    @Test
    public void RunCommandIsCommandInserted()
    {
        Scheduler s = new Scheduler();
        CheckingCommand c = new CheckingCommand();
        s.sched(c);
        s.doOneCommand();
        assertTrue(c.hasBeenRun);
    }

    @Test
    public void DoOneCommandOnlyDoesOneCommand()
    {
        Scheduler s = new Scheduler();
        CheckingCommand c = new CheckingCommand();
        CheckingCommand c2 = new CheckingCommand();
        s.sched(c);
        s.sched(c2);
        s.doOneCommand();
        assertFalse(c2.hasBeenRun);
    }

    public class CheckingCommand extends SimpleCommand
    {
        public boolean hasBeenRun = false;

        @Override
        public boolean exec()
        {
            hasBeenRun = true;
            return true;
        }

    }
}
