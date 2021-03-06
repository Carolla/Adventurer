/**
 * Event.java Copyright (c) 2018, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */

package chronos.pdc.command;


/**
 * Wrap Commands using the Command.delay to create a delta because the delay attribute of
 * Command should not be modified here.
 * 
 * @author Alan Cline
 * @version Aug 25 2006 // original <br>
 *          Jul 5, 2008 // Final commenting for Javadoc compliance <br>
 *          June 10, 2017 // Updated Javadoc and for testing <br>
 * 
 * @see DeltaCmdList
 */
public class Event implements Comparable<Event>
{
  /**
   * The key into the DeltaList, taken from Command.delay. The _delta is the time before
   * execution relative to the previous command's delta.
   */
  private int _delta;

  /** Can be any object, but is most likely a Command */
  private Command _cmd;


  // ===============================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ===============================================================================

  /**
   * Builds an Event around the command being encapsulated, and the command's delay.
   * 
   * @param cmd the Command to be wrapped into the Event and enqueued.
   */
  public Event(Command cmd)
  {
    _delta = cmd.getDelay();
    _cmd = cmd;
  }


  // ===============================================================================
  // PUBLIC METHODS
  // ===============================================================================

  @Override
  public int compareTo(Event other)
  {
    return _delta - other._delta;
  }


  /**
   * Gets the Command inside the Event wrapper
   * 
   * @return the name of the encapsulated Command.
   */
  public Command getCommand()
  {
    return _cmd;
  }


  /**
   * Gets the delta value
   * 
   * @return the delta from the Event.
   */
  public int getDelta()
  {
    return _delta;
  }


  /**
   * Sets the Event's delay .
   * 
   * @param value time before execution relative to the previous command's delta
   */
  public void setDelta(int value)
  {
    _delta = value;
  }


} // end Event class

