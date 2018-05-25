/**
 * TestHalfElf.java Copyright (c) 2017, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use from
 * acline@wowway.com.
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
import chronos.pdc.race.HalfElf;
import mylib.MsgCtrl;

/**
 * @author --generated by QA Tool--
 * @version September 16, 2017 // original <br>
 *          May 11, 2018 // removed detailed stat tests <br>
 *          May 24, 2018 // removed dup tests and simplied Override methods <br>
 */
public class TestHalfElf
{
  // Generate a bunch of values in a loop for stat calculation
  private int NBR_LOOPS = 100;

  private HalfElf _him;
  private HalfElf _her;

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
    _him = new HalfElf(new Gender("male"), "black");
    assertNotNull(_him);

    _her = new HalfElf(new Gender("female"), "brown");
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
   * @Normal.Test void adjustTraitsForRace(TraitList) -- Half-Elves have no modifications
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

    // No adjustment traits
    _him.adjustTraitsForRace(traits);
    assertEquals(original[0], traits.getTrait(PrimeTraits.STR));
    assertEquals(original[1], traits.getTrait(PrimeTraits.INT));
    assertEquals(original[2], traits.getTrait(PrimeTraits.WIS));
    assertEquals(original[3], traits.getTrait(PrimeTraits.CON));
    assertEquals(original[4], traits.getTrait(PrimeTraits.DEX));
    assertEquals(original[5], traits.getTrait(PrimeTraits.CHR));
  }

  
  /**
   * @Not.Needed int calcHeight(int min, int avg) -- test given in base class
   */
  @Test
  public void testCalcHeight()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.WRAPPER);
  }


  /**
   * @Not.Needed int calcHeight(int min, int avg) -- test given in base class
   */
  @Test
  public void testCalcWeight()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    MsgCtrl.msgln(MsgCtrl.NOTEST + MsgCtrl.WRAPPER);
  }


  /**
   * @Normal.Test String getRaceLang() -- 50% chance to know Elvish
   */
  @Test
  public void testGetRaceLang()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    int langCount = 0;
    int maxCnt = 0;
    int minCnt = 0;

    // Generate many Half-Elves and see how many times they know elvish
    HalfElf helf = new HalfElf(new Gender("male"), "black");
    assertNotNull(helf);
    for (int k = 0; k < NBR_LOOPS; k++) {
      String lang = helf.getRaceLang();
      langCount += (lang.isEmpty()) ? -1 : 1;
      // MsgCtrl.msg("\t " + langCount);
      // Print out every 20th lang per line
      // if ((k % 20) == 0) {
      // MsgCtrl.msgln("");
      // }
      maxCnt = Math.max(maxCnt, langCount);
      minCnt = Math.min(minCnt, langCount);
    }
    MsgCtrl.msg("\n\t Lang count after " + NBR_LOOPS + " tries = " + langCount);
    MsgCtrl.msgln("  (ideally, should be 0)");
    MsgCtrl.msgln("\t Lang max = " + maxCnt + "; Lang min = " + minCnt);
    // About half of the Half-Elves should know elvish
    assertTrue(Math.abs(langCount) <= (NBR_LOOPS) * 0.10);
  }


} // end of TestHalfElf.java class
