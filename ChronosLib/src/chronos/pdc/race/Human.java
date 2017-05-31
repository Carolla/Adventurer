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
 *          May 30, 2017 // revised weight and height algorithm to revised {@code RangedValues}
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
  // protected final RangedValue _weightRangeFemale = new RangedValue(130, "3d12", "5d12");
  // protected final RangedValue _weightRangeMale = new RangedValue(175, "3d12", "5d12");
  //protected final RangedValue _weightRange = new RangedValue(130, "3d4");
  protected final int MALE_LOW_WT = 130; 
  protected final double FEMALE_ADJ = 0.90; // weight and height is 10% less
  protected final String WT_RANGE_DICE = "2d6-2";  // varying weight = 0 - 100 lb
  protected int _weight;
  
  protected final int MALE_LOW_HT = 60; 
  protected final String HT_RANGE_DICE = "2d10-2";  // varying height = 0 - 18 in
  protected int _height;

  /** Height ranges */
  protected final RangedValue _heightRange = new RangedValue(64, "d12");

  /** Human has no Race descriptor, so merely ends the description suffix */
  private final String _raceDescriptor = "a naive look in the eyes";

  /** No special human skills, but don't leave this null */
  private final String[] _humanSkills = {"None"};

  /** Humans get no special mods for thief skills */
  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  // Hide in Shadows | Listening | Climb Walls | Back Attack
  protected final int[] _humanThiefMods = {0, 0, 0, 0, 0, 0, 0, 0, 0};

  /**
   * Default constructor, called reflectively by Klass
   * 
   * @param gender
   */
  // public Human(Gender gender)
  public Human()
  {
    _raceName = "Human";
    _minLimit = minLimit;
    _maxLimit = maxLimit;
    _raceLang = "";

    _descriptor = _raceDescriptor;
    _racialThiefMods = _humanThiefMods;
    _raceSkills = _humanSkills;
  }


  /**
   * Call the base method with these human-specific values
   * 
   * @param g male or female
   * @return the gender-adjusted weight of the Hero
   */
  public int calcWeight(Gender g)
  {
    int lowWt = (int) ((g.isMale()) ? MALE_LOW_WT : MALE_LOW_WT * FEMALE_ADJ);
    _weight = calcWeight(lowWt, WT_RANGE_DICE);
    return _weight;
  }

  
  /**
   * Call the base method with these human-specific values
   * 
   * @param g male or female
   * @return the gender-adjusted weight of the Hero
   */
  public int calcHeight(Gender g)
  {
    int lowHt = (int) ((g.isMale()) ? MALE_LOW_HT : MALE_LOW_HT * FEMALE_ADJ);
    _height = calcHeight(lowHt, HT_RANGE_DICE);
    return _height;
  }

//  @Override
//  public int calcHeight()
//  {
//    return _heightRange.calcValue();
//  };
  
  
}
