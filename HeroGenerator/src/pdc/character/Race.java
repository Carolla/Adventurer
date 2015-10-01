/**
 * Race.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package pdc.character;

import java.io.Serializable;
import java.util.ArrayList;

import chronos.Chronos;
import mylib.pdc.MetaDie;

/**
 * Defines the common methods and attributes for all Races.
 * 
 * @author Alan Cline
 * @version Sept 4 2015 // rewrite to support Hero rewrite <br>
 */
public abstract class Race implements Serializable
{
  // Statics and transients that are not serialized with the Race class hierarchy
  /** Recommended serialization constant. */
  static final long serialVersionUID = 1100L;

  // RACE-SPECIFIC ATTRIBUTES and METHODS
  /** Name of the subclass of Race, e.g, Human or Hobbit */
  protected String _raceName = null;
  /** Name of the subclass race language */
  protected String _raceLang = null;

  /** Races have different advantages and disadvantages */
  public abstract int[] adjustTraitsForRace(int[] traits);

  /** Hero male and female weight ranges */
  protected int _weightMaleMedValue = 0;;
  protected int _weightFemaleMedValue = 0;
  protected String _weightLowDice = null;
  protected String _weightHighDice = null;

  /** Hero male and female height ranges */
  protected int _heightMaleMedValue = 0;;
  protected int _heightFemaleMedValue = 0;
  protected String _heightLowDice = null;
  protected String _heightHighDice = null;

  /** Racial limits for a each subclass for the traits */
  protected int[] _minLimit = null;
  protected int[] _maxLimit = null;

  /**
   * Some things apply across all Races, e.g. Body Type descriptors. The following height and weight
   * ranges are dubbed "standard" (human) because what is "short" and "tall" is a human perspective.
   */
  transient protected final double STD_MIN_HEIGHT = 54;
  transient protected final double STD_MAX_HEIGHT = 70;
  transient protected final double STD_MIN_WEIGHT = 110;
  transient protected final double STD_MAX_WEIGHT = 175;

  /** Most races have a racial language */
  protected String _racialLang;
  /** All races but human have some defining characteristic */
  /** Hobbits have hairy bare feet */
  protected String _descriptor;

  /** Thief skills are adjusted by race */
  protected int[] _racialThiefMods;

  /** Racial skills are race- specific */
  protected String[] _raceSkills;

  private static final int NOT_FOUND = -1;

  /**
   * Create a specific subclass of Race based on the Race name. <br>
   * NOTE: The subclass must be in the same package as the Race class.
   *
   * @param RaceName the name of the subclass to be created
   * @return Race, the subclass created, but referenced polymorphically; else null
   */
  static public Race createRace(String raceName)
  {
    // If the race has a hyphen in it (e.g. Half-Elf), it is removed
    int ndx = raceName.indexOf('-');
    if (ndx != NOT_FOUND) {
      String tmp = raceName.substring(0, ndx);
      tmp += raceName.substring(ndx + 1);
      raceName = tmp;
    }
    Race newRace = null;
    try {
      // Class Commands must have empty constructors (no formal input arguments)
      String racePath = Chronos.getPackageName() + raceName;
      newRace = (Race) Class.forName(racePath).newInstance();
    } catch (Exception e) {
      System.err.println("Race.createRace(): Cannot find class requested: " + e.getMessage());
    }
    return newRace;
  }


  // Add race-specific skills to the Hero's skill list */
  public ArrayList<String> addRaceSkills(ArrayList<String> existingSkills)
  {
    for (String s : _raceSkills) {
      existingSkills.add(s);
    }
    return existingSkills;
  }
  
 
  // Assign the chances for thief skills for level 1 by race
  protected String[][] adjRacialThiefSkills(String[][] skills) 
  {
    // Adjust the thief skills by the racial mods
    for (int k=0; k < skills.length; k++) {
      int oldChance = Integer.parseInt(skills[k][1]);
      int newChance = oldChance + _racialThiefMods[k];
      skills[k][1] = String.format("%s",  newChance);     
    }
    return skills;
  }
      


  /**
   * Verify that the traits do not exceed the racial limits. If they do, the trait is set to the
   * limit value.
   * 
   * @param traits traits to examine and possibly redefine
   * @param minLimit the minimum values for the race, set by the subclass constructor
   * @param maxLimit the maximum values for the race, set by the subclass constructor
   * @return the original or modified traits
   */
  public int[] verifyRaceLimits(int[] traits)
  {
    for (int k = 0; k < traits.length; k++) {
      traits[k] = (traits[k] < _minLimit[k]) ? _minLimit[k] : traits[k];
      traits[k] = (traits[k] > _maxLimit[k]) ? _maxLimit[k] : traits[k];
    }
    return traits;
  };

  /** Calculate the weight of the Hero based on deviation from average */
  public int calcWeight(String gender)
  {
    int medValue = (gender.equalsIgnoreCase("female")) ? _weightFemaleMedValue : _weightMaleMedValue;
    int weight = getDeviationFromMedValue(medValue, _weightLowDice, _weightHighDice);
    return weight;
  }

