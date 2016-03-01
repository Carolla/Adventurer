/**
 * Human.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.race;

import chronos.pdc.character.Gender;



/**
 * @author Al Cline
 * @version Sep 4, 2015 // original <br>
 */
public class Human extends Race
{
  // Statics and transients that are not serialized with the Race class hierarchy
  /** Recommended serialization constant. */
  static final long serialVersionUID = 1100L;

  /** Racial limits for a Human for the traits */
  private final int[] minLimit = {8, 8, 8, 8, 8, 8};
  private final int[] maxLimit = {18, 18, 18, 18, 18, 18};

  /** Weight ranges */
  protected final RangedValue _weightRange = new RangedValue(130, "3d12", "5d12");

  /** Height ranges */
  protected final RangedValue _heightRange = new RangedValue(64, "d12");
  
  /** Human has no Race descriptor, so merely ends the description suffix */
  private final String _raceDescriptor = "a naive look in the eyes";

  /** No special human skills, but don't leave this null */
  private final String[] _humanSkills = {"None"};

  /** Humans get no special mods for thief skills */
  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  //    Hide in Shadows | Listening | Climb Walls | Back Attack
  protected final int[] _humanThiefMods = {0, 0, 0, 0, 0, 0, 0, 0, 0};

  /**
   * Default constructor, called reflectively by Klass
   * @param gender 
   */
  public Human(Gender gender)
  {
    _raceName = "Human";
    _minLimit = minLimit;
    _maxLimit = maxLimit;
    _raceLang = "";

    _descriptor = _raceDescriptor;
    _racialThiefMods = _humanThiefMods;
    _raceSkills = _humanSkills;
  }


  @Override
  public int calcWeight()
  {
    return _weightRange.calcValue();
  }

  @Override
  public int calcHeight()
  {
    return _heightRange.calcValue();
  };
}
