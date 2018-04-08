/**
 * AboutDialog.java Copyright (c) 2008, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package hic;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * A modal window to display author credits and version information.
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Jan 17, 2009 //adapted from Authoring Tool for Player module
 *          <DD>
 *          <DT>Build 2.0 Nov 23, 2013 // major GUI changes wit open source team
 *          <DD>
 *          </DL>
 * 
 */
public class AboutDialog extends JDialog
{
  /** Standard copyright notice for About box and Splash screen. */
  private final String COPYRIGHT_MSG =
      "Copyright (c) 2011 by Carolla Development, Inc.   All Rights Reserved.";

  /** Version information. */
  // private final String VERSION_MSG1 = "Version 1.0 January, 2009";
  // private final String VERSION_MSG2 = "Version 2.0  June 18, 2012";
  private final String VERSION_MSG3 = "Version 3.0  November 23, 2013";
  private final String BUILD_MSG = "Build 42 -- The answer to everything.";
  private final String TEAM_CREDITS =
      "Designed and written by the Carolla Development Open Source Team:";
  private final String CREDITS_LEAD = "Project Leader and Designer: Alan Cline";
  private final String CREDITS_DEVS =
      "Developers: Tim Armstrong, Adam Harrison, Dave Campbell";
  private final String CREDITS_TEST = "Testing: Mary Lynn Monge";
  private final String CREDITS_USE = "Usability Design and Testing: Martha Lindeman";
  private final String CREDITS_ART = "Artwork: Sally Less";
  private final String CREDITS_DOCS = "Documentation: Dave Campbell";
  /** Subversion commit (Build) number */
  // private final String BUILD_NBR = $LastChangedDate$;
  /** Program name information. */
  static private final String PROGRAM_NAME = "Chronos Game Framework";
  static private final String INDENT = "<P> &nbsp&nbsp&nbsp &nbsp&nbsp&nbsp -- ";
  /** Start size for dialog width */
  private final int DIALOG_WIDTH = 480;
  /** Start size for dialog height */
  private final int DIALOG_HEIGHT = 400;


  /**
   * Display the About box in a dialog window.
   * 
   * @param owner the main menu frame that supports this dialog.
   */
  public AboutDialog(JFrame owner)
  {
    super(owner, PROGRAM_NAME, true);
    Container contentPane = getContentPane();

    // Get screensize and set location to center
    Toolkit kit = Toolkit.getDefaultToolkit();
    Dimension screensize = kit.getScreenSize();
    int screenHeight = screensize.height;
    int screenWidth = screensize.width;
    setLocation(screenWidth / 4, screenHeight / 4);
    setSize(DIALOG_WIDTH, DIALOG_HEIGHT);

    // Build a JLabel to place text on the contentPane
    final String title1 = "<H1><I> &nbsp Adventurer! </I></H1>";
    final String title2 = "<H2> &nbsp &nbsp &nbsp -- Player's Module </H2>";
    // final String byline =
    // "<H3> &nbsp &nbsp Designed and written <br> &nbsp &nbsp by Alan Cline </H3>";
    final String byline =
        INDENT + CREDITS_LEAD +
            INDENT + CREDITS_DEVS +
            INDENT + CREDITS_TEST +
            INDENT + CREDITS_USE +
            INDENT + CREDITS_ART +
            INDENT + CREDITS_DOCS;

    contentPane.add(new JLabel(
        "<HTML>" + title1 + title2 + "<HR>" +
            "<P> &nbsp&nbsp&nbsp " + COPYRIGHT_MSG +
            "<P> &nbsp&nbsp&nbsp " + VERSION_MSG3 +
            "<P> &nbsp&nbsp&nbsp " + BUILD_MSG +
            "<P><P> &nbsp&nbsp&nbsp " + TEAM_CREDITS +
            byline + "</HTML>"), BorderLayout.CENTER);

    // Add the OK button with listener
    JPanel okPanel = new JPanel();
    JButton okBtn = new JButton("OK");
    okBtn.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        setVisible(false);
      }
    });

    okPanel.add(okBtn);
    contentPane.add(okPanel, BorderLayout.SOUTH);
  }

} // end AboutDialog class
