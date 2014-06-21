/**
 * MainFrame.java
 *
 * Copyright (c) 2004, Carolla Development, All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package rumorMillStuff.src.hic;

import rumorMillStuff.src.pdc.GameClock;
import rumorMillStuff.src.pdc.Hero;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


/**
 *	This class serves as the background frame for all GUI activities.
 * It contains the MainFrame class, the NotImplemented class, and the About Dialog. The MainFrame class also
 * creates the SplashInitWindow that displays until initialization processing is completed.
 *	@author Alan Cline 
 *  @version <DL>
 * <DT>1.0		Dec 10		2004	 		// original 	<DD>
 * <DT>2.0 	Dec 24 	2004			// Moved to Eclipse platform on Windows ME <DD>
 * <DT>3.0 	Feb 24 	2008			// Resued this to wrap around Rumor Inn program <DD>
 *  <DT>4.0	Mar 23 	2008			// Updated for SE 6.0, particularly to follow Swing's "strict single-thread rule."<DD>
 *  <DT>4.1	Mar 31 	2008			// Add command input window to user output.<DD>
 *  <DT>4.2	Apr 10 	2008			// Hook cmdWindow and output into the PDC engine.<DD>
 *  <DT>4.3	May 25 	2008			// Force text to display at bottom of scrollable window, not at the top. <DD>
 *  <DT>4.4 	Jul 1 		2008			// Final commenting for Javadoc compliance<DD>
 * </DL>
*/
@SuppressWarnings("serial")
public class MainFrame extends JFrame
{
	/** Number of rows in the user's output window */
	private final int USERWIN_HEIGHT = 40;
	/** Number of rows in the user's output window */
	private final int USERWIN_WIDTH = 105;
	/** Number of rows in the user's output window */
	private final int CMDWIN_WIDTH = USERWIN_WIDTH/3;
	/** Number of pixels on emty border spacing */
	private final int PADDING = 5;

	/** Status value for Hero's gold; updated periodically. */
	private JTextField _gold;
	/** Default label for status. */
	private String GOLD_MSG = " GOLD:   ";
	/** Units for Hero's gold; later, units will gp and sp (1 gp = 10sp). */
	private String GOLD_UNITS = " gp";
	
	/** Status value for time on game clock; updated each command. */
	private JTextField _time;
	/** Default label for time on game clock. */
	private String CLOCK_MSG = " TIME:  ";


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
		
	/** Reference to the singleton windowing system. */
	static private MainFrame _mainframe = null;
	
    /** Unused default constructor */
    public MainFrame() { }


    /**
	 * Constructor for main menu frame.
	 * Build the GUI frame for the splash screen and menus to hang from.
	 * 
	 * @param title	name to display in title border
	 */
	public MainFrame(String title)
	{
	    // Set title, screen size, and closing actions for main frame background window.
		setupMainframe(title);

		/** Create the remainder of the window system. */
	    // Create the status panel to show the gold and game clock.
	    final JPanel statusPanel = createStatusPanel();
	    getContentPane().add(statusPanel, BorderLayout.NORTH);
	   
	    // Create the user input window and add it to the top of the main frame
		final JScrollPane outputPanel = createOutputPanel();
	    getContentPane().add(outputPanel, BorderLayout.CENTER);
	    
		// Create the user input window and add it to the bottom of the main frame
		final JPanel inputPanel = createInputPanel();
	    getContentPane().add(inputPanel, BorderLayout.SOUTH);
	    
		// Create the menuing system and add to main frame
		createMenus();
		
		// Set the mainframe visible before the SplashInitWindow is shown. This starts a new thread for the windowing system.
		// Events are placed on Swing's Event Queue, from which all windowing events must originate.
		_mainframe = this;
			
	}	// end of MainFrame constructor
		
	
    /**
     * Allow the reference for the MainFrame windowing system to be shared. 
     */
    public static synchronized MainFrame getInstance()
    {
        if (_mainframe == null) {
        	System.err.println("Main Window system not yet created.");
        }
        return _mainframe;
    }

	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

    
	/** Close down the windowing system when user requests the quit command. */
	public void quit()
	{
		setVisible(false);
		System.exit(0);			// close all threads running
	}

	
	/** 
	 * Update the status panel with the Hero's current gold, and the current time on the game clock.
	 */
    public void updateStatus()
    {
    	Hero guy = Hero.createInstance();
    	_gold.setText(GOLD_MSG + guy.checkMoney() + GOLD_UNITS);
    	GameClock clock = GameClock.getInstance();
    	_time.setText(CLOCK_MSG + clock.getFormattedTime());
    }

	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PRIVATE METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

