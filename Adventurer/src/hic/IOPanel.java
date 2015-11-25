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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import mylib.Constants;
import net.miginfocom.swing.MigLayout;
import chronos.Chronos;
import civ.CommandParser;
/**
 * This class serves as the text output and command line input after an Adventure is selected
 * 
 * @author Alan Cline
 * @version Feb 18, 2013 // TAA Initial object <br>
 *          Jul 9, 2014 // ABC refactored for clearer output panel and command line input <br>
 *          Aug 2, 2014 // ABC replaced frame sizing with call to {@code Mainframe.getWindowSize()}
 *          <br>
 *          Aug 6, 2014 // ABC Merged {@code Mainframe.StandardLayout} inner class with this class
 *          <br>
 *          Aug 18, 2014 // ABC Removed as inner class and made stand-along class <br>
 *          Nov 11 2015 // ABC modified to be displayed with {@code Mainframe.replaceLeftPanel()}
 *          instead of its own method <br>
 */
@SuppressWarnings("serial")
public class IOPanel extends ChronosPanel
{
  private final StyledDocument _output;
  private final JTextPane _transcriptPane;
  private final JScrollPane _scrollpane;

  private JTextField _cmdWin = null;
  static private final String IOPANEL_TITLE = "Commands & Transcript";

  /**
   * Color class does not have brown, so I have to make it. Color constuctor args = red, green, blue
   * for values 0-255, kicked up one notch of brightness
   */
  private Color _backColor = Constants.MY_BROWN;
  private Color _foreColor = Color.BLACK;

  private final SimpleAttributeSet _errorAttributes;
  private CommandParser _commandParser;


  // ============================================================
  // Constructors and constructor helpers
  // ============================================================

  /**
   * Creates output transcript panel and input CommandLine Input panel The IOPanel is used for user
   * commands both inside and outside buildings, so the
   * 
   * @param bldgCiv manages the IOPanel and its input/output messages.
   * @param cp  handles all input commands from the user
   */
  public IOPanel(CommandParser cp)
  {
    super(IOPANEL_TITLE);
    _commandParser = cp;

    setLayout(new MigLayout("", "[grow]", "[][]"));
    _transcriptPane = new JTextPane();
    _transcriptPane.setAlignmentY(JTextArea.TOP_ALIGNMENT);
    _output = _transcriptPane.getStyledDocument();

    _errorAttributes = new SimpleAttributeSet();
    StyleConstants.setForeground(_errorAttributes, Color.RED.darker());
    StyleConstants.setFontFamily(_errorAttributes, "Sans Serif");
    StyleConstants.setFontSize(_errorAttributes, 14);
    StyleConstants.setBold(_errorAttributes, true);

    _scrollpane = createOutputPanel();
    this.add(_scrollpane, "cell 0 1");

    final JPanel inputPanel = createCmdLinePanel();
    this.add(inputPanel, "cell 0 2");
  }


  // ============================================================
  // Public Methods
  // ============================================================

  /**
   * Display error text, using different Font and color, then return to standard font and color.
   * 
   * @param msg text block to display
   */
  public void displayErrorText(String msg)
  {
    displayText(msg + Constants.NEWLINE, _errorAttributes);
  }


  /**
   * Display a block of text in the output Transcript area. Isolate the user's command on its own
   * line (or text block).
   * 
   * @param msg text block to display
   */
  public void displayText(String msg)
  {
    displayText(Constants.NEWLINE + msg + Constants.NEWLINE, null);
  }

  public void setFocusOnCommandWindow()
  {
    // Ensure that the text scrolls as new text is appended
    _cmdWin.setFocusable(true);
    _cmdWin.requestFocusInWindow();
  }


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

    // Add function to send commands to command parser.
    _cmdWin.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event)
      {
        // Save the user's input to be retrieved by the command parser
        _cmdWin.requestFocusInWindow();
        String in = _cmdWin.getText();
        _commandParser.receiveCommand(in);
        // Echo the text and clear the command line
        displayText(in, null);
        _cmdWin.setText("");
      }
    });
    return southPanel;
  }

  /**
   * Create the top (north) panel transcript output window: scollable, non-editable output area
   * 
   * @return the primary user output window
   */
  private JScrollPane createOutputPanel()
  {
    _transcriptPane.setEditable(false);
    _transcriptPane.setFocusable(false);
    _transcriptPane.setFont(Chronos.RUNIC_FONT);
    _transcriptPane.setBackground(_backColor); 
    _transcriptPane.setForeground(_foreColor); 

    // Ensure that the text always autoscrolls as more text is added
    DefaultCaret caret = (DefaultCaret) _transcriptPane.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

    // Make text output scrollable-savvy
    add(_transcriptPane, BorderLayout.SOUTH);
    JScrollPane scrollPane = new JScrollPane(this);
    scrollPane.setAlignmentY(BOTTOM_ALIGNMENT);
    Dimension frame = Mainframe.getWindowSize();
    scrollPane.setPreferredSize(new Dimension(frame.height, frame.width));
    scrollPane.setViewportView(_transcriptPane);
    return scrollPane;
  }


  // ============================================================
  // Private Methods
  // ============================================================

  /**
   * Wrapper method for StyledDocument insertString
   * 
   * @param string the message to be displayed
   * @param attributes attributes for this text, can be null
   */
  private void displayText(String string, AttributeSet attributes)
  {
    try {
      _output.insertString(_output.getLength(), string + Constants.NEWLINE, attributes);
    } catch (BadLocationException e) {
      // Shouldn't happen
      e.printStackTrace();
    }
  }


} // end OutputPanel class

