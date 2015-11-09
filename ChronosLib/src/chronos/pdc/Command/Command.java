/*
 * Command.java
 * 
 * Copyright (c) 2007, Carolla Development, Inc. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, post on servers, to reuse, or to redistribute to lists, requires prior
 * specific permission and/or a fee. Request permission to use from Carolla Development, Inc. by
 * email: acline@carolla.com.
 */

package chronos.pdc.Command; // This package value is needed by the subcommands; see _cmdPackage field

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract base class from which all Commands originate. All abstract services must be defined
 * in the subclass commands. All derived commands are assumed to be in the same package as this
 * <code>Command</code> base class.
 * <p>
 * Instructions for adding a new command:
 * <OL>
 * <LI>Create the command in its own Java file, extending the abstract base class Command. The
 * command file must be in the <code>pdc.Command</code> package.</LI>
 * <LI>Implement the required constructor(s), <code>init()</code> and <code>exec()</code> methods to
 * override the abstract <code>Command</code> methods.</LI>
 * <LI>The <code>init()</code> method validates the parms that follow the command word from the
 * user.</LI>
 * <LI>The <code>exec()</code> method is called by the {@code Scheduler} (which runs on its own
 * Thread), and performs the work of the command.</LI>
 * <LI>Each new command requires certain static attributes, and usually used as defaults in the
 * constructor.
 * <UL>
 * <LI>A command string <I>name</I> (from the user) that is matched with the command class name.
 * </LI>
 * <LI>A one-line <I>description</I> of the command, which is displayed to the user if they need
 * help.</LI>
 * <LI>A <I>delay</I> (time before the command executes).</LI>
 * <LI>A <I>duration</I> (time before command ends).</LI>
 * </UL>
 * Example: The LOOK command has a delay of 0 seconds, and a duration of 4 seconds. It starts
 * immediately on user request, and when it is over, the game clock is incremented 4 seconds.
 * <LI>Place any globals into the <code>Chronos</code> class with the other static globals. Some
 * general potential parms, such as current Room, are attributes of the base class Command; others
 * are available through calls that retrieve singleton pointers to the most common objects a new
 * command might need.</LI>
 * <LI>Override the <code>usage()</code> method if the command uses parms on the command line.</LI>
 * <LI>Add the command string, possible synonyms, and the command name into the
 * <code>CommandParser</code> command table, or into the Building's command table.<br>
 * The generic <code>CommandParser</code> command table is searched first, and if not found, then
 * the command table of the current Building is searched. Once found, the
 * <code>CommandFactory</code> is called to create the command object from its command token.</LI>
 * </OL>
 * 
 * @author Alan Cline
 * @version Aug 24 2006 // original <br>
 *          Dec 31 2007 // Add package constant to wrap all command into this Command package <br>
 *          Feb 16 2008 // Add base class {@code usage} avoid subcommand overrides <br>
 *          Feb 19 2008 // Moved {@code getTalkee} from subcommands into this base class. <br>
 *          Jul 4 2008 // Final commenting for Javadoc compliance <br>
 */
public abstract class Command
{
  public enum CommandStatus { INTERNAL, USER };
  /** The name of the command */
  protected String _name = null;
  /** The amount of time before the command is triggered */
  protected int _delay = 0;
  /** The amount of time that passes while the command is in effect */
  protected int _duration = 0;

    /**
     * The parameters that the subcommand needs must be wrapped in an ArrayList for the subcommand's
     * {@code init} method.
     */
    protected final List<String> _parms;
    /** A short description of the command, used in the general help method. */
    protected final String _description;
    /** The syntax of the command, used in the {@code usage()} method. */
    protected final String _cmdfmt;
    /** Whether params are correct */
    protected boolean _isInitialized = false;

    /** Every command sends user messages to this object */
    protected Object _output;
    
    // ============================================================
    // PUBLIC METHODS
    // ============================================================

    /**
     * Creates a Command based on its name, and assigns it a delay and duration of effect; also
     * initializes a few common references that most subcommands use.
     * 
     * @param name command string to create (as found in the CommandParser table).
     * @param delay the length of time before the command fires
     * @param duration the length of time taken by executing the command
     * @param desc a short explanation of the purpose of the command (for help).
     * @param fmt command syntax for the {@code usage} error message; is null is no parms are needed
     * @throws NullPointerException if the name or fmt parms are null
     */
    public Command(String name, int delay, int duration, String desc, String fmt)
            throws NullPointerException
    {
        if (name == null) {
            throw new NullPointerException("Invalid parms in Command constructor");
        }
        // Assign formal arg values
        _name = name;
        _delay = delay;
        _duration = duration;
        _description = desc;
        _cmdfmt = fmt;
        // Initialize internal attributes
        _parms = new ArrayList<String>();
    }

