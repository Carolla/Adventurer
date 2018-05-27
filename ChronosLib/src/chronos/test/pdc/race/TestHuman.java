/**
 * TestHuman.java Copyright (c) 2017, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use from
 * acline@carolla.com.
 */

package chronos.test.pdc.race;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;
import chronos.pdc.race.Human;
import mylib.MsgCtrl;

/**
 * @author --generated by QA Tool--
 * @version August 15, 2017 // original <br>
 *          Sept 25, 2017 // moved calcWeight() and calcHeight() into base test class <br>
 *          May 24, 2018 // removed dup tests and simplied Override methods <br>
 *          May 26, 2018 // Verified gender adj and standardized <br>
 */
public class TestHuman
{
  private Human _him;
  private Human _her;
  /** Female characters also need gender adjustments: STR-1, CON+1, CHR+1 */
  static private Gender _gender;

  @BeforeAll
  public static void setUpBeforeClass() throws Exception
  {
    _gender = new Gender("Female");
    assertNotNull(_gender);
  }

  @AfterAll
  public static void tearDownAfterClass() throws Exception
  {
    _gender = null;
  }

  @BeforeEach
  public void setUp()
  {
    _him = new Human(new Gender("male"), "black");
    assertNotNull(_him);

    _her = new Human(new Gender("female"), "blonde");
    assertNotNull(_her);
  }

  @AfterEach
  public void tearDown()
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
   * @Normal.Test TraitList adjustTraitsForRace(TraitList traits) -- no adjustments for Race
   */
  @Test
  public void testAdjustTraitsForRace()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Save the original TraitList to an array for later comparison
    // STR, INT, WIS, CON, DEX, CHR
    TraitList traits = new TraitList();
    int[] original = traits.toArray();
    MsgCtrl.msgln("\t Raw traits:\t\t " + traits.toString());

    // Adjust traits: male Human: traits as is
    _him.adjustTraitsForRace(traits);
    MsgCtrl.msgln("\t No Adj for Race:\t " + traits.toString());
    assertEquals(original[0], traits.getTrait(PrimeTraits.STR));
    assertEquals(original[1], traits.getTrait(PrimeTraits.INT));
    assertEquals(original[2], traits.getTrait(PrimeTraits.WIS));
    assertEquals(original[3], traits.getTrait(PrimeTraits.CON));
    assertEquals(original[4], traits.getTrait(PrimeTraits.DEX));
    assertEquals(original[5], traits.getTrait(PrimeTraits.CHR));

    // Net traits for female Humans = STR-1, CON+1, CHR+1
    MsgCtrl.msgln("\t Gender = " + _gender);
    traits = _gender.adjustTraitsForGender(traits);
    MsgCtrl.msgln("\t STR-1, CON+1, CHR+1 net female adj: " + traits.toString());
    assertEquals(original[0] - 1, traits.getTrait(PrimeTraits.STR));
    assertEquals(original[1], traits.getTrait(PrimeTraits.INT));
    assertEquals(original[2], traits.getTrait(PrimeTraits.WIS));
    assertEquals(original[3] + 1, traits.getTrait(PrimeTraits.CON));
    assertEquals(original[4], traits.getTrait(PrimeTraits.DEX));
    assertEquals(original[5]+1, traits.getTrait(PrimeTraits.CHR));
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


} // end of TestHuman.java class
