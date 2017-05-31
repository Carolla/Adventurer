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

/**
 * @author Al Cline
 * @version Sep 6, 2015 // original <br>
 */
public class HalfElf extends Race
{
  /** Weight ranges */
  protected final RangedValue _weightRange = new RangedValue(100, "d20", "d20");

  /** Height ranges */
  protected final RangedValue _heightRange = new RangedValue(62, "d6");
  
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

    _descriptor = _raceDescriptor;
    _racialThiefMods = _halfelfThiefMods;
    _raceSkills = _halfelfSkills;
  }

  /** Half-elf has 50% chance of knowing elvish */
  private String getRaceLang()
  {
    String s = (_md.rollPercent() < 50) ? "Common" : "Elvish";
    return s;
  }

  @Override
  public int calcWeight(Gender g)
  {
    return _weightRange.calcValue();
  }

  @Override
  public int calcHeight(Gender g)
  {
    return _heightRange.calcValue();
  };
}
