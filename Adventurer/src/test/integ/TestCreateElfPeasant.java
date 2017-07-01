/**
 * TestCreateElffPeasant.java Copyright (c) 2017, Carolla Development, Inc. All Rights Reserved
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
public class TestCreateElfPeasant extends TestCreateHero
{
  // Trait range for elf males, adjusted from norm by CON-1, DEX+1

  protected final int[] MALE_LOWTRAITS =  { 8,  8,  8,  7,  9,  8};
  protected final int[] MALE_HIGHTRAITS = {18, 18, 19, 17, 19, 19};
  /** Female limits after adjustments from the male: STR-1, CON+1, CHR+1 */
  protected final int[] FEMALE_LOWTRAITS =  { 7,  8,  8,  8,  9,  9};
  protected final int[] FEMALE_HIGHTRAITS = {17, 18, 18, 18, 19, 19};

  // Height range for elves
  private final int MALE_LOWWT = 80;
  private final int MALE_HIGHWT = 120;
  private final int MALE_LOWHT = 60;
  private final int MALE_HIGHHT = 72;

  // Weight range for elves
  private final int FEMALE_LOWWT = 72;
  private final int FEMALE_HIGHWT = 112;
  private final int FEMALE_LOWHT = 54;
  private final int FEMALE_HIGHHT = 66;


  // ============================================================
  // BEGIN TESTING
  // ============================================================

  /** Verify that the female elf complies */
  @Test
  public void testFemalePeasant()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    /** Set data and create a female elf Peasant */
    _myHero = createHero("Gladriel", "Female", "silver", "Elf");
    printHero(_opMap);

    // Verify female-specific weight and height
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    assertTrue(checkRangeValue(FEMALE_LOWWT, FEMALE_HIGHWT, weight));

    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    assertTrue(checkRangeValue(FEMALE_LOWHT, FEMALE_HIGHHT, height));

    // Verify female trait adjustments, and Common language
    assertTrue(_opMap.get(PersonKeys.LANGUAGES).equals("Common, Elvish"));
    verifyTraits(FEMALE_LOWTRAITS, FEMALE_HIGHTRAITS);

    // Now check all gender- and race-neutral attributes
    verifyGenericAttributes();
  }


  /** Verify that the male elf traits complies */
  @Test
  public void testMalePeasant()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    /** Set data and create a male gnome Peasant */
    _myHero = createHero("Glarodin", "Male", "brown", "Elf");
    printHero(_opMap);

    // Verify male-specific weight and height
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    assertTrue(checkRangeValue(MALE_LOWWT, MALE_HIGHWT, weight));

    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    assertTrue(checkRangeValue(MALE_LOWHT, MALE_HIGHHT, height));

    // Verify male (unadjusted) traits, and only Common language
    assertTrue(_opMap.get(PersonKeys.LANGUAGES).equals("Common, Elvish"));
    verifyTraits(MALE_LOWTRAITS, MALE_HIGHTRAITS);

    // Now check all gender- and race-neutral attributes
    verifyGenericAttributes();
  }


  // ============================================================
  // PRIVATE VERIFICATION METHODS
  // ============================================================



} // end of TestCreateGnomePeasant subclass

