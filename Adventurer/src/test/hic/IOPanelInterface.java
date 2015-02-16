/**
 * IOPanelInterface.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.hic;

import javax.swing.JTextArea;

/**
 * Socket bewtween the {@code hiv.IOPanel} and the {@code civ.CommandParser} for production and
 * testing.
 * 
 * @author Al Cline
 * @version Feb 15, 2014 // original <br>
 */

public interface IOPanelInterface
{

  public void displayText(String msg);

  public void displayErrorText(String msg);

  public JTextArea getOutputArea();

  public boolean isOnTown();

}
