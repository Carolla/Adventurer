package test.pdc.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.command.Command;
import pdc.command.DeltaCmdList;

public class TestDeltaCmdList {
	private DeltaCmdList dcl;
	private static Random rng;
	
	@BeforeClass
	public static void SetupBeforeClass()
	{
		rng = new Random(System.currentTimeMillis());
	}
	
	@Before
	public void Setup()
	{
		dcl = new DeltaCmdList();
	}

	@Test
	public void InsertedItemComesBackOut() {
		Command testCmd = new SimpleCommand();
		dcl.insert(testCmd);
		Command c = dcl.getNextCmd();
		assertEquals(testCmd, c);
	}
	
	@Test
	public void InsertedItemDoesntMatchIdenticalCommand() {
		Command testCmd = new SimpleCommand();
		Command testCmd2 = new SimpleCommand();
		dcl.insert(testCmd);
		Command c = dcl.getNextCmd();
		assertTrue(c != testCmd2);
	}
	
	@Test
	public void InsertedCommandWithSmallerDelayComesOutFirst() {
		Command fastOne = new SimpleCommand(1);
		Command slowOne = new SimpleCommand(2);
		dcl.insert(slowOne);
		dcl.insert(fastOne);
		Command c = dcl.getNextCmd();
		assertTrue(c != slowOne);
		assertEquals(fastOne, c);
	}
	
	@Test
	public void InsertedCommandWithSmallerDelayComesOutFirstOrderIndependent() {
		Command fastOne = new SimpleCommand(1);
		Command slowOne = new SimpleCommand(2);
		dcl.insert(fastOne);
		dcl.insert(slowOne);
		Command c = dcl.getNextCmd();
		assertTrue(c != slowOne);
		assertEquals(fastOne, c);
	}

	@Test
	public void InsertedCommandWithSmallerDelayComesOutFirstWithManyEntries() {
		Command fastOne = new SimpleCommand(1);
		Command slowOne = new SimpleCommand(2);
		Command slowOne2 = new SimpleCommand(2);
		Command slowOne3 = new SimpleCommand(2);
		Command slowOne4 = new SimpleCommand(2);
		Command slowOne5 = new SimpleCommand(2);
		dcl.insert(slowOne);
		dcl.insert(slowOne2);
		dcl.insert(slowOne3);
		dcl.insert(slowOne4);
		dcl.insert(slowOne5);
		dcl.insert(fastOne);
		Command c = dcl.getNextCmd();
		assertEquals(fastOne, c);
	}
	
	@Test
	public void TimeToNextCommandMatchesSingleCommand() {
		Command slowOne = new SimpleCommand();
		dcl.insert(slowOne);
		assertEquals(0, dcl.timeToNextCmd());
	}
	
	@Test
	public void TimeToNextCommandMatchesLowerOfInsertedCommands() {
		int delay1 = 1;
		int delay2 = 2;
		Command fastOne = new SimpleCommand(delay1);
		Command slowOne = new SimpleCommand(delay2);
		dcl.insert(slowOne);
		dcl.insert(fastOne);
		assertEquals(delay1, dcl.timeToNextCmd());	
	}
	
	@Test
	public void RemainingCommandsAreUpdatedToShowTimePassed() {
		int delay1 = 1;
		int delay2 = 2;
		Command fastOne = new SimpleCommand(delay1);
		Command slowOne = new SimpleCommand(delay2);
		dcl.insert(fastOne);
		dcl.insert(slowOne);
		dcl.getNextCmd();
		assertEquals(dcl.timeToNextCmd(), delay2 - delay1);
	}
	
	@Test
	public void IsEmptyWhenConstructed() {
		assertTrue(dcl.isEmpty());
	}
	
	@Test
	public void IsNotEmptyWhenSomethingInserted() {
		dcl.insert(new SimpleCommand());
		assertFalse(dcl.isEmpty());
	}
	
	@Test
	public void IsEmptyWhenLastItemRemoved() {
		int numCommands = rng.nextInt(20);
		for (int i = 0; i < numCommands; i++) {
			dcl.insert(new SimpleCommand());
		}
		
		for (int i = 0; i < numCommands; i++) {
			assertFalse(dcl.isEmpty());
			dcl.getNextCmd();
		}
		
		assertTrue(dcl.isEmpty());
	}
}
