/**
 * Hobbit.java Copyright (c) 2017, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@wowway.com
 */

package chronos.pdc.race;

import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;


/**
 * @author Al Cline
 * @version Sep 6, 2015 // original <br>
 *          July 1, 2017 // refactored for Peasant klass and reduce duplication <br>
 */
public class Hobbit extends Race
{
  private final String RACE_NAME = "Hobbit";
  private final String RACE_LANGUAGE = "Tolkeen";

  /** Weight and height is generated in a normal distribution across the range's average value */
  protected final int WT_LOW = 70; // male range [70, 110]; female [63, 103]
  protected final int HT_LOW = 38; // male range [38, 46]; female [34, 42]
  protected final String WT_RANGE_DICE = "2d3-2"; // times 10: varying weight = 0 - 40 lb
  protected final String HT_RANGE_DICE = "2d5-2"; // varying height = 0 - 8 in

  /** Racial limits for a male Gnome for the traits SIWCDCh: STR-1, CON+1, DEX+1 */
  protected final int[] MALE_MINLIMIT = {7, 8, 8, 9, 9, 8};
  protected final int[] MALE_MAXLIMIT = {17, 18, 18, 19, 19, 18};
  /** Female limits after adjustments from the male: STR-1, CON+1, CHR+1 */
  protected final int[] FEMALE_MINLIMIT = {6, 8, 8, 10, 9, 9};
  protected final int[] FEMALE_MAXLIMIT = {16, 18, 18, 20, 19, 19};

  /** Hobbits have hairy bare feet */
  private final String _raceDescriptor = "hairy bare feet";

  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  // Hide in Shadows | Listening | Climb Walls | Back Attack
  protected final int[] _hobbitThiefMods = {5, 5, 5, 5, 10, 15, 5, -15, 10};

  // Special Hobbit skills
  private final String[] _hobbitSkills = {
      "Infravision (30')",
      "Resistance to Poison: Special Save includes HPMod and Magic Attack Mod",
      "Detect slopes in underground passages (75%)",
      "Determine direction of underground travel (50%)"
  };

  /**
   * Default constructor, called reflectively by Klass
   * 
   * @param gender
   */
  public Hobbit(Gender gender, String hairColor)
  {
    _raceName = RACE_NAME;
    _raceLang = RACE_LANGUAGE;
    _descriptor = _raceDescriptor;
    _raceSkills = _hobbitSkills;
    _gender = gender;
    _hairColor = hairColor;
  }


  /** Hobbits are more agile and hearty, but weaker: STR-1, CON+1, DEX+1 */
  @Override
  public TraitList adjustTraitsForRace(TraitList traits)
  {
    traits.adjust(PrimeTraits.STR, -1);
    traits.adjust(PrimeTraits.CON, 1);
    traits.adjust(PrimeTraits.DEX, 1);
    return traits;
  };


  /**
   * Call the base method with race-specific values
   * 
   * @return the gender-adjusted height of the Hero
   */
  @Override
  public int calcHeight()
  {
    return calcHeight(HT_LOW, HT_RANGE_DICE);
  }


  /**
   * Call the base method with race-specific values
   * 
   * @return the gender-adjusted weight of the Hero
   */
  @Override
  public int calcWeight()
  {
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
    if (_gender.isFemale()) {
      traits = constrainTo(traits, FEMALE_MINLIMIT, FEMALE_MAXLIMIT);
    } else {
      traits = constrainTo(traits, MALE_MINLIMIT, MALE_MAXLIMIT);
    }
    return traits;
  }


} // end of Hobbit subclass

