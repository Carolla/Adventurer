/**
 * TestSummonHeroes.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.civ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.character.Hero;
import chronos.pdc.registry.HeroRegistry;
import civ.MainframeCiv;
import mylib.MsgCtrl;

/**
 * @author Al Cline
 * @version Mar 25, 2016 // original <br>
 */
public class TestSummonHeroes
{
  private static HeroRegistry _dorm;
  
  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    // Init the system
    MainframeCiv mfc = new MainframeCiv();
    assertNotNull(mfc);
    _dorm = new HeroRegistry();
    assertNotNull(_dorm);
    
    Hero hero1 = new Hero("Alpha", "male", "brown", "Human", "Fighter");
    assertNotNull(hero1);
    Hero hero2 = new Hero("Beta", "female", "blond", "Elf", "Thief");
    assertNotNull(hero2);
    Hero hero3 = new Hero("Gamma", "male", "brown", "Dwarf", "Cleric");
    assertNotNull(hero3);

    _dorm.add(hero1);
    _dorm.add(hero2);
    _dorm.add(hero3);
    assertEquals(3, _dorm.getNbrElements());
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    _dorm = null;
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {}

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }

  // ============================================================
  // BEGIN TESTING
  // ============================================================

  @Test
  public void testGetNamePlates()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);
    
    List<Hero> heroes = _dorm.getHeroList();
    assertEquals(3, heroes.size());
    for (Hero h : heroes) {
      MsgCtrl.msgln("\t" + h.toNamePlate());
    }
    
  }
  
  
} // end of TestSummonHeroes class
