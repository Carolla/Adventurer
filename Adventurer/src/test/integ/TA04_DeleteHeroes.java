/**
 * TA03a_SummonHeroes.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.character.Hero;
import chronos.pdc.registry.HeroRegistry;

public class TA04_DeleteHeroes
{
  private static HeroRegistry _dorm = new HeroRegistry(); 
  private static Hero hero1 = new Hero("Alpha", "male", "brown", "Human", "Fighter");
  
  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() 
  {
    _dorm.add(hero1);
    assertTrue(_dorm.contains(hero1));
  }
  
  // ============================================================
  // BEGIN TESTING
  // ============================================================

  @Test
  public void testDeleteHero()
  {    
    _dorm.delete(hero1);
    assertFalse(_dorm.contains(hero1));
  }
}