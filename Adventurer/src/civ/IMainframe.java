/**
 * IMainframe.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com
 */

package civ;

import hic.ChronosPanel;
import hic.ImagePanel;

/**
 * Used as socket for production and testing code to allow civ classes to support both.
 * 
 * @author Al Cline
 * @version Feb 21, 2015 // original <br>
 */
public interface IMainframe
{
  public void back();

  public void backToMain(String newFrameTitle);

  /** Display a prompt message asking for confirmation */
  public boolean displayPrompt(String msg);

  public void replaceLeftPanel(ChronosPanel leftPanel);

  public void replaceRightPanel(ChronosPanel rightPanel);

  /** Put a title into the frame's border */
  public void setTitle(String title);

  /** Sets title of left-side panel **/
  public void setLeftTitle(String title);

  public void showHelp();

  public void setImagePanel(ImagePanel imagePanel);

  public void displayImage(String title, String imageName);


} // end of MainframeInterface
