/*
 * Command.java
 *
 * Copyright (c) 2007, Carolla Development, Inc. All Rights Reserved.
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com.  
 */

package pdc.command;				// This package value is needed by the subcommands; see _cmdPackage field

import java.util.ArrayList;
import java.util.List;

import civ.MainframeCiv;


/**
 *	An abstract base class from which all Commands originate. 
 *	All abstract services must be defined in the subclass commands. 
 * All derived commands are assumed to be in the same package as this 
 * <code>Command</code> base class. 
 * <p>
 * Instructions for adding a new command: <OL>
 * <LI> Create the command in its own Java file, extending the abstract base class Command. 
 * 			The command file must be in the <code>pdc.Command</code> package. </LI>
 * <LI> Implement the required constructor(s), <code>init()</code> and 
 * 			<code>exec()</code> methods to override the abstract <code>Command</code> 
 * 			methods. </LI>
 * <LI> The <code>init()</code> method validates the parms that follow the command string 
 * 			from the user. </LI>
 * <LI> The <code>exec()</code> method is called by the Scheduler and performs the work 
 * 			of the command. </LI>
 * <LI> Each new command requires certain static attributes, and usually used as defaults in 
 * 			the constructor. <UL>
 * <LI> A command string <I>name</I> (from the user) that is matched with the command 
 * 			class name. </LI>
 * <LI> A one-line <I>description</I> of the command, which is displayed to the user if they 
 * 			need help. </LI>
 * <LI> A <I>delay</I> (time before the command executes). </LI>
 * <LI> A <I>duration</I> (time before command ends). </LI>
 * <LI> Example: The LOOK command has a delay of 0 seconds, and a duration of 4 seconds. 
 * 			It starts immediately 	on user request, and when it is over, the game clock is incremented
 * 			4 seconds.</LI> 
 * 		</UL>
 * <LI> Place any globals into the <code>Chronos</code> class with the other static globals. 
 *			Some general potential parms, such as current Room, are attributes of the base class 
 *			Command; others are available through calls that retrieve singleton pointers to the 
 *			most common objects a new command might need.</LI>
 * <LI> Override the <code>usage()</code> method if the command uses parms on the 
 * 			command line.</LI>
 * <LI> Add the command string, possible synonyms, and the command name into the 
 * 			<code>CommandParser</code> command table, or into the Building's command table.<br>
 *          The generic <code>CommandParser</code> command table is searched first, and if not 
 *          found, then the command table of the current Building is searched.     
 *          Once found, the <code>CommandFactory</code> is called to create the command object 
 * 			from its command token. </LI>
 * </OL> 
 * @author Alan Cline
*  @version <DL>
*  <DT>1.0		Aug 24	2006		// original  <DD>
*  <DT>2.0		Dec 31		2007 		// Add package constant to wrap all command into this Command package <DD>
*  <DT>2.1		Feb 16		2008 		// Add base class <code>usage()</code> to avoid subcommand overrides <DD>
*  <DT>2.2		Feb 19		2008 		// Moved getTalkee() from subcommands into this base class. <DD>
 * <DT>2.3		Jul 4			2008		// Final commenting for Javadoc compliance<DD>
 * </DL>
*/
public abstract class Command
{
    /** The package field that allows the CommandFactory to know where the commands are */
    static final protected String CMD_PACKAGE = "pdc.command.";
    /** The name of the command */
    protected String _name = null;
    /** The amount of time before the command is triggered */
    protected int _delay = 0;
    /** The amount of time that passes while the command is in effect */
    protected int _duration = 0;
    /** The parameters that the subcommand needs must be wrapped in an ArrayList 
     * for the subcommand's <code>init</code> method. */
    protected List<String> _parms = null;
    /** A short description of the command, used in the general help method. */
    protected String _description = null;
    /** The syntax of the command, used in the <code>usage()</code> method. */
    protected String _cmdfmt = null;

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 								CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Default contructor */
    public Command() {}

