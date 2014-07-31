/**
 * IHelpText.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package mylib.hic;

/**
 * Sets the help text for the GUI window that implements this interfance
 * 
 * @author alancline
 * @version Jul 28, 2014 // original <br>
 */
public interface IHelpText
{
  /** The Help key is defined as the {@code F1} key */
  final String HELP_KEY = "F1";

  /** Set the widget's specific help message, which will be displayed when the Help key is pressed */
  void initHelpText(String text);
}