	/**
	 * Create the south panel input window.
     * It must provide data to the backend command parser
	 * @return the user input window
	 */
	private  JPanel createInputPanel()
	{
		// Create the command prompt label
	    JLabel cmdPrompt = new JLabel("  Command? ", JLabel.LEFT);

	    // Create the text field to collect the user's command
	    final JTextField cmdWin = new JTextField(CMDWIN_WIDTH);
	    cmdWin.setFocusable(true);				// set the focus to this field when starting up
	    
	    // Create the command input text area
		JPanel southPanel = new JPanel(new BorderLayout());
	    southPanel.add(cmdPrompt, BorderLayout.WEST);
	    southPanel.add(cmdWin, BorderLayout.CENTER);
	    southPanel.setBorder(new EmptyBorder(PADDING, PADDING, 10*PADDING, 10*PADDING));
	    
	    // Add function to send commands to command parser.
		cmdWin.addActionListener(new ActionListener()
			{	
				public void actionPerformed(ActionEvent event)
				{
					// Save the user's input to be retrieved by the command parser
					CommandParser cp = CommandParser.getInstance();
					cp.receiveCommand(cmdWin.getText());
					cmdWin.setText("");						// clear input from the command window
				}
			});
		// Return the input field and its label within its own panel
		return southPanel;
	}
		
	
	/**
	 * Create the menu system 
	 * @return the primary Jmenu bar
	 */
	private JMenuBar createMenus()
	{
	    // Add menus to the background frame
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
	
		// Define and populate the FILE menu
		JMenu fileMenu = new JMenu(" File ");
		menuBar.add(fileMenu);

		JMenuItem newItem = new JMenuItem("New");
		newItem.addActionListener(new NotImplementedAction("New"));
		fileMenu.add(newItem);
		
		// Put a SEPARATOR above the EXIT command
		fileMenu.add(new JSeparator(SwingConstants.HORIZONTAL));			

		// Add EXIT item and anonymous exit action listener to FILE menu
		fileMenu.add(new 	AbstractAction("Exit")
			{	
				public void actionPerformed(ActionEvent event)
				{
				    Dgn.auditMsg("Closing main menu window and exiting system."); 
			        System.exit(0);
				}
			});
			
			// Define and populate the HELP menu
			JMenu helpMenu = new JMenu("Help");
			menuBar.add(helpMenu);
	
			// Attach a new Help window to the Help menu item
			JMenuItem helpItem = new JMenuItem("Help");
			helpItem.addActionListener(new ActionListener()
			{	
				public void actionPerformed(ActionEvent evt)
				{
					CommandParser.getInstance().help();
				}
			}); 
			
			// Add the Help window to the menubar
			helpMenu.add(helpItem);
			
			// Attach a new Dialog window to the About menu item
			JMenuItem aboutItem = new JMenuItem("About...");
			aboutItem.addActionListener(new ActionListener()
				{	
					public void actionPerformed(ActionEvent evt)
					{
						JDialog dialog = new AboutDialog(MainFrame.this);		// attach the dialog to the main frame
						dialog.setVisible(true);
					}
				}); 

			// Add the About box to the menu bar
			helpMenu.add(aboutItem);

			return menuBar;
		}
	
	
	/**
	 * Create the north panel output window: scollable, non-editable output area
	 * @return the primary user output window
	 */
	private JScrollPane createOutputPanel()
	{
		final JTextArea output = new JTextArea(USERWIN_HEIGHT, USERWIN_WIDTH);
	    output.setEditable(false);
	    output.setLineWrap(true);
	    output.setFocusable(false);
	    
	    // Add the text area into a scroll pane
	    output.setBackground(Color.LIGHT_GRAY);			// just for fun, make the background non-white
	    output.setForeground(Color.BLACK);						// text is colored with the setForeground statement
	    
	    //	 Redirect the System stdout and stderr to the user output window
	    System.setOut(redirectIO(output));
	    System.setErr(redirectIO(output));
	    //	 Audit trail and debug messages also go to outpout window temporarily
	    if (Dgn.AUDIT == true) {
	    	System.setErr(redirectIO(output));
	    }
	    if (Dgn.DEBUG == true) {
	    	System.setErr(redirectIO(output));
	    }
	    
	    // Make text output scrollable-savvy
	    JScrollPane scrollPane = new JScrollPane(output);
	    scrollPane.setPreferredSize(new Dimension(USERWIN_HEIGHT, USERWIN_WIDTH));
	    scrollPane.setViewportView(output);

	    return scrollPane;
	}
	
	
    /**
     * Create the status panel that shows gold pieces and game clock.
     * @return the status panel
     */
    private JPanel createStatusPanel()
    {
    	// Create status panel
		JPanel status = new JPanel(new BorderLayout());
		Color bkground = status.getBackground();
		
		// Create gold label with placeholder
		// JLabel goldLabel = new JLabel("Gold: gg.ss", SwingConstants.LEFT);
		_gold = new JTextField();
	    _gold.setBackground(bkground);
		_gold.setText(GOLD_MSG); 
		_gold.setFocusable(false);
		_gold.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
		
		// Create clock label with placeholder
		// JLabel clockLabel = new JLabel("Time: hh:mm MM", SwingConstants.RIGHT);
		_time = new JTextField();
		_time.setText(CLOCK_MSG);
	    _time.setBackground(bkground);
		_time.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
		_time.setFocusable(false);
		
		// Add the subpanels to the status panel
		status.add(_gold, BorderLayout.WEST);
		status.add(_time, BorderLayout.EAST);
	    status.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

		updateStatus();
		return status;
    }

    
	/** 
     * Redirect System out messages (user output) to the window area.
     * Define a PrintStream that sends its bytes to the output text area
	 * @param outWin			the text area window receiving the output stream; 'final' is required because it is referenced
	 * 				from inside an inner class
	 * 	if both flags are set, both kinds of messages go to the same audit window with a different text color.
	 */
	private PrintStream redirectIO(final JTextArea outWin)
	{	
		PrintStream op = new PrintStream(new OutputStream()
		{
			public void write(int b) {} // never called
			public void write(byte[] b, int off, int len)
			{
				outWin.append(new String(b, off, len));
				// Ensure that the text scrolls as new text is appended
				outWin.setCaretPosition(outWin.getDocument().getLength());
			}
		});

		return op;
	}


