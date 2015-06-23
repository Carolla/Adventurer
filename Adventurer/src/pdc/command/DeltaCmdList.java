/*
 * DeltaCmdList.java
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

import java.util.Comparator;
import java.util.PriorityQueue;

import pdc.GameClock;

/**
 *	Puts Events on an insertion list in the order in which the 
 *  Events should be executed. Events are Commands, wrapped with a delta value,
 *  and placed on a DeltaQueue (DQ), a specialized priority queue. 
 *   <p>
 *  For example, the four delays of d1=10, d2=25, d3=7, and d4=29 would be inserted as 
 *  7, 3, 15, and 4, the difference between the delays, representing d3, d1, d2, and d4 
 *  respectively. This algorithm ensures that the Events (commands) are executed in the order 
 *  of their delays by looking only at the first item in the DQ each time. 
 *  <p> 
 *  ALGORITHM: <DL>
 * <DT> 1. If the list is empty, simply add the Event to the DQ.  <DD>
 * <DT> 2. If the new delta is less than the first node's delta, insert new Event in front, and decrement
 *  the (now second) node's delta to the difference, so second node's running delta total is the same as 
 *  before the insertion. <DD>
 * <DT> 3. If the new delta is greater than the first node's delta, reset the new delta to the difference
 *  between them and check next node (now current).  Repeat step 2 until the new delta (continually decreasing) 
 *  is inserted, or there are no more items on the list. <DD> 
 * <DT> 4. If the new delta has not been inserted but there are no more nodes, then decrement the new delta by
 *  the last node's delta and add it to the end of the list. <DD>
 * </DL>  
 * 	Implemented as a LinkedList, following Douglas Comer,
 * 	"Operating System Design: The XINU Approach", (c) 1984, pp123-131.
 * 
 * @author Alan Cline 
 * @version <DL>
 * <DT>1.0		Aug 18 	2006 		// original <DD>
 * <DT>1.1		Dec 24 	2007		// cleaned up and sorting bug removed <DD>
 * <DT>2.0		Jun 4 		2008		// move the game clock out into its own class <DD>
 * <DT>2.1		Jul 5			2008		// Final commenting for Javadoc compliance<DD>
 * </DL>
 */
public class DeltaCmdList
{
    /** Internal reference */
    private PriorityQueue<Event> _dlist = null;
    /** Internal reference */
    private GameClock _clock = null;

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 								CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Default constuctor creates the DQ, currently implemented as a LinkedList. */
    public DeltaCmdList()
    {
        _dlist = new PriorityQueue<Event>(new Comparator<Event>() {
			@Override
			public int compare(Event first, Event second) {
				return first.getDelta() - second.getDelta();
			}});
        _clock = GameClock.getInstance();
    }

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 								PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /**
     * Takes the next Event from the DQ (Delta Queue) and increments the timeLog (GameClock)
     * by that delta.
     * 
     * @return	the command to be executed next
     */
    public Command getNextCmd()
    {
        // Extract the first node from the list
        Event evt = _dlist.poll();
        _clock.increment(evt.getDelta());
        Command cmd = evt.getCommand();
        return cmd;
    }

    /** 
     * Inserts a Command into the DQ based on its delta. This is the main engine for building
     * the DQ by implementing the algorithm defined in the class description.
     * 	User commands also contain a duration, which is used to generate an
     * 	internal CmdEnd to trigger the CmdParser to get the next user Command.
     * 
     * @param cmd		a command containing the delay to be used for inserting the delta.
     */
    public void insert(Command cmd)
    {
        // Wrap the command with an Event for insertion when the final delta is known
        Event newEvent = new Event(cmd);
        _dlist.add(newEvent);
    }

    /** Dump the delta list. For debugging only. */
    public void dump()
    {
        Command cmd = null;
        int nbrNodes = _dlist.size();
        Event[] evlist = new Event[nbrNodes];
        _dlist.toArray(evlist);

        int pos = 0;
        for (Event e : _dlist)
        {
            cmd = e.getCommand();
            System.err.println(cmd.getName() + "(" + cmd.getDelay() + ", " +
                    cmd.getDuration() + "); delta = " + e.getDelta() + ", pos = " + pos++);
        }
        System.err.println("-------------------------\n");
    }

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 								PRIVATE METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
//
//    /**
//     * Inserts the new delta in front of the current node, and decrements that node's delta in place.
//     * This private method implements Step 2 of the algorithm described in the class description.
//     * 
//     * @param newEvent 	the new Event to be inserted in the DQ
//     * @param curEvent 		the current Event to be displaced and delta-adjusted by the insertion
//     * @param index 			the position of the node containing the curEvent on the list
//     * @return true if new delta < current node's delta and insertion took place; else return false
//     */
//    private boolean insertLocal(Event newEvent, Event curEvent, int index)
//    {
//        // Guard: new.delta < curNode.delta
//        int newDelta = newEvent.getDelta();
//        int curDelta = curEvent.getDelta();
//        // Insert sooner Event at front of list
//        if (newDelta < curDelta) {
//            // Calc and reset the current delta
//            curEvent.setDelta(curDelta - newDelta);
//            // Update the old Event in the DQ
//            _dlist.set(index, curEvent);
//            // Add the new Event, which pushes the current Node back one
//            _dlist.add(index, newEvent);
//            return true;
//        }
//        else {
//            // Else decrement the newEvent by curEvent.delay and return
//            newEvent.setDelta(newDelta - curDelta);
//            return false;
//        }
//    }

}	// end DeltaCmdList class

