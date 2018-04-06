/**
 * Elf.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
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
 *          July 1, 2017 // refactored for Peasant klass and base methods {br}
 */
public class Elf extends Race
{
  private final String RACE_NAME = "Elf";
  private final String RACE_LANGUAGE = "Elvish";
  /** Elves have pointed ears */
  private final String _raceDescriptor = "pointed ears";

  /** Weights and heights are generated in a normal distribution about an average over a Range */
  protected final int WT_LOW = 80;   // range: male [80, 120]; female [72, 112]
  protected final int WT_HIGH = 120; 
  protected final int HT_LOW = 60;    // range: male [60, 72]; female [54, 66]
  protected final int HT_HIGH = 72;  

  /** Racial limits for a male dwarf for the traits SIWCDCh: CON-1, DEX+1 */
  protected final int[] MALE_MINLIMIT = { 8,  8,  8,  7,  9,  8};
  protected final int[] MALE_MAXLIMIT = {18, 18, 19, 17, 19, 19};
  /** Female limits after adjustments from the male: STR-1, CON+1, CHR+1 */
  protected final int[] FEMALE_MINLIMIT = { 7,  8,  8,  8,  9,  9};
  protected final int[] FEMALE_MAXLIMIT = {17, 18, 18, 18, 19, 19};

  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  // Hide in Shadows | Listening | Climb Walls | Back Attack
//  protected final int[] _elfThiefMods = {0, 5, -5, 0, 5, 10, 5, 0, 5};

  // Special Elf skills
  private final String[] _elfSkills = {
      "Infravision (60')",
      "Resistance to Sleep and Charm spells (90%) (second std Save allowed if first fails)",
      "Archery: +1 To Hit with bow (not crossbow)",
      "Tingling: Detect hidden or secret doors if within 10' (67% active; 33% passive)",
      "Move Silently (26%)"
  };

  /**
   * Default constructor, called reflectively by Klass
   * 
   * @param gender
   */
  public Elf(Gender gender, String hairColor)
  {
    _raceName = RACE_NAME;
    _raceLang = RACE_LANGUAGE;
    _descriptor = _raceDescriptor;
    _raceSkills = _elfSkills;
    _gender = gender;
    _hairColor = hairColor;
  }


  /** Elves are more agile, but not as hardy: CON-1, DEX+1 */
  @Override
//  public TraitList adjustTraitsForRace(TraitList traits)
  public void adjustTraitsForRace(TraitList traits)
  {
    traits.adjust(PrimeTraits.CON, -1);
    traits.adjust(PrimeTraits.DEX, 1);
//    return traits;
  };


  @Override
  public int calcHeight()
  {
    return calcVariance(HT_LOW, HT_HIGH);
  }


  @Override
  public int calcWeight()
  {
    return calcVariance(WT_LOW, WT_HIGH);
  }


//  /**
//   * Ensure that the traits fall within the proper male/female. After the limits are defined for
//   * this subclass, the base class is called with that data.
//   * 
//   * @param traits the six prime traits of any Hero
//   * @return the adjusted traits
//   */
//  @Override
//  public TraitList setTraitLimits(TraitList traits)
//  {
//    if (_gender.isFemale()) {
//      traits = constrainTo(traits, FEMALE_MINLIMIT, FEMALE_MAXLIMIT);
//    } else {
//      traits = constrainTo(traits, MALE_MINLIMIT, MALE_MAXLIMIT);
//    }
//    return traits;
//  }


} // end of Elf subclass

