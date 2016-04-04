/**
 * TestHeroRegistry.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.test.pdc.registry;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.character.Hero;
import chronos.pdc.registry.HeroRegistry;
import chronos.pdc.registry.HeroRegistry.MockHeroRegistry;
import mylib.MsgCtrl;
import mylib.dmc.DbReadWriter;

/**
 * @author Al Cline
 * @version Mar 29, 2016 // original <br>
 * @param <E>
 */
public class TestHeroRegistry
{
  private HeroRegistry _heroReg;
  private MockHeroRegistry _mock;
  private DbReadWriter<Hero> _dbrw;

  static private Hero _hero1;
  static private Hero _hero2;
  static private Hero _hero3;


  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    _hero1 = new Hero("Falsoon", "Male", "Human", "Fighter");
    _hero2 = new Hero("Blythe", "Female", "Elf", "Druid");
    _hero3 = new Hero("Balthazar", "Male", "Human", "Cleric");
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    _hero1 = null;
    _hero2 = null;
    _hero3 = null;
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    _heroReg = new HeroRegistry();
    assertNotNull(_heroReg);
    _mock = _heroReg.new MockHeroRegistry();
    assertNotNull(_mock);

    assertEquals(0, _heroReg.getNbrElements());
    _heroReg.add(_hero1);
    _heroReg.add(_hero2);
    _heroReg.add(_hero3);
    assertEquals(3, _heroReg.getNbrElements());

  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    _mock.getDb().clear(); // remove persisting objects
    _mock = null;
    _heroReg = null;
  }


  // ========================================================
  // BEGIN TESTS
  // ========================================================

  /**
   * @NORMAL.TEST HeroRegistry HeroRegistry()
   */
  @Test
  public void testCtor()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    _dbrw = _mock.getDb();
    assertNotNull(_dbrw);
    List<Hero> regRW = _mock.getList();
    assertNotNull(regRW);
  }


  /**
   * @NORMAL.TEST Hero getHero(String name)
   */
  @Test
  public void testGetHero()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    Hero savedHero = _heroReg.get("Blythe");
    assertEquals(3, _heroReg.getNbrElements());
    assertEquals("Elf", savedHero.getRaceName());
    assertEquals("Druid", savedHero.getKlassName());
  }


  /**
   * @NORMAL.TEST List<Hero> getAll()
   */
  @Test
  public void testGetAll()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // RUN
    List<Hero> heroList = _heroReg.getAll();

    // VERIFY
    assertEquals(3, heroList.size());
    assertEquals(3, _heroReg.getNbrElements());
    // TOD Not sure why this returns null always
    // DbReadWriter<Hero> db = _mock.getDb();
    // int x = db.size();
    // assertEquals(3, x);
  }

  /**
   * @NORMAL.TEST List<String> getNamePlates()
   */
  @Test
  public void testGetNamePlates()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    String[] expNamePlates = {"Falsoon: Male Human Fighter", "Blythe: Female Elf Druid",
        "Balthazar: Male Human Cleric"};

    List<String> list = _heroReg.getNamePlates();
    for (int k=0; k < expNamePlates.length; k++) {
      MsgCtrl.msgln("\t" + list.get(k));
      assertEquals(expNamePlates[k], list.get(k));
    }

  }

  
  /**
   * @NORMAL.TEST   void saveHero(Hero hero)
   */
  @Test
  public void testSaveHero()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
  
   // SETUP Create new heroes to add
    Hero newHero = new Hero("Red Shirt", "male", "Human", "Fighter");

    // VERIFY that both list and db are updated...
    assertEquals(3, _heroReg.getNbrElements());
    assertTrue(_heroReg.add(newHero));
    assertEquals(4, _heroReg.getNbrElements());
    // ...and uniquely
    assertFalse(_heroReg.add(newHero));    // shouldn't work this time
    assertEquals(4, _heroReg.getNbrElements());
    
  }


} // end of TestHeroRegistry class
