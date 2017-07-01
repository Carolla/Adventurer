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
 * Test class for a gnome Peasant. Verifies female and male traits, and gnome-specific attributes.
 * Non-specific attributes are verified in the base class.
 * 
 * @author Al Cline
 * @version Jun 30, 2017 // original <br>
 */
public class TestCreateGnomePeasant extends TA01_CreateHero
{
  // Trait range for gnome males, adjusted from norm by STR-2, CHR-1
  private final int[] MALE_LOWTRAITS =  { 6,  8,  8,  8,  8,  7};
  private final int[] MALE_HIGHTRAITS = {16, 18, 18, 18, 18, 17};

  // Trait range for gnome females, adjusted from male by STR-1, CON+1, CHR+1
  private final int[] FEMALE_LOWTRAITS =  { 5,  8,  8,  9,  8,  8};
  private final int[] FEMALE_HIGHTRAITS = {15, 18, 18, 19, 18, 18};

  // Height range for gnomes
  private final int MALE_LOWWT = 60;
  private final int MALE_HIGHWT = 100;
  private final int MALE_LOWHT = 33;
  private final int MALE_HIGHHT = 39;

  // Weight range for gnomes
  private final int FEMALE_LOWWT = 54;
  private final int FEMALE_HIGHWT = 94;
  private final int FEMALE_LOWHT = 30;
  private final int FEMALE_HIGHHT = 36;


  // ============================================================
  // BEGIN TESTING
  // ============================================================

  /** Verify that the female gnome complies */
  @Test
  public void testFemalePeasant()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    /** Set data and create a female gnome Peasant */
    _myHero = createHero("Gnomilla", "Female", "blond", "Gnome");
    printHero(_opMap);

    // Verify female-specific weight and height
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    assertTrue(checkRangeValue(FEMALE_LOWWT, FEMALE_HIGHWT, weight));

    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    assertTrue(checkRangeValue(FEMALE_LOWHT, FEMALE_HIGHHT, height));

    // Verify female trait adjustments, and Common language
    assertTrue(_opMap.get(PersonKeys.LANGUAGES).equals("Common, Gnomish"));
    verifyTraits(FEMALE_LOWTRAITS, FEMALE_HIGHTRAITS);

    // Now check all gender- and race-neutral attributes
    verifyGenericAttributes();
  }


  /** Verify that the male gnome traits complies */
  @Test
  public void testMalePeasant()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    /** Set data and create a male gnome Peasant */
    _myHero = createHero("Gnorm", "Male", "brown", "Gnome");
    printHero(_opMap);

    // Verify male-specific weight and height
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    assertTrue(checkRangeValue(MALE_LOWWT, MALE_HIGHWT, weight));

    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    assertTrue(checkRangeValue(MALE_LOWHT, MALE_HIGHHT, height));

    // Verify male (unadjusted) traits, and only Common language
    assertTrue(_opMap.get(PersonKeys.LANGUAGES).equals("Common, Gnomish"));
    verifyTraits(MALE_LOWTRAITS, MALE_HIGHTRAITS);

    // Now check all gender- and race-neutral attributes
    verifyGenericAttributes();
  }


  // ============================================================
  // PRIVATE VERIFICATION METHODS
  // ============================================================



} // end of TestCreateGnomePeasant subclass

