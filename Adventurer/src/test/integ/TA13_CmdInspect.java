/**
 * TA13_CmdInspect.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.NPC;
import chronos.pdc.buildings.Building;
import chronos.pdc.command.Command;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.NPCRegistry;
import chronos.pdc.registry.RegistryFactory.RegKey;
import mylib.MsgCtrl;
import pdc.command.CommandInput;

/**
 * Ensures that a Hero can inspect an NPC (returns the near description) in a particular building.
 * Each Building has at least its Building Master NPC; some buildings, e.g., the Inn, can have many
 * more NPCs (patrons).
 * <p>
 * <ol>
 * <li>User enters {@code Inspect <NPC name>} on the IOPanel CommandLine for the NPC in a particular
 * Building.</li>
 * <li>The command's parms are verified and the Scheduler executes the command.</li>
 * <li>The requested NPC's near description is displayed in the IOPanel Transcript section.</li>
 * <li>If the NPC is not there, then an error message is returned to the IOPanel.</li>
 * </ol>
 * <p>
 * The {@code BuildingDisplayCiv} knows what Building is currently displayed, and can make that
 * available to the command, and therefore is an input to the {@code CmdInpsect} constructor.
 * <p>
 * Implementation Note for all {@code Command} integration tests: <br>
 * JUnit uses a simulated CommandLine in {@code IOPanelProxy} to send command strings to
 * {@code civ.CommandParser}. All command responses are returned to the simulated transcript area
 * (console) of {@code IOPanelProxy}.
 * 
 * @author Al Cline
 * @version April 29, 2016 // original <br>
 */
public class TA13_CmdInspect extends IntegrationTest
{
  static private Building _curBldg;
  static private NPC _bldgMaster;

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void CmdSpecificSetUp() throws Exception
  {
    // Set the current Building to be the Bank, the NPC = bank master
    _bldgCiv.enterBuilding("The Bank");
    String bldgName = _bldgCiv.getCurrentBuilding();
    BuildingRegistry breg = (BuildingRegistry) _regFactory.getRegistry(RegKey.BLDG);
    _curBldg = breg.getBuilding(bldgName);
    assertNotNull(_curBldg);

    String name = _curBldg.getMaster();
    NPCRegistry nreg = (NPCRegistry) _regFactory.getRegistry(RegKey.NPC);
    _bldgMaster = nreg.getNPC(name);
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {}

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ============================================================
  // BEGIN TESTING
  // ============================================================

  @Test
  public void testBuildingMaster()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP
    MsgCtrl.msgln("\t Current building = " + _curBldg.getName());
    MsgCtrl.msgln("\t Building master = " + _bldgMaster.getName());
    MsgCtrl.msgln("");

    // RUN
    String parm = "Inspect J.P. Pennypacker";
    _cp.receiveCommand(parm);
    
    // VERIFY
    //  Get NPC from registry and confirm near description
    MsgCtrl.msgln("\tExpected: " + _bldgMaster.getNearDescription());
//    MsgCtrl.msgln("\tReceived: " + _mfCiv.displayText(result));

  }

  
  // @Test
  public void testCmdParserConnection()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // CmdParser returns false until command is initialized
    assertFalse(_cp.receiveCommand("Inspect Falsoon"));

    // CmdParser returns false until command is initialized
    CommandInput ci = _cp.createCommandInput("Inspect Falsoon");
    assertNotNull(ci);
    MsgCtrl.msgln("\t" + ci.toString());
    Command cmd = _cmdFac.createCommand(ci);
    assertNotNull(cmd);
    MsgCtrl.msgln("\t" + cmd.getName() + ": " + cmd.getDescription());

  }

} // end of TA13_CmdInspect integration test

