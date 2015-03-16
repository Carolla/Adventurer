
package hic;

import javax.swing.JTextArea;


public interface IOPanelInterface
{

  /**
   * Display error text, using different Font and color, then return to standard font and color.
   * 
   * @param msg text block to display
   */
  public void displayErrorText(String msg);

  /**
   * Display a block of text in the output Transcript area
   * 
   * @param msg text block to display
   */
  public void displayText(String msg);

  /**
   * Retrieve the output viewer
   * 
   * @return the output panel for error and other messages
   */
  public JTextArea getOutputArea();

  /**
   * Call the Mainframe's Civ for the building/town status
   * 
   * @return true if there are no buildings displayed and hero is at town level
   */
  public boolean isOnTown();

}
