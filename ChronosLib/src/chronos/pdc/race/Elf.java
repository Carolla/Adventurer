/**
 * Elf.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
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
public class Elf extends Race
{
  // /** Weight ranges */
  // protected final RangedValue _weightRange = new RangedValue(80, "d10", "d20");
  // /** Height ranges */
  // protected final RangedValue _heightRange = new RangedValue(54, "d4", "d6");

  private final String RACE_NAME = "Elf";
  private final String RACE_LANGUAGE = "Elvish";

  /** Elves have pointed ears */
  private final String _raceDescriptor = "pointed ears";

  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  // Hide in Shadows | Listening | Climb Walls | Back Attack
  protected final int[] _elfThiefMods = {0, 5, -5, 0, 5, 10, 5, 0, 5};

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
  public Elf()
  {
    _raceName = RACE_NAME;
    _raceLang = RACE_LANGUAGE;

    _descriptor = _raceDescriptor;
    _racialThiefMods = _elfThiefMods;
    _raceSkills = _elfSkills;
  }


  /** Elves are more agile, but not as hardy: CON-1, DEX+1 */
  @Override
  public TraitList adjustTraitsForRace(TraitList traits)
  {
    traits.adjust(PrimeTraits.CON, -1);
    traits.adjust(PrimeTraits.DEX, 1);
    return traits;
  };


  @Override
  public int calcWeight()
  {
//    return calcWeight(WT_AVG, WT_RANGE, WT_RANGE_DICE);
    return 42;
  }

  @Override
  public int calcHeight()
  {
//    return calcHeight(HT_AVG, HT_RANGE, HT_RANGE_DICE);
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


} // end of Elf subclass

