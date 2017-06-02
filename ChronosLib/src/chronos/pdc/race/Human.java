/**
 * Human.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.race;

import chronos.pdc.character.TraitList;


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
  protected final int[] minLimit = {8, 8, 8, 8, 8, 8};
  protected final int[] maxLimit = {18, 18, 18, 18, 18, 18};

  /** Weight ranges */
  protected final int MALE_LOW_WT = 130;
  protected final double FEMALE_ADJ = 0.90; // weight and height is 10% less
  protected final String WT_RANGE_DICE = "2d6-2"; // varying weight = 0 - 100 lb
  protected int _weight;

  protected final int MALE_LOW_HT = 60;
  protected final String HT_RANGE_DICE = "2d10-2"; // varying height = 0 - 18 in
  protected int _height;

  /** Human has no Race descriptor, so merely ends the description suffix */
  private final String _raceDescriptor = "a naive look in the eyes";

  /** No special human skills, but don't leave this null */
  private final String[] _humanSkills = {"None"};

  /** Humans get no special mods for thief skills */
  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  // Hide in Shadows | Listening | Climb Walls | Back Attack
//  protected final int[] _humanThiefMods = {0, 0, 0, 0, 0, 0, 0, 0, 0};

  /**
   * Default constructor, called by Race base class
   * 
   * @param gender
   */
  public Human()
  {
    super._raceName = "Human";
    super._minLimit = minLimit;
    super._maxLimit = maxLimit;
    super._raceLang = "";
    super._descriptor = _raceDescriptor;
    super._raceSkills = _humanSkills;
  }


  /**
   * Human has no adjustments, but traits must remain within minLimit and maxLimit bounds
   * 
   * @param traits the six prime traits of any Hero
   * @return the adjusted traits
   */
  @Override
  public TraitList adjustTraitsForRace(TraitList traits)
  {
    return traits;
  }


  /**
   * Call the base method with these human-specific values
   * 
   * @return the gender-adjusted weight of the Hero
   */
  public int calcWeight()
  {
    int lowWt = (int) ((_gender.isMale()) ? MALE_LOW_WT : MALE_LOW_WT * FEMALE_ADJ);
    _weight = calcWeight(lowWt, WT_RANGE_DICE);
    return _weight;
  }


  /**
   * Call the base method with these human-specific values
   * 
   * @param g male or female
   * @return the gender-adjusted weight of the Hero
   */
  public int calcHeight()
  {
    int lowHt = (int) ((_gender.isMale()) ? MALE_LOW_HT : MALE_LOW_HT * FEMALE_ADJ);
    _height = calcHeight(lowHt, HT_RANGE_DICE);
    return _height;
  }

  

} // end of Human subclass
