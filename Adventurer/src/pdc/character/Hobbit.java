/**
 * Hobbit.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc.character;

import chronos.pdc.Race;
import chronos.pdc.character.Hero.PrimeTraits;


/**
 * @author Al Cline
 * @version Sep 6, 2015 // original <br>
 */
public class Hobbit extends Race
{
  // Statics and transients that are not serialized with the Race class hierarchy
  /** Recommended serialization constant. */
  static final long serialVersionUID = 1100L;

  /** Racial limits for a Hobbit for the traits */
  private final int[] minLimit = { 7,  7,  7,  8, 10,  7};
  private final int[] maxLimit = {17, 18, 17, 18, 19, 18};

  /** Weight ranges */
  protected final int _maleMedValue = 60;;
  protected final int _femaleMedValue = 50;
  protected final String _wtLowDice = "2d4";
  protected final String _wtHighDice = "2d6";

  /** Height ranges */
  protected final int _htMaleMedValue = 36;;
  protected final int _htFemaleMedValue = 33;
  protected final String _htLowDice = "d3";
  protected final String _htHighDice = "d6";

  /** Hobbits have hairy bare feet */
  private final String _raceDescriptor = "hairy bare feet";
  
  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  //    Hide in Shadows | Listening | Climb Walls | Back Attack
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
   */
  public Hobbit() 
  {
    _raceName = "Hobbit";
    _raceLang = "Tolkeen";
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
    _racialThiefMods = _hobbitThiefMods;
    _raceSkills = _hobbitSkills;
  } 

  
  /** Hobbits are more agile but weaker: STR-1, DEX+1 */
  @Override
  public int[] adjustTraitsForRace(int[] traits)
  {
    traits[PrimeTraits.STR.ordinal()] -= 1;
    traits[PrimeTraits.DEX.ordinal()] += 1;
    return traits;    
  };
}
