/**
 * IOPanelProxy.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import hic.IOPanelInterface;
import mylib.MsgCtrl;

/**
 * Replaces the IOPanel GUI object with this facade to simulate GUI-CommandParser interactions
 * 
 * @author Alan Cline
 * @version Feb 7, 2015 // original <br>
 *          Mar 39 2016 // added MsgCtrl.where(this) to locate when this proxy runs <br>
 */
public class IOPanelProxy implements IOPanelInterface
{
  /** Buffer for holding messages for auditing */
  static private String _msgOut;

  /** Default constructor */
  public IOPanelProxy()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
  }

  @Override
  public void displayText(String msg)
  {
    MsgCtrl.where(this);
    _msgOut = msg;
    MsgCtrl.msgln("\t" + msg);
  }

  @Override
  public void displayErrorText(String msg)
  {
    MsgCtrl.where(this);
    _msgOut = msg;
    MsgCtrl.errMsgln("\t" + _msgOut);
  }

  /**
   * Return last message out and clear buffer
   * 
   * @return whatever message was last intended for the GUI
   */
  public String msgOut()
  {
    MsgCtrl.where(this);
    return _msgOut;
  }


} // end of IOPanelProxy
