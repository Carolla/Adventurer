/**
 * HalfOrc.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc.character;

import java.util.ArrayList;

import mylib.pdc.MetaDie;
import pdc.character.Hero.PrimeTraits;

/**
 * @author Al Cline
 * @version Sep 6, 2015 // original <br>
 */
public class HalfOrc extends Race
{
  // Statics and transients that are not serialized with the Race class hierarchy
  /** Recommended serialization constant. */
  static final long serialVersionUID = 1100L;

  /** Racial limits for a Half-Orc for the traits */
  private final int[] minLimit = { 9,  7,  7,  7, 13,  7};
  private final int[] maxLimit = {19, 17, 14, 17, 19, 12};

  /** Weight ranges */
  protected final int _maleMedValue = 180;;
  protected final int _femaleMedValue = 150;
  protected final String _wtLowDice = "3d8";
  protected final String _wtHighDice = "4d10";

  /** Height ranges */
  protected final int _htMaleMedValue = 70;;
  protected final int _htFemaleMedValue = 65;
  protected final String _htLowDice = "2d4";
  protected final String _htHighDice = "2d4";

  /** Half-orcs are burly and pig-like */
  private final String _raceDescriptor = "a squat snoutish face";

  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  //    Hide in Shadows | Listening | Climb Walls | Back Attack
  protected final int[] _halforcThiefMods = {5, -5, 5, 5, 0, 0, 5, -5, 0};
      
  // Special Half-Orc skills
  private final String[] _halforcSkills = { "Infravision (60')" }; 


  /**
   * Default constructor, called reflectively by Race
   */
  public HalfOrc() 
  {
    _raceName = "Half-Orc";
    _raceLang = getRaceLang();
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
    _racialThiefMods = _halforcThiefMods;
    _raceSkills = _halforcSkills;

  } 

  /** Half-orc has 50% chance of knowing elvish */
  private String getRaceLang()
  {
    MetaDie md = new MetaDie();
    String s = (md.rollPercent() < 50) ? null: "Orcish";
    return s;
  }
  
  /** Half-Orcs are sturdier, stronger, and uglier: STR+1, CON+1, CHR-2 */
  @Override
  public int[] adjustTraitsForRace(int[] traits)
  {
    traits[PrimeTraits.STR.ordinal()] += 1;
    traits[PrimeTraits.CON.ordinal()] += 1;
    traits[PrimeTraits.CHR.ordinal()] -= 2;
    return traits;    
  };



}