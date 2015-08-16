/**
 * CommandFactory.java
 *
 * Copyright (c) 2007, Carolla Development, Inc. All Rights Reserved.
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, post on servers, to reuse, or to redistribute to lists, requires prior
 * specific permission and/or a fee. Request permission to use from Carolla Development, Inc. by
 * email: acline@carolla.com.
 */

package pdc.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import mylib.MsgCtrl;
import civ.BuildingDisplayCiv;
import civ.MainframeCiv;



/**
 * Creates a concrete object that is a subclass of the Abstract Command class. Once the command is
 * created, the command line string is passed for parsing to retrieve the specific data arguments
 * for the command.
 * 
 * @author Alan Cline
 * @version Aug 31, 2006 // original version <br>
 *          Jun 5, 2007 // updated for new runtime version <br
 *          . Jul 5, 2008 // Final commenting for Javadoc compliance <br>
 *          Feb 18, 2015 // add IOInterface parm for testing and msg outputs <br>
 */
public class CommandFactory
{
    /** Error message if command cannot be found. */
    public static final String ERRMSG_INIT_FAILURE = "Failed to initialize command from user input";

    /** Use Java 8 supplier interface to avoid verbose reflection */
    private Map<String, Supplier<Command>> _commandMap = new HashMap<String, Supplier<Command>>();
    
    private final BuildingDisplayCiv _bdCiv;
    private final MainframeCiv _mfCiv;

    public CommandFactory(MainframeCiv mfCiv, BuildingDisplayCiv bdCiv)
    {
        _mfCiv = mfCiv;
        _bdCiv = bdCiv;
        initMap();
    }

    /**
     * Provide initial values for the commandMap. This can be set up differently as needed for test.
     */
    protected void initMap()
    {
        _commandMap.put("APPROACH", () -> new CmdApproach(_bdCiv)); // Display the description and image of Building exterior
        _commandMap.put("ENTER", () -> new CmdEnter(_bdCiv)); // Display the description and image of Building interior
        _commandMap.put("EXIT", () -> new CmdLeave(_bdCiv)); // Synonym for Leave
        _commandMap.put("LEAVE", () -> new CmdLeave(_bdCiv)); // Leave the interior and go to building's exterior
        _commandMap.put("QUIT", () -> new CmdQuit(_mfCiv, _bdCiv)); // End the program.
        _commandMap.put("RETURN", () -> new CmdReturn(_mfCiv)); // Return to town view
        
        // _commandMap.put("GOTO", CmdGoTo.class); // If parm is a Building or Building type,
        // "Approach" building;
        // if parm = Town, goes to Town view; if null parm, info msg
        // {"TO TOWN", "CmdReturn"}, // Return to Town View
        // { "HELP", "CmdHelp" }, // List the user command names and their descriptions.
        // { "INVENTORY", "CmdInventory" }, // Describe the money the Hero has (later, this will
        // tell
        // the items too)
        // { "LOOK", "CmdLook" }, // Give a description of the Room and any People inside it.
        // { "WAIT", "CmdWait" }, // Wait a specific amount of time, in hours or minutes.
        _commandMap = Collections.unmodifiableMap(_commandMap);
    }

    /**
     * Creates a user Command from its canonical name.<br>
     * NOTE: The subclass command must be in the same package as the Command class.
     * 
     * @param cmdInput the name of the subclass to be created
     * @return Command, the subclass Command created, but referenced polymorphically
     */
    public Command createCommand(CommandInput cmdInput)
    {
        Command command = new NullCommand();
        if (!canCreateCommand(cmdInput)) {
            command.init(cmdInput.parameters);
            return command;
        } else {
            Supplier<Command> supplier = _commandMap.get(cmdInput.commandToken);
            if (supplier != null) {
                command = supplier.get();
            } 
            
            if (command.init(cmdInput.parameters) == false) {
                MsgCtrl.errMsg(ERRMSG_INIT_FAILURE);
            }

            return command;
        }
    }

    public boolean canCreateCommand(CommandInput ci)
    {
        return _commandMap.get(ci.commandToken) != null;
    }

} // end of CommandFactory class

