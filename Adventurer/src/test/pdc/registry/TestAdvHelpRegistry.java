/**
 * TestAdvHelpRegistry.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package test.pdc.registry;


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

import pdc.registry.AdvHelpRegistry;
import pdc.registry.AdvHelpRegistry.MockAdvHelpRegistry;
import chronos.Chronos;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import civ.HelpTextObject;


/**
 * Unit test AdvhelpRegistry
 * 
 * @author Alan Cline
 * @version Dec 27, 2013 // original
 */
public class TestAdvHelpRegistry
{

  private AdvHelpRegistry _reg = null;
  private MockAdvHelpRegistry _mock = null;

  private final String ID = "INSTRUCTIONS";
  private final String EXPECTED_TEXT_PART = "Goal: Explore the Arena";


  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {}


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
    MsgCtrl.errorMsgsOn(true);
    _reg = (AdvHelpRegistry) RegistryFactory.getInstance().getRegistry(RegKey.HELP);
    _mock = _reg.new MockAdvHelpRegistry();
  }


  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    _reg.closeRegistry();
    _reg = null;
    _mock = null;
    MsgCtrl.auditMsgsOn(false);
  }


  // _____________________________________________________________________
  //
  // TESTS BEGIN
  // _____________________________________________________________________


  @Test
  public void testCloseRegistry()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.msg(this, "\ntestCloseRegistry()");

    // Verify registry is open
    assertEquals(1, _reg.getNbrElements());

    // Exercise target method
    _reg.closeRegistry();

    // Registry is closed when internal reference
    // assertNull(_mock.getInternalReference());

    // Reopen registries so that the tearDown() method doesn't bomb
    _reg = (AdvHelpRegistry) RegistryFactory.getInstance().getRegistry(RegKey.HELP);

  }


  @Test
  public void testDeleteRegistry()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.msg(this, "\ntestDeleteRegistry()");

    // Verify registry file is open and contains one element
    File file = new File(Chronos.AdventureHelpRegPath);
    assertTrue(file.exists());
    assertEquals(1, _reg.getNbrElements());

    // Exercise target method
    _reg.deleteRegistry();

    // Registry is closed and file no longer exists
    // assertNull(_mock.getInternalReference());
    assertFalse(file.exists());

    // Reopen registries so that the tearDown() method doesn't bomb
    _reg = (AdvHelpRegistry) RegistryFactory.getInstance().getRegistry(RegKey.HELP);

  }


  @Test
  public void testInitialize()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.msg(this, "\ntestInitialize()");

    // Get text from registry and test first part of it
    HelpTextObject help = (HelpTextObject) _reg.getUnique(AdvHelpRegistry.GENERIC_ID);
    String text = help.extractText();
    int len = EXPECTED_TEXT_PART.length();
    assertTrue(EXPECTED_TEXT_PART.equals(text.substring(0, len)));
  }


  // _____________________________________________________________________
  //
  // PRIVATE METHODS
  // _____________________________________________________________________

} // end of TestAdvHelpRegistry
