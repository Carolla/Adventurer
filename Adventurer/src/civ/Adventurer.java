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

import mylib.ApplicationException;
import mylib.pdc.Registry;
import pdc.registry.AdvRegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

/**
 * This Launcher class detects command line arguments, takes appropriate loading action, and
 * launches the mainframe when all requests are completed
 * 
 * @author Tim Armstrong
 * @version <DL>
 *          <DT>Build 1.0 April 16, 2012 TAA // original
 *          <DD>
 *          <DT>Build 1.1 May 13, 2012 TAA // added new refresh flag
 *          <DD>
 *          </DL>
 */
public class Adventurer
{

  /**
   * Creates the main frame and passes control to it; starts a separate Swing thread for the GUI's
   * <code>Swing Event Queue</code> (SEQ), per SE 6.0 and Swing's "strict single-thread rule". All
   * Swing processing occurs from the single SEQ thread. Unlike older versions of Java, Swing must
   * now be invoked inside an EventQueue <code>Runnable</code>. As <i>Core Java</i> (Volume 1,
   * Horstmann & Cornell, (c) 2008, p287) states: "For now, you should simply consider it a magic
   * incantation that is used to start a Swing program."
   * 
   * Also initializes the system by creating necessary singletons and data files.
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
              for (RegKey key : RegKey.values()) {
                Registry reg = AdvRegistryFactory.getRegistry(key);
                reg.closeRegistry();
              }
              super.windowClosing(e);
            }
          });
        } catch (Exception e) {
          e.printStackTrace();
          System.exit(0);
        }
      }
    });
    
  } // end of static main()



  /**
   * Open all database Registries (singletons) for convenience and performance
   * 
   * @throws ApplicationException if a registry cannot be opened
   */
  private static void initRegistries()
  {
    for (RegKey key : RegKey.values()) {
      AdvRegistryFactory.getRegistry(key);
    }
  }


} // end of Launcher class

