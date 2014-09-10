/**
 * MsgCtrl.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package mylib;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

/**
 * Selectively controls error and output messages, and can be placed into production code. All
 * messages to the console, normal or error, should use this class for output. This static class can
 * be accessed anywhere and wraps <code>System.out.println()</code> and
 * <code>System.err.println()</code>, allowing messages to be turned off during unit testing;
 * particularly useful during JUnit testing.
 * <P>
 * The overloaded <code>msgln(Object object, String msg)</code> and
 * <code>errMsgln(Object object, String msg)</code> will provide the name of the class from which
 * the method was called, providing a cleaner audit trail.
 * <P>
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT> Build 1.0 Jun 6, 2009 // original <DD> <DT> Build 1.1 May 30, 2010 // added Object
 *          parm overloads <DD>
 *          </DL>
 */

public class MsgCtrl
{
  /** Flag to suppress audit messages that might otherwise appear */
  static private boolean AUDIT_SUPPRESS = true;

  /** Flag to suppress error messages that might otherwise appear */
  static private boolean ERROR_SUPPRESS = false;


  // -------------------------------------------------------------------------------------------
  // PUBLIC STATIC METHODS
  // -------------------------------------------------------------------------------------------

  /**
   * Turn on Audit Messages to the console.
   * 
   * @param state if true, MsgCtrl audit messages will display
   */
  static public void auditMsgsOn(boolean state)
  {
    AUDIT_SUPPRESS = !state;
  }


  /**
   * Display a standard error message without newline; used typically for constructors or statics
   * where the <code>this</code> reference is not defined
   * 
   * @param msg error message to be displayed
   */
  static public void errMsg(String msg)
  {
    if (ERROR_SUPPRESS == false) {
      System.err.print(msg);
    }
  }


  /**
   * Display a standard error message, and its caller, without newline.
   * 
   * @param obj object that called this method; used to determine the class that is sending the
   *        message
   * @param msg error message normally to be displayed
   */
  static public void errMsg(Object obj, String msg)
  {
    if ((obj != null) && (ERROR_SUPPRESS == false)) {
      System.err.print("\n" + obj.getClass().getName());
      System.err.println(msg);
    }
  }


  /**
   * Display a standard error message with newline
   * 
   * @param msg error message to be displayed
   */
  static public void errMsgln(String msg)
  {
    if (ERROR_SUPPRESS == false) {
      System.err.println(msg);
    }
  }


  /**
   * Display a standard error message, and its caller, with newline
   * 
   * @param obj object that called this method; used to determine the class that is sending the
   *        message
   * @param msg error message normally to be displayed
   */
  static public void errMsgln(Object obj, String msg)
  {
    if ((obj != null) && (ERROR_SUPPRESS == false)) {
      System.err.print(obj.getClass().getName() + "\t");
      System.err.println(msg);
    }
  }


  /**
   * Turn on Error Messages to the console.
   * 
   * @param state if true, MsgCtrl error messages will display
   */
  static public void errorMsgsOn(boolean state)
  {
    ERROR_SUPPRESS = !state;
  }


  /**
   * Debug class to see event and source of it
   * 
   * @param event actionEvent pass by the SwignEventQueue into actionPerformed()
   */
  static public void traceEvent(ActionEvent event)
  {
    auditMsgsOn(true);
    msg("traceEvent(): ActionEvent invoked from ");
    msgln(event.getSource().getClass().getName());
    auditMsgsOn(false);
  }


  /**
   * Debug class to see event and source of it
   * 
   * @param event focusEvent passed by the SwingEventQueue into focusLost()
   */
  static public void traceEvent(FocusEvent event)
  {

    msg("traceEvent(): FocusEvent invoked from ");
    msgln(event.getSource().getClass().getName());
    // auditMsgsOn(false);
  }

  /**
   * Debug class to see event and source of it
   * 
   * @param key KeyEvent pass by the SwignEventQueue into actionPerformed()
   */
  static public void traceKeyEvent(KeyEvent key)
  {
    auditMsgsOn(true);
    msg(KeyEvent.getKeyText(key.getKeyCode()) + " key invoked from ");
    msgln(key.getSource().getClass().getName());
    auditMsgsOn(false);
  }


  /**
   * Display a standard user message, and its caller, without newline.
   * 
   * @param obj object that called this method; used to determine the class that is sending the
   *        message
   * @param msg error message normally to be displayed
   */
  static public void msg(Object obj, String msg)
  {
    if ((obj != null) && (AUDIT_SUPPRESS == false)) {
      System.out.print("\n" + obj.getClass().getName() + "\t");
      System.out.print(msg);
    }
  }


  /**
   * Display a standard user message without newline.
   * 
   * @param msg error message to be displayed
   */
  static public void msg(String msg)
  {
    if (AUDIT_SUPPRESS == false) {
      System.out.print(msg);
    }
  }


  /**
   * Display a standard user message with newline.
   * 
   * @param key error message to be displayed
   */
  static public void msgln(String key)
  {
    if (AUDIT_SUPPRESS == false) {
      System.out.println(key);
    }
  }


  /**
   * Display a standard user message, and its caller, with newline
   * 
   * @param obj object that called this method; used to determine the class that is sending the
   *        message
   * @param msg error message normally to be displayed
   */
  static public void msgln(Object obj, String msg)
  {
    if ((obj != null) && (AUDIT_SUPPRESS == false)) {
      System.out.print("\n" + obj.getClass().getName());
      System.out.println(msg);
    }
  }

  /**
   * Display the class and method name of currently executing method
   * 
   * @param obj object that called this method; used to determine the class that is sending the
   *        message
   */
  static public void where(Object obj)
  {
    if ((obj != null) && ((AUDIT_SUPPRESS == false) || (ERROR_SUPPRESS == false))) {
      String method = Thread.currentThread().getStackTrace()[2].getMethodName();
      System.out.println("\n" + obj.getClass().getName() + ":\t" + method);
    }
  }

} // end of MsgCtrl class

