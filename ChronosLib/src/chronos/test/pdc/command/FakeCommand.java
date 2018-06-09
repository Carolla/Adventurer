/**
 * FakeCommand.java Copyright (c) 2018, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */

package chronos.test.pdc.command;

import java.util.List;

import chronos.pdc.command.Command;

/** Test command that requires two arguments */
public class FakeCommand extends Command
{
  static private final String CMD_NAME = "FAKE_CMD";
  static private final String CMD_DESCRIPTION =
      "A simple no-parm command used for testing the Command class";
  static private final String CMDFMT = CMD_NAME + " [Keyword1] [Keyword2]";
  static private final int DELAY = 0;
  static private final int DURATION = 10;

  /** Error message if command cannot be found. */
  public static final String ERRMSG_UNKNOWN = "I don't understand what you want to do.";


  public FakeCommand()
      throws NullPointerException
  {
    super(CMD_NAME, DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
  }

  @Override
  public boolean init(List<String> args)
  {
    return true;
  }

  @Override
  public boolean exec()
  {
    return true;
  }


}
