/**
 * TestRegistryFactory.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import mylib.MsgCtrl;
import mylib.pdc.Registry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.Chronos;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;


/**
 * {@code RegistryFactory} is an non-instantable static class comprised <i>mostly</i> of static
 * methods.
 * 
 * @author Al Cline
 * @version Jul 19, 2014 // original <br>
 *          Jul 24, 2014 // refactored to allow for registries not residing in the common location <br>
 *          Sep 20, 2014 // test removeAllRegistries <br>
 */
public class TestRegistryFactory
{
  static private RegistryFactory _rf = null;

  // ============================================================
  // Fixtures
  // ============================================================

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {}

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {}

  @Before
  public void setUp() throws Exception
  {
    _rf = RegistryFactory.getInstance();
    assertNotNull(_rf);
  }

  @After
  public void tearDown() throws Exception
  {
    _rf.closeAllRegistries();
    assertEquals(_rf.getNumberOfRegistries(), 0);
    _rf = null;
    // Turn off messaging at end of each test
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ============================================================
  // Begin Tests
  // ============================================================

  /**
   * @NotNeeded {@code RegistryFactory()} -- wrapper method <br>
   * @NotNeeded {@code getInstance()} -- wrapper method <br>
   */
  public void _testsNotNeeded()
  {}

  /*
   * closeAllRegistries() deleteAllRegistries() getExisting(RegKey)
   */

  /**
   * Close a registry and remove it from the factory collection. Do not delete the file.
   * 
   * @Normal close an existing registry
   */
  @Test
  public void testCloseRegistry()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP: ensure that registry already exists and is open
    _rf.getRegistry(RegKey.ITEM);
    int regnum = _rf.getNumberOfRegistries();
    File regfile = new File(Chronos.ItemRegPath);
    assertTrue(regfile.exists());

    // DO
    MsgCtrl.msgln("Number of registries before closing = " + regnum);
    _rf.closeRegistry(RegKey.ITEM);
    MsgCtrl.msgln("Number of registries after closing = " + regnum);

    // VERIFY
    assertEquals(_rf.getNumberOfRegistries(), regnum - 1);
    assertTrue(regfile.exists()); // file did not get deleted
  }


  /**
   * Close a registry and remove it from the factory collection. Do not delete the file.
   * 
   * @Error close a registry that is closed (but file may or may not exist)
   */
  @Test
  public void testCloseRegistry_Empty()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP: ensure that registry is exists and is closed
    _rf.getRegistry(RegKey.ITEM);
    int regnum = _rf.getNumberOfRegistries();
    _rf.closeRegistry(RegKey.ITEM);
    assertEquals(_rf.getNumberOfRegistries(), regnum - 1);

    // DO: try closing it again
    _rf.closeRegistry(RegKey.ITEM);

    // VERIFY: size does not decrease; no runtime error; nothing else happens
    assertEquals(_rf.getNumberOfRegistries(), regnum - 1);

  }

  /**
   * Create a singleton registry given a registry type, using Class reflection
   * 
   * @Normal create a valid registry
   * @Error passing a non-valid enum RegKey type will cause a compile error
   * @Null passing a null enum RegKey type will NOT cause a compile error
   */
  @Test
  public void testCreateRegistry()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP: ensure that registry doesn't already exist
    int regnum = _rf.getNumberOfRegistries();
    File regfile = new File(Chronos.ItemRegPath);
    regfile.delete();
    assertFalse(regfile.exists());

    // DO
    _rf.createRegistry(RegKey.ITEM);

    // VERIFY
    assertEquals(_rf.getNumberOfRegistries(), regnum + 1);
    assertTrue(regfile.exists());

    // Error pass in a null regkey
    assertNull(_rf.createRegistry(null));

  }


  /**
   * How many registries exist in the Factory?
   */
  @Test
  public void testGetNumberOfRegistries()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP: Clear all registry entries
    _rf.closeAllRegistries();

    // DO: create two registries
    _rf.getRegistry(RegKey.ITEM);
    _rf.getRegistry(RegKey.SKILL);
    // VERIFY: Ensure that only two registries exist
    assertEquals(_rf.getNumberOfRegistries(), 2);

    // DO: create a third registry
    _rf.getRegistry(RegKey.ADV);
    // VERIFY: Ensure that three registries exist
    assertEquals(_rf.getNumberOfRegistries(), 3);

    // DO: remove two registries
    _rf.closeRegistry(RegKey.SKILL);
    _rf.closeRegistry(RegKey.ITEM);

    // VERIFY: Ensure that only one registry exists
    assertEquals(_rf.getNumberOfRegistries(), 1);

    // DO: last registrys
    _rf.closeRegistry(RegKey.ADV);
    // VERIFY: Ensure that only one registry exists
    assertEquals(_rf.getNumberOfRegistries(), 0);

    // TEARDOWN
    // Nothing to do
  }


  /**
   * Get a Registry, and if it doesn't exist, create it and add the entry to the factory's map
   * 
   * @Normal Get a registry that does not yet exist
   */
  @Test
  public void testGetRegistry_Uncreated()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP: ensure that registries to be created does not yet exist
    _rf.closeAllRegistries();
    assertEquals(_rf.getNumberOfRegistries(), 0);
    File regfile1 = new File(Chronos.AdventureRegPath);
    regfile1.delete();
    assertFalse(regfile1.exists());

    // DO:
    _rf.getRegistry(RegKey.ADV);

    // VERIFY: factory has a new registry and file exists
    assertTrue(regfile1.exists());
    assertEquals(_rf.getNumberOfRegistries(), 1);

    // TEARDOWN
  }


  /**
   * Get a Registry, and if it doesn't exist, create it and add the entry to the factory's map
   * 
   * @Normal Get a registry that already exists
   */
  @Test
  public void testGetRegistry_Exists()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP: ensure that registry to be created already exists
    assertEquals(_rf.getNumberOfRegistries(), 0);
    Registry testreg = _rf.getRegistry(RegKey.ADV);
    File regfile = new File(Chronos.AdventureRegPath);
//    assertTrue(regfile.exists());
    assertEquals(_rf.getNumberOfRegistries(), 1);

    // DO:
    Registry testreg2 = _rf.getRegistry(RegKey.ADV);

    // VERIFY: factory has same registry file exists
    assertTrue(regfile.exists());
    assertEquals(_rf.getNumberOfRegistries(), 1);
    assertEquals(testreg, testreg2);

    // TEARDOWN
    // Nothing to do
  }


  /**
   * Get a Registry, and if it doesn't exist, create it and add the entry to the factory's map
   * 
   * @Null use null to request a null registry returns null
   */
  @Test
  public void testGetRegistry_Errors()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP
    assertEquals(_rf.getNumberOfRegistries(), 0);

    // DO: Null request
    Registry testreg = _rf.getRegistry(null);
    
    // VERIFY
    assertNull(testreg);
    assertEquals(_rf.getNumberOfRegistries(), 0);

    // TEARDOWN
    // None

  }

  // ============================================================
  // Helper Methods
  // ============================================================



} // end of TestRegistryFactory
