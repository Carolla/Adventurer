/**
 * Hobbit.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.race;

import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;


/**
 * @author Al Cline
 * @version Sep 6, 2015 // original <br>
 */
public class Hobbit extends Race
{
  // /** Weight ranges */
  // protected final RangedValue _weightRange = new RangedValue(50, "2d4", "2d6");
  // /** Height ranges */
  // protected final RangedValue _heightRange = new RangedValue(33, "d3", "d6");

  private final String RACE_NAME = "Hobbit";
  private final String RACE_LANGUAGE = "Tolkeen";

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
  public Hobbit()
  {
    super._raceName = RACE_NAME;
    super._raceLang = RACE_LANGUAGE;

    _descriptor = _raceDescriptor;
    _racialThiefMods = _hobbitThiefMods;
    _raceSkills = _hobbitSkills;
  }


  /** Hobbits are more agile but weaker: STR-1, DEX+1 */
  @Override
  public TraitList adjustTraitsForRace(TraitList traits)
  {
    traits.adjust(PrimeTraits.STR, -1);
    traits.adjust(PrimeTraits.DEX, 1);
    return traits;
  };


  @Override
  public int calcWeight()
  {
    // return calcWeight(WT_AVG, WT_RANGE, WT_RANGE_DICE);
    return 42;
  }


  @Override
  public int calcHeight()
  {
    // return calcHeight(HT_AVG, HT_RANGE, HT_RANGE_DICE);
    return 42;
  }


  /* (non-Javadoc)
   * @see chronos.pdc.race.Race#setTraitLimits(chronos.pdc.character.TraitList)
   */
  @Override
  public TraitList setTraitLimits(TraitList traits)
  {
    // TODO Auto-generated method stub
    return null;
  };

  // /* (non-Javadoc)
  // * @see chronos.pdc.race.Race#getRaceLang()
  // */
  // @Override
  // protected String getRaceLang()
  // {
  // // TODO Auto-generated method stub
  // return null;
  // };


} // end of Hobbit subclass

