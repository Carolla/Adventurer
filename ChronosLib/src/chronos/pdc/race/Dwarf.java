/**
 * Dwarf.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
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
import chronos.pdc.race.Race;

/**
 * @author Al Cline
 * @version Sep 6, 2015 // original <br>
 */

public class Dwarf extends Race
{
  /** Weight ranges */
  protected final RangedValue _weightRange = new RangedValue(120, "2d8", "2d11");

  /** Height ranges */
  protected final RangedValue _heightRange = new RangedValue(65, "d4", "d6");
  
  /** All Dwarves have beards, even the females */
  private final String _raceDescriptor = "a scraggly beard";

  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  //    Hide in Shadows | Listening | Climb Walls | Back Attack
  protected final int[] _dwarfThiefMods = {15, 0, 10, 15, 0, 0, 0, -10, 0};

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
   * @param gender 
   */
  public Dwarf()
  {
    _raceName = "Dwarf";
    _raceLang = "Groken";
    
    _descriptor = _raceDescriptor;
    _racialThiefMods = _dwarfThiefMods;
    _raceSkills = _dwarfSkills;
  }



  /** Dwarfs are hardier but less likeable: CON+1, CHR-1
   * 
   *  @param traits `original umadjusted traits
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
    return _weightRange.calcValue();
  }

  @Override
  public int calcHeight()
  {
    return _heightRange.calcValue();
  };
}
