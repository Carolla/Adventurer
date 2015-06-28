package test.pdc.command;

import java.util.List;

import pdc.command.Command;

public class SimpleCommand extends Command {

	public SimpleCommand() { this(0); }

	public SimpleCommand(int delay) {
		super("", delay, 0, "", "");
	}

	@Override
	public boolean init(List<String> args) {
		return true;
	}

	@Override
	public boolean exec() {
		return true;
	}
}
