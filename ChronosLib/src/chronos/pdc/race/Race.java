/**
 * Race.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.race;

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
import mylib.pdc.Utilities;

/**
 * Defines the common methods and attributes for all Races.
 * 
 * @author Alan Cline
 * @version Sept 4 2015 // rewrite to support Hero rewrite <br>
 *          June 2 2017 // Refactored for better organization <br>
 *          June 17 2017 // Modified trait limits <br>
 */
public abstract class Race
{
  public static final String[] RACE_LIST =
      {"Human", "Dwarf", "Elf", "Gnome", "Half-Elf", "Half-Orc", "Hobbit"};

  public static final String HUMAN_RACENAME = "Human";
  public static final String DWARF_RACENAME = "Dwarf";
  public static final String ELF_RACENAME = "Elf";
  public static final String GNOME_RACENAME = "Gnome";
  public static final String HALFELF_RACENAME = "Half-Elf";
  public static final String HALFORC_RACENAME = "Half-Orc";
  public static final String HOBBIT_RACENAME = "Hobbit";

  static protected Gender _gender;
  static protected String _hairColor;

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

  /** Modify prime traits for racial specialties */
  public abstract TraitList adjustTraitsForRace(TraitList traits);

  /** Calculate the gender- and racial specific weight of the Hero */
  public abstract int calcWeight();

  /** Calculate the gender- and racial specific height of the Hero */
  public abstract int calcHeight();

  /** Ensure that all traits are within the proper range after all adjustments are made */
  public abstract TraitList setTraitLimits(TraitList traits);


  // ====================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ====================================================

  /** For subclass constructors */
  protected Race()
  {
    _languages = new ArrayList<String>();
  }


  /**
   * Genders have slightly different traits. Once the specific Race is created, then specific
   * attributes are refined or created: trait adjustment for race and gender, weight and height, and
   * a physical description.
   * 
   * @param traits the six prime unadjusted traits for all Heroes
   */
  public TraitList buildRace()
  {
    // Set the race-specific traits
    TraitList traits = setRaceTraits();

    _languages.add(DEFAULT_LANG);
    _languages.add(_raceLang);
    _weight = calcWeight();
    _height = calcHeight();
    _hunger = "Full";
    _description = new Description(traits.getTrait(PrimeTraits.CHR), _descriptor, _hairColor,
        _gender, _height, _weight);
    return traits;
  }

  /**
   * Create a specific subclass of Race based on the Race name. <br>
   * NOTE: (1) The method must be static because Race works as dispatcher, and itself cannot be
   * instantiated. (2) The subclass must be in the same package as the Race class.
   *
   * @param RaceName the name of the subclass to be created
   * @param gender male or female of all Races
   * @param hairColor of any Race
   */
  static public Race createRace(String raceName, Gender gender, String hairColor)
  {
    _gender = gender;
    _hairColor = hairColor;
    Race race = null;
    switch (raceName)
    {
      case HUMAN_RACENAME:
        race = new Human();
        break;
      case DWARF_RACENAME:
        race = new Dwarf();
        break;
      case ELF_RACENAME:
        race = new Elf();
        break;
      case GNOME_RACENAME:
        race = new Gnome();
        break;
      case HALFELF_RACENAME:
        race = new HalfElf();
        break;
      case HALFORC_RACENAME:
        race = new HalfOrc();
        break;
      case HOBBIT_RACENAME:
        race = new Hobbit();
        break;
      default:
        throw new NullPointerException(
            "Race.createRace(): Cannot find race requested " + raceName);
    }
    return race;
  }


  // ====================================================
  // PROTECTED METHODS
  // ====================================================

  /**
   * Calculate the height of the Hero based on deviation from average
   * 
   * @param low lowest value for a male; female adjustment made
   * @param variance dice format for normal-curve distribution across average value
   * @return the randomly-determined height over the average with a normal distribution
   */
  protected int calcHeight(int low, String variance)
  {
    int newLow  = (int) (_gender.isMale() ? low : low * FEMALE_ADJ);
    int height = newLow + _md.roll(variance) - 1; // adjust for 1 when 0 is rolled
    return height;
  }


  /**
   * Calculate the weight of the Hero based on deviation from average
   * 
   * @param low lowest value for a male; female adjustment made
   * @param variance dice format for normal-curve distribution across average value
   * @return the randomly-determined weight over the average with a normal distribution
   */
  protected int calcWeight(int low, String variance)
  {
    int newLow = (int) (_gender.isMale() ? low : low * FEMALE_ADJ);
    int weight = newLow + _md.roll(variance) - 1;
    return weight;
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
  protected TraitList constrainTo(TraitList traits, int[] lowerLimits, int[] upperLimits)
  {
    int[] values = traits.toArray();
    values = Utilities.constrain(values, lowerLimits, upperLimits);
    return new TraitList(values);
  }



  // ====================================================
  // PUBLIC METHODS
  // ====================================================

  public String getGender()
  {
    return _gender.toString();
  }


  public int getHeight()
  {
    return _height;
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
   * Return the language specific to the race, or null. Hybrid races have a 50% chance of knowing
   * their race language.
   * 
   * @return the race language
   */
  public ArrayList<String> getLanguages()
  {
    return _languages;
  }


  public List<String> getSkills()
  {
    return new ArrayList<String>(Arrays.asList(_raceSkills));
  }

  public int getWeight()
  {
    return _weight;
  }


  // ====================================================
  // PRIVATE METHODS
  // ====================================================

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
    traits = adjustTraitsForRace(traits);
    traits = _gender.adjustTraitsForGender(traits);
    traits = setTraitLimits(traits); // set to race-specific trait ranges
    return traits;
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


  // private void addRacialPoisonResist(int constitution, int magicAttackMod)
  // {
  // if ((_raceName.equalsIgnoreCase("Dwarf")) ||
  // (_raceName.equalsIgnoreCase("Gnome")) ||
  // (_raceName.equalsIgnoreCase("Hobbit"))) {
  // _racialPoisonResist = (int) Math.round(constitution / 3.5);
  // magicAttackMod += _racialPoisonResist;
  // }
  // }


} // end of abstract Race class
