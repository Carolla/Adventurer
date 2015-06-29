
package hic;



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
}
