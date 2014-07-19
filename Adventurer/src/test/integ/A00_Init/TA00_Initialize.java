/**
 * TA00_Initialize.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package test.integ.A00_Init;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import mylib.pdc.Registry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.Chronos;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.MainframeCiv;


/**
 * Verify that the Adventure program initializes properly. <br>
 * Loads all registries from the {@code RegistryFactory}
 * 
 * @author Alan Cline
 * @version Nov 2, 2013 // original <br>
 *          Jul 18, 2014 // updated after architectural refactoring <br>
 */
public class TA00_Initialize
{
  private static MainframeCiv _advCiv = null;


  // ============================================================
  // Fixtures
  // ============================================================

  /**
   * @throws java.lang.Exception if something unexpected happens
   */
  @BeforeClass
  static public void setUpOnce() throws Exception
  {}

  /**
   * @throws java.lang.Exception if something unexpected happens
   */
  @Before
  public void setUp() throws Exception
  {}

  /**
   * @throws java.lang.Exception if something unexpected happens
   */
  @After
  public void tearDown() throws Exception
  {}

  /**
   * @throws java.lang.Exception if something unexpected happens
   */
  @AfterClass
  static public void teardownAfter() throws Exception
  {}


  // ============================================================
  // Integration Tests
  // ============================================================

  /**
   * Ensure that all Registries objects and fikles are created, and that the MainframeCiv has
   * non-null references to each.
   */
  @Test
  public void initRegistries()
  {
    // SETUP: no registries open, no regitry files exist
    Registry advreg = RegistryFactory.getRegistry(RegKey.ADV);
    deleteAllRegistries();
    assertNull(advreg);

    verifyNoRegFilesExist();

    // DO: call Adventurer.initRegistries to create all Registry objects and files
    _advCiv = new MainframeCiv(null);

    // VERIFY: the correct number of files exist
    verifyAllRegFilesExist();

    // TEARDOWN: Remove all registries and files
    deleteAllRegistries();
    verifyNoRegFilesExist();
  }


  /**
   * Ensure that when Adventurer is launched, the MainFrameCiv is created
   */
  @Test
  public void createMFCiv()
  {
    // SETUP
    // DO
    // VERIFY
    // TEARDOWN
  }



  // ============================================================
  // Private Helper Methods
  // ============================================================

  /** Close all registries, and delete registry singletons */
  private void deleteAllRegistries()
  {
    Registry reg = RegistryFactory.getExisting(RegKey.ADV);
    reg.closeRegistry();
    // AdventureRegistry.getInstance().deleteRegistry();
    // AdvHelpRegistry.getInstance().deleteRegistry();
    // BuildingRegistry.getInstance().deleteRegistry();
    // ItemRegistry.getInstance().deleteRegistry();
    // ((NPCRegistry) AdvRegistryFactory.getRegistry(RegKey.NPC)).deleteRegistry();
    // ((OccupationRegistry) AdvRegistryFactory.getRegistry(RegKey.OCP)).deleteRegistry();
    // (SkillRegistry) AdvRegistryFactory.getRegistry(RegKey.SKILL).deleteRegistry();
    // TownRegistry.getInstance().deleteRegistry();
  }


  /** Remove all registry files */
  private void deleteAllRegistryFiles()
  {
    // Delete each of the Registries directly, without going through the Registry objects
    new File(Chronos.AdventureRegPath).delete();
    new File(Chronos.AdventureHelpRegPath).delete();
    new File(Chronos.BuildingRegPath).delete();
    new File(Chronos.ItemRegPath).delete();
    new File(Chronos.NPCRegPath).delete();
    new File(Chronos.OcpRegPath).delete();
    new File(Chronos.SkillRegPath).delete();
    new File(Chronos.TownRegPath).delete();
  }


  // ============================================================
  // Private Helper Methods
  // ============================================================

  // /** Verify that not one Registry is null; all registries are instantiated */
  // private void verifyNoRegIsNull()
  // {
  // assertTrue(_mock.isNotNullReg(reg))
  // }


  /** Verify that the Registry files exist but are not open for reading */
  private void verifyAllRegFilesExist()
  {
    File reg = new File(Chronos.AdventureRegPath);
    assertTrue(Chronos.AdventureRegPath + " does not exist", reg.exists());

    reg = new File(Chronos.AdventureHelpRegPath);
    assertTrue(Chronos.AdventureHelpRegPath + " does not exist", reg.exists());

    reg = new File(Chronos.BuildingRegPath);
    assertTrue(Chronos.BuildingRegPath + " does not exist", reg.exists());

    reg = new File(Chronos.ItemRegPath);
    assertTrue(Chronos.ItemRegPath + " does not exist", reg.exists());

    reg = new File(Chronos.NPCRegPath);
    assertTrue(Chronos.NPCRegPath + " does not exist", reg.exists());

    reg = new File(Chronos.OcpRegPath);
    assertTrue(Chronos.OcpRegPath + " does not exist", reg.exists());

    reg = new File(Chronos.SkillRegPath);
    assertTrue(Chronos.SkillRegPath + " does not exist", reg.exists());

    reg = new File(Chronos.TownRegPath);
    assertTrue(Chronos.TownRegPath + " does not exist", reg.exists());
  }


  /** Verify that the Registry files do not exist */
  private void verifyNoRegFilesExist()
  {
    File reg = new File(Chronos.AdventureRegPath);
    assertFalse(Chronos.AdventureRegPath + " unexpectedly exists", reg.exists());

    reg = new File(Chronos.AdventureHelpRegPath);
    assertFalse(Chronos.AdventureHelpRegPath + "  unexpectedly exists", reg.exists());

    reg = new File(Chronos.BuildingRegPath);
    assertFalse(Chronos.BuildingRegPath + "  unexpectedly exists", reg.exists());

    reg = new File(Chronos.ItemRegPath);
    assertFalse(Chronos.ItemRegPath + "  unexpectedly exists", reg.exists());

    reg = new File(Chronos.NPCRegPath);
    assertFalse(Chronos.NPCRegPath + "  unexpectedly exists", reg.exists());

    reg = new File(Chronos.OcpRegPath);
    assertFalse(Chronos.OcpRegPath + "  unexpectedly exists", reg.exists());

    reg = new File(Chronos.SkillRegPath);
    assertFalse(Chronos.SkillRegPath + "  unexpectedly exists", reg.exists());

    reg = new File(Chronos.TownRegPath);
    assertFalse(Chronos.TownRegPath + "  unexpectedly exists", reg.exists());
  }



} // end of TA00_Initialize class
