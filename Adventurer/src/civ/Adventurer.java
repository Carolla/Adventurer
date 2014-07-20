/**
 * Adventurer.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package civ;

import hic.Mainframe;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import mylib.pdc.Registry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

/**
 * This Launcher class detects command line arguments, takes appropriate loading action, and
 * launches the mainframe when all requests are completed
 * 
 * @author Tim Armstrong
 * @version 1.0 April 16, 2012 TAA // original <br>
 *          1.1 May 13, 2012 TAA // added new refresh flag <br>
 *          1.2 July 15, 2014 ABC // Enabled Exit menu option to call static exit() method and work
 *          similar to windowClosing event <br>
 */
public class Adventurer
{
  /** Launcher class does not require a constructor--it has the <code>main</code> method. */
  private Adventurer()
  {}

  /**
   * Creates the main frame and passes control to it.
   * <UL>
   * <LI>Starts a separate Swing thread for the GUI's <code>Swing Event Queue</code> (SEQ), per
   * Swing's "strict single-thread rule". All Swing processing occurs from the single SEQ thread.
   * Unlike older versions of Java, Swing must now be invoked inside an EventQueue
   * <code>Runnable</code>. As <i>Core Java</i> (Volume 1, Horstmann & Cornell, (c) 2008, p287)
   * states: "For now, you should simply consider it a magic incantation that is used to start a
   * Swing program."</LI>
   * <LI>
   * The Event <code>Scheduler</code> runs a second thread concurrently to poll the
   * <code>CommandParser</code> for user command inputs.</LI>
   * <LI>
   * Initializes the system by creating necessary singletons, registries, and data files; on exit,
   * closes all registries.</LI>
   * </UL>
   * 
   * @param args unused command line arguments
   */
  public static void main(String[] args)
  {
    /** All Swing processing occurs from the single EventQueue thread. */
    EventQueue.invokeLater(new Runnable() {
      public void run()
      {
        try {
          initRegistries();
          final Mainframe frame = Mainframe.getInstance();
          frame.setVisible(true);
          frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
              closeRegistries();
              super.windowClosing(e);
            }
          });
        } catch (Exception e) {
          e.printStackTrace();
          System.exit(-1);
        }
      }
    });

  } // end of static main()


  /** Close all registries and shutdown the system */
  public static void exit()
  {
    closeRegistries();
    System.exit(1);
  }


  /**
   * Open all database Registries (singletons) for convenience and performance
   */
  private static void initRegistries()
  {
    for (RegKey key : RegKey.values()) {
      RegistryFactory.getRegistry(key);
    }
  }


  /**
   * Close all database Registries (singletons)
   */
  private static void closeRegistries()
  {
    for (RegKey key : RegKey.values()) {
      Registry reg = RegistryFactory.getRegistry(key);
      reg.closeRegistry();
    }
  }


  // ============================================================
  // Inner class for testing
  // ============================================================

  // public class MockLauncher
  // {
  // public MockLauncher()
  // {}
  //
  // public static void initRegistries()
  // {
  // Adventurer.this.initRegistries();
  // }
  //
  // }


} // end of Launcher class

