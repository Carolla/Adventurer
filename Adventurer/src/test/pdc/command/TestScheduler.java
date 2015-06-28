package test.pdc.command;

import org.junit.Test;

import pdc.command.DeltaCmdList;
import pdc.command.Scheduler;

public class TestScheduler {

	@Test
	public void test() {
	    Scheduler s = new Scheduler(new DeltaCmdList());
	    s.doOneCommand();
	}

}
