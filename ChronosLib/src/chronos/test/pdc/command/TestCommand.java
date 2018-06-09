/**
 * TestCommand.java Copyright (c) 2018, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com.
 */

package chronos.test.pdc.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mylib.MsgCtrl;


/**
 * @author Al Cline
 * @version May 28, 2018 // original <br>
 */
public class TestCommand
{
  static private FakeCommand _testCmd;

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @BeforeAll
  public static void setUpBeforeClass() throws Exception
  {
    _testCmd = new FakeCommand();
    assertNotNull(_testCmd);
  }

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
   * @Normal.Test String convertArgsToString(List)
   */
  @Test
  public void testConvertArgsToString()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Multiple list entries return as single string
    ArrayList<String> plist = new ArrayList<String>(Arrays.asList("Talk", "to", "Bork"));
    String result = _testCmd.convertArgsToString(plist);
    String expResult = "Talk to Bork";
    assertTrue(expResult.equals(result));

    // Single entry return as single string
    plist = new ArrayList<String>(Arrays.asList("Word"));
    result = _testCmd.convertArgsToString(plist);
    expResult = "Word";
    assertTrue(expResult.equals(result));
  }


  /**
   * @Error.Test String convertArgsToString(List) -- empty string args
   */
  @Test
  public void testConvertArgsToString_Empty()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Multiple list entries return as single string
    ArrayList<String> plist = new ArrayList<String>();
    String result = _testCmd.convertArgsToString(plist);
    String expResult = "";
    assertTrue(expResult.equals(result));
  }


  /**
   * @Not.Needed int getDelay() -- getter
   */
  @Test
  public void testGetDelay()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed String getDescription() -- getter
   */
  @Test
  public void testGetDescription()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed int getDuration() -- getter
   */
  @Test
  public void testGetDuration()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed String getName() -- getter
   */
  @Test
  public void testGetName()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed boolean isInternal() -- getter
   */
  @Test
  public void testIsInternal()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed boolean isUserInput() -- getter
   */
  @Test
  public void testIsUserInput()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.GETTER);
  }


  /**
   * @Not.Needed void setDelay(int) -- setter
   */
  @Test
  public void testSetDelay()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.SETTER);
  }


  /**
   * @Not.Needed void setOutput(UserMsgInterface) -- setter
   */
  @Test
  public void testSetOutput()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.SETTER);
  }


  /**
   * @Normal.Test String toString() -- returns command name
   */
  @Test
  public void testToString()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Parse the parms before printing the string
    String expOut = "FAKE_CMD";
    String actOut = _testCmd.toString();
    MsgCtrl.msgln("\t" + actOut);
    assertTrue(expOut.equals(actOut));
  }


  /**
   * @Normal.Test String usage() -- print response for command that takes parms
   */
  @Test
  public void testUsage()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Verify that base class method prints valid command format
    String usageFmt = _testCmd.usage();
    MsgCtrl.msgln("\t" + _testCmd.getDescription());
    MsgCtrl.msgln("\t" + usageFmt);
    assertEquals("USAGE: FAKE_CMD [Keyword1] [Keyword2]", usageFmt);
  }


  /**
   * @Normal.Test String usage() -- print response for command that takes no parms
   */
  @Test
  public void testUsage_NoParms()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Verify that base class method prints valid command format
    FakeCommand2 cmd2 = new FakeCommand2();
    assertNotNull(cmd2);

    String usageFmt = cmd2.usage();
    MsgCtrl.msgln("\t" + cmd2.getDescription());
    MsgCtrl.msgln("\t" + usageFmt);
    assertEquals("USAGE: ANOTHER_FAKE_CMD (command takes no parms)", usageFmt);
  }


} // end of TestCommand.java class
