/**
 * TestAdvMainFrameCiv.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package test.civ;

import org.junit.Test;

/**
 * Test the various methods in AdvMainframeCiv
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Dec 21, 2013 // original
 *          <DD>
 *          </DL>
 */
public class TestMainframeCiv
{
  @Test
  public void allMethodsArePassthroughToGuiClasses()
  {}

  /**
   * List of methods that do not need JUnit test because they are too trivial,
   * or some other test method tests them equally well. <br>
   * <code> back() </code> -- calls GUI
   * <code> backToMain() </code> -- calls GUI
   * <code> displayErrorText() </code> -- calls GUI
   * <code> displayImage() </code> -- calls GUI
   * <code> displayText() </code> -- calls GUI
   * <code> replaceLeftPanel(ChronosPanel) </code> -- calls GUI
   * <code> replaceLeftPanel(IOPanel) </code> -- calls GUI
   * <code> quit() </code> -- quits
   */
  public void notNeeded()
  {}

} // end of TestMainFrameCiv class
