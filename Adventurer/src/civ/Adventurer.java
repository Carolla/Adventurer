/**
 * Adventurer.java Copyright (c) 2018, Alan Cline. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */

package civ;

import java.awt.EventQueue;

import hic.Mainframe;
import pdc.Initialization;

/**
 * This Launcher class detects command line arguments, takes appropriate loading action,
 * initializes all Registries, and turns control over the Mainframe GUI.
 * 
 * @author Tim Armstrong
 * @version April 16, 2012 TAA // original <br>
 *          May 13, 2012 TAA // added new refresh flag <br>
 *          July 15, 2014 ABC // Enabled Quit menu option to call static quit() method and work
 *          similar to windowClosing event <br>
 *          July 21, 2014 ABC // removed environment variables, based everything off user's
 *          home directory <br>
 *          Aug 24, 2014 // renamed {@code exit} method to {@code quit} for consistency with
 *          menu <br>
 *          Sept 6, 2014 // ABC removed repeated getInstance calls from loop <br>
 *          Mar 19, 2018 // ABC added support for MVP proxy tests <br>
 */
public class Adventurer
{
  /**
   * Launcher class does not require a constructor--it has the {@code main} method.
   */
  public Adventurer()
  {}

  /**
   * Creates the main frame and passes control to it.
   * <UL>
   * <LI>Starts a separate Swing thread for the GUI's Swing Event Queue (SEQ), per Swing's
   * "strict single-thread rule". All Swing processing occurs from the single SEQ thread.
   * Unlike older versions of Java, Swing must now be invoked inside an Event Queue
   * {@code Runnable}. As <i>Core Java</i> (Volume 1, Horstmann &amp; Cornell, (c) 2008, p287)
   * states: "For now, you should simply consider it a magic incantation that is used to start
   * a Swing program."</LI>
   * <LI>The Event Scheduler runs a second thread concurrently to poll the
   * {@code CommandParser} for user command inputs.</LI>
   * <LI>Initializes the system by creating necessary registries and data files; on exit,
   * closes all registries.</LI>
   * </UL>
   * 
   * @param args unused command line arguments
   */
  public static void main(String[] args)
  {
    /** All Swing processing occurs from the single EventQueue thread. */
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run()
      {
        try {
          // Initializations and primary actions in MainActionCiv
          // new Initialization();
          new Mainframe();
        } catch (Exception e) {
          e.printStackTrace();
          System.exit(-1);
        }
      }
    });

  } // end of static main()

  /** Close all registries and shutdown the system */
  public static void approvedQuit()
  {
    System.exit(0);
  }


} // end of Adventurer class