    /**
     * Creates a Command based on its name, and assigns it a delay and duration of effect;
     * also initializes a few common references that most subcommands use.
     * 
     * @param name		command string to create (as found in the CommandParser table).
     * @param delay		the length of time before the command fires
     * @param duration	the length of time taken by executing the command
     * @param desc			a short explanation of the purpose of the command (for help).
     * @param fmt			command syntax for the <code>usage()</code> error message
     * @throws NullPointerException if the name or fmt parms are null
     */
    public Command(String name, int delay, int duration, String desc, String fmt) 
                    throws NullPointerException
    {
        if ((name == null) || (fmt == null)) {
            throw new NullPointerException("Invalid parms in Command constructor");
        }
        // Assign formal arg values
        this._name = name;
        this._delay = delay;
        this._duration = duration;
        this._description = desc;
        this._cmdfmt = fmt;
        // Initialize internal attributes
        this._parms = new ArrayList<String>();
        //        this._inn = Inn.getInstance();
        //        this._curRoom = _inn.getCurrentRoom();
    }

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 								ABSTRACT METHODS IMPLEMENTED in SubClass
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /**
     *  This abstract method must be implemented by the subcommand to collect whatever 
     *  references or initial actions the subcommand needs to do. 
     * The non-default parameters to the subcommand are passed as an ArrayList in the order
     * defined in the Format phrase of the subCommand.
     * 
     * @param args	list of parms that apply to the Command
     * @param mfCiv 
     * @return true if the all parms are valid
     */
    public abstract boolean init(List<String> args, MainframeCiv mfCiv);

    /** 
     * This abstract method must be implemented by the subcommand to execute whatever 
     * specific action the subcommand needs to perform. 
     * It is called by the Scheduler polymorphically (for all subcommands).
     *  
     * @return true if the subcomment executed correctly.
     */
    public abstract boolean exec();

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 								PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    public String convertArgsToString(List<String> args)
    {
        StringBuffer parmString = new StringBuffer();
        for (int k = 0; k < args.size(); k++) {
            parmString.append(args.get(k));
            parmString.append(" ");
        }
        return parmString.toString().trim();    // removing appending space
    }

    /** 
     * Gets the command's delay before being triggered; used when converting to Event.  
    * @return command's delay 
    * */
    public int getDelay()
    {
        return _delay;
    }

    /** Gets short description of the command.
    * @return command description 
    */
    public String getDescription()
    {
        return _description;
    }

    /** 
      * Gets the command's delay; used when converting to Event.
      * @return the command's delay
      */
    public int getDuration()
    {
        return _duration;
    }

    /** Gets the command's name. 
     * @return the name. 
     */
    public String getName()
    {
        return _name;
    }

    /** 
     * Resets the delay (when Patrons initialize in the Inn).
     * @param newDelay		the time that will override the current delay 
     */
    public void setDelay(int newDelay)
    {
        _delay = newDelay;
    }

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 								PROTECTED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** 
     * Displays correct usage format for user command in case of error. This method will work
     * if there are no parms needed for the subcommand. If parms are needed, the  
     * subcommand must supply the override.  
     */
    protected void usage()
    {
        if (_cmdfmt == null) {
            System.err.println(_name + " command takes no parms");
        }
        else {
            System.err.println(_name + " " + _cmdfmt);
        }
        // Do not increment the game clock for this command
        _delay = 0;
        _duration = 0;
    }


    /** TODO This belings in the Inn, not the Command class
//    /**
//      * Gets the Person, including the Innkeeper, requested by name.
//      *  
//      * @param nameRequested		name of the Person to return
//      * @return the Person, or null if not in current room
//      */
//    protected Person getTalkee(String nameRequested)
//    {
//        // Return the Innkeeper if requested
//        if (nameRequested.equalsIgnoreCase("Innkeeper") == true) {
//            _talkee = _curRoom.getInnkeeper();
//        }
//        else {
//            _talkee = _curRoom.getPerson(nameRequested);
//        }
//        return _talkee;
//    }

    
}	// end abstract Command class

