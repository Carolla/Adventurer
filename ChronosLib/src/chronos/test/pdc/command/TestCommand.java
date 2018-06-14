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
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chronos.pdc.command.Command;
import mylib.MsgCtrl;


/**
 * @author Al Cline
 * @version May 28, 2018 // original <br>
 */
public class TestCommand
{
  /** Test Command with no parms */
  private CommandProxy _testCmd;
  private final String CMD_NAME = "CmdProxy_NoParms";
  private final int DELAY = 0;
  private final int DURATION = 10;
  private final String CMD_DESCRIPTION =
      "A simple no-parm command used for testing the Command class";
  private final String CMDFMT = null;

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
  {
    _testCmd = new CommandProxy(CMD_NAME, DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
    assertNotNull(_testCmd);

  }

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @AfterEach
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);

    _testCmd = null;
  }


  // ===============================================================================
  // BEGIN TESTING
  // ===============================================================================

  /**
   * @Normal.Test String Command() -- verify that all fields and methods report correctly. This
   *              test is not actually needed because init() is a wrapper, but this test is
   *              useful for context processing.
   */
  @Test
  public void testCtor_NoParms()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    String cmdName = "CmdProxy";
    String desc = "No-parm command description";
    String fmt = "CommandProxy (no parms)";

    // Verify that base class method prints valid command format
    Command proxy = new CommandProxy(cmdName, 3, 15, desc, fmt);
    assertNotNull(proxy);
    dump(proxy);;

    assertEquals(cmdName, proxy.getName());
    assertEquals(desc, proxy.getDescription());
    assertEquals(3, proxy.getDelay());
    assertEquals(15, proxy.getDuration());
    assertEquals("USAGE: " + fmt, proxy.usage());
  }

  /**
   * @Normal.Test String Command() -- verify that all parms, fields, and methods report
   *              correctly. This test is not actually needed because init() is a wrapper, but
   *              this test is useful for context processing.
   */
  @Test
  public void testCtor_TwoParms()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    String cmdName = "CmdProxy";
    String desc = "Two-parm command description";
    String fmt = "CommandProxy [keyword1] [keyword2]";

    // Verify that base class method prints valid command format
    Command proxy = new CommandProxy(cmdName, 3, 15, desc, fmt);
    assertNotNull(proxy);

    // The parms must be put into the command as ArrayList<String>
    String textIn = "CmdProxy parm1 parm2";
    ArrayList<String> cmdList = new ArrayList<String>(Arrays.asList(textIn.split(" ")));

    // If parms init'd correctly, init(List<String>) returns true
    assertTrue(proxy.init(cmdList));
    dump(proxy);

    assertEquals(cmdName, proxy.getName());
    assertEquals(desc, proxy.getDescription());
    assertEquals(3, proxy.getDelay());
    assertEquals(15, proxy.getDuration());
    assertEquals("USAGE: " + fmt, proxy.usage());
  }


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
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Parse the parms before printing the string
    String expOut = CMD_NAME;
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
    assertEquals("USAGE: CmdProxy_NoParms (command takes no parms)", usageFmt);
  }


  // ===============================================================================
  // PRIVATE HELPERS
  // ===============================================================================

  private void dump(Command proxy)
  {
    MsgCtrl.msgln("\t Command:\t" + proxy.getName());
    MsgCtrl.msgln("\t Description:\t" + proxy.getDescription());
    MsgCtrl.msgln("\t Delay:\t\t " + proxy.getDelay());
    MsgCtrl.msgln("\t Duration:\t" + proxy.getDuration());
    MsgCtrl.msgln("\t " + proxy.usage());

    List<String> plist = proxy.getParms();
    if (plist.size() == 0) {
      MsgCtrl.msgln("\t No command parms");
    } else {
      for (String s : plist) {
        MsgCtrl.msg("\t" + s);
      }
      MsgCtrl.msgln("\n");
    }
  }



} // end of TestCommand.java class
