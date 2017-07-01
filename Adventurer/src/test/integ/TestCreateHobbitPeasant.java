/**
 * TestCreateHobbitPeasant.java Copyright (c) 2017, Carolla Development, Inc. All Rights Reserved
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
 * @version July 1, 2017 // original <br>
 */
public class TestCreateHobbitPeasant extends TestCreateHero
{
  // Trait range for males, adjusted from norm by STR-1, CON+1, DEX+1
  private final int[] MALE_LOWTRAITS =  { 7,  8,  8,  9,  9,  8};
  private final int[] MALE_HIGHTRAITS = {17, 18, 18, 19, 19, 18};

  // Trait range for gnome females, adjusted from male by STR-1, CON+1, CHR+1
  private final int[] FEMALE_LOWTRAITS =  { 6,  8,  8, 10,  9,  9};
  private final int[] FEMALE_HIGHTRAITS = {16, 18, 18, 20, 19, 19};

  // Height range
  private final int MALE_LOWWT = 70;
  private final int MALE_HIGHWT = 110;
  private final int MALE_LOWHT = 38;
  private final int MALE_HIGHHT = 46;

  // Weight range
  private final int FEMALE_LOWWT = 63;
  private final int FEMALE_HIGHWT = 103;
  private final int FEMALE_LOWHT = 34;
  private final int FEMALE_HIGHHT = 42;


  // ============================================================
  // BEGIN TESTING
  // ============================================================

  /** Verify that the female complies */
  @Test
  public void testFemalePeasant()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    /** Set data and create a female Peasant */
    _myHero = createHero("Marian", "Female", "blond", "Hobbit");
    printHero(_opMap);

    // Verify female-specific weight
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    assertTrue(checkRangeValue(FEMALE_LOWWT, FEMALE_HIGHWT, weight));
    // Verify female-specific height
    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    assertTrue(checkRangeValue(FEMALE_LOWHT, FEMALE_HIGHHT, height));

    // Verify female trait adjustments, and Common language
    assertTrue(_opMap.get(PersonKeys.LANGUAGES).equals("Common, Tolkeen"));
    verifyTraits(FEMALE_LOWTRAITS, FEMALE_HIGHTRAITS);

    // Now check all gender- and race-neutral attributes
    verifyGenericAttributes();
  }


  /** Verify that the male traits comply */
  @Test
  public void testMalePeasant()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    /** Set data and create a male gnome Peasant */
    _myHero = createHero("Frodo", "Male", "brown", "Hobbit");
    printHero(_opMap);

    // Verify male-specific weight
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    assertTrue(checkRangeValue(MALE_LOWWT, MALE_HIGHWT, weight));
    // Verify male-specific height
    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    assertTrue(checkRangeValue(MALE_LOWHT, MALE_HIGHHT, height));

    // Verify male (unadjusted) traits, and only Common language
    assertTrue(_opMap.get(PersonKeys.LANGUAGES).equals("Common, Tolkeen"));
    verifyTraits(MALE_LOWTRAITS, MALE_HIGHTRAITS);

    // Now check all gender- and race-neutral attributes
    verifyGenericAttributes();
  }


  // ============================================================
  // PRIVATE VERIFICATION METHODS
  // ============================================================



} // end of TestCreateGnomePeasant subclass

