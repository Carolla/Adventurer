
package test.pdc.command;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chronos.pdc.command.Command.CommandStatus;
import chronos.pdc.command.Scheduler;

public class TestScheduler
{
    
    private Scheduler _s;

    @Before
    public void setup()
    {
        _s = new Scheduler();
    }
    
    @Test
    public void UserCommandIsRunWhenUserCommandScheduled()
    {   
//        CheckingCommand c = new CheckingCommand(CommandStatus.USER);
//        _s.sched(c);
//        assertTrue(c.hasBeenRun);
    }
    
    @Test
    public void InternalCommandIsRunWhenUserCommandScheduled()
    {   
        CheckingCommand c = new CheckingCommand(CommandStatus.INTERNAL);
        CheckingCommand c2 = new CheckingCommand();
        _s.sched(c);
        _s.sched(c2);
        assertTrue(c.hasBeenRun);
    }

    @Test
    public void InternalCommandIsNotRunWhenScheduled()
    {
        CheckingCommand c = new CheckingCommand(CommandStatus.INTERNAL);
        _s.sched(c);
        assertFalse(c.hasBeenRun);
    }

    public class CheckingCommand extends SimpleCommand
    {
        public boolean hasBeenRun = false;
        public boolean isInternal = false;

        public CheckingCommand()
        {
            this(CommandStatus.USER);
        }
        
        public CheckingCommand(CommandStatus status)
        {
            if (status == CommandStatus.INTERNAL) {
                isInternal = true;
            }
        }

        @Override
        public boolean exec()
        {
            hasBeenRun = true;
            return true;
        }
        
        @Override
        public boolean isInternal()
        {
            return isInternal;
        }
        
        @Override
        public boolean isUserInput()
        {
            return !isInternal;
        }

    }
}
