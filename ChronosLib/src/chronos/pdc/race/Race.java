/**
 * Race.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.race;

import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import chronos.civ.PersonKeys;
import chronos.pdc.character.Description;
import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;
import chronos.pdc.character.TraitList.PrimeTraits;
import mylib.MsgCtrl;
import mylib.pdc.MetaDie;

/**
 * Defines the common methods and attributes for all Races.
 * 
 * @author Alan Cline
 * @version Sept 4 2015 // rewrite to support Hero rewrite <br>
 *          June 2 2017 // Refactored for better organization <br>
 *          June 17 2017 // Modified trait limits <br>
 *          Aug 10, 2017 // updated per QATool <br>
 *          Aug 15, 2017 // expand {@code equals()} to include gender <br>
 */
public abstract class Race implements Serializable
{
  // Required for serialization
  static final long serialVersionUID = 20170804450L;

  /** Names of Race subclasses, hyphen for Half- races removed */
  public static final String[] RACE_LIST =
      {"Human", "Dwarf", "Elf", "Gnome", "Half-Elf", "Half-Orc", "Hobbit"};

  public static final String HUMAN_RACENAME = "Human";
  public static final String DWARF_RACENAME = "Dwarf";
  public static final String ELF_RACENAME = "Elf";
  public static final String GNOME_RACENAME = "Gnome";
  public static final String HALFELF_RACENAME = "Half-Elf";
  public static final String HALFORC_RACENAME = "Half-Orc";
  public static final String HOBBIT_RACENAME = "Hobbit";

  protected Gender _gender;
  protected String _hairColor;

  protected String _raceName;
  protected String _raceLang;
  protected ArrayList<String> _languages;
  protected int _height;
  protected int _weight;
  protected Description _description;
  protected String _hunger;

  protected static MetaDie _md = new MetaDie();

  protected String _descriptor;
  protected int[] _racialThiefMods;
  protected String[] _raceSkills;
  protected int _racialPoisonResist = 0;

  /** Weight and height are 10% less for females */
  private final double FEMALE_ADJ = 0.90;
  /** All races know the 'Common' language */
  private final String DEFAULT_LANG = "Common";


  // ====================================================
  // ABSTRACT METHODS FOR SUBCLASSES
  // ====================================================

  /** Modify prime traits in-place for racial specialties */
  public abstract void adjustTraitsForRace(TraitList traits);

  /** Calculate the gender- and racial specific weight of the Hero */
  public abstract int calcWeight();

  /** Calculate the gender- and racial specific height of the Hero */
  public abstract int calcHeight();

  // TODO Shouldn't need this method; use a test to ensure the range; don't clip range
  /** Ensure that all traits are within the proper range after all adjustments are made */
  // public abstract TraitList setTraitLimits(TraitList traits);


  // ====================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ====================================================

  /** For subclass constructors */
  protected Race()
  {
    _languages = new ArrayList<String>();
  }


  // ====================================================
  // PUBLIC METHODS
  // ====================================================

  /**
   * Genders have slightly different traits. Once the specific Race is created, then specific
   * attributes are refined or created: trait adjustment for race and gender, weight and height, and
   * a physical description.
   */
  public TraitList buildRace(String gender, String hairColor)
  {
    // Set the race-specific traits
    TraitList traits = setRaceTraits();
    _gender = new Gender(gender);
    _hairColor = hairColor;

    addLanguages();
    _weight = calcWeight();
    _height = calcHeight();
    _hunger = "Full";
    _description = new Description(traits.getTrait(PrimeTraits.CHR), _descriptor, _hairColor,
        _gender, _height, _weight);
    return traits;
  }

  
  /**
   * Calculate the height of the Hero based on deviation from average
   * 
   * @param lowBound   lowest value for a male; female adjustment made
   * @param average the average height for the normal population
   * @return the randomly-determined height over the average with a normal distribution
   */
  protected int calcHeight(double lowBound, double average)
  {
    double low = lowBound;
    double avg = average;
    // Adjust for gender
    if (_gender.isFemale()) {
      low = low * FEMALE_ADJ;
      avg = average * FEMALE_ADJ;
    }
    int height = Math.round(_md.rollVariance(low, avg));
    return height;
  }


