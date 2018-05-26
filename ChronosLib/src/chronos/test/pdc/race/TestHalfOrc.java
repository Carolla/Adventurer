/**
 * TestHalfOrc.java Copyright (c) 2017, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use from
 * acline@carolla.com.
 */

package chronos.test.pdc.race;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chronos.pdc.Chronos;
import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;
import chronos.pdc.race.HalfElf;
import chronos.pdc.race.HalfOrc;
import chronos.pdc.race.Race.MockRace;
import mylib.MsgCtrl;
import mylib.pdc.Utilities;

/**
 * @author --generated by QA Tool--
 * @version August 17, 2017 // original <br>
 *          Sept 25, 2017 // moved calcWeight() and calcHeight() into base test class <br>
 *          May 26, 2018 // Verified gender adj and standardized <br>
 */
public class TestHalfOrc
{
  // Generate a bunch of values in a loop for stat calculation
  private int NBR_LOOPS = 1000;

  private HalfOrc _him;
  private HalfOrc _her;
  private MockRace _mockHim;
  private MockRace _mockHer;
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

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @BeforeEach
  public void setUp() throws Exception
  {
    _him = new HalfOrc(new Gender("male"), "black");
    assertNotNull(_him);
    _mockHim = _him.new MockRace();
    assertNotNull(_mockHim);

    _her = new HalfOrc(new Gender("female"), "red");
    assertNotNull(_her);
    _mockHer = _her.new MockRace();
    assertNotNull(_mockHer);
  }

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @AfterEach
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);

    _mockHer = null;
    _her = null;
    _mockHim = null;
    _him = null;
  }


  // ===============================================================================
  // BEGIN TESTING
  // ===============================================================================

  /**
   * @Normal.Test HalfOrc(Gender gender, String hairColor) -- verify ctor
   */
  @Test
  public void testCtor()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    String halfOrcSkill = "Infravision (60')";
    String raceLang = "Orcish"; // at 50% chance

    // VERIFY male Half-Orc
    // Get fields set during male constructor: race name ("Half-Orc"), race lang ("Orcish
    // 50%"),
    // skills ("Infravision (60')"), gender ("male), and hair color ("black")
    assertEquals("Half-Orc", _mockHim.getRaceName());
    // Half-Orc's have a 50% chance of knowing Orcish
    String lang = _mockHim.getRaceLang();
    if (!lang.isEmpty()) {
      assertEquals(raceLang, lang);
    }
    // Half-orcs have only one racial skill
    List<String> skills = _him.getSkills();
    assertEquals(1, skills.size());
    MsgCtrl.msgln("\t Male Half-Orc skill: " + skills.get(0));
    assertEquals(halfOrcSkill, skills.get(0));
    assertEquals("Male", _him.getGender());
    assertEquals("black", _mockHim.getHairColor());

    // VERIFY female Half-Orc
    // Get fields set during male constructor: race name ("Half-Orc"), race lang ("Orcish
    // 50%"),
    // skills ("Infravision (60')"), gender ("male), and hair color ("black")
    assertEquals("Half-Orc", _mockHim.getRaceName());
    // Half-Orc's have a 50% chance of knowing Orcish
    lang = _mockHim.getRaceLang();
    if (!lang.isEmpty()) {
      assertEquals(raceLang, lang);
    }
    // Half-orcs have only one racial skill
    skills = _her.getSkills();
    assertEquals(1, skills.size());
    MsgCtrl.msgln("\t Female Half-Orc skill: " + skills.get(0));
    assertEquals(halfOrcSkill, skills.get(0));
    assertEquals("Female", _her.getGender());
    assertEquals("red", _mockHer.getHairColor());
  }


  /**
   * @Normal.Test TraitList adjustTraitsForRace(TraitList traits) -- STR+1, CON+1, CHR-2
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
    MsgCtrl.msgln("\t Raw traits:\t\t\t " + traits.toString());

    // Adjust traits: male Half-Orc = STR+1, CON+1, CHR-2
    _him.adjustTraitsForRace(traits);
    MsgCtrl.msgln("\t STR+1, CON+1, CHR-2 Race adj:\t " + traits.toString());
    assertEquals(original[0] + 1, traits.getTrait(PrimeTraits.STR));
    assertEquals(original[1], traits.getTrait(PrimeTraits.INT));
    assertEquals(original[2], traits.getTrait(PrimeTraits.WIS));
    assertEquals(original[3] + 1, traits.getTrait(PrimeTraits.CON));
    assertEquals(original[4], traits.getTrait(PrimeTraits.DEX));
    assertEquals(original[5] - 2, traits.getTrait(PrimeTraits.CHR));

    // Net traits for female Half-Orcs = CON+2, CHR-1
    MsgCtrl.msgln("\t Gender = " + _gender);
    traits = _gender.adjustTraitsForGender(traits);
    MsgCtrl.msgln("\t CON+2, CHR-1 net female adj:\t " + traits.toString());
    assertEquals(original[0], traits.getTrait(PrimeTraits.STR));
    assertEquals(original[1], traits.getTrait(PrimeTraits.INT));
    assertEquals(original[2], traits.getTrait(PrimeTraits.WIS));
    assertEquals(original[3] + 2, traits.getTrait(PrimeTraits.CON));
    assertEquals(original[4], traits.getTrait(PrimeTraits.DEX));
    assertEquals(original[5] - 1, traits.getTrait(PrimeTraits.CHR));
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
   * @Normal.Test String getRaceLang() -- 50% chance to know Orcish
   */
  @Test
  public void testGetRaceLang()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    int[] vals = new int[NBR_LOOPS];

    // Generate many Half-Ocrs and see how many times they know Orcish
    HalfOrc horc = new HalfOrc(new Gender("male"), "black");
    assertNotNull(horc);
    for (int k = 0; k < NBR_LOOPS; k++) {
      String lang = horc.getRaceLang();
      vals[k] = (lang.isEmpty()) ? -1 : 1;
    }
    double avg = Utilities.average(vals);
    MsgCtrl.msg("\n\t Lang count after " + NBR_LOOPS + " tries = " + avg);
    // About half of the Half-Orcs should know elvish
    assertEquals(0.0, avg, Chronos.TOLERANCE);
  }


} // end of TestHalfOrc.java class
