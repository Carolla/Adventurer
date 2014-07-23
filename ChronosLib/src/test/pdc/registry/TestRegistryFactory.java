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
 * {@code RegistryFactory} is an non-instantable static class. This will be a challenge.
 * 
 * @author alancline
 * @version Jul 19, 2014 // original <br>
 */
public class TestRegistryFactory
{

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
    // each test turns on its own messaging when needed
  }

  @After
  public void tearDown() throws Exception
  {
    // Turn off messaging at end of each test
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ============================================================
  // Tests
  // ============================================================
  
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
    MsgCtrl.msgln(this, ": testGetRegistry()");

    // SETUP: ensure that registry to be created does not yet exist
    File regfile = new File(Chronos.AdventureRegPath);
    regfile.delete();
    assertFalse(regfile.exists());

    // DO:
    Registry testreg = RegistryFactory.getRegistry(RegKey.ADV);

    // VERIFY: factory has a new registry and file exists
    assertTrue(regfile.exists());
    assertEquals(RegistryFactory.getNumberOfRegistries(), 1);

    // TEARDOWN: close all registries opened
    testreg.closeRegistry();
  }

  
  /**
   * Get a Registry, and if it doesn't exist, create it and add the entry to the factory's map
   * 
   * @Normal Get a registry that already exists
   */
  @Test
  public void testGetRegistry_Exists()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.msgln(this, ": testGetRegistry()");

    // SETUP: ensure that registry to be created already exists
    assertEquals(RegistryFactory.getNumberOfRegistries(), 0);
    Registry testreg = RegistryFactory.getRegistry(RegKey.ADV);
    File regfile = new File(Chronos.AdventureRegPath);
    assertTrue(regfile.exists());
    assertEquals(RegistryFactory.getNumberOfRegistries(), 1);

    // DO:
    Registry testreg2 = RegistryFactory.getRegistry(RegKey.ADV);

    // VERIFY: factory has same registry file exists
    assertTrue(regfile.exists());
    assertEquals(RegistryFactory.getNumberOfRegistries(), 1);
    assertEquals(testreg, testreg2);
    
    // TEARDOWN: close all registries opened
    testreg.closeRegistry();
    testreg2.closeRegistry();
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
    MsgCtrl.msgln(this, ": testGetRegistry()");

    // DO: Null request
    assertEquals(RegistryFactory.getNumberOfRegistries(), 0);
    Registry testreg = RegistryFactory.getRegistry(null);
    assertNull(testreg);
    assertEquals(RegistryFactory.getNumberOfRegistries(), 0);
    
  }

  // ============================================================
  // Helper Methods
  // ============================================================


} // end of TestRegistryFactory
