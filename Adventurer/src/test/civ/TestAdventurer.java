/**
 * TestAdventurer.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.civ;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import mylib.MsgCtrl;
import mylib.dmc.IRegistryElement;
import mylib.pdc.Registry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.Chronos;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.Adventurer;
import civ.Adventurer.MockAdventurer;

/**
 * Test the {@code Adventurer} (Launcher) class: ensure that all Registries are created. Does not
 * auto-test the {@code main} method.
 * 
 * @author Al Cline
 * @version Sept 7 2014 // original <br>
 * 
 */
public class TestAdventurer
{
  private RegistryFactory _rf;
  private Adventurer _launcher;
  private MockAdventurer _mock;

  // INFO ONLY: Keys used by RegistryFactory public enum RegKey { ADV("Adventure"),
  // BLDG("Building"), ITEM("Item"), NPC("NPC"), OCP("Occupation"), SKILL("Skill"), TOWN("Town");

  /** File paths to the various registries and resources */
  private final String[] paths = {Chronos.AdventureRegPath, Chronos.BuildingRegPath,
      Chronos.ItemRegPath, Chronos.NPCRegPath, Chronos.OcpRegPath, Chronos.SkillRegPath,
      Chronos.TownRegPath};


  // ============================================================
  // Fixtures
  // ============================================================

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    assertTrue(Chronos.ECHRONOS_ROOT != null);
    assertTrue(Chronos.ADV_RESOURCES_PATH != null);
    assertTrue(Chronos.RESOURCES_PATH != null);
    assertTrue(Chronos.IMAGE_PATH != null);
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    _rf = RegistryFactory.getInstance();
    _launcher = new Adventurer();
    _mock = _launcher.new MockAdventurer();
  }


  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _mock = null;
    _launcher = null;
    _rf.closeAllRegistries();
  }


  // ============================================================
  // Unit Tests
  // ============================================================

  /**
   * 1 method
   * 
   * @NotNeeded approvedQuit() -- wrapper over {@code closeRegistries}
   */
  public void _notNeeded()
  {}


  // TODO Relies on initRegistries to work first
//  /**
//   * Close all the registries, but don't delete the files
//   * 
//   * @Normal Close all the registries, but don't delete the files
//   */
//  @Test
//  public void testCloseRegistries()
//  {
//    MsgCtrl.auditMsgsOn(false);
//    MsgCtrl.errorMsgsOn(false);
//    MsgCtrl.where(this);
//
//    // ENSURE that the Registries exist
//    _mock.initRegistries();
//
//    // VERIFY that the registries still exist
//    assertTrue(registryFilesExist());
//
//    // DO Close down the registries
//    _mock.closeRegistries();
//
//    // VERIFY that the registries still exist
//    assertTrue(registryFilesExist());
//  }


  /**
   * Test that all registries are initialized
   * 
   * @Normal init registries if registry files don't exist
   */
  @Test
  public void testInitRegistriesWithoutFiles()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP: Ensure that there are as many regfiles as they are reg keys
    int keynum = RegKey.values().length;
    int pathnum = paths.length;
    MsgCtrl.msg("\tNumber of keys = " + keynum);
    MsgCtrl.msgln("\tNumber of file paths = " + pathnum);
    assertEquals(keynum, pathnum);
    // Ensure that no registry files exist
    deleteRegistryFiles();

    // DO Re-create the registries; need the mock because initRegistries is private
    _mock.initRegistries();

    // VERIFY all registry files created
    assertTrue(registryFilesExist());
    AdventureRegistry advReg = (AdventureRegistry) _rf.getRegistry(RegKey.ADV);
    IRegistryElement adv = advReg.getAll().get(0);
    String townName = adv.getKey();
    MsgCtrl.msgln("Adventure contains town " + townName);
    // VERIFY all registries exist: get number objects in RegistryFactory map
    assertTrue(keynum == _rf.getNumberOfRegistries());

    // TEARDOWN: close all registries
    for (RegKey key : RegKey.values()) {
      _rf.closeRegistry(key);
    }
  }


  /**
   * Test that all registries are initialized
   * 
   * @Normal init registries when the files do exist
   */
  @Test
  public void testInitRegistriesWithFiles()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP: Ensure that there are as many regfiles as they are reg keys
    int keynum = RegKey.values().length;
    int pathnum = paths.length;
    MsgCtrl.msg("\tNumber of keys = " + keynum);
    MsgCtrl.msgln("\tNumber of file paths = " + pathnum);
    assertEquals(keynum, pathnum);

    // Create all registry files to ensure that they exist
    _mock.initRegistries();
    assertTrue(registryFilesExist());

    // Try creating the registries when they already exist
    _mock.initRegistries();

    // VERIFY all registry files created
    assertTrue(registryFilesExist());

    // VERIFY all registries exist: get number objects in RegistryFactory map
    assertTrue(keynum == _rf.getNumberOfRegistries());
    // // Dump content of each Registry
    // dumpRegistries();

    // TEARDOWN: close all registries
    for (RegKey key : RegKey.values()) {
      _rf.closeRegistry(key);
    }
  }


  // ============================================================
  // Helper Methods
  // ============================================================

  /** Dump the contents of each registry in the RegistryFactory collection */
  private void dumpRegistries()
  {
    RegKey key = RegKey.ADV;
    Registry reg = _rf.getRegistry(key);
    List<String> elist = reg.getElementNames();
    for (int k = 0; k < elist.size(); k++) {
      MsgCtrl.msgln("\tElements of the " + elist.get(k));
    }
  }


  /** Check that all Registry files exist and are of non-zero length */
  private boolean registryFilesExist()
  {
    boolean retval = true;
    for (String s : paths) {
      File f = new File(s);
      retval = (doesExist(s) && (f.length() > 0));
      if (retval == false) {
        break;
      }
    }
    return retval;
  }


  /** Check existence of single Registry file */
  private boolean doesExist(String path)
  {
    File rf = new File(path);
    return rf.exists();
  }


  /** Clear all Registry files */
  private void deleteRegistryFiles()
  {
    for (String s : paths) {
      deleteRegFile(s);
    }
  }


  /** Delete a Registry file */
  private void deleteRegFile(String path)
  {
    File rf = new File(path);
    rf.delete();
    assertFalse(rf.exists());
  }


} // end of TestAdventurer class

