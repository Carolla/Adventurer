/**
 * Gnome.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc.character;

import chronos.pdc.Race;


/**
 * @author Al Cline
 * @version Sep 6, 2015 // original <br>
 */
public class Gnome extends Race
{
  // Statics and transients that are not serialized with the Race class hierarchy
  /** Recommended serialization constant. */
  static final long serialVersionUID = 1100L;

  private String _racename = null;

  /** Racial limits for a Gnome for the traits */
  private final int[] minLimit = { 7,  7,  7,  7,  8,  7};
  private final int[] maxLimit = {18, 18, 18, 18, 18, 18};

  /** Weight ranges */
  protected final int _maleMedValue = 80;;
  protected final int _femaleMedValue = 75;
  protected final String _wtLowDice = "2d4";
  protected final String _wtHighDice = "2d6";

  /** Height ranges */
  protected final int _htMaleMedValue = 42;;
  protected final int _htFemaleMedValue = 39;
  protected final String _htLowDice = "d3";
  protected final String _htHighDice = "d3";
  
  /** Gnomes have piercing blue eyes */
  private final String _raceDescriptor = "piercing blue eyes";

  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  //    Hide in Shadows | Listening | Climb Walls | Back Attack
  protected final int[] _gnomeThiefMods = {10, 0, 5, 10, 5, 5, 10,-15, 5};

  // Special Gnome skills
  private final String[] _gnomeSkills = {
      "Infravision (60')", 
      "Detect slopes in underground passages (80%)", 
      "Detect unsafe walls, ceilings, floors (70%)", 
      "Detect direction of underground travel (50%)",
      "Determine approximate underground depth (60%)" 
  };

  
  /**
   * Default constructor, called reflectively by Klass
   */
  public Gnome() 
  {
    _racename = "Gnome";
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
    _racialThiefMods = _gnomeThiefMods;
    _raceSkills = _gnomeSkills;
  } 

  
  /** Every race knows its name */
  @Override
  public String getRaceName()
  {
    return _racename;
  };

  /** Gnomes have no modifications */
  @Override
  public int[] adjustTraitsForRace(int[] traits)
  {
    return traits;
  };


}