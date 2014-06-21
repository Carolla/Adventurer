/**
 * SplashInitWindow.java
 *
 * Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved.
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com.  
 */

package rumorMillStuff.src.hic;

import rumorMillStuff.src.dmc.DgnBuild;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;

/**
 * Shows credits, copyrights, and an image until initialization processes are completed.
 * It closes automatically after init processing, or after a short pre-defined delay, whichever is 
 * longer. Of course, if the user clicks the OS-based icon X at the top of the window frame, 
 * the SplashInitWindow will close.
 * <p> 
 * NOTE: Although there is a reusable class <code>SplashWindow</code>, the functionality 
 * of that class has much more than is needed, so many of the methods are removed from 
 * <code>SplashWindow</code> for <code>SplashInitWindow</code> (similarly to why
 * the <code>WindowAdaptor</code> class was created).  
 * @author 	Al Cline
 * @version <DL>
 * <DT>	1.0	Feb 28 2008		// original version, based on 2004 SplashWindow class <DD>
 * <DT>	2.0	Mar 26 2008		// updated for multi-threading background initializaton <DD>
 * <DT>	2.1	May 21 2008		// updated for run alone outside the main window while program inits. <DD>
 * <DT> 2.2	Jul      1 2008		// Final commenting for Javadoc compliance<DD>
 * </DL>
 */
@SuppressWarnings("serial")
public class SplashInitWindow extends JDialog 
{
	/** Title to display on splash screen */
	private static final String SPLASH_MSG = "Please wait. Opening the Inn for business....";
	/** Image to display on splash screen */
	private static final String SPLASH_PIC = Dgn.INPUT_FILEBASE+ "tavern.jpg";    	
	/** Splash screen will display this long initally before checking for end of background processing. */
	private final int TIMER_INITIAL_DELAY = 3000; 			// minimum time for the splash window to show (in milliseconds)  
	/** Splash screen will check processing periodically, as defined by this constant. */
	private final int TIMER_DELAY = 500; 	// recheck for end of init processing this often (in milliseconds).  
	
    // Method constants
	/** Slight margin around panels on contentPane */
    private final int PANEL_PADDING	= 25;
	/** Lifts above center slightly for psychological readability */
    private final int UP_PADDING	= 75;

	/** Title to display on the splash screen */
	private final String TITLE = "<HTML><H2>Welcome to the Rumor Mill</H2></HTML>";

	/** Timer to close window automatically after designated time. */
    private Timer _timer;
    
    /** Flag to indicate that the initialization is complete. 
     * Timer will not close the window if this is set to false. 
     */
	private boolean _initDone = false;
		
