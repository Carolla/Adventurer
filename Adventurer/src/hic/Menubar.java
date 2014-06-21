
package hic;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import mylib.MsgCtrl;
import mylib.hic.NotImplementedAction;
import civ.AdvHelpCiv;

/** Contains all the actions available from the menubar. This class uses the AdvMainframeCiv as
 * its Civ.
 *
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		Dec 8, 2013   // original <DD>
 * </DL>
 */
@SuppressWarnings("serial")
public class Menubar extends JMenuBar
{
    public Menubar()
    {
        this.setBackground(Color.LIGHT_GRAY);
        populateAdventureMenu();
        populateHeroMenu();
        populateEnterMenu();
        populateHelpMenu();
    }


    private void populateAdventureMenu()
    {
        // Populate the Adventure menu
        JMenu mnAdventure = new JMenu("Adventure");
        this.add(mnAdventure);
        mnAdventure.add(createMenuItemWithNoAction("New"));
        mnAdventure.add(createMenuItemWithNoAction("Open"));
        mnAdventure.add(createMenuItemWithNoAction("Close"));
        mnAdventure.add(createMenuItemWithNoAction("Save"));
        mnAdventure.add(createMenuItemWithNoAction("Save As..."));
        mnAdventure.add(new JMenuItem("---"));
        mnAdventure.add(createMenuItemWithAction("Exit", new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        }));
    }


    private JMenuItem createMenuItemWithAction(String string, ActionListener exitAction)
    {
//        return new JMenuItem();
        JMenuItem item = new JMenuItem(string);
        item.addActionListener(exitAction);
        return item;
    }


    /**
     * Create a menu item that calls the Not Implemented action listener
     * 
     * @param string the name of the menu item
     * @return a menu item
     */
    private JMenuItem createMenuItemWithNoAction(String string)
    {
        JMenuItem item = new JMenuItem(string);
        item.addActionListener(new NotImplementedAction(string));
        return item;
    }


    private void populateHeroMenu()
    {
        // Populate the Hero menu
        JMenu mnHeroes = new JMenu("Heroes");
        this.add(mnHeroes);
        mnHeroes.add(createMenuItemWithNoAction("Summon Hero"));
        mnHeroes.add(createMenuItemWithNoAction("Save Hero"));
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
                // Create the civ that displays the help text
                AdvHelpCiv helpCiv = AdvHelpCiv.getInstance();
                helpCiv.showHelp("INSTRUCTIONS");
                // HelpDialog helpDlg = HelpDialog.getInstance();
                // helpDlg.showHelp("Mainframe");
            }
        });
        mnHelp.add(mntmHelp);

        // Setup About Item action
        JMenuItem mntmAbout = new JMenuItem("About...");
        mntmAbout.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                // Audit statement
                MsgCtrl.traceEvent(evt);
                // Attach the (inner class) dialog to the main frame
                JDialog dialog = new AboutDialog((JFrame) getParent().getParent().getParent());
                dialog.setVisible(true);
            }
        });
        mnHelp.add(mntmAbout);
    }


    private void populateEnterMenu()
    {
        // Populate the Enter menu
        JMenu mnEnter = new JMenu("Enter");
        this.add(mnEnter);
        mnEnter.add(createMenuItemWithNoAction("Registrar"));
        mnEnter.add(createMenuItemWithNoAction("Inn"));
        mnEnter.add(createMenuItemWithNoAction("Store"));
        mnEnter.add(createMenuItemWithNoAction("Arena"));
    }
}
