/**
 * Hunger.java Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc.character;

import mylib.pdc.Utilities;

/**
 * Hunger determines if the Person needs to eat or not, measured in satiety points to represent
 * calories. A person's initial maximum satiety points are based on the nutritional rule of 15
 * calories per pound times their weight. This class forces persons to risk eating things in the
 * dungeon, such as food found, potions and poisons. As they burn calories, they will move from a
 * FULL state to a STARVING state. If the person has no satiety points, he will begin losing STR
 * points until dead (or until he eats something.)
 * 
 * @author Alan Cline
 * @version <DL>
 *          <DT>Build 1.0 Oct 30, 2010 // original <DD> <DT>Build 1.1 April 10 2011 // TAA added
 *          check for invalid weight <DD>
 *          </DL>
 */
public class Hunger
{
  public enum State {
    FULL, NOT_HUNGRY, HUNGRY, WEAK, STARVED
  };

  private static final int SATIETY_PER_DAY = 100;
  private static final int MAX_SATIETY = 3 * SATIETY_PER_DAY;

  private int _curSatiety;
  private State _hunger;


  /**
   * Constructor
   */
  public Hunger()
  {
    _curSatiety = MAX_SATIETY;
    _hunger = State.FULL;
  }


  /**
   * Calculate the hunger of the Person as they perform certain activities
   * 
   * @param burnTime the time the burnrate is applied
   * @return state after the elapsed time
   */
  public State burn(long burnTime)
  {
    int pointsBurned = (int) (burnTime * SATIETY_PER_DAY / Utilities.SECONDS_PER_DAY);
    _curSatiety -= pointsBurned;
    return findHungerState();
  }

  /**
   * @return one of the Hunger States
   */
  public State findHungerState()
  {
    if (_curSatiety == MAX_SATIETY) {
      _hunger = State.FULL;
    } else if (_curSatiety > 2 * SATIETY_PER_DAY) {
      _hunger = State.NOT_HUNGRY;
    } else if (_curSatiety > 1 * SATIETY_PER_DAY) {
      _hunger = State.HUNGRY;
    } else if (_curSatiety > 0) {
      _hunger = State.WEAK;
    } else {
      _hunger = State.STARVED;
    }
    return _hunger;
  }
} // end of Hunger class
