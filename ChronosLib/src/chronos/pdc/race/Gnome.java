/**
 * Gnome.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
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
public class Gnome extends Race
{
  /** Weight and height is generated in a normal distribution across the range's average value */
  protected final int WT_LOW = 60;
//  protected final int WT_RANGE = 40; // male range [60, 100]
  protected final int HT_LOW = 33;
//  protected final int HT_RANGE = 6; // male range [33, 39]
  protected final String WT_RANGE_DICE = "2d3-2"; // times 10: varying weight = 0 - 40 lb
  protected final String HT_RANGE_DICE = "2d4-2"; // varying height = 0 - 6 in


  /** Racial limits for a male Gnome for the traits SIWCDCh: STR-1, CHR-1 */
  protected final int[] MALE_MINLIMIT = {6, 8, 8, 8, 8, 7};
  protected final int[] MALE_MAXLIMIT = {16, 18, 18, 18, 18, 17};
  /** Female limits after adjustments from the male: STR-1, CON+1, CHR+1 */
  protected final int[] FEMALE_MINLIMIT = {5, 8, 8, 9, 8, 8};
  protected final int[] FEMALE_MAXLIMIT = {15, 18, 18, 19, 18, 18};

  private final String RACE_NAME = "Gnome";
  private final String RACE_LANGUAGE = "Gnomish";

  /** Gnomes have piercing blue eyes */
  private final String _raceDescriptor = "piercing blue eyes";

  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  // Hide in Shadows | Listening | Climb Walls | Back Attack
  // protected final int[] _gnomeThiefMods = {10, 0, 5, 10, 5, 5, 10, -15, 5};

  // Special Gnome skills
  private final String[] _gnomeSkills = {
      "Infravision (60')",
      "Detect slopes in underground passages (80%)",
      "Detect unsafe walls, ceilings, floors (70%)",
      "Detect direction of underground travel (50%)",
      "Determine approximate underground depth (60%)"
  };


  /**
   * Default constructor, called reflectively by Klass
   * 
   * @param gender
   */
  public Gnome()
  {
    _raceName = RACE_NAME;
    _raceLang = RACE_LANGUAGE;
    _descriptor = _raceDescriptor;
    _raceSkills = _gnomeSkills;
  }


  /**
   * Male Gnome has a few adjustments from the default: STR-2, INT-1
   * 
   * @param traits the six prime traits of any Hero
   * @return the adjusted traits
   */
  @Override
  public TraitList adjustTraitsForRace(TraitList traits)
  {
    traits.adjust(PrimeTraits.STR, -2);
    traits.adjust(PrimeTraits.CHR, -1);
    return traits;
  }


  /**
   * Call the base method with gnome-specific values
   * 
   * @return the gender-adjusted height of the Hero
   */
  @Override
  public int calcHeight()
  {
//    return calcHeight(HT_LOW, HT_RANGE, HT_RANGE_DICE);
    return calcHeight(HT_LOW, HT_RANGE_DICE);
  }


  /**
   * Call the base method with these gnome-specific values
   * 
   * @return the gender-adjusted weight of the Hero
   */
  @Override
  public int calcWeight()
  {
//    return calcWeight(WT_LOW, WT_RANGE, WT_RANGE_DICE);
    return calcWeight(WT_LOW, WT_RANGE_DICE);
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
    if (_gender.equals(Gender.FEMALE_STRING)) {
      traits = constrainTo(traits, FEMALE_MINLIMIT, FEMALE_MAXLIMIT);
    } else {
      traits = constrainTo(traits, MALE_MINLIMIT, MALE_MAXLIMIT);
    }
    return traits;
  }


} // end of Gnome subClass
