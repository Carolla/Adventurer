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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.character.Hero;
import chronos.pdc.registry.HeroRegistry;

/**
 * @author Al Cline
 * @version Mar 29, 2016 // original <br>
 */
public class TestHeroRegistry
{
  private static HeroRegistry _heroReg;

  static private Hero _hero1 = new Hero("Falsoon", "Male", "Brown", "Human");
  static private Hero _hero2 = new Hero("Blythe", "Female", "Red", "Elf");
  static private Hero _hero3 = new Hero("Balthazar", "Male", "Bald", "Human");

  @BeforeClass
  public static void setUp() 
  {
    _heroReg = new HeroRegistry();

    assertTrue(_heroReg.add(_hero1));
    assertTrue(_heroReg.add(_hero2));
    assertTrue(_heroReg.add(_hero3));
  }

  @AfterClass
  public static void tearDown() 
  {
    _heroReg.delete(_hero1);
    _heroReg.delete(_hero2);
    _heroReg.delete(_hero3);
  }


  // ========================================================
  // BEGIN TESTS
  // ========================================================

  /**
   * @NORMAL.TEST boolean add(Hero hero)
   */
  @Test
  public void testAdd()
  {
    // SETUP Create new heroes to add
    Hero newHero = new Hero("Red Shirt", "male", "White", "Human");
    int oldNbrHeros = _heroReg.getNbrElements();
    assertTrue(_heroReg.add(newHero));
    assertEquals(oldNbrHeros + 1, _heroReg.getNbrElements());
    _heroReg.delete(newHero);
  }
  
  @Test
  public void addFailsWhenAlreadyInRegistry()
  {
    assertTrue(_heroReg.contains(_hero2));
    assertFalse(_heroReg.add(_hero2));
  }


  /**
   * @NORMAL.TEST Hero getHero(String name)
   */
  @Test
  public void testGetHero()
  {
    int nbr = _heroReg.getNbrElements();
    _heroReg.get("Blythe");
    assertEquals(nbr, _heroReg.getNbrElements());
  }


  /**
   * @NORMAL.TEST List<Hero> getAll()
   */
  @Test
  public void testGetAll()
  {
    List<Hero> heroList = _heroReg.getAll();
    assertEquals(_heroReg.getNbrElements(), heroList.size());
  }


  /**
   * @NORMAL.TEST List<String> getNamePlates()
   */
  @Test
  public void testGetNamePlates()
  {
    String[] expNamePlates = {"Falsoon: Male Human Fighter", "Blythe: Female Elf Wizard",
        "Balthazar: Male Human Cleric"};

    List<Hero> list = _heroReg.getAll();
    List<String> plateList = new ArrayList<String>();
    for (Hero h : list) {
      plateList.add(h.toNamePlate());
    }
    
    String fullList = plateList.toString();
    for (int k = 0; k < expNamePlates.length; k++) {
      assertTrue("Didn't find " + expNamePlates[k] + " in " + fullList, fullList.contains(expNamePlates[k]));
    }
  }


} // end of TestHeroRegistry class
