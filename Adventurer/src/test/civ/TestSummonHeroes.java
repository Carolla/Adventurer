/**
 * TestSummonHeroes.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.civ;

import static org.junit.Assert.*;

import java.util.List;

import mylib.MsgCtrl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.character.Hero;
import chronos.pdc.registry.HeroRegistry;

/**
 * @author Al Cline
 * @version Mar 25, 2016 // original <br>
 */
public class TestSummonHeroes
{
  private static HeroRegistry _dorm = new HeroRegistry(); 
  private static Hero hero1 = new Hero("Alpha", "male", "brown", "Human", "Fighter");
  private static Hero hero2 = new Hero("Beta", "female", "blond", "Elf", "Thief");
  private static Hero hero3 = new Hero("Gamma", "male", "brown", "Dwarf", "Cleric");
  
  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    _dorm.add(hero1);
    System.out.println(_dorm.getAll());
    _dorm.add(hero2);
    System.out.println(_dorm.getAll());
    _dorm.add(hero3);
    System.out.println(_dorm.getAll());
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    _dorm.delete(hero1);
    _dorm.delete(hero2);
    _dorm.delete(hero3);
  }
  
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
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    
    List<Hero> heroes = _dorm.getAll();
    assertTrue(heroes.contains(hero1));
    assertTrue(heroes.contains(hero2));
    assertTrue(heroes.contains(hero3));
  }
  
  
} // end of TestSummonHeroes class
