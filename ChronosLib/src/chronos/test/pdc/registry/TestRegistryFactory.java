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

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import mylib.MsgCtrl;
import mylib.pdc.Registry;

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
 *          May 16, 2018 // implemented missing tests <br>
 */
public class TestRegistryFactory
{
  /** All registries should be in the registry factory after initialization */
  static final private int NBR_REGISTRIES = 8;

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
    assertEquals(NBR_REGISTRIES, _rf.size());
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
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Verify 8 registries; check in alphabetical order, except Hero, which represents a file
    String[] regNames =
        {"Adventure", "Building", "Item", "NPC", "Occupation", "Town", "Skill", "Hero"};
    int expSize = regNames.length;
    int regSize = _rf.size();
    assertEquals(expSize, regSize);
  }


  /**
   * @Normal.Test Registry getRegistry(String name)
   */
  @Test
  public void testGetRegistryByName()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    ArrayList<String> regNames = new ArrayList<String>(Arrays.asList(
        "Adventure", "Building", "Hero", "Item", "NPC", "Occupation", "Town", "Skill"));

    // RUN & VERIFY
    for (int k = 0; k < regNames.size(); k++) {
      Registry<?> r = _rf.getRegistry(regNames.get(k));
      assertNotNull(r);
      MsgCtrl.msgln("\t" + r.toString() + " retrieved");
    }
  }


  /**
   * @Normal.Test Registry getRegistry(RegistryFactory$RegKey)
   */
  @Test
  public void testGetRegistryByRegKey()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Ensure that all the Registries are obtainable
    RegKey[] regs = RegKey.values();
    for (int k = 0; k < regs.length; k++) {
      Registry<?> r = _rf.getRegistry(regs[k]);
      assertNotNull(r);
      MsgCtrl.msgln("\t Registry " + r.toString());
    }
  }


  /**
   * @Not.Needed void initRegistries() -- part of setup, and tested in testGetRegistry()
   */
  @Test
  public void testInitRegistries()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
  }


  /**
   * @Normal.Test int size() -- get size of all registries
   */
  @Test
  public void testSize()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP
    // Verify 8 registries; check in alphabetical order
    ArrayList<String> regNames = new ArrayList<String>(Arrays.asList(
        "Adventure", "Building", "Hero", "Item", "NPC", "Occupation", "Town", "Skill"));
    int[] regSize = {1, 9, 0, 46, 17, 31, 1, 63};

    // RUN & VERIFY
    for (int k = 0; k < regSize.length; k++) {
      Registry<?> r = _rf.getRegistry(regNames.get(k));
      assertNotNull(r);
      MsgCtrl.msgln("\t Registry " + r.toString() + " contains " + r.size() + " elements");
      assertEquals(regSize[k], r.size());
    }
  }


} // end of TestRegistryFactory
