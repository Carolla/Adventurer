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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.civ.DefaultUserMsg;
import chronos.pdc.Chronos;
import chronos.pdc.command.Scheduler;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;

/**
 * Test the Adventurer (Launcher) class: ensure that all Registries are created.
 * 
 * @author Al Cline
 * @version July 19, 2014 // ABC original
 * 
 */
public class TA00a_Initialize
{
  static private RegistryFactory _rf;
  /**
   * INFO ONLY: Keys used by RegistryFactory public enum RegKey { ADV("Adventure"),
   * BLDG("Building"), ITEM("Item"), NPC("NPC"), OCP("Occupation"), SKILL("Skill"), TOWN("Town");
   */

  private final String[] paths = {Chronos.AdventureRegPath, Chronos.BuildingRegPath,
      Chronos.ItemRegPath, Chronos.NPCRegPath, Chronos.OcpRegPath, Chronos.SkillRegPath,
      Chronos.TownRegPath, Chronos.HeroRegPath};


  // ============================================================
  // Fixtures
  // ============================================================

  @BeforeClass
  public static void setUpBeforeClass()
  {
    _rf = new RegistryFactory();
    _rf.initRegistries(new Scheduler(new DefaultUserMsg()));
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    assertEquals(paths.length, RegKey.values().length);
    assertTrue(paths.length == _rf.getNumberOfRegistries());
  }


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
  // Integration Test
  // ============================================================

  /** Run the main to create the registries, the mainframe and mainframe civ */
  @Test
  public void testMainNoRegs()
  {
    // Ensure that no registry files exist
    deleteRegistryFiles();

    // DO create the registries
    _rf = new RegistryFactory();
    _rf.initRegistries(new Scheduler(new DefaultUserMsg()));

    // VERIFY all registry files created
    assertTrue(RegistryFilesExist());

    // VERIFY all registries exist: get number objects in RegistryFactory map
    assertTrue(RegKey.values().length == _rf.getNumberOfRegistries());
  }


  /** Run the main to create the registries when the files already exist */
  @Test
  public void testMainWithRegs()
  {
    // VERIFY all registry files created
    assertTrue(RegistryFilesExist());
  }


  // ============================================================
  // Helper Methods
  // ============================================================

  /** Check that all Registry files exist and are of non-zero length */
  private boolean RegistryFilesExist()
  {
    boolean retval = true;
    for (String s : paths) {
      File f = new File(s);
      retval &= f.exists() && (f.length() > 0);
    }
    return retval;
  }

  /** Clear all Registry files */
  private void deleteRegistryFiles()
  {
    for (String s : paths) {
      File rf = new File(s);
      rf.delete();
      assertFalse(rf.exists());
    }
  }


} // end of TestLauncher class



