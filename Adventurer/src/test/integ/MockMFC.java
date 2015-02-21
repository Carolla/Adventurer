/**
 * MockMFC.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import mylib.MsgCtrl;
import civ.MainframeCiv;


/**
 * Create an empty replacement for hic.MainframeCiv to receive outputs from the CIV classes
 * 
 * @author Al Cline
 * @version Jan 31, 2015 // original <br>
 */
public class MockMFC extends MainframeCiv
{
  /** Context state for Hero being at town view (true) or not */
  private boolean _onTown;

  // Create the super constructor without a Mainframe
  public MockMFC()
  {
    super(new MainframeProxy());
  }

  /** Enter the designated building, or the current building if displayed */
//  @Override
//  public void enterBuilding(String bldgName)
//  {
//    // Null is legal parm for this call
//    MsgCtrl.msg(this, "Entering " + bldgName);
//  }
  

  /**
   * Is a building displayed, or is the Hero at the Town view?
   * 
   * @return true if there is no current building displayed
   */
  public boolean isOnTown()
  {
    return _onTown;
  }


  /**
   * Set the building context, as at the town view (true) or not (false)
   * 
   * @param isOnTown is true if Hero is not inside or outside a building; else false
   */
  public void setTownView(boolean isOnTown)
  {
    _onTown = isOnTown;
  }


  /**
   * Turn on (or off) logging message for this class
   * 
   * @param state true turns on logging; false turns it off
   */
  public void loggingOn(boolean state)
  {
    MsgCtrl.auditMsgsOn(state);
    MsgCtrl.errorMsgsOn(state);
  }


} // end of MockMFC
