/**
 * Human.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.race;

import chronos.pdc.character.Gender;



/**
 * @author Al Cline
 * @version Sep 4, 2015 // original <br>
 */
public class Human extends Race
{
  // Statics and transients that are not serialized with the Race class hierarchy
  /** Recommended serialization constant. */
  static final long serialVersionUID = 1100L;

  /** Racial limits for a Human for the traits */
  private final int[] minLimit = { 8,  8,  8,  8,  8,  8};
  private final int[] maxLimit = {18, 18, 18, 18, 18, 18};

  /** Weight ranges */
  protected final int _maleMedValue = 175;
  protected final int _femaleMedValue = 130;
  protected final String _wtLowDice = "3d12";
  protected final String _wtHighDice = "5d12";
  
  /** Height ranges */
  protected final int _htMaleMedValue = 68;
  protected final int _htFemaleMedValue = 64;
  protected final String _htLowDice = "d12";
  protected final String _htHighDice = "d12";

  /** Human has no Race descriptor, so merely ends the description suffix */
  private final String _raceDescriptor = "a naive look in the eyes";

  /** No special human skills, but don't leave this null */
  private final String[] _humanSkills = { "None" }; 
  
  /** Humans get no special mods for thief skills */
  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  //    Hide in Shadows | Listening | Climb Walls | Back Attack
  protected final int[] _humanThiefMods = {0, 0, 0, 0, 0, 0, 0, 0, 0 };

  /**
   * Default constructor, called reflectively by Klass
   * @param gender 
   */
  public Human(Gender gender) 
  {
    _raceName = "Human";
    _minLimit = minLimit;
    _maxLimit = maxLimit;
    _raceLang = null;     // There is no special language for Humans
    _weightLowDice = _wtLowDice;
    _weightHighDice = _wtHighDice;
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
    _heightLowDice = _htLowDice;
    _heightHighDice = _htHighDice;
    _descriptor = _raceDescriptor;
    _racialThiefMods = _humanThiefMods;
    _raceSkills = _humanSkills;
  } 
}
