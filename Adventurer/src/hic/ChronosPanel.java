/**
 * ChronosPanel.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use from Carolla
 * Development, Inc. by email: acline@carolla.com
 */

package hic;

import javax.swing.JPanel;

import mylib.Constants;

/**
 * Base class for most panels used in {@code Adventurer}. Each civ controls two
 * {@code ChronosPanels}, one on the left for user interaction, and one on the right for
 * images.
 * 
 * @author Al Cline
 * @version Nov 9, 2015 // original <br>
 */
public class ChronosPanel extends JPanel
{
  /** Default title field for all ChronosPanels, to be overwritten */
  protected String _title;

  // ====================================================================================
  // Constructor
  // ====================================================================================

  /** Default constructor */
  public ChronosPanel(String title)
  {
    _title = title;
  }


  // ====================================================================================
  // Default methods in common for a base class of ChronosPanels
  // ====================================================================================

  /** Get the panel's title for display */
  public String getTitle()
  {
    return _title;
  }

  /**
   * Force ChronosPanel implementers to set a title, either in the Panel itself of in the
   * Panel's border.
   * 
   * @param title of the panel
   */
  public void setTitle(String title)
  {
    if (!title.startsWith(Constants.SPACE)) {
      title = Constants.SPACE + title;
    }
    if (!title.endsWith(Constants.SPACE)) {
      title = title + Constants.SPACE;
    }
    _title = title;
  }

} // end of ChronosPanel
