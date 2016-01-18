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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import mylib.MsgCtrl;
import mylib.pdc.Registry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.Chronos;
import chronos.civ.DefaultUserMsg;
import chronos.pdc.command.Scheduler;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import chronos.pdc.registry.SkillRegistry;


/**
 * {@code RegistryFactory} is an non-instantiable static class comprised <i>mostly</i> of static
 * methods.
 * 
 * @author Al Cline
 * @version Jul 19, 2014 // original <br>
 *          Jul 24, 2014 // refactored to allow for registries not residing in the common location <br>
 *          Sep 20, 2014 // test removeAllRegistries <br>
 */
public class TestRegistryFactory
{
  private RegistryFactory _rf = null;
  private Scheduler skedder = null;

  // ============================================================
  // Fixtures
  // ============================================================

  @Before
  public void setUp() throws Exception
  {
    skedder = new Scheduler(new DefaultUserMsg());
    _rf = new RegistryFactory();
    _rf.initRegistries(skedder);
  }

  @After
  public void tearDown() throws Exception
  {
    // Turn off messaging at end of each test
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  // ============================================================
  // Begin Tests
  // ============================================================

  /**
   * @Not.Needed {@code RegistryFactory()} -- wrapper method <br>
   */
  public void _testsNotNeeded()
  {}

  /**
   * Get a Registry, and if it doesn't exist, create it and add the entry to the factory's map
   * 
   * @Normal.Test Get a registry that already exists
   */
  @Test
  public void testGetRegistry_Exists()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP: ensure that registry to be created already exists
    SkillRegistry testreg = (SkillRegistry) _rf.getRegistry(RegKey.SKILL);
    File regfile = new File(Chronos.SkillRegPath);

    // DO:
    SkillRegistry testreg2 = (SkillRegistry) _rf.getRegistry(RegKey.SKILL);

    // VERIFY: factory has same registry file exists
    assertTrue(regfile.exists());
    assertEquals(testreg, testreg2);

    // TEARDOWN
    // Nothing to do
  }


  /**
   * Get a Registry, and if it doesn't exist, create it and add the entry to the factory's map
   * 
   * @Null.Test use null to request a null registry returns null
   */
  @Test
  public void testGetRegistry_Errors()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP

    // DO: Null request
    Registry<?> testreg = _rf.getRegistry(null);

    // VERIFY
    assertNull(testreg);
  }

  // ============================================================
  // Helper Methods
  // ============================================================



} // end of TestRegistryFactory
