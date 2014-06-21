/**
 * OccupationMainFrame.java
 *
 * Copyright (c) 2009, Carolla Development, All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package hic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 *	Serves as the background frame for all GUI activities, 
 * and contains the various MenuBars: Heroes,  Enter, and Help
 *  <code>MainFrame</code> is the object owner through which the menubar works.
 *  * 
 *	@author Timothy Armstrong
 *  @version <DL>
 * <DT>1.0		Sept 12 2011	 		// original- adapted 	<DD>
 * </DL>
*/
@SuppressWarnings("serial")
public class OccupationMainFrame extends JFrame
{
	/** Number of rows for subpanels */
	static public final int PANEL_SHIFT = 40;			// not private because inner class needs it
	/** Default color for panels */
	static public final Color PANEL_COLOR = Color.ORANGE;
	/** Number of pixels on empty border spacing */
	static public final int PAD = 10;

/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PACKAGE SCOPE
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

    /** All resources are stored in this directory for this application */
    public final String RESOURCES_DIRECTORY = 
                "/Projects/workspace/Adventurer/src/resources/"; //$NON-NLS-1$

    /** Help directory specific to this application */
    public final String APPHELP_DIRECTORY = 
                "/Projects/workspace/Adventurer/src/resources/help/"; //$NON-NLS-1$

    /** Help directory specific to this application */
    public final String APPHELP_FILENAME =  "AdventurerHelp.xml"; //$NON-NLS-1$
    

	/** Number of rows in the user's output window */
	static int USERWIN_HEIGHT;

	/** Number of columns in the user's output window */
	static int USERWIN_WIDTH;

	/** Return values for the saving prompt screen */
	public static int SAVE = 1;
	public static int NOSAVE = 0;
	public static int CANCEL = -1;
	
	
	/** Set true if any changes occurred since last save */
	static private boolean _editFlag = false;

	/** Internal reference to the singleton windowing system. */ 
	static private OccupationMainFrame _mainframe = null;
	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								LOCALS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

    // /** Content pane window before panels overwrite it */
    // private final Color BACKGROUND_COLOR = Color.BLUE;
    //
    // /** Default title for window until author names the dungeon */
    // private final String DEF_WIN_TITLE = "MainFrame.BiljurBase";

/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								STATIC MAIN METHOD
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
		
	/** 
	 * Creates the main frame and passes control to it; starts a separate Swing thread for the
	 * GUI's <code>Swing Event Queue</code> (SEQ), per SE 6.0 and Swing's 
	 * "strict single-thread rule". 
	 * All Swing processing occurs from the single SEQ thread. Unlike older versions 
	 * of Java, Swing must now be invoked inside an EventQueue <code>Runnable</code>. 
	 * As <i>Core Java</i> (Volume 1, Horstmann & Cornell, (c) 2008, p287) states: 
	 * "For now, you should simply consider it a magic incantation that is used to start a 
	 * Swing program." 
	 * 
	 * Also initializes the system by creating necessary singletons and data files.
	 * 
	 * @param	args 	unused command line arguments 
	 */
	public static void main(String[] args)
	{
        /** All Swing processing occurs from the single EventQueue thread. */
        EventQueue.invokeLater(new Runnable()
        {
    		public void run()
    		{	
		    	// Creates the main windowing system, set window title from dungeon name
    			OccupationMainFrame frame = OccupationMainFrame.getInstance();
    			// Calls the inner class Terminator WindowEvent Listener instead 
    			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    			
    			OccupationDialogue occDlg = new OccupationDialogue(frame);
    			occDlg.setVisible(true);
    			System.exit(0);
    		}
        });
        
	}	// end of static main()


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

    /**
     * Allows the reference for the MainFrame windowing system to be shared.
     * @return reference to the main frame
     */
    public static  OccupationMainFrame getInstance()
    {
        if (_mainframe == null) {
            _mainframe = new OccupationMainFrame();
        }
        return _mainframe;
    }


    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 			CONSTRUCTOR(S) AND RELATED METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
    
        /** Get the editFlag to know if if any edits have made Saving necessary */
        static public boolean getEditFlag()
        {
        	return _editFlag;
        }


    /**
	 * Singleton constructor for main menu frame, builds the GUI frame for the menus and 
	 * panels to hang from. It also creates and initializes the <code>HelpDialog</code>,
	 * which keeps its own reference, and can be accessed by 
	 * <code>HelpDialog.createInstance(..)</code> when needed. 
	 */
	private OccupationMainFrame()
	{
	    // Set title, screen size, and closing actions for main frame background window.
		setupMainframe();

//		// Create non-modal HelpDialog singleton for objects to reference later
//		HelpDialog.createInstance(this, APPHELP_DIRECTORY, APPHELP_FILENAME);
//		/** Set the default HelpKey for the general panel; there is no specific help text. */
//		HelpKeyListener _helpKey = new HelpKeyListener("Help");
//		// Add the Help Key listener in addition to the MenuBar Help
//		addKeyListener(_helpKey);
		
	}	// end of MainFrame constructor
		
	

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     * 								PUBLIC METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

/** Set the editFlag to mark changes since the last save
	 * @param editState if changes have not yet been saved
	 */
	public void setEditFlag(boolean editState)
	{
		_editFlag = editState;
	}

	
    /** 
     * Updates the mainframe window title with the appropriate context, e.g., name of the 
     * dungeon, which Guild the Hero is in, or the Tavern
     * from the overview's attribute panel.
     * 
     * @param name  or title to use
     */
    public void updateTitle(String name)
    {
    	setTitle(name);
    }

    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
     *                              PRIVATE METHODS
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
    
    /** Sets title, screen size, and closing actions for main frame background window */
    private void setupMainframe()
    {
        setTitle("Occupation Mainframe");
    
        // Set main frame size and properties. All components go onto this frame. 
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        //    Use the entire screen for placing components, save screensize for later
        USERWIN_WIDTH = screenSize.width/2;
        USERWIN_HEIGHT = screenSize.height/2;
        setSize(USERWIN_WIDTH, USERWIN_HEIGHT); 
//        setLocationByPlatform(true);                        // Operating System specific windowing
//        setExtendedState(Frame.MAXIMIZED_BOTH);
//        setResizable(true);
//        System.out.println(getBackground());
//        setBackground(Color.orange);
//        System.out.println(getBackground());
//        
//        // Show the MainFrame window
//        setVisible(true);
    }

	
} 	// end of MainFrame class

