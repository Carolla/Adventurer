/**
 * TestHeroRegistry.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
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

import java.io.File;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.character.Hero;
import chronos.pdc.registry.HeroRegistry;
import chronos.pdc.registry.HeroRegistry.MockHeroRegistry;
import mylib.MsgCtrl;


/**
 * @author Alan Cline
 * @version Mar 29, 2016 // original <br>
 *          Aug 1, 2017 // updated per QATool <br>
 */
public class TestHeroRegistry
{
  private static HeroRegistry _heroReg;
  private static MockHeroRegistry _mock;

  static private Hero _hero1 = new Hero("Falsoon", "Male", "Brown", "Human");
  static private Hero _hero2 = new Hero("Blythe", "Female", "Red", "Elf");
  static private Hero _hero3 = new Hero("Balthazar", "Male", "Bald", "Human");

  @BeforeClass
  public static void setUpBeforeClass()
  {
    _heroReg = new HeroRegistry();
    assertNotNull(_heroReg);
    _mock = _heroReg.new MockHeroRegistry();
    assertNotNull(_mock);
  }

  @AfterClass
  public static void tearDownAfterClass()
  {
    _mock = null;
    _heroReg = null;
    // Delete the reg file from the file system
    //_mock.delete();
  }

  @Before
  public void setUp()
  {
    assertTrue(_heroReg.add(_hero1));
    assertTrue(_heroReg.add(_hero2));
    assertTrue(_heroReg.add(_hero3));
    assertEquals(3, _heroReg.size());
  }

  @After
  public void tearDown()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    _heroReg.delete(_hero1);
    _heroReg.delete(_hero2);
    _heroReg.delete(_hero3);
  }


  // ========================================================
  // BEGIN TESTS
  // ========================================================

  /**
   * @Normal.Test HeroRegistry()-- heroRegistry is created empty with an open file
   */
  @Test
  public void testCtor()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Verify that three heroes are in the registry
    assertEquals(3, _heroReg.size());

    // Verify: there are 3 Items in the registry
    int expEntries = 3;
    ArrayList<Hero> entryList = (ArrayList<Hero>) _heroReg.getAll();
    assertNotNull(entryList);
    int len = entryList.size();
    MsgCtrl.msg("\t There are " + len + " Heroes in the registry");
    assertEquals(expEntries, len);

    // Print out all names in the HeroRegistry
    StringBuilder sb = new StringBuilder();
    for (int k = 0; k < len; k++) {
      sb.append(entryList.get(k).getName());
      sb.append("\t ");
    }
    MsgCtrl.msg("\t " + sb.toString());
  }


  /**
   * @Normal.Test Hero getHero(String name) -- get a copy of the hero from registry
   * @Error.Test Hero getHero(String name) -- attempt to get hero not in registry
   */
  @Test
  public void testGetHero()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Retrieve all three heroes in a different creation order
    assertEquals(3, _heroReg.size());
    Hero h1 = _heroReg.getHero("Blythe");
    Hero h2 = _heroReg.getHero("Falsoon");
    Hero h3 = _heroReg.getHero("Balthazar");
    
    assertNotNull(h1);
    assertNotNull(h2);
    assertNotNull(h3);
    assertEquals("Falsoon", h2.getName());
    assertEquals("Blythe", h1.getName());
    assertEquals("Balthazar", h3.getName());
    assertEquals(3, _heroReg.size());

    // Get a hero not in the registry (size doesn't change)
    Hero badguy = _heroReg.get("Wanderer");
    assertNull(badguy);
    assertEquals(3, _heroReg.size());
  }


  /**
   * @Not.Needed <Hero> getAll() -- tested in ctor test
   */
  @Test
  public void testGetAll()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.PRIMITIVE);
  }


  /**
   * @Not.Needed initialize() -- verified in ctor test
   */
  @Test
  public void testInitialize()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.PRIMITIVE);
  }


  /**
   * @Normal.Test boolean save() -- save empty registry
   */
  @Test
  public void testSave()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // SETUP: Save an empty registry 
    _heroReg.delete(_hero1);
    _heroReg.delete(_hero2);
    _heroReg.delete(_hero3);
    
    // RUN
    MsgCtrl.msg("\t Reading...");
    _heroReg.save();
    
    // Create a file to get some file stats
    File regFile = _mock.getRegFile();
    assertTrue(regFile.exists());
    MsgCtrl.msgln("\t Dormitory file is size " + regFile.length() + " bytes");
    
    // Try to read it back in
    MsgCtrl.msg("\t Loading...");
    HeroRegistry reg2 = _heroReg.load();
    assertNotNull(reg2);
    assertEquals(reg2, _heroReg);
    MsgCtrl.msgln("\t Dormitory is restored at " + regFile.length() + " bytes");
    
  }


  /**
   * @Normal.Test boolean save() -- save a registry populated with Heroes
   */
  @Test
  public void testSave_Populated()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP: Save an empty registry 
    _heroReg.save();
    
    // Create a file to get some file stats
    File regFile = _mock.getRegFile();
    assertTrue(regFile.exists());
    MsgCtrl.msgln("\t Dormitory file is size " + regFile.length() + " bytes");
  }

  
 
} // end of TestHeroRegistry class