    public void setOutput(Object mac) 
    {
      _output = mac;
    }
    
    
    // ============================================================
    // ABSTRACT METHODS IMPLEMENTED in SubClass
    // ============================================================

    /**
     * This abstract method must be implemented by the subcommand to validate the parms that follow
     * the command word. The non-default parameters to the subcommand are passed as an ArrayList in
     * the order defined in the Format phrase of the subCommand. The arg list contains all words
     * that follow the command name, so any words that must be combined, such as buildings with
     * multi-word names, must be combined in this method.
     * 
     * @param args list of parms that apply to the Command
     * @return true if the all parms are valid
     */
    public abstract boolean init(List<String> args);

    /**
     * This abstract method must be implemented by the subcommand to execute whatever specific
     * action the subcommand needs to perform. It is called by the {@code Scheduler} polymorphically
     * (for all subcommands).
     * <P>
     * Warning: Be careful that any data stored in the {@code init()} method is still valid by the
     * time the {@code exec()} method is called. Defer collecting needed data as long as possible to
     * avoid mismatching system state.
     * 
     * @return true if the subcomment executed correctly.
     */
    public abstract boolean exec();


    /** If the command parms are incorrect, then a user message must be displayed.
     * It is preferred that the error message is specific, and slightly humoros, while still telling the user
     * how to correct the error.
     * @return the message to be displayed
     */
//    public abstract String usage();
    
    // ============================================================
    // PUBLIC METHODS
    // ============================================================

    /**
     * Combine multiple args input to single-String parm
     * 
     * @param args words that follow the command token
     * @return a single command parm string
     */
    public String convertArgsToString(List<String> args)
    {
        StringBuffer parmString = new StringBuffer();
        for (int k = 0; k < args.size(); k++) {
            parmString.append(args.get(k));
            parmString.append(" ");
        }
        return parmString.toString().trim(); // removing enveloping space
    }

    /**
     * Gets the command's delay before being triggered; used when converting to Event.
     * 
     * @return command's delay
     */
    public int getDelay()
    {
        return _delay;
    }

    /**
     * Gets short description of the command.
     * 
     * @return command description
     */
    public String getDescription()
    {
        return _description;
    }

    /**
     * Gets the command's delay; used when converting to Event.
     * 
     * @return the command's delay
     */
    public int getDuration()
    {
        return _duration;
    }

    /**
     * Gets the command's name.
     * 
     * @return the name.
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Resets the delay (when Patrons initialize in the Inn).
     * 
     * @param newDelay the time that will override the current delay
     */
    public void setDelay(int newDelay)
    {
        _delay = newDelay;
    }

    // ============================================================
    // PROTECTED METHODS
    // ============================================================

    /**
     * Return a generic error message if the Command itself has none.
     * The message is displayed to the User in the user IOPanel
     */
    public String usage()
    {
        // Do not increment the game clock for this command
        _delay = 0;
        _duration = 0;
        String msg = "";
        if (_cmdfmt == null) {
            msg = String.format("USAGE: %s command takes no parms", _name);
        } else {
            msg = String.format("USAGE: %s",  _cmdfmt);
        }
        return msg;
    }

    /**
     * @return true if the parms are correct
     */
    public boolean isInitialized()
    {
        return _isInitialized;
    }

  /**
   * By default, assume commands are UserInput.
   * 
   * @return whether command is input from user
   */
    public boolean isUserInput()
    {
        return true;
    }

    /**
     * By default, assume commands are UserInput.
     * 
     * @return whether command is internal system commnad
     */
    public boolean isInternal()
    {
        return false;
    }

    /**
     * TODO This belongs in the Inn, not the Command class // /** // * Gets the Person, including
     * the Innkeeper, requested by name. // * // * @param nameRequested name of the Person to return
     * // * @return the Person, or null if not in current room //
     */
    // protected Person getTalkee(String nameRequested)
    // {
    // // Return the Innkeeper if requested
    // if (nameRequested.equalsIgnoreCase("Innkeeper") == true) {
    // _talkee = _curRoom.getInnkeeper();
    // }
    // else {
    // _talkee = _curRoom.getPerson(nameRequested);
    // }
    // return _talkee;
    // }


} // end abstract Command class

