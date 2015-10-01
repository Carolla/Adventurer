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
  private Hero _hero;
  private MockHero _mock;

  /* Test Data */
  private String NAME = "Falsoon";
  private String GENDER = "male";
  private String HAIR = "black";
  private String RACE = "Human";
  private String KLASS = "Fighter";

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    // Audit messages are OFF at start of each test
    MsgCtrl.auditMsgsOn(false);
    // Error messages are ON at start of each test
    MsgCtrl.errorMsgsOn(true);
    // Create a new Hero
    _hero = new Hero(NAME, GENDER, HAIR, RACE, KLASS);
    assertNotNull(_hero);
    _mock = _hero.new MockHero();
    assertNotNull(_mock);
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    _hero = null;
    _mock = null;
    MsgCtrl.auditMsgsOn(false);
  }


  /**
   * Generate the first rolled (unadjusted) traits
   */
  @Test
  public void testHeroRawTraits() throws InstantiationException
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    int[] traits = _mock.getTraits();
    assertNotNull(traits);
    displayTraits(traits);
  }

  /**
   * Generate the first rolled (unadjusted) traits
   */
  @Test
  public void testKlassTraitAdj() throws InstantiationException
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    int[] traits = _mock.getTraits();
    assertNotNull(traits);
    displayTraits(traits);
  }

  // ====================================================
  // INNER CLASS MockHero
  // ====================================================

  /** Display the Hero's prime traits */
  private void displayTraits(int[] traits)
  {
    String[] ndx = {"STR", "INT", "WIS", "CON", "DEX", "CHR"};
    for (int k = 0; k < 6; k++) {
      MsgCtrl.msg(ndx[k] + " = " + traits[k] + "\t");
    }
  }



} // end of TestHero
