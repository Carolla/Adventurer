/**
 * TestCreateDwarfPeasant.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
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
 * @version Jun 27, 2017 // original <br>
 */
public class TestCreateDwarfPeasant extends TA01_CreateHero
{

  // ============================================================
  // BEGIN TESTING
  // ============================================================

  /** Verify that the weight complies with the male human weight range */
  @Test
  public void testFemalePeasant()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    /** Set data and create a female human Peasant */
    _myHero = createHero("Durtha", "Female", "red", "Dwarf");
    printHero(_opMap);

    // Verify female-specific weight and height
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    assertTrue(checkRangeValue(95, 175, weight));

    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    assertTrue(checkRangeValue(44, 54, height));

    // Verify female trait adjustments, and Common language
    assertTrue(_opMap.get(PersonKeys.LANGUAGES).equals("Common, Groken"));
    verifyFemaleTraits();

    // Now check all gender- and race-neutral attributes
    verifyGenericAttributes();
  }


  /** Verify that the weight complies with the male human weight range */
  @Test
  public void testMalePeasant()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    /** Set data and create a male human Peasant */
    _myHero = createHero("Balim", "Male", "red", "Dwarf");
    printHero(_opMap);

    // Verify male-specific weight and height
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    assertTrue(checkRangeValue(110, 190, weight));

    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    assertTrue(checkRangeValue(49, 59, height));

    // Verify male (unadjusted) traits, and only Common language
    assertTrue(_opMap.get(PersonKeys.LANGUAGES).equals("Common, Groken"));
    verifyMaleTraits();

    // Now check all gender- and race-neutral attributes
    verifyGenericAttributes();
  }


  /**
   * Verify that all female traits fall within trait-specific range: Females get -1 STR, +1 CON, and
   * +1 CHR. There are no race-specific adjustments.
   */
  private void verifyFemaleTraits()
  {
    MsgCtrl.where(this);

    // Expected range values for female SIWCDCh
    String traitStrings = loadTraitStrings(_opMap);
    MsgCtrl.msgln("\tTraits: \t" + traitStrings);
    _traits = loadTraits(_opMap);

    // Check that each trait (SIWCDCh) falls within the proper range CON+1, CHR-1
    // Plus adjustments for Female = STR-1, CON+1, CHR+1 => net STR-1, CON+2, CHR+0
    int[] lowVals = {7, 8, 8, 10, 8, 8};
    int[] hiVals = {17, 18, 18, 20, 18, 18};
    // MsgCtrl.msg("\tTrait check: ");
    for (int k = 0; k < MAX_TRAITS; k++) {
       MsgCtrl.msg(" " + _traits[k]);
      assertTrue(checkRangeValue(lowVals[k], hiVals[k], _traits[k]));
    }
  }


  /**
   * Verify that all Hero traits fall within range of [8,18] (No Race adjustments)
   */
  private void verifyMaleTraits()
  {
    MsgCtrl.where(this);

    // Verify ALL male traits ranges within [8, 18]
    String traitStrings = loadTraitStrings(_opMap);
    MsgCtrl.msgln("\tTraits: \t" + traitStrings);
//    _traits = loadTraits(_opMap);

    // Check that each trait (SIWCDCh) falls within the proper range CON+1, CHR-1
    int[] lowVals = {8, 8, 8, 9, 8, 7};
    int[] hiVals = {18, 18, 18, 19, 18, 17};
    // MsgCtrl.msg("\tTrait check: ");
    for (int k = 0; k < MAX_TRAITS; k++) {
       MsgCtrl.msg(" " + _traits[k]);
      assertTrue(checkRangeValue(lowVals[k], hiVals[k], _traits[k]));
    }
  }



} // end of TestCreateHumanPeasant subclass

