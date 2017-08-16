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
import static org.junit.Assert.assertFalse;
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

import chronos.pdc.Chronos;
import chronos.pdc.character.Hero;
import chronos.pdc.registry.HeroRegistry;
import chronos.pdc.registry.HeroRegistry.MockHeroRegistry;
import mylib.MsgCtrl;


/**
 * @author Alan Cline
 * @version Mar 29, 2016 // original <br>
 *          Aug 1, 2017 // updated per QATool <br>
 *          Aug 4, 2017 // updated to solve serialization problems <br>
 */
public class TestHeroRegistry
{
  private static HeroRegistry _heroReg;
  private static MockHeroRegistry _mock;

  // Heroes to add, one with a duplicate race
  static private Hero _hero1 = new Hero("Falsoon", "Male", "Brown", "Human");
  static private Hero _hero2 = new Hero("Blythe", "Female", "Red", "Elf");
  static private Hero _hero3 = new Hero("Balthazar", "Male", "Bald", "Hobbit");
  static private Hero _hero4 = new Hero("Borca", "Male", "Black", "Half-Orc");
  static private Hero _hero5 = new Hero("Blandershins", "Female", "Red", "Gnome");
  static private Hero _hero6 = new Hero("Gromet", "Female", "Red", "Dwarf");
  static private Hero _hero7 = new Hero("Cynon", "Male", "Brown", "Human");

  // ArrayList of Heros to use for Registry testing
  static private final int NBR_HEROES = 7;
  static private ArrayList<Hero> _heroList = new ArrayList<>(7);

  // Directory to use for testing Hero files
  private static final String TESTDIRPATH =
      "/Projects/eChronos/ChronosLib/src/chronos/test/pdc/registry/ForTesting/";
  // Save the current HeroRegistry location to restore later
  private static String _tempath;
  // Current location for all file testing
  private static File _testDir;


  @BeforeClass
  public static void setUpBeforeClass()
  {
    _heroList.add(_hero1);
    _heroList.add(_hero2);
    _heroList.add(_hero3);
    _heroList.add(_hero4);
    _heroList.add(_hero5);
    _heroList.add(_hero6);
    _heroList.add(_hero7);
    // Reset the registry directory so that it initializes to the test directory
    _tempath = Chronos.HeroRegPath;
    Chronos.HeroRegPath = TESTDIRPATH;
    // Create file access to the test directory
    _testDir = new File(TESTDIRPATH);
    assertTrue(_testDir.isDirectory());
  }

  @AfterClass
  public static void tearDownAfterClass()
  {
    _heroList = null;
    _testDir = null;
    // Restore the test directory to the original HeroRegistry location
    Chronos.HeroRegPath = _tempath;
  }

  @Before
  public void setUp()
  {
    _heroReg = new HeroRegistry();
    assertNotNull(_heroReg);
    _mock = _heroReg.new MockHeroRegistry();
    assertNotNull(_mock);
    assertNotNull(_mock.getRegFile());
  }

  @After
  public void tearDown()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    // clear registry
    _mock.clear();
    _heroReg = null;
    // Delete all Hero files in the test directory to prepare for next test
    clearFolder(_testDir);
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

