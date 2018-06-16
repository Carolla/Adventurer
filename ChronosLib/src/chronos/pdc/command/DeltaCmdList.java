/**
 * DeltaCmdList.java Copyright (c) 2018, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */

package chronos.pdc.command;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;



/**
 * Puts Events on an insertion list in the order in which the Events should be executed. Events
 * are Commands, wrapped with a delta value, and placed on a DeltaQueue (DQ), a specialized
 * priority queue.
 * <P>
 * For example, the four delays of d1=10, d2=25, d3=7, and d4=29 would be inserted as 7, 3, 15,
 * and 4, the difference between the delays, representing d3, d1, d2, and d4 respectively. This
 * algorithm ensures that the Events (commands) are executed in the order of their delays by
 * looking only at the first item in the DQ each time.
 * <p>
 * ALGORITHM:
 * <ol>
 * <li>If the list is empty, simply add the Event to the DQ.
 * <li>If the new delta is less than the first node's delta, insert new Event in front, and
 * decrement the (now second) node's delta to the difference, so second node's running delta
 * total is the same as before the insertion.
 * <li>If the new delta is greater than the first node's delta, reset the new delta to the
 * difference between them and check next node (now current). Repeat step 2 until the new delta
 * (continually decreasing) is inserted, or there are no more items on the list.
 * <li>If the new delta has not been inserted but there are no more nodes, then decrement the
 * new delta by the last node's delta and add it to the end of the list.
 * </ol>
 * <P>
 * Implemented as a BlockingQueue, but following Douglas Comer ("Operating System Design: The
 * XINU Approach", (c) 1984, pp123-131) who implemented the algorithm as a LinkedList.
 * 
 * @author Alan Cline
 * @version Aug 18 2006 // original <br>
 *          Dec 24 2007 // cleaned up and sorting bug removed <br>
 *          Jun 4 2008 // move the game clock out into its own class <br>
 *          Jul 5 2008 // Final commenting for Javadoc compliance <br>
 *          June 10, 2018 // updated Javadoc and for testing <br>
 *          June 14, 2018 // modified as needed to pass tests <br>
 *          June 16, 2018 // Moved all clock references outside this library routine <br>
 */
public class DeltaCmdList
{
  /** The actual Delta Queue (DQ) */
  private PriorityBlockingQueue<Event> _dlist = null;
  private int DEFAULT_QUEUE_SIZE = 11;

  // ------------------------------------------------------------------------------------
  // CONSTRUCTOR
  // ------------------------------------------------------------------------------------

  /** Default constuctor creates the DQ, currently implemented as a PriorityBlockingQueue. */
  public DeltaCmdList()
  {
    _dlist = new PriorityBlockingQueue<Event>(DEFAULT_QUEUE_SIZE, new Comparator<Event>() {
      @Override
      public int compare(Event first, Event second)
      {
        return first.getDelta() - second.getDelta();
      }
    });
  }


  // ------------------------------------------------------------------------------------
  // PUBLIC METHODS
  // ------------------------------------------------------------------------------------

  /** Clear the DQ of all elements. Mostly used for testing, has infrequent use */
  public void clear()
  {
    _dlist.clear();
  }


  /**
   * Convert the DQ to an array of elements for special processing
   * 
   * @return the array in the order the Commands will be executed
   */
  public Object[] toArray()
  {
    Object[] ary = new Object[_dlist.size()];
    ary = _dlist.toArray();
    Arrays.sort(ary);
    return ary;
  }


  /**
   * Takes the next Event from the DQ (Delta Queue)
   * 
   * @return the command to be executed next, or NullCommand if the DQ is empty
   */
  public Command getNextCmd()
  {
    if (_dlist.isEmpty()) {
      return new NullCommand();
    }
    Event evt;
    try {
      evt = _dlist.take();
      int deltaTime = evt.getDelta();
      Command cmd = evt.getCommand();
      for (Event e : _dlist) {
        e.setDelta(e.getDelta() - deltaTime);
      }
      return cmd;

    } catch (InterruptedException e1) {
      e1.printStackTrace();
      return new NullCommand();
    }
  }


  /**
   * Inserts a Command into the DQ based on its delta. This is the main engine for building the
   * DQ by implementing the algorithm defined in the class description. User commands also
   * contain a duration, which is used to generate an internal CmdEnd to trigger the CmdParser
   * to get the next user Command.
   * 
   * @param cmd a command containing the delay to be used for inserting the delta.
   * @throws NullPointrException if parm is null
   */
  public void insert(Command cmd)
  {
    // Wrap the command with an Event for insertion when the final delta is known
    Event newEvent = new Event(cmd);
    _dlist.add(newEvent);
  }

  public boolean isEmpty()
  {
    return _dlist.isEmpty();
  }


  /** Get the number of elements in the DQ */
  public int size()
  {
    return _dlist.size();
  }


} // end DeltaCmdList class
