/**
 * TestCreateGnomePeasant.java Copyright (c) 2017, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@wowway.com
 */

package test.integ;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chronos.civ.PersonKeys;
import mylib.MsgCtrl;

/**
 * Test class for a human Peasant. Verifies female and male traits, and human-specifc attributes.
 * Non-specific attributes are verified in the base class.
 * 
 * @author Al Cline
 * @version Jun 30, 2017 // original <br>
 */
public class TestCreateHumanPeasant extends TestCreateHero
{
  // Trait range for human males STR, INT, WIS, CON, DEX, CHR
  private final int[] MALE_LOWTRAITS = {8, 8, 8, 8, 8, 8};
  private final int[] MALE_HIGHTRAITS = {18, 18, 18, 18, 18, 18};

  // Trait range for human females
  private final int[] FEMALE_LOWTRAITS = {7, 8, 8, 9, 8, 9};
  private final int[] FEMALE_HIGHTRAITS = {17, 18, 18, 19, 18, 19};

  // Weight range for gnomes
  private final int FEMALE_LOWWT = 112;
  private final int FEMALE_HIGHWT = 212;
  private final int FEMALE_LOWHT = 53;
  private final int FEMALE_HIGHHT = 71;

  // Height range for gnomes
  private final int MALE_LOWWT = 130;
  private final int MALE_HIGHWT = 230;
  private final int MALE_LOWHT = 60; 
  private final int MALE_HIGHHT = 78;
  
  // ============================================================
  // BEGIN TESTING
  // ============================================================

  /** Verify that the weight complies with the male human weight range */
  @Test
  public void testFemalePeasant()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    /** Set data and create a female human Peasant */
    _myHero = createHero("Belinda", "Female", "blond", "Human");
    printHero(_opMap);

    // Verify female-specific weight and height
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    assertTrue(checkRangeValue(FEMALE_LOWWT, FEMALE_HIGHWT, weight));

    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    assertTrue(checkRangeValue(FEMALE_LOWHT, FEMALE_HIGHHT, height));

    // Verify female trait adjustments, and Common language
    assertTrue(_opMap.get(PersonKeys.LANGUAGES).equals("Common"));
    verifyTraits(FEMALE_LOWTRAITS, FEMALE_HIGHTRAITS);

    // Now check all gender- and race-neutral attributes
    verifyGenericAttributes();
  }


  /** Verify that the weight complies with the male human weight range */
  @Test
  public void testMalePeasant()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    /** Set data and create a male human Peasant */
    _myHero = createHero("Falsoon", "Male", "brown", "Human");
    printHero(_opMap);

    // Verify male-specific weight and height
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    assertTrue(checkRangeValue(MALE_LOWWT, MALE_HIGHWT, weight));

    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    assertTrue(checkRangeValue(MALE_LOWHT, MALE_HIGHHT, height));

    // Verify male (unadjusted) traits, and only Common language
    assertTrue(_opMap.get(PersonKeys.LANGUAGES).equals("Common"));
    verifyTraits(MALE_LOWTRAITS, MALE_HIGHTRAITS);

    // Now check all gender- and race-neutral attributes
    verifyGenericAttributes();
  }


  // ============================================================
  // PRIVATE VERIFICATION METHODS
  // ============================================================



} // end of TestCreateHumanPeasant subclass

