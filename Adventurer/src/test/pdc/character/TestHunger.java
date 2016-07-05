/**
 * TestHunger.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package test.pdc.character;

import static org.junit.Assert.assertEquals;
import mylib.MsgCtrl;
import mylib.pdc.Utilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pdc.character.Hunger;

/**
 * @author Alan Cline
 * @version Oct 30, 2010 // original <br>
 *          April 9, 2011 // TAA added Error/Nulls <br>
 *          Mar 29 2016 // Reviewed and tested for overall QA <br>
 */
public class TestHunger
{
  Hunger _hunger = null;

  @Before
  public void setUp()
  {
    _hunger = new Hunger();
  }

  @After
  public void tearDown()
  {
    MsgCtrl.auditMsgsOn(false);
    MsgCtrl.errorMsgsOn(false);
  }
  
  @Test
  public void hungerStateStartsAsFull()
  {
    assertEquals(Hunger.State.FULL, _hunger.findHungerState());    
  }
  
  @Test
  public void notHungryAfter6Hours()
  {
    Hunger.State hungerState = _hunger.burn(6 * Utilities.SECONDS_PER_HOUR);
    assertEquals(Hunger.State.NOT_HUNGRY, hungerState);
  }
  
  @Test
  public void findHungerAfterOneDay()
  {
    Hunger.State hungerState = _hunger.burn(Utilities.SECONDS_PER_DAY);
    assertEquals(Hunger.State.HUNGRY, hungerState);
  }

  @Test
  public void findHungerAfterTwoDay()
  {
    Hunger.State hungerState = _hunger.burn(2 * Utilities.SECONDS_PER_DAY);
    assertEquals(Hunger.State.WEAK, hungerState);
  }
  
  @Test
  public void findHungerAfterThreeDays()
  {
    Hunger.State hungerState = _hunger.burn(3 * Utilities.SECONDS_PER_DAY);
    assertEquals(Hunger.State.STARVED, hungerState);    
  }
} // end of TestHunger class

