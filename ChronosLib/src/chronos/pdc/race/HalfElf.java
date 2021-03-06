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
import chronos.pdc.character.TraitList;

/**
 * @author Al Cline
 * @version Sep 6, 2015 // original <br>
 */
public class HalfElf extends Race
{
  private final String RACE_NAME = "Half-Elf";

  /** Weights and heights are generated in a normal distribution about an average over a Range */
  protected final int WT_LOW = 90;   // range: male [90, 170]; female [81, 161]
  protected final int WT_HIGH = 170;
  protected final int HT_LOW = 60;    // range: male [60, 76]; female [54, 70]
  protected final int HT_HIGH = 76; 

  /** Racial limits for a male for the traits SIWCDCh: STR-1, DEX+1 */
  protected final int[] MALE_MINLIMIT = { 7,  8,  8,  8,  9,  8};
  protected final int[] MALE_MAXLIMIT = {17, 18, 18, 18, 19, 18};
  /** Female limits after adjustments from the male: STR-1, CON+1, CHR+1 */
  protected final int[] FEMALE_MINLIMIT = { 6,  8,  8,  9,  9,  9};
  protected final int[] FEMALE_MAXLIMIT = {16, 18, 18, 19, 19, 19};

  /** Half-Elves have partially pointed ears */
  private final String _raceDescriptor = "somewhat pointed ears";

  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  //    Hide in Shadows | Listening | Climb Walls | Back Attack
//  protected final int[] _halfelfThiefMods = {0, 10, 0, 0, 0, 5, 0, 0, 0};

  // Special Half-Elf skills
  private final String[] _halfelfSkills = {
      "Infravision (60')",
      "Resistance to Sleep and Charm spells (30%) (second Save allowed on first fail)",
      "Tingling: Detect hidden or secret doors if within 10' (33% active; 16% passive)",
  };


  /**
   * Default constructor, called by Race base class
   * 
   * @param gender
   */
  public HalfElf(Gender gender, String hairColor)
  {
    _raceName = RACE_NAME;
    _raceLang = getRaceLang();
    _descriptor = _raceDescriptor;
    _raceSkills = _halfelfSkills;
    _gender = gender;
    _hairColor = hairColor;
  }

  
  /** Half-Elves have no racial adjustments */
  @Override
  public void adjustTraitsForRace(TraitList traits)
  {
  }


  @Override
  public int calcHeight()
  {
    return calcVariance(HT_LOW, HT_HIGH);
  }


  @Override
  public int calcWeight()
  {
    return calcVariance(WT_LOW, WT_HIGH);
  }

  /** Half-elf has 50% chance of knowing elvish */
  public String getRaceLang()
  {
    return (_md.isOdd() ? "" : "Elvish");
  }


  
}   // end of HalfElf class
