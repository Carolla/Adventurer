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
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import mylib.MsgCtrl;
import net.miginfocom.swing.MigLayout;
import chronos.Chronos;
import civ.CommandParser;

/**
 * This class serves as the text output and command line input after and aventure is selected
 * 
 * @author Alan Cline
 * @version 1.0 Feb 18, 2013 TAA Initial object
 *          <P>
 * @version 1.1 Jul 9, 2014 ABC refactored for clearer output panel and command line input
 */
@SuppressWarnings("serial")
public class IOPanel extends JPanel
{
  private static final float FONT_HT = 14f;
  /** Number of rows in the user's output window */
  private final int winHeight = Mainframe.USERWIN_HEIGHT;
  /** Number of rows in the user's output window */
  private final int winWidth = winHeight;
  private final JTextArea output;
  private boolean redirectIO;

  // Command window requires the Command Parser to send commands
  private CommandParser _cp = null;
  private JTextField _cmdWin = null;


  /**
   * Creates 2-paned panel to represent Input and Output
   */
  // public OutputPanel(CommandParser parser)
  public IOPanel()
  {
    // _cp = new CommandParser();
    setLayout(new MigLayout("", "[grow]", "[][]"));
    output = new JTextArea();
    output.setAlignmentY(JTextArea.BOTTOM_ALIGNMENT);

    final JScrollPane outputPanel = createOutputPanel();
    this.add(outputPanel, "cell 0 1");

    final JPanel inputPanel = createCmdLinePanel();
    this.add(inputPanel, "cell 0 2");
  }

  /*
   * PUBLIC METHODS
   */

  /**
   * Set the text into the output pane, then return focus to the command line
   * 
   * @param text to display
   */
  public void setDescription(String text)
  {
    output.setText(text + "\n");
    _cmdWin.setFocusable(true);
    _cmdWin.requestFocusInWindow();
  }


  /*
   * PRIVATE METHODS
   */

  /**
   * Create the south panel input window. It must provide data to the backend command parser
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
    _cmdWin.setFocusable(true); // set the focus to this field when starting up
    _cmdWin.setCaretPosition(0);
    _cmdWin.requestFocusInWindow();

    // Create the command input text area
    JPanel southPanel = new JPanel();
    southPanel.setLayout(new MigLayout("", "[][grow]", "[]"));
    southPanel.add(cmdPrompt, "cell 0 0,alignx center");
    southPanel.add(_cmdWin, "cell 1 0,alignx left");

    // Add function to send commands to command parser.
    _cmdWin.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent event)
      {
        // Save the user's input to be retrieved by the command parser
        _cp.receiveCommand(_cmdWin.getText());
        // Echo the text and clear the command line
        output.append(_cmdWin.getText() + "\n");
        _cmdWin.setText("");
        _cmdWin.setFocusable(true);
      }
    });
    return southPanel;
  }


  /**
   * Create the north panel output window: scollable, non-editable output area
   * 
   * @return the primary user output window
   */
  private JScrollPane createOutputPanel()
  {
    output.setEditable(false);
    output.setLineWrap(true);
    output.setWrapStyleWord(true);
    output.setFocusable(false);
    output.setFont(makeRunicFont());
    output.setBackground(Color.LIGHT_GRAY); // just for fun, make the
    // background non-white
    output.setForeground(Color.BLACK); // text is colored with the
    // setForeground statement

    // TODO Is this necessary now?
    // Redirect the System stdout and stderr to the user output window
    if (redirectIO) {
      System.setOut(redirectIO());
    }

    // Make text output scrollable-savvy
    JPanel panel = new JPanel();
    panel.add(output, BorderLayout.SOUTH);
    JScrollPane scrollPane = new JScrollPane(panel);
    scrollPane.setAlignmentY(BOTTOM_ALIGNMENT);
    scrollPane.setPreferredSize(new Dimension(winHeight, winWidth));
    scrollPane.setViewportView(output);

    return scrollPane;
  }

  private Font makeRunicFont()
  {
    Font font = null;
    try {
      Font newFont = Font.createFont(Font.TRUETYPE_FONT, new File(
          Chronos.RUNIC_ENGLISH2_FONT_FILE));
      font = newFont.deriveFont(FONT_HT);
    } catch (Exception e) {
      MsgCtrl.errMsgln("Could not create font: " + e.getMessage());
    }
    return font;
  }


  /**
   * Redirect System out messages (user output) to the window area. Define a PrintStream that sends
   * its bytes to the output text area
   * 
   * @return reference to output stream
   */
  private PrintStream redirectIO()
  {
    PrintStream op = new PrintStream(new OutputStream()
    {
      public void write(int b)
      {} // never called


      public void write(byte[] b, int off, int len)
      {
        output.append(new String(b, off, len));
        // Ensure that the text scrolls as new text is appended
        // output.setCaretPosition(output.getDocument().getLength());
        _cmdWin.setFocusable(true);
        _cmdWin.requestFocusInWindow();
        _cmdWin.setCaretPosition(0);
      }
    });

    return op;
  }

  /**
   * Adds the string onto the existing text
   * 
   * @param msg the string to append
   */
  public void appendText(String msg)
  {
    output.append(msg);
  }

} // end OutputPanel class

