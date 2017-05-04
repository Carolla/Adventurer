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
import java.util.ArrayList;
import java.util.Map;

/**
 * Selectively controls error and output messages, and can be placed into production code. All
 * messages to the console, normal or error, should use this class for output. This static class can
 * be accessed anywhere and wraps {@code System.out.println()} and {@code System.err.println()},
 * allowing messages to be turned off during unit testing; particularly useful during JUnit testing.
 * <P>
 * The overloaded {@code msgln(Object object, String msg)} and
 * {@code errMsgln(Object object, String msg)} will provide the name of the class from which the
 * method was called, providing a cleaner audit trail.
 * <P>
 * 
 * @author Alan Cline
 * @version Jun 6, 2009 // original <br>
 *          May 30, 2010 // added Object parm overloads <br>
 *          Feb 21, 2015 // added getState() <br>
 *          Apr 30, 2017 // added methods to check state of SUPPRESS states <br>
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
    * Display an error message to the console. Error tracking must be set using
    * {@code MsgCtrl.errorMsgsOn()}
    *
    * @param msg message to display
    */
   static public void auditErrorMsg(String msg)
   {
      if (MsgCtrl.isErrorOn()) {
         System.err.println(msg);
      }
   }


   /**
    * Display a message to the console for audit trail. Audit state must be set using
    * {@code MsgCtrl.auditMsgsOn()}
    *
    * @param msg message to display
    */
   static public void auditMsg(String msg)
   {
      if (MsgCtrl.isAuditOn()) {
         System.out.println(msg);
      }
   }


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
    * Send a list to the console as audit trail. Audit state must be set using
    * {@code MsgCtrl.auditMsgsOn()}
    * 
    * @param msg message to be printed above list dump
    * @param alist some list to be printed
    */
   static public void auditPrintList(String msg, ArrayList<String> alist)
   {
      if (MsgCtrl.isAuditOn()) {
         System.out.println("\n" + msg);
         for (String s : alist) {
            System.out.println("\t" + s);
         }
      }
   }


   /**
    * Send map entries to the console as audit trail. Audit state must be set using
    * {@code MsgCtrl.auditMsgsOn()}
    * 
    * @param msg message to be printed above list dump
    * @param amap some map to be printed as key, value
    */
   static public void auditPrintMap(String msg, Map<String, String> amap)
   {
      if (MsgCtrl.isAuditOn()) {
         System.out.println("\n" + msg);
         for (String key : amap.keySet()) {
            System.out.println("\t" + key + "\t \\\\  " + amap.get(key));
         }
      }
   }


   /**
    * Display a standard error message without newline; used typically for constructors or statics
    * where the {@code this} reference is not defined
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
    *           message
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
    *           message
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
    * Returns true if the audit flag is on
    */
   static public boolean isAuditOn()
   {
      return !AUDIT_SUPPRESS;
   }

   
   /**
    * Returns true if the error msg flag is on
    */
   static public boolean isErrorOn()
   {
      return !ERROR_SUPPRESS;
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
    *           message
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
    *           message
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
    *           message
    */
   static public void where(Object obj)
   {
      if ((obj != null) && ((AUDIT_SUPPRESS == false) || (ERROR_SUPPRESS == false))) {
         String method = Thread.currentThread().getStackTrace()[2].getMethodName();
         System.out.println("\n" + obj.getClass().getName() + ":\t" + method);
      }
   }

} // end of MsgCtrl class

