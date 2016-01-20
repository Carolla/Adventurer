/**
 * TestAdventureRegistry.java Copyright (c) 2014, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.test.pdc.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.Adventure;
import chronos.pdc.registry.AdventureRegistry;

/**
 * Test the repository for Adventure storage
 * 
 * @author Alan Cline
 * @version Feb 16, 2014 // original <br>
 *          Sep 13, 2014 // cleaned up and added MsgCtrl.where() method <br>
 */
public class TestAdventureRegistry
{

  private String DEF_ADVENTURE = "The Quest for Rogahn and Zelligar";
  private AdventureRegistry areg;

  // ===========================================================================
  // FIXTURES
  // ===========================================================================

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    areg = new AdventureRegistry();
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {}


  // ===========================================================================
  // BEGIN TESTS
  // ===========================================================================

  /**
   * @Normal Add a new Adventure into the AdvReg, then retrieve it without recreating it
   */
  @Test
  public void testNewInstance()
  {
    MsgCtrl.where(this);

    // VERIFY AdvReg contains single element
    assertEquals(1, areg.getNbrElements());

    // and the element is the recent adventure
    Adventure adv = areg.getAdventure(DEF_ADVENTURE);
    assertTrue(adv.getName().equals(DEF_ADVENTURE));
  }


  /**
   * @Normal Return an adventure by name
   */
  @Test
  public void testGetAdventure()
  {
    MsgCtrl.where(this);

    Adventure adv = (Adventure) areg.get(DEF_ADVENTURE);
    assertNotNull(adv);
    assertEquals(DEF_ADVENTURE, adv.getName());
  }


  /**
   * @Error Return an non-existing adventure
   * @Error Return an adventure with an empty null key
   * @Null Return an adventure with a null key
   * @Error Retrieve an adventure from a non-existing AdventureRegistry
   */
  @Test
  public void testGetAdventure_Nonexisting()
  {
    MsgCtrl.where(this);

    Adventure adv = (Adventure) areg.get("Salazar's Lair");
    assertTrue(adv == null);
  }


  /**
   * @Error Attempt to return an adventure with an empty key
   * @Null Return an adventure with a null key -- compile error
   */
  @Test
  public void testGetAdventure_EmptyKey()
  {
    MsgCtrl.where(this);

    Adventure adv = (Adventure) areg.get("   ");
    assertNull(adv);
  }


  /**
   * @Normal Get a list of all adventures in the AdventureRegistry
   */
  @Test
  public void testGetAdventureList()
  {
    MsgCtrl.where(this);
    List<String> advList = areg.getAdventureList();
    assertEquals(1, advList.size());
  }


  // ===========================================================================
  // PRIVATE HELPER METHODS
  // ===========================================================================

  /**
   * @Null Return an adventure with a null key
   */

  /**
   * All methods tested
   */
  void _testsNotNeeded()
  {}


} // end of TestAdvHelp class
