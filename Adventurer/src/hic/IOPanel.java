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

import civ.CommandParser;
import civ.UserMsg;
import mylib.Constants;
import net.miginfocom.swing.MigLayout;
import pdc.Util;

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
 */
@SuppressWarnings("serial")
public class IOPanel extends JPanel // implements IOPanelInterface
{
    private final StyledDocument _output;
    private final JTextPane _pane;
    private final JScrollPane _scrollpane;

    private JTextField _cmdWin = null;

    /**
     * Color class does not have brown, so I have to make it. Color constuctor args = red, green,
     * blue for values 0-255, kicked up one notch of brightness
     */
//    private final Color MY_LIGHT_BROWN = new Color(130, 100, 90).brighter();
    private Color _backColor = Constants.MY_BROWN;
    private Color _foreColor = Color.BLACK;
    
    private final SimpleAttributeSet _errorAttributes;
    private final CommandParser _commandParser;


    // ============================================================
    // Constructors and constructor helpers
    // ============================================================

    /**
     * Creates output test panel and input CommandLine Input panel
     */
    public IOPanel(UserMsg mfCiv, CommandParser cp)
    {
        _commandParser = cp;
        setLayout(new MigLayout("", "[grow]", "[][]"));
        _pane = new JTextPane();
        _pane.setAlignmentY(JTextArea.TOP_ALIGNMENT);
        _output = _pane.getStyledDocument();

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


    public void setFocusOnCommandWindow()
    {
        // Ensure that the text scrolls as new text is appended
        _cmdWin.setFocusable(true);
        _cmdWin.requestFocusInWindow();
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
     * Create the north panel transcript output window: scollable, non-editable output area
     * 
     * @return the primary user output window
     */
    private JScrollPane createOutputPanel()
    {
        _pane.setEditable(false);
        _pane.setFocusable(false);
        _pane.setFont(Util.makeRunicFont(14f));
//        _pane.setBackground(MY_LIGHT_BROWN); // make the background my version of a nice warm brown
        _pane.setBackground(_backColor); // make the background my version of a nice warm brown
        _pane.setForeground(_foreColor); // text is colored with the setForeground statement

        // Ensure that the text always autoscrolls as more text is added
        DefaultCaret caret = (DefaultCaret) _pane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // Make text output scrollable-savvy
        JPanel panel = new JPanel();
        panel.add(_pane, BorderLayout.SOUTH);
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setAlignmentY(BOTTOM_ALIGNMENT);
        Dimension frame = Mainframe.getWindowSize();
        scrollPane.setPreferredSize(new Dimension(frame.height, frame.width));
        scrollPane.setViewportView(_pane);
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

