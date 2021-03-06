/**
 * HalfOrc.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.race;

import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;

/**
 * @author Al Cline
 * @version Sep 6, 2015 // original <br>
 */
public class HalfOrc extends Race
{
  private final String RACE_NAME = "Half-Orc";

  /** Weights and heights are generated in a normal distribution about an average over a Range */
  protected final int WT_LOW = 140; // range: male [140, 260]; female [126, 234]
  protected final int WT_HIGH = 260;
  protected final int HT_LOW = 60; // range: male [60, 80]; female [54, 72]
  protected final int HT_HIGH = 80;

  /** Racial limits for a male for the traits SIWCDCh: STR+1, CON+1, CHR-2 */
  protected final int[] MALE_MINLIMIT = {9, 8, 8, 9, 8, 6};
  protected final int[] MALE_MAXLIMIT = {19, 18, 18, 19, 18, 16};
  /** Female limits after adjustments from the male: STR-1, CON+1, CHR+1 */
  protected final int[] FEMALE_MINLIMIT = {8, 8, 8, 10, 8, 7};
  protected final int[] FEMALE_MAXLIMIT = {18, 18, 18, 20, 18, 17};

  /** Half-orcs are burly and pig-like */
  private final String _raceDescriptor = "a squat snoutish face";

  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  // Hide in Shadows | Listening | Climb Walls | Back Attack
  // protected final int[] _halforcThiefMods = {5, -5, 5, 5, 0, 0, 5, -5, 0};

  // Special Half-Orc skills
  private final String[] _halforcSkills = {"Infravision (60')"};


  /**
   * Default constructor, called reflectively by Race
   * 
   * @param gender
   */
  public HalfOrc(Gender gender, String hairColor)
  {
    _raceName = RACE_NAME;
    _raceLang = getRaceLang();
    _descriptor = _raceDescriptor;
    _raceSkills = _halforcSkills;
    _gender = gender;
    _hairColor = hairColor;
  }


  /** Half-Orcs are sturdier, stronger, and uglier: STR+1, CON+1, CHR-2 */
  @Override
  public void adjustTraitsForRace(TraitList traits)
  {
    traits.adjust(PrimeTraits.STR, 1);
    traits.adjust(PrimeTraits.CON, 1);
    traits.adjust(PrimeTraits.CHR, -2);
  };


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


  /**
   * Half-orc has 50% chance of knowing orcish
   * 
   * @return Orcish as a language or empty string
   */
  public String getRaceLang()
  {
    // Randomizer is not good with large numbers, so even and odd tests are better for this
    return _md.isOdd() ? "" : "Orcish";
  }

  

} // end of HalfOrc subclass
