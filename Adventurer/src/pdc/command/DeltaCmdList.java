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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

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
    private BlockingQueue<Event> _dlist = null;
    /** Internal reference */
    private GameClock _clock = null;
    private int DEFAULT_QUEUE_SIZE = 10;

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 								CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

    /** Default constuctor creates the DQ, currently implemented as a LinkedList. */
    public DeltaCmdList()
    {
        _dlist = new PriorityBlockingQueue<Event>(DEFAULT_QUEUE_SIZE , new Comparator<Event>() {
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
        Event evt;
        try {
            evt = _dlist.take();
            int deltaTime = evt.getDelta();
            Command cmd = evt.getCommand();
            
            _clock.increment(deltaTime);
            for (Event e : _dlist) {
                e.setDelta(e.getDelta() - deltaTime);
            }
            return cmd;
        } catch (InterruptedException e1) {
            e1.printStackTrace();
            return new NullCommand();
        }

    }
    
    public boolean isEmpty()
    {
    	return _dlist.isEmpty();
    }
    
    /**
     * How long until next Command should run.  Will block if called on an
     * empty queue.
     * 
     * @return how long
     */
    public int timeToNextCmd()
    {
    	return _dlist.peek().getDelta();
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
        int pos = 0;
        for (Event e : _dlist)
        {
            Command cmd = e.getCommand();
            System.err.println(cmd.getName() + "(" + cmd.getDelay() + ", " +
                    cmd.getDuration() + "); delta = " + e.getDelta() + ", pos = " + pos++);
        }
        System.err.println("-------------------------\n");
    }

}	// end DeltaCmdList class