  /**
   * Calculate the weight of the Hero based on deviation from average
   * 
   * @param lowBound   lowest value for a male; female adjustment made
   * @param average the average weight for the normal population
   * @return the randomly-determined weight over the average with a normal distribution
   */
  protected int calcWeight(double lowBound, double average)
  {
    double low = lowBound;
    double avg = average;
    // Adjust for gender
    if (_gender.isFemale()) {
      low = low * FEMALE_ADJ;
      avg = average * FEMALE_ADJ;
    }
    int height = (int) _md.rollVariance(low, avg);
    return height;
  }

  
  /**
   * Constrain all traits to fall within a given range for the racial subclass. Any value outside
   * those limits are set to the range boundary. NOTE: TraitList is created anew so return value
   * must be assigned to the _traits field
   * 
   * @param traits the original set to check
   * @param lowerLimits the lower bound per trait
   * @param upperLimits the upper bound per trait
   */
//  protected TraitList constrainTo(TraitList traits, int[] lowerLimits, int[] upperLimits)
//  {
//    int[] values = traits.toArray();
//    values = Utilities.constrain(values, lowerLimits, upperLimits);
//    return new TraitList(values);
//  }



  // ====================================================
  // PUBLIC METHODS
  // ====================================================

  /**
   * Create a specific subclass of Race based on the Race name. <br>
   * NOTE: (1) The method must be static because Race works as dispatcher, and itself cannot be
   * instantiated. (2) The subclass must be in the same package as the Race class. Maps String
   * racename to class racename; beware the hyphenated names, like Half-Elf
   *
   * @param raceName the name of the subclass to be created
   * @param gender male or female of all Races
   * @param hairColor of any Race
   */
  static public Race createRace(String raceName, Gender gender, String hairColor)
  {
    Race race = null;
    switch (raceName)
    {
      case HUMAN_RACENAME:
        race = new Human(gender, hairColor);
        break;
      case DWARF_RACENAME:
        race = new Dwarf(gender, hairColor);
        break;
      case ELF_RACENAME:
        race = new Elf(gender, hairColor);
        break;
      case GNOME_RACENAME:
        race = new Gnome(gender, hairColor);
        break;
      case HALFELF_RACENAME:
        race = new HalfElf(gender, hairColor);
        break;
      case HALFORC_RACENAME:
        race = new HalfOrc(gender, hairColor);
        break;
      case HOBBIT_RACENAME:
        race = new Hobbit(gender, hairColor);
        break;
      default:
        throw new NullPointerException(
            "Race.createRace(): Cannot find race requested " + raceName);
    }
    return race;
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

    if (_gender == null) {
      if (other._gender != null)
        return false;
    } else if (!_gender.equals(other._gender))
      return false;

    return true;
  }


  public String getGender()
  {
    return _gender.toString();
  }


  public int getHeight()
  {
    return _height;
  }


  /**
   * Return the language specific to the race, or null. Hybrid races have a 50% chance of knowing
   * their race language.
   * 
   * @return the race language
   */
  public ArrayList<String> getLanguages()
  {
    return _languages;
  }

  public String getName()
  {
    return _raceName;
  }


  public String getRaceDescriptor()
  {
    return _descriptor;
  }


  public List<String> getSkills()
  {
    return new ArrayList<String>(Arrays.asList(_raceSkills));
  }