  /** Calculate the height of the Hero based on deviation from average */
  public int calcHeight(String gender)
  {
    int medValue = (gender.equalsIgnoreCase("female")) ? _heightFemaleMedValue : _heightMaleMedValue;
    int height = getDeviationFromMedValue(medValue, _heightLowDice, _heightHighDice);
    return height;
  }

  
  /**
   * Get the weight (in gpw) for subclass races in range of low, medium, high
   *
   * @param medValue the average weight for the Hero
   * @param lowDice deviation below medValue in dice notation (e.g. 2d4)
   * @param highDice deviation above medValue in dice notation (e.g. 2d4)
   * @return the weight of the character
   */
  private int getDeviationFromMedValue(int medValue, String lowDice, String highDice)
  {
    MetaDie md = new MetaDie();
    int result = 0;
    int range = md.rollPercent();
    if (range <= 30) {
      result = medValue - md.roll(lowDice);
    } else if (range >= 71) {
      result = medValue + md.roll(highDice);
    } else {
      result = medValue;
    }
    return result;
  };


  /**
   * Return the language specific to the race, or null. Half-breed races have a 50% chance of
   * knowing their race language.
   * 
   * @return the race language
   */
  public String getRacialLanguage()
  {
    return _raceLang;
  }

  public String getRaceName()
  {
    return _raceName;
  }

  public String getRaceDescriptor()
  {
    return _descriptor;
  }

  /**
   * Associate the Charisma of the character with their attractiveness, a simple string matching
   * algorithm to a description table.
   * 
   * @param charisma the Person's prime trait for making friends and negotiating
   * @return what the person looks like for their Charisma trait
   */
  public String initCharismaDescriptor(int charisma)
  {
    // Possible descriptors for charismas in increasing order. 
    // Ranges from CHR=8 to CHR=18 are normal; CHR=7 and CHR=19 are exceptional and rarely occur
    final String[] _chrDescs = {
        "crippled and horribly ugly",   // < 8
        "horribly scarred",             // 8
        "scarred from war or fire",     // 9
        "the result of years of misery",  // 10 
        "weather-beaten and tough",     // 11
        "nothing special to look at",   // 12
        "clear-eyed and rugged but not handsome", // 13
        "slightly attractive if one could scrape off the years of wear and tear", //14
        "a handsome adventurer", // 15
        "gorgeous",              // 16 
        "very attactive",        // 17
        "stunningly beautiful",  // 18
        "mesmerizing, and you will do whatever this person suggests to you", // > 18
    };

    String deschr = null;
    // Find if Person is ugly, average, or beautiful (brute force lookup)
    // Check for exception cases first, before calling generic routine
    if (charisma < Chronos.LOW_TRAIT) {
      deschr = _chrDescs[0];
    } else if (charisma > Chronos.HIGH_TRAIT) {
      deschr = _chrDescs[_chrDescs.length - 1];
    } else {
      deschr = _chrDescs[charisma - Chronos.LOW_TRAIT + 1];
    }
    return deschr;
  }

  /**
   * Associate the height and weight of the character with their Charisma to get a body type
   * descriptor. For this implementation, height and weight are broken into only three cartegories.
   * 
   * @param charisma body types are perceived as favorable or unfavorable depending on the Person's
   *        CHR
   * @return what the person looks like for their Charisma trait
   */
  public String initBodyType(int charisma, int height, int weight)
  {
    // Possible descriptors for positive charismas in a Height x Weight matrix,
    // must be in increasing order to call findRangeDescriptor
    final String[][] posBody = {
        // Light Average Heavy
        {"petite", "compact", "burly"}, // Short height
        {"lithe", "athletic", "muscular"}, // Average height
        {"thin", "tall", "towering"}, // Tall height
    };
    // Possible descriptors for negative charismas in a Height x Weight matrix,
    // must be in increasing order when calling findRangeDescriptor()
    final String[][] negBody = {{"tiny", "pudgy", "squat"}, // Short height
        {"slinky", "average-size", "heavy"}, // Average height
        {"skinny", "tall", "giant"} // Tall height
    };

    // Find which list to use
    String[][] descrChoice = ((double) charisma >= Chronos.AVERAGE_TRAIT) ? posBody : negBody;

    // Find if Person is heavy, average, or light
    // String rowNbr = findRangeDescriptor(_weight, STD_MIN_WEIGHT,
    // STD_MAX_WEIGHT, indexes);
    // Find which category the height is in
    int rowNbr = -1;
    if (height <= STD_MIN_HEIGHT) {
      rowNbr = 0;
    } else if (height >= STD_MAX_HEIGHT) {
      rowNbr = 2;
    } else {
      rowNbr = 1;
    }

    // Find which category the weight is in
    int colNbr = -1;
    if (weight <= STD_MIN_WEIGHT) {
      colNbr = 0;
    } else if (weight >= STD_MAX_WEIGHT) {
      colNbr = 2;
    } else {
      colNbr = 1;
    }
    String descr = descrChoice[rowNbr][colNbr];
    return descr;
  }


} // end of abstract Race class
