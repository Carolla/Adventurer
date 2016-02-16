package chronos.pdc.race;

/**
 * Race.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

import static chronos.pdc.character.TraitList.PrimeTraits.CON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import mylib.pdc.MetaDie;
import chronos.Chronos;
import chronos.civ.PersonKeys;
import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;

/**
 * Defines the common methods and attributes for all Races.
 * 
 * @author Alan Cline
 * @version Sept 4 2015 // rewrite to support Hero rewrite <br>
 */
public abstract class Race
{
  public static int LOW_TRAIT = 8;
  public static int HIGH_TRAIT = 18;
  
  public static final String[] RACE_LIST =
      {"Human", "Dwarf", "Elf", "Gnome", "Half-Elf", "Half-Orc", "Hobbit"};

  /**
   * Some things apply across all Races, e.g. Body Type descriptors. The following height and weight
   * ranges are dubbed "standard" (human) because what is "short" and "tall" is a human perspective.
   */
  static protected final double STD_MIN_HEIGHT = 54;
  static protected final double STD_MAX_HEIGHT = 70;
  static protected final double STD_MIN_WEIGHT = 110;
  static protected final double STD_MAX_WEIGHT = 175;
  protected String _raceName = null;
  protected String _raceLang = null;
  
  protected static MetaDie _md = new MetaDie();
  
  /** Hero male and female weight ranges */
  protected int _weightMedValue;
  /** Hero male and female height ranges */
  protected int _heightMedValue;
  
  /** Racial limits for a each subclass for the traits */
  protected int[] _minLimit = null;
  protected int[] _maxLimit = null;
  
  protected String _racialLang;
  protected String _descriptor;
  protected int[] _racialThiefMods;
  protected String[] _raceSkills;
  protected int _racialPoisonResist = 0;

  public abstract int calcWeight();
  public abstract int calcHeight();

  /**
   * Create a specific subclass of Race based on the Race name. <br>
   * NOTE: The subclass must be in the same package as the Race class.
   *
   * @param RaceName the name of the subclass to be created
   */
  static public Race createRace(String raceName, Gender gender)
  {
    Race race = null;
    if (raceName.equals("Dwarf")) {
      race =  new Dwarf(gender);
    } else if (raceName.equals("Elf")) {
      race =  new Elf(gender);
    } else if (raceName.equals("Gnome")) {
      race =  new Gnome(gender);
    } else if (raceName.equals("Half-Elf")) {
      race =  new HalfElf(gender);
    } else if (raceName.equals("Half-Orc")) {
      race =  new HalfOrc(gender);
    } else if (raceName.equals("Hobbit")) {
      race =  new Hobbit(gender);
    } else {
      race =  new Human(gender);      
    }
    return race;
  }
  
  /** Races have different advantages and disadvantages */
  public TraitList adjustTraitsForRace(TraitList traits)
  {
    addRacialPoisonResist(traits.getTrait(CON), traits.getMagicAttackMod());
    return traits;
  }



  private void addRacialPoisonResist(int constitution, int magicAttackMod)
  {
    if ((_raceName.equalsIgnoreCase("Dwarf")) ||
        (_raceName.equalsIgnoreCase("Gnome")) ||
        (_raceName.equalsIgnoreCase("Hobbit"))) {
      _racialPoisonResist = (int) Math.round((float) constitution / 3.5);
      magicAttackMod += _racialPoisonResist;
    }
  }
  
  // Assign the chances for thief skills for level 1 by race
  public String[][] adjustRacialThiefSkills(String[][] skills) 
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
   * Return the language specific to the race, or null. Half-breed races have a 50% chance of
   * knowing their race language.
   * 
   * @return the race language
   */
  public String getRacialLanguage()
  {
    return _raceLang;
  }

  public String getName()
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
    if (charisma < LOW_TRAIT) {
      deschr = _chrDescs[0];
    } else if (charisma > HIGH_TRAIT) {
      deschr = _chrDescs[_chrDescs.length - 1];
    } else {
      deschr = _chrDescs[charisma - LOW_TRAIT + 1];
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
    final String[][] negBody = {
        {"tiny", "pudgy", "squat"}, // Short height
        {"slinky", "average-size", "heavy"}, // Average height
        {"skinny", "tall", "giant"} // Tall height
    };

    // Find which list to use
    String[][] descrChoice = ((double) charisma >= Chronos.AVERAGE_TRAIT) ? posBody : negBody;
    int rowNbr = 1;
    if (height <= STD_MIN_HEIGHT) {
      rowNbr = 0;
    } else if (height >= STD_MAX_HEIGHT) {
      rowNbr = 2;
    } else {
      rowNbr = 1;
    }

    // Find which category the weight is in
    int colNbr = 1;
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


  /**
   * Verify that the traits do not exceed the racial limits. If they do, the trait is set to the
   * limit value.
   * 
   * @param _traits traits to examine and possibly redefine
   * @param minLimit the minimum values for the race, set by the subclass constructor
   * @param maxLimit the maximum values for the race, set by the subclass constructor
   * @return the original or modified traits
   */
  public TraitList verifyRaceLimits(TraitList _traits)
  {
//    for (int k = 0; k < _traits.length; k++) {
//      _traits[k] = (_traits[k] < _minLimit[k]) ? _minLimit[k] : _traits[k];
//      _traits[k] = (_traits[k] > _maxLimit[k]) ? _maxLimit[k] : _traits[k];
//    }
    return _traits;
  }

  
  public List<String> getSkills()
  {
    return new ArrayList<String>(Arrays.asList(_raceSkills));
  }
  
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_raceName == null) ? 0 : _raceName.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Race other = (Race) obj;
    if (_raceName == null) {
      if (other._raceName != null)
        return false;
    } else if (!_raceName.equals(other._raceName))
      return false;
    return true;
  }
  
  public void loadRaceKeys(EnumMap<PersonKeys, String> map)
  {
    map.put(PersonKeys.RACENAME, _raceName);
    map.put(PersonKeys.RMR, "" + _racialPoisonResist); 
  }


} // end of abstract Race class
