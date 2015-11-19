/**
 * ChronosPanel.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package hic;

import java.awt.Image;

import javax.swing.JPanel;

/**
 * Adds a title method to JPanel for Chronos panels. Instead of using various constructor parms, the
 * attributes are added as set() functions. <br>
 * For example, instead of
 * <P>
 * {@code JPanel myPanel = new JPanel(LayoutManager lm);}
 * <P>
 * use
 * <P>
 * {@code ChronosPanel myPanel = new ChronosPanel();} <br>
 * {@code myPanel.set(LayoutManagerlm);}.
 * <P>
 * 
 * @see {@code Mainframe.setFrameAndMenubar() for a better example.
 * 
 * @author Al Cline
 * @version Nov 9, 2015 // original <br>
 */
@SuppressWarnings("serial")
public class ChronosPanel extends JPanel
{
  /** Default title field for all ChronosPanels, to be overwritten */
  protected String _title = " ";

  /** If image is shown in panel... */
  protected Image _image;


  // ====================================================================================
  // Constructor
  // ====================================================================================

  /** Default constructor */
  public ChronosPanel() {} 
  
  
  // ====================================================================================
  // Default methods in common for a base class of ChronosPanels
  // ====================================================================================

  /** Get the panel's title for display */
  public String getTitle()
  {
    return _title;
  }

  /**
   * Force ChronosPanel implementers to set a title, either in the Panel itself of in the Panel's
   * border.
   * 
   * @param title of the panel
   */
  public void setTitle(String title)
  {
    _title = title;
  }

  /**
   * Display the image in the calling panel using JPanel's method
   * 
   * @param image to be displayed
   */
  public void setImage(Image image)
  {
    _image = image;
  }



} // end of ChronosPanel
