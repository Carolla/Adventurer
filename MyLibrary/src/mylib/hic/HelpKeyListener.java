/**
 * HelpKeyListener.java Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package mylib.hic;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import mylib.MsgCtrl;



/**
 * Creates a help key (F1) listener object for triggering text to the {@code HelpDialog}. If the
 * window is not open, the {@code keyPressed()} method will open it before displaying text.
 * 
 * @author Alan Cline
 * @version Dec 31 2008 // moved from inner class to here <br>
 *          Jan 22 2009 // made more generic for library; added owner ctor <br>
 *          Jul 28 2014 // activated this again for lastest <i>Adventurer</i> <br>
 */
public class HelpKeyListener extends KeyAdapter
{
  /** Contains help window reference to avoid collecting it during the {@code keyPressed()} method */
  private HelpDialog _helpDialog = null;
  /** Unique identifier used to retrieve the text from the help.xml file */
  private String _helpID = null;

  /**
   * Constructor fetches existing HelpDialog, else returns null object
   * 
   * @param owner the frame what will own the help dialog when it pops up
   * @param helpTag unique identifier used to retrieve the text from the help.xml file
   */
  public HelpKeyListener(String helpTag)
  {
    super();
    _helpDialog = HelpDialog.getInstance();
    if (_helpDialog == null) {
      MsgCtrl.errMsgln("HelpKeyListener ctor: Can't see instance of Help Dialog");
      return;
    }
    // Link this listener to its source object for help retrieval
    _helpID = helpTag;
  }


  /**
   * Display text associated with the widget in focus when the help key (F1) is pressed. <br>
   * NOTE: Keep this method small; it is called *every* keystroke at least once
   * 
   * @param key the key pressed by the user.
   */
  @Override
  public void keyPressed(KeyEvent key)
  {
    // Audit statement; comment out when not debugging
    MsgCtrl.traceKeyEvent(key);
    MsgCtrl.msgln("Help ID = " + _helpID);
    try {
      if (key.getKeyCode() == KeyEvent.VK_F1) {
        MsgCtrl.msgln("Help ID = " + _helpID);
        _helpDialog.setVisible(true);
        // _helpDialog .help(_helpID);
      }
    } catch (NullPointerException ex) {
      MsgCtrl.errMsgln("HelpKeyListener.keyPressed(): " + ex.getMessage());
    }
  }

} // end of HelpKeyListener class

