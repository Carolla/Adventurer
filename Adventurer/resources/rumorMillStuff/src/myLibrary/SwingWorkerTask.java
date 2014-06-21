/**
 * SwingWorkerTask.java
 * Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 */

package rumorMillStuff.src.myLibrary;

import java.awt.EventQueue;

/**
 *  Extend this class to define an asynchronous (background) task that udpates a Swing UI. 
 *  Its main purpose it to provide the anonymous Runnable and thread code to make the derived class more readable. 
 *  This class is adapted from Hans Muller's <code>SwingWorker</code> class, as reported in 
 *  "Core Java", Volume II, p78, by Cay Horstmann and Gary Cornell, Sun MicroSystems Press, 2005. 
 * See URL <code>http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html</code>. <p>
 * Note: This utility is based on Java SE 5, and is superceded by a <code>SwingWorker <T,V></code> class in SE 6. 
 * @author Al Cline 
 * @version <DL>
 * <DT> Build 1.0		Mar 12, 2008   // original adaption <DD>
 * </DL>
 */
public abstract class SwingWorkerTask implements Runnable
{
	/** Flag that designates the background thread has completed. */
	private boolean _initDone;
	
	/** 
	 * Place your task in this method. Be sure to call <code>doUpdate()</code>, not <code>update()</code>,
	 * to show the update after each unit of work.
	 */
	public abstract void work() throws InterruptedException;

	/** Override this method for UI operations before work commences. */
	public void init() { }
	
	/** Override this method for UI operations after each unit of work, in your <code>work()</code> method. */
	public void update() { }
	
	/** Override this method for UI operations after work is completed. */
	public void finish() { }
	
	
	/** 
	 * Implements the anonymous Runnable code to put an Event onto the EventQueue for <code>init()</code>. 
	 */
	private void doInit()
	{
		EventQueue.invokeLater(new 
			Runnable()
			{
				public void run() { init(); }
			});
	}
	
	
	/**
	 * Call this method from within your <code>work()</code> method to show the update after each unit of work. 
	 * Implements the anonymous Runnable code to put an Event onto the EventQueue for <code>update()</code>. 
	 */
	protected final void doUpdate()
	{
		if (_initDone) {
			return;
		}
		EventQueue.invokeLater(new 
				Runnable()
				{
					public void run() { update(); }
				});
		}
	
	
	/** 
	 * Implements the anonymous Runnable code to put an Event onto the EventQueue for <code>finish()</code>. 
	 */
	private void doFinish()
	{
		EventQueue.invokeLater(new 
			Runnable()
			{
				public void run() { finish(); }
			});
	}
	
	
	/**
	 * The actual complete framework weaving <code>init()</code>, <code>work()</code>, and <code>finish()</code> together.
	 * The <code>update()</code> method should be integrated as part of <code>work()</code> (called within) 
	 * by the author of the derived class. 
	 */
	public final void run()
	{
		doInit();
		try
		{
			_initDone = false;
			work();
		}
		catch (InterruptedException ex)
		{
		}
		finally
		{
			_initDone = true;
			doFinish();
		}
	}
						
}	// end of abstract SwingWorkerTask class				
