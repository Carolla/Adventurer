/**
 * Race.java Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package chronos.pdc.race;

import static chronos.pdc.character.TraitList.PrimeTraits.CON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import chronos.civ.PersonKeys;
import chronos.pdc.character.Gender;
import chronos.pdc.character.TraitList;
import mylib.pdc.MetaDie;

/**
 * Defines the common methods and attributes for all Races.
 * 
 * @author Alan Cline
 * @version Sept 4 2015 // rewrite to support Hero rewrite <br>
 */
public abstract class Race
{

  public static final String[] RACE_LIST =
      {"Human", "Dwarf", "Elf", "Gnome", "Half-Elf", "Half-Orc", "Hobbit"};

  protected String _raceName;
  protected String _raceLang;

  protected static MetaDie _md = new MetaDie();

  /** Hero male and female weight ranges */
  protected int _weightMedValue;
  /** Hero male and female height ranges */
  protected int _heightMedValue;

  /** Racial limits for a each subclass for the traits */
  protected int[] _minLimit;
  protected int[] _maxLimit;

  protected String _descriptor;
  protected int[] _racialThiefMods;
  protected String[] _raceSkills;
  protected int _racialPoisonResist = 0;

  public abstract int calcWeight(Gender g);

  public abstract int calcHeight(Gender g);

  
  /**
   * Create a specific subclass of Race based on the Race name. <br>
   * NOTE: The subclass must be in the same package as the Race class.
   *
   * @param RaceName the name of the subclass to be created
   */
  static public Race createRace(String raceName, Gender gender)
  {
    Race race = null;
    if (raceName.toLowerCase().equals("dwarf")) {
      race = new Dwarf(gender);
    } else if (raceName.toLowerCase().equals("elf")) {
      race = new Elf(gender);
    } else if (raceName.toLowerCase().equals("gnome")) {
      race = new Gnome(gender);
    } else if (raceName.toLowerCase().equals("half-elf")) {
      race = new HalfElf(gender);
    } else if (raceName.toLowerCase().equals("half-orc")) {
      race = new HalfOrc(gender);
    } else if (raceName.toLowerCase().equals("hobbit")) {
      race = new Hobbit(gender);
    } else {
      race = new Human();
    }
    return race;
  }

  /** Races have different advantages and disadvantages */
  public TraitList adjustTraitsForRace(TraitList traits)
  {
    addRacialPoisonResist(traits.getTrait(CON), (traits).getMagicDefenseMod());
    return traits;
  }


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

  public void loadRaceKeys(Map<PersonKeys, String> map)
  {
    map.put(PersonKeys.RACENAME, _raceName);
    map.put(PersonKeys.RMR, "" + _racialPoisonResist);
  }

  private void addRacialPoisonResist(int constitution, int magicAttackMod)
  {
    if ((_raceName.equalsIgnoreCase("Dwarf")) ||
        (_raceName.equalsIgnoreCase("Gnome")) ||
        (_raceName.equalsIgnoreCase("Hobbit"))) {
      _racialPoisonResist = (int) Math.round(constitution / 3.5);
      magicAttackMod += _racialPoisonResist;
    }
  }


} // end of abstract Race class
