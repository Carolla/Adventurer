/**
 * TestRegistryFactory.java Copyright (c) 2018, Alan Cline. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use by email:
 * acline@wowway.com
 */

package chronos.test.pdc.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chronos.pdc.registry.RegistryFactory;
import mylib.MsgCtrl;

/**
 * {@code RegistryFactory} is a non-instantiable static class comprised <i>mostly</i> of static
 * methods.
 * 
 * @author Al Cline
 * @version Jul 19, 2014 // original <br>
 *          Jul 24, 2014 // refactored to allow for registries not residing in the common
 *          location <br>
 *          Sep 20, 2014 // test removeAllRegistries <br>
 *          July 31, 2017 // update per QATool <br>
 *          July 31, 2017 // autogen: QA Tool added missing test methods <br>
 *          Mar 19, 2018 // factoring into test suite <br>
 *          May 14, 2018 // updated to JUnit 5 <br>
 */
public class TestRegistryFactory
{
  static private RegistryFactory _rf;

  // ============================================================
  // Fixtures
  // ============================================================

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @BeforeAll
  public static void setUpBeforeClass() throws Exception
  {
    _rf = new RegistryFactory();
    assertNotNull(_rf);
    _rf.initRegistries();
  }

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @AfterAll
  public static void tearDownAfterClass() throws Exception
  {
    _rf = null;
  }

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @BeforeEach
  public void setUp() throws Exception
  {}

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @AfterEach
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }

  // ============================================================
  // BEGIN TESTS
  // ============================================================

  /**
   * @Normal.Test RegistryFactory() -- confirm all registries were created properly
   */
  @Test
  public void testCtor()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Verify 8 registries; check in alphabetical order, except Hero, which
    // represents a file reg
    String[] regNames =
        {"Adventure", "Building", "Item", "NPC", "Occupation", "Town", "Skill", "Hero"};
    int expSize = regNames.length;
    int regSize = _rf.size();
    assertEquals(expSize, regSize);
  }


  /**
   * Get a Registry, and if it doesn't exist, create it and add the entry to the factory's map
   * 
   * @Null.Test use null to request a null registry returns null
   */
  @Test
  public void testGetRegistry_Errors()
  {
    assertNull(_rf.getRegistry(null));
  }

  /**
   * @Not.Implemented Registry getRegistry(RegistryFactory$RegKey)
   */
  @Test
  public void testGetRegistry()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
  }

  /**
   * @Not.Implemented void initRegistries()
   */
  @Test
  public void testInitRegistries()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
  }

  /**
   * @Not.Implemented int size()
   */
  @Test
  public void testSize()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    MsgCtrl.errMsgln("\t\t TEST METHOD NOT YET IMPLEMENTED");
  }

} // end of TestRegistryFactory
