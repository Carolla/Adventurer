/**
 * Dwarf.java Copyright (c) 2015, Carolla Development, Inc. All Rights Reserved
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
 *          June 27, 2017 // simplified and adjusted for Peasant klass <br>
 *          Sept 16, 2017 // revised to support QATool tests <br>
 */

public class Dwarf extends Race
{
  /** Weights and heights are generated in a normal distribution about an average over a Range */
  protected final int WT_LOW = 110;   // range: male [110, 190]; female [99, 179]
  protected final int WT_AVG = 150;
  protected final int HT_LOW = 49;    // range: male [49, 59]; female [44, 54]
  protected final int HT_AVG = 54; 

  private final String RACE_NAME = "Dwarf";
  private final String RACE_LANGUAGE = "Dwarvish";
  
  /** Racial limits for a male dwarf for the traits SIWCDCh: CON+1, CHR-1 */
  protected final int[] MALE_MINLIMIT = { 8,  8,  8,  9,  8,  7};
  protected final int[] MALE_MAXLIMIT = {18, 18, 19, 19, 18, 17};
  /** Female limits after adjustments from the male: STR-1, CON+1, CHR+1 */
  protected final int[] FEMALE_MINLIMIT = { 7,  8,  8, 10,  8,  8};
  protected final int[] FEMALE_MAXLIMIT = {17, 18, 18, 20, 18, 18};

  /** All Dwarves have beards, even the females */
  private final String _raceDescriptor = "a scraggly beard";

  // Find Secret Door | Pick Pockets | Open Locks | Find/Remove Traps | Move Silently |
  // Hide in Shadows | Listening | Climb Walls | Back Attack
  // protected final int[] _dwarfThiefMods = {15, 0, 10, 15, 0, 0, 0, -10, 0};

  // Special Dwarf skills
  private final String[] _dwarfSkills = {
      "Infravision (60')",
      "Detect slopes in underground passages (75%)",
      "Detect new construction in tunnel (75%)",
      "Detecting sliding or shifting walls or rooms (66%)",
      "Detect stonework traps (50%)",
      "Determine approximate underground depth (50%)"
  };


  /**
   * Default constructor, called reflectively by Klass
   */
  public Dwarf(Gender gender, String hairColor)
  {
    _raceName = RACE_NAME;
    _raceLang = RACE_LANGUAGE;
    _descriptor = _raceDescriptor;
    _raceSkills = _dwarfSkills;
    _gender = gender;
    _hairColor = hairColor;

  }


  /**
   * Dwarfs are hardier but less likable: CON+1, CHR-1
   * 
   * @param traits  original unadjusted traits; traits revised in place
   */
  @Override
  public void adjustTraitsForRace(TraitList traits)
  {
    traits.adjust(PrimeTraits.CON, 1);
    traits.adjust(PrimeTraits.CHR, -1);
  }


  @Override
  public int calcWeight()
  {
    return calcWeight(WT_LOW, WT_AVG);
  }
  

  @Override
  public int calcHeight()
  {
      return calcHeight(HT_LOW, HT_AVG);
  }


}
