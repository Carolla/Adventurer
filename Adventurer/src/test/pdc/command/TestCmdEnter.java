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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdc.command.CmdEnter;
import pdc.command.CmdEnter.MockCmdEnter;
import pdc.command.CommandFactory;
import test.integ.IOPanelProxy;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.CommandParser;

/**
 * @author Al Cline
 * @version May 4, 2015 // original <br>
 */
public class TestCmdEnter
{
   private static CommandParser _cp = null; 
   private static IOPanelProxy _iopx = null;
   private static CommandFactory _cmdFac = null;
   
   private CmdEnter _cmdEnter = null;
   private MockCmdEnter _mock = null;
   
   /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    _iopx = new IOPanelProxy();
    assertNotNull(_iopx);
    _cp = CommandParser.getInstance(_iopx);
    assertNotNull(_cp);
    _cmdFac = new CommandFactory(_cp); 
    assertNotNull(_cmdFac);
        
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    _cmdFac = null;
    _cp = null;
    _iopx = null;
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    _cmdEnter = (CmdEnter) _cmdFac.createCommand("CmdEnter");
    assertNotNull(_cmdEnter);
    _mock = _cmdEnter.new MockCmdEnter();
    assertNotNull(_mock);
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    _mock = null;
    _cmdEnter = null;
    // Shutdown building registry created by CmdEnter
    RegistryFactory regfac = RegistryFactory.getInstance();
    BuildingRegistry breg = (BuildingRegistry) regfac.getRegistry(RegKey.BLDG);
    breg.closeRegistry();
  }

  
  //=================================================
  //  BEGIN TESTING
  //=================================================
      
  /** Normal verify CmdEnter constructor */
  @Test
  public void CtorVerifiedn()
  {
    int delay = 0;
    int duration = 10;
    
    assertEquals(delay, _mock.getDelay());
    assertEquals(duration, _mock.getDuration());
  }

  /** Normal CmdEnter given building */
  @Test
  public void initBuildingGiven()
  {
    List<String> bName = new ArrayList<String>();
    bName.add("Jail");
    
    Building tBldg = _mock.getTargetBldg();
    assertNull(tBldg);
    assertTrue(_cmdEnter.init(bName));
    // Verify target and current building
    tBldg = _mock.getTargetBldg();
    assertEquals(tBldg.getName(), bName.get(0));
    Building curBldg = _mock.getCurrentBldg();
    assertEquals(curBldg.getName(), bName.get(0));
  }


}
