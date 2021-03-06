/**
 * CommandFactory.java
 *
 * Copyright (c) 2007, Carolla Development, Inc. All Rights Reserved.
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development,
 * Inc. by email: acline@carolla.com.
 */

package pdc.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import chronos.civ.UserMsgInterface;
import chronos.pdc.command.Command;
import civ.BuildingDisplayCiv;
import civ.MainframeCiv;


/**
 * Creates a concrete object that is a subclass of the Abstract Command class. Once the command
 * is created, the command line string is passed for parsing to retrieve the specific data
 * arguments for the command.
 * 
 * @author Tim Armstrong
 * @version Aug 31, 2006 // original version <br>
 *          Jun 5, 2007 // updated for new runtime version <br>
 *          Jul 5, 2008 // Commenting for Javadoc compliance <br>
 *          Feb 18, 2015 // add IOInterface parm for testing and msg outputs <br>
 */
public class CommandFactory
{
  /** Error message if command cannot be found. */
  public static final String ERRMSG_INIT_FAILURE = "Invalid parms for command";
  public static final String ERRMSG_UNKNOWN = "I don't undestand what you want to do";

  /** Use Java 8 supplier interface to avoid reflection */
  private Map<String, Supplier<Command>> _commandMap = new HashMap<String, Supplier<Command>>();

  private BuildingDisplayCiv _bdCiv;
  private MainframeCiv _mfCiv;
  private UserMsgInterface _output;


  /** Keep a table for command, as lambda functions */
  public CommandFactory()
  {}

  /** Keep a table for command, as lambda functions */
  public CommandFactory(BuildingDisplayCiv bdCiv, MainframeCiv mfCiv)
  {
    _bdCiv = bdCiv;
    _mfCiv = mfCiv;
    _output = _mfCiv.getOutput();
  }

  /**
   * Provide initial values for the commandMap. This can be set up differently as needed for
   * test.
   */
  public void initMap()
  {
    // Display the description and image of Building exterior
    _commandMap.put("APPROACH", () -> new CmdApproach(_bdCiv));
    // Enter the interior of the Building
    _commandMap.put("ENTER", () -> new CmdEnter(_bdCiv));
    // Synonym for Leave and then Quit the program
    _commandMap.put("EXIT", () -> new CmdExit(_mfCiv));
    // Get near description for NPC
    _commandMap.put("INSPECT", () -> new CmdInspect(_bdCiv));
    // Leave the inside of the Building and go outside
    _commandMap.put("LEAVE", () -> new CmdLeave(_bdCiv));
    // Get Room Description or Get NPC names
    _commandMap.put("LOOK", () -> new CmdLook(_bdCiv));
    // End the program.
    _commandMap.put("QUIT", () -> new CmdQuit(_mfCiv, _bdCiv));
    // Return to town view
    _commandMap.put("RETURN", () -> new CmdReturn(_bdCiv));
    // Get information from an NPC
    _commandMap.put("TALK", () -> new CmdTalk(_bdCiv));
    // Just sit there
    _commandMap.put("WAIT", () -> new CmdWait());

    // Locks the command map as read-only
    _commandMap = Collections.unmodifiableMap(_commandMap);
  }


  /**
   * Creates a user Command from its canonical name.<br>
   * NOTE: The subclass command must be in the same package as the Command class.
   * 
   * @param cmdToken the name of the subclass to be created
   * @return Command, the subclass Command created, but referenced polymorphically
   */
  public Command createCommand(String cmdToken)
  {
    Supplier<Command> supplier = _commandMap.get(cmdToken);
    if (supplier != null) {
      Command cmd = supplier.get();
      cmd.setOutput(_output);
      return cmd;
    } else {
      return null;
    }
  }


  /** MockCommands inner class for testing */
  public class MockCommands
  {
    public MockCommands()
    {}

    public Map<String, Supplier<Command>> getCommands()
    {
      return _commandMap;
    }

  } // end of MockCommands inner class


} // end of CommandFactory class

