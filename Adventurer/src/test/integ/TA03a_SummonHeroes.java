/**
 * TA03a_SummonHeroes.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

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
import chronos.pdc.registry.HeroRegistry.MockHeroRegistry;
import mylib.MsgCtrl;

/**
 * @author Al Cline
 * @version Mar 25, 2016 // original <br>
 */
public class TA03a_SummonHeroes
{
  private static HeroRegistry _dorm;
  private static MockHeroRegistry _mock;

  static class HeroProxy extends Hero 
  {
    public HeroProxy(String name, String gender, String racename, String klassname)
    {
      super(name, gender, racename, klassname);
    }
  }

  
  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    // Init the system
//    MainframeCiv mfc = new MainframeCiv(new MainframeProxy());
//    assertNotNull(mfc);
    _dorm = new HeroRegistry();
    assertNotNull(_dorm);
    _mock = _dorm.new MockHeroRegistry();
    assertNotNull(_mock);

    _mock.clear();
    HeroProxy hero1 = new HeroProxy("Alpha", "male", "Human", "Fighter");
    assertNotNull(hero1);
    HeroProxy hero2 = new HeroProxy("Beta", "female", "Elf", "Thief");
    assertNotNull(hero2);
    HeroProxy hero3 = new HeroProxy("Gamma", "male", "Dwarf", "Cleric");
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
    _mock = null;
    _dorm.close();
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
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    
    List<Hero> heroes = _dorm.getAll();
    assertEquals(3, heroes.size());
    for (Hero h : heroes) {
      MsgCtrl.msgln("\t" + h.toNamePlate());
    }
    
  }
  
  
} // end of TestSummonHeroes class
