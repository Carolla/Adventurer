/**
 * TestBuildingRegistry.java Copyright (c) 2017, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com.
 */

package chronos.test.pdc.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.buildings.Building;
import chronos.pdc.registry.BuildingRegistry;
import mylib.MsgCtrl;

/**
 * @author Al Cline
 * @version August 9, 2017 // original <br>
 */
public class TestBuildingRegistry
{
  private BuildingRegistry _bldgReg;

  // These are the building kept in the registry
  static private final String[] _buildings = {
      "Inn", // Ugly Ogre Inn
      "Store", // Rat's Pack
      "Jail", // Jail
      "Bank", // The Bank
      "Stables", // Larry's Livery
      "FightersGuild", // Stadium
      "RoguesGuild", // Rouge's Tavern
      "ClericsGuild", // Monastery
      "WizardsGuild" // Arcaneum
  };

  static private ArrayList<String> _bldgNames;

  
  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    _bldgNames = new ArrayList<String>();
    for (int k=0; k < _buildings.length; k++) {
      _bldgNames.add(_buildings[k]);
    }
    assertEquals(_buildings.length, _bldgNames.size());
  }

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    _bldgNames = null;
  }

  @Before
  public void setUp()
  {
    _bldgReg = new BuildingRegistry();
    assertNotNull(_bldgReg);
    // _mock = _bldgReg.new MockBuildingRegistry();
    // assertNotNull(_mock);
  }

  @After
  public void tearDown()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _bldgReg = null;
  }


  // ===============================================================================
  // BEGIN TESTING
  // ===============================================================================

  /**
   * @Normal.Test BuildingRegistry() -- confirm proper data in registry
   */
  @Test
  public void testCtor()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    
    MsgCtrl.msgln("\t BuildingRegistry created with " + _bldgReg.size());
    ArrayList<Building> bldgs = (ArrayList<Building>) _bldgReg.getAll();
    for (Building b : bldgs) {
      MsgCtrl.msgln("\t " + b.getName() + " (" + b.getProprietor() + ")");
    }
    assertEquals(bldgs.size(), _bldgReg.size());
  }

  
  /**
   * @Not.Needed Building getBuilding(String) -- testing in testCtor
   */
  @Test
  public void testGetBuilding()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.PRIMITIVE);
  }


  /**
   * @Not.Needed List getBuildingList() -- tested in testCtor
   */
  @Test
  public void testGetBuildingList()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.PRIMITIVE);
  }


  /**
   * @Not.Needed void initialize() -- tested in testCtor
   */
  @Test
  public void testInitialize()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.PRIMITIVE);
  }


} // end of TestBuildingRegistry class