  public int getWeight()
  {
    return _weight;
  }


  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_raceName == null) ? 0 : _raceName.hashCode());
    result = prime * result + ((_gender == null) ? 0 : _gender.hashCode());
    return result;
  }


  public void loadRaceKeys(Map<PersonKeys, String> map)
  {
    map.put(PersonKeys.GENDER, _gender.toString());
    map.put(PersonKeys.DESCRIPTION, _description.toString());
    map.put(PersonKeys.HAIR_COLOR, _hairColor);
    map.put(PersonKeys.HEIGHT, Integer.toString(_height));
    map.put(PersonKeys.HUNGER, _hunger);
    map.put(PersonKeys.RACENAME, _raceName);
    map.put(PersonKeys.RMR, "" + _racialPoisonResist);
    map.put(PersonKeys.WEIGHT, Integer.toString(_weight));
  }


  // ====================================================
  // PRIVATE METHODS
  // ====================================================

  /**
   * All Races have Common language, and their race language. Humans have no reace language
   */
  private void addLanguages()
  {
    _languages.add(DEFAULT_LANG);
    if (_raceLang != null) {
      _languages.add(_raceLang);
    }
  }


  /**
   * Create the traits for a particular race. Starts with the default (human) trait list, then
   * modifies then for specific race and gender, then verifies that they fall with the proper
   * race-specific trait limits.
   * 
   * @return set of final traits for the Hero: STR, INT, WIS, CON, DEX, CHR
   */
  private TraitList setRaceTraits()
  {
    TraitList traits = new TraitList(); // default range of [8,18]
    adjustTraitsForRace(traits);
    _gender.adjustTraitsForGender(traits);
    return traits;
  }


  // ====================================================
  // MockRace INNER CLASS
  // ====================================================

  public class MockRace
  {
    public MockRace()
    {};

    public int calcHeight(double low, double average)
    {
      return Race.this.calcHeight(low, average);
    }

    public int calcWeight(double low, double average)
    {
      return Race.this.calcWeight(low, average);
    }

    /**
     * Count the number of values with 1/3 of the distribution
     * 
     * @param race generic base class of object that contains the method to call
     * @param bin top range of low, mid, high bins
     * @param count number of times to execute the method and find the average
     * @return average of all values calculated by the object's method
     */
    public double calcHeightWithBins(Race race, int[] bin, int count)
    {
      int sum = 0;
      int lowCnt = 0;
      int midCnt = 0;
      int hiCnt = 0;
      int cntSum = 0;
      // Range runs from 1 to 10
      for (int k = 0; k < count; k++) {
        int dp = race.calcHeight();
        lowCnt += ((dp >= bin[0]) && (dp < bin[1])) ? 1 : 0;
        midCnt += ((dp >= bin[1]) && (dp < bin[2])) ? 1 : 0;
        hiCnt += ((dp >= bin[2]) && (dp <= bin[3])) ? 1 : 0;
        assertTrue((dp >= bin[0]) && (dp <= bin[3]));
        sum += dp;
        cntSum = lowCnt + midCnt + hiCnt;
      }
      // Tally bins
      MsgCtrl.msgln("\t " + lowCnt + " low count values");
      MsgCtrl.msgln("\t " + midCnt + " mid count values");
      MsgCtrl.msgln("\t " + hiCnt + " high count values");
      MsgCtrl.msgln("\t " + cntSum + " numbers accounted for");
      // Average should be midway between all values
      double avg = sum / count;
      return avg;
    }


    /**
     * Count the number of values within 1/3 of the distribution
     * 
     * @param race generic base class of object that contains the method to call
     * @param bin top range of low, mid, high bins
     * @param count number of times to execute the method and find the average
     * @return average of all values calculated by the object's method
     */
    public double calcWeightWithBins(Race race, int[] bin, int count)
    {
      int sum = 0;
      int lowCnt = 0;
      int midCnt = 0;
      int hiCnt = 0;
      int cntSum = 0;
      // Range runs from 1 to 10
      for (int k = 0; k < count; k++) {
        int dp = race.calcWeight();
        lowCnt += ((dp >= bin[0]) && (dp < bin[1])) ? 1 : 0;
        midCnt += ((dp >= bin[1]) && (dp < bin[2])) ? 1 : 0;
        hiCnt += ((dp >= bin[2]) && (dp <= bin[3])) ? 1 : 0;
        assertTrue((dp >= bin[0]) && (dp <= bin[3]));
        sum += dp;
        cntSum = lowCnt + midCnt + hiCnt;
      }
      // Tally bins
      MsgCtrl.msgln("\t " + lowCnt + " low count values");
      MsgCtrl.msgln("\t " + midCnt + " mid count values");
      MsgCtrl.msgln("\t " + hiCnt + " high count values");
      MsgCtrl.msgln("\t " + cntSum + " numbers accounted for");
      // Average should be midway between all values
      double avg = sum / count;
      return avg;
    }


//    public TraitList constrainTo(TraitList tr, int[] low, int[] hi)
//    {
//      return Race.this.constrainTo(tr, low, hi);
//    }

    public String getHairColor()
    {
      return _hairColor;
    }

    public String getRaceLang()
    {
      return _raceLang;
    }

    public String getRaceName()
    {
      return _raceName;
    }

    /**
     * @param gender give proper gender string to create gender object in race
     */
    public void setGender(String gender)
    {
      _gender = new Gender(gender);
    }


  } // end of MockRace inner class



} // end of abstract Race class