    /** 
     * Sets title, screen size, and closing actions for main frame background window 
     * @param mainTitle	name to display in title border
     */
	private void setupMainframe(String mainTitle)
	{
		setTitle(mainTitle + " Command Window");
				
		// Set default X (upper left or right corner) to exit app
		addWindowListener(new WindowAdapter() 
		    { 
				public void windowClosing(WindowEvent e)
				{ 
					Dgn.auditMsg("Closing main window and closing program.");
				}
		    });

		// Set main frame size and properties. All components go onto this frame. 
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		//	  Use the entire screen for placing components
		setSize(screenSize.width, screenSize.height); 
		setLocationByPlatform(true);						// Operating System specific windowing
		setResizable(false);
	}

			
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								INNER CLASS 1
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
	
/** 
 * Inner Class: A generic dialog box for not implemented menu items. 
 */
private class NotImplementedAction extends AbstractAction
{
	/** 
	 * This constructor call's the parent's constructor.
	 * @param name  the command (menu item) that is not yet implemented
	 */
	public NotImplementedAction(String name)
	{
		super(name);
	}
	
	/** 
	* 	Display the Not Implemented message when an item is selected.
	*	@param evt	ActionEvent for unimplemented selections 
	*/
	public void actionPerformed(ActionEvent evt)
	{
		Toolkit kit = Toolkit.getDefaultToolkit();
		kit.beep();		
		String cmdName = getValue(Action.NAME).toString(); 
		JOptionPane.showMessageDialog(null, 	"The " + cmdName + " menu command is not yet implemented.", 
			cmdName, JOptionPane.INFORMATION_MESSAGE);
	}

}	// end of NotImplementedAction inner class


/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								INNER CLASS 2
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  

/** 
*	Inner Class: A modal window to display author credits
*/
class AboutDialog extends JDialog
{
	final int ABOUT_WIDTH = 460;
	final int ABOUT_HEIGHT = 250;
		
	/**
	 * Display the About box in a dialog window.
	 * @param owner	the main menu frame that supports this dialog.
	 */
	public AboutDialog(JFrame owner)
	{
		super(owner, "About..", true);
		Container contentPane = getContentPane();
		
		// Get screensize and set location to center
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screensize = kit.getScreenSize();
		int screenHeight = screensize.height;
		int screenWidth = screensize.width;
		setLocation(screenWidth/4, screenHeight/4);
		setSize(ABOUT_WIDTH, ABOUT_HEIGHT);

		// Build a JLabel to place text on the contentPane
		final String title =	"<H1><I> &nbsp &nbsp &nbsp &nbsp The Rumor Inn </I></H1>";
		final String byline =	"<H2> &nbsp &nbsp Designed and written <br> &nbsp &nbsp by Alan Cline </H2>";
		
		contentPane.add(new JLabel(
						"<HTML>" + title + "<HR>" + byline + 
						"<P> &nbsp&nbsp&nbsp "+ Dgn.COPYRIGHT + "</HTML>"),
						BorderLayout.CENTER);

		// Add the OK button with listener
		JPanel okPanel = new JPanel();
		JButton okBtn = new JButton("OK");
		okBtn.addActionListener(new 	ActionListener()
			{
				public void actionPerformed(ActionEvent event)
				{
					setVisible(false);
				}
			});
			
		okPanel.add(okBtn);
		contentPane.add(okPanel, BorderLayout.SOUTH);
	}

}	// end AboutDialog inner class


}	// end MainFrame outer class

