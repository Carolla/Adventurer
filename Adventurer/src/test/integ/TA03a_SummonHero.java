/**
 * TA03a_SummonHero.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package test.integ;


import static org.junit.Assert.assertTrue;

import java.util.List;

import mylib.MsgCtrl;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.character.Hero;
import chronos.pdc.registry.HeroRegistry;
import chronos.pdc.registry.RegistryFactory.RegKey;


/**
 * The player selects an existing Hero from the Dormitory to activate and use for play. <br>
 * There are two parts to this test:
 * <OL>
 * <LI>The user requests to summon a hero from the dormitory, and gets a list of Heroes to choose
 * from.</li>
 * <LI>The user then selects one of the heroes. The hero is removed from the dormitory and his or
 * her name plate (name, gender, klass, and race) is displayed as the title of the IOPanel.</li>
 * </OL>
 * <P>
 * PRE-CONDITIONS:
 * <P>
 * At least one Hero exists in the Dormitory.
 * <P>
 * POST-CONDITIONS:
 * <UL>
 * <LI>Hero is not in the Dormitory, but is active (can interact) with the game elements; AND</li>
 * <li>The Hero's name, gender, klass, and race is displayed as title to the IOPanel.</li>
 * </ul>
 * <P>
 * INPUT: Name of selected Hero.
 * <P>
 * OUTPUT: Hero's "name plate" (name, gender, klass, and race)
 * 
 * @author Al Cline
 * @version Dec 8, 2015 // original <br>
 */
public class TA03a_SummonHero extends IntegrationTest
{
  /**
   * Puts three Heroes into the Dormitory to provide a selection base
   * 
   */
  @BeforeClass
  public static void childSetupBeforeClass()
  {
    assertTrue(dormNotEmpty());
  }

  /**
   * Removes all test Heroes from the Dormitory to provide a selection base
   * 
   */
  @AfterClass
  public static void tearDownAfterClass() 
  {
    verifyPostconditions();
    unsetPreconditions();
  }

  // ==============================================================================
  // TESTS BEGIN
  // ==============================================================================

  /** Get a list of all Heroes in the Dormitory. There should be at least three. */
  @Test
  public void getAllHeroes()
  {

  }

  // ==============================================================================
  // Private helper
  // ==============================================================================

  /** At least one Hero exists in the Dormitory. */
  static private boolean dormNotEmpty()
  {
    MsgCtrl.msg("Setting pre-conditions: ");
    MsgCtrl.msgln("\tAt least one Hero must be in the Dormitory.");

    HeroRegistry dorm = (HeroRegistry) _regFactory.getRegistry(RegKey.HERO);
    int nbr = dorm.getNbrElements();
    MsgCtrl.msgln("Dormitory contains " + nbr + " Heroes");
    if (nbr > 0) {
      return true;
    }
    else {
      // Force three Heroes into the dorm
      try {
        Hero hero1 = new Hero("Falsoon", "male", "black", "Human", "Cleric");
        Hero hero2 = new Hero("Galadriel", "female", "blonde", "Elf", "Thief");
        Hero hero3 = new Hero("Blonk", "male", "black", "Dwarf", "Fighter");
        dorm.add(hero1);
        dorm.add(hero2);
        dorm.add(hero3);
      } catch (Exception ex) {
        MsgCtrl.errMsgln(ex.getMessage());
      }
    }

    List<Hero> heroList = dorm.getAll();

    MsgCtrl.msgln("Dormitory contains " + heroList.size() + " Heroes");
    for (int k = 0; k < nbr; k++) {
      MsgCtrl.msgln("\t" + heroList.get(k).getName());
    }

    return true;

  }


  /** The dormitory is returned to its original size and the test hero is removed */
  static private void unsetPreconditions()
  {
    MsgCtrl.msg("Unsetting pre-conditions: ");
    MsgCtrl.msgln("\tThe dormitory is returned to its original size and the test hero is removed.");
  }


  /**
   * Test Hero is not in the Dormitory, but is active (can interact) with the game elements
   */
  static private void verifyPostconditions()
  {
    MsgCtrl.msgln("Verifying post-conditions: ");
    MsgCtrl.msgln("\tTest Hero is not in the Dormitory but is active in the game");
  }



} // end of TA03a_SummonHero integration test
