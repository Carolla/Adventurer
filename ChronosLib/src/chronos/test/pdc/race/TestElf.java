/**
 * TestElf.java Copyright (c) 2017, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com.
 */

package chronos.test.pdc.race;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;
import chronos.pdc.race.Elf;
import mylib.MsgCtrl;

/**
 * @author --generated by QA Tool--
 * @version September 16, 2017 // original <br>
 */
public class TestElf
{
  private Elf _him;
  private Elf _her;

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {}

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @Before
  public void setUp() throws Exception
  {
    _him = new Elf(new Gender("male"), "black");
    assertNotNull(_him);

    _her = new Elf(new Gender("female"), "brown");
    assertNotNull(_her);
  }

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);

    _her = null;
    _him = null;
  }


  // ===============================================================================
  // BEGIN TESTING
  // ===============================================================================

  /**
   * @Normal.Test void adjustTraitsForRace(TraitList) -- Elves are CON-1, DEX+1; adjustments for
   *              gender are not included
   */
  @Test
  public void testAdjustTraitsForRace()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Save the original traitlist to another object for later comparison
    // STR, INT, WIS, CON, DEX, CHR
    TraitList traits = new TraitList();
    int[] original = traits.toArray();

    // Adjust traits: male Dwarf = CON+1, CHR-1
    _him.adjustTraitsForRace(traits);
    assertEquals(original[0], traits.getTrait(PrimeTraits.STR));
    assertEquals(original[1], traits.getTrait(PrimeTraits.INT));
    assertEquals(original[2], traits.getTrait(PrimeTraits.WIS));
    assertEquals(original[3] - 1, traits.getTrait(PrimeTraits.CON));
    assertEquals(original[4] + 1, traits.getTrait(PrimeTraits.DEX));
    assertEquals(original[5], traits.getTrait(PrimeTraits.CHR));
  }


  /**
   * @Normal.Test int calcHeight() -- random selection of gender-specific heights
   */
  @Test
  public void testCalcHeight()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    int NBR_LOOPS = 10;
    int height = 0;

    // Height range: male [60, 72]; female [54, 66]
    // Generate a number of heights to check central tendency
    MsgCtrl.msgln("\t Male height in inches [60, 72]");
    for (int k = 0; k < NBR_LOOPS; k++) {
      height = _him.calcHeight();
      MsgCtrl.msg("\t " + height);
      assertTrue((height >= 60) && (height <= 72));
    }
    MsgCtrl.msgln("\n\t Female height in inches [54, 66]");
    for (int k = 0; k < NBR_LOOPS; k++) {
      height = _her.calcHeight();
      MsgCtrl.msg("\t " + height);
      assertTrue((height >= 54) && (height <= 66));
    }
  }


  /**
   * @Normal.Test int calcWeight() -- random selection of gender-specific weights
   */
  @Test
  public void testCalcWeight()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    int NBR_LOOPS = 10;
    int height = 0;

    // Height range: male [80, 120]; female [72, 112]
    // Generate a number of weights to check central tendency
    MsgCtrl.msgln("\t Male weight in lb [80, 120]");
    for (int k = 0; k < NBR_LOOPS; k++) {
      height = _him.calcWeight();
      MsgCtrl.msg("\t " + height);
      assertTrue((height >= 80) && (height <= 120));
    }
    MsgCtrl.msgln("\n\t Female weight in lb [72, 112]");
    for (int k = 0; k < NBR_LOOPS; k++) {
      height = _her.calcWeight();
      MsgCtrl.msg("\t " + height);
      assertTrue((height >= 72) && (height <= 112));
    }
  }

  
} // end of TestElf.java class