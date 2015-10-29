/**
 * Elf.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc.character;

import java.util.ArrayList;

import pdc.character.Hero.PrimeTraits;

/**
 * @author Al Cline
 * @version Sep 6, 2015 // original <br>
 */
public class Elf extends Race
{
  // Statics and transients that are not serialized with the Race class hierarchy
  /** Recommended serialization constant. */
  static final long serialVersionUID = 1100L;

  /** Racial limits for a Elf for the traits */
  private final int[] minLimit = { 7,  8,  7,  7,  7,  8};
  private final int[] maxLimit = {18, 18, 18, 19, 18, 18};

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
      "Resistance to Sleep and Charm spells (90%) (second Save allowed on first fail)", 
      "Archery: +1 To Hit with bow (not crossbow)", 
      "Tingling: Detect hidden or secret doors if within 10' (67% active; 33% passive)",
      "Move Silently (26%)"
  };

  /**
   * Default constructor, called reflectively by Klass
   */
  public Elf() 
  {
    _raceName = "Elf";
    _raceLang = "Elvish";
    _minLimit = minLimit;
    _maxLimit = maxLimit;
    // Define weight ranges for Hero
    _weightMaleMedValue = _maleMedValue;
    _weightFemaleMedValue = _femaleMedValue;
    _weightLowDice = _wtLowDice;
    _weightHighDice = _wtHighDice;
    // Define height ranges for Hero
    _heightMaleMedValue = _htMaleMedValue;
    _heightFemaleMedValue = _htFemaleMedValue;
    _heightLowDice = _htLowDice;
    _heightHighDice = _htHighDice;
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

  

}
