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
import chronos.pdc.character.Hero.PrimeTraits;

/**
 * @author Al Cline
 * @version Sep 6, 2015 // original <br>
 */
public class Elf extends Race
{
  // Statics and transients that are not serialized with the Race class hierarchy
  /** Recommended serialization constant. */
  static final long serialVersionUID = 1100L;

  /** Weight ranges */
  protected final int _maleMedValue = 100;;
  protected final int _femaleMedValue = 80;
  protected final String _wtLowDice = "d10";
  protected final String _wtHighDice = "d20";

  /** Height ranges */
  protected final int _htMaleMedValue = 60;;
  protected final int _htFemaleMedValue = 54;
  protected final String _htLowDice = "d4";
  protected final String _htHighDice = "d6";

  /** Elves have pointed ears */
  private final String _raceDescriptor = "pointed ears";
  
  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  //    Hide in Shadows | Listening | Climb Walls | Back Attack
  protected final int[] _elfThiefMods = {0, 5,-5, 0, 5, 10, 5, 0, 5 };

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
   * @param gender 
   */
  public Elf(Gender gender) 
  {
    _raceName = "Elf";
    _raceLang = "Elvish";
    
    // Define height ranges for Hero
    if (gender.isMale()) {
      // Define weight ranges for Hero
      _heightMedValue = _htMaleMedValue;
      _weightMedValue = _maleMedValue;
    } else {
      // Define height ranges for Hero
      _weightMedValue = _femaleMedValue;
      _heightMedValue = _htFemaleMedValue;
    }

    _descriptor = _raceDescriptor;
    _racialThiefMods = _elfThiefMods;
    _raceSkills = _elfSkills;
  } 


  /** Elves are more agile, but not as hardy: CON-1, DEX+1 */
  @Override
  public int[] adjustTraitsForRace(int[] traits)
  {
    traits[PrimeTraits.CON.ordinal()] -= 1;
    traits[PrimeTraits.DEX.ordinal()] += 1;
    return traits;    
  };

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