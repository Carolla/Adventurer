/**
 * TA00a_Initalize.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.Chronos;
import chronos.pdc.registry.HeroRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import mylib.MsgCtrl;

/**
 * Integration test for initializing the application class: ensure that all Registries are created.
 * 
 * @author Al Cline
 * @version July 19, 2014 // ABC original <br>
 *          April 5 2016 // updated for HeoRegistry as only persistent registry <br>
 */
public class TA00a_Initialize // extends IntegrationTest
{
  /**
   * INFO ONLY: Keys used by RegistryFactory public enum RegKey { ADV("Adventure"),
   * BLDG("Building"), HERO ("Hero"), ITEM("Item"), NPC("NPC"), OCP("Occupation"), SKILL("Skill"),
   * TOWN("Town");
   */

  protected static final RegistryFactory _regFactory = new RegistryFactory();

  private final String[] paths = {Chronos.AdventureRegPath, Chronos.BuildingRegPath,
      Chronos.HeroRegPath, Chronos.ItemRegPath, Chronos.NPCRegPath, Chronos.OcpRegPath,
      Chronos.SkillRegPath, Chronos.TownRegPath,};


  // ============================================================
  // Fixtures
  // ============================================================
  @BeforeClass
  public static void setUpBeforeClass()
  {
  }

  @AfterClass
  public static void tearDownAfterClass()
  {
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
  }


  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);

    // This registry has persistence, so must be closed
    HeroRegistry heroReg = (HeroRegistry) _regFactory.getRegistry(RegKey.HERO);
    heroReg.close();
}


  // ============================================================
  // Integration Test
  // ============================================================

  @Test
  public void testInitRegs()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    _regFactory.initRegistries();
    assertEquals(paths.length, RegKey.values().length);
    assertTrue(paths.length == _regFactory.getNumberOfRegistries());
  }


  /** Run the main to create the registries, the mainframe and mainframe civ */
  // @Test
  public void testMainNoRegs()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // VERIFY all registry files created
    assertTrue(RegistryFilesExist());

    // VERIFY all registries exist: get number objects in RegistryFactory map
    assertTrue(RegKey.values().length == _regFactory.getNumberOfRegistries());
  }


  /** Run the main to create the registries when the files already exist */
  // @Test
  public void testMainWithRegs()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // VERIFY all registry files created
    assertTrue(RegistryFilesExist());
  }


  // ============================================================
  // Helper Methods
  // ============================================================

  // TODO: Fix this method stub: add its implementation
  /** Check that all Registry files exist and are of non-zero length */
  private boolean RegistryFilesExist()
  {
    return true;
  }

} // end of TA00a_Initialize class


