/**
 * TA01_CreateHero.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.integ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.EnumMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.civ.PersonKeys;
import chronos.pdc.character.Hero;
import chronos.pdc.character.Hero.HeroInput;
import chronos.pdc.character.TraitList;
import civ.NewHeroCiv;
import mylib.MsgCtrl;

/**
 * @author Al Cline
 * @version May 21, 2017 // original <br>
 */
public class TA01_CreateHero
{
  /** Hero input: name, gender, hair color, race, klass = peasant (only Peasants can be created) */
  private String[] _heroData = {"Falsoon", "Male", "brown", "Human", "Peasant"};

  // Keys into the traits
  private final int STR = 0;
  private final int INT = 1;
  private final int WIS = 2;
  private final int CON = 3;
  private final int DEX = 4;
  private final int CHR = 5;

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

  /**
   * @NORMAL Create a random human peasant Hero
   */
  @Test
  public void testCreateHero()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    // Setup: Define simulated data from the input panel
    NewHeroCiv nhCiv = new NewHeroCiv();
    EnumMap<HeroInput, String> inputMap = new EnumMap<HeroInput, String>(HeroInput.class);
    // TODO Re-use PersonKeys instead of requiring a new HeroInput map
    inputMap.put(HeroInput.NAME, _heroData[0]);
    inputMap.put(HeroInput.GENDER, _heroData[1]);
    inputMap.put(HeroInput.HAIR, _heroData[2]);
    inputMap.put(HeroInput.RACE, _heroData[3]);
    inputMap.put(HeroInput.KLASS, _heroData[4]);

    // RUN: Create Peasant
    // TODO: Use TestProxy with Civs to ensure same interface to Civ and GUI
    Hero myHero = nhCiv.createHero(inputMap);
    Map<PersonKeys, String> opMap = myHero.loadAttributes();

    // VERIFY
    MsgCtrl.msgln("\tHero " + opMap.get(PersonKeys.NAME) + ": " + opMap.get(PersonKeys.RACENAME)
        + " " + opMap.get(PersonKeys.GENDER) + " " + opMap.get(PersonKeys.KLASSNAME)
        + " with " + opMap.get(PersonKeys.HAIR_COLOR) + " hair.");
    String traits = loadTraitStrings(opMap);
    MsgCtrl.msgln("\tTraits: \t" + traits);

    MsgCtrl.msgln("\t" + opMap.get(PersonKeys.DESCRIPTION));
    assertEquals(_heroData[0], opMap.get(PersonKeys.NAME));
    assertEquals(_heroData[1], opMap.get(PersonKeys.GENDER));
    assertEquals(_heroData[2], opMap.get(PersonKeys.HAIR_COLOR));
    assertEquals(_heroData[3], opMap.get(PersonKeys.RACENAME));
    assertEquals(_heroData[4], opMap.get(PersonKeys.KLASSNAME));

    MsgCtrl.msg("\tLevel = " + opMap.get(PersonKeys.LEVEL) + "; XP = " + opMap.get(PersonKeys.XP));
    assertEquals("0", opMap.get(PersonKeys.LEVEL));
    assertEquals("0", opMap.get(PersonKeys.XP));


    // All values must be within range of 8 and 18
    int[] trait = new int[6];
    trait[STR] = Integer.parseInt(opMap.get(PersonKeys.STR));
    trait[INT] = Integer.parseInt(opMap.get(PersonKeys.INT));
    trait[WIS] = Integer.parseInt(opMap.get(PersonKeys.WIS));
    trait[CON] = Integer.parseInt(opMap.get(PersonKeys.CON));
    trait[DEX] = Integer.parseInt(opMap.get(PersonKeys.DEX));
    trait[CHR] = Integer.parseInt(opMap.get(PersonKeys.CHR));

    for (int k = 0; k < trait.length; k++) {
      assertTrue((trait[k] >= TraitList.MIN_TRAIT) && (trait[k] <= 18));
    }

    // HP Mod
    int hpMod = (trait[CON] - 10) /2;   // expected value
    MsgCtrl.msg("\tCON = " + trait[CON]);
    MsgCtrl.msgln("; \tCON Mod = " + hpMod);
    assertEquals(hpMod, Integer.parseInt(opMap.get(PersonKeys.HP_MOD)));

    // // HP = 10 + HP Mod
    // int hp = 10 + ;
    // MsgCtrl.msg("\tHP = " + opMap.get(PersonKeys.HP));
    // MsgCtrl.msgln("\tCON Mod = " + opMap.get(PersonKeys.HP_MOD));
    // + Integer.parseInt(opMap.get(PersonKeys.HP_MOD))
    // assertTrue((hp >= 10) && (hp <= 14));

  }


  // ============================================================
  // Private Helper Methods
  // ============================================================

  /**
   * Convert the hero's traits to a set of labeled strings, e.g., STR = 12
   * 
   * @param traits map of Hero's labels to trait values
   * @return string of all six traits
   */
  private String loadTraitStrings(Map<PersonKeys, String> traits)
  {
    String[] trNames = {"STR", "INT", "WIS", "CON", "DEX", "CHR"};
    String[] trValues = {traits.get(PersonKeys.STR), traits.get(PersonKeys.INT),
        traits.get(PersonKeys.WIS), traits.get(PersonKeys.CON), traits.get(PersonKeys.DEX),
        traits.get(PersonKeys.CHR)};

    StringBuilder sb = new StringBuilder();
    for (int k = 0; k < trNames.length; k++) {
      sb.append(trNames[k]);
      sb.append("=");
      sb.append(trValues[k]);
      if (k != trNames.length - 1) {
        sb.append("; ");
      }
    }
    return sb.toString();
  }



} // end of TestSummonHeroes class
