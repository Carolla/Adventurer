/**
 * HalfOrc.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.race;

import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;

/**
 * @author Al Cline
 * @version Sep 6, 2015 // original <br>
 */
public class HalfOrc extends Race
{
  private final String RACE_NAME = "Half-Orc";

  /** Weights and heights are generated in a normal distribution about an average over a Range */
  protected final int WT_LOW = 140; // range: male [140, 260]; female [126, 246]
  protected final int WT_AVG = 200; 
  protected final int HT_LOW = 60; // range: male [60, 80]; female [54, 74]
  protected final int HT_AVG = 70; 
//  protected final String HT_RANGE_DICE = "2d11-2"; // varying height = [0 - 20] in
//  protected final String WT_RANGE_DICE = "2d7-2"; // varying weight = [0 - 120] lb

  /** Racial limits for a male for the traits SIWCDCh: STR+1, CON+1, CHR-2 */
  protected final int[] MALE_MINLIMIT = {9, 8, 8, 9, 8, 6};
  protected final int[] MALE_MAXLIMIT = {19, 18, 18, 19, 18, 16};
  /** Female limits after adjustments from the male: STR-1, CON+1, CHR+1 */
  protected final int[] FEMALE_MINLIMIT = {8, 8, 8, 10, 8, 7};
  protected final int[] FEMALE_MAXLIMIT = {18, 18, 18, 20, 18, 17};

  /** Half-orcs are burly and pig-like */
  private final String _raceDescriptor = "a squat snoutish face";

  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  // Hide in Shadows | Listening | Climb Walls | Back Attack
  // protected final int[] _halforcThiefMods = {5, -5, 5, 5, 0, 0, 5, -5, 0};

  // Special Half-Orc skills
  private final String[] _halforcSkills = {"Infravision (60')"};


  /**
   * Default constructor, called reflectively by Race
   * 
   * @param gender
   */
  public HalfOrc(Gender gender, String hairColor)
  {
    _raceName = RACE_NAME;
    _raceLang = getRaceLang();
    _descriptor = _raceDescriptor;
    _raceSkills = _halforcSkills;
    _gender = gender;
    _hairColor = hairColor;
  }


  /** Half-Orcs are sturdier, stronger, and uglier: STR+1, CON+1, CHR-2 */
  @Override
  // public TraitList adjustTraitsForRace(TraitList traits)
  public void adjustTraitsForRace(TraitList traits)
  {
    traits.adjust(PrimeTraits.STR, 1);
    traits.adjust(PrimeTraits.CON, 1);
    traits.adjust(PrimeTraits.CHR, -2);
    // return traits;
  };


  @Override
  public int calcHeight()
  {
    return calcHeight(HT_LOW, HT_AVG);
  }


  @Override
  public int calcWeight()
  {
    return calcWeight(WT_LOW, WT_AVG);
  }


  /** Half-orc has 50% chance of knowing orcish */
  public String getRaceLang()
  {
    String s = (_md.rollPercent() <= 50) ? "" : "Orcish";
    return s;
  }


  // /**
  // * Ensure that the traits fall within the proper male/female. After the limits are defined for
  // * this subclass, the base class is called with that data.
  // *
  // * @param traits the six prime traits of any Hero
  // * @return the adjusted traits
  // */
  // @Override
  // public TraitList setTraitLimits(TraitList traits)
  // {
  // if (_gender.isFemale()) {
  // traits = constrainTo(traits, FEMALE_MINLIMIT, FEMALE_MAXLIMIT);
  // } else {
  // traits = constrainTo(traits, MALE_MINLIMIT, MALE_MAXLIMIT);
  // }
  // return traits;
  // };


} // end of HalfOrc subclass
