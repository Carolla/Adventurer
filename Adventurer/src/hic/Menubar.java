
package hic;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import civ.MainframeCiv;

/**
 * Contains all the actions available from the menubar. This class uses the AdvMainframeCiv as its
 * Civ.
 * 
 * @author Alan Cline
 * @version Dec 8, 2013 // original <br>
 *          Jul 31 2014 // connected Help option to HelpDialog; disabled unimplemented options <br>
 */
@SuppressWarnings("serial")
public class Menubar extends JMenuBar
{

  private MainframeInterface _mainframe;
  private MainframeCiv _mfCiv;


// ============================================================
  // Constructor and constructor helpers
  // ============================================================

  public Menubar(MainframeInterface mainframe, MainframeCiv mfCiv)
  {
      _mainframe = mainframe;
      _mfCiv = mfCiv;
      doConstructorWork();
  }


    public void doConstructorWork()
    {
        setBackground(Color.LIGHT_GRAY);
        populateAdventureMenu();
        populateHelpMenu();
    }


  // ============================================================
  // Public Methods
  // ============================================================


  // ============================================================
  // Private Methods
  // ============================================================

  private void populateAdventureMenu()
  {
    // Populate the Adventure menu
    JMenu mnAdventure = new JMenu("Adventure");
    this.add(mnAdventure);
    mnAdventure.add(createMenuItemWithAction("Quit", new ActionListener() {
      public void actionPerformed(ActionEvent e)
      {
          _mfCiv.quit();
      }
    }));
  }



  /** Populate the Help menu with the Help and About options */
  private void populateHelpMenu()
  {
    // Populate the Help menu with menu Options
    JMenu mnHelp = new JMenu("Help");
    add(mnHelp);

    // Setup About Help action
    JMenuItem mntmHelp = new JMenuItem("Help");
    mntmHelp.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
          _mainframe.showHelp();
      }
    });
    mnHelp.add(mntmHelp);

    // Setup About Item action
    JMenuItem mntmAbout = new JMenuItem("About...");
    mntmAbout.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        JDialog dialog = new AboutDialog((JFrame) getParent().getParent().getParent());
        dialog.setVisible(true);
      }
    });
    mnHelp.add(mntmAbout);
  }


  private JMenuItem createMenuItemWithAction(String string, ActionListener exitAction)
  {
    JMenuItem item = new JMenuItem(string);
    item.addActionListener(exitAction);
    return item;
  }


//  /**
//   * Create a menu item that is disabled--until it is implemented
//   * 
//   * @param string the name of the menu item
//   * @return a menu item
//   */
//  private JMenuItem createMenuItemWithNoAction(String string)
//  {
//    JMenuItem item = new JMenuItem(string);
//    item.setEnabled(false);
//    return item;
//  }


  // ============================================================
  // Deprecated Methods (may be used later)
  // ============================================================

  // private void populateEnterMenu()
  // {
  // // Populate the Enter menu
  // JMenu mnEnter = new JMenu("Enter");
  // this.add(mnEnter);
  // mnEnter.add(createMenuItemWithNoAction("Registrar"));
  // mnEnter.add(createMenuItemWithNoAction("Arena"));
  // mnEnter.add(new JMenuItem("---"));
  // mnEnter.add(createMenuItemWithNoAction("Inn"));
  // mnEnter.add(createMenuItemWithNoAction("Store"));
  // mnEnter.add(createMenuItemWithNoAction("Bank"));
  // mnEnter.add(createMenuItemWithNoAction("Jail"));
  // mnEnter.add(createMenuItemWithNoAction("Guild"));
  // }



  // private void populateHeroMenu()
  // {
  // // Populate the Hero menu
  // JMenu mnHeroes = new JMenu("Heroes");
  // this.add(mnHeroes);
  // mnHeroes.add(createMenuItemWithNoAction("Summon Hero"));
  // mnHeroes.add(createMenuItemWithNoAction("Save Hero"));
  // }

} // end of Menubar class
