/**
 * TestCreateDwarfPeasant.java Copyright (c) 2017, Carolla Development, Inc. All Rights Reserved
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
public class TestCreateDwarfPeasant extends TestCreateHero
{
  // Trait range for gnome males, adjusted from norm by CON+1, CHR-1
  private final int[] MALE_LOWTRAITS =  { 8,  8,  8,  9,  8,  7};
  private final int[] MALE_HIGHTRAITS = {18, 18, 18, 19, 18, 17};

  // Trait range for gnome females, adjusted from male by STR-1, CON+1, CHR+1
  private final int[] FEMALE_LOWTRAITS =  { 7,  8,  8, 10,  8,  8};
  private final int[] FEMALE_HIGHTRAITS = {17, 18, 18, 20, 18, 18};

  // Height range for gnomes
  private final int MALE_LOWWT = 110;
  private final int MALE_HIGHWT = 190;
  private final int MALE_LOWHT = 49;
  private final int MALE_HIGHHT = 59;

  // Weight range for gnomes
  private final int FEMALE_LOWWT = 99;
  private final int FEMALE_HIGHWT = 179;
  private final int FEMALE_LOWHT = 44;
  private final int FEMALE_HIGHHT = 54;


  // ============================================================
  // BEGIN TESTING
  // ============================================================

  /** Verify that the female dwarf complies */
  @Test
  public void testFemalePeasant()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    /** Set data and create a female dwarf Peasant */
    _myHero = createHero("Brohimina", "Female", "red", "Dwarf");
    printHero(_opMap);

    // Verify female-specific weight and height
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    assertTrue(checkRangeValue(FEMALE_LOWWT, FEMALE_HIGHWT, weight));

    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    assertTrue(checkRangeValue(FEMALE_LOWHT, FEMALE_HIGHHT, height));

    // Verify female trait adjustments, and Common language
    assertTrue(_opMap.get(PersonKeys.LANGUAGES).equals("Common, Dwarvish"));
    verifyTraits(FEMALE_LOWTRAITS, FEMALE_HIGHTRAITS);

    // Now check all gender- and race-neutral attributes
    verifyGenericAttributes();
  }


  /** Verify that the male dwarf traits complies */
  @Test
  public void testMalePeasant()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    /** Set data and create a male gnome Peasant */
    _myHero = createHero("Brolim", "Male", "red", "Dwarf");
    printHero(_opMap);

    // Verify male-specific weight and height
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    assertTrue(checkRangeValue(MALE_LOWWT, MALE_HIGHWT, weight));

    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    assertTrue(checkRangeValue(MALE_LOWHT, MALE_HIGHHT, height));

    // Verify male (unadjusted) traits, and only Common language
    assertTrue(_opMap.get(PersonKeys.LANGUAGES).equals("Common, Dwarvish"));
    verifyTraits(MALE_LOWTRAITS, MALE_HIGHTRAITS);

    // Now check all gender- and race-neutral attributes
    verifyGenericAttributes();
  }


  // ============================================================
  // PRIVATE VERIFICATION METHODS
  // ============================================================



} // end of TestCreateGnomePeasant subclass

