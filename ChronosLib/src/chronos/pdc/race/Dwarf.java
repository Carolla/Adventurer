/**
 * Dwarf.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
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
 *          June 27, 2017 // simplified and adjusted for Peasant klass <br>
 */

public class Dwarf extends Race
{
  /** Weights and heights are generated in a normal distribution about an Average over a Range */
  protected final int WT_AVG = 150;
  protected final int WT_RANGE = 80; // range: male [110, 190]; female [99, 179]
  protected final int HT_AVG = 54;
  protected final int HT_RANGE = 10; // range: male [49, 59]; female [44, 54]
  protected final String WT_RANGE_DICE = "2d5-2"; // varying weight = (0 - 8) * 10 lb
  protected final String HT_RANGE_DICE = "2d6-2"; // varying height = (0 - 10) in

  private final String RACE_NAME = "Dwarf";
  private final String RACE_LANGUAGE = "Groken";

  /** All Dwarves have beards, even the females */
  private final String _raceDescriptor = "a scraggly beard";

  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  // Hide in Shadows | Listening | Climb Walls | Back Attack
  // protected final int[] _dwarfThiefMods = {15, 0, 10, 15, 0, 0, 0, -10, 0};

  // Special Dwarf skills
  private final String[] _dwarfSkills = {
      "Infravision (60')",
      "Detect slopes in underground passages (75%)",
      "Detect new construction in tunnel (75%)",
      "Detecting sliding or shifting walls or rooms (66%)",
      "Detect stonework traps (50%)",
      "Determine approximate underground depth (50%)"
  };


  /**
   * Default constructor, called reflectively by Klass
   */
  public Dwarf()
  {
    super._raceName = RACE_NAME;
    super._raceLang = RACE_LANGUAGE;

    super._descriptor = _raceDescriptor;
    // _racialThiefMods = _dwarfThiefMods;
    super._raceSkills = _dwarfSkills;
  }


  /**
   * Dwarfs are hardier but less likable: CON+1, CHR-1
   * 
   * @param traits `original unadjusted traits
   */
  @Override
  public TraitList adjustTraitsForRace(TraitList traits)
  {
    traits.adjust(PrimeTraits.CON, 1);
    traits.adjust(PrimeTraits.CHR, -1);
    return traits;
  }


  @Override
  public int calcWeight()
  {
    return calcWeight(0, WT_RANGE_DICE);
  }
  

  @Override
  public int calcHeight()
  {
    return calcHeight(0, HT_RANGE_DICE);
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

  // /** A Dwarf naturally speaks Groken */
  // @Override
  // protected String getRaceLang()
  // {
  // return RACE_LANGUAGE;
  // }

}
