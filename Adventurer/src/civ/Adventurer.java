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
 * This Launcher class detects command line arguments, takes appropriate loading action, initializes
 * all singleton Registries, and turns control over the Mainframe GUI singleton.
 * 
 * @author Tim Armstrong
 * @version April 16, 2012 TAA // original <br>
 *          May 13, 2012 TAA // added new refresh flag <br>
 *          July 15, 2014 ABC // Enabled Quit menu option to call static quit() method and work
 *          similar to windowClosing event <br>
 *          July 21, 2014 ABC // removed environment variables, based everything off user's home
 *          directory <br>
 *          Aug 24, 2014 // renamed {@code exit} method to {@code quit} for consistency with menu <br>
 *          Sept 6, 2014 // ABC removed repeated getInstance calls from loop <br>
 */
public class Adventurer
{
  /** Quick reference to avoid repeated calls to {@code getInstance} */
  static private RegistryFactory _rf;

  /** Launcher class does not require a constructor--it has the {@code main} method. */
  public Adventurer()
  {}


  /**
   * Creates the main frame and passes control to it.
   * <UL>
   * <LI>Starts a separate Swing thread for the GUI's {@code Swing Event Queue} (SEQ), per Swing's
   * "strict single-thread rule". All Swing processing occurs from the single SEQ thread. Unlike
   * older versions of Java, Swing must now be invoked inside an EventQueue {@code Runnable}. As
   * <i>Core Java</i> (Volume 1, Horstmann & Cornell, (c) 2008, p287) states: "For now, you should
   * simply consider it a magic incantation that is used to start a Swing program."</LI>
   * <LI>
   * The Event {@code Scheduler} runs a second thread concurrently to poll the {@code CommandParser}
   * for user command inputs.</LI>
   * <LI>
   * Initializes the system by creating necessary singletons, registries, and data files; on exit,
   * closes all registries.</LI>
   * </UL>
   * 
   * @param args unused command line arguments
   */
  public static void main(String[] args)
  {
      final Adventurer adv = new Adventurer();
      
    /** All Swing processing occurs from the single EventQueue thread. */
    EventQueue.invokeLater(new Runnable() {
      public void run()
      {
        try {
          adv.initRegistries();
//          dumpAllRegistries();
          final Mainframe frame = Mainframe.getInstance();
          frame.setVisible(true);
          frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
              super.windowClosing(e);
              adv.approvedQuit();
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
  public void approvedQuit()
  {
    closeRegistries();
    System.exit(0);
  }


  /**
   * Close all database Registries (singletons)
   */
  private static void closeRegistries()
  {
    _rf.closeAllRegistries();
  }

//  /**
//   * Dump the contents of all the registries for viewing
//   */
//  static private void dumpAllRegistries()
//  {
//    _rf = RegistryFactory.getInstance();
//    for (RegKey key : RegKey.values()) {
//      Registry reg = _rf.getRegistry(key);
//      List<IRegistryElement> reglist = reg.getAll();
//      System.out.print("Registry " + key.name());
//      System.out.println("\t" + reglist.size() + " elements");
//      System.out.println("\t" + reglist.toString());
//    }
//  }

  // ============================================================
  // Inner class for testing
  // ============================================================

  /**
   * Open all database Registries (singletons) for convenience and performance
   */
  private void initRegistries()
  {
    _rf = RegistryFactory.getInstance();
    for (RegKey key : RegKey.values()) {
      _rf.getRegistry(key);
    }
    // Check proper initialization, else terminate program
    if (verifyRegistries() == false) {
      approvedQuit();
    }
  }


  /**
   * Validate the creation of all the registries for default sizes. Later, this method will have to
   * be modified for any new material added beyond the defaults.
   */
  static private boolean verifyRegistries()
  {
    boolean retval = true;
    // Expected size of each of the registries
    _rf = RegistryFactory.getInstance();
    for (RegKey key : RegKey.values()) {
      Registry reg = _rf.getRegistry(key);
      int defaultSize = key.getDefaultSize();
      int foundSize = reg.getNbrElements();
      // Check if the registries are not initialized properly
      if (foundSize != defaultSize) {
        System.err.println("Registry " + reg.toString()
            + " has the wrong number of default elements");
        System.err.println("\tExpected " + defaultSize + "; found " + foundSize);
        retval = false;
      }
    }
    return retval;
  }

  /** Inner class for testing {@code Adventurer} launcher */
  public class MockAdventurer
  {
    public MockAdventurer()
    {}

    public void initRegistries()
    {
      this.initRegistries();
    }

    public void closeRegistries()
    {
      this.closeRegistries();
    }


  } // end of MockAdventurer inner class


} // end of Adventurer class

