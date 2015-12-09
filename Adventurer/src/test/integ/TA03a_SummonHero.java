/**
 * TA03a_SummonHero.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package test.integ;


import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chronos.pdc.character.Hero;
import civ.MainActionCiv;
import civ.MainframeCiv;
import mylib.MsgCtrl;


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
 * PRE-CONDITIONS: <P>
 * At least one Hero exists in the Dormitory.
 * <P>
 * POST-CONDITIONS:
 * <UL>
 * <LI>Hero is not in the Dormitory, but is active (can interact) with the game elements; AND</li>
 * <li>The Hero's nameplate is displayed as title to the IOPanel.</li>
 * </ul>
 * <P>
 * INPUT: Name of selected Hero.
 * <P>
 * OUTPUT: Hero's nameplate
 * 
 * @author Al Cline
 * @version Dec 8, 2015 // original <br>
 */
public class TA03a_SummonHero
{

  /**
   * Puts three Heroes into the Dormitory to provide a selection base
   * 
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.msgln("setUpBeforeClass()");
  }


  /**
   * Removes all test Heroes from the Dormitory to provide a selection base
   * 
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    MsgCtrl.msgln("tearDownAfterClass()");
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }


  /** Get a list of all Heroes in the Dormitory. There should be at least three. */
  @Test
  public void getAllHeroes()
  {
    MsgCtrl.auditMsgsOn(true);
    MsgCtrl.errorMsgsOn(true);
    MsgCtrl.where(this);

    MainActionCiv mac = new MainActionCiv(new MainframeCiv());
    assertNotNull(mac);
    ArrayList<Hero> heroList = mac.getAllHeroes();
    assertNotNull(heroList);

  }

} // end of TA03a_SummonHero integration test
