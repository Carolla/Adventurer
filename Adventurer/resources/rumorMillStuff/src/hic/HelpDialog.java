/**
 * HelpDialog.java
 * Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package rumorMillStuff.src.hic;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *	A non-modal singleton window to display command descriptions.
 * This window has a lock so that once help messages are written, no further information can 
 * be written to it. If the <code>HelpDialog</code> is called multiple times, the help text is 
 * not written multiple times to the window.
 * 
 * @author Alan Cline
 * @version <DL>
 * <DT>1.0		May 30 2008		// original <DD>
 * <DT>1.1		Jun 4	 2008		// added lock() and override method append() to use the lock <DD>
 * <DT>1.2 	Jul 1 	2008		// Final commenting for Javadoc compliance<DD>
 * </DL>
 */
@SuppressWarnings("serial")
public class HelpDialog extends JDialog
{
		/** Position this window this number of pixels down from the top of the frame */
		private final int DOWN_FROM_TOP = 110;		

		/** Allow (false) or disallow (true) text to be written to the window; defaults to true to
		 *	remind programmer to unlock it before writing, and then relock it if desired. */
		private boolean _LOCK = false;							
		
		/** Common text area for writing */
		private JTextArea _helpArea = null;

		
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								CONSTRUCTOR(S) AND RELATED METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  	
	
		/** <code>HelpDialog</code> reference to ensure a singleton. */ 
		private static HelpDialog _helpDlg = null;
		
		/** 
		 * Create the singleton HelpDalog if it doesn't exist
		 *  
		 * @param owner	the window to which this JDialog is attached 
		 * @return this <code>HelpDialog</code>
		 */
	    public static synchronized HelpDialog createInstance(JFrame owner)
	    {
	        if (_helpDlg == null) {
	            _helpDlg = new HelpDialog(owner);
	        }
	        return _helpDlg;
	    }
	    
		/**
		 * Return an already existing HelpDialog 
		 * @return this help window 
		 */
	    public static synchronized HelpDialog getInstance()
	    {
	        if (_helpDlg == null) {
	            Dgn.debugMsg("The help window does not yet exist.");
	        }
	         return _helpDlg;
	    }
	    
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PUBLIC METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
	
		/** 
		 * Overrides the<code> JTextArea</code> append method so that 
		 * <code>HelpDialog</code>can lock data from being written to it. 
		 * If LOCK is true, allow no more writing to this window
		 *
		 * @param msg		Whatever text is to be written in the window
		 */
		public void append(String msg)
		{
			if (_LOCK == false) {
				_helpArea.append(msg);
			}
		}

		/** Sets a lock on the <code>append()</code> method so that once it is written and 
		 * displayed, multiple help messages aren't written to the same window. The caller must 
		 * set or unset the lock.
		 * 
		 * @param lockStatus		true to lock text (default); false to unlock text
		 */
		public void lockText(boolean lockStatus)
		{
			_LOCK = lockStatus;
		}
		
	
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 * 								PRIVATE METHODS
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */  
	    		
		/**
		 * Display the Help box in a resizable, non-modal dialog window.
		 * @param owner	the main menu frame that supports this dialog.
		 */
		private HelpDialog(JFrame owner)
		{
			super(owner, "Command Help", true);
			Container contentPane = getContentPane();
			
			// Set main frame size and properties. All components go onto this frame. 
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screenSize = kit.getScreenSize();
			int scnWidth = screenSize.width;
			int scnHeight = screenSize.height;

			// Set to some default size until the client customizes it
			 _helpArea = new JTextArea(scnWidth/2, scnHeight/2);
			setLocation(scnWidth/2, DOWN_FROM_TOP);
		    _helpArea.setEditable(false);
		    _helpArea.setLineWrap(true);
		    setModal(false);
			contentPane.add(_helpArea);
		    
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
			
			// Make appear to set some of the windows properties 
			setVisible(true);
		}

		
}	// end HelpDialog class

