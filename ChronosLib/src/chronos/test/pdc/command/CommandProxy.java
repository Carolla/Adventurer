/**
 * CommandProxy.java Copyright (c) 2018, Alan Cline. All Rights Reserved.
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
public class CommandProxy extends Command
{
  /** Error message if command cannot be found. */
  public static final String ERRMSG_UNKNOWN = "I don't understand what you want to do.";


  /**
   * Create a flexible command proxy for testing. 
   * 
   * @param name of the command
   * @param delay interval of time before the command starts
   * @param duration amount of time the command consumes on the game clock
   * @parm desc A simple description of what the command does
   * @param fmt Rhe command format to use in the usage statement if invalid args are received
   * @throws NullPointerException
   */
  public CommandProxy(String name, int delay, int duration, String desc, String fmt)
      throws NullPointerException
  {
    super(name, delay, duration, desc, fmt);
  }

  @Override
  public boolean init(List<String> args)
  {
    if (args.size() == 0) {
      return true;
    }
    for (String s : args) {
      _parms.add(s);
    }
    return true;
  }

  
  @Override
  public boolean exec()
  {
    return true;
  }


}
