package test.pdc.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chronos.pdc.Command.Command;
import chronos.pdc.Command.Event;

public class TestEvent {

	@Test
	public void DeltaIsSetToCommandDelay() {
		Command command = new SimpleCommand();
		Event e = new Event(new SimpleCommand());
		assertEquals(e.getDelta(), command.getDelay());
	}

}
