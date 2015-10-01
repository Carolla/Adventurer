/**
 * HeroTest.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc.character;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mylib.MsgCtrl;
import pdc.character.Hero;
import pdc.character.Hero.MockHero;

/**
 * @author Al Cline
 * @version Sep 4, 2015 // original for rewritten Hero class<br>
 */
public class TestHero
{
  private static Hero _hero;
  private static MockHero _mock;

  private final static int NBR_TRAITS = 6;
  private static ArrayList<Integer> _traits = null;


  private final String[] _klasses = {"Fighter", "Cleric", "Wizard", "Thief"};

  private final String[] _races =
      {"Human", "Dwarf", "Elf", "Gnome", "Half-Elf", "Half-Orc", "Hobbit"};

  /* Test Data */
  private static String NAME = "Falsoon";
  private static String GENDER = "male";
  private static String HAIR = "black";
  private static String RACE = "Human";
  private static String KLASS = "Cleric";

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    // Create a new Hero
    _hero = new Hero(NAME, GENDER, HAIR, RACE, KLASS);
    assertNotNull(_hero);
    _mock = _hero.new MockHero();
    assertNotNull(_mock);
    _traits = new ArrayList<Integer>(NBR_TRAITS);
    assertNotNull(_traits);
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    _hero = null;
    _mock = null;
    _traits = null;
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

  
  // ====================================================
  // Tests Begin Here
  // ====================================================

  /**
   * Generate the Hero and examine the Hero.java output 
   */
  @Test
  public void testHeroCtor() throws InstantiationException
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);
    displayHero(_hero);
  }
  
  
  /**
   * Generate the Hero klass generation. 
   * Note: This test does not use the one created in setUpBeforeClass
   */
// @Test
  public void testHeroKlasses() throws InstantiationException
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Create a bunch of new heroes of the right klass
    for (int k = 0; k < _klasses.length; k++) {
      Hero aHero = new Hero(NAME, GENDER, HAIR, RACE, _klasses[k]);
      MockHero aMock = aHero.new MockHero();
      String name = aMock.getKlassName();
      assertTrue(_klasses[k] == name);
      MsgCtrl.msgln("Klass created: " + name);
    }
  }

  /**
   * Generate the Hero race generation.
   * Note: This test does not use the one created in setUpBeforeClass
   */
//  @Test
  public void testHeroRaces() throws InstantiationException
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Create a bunch of new heroes of the right klass
    for (int k = 0; k < _races.length; k++) {
      Hero aHero = new Hero(NAME, GENDER, HAIR, _races[k], KLASS);
      MockHero aMock = aHero.new MockHero();
      String name = aMock.getRaceName();
      assertTrue(_races[k] == name);
      MsgCtrl.msgln("Race created: " + name);
    }
  }


//  /**
//   * Adjust traits for Klass
//   */
//  @Test
//  public void testKlassTraitAdj() throws InstantiationException
//  {
//    MsgCtrl.auditMsgsOn(true);
//    MsgCtrl.errorMsgsOn(true);
//    MsgCtrl.where(this);
//
//    int FIGHTER = 0;
//
//    _traits = _mock.getTraits();
//    assertNotNull(_traits);
//
//    Klass myKlass = _mock.getKlass();
//    int prime = myKlass.getPrimeNdx();
//    assertTrue(prime == FIGHTER);
//    displayTraits("After Klass adjustment:", _traits);
//  }
//
//  public void testRaceTraitAdj() throws InstantiationException
//  {
//    MsgCtrl.auditMsgsOn(false);
//    MsgCtrl.errorMsgsOn(false);
//    MsgCtrl.where(this);
//
//    _traits = _mock.getTraits();
//    assertNotNull(_traits);
//
//    Race myRace = _mock.getRace();
//    assertTrue(myRace.getRaceName() == "Human");
//    displayTraits("After Race adjustment:", _traits);
//  }

  
  // ====================================================
  // Support Methods
  // ====================================================

  /** Display the Hero's key characteristics */
  private void displayHero(Hero hero)
  {
    StringBuilder out = new StringBuilder();
    out.append(hero.getName() + " ");
    out.append(hero.getGender() + " ");
    out.append(hero.getRaceName() + " ");
    out.append(hero.getKlassName() + " ");
    MsgCtrl.msg("Hero " + out);
  }

  /**
   * Display the Hero's prime traits
   * 
   * @param msg header message before traits display
   * @param _traits2 traits for display
   */
  private void displayTraits(String msg, ArrayList<Integer> _traits2)
  {
    String[] ndx = {"STR", "INT", "WIS", "CON", "DEX", "CHR"};
    MsgCtrl.msgln("\n" + msg);
    for (int k = 0; k < 6; k++) {
      MsgCtrl.msg("\t" + ndx[k] + " = " + _traits2.get(k) + "\t");
    }
  }



} // end of TestHero
