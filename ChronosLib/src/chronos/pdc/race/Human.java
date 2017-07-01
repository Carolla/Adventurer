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
 *          May 30, 2017 // revised weight and height algorithm to revised {@code RangedValues} <br>
 *          Jun 27, 2017 // removed getRaceLang() and set base class field instead <br>
 */
public class Human extends Race
{
  /** Weights and heights are generated in a normal distribution about an Average over a Range */
  protected final int WT_LOW = 130;
//  protected final int WT_RANGE = 100; // range: male [130, 230]; female [112, 212]
  protected final int HT_LOW = 60;
//  protected final int HT_RANGE = 18; // range: male [60, 78]; female [53, 71]
  protected final String WT_RANGE_DICE = "2d6-2"; // varying weight = 0 - 100 lb
  protected final String HT_RANGE_DICE = "2d10-2"; // varying height = 0 - 18 in

  private final String RACE_NAME = "Human";
  private final String RACE_LANGUAGE = "";

  /** Racial limits for a male Human for the traits SIWCDCh */
  private final int[] MALE_MINLIMIT = {8, 8, 8, 8, 8, 8};
  private final int[] MALE_MAXLIMIT = {18, 18, 18, 18, 18, 18};
  /** Female limits after adjustments from the male: STR-1, CON+1, CHR+1 */
  private final int[] FEMALE_MINLIMIT = {7, 8, 8, 9, 8, 9};
  private final int[] FEMALE_MAXLIMIT = {17, 18, 18, 19, 18, 19};

  /** Human has no Race descriptor, so merely ends the description suffix */
  private final String _raceDescriptor = "a naive look in the eyes";

  /** No special human skills, but don't leave this null */
  private final String[] _humanSkills = {"None"};

  /** Humans get no special mods for thief skills */
  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  // Hide in Shadows | Listening | Climb Walls | Back Attack
  // protected final int[] _humanThiefMods = {0, 0, 0, 0, 0, 0, 0, 0, 0};

  /**
   * Default constructor, called by Race base class
   * 
   * @param gender
   */
  public Human()
  {
    _raceName = RACE_NAME;
    _raceLang = RACE_LANGUAGE;
    _descriptor = _raceDescriptor;
    _raceSkills = _humanSkills;
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
  @Override
  public int calcWeight()
  {
    return calcWeight(WT_LOW, WT_RANGE_DICE);
  }


  /**
   * Call the base method with human-specific values
   * 
   * @return the gender-adjusted height of the Hero
   */
  @Override
  public int calcHeight()
  {
    return calcHeight(HT_LOW, HT_RANGE_DICE);
  }


  /**
   * Ensure that the traits fall within the proper male/female. After the limits are defined for
   * this subclass, the base class is called with that data.
   * 
   * @param traits the six prime traits of any Hero
   * @return the adjusted traits
   */
  @Override
  public TraitList setTraitLimits(TraitList traits)
  {
    if (_gender.isFemale()) {
      traits = constrainTo(traits, FEMALE_MINLIMIT, FEMALE_MAXLIMIT);
    } else {
      traits = constrainTo(traits, MALE_MINLIMIT, MALE_MAXLIMIT);
    }
    return traits;
  }


} // end of Human subclass
