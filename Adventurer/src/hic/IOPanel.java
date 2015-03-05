/**
 * IOPanel.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package hic;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import mylib.Constants;
import net.miginfocom.swing.MigLayout;
import pdc.Util;
import civ.CommandParser;
import civ.MainframeCiv;

/**
 * This class serves as the text output and command line input after an Adventure is selected
 * 
 * @author Alan Cline
 * @version Feb 18, 2013 // TAA Initial object <br>
 *          Jul 9, 2014 // ABC refactored for clearer output panel and command line input <br>
 *          Aug 2, 2014 // ABC replaced frame sizing with call to {@code Mainframe.getWindowSize()}<br>
 *          Aug 6, 2014 // ABC Merged {@code Mainframe.StandardLayout} inner class with this class <br>
 *          Aug 18, 2014 // ABC Removed as inner class and made stand-along class <br>
 */
@SuppressWarnings("serial")
public class IOPanel extends JPanel implements IOPanelInterface
{
  private final JTextArea _output;
  private final JScrollPane _scrollpane;
  // private boolean _redirectIO;

  private JTextField _cmdWin = null;

  /**
   * Color class does not have brown, so I have to make it. Color constuctor args = red, green, blue
   * for values 0-255, kicked up one notch of brightness
   */
  private final Color MY_LIGHT_BROWN = new Color(130, 100, 90).brighter();


  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /**
   * Creates output test panel and input CommandLine Input panel
   */
  public IOPanel()
  {
    setLayout(new MigLayout("", "[grow]", "[][]"));
    _output = new JTextArea();
    _output.setAlignmentY(JTextArea.TOP_ALIGNMENT);

    _scrollpane = createOutputPanel();
    this.add(_scrollpane, "cell 0 1");

    final JPanel inputPanel = createCmdLinePanel();
    this.add(inputPanel, "cell 0 2");
  }


  /**
   * Display a block of text in the output Transcript area
   * 
   * @param msg text block to display
   */
  public void displayText(String msg)
  {
    _output.append(Constants.NEWLINE + msg + Constants.NEWLINE);

    // Ensure that the text scrolls as new text is appended
    _cmdWin.setFocusable(true);
    _cmdWin.requestFocusInWindow();
  }


  // TODO JTextArea will not change color and font for individual text lines, JTextPane is needed for
  // that.
  /**
   * Display error text, using different Font and color, then return to standard font and color.
   * 
   * @param msg text block to display
   */
  public void displayErrorText(String msg)
  {
    Color prevColor = _output.getForeground();
    Font prevFont = _output.getFont();
    _output.setForeground(Color.RED);
    _output.setFont(new Font("Sans Serif", Font.BOLD, 14));
    // Write the text
    displayText(msg);
    // Reset the original colors
    _output.setForeground(prevColor);
    _output.setFont(prevFont);
  }


  /**
   * Retrieve the output viewer
   * 
   * @return the output panel for error and other messages
   */
  public JTextArea getOutputArea()
  {
    return _output;
  }



  /**
   * Call the Mainframe's Civ for the building/town status
   * 
   * @return true if there are no buildings displayed and hero is at town level
   */
  public boolean isOnTown()
  {
    Mainframe mf = Mainframe.getInstance();
    MainframeCiv mfciv = mf.getMainframeCiv();
    return mfciv.isOnTown();
  }

  // ============================================================
  // Public Methods
  // ============================================================

  // /**
  // * Set the text into the output pane, then return focus to the command line
  // *
  // * @param text to display
  // */
  // public void setDescription(String text)
  // {
  // _output.setText(text + "\n");
  // // Set cursor to top of description on first display
  // _output.setCaretPosition(0);
  // // JViewport jv = _output.getViewport();
  // // jv.setViewPosition(new Point(0,0));
  // _cmdWin.requestFocusInWindow();
  // }


  // ============================================================
  // Private Methods
  // ============================================================

  /**
   * Create the south panel input window that provides data to the backend command parser
   * 
   * @return the user input window
   */
  private JPanel createCmdLinePanel()
  {
    // Create the command prompt label
    JLabel cmdPrompt = new JLabel();
    cmdPrompt.setText("  Command? ");

    // Create the text field to collect the user's command
    _cmdWin = new JTextField(100);
    _cmdWin.setFocusable(true);
    _cmdWin.setCaretPosition(0);
    _cmdWin.requestFocusInWindow();

    // Create the command input text area
    JPanel southPanel = new JPanel();
    southPanel.setLayout(new MigLayout("", "[][grow]", "[]"));
    southPanel.add(cmdPrompt, "cell 0 0,alignx center");
    southPanel.add(_cmdWin, "cell 1 0,alignx left");

    // Create the command parser that goes in here
    // final CommandParser cp =
    // CommandParser.getInstance(Mainframe.getInstance().getMainframeCiv());
    final CommandParser cp = CommandParser.getInstance(this);

    // Add function to send commands to command parser.
    _cmdWin.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        // Save the user's input to be retrieved by the command parser
        _cmdWin.requestFocusInWindow();
        String in = _cmdWin.getText();
        cp.receiveCommand(in);
        // Echo the text and clear the command line
        _output.append(in + "\n");
        _cmdWin.setText("");
      }
    });
    return southPanel;
  }

  /**
   * Create the north panel transcript output window: scollable, non-editable output area
   * 
   * @return the primary user output window
   */
  private JScrollPane createOutputPanel()
  {
    _output.setEditable(false);
    _output.setLineWrap(true);
    _output.setWrapStyleWord(true);
    _output.setFocusable(false);
    _output.setFont(Util.makeRunicFont(14f));
    _output.setBackground(MY_LIGHT_BROWN); // make the background my version of a nice warm brown
    _output.setForeground(Color.BLACK); // text is colored with the setForeground statement

    // Ensure that the text always autoscrolls as more text is added
    DefaultCaret caret = (DefaultCaret) _output.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

    // Make text output scrollable-savvy
    JPanel panel = new JPanel();
    panel.add(_output, BorderLayout.SOUTH);
    JScrollPane scrollPane = new JScrollPane(panel);
    scrollPane.setAlignmentY(BOTTOM_ALIGNMENT);
    Dimension frame = Mainframe.getWindowSize();
    scrollPane.setPreferredSize(new Dimension(frame.height, frame.width));
    scrollPane.setViewportView(_output);
    return scrollPane;
  }


  // /**
  // * Redirect System out messages (user output) to the window area. Define a PrintStream that
  // sends
  // * its bytes to the output text area
  // *
  // * @return reference to output stream
  // */
  // private PrintStream redirectIO()
  // {
  // PrintStream op = new PrintStream(new OutputStream()
  // {
  // public void write(int b)
  // {} // never called
  //
  // public void write(byte[] b, int off, int len)
  // {
  // _output.append(new String(b, off, len));
  // // Ensure that the text scrolls as new text is appended
  // _cmdWin.setFocusable(true);
  // _cmdWin.requestFocusInWindow();
  // _cmdWin.setCaretPosition(0);
  // }
  // });
  //
  // return op;
  // }


} // end OutputPanel class

