/**
 * CreateMaleHumanPeasant.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
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
import chronos.pdc.character.TraitList;
import mylib.MsgCtrl;

/**
 * Test class for a human Peasant. Verifies female and male traits, and human-specifc attributes.
 * Non-specific attributes are verified in the base class. 
 * 
 * @author Al Cline
 * @version Jun 9, 2017 // original <br>
 *          Jun 14 2017 // refactored down to simple base method with parms <br>
 */
public class CreateHumanPeasant extends TA01_CreateHero
{

  // ============================================================
  // BEGIN TESTING
  // ============================================================

  /** Verify that the weight complies with the male human weight range */
  @Test
  public void testMalePeasant()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    /** Set data and create a male human Peasant */
    _myHero = createHero("Falsoon", "Male", "brown", "Human", "Peasant");
    printHero(_opMap);
    
    // Verify male-specific weight and height
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    assertTrue(checkRangeValue(130, 230, weight));

    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    assertTrue(checkRangeValue(60, 78, height));
    
    // Verify Human-specific traits
    verifyHumanAttributes();
    
    // Now check all gender- and race-neutral attributes
    verifyGenericAttributes();
  }


  /** Verify that the weight complies with the male human weight range */
  @Test
  public void testFemalePeasant()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    /** Set data and create a female human Peasant */
    _myHero = createHero("Belinda", "Female", "blond", "Human", "Peasant");
    printHero(_opMap);

    // Verify female-specific weight and height
    int weight = Integer.parseInt(_opMap.get(PersonKeys.WEIGHT));
    assertTrue(checkRangeValue(117, 207, weight));

    int height = Integer.parseInt(_opMap.get(PersonKeys.HEIGHT));
    assertTrue(checkRangeValue(54, 70, height));
    
    // Verify Human-specific traits
    verifyHumanAttributes();
    
    // Now check all gender- and race-neutral attributes
    verifyGenericAttributes();
  }

    
  /** Verify that all Hero traits fall within range of [8,18] (No Race adjustments) */
  private void verifyHumanAttributes()
  {
    MsgCtrl.where(this);
    
    // Verify human trait ranges [8, 18]
    String traitStrings = loadTraitStrings(_opMap);
    MsgCtrl.msgln("\tTraits: \t" + traitStrings);
    _traits = loadTraits(_opMap);

    for (int k = 0; k < _traits.length - 1; k++) {
      assertTrue((_traits[k] >= TraitList.MIN_TRAIT) && (_traits[k] <= TraitList.MAX_TRAIT));
    }
  }
  
  

} // end of CreateHumanPeasant test subclass

