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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.Chronos;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.Adventurer;
import civ.Adventurer.MockAdventurer;

/**
 * Test the Adventurer (Launcher) class: ensure that all Registries are created.
 * 
 * @author alancline
 * @version July 19, 2014 // ABC original
 * 
 */
public class TA00a_Initialize
{
  static private RegistryFactory _rf;
  private Adventurer _launcher;
  private MockAdventurer _mock;

  /**
   * INFO ONLY: Keys used by RegistryFactory public enum RegKey { ADV("Adventure"),
   * BLDG("Building"), ITEM("Item"), NPC("NPC"), OCP("Occupation"), SKILL("Skill"), TOWN("Town");
   */

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
    assertTrue(Chronos.CHRONOS_ROOT != null);
    assertTrue(Chronos.ADV_RESOURCES_PATH != null);
    assertTrue(Chronos.RESOURCES_PATH != null);
    assertTrue(Chronos.IMAGE_PATH != null);
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
  }

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
    _rf = null;
    _mock = null;
    _launcher = null;
  }


  // ============================================================
  // Integration Test
  // ============================================================

  /** Run the main to create the registries, the mainframe and mainframe civ */
  @Test
  public void testMainNoRegs()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.msgln(this, "\t testMainNoRegs");

    // SETUP: Ensure that there are as many regfiles as they are reg keys
    int keynum = RegKey.values().length;
    int pathnum = paths.length;
    MsgCtrl.msg("\tNumber of keys = " + keynum);
    MsgCtrl.msgln("\tNumber of file paths = " + pathnum);
    assertEquals(keynum, pathnum);
    // Ensure that no registry files exist
    deleteRegistryFiles();

    // DO create the registries
    _mock.initRegistries();
    
    // VERIFY all registry files created
    assertTrue(RegistryFilesExist());

    // VERIFY all registries exist: get number objects in RegistryFactory map
    assertTrue(keynum == _rf.getNumberOfRegistries());

    // TEARDOWN: close all registries
    for (RegKey key : RegKey.values()) {
      _rf.closeRegistry(key);
    }
  }


  /** Run the main to create the registries when the files already exist */
  @Test
  public void testMainWithRegs()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.msgln(this, "\t testMainWithRegs");

    // SETUP: Ensure that there are as many regfiles as they are reg keys
    int keynum = RegKey.values().length;
    int pathnum = paths.length;
    MsgCtrl.msg("\tNumber of keys = " + keynum);
    MsgCtrl.msgln("\tNumber of file paths = " + pathnum);
    assertEquals(keynum, pathnum);

    // Create all registry files to ensure that they exist
    _mock.initRegistries();
    assertTrue(RegistryFilesExist());

    // Try creating the registries when they already exist 
    _mock.initRegistries();

    // VERIFY all registry files created
    assertTrue(RegistryFilesExist());

    // VERIFY all registries exist: get number objects in RegistryFactory map
    assertTrue(keynum == _rf.getNumberOfRegistries());

    // TEARDOWN: close all registries
    for (RegKey key : RegKey.values()) {
      _rf.closeRegistry(key);
    }
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
      retval = (doesExist(s) && (f.length() > 0));
      if (retval == false) {
        break;
      }
    }
    return retval;
  }

  
//  /** Create almost-empoty Registry files for testing */
//  private void createFiles()
//  {
//    for (String s : paths) {
//      FileOutputStream fos = new FileOutputStream(s);
//      fos.wr
//    }
//  }

  
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


} // end of TestLauncher class