    /**
     * Create the SplashInitWindow to display an image. 
     * This method also creates a timer to keep track of when to close it automatically. 
     */
	public SplashInitWindow() 
	{
		// Set initial properties
        setUndecorated(true);			// disable the close and resize buttons while init is processing
		setModal(true);
        Container contentPane = getContentPane();

        // THE TITLE PANEL
        JLabel titleLabel = new JLabel(TITLE, SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLUE);
        // Empty borders puts spacing around labels
		Border lineBorder = BorderFactory.createLineBorder(Color.DARK_GRAY);
		titleLabel.setBorder(lineBorder);
		
        // THE IMAGE PANEL
        ImageIcon image = new ImageIcon(SPLASH_PIC);
        JLabel imageLabel = new JLabel(image);
	    
	    // THE MESSAGE PANEL 
	    // Show copyright info, and ask users to wait for initialization to complete.
        // Empty borders puts spacing around labels
        // Create panel to contain both message labels.
        JPanel msgPanel = new JPanel(new BorderLayout());
        JLabel copyrightLabel = new JLabel(
        				"<HTML> &nbsp&nbsp&nbsp "+ Dgn.COPYRIGHT  + "<HTML>"); 

        JLabel msgLabel = new JLabel(	"<HTML><H3>&nbsp " + SPLASH_MSG + "</H3></HTML>");
        msgLabel.setBorder(lineBorder);

        msgPanel.add(copyrightLabel, BorderLayout.NORTH);
        msgPanel.add(msgLabel, BorderLayout.SOUTH);
        
        // ADD THE SPLASH WINDOW TO THE WINDOW 
        contentPane.add(titleLabel, "North");			
        contentPane.add(imageLabel, "Center");
        contentPane.add(msgPanel, "South");			

        // Size the Splash window based on screen dimensions and number of panels
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension cpSize = contentPane.getPreferredSize();

        int adjSplashWidth = Math.min(screenDim.width, cpSize.width + PANEL_PADDING);
        int adjSplashHeight = Math.min(screenDim.height, cpSize.height + PANEL_PADDING);
        setSize(adjSplashWidth, adjSplashHeight);

        // Center Splash Window on screen
        int leftHoriz = (screenDim.width - adjSplashWidth)/2;
        int topVert =   (screenDim.height - adjSplashHeight)/2 - UP_PADDING; ;
        setLocation(leftHoriz, topVert);
        
        // Set the wait cursor
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	        
        // ADD CLOSING EVENT HANDLER
        // Constructor that sets up the window using anonymous inner adaptor class
	    // WindowAdaptor is used to implement WindowListener without all the no-op methods for the required overrides.
        addWindowListener(new WindowAdapter() 
        {	public void windowClosing(WindowEvent e)
        	{ 
        		setVisible(false);
        	}
    	} );

        // START THE SECOND THREAD AND TIMEOUT LOGIC
        // Create the timer to signal each interval, and attach its own actionListener. 
       ActionListener timedTask = new ActionListener()
        	{ 	
        		public void actionPerformed(ActionEvent evt) 
        		{
	                Dgn.debugMsg("Timer event returned to SplashInitWindow.actionPerformed()");
                	if (_initDone == true) {				
//	                		timer.stop();
                		timedOut();
                	}
            		// If timer returns before init processing is completed, reset the delay and continue checking
                	else {
	            		Dgn.debugMsg("Timer returned but processing still in play." + System.currentTimeMillis());
                	}
        		}
        	};

        // Start the timer thread
        _timer = new Timer(TIMER_DELAY, timedTask);
        _timer.setInitialDelay(TIMER_INITIAL_DELAY);			// the timer returns initially after this amount of time
        _timer.start();
        Dgn.auditMsg("Timer started in splash window.");
        runBackgroundTask();					// The timer will check periodically if this task is completed
	        
	}	// end SplashInitWindow constructor


    /**
     * Following <I>Big Java</I>, by Cay Horstmann, John Wiley & Sons, 2002, pp810-815
     * <UL>
     * <LI> This class implements Runnable. The caller, SplashInitWindow, creates a new Thread with this class as the Runnable,
     * 			aand begins executing it by calling <code>Thread.start()</code>. </LI>
     * <LI> This class's override <code>run()</code> method is called implicitly by the <code>Thread.start()</code> method. </LI>
     * <LI> The <code>Thread.stop()</code> method is deprecated; the thread terminates when <code>run()</code> exits. </LI>
     *  </UL>
     */		
    private void runBackgroundTask()
	{
    	Dgn.auditMsg("Thread invoked background task run method.");
		// Create the DgnBuild object to load and parse the input files
    	DgnBuild db = new DgnBuild();
    	db.load();								// Read, parse, validate, and load the xml file
    	db.init();									// Create the singletons for the user session
    	_initDone = true;
	}
         
         
    /** Close the splash. */
    private synchronized void timedOut()
    {
    	Dgn.debugMsg("SplashInitWindow.timedOut(): initializaton complete.");
    	_timer.stop();
        setVisible(false);
    }

    
    /**
     * Null method to implement the required interface for lost focus events. 
     * @param evt	FocusEvent to ignore
     */
    public void focusLost(FocusEvent evt)
    {
    	// NO OP
    }

    /**
     * Null method to implement the required interface to bounce the focus event back to the 
     * Windows dispatcher.
     *  
     * @param evt	FocusEvent to set
     */
    public void focusGained(FocusEvent evt)
    {
    	// NO OP
    }

    /**
     * Null method to implement the required interface for key hit events. 
     * @param evt	FocusEvent to ignore
     */
    public void keyTyped(KeyEvent evt)
    {
    	// NO OP
    }
    
    /**
     * Null method to implement the required interface for key release events. 
     * @param evt	FocusEvent to ignore
     */
    public void keyReleased(KeyEvent evt) 
    {
    	// NO OP
    }

    /**
     * Null method to implement the required interface for key press events. 
     * @param evt	FocusEvent to ignore
     */
    public void keyPressed(KeyEvent evt) 
    {
    	// NO OP
    }

} // end of SplashInitWindow outer class
