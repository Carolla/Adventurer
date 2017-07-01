/**
 * HalfOrc.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
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
public class HalfOrc extends Race
{
//  /** Weight ranges */
//  protected final RangedValue _weightRange = new RangedValue(150, "3d8", "4d10");
//  /** Height ranges */
//  protected final RangedValue _heightRange = new RangedValue(65, "2d4");

  private final String RACE_NAME = "Half-Orc";
  private final String RACE_LANGUAGE = "Orcish";

  /** Half-orcs are burly and pig-like */
  private final String _raceDescriptor = "a squat snoutish face";

  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  //    Hide in Shadows | Listening | Climb Walls | Back Attack
  protected final int[] _halforcThiefMods = {5, -5, 5, 5, 0, 0, 5, -5, 0};

  // Special Half-Orc skills
  private final String[] _halforcSkills = {"Infravision (60')"};


  /**
   * Default constructor, called reflectively by Race
   * @param gender 
   */
  public HalfOrc()
  {
    _raceName = RACE_NAME;
    _raceLang = RACE_LANGUAGE;

    _descriptor = _raceDescriptor;
    _racialThiefMods = _halforcThiefMods;
    _raceSkills = _halforcSkills;

  }

  
  /** Half-Orcs are sturdier, stronger, and uglier: STR+1, CON+1, CHR-2 */
  @Override
  public TraitList adjustTraitsForRace(TraitList traits)
  {
    traits.adjust(PrimeTraits.STR, 1);
    traits.adjust(PrimeTraits.CON, 1);
    traits.adjust(PrimeTraits.CHR, -2);
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

  
//  /** Half-orc has 50% chance of knowing elvish */
//  @Override
//  protected String getRaceLang()
//  {
//    String s = (_md.rollPercent() < 50) ? "Common" : "Orcish";
//    return s;
//  }

  
} // end of HalfOrc subclass
