/**
 * NotImplementedAction.java
 * Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package mylib.hic;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

/**
 * Displays a generic dialog window message to remind me that certain user interface 
 * functionality (Human Interface Component) is not yet completed. 
 * Calls <code>JOptionPane</code> to display the message.  
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Aug 31, 2008   // original <DD>
 * </DL>
 */
@SuppressWarnings("serial")
public class NotImplementedAction extends AbstractAction
{
	/** 
	 * Call's the abstract parent's constructor.
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
		JOptionPane.showMessageDialog(null, 	
						"The " + cmdName + " command is not yet implemented.", 
			cmdName, JOptionPane.INFORMATION_MESSAGE);
	}

}	// end of NotImplementedAction class

