/**
 * CommandFactory.java
 *
 * Copyright (c) 2007, Carolla Development, Inc. All Rights Reserved.
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com.  
 */

package pdc.command;

import civ.MainframeCiv;



/** 
 * Creates a concrete object that is a subclass of the Abstract Command class.
 * Once the command is created, the command line string is passed for parsing to retrieve
 * the specific data arguments for the command.
 * 
 * @author		Alan Cline
 * @version   Aug 31, 2006		// original version <br>
 *            Jun 5, 2007		// updated for new runtime version <br.
 *            Jul 5, 2008   // Final commenting for Javadoc compliance <br>
 *            Feb 18, 2015 // add IOInterface parm for testing and msg outputs <br>
 */
public class CommandFactory 
{
    /** All commands must be in the current package, which is needed for Command creation. */ 
    private Command _curCmd = null;
    /** Use the |@code civ.CommandParser} for handling command errors and message Texts */ 
    private MainframeCiv _msgHandler = null;

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

    /** Default constructor. */
    public CommandFactory(MainframeCiv mfCiv) 
    { 
      _msgHandler = mfCiv;
    }
    
    
    /**
     * Creates a user Command from its canonical name.<br>
     * NOTE: The subclass command must be in the same package as the Command class.
     * 
     * @param	cmdClassName	the name of the subclass to be created
     * @return	Command, the subclass Command created, but referenced polymorphically
     */
    public Command createCommand(String cmdClassName)
	{
		try {
			// Subclass Commands must have empty constructors (no formal input arguments)
      _curCmd = (Command) Class.forName(Command.CMD_PACKAGE + cmdClassName).newInstance();
      _curCmd.setMsgHandler(_msgHandler);
        } catch (NullPointerException e) {
            System.err.println("Command name or format is illegally null: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Can't find Class to load: " + e.getMessage());
		}
		return _curCmd;
	}

					
}	// end of CommandFactory class

