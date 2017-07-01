/**
 * TestCreateHalfOrcPeasant.java Copyright (c) 2017, Carolla Development, Inc. All Rights Reserved
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
 * Test class for a Peasant. Verifies female and male traits, and race-specific attributes.
 * Non-specific attributes are verified in the base class.
 * 
 * @author Al Cline
 * @version July 1, 2017 // original <br>
 */
public class TestCreateHalfOrcPeasant extends TestCreateHero
{
  /** Racial limits for a male for the traits SIWCDCh: STR+1, CON+1, CHR-2 */
  protected final int[] MALE_LOWTRAITS =  { 9,  8,  8,  9,  8,  6};
  protected final int[] MALE_HIGHTRAITS = {19, 18, 18, 19, 18, 16};
  /** Female limits after adjustments from the male: STR-1, CON+1, CHR+1 */
  protected final int[] FEMALE_LOWTRAITS =  { 8,  8,  8, 10,  8,  7};
  protected final int[] FEMALE_HIGHTRAITS = {18, 18, 18, 20, 18, 17};

  // Height range
  private final int MALE_LOWWT = 140;
  private final int MALE_HIGHWT = 260;
  private final int MALE_LOWHT = 60;
  private final int MALE_HIGHHT = 80;

  // Weight range
  private final int FEMALE_LOWWT = 126;
  private final int FEMALE_HIGHWT = 246;
  private final int FEMALE_LOWHT = 54;
  private final int FEMALE_HIGHHT = 74;


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
    _myHero = createHero("Blattery", "Female", "black", "Half-Orc");
    printHero(_opMap);

    // Verify female-specific weight
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    assertTrue(checkRangeValue(FEMALE_LOWWT, FEMALE_HIGHWT, weight));
    // Verify female-specific height
    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    assertTrue(checkRangeValue(FEMALE_LOWHT, FEMALE_HIGHHT, height));

    // Has Common language, and 50% chance of having Orcish 
    boolean hasElvish = _opMap.get(PersonKeys.LANGUAGES).equals("Common, Orcish");
    boolean hasNot = _opMap.get(PersonKeys.LANGUAGES).equals("Common");
    assertTrue(hasElvish || hasNot);
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
    _myHero = createHero("Brute", "Male", "brown", "Half-Orc");
    printHero(_opMap);

    // Verify male-specific weight
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    assertTrue(checkRangeValue(MALE_LOWWT, MALE_HIGHWT, weight));
    // Verify male-specific height
    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    assertTrue(checkRangeValue(MALE_LOWHT, MALE_HIGHHT, height));

    // Has Common language, and 50% chance of having Orcish 
    boolean hasElvish = _opMap.get(PersonKeys.LANGUAGES).equals("Common, Orcish");
    boolean hasNot = _opMap.get(PersonKeys.LANGUAGES).equals("Common");
    assertTrue(hasElvish || hasNot);
    verifyTraits(MALE_LOWTRAITS, MALE_HIGHTRAITS);

    // Now check all gender- and race-neutral attributes
    verifyGenericAttributes();
  }


  // ============================================================
  // PRIVATE VERIFICATION METHODS
  // ============================================================



} // end of TestCreateGnomePeasant subclass

