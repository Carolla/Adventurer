/**
 * TestEvent.java Copyright (c) 2018, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */

package chronos.test.pdc.command;

import static org.junit.Assert.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import mylib.MsgCtrl;

/**
 * Mostly a wrapper object for the Commands that are placed into the DeltaQueue
 * 
 * @author Al Cline
 * @version June 15, 2018 // original <br>
 */
public class TestEvent
{
  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @BeforeAll
  public static void setUpBeforeClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @AfterAll
  public static void tearDownAfterClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @BeforeEach
  public void setUp() throws Exception
  {}

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @AfterEach
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ===============================================================================
  // BEGIN TESTING
  // ===============================================================================

  /**
   * @Not.Needed int compareTo(Event) -- wrapper
   */
  @Test
  public void testCompareTo1()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.WRAPPER);
  }


  /**
   * @Not.Needed int compareTo(Object) -- wrapper
   */
  @Test
  public void testCompareTo2()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.WRAPPER);
  }


  /**
   * @Not.Needed Command getCommand() -- getter
   */
  @Test
  public void testGetCommand()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed int getDelta() -- getter
   */
  @Test
  public void testGetDelta()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed void setDelta(int) -- setter
   */
  @Test
  public void testSetDelta()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.SETTER);
  }


} // end of TestEvent.java class
