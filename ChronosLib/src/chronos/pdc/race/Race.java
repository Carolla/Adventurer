/**
 * Race.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists,
 * requires prior specific permission and/or a fee. Request permission to use from Carolla
 * Development, Inc. by email: acline@carolla.com
 */


package chronos.pdc.race;

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

  /** Names of Race subclasses */
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
  protected final double FEMALE_ADJ = 0.90;

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


  // ====================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ====================================================

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

  /** For subclass constructors */
  protected Race()
  {
    _languages = new ArrayList<String>();
  }


  // ====================================================
  // PUBLIC METHODS
  // ====================================================

  /**
   * select from a Guassian population to set certain traits, e.g., weight and height. Females
   * are adjusted so that they are FEMALE_ADJ shorter or lighter.
   * 
   * @param lowBound lowest value for a male
   * @param hiBound highest value for a male
   * @return the randomly-determined value that varies about the average of a normal
   *         distribution
   */
  protected int calcVariance(int lowBound, int hiBound)
  {
    int low = lowBound;
    int hi = hiBound;
    // Adjust for gender
    if (_gender.isFemale()) {
      low = (int) Math.round(low * FEMALE_ADJ);
      hi = (int) Math.round(hi * FEMALE_ADJ);
    }
    int value = _md.rollVariance(low, hi);
    return value;
  }


  // ====================================================
  // PUBLIC METHODS
  // ====================================================

  // ====================================================
  // PUBLIC METHODS
  // ====================================================
  
  /**
   * Genders have slightly different traits. Once the specific Race is created, then specific
   * attributes are refined or created: trait adjustment for race and gender, weight and
   * height, and a physical description.
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
   * Return the language specific to the race, or null. Hybrid races have a 50% chance of
   * knowing their race language.
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

    public int calcVariance(int low, int high)
    {
      return Race.this.calcVariance(low, high);
    }

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
