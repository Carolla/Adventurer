package test.pdc.command;

import org.junit.Test;

import pdc.command.Command;
import pdc.command.DeltaCmdList;
import pdc.command.Scheduler;

public class TestScheduler {

	@Test(expected=NullPointerException.class)
	public void RunningEmptyCommandThrowsException() {
	    Scheduler s = new Scheduler(new DeltaCmdList());
	    s.doOneCommand();
	}

	public class PhonyDeltaCmdList extends DeltaCmdList {
	    @Override
	    public Command getNextCmd()
	    {
	        return new SimpleCommand();
	    }
	}
}
