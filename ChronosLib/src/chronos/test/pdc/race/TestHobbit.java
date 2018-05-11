/**
 * TestHobbit.java Copyright (c) 2017, Alan Cline. All Rights Reserved.
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from acline@carolla.com.
 */

package chronos.test.pdc.race;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;
import chronos.pdc.race.Hobbit;
import chronos.pdc.race.Race.MockRace;
import mylib.MsgCtrl;

/**
 * @author --generated by QA Tool--
 * @version August 17, 2017 // original <br>
 *          Sept 25, 2017 // moved calcWeight() and calcHeight() into base test class <br>
 */
public class TestHobbit
{
  private Hobbit _him;
  private Hobbit _her;
  private MockRace _mockHim;
  private MockRace _mockHer;

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
    _him = new Hobbit(new Gender("male"), "black");
    assertNotNull(_him);
    _mockHim = _him.new MockRace();
    assertNotNull(_mockHim);

    _her = new Hobbit(new Gender("female"), "brown");
    assertNotNull(_her);
    _mockHer = _her.new MockRace();
    assertNotNull(_mockHer);
  }

  /**
   * @throws java.lang.Exception -- general catch-all for exceptions not caught by the tests
   */
  @After
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
   * @Normal.Test Hobbit(Gender gender, String hairColor) -- verify ctor
   */
  @Test
  public void testCtor()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    String[] hobbitSkills = {
        "Infravision (30')",
        "Resistance to Poison: Special Save includes HPMod and Magic Attack Mod",
        "Detect slopes in underground passages (75%)",
        "Determine direction of underground travel (50%)"
    };

    // VERIFY male hobbit
    // Get fields set during male constructor: race name ("Hobbit"), race lang ("Tolkeen"), skills
    // (listed above), gender ("male), and hair color ("black")
    assertEquals("Hobbit", _mockHim.getRaceName());
    assertEquals("Tolkeen", _mockHim.getRaceLang()); // Hobbits have race language Tolkeen

    List<String> skills = _him.getSkills();
    assertEquals(hobbitSkills.length, skills.size());
    for (int k = 0; k < skills.size(); k++) {
      MsgCtrl.msgln("\t Male Hobbit skills: " + skills.get(k));
      assertEquals(hobbitSkills[k], skills.get(k));
    }
    assertEquals("Male", _him.getGender());
    assertEquals("black", _mockHim.getHairColor());

    // VERIFY female hobbit
    // Get fields set during male constructor: race name ("Hobbit"), race lang ("Tolkeen"), skills
    // (listed above), gender ("female), and hair color ("brown")
    assertEquals("Hobbit", _mockHer.getRaceName());
    assertEquals("Tolkeen", _mockHer.getRaceLang()); // Hobbits have race language Tolkeen

    skills = _her.getSkills();
    assertEquals(hobbitSkills.length, skills.size());
    for (int k = 0; k < skills.size(); k++) {
      MsgCtrl.msgln("\t Female Hobbit skills: " + skills.get(k));
      assertEquals(hobbitSkills[k], skills.get(k));
    }
    assertEquals("Female", _her.getGender());
    assertEquals("brown", _mockHer.getHairColor());
  }


  /**
   * @Normal.Test TraitList adjustTraitsForRace(TraitList traits) -- STR-1, CON+1, DEX+1
   */
  @Test
  public void testAdjustTraitsForRace()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // SETUP Provide some base traits to be adjusted
    // Dump the prime traits' names for easier processing
    PrimeTraits[] traitName = PrimeTraits.values();

    // Save the original traitlist to another object for later comparison
    TraitList baseTraits = new TraitList();
    int[] original = baseTraits.toArray();

    // VERIFY STR-1, CON+1, DEX+1
    _him.adjustTraitsForRace(baseTraits);
    for (int k = 0; k < 6; k++) {
      int beforeTrait = original[k];
      int afterTrait = baseTraits.getTrait(traitName[k]);
      if (traitName[k] == PrimeTraits.STR) {
        assertEquals(afterTrait, beforeTrait - 1);
      } else if (traitName[k] == PrimeTraits.CON) {
        assertEquals(afterTrait, beforeTrait + 1);
      } else if (traitName[k] == PrimeTraits.DEX) {
        assertEquals(afterTrait, beforeTrait + 1);
      } else {
        assertEquals(beforeTrait, afterTrait);
      }
    }
    // VERIFY STR-1, CON+1, DEX+1
    baseTraits = new TraitList();
    original = baseTraits.toArray();
    _her.adjustTraitsForRace(baseTraits);
    for (int k = 0; k < 6; k++) {
      int beforeTrait = original[k];
      int afterTrait = baseTraits.getTrait(traitName[k]);
      if (traitName[k] == PrimeTraits.STR) {
        assertEquals(afterTrait, beforeTrait - 1);
      } else if (traitName[k] == PrimeTraits.CON) {
        assertEquals(afterTrait, beforeTrait + 1);
      } else if (traitName[k] == PrimeTraits.DEX) {
        assertEquals(afterTrait, beforeTrait + 1);
      } else {
        assertEquals(beforeTrait, afterTrait);
      }
    }
  }


  /**
   * @Normal.Test Call the base test calcHeight(Race race, int min, int max) to allow Race-specific
   *              values to be entered
   */
  @Test
  public void baseTestCalcHeight()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Check the default values
    TestRace.baseTestCalcHeight(_him, 38, 46);
    MsgCtrl.msgln("\t Hobbit male height verified");
    // Check the female values
    TestRace.baseTestCalcHeight(_her, 34, 42);
    MsgCtrl.msgln("\t Hobbit female height verified");
  }


  /**
   * @Normal.Test Call the base test calcWeight(Race race, int min, int max) to allow Race-specific
   *              values to be entered
   */
  @Test
  public void baseTestCalcWeight()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
    MsgCtrl.where(this);

    // Check the default values
    TestRace.baseTestCalcVariance(_him, 70, 110);
    MsgCtrl.msgln("\t Hobbit male weight verified");
    // Check the female values
    TestRace.baseTestCalcVariance(_her, 63, 99);
    MsgCtrl.msgln("\t Hobbit female weight verified");
  }

  
  // ===============================================================================
  // PRIVATE HELPERS
  // ===============================================================================


} // end of TestHobbit.java class