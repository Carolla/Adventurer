/**
 * Dwarf.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc.character.race;

import chronos.pdc.character.Gender;
import chronos.pdc.character.Trait.PrimeTraits;
import chronos.pdc.race.Race;

/**
 * @author Al Cline
 * @version Sep 6, 2015 // original <br>
 */

public class Dwarf extends Race
{
  // Statics and transients that are not serialized with the Race class hierarchy
  /** Recommended serialization constant. */
  static final long serialVersionUID = 1100L;

  /** Weight ranges */
  protected final int _wtMaleMedValue = 150;
  protected final int _wtFemaleMedValue = 120;
  protected final String _wtLowDice = "2d8";
  protected final String _wtHighDice = "2d12";

  /** Height ranges */
  protected final int _htMaleMedValue = 48;;
  protected final int _htFemaleMedValue = 46;
  protected final String _htLowDice = "d4";
  protected final String _htHighDice = "d6";

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
  public Dwarf(Gender gender)
  {
    _raceName = "Dwarf";
    _raceLang = "Groken";

    if (gender.isMale()) {
      // Define weight ranges for Hero
      _heightMedValue = _htMaleMedValue;
      _weightMedValue = _wtMaleMedValue;
    } else {
      // Define height ranges for Hero
      _weightMedValue = _wtFemaleMedValue;
      _heightMedValue = _htFemaleMedValue;
    }
    
    _descriptor = _raceDescriptor;
    _racialThiefMods = _dwarfThiefMods;
    _raceSkills = _dwarfSkills;
  }



  /** Dwarfs are hardier but less likeable: CON+1, CHR-1
   * 
   *  @param traits `original umadjusted traits
   */
  @Override
  public int[] adjustTraitsForRace(int[] traits)
  {
    traits[PrimeTraits.CON.ordinal()] += 1;
    traits[PrimeTraits.CHR.ordinal()] -= 1;
    return traits;
  }

  @Override
  public int calcWeight()
  {
    return super.calcWeight(_wtLowDice, _wtHighDice);
  }

  @Override
  public int calcHeight()
  {
    return super.calcWeight(_htLowDice, _htHighDice);
  };
}
