/**
 * TestCmdEnter.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import hic.MainframeInterface;

import java.util.ArrayList;
import java.util.List;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.command.CmdEnter;
import pdc.command.CmdEnter.MockCmdEnter;
import pdc.command.CommandFactory;
import test.integ.IOPanelProxy;
import test.integ.MainframeProxy;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.BuildingDisplayCiv;
import civ.BuildingDisplayCiv.MockBldgCiv;
import civ.CommandParser;
import civ.MainframeCiv;

/**
 * @author Al Cline
 * @version May 4, 2015 // original <br>
 */
public class TestCmdEnter
{
  private static CommandParser _cp = null;
  private static IOPanelProxy _iopx = null;
  private static CommandFactory _cmdFac = null;
  private static BuildingDisplayCiv _bdciv = null;
  private static MockBldgCiv _mockbdciv = null;

  private CmdEnter _cmdEnter = null;
  private MockCmdEnter _mock = null;

  private static RegistryFactory _regfac = null;
  private static BuildingRegistry _breg = null;
  private static List<Building> _bList = null;


  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    _iopx = new IOPanelProxy();
    MainframeInterface mfInterface = new MainframeProxy();
    MainframeCiv mfCiv = new MainframeCiv(mfInterface);
    _cp = CommandParser.getInstance(mfCiv);
    _cmdFac = new CommandFactory(mfCiv);
    //_cp = CommandParser.getInstance(_iopx);
    //_cmdFac = new CommandFactory(_cp);
    _bdciv = BuildingDisplayCiv.getInstance(); // for CmdEnter context
    _bdciv.setOutput(mfInterface);
    _mockbdciv = _bdciv.new MockBldgCiv();

    // Get a list of all buildings to enter
    _regfac = RegistryFactory.getInstance();
    _breg = (BuildingRegistry) _regfac.getRegistry(RegKey.BLDG);
    _bList = _breg.getBuildingList();
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    _bList = null;
    _regfac = null;
    _cmdFac = null;
    _cp = null;
    _iopx = null;

    _mockbdciv = null;
    _bdciv = null;

    // Shutdown building registry created by CmdEnter
    _breg.closeRegistry();
    _breg = null;
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);

    _cmdEnter = (CmdEnter) _cmdFac.createCommand("CmdEnter");
    _mock = _cmdEnter.new MockCmdEnter();
    
    // Ensure that current building is null to start
    _bdciv.setCurrentBuilding(null);
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    // Clear targetBldg from CmdEnter
    _mock.clearTargetBldg();
    _mock = null;
    _cmdEnter = null;

    // Ensure that current building is null to end
    _bdciv.setCurrentBuilding(null);
    _mockbdciv.setInsideBldg(false);

    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // =================================================
  // BEGIN TESTING
  // =================================================

  /** Normal verify CmdEnter constructor */
  @Test
  public void ctorVerified()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    int delay = 0;
    int duration = 10;
    String cmdfmt = "ENTER [Building Name]";
    MsgCtrl.msgln("\tCommand format: " + _mock.getCmdFormat());
    
    assertEquals(delay, _mock.getDelay());
    assertEquals(duration, _mock.getDuration());
    assertEquals(cmdfmt, _mock.getCmdFormat());
  }


  /** Normal CmdEnter given building(s) */
  @Test
  public void initValidBuilding()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    List<String> bNames = new ArrayList<String>();

    // For each building, enter it and check its attributes
    for (int k = 0; k < _bList.size(); k++) {
      String name = _bList.get(k).getName();
      bNames.add(0, name);
      MsgCtrl.msgln("\tEntering Building:\t" + bNames.get(0));
      assertTrue(_cmdEnter.init(bNames));

      // Verify target building
      Building tBldg = _mock.getTargetBldg();
      assertEquals(tBldg, _bList.get(k));
      // Clear out arglist
      bNames.remove(0);
    }
    MsgCtrl.msgln("\tAll buildings entered successfully.");
  }


  /** Normal CmdEnter default (current) building */
  @Test
  public void initWithoutParms()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    List<String> bNames = new ArrayList<String>();

    // Set first building in registry to the current building (in context object)
    Building b = _bList.get(0);
    assertNotNull(b);
    _bdciv.setCurrentBuilding(b);

    // Now try to enter current Building without a parm
    bNames.clear();
    MsgCtrl.msgln("\tEntering current Building (target Building) " + b.getName());
    assertTrue(_cmdEnter.init(bNames));
    // Verify
    Building tBldg = _mock.getTargetBldg();
    assertTrue(_cmdEnter.init(bNames));
    assertEquals(b.getName(), tBldg.getName());
  }


  /** Error: Enter invalid building for ERR_NOBLDG*/
  @Test
  public void initInvalidBuilding()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    List<String> bNames = new ArrayList<String>();
    // Place an invalid building as the parm
    bNames.add("Winery");
    MsgCtrl.errMsg("\tExpected error: ");
    assertFalse(_cmdEnter.init(bNames));
  }    

  /** Error: Trying to enter one building from inside another gives ERRMSG_JUMPBLDG */
  @Test
  public void initJumpBuilding()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Set context to be inside valid building: Jail
    List<String> bNames = new ArrayList<String>();
    
    Building b = _breg.getBuilding("Jail");
    bNames.add("Jail");

    _bdciv.setCurrentBuilding(b);
    _mockbdciv.setInsideBldg(true);
    
    MsgCtrl.errMsg("\tExpected error: ");
    assertFalse(_cmdEnter.init(bNames));
   
  }    


}   // end of TestCmdEnter class

