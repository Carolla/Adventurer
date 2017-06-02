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

/**
 * Defines the common methods and attributes for all Races.
 * 
 * @author Alan Cline
 * @version Sept 4 2015 // rewrite to support Hero rewrite <br>
 *          June 2 2017 // Refactored for better organization <br>
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
  protected int _height;
  protected int _weight;
  protected Description _description;
  protected String _hunger;

  protected static MetaDie _md = new MetaDie();

  /** Racial limits for a each subclass for constraining the traits */
  protected int[] _minLimit;
  protected int[] _maxLimit;

  protected String _descriptor;
  protected int[] _racialThiefMods;
  protected String[] _raceSkills;
  protected int _racialPoisonResist = 0;

  // ====================================================
  // ABSTRACT METHODS FOR SUBCLASSES
  // ====================================================

  /** Modify prime traits for racial specialties */
  public abstract TraitList adjustTraitsForRace(TraitList traits);

  /** Calculate the gender- and racial specific weight of the Hero */
  public abstract int calcWeight();

  /** Calculate the gender- and racial specific height of the Hero */
  public abstract int calcHeight();


  // ====================================================
  // CONSTRUCTOR(S) AND RELATED METHODS
  // ====================================================

  /** For subclass constructors */
  protected Race()
  {}

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

  
  /**
   * Genders have slightly different traits Once the specific Race is created, then specific
   * attributes are refined or created: trait adjustment for race and gender, weight and height, and
   * a physical description
   * 
   * @param traits the six prime traits for all Heroes
   */
  public TraitList buildRace(TraitList traits)
  {
    adjustTraitsForRace(traits);
    _gender.adjustTraitsForGender(traits);
    _weight = calcWeight();
    _height = calcHeight();
    _hunger = "Full";
    _description = new Description(traits.getTrait(PrimeTraits.CHR), _descriptor, _hairColor,
        _gender, _height, _weight);
    return traits;
  }

  // ====================================================
  // PROTECTED METHODS
  // ====================================================

  /**
   * Calculate the height of the Hero based on deviation from average
   * 
   * @param lowValue lower boundary for the height distribution
   * @param variance dice format for normal-curve distribution to add to lowValue
   * @return the randomly-determined height within the lower bound and average distribution
   */
  protected int calcHeight(int lowValue, String variance)
  {
    int height = lowValue + _md.roll(variance);
    return height;
  }



  // ====================================================
  // PROTECTED METHODS
  // ====================================================

  /**
   * Calculate the weight of the Hero based on deviation from average
   * 
   * @param lowValue lower boundary for the weight distribution
   * @param variance dice format for normal-curve distribution to add to lowValue
   * @return the randomly-determined weight within the lower bound and average distribution
   */
  protected int calcWeight(int lowValue, String variance)
  {
    int weight = lowValue + (_md.roll(variance) * 10);
    return weight;
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

  // ====================================================
  // PRIVATE METHODS
  // ====================================================



  // ====================================================
  // PRIVATE METHODS
  // ====================================================



  public String getName()
  {
    return _raceName;
  }

  // ====================================================
  // PRIVATE METHODS
  // ====================================================



  public String getRaceDescriptor()
  {
    return _descriptor;
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

  // ====================================================
  // PRIVATE METHODS
  // ====================================================



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


  // ====================================================
  // PRIVATE METHODS
  // ====================================================

//  private void addRacialPoisonResist(int constitution, int magicAttackMod)
//  {
//    if ((_raceName.equalsIgnoreCase("Dwarf")) ||
//        (_raceName.equalsIgnoreCase("Gnome")) ||
//        (_raceName.equalsIgnoreCase("Hobbit"))) {
//      _racialPoisonResist = (int) Math.round(constitution / 3.5);
//      magicAttackMod += _racialPoisonResist;
//    }
//  }


} // end of abstract Race class
