/**
 * HelpDialog.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package mylib.hic;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;


/**
 * Creates a non-modal singleton window to display help information. This object has only one real
 * interface method:
 * <p>
 * ==>&nbsp&nbsp&nbsp {@code help(String helpID)} to display the text associated with the Help ID
 * when the Help key is pressed (or menu item selected).
 * <p>
 * The text displayed depends on the focus object, and changes accordingly. This window has a lock
 * so that help messages are written only once even if the {@code HelpDialog} is called multiple
 * times.
 * <p>
 * The first time {@code HelpDialog} is opened (i.e., the singleton is created), it creates the PDC
 * {@code HelpEngine} to coordinate between this HIC class and the {@code HelpReader} DMC class.
 * <p>
 * Implementation Note: Applications may hook into the Help library objects by defining an
 * actionListener that calls the {@code HelpDialog} constructor in its {@code actionPerformed()}
 * method. The application must pass a directory name containing the help files for the application,
 * and the name of the XML text file. The {@code HelpEngine} will do all the rest: It will create
 * the {@code HelpReader} that will convert the XML text file to a binary indexed random access file
 * (named after the xml filename, but with a <i>.hlp</i> extension), place it in the target
 * directory, and manage the retrieval of help text based on the helpID requested.
 * 
 * @author Alan Cline
 * @version Nov 22 2008 // adapted from older program <<br>
 *          Dec 13 2008 // adapted for this program <br>
 *          July 9 2009 // refactored to make more application-independent <br>
 *          Aug 18 2009 // minor revisions to accommodate other class changes <br>
 *          Nov 23 2013 // converted from XML input files to Registry database <br>
 *          Jul 30 2014 // replaced registry with IHelpText interface <br>
 */
public class HelpDialog extends JDialog
{
  /** Internal reference of this singleton for clients' use. Must be static for constructor. */
  static private HelpDialog _helpDlg = null;

  /** Position this window this number of pixels down from the top of the frame */
  private final int DOWN_FROM_TOP = 110;
  /** End of text msg since text can scroll in the help window */
  private final String EOT = "\n\n--- end of Help\n";

  /**
   * Allow (false) or disallow (true) text to be written to the window; defaults to {@code true} to
   * remind programmer to unlock it before writing, and then relock it if desired.
   */
  private boolean _lockflag = true;

  /** Common text area for writing */
  private JTextArea _helpArea = null;


  // ==========================================================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ==========================================================================================

  /**
   * Create the singleton HelpDialog if it doesn't already exist; pass the parms on to the private
   * constructor. HelpEngine expects a Registry to hold the help text
   * 
   * @param owner the main menu frame that supports this dialog.
   * @param helpciv the civ that supports the help function that displays to ths dialog
   */
  public static HelpDialog getInstance(JFrame owner)
  {
    if (_helpDlg == null) {
      _helpDlg = new HelpDialog(owner);
    }
    return _helpDlg;
  }


  /**
   * Create the singleton HelpDialog as a resizable, non-modal dialog window.
   * 
   * @param owner the main menu frame that supports this dialog.
   */
  private HelpDialog(JFrame owner)
  {
    // Call the base class with no title
    super(owner, null, true);

    Container contentPane = getContentPane();
    // Set main frame size and properties. All components go onto this frame.
    Toolkit kit = Toolkit.getDefaultToolkit();
    Dimension screenSize = kit.getScreenSize();
    int scnWidth = screenSize.width;
    int scnHeight = screenSize.height;

    // Set to some default size until the client customizes it
    _helpArea = new JTextArea(scnWidth / 2, scnHeight / 2);
    setLocation(scnWidth / 2, DOWN_FROM_TOP);
    double helpWidth = scnWidth * .45;
    double helpHt = scnHeight * .8;
    setSize((int) helpWidth, (int) helpHt);
    _helpArea.setEditable(false); // turn off user-editing
    _helpArea.setLineWrap(true); // wrap text within window, and...
    _helpArea.setWrapStyleWord(true); // ...wrap on word boundaries
    _helpArea.setMargin(new Insets(5, 5, 5, 5)); // set margin space around text

    // Create a vertical-only scrolling text area to collect the dungeon description
    final JScrollPane helpSP = new JScrollPane(_helpArea);
    helpSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    helpSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    // Add the scrolling helpwindow to the frame
    contentPane.add(helpSP);

    // Add the OK button with listener
    JPanel okPanel = new JPanel();
    JButton okBtn = new JButton("OK");
    okBtn.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        setVisible(false);
      }
    });

    okPanel.add(okBtn);
    contentPane.add(okPanel, BorderLayout.SOUTH);

    // Don't make the help window visible until it is requested
    setModal(false);
    setVisible(false);

    // Save this singleton for later reference
    _helpDlg = this;

  } // end of HelpDialog constructor


  /**
   * Return the globally-available static Help Dialog reference
   * 
   * @return HelpDialog reference
   */
  static public HelpDialog getInstance()
  {
    return HelpDialog._helpDlg;
  }


  // ==========================================================================================
  // PUBLIC METHODS
  // ==========================================================================================

  /**
   * Retrieves and displays help text from the Help file for the given key associated with the
   * widget for which the Help was requested, either by the F1 key or the Help MenuItem. The
   * {@code HelpDialog} window is non-modal and the text within is replaced with the appropriate
   * text for the widget under focus. <br>
   * If there is no associated widget, the help window is displayed for the owner (frame or panel),
   * and should contain general information.
   * 
   * @param helpID the key associated with the widget or panel that called the Help, and indicates
   *        which msg to display from the Help file
   * @param helpText text that is inserted into HelpDialog for display
   * @param title widget or panel for which the helpText applies
   */
  public void showHelp(String title, String helpText)
  {
    // Don't show help text if window is not open
    if (isVisible() == false) {
      return;
    }
    if (helpText == null) {
      append("Called for Help...\n");
      append("...but help text not found!");
    }
    clear();
    _helpArea.setRows(1);
    // Visually show end of text since help can scroll
    insert("\n");
    insert(helpText);
    append(EOT);
    // Show the window title for which this help applies
    setTitle(title);
    setVisible(true);
  }


  /**
   * Clears the contents of the HelpWindow so new content can be added. Respects the lock flag; if
   * Lock is true, does not clear this window.
   */
  public void clear()
  {
    _lockflag = false;
    _helpArea.setText("");
  }

  // ============================================================
  // PRIVATE METHODS
  // ============================================================

  /**
   * Overrides the {@code  JTextArea} append method so that {@code HelpDialog} can lock data from
   * being written to it. If Lock is true, allow no more writing to this window
   * 
   * @param msg Whatever text is to be written in the window
   */
  private void append(String msg)
  {
    if (_lockflag == false) {
      _helpArea.append(msg);
    }
  }


  /**
   * Overloads the component insert method to ensure that text added is shown from the top down, and
   * not shown only at the bottom. If Lock is true, allow no more writing to this window
   * 
   * @param msg Whatever text is to be written in the window
   */
  private void insert(String msg)
  {
    if (_lockflag == false) {
      append(msg);
      _helpArea.setCaretPosition(0);
    }
  }

} // end HelpDialog class

