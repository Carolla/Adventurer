/**
 * FakeCommand2.java Copyright (c) 2018, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */

package chronos.test.pdc.command;

import java.util.List;

import chronos.pdc.command.Command;

/** Test command that requires no argument */
public class FakeCommand2 extends Command
{
  static private final String CMD_NAME = "ANOTHER_FAKE_CMD";
  static private final String CMD_DESCRIPTION =
      "Another test-only command, requiring no parms, used for testing the Command class";
  static private final String CMDFMT = null;
  static private final int DELAY = 0;
  static private final int DURATION = 10;

  /** Error message if command cannot be found. */
  public static final String ERRMSG_UNKNOWN = "I don't understand what you want to do.";


  public FakeCommand2()
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