    // Confirm no heroes currently in the registry
    assertEquals(0, _heroReg.size());
    loadRegistry();
    // Verify that all of them are in the registry
    assertEquals(NBR_HEROES, _heroReg.size());
  }


  /**
   * @Error.Test boolean add(Hero hero) -- try to add different Hero but with same name
   */
  @Test
  public void testAdd_SameNameDiffHero()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP No heroes saved to registry yet
    Hero h = _heroList.get(2);
    String name1 = h.getName();
    assertTrue(_heroReg.add(h));

    // RUN: Add a Hero same as the other but of different gender and race
    Hero chHero = new Hero("Balthazar", "Female", "Bald", "Hobbit");
    String name2 = chHero.getName();
    assertFalse(_heroReg.add(chHero));

    // VERIFY
    MsgCtrl.msg("\t HeroRegistry contains " + _heroReg.size() + " Heroes:");
    MsgCtrl.msgln("\t " + name1 + "; \t " + name2);
    assertEquals(1, _heroReg.size());
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

    // Load all the Heroes into the Registry
    loadRegistry();

    // Retrieve three of the heroes
    assertEquals(NBR_HEROES, _heroReg.size());
    Hero h1 = _heroReg.getHero("Blythe");
    assertNotNull(h1);
    Hero h2 = _heroReg.getHero("Borca");
    assertNotNull(h2);
    Hero h3 = _heroReg.getHero("Cynon");
    assertNotNull(h3);
    // Size does not change
    assertEquals(NBR_HEROES, _heroReg.size());

    // Get a hero not in the registry (size doesn't change)
    Hero badguy = _heroReg.get("Wanderer");
    assertNull(badguy);
    assertEquals(NBR_HEROES, _heroReg.size());
  }


  /**
   * @Normal.Test List<Hero> getAll() -- ensure the same number retrieved as loaded
   */
  @Test
  public void testGetAll()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Load all the Heroes into the Registry
    loadRegistry();

    // Verify heroes copies out, not removed
    ArrayList<Hero> newList = (ArrayList<Hero>) _heroReg.getAll();
    assertEquals(NBR_HEROES, newList.size());
    assertEquals(NBR_HEROES, _heroReg.size());
  }


  /**
   * @Not.Needed void initialize() -- wrapper for @{code initialize(String dirname)}. See that
   *             series of test
   */
  public void testInitialize()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.WRAPPER);
  }


  /**
   * @Special.Test void initialize(String dirname) -- Ensures that the HeroRegistry loads from all
   *               Hero files. NOTE: wraps (@code initialize(String path)} which must be tested,
   *               even though it is a private method.
   */
  @Test
  public void testInitialize_EmptyDir()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP Create new HeroRegistry with no files
    _heroReg = null;
    HeroRegistry newReg = new HeroRegistry();
    assertNotNull(newReg);
    assertEquals(0, newReg.size());
  }


  /**
   * @Normal.Test void initialize(String dirname) -- Ensures that the HeroRegistry loads one Hero
   *              files. NOTE: wraps (@code initialize(String path)} which must be tested, even
   *              though it is a private method.
   */
  @Test
  public void testInitialize_OneHeroFile()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP Save a test Hero to the test folder
    Hero hero = _heroList.get(0); // should be Falsoon
    _heroReg.add(hero);
    _heroReg.saveHero(hero.getName());

    // Confirm the hero was saved in a file
    File[] filelist = _testDir.listFiles();
    assertEquals(1, filelist.length);
    // Delete the registry and recreate the HeroRegistry, and force the {@code initialize()} call
    _heroReg = null;

    // RUN
    // Create the a HeroRegistry that contains only the single Hero file
    HeroRegistry newReg = new HeroRegistry();

    // VERIFY
    assertNotNull(newReg);
    assertEquals(1, newReg.size());
    Hero testHero = newReg.get("Falsoon");
    assertNotNull(testHero);
  }


  /**
   * @Normal.Test void initialize(String dirname) -- Ensures that the HeroRegistry loads one Hero
   *              files. NOTE: wraps (@code initialize(String path)} which must be tested, even
   *              though it is a private method.
   */
  @Test
  public void testInitialize_MultipleFiles()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP
    // Save all Heroes from the HeroRegistry to the test folder
    for (int k = 0; k < _heroList.size(); k++) {
      Hero h = _heroList.get(k);
      // Add the hero then save the hero
      _heroReg.add(h);
      _heroReg.saveHero(h.getName());
    }

    // Confirm each hero was saved in a file
    File[] filelist = _testDir.listFiles();
    assertEquals(_heroList.size(), filelist.length);
    // Delete the registry and recreate the HeroRegistry, and force the {@code initialize()} call
    _heroReg = null;

    // RUN
    // Create a HeroRegistry that contains all the Hero files
    HeroRegistry newReg = new HeroRegistry();

    // VERIFY
    assertNotNull(newReg);
    assertEquals(_heroList.size(), filelist.length);
    // Just to verify a couple Heroes
    Hero h1 = newReg.get("Falsoon");
    Hero h2 = newReg.get("Borca");
    assertNotNull(h1);
    assertNotNull(h2);
  }


  /**
   * @Normal.Test boolean saveAll() -- save all Heroes that populate the registry
   */
  @Test
  public void testSaveAll()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP: Save a populated registry
    loadRegistry();
    assertEquals(_heroList.size(), _heroReg.size());
    MsgCtrl.msg("\t Saving all Heroes...");
    _heroReg.saveAll();

    // VERIFY All hero files are in dormitory
    File dorm = new File(TESTDIRPATH);
    File[] heroFiles = dorm.listFiles();
    long nbrHeroes = heroFiles.length;
    MsgCtrl.msgln("\t Dormitory contains " + nbrHeroes + " sleeping heroes (files)");

    // Delete the in-memory registry and then try to read it back in
    _heroReg = null;

    // Reading
    MsgCtrl.msg("\t Loading alternative HeroRegistry...");
    HeroRegistry reg2 = new HeroRegistry();
    assertNotNull(reg2);
    assertEquals(_heroList.size(), reg2.size());
    MsgCtrl.msgln("\t Dormitory restored for " + reg2.size() + " Heroes.");
  }


  /**
   * @Special.Test boolean saveAll() -- save a registry without any Heroes
   */
  @Test
  public void testSaveAll_Empty()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP: Save an empty registry
    MsgCtrl.msg("\t Saving registry, no Heroes...");
    assertEquals(0, _heroReg.size());

    // RUN
    _heroReg.saveAll();

    // VERIFY All No files are in dormitory (which was cleared for this test)
    File dorm = new File(TESTDIRPATH);
    File[] heroFiles = dorm.listFiles();
    long nbrHeroes = heroFiles.length;
    MsgCtrl.msgln("\t Dormitory contains " + nbrHeroes + " sleeping heroes (files)");
  }


  /**
   * @Normal.Test boolean saveHero() -- write a Hero to its individual file
   */
  @Test
  public void testSaveHero()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP Add one Hero to registry and save
    Hero h = _heroList.get(3);
    _heroReg.add(h);
    MsgCtrl.msg("\t Saving...");
    _heroReg.saveHero(h.getName());

    // Get the (only) file in the test directory
    File dir = _mock.getRegFile();
    assertNotNull(dir);
    File[] heroes = dir.listFiles();
    assertEquals(1, heroes.length);
    MsgCtrl.msgln("\t" + heroes[0].getName() + ":\t" + heroes[0].length() + " bytes");

    // VERIFY
    heroes = dir.listFiles();
    assertEquals(1, heroes.length);
    MsgCtrl.msgln("\t" + heroes[0].getName() + ":\t" + heroes[0].length() + " bytes");
  }


  


  /**
   * @Error.Test boolean saveHero(String name) -- overwrite an existing Hero file
   */
  @Test
  public void testSaveHero_SameName()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
  
    // SETUP Save one Hero to dormitory
    Hero h = new Hero("Borca", "Male", "bald", "Half-Orc");
    String name1 = h.getName();
    assertTrue(_heroReg.add(h));
    assertEquals(1, _heroReg.size());
    MsgCtrl.msgln("\t " + _heroReg.get("Borca").toNamePlate());
  
    _heroReg.saveHero(name1);
  
    // RUN: Try to add a Hero with the same name as the other but of different gender and race
    Hero chHero = new Hero("Borca", "Female", "blonde", "Hobbit");
    assertFalse(_heroReg.add(chHero));
    assertEquals(1, _heroReg.size());
    MsgCtrl.msgln("\t HeroRegistry contains " + _heroReg.size() + " Hero:");
  
    // VERIFY by proper Hero file in dormitory
    File[] heroFiles = _mock.getRegFile().listFiles();
  
    MsgCtrl.msgln("\t " + _heroReg.get("Borca").toNamePlate() +
        ", saved in " + heroFiles[0].getName());
    assertEquals(1, _heroReg.size());
  }

  // ========================================================
  // PRIVATE HELPER METHODS
  // ========================================================

  /**
   * Delete all the files in the test directory
   */
  private void clearFolder(File dir)
  {
    // Delete all Hero files in that directory
    File[] filelist = dir.listFiles();
    if (filelist.length != 0) {
      for (int k = 0; k < filelist.length; k++) {
        assertTrue(filelist[k].delete());
      }
    }
  }


  /**
   * Load all Heroes in the hero's list into the registry
   */
  private void loadRegistry()
  {
    for (Hero h : _heroList) {
      _heroReg.add(h);
    }
  }


} // end of TestHeroRegistry class
