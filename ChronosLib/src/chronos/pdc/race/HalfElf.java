/**
 * HalfElf.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.race;

import chronos.pdc.character.Gender;
import mylib.pdc.MetaDie;

/**
 * @author Al Cline
 * @version Sep 6, 2015 // original <br>
 */
public class HalfElf extends Race
{
  // Statics and transients that are not serialized with the Race class hierarchy
  /** Recommended serialization constant. */
  static final long serialVersionUID = 1100L;

  /** Racial limits for a Half-Elf for the traits */
  private final int[] minLimit = { 7,  7,  7,  7,  8,  7};
  private final int[] maxLimit = {18, 18, 18, 18, 18, 18};

  /** Weight ranges */
  protected final int _maleMedValue = 130;;
  protected final int _femaleMedValue = 100;
  protected final String _wtLowDice = "d20";
  protected final String _wtHighDice = "d20";

  /** Height ranges */
  protected final int _htMaleMedValue = 66;;
  protected final int _htFemaleMedValue = 62;
  protected final String _htLowDice = "d6";
  protected final String _htHighDice = "d6";

  /** Half-Elves have partially pointed ears */
  private final String _raceDescriptor = "somewhat pointed ears";

  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  //    Hide in Shadows | Listening | Climb Walls | Back Attack
  protected final int[] _halfelfThiefMods = {0, 10, 0, 0, 0, 5, 0, 0, 0};

  // Special Half-Elf skills
  private final String[] _halfelfSkills = {
      "Infravision (60')", 
      "Resistance to Sleep and Charm spells (30%) (second Save allowed on first fail)", 
      "Tingling: Detect hidden or secret doors if within 10' (33% active; 16% passive)",
  };
 
  
  /**
   * Default constructor, called reflectively by Race
   * @param gender 
   */
  public HalfElf(Gender gender) 
  {
    _raceName = "Half-Elf";
    _raceLang = getRaceLang();
    _minLimit = minLimit;
    _maxLimit = maxLimit;

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
    _racialThiefMods = _halfelfThiefMods;
    _raceSkills = _halfelfSkills;
  } 

  /** Half-elf has 50% chance of knowing elvish */
  private String getRaceLang()
  {
    MetaDie md = new MetaDie();
    String s = (md.rollPercent() < 50) ? null: "Elvish";
    return s;
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